/*
 * ============================================================================
 *  三、数据层
 *  ---------------------------------------------------------------------------
 *  【这个文件是怎么来的】整个 `object Store` 原本在 MainActivity.kt 里，
 *  源码被误删后从最后一次成功编译的字节码反编译还原。逻辑、常量与全部中文文案
 *  都逐项核对过，**原注释找不回来了**，下面的注释是事后补写的。
 *
 *  【Store 是什么】应用里所有读写都经过它，没有数据库，全是 JSON 文件：
 *      events/<日期>.json              一天的全部记录（行迹、心情、当日熵值、步数、游戏状态）
 *      docs/<类型>/<分类>/<id>.json     一篇文章或一篇日记（含富文本与图片标记）
 *      names/names.json                唤名记录
 *      config/                         各种设置与预设
 *      gallery/                        画像
 *
 *  【两条重要约定】
 *    1. 所有写盘都走 `writeAtomicText`（先写 .tmp、再原子改名）。
 *       直接 writeText 是先清空再写，进程中途被杀就留下半个文件 —— JSON 解析失败，
 *       那条记录会在界面上凭空消失。文章正文尤其经不起这个。
 *    2. 「今天」与「过去」走不同路径：当天可增删并实时重算熵值；
 *       过零点后冻结（locked），补记历史不会回头改写已经定下的那一天。
 * ========================================================================== */

package com.example.asephina

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.ConcurrentHashMap
import org.json.JSONArray
import org.json.JSONObject

object Store {
    private var cacheTick = -1
    private var cacheIndex: StatsIndex? = null
    private val dirtyDays: MutableSet<String> = ConcurrentHashMap.newKeySet<String>()
    @Volatile
    private var dirtyNames = false

    /** 数据根目录。注意它是 Android/data 下的私有目录，卸载会一起没掉，所以导出必须落在外面。 */
    fun root(ctx: Context): File {
        val base = ctx.getExternalFilesDir(null) ?: ctx.filesDir
        val dir = File(base, "Asephina")
        if (!dir.exists()) dir.mkdirs()
        File(dir, "events").mkdirs()
        File(dir, "docs/article").mkdirs()
        File(dir, "docs/diary").mkdirs()
        File(dir, "names").mkdirs()
        File(dir, "media").mkdirs()
        File(dir, "config").mkdirs()
        return dir
    }

    fun eventsDir(ctx: Context): File = File(root(ctx), "events")

    fun docsTypeDir(ctx: Context, type: String): File {
        val d = File(File(root(ctx), "docs"), type)
        if (!d.exists()) d.mkdirs()
        return d
    }

    fun mediaDir(ctx: Context): File {
        val d = File(root(ctx), "media")
        if (!d.exists()) d.mkdirs()
        return d
    }

    fun pathText(ctx: Context): String = root(ctx).absolutePath

    fun listDays(ctx: Context): List<String> =
        eventsDir(ctx).listFiles { f -> f.isFile && f.name.endsWith(".json") }
            ?.map { it.name.removeSuffix(".json") }
            ?.sorted()
            ?: emptyList()

    private fun parseEvents(o: JSONObject): List<Ev> {
        val out = ArrayList<Ev>()
        val arr = o.optJSONArray("events") ?: JSONArray()
        for (i in 0 until arr.length()) {
            val e = arr.getJSONObject(i)
            out.add(
                Ev(
                    e.optLong("id", System.currentTimeMillis()),
                    e.optString("title", ""),
                    e.optInt("level", 3),
                    e.optInt("minutes", 30),
                    e.optInt("eff", 60),
                    e.optBoolean("forHer", false),
                    e.optString("note", ""),
                    e.optBoolean("pomo", false),
                    e.optInt("pomoCycles", 0),
                    e.optBoolean("planned", false),
                    e.optBoolean("done", false)
                )
            )
        }
        return out
    }

    private fun parseDocEntries(o: JSONObject): MutableList<DayDocEntry> {
        val out = ArrayList<DayDocEntry>()
        val arr = o.optJSONArray("docs") ?: JSONArray()
        for (i in 0 until arr.length()) {
            val e = arr.getJSONObject(i)
            out.add(
                DayDocEntry(
                    e.optLong("id", 0L),
                    e.optString("title", ""),
                    e.optDouble("ae", 0.0),
                    e.optBoolean("locked", false)
                )
            )
        }
        return out
    }

    private fun parseMoods(o: JSONObject): MutableList<MoodEntry> {
        val out = ArrayList<MoodEntry>()
        val arr = o.optJSONArray("moods") ?: JSONArray()
        for (i in 0 until arr.length()) {
            val e = arr.getJSONObject(i)
            out.add(
                MoodEntry(
                    e.optLong("id", 0L),
                    e.optLong("ts", 0L),
                    e.optString("emotion", "平静"),
                    e.optInt("intensity", 0),
                    e.optDouble("ae", 0.0),
                    e.optBoolean("locked", false),
                    e.optString("note", "")
                )
            )
        }
        return out
    }

    private fun readDayObject(ctx: Context, dayKey: String): JSONObject? {
        return try {
            val f = File(eventsDir(ctx), "$dayKey.json")
            if (f.exists()) JSONObject(f.readText()) else null
        } catch (e: Exception) {
            null
        }
    }

    /** 读某一天的全部行迹/运动条目。返回只读 List —— 要改先 toMutableList()。 */
    fun loadDay(ctx: Context, dayKey: String): List<Ev> {
        val o = readDayObject(ctx, dayKey) ?: return ArrayList()
        return try {
            parseEvents(o)
        } catch (e: Exception) {
            ArrayList()
        }
    }

    fun loadDayDocEntries(ctx: Context, dayKey: String): MutableList<DayDocEntry> {
        val o = readDayObject(ctx, dayKey) ?: return ArrayList()
        return try {
            parseDocEntries(o)
        } catch (e: Exception) {
            ArrayList()
        }
    }

    fun loadDayMoods(ctx: Context, dayKey: String): MutableList<MoodEntry> {
        val o = readDayObject(ctx, dayKey) ?: return ArrayList()
        return try {
            parseMoods(o)
        } catch (e: Exception) {
            ArrayList()
        }
    }

    fun loadDayMoodSwing(ctx: Context, dayKey: String): Double =
        readDayObject(ctx, dayKey)?.optDouble("moodSwing", 0.0) ?: 0.0

    private fun swingOf(moods: List<MoodEntry>): Double {
        val positions = moods.sortedBy { it.ts }
            .map { AE.moodPosition(AE.resolve(it.emotion), it.intensity) }
        return AE.moodSwing(positions)
    }

    fun loadDayLockedMood(ctx: Context, dayKey: String): Double =
        readDayObject(ctx, dayKey)?.optDouble("lockedMoodAE", -1.0) ?: -1.0

    private fun parseDaySteps(o: JSONObject): DaySteps = DaySteps(
        o.optLong("stepAccum", 0L),
        o.optLong("stepBase", -1L),
        o.optLong("stepLast", -1L),
        o.optLong("stepOffset", 0L),
        o.optLong("stepTs", 0L)
    )

    fun loadDaySteps(ctx: Context, dayKey: String): DaySteps {
        val o = readDayObject(ctx, dayKey) ?: return DaySteps()
        return try {
            parseDaySteps(o)
        } catch (e: Exception) {
            DaySteps()
        }
    }

    fun loadDayStepsTotal(ctx: Context, dayKey: String): Long = loadDaySteps(ctx, dayKey).total()

    fun saveDaySteps(ctx: Context, dayKey: String, s: DaySteps) {
        val before = try {
            parseDaySteps(readDayObject(ctx, dayKey) ?: JSONObject()).total()
        } catch (e: Exception) {
            0L
        }
        val now = s.total()
        val delta = now - before
        writeDayFile(
            ctx, dayKey, loadDay(ctx, dayKey), loadDayDocEntries(ctx, dayKey),
            loadDayMoods(ctx, dayKey), steps = s
        )
        if (delta >= 300L) {
            AiProactive.notify(
                ctx, AiEvent.STEPS,
                "我好像正在运动：刚才这一会儿步数涨了 " + delta + " 步，今天累计 " + now + " 步。"
            )
        }
    }

    /**
     * 把某一天整体写回盘。这是「一天的快照」——events / docs / moods / steps / 游戏状态
     * 全在一个文件里，所以任何一处改动都要把整份重新写一遍。
     *
     * 末尾那串 `locked*` 参数是**冻结值**：传 NaN 表示「这次不改动，沿用盘上原值」。
     * 这样补记历史时不会把已经定下的那一天的熵值覆盖掉。
     */
    fun writeDayFile(
        ctx: Context,
        dayKey: String,
        events: List<Ev>,
        docs: List<DayDocEntry>,
        moods: List<MoodEntry>,
        moodSwing: Double = Double.NaN,
        lockedTrace: Double = Double.NaN,
        lockedMoodAE: Double = Double.NaN,
        game: DayGame? = null,
        lingweiShown: Boolean? = null,
        lingweiVer: Int? = null,
        specialShown: Boolean? = null,
        specialVer: Int? = null,
        steps: DaySteps? = null,
        lockedStepsAE: Double = Double.NaN
    ) {
        try {
            val old = readDayObject(ctx, dayKey)
            val swing = if (moodSwing.isNaN()) old?.optDouble("moodSwing", 0.0) ?: 0.0 else moodSwing
            val locked = if (lockedTrace.isNaN()) old?.optDouble("lockedTrace", -1.0) ?: -1.0 else lockedTrace
            val lockedMood = if (lockedMoodAE.isNaN()) old?.optDouble("lockedMoodAE", -1.0) ?: -1.0 else lockedMoodAE
            val lockedSteps = if (lockedStepsAE.isNaN()) old?.optDouble("lockedStepsAE", -1.0) ?: -1.0 else lockedStepsAE

            val st = steps ?: old?.let {
                try {
                    parseDaySteps(it)
                } catch (e: Exception) {
                    DaySteps()
                }
            } ?: DaySteps()

            val g = game ?: DayGame(
                old?.optInt("scratchUsed", 0) ?: 0,
                old?.optDouble("scratchAE", 0.0) ?: 0.0,
                old?.optDouble("dayMul", 1.0) ?: 1.0,
                old?.optDouble("traceMul", 1.0) ?: 1.0,
                old?.optDouble("docMul", 1.0) ?: 1.0,
                old?.optDouble("moodMul", 1.0) ?: 1.0,
                old?.optDouble("bondBonus", 0.0) ?: 0.0,
                old?.optInt("extraScratch", 0) ?: 0
            )
            val lwShown = lingweiShown ?: old?.optBoolean("lingweiShown", false) ?: false
            val lwVer = lingweiVer ?: old?.optInt("lingweiVer", 1) ?: 1
            val spShown = specialShown ?: old?.optBoolean("specialShown", false) ?: false
            val spVer = specialVer ?: old?.optInt("specialVer", 1) ?: 1

            val evArr = JSONArray()
            events.forEach { e ->
                val jo = JSONObject()
                jo.put("id", e.id)
                jo.put("title", e.title)
                jo.put("level", e.level)
                jo.put("minutes", e.minutes)
                jo.put("eff", e.eff)
                jo.put("forHer", e.forHer)
                jo.put("note", e.note)
                if (e.pomo) {
                    jo.put("pomo", true)
                    jo.put("pomoCycles", e.pomoCycles)
                }
                // 计划完成：有才写，普通条目一个字节都不多占
                if (e.planned) {
                    jo.put("planned", true)
                    jo.put("done", e.done)
                }
                evArr.put(jo)
            }

            val docArr = JSONArray()
            docs.forEach { d ->
                val jo = JSONObject()
                jo.put("id", d.id)
                jo.put("title", d.title)
                jo.put("ae", d.ae)
                jo.put("locked", d.locked)
                docArr.put(jo)
            }

            val moodArr = JSONArray()
            moods.forEach { m ->
                val jo = JSONObject()
                jo.put("id", m.id)
                jo.put("ts", m.ts)
                jo.put("emotion", m.emotion)
                jo.put("intensity", m.intensity)
                jo.put("ae", m.ae)
                jo.put("locked", m.locked)
                jo.put("note", m.note)
                moodArr.put(jo)
            }

            val text = JSONObject()
                .put("date", dayKey)
                .put("events", evArr)
                .put("docs", docArr)
                .put("moods", moodArr)
                .put("moodSwing", swing)
                .put("lockedTrace", locked)
                .put("lockedMoodAE", lockedMood)
                .put("lockedStepsAE", lockedSteps)
                .put("stepAccum", st.accum)
                .put("stepBase", st.base)
                .put("stepLast", st.last)
                .put("stepOffset", st.offset)
                .put("stepTs", st.ts)
                .put("steps", st.total())
                .put("stepsAE", AE.stepsAE(st.total(), Steps.cfg(ctx).goal))
                .put("scratchUsed", g.scratchUsed)
                .put("scratchAE", g.flatAE)
                .put("dayMul", g.dayMul)
                .put("traceMul", g.traceMul)
                .put("docMul", g.docMul)
                .put("moodMul", g.moodMul)
                .put("bondBonus", g.bondBonus)
                .put("extraScratch", g.extraScratch)
                .put("lingweiShown", lwShown)
                .put("lingweiVer", lwVer)
                .put("specialShown", spShown)
                .put("specialVer", spVer)
                .toString(2)
            // 原子写：当天记录是全应用最要紧的一份数据，绝不能留下半个文件
            writeAtomicText(File(eventsDir(ctx), "$dayKey.json"), text)
            markDirty(dayKey)
        } catch (e: Exception) {
        }
    }

    fun saveDay(ctx: Context, dayKey: String, list: List<Ev>) {
        // 记下改动前的样子：既要知道哪些是新增的，也要能看出「计划被勾上了」这一下转变
        val oldById: Map<Long, Ev> = try {
            loadDay(ctx, dayKey).associateBy { it.id }
        } catch (e: Exception) {
            emptyMap()
        }
        writeDayFile(ctx, dayKey, list, loadDayDocEntries(ctx, dayKey), loadDayMoods(ctx, dayKey))
        list.forEach { fresh ->
            val before = oldById[fresh.id]
            if (before == null) {
                when {
                    fresh.pomo -> AiProactive.notify(
                        ctx, AiEvent.POMO,
                        "我刚走完一轮番茄钟，记下来的是：「" + fresh.title + "」" +
                            (if (fresh.minutes > 0) "，专注了 " + fresh.minutes + " 分钟" else "")
                    )
                    // 计划完成条目刚立下、还没做：措辞必须点明「还没开始」，
                    // 否则它会当成一件已经做完的事来道贺。
                    fresh.planned && !fresh.done -> AiProactive.notify(
                        ctx, AiEvent.TRACE,
                        "我刚立下一个计划，还没有开始做：「" + fresh.title + "」" +
                            (if (fresh.minutes > 0) "，打算花 " + fresh.minutes + " 分钟" else "") +
                            (if (!fresh.note.isBlank()) "，备注：「" + fresh.note + "」" else "")
                    )
                    else -> AiProactive.notify(
                        ctx, AiEvent.TRACE,
                        "我刚记下一条行迹：「" + fresh.title + "」" +
                            (if (fresh.minutes > 0) "，花了 " + fresh.minutes + " 分钟" else "") +
                            (if (!fresh.note.isBlank()) "，备注：「" + fresh.note + "」" else "")
                    )
                }
            } else if (before.planned && !before.done && fresh.planned && fresh.done) {
                // 之前立的那个计划，刚刚被勾上了 —— 这才是「完成」的那一刻
                AiProactive.notify(
                    ctx, AiEvent.TRACE,
                    "我之前计划要做的一件事，刚刚做完了：「" + fresh.title + "」" +
                        (if (fresh.minutes > 0) "，实际花了 " + fresh.minutes + " 分钟" else "") +
                        (if (fresh.eff > 0) "，效率 " + fresh.eff + "%" else "") +
                        (if (!fresh.note.isBlank()) "，备注：「" + fresh.note + "」" else "")
                )
            }
        }
    }

    fun upsertTodayDocEntry(ctx: Context, doc: Doc, ae: Double) {
        val entries = loadDayDocEntries(ctx, doc.dateKey)
        val i = entries.indexOfFirst { it.id == doc.id }
        val entry = DayDocEntry(doc.id, doc.title, ae, false)
        if (i >= 0) entries[i] = entry else entries.add(entry)
        writeDayFile(
            ctx, doc.dateKey, loadDay(ctx, doc.dateKey), entries,
            loadDayMoods(ctx, doc.dateKey)
        )
    }

    fun removeTodayDocEntry(ctx: Context, doc: Doc) {
        val entries = loadDayDocEntries(ctx, doc.dateKey)
        if (entries.removeAll { it.id == doc.id }) {
            writeDayFile(
                ctx, doc.dateKey, loadDay(ctx, doc.dateKey), entries,
                loadDayMoods(ctx, doc.dateKey)
            )
        }
    }

    /** 记一条心情。当天会顺带重算「情绪方差项」；补记过去则直接标记为已冻结。 */
    fun addMood(
        ctx: Context, dayKey: String, emotion: String, intensity: Int,
        note: String = "",
        /** 这一批的共同时间戳；不传就取当前时刻 */
        stamp: Long = System.currentTimeMillis()
    ) {
        val isToday = dayKey == Dates.todayKey()
        val moods = loadDayMoods(ctx, dayKey)
        // ⚠️ 时间戳必须由调用方给：多选一次记好几条时，若每条各取一次当前毫秒，
        //    相邻两条会差几毫秒（中间还要落盘），列表就按时间戳并不到一起，
        //    「心情/心情/心情」永远不会出现。同一批共用一个时间戳才对。
        val ts = stamp
        // ⚠️ id 也必须自己保证唯一：原来 id 也是「当前毫秒」，同一毫秒内加两条就撞了，
        //    而列表拿 id 当 key、删除也按 id 找 —— 撞了会丢条目或删错。
        var id = ts
        while (moods.any { it.id == id }) { id++ }
        moods.add(
            MoodEntry(
                id, ts, emotion, intensity,
                AE.ofMood(emotion, intensity), !isToday, note.trim()
            )
        )
        writeDayFile(
            ctx, dayKey, loadDay(ctx, dayKey), loadDayDocEntries(ctx, dayKey), moods,
            moodSwing = if (isToday) swingOf(moods) else Double.NaN
        )
        // ⚠️ 这里**不发**主动互动通知。
        //    多选记心情时这里是循环调用的，每次发一条就会连弹好几个窗；
        //    用户眼里那只是「记了一条心情」。改为由调用方在整批写完后发一次。
    }

    fun deleteMood(ctx: Context, dayKey: String, id: Long) {
        val isToday = dayKey == Dates.todayKey()
        val moods = loadDayMoods(ctx, dayKey)
        if (moods.removeAll { it.id == id }) {
            writeDayFile(
                ctx, dayKey, loadDay(ctx, dayKey), loadDayDocEntries(ctx, dayKey), moods,
                moodSwing = if (isToday) swingOf(moods) else Double.NaN
            )
        }
    }

    /**
     * 删掉「同一刻一起记下的那一条」。
     *
     * 多选记心情时会一次写下好几行，它们共用一个时间戳（见 addMood 的说明）。
     * 在用户眼里那是**一条**记录，所以删除也要整条一起删 ——
     * 只删其中一行，剩下几行就像凭空裂出来的碎片。
     */
    fun deleteMoodStamp(ctx: Context, dayKey: String, stamp: Long) {
        val isToday = dayKey == Dates.todayKey()
        val moods = loadDayMoods(ctx, dayKey)
        if (moods.removeAll { it.ts == stamp }) {
            writeDayFile(
                ctx, dayKey, loadDay(ctx, dayKey), loadDayDocEntries(ctx, dayKey), moods,
                moodSwing = if (isToday) swingOf(moods) else Double.NaN
            )
        }
    }

    fun loadDayGame(ctx: Context, dayKey: String): DayGame {
        val o = readDayObject(ctx, dayKey) ?: return DayGame()
        return DayGame(
            o.optInt("scratchUsed", 0),
            o.optDouble("scratchAE", 0.0),
            o.optDouble("dayMul", 1.0),
            o.optDouble("traceMul", 1.0),
            o.optDouble("docMul", 1.0),
            o.optDouble("moodMul", 1.0),
            o.optDouble("bondBonus", 0.0),
            o.optInt("extraScratch", 0)
        )
    }

    fun applyScratchPrize(ctx: Context, dayKey: String, prize: Game.ScratchPrize) {
        val g = loadDayGame(ctx, dayKey)
        val used = g.scratchUsed + 1
        val merged = when (prize.kind) {
            0 -> g.copy(scratchUsed = used, flatAE = g.flatAE + prize.value)
            1 -> g.copy(scratchUsed = used, dayMul = g.dayMul * prize.value)
            2 -> g.copy(scratchUsed = used, traceMul = g.traceMul * prize.value)
            3 -> g.copy(scratchUsed = used, docMul = g.docMul * prize.value)
            4 -> g.copy(scratchUsed = used, moodMul = g.moodMul * prize.value)
            5 -> g.copy(scratchUsed = used, bondBonus = g.bondBonus + prize.value)
            6 -> g.copy(scratchUsed = used, extraScratch = g.extraScratch + prize.value.toInt())
            else -> g.copy(scratchUsed = used)
        }
        writeDayFile(
            ctx, dayKey, loadDay(ctx, dayKey), loadDayDocEntries(ctx, dayKey),
            loadDayMoods(ctx, dayKey), game = merged
        )
    }

    fun loadLingweiShown(ctx: Context, dayKey: String): Boolean {
        val o = readDayObject(ctx, dayKey) ?: return false
        return o.optBoolean("lingweiShown", false) && o.optInt("lingweiVer", 1) >= 2
    }

    fun markLingweiShown(ctx: Context, dayKey: String) {
        writeDayFile(
            ctx, dayKey, loadDay(ctx, dayKey), loadDayDocEntries(ctx, dayKey),
            loadDayMoods(ctx, dayKey), lingweiShown = true, lingweiVer = 2
        )
    }

    fun loadSpecialShown(ctx: Context, dayKey: String): Boolean {
        val o = readDayObject(ctx, dayKey) ?: return false
        return o.optBoolean("specialShown", false) && o.optInt("specialVer", 1) >= 2
    }

    fun markSpecialShown(ctx: Context, dayKey: String) {
        writeDayFile(
            ctx, dayKey, loadDay(ctx, dayKey), loadDayDocEntries(ctx, dayKey),
            loadDayMoods(ctx, dayKey), specialShown = true, specialVer = 2
        )
    }

    fun gameSummaryLines(g: DayGame): List<String> {
        val parts = ArrayList<String>()
        if (g.dayMul > 1.0001) parts.add("全体×" + String.format(Locale.CHINA, "%.2f", g.dayMul))
        if (g.traceMul > 1.0001) parts.add("行迹×" + String.format(Locale.CHINA, "%.2f", g.traceMul))
        if (g.docMul > 1.0001) parts.add("文记×" + String.format(Locale.CHINA, "%.2f", g.docMul))
        if (g.moodMul > 1.0001) parts.add("心情×" + String.format(Locale.CHINA, "%.2f", g.moodMul))
        if (g.bondBonus > 1.0E-4) parts.add("联结+" + String.format(Locale.CHINA, "%.2f", g.bondBonus))
        if (g.flatAE > 1.0E-4) parts.add("直接 +" + String.format(Locale.CHINA, "%.1f", g.flatAE) + " AE")
        if (parts.isEmpty()) parts.add("无加成")
        return parts
    }

    fun gameSummary(g: DayGame): String = gameSummaryLines(g).joinToString(" ｜ ")

    private fun knockFile(ctx: Context): File = File(File(root(ctx), "config"), "knock.json")

    fun loadKnock(ctx: Context): Long {
        return try {
            val f = knockFile(ctx)
            if (f.exists()) JSONObject(f.readText()).optLong("count", 0L) else 0L
        } catch (e: Exception) {
            0L
        }
    }

    /** 联结页「敲门」的终身累计次数（不按天结算）。返回累加后的值。 */
    fun addKnock(ctx: Context, delta: Long = 1L): Long {
        val v = loadKnock(ctx) + delta
        return try {
            writeAtomicText(knockFile(ctx), JSONObject().put("count", v).toString(2))
            v
        } catch (e: Exception) {
            v
        }
    }

    fun displayedDocAE(ctx: Context, doc: Doc, isDiary: Boolean): Double {
        if (doc.dateKey == Dates.todayKey()) return AE.ofDoc(doc.chars, isDiary)
        val entry = loadDayDocEntries(ctx, doc.dateKey).firstOrNull { it.id == doc.id }
        return entry?.ae ?: AE.ofDoc(doc.chars, isDiary)
    }

    fun listCategories(ctx: Context, type: String): List<Category> {
        val dir = docsTypeDir(ctx, type)
        val out = ArrayList<Category>()
        dir.listFiles { f -> f.isDirectory }?.sortedBy { it.name }?.forEach { c ->
            var chars = 0
            var count = 0
            c.listFiles { f -> f.isFile && f.name.endsWith(".json") }?.forEach { f ->
                val d = readDocFile(f)
                if (d != null) {
                    chars += d.chars
                    count++
                }
            }
            out.add(Category(c.name, count, chars))
        }
        return out
    }

    fun createCategory(ctx: Context, type: String, name: String): Boolean {
        val safe = Regex("[\\\\/:*?\"<>|]").replace(name.trim(), "_")
        if (safe.isEmpty()) return false
        val d = File(docsTypeDir(ctx, type), safe)
        return if (d.exists()) false else d.mkdirs()
    }

    fun deleteCategory(ctx: Context, type: String, name: String) {
        try {
            File(docsTypeDir(ctx, type), name).deleteRecursively()
        } catch (e: Exception) {
        }
    }

    private fun readDocFile(f: File): Doc? {
        return try {
            val o = JSONObject(f.readText())
            val imgs = ArrayList<String>()
            val ia = o.optJSONArray("images")
            if (ia != null) {
                for (i in 0 until ia.length()) imgs.add(ia.getString(i))
            }
            val vids = ArrayList<String>()
            val va = o.optJSONArray("videos")
            if (va != null) {
                for (i in 0 until va.length()) vids.add(va.getString(i))
            }
            Doc(
                o.optLong("id", f.nameWithoutExtension.toLongOrNull() ?: 0L),
                o.optString("title", ""),
                o.optString("body", ""),
                imgs,
                vids,
                o.optLong("created", 0L),
                o.optLong("updated", 0L),
                o.optString("dateKey", Dates.todayKey()),
                SpanTool.fromJson(o.optJSONArray("spans"))
            )
        } catch (e: Exception) {
            null
        }
    }

    /** 列出某个分类下的文章/日记。解析失败的文件会被跳过而不是让整页崩掉。 */
    fun listDocs(ctx: Context, type: String, category: String): List<Doc> {
        val dir = File(docsTypeDir(ctx, type), category)
        val out = ArrayList<Doc>()
        dir.listFiles { f -> f.isFile && f.name.endsWith(".json") }?.forEach { f ->
            val it = readDocFile(f)
            if (it != null) out.add(it)
        }
        return out.sortedByDescending {
            if (it.created > 0L) it.created else Dates.parse(it.dateKey).timeInMillis
        }
    }

    /**
     * 保存一篇文章/日记。
     * ⚠️ 落盘走 writeAtomicText —— 正文是最经不起「半个文件」的数据，
     *    闪退或中途被杀会让这篇**整篇消失**。别改回 writeText。
     */
    fun saveDoc(ctx: Context, type: String, category: String, doc: Doc) {
        val wasNew = !File(File(docsTypeDir(ctx, type), category), "${doc.id}.json").exists()
        try {
            val dir = File(docsTypeDir(ctx, type), category)
            if (!dir.exists()) dir.mkdirs()
            val jo = JSONObject()
            jo.put("id", doc.id)
            jo.put("title", doc.title)
            jo.put("body", doc.body)
            jo.put("images", JSONArray(doc.images))
            jo.put("videos", JSONArray(doc.videos))
            jo.put("created", doc.created)
            jo.put("updated", doc.updated)
            jo.put("dateKey", doc.dateKey)
            jo.put("chars", doc.chars)
            jo.put("spans", SpanTool.toJson(doc.spans))
            // 原子写：文章/日记正文在这里落盘。以前是直接 writeText，
            // 闪退或写到一半被杀就会留下截断的 JSON —— 那条文章会**整篇消失**。
            writeAtomicText(File(dir, "${doc.id}.json"), jo.toString(2))
        } catch (e: Exception) {
        }
        if (doc.dateKey == Dates.todayKey()) {
            upsertTodayDocEntry(ctx, doc, AE.ofDoc(doc.chars, type == "diary"))
        }
        if (wasNew) {
            val isDiary = type == "diary"
            AiProactive.notify(
                ctx, if (isDiary) AiEvent.DIARY else AiEvent.DOC,
                "我刚写完一" + (if (isDiary) "篇日记" else "篇文章") +
                    (if (!doc.title.isBlank()) "，标题是「" + doc.title + "」" else "") +
                    "，一共 " + doc.chars + " 个字。整篇在下面：",
                doc.body
            )
        }
    }

    /** 删一篇。只有当天写的才撤掉当日熵值条目；历史日期的熵值照旧保留。 */
    fun deleteDoc(ctx: Context, type: String, category: String, doc: Doc) {
        try {
            File(File(docsTypeDir(ctx, type), category), "${doc.id}.json").delete()
        } catch (e: Exception) {
        }
        if (doc.dateKey == Dates.todayKey()) {
            removeTodayDocEntry(ctx, doc)
        }
    }

    private fun allDocs(ctx: Context): List<Pair<String, Doc>> {
        val out = ArrayList<Pair<String, Doc>>()
        for (type in listOf("article", "diary")) {
            docsTypeDir(ctx, type).listFiles { f -> f.isDirectory }?.forEach { c ->
                c.listFiles { f -> f.isFile && f.name.endsWith(".json") }?.forEach { f ->
                    val it = readDocFile(f)
                    if (it != null) out.add(type to it)
                }
            }
        }
        return out
    }

    /** 联结页的全部唤名记录。 */
    fun loadNames(ctx: Context): List<NameRec> {
        val out = ArrayList<NameRec>()
        val f = File(File(root(ctx), "names"), "names.json")
        if (!f.exists()) return out
        try {
            val arr = JSONObject(f.readText()).optJSONArray("items") ?: JSONArray()
            for (i in 0 until arr.length()) {
                val o = arr.getJSONObject(i)
                out.add(
                    NameRec(
                        o.optLong("ts", 0L),
                        o.optString("text", Persona.data.displayName),
                        o.optInt("fill", 0).coerceIn(0, 100),
                        o.optString("note", "")
                    )
                )
            }
        } catch (e: Exception) {
        }
        return out
    }

    /** 写回全部唤名记录（整份覆盖）。 */
    fun saveNames(ctx: Context, list: List<NameRec>) {
        val oldTs: Set<Long> = try {
            loadNames(ctx).map { it.ts }.toSet()
        } catch (e: Exception) {
            emptySet()
        }
        try {
            val arr = JSONArray()
            list.forEach {
                val jo = JSONObject()
                jo.put("ts", it.ts)
                jo.put("text", it.text)
                jo.put("fill", it.fill)
                if (!it.note.isBlank()) jo.put("note", it.note)
                arr.put(jo)
            }
            writeAtomicText(
                File(File(root(ctx), "names"), "names.json"),
                JSONObject().put("items", arr).toString(2)
            )
            markNamesDirty()
        } catch (e: Exception) {
        }
        val fresh = list.firstOrNull { !oldTs.contains(it.ts) }
        if (fresh != null) {
            AiProactive.notify(
                ctx, AiEvent.NAME,
                "我刚在联结页写下" + Persona.pro() + "的名字" +
                    (if (!fresh.note.isBlank()) "，还留了一句缘由：「" + fresh.note + "」" else "") + "。",
                subject = fresh.text
            )
        }
    }

    private fun gameOf(o: JSONObject): DayGame = DayGame(
        o.optInt("scratchUsed", 0),
        o.optDouble("scratchAE", 0.0),
        o.optDouble("dayMul", 1.0),
        o.optDouble("traceMul", 1.0),
        o.optDouble("docMul", 1.0),
        o.optDouble("moodMul", 1.0),
        o.optDouble("bondBonus", 0.0),
        o.optInt("extraScratch", 0)
    )

    private fun dayValues(ctx: Context, d: String, goal: Int): DayValues? {
        val o = readDayObject(ctx, d) ?: return null
        val live = d == Dates.todayKey()
        val evs: List<Ev> = try {
            parseEvents(o)
        } catch (e: Exception) {
            ArrayList()
        }
        val docEntries: MutableList<DayDocEntry> = try {
            parseDocEntries(o)
        } catch (e: Exception) {
            ArrayList()
        }
        val moodEntries: MutableList<MoodEntry> = try {
            parseMoods(o)
        } catch (e: Exception) {
            ArrayList()
        }
        val st: DaySteps = try {
            parseDaySteps(o)
        } catch (e: Exception) {
            DaySteps()
        }
        val lockedTrace = o.optDouble("lockedTrace", -1.0)
        val lockedMood = o.optDouble("lockedMoodAE", -1.0)
        val lockedSteps = o.optDouble("lockedStepsAE", -1.0)
        val steps = st.total()
        val trace = if (!live && lockedTrace >= 0.0) lockedTrace else AE.ofEvents(evs)
        val docs = docEntries.sumOf { it.ae }
        val moods = if (!live && lockedMood >= 0.0) {
            lockedMood
        } else {
            moodEntries.sumOf { it.ae } + o.optDouble("moodSwing", 0.0)
        }
        val stepsAE = if (!live && lockedSteps >= 0.0) lockedSteps else AE.stepsAE(steps, goal)
        val game = gameOf(o)
        return DayValues(
            trace, docs, moods, steps, stepsAE, game,
            evs.size, docEntries.size, moodEntries.size, evs.count { it.pomo },
            !live && (lockedTrace < 0.0 || lockedMood < 0.0 || lockedSteps < 0.0)
        )
    }

    private fun dayTotalOf(
        d: String,
        trace: Map<String, Double>,
        docs: Map<String, Double>,
        moods: Map<String, Double>,
        names: Map<String, Int>,
        games: Map<String, DayGame>,
        steps: Map<String, Long>,
        stepsAE: Map<String, Double>,
        nameFill: Map<String, Double>
    ): Double {
        val g = games[d] ?: DayGame()
        return AE.dayTotal(
            trace[d] ?: 0.0,
            docs[d] ?: 0.0,
            moods[d] ?: 0.0,
            names[d] ?: 0,
            g.traceMul,
            g.docMul,
            g.moodMul,
            g.bondBonus,
            g.dayMul,
            g.flatAE,
            if (Dates.isLingweiDay(d)) Persona.annivMul(d) else 1.0,
            stepsAE[d] ?: 0.0,
            AE.stepsOverMul(steps[d] ?: 0L),
            nameFill[d] ?: 0.0,
            SpecialDays.mulOf(d)
        )
    }

    /**
     * 扫全部文件、算出统计索引（每天/每类的熵值、累计步数、番茄次数……）。
     * 这是**最贵**的一次调用，所以外面套了 [index] 做缓存，不要直接调它。
     */
    fun buildIndex(ctx: Context): StatsIndex {
        val today = Dates.todayKey()
        val goal = Steps.cfg(ctx).goal
        val docsByDay = HashMap<String, MutableList<Pair<String, Doc>>>()
        allDocs(ctx).forEach { pair ->
            docsByDay.getOrPut(pair.second.dateKey) { ArrayList() }.add(pair)
        }
        val trace = HashMap<String, Double>()
        val docsAE = HashMap<String, Double>()
        val moodsAE = HashMap<String, Double>()
        val games = HashMap<String, DayGame>()
        val stepsByDay = HashMap<String, Long>()
        val stepsAEByDay = HashMap<String, Double>()
        val eventsCount = HashMap<String, Int>()
        val docsCount = HashMap<String, Int>()
        val moodsCount = HashMap<String, Int>()
        val pomosCount = HashMap<String, Int>()
        val blank = ArrayList<String>()
        val days = LinkedHashSet<String>()
        days.addAll(listDays(ctx))
        days.addAll(docsByDay.keys)
        for (d in days) {
            val o = readDayObject(ctx, d)
            val live = d == today
            var events: List<Ev> = ArrayList()
            var entries: MutableList<DayDocEntry> = ArrayList()
            var moods: MutableList<MoodEntry> = ArrayList()
            var st = DaySteps()
            if (o != null) {
                events = try {
                    parseEvents(o)
                } catch (e: Exception) {
                    ArrayList()
                }
                entries = try {
                    parseDocEntries(o)
                } catch (e: Exception) {
                    ArrayList()
                }
                moods = try {
                    parseMoods(o)
                } catch (e: Exception) {
                    ArrayList()
                }
                st = try {
                    parseDaySteps(o)
                } catch (e: Exception) {
                    DaySteps()
                }
            }
            val onDisk = docsByDay[d]
            val blankish = !live && o != null && (onDisk == null || onDisk.isEmpty()) && isEmptyObject(o)
            var needWrite = false
            if (onDisk != null && onDisk.isNotEmpty()) {
                val known = HashSet<Long>()
                for (it in entries) known.add(it.id)
                for ((type, doc) in onDisk) {
                    if (!known.contains(doc.id)) {
                        entries.add(
                            DayDocEntry(doc.id, doc.title, AE.ofDoc(doc.chars, type == "diary"), !live)
                        )
                        needWrite = true
                    }
                }
            }
            var lockedTrace = o?.optDouble("lockedTrace", -1.0) ?: -1.0
            var lockedMood = o?.optDouble("lockedMoodAE", -1.0) ?: -1.0
            var lockedSteps = o?.optDouble("lockedStepsAE", -1.0) ?: -1.0
            if (live) {
                if (lockedTrace >= 0.0 || lockedMood >= 0.0 || lockedSteps >= 0.0) {
                    writeDayFile(
                        ctx, d, events, entries, moods,
                        lockedTrace = -1.0, lockedMoodAE = -1.0, lockedStepsAE = -1.0
                    )
                    lockedTrace = -1.0
                    lockedMood = -1.0
                    lockedSteps = -1.0
                }
            } else {
                if (lockedTrace < 0.0) {
                    lockedTrace = AE.ofEvents(events)
                    needWrite = true
                }
                if (lockedMood < 0.0) {
                    val sw = o?.optDouble("moodSwing", 0.0) ?: 0.0
                    val swing = if (sw > 0.0) sw else swingOf(moods)
                    lockedMood = moods.sumOf { it.ae } + swing
                    needWrite = true
                }
                if (lockedSteps < 0.0) {
                    lockedSteps = AE.stepsAE(st.total(), goal)
                    needWrite = true
                }
                var allLocked = true
                val lockedDocList = entries.map { e ->
                    if (e.locked) {
                        e
                    } else {
                        allLocked = false
                        DayDocEntry(e.id, e.title, e.ae, true)
                    }
                }
                val lockedMoodList = moods.map { m ->
                    if (m.locked) {
                        m
                    } else {
                        allLocked = false
                        MoodEntry(m.id, m.ts, m.emotion, m.intensity, m.ae, true)
                    }
                }
                if (!allLocked) {
                    entries = ArrayList(lockedDocList)
                    moods = ArrayList(lockedMoodList)
                    needWrite = true
                }
                if (needWrite) {
                    writeDayFile(
                        ctx, d, events, entries, moods,
                        lockedTrace = lockedTrace, lockedMoodAE = lockedMood,
                        lockedStepsAE = lockedSteps
                    )
                }
            }
            trace[d] = if (!live && lockedTrace >= 0.0) lockedTrace else AE.ofEvents(events)
            docsAE[d] = entries.sumOf { it.ae }
            moodsAE[d] = if (!live && lockedMood >= 0.0) {
                lockedMood
            } else {
                moods.sumOf { it.ae } + (o?.optDouble("moodSwing", 0.0) ?: 0.0)
            }
            val stepsNow = st.total()
            stepsByDay[d] = stepsNow
            stepsAEByDay[d] = if (!live && lockedSteps >= 0.0) lockedSteps else AE.stepsAE(stepsNow, goal)
            games[d] = o?.let { gameOf(it) } ?: DayGame()
            eventsCount[d] = events.size
            docsCount[d] = entries.size
            moodsCount[d] = moods.size
            pomosCount[d] = events.count { it.pomo }
            if (blankish && trace[d] == 0.0 && docsAE[d] == 0.0 && moodsAE[d] == 0.0 && stepsNow == 0L) {
                blank.add(d)
            }
        }
        val namesByDay = HashMap<String, Int>()
        val nameFillByDay = HashMap<String, Double>()
        loadNames(ctx).forEach { n ->
            val real = dayKeyOfTs(n.ts)
            if (real != null) {
                namesByDay[real] = (namesByDay[real] ?: 0) + 1
                nameFillByDay[real] = (nameFillByDay[real] ?: 0.0) + n.fill.coerceIn(0, 100)
            }
        }
        blank.removeAll { (namesByDay[it] ?: 0) > 0 }
        if (blank.isNotEmpty()) {
            for (d in blank) {
                try {
                    File(eventsDir(ctx), "$d.json").delete()
                } catch (e: Exception) {
                }
                trace.remove(d)
                docsAE.remove(d)
                moodsAE.remove(d)
                stepsByDay.remove(d)
                stepsAEByDay.remove(d)
                games.remove(d)
                eventsCount.remove(d)
                docsCount.remove(d)
                moodsCount.remove(d)
                pomosCount.remove(d)
            }
        }
        var total = 0.0
        val allDays: Set<String> =
            trace.keys + docsAE.keys + moodsAE.keys + namesByDay.keys + games.keys + stepsByDay.keys
        allDays.forEach { d ->
            total += dayTotalOf(
                d, trace, docsAE, moodsAE, namesByDay, games, stepsByDay, stepsAEByDay, nameFillByDay
            )
        }
        return StatsIndex(
            trace, docsAE, moodsAE, namesByDay, games, total, stepsByDay, stepsAEByDay,
            eventsCount, docsCount, moodsCount, pomosCount, nameFillByDay
        )
    }

    private fun refreshIndex(ctx: Context, base: StatsIndex, days: List<String>): StatsIndex? {
        val goal = Steps.cfg(ctx).goal
        val trace = HashMap(base.traceByDay)
        val docs = HashMap(base.docByDay)
        val moods = HashMap(base.moodByDay)
        val steps = HashMap(base.stepsByDay)
        val stepsAE = HashMap(base.stepsAEByDay)
        val games = HashMap(base.gamesByDay)
        val evCount = HashMap(base.eventsByDay)
        val docCount = HashMap(base.docsCountByDay)
        val moodCount = HashMap(base.moodsCountByDay)
        val pomoCount = HashMap(base.pomosByDay)
        for (d in days) {
            val v = dayValues(ctx, d, goal)
            if (v == null) {
                trace.remove(d)
                docs.remove(d)
                moods.remove(d)
                steps.remove(d)
                stepsAE.remove(d)
                games.remove(d)
                evCount.remove(d)
                docCount.remove(d)
                moodCount.remove(d)
                pomoCount.remove(d)
            } else {
                if (v.needFreeze) return null
                trace[d] = v.trace
                docs[d] = v.docs
                moods[d] = v.moods
                steps[d] = v.steps
                stepsAE[d] = v.stepsAE
                games[d] = v.game
                evCount[d] = v.eventsCount
                docCount[d] = v.docsCount
                moodCount[d] = v.moodsCount
                pomoCount[d] = v.pomosCount
            }
        }
        val namesByDay = base.namesByDay
        val nameFillByDay = base.nameFillByDay
        var total = 0.0
        val allDays: Set<String> =
            trace.keys + docs.keys + moods.keys + namesByDay.keys + games.keys + steps.keys
        allDays.forEach { d ->
            total += dayTotalOf(d, trace, docs, moods, namesByDay, games, steps, stepsAE, nameFillByDay)
        }
        return StatsIndex(
            trace, docs, moods, namesByDay, games, total, steps, stepsAE,
            evCount, docCount, moodCount, pomoCount, nameFillByDay
        )
    }

    private fun isEmptyObject(o: JSONObject): Boolean {
        return try {
            (o.optJSONArray("events")?.length() ?: 0) == 0 &&
                (o.optJSONArray("docs")?.length() ?: 0) == 0 &&
                (o.optJSONArray("moods")?.length() ?: 0) == 0 &&
                o.optInt("scratchUsed", 0) == 0 &&
                o.optDouble("scratchAE", 0.0) < 0.001 &&
                o.optLong("stepAccum", 0L) == 0L &&
                o.optLong("stepOffset", 0L) == 0L &&
                o.optLong("stepLast", -1L) < 0L
        } catch (e: Exception) {
            false
        }
    }

    private fun dayKeyOfTs(ts: Long): String? {
        return try {
            SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(Date(ts))
        } catch (e: Exception) {
            null
        }
    }

    private fun markDirty(dayKey: String) {
        dirtyDays.add(dayKey)
    }

    private fun markNamesDirty() {
        dirtyNames = true
    }

    @Synchronized
    /**
     * 带缓存的索引：`tick` 是调用方给的「数据版本号」，变了才重扫。
     * 另外 `dirtyDays` / `dirtyNames` 会记录「哪些天被动过」，
     * 只有被动过的那几天才需要重算 —— 否则改一条记录就要扫全部历史。
     */
    fun index(ctx: Context, tick: Int): StatsIndex {
        val c = cacheIndex
        if (c != null && cacheTick == tick) return c
        val dirty = dirtyDays.toList()
        var built: StatsIndex? = null
        if (c != null && !dirtyNames && dirty.isNotEmpty() && dirty.size <= 8) {
            built = try {
                refreshIndex(ctx, c, dirty)
            } catch (e: Exception) {
                null
            }
        }
        if (built == null) built = buildIndex(ctx)
        dirtyDays.clear()
        dirtyNames = false
        cacheIndex = built
        cacheTick = tick
        return built
    }

    fun streak(ctx: Context): Int {
        val days = listDays(ctx).toSet()
        var cursor = Dates.todayKey()
        if (!days.contains(cursor)) cursor = Dates.shift(cursor, -1)
        var n = 0
        while (days.contains(cursor)) {
            n++
            cursor = Dates.shift(cursor, -1)
        }
        return n
    }

    fun monthStats(idx: StatsIndex, year: Int, month1: Int): List<StatPoint> {
        val dim = Dates.daysInMonth(year, month1 - 1)
        val out = ArrayList<StatPoint>(dim)
        for (d in 1..dim) {
            val k = Dates.dayKeyOf(year, month1, d)
            out.add(StatPoint(d, idx.trace(k), idx.doc(k), idx.mood(k), idx.day(k), idx.steps(k)))
        }
        return out
    }

    fun yearStats(idx: StatsIndex, year: Int): List<StatPoint> {
        val out = ArrayList<StatPoint>(12)
        for (m in 1..12) {
            var t = 0.0
            var dd = 0.0
            var mm = 0.0
            var tot = 0.0
            var st = 0L
            val dim = Dates.daysInMonth(year, m - 1)
            for (d in 1..dim) {
                val k = Dates.dayKeyOf(year, m, d)
                t += idx.trace(k)
                dd += idx.doc(k)
                mm += idx.mood(k)
                tot += idx.day(k)
                st += idx.steps(k)
            }
            out.add(StatPoint(m, t, dd, mm, tot, st))
        }
        return out
    }

    /** 把外部选中的图片/视频拷进应用私有目录，返回落地后的文件。 */
    fun importMedia(ctx: Context, uri: Uri, kind: String): File? {
        return try {
            val mime = ctx.contentResolver.getType(uri)
                ?: if (kind == "video") "video/mp4" else "image/jpeg"
            val ext = when {
                mime.contains("png") -> "png"
                mime.contains("gif") -> "gif"
                mime.contains("webp") -> "webp"
                mime.contains("mp4") -> "mp4"
                mime.contains("3gp") -> "3gp"
                mime.contains("webm") -> "webm"
                mime.contains("quicktime") -> "mov"
                kind == "video" -> "mp4"
                else -> "jpg"
            }
            val f = File(mediaDir(ctx), kind + "_" + System.currentTimeMillis() + "." + ext)
            ctx.contentResolver.openInputStream(uri)?.use { ins ->
                FileOutputStream(f).use { outs -> ins.copyTo(outs) }
            }
            f
        } catch (e: Exception) {
            null
        }
    }

    /** 用系统播放器打开一段视频。返回一句给界面看的提示。 */
    fun openVideo(ctx: Context, file: File): String {
        return try {
            if (Build.VERSION.SDK_INT >= 29) {
                val values = ContentValues()
                values.put("_display_name", file.name)
                values.put("mime_type", "video/*")
                values.put("relative_path", Environment.DIRECTORY_MOVIES + "/" + Persona.exportFolder())
                val uri = ctx.contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values)
                if (uri != null) {
                    ctx.contentResolver.openOutputStream(uri)?.use { os ->
                        FileInputStream(file).use { ins -> ins.copyTo(os) }
                    }
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.setDataAndType(uri, "video/*")
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    ctx.startActivity(intent)
                    "正在用系统播放器打开"
                } else {
                    "无法创建播放文件"
                }
            } else {
                "视频保存在：" + file.absolutePath
            }
        } catch (e: Exception) {
            "打开失败：" + e.message + "\n文件位置：" + file.absolutePath
        }
    }

    fun allDocsPublic(ctx: Context): List<Pair<String, Doc>> = allDocs(ctx)

    fun allDocsWithCat(ctx: Context): List<Triple<String, String, Doc>> {
        val out = ArrayList<Triple<String, String, Doc>>()
        for (type in listOf("article", "diary")) {
            docsTypeDir(ctx, type).listFiles { f -> f.isDirectory }?.forEach { c ->
                c.listFiles { f -> f.isFile && f.name.endsWith(".json") }?.forEach { f ->
                    val it = readDocFile(f)
                    if (it != null) out.add(Triple(type, c.name, it))
                }
            }
        }
        return out
    }

    fun exportWordDoc(ctx: Context): String {
        return try {
            val bin = WordExport.build(ctx)
            if (bin.size < 500) return "导出失败：文档内容异常（" + bin.size + " 字节）"
            val fileName =
                "Sympnoia_" + SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(Date()) + ".docx"
            val ok = Pub.writeBytes(
                ctx, "manual", fileName,
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document", bin
            )
            if (!ok) {
                "导出失败：无法写入文件"
            } else {
                "已导出：" + Pub.pathText("manual") + fileName + "（" + bin.size / 1024 + " KB）"
            }
        } catch (e: Exception) {
            "导出失败：" + e.message
        }
    }

    class DayDocEntry(
        val id: Long,
        val title: String = "",
        val ae: Double = 0.0,
        val locked: Boolean = false
    )

    class MoodEntry(
        val id: Long = System.currentTimeMillis(),
        val ts: Long = System.currentTimeMillis(),
        val emotion: String = "平静",
        val intensity: Int = 0,
        val ae: Double = 0.0,
        val locked: Boolean = false,
        val note: String = ""
    )

    class DaySteps(
        val accum: Long = 0L,
        val base: Long = -1L,
        val last: Long = -1L,
        val offset: Long = 0L,
        val ts: Long = 0L
    ) {
        fun seg(): Long = if (base >= 0L && last >= 0L) (last - base).coerceAtLeast(0L) else 0L

        fun sensor(): Long = accum + seg()

        fun total(): Long = (sensor() + offset).coerceAtLeast(0L)
    }

    data class DayGame(
        val scratchUsed: Int = 0,
        val flatAE: Double = 0.0,
        val dayMul: Double = 1.0,
        val traceMul: Double = 1.0,
        val docMul: Double = 1.0,
        val moodMul: Double = 1.0,
        val bondBonus: Double = 0.0,
        val extraScratch: Int = 0
    )

    class StatsIndex(
        val traceByDay: Map<String, Double>,
        val docByDay: Map<String, Double>,
        val moodByDay: Map<String, Double>,
        val namesByDay: Map<String, Int>,
        val gamesByDay: Map<String, DayGame>,
        val total: Double,
        val stepsByDay: Map<String, Long> = emptyMap(),
        val stepsAEByDay: Map<String, Double> = emptyMap(),
        val eventsByDay: Map<String, Int> = emptyMap(),
        val docsCountByDay: Map<String, Int> = emptyMap(),
        val moodsCountByDay: Map<String, Int> = emptyMap(),
        val pomosByDay: Map<String, Int> = emptyMap(),
        val nameFillByDay: Map<String, Double> = emptyMap()
    ) {
        fun trace(dayKey: String): Double = traceByDay[dayKey] ?: 0.0

        fun doc(dayKey: String): Double = docByDay[dayKey] ?: 0.0

        fun mood(dayKey: String): Double = moodByDay[dayKey] ?: 0.0

        fun names(dayKey: String): Int = namesByDay[dayKey] ?: 0

        fun nameFill(dayKey: String): Double = nameFillByDay[dayKey] ?: 0.0

        fun game(dayKey: String): DayGame = gamesByDay[dayKey] ?: DayGame()

        fun steps(dayKey: String): Long = stepsByDay[dayKey] ?: 0L

        fun stepsAE(dayKey: String): Double = stepsAEByDay[dayKey] ?: 0.0

        fun stepsAEPrefix(prefix: String): Double =
            stepsAEByDay.entries.sumOf { if (it.key.startsWith(prefix)) it.value else 0.0 }

        fun stepsOverMul(dayKey: String): Double = AE.stepsOverMul(steps(dayKey))

        val totalSteps: Long get() = stepsByDay.values.sum()

        val totalEvents: Int get() = eventsByDay.values.sum()

        val totalDocs: Int get() = docsCountByDay.values.sum()

        val totalMoods: Int get() = moodsCountByDay.values.sum()

        val totalPomos: Int get() = pomosByDay.values.sum()

        val totalNames: Int get() = namesByDay.values.sum()

        val recordedDays: Int
            get() = (traceByDay.keys + docByDay.keys + moodByDay.keys + namesByDay.keys + stepsByDay.keys).size

        val firstDay: String?
            get() = (traceByDay.keys + docByDay.keys + moodByDay.keys + namesByDay.keys + stepsByDay.keys).minOrNull()

        fun day(dayKey: String): Double {
            val g = game(dayKey)
            return AE.dayTotal(
                trace(dayKey), doc(dayKey), mood(dayKey), names(dayKey),
                g.traceMul, g.docMul, g.moodMul, g.bondBonus, g.dayMul, g.flatAE,
                if (Dates.isLingweiDay(dayKey)) Persona.annivMul(dayKey) else 1.0,
                stepsAE(dayKey), stepsOverMul(dayKey), nameFill(dayKey), SpecialDays.mulOf(dayKey)
            )
        }
    }

    class StatPoint(
        val unit: Int,
        val trace: Double,
        val doc: Double,
        val mood: Double,
        val dayTotal: Double,
        val steps: Long = 0L
    )

    private class DayValues(
        val trace: Double,
        val docs: Double,
        val moods: Double,
        val steps: Long,
        val stepsAE: Double,
        val game: DayGame,
        val eventsCount: Int,
        val docsCount: Int,
        val moodsCount: Int,
        val pomosCount: Int,
        val needFreeze: Boolean
    )
}
