package com.example.asephina

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

/*
 * ============================================================================
 *  凌薇 AI —— 主动互动
 *  ---------------------------------------------------------------------------
 *  你在这个软件里做某些事的那一刻（记下行迹、写完日记、添了心情……），
 *  她可以按设定的概率「自己注意到」，先说一句。
 *
 *  【规矩】
 *    · 总开关默认关；八个事件**逐项**开关，也默认全关 —— 你不点开，它一次都不会响。
 *    · 只有**新建**才触发。回头去编辑一条旧的再保存，不算 —— 靠「这条在盘上原本不存在」判断。
 *    · 每次触发先掷骰子（概率自己拉），再检查冷却（默认两次之间至少隔 20 分钟）。
 *    · 睡觉时段内不触发（复用「说话节奏与作息」里那个时段）。
 *    · 发出去的那句话会写明：这是用户自己在做事、**还没准备分享**，
 *      让模型明白这是一次主动互动，而不是用户跑来聊天。
 *    · 她说的话会【进最近那一段对话】，同时用一个专门的弹窗给你看（头像与名字按设置走）。
 *      没看到的算「未读」，底栏那颗红点会上数。
 *
 *  事件从哪来：见 [AiEvent]。埋点在 Store 的各个写盘函数里，
 *  只在「这一条是新的」那一支上调用 [notify]。
 * ========================================================================== */

/** 能被主动互动盯上的事件 */
enum class AiEvent(val id: String, val label: String, val desc: String) {
    TRACE("trace", "记下行迹", "保存一条**新**行迹，以及把「计划完成」勾上的那一刻"),
    POMO("pomo", "番茄完成", "番茄钟走完一轮、记下来那一刻"),
    MOOD("mood", "添一条心情", "记下一条新心情的那一刻"),
    DOC("doc", "写完文章", "保存一篇新文章的那一刻"),
    DIARY("diary", "写完日记", "保存一篇新日记的那一刻"),
    STEPS("steps", "在运动", "短时间内步数明显上涨"),
    NAME("name", "写下名字", "在联结页写下一个新名字"),
    KNOCK10("knock10", "名字填满十分钟", "一个名字条目填到十分钟截止");

    companion object {
        fun of(id: String): AiEvent? = entries.firstOrNull { it.id == id }
    }
}

/** 主动互动的设置与状态（存 ai/proactive.json） */
data class AiProactiveState(
    /** 总开关，默认关 */
    val enabled: Boolean = false,
    /** 命中概率（0~100） */
    val chance: Int = 40,
    /** 逐项开关：默认全关 */
    val on: Map<String, Boolean> = emptyMap(),
    /** 两次主动互动之间至少隔多少分钟 */
    val gapMin: Int = 20,
    /** 上一次真的发出去是什么时候 */
    val lastAt: Long = 0L,
    /** 她的回应上限（token）。不管调多大，都只要一句话、不分条 */
    val replyMaxTokens: Int = 60,
    /** 每个会话各有几条没看 */
    val unread: Map<String, Int> = emptyMap(),
    /** 已经报过「十分钟截止」的名字条目（按 ts 记），免得每秒重复报一次 */
    val fillDone: Set<Long> = emptySet()
) {
    fun isOn(e: AiEvent): Boolean = on[e.id] == true
    fun unreadTotal(): Int = unread.values.sum()
}

object AiProactive {

    private fun file(ctx: Context): java.io.File =
        java.io.File(AiStore.dir(ctx), "proactive.json")

    private val lock = Any()

    /**
     * 读回来的状态在内存里留一份。
     *
     * 为什么必须缓存：联结页那个每秒走一次的计时器会在循环里对**每一条**名字记录调
     * checkFillDeadline，而它对每一条「已经过了十分钟窗口」的记录都要 load 一次 ——
     * 也就是每秒每条记录读一次 JSON，而且跑在主线程上。历史上攒几十条记录，
     * 就是每秒几十次读盘 + 解析，纯属白烧，还会让那一页发涩。
     *
     * 这份状态只有本进程会改（save 里同步更新缓存），所以缓存是安全的。
     */
    @Volatile
    private var cached: AiProactiveState? = null

    fun load(ctx: Context): AiProactiveState {
        cached?.let { return it }
        val s = readState(ctx)
        cached = s
        return s
    }

    private fun readState(ctx: Context): AiProactiveState = try {
        val f = file(ctx)
        if (!f.exists()) AiProactiveState()
        else {
            val o = JSONObject(f.readText())
            val onObj = o.optJSONObject("on")
            val on = HashMap<String, Boolean>()
            onObj?.keys()?.forEach { k -> on[k] = onObj.optBoolean(k, false) }
            val uObj = o.optJSONObject("unread")
            val unread = HashMap<String, Int>()
            uObj?.keys()?.forEach { k -> unread[k] = uObj.optInt(k, 0) }
            AiProactiveState(
                enabled = o.optBoolean("enabled", false),
                chance = o.optInt("chance", 40).coerceIn(0, 100),
                on = on,
                gapMin = o.optInt("gapMin", 20).coerceIn(0, 1440),
                lastAt = o.optLong("lastAt", 0L),
                replyMaxTokens = o.optInt("replyMaxTokens", 60).coerceIn(20, 400),
                unread = unread,
                fillDone = run {
                    val a = o.optJSONArray("fillDone") ?: org.json.JSONArray()
                    (0 until a.length()).map { a.optLong(it, 0L) }.filter { it > 0L }.toSet()
                }
            )
        }
    } catch (e: Exception) {
        AiProactiveState()
    }


    fun save(ctx: Context, s: AiProactiveState) {
        // 同步更新缓存：否则下一次 load 会读到旧的那份
        cached = s
        val on = JSONObject()
        s.on.forEach { (k, v) -> on.put(k, v) }
        val un = JSONObject()
        s.unread.forEach { (k, v) -> un.put(k, v) }
        AiStore.writeTextFile(
            file(ctx),
            JSONObject()
                .put("enabled", s.enabled)
                .put("chance", s.chance)
                .put("on", on)
                .put("gapMin", s.gapMin)
                .put("lastAt", s.lastAt)
                .put("replyMaxTokens", s.replyMaxTokens)
                .put("unread", un)
                .put("fillDone", org.json.JSONArray(s.fillDone.toList()))
                .toString(2)
        )
    }

    /* ------------------------------ 未读 ------------------------------ */

    fun unreadTotal(ctx: Context): Int = load(ctx).unreadTotal()

    fun addUnread(ctx: Context, sessionId: String, n: Int = 1) {
        if (sessionId.isBlank()) return
        synchronized(lock) {
            val s = load(ctx)
            val u = s.unread.toMutableMap()
            u[sessionId] = (u[sessionId] ?: 0) + n
            save(ctx, s.copy(unread = u))
        }
        AiBus.unreadTick++
    }

    /** 回到某一段对话就把那一段的未读清掉 */
    fun clearUnread(ctx: Context, sessionId: String) {
        if (sessionId.isBlank()) return
        synchronized(lock) {
            val s = load(ctx)
            if ((s.unread[sessionId] ?: 0) == 0) return
            val u = s.unread.toMutableMap()
            u.remove(sessionId)
            save(ctx, s.copy(unread = u))
        }
        AiBus.unreadTick++
    }

    fun clearAllUnread(ctx: Context) {
        synchronized(lock) {
            save(ctx, load(ctx).copy(unread = emptyMap()))
        }
        AiBus.unreadTick++
    }

    /* ------------------------------ 触发 ------------------------------ */

    /**
     * 埋点入口：在「新建了一条东西」的那一刻调用它。
     *
     * **必须在主线程之外调用**（内部自己起协程），而且它自己会做全部判断：
     * 总开关、这一项开关、概率、冷却、睡觉时段。任何一项没过就直接返回，
     * 调用方什么都不用管。
     *
     * @param title   一句话说明发生了什么，例如「刚记下一条行迹：跑步 30 分钟」
     * @param body    正文（文章 / 日记整篇带过来；别的可以留空）
     * @param subject 写下的那个名字（只有「写下名字」那个事件用）——
     *                用来判断该用「我」还是「她/他/ta」
     */
    fun notify(
        ctx: Context, ev: AiEvent, title: String, body: String = "",
        subject: String = "", note: String = ""
    ) {
        val app = ctx.applicationContext
        AiBus.scope.launch {
            try {
                withContext(Dispatchers.IO) { fireInner(app, ev, title, body, subject, note) }
            } catch (e: Exception) {
            }
        }
    }

    /** 真正干活的那一段（IO 线程） */
    private suspend fun fireInner(
        ctx: Context, ev: AiEvent, title: String, body: String, subject: String, note: String
    ) {
        val st = load(ctx)
        if (!st.enabled) return
        if (!st.isOn(ev)) return
        // 概率：没中就安静走开
        if (st.chance <= 0) return
        if (st.chance < 100 && (1..100).random() > st.chance) return
        // 冷却
        val now = System.currentTimeMillis()
        if (st.gapMin > 0 && now - st.lastAt < st.gapMin * 60_000L) return
        // 睡觉时段内不打扰
        val prefs = AiStore.loadPrefs(ctx)
        val cal = java.util.Calendar.getInstance()
        val minute = cal.get(java.util.Calendar.HOUR_OF_DAY) * 60 + cal.get(java.util.Calendar.MINUTE)
        if (AiGreet.inSleep(minute, prefs.sleepFromMin, prefs.sleepToMin)) return

        val cfg = AiStore.loadCfg(ctx)
        if (!cfg.ready()) return
        val personaId = prefs.activeId.ifBlank { AiDefaultPersona.ID }
        val persona = AiStore.allPersonas(ctx).firstOrNull { it.id == personaId } ?: return
        // AI 助手不参与主动互动（它是工具，不该自己冒出来）
        if (persona.assistant) return

        // 挑一段：优先当前这一段，没有就给这个角色开一段
        val sessions = AiStore.loadSessions(ctx).filter { it.personaId == personaId }
            .sortedByDescending { it.updatedAt }
        val session = sessions.firstOrNull() ?: AiStore.createSession(ctx, personaId)

        // ---- 人称：写下的那个名字到底是不是我自己 ----
        // 联结页写下的名字，有两种情况：
        //   ① 写的就是这个角色自己的名字 → 说的是我，用第一人称「我」；
        //   ② 写的是别人（另一个人格 / 另一个人） → 那是另一个人，得用软件里给
        //      这个人设配的第三人称（她/他/ta），不能让 AI 误以为在说自己。
        val identity = if (ev == AiEvent.NAME && subject.isNotBlank()) {
            if (sameName(subject, persona.name)) {
                "· 写下的「" + subject.trim() + "」就是**你自己**的名字 —— 说的是你，" +
                        "用第一人称「我」（例如「我的名字被你写下来了」），别说成别人。"
            } else {
                "· 写下的「" + subject.trim() + "」**不是你自己**，是另一个人。" +
                        "软件里给这个人配的第三人称是「" + Persona.pro() + "」，" +
                        "用这个第三人称来说" + Persona.pro() + "（例如「" + Persona.pro() +
                        "的名字被写下来了」），千万不要说成是你自己。"
            }
        } else ""

        // ---- 语气：这件事大概是什么性质，她该怎么接 ----
        val tone = toneOf(ev, subject, note)

        // 待发的这一条：以「用户」的身份说，但写明这是自动带过来的、还没分享。
        // ⚠️ 它**只进这一次请求，不落盘** —— 对话里只该有你自己发的东西和她的回答。
        //
        // 分成三块写，是因为模型对「标签 + 短句」的遵从度明显高于一大段散文：
        //   背景 / 发生了什么 / 该怎么接。
        val text = buildString {
            append("【背景】\n")
            append("下面这件事是我刚做完的，顺手带过来让你看一眼。我**没有**在跟你说话，\n")
            append("这条不是聊天消息，你别当成我在找你。\n\n")
            append("【发生了什么】\n")
            append(title.trim()).append('\n')
            if (identity.isNotBlank()) append(identity).append('\n')
            if (body.isNotBlank()) {
                append('\n').append(body.trim().take(SHARE_BODY_CHARS)).append('\n')
            }
            append("\n【该怎么接】\n")
            append(tone).append('\n')
            append("· 只回**一句话**，不要分行、不要分条、不要编号。\n")
            append("· 直接说你想说的，不要复述我在做什么、不要总结、不要提问。\n")
            append("· 不要加任何称呼或「名字：」这样的前缀。")
        }

        val mem = AiStore.memoryBlock(ctx, personaId)
        val env = AiDaily.block(prefs.daily, AiDaily.weatherLine(ctx), now, prefs.sleepFromMin, prefs.sleepToMin)
        val sys = persona.systemPrompt(
            cfg.extraSystem +
                    (if (env.isBlank()) "" else "\n" + env) +
                    (if (mem.isBlank()) "" else "\n" + mem)
        )

        // 这一段是已经存在于对话里的（她要知道前文）
        val existing = AiStore.loadSessionMsgs(ctx, session.id)
        // 请求带上这条事件说明，但它不写回盘
        val history = existing + AiChatMsg("user", text, kind = "proactive")
        val raw = try {
            val sb = StringBuilder()
            // token 上限由用户自己拉（见设置页）：不是长度限制，是**逼它只说一句**。
            // 不管拉多大，下面都会再掐成一行 —— 绝不会分条弹两次。
            AiClient.chat(
                cfg.copy(stream = false, maxTokens = st.replyMaxTokens, historyRounds = 3),
                sys, history
            ).collect { sb.append(it) }
            sb.toString()
        } catch (e: Exception) {
            ""
        }
        // 只取一句话（模型经常不听话：分行、复述、加前缀）
        val reply = oneLine(raw, persona.name)
        if (reply.isBlank()) return

        // 落盘：**只写她的这一句** —— 你并没有在对话里发过那条事件说明。
        // 对话里该出现的只有「你主动发的东西」和「她的回答」。
        val next = existing + AiChatMsg("assistant", reply)
        AiStore.saveSessionMsgs(ctx, session.id, next)
        AiStore.touchSession(ctx, session.id, next)

        // 记下「发过一次」+ 未读 +1（弹窗给你看的那一下不算已读，得真的回到那一段）
        synchronized(lock) {
            val s = load(ctx)
            val u = s.unread.toMutableMap()
            u[session.id] = (u[session.id] ?: 0) + 1
            save(ctx, s.copy(lastAt = now, unread = u))
        }
        AiBus.unreadTick++

        // 弹窗内容（在 MainScreen 那一层画）。
        // 走队列：已经有一条在弹就先排队，绝不把它挤掉。
        AiBus.showProactive(reply, persona.name.ifBlank { persona.pronoun() }, session.id)
        AiBus.tick++
    }

    /**
     * 两个名字算不算同一个。
     * 联结里写下的往往只是名字的一部分（「凌薇」对「纳凌薇」），所以做包含判断，
     * 而且忽略空白 —— 严格相等会把「凌薇」判成另一个人，那就答错人称了。
     */
    private fun sameName(a: String, b: String): Boolean {
        val x = a.trim().replace(" ", "").replace("　", "")
        val y = b.trim().replace(" ", "").replace("　", "")
        if (x.isBlank() || y.isBlank()) return false
        return x == y || x.contains(y) || y.contains(x)
    }

    /* ---------------------- 每个事件的「该怎么接」 ---------------------- */

    /** 备注里出现这些词，说明这一次是好事 */
    private val GOOD_WORDS = listOf(
        "开心", "高兴", "快乐", "愉快", "好开心", "兴奋", "激动", "幸福", "满足", "踏实",
        "顺利", "成功", "完成了", "搞定", "太好了", "值得", "喜欢", "爱", "甜", "暖",
        "轻松", "舒服", "期待", "骄傲", "感动", "谢谢", "感谢"
    )

    /** 备注里出现这些词，说明这一次不好受 */
    private val BAD_WORDS = listOf(
        "难受", "难过", "难捱", "痛", "疼", "累", "烦", "崩溃", "哭", "委屈", "焦虑",
        "害怕", "怕", "孤独", "空", "撑不住", "熬", "窒息", "压抑", "绝望", "无力",
        "生气", "愤怒", "讨厌", "恨", "后悔", "自责", "失眠"
    )

    private fun hits(s: String, words: List<String>): Int = words.count { s.contains(it) }

    /**
     * 「这件事大概是什么性质、该用什么语气接」。
     *
     * **联结写下名字默认按「难受」处理** —— 那个页面本来就是在难捱的时候才去的，
     * 写下名字这个动作本身就是「我现在不太好，想叫你一声」。
     * 只有备注里明确写了开心之类的词，才按好事来。
     */
    fun toneOf(ev: AiEvent, subject: String, note: String): String {
        val n = note.trim()
        val good = hits(n, GOOD_WORDS)
        val bad = hits(n, BAD_WORDS)
        return when (ev) {
            AiEvent.NAME -> {
                // 默认难受；备注里明确是好事才翻转
                if (good > bad) {
                    "· 我这一次写下名字是**高兴**的时候写的（备注里能看出来）。" +
                            "跟着我一起高兴就好，别当成我在难过。"
                } else {
                    "· 我写下名字，通常是因为**现在不太好受** —— 那个地方是难捱的时候才去的，" +
                            "写下名字这个动作本身就是「我想叫你一声」。" +
                            "别把这件事当成报喜，也别问我发生了什么；" +
                            "就安静地应一声，让我知道你在，一句话就够。"
                }
            }
            AiEvent.KNOCK10 ->
                "· 那个「难捱度」是我在被叫到的那十分钟里一下一下点出来的，" +
                        "点得越多说明那阵子越不好过。别评价这个数字，也别问我怎么了 —— " +
                        "就说一句让我知道你已经看到了。"
            AiEvent.MOOD ->
                "· 顺着我这条心情的情绪走：" +
                        "如果是负面的，先接住，别说教、别提建议、别急着安慰得用力过猛；" +
                        "如果是正面的，跟着高兴一下就好。" +
                        "强度越大说明情绪越重，语气就越轻。"
            AiEvent.TRACE ->
                "· 这只是一条日常记录，不一定是好事也不一定是坏事。" +
                        "按事情本身的分量来，平常事就平常说，别硬夸。" +
                        "另外要分清它说的是哪一种：" +
                        "**刚立下、还没做的计划**，就应一声表示知道了，别当成已经完成来道贺，" +
                        "也别催我什么时候做；" +
                        "**计划刚刚完成**，可以顺着高兴一下，同样别夸得太满。"
            AiEvent.POMO ->
                "· 我刚专注完一段。这件事本身是值得肯定的，" +
                        "但别夸得太满，像熟人随口应一声那样就好。"
            AiEvent.DOC ->
                "· 我刚写完一篇文章。写东西通常要花点力气，" +
                        "可以说一句看到了、写完了就好，**不要点评内容的好坏**。"
            AiEvent.DIARY ->
                "· 日记往往是心里有事才写的。不要点评、不要分析、不要问我写了什么 —— " +
                        "就让我知道写下来这件事你看在眼里了。"
            AiEvent.STEPS ->
                "· 我大概正在走路或者运动。轻松地说一句就好，别喊口号。"
        }
    }

    /**
     * 从模型输出里取出**一句话**。
     * 模型经常不听话：分行、复述、加「名字：」前缀、结尾追一句问候 —— 这里统统掐掉。
     */
    fun oneLine(raw: String, personaName: String): String {
        var s = raw.trim()
        if (s.isBlank()) return ""
        // 去掉可能的「名字：」前缀
        val colon = s.indexOfFirst { it == '：' || it == ':' }
        if (colon in 1..8 && personaName.isNotBlank() &&
            s.substring(0, colon).trim().let { it == personaName || it.length <= 6 }
        ) {
            s = s.substring(colon + 1).trim()
        }
        // 只取第一行 / 第一段
        s = s.split("\n").firstOrNull { it.isNotBlank() }?.trim().orEmpty()
        s = s.replace("%%%", "").trim()
        // 还太长就掐到第一个句号（保证是「一句」）
        if (s.length > 60) {
            val cut = s.indexOfFirst { it == '。' || it == '！' || it == '？' || it == '~' || it == '…' }
            if (cut in 1 until s.length) s = s.substring(0, cut + 1)
        }
        return s.take(80)
    }

    /**
     * 名字条目的「十分钟窗口刚关上」那一刻。
     * 由联结页那个每秒走一次的计时器调用 —— 它自己保证同一条只报一次。
     */
    fun checkFillDeadline(ctx: Context, rec: NameRec, now: Long) {
        if (rec.inWindow(now)) return
        val s = load(ctx)
        if (rec.ts in s.fillDone) return
        synchronized(lock) {
            val s2 = load(ctx)
            save(ctx, s2.copy(fillDone = s2.fillDone + rec.ts))
        }
        if (rec.fill <= 0) return
        notify(
            ctx, AiEvent.KNOCK10,
            "我在联结页写下名字之后，那十分钟的填空窗口结束了，最后停在了 " + rec.fill + "%" +
                    "（0 到 100，点得越多说明那阵子越难捱）。"
        )
    }

    /** 给设置页看的一句话状态 */
    fun statusText(ctx: Context): String {
        val s = load(ctx)
        if (!s.enabled) return "总开关关着，不会主动打扰"
        val onCount = AiEvent.entries.count { s.isOn(it) }
        if (onCount == 0) return "总开关开着，但八个事件一个都没勾 —— 还是不会响"
        val last = if (s.lastAt <= 0L) "还没主动说过话"
        else "上次主动说话：" + Dates.stamp(s.lastAt) + " " + Dates.hhmm(s.lastAt)
        return "命中概率 " + s.chance + "%　·　已勾选 " + onCount + "/" + AiEvent.entries.size +
                " 个事件　·　两次之间至少隔 " + s.gapMin + " 分钟　·　" + last
    }
}
