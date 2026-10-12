/*
 * ============================================================================
 *  一、行动熵引擎与日期工具
 *  ---------------------------------------------------------------------------
 *  【这个文件是怎么来的】它原本在 MainActivity.kt 的开头，源码被误删后，
 *  从损坏前最后一次成功编译的字节码反编译还原而成。逻辑、常量与全部中文文案
 *  都逐项核对过（这一份还做过「编回去再比字节码」的验证，结果完全一致），
 *  但**原来的注释找不回来了** —— 下面的注释是事后补写的。
 *
 *  【Dates】只负责「日期的表示与换算」：把时间切成 `yyyy-MM-dd` 这样的日键，
 *  以及节假日、星期、儒略日之类的辅助换算。
 *  注意真正的节日/节气数据表在 SpecialDays 里，这里只是转发。
 *
 *  【AE】是整个应用的核心计分器 —— 「行动熵」。设计意图：
 *    · 记录本身不直接加减分数，而是**先算出每一类各自的贡献，再乘上倍率**，
 *      这样调整倍率就能整体改变手感，不用去动每一项的公式；
 *    · 情绪的价值不对称：越负面、越极端，权重越高（`moodBase`）；
 *      一天之内情绪起伏大，另有一项「情绪方差」加成（`moodSwing`）；
 *    · 「联结」（写下名字）不直接给分，而是**加权**（`bondWeight`）——
 *      这也是为什么斐波那契之花里「唤名」那片花瓣的数值要单独算差值。
 * ========================================================================== */

package com.example.asephina

import androidx.compose.ui.graphics.Color
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.abs
import kotlin.math.ln
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * 日期工具。
 *
 * `ThreadLocal` 那一份是为了**线程安全**：`SimpleDateFormat` 不是线程安全的，
 * 而这里既会在界面线程被调用，也会在导出/备份的后台线程被调用。
 * 每个线程各拿一份自己的格式化器，就不会互相踩。
 */
object Dates {

    private val fmts = object : ThreadLocal<Array<SimpleDateFormat>>() {
        override fun initialValue(): Array<SimpleDateFormat> = arrayOf(
            SimpleDateFormat("yyyy-MM-dd", Locale.CHINA),
            SimpleDateFormat("yy/M/d/HH.mm", Locale.CHINA),
            SimpleDateFormat("HH:mm", Locale.CHINA),
            SimpleDateFormat("yyyy年M月d日", Locale.CHINA)
        )
    }

    private val dayFmt: SimpleDateFormat get() = fmts.get()!![0]
    private val stampFmt: SimpleDateFormat get() = fmts.get()!![1]
    private val hhmmFmt: SimpleDateFormat get() = fmts.get()!![2]
    private val cnFmt: SimpleDateFormat get() = fmts.get()!![3]

    private val weekCn = arrayOf("日", "一", "二", "三", "四", "五", "六")

    fun todayKey(): String = dayFmt.format(Date())

    fun key(cal: Calendar): String = dayFmt.format(cal.time)

    fun parse(key: String): Calendar {
        val c = Calendar.getInstance()
        return try {
            c.time = dayFmt.parse(key) ?: Date()
            c
        } catch (e: Exception) {
            c
        }
    }

    fun cnOf(key: String): String = try {
        cnFmt.format(dayFmt.parse(key)!!)
    } catch (e: Exception) {
        key
    }

    fun stamp(ts: Long): String = stampFmt.format(Date(ts))

    fun hhmm(ts: Long): String = hhmmFmt.format(Date(ts))

    fun weekName(cal: Calendar): String = "周" + weekCn[cal.get(Calendar.DAY_OF_WEEK) - 1]

    fun shift(key: String, deltaDays: Int): String {
        val c = parse(key)
        c.add(Calendar.DAY_OF_MONTH, deltaDays)
        return key(c)
    }

    fun yearOf(key: String): Int = parse(key).get(Calendar.YEAR)

    fun monthOf(key: String): Int = parse(key).get(Calendar.MONTH) + 1

    fun lingweiDayName(dayKey: String): String? = Persona.annivName(dayKey)

    fun isLingweiDay(dayKey: String): Boolean = Persona.annivOf(dayKey) != null

    fun tagsOf(dayKey: String): List<DayTag> = SpecialDays.tagsOf(dayKey)

    fun festivalMul(dayKey: String): Double = SpecialDays.mulOf(dayKey)

    fun dayKeyOf(year: Int, month1: Int, day: Int): String = String.format(Locale.CHINA, "%04d-%02d-%02d", year, month1, day)

    fun daysToNextAnniversary(month: Int, day: Int): Long {
        val now = Calendar.getInstance()
        val target = Calendar.getInstance().apply {
            set(Calendar.MONTH, month - 1)
            set(Calendar.DAY_OF_MONTH, day)
            set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
        }
        if (target.before(now)) target.add(Calendar.YEAR, 1)
        val d0 = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
        }
        return ((target.timeInMillis - d0.timeInMillis) / 8.64e7).roundToInt().toLong()
    }

    fun daysSince(year: Int, month: Int, day: Int): Long {
        val start = Calendar.getInstance().apply {
            set(year, month - 1, day, 0, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return ((Calendar.getInstance().timeInMillis - start.timeInMillis) / 8.64e7).roundToInt().toLong()
    }

    fun daysBetween(a: String, b: String): Int = ((parse(b).timeInMillis - parse(a).timeInMillis) / 8.64e7).roundToInt()

    fun daysInMonth(year: Int, month0: Int): Int =
        Calendar.getInstance().apply { set(year, month0, 1) }
            .getActualMaximum(Calendar.DAY_OF_MONTH)

    fun firstWeekday(year: Int, month0: Int): Int =
        Calendar.getInstance().apply { set(year, month0, 1) }
            .get(Calendar.DAY_OF_WEEK) - 1
}

/*
 * 行动熵引擎。
 *
 * 一天的分数大致是：
 *   行动熵 = (行迹 + 文记 + 心情 + 运动) × (联结加权 + 额外加成) × 当日倍率
 *            + 刮刮乐之类的固定加成，最后再乘节日倍率
 * 每一项都有自己的「倍率」旋钮，调手感时只动旋钮、不动公式。
 */
object AE {

    /** 基准线：一天的熵低于它算「轻」，这是分档与花开的参照 */
    const val BASE = 100.0
    const val OVER = 170.0
    const val TOTAL = 270.0
    const val COEF = 6.0
    const val DOC_COEF = 10.0
    const val DIARY_COEF = 12.5
    const val DOC_SCALE = 300.0

    /**
     * 一次「唤名」值多少。
     * 难捱度（fill）越高越重 —— 在很难受的时候还愿意写下名字，本身就被看得更重。
     */
    fun nameWeight(fill: Int): Double = 0.025 * (1.0 + fill.coerceIn(0, 100) / 100.0)

    /**
     * 联结加权：唤名**不加分**，而是把当天其它所有东西整体抬起来。
     * 这也是为什么「唤名」那片花瓣的数值要取「加权前后的差值」——
     * 它没有自己独立的分数，只能从它抬起来的那部分里读。
     * 上限锁在 1.2，避免写很多次名字把一天的分数拉到失真。
     */
    fun bondWeight(nameCount: Int, nameFillSum: Double = 0.0): Double =
        1.0 + min(nameCount * nameWeight(0) + max(nameFillSum, 0.0) * 2.5e-4, 0.2)

    fun levelWeight(level: Int): Double = when (level) {
        1 -> 3.0
        2 -> 2.2
        3 -> 1.6
        4 -> 1.2
        else -> 0.8
    }

    fun timeTerm(minutes: Int): Double = ln(1.0 + max(minutes, 0) / 30.0)

    fun effTerm(eff: Int): Double = 0.5 + eff.coerceIn(0, 100) / 100.0

    fun herBonus(forHer: Boolean): Double = if (forHer) 1.2 else 1.0

    fun pomoMul(cycles: Int): Double = 1.1 * (1.0 + min(max(cycles - 1, 0), 8) * 0.03)

    /** 计划完成在勾选完成时给的加成 */
    const val PLANNED_DONE_MUL = 1.05

    /** 这一条的原始熵（不含计划完成的加成） */
    private fun rawEventAE(e: Ev): Double =
        levelWeight(e.level) * timeTerm(e.minutes) * effTerm(e.eff) * herBonus(e.forHer) *
            COEF * (if (e.pomo) pomoMul(e.pomoCycles) else 1.0)

    /**
     * 这一条**算进去**的熵。
     * 计划完成但还没勾选 → 0：条目照常显示、但不计入当日总量；
     * 勾上之后才计入，并且按计划完成的加成 ×1.05。
     */
    fun ofEvent(e: Ev): Double {
        if (e.planned && !e.done) return 0.0
        val base = rawEventAE(e)
        return if (e.planned) base * PLANNED_DONE_MUL else base
    }

    /**
     * 这一条**能给多少**熵 —— 不管勾没勾都返回。
     * 界面用它显示「可获得」，所以没完成的计划条目也看得到自己能拿多少。
     */
    fun eventPotential(e: Ev): Double =
        rawEventAE(e) * (if (e.planned) PLANNED_DONE_MUL else 1.0)

    fun ofEvents(list: List<Ev>): Double = list.sumOf { ofEvent(it) }

    fun ofDoc(chars: Int, isDiary: Boolean = false): Double =
        (if (isDiary) DIARY_COEF else DOC_COEF) * ln(1.0 + max(chars, 0) / DOC_SCALE)

    const val CUSTOM_WEIGHT = 1.1

    class Emotion(val name: String, val valence: Double, val weight: Double)

    val Emotions = listOf(
        Emotion("开心", 1.0, 1.0),
        Emotion("惊喜", 0.9, 1.1),
        Emotion("充实", 0.85, 1.0),
        Emotion("感动", 0.7, 1.2),
        Emotion("期待", 0.6, 1.1),
        Emotion("释然", 0.5, 0.9),
        Emotion("平静", 0.2, 0.8),
        Emotion("不知道", 0.0, 1.05),
        Emotion("尴尬", -0.3, 1.1),
        Emotion("迷茫", -0.4, 1.3),
        Emotion("逃避", -0.45, 1.3),
        Emotion("疲惫", -0.5, 1.3),
        Emotion("难受", -0.55, 1.35),
        Emotion("委屈", -0.6, 1.4),
        Emotion("孤独", -0.65, 1.5),
        Emotion("烦躁", -0.7, 1.4),
        Emotion("焦虑", -0.75, 1.45),
        Emotion("生气", -0.8, 1.4),
        Emotion("难过", -0.85, 1.5),
        Emotion("虚无", -1.0, 1.6)
    )

    fun knownEmotion(name: String): Emotion? = Emotions.firstOrNull { it.name == name.trim() }

    fun resolve(name: String): Emotion = knownEmotion(name) ?: Emotion(name.trim().ifBlank { "自定义" }, 0.0, CUSTOM_WEIGHT)

    fun emotionOf(name: String): Emotion = Emotions.firstOrNull { it.name == name } ?: Emotions[7]

    /**
     * 一条心情的基础熵：权重 ×（1 + |强度| × 0.6）。
     * 强度取绝对值 —— 极端的高兴和极端的难过一样「重」，
     * 方向由 valence 决定（见 moodPosition），不由大小决定。
     */
    fun moodBase(e: Emotion, intensity: Int): Double = e.weight * (1.0 + min(abs(intensity), 5) * 0.6)

    fun moodPosition(e: Emotion, intensity: Int): Double = e.valence + intensity.coerceIn(-5, 5) / 5.0

    /**
     * 情绪方差项：同一天多条心情之间的**起伏**。
     * 两项相加：整体跨度（range）+ 相邻两次之间的平均跳变（avgPath）。
     * 封顶 30 —— 一天之内情绪翻江倒海也不该把分数拉到失控。
     */
    fun moodSwing(positions: List<Double>): Double {
        if (positions.size < 2) return 0.0
        val range = positions.max() - positions.min()
        var path = 0.0
        for (i in 1 until positions.size) path += abs(positions[i] - positions[i - 1])
        val avgPath = path / (positions.size - 1)
        return min(2.5 * range + 1.5 * avgPath, 30.0)
    }

    fun ofMood(emotion: String, intensity: Int): Double = moodBase(resolve(emotion), intensity)

    const val STEPS_COEF = 11.0
    const val STEPS_SCALE = 1200.0
    const val STEPS_CAP = 27000L
    const val STEPS_GOAL_MUL = 1.2
    const val STEPS_OVER_MUL = 1.5

    fun ofSteps(steps: Long): Double = if (steps <= 0L) 0.0 else STEPS_COEF * ln(1.0 + steps / STEPS_SCALE)

    fun ofStepsCapped(steps: Long): Double = ofSteps(min(steps, STEPS_CAP))

    fun stepsGoalMul(steps: Long, goal: Int): Double = if (goal > 0 && steps >= goal) STEPS_GOAL_MUL else 1.0

    fun stepsOverMul(steps: Long): Double = if (steps > STEPS_CAP) STEPS_OVER_MUL else 1.0

    fun stepsAE(steps: Long, goal: Int): Double = ofStepsCapped(steps) * stepsGoalMul(steps, goal)

    fun stepsToKm(steps: Long, strideCm: Int): Double = steps * strideCm / 100000.0

    fun stepsToKcal(steps: Long, weightKg: Int): Double = steps * weightKg * 5.0e-4

    fun dayTotal(
        traceAE: Double,
        docAE: Double,
        moodAE: Double,
        nameCount: Int,
        traceMul: Double = 1.0,
        docMul: Double = 1.0,
        moodMul: Double = 1.0,
        bondBonus: Double = 0.0,
        dayMul: Double = 1.0,
        flatAE: Double = 0.0,
        lingweiMul: Double = 1.0,
        stepsAE: Double = 0.0,
        stepsOverMul: Double = 1.0,
        nameFillSum: Double = 0.0,
        festivalMul: Double = 1.0
    ): Double {
        val base = traceAE * traceMul + docAE * docMul + moodAE * moodMul + stepsAE
        return ((base * (bondWeight(nameCount, nameFillSum) + bondBonus) * dayMul + flatAE) * lingweiMul * festivalMul * stepsOverMul)
            .coerceAtLeast(0.0)
    }

    const val LINGWEI_MUL = 1.5

    fun stateOf(ae: Double): Pair<String, Color> = when {
        ae < 20.0 -> Persona.states()[0] to TextMuted
        ae < 45.0 -> Persona.states()[1] to CatTrace
        ae < 70.0 -> Persona.states()[2] to PurpleMain
        ae < 100.0 -> Persona.states()[3] to PinkSoft
        ae < 185.0 -> Persona.states()[4] to WarmLight
        else -> Persona.states()[5] to GoldSoft
    }

    fun stateWords(ae: Double): String {
        val w = Persona.stateLines()
        return when {
            ae < 20.0 -> w[0]
            ae < 45.0 -> w[1]
            ae < 70.0 -> w[2]
            ae < 100.0 -> w[3]
            ae < 185.0 -> w[4]
            else -> w[5]
        }
    }
}
