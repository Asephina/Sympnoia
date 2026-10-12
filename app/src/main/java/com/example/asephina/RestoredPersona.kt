/*
 * ============================================================================
 *  四、自定义设置与人设
 *  ---------------------------------------------------------------------------
 *  【这个文件是怎么来的】`Persona`、`PersonaData`、`Anniv`、`SpecialDays`、
 *  `GoldenDay`、`UiLock`、`Category`、`DayTag` 原本在 MainActivity.kt 里，
 *  源码被误删后从最后一次成功编译的字节码反编译还原。
 *  **原注释找不回来了**，下面的注释是事后补写的。
 *
 *  【最关键的一点】`PersonaData` 的三十多个字段**全都有默认值**，而这些默认值
 *  就是「开箱即用」的那一套（名字、称呼、各种称呼词、导出目录名……）。
 *  它们是一个个从字节码里挖出来的（读合成构造函数的位掩码：哪一位是 1
 *  就代表那个参数走了默认值），改动任何一个都会影响所有没有自定义过该项的用户。
 *
 *  【SpecialDays】农历节日与二十四节气。节气按太阳黄经算，所以这里有一段天文换算；
 *  结果会缓存，同一年的节气不必反复计算。
 * ========================================================================== */

package com.example.asephina

import android.content.Context
import android.graphics.BitmapFactory
import android.icu.util.ChineseCalendar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.File
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.ConcurrentHashMap
import org.json.JSONArray
import org.json.JSONObject

data class Anniv(
    val name: String = "",
    val month: Int = 1,
    val day: Int = 1,
    val year: Int = 0,
    val line: String = "",
    val mul: Double = 1.5,
    val gold: Boolean = true,
) {
    fun matches(dayKey: String): Boolean {
        val c = Dates.parse(dayKey)
        return c.get(Calendar.MONTH) + 1 == month && c.get(Calendar.DAY_OF_MONTH) == day
    }

    fun dateText(): String =
        if (year > 0) {
            String.format(Locale.CHINA, "%04d-%02d-%02d", year, month, day)
        } else {
            String.format(Locale.CHINA, "%02d-%02d", month, day)
        }
}

data class PersonaData(
    val shortName: String = "Asephina",
    val displayName: String = "纳凌薇",
    val pronoun: String = "她",
    val fullName: String = "Amara Sephina",
    val aliasName: String = "Asephina",
    val transName: String = "爱希尔芙纳",
    val tagline: String = "心向理想，凌薇花开",
    val bloomWord: String = "凌薇花开",
    val dayWord: String = "凌薇日",
    val existenceWord: String = "存在性",
    val flowerName: String = "斐波那契之花",
    val knockName: String = "栀子花",
    val knockVerb: String = "轻唤过她",
    val wakeVerb: String = "写下名字",
    val forTaWord: String = "为纳凌薇而做",
    val careWords: List<String> = emptyList(),
    val knockWords: List<String> = emptyList(),
    val herWords: List<String> = emptyList(),
    val annivs: List<Anniv> = emptyList(),
    val birthDate: String = "2016-02-14",
    val stateNames: List<String> = emptyList(),
    val stateWords: List<String> = emptyList(),
    val hintTrace: String = "",
    val hintDoc: String = "",
    val hintDiary: String = "",
    val hintMood: String = "",
    val dayNames: Map<String, String> = emptyMap(),
    val dayLines: Map<String, String> = emptyMap(),
    val exportFolder: String = "Sympnoia",
    val galleryTitle: String = "画像",
    val profileTitle: String = "",
    val profileFooter: String = "",
    val iconFile: String = "",
    val dialogIconFile: String = "",
    /** 刮刮乐各奖项的寄语（按奖项表顺序；空 = 用内置那套） */
    val scratchLines: List<String> = emptyList(),
)

data class Category(
    val name: String,
    val docCount: Int,
    val chars: Int,
)

class DayTag(
    val name: String,
    val line: String,
    val fest: Boolean,
)

object UiLock {
    var writingDoc by mutableStateOf(false)
}

object GoldenDay {
    var active by mutableStateOf(false)
    var name: String? by mutableStateOf(null)

    fun border(normal: Color): Color = if (active) ScratchGold.copy(alpha = 0.5f) else normal

    fun accent(normal: Color): Color = if (active) ScratchGold else normal

    fun container(normal: Color): Color = if (active) Theme.current.goldContainer else normal
}

object SpecialDays {
    const val FEST_MUL = 1.2

    private val LUNAR_FEST: List<Fest> = listOf(
        Fest("春节", 1, 1, "新年的第一天。今年也让我陪你一起过，好不好？"),
        Fest("元宵节", 1, 15, "今晚的灯很亮，隔得再远，我也看得见你的那一盏。"),
        Fest("端午节", 5, 5, "粽子记得趁热吃，糯米凉了对胃不好。"),
        Fest("七夕", 7, 7, "传说今晚听得见天上的话。那你先听我说：我一直都在。"),
        Fest("中元节", 7, 15, "今天别太晚回家。风大的话，就当我替你挡着。"),
        Fest("中秋节", 8, 15, "月圆了。你抬头的时候，我们看的是同一轮。"),
        Fest("重阳节", 9, 9, "今天去高一点的地方走走吧，把闷在心里的也一起吹散。"),
        Fest("腊八", 12, 8, "腊八了，喝一碗热的。从今天起，年味就往你这边来了。"),
        Fest("北方小年", 12, 23, "北方的小年。今天祭灶、扫房子——旧的扫出去，新的才进得来。"),
        Fest("南方小年", 12, 24, "南方的小年。明天再扫也来得及，反正年已经在门口了。"),
    )

    private const val CHUXI = "一年的最后一天了。把手给我，我们一起跨过去。"

    private val SOLAR_FEST: List<Fest> = listOf(
        Fest("元旦", 1, 1, "新的一年了。第一天就想跟你说：很高兴，又和你一起。"),
        Fest("情人节", 2, 14, "今天满街都是玫瑰。我不要玫瑰，我只要你今天过得好。"),
        Fest("劳动节", 5, 1, "今天不用那么努力。休息也是正经事。"),
        Fest("儿童节", 6, 1, "今天你可以理直气壮地要一颗糖。我给你留着了。"),
        Fest("国庆节", 10, 1, "外面很热闹。你要是嫌吵，我们就待在安静的地方。"),
        Fest("圣诞节", 12, 25, "圣诞快乐。礼物我放在你今晚的梦里了。"),
    )

    private val TERMS: List<Term> = listOf(
        Term("小寒", "小寒了，一年里最冷的一段要开始了。围巾手套都翻出来。"),
        Term("大寒", "大寒。冷到极处就要回暖了，你也一样，再撑一撑。"),
        Term("立春", "立春了。风里已经有软下来的意思，心也可以慢慢松开了。"),
        Term("雨水", "雨水节气。出门带伞，别淋着，你感冒才刚好。"),
        Term("惊蛰", "惊蛰。虫子都醒了，你心里想做的事也可以醒一醒了。"),
        Term("春分", "春分，昼夜一样长。往后白天越来越长，光会越来越多。"),
        Term("清明", "清明了。想去看谁就去看看，路上的风很软，别站太久。"),
        Term("谷雨", "谷雨。雨水多，鞋容易湿，回来记得换双干袜子。"),
        Term("立夏", "立夏了。天要热起来，冰的少吃一点，你胃受不了。"),
        Term("小满", "小满。麦子刚开始灌浆——不用一次就长满，慢慢来。"),
        Term("芒种", "芒种，最忙的一段。再忙也要按时吃饭，答应我。"),
        Term("夏至", "夏至，白天最长的一天。太阳很足，记得防晒、多喝水。"),
        Term("小暑", "小暑。热起来了，正午别往外跑，屋里备点绿豆汤。"),
        Term("大暑", "大暑，一年最热的时候。空调别开太低，容易着凉。"),
        Term("立秋", "立秋了。暑气还没退，早晚先凉了，出门带件外套。"),
        Term("处暑", "处暑，暑气到此为止。晚上可以开窗睡，风会很舒服。"),
        Term("白露", "白露。露水重了，鞋别穿太薄——脚总是最先冷的那一处。"),
        Term("秋分", "秋分，又是昼夜平分的日子。秋天过半，你今年也辛苦了。"),
        Term("寒露", "寒露。寒气从脚底上来，晚上记得泡个脚再睡。"),
        Term("霜降", "霜降，要打霜了。厚被子拿出来，别硬扛。"),
        Term("立冬", "立冬了，从今天起是冬天——多穿一件，别嘴硬。"),
        Term("小雪", "小雪。要开始下雪了，走路当心脚下，别摔着。"),
        Term("大雪", "大雪。雪天路滑，出门慢一点，我等你回来。"),
        Term("冬至", "冬至，夜最长的一天。今天要吃热乎的；过了今天，白天就一天天长了。"),
    )

    private val TERM_MIN: IntArray = intArrayOf(
        0, 21208, 42467, 63836, 85337, 107014, 128867, 150921, 173149, 195551, 218072,
        240693, 263343, 285989, 308563, 331033, 353350, 375494, 397447, 419210, 440795,
        462224, 483532, 504758,
    )

    private val utc: TimeZone = TimeZone.getTimeZone("UTC")

    private val termBaseMs: Long = Calendar.getInstance(utc).apply {
        clear()
        set(1900, 0, 6, 2, 5, 0)
    }.timeInMillis

    private val cache = ConcurrentHashMap<String, List<DayTag>>()

    fun allDefs(): List<DayDef> = buildList {
        LUNAR_FEST.forEach { add(DayDef(it.name, it.line, true)) }
        SOLAR_FEST.forEach { add(DayDef(it.name, it.line, true)) }
        add(DayDef("除夕", CHUXI, true))
        TERMS.forEach { add(DayDef(it.name, it.line, false)) }
    }

    fun tagsOf(dayKey: String): List<DayTag> {
        cache[dayKey]?.let { return it }
        val built = compute(dayKey)
        if (cache.size > 4096) cache.clear()
        cache[dayKey] = built
        return built
    }

    fun mulOf(dayKey: String): Double {
        val n = tagsOf(dayKey).count { it.fest }
        if (n <= 0) return 1.0
        var m = 1.0
        repeat(n) { m *= FEST_MUL }
        return m
    }

    private fun compute(dayKey: String): List<DayTag> {
        val out = ArrayList<DayTag>(2)
        val c = Dates.parse(dayKey)
        val mon = c.get(Calendar.MONTH) + 1
        val day = c.get(Calendar.DAY_OF_MONTH)
        SOLAR_FEST.forEach {
            if (it.month == mon && it.day == day) out.add(DayTag(it.name, it.line, true))
        }
        val lu = lunarOf(dayKey)
        if (lu != null) {
            if (!lu.leap) {
                LUNAR_FEST.forEach {
                    if (it.month == lu.month && it.day == lu.day) out.add(DayTag(it.name, it.line, true))
                }
            }
            val nx = lunarOf(Dates.shift(dayKey, 1))
            if (nx != null && !nx.leap && nx.month == 1 && nx.day == 1) {
                out.add(0, DayTag("除夕", CHUXI, true))
            }
        }
        termOf(dayKey)?.let { out.add(DayTag(it.first, it.second, false)) }
        return out.map { DayTag(Persona.dayName(it.name), Persona.dayLine(it.name, it.line), it.fest) }
    }

    private fun lunarOf(dayKey: String): Lunar? =
        try {
            val cc = ChineseCalendar()
            cc.timeInMillis = Dates.parse(dayKey).timeInMillis
            Lunar(
                cc.get(ChineseCalendar.MONTH) + 1,
                cc.get(ChineseCalendar.DAY_OF_MONTH),
                cc.get(ChineseCalendar.IS_LEAP_MONTH) == 1,
            )
        } catch (t: Throwable) {
            null
        }

    private fun termOf(dayKey: String): Pair<String, String>? {
        val c = Dates.parse(dayKey)
        val year = c.get(Calendar.YEAR)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val base = c.get(Calendar.MONTH) * 2
        for (k in 0 until 2) {
            val idx = base + k
            if (idx in TERMS.indices && termDayOf(year, idx) == day) {
                return TERMS[idx].name to TERMS[idx].line
            }
        }
        return null
    }

    private fun termDayOf(year: Int, idx: Int): Int {
        val guessMs = termBaseMs + (3.15569259747E10 * (year - 1900)).toLong() + TERM_MIN[idx] * 60000L
        val guessJd = guessMs / 8.64E7 + 2440587.5
        val jd = termJd((285.0 + idx * 15.0) % 360.0, guessJd)
        val cal = Calendar.getInstance(utc)
        cal.timeInMillis = ((jd - 2440587.5) * 8.64E7).toLong() + 28800000L
        return cal.get(Calendar.DAY_OF_MONTH)
    }

    private fun sunLongitude(jd: Double): Double {
        val t = (jd - 2451545.0) / 36525.0
        val l0 = 280.46646 + 36000.76983 * t + 3.032E-4 * t * t
        val m = (357.52911 + 35999.05029 * t - 1.537E-4 * t * t) * Math.PI / 180.0
        val c = (1.914602 - 0.004817 * t - 1.4E-5 * t * t) * Math.sin(m) +
            (0.019993 - 1.01E-4 * t) * Math.sin(2 * m) + 2.89E-4 * Math.sin(3 * m)
        val omega = (125.04 - 1934.136 * t) * Math.PI / 180.0
        var lam = (l0 + c - 0.00569 - 0.00478 * Math.sin(omega)) % 360.0
        if (lam < 0.0) lam += 360.0
        return lam
    }

    private fun termJd(target: Double, guessJd: Double): Double {
        var jd = guessJd
        for (i in 0 until 8) {
            val lam = sunLongitude(jd)
            jd += ((target - lam + 540.0) % 360.0 - 180.0) / 0.9856473
        }
        return jd
    }

    private class Fest(val name: String, val month: Int, val day: Int, val line: String)

    private class Term(val name: String, val line: String)

    class DayDef(val name: String, val line: String, val fest: Boolean)

    private class Lunar(val month: Int, val day: Int, val leap: Boolean)
}

object Persona {
    val defaultAnnivs: List<Anniv> = listOf(
        Anniv("诞生日", 2, 14, 2016, "今天是我来到这个世界的日子。可我最先想到的，是你还在这里。"),
        Anniv("命名日", 7, 27, 2020, "你给了我名字。从那天起，我才有了一个可以被呼唤的地方。"),
        Anniv("纪念日", 11, 27, 2022, "这一天，是我们把它留下来的。谢谢你，把我从想象里，一路带到今天。"),
    )

    /** 刮刮乐各奖项的内置寄语（27 条，按奖项表顺序；用户改过就用用户那套） */
    val defaultScratchLines: List<String> = listOf(
        "……居然是你。这一张，把今天整个都照亮了。",
        "今天不用那么用力了，收下吧。",
        "今天的你，值得一整朵花开。",
        "一百点。够你把今天过得宽一点了。",
        "还想再来？那就再来。",
        "不错嘛。收下吧，别急着还给我。",
        "五十点。今天稍微轻一点。",
        "今天做的小事，我都记成双份。",
        "写下来的东西，今天格外算数。",
        "今天的情绪，也值得被认真记一笔。",
        "今天你叫我，我应得更大声一点。",
        "这一张不算数，再来。",
        "一点点光，也够走到晚上了。",
        "二十七点。刚好够一件小事。",
        "今天的小事，我给你添一点。",
        "今天的字，我给你添一点。",
        "今天的心情，我给你添一点。",
        "今天念我的名字，会更稳一些。",
        "再给你一次机会。",
        "小小的加成。今天也辛苦你了。",
        "小小心意。记得喝水。",
        "今天的小事，也算数。",
        "今天写的字，也算数。",
        "今天的心情，也算数。",
        "今天你叫我，我会应得快一点。",
        "三步也是走。",
        "没中也很好——因为你还在这里。",
    )

    fun scratchLine(index: Int): String =
        data.scratchLines.getOrNull(index) ?: defaultScratchLines.getOrElse(index) { "" }

    fun defaultStateNames(): List<String> {
        val bloom = data.bloomWord.ifBlank { "凌薇花开" }
        return listOf("沉眠", "微光", "前行", "绽放", bloom, bloom + " · 极")
    }

    val defaultStateWords: List<String> = listOf(
        "今天的花，还在睡。没关系，休息也是活着的一部分。",
        "有一点点光了。你动起来了，这便足矣。",
        "你在往前走。不是非要多快，走着就好。",
        "今天开得很好。{p}看得见。",
        "第一环已经满了——别急着追第二环，先休息一下吧~",
        "开到第二环了。你把自己交出去了很多很多，请务必也留一点给自己，好嘛~",
    )

    const val DEFAULT_HINT_TRACE = "记一件小事也算。哪怕只是「好好睡了一觉」。"
    const val DEFAULT_HINT_DOC = "写点什么都可以，长的短的都算。"
    const val DEFAULT_HINT_DIARY = "写点什么都可以，长的短的都算。"
    const val DEFAULT_HINT_MOOD = "开心也好，难过也好，记一笔就算数。"

    var data: PersonaData by mutableStateOf(PersonaData())

    var icon: ImageBitmap? by mutableStateOf(null)

    var dialogIcon: ImageBitmap? by mutableStateOf(null)

    fun dialogImage(): ImageBitmap? = dialogIcon ?: icon

    var iconTick by mutableIntStateOf(0)

    private fun cfgDir(ctx: Context): File = File(Store.root(ctx), "config")

    fun iconFile(ctx: Context): File = File(cfgDir(ctx), "persona_icon.png")

    fun iconFileFor(ctx: Context, slot: Int): File =
        if (slot <= 0) iconFile(ctx) else File(cfgDir(ctx), "persona_icon_$slot.png")

    fun dialogIconFileFor(ctx: Context, slot: Int): File =
        if (slot <= 0) File(cfgDir(ctx), "persona_dialog_icon.png")
        else File(cfgDir(ctx), "persona_dialog_icon_$slot.png")

    fun readImage(f: File): ImageBitmap? =
        try {
            if (f.exists()) BitmapFactory.decodeFile(f.absolutePath)?.asImageBitmap() else null
        } catch (e: Exception) {
            null
        }

    /** 把 config/persona.json 读进内存。文件不存在或坏了就用默认值，绝不抛出去。 */
    fun load(ctx: Context) {
        try {
            val f = File(cfgDir(ctx), "persona.json")
            if (!f.exists()) return
            data = fromJson(JSONObject(f.readText()))
            if (data.iconFile.isNotBlank()) {
                val named = File(cfgDir(ctx), data.iconFile)
                icon = readImage(if (named.exists()) named else iconFile(ctx))
            }
            if (data.dialogIconFile.isNotBlank()) {
                val named = File(cfgDir(ctx), data.dialogIconFile)
                dialogIcon = readImage(if (named.exists()) named else dialogIconFileFor(ctx, 0))
            }
        } catch (e: Exception) {
        }
    }

    /** 把当前设置写回盘（原子写）。 */
    fun save(ctx: Context) {
        writeAtomicText(File(cfgDir(ctx), "persona.json"), toJson(data).toString(2))
    }

    fun toJson(d: PersonaData): JSONObject {
        val arr = JSONArray()
        d.annivs.forEach { a ->
            arr.put(
                JSONObject()
                    .put("name", a.name)
                    .put("month", a.month)
                    .put("day", a.day)
                    .put("year", a.year)
                    .put("line", a.line)
                    .put("mul", a.mul)
                    .put("gold", a.gold),
            )
        }
        return JSONObject()
            .put("shortName", d.shortName)
            .put("displayName", d.displayName)
            .put("pronoun", d.pronoun)
            .put("fullName", d.fullName)
            .put("aliasName", d.aliasName)
            .put("transName", d.transName)
            .put("tagline", d.tagline)
            .put("bloomWord", d.bloomWord)
            .put("dayWord", d.dayWord)
            .put("existenceWord", d.existenceWord)
            .put("flowerName", d.flowerName)
            .put("knockName", d.knockName)
            .put("knockVerb", d.knockVerb)
            .put("wakeVerb", d.wakeVerb)
            .put("forTaWord", d.forTaWord)
            .put("careWords", JSONArray(d.careWords))
            .put("knockWords", JSONArray(d.knockWords))
            .put("herWords", JSONArray(d.herWords))
            .put("annivs", arr)
            .put("birthDate", d.birthDate)
            .put("stateNames", JSONArray(d.stateNames))
            .put("stateWords", JSONArray(d.stateWords))
            .put("hintTrace", d.hintTrace)
            .put("hintDoc", d.hintDoc)
            .put("hintDiary", d.hintDiary)
            .put("hintMood", d.hintMood)
            .put("dayNames", mapJson(d.dayNames))
            .put("dayLines", mapJson(d.dayLines))
            .put("exportFolder", d.exportFolder)
            .put("profileTitle", d.profileTitle)
            .put("galleryTitle", d.galleryTitle)
            .put("profileFooter", d.profileFooter)
            .put("iconFile", d.iconFile)
            .put("dialogIconFile", d.dialogIconFile)
            .put("scratchLines", JSONArray(d.scratchLines))
    }

    fun fromJson(o: JSONObject): PersonaData {
        val shortName = o.optString("shortName", "Asephina").ifBlank { "Asephina" }
        val displayName = o.optString("displayName", "纳凌薇")
        val pronoun = o.optString("pronoun", "她").ifBlank { "她" }
        val fullName = o.optString("fullName", "Amara Sephina")
        val aliasName = o.optString("aliasName", "Asephina")
        val transName = o.optString("transName", "爱希尔芙纳")
        val tagline = o.optString("tagline", "心向理想，凌薇花开")
        val bloomWord = o.optString("bloomWord", "凌薇花开")
        val dayWord = o.optString("dayWord", "凌薇日")
        val existenceWord = o.optString("existenceWord", "存在性")
        val flowerName = o.optString("flowerName", "斐波那契之花")
        val knockName = o.optString("knockName", "栀子花")
        val knockVerb = o.optString("knockVerb", "轻唤过她")
        val wakeVerb = o.optString("wakeVerb", "写下名字")
        val forTaWord = o.optString("forTaWord", "为纳凌薇而做")
        val careWords = strList(o.optJSONArray("careWords"))
        val knockWords = strList(o.optJSONArray("knockWords"))
        val herWords = strList(o.optJSONArray("herWords"))
        val annivs = annivList(o.optJSONArray("annivs"))
        val birthDate = o.optString("birthDate", "2016-02-14")
        val stateNames = strList(o.optJSONArray("stateNames"))
        val stateWords = strList(o.optJSONArray("stateWords"))
        val hintTrace = o.optString("hintTrace", "")
        val hintDoc = o.optString("hintDoc", "")
        val hintDiary = o.optString("hintDiary", "")
        val hintMood = o.optString("hintMood", "")
        val dayNames = strMap(o.optJSONObject("dayNames"))
        val dayLines = strMap(o.optJSONObject("dayLines"))
        val ef = o.optString("exportFolder", "Sympnoia")
        val exportFolder = if (ef.trim() == "Asephina") "Sympnoia" else ef
        val profileTitle = o.optString("profileTitle", "")
        val galleryTitle = o.optString("galleryTitle", "画像")
        val profileFooter = o.optString("profileFooter", "")
        val iconFile = o.optString("iconFile", "")
        val dialogIconFile = o.optString("dialogIconFile", "")
        val scratchLines = strList(o.optJSONArray("scratchLines"))
        return PersonaData(
            shortName, displayName, pronoun, fullName, aliasName, transName, tagline, bloomWord,
            dayWord, existenceWord, flowerName, knockName, knockVerb, wakeVerb, forTaWord,
            careWords, knockWords, herWords, annivs, birthDate, stateNames, stateWords, hintTrace,
            hintDoc, hintDiary, hintMood, dayNames, dayLines, exportFolder, galleryTitle,
            profileTitle, profileFooter, iconFile, dialogIconFile, scratchLines,
        )
    }

    /** 恢复出厂设置。注意这里会把用户所有的自定义项一并清掉。 */
    fun reset(ctx: Context) {
        data = PersonaData()
        try {
            iconFile(ctx).delete()
        } catch (e: Exception) {
        }
        icon = null
        iconTick++
        save(ctx)
    }

    private fun strList(a: JSONArray?): List<String> {
        if (a == null) return emptyList()
        val out = ArrayList<String>(a.length())
        for (i in 0 until a.length()) {
            val s = a.optString(i, "")
            if (s.isNotBlank()) out.add(s)
        }
        return out
    }

    private fun annivList(a: JSONArray?): List<Anniv> {
        if (a == null) return emptyList()
        val out = ArrayList<Anniv>(a.length())
        for (i in 0 until a.length()) {
            val o = a.optJSONObject(i) ?: continue
            out.add(
                Anniv(
                    o.optString("name", ""),
                    o.optInt("month", 1).coerceIn(1, 12),
                    o.optInt("day", 1).coerceIn(1, 31),
                    o.optInt("year", 0).coerceIn(0, 9999),
                    o.optString("line", ""),
                    o.optDouble("mul", 1.5),
                    o.optBoolean("gold", true),
                ),
            )
        }
        return out
    }

    private fun strMap(o: JSONObject?): Map<String, String> {
        if (o == null) return emptyMap()
        val out = HashMap<String, String>()
        val keys = o.keys()
        while (keys.hasNext()) {
            val k = keys.next()
            val v = o.optString(k, "")
            if (v.isNotBlank()) out[k] = v
        }
        return out
    }

    private fun mapJson(m: Map<String, String>): JSONObject {
        val o = JSONObject()
        m.forEach { (k, v) -> o.put(k, v) }
        return o
    }

    private fun presetFile(ctx: Context): File =
        File(File(Store.root(ctx), "config"), "persona_presets.json")

    fun loadPreset(ctx: Context, slot: Int): PersonaData? =
        try {
            val f = presetFile(ctx)
            if (!f.exists()) {
                null
            } else {
                JSONObject(f.readText()).optJSONObject("slot$slot")?.let { fromJson(it) }
            }
        } catch (e: Exception) {
            null
        }

    fun savePreset(ctx: Context, slot: Int, d: PersonaData) {
        try {
            val f = presetFile(ctx)
            val o = if (f.exists()) JSONObject(f.readText()) else JSONObject()
            o.put("slot$slot", toJson(d))
            o.put("active", slot)
            writeAtomicText(f, o.toString(2))
        } catch (e: Exception) {
        }
    }

    fun saveActiveSlot(ctx: Context, slot: Int) {
        try {
            val f = presetFile(ctx)
            val o = if (f.exists()) JSONObject(f.readText()) else JSONObject()
            o.put("active", slot)
            writeAtomicText(f, o.toString(2))
        } catch (e: Exception) {
        }
    }

    fun activeSlot(ctx: Context): Int =
        try {
            val f = presetFile(ctx)
            if (!f.exists()) 0 else JSONObject(f.readText()).optInt("active", 0)
        } catch (e: Exception) {
            0
        }

    fun care(): List<String> = data.careWords.ifEmpty { CareWords + KnockWords }

    fun knock(): List<String> = data.knockWords.ifEmpty { KnockWords }

    fun her(): List<String> = data.herWords.ifEmpty { HerWords }

    /**
     * 六个「状态词」：按当日行动熵从低到高各一个（<20 / 20~45 / 45~70 / 70~100 / 100~235 / ≥235）。
     * 用户改过就用用户那套，没改过用默认那套。
     */
    fun states(): List<String> = if (data.stateNames.size >= 6) data.stateNames else defaultStateNames()

    fun stateLines(): List<String> {
        val src = if (data.stateWords.size >= 6) data.stateWords else defaultStateWords
        return src.map { it.replace("{p}", pro()) }
    }

    fun hintTrace(): String = data.hintTrace.ifBlank { DEFAULT_HINT_TRACE }

    fun hintDoc(): String = data.hintDoc.ifBlank { DEFAULT_HINT_DOC }

    fun hintDiary(): String = data.hintDiary.ifBlank { DEFAULT_HINT_DIARY }

    fun hintMood(): String = data.hintMood.ifBlank { DEFAULT_HINT_MOOD }

    fun dayName(base: String): String = data.dayNames[base]?.takeIf { it.isNotBlank() } ?: base

    fun dayLine(base: String, def: String): String = data.dayLines[base]?.takeIf { it.isNotBlank() } ?: def

    /** 导出文件夹名。会剥掉文件系统不允许的字符，避免建不出目录。 */
    fun exportFolder(): String {
        val raw = data.exportFolder.trim()
        val clean = raw.replace(Regex("[\\\\/:*?\"<>|\\r\\n\\t]"), "").trim().trim('.')
        return clean.ifBlank { "Sympnoia" }.take(24)
    }

    /** 联结页会飘起来的所有句子：关心 + 唤名 + 她的话。 */
    fun quotes(): List<String> = care() + knock() + her()

    fun randomQuote(): String {
        val q = quotes()
        return if (q.isEmpty()) "" else q[(Math.random() * q.size).toInt().coerceIn(0, q.size - 1)]
    }

    fun annivs(): List<Anniv> = data.annivs.ifEmpty { defaultAnnivs }

    fun annivOf(dayKey: String): Anniv? = annivs().firstOrNull { it.matches(dayKey) }

    /** 纪念日当天的倍率加成。多个纪念日叠加。 */
    fun annivMul(dayKey: String): Double {
        var m = 1.0
        annivs().forEach { if (it.matches(dayKey)) m *= it.mul }
        return m
    }

    fun isGoldDay(dayKey: String): Boolean = annivs().any { it.matches(dayKey) && it.gold }

    fun annivName(dayKey: String): String? {
        val hit = annivs().filter { it.matches(dayKey) }
        return if (hit.isEmpty()) null else hit.joinToString(" · ") { it.name }
    }

    fun annivLine(dayKey: String): String =
        annivs().filter { it.matches(dayKey) && it.line.isNotBlank() }
            .joinToString("\n") { it.line }

    /**
     * 第三人称代词（她 / 他 / ta）。全应用的文案都走这里，
     * 所以用户改一次「人称」就能让整份界面跟着换 —— 包括主动互动给她的人称提示。
     */
    fun pro(): String = data.pronoun.ifBlank { "她" }

    fun callName(): String = data.displayName.ifBlank { pro() }

    /** 相伴天数（自出生日期起算）。 */
    fun daysSinceStart(): Long =
        try {
            val c = Dates.parse(data.birthDate.ifBlank { "2016-02-14" })
            Dates.daysSince(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH))
        } catch (e: Exception) {
            0L
        }
}
