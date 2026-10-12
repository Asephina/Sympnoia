package com.example.asephina

/*
 * ============================================================================
 *  凌薇 AI —— 主动问候（她先开口）
 *  ---------------------------------------------------------------------------
 *  规则（全部对话**统一度量**，不是每段各算各的）：
 *    · 用户超过 6 小时没有主动说话 → 挑**一段**对话（最近聊过的那段）主动问候一句；
 *    · 还是没回 → 间隔延长到 12 小时，再问一次；
 *    · 再没回 → 12 小时、24 小时、48 小时……每次翻倍，但封顶 48 小时；
 *    · 一共问候 5 次之后就不再打扰了；
 *    · 用户只要回了一句，计数与间隔立刻归零，从 6 小时重新开始；
 *    · 每次「上一次没被搭理」都会记进她的长期记忆 —— 所以她会记得你晾过她。
 *
 *  【什么时候会触发】
 *    应用开着的时候每分钟检查一次；关着应用不会唤醒（本应用没有常驻后台服务，
 *    也不想为这件事多要权限）。关掉期间错过的，下次打开应用会立刻补上。
 *
 *  问候只用一句话，走一次很短的模型调用（max_tokens 很小）；
 *  万一失败就用一句兜底台词，绝不因为问候出问题影响正常聊天。
 * ========================================================================== */

/** 主动问候的状态（全局一份，不按会话分） */
data class GreetState(
    /** 用户最后一次主动说话的时刻 */
    val lastUserAt: Long = 0L,
    /** 上一次主动问候的时刻 */
    val lastGreetAt: Long = 0L,
    /** 这一轮"没人理"期间已经问候过几次 */
    val count: Int = 0,
    /** 下一次要等多少小时 */
    val gapH: Int = AiGreet.FIRST_GAP_H
)

object AiGreet {

    /** 第一次等多久 */
    const val FIRST_GAP_H = 3

    /** 最多主动问候几次（之后就不再打扰） */
    const val MAX_GREETS = 5

    /** 间隔的上限（小时）：3 → 6 → 12 → 24 → 48 */
    const val MAX_GAP_H = 48

    private fun file(ctx: android.content.Context): java.io.File =
        java.io.File(AiStore.dir(ctx), "greet.json")

    /**
     * greet.json 的读-改-写必须串起来。
     *
     * ⚠️ markGreeted 是「读盘 → 生成问候（要等模型好几秒）→ 写盘」，而用户一发消息就会
     *    另外调 onUserSpoke（而且是不等待的那种）。两边交错时，markGreeted 手里那份是旧的，
     *    一写回去就把用户那次「归零」覆盖掉了 —— 结果是她会一直往 MAX_GREETS 上爬，
     *    对一个明明回过话的人一直主动开口。
     */
    private val lock = Any()

    fun load(ctx: android.content.Context): GreetState = try {
        val f = file(ctx)
        if (!f.exists()) GreetState()
        else {
            val o = org.json.JSONObject(f.readText())
            GreetState(
                lastUserAt = o.optLong("lastUserAt", 0L),
                lastGreetAt = o.optLong("lastGreetAt", 0L),
                count = o.optInt("count", 0),
                gapH = o.optInt("gapH", FIRST_GAP_H)
            )
        }
    } catch (e: Exception) {
        GreetState()
    }

    fun save(ctx: android.content.Context, s: GreetState) {
        // 与 AiStore 共用同一把写锁 + 原子改名：半截 JSON 会让 load 退回默认值，
        // 而默认值 lastUserAt = 0 意味着「从来没聊过」，主动问候就再也不开口了
        AiStore.writeTextFile(
            file(ctx),
            org.json.JSONObject()
                .put("lastUserAt", s.lastUserAt)
                .put("lastGreetAt", s.lastGreetAt)
                .put("count", s.count)
                .put("gapH", s.gapH)
                .toString(2)
        )
    }

    /**
     * 用户主动说话了（任何一段对话里发了一条）→ 计数与间隔全部归零。
     * 这样她下次要重新等满 6 小时才会再开口。
     */
    fun onUserSpoke(ctx: android.content.Context, now: Long = System.currentTimeMillis()) {
        synchronized(lock) {
            save(ctx, GreetState(lastUserAt = now, lastGreetAt = 0L, count = 0, gapH = FIRST_GAP_H))
        }
    }

    /** 距离该问候还差多少毫秒；0 = 现在就该问候；-1 = 不该问候 */
    /**
     * 这个时刻在不在「睡觉时段」里。
     * 时段用「当天零点起的分钟数」表示（0~1440），起点等于终点 = 没有设限。
     * 跨零点是常态（23:30 → 07:00），所以分两段判。
     */
    fun inSleep(minuteOfDay: Int, from: Int, to: Int): Boolean {
        // ⚠️ 先归一化再比：直接在原值上判 `from == to` 的话，
        //    0 与 1440 会被当成两个不同的值，随后归一化又都变成 0 ——
        //    落进 else 分支就成了「一天 24 小时都在睡」，主动问候从此再也不开口。
        val f = ((from % 1440) + 1440) % 1440
        val t = ((to % 1440) + 1440) % 1440
        if (f == t) return false
        return if (f < t) minuteOfDay in f until t
        else minuteOfDay >= f || minuteOfDay < t
    }

    /** 从现在到「睡醒」（走出睡觉时段的那一刻）还有多少毫秒 */
    private fun msUntilAwake(now: Long, to: Int): Long {
        val cal = java.util.Calendar.getInstance().apply { timeInMillis = now }
        val cur = cal.get(java.util.Calendar.HOUR_OF_DAY) * 60 + cal.get(java.util.Calendar.MINUTE)
        val need = if (to > cur) to - cur else to + 1440 - cur
        return need * 60_000L
    }

    /**
     * 距离该问候还差多少毫秒；0 = 现在就该问候；-1 = 不该问候。
     *
     * 多了一条限制：**睡觉时段里不主动开口**（时段由用户自己在设置里拉）。
     * 已经在时段里的话就顺延到睡醒之后 —— 大半夜手机自己冒出一句话，谁都受不了。
     */
    fun msUntilDue(
        ctx: android.content.Context,
        sleepFrom: Int = 0,
        sleepTo: Int = 0,
        now: Long = System.currentTimeMillis()
    ): Long {
        val s = load(ctx)
        if (s.count >= MAX_GREETS) return -1L
        // 基准取「用户上次说话」与「上次问候」里更晚的那个：
        // 问候过他还不回，就要从问候那一刻重新计时。
        val base = maxOf(s.lastUserAt, s.lastGreetAt)
        // 从来没聊过就不主动开口 —— 免得刚装上也冒出来一句
        if (base <= 0L) return -1L
        val cal = java.util.Calendar.getInstance().apply { timeInMillis = now }
        val minute = cal.get(java.util.Calendar.HOUR_OF_DAY) * 60 + cal.get(java.util.Calendar.MINUTE)
        if (inSleep(minute, sleepFrom, sleepTo)) {
            return msUntilAwake(now, ((sleepTo % 1440) + 1440) % 1440)
        }
        val need = s.gapH * 3600_000L
        val passed = now - base
        return if (passed >= need) 0L else need - passed
    }

    fun isDue(
        ctx: android.content.Context,
        sleepFrom: Int = 0,
        sleepTo: Int = 0,
        now: Long = System.currentTimeMillis()
    ): Boolean = msUntilDue(ctx, sleepFrom, sleepTo, now) == 0L

    /**
     * 记下「问候过一次」：
     * 把间隔翻倍（封顶），并且如果上一次问候**没被搭理**，就把这件事记进她的记忆。
     *
     * @param personaId 记到哪个角色名下
     */
    fun markGreeted(ctx: android.content.Context, personaId: String, now: Long = System.currentTimeMillis()) {
        synchronized(lock) {
            val s = load(ctx)
            val previous = s.lastGreetAt
            // 上一次问候之后用户一句话都没说 → 对方确实晾着这一头了
            if (s.count >= 1 && previous > 0L && s.lastUserAt < previous) {
                val whenText = Dates.stamp(previous) + " " + Dates.hhmm(previous)
                AiStore.appendMemories(
                    ctx, personaId,
                    listOf("你先开口找过对方一次（" + whenText + "），对方没有回你。")
                )
            }
            save(
                ctx,
                s.copy(
                    lastGreetAt = now,
                    count = s.count + 1,
                    gapH = minOf(s.gapH * 2, MAX_GAP_H)
                )
            )
        }
    }

    /** 给界面看的一句话状态 */
    fun statusText(ctx: android.content.Context, sleepFrom: Int = 0, sleepTo: Int = 0): String {
        val s = load(ctx)
        val cal = java.util.Calendar.getInstance()
        val minute = cal.get(java.util.Calendar.HOUR_OF_DAY) * 60 + cal.get(java.util.Calendar.MINUTE)
        // 睡觉时段里给出的「还差多久」其实是「离开睡觉时段还有多久」——
        // 不说明白的话看着像坏掉了，这里直接讲清楚
        val sleeping = inSleep(minute, sleepFrom, sleepTo)
        if (s.count >= MAX_GREETS) return "已经问候过 $MAX_GREETS 次，不再主动打扰"
        val left = msUntilDue(ctx, sleepFrom, sleepTo)
        val core = when {
            left == 0L -> "该问候了（下次打开对话就会先开口）"
            left > 0 -> {
                val h = left / 3600_000L
                val m = (left % 3600_000L) / 60_000L
                (if (sleeping) "现在是睡觉时段，等睡醒（" else "还差 ") +
                        (if (h > 0) "${h} 小时 ${m} 分" else "${m} 分钟") +
                        (if (sleeping) "后）才会主动开口" else " 才到下一次问候")
            }
            else -> "还没有聊过，不会主动开口"
        }
        return core + "　·　已问候 " + s.count + "/" + MAX_GREETS + " 次，下次间隔 " + s.gapH + " 小时"
    }

    /** 她已经主动问候过几次了（设置页显示用） */
    fun countOf(ctx: android.content.Context): Int = load(ctx).count

    /** 不想让她再主动开口，或者想重新计时 */
    fun reset(ctx: android.content.Context) {
        synchronized(lock) { save(ctx, GreetState()) }
    }

    /** 给模型的那句指令（只加在这一次请求的设定里，不写进对话记录） */
    fun instruction(personaName: String): String =
        "\n（现在对方没在跟你说话，已经有一阵子了。你主动开口找对方说一句，【只一句】，" +
                "像平时那样短，不要问一连串问题，不要提这是提醒你说话，也不要道歉。" +
                "可以顺着上面的时候、天气或你记得的事说。）"

    /**
     * 兜底台词：**模型调用失败**时用（接口没配好时不会走到这里 ——
     * 那种情况压根不会发起问候，见 AiChatScreen.maybeGreet 开头的 cfg.ready 判断）。
     * 按当前时段挑一句，不至于出现「大下午问晚安」这种尴尬。
     */
    fun fallbackLine(now: Long = System.currentTimeMillis()): String {
        val h = java.util.Calendar.getInstance().apply { timeInMillis = now }
            .get(java.util.Calendar.HOUR_OF_DAY)
        return when (h) {
            in 5..8 -> "早。今天也慢慢来就好。"
            in 9..11 -> "在忙吗？记得起来动一动。"
            in 12..13 -> "中午了，吃点什么吧。"
            in 14..17 -> "下午好。喝口水，歇一会儿。"
            in 18..22 -> "晚上好。今天过得怎么样？"
            else -> "这么晚了还没睡？早点休息。"
        }
    }
}
