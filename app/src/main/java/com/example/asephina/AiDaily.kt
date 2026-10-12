package com.example.asephina

/*
 * ============================================================================
 *  凌薇 AI —— 日常信息（时间 / 节日节气 / 天气）
 *  ---------------------------------------------------------------------------
 *  为什么要它：模型自己不知道现在几点。没有这个的话，她会在大下午催你睡觉、
 *  在冬天问你热不热 —— 因为她手里的世界是静止的。
 *
 *  这里给她三样真实信息，拼成一段【现在的情况】放进设定里：
 *    · 时间：年月日、星期、时段（上午/中午/下午/傍晚/晚上/深夜）
 *    · 节日与节气：复用软件里那套**完全离线**的推算（Dates.tagsOf）与纪念日
 *    · 天气与温度：来自 open-meteo.com —— 免费的公开接口，**不需要 API Key**，
 *      但它是本应用除"你自己填的模型接口"之外唯一会主动访问的外部地址，
 *      所以默认不开，要你填了城市才生效，随时可以关掉。
 *
 *  定位没有做：那要加一项位置权限，而手填一次城市就一劳永逸，不必为它多要权限。
 *
 *  天气缓存在 ai/daily.json，超过 30 分钟才重新取一次；
 *  取不到就只报时间，绝不因为天气失败影响对话。
 * ========================================================================== */

import android.content.Context
import org.json.JSONObject
import java.io.BufferedReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.Calendar

/** 缓存下来的城市与天气 */
data class DailyInfo(
    val city: String = "",
    val lat: Double = 0.0,
    val lon: Double = 0.0,
    val desc: String = "",
    val temp: Double = 0.0,
    /** 天气是什么时候取的（毫秒） */
    val ts: Long = 0L
)

object AiDaily {

    /** 缓存多久算旧（毫秒） */
    private const val STALE_MS = 30 * 60 * 1000L

    private fun file(ctx: Context): java.io.File = java.io.File(AiStore.dir(ctx), "daily.json")

    fun load(ctx: Context): DailyInfo = try {
        val f = file(ctx)
        if (!f.exists()) DailyInfo()
        else {
            val o = JSONObject(f.readText())
            DailyInfo(
                city = o.optString("city", ""),
                lat = o.optDouble("lat", 0.0),
                lon = o.optDouble("lon", 0.0),
                desc = o.optString("desc", ""),
                temp = o.optDouble("temp", 0.0),
                ts = o.optLong("ts", 0L)
            )
        }
    } catch (e: Exception) {
        DailyInfo()
    }

    fun save(ctx: Context, d: DailyInfo) {
        // 走 AiStore 那把写锁 + 唯一临时名 + 原子改名：
        // 原来这里是截断式直写，写到一半被杀就留下半截 JSON —— 解析失败会静悄悄退回
        // 默认值（城市没了 → 天气再也不查；而且看起来「什么都没发生」，很难查）
        AiStore.writeTextFile(
            file(ctx),
            JSONObject()
                .put("city", d.city).put("lat", d.lat).put("lon", d.lon)
                .put("desc", d.desc).put("temp", d.temp).put("ts", d.ts)
                .toString(2)
        )
    }

    /** 设城市（顺带把旧天气清掉，免得张冠李戴） */
    fun setCity(ctx: Context, city: String, lat: Double, lon: Double) {
        save(ctx, DailyInfo(city = city, lat = lat, lon = lon))
    }

    fun clear(ctx: Context) {
        try {
            file(ctx).delete()
        } catch (e: Exception) {
        }
    }

    /* ------------------------------ 一个简单的 GET ------------------------------ */

    private fun getJson(url: String, timeoutMs: Int = 12000): String? = try {
        val conn = URL(url).openConnection() as HttpURLConnection
        try {
            conn.requestMethod = "GET"
            conn.connectTimeout = timeoutMs
            conn.readTimeout = timeoutMs
            conn.setRequestProperty("Accept", "application/json")
            val code = conn.responseCode
            if (code !in 200..299) null
            else conn.inputStream.bufferedReader(Charsets.UTF_8).use(BufferedReader::readText)
        } finally {
            try {
                conn.disconnect()
            } catch (e: Exception) {
            }
        }
    } catch (e: Exception) {
        null
    }

    /* ------------------------------ 城市 → 经纬度 ------------------------------ */

    /**
     * 地名的检索结果。
     *
     * ⚠️ 为什么不直接取第一条：同名的地方太多了。
     *    实测查一个常见地名时，返回的第一条常常是**另一个省的同名乡镇**
     *    （比如「××」既有地级市、也有外省的一个镇），坐标能差出几百公里 ——
     *    直接用第一条会报到别处的天气。所以这里返回一批候选，让用户自己点一个。
     */
    data class GeoHit(val label: String, val name: String, val lat: Double, val lon: Double)

    /**
     * 行政区划后缀。实测 open-meteo 的地名库里，很多「××区」「××市」「××县」的写法
     * 直接查是空的，把后缀去掉反而一查就中。
     * 所以查不到时自动去掉后缀再试一次 —— 用户按平时的习惯填也能用。
     */
    private val ADMIN_SUFFIXES = listOf(
        "特别行政区", "自治区", "自治县", "自治州", "地区", "街道",
        "区", "市", "县", "镇", "乡", "盟"
    )

    private fun stripAdmin(s: String): String {
        ADMIN_SUFFIXES.forEach { suf ->
            if (s.length > suf.length && s.endsWith(suf)) return s.dropLast(suf.length)
        }
        return s
    }

    /** 查一次（原样查） */
    private fun geocodeQuery(q: String): List<GeoHit> {
        if (q.isBlank()) return emptyList()
        val enc = try {
            URLEncoder.encode(q, "UTF-8")
        } catch (e: Exception) {
            q
        }
        val body = getJson(
            "https://geocoding-api.open-meteo.com/v1/search?name=$enc&count=8&language=zh&format=json"
        ) ?: return emptyList()
        return try {
            val arr = JSONObject(body).optJSONArray("results") ?: return emptyList()
            (0 until arr.length()).mapNotNull { i ->
                val o = arr.optJSONObject(i) ?: return@mapNotNull null
                val name = o.optString("name", "").ifBlank { return@mapNotNull null }
                val lat = o.optDouble("latitude", 0.0)
                val lon = o.optDouble("longitude", 0.0)
                if (lat == 0.0 && lon == 0.0) return@mapNotNull null
                // 省 + 市都带上：光有省分不清同名的县区
                val admin1 = o.optString("admin1", "")
                val admin2 = o.optString("admin2", "")
                val country = o.optString("country", "")
                val extra = listOf(admin1, admin2, country)
                    .filter { it.isNotBlank() }
                    .distinct()
                    .joinToString(" ")
                GeoHit(
                    label = if (extra.isBlank()) name else "$name · $extra",
                    name = name, lat = lat, lon = lon
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    /** 把地名换成若干候选：先按原样查，查不到再去掉「区/市/县」这类后缀重查 */
    fun geocodeHits(city: String): List<GeoHit> {
        val raw = city.trim()
        if (raw.isBlank()) return emptyList()
        val out = ArrayList<GeoHit>()
        fun merge(hits: List<GeoHit>) {
            hits.forEach { h ->
                if (out.none { it.lat == h.lat && it.lon == h.lon }) out.add(h)
            }
        }
        merge(geocodeQuery(raw))
        if (out.isEmpty()) {
            val stripped = stripAdmin(raw)
            if (stripped.isNotBlank() && stripped != raw) merge(geocodeQuery(stripped))
        }
        return out.take(8)
    }

    /* ------------------------------ 天气 ------------------------------ */

    /** WMO 天气代码 → 中文 */
    private fun code2text(code: Int): String = when (code) {
        0 -> "晴"
        1 -> "大致晴朗"
        2 -> "局部多云"
        3 -> "阴"
        45, 48 -> "有雾"
        51, 53, 55 -> "毛毛雨"
        56, 57 -> "冻毛毛雨"
        61 -> "小雨"
        63 -> "中雨"
        65 -> "大雨"
        66, 67 -> "冻雨"
        71 -> "小雪"
        73 -> "中雪"
        75 -> "大雪"
        77 -> "雪粒"
        80 -> "阵雨"
        81 -> "强阵雨"
        82 -> "暴雨"
        85, 86 -> "阵雪"
        95 -> "雷阵雨"
        96, 99 -> "雷阵雨伴冰雹"
        else -> "天气未知"
    }

    /** 取一次天气，成功就写进缓存；失败返回 null（调用处忽略即可） */
    fun refreshWeather(ctx: Context): DailyInfo? {
        val cur = load(ctx)
        if (cur.city.isBlank() || (cur.lat == 0.0 && cur.lon == 0.0)) return null
        val body = getJson(
            "https://api.open-meteo.com/v1/forecast?latitude=${cur.lat}&longitude=${cur.lon}" +
                    "&current=temperature_2m,weather_code&timezone=auto"
        ) ?: return null
        return try {
            val o = JSONObject(body).optJSONObject("current") ?: return null
            val t = o.optDouble("temperature_2m", Double.NaN)
            val c = o.optInt("weather_code", -1)
            if (t.isNaN()) return null
            val next = cur.copy(
                desc = code2text(c),
                temp = t,
                ts = System.currentTimeMillis()
            )
            save(ctx, next)
            next
        } catch (e: Exception) {
            null
        }
    }

    /** 天气缓存超过 30 分钟（或压根没有）就顺手刷一次；**必须在 IO 线程调用** */
    fun refreshIfStale(ctx: Context) {
        val cur = load(ctx)
        if (cur.city.isBlank()) return
        val fresh = cur.ts > 0 && System.currentTimeMillis() - cur.ts < STALE_MS
        if (!fresh) refreshWeather(ctx)
    }

    /* ------------------------------ 拼进设定 ------------------------------ */

    /** 一行天气，例如「海淀区：22°C，阴」；没有就返回空串 */
    fun weatherLine(ctx: Context): String {
        val d = load(ctx)
        if (d.city.isBlank() || d.ts <= 0L) return ""
        val t = if (d.temp == Math.floor(d.temp)) d.temp.toInt().toString()
        else String.format(java.util.Locale.CHINA, "%.1f", d.temp)
        return d.city + "：" + t + "°C，" + d.desc
    }

    /** 时段说法 */
    private fun partOfDay(hour: Int): String = when (hour) {
        in 5..7 -> "清晨"
        in 8..10 -> "上午"
        in 11..12 -> "中午"
        in 13..16 -> "下午"
        in 17..18 -> "傍晚"
        in 19..22 -> "晚上"
        else -> "深夜"
    }

    /** 分钟数 → 「23:30」 */
    fun hhmmOfMin(min: Int): String {
        val m = ((min % 1440) + 1440) % 1440
        return String.format(java.util.Locale.CHINA, "%02d:%02d", m / 60, m % 60)
    }

    /**
     * 拼出【现在的情况】那一段。**纯函数**：时间按传进来的"此刻"算，
     * 天气用外面读好的那一行 —— 这样它可以随时调用，不碰文件。
     *
     * @param showDaily 用户在设置里有没有打开这个功能
     * @param weatherLine [weatherLine] 的结果（没配城市就给空串）
     * @param now 当前时刻
     * @param sleepFrom 睡觉时段的起点（当天零点起的分钟数；等于终点表示没有设限）
     * @param sleepTo 睡觉时段的终点
     */
    fun block(
        showDaily: Boolean,
        weatherLine: String,
        now: Long,
        sleepFrom: Int = 0,
        sleepTo: Int = 0
    ): String {
        if (!showDaily) return ""
        val cal = Calendar.getInstance().apply { timeInMillis = now }
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)
        val dayKey = Dates.todayKey()
        val nowMin = hour * 60 + minute
        val f = ((sleepFrom % 1440) + 1440) % 1440
        val t = ((sleepTo % 1440) + 1440) % 1440
        val hasSleep = f != t
        val asleep = hasSleep && AiGreet.inSleep(nowMin, sleepFrom, sleepTo)

        // 挤成两三行：这一段每次请求都要带上，啰嗦一点都不划算
        val sb = StringBuilder()
        sb.append("【现在（真实信息）】")
            .append(dayKey).append(' ').append(Dates.weekName(cal)).append(' ')
            .append(String.format(java.util.Locale.CHINA, "%02d:%02d", hour, minute))
            .append('（').append(if (asleep) "对方的睡觉时段" else partOfDay(hour)).append('）')

        val tags = try {
            Dates.tagsOf(dayKey)
        } catch (e: Exception) {
            emptyList()
        }
        val anniv = try {
            Persona.annivName(dayKey)
        } catch (e: Exception) {
            null
        }
        val todayWords = ArrayList<String>()
        anniv?.let { todayWords.add(it) }
        tags.forEach { todayWords.add(it.name) }
        if (todayWords.isNotEmpty()) {
            sb.append(" 今日「").append(todayWords.joinToString("」「")).append('」')
        }
        if (weatherLine.isNotBlank()) sb.append(" 天气：").append(weatherLine)
        sb.append('\n')

        if (hasSleep) {
            sb.append("【对方的作息】平时 ").append(hhmmOfMin(sleepFrom)).append(" 到 ")
                .append(hhmmOfMin(sleepTo)).append(" 是睡觉时段。").append('\n')
        }

        // ⚠️ 这一段每次请求都要带上，是纯成本。只留「会改变行为」的那几句：
        //    什么时候该提、什么时候别提、夜里该怎么接、夜班怎么处理。
        //    语气示例只留一个最短的 —— 多写例子的边际收益很低，token 却是实打实的。
        sb.append("用法：轻松时主动带一点进来（快下雨→记得带伞），别等着被问；不要生硬报数据，也不要编造。")
        if (asleep) {
            sb.append("现在是对方的睡觉时段：他还醒着就先关心一句，别追问、别催睡；他说不想睡就顺着聊。")
        } else {
            sb.append("白天别提睡觉；临近睡觉时段可以自然提一句早点休息。")
        }
        sb.append("【他若说过自己在值夜班、作息相反，以他说的为准，别按上面的时段判断他该不该睡。】")
        return sb.toString()
    }
}
