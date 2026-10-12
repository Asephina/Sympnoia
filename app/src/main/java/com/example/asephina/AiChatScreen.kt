package com.example.asephina

/*
 * ============================================================================
 *  凌薇 AI —— 对话页（底栏第 3 栏）
 *  ---------------------------------------------------------------------------
 *  · 一张人设卡 = 一个对话对象；一个角色可以有**很多段对话**（会话），
 *    像聊天软件那样分开存，可新建、可改名、可删除。会话列表在会话页里看。
 *  · 气泡宽度随文字长短自由变化（上限约屏宽的 78%），不再一律撑满。
 *  · 时间不放在气泡里：按聊天软件的习惯做成**消息之间的时间分隔**，
 *    只在隔得久了（默认 30 分钟）或第一句之前出现一次。
 *  · 头像可选：给她设了头像就显示在她的气泡旁，给自己设了就显示在自己的气泡旁；
 *    不设就整个不显示，气泡照旧占满可用宽度。
 *  · 流式输出逐字显示，可随时「停止」；每轮结束立刻写盘。
 * ========================================================================== */

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.ripple
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.platform.LocalDensity

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/** 隔多久没聊，就在新一轮前面插一条时间分隔（毫秒） */
private const val AI_TIME_GAP_MS = 30 * 60 * 1000L

/**
 * 全局刷新信号：设置页 / 会话页改了东西之后，对话页靠它收到通知重新读盘。
 * 与 Persona.iconTick 是同一套做法（几个页面是兄弟节点，不互相持有状态）。
 */
object AiBus {
    /** 随便什么变了就 +1，对话页据此重新读盘 */
    var tick by mutableIntStateOf(0)

    /** 非空 = 请打开这一段会话（会话页点某一条时写入，处理完对话页会清空） */
    var openSessionId by mutableStateOf("")

    /** +1 = 请新建一段会话并打开（会话页点「新建」时写入） */
    var requestNew by mutableIntStateOf(0)

    /** 新建到哪个人设名下（会话页不知道当前是谁，由对话页时时写进来） */
    var currentPersonaId by mutableStateOf("")

    /* ---------------- 「分享到对话」用的一条通路 ----------------
     * 各个条目上的分享按钮把内容写进 AiShare，MainScreen 弹出会话选择页；
     * 选完把这三样填好并 tick++，对话页看见 shareText 非空就把它当作一条发言发出去。 */
    /** 待发送的分享文本（对话页处理完会清空） */
    var shareText by mutableStateOf("")
    /** 分享到哪一段会话 */
    var shareSessionId by mutableStateOf("")
    /** 那一段属于哪个角色（可能和当前正在看的角色不同） */
    var shareToPersona by mutableStateOf("")

    /** 分享那一行说明（「日记 · 2026-09-27 今天很累」），会跟着消息一起存下来 */
    var shareLabel by mutableStateOf("")

    /* ---- 她主动开口了，但不在你正看着的那一段时，用这条提示告诉你 ---- */
    var greetNotice by mutableStateOf("")
    var greetNoticeSession by mutableStateOf("")

    /** 当前这一段会话的 id（会话页用来标「当前」） */
    var currentSessionId by mutableStateOf("")

    /**
     * +1 = 长期记忆有新内容了。
     * 设置页正开着「记住的事」那一节时靠它刷新（否则得退出去再进来才看得见新记的）。
     */
    var memTick by mutableIntStateOf(0)

    /* ---- 主动互动：她说了一句，用一个专门的弹窗给你看 ---- */
    /** 弹窗里第一句（没在弹窗时就清空） */
    var proactiveText by mutableStateOf("")
    /** 剩下的几条（收在「还有 N 条」里） */
    var proactiveMore by mutableStateOf("")
    /** 说话的是谁（弹窗上显示的名字） */
    var proactiveName by mutableStateOf("")
    /** 落在哪一段对话里（点「去看看」跳过去） */
    var proactiveSession by mutableStateOf("")
    /** 每来一条 +1：弹窗靠它重播一次「出现」的动画 */
    var proactiveTick by mutableIntStateOf(0)

    /**
     * 排队等着的主动互动。
     *
     * 为什么要排队：两条事件挨得近时（比如刚记完行迹又写完日记），
     * 第二条会把第一条**直接挤掉** —— 你连第一句都没读完它就没了。
     * 所以后来的先排队，等这一条被关掉再弹出下一条。
     */
    val proactiveQueue = mutableListOf<Triple<String, String, String>>()

    /** 弹一条：没人占着就直接弹，占着就排队 */
    fun showProactive(text: String, name: String, session: String) {
        if (proactiveText.isBlank()) {
            proactiveText = text
            proactiveName = name
            proactiveSession = session
            proactiveTick++
        } else {
            proactiveQueue.add(Triple(text, name, session))
        }
    }

    /** 关掉当前这条；排着队的话接着弹下一条 */
    fun nextProactive() {
        proactiveText = ""
        proactiveName = ""
        proactiveSession = ""
        val n = proactiveQueue.removeFirstOrNull() ?: return
        proactiveText = n.first
        proactiveName = n.second
        proactiveSession = n.third
        proactiveTick++
    }

    /** 未读数变了就 +1：底栏那颗红点靠它刷新 */
    var unreadTick by mutableIntStateOf(0)

    /**
     * 埋点用的一把协程作用域。
     * 埋点发生在各个页面里（存一条行迹、写完一篇日记……），那些时候对话页可能压根
     * 没在组合中 —— 不能借 compose 的 scope，得有一个活得更久的。
     */
    val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
}

/**
 * 各页面条目上的「分享」按钮把内容放到这里，MainScreen 据此弹出会话选择页。
 * text 非空 = 正在选要分享到哪一段对话；选完由 MainScreen 转交给 AiBus。
 */
object AiShare {
    /** 要分享的正文（已经排版好、截过长度） */
    var text by mutableStateOf("")

    /** 给选择页显示的一行说明，例如「日记 · 2026-09-27 今天很累」 */
    var label by mutableStateOf("")

    fun request(label: String, body: String) {
        this.label = label
        this.text = body
    }

    fun clear() {
        label = ""
        text = ""
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AiChatScreen(
    ctx: Context,
    active: Boolean,
    onOpenSettings: () -> Unit,
    onOpenSessions: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    var cfg by remember { mutableStateOf(AiApiCfg()) }
    var prefs by remember { mutableStateOf(AiPrefs()) }
    val personas = remember { mutableStateListOf<AiPersona>() }
    var activeId by remember { mutableStateOf("") }
    var sessionId by remember { mutableStateOf("") }
    val msgs = remember { mutableStateListOf<AiChatMsg>() }

    var herAvatar by remember { mutableStateOf<ImageBitmap?>(null) }
    var myAvatar by remember { mutableStateOf<ImageBitmap?>(null) }

    /** 她自己总结出来的长期记忆（跨会话共用），每次发消息时拼进设定 */
    var memories by remember { mutableStateOf("") }
    /** 天气那一行（「海淀区：22°C，阴」）；不配城市就是空串 */
    var weatherLine by remember { mutableStateOf("") }
    /** 上一次做总结时的消息条数：每攒够 8 条总结一次，避免重复调用 */
    /**
     * 这一段对话里，已经折进「长期记忆」的消息条数（落盘在会话索引的 memMark 里）。
     *
     * ⚠️ 以前它是对话页内存里的一个 `summarizedAt`，并且在每次打开会话时被重置成
     *    「当前条数」—— 而对话页切走再回来就会被重建一次，于是只要不是一口气聊满 8 条，
     *    计数永远攒不够，记忆一直是 0 条。现在比的是「这一段的总条数」，跟打开过几次无关。
     */
    var memMark by remember { mutableIntStateOf(0) }
    var summarizing by remember { mutableStateOf(false) }

    var input by remember { mutableStateOf("") }
    var busy by remember { mutableStateOf(false) }
    var err by remember { mutableStateOf("") }
    var job by remember { mutableStateOf<Job?>(null) }

    /* ---- 长按多选：点选若干条气泡后批量处理 ---- */
    var selecting by remember { mutableStateOf(false) }
    val picked = remember { mutableStateListOf<Int>() }
    /** 正在等确认的操作："del" / "save" / "copy" / null */
    var batchAsk by remember { mutableStateOf<String?>(null) }
    var favTitle by remember { mutableStateOf("") }
    /** 收藏列表开着没有 */
    var showFavs by remember { mutableStateOf(false) }
    var favTick by remember { mutableIntStateOf(0) }

    fun enterSelect(index: Int) {
        if (busy) return
        selecting = true
        picked.clear()
        picked.add(index)
    }

    fun togglePick(index: Int) {
        if (index in picked) picked.remove(index) else picked.add(index)
    }

    /**
     * 把选中的这几条拼成一段文本。
     * 一行一条，前面挂说话人：她说的话用角色名，自己说的话用「你」。
     */
    fun batchText(): String {
        // 这里不能调 curPersona()：那是本页后面才声明的局部函数（Kotlin 要求先声明再用）
        val p = personas.firstOrNull { it.id == activeId }
        val her = p?.name?.takeIf { it.isNotBlank() } ?: "ta"
        return picked.sorted().mapNotNull { i ->
            val m = msgs.getOrNull(i) ?: return@mapNotNull null
            val who = if (m.role == "assistant") her else "你"
            val body = if (m.kind == "file") (m.head.ifBlank { m.label }).trim() else m.content.replace("\n", " ").trim()
            val img = if (m.img.isNotBlank()) "［图片］" else ""
            if (body.isBlank() && img.isBlank()) null else who + "：" + img + body
        }.joinToString("\n")
    }

    /* ---- 「＋」：相册选图 / 表情包合集 ---- */
    /** 已经选好、还没发出去的那张图（相对于 ai/ 的路径） */
    var pendingImg by remember { mutableStateOf("") }
    var showAttach by remember { mutableStateOf(false) }
    var showStickers by remember { mutableStateOf(false) }
    /** 表情包管理态：点一下是删而不是发 */
    var stickerEdit by remember { mutableStateOf(false) }
    var stickerTick by remember { mutableIntStateOf(0) }

    /**
     * 输入框右侧那个余额。
     *
     * 只对确认过接口的服务商有值（见 AiProvider.balanceKind）；别家一直是 null，
     * 界面上就什么都不显示 —— 不编造、不用 0 冒充。
     */
    var balance by remember { mutableStateOf<AiBalance?>(null) }

    var stickers by remember { mutableStateOf<List<String>>(emptyList()) }

    /**
     * 「＋」里挑进来的文档，等发送时才用。
     * 存的是**抽出来的正文**，不是原文件 —— 原文件一个字节都不复制（见 DocText 的说明）。
     */
    var pendingFiles by remember { mutableStateOf<List<DocText.Picked>>(emptyList()) }

    // 从相册挑一张：压到 1280 长边、转成 JPEG 存进 ai/media/
    val photoPicker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri == null) return@rememberLauncherForActivityResult
        scope.launch {
            val rel = withContext(Dispatchers.IO) {
                val bmp = decodePicked(ctx, uri, 1280) ?: return@withContext ""
                AiStore.saveMedia(ctx, bmp)
            }
            if (rel.isBlank()) err = "这张图读不出来，换一张试试"
            else {
                pendingImg = rel
                showAttach = false
            }
        }
    }

    // 从相册收藏一个表情包
    val stickerPicker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri == null) return@rememberLauncherForActivityResult
        scope.launch {
            val ok = withContext(Dispatchers.IO) {
                val bmp = decodePicked(ctx, uri, 512) ?: return@withContext false
                AiStore.addSticker(ctx, bmp) != null
            }
            if (ok) stickerTick++ else err = "这个表情包存不下来"
        }
    }

    // 从本地挑一个文档。走 OpenDocument 而不是 GetContent：
    // 前者的 uri 能稳定重复读、也能拿到显示名；后者的授权只在当次会话里有效。
    val docPicker = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri == null) return@rememberLauncherForActivityResult
        scope.launch {
            val p = withContext(Dispatchers.IO) { DocText.pick(ctx, uri) }
            pendingFiles = pendingFiles + p
            showAttach = false
        }
    }

    LaunchedEffect(stickerTick) {
        stickers = withContext(Dispatchers.IO) { AiStore.listStickers(ctx) }
    }
    // 已经处理过的「新建」请求号。初值取当前全局计数：本页状态若被系统重建过，
    // 也不会把之前那些请求当成新的、凭空多开一段会话。
    var handledNew by remember { mutableIntStateOf(AiBus.requestNew) }
    // 已经处理过的刷新号：与 busy 一起当钥匙，见下面那个 LaunchedEffect
    var handledTick by remember { mutableIntStateOf(AiBus.tick) }
    /** 每成功打开一段会话 +1：用来在「刚打开」时补一次长期记忆 */
    var loadedTick by remember { mutableIntStateOf(0) }

    fun curPersona(): AiPersona? =
        personas.firstOrNull { it.id == activeId } ?: personas.firstOrNull()

    /** 读一张人设卡对应的两个头像 */
    suspend fun loadAvatars(pid: String) {
        herAvatar = withContext(Dispatchers.IO) { AiStore.loadAvatar(ctx, pid) }
        myAvatar = withContext(Dispatchers.IO) { AiStore.loadAvatar(ctx, AiStore.AVATAR_ME) }
    }

    /** 打开某个角色名下的一段会话；没给就挑最近的一段，一段都没有就新建 */
    suspend fun openSessionOf(pid: String, prefer: String = "") {
        // ★ 这一行是必须的：activeId 是界面状态（顶栏名字、切换条高亮、新建会话挂到谁名下
        //   都靠它）。原来这里只写了 prefs.activeId，忘了给 activeId 赋值，
        //   于是它一直是空串 —— 「新对话」就把会话挂在了 personaId="" 名下，
        //   而列表是按角色 id 分组的，那一段就永远看不见。
        activeId = pid
        // 顺手把历史上挂空的会话认领回来（见 AiStore.adoptOrphanSessions 的说明）
        if (pid.isNotBlank()) {
            withContext(Dispatchers.IO) { AiStore.adoptOrphanSessions(ctx, pid) }
        }
        val list = withContext(Dispatchers.IO) {
            AiStore.loadSessions(ctx).filter { it.personaId == pid }.sortedByDescending { it.updatedAt }
        }
        val pick = list.firstOrNull { it.id == prefer } ?: list.firstOrNull()
        val s = pick ?: withContext(Dispatchers.IO) { AiStore.createSession(ctx, pid) }
        sessionId = s.id
        prefs = prefs.copy(activeId = pid, activeSession = s.id)
        withContext(Dispatchers.IO) { AiStore.savePrefs(ctx, prefs) }
        val loaded = withContext(Dispatchers.IO) { AiStore.loadSessionMsgs(ctx, s.id) }
            // 上一次生成到一半被杀掉留下的空壳（有 role 没正文）：读的时候直接丢掉，
            // 否则它会在界面上永远显示一个「…」气泡
            .filterNot { it.role == "assistant" && it.content.isBlank() }
            // 主动互动自动带过去的那一条：**对话里不该出现**（只留她的回答）。
            // 新版已经不再写它了，这里是为旧版本已经写进去的那几条兜底。
            .filterNot { it.role == "user" && it.kind == "proactive" }
        msgs.clear()
        msgs.addAll(loaded)
        AiBus.currentPersonaId = pid
        AiBus.currentSessionId = s.id
        memories = withContext(Dispatchers.IO) { AiStore.memoryBlock(ctx, pid) }
        // 这一段的「记忆进度」跟着会话走（不是跟着这一次打开走）
        memMark = withContext(Dispatchers.IO) {
            AiStore.loadSessions(ctx).firstOrNull { it.id == s.id }?.memMark ?: 0
        }
        loadAvatars(pid)
        // 顺手把天气刷新一下（超过 30 分钟才算旧），失败就还是老数据。
        // ⚠️ 状态赋值要留在主线程上：withContext 里那个块跑在 IO 线程，
        //    在里面写 Compose 状态是错的（快照系统不保证，可能丢更新）。
        weatherLine = withContext(Dispatchers.IO) {
            try {
                AiDaily.refreshIfStale(ctx)
            } catch (e: Exception) {
            }
            AiDaily.weatherLine(ctx)
        }
        // 这一段的正文与记忆进度都就位了：通知下面那个 LaunchedEffect 补一次长期记忆
        loadedTick++
    }

    /** 从盘上重新读配置、人设卡与当前会话 */
    suspend fun reloadAll() {
        cfg = withContext(Dispatchers.IO) { AiStore.loadCfg(ctx) }
        val pf = withContext(Dispatchers.IO) { AiStore.loadPrefs(ctx) }
        val ps = withContext(Dispatchers.IO) { AiStore.ensure(ctx); AiStore.allPersonas(ctx) }
        prefs = pf
        personas.clear()
        personas.addAll(ps)
        val want = if (activeId.isNotBlank()) activeId else pf.activeId
        val pid = if (ps.any { it.id == want }) want else ps.firstOrNull()?.id.orEmpty()
        // 当前这段还在不在？（可能刚被删掉）
        val cur = withContext(Dispatchers.IO) { AiStore.loadSessions(ctx) }
            .firstOrNull { it.id == sessionId && it.personaId == pid }
        openSessionOf(pid, cur?.id ?: pf.activeSession)
    }

    // 两个 LaunchedEffect 挪到了下面（sendText / maybeSummarize 定义之后）——
    // Kotlin 的局部函数必须先声明后使用，写在前面会编译不过。

    /**
     * 把当前这一段写盘。
     * ⚠️ 必须能「等它写完」——新建对话 / 切换角色之前都要先存好上一段：
     *    存索引和改索引是两件事，异步并行会互相覆盖（详见 AiStore.updateIndex 那段注释）。
     */
    suspend fun saveNow() {
        val snapshot = msgs.toList()
        val sid = sessionId
        if (sid.isBlank()) return
        withContext(Dispatchers.IO) {
            AiStore.saveSessionMsgs(ctx, sid, snapshot)
            AiStore.touchSession(ctx, sid, snapshot)
        }
    }

    fun persist() {
        scope.launch { saveNow() }
    }

    fun switchPersona(id: String) {
        if (id == activeId || busy) return
        scope.launch { saveNow(); openSessionOf(id) }
    }

    fun newSession() {
        if (busy) return
        scope.launch {
            saveNow()
            val pid = activeId
            val s = withContext(Dispatchers.IO) { AiStore.createSession(ctx, pid) }
            sessionId = s.id
            prefs = prefs.copy(activeSession = s.id)
            withContext(Dispatchers.IO) { AiStore.savePrefs(ctx, prefs) }
            msgs.clear()
            // ★ 新的一段：记忆进度必须跟着归零。
            //   不归零的话它还是上一段的值（可能几十条），而新会话只有几条，
            //   total - memMark 是负数 —— 新对话里的自动长期记忆就永远不会触发。
            memMark = 0
            err = ""
            AiBus.currentSessionId = s.id
        }
    }

    fun stop() {
        job?.cancel()
        job = null
        busy = false
    }

    /**
     * 攒够一定轮数就让她自己总结一次长期记忆。
     *
     * · 单独发一次请求（低温度、短输出），失败就算了，绝不影响正在进行的对话；
     * · 【每攒够 8 条消息】做一次，比的是这一段的**总条数**减去上次总结时的条数 ——
     *   进度存在会话索引里，所以「分几次聊满 8 条」和「一口气聊满 8 条」是一样的；
     * · 总结结果存到 ai/memory/<人设 id>.json，跨会话共用 —— 这正是「记得住」的关键：
     *   换一段对话、甚至换一天再聊，她也还记得你之前说过的事。
     */
    fun maybeSummarize() {
        val pid = activeId
        if (pid.isBlank() || summarizing || !cfg.ready()) return
        if (sessionId.isBlank()) return
        val snap = msgs.toList()
        val total = snap.count { it.content.isNotBlank() }
        if (total - memMark < 8) return
        val p = curPersona() ?: return
        // AI 助手不写长期记忆 —— 它是工具，不需要「记住你」
        if (p.assistant) return
        val c = cfg
        val sid = sessionId
        // 先推进进度再发请求：无论成败都只算一次，接口坏了也不会每轮都白调一次
        memMark = total
        summarizing = true
        scope.launch {
            try {
                withContext(Dispatchers.IO) { AiStore.markSessionMem(ctx, sid, total) }
                val transcript = snap.filter { it.content.isNotBlank() }.takeLast(20)
                    .joinToString("\n") { m ->
                        val who = if (m.role == "assistant") p.name.ifBlank { p.pronoun() } else "你"
                        who + "：" + m.content.replace("\n", " ").take(300)
                    }
                val got = AiClient.summarize(c, p.name.ifBlank { p.pronoun() }, transcript)
                if (got.isNotEmpty()) {
                    withContext(Dispatchers.IO) { AiStore.appendMemories(ctx, pid, got) }
                    // 攒得太多就自动精简一遍：合并重复、丢掉没用的。
                    // 整理完没变少（压不动）就保持原样，宁可多几条也不丢东西。
                    val all = withContext(Dispatchers.IO) { AiStore.loadMemories(ctx, pid) }
                    if (all.size >= AiStore.MEM_CONSOLIDATE_AT) {
                        val slim = withContext(Dispatchers.IO) {
                            AiClient.consolidate(c, p.name.ifBlank { p.pronoun() }, all)
                        }
                        if (slim.isNotEmpty() && slim.size < all.size) {
                            withContext(Dispatchers.IO) { AiStore.saveMemories(ctx, pid, slim) }
                        }
                    }
                    // 只有还停在原来那一段、还是原来那个角色时才刷新界面上的那份
                    if (sessionId == sid && activeId == pid) {
                        memories = withContext(Dispatchers.IO) { AiStore.memoryBlock(ctx, pid) }
                    }
                    AiBus.memTick++
                }
            } catch (e: Exception) {
            } finally {
                summarizing = false
            }
        }
    }

    /**
     * 她主动开口。
     *
     * 规则见 AiGreet：超过 6 小时没说话就问候一句，没人理就 12/24/48/96 小时地翻倍，
     * 一共 5 次之后不再打扰；用户一回应就归零。
     * 「上一次没被搭理」会记进她的长期记忆。
     *
     * **必须在 IO 之外调用**（内部自己切线程）。
     */
    suspend fun maybeGreet() {
        if (busy || summarizing) return
        if (!cfg.ready()) return
        // ⚠️ isDue 要读盘（Store.root 会 mkdirs、还要 readText），而这里跑在主线程上
        //    （LaunchedEffect 每分钟一次），必须自己切到 IO —— 别让它卡界面
        if (!withContext(Dispatchers.IO) { AiGreet.isDue(ctx, prefs.sleepFromMin, prefs.sleepToMin) }) return
        // 挑一段：所有对话统一度量，只看"最近聊过的那一段"
        val target = withContext(Dispatchers.IO) {
            AiStore.loadSessions(ctx).filter { it.count > 0 }.maxByOrNull { it.updatedAt }
        } ?: return
        val p = withContext(Dispatchers.IO) {
            AiStore.allPersonas(ctx).firstOrNull { it.id == target.personaId }
        } ?: return
        // 「AI 助手」不主动开口：它是一把工具，不该自己蹦出来找话说
        if (p.assistant) return

        // 先记账（含"他没回我"的长期记忆），再生成 —— 万一生成失败也不会反复重试
        withContext(Dispatchers.IO) { AiGreet.markGreeted(ctx, p.id) }

        val hist = withContext(Dispatchers.IO) { AiStore.loadSessionMsgs(ctx, target.id) }
        val mem = withContext(Dispatchers.IO) { AiStore.memoryBlock(ctx, p.id) }
        val env = AiDaily.block(prefs.daily, weatherLine, System.currentTimeMillis(), prefs.sleepFromMin, prefs.sleepToMin)
        val sys = p.systemPrompt(
            cfg.extraSystem +
                    (if (env.isBlank()) "" else "\n" + env) +
                    (if (mem.isBlank()) "" else "\n" + mem) +
                    AiGreet.instruction(p.name)
        )
        val line = try {
            val sb = StringBuilder()
            AiClient.chat(
                cfg.copy(stream = false, maxTokens = 80, historyRounds = 6),
                sys, hist
            ).collect { sb.append(it) }
            sb.toString().trim().take(200)
        } catch (e: Exception) {
            ""
        }
        // 问候只要一句：万一模型还是按「分条」发了好几条，就只留第一条
        val text = AiReply.parts(line).firstOrNull()?.take(80).orEmpty()
            .ifBlank { AiGreet.fallbackLine() }

        // ⚠️ 不能写 `hist + 问候`：hist 是发请求**之前**读的，而这一趟模型调用要好几秒。
        //    这几秒里用户完全可能自己发了一条 —— 那份过期历史一写回去就把用户刚发的
        //    和模型的回复一起抹掉了。所以统一交给「锁里重读再追加」。
        withContext(Dispatchers.IO) {
            AiStore.appendSessionMsg(ctx, target.id, AiChatMsg("assistant", text))
        }
        if (target.id == sessionId && !busy) {
            // 正看着这一段：直接出现在对话里
            msgs.add(AiChatMsg("assistant", text))
        } else if (target.id != sessionId) {
            // 在别的一段：给一条提示，点一下跳过去
            AiBus.greetNotice = "「" + p.name.ifBlank { p.pronoun() } + "」在一段对话里先开口了"
            AiBus.greetNoticeSession = target.id
        }
    }

    /** 发一条指定的内容（输入框发送、「分享到对话」、发图共用这条路） */
    fun sendText(
        raw: String, kind: String = "", label: String = "", img: String = "",
        /** 「重新生成」用：不新增用户消息，直接针对现有历史再问一次 */
        again: Boolean = false,
        /** 「继续生成」用：新写出来的字接在这段已有正文后面 */
        prefix: String = "",
        /** 「继续生成」用：额外塞给模型的一句，免得它看到最后一条是自己、就从头再讲一遍 */
        nudge: String = "",
        /**
         * 「继续生成」专用：把新写出来的接在**这一条**气泡后面（-1 = 另起一条）。
         *
         * ⚠️ 这个必须显式传，不能靠「末尾那条是不是助手」去猜：
         *    「重新生成」会把某条连同它后面的都丢掉，若被丢掉的那条后面
         *    恰好还跟着一条助手消息（多段回复就是连着两条），末尾看起来也是助手 ——
         *    一猜就会把「重新生成」误当成「继续」，变成往后追加而不是重写。
         */
        appendTo: Int = -1
    ) {

        // 带了文件：正文 = 用户打的字 + 每个文件抽出来的正文；气泡改按文件卡片显示。
        // 只有「正常发送」这条路会捎带文件 —— 分享记录、发贴纸那两条不该被顺手带上。
        val files = if (kind.isEmpty() && img.isBlank()) pendingFiles else emptyList()
        val text = if (files.isEmpty()) raw.trim()
        else (raw.trim() + files.joinToString("") { DocText.asMessageTail(it) }).trim()
        val msgKind = if (files.isEmpty()) kind else "file"
        val msgLabel = if (files.isEmpty()) label else files.joinToString("、") { it.title }
        // 带图的消息允许没有文字（表情包就是这种）
        //
        // 「重新生成」没有新内容要发，所以那两条检查要分开写：它只需要「不忙」。
        if (again) {
            if (busy) return
        } else if ((text.isEmpty() && img.isBlank()) || busy) return
        val p = curPersona()
        if (p == null) {
            err = "还没有人设卡，去「设置」里建一张"
            return
        }
        if (!cfg.ready()) {
            err = "还没配置接口。点右上角「设置」，填 Base URL / API Key / 模型名"
            return
        }
        if (sessionId.isBlank()) {
            err = "会话还没准备好，稍等一下再发"
            return
        }

        if (!again) {
            input = ""
            pendingFiles = emptyList()
        }
        err = if (img.isNotBlank() && !cfg.vision) {
            "这条带图片，但「能看图」是关着的 —— 她只会收到一句「对方发来一张图片」。" +
                    "要让她真看到，去「设置 → 接口」把「能看图」打开（只有多模态模型支持）。"
        } else ""
        // 用户主动说话了：主动问候的计数与间隔归零
        // 用户主动说话了：主动问候的计数与间隔归零（「重新生成」不算他开口）
        if (!again) scope.launch { withContext(Dispatchers.IO) { AiGreet.onUserSpoke(ctx) } }
        // ★ 这一轮属于哪一段对话，在发出去之前就定死。
        //   收尾时绝不能再去读 sessionId —— 用户完全可能在这中间切了会话/人设/新建，
        //   那样整份消息会被写进**另一段**对话（串台），甚至把新会话覆盖掉。
        val sid = sessionId
        if (!again) {
            msgs.add(AiChatMsg(role = "user", content = text, kind = msgKind, label = msgLabel, img = img,
                head = if (files.isEmpty()) "" else raw.trim()))
        }
        // 「继续生成」不另起一条气泡：上一段本来就在最后一条里，新内容接在它后面就行。
        //
        // ⚠️ 这里原来是「新建一条占位气泡、内容先摆上 prefix」，而下面的 paint() 里
        //    又拼了一次 prefix —— 同一段文字被摆了两遍，看着就是「把上次的结果又输出了一遍」，
        //    既占地方又没必要。
        val continuing = appendTo in msgs.indices
        val tail = if (continuing) msgs[appendTo].content else ""
        val idx = if (continuing) appendTo else msgs.size
        if (!continuing) msgs.add(AiChatMsg("assistant", prefix))

        busy = true
        persist()
        val sentAt = System.currentTimeMillis()
        // 节奏两条在开跑之前取一次：跑到一半用户去改设置，这一轮不跟着变，免得忽快忽慢
        //
        // 「AI 助手」这张卡是**工具**，不是那个会撒娇的人：出手前不停顿、不分条、不打字机。
        // 它要的是「问完立刻看到答案」，装出来的思考停顿在这里只是碍事。
        // 所以这三条对它一律取 0 —— 后面对话框里的设置再多，也影响不到它。
        val toolMode = p.assistant
        val replyDelay = if (toolMode) 0 else prefs.replyDelayMs
        val partDelay = if (toolMode) 0 else prefs.partDelayMs
        val typeSpeed = if (toolMode) 0 else prefs.typeSpeedMs

        // 设定 = 人设 + 全局附加指令 + 【现在的情况】（时间/节日/天气）+ 她自己的长期记忆
        // 「AI 助手」那张卡是纯工具：不发人设、不发日常信息、不发长期记忆，
        // 只留全局附加指令（用户自己写的要求还是照发）。
        val env = if (p.assistant) "" else
            AiDaily.block(prefs.daily, weatherLine, System.currentTimeMillis(), prefs.sleepFromMin, prefs.sleepToMin)
        val sys = p.systemPrompt(
            cfg.extraSystem +
                    (if (env.isBlank()) "" else "\n" + env) +
                    (if (p.assistant || memories.isBlank()) "" else "\n" + memories)
        )
        // 个别兼容端点不接受以 assistant 开头的历史，这里把开头那条剔掉
        // 续写时要把「正在续写的那一条」也发给模型，否则它看不到自己写到哪了。
        val history = msgs.take(if (continuing) idx + 1 else idx)
            .filter { !it.failed && (it.content.isNotBlank() || it.img.isNotBlank()) }
            .dropWhile { it.role == "assistant" } +
                (if (nudge.isBlank()) emptyList() else listOf(AiChatMsg("user", nudge)))

        // 图片在发出去的那一刻才读盘、转 base64（历史会被裁剪，提前全转是白费力气）
        val imgData: (String) -> String? = { rel ->
            AiStore.readMsgImage(ctx, rel)?.let { b ->
                AiClient.dataUrl(b, if (rel.endsWith(".png", true)) "image/png" else "image/jpeg")
            }
        }

        // 发出时的样子（含刚塞进去的占位）。万一流式途中用户切走了会话，
        // 收尾就靠这份副本 + 累积到的文字把这一轮写回**它自己**那一段。
        val base = msgs.toList()

        job = scope.launch {
            var acc = ""
            var n = 0
            // 第一个字露出来没有；「出手前停一下」就靠它计时
            var firstShown = false
            // 打字机：已经显示出来的**可见字数**（分隔符不算）
            var revealed = 0
            // 已经露出来的条数；「分条之间停一下」靠它认出新的一条
            var shownParts = 1
            // 出错时那句人话（已经抹掉 Key）。会话中途被切走时，最后要把它写回**原来那一段**
            var failure = ""

            /** 按「已经显示到第几个可见字」把气泡刷出来 */
            fun paint(visible: Int, full: List<String>) {
                var left = visible
                val vis = ArrayList<String>()
                for (one in full) {
                    if (left <= 0) break
                    vis.add(if (one.length <= left) one else one.take(left))
                    left -= one.length
                }
                if (vis.isEmpty()) vis.add("")
                while (msgs.size < idx + vis.size) msgs.add(AiChatMsg("assistant", ""))
                vis.forEachIndexed { k, s ->
                    if (idx + k < msgs.size) msgs[idx + k] = msgs[idx + k].copy(content = if (k == 0 && tail.isNotBlank()) tail + s else s)
                }
            }

            try {
                // 思维链：单独攒一份，边收边写进这一条消息。
                // 只影响界面 —— 它不进发给模型的历史，也不会被当成她的话。
                var thinkAcc = ""
                // 深度思考只给「AI 助手」：别的人设卡就算开关开着也不开 ——
                // 那是让模型自言自语，对「她」来说只会变慢变怪。
                AiClient.chat(cfg, sys, history, imgData, deepThink = toolMode && cfg.thinking, onThink = {


                    thinkAcc += it
                    if (sessionId == sid && idx < msgs.size) {
                        msgs[idx] = msgs[idx].copy(think = thinkAcc)
                    }
                }).collect { delta ->
                    acc += delta

                    // 只在还停在这一段时才动界面上的列表
                    if (sessionId == sid && idx <= msgs.size) {
                        // ① 出手前停一下：从按下发送到看见第一个字，至少隔这么久 —— 秒回不像人
                        if (!firstShown) {
                            val wait = replyDelay - (System.currentTimeMillis() - sentAt)
                            if (wait > 0) kotlinx.coroutines.delay(wait)
                            firstShown = true
                        }
                        // ② 分条：模型用单独一行的 %%% 或空行把「一条一条」隔开，
                        //    这里把它摊成相邻的多个气泡 —— 前几段已经说完，最后一段还在往下写。
                        // 「AI 助手」不分条：整段就是一个气泡（分隔符本身也抹掉，
                        //   否则模型照着示例写的 %%% 会原样露在正文里）。
                        val full = if (toolMode) AiReply.oneBlock(acc) else AiReply.liveParts(acc)
                        val total = full.sumOf { it.length }
                        // ③ 打字机：收到的先攒在 acc 里，界面按设定的速度一个字一个字往外走。
                        //    落后太多就直接追上 —— 否则一条长回复要一个字一个字等上好几分钟。
                        while (revealed < total) {
                            val backlog = total - revealed
                            val step: Int
                            val waitMs: Long
                            when {
                                typeSpeed <= 0 -> {
                                    step = backlog; waitMs = 0
                                }
                                backlog > 400 -> {
                                    step = backlog; waitMs = 0
                                }
                                backlog > 120 -> {
                                    step = 4; waitMs = typeSpeed.toLong()
                                }
                                else -> {
                                    step = 1; waitMs = typeSpeed.toLong()
                                }
                            }
                            revealed = minOf(total, revealed + step)
                            if (full.size > shownParts) {
                                shownParts = full.size
                                if (partDelay > 0) kotlinx.coroutines.delay(partDelay.toLong())
                            }
                            paint(revealed, full)
                            if (waitMs > 0) kotlinx.coroutines.delay(waitMs)
                        }
                    }
                    n++
                    if (n % 6 == 0 && msgs.size > 1 && sessionId == sid) {
                        try {
                            listState.scrollToItem(msgs.lastIndex, SCROLL_TO_END)
                        } catch (e: Exception) {
                        }
                    }
                }
                if (sessionId == sid && idx < msgs.size && msgs[idx].content.isBlank()) {
                    msgs[idx] = msgs[idx].copy(content = "（没有收到内容）", failed = true)
                }
            } catch (e: kotlinx.coroutines.CancellationException) {
                if (sessionId == sid && idx < msgs.size && msgs[idx].content.isBlank()) {
                    msgs[idx] = msgs[idx].copy(content = "（已停止）")
                }
                throw e
            } catch (e: Exception) {
                // ⚠️ 有些服务商会把请求头（含 Key）回显在错误响应体里，而这条文本
                //    是要写进消息、进而进导出物（zip / docx / 镜像都在公共目录）的 ——
                //    所以落盘前先抹一遍。
                val m = AiStore.redact(e.message ?: e.javaClass.simpleName, cfg.apiKey)
                failure = m
                if (sessionId == sid) err = m
                if (sessionId == sid && idx < msgs.size && msgs[idx].content.isBlank()) {
                    msgs[idx] = msgs[idx].copy(content = m, failed = true)
                } else if (acc.isBlank()) {
                    acc = m
                }
            } finally {
                // ⚠️ 只清理「自己那一份」状态。
                //    「停止」与「发送」是同一个按钮：用户点了停止、马上又发一条时，
                //    job 已经指向新的那个协程 —— 这里若无条件置空，
                //    新的那条就再也停不下来，busy 也会被错误地清掉。
                val me = kotlinx.coroutines.currentCoroutineContext()[Job]
                if (job == null || job === me) {
                    job = null
                    busy = false
                }
                // 停在原地就用界面上的真身；已经切走了就用副本 + 累积文字，
                // 总之只写回 sid 这一段，绝不碰当前显示的那一段
                val snapshot: List<AiChatMsg> = if (sessionId == sid) {
                    msgs.toList()
                } else {
                    // 已经切走了，界面上那份不能动：把这一轮收到的整段文字按分隔符
                    // 在副本上重建成一条一条（原来是整段塞进一个气泡）
                    val l = base.toMutableList()
                    // base 的尾巴上是出发时占的那个空位，先摘掉，免得留下一个空气泡
                    while (l.size > idx) l.removeAt(l.lastIndex)
                    val parts = AiReply.parts(acc)
                    if (parts.isEmpty()) {
                        val one = failure.ifBlank { if (acc.isBlank()) "（已停止）" else acc }
                        l.add(AiChatMsg("assistant", one, failed = failure.isNotBlank()))
                    } else {
                        parts.forEach { l.add(AiChatMsg("assistant", it)) }
                    }
                    l
                }
                // NonCancellable：用户点「停止」时协程已被取消，
                // 普通 withContext 会直接抛 CancellationException，刚收到的半截回复就白丢了
                try {
                    withContext(NonCancellable + Dispatchers.IO) {
                        // 这一段如果在生成期间被删了，就别把它写回来
                        //（否则「已删除」的对话会因收尾写盘而复活，还会进每日镜像）
                        if (AiStore.sessionExists(ctx, sid)) {
                            AiStore.saveSessionMsgs(ctx, sid, snapshot)
                            AiStore.touchSession(ctx, sid, snapshot)
                        }
                    }
                } catch (e: Exception) {
                }
                // busy 放在写盘之后：上面那个 LaunchedEffect 拿 busy 当钥匙，
                // 先置 false 会让它在写盘完成前抢跑，把刚聊完的这一轮读丢。
                // 同样只在自己还「在位」时才清 —— 否则会把新一轮的 busy 抹掉。
                if (job === me) busy = false
                // 聊完再看要不要总结一次长期记忆（后台跑，不挡着用户）
                maybeSummarize()
                // 顺手把天气刷一下（超过 30 分钟才算旧）；失败无所谓，下次再说
                scope.launch {
                    try {
                        withContext(Dispatchers.IO) { AiDaily.refreshIfStale(ctx) }
                        weatherLine = withContext(Dispatchers.IO) { AiDaily.weatherLine(ctx) }
                    } catch (e: Exception) {
                    }
                }
            }
        }
    }

    /* ================= 副作用（放在函数定义之后，见上面的说明） ================= */

    LaunchedEffect(Unit) { reloadAll() }

    // 设置页 / 会话页 / 分享 动过东西之后。
    // ⚠️ busy 也当钥匙：正在生成时先不处理，等这一轮结束（busy 变回 false）会自动补上 ——
    //    否则那一下改动就被悄悄丢掉了。
    // ⚠️ 生成结束时先写完盘再置 busy=false（见 finally 里的顺序），
    //    这样这里补处理时读到的一定是最新的。
    LaunchedEffect(AiBus.tick, busy, active) {
        if (busy) return@LaunchedEffect

        // ⚠️ 分享必须按**待办**处理，不能按**事件**处理 —— 这正是「偶尔分享不过去」的根：
        //
        //    handledTick 的初值是「这一页组合那一刻的 AiBus.tick」。而分享的流程是：
        //    先在别处把 shareText 填好、tick 加一，再 goto(2) 切到这一页。
        //    如果这一页是在 tick 加一**之后**才第一次组合出来的（冷启动，或者 pager
        //    还没预热到这一页），handledTick 的初值就恰好等于那个新 tick ——
        //    下面那句 tick 判断直接返回，整条分享被丢掉；
        //    而 onPicked 里已经 AiShare.clear() 过了，源头也没了，找不回来。
        //    等页面预热好（warmTabs）之后 handledTick 才变回旧值，于是又能分享了 ——
        //    表现出来就是「偶尔好、偶尔坏，过一阵子又好了」。
        //
        //    所以这里只看「shareText 还挂着没有」：挂着就一定处理，与 tick 无关。
        val pid = AiBus.currentPersonaId.ifBlank { activeId }


        // ---- 优先处理「分享到对话」：切到目标那一段，然后把内容当作一条发言发出去 ----
        if (AiBus.shareText.isNotBlank()) {
            val text = AiBus.shareText
            val label = AiBus.shareLabel
            val target = AiBus.shareSessionId
            val tpid = AiBus.shareToPersona.ifBlank { pid }
            AiBus.shareText = ""
            AiBus.shareLabel = ""
            AiBus.shareSessionId = ""
            AiBus.shareToPersona = ""
            if (tpid.isNotBlank()) {
                withContext(Dispatchers.IO) { AiStore.adoptOrphanSessions(ctx, tpid) }
            }
            if (target.isNotBlank()) openSessionOf(tpid, target) else reloadAll()
            // 界面上按小卡片显示（kind = "share"），正文照常进上下文
            sendText(text, kind = "share", label = label)
            return@LaunchedEffect
        }

        // tick 只用来触发下面那些真正的「事件」（新建 / 打开某一段 / 刷新）。
        // ⚠️ 它必须排在分享之后 —— 理由见上面那段注释。
        if (AiBus.tick == 0 || AiBus.tick == handledTick) return@LaunchedEffect
        handledTick = AiBus.tick


        when {
            AiBus.requestNew > handledNew -> {
                handledNew = AiBus.requestNew
                AiBus.openSessionId = ""
                val s = withContext(Dispatchers.IO) { AiStore.createSession(ctx, pid) }
                sessionId = s.id
                prefs = prefs.copy(activeId = pid, activeSession = s.id)
                withContext(Dispatchers.IO) { AiStore.savePrefs(ctx, prefs) }
                msgs.clear()
                // 与新对话同理：记忆进度跟着新的一段归零（见 newSession 里的说明）
                memMark = 0
                AiBus.currentSessionId = s.id
            }
            AiBus.openSessionId.isNotBlank() -> {
                val want = AiBus.openSessionId
                AiBus.openSessionId = ""
                // 查一下这一段到底属于哪个角色：她主动开口的那一段，可能不是现在看的这个角色
                val owner = withContext(Dispatchers.IO) {
                    AiStore.loadSessions(ctx).firstOrNull { it.id == want }?.personaId
                }
                reloadAll()
                when {
                    owner.isNullOrBlank() -> if (want != sessionId) {
                        openSessionOf(activeId.ifBlank { pid }, want)
                    }
                    else -> openSessionOf(owner, want)
                }
            }
            else -> reloadAll()
        }
    }

    /**
     * 未读的清除**单独用一个 effect**，不再挂在上面那个大 effect 后面。
     *
     * 为什么原来不生效：上面那个的 key 是 (tick, busy, active)，切到「对话」栏时
     * `active` 确实变了、effect 也重跑了，但那时 `AiBus.currentSessionId` 还没被赋值
     * （它只在新建会话或主动互动时才写），于是那一段的未读一次都没被清掉。
     * 这里直接用这一页**正在显示的那一段** id 做 key，稳。
     */
    LaunchedEffect(active, sessionId, AiBus.unreadTick) {
        if (active && sessionId.isNotBlank()) {
            AiBus.currentSessionId = sessionId
            withContext(Dispatchers.IO) { AiProactive.clearUnread(ctx, sessionId) }
        }
    }

    // 她主动开口的检查：打开就查一次（补上关着应用时错过的），之后每分钟一次。
    // 关着应用不会唤醒 —— 本应用没有常驻后台服务，也不想为这件事多要权限。
    LaunchedEffect(Unit) {
        while (true) {
            try {
                maybeGreet()
            } catch (e: Exception) {
            }
            kotlinx.coroutines.delay(60_000)
        }
    }

    // 消息条数变化时滚到底（流式过程中由收集循环里的节流滚动负责）。
    // active 也当钥匙：不在这一栏时滚不动，回来时要补一次，否则新消息看不见。
    LaunchedEffect(msgs.size, busy, active) {
        if (msgs.isNotEmpty() && active) {
            try {
                // 瞬时跳到底，不要动画：从别的页面/人设切进来时，
                // 动画会先滑几帧再到底，看着就是「页面进来之后还在动」。
                listState.scrollToItem(msgs.lastIndex, SCROLL_TO_END)
            } catch (e: Exception) {
            }
        }
    }

    // 键盘弹出 / 收起时也补一次滚到底。
    //
    // 为什么必须单独来一条：键盘压小的是**可视区**，消息条数一条都没变 ——
    // 上面那条 LaunchedEffect 的钥匙是 msgs.size / busy / active，一个都不会变，
    // 于是列表停在原地，最新那条被键盘顶到了看不见的地方，用户只能自己往下滑。
    // 这里换成拿键盘高度当钥匙，一弹一收都补一次。
    //
    // 用 scrollToItem（瞬时）而不是 animate：键盘本身正在做位移动画，
    // 两个动画叠在一起反而会打架。
    // 键盘弹起 / 收起时，视口高度是**连续**变化的（系统在放动画）。
    //
    // 之前这里是「等 80 毫秒 → 瞬移到底 → 再等 180 毫秒 → 再瞬移一次」：
    // 那 80 毫秒里列表还停在旧位置、底下露出一块空白，然后突然跳一下 ——
    // 就是你看到的那几帧割裂感。固定等待本质上是在猜系统动画什么时候结束。
    //
    // 现在改成盯着**视口高度**：只要它变了就立刻贴一次底。
    // 键盘动画的每一帧都把内容顶回底部，看起来就是气泡一直黏在输入框上方，
    // 而不是「先露白、再跳一下」。也不再有「猜多久」的问题。
    //
    // 用不着担心和用户自己滑动打架：手动滑动不会改变视口高度，这个流不会触发。
    // ⚠️ active 必须当钥匙，不能只写 listState。
    //
    //    active 是普通参数（不是 State），在协程里读到的是**效果启动那一刻**的值。
    //    而这一页会被 pager 预组合 —— 它往往是在「还不在这一栏」（active = false）
    //    的时候就把这个效果启起来了；之后切回来时 listState 没变，
    //    LaunchedEffect 不会重启，于是那个 false 被一直用下去，永远不滚。
    //
    //    这正是「从别的人设卡转回来会贴、在本对话里弹键盘不贴」的原因：
    //    转回来时是**另一条**效果（钥匙里带 active）重启了它、顺手贴了一次；
    //    而弹键盘不改 active，两条都指望不上。
    //
    //    把 active 也当钥匙：false → true 时效果重启，从头拿到新的 active，
    //    而且 snapshotFlow 会立刻重发一次当前视口高度 —— 等于进来就贴一次底。
    LaunchedEffect(listState, active) {

        snapshotFlow { listState.layoutInfo.viewportSize.height }
            .collect { h ->
                if (h > 0 && msgs.isNotEmpty() && active) {
                    try {
                        listState.scrollToItem(msgs.lastIndex, SCROLL_TO_END)
                    } catch (e: Exception) {
                    }
                }
            }
    }



    // 余额：进这一页拉一次，之后每 60 秒刷一次 —— 想一边聊一边看着消费。
    //
    // 只在「这一家确实有余额接口」并且「接口配置齐全」时才查。
    // 拉失败时**保留上一次的值**（网络抖一下而已，没必要把数字抹掉）；
    // 但换到一家没有余额接口的服务商时，要把旧值清掉 —— 否则会显示别人家的余额。
    LaunchedEffect(active, cfg.provider, cfg.baseUrl, cfg.apiKey) {
        if (!active) return@LaunchedEffect
        val canQuery = (AI_PROVIDERS.firstOrNull { it.id == cfg.provider }?.balanceKind ?: "").isNotBlank()
        if (!canQuery) {
            balance = null
            return@LaunchedEffect
        }
        while (true) {
            val b = withContext(Dispatchers.IO) { AiClient.fetchBalance(cfg) }
            if (b != null) balance = b
            kotlinx.coroutines.delay(60_000)
        }
    }
    /* ---------------- 全屏展开某一条（给「框选」用） ---------------- */


    // 「框选」为什么改成全屏展开，而不是在气泡里就地选：
    //   就地选要把这一条包进 SelectionContainer，那样**长按**就变成选字了，
    //   和聊天列表本来就有的「长按多选」正好撞在一起 —— 两个都用不顺手。
    //   现在：点「框选」= 把这一条摊开铺满整屏，在那儿慢慢选；
    //         长按依旧是列表的多选。两边互不打扰。
    // 用 Dialog 而不是在本页叠一层：Dialog 是独立窗口，能真正铺满整屏，
    // 而且**自带返回键处理** —— 系统返回 / 边缘返回就是关掉它。
    var expanded by remember { mutableStateOf<Int?>(null) }

    /** 「继续生成」：这一条留着，让模型接着往下写 */
    fun continueAt(i: Int) {
        if (busy || i !in msgs.indices) return
        val k = msgs[i].content
        selecting = false
        picked.clear()
        while (msgs.size > i + 1) msgs.removeAt(msgs.size - 1)
        sendText("", again = true, prefix = k, nudge = CONTINUE_NUDGE, appendTo = i)
    }

    /** 「重新生成」：把这一条连同它后面的一起丢掉，按同一段历史重问一次 */
    fun regenerateAt(i: Int) {
        if (busy || i !in msgs.indices) return
        selecting = false
        picked.clear()
        while (msgs.size > i) msgs.removeAt(msgs.size - 1)
        persist()
        sendText("", again = true)
    }

    expanded?.let { ex ->
        val em = msgs.getOrNull(ex)
        if (em == null) {
            expanded = null
        } else {
            Dialog(
                onDismissRequest = { expanded = null },
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(PanelBg)
                        .padding(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("框选文本", color = TextMain, fontSize = 14.sp, modifier = Modifier.weight(1f))
                        Text("按住可以选一段", color = TextFaint, fontSize = 11.sp)
                        Spacer(Modifier.width(8.dp))
                        TextButton(onClick = { expanded = null }) {
                            Text("关闭", color = TextMuted, fontSize = 12.sp)
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    SelectionContainer(
                        modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())
                    ) {
                        Text(em.content, color = TextMain, fontSize = 14.sp)
                    }
                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        BubbleAct("复制") {
                            val cm = ctx.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            cm.setPrimaryClip(ClipData.newPlainText("Sympnoia 对话", em.content))
                        }
                        BubbleAct("继续") { expanded = null; continueAt(ex) }
                        BubbleAct("重新生成") { expanded = null; regenerateAt(ex) }
                    }
                }
            }
        }
    }




    // 刚打开一段对话时补一次长期记忆：以前攒够 8 条却没做成的，进来就补上。
    // 放在这里而不是 openSessionOf 里面 —— Kotlin 的局部函数必须先声明再使用。
    LaunchedEffect(loadedTick) {
        if (loadedTick == 0 || busy) return@LaunchedEffect
        maybeSummarize()
    }

    val p = curPersona()

    // 注意：外层（MainScreen）已经做过 imePadding，这里**不能再来一次**，
    // 否则键盘弹出时会被顶两倍高度。
    //
    // ⚠️ 外面这层 Box 是必须的：确认框 / 收藏页都是 fillMaxSize 的浮层，
    //    如果它们直接跟正文一起放进 Column，就会被排在正文**下面**（屏幕外），
    //    表现为「点了按钮什么也没发生」。放进 Box 才是叠在上面。
    Box(modifier = Modifier.fillMaxSize()) {

    Column(modifier = Modifier.fillMaxSize()) {

        /* ---------------- 顶栏：人设名（左） + 列表 / 新对话 / 设置（右） ----------------
         * 模型名不放在这里 —— 它是技术细节，在设置页里看就够了。 */
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { onOpenSessions() }
                    .padding(horizontal = 8.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 名字左边永远是「主题色小圆点」，不换成头像 ——
                // 头像是聊天气泡旁边的，这里是标识「现在是谁」。
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(p?.tint() ?: TextFaint)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    p?.name?.ifBlank { "未命名" } ?: "还没有人设卡",
                    color = TextMain, fontSize = 14.sp
                )
                Spacer(Modifier.width(4.dp))
                Text("▾", color = TextFaint, fontSize = 10.sp)
            }
            Spacer(Modifier.weight(1f))
            TextButton(onClick = { onOpenSessions() }) {
                Text("列表", color = TextMuted, fontSize = 12.sp)
            }
            TextButton(onClick = { showFavs = true }) {
                Text("收藏", color = TextMuted, fontSize = 12.sp)
            }
            TextButton(onClick = { newSession() }) {
                Text("新对话", color = TextMuted, fontSize = 12.sp)
            }
            TextButton(onClick = onOpenSettings) {
                Text("设置", color = PinkSoft, fontSize = 12.sp)
            }
        }

        // 她主动开口了，但不在这一段：给一条可点的提示
        if (AiBus.greetNotice.isNotBlank()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 2.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0x33FBC2EB))
                    .clickable {
                        val sid = AiBus.greetNoticeSession
                        AiBus.greetNotice = ""
                        AiBus.greetNoticeSession = ""
                        if (sid.isNotBlank()) {
                            AiBus.openSessionId = sid
                            AiBus.tick++
                        }
                    }
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(AiBus.greetNotice, color = PinkSoft, fontSize = 11.sp, modifier = Modifier.weight(1f))
                Text("去看看", color = TextMain, fontSize = 11.sp)
                Spacer(Modifier.width(8.dp))
                Text(
                    "✕", color = TextFaint, fontSize = 11.sp,
                    modifier = Modifier.clickable {
                        AiBus.greetNotice = ""
                        AiBus.greetNoticeSession = ""
                    }
                )
            }
        }


        Box(modifier = Modifier.weight(1f)) {
        /* ---------------- 消息区 ---------------- */
        if (msgs.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize().padding(28.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (herAvatar != null) {
                    Image(
                        bitmap = herAvatar!!,
                        contentDescription = null,
                        modifier = Modifier.size(84.dp).clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(Modifier.height(12.dp))
                }
                if (p != null && p.greeting.isNotBlank()) {
                    Text(p.greeting, color = p.tint().copy(alpha = 0.9f), fontSize = 16.sp)
                    Spacer(Modifier.height(10.dp))
                }
                Text(
                    if (cfg.ready()) "说点什么吧。"
                    else "还没配置接口：点右上角「设置」，填入服务商的 Base URL、API Key 与模型名。\n" +
                            "对话内容只保存在这台手机里，除了你选定的那个接口，不会发给任何人。",
                    color = TextFaint, fontSize = 12.sp, textAlign = TextAlign.Center
                )
            }
        } else {
            // 用 BoxWithConstraints 拿到可用宽度，算气泡的宽度上限：
            // 气泡本身按内容自适应，只在超过这个上限时才换行。
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val hasAvatar = herAvatar != null || myAvatar != null
                // 「AI 助手」是工具不是聊天对象：同样的屏幕宽度里让它一次多说一点 ——
                // 它的回复常是步骤、列表、代码，窄气泡一折行就很难读。
                val toolWide = p?.assistant == true
                val maxBubble = maxWidth *
                        (if (toolWide) 0.94f else if (hasAvatar) 0.70f else 0.78f)
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 12.dp, end = 12.dp, top = if (personas.size > 1) 44.dp else 8.dp, bottom = 84.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    itemsIndexed(msgs) { index, m ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // 多选时的空心圆点：点整行点亮 / 熄灭
                            if (selecting) {
                                Box(
                                    modifier = Modifier
                                        .padding(start = 2.dp, end = 8.dp)
                                        .size(18.dp)
                                        .clip(CircleShape)
                                        .background(if (index in picked) PinkSoft else Color.Transparent)
                                        .border(
                                            1.5.dp,
                                            if (index in picked) PinkSoft else TextFaint,
                                            CircleShape
                                        )
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .then(
                                        if (selecting) {
                                            // 多选时：点整行点亮 / 熄灭。这里的涟漪是有用的反馈，留着
                                            Modifier.clickable(
                                                interactionSource = remember { MutableInteractionSource() },
                                                indication = ripple(),
                                                onClick = { togglePick(index) }
                                            )
                                        } else {
                                            // 平时：**只能长按**。普通点击不给任何反馈 ——
                                            // 之前默认的涟漪会铺满整行、看起来就是一条「长白条」，
                                            // 而且点哪儿都亮，很容易以为点坏了。
                                            Modifier.combinedClickable(
                                                interactionSource = remember { MutableInteractionSource() },
                                                indication = null,
                                                onClick = {},
                                                onLongClick = { enterSelect(index) }
                                            )
                                        }
                                    )
                            ) {
                                if (prefs.showTime && needTimeSeparator(msgs, index)) {
                                    Text(
                                        sepText(m.ts),
                                        color = TextFaint,
                                        fontSize = 10.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
                                    )
                                }
                                AiBubble(
                                    ctx = ctx,
                                    msg = m,
                                    persona = p,
                                    busy = busy,
                                    mine = m.role != "assistant",
                                    herAvatar = herAvatar,
                                    myAvatar = myAvatar,
                                    maxWidth = maxBubble,
                                    // 「继续生成」：这一条留着，让模型接着说，新字接在原文后面。
                                    // nudge 那句只进上下文、不显示 —— 不提醒的话，模型看到最后
                                    // 一条是自己，很可能从头再讲一遍。
                                    // 三个动作都收敛到上面那两个局部函数里（continueAt / regenerateAt），
                                    // 全屏展开层用的是同一对函数 —— 一处改、两处生效。
                                    onContinue = { continueAt(index) },
                                    onRegenerate = { regenerateAt(index) },
                                    onExpand = { expanded = index }

                                )

                            }
                        }
                    }
                }
            }
        }

        // 还有其他角色时，给一条切换用的窄条：浮在消息之上，自己不铺底
        if (personas.size > 1) {
            Row(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                personas.forEach { one ->
                    val on = one.id == activeId
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (on) lerp(NightBottom, one.tint(), 0.20f) else PanelBgSoft)
                            .border(
                                1.dp,
                                if (on) one.tint().copy(alpha = 0.55f) else PurpleMain.copy(alpha = 0.2f),
                                RoundedCornerShape(20.dp)
                            )
                            .clickable { switchPersona(one.id) }
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.size(6.dp).clip(CircleShape).background(one.tint())
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            one.name.ifBlank { "未命名" },
                            color = if (on) TextMain else TextMuted,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        Column(modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth()) {
        /* ---------------- 错误提示 ---------------- */
        if (err.isNotBlank()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0x33FF6B8A))
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(err, color = Color(0xFFFFB3C1), fontSize = 11.sp, modifier = Modifier.weight(1f))
                TextButton(onClick = { err = "" }) { Text("知道了", color = TextMuted, fontSize = 11.sp) }
            }
        }

        /* ---------------- 待发送的图片 ---------------- */
        if (pendingImg.isNotBlank()) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AiMsgImage(
                    ctx = ctx, rel = pendingImg,
                    modifier = Modifier.size(46.dp).clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "这条会带上一张图片",
                    color = TextMuted, fontSize = 11.sp, modifier = Modifier.weight(1f)
                )
                TextButton(onClick = {
                    val old = pendingImg
                    pendingImg = ""
                    // 图刚存进来又被取消发送：顺手删掉，别让它烂在 media/ 里
                    if (old.isNotBlank()) {
                        scope.launch { withContext(Dispatchers.IO) { AiStore.deleteMedia(ctx, old) } }
                    }
                }) { Text("取消", color = TextMuted, fontSize = 11.sp) }
            }
        }

        /* ---------------- 待发送的文件（挂在输入框上方） ---------------- */
        if (pendingFiles.isNotEmpty()) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                pendingFiles.forEachIndexed { i, f ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("📄", fontSize = 13.sp)
                        Spacer(Modifier.width(6.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                f.title, color = TextMain, fontSize = 12.sp, maxLines = 1,
                                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                            )
                            Text(
                                f.note, color = TextFaint, fontSize = 10.sp, maxLines = 1,
                                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                            )
                        }
                        TextButton(onClick = {
                            val nl = ArrayList<DocText.Picked>(pendingFiles)
                            nl.removeAt(i)
                            pendingFiles = nl
                        }) { Text("取消", color = TextMuted, fontSize = 11.sp) }
                    }
                }
            }
        }

        /* ---------------- 表情包面板 ---------------- */
        if (showStickers) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PanelBgSoft)
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("表情包（点一下直接发）", color = TextMuted, fontSize = 11.sp, modifier = Modifier.weight(1f))
                    Text(
                        if (stickerEdit) "完成" else "管理",
                        color = PinkSoft, fontSize = 11.sp,
                        modifier = Modifier.clickable { stickerEdit = !stickerEdit }
                    )
                }
                Spacer(Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // 第一个格子永远是「从相册加一个」
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(PanelBg)
                            .border(1.dp, PurpleMain.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                            .clickable { stickerPicker.launch("image/*") },
                        contentAlignment = Alignment.Center
                    ) { Text("＋", color = PinkSoft, fontSize = 18.sp) }

                    stickers.forEach { name ->
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(PanelBg)
                                .clickable {
                                    if (stickerEdit) {
                                        scope.launch {
                                            withContext(Dispatchers.IO) { AiStore.deleteSticker(ctx, name) }
                                            stickerTick++
                                        }
                                    } else {
                                        // 表情包发出去 = 复制一份进 media/，
                                        // 这样以后从合集里删掉它，对话里那条还在
                                        scope.launch {
                                            val rel = withContext(Dispatchers.IO) {
                                                AiStore.copyStickerToMedia(ctx, name)
                                            }
                                            if (rel.isNotBlank()) {
                                                showStickers = false
                                                sendText(input, kind = "sticker", img = rel)
                                            } else {
                                                err = "这个表情包读不出来"
                                            }
                                        }
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            AiMsgImage(
                                ctx = ctx, rel = AiStore.stickerRel(name),
                                modifier = Modifier.size(56.dp).clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop,
                                placeholder = "？"
                            )
                            if (stickerEdit) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color(0x99000000)),
                                    contentAlignment = Alignment.Center
                                ) { Text("删", color = Color(0xFFFFB3C1), fontSize = 12.sp) }
                            }
                        }
                    }
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    "从相册加进来的图会按原样收藏；点「管理」可以删掉不要的。",
                    color = TextFaint, fontSize = 10.sp
                )
            }
        }

        /* ---------------- ＋ 展开的两个入口（紧挨着输入框上方） ---------------- */
        if (showAttach) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                SetChip("🖼 相册") { photoPicker.launch("image/*") }
                SetChip("📄 文件") { docPicker.launch(DocText.MIME) }
                SetChip("😀 表情包") {
                    showAttach = false
                    showStickers = true
                }
            }
        }

        /* ---------------- 多选操作条（选中时顶掉输入区） ---------------- */
        if (selecting) {
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 6.dp)) {
                Text(
                    "已选 " + picked.size + " 条" + if (busy) "（正在生成，先等这一轮结束）" else "",
                    color = TextMuted, fontSize = 11.sp
                )
                Spacer(Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    SetChip("删除", primary = true) { if (picked.isNotEmpty()) batchAsk = "del" }
                    SetChip("保存") {
                        if (picked.isNotEmpty()) {
                            val p0 = curPersona()
                            favTitle = (p0?.name?.ifBlank { "对话" } ?: "对话") + " · " +
                                    Dates.stamp(msgs.getOrNull(picked.minOrNull() ?: 0)?.ts ?: System.currentTimeMillis())
                            batchAsk = "save"
                        }
                    }
                    SetChip("复制") { if (picked.isNotEmpty()) batchAsk = "copy" }
                    SetChip(if (picked.size == msgs.size) "全不选" else "全选") {
                        if (picked.size == msgs.size) picked.clear()
                        else { picked.clear(); msgs.indices.forEach { picked.add(it) } }
                    }
                    SetChip("取消") { selecting = false; picked.clear() }
                }
            }
        } else {
        /* ---------------- 输入区 ---------------- */
        /* ---------------- 输入区 ---------------- */
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            // 「＋」：相册 / 表情包
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (showAttach) lerp(NightBottom, PurpleDeep, 0.45f) else PanelBgSoft)
                    .clickable {
                        showStickers = false
                        showAttach = !showAttach
                    }
                    .padding(horizontal = 12.dp, vertical = 14.dp)
            ) { Text("＋", color = TextMain, fontSize = 15.sp) }
            Spacer(Modifier.width(6.dp))
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = PanelBg,
                    focusedContainerColor = PanelBg,
                ),
                placeholder = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("说点什么…", color = TextFaint, fontSize = 12.sp)
                        Spacer(Modifier.weight(1f))
                        // 余额就放在这里：placeholder 本来就只在输入框**空着**时显示，
                        // 所以「一开始打字就隐藏」是天然的，不用额外加判断。
                        // 颜色比提示语亮一档、加半粗 —— 看得清，但不至于抢正文的视线。
                        balance?.let { b ->
                            Text(
                                b.text(cfg.baseUrl),
                                color = WarmLight, fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                },

                textStyle = TextStyle(fontSize = 14.sp),
                maxLines = 5
            )
            Spacer(Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (busy) lerp(NightBottom, Color(0xFFFF6B8A), 0.25f)
                        else if (input.isBlank() && pendingImg.isBlank()) PanelBgSoft
                        else lerp(NightBottom, PurpleDeep, 0.55f)
                    )
                    .clickable {
                        if (busy) stop()
                        else {
                            val img = pendingImg
                            pendingImg = ""
                            showAttach = false
                            sendText(input, img = img)
                        }
                    }
                    .padding(horizontal = 12.dp, vertical = 14.dp)
            ) {
                Text(
                    if (busy) "停止" else "发送",
                    color = if (busy || input.isNotBlank() || pendingImg.isNotBlank()) TextMain else TextFaint,
                    fontSize = 13.sp
                )
            }
        }

        }   // ← 多选操作条那个 if/else 到此为止
        }
        }

        Spacer(Modifier.height(4.dp))
    }       // ← 正文 Column 到此为止

    // ===== 以下是浮层：画在正文之上（靠外面那层 Box） =====

    /* ---------------- 批量操作的确认框 ---------------- */
    batchAsk?.let { what ->
        val title = when (what) {
            "del" -> "删除选中的 " + picked.size + " 条？"
            "save" -> "保存到收藏"
            else -> "复制选中的 " + picked.size + " 条？"
        }
        val message = when (what) {
            "del" -> "它们会从这段对话里永久删除，也不再进上下文。这一段里的其他消息不受影响。"
            "save" -> "存成一个收藏条目，之后可以在「收藏」里翻看。条目可以随时删掉。"
            else -> "按「说话人：内容」一行一条生成文本，复制到剪贴板。"
        }
        ConfirmOverlay(
            title = title,
            message = message,
            danger = what == "del",
            okText = when (what) {
                "del" -> "删除"
                "save" -> "保存"
                else -> "复制"
            },
            onConfirm = {
                val act = what
                batchAsk = null
                val idxs = picked.sorted()
                when (act) {
                    "del" -> {
                        // 从后往前删，下标才不会错位
                        idxs.reversed().forEach { i -> if (i in msgs.indices) msgs.removeAt(i) }
                        selecting = false
                        picked.clear()
                        persist()
                    }
                    "save" -> {
                        val items = idxs.mapNotNull { i -> msgs.getOrNull(i) }
                        if (items.isNotEmpty()) {
                            scope.launch {
                                val p0 = curPersona()
                                withContext(Dispatchers.IO) {
                                    AiStore.addFav(
                                        ctx,
                                        AiFav(
                                            title = favTitle,
                                            savedAt = System.currentTimeMillis(),
                                            persona = p0?.name.orEmpty(),
                                            fromAt = items.minOf { it.ts },
                                            msgs = items
                                        )
                                    )
                                }
                                selecting = false
                                picked.clear()
                                favTick++
                                err = "已保存到收藏"
                            }
                        }
                    }
                    else -> {
                        val txt = batchText()
                        val n = picked.size
                        try {
                            val cm = ctx.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            cm.setPrimaryClip(ClipData.newPlainText("Sympnoia 对话", txt))
                            // 光靠界面里那行小字不够明显 —— 再补一个系统的提示，
                            // 确保「复制成功了没有」一眼就能看见
                            try {
                                android.widget.Toast.makeText(
                                    ctx, "已复制 " + n + " 条到剪贴板",
                                    android.widget.Toast.LENGTH_SHORT
                                ).show()
                            } catch (e: Exception) {
                            }
                            err = "已复制 " + n + " 条到剪贴板"
                        } catch (e: Exception) {
                            err = "复制失败：" + (e.message ?: "系统不允许")
                        }
                        selecting = false
                        picked.clear()
                    }
                }
            },
            onCancel = { batchAsk = null },
            extra = if (what == "save") {
                {
                    OutlinedTextField(
                        value = favTitle,
                        onValueChange = { favTitle = it.take(40) },
                        singleLine = true,
                        textStyle = TextStyle(fontSize = 13.sp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            } else null
        )
    }

    /* ---------------- 收藏列表 ---------------- */
    if (showFavs) {
        AiFavOverlay(
            ctx = ctx,
            tick = favTick,
            onClose = { showFavs = false },
            onChanged = { favTick++ }
        )
    }

    }   // ← 外层 Box 到此为止
}

/* ============================== 消息里的图片 ============================== */

/**
 * 读一张聊天图片并显示。
 * ⚠️ 不能在 remember 里直接读盘：那是**组合期间**的文件 IO + 位图解码，
 *    一屏里有几张图就是肉眼可见的掉帧。进组合后异步读，读之前先占个灰底。
 */
@Composable
private fun AiMsgImage(
    ctx: Context,
    rel: String,
    modifier: Modifier,
    contentScale: ContentScale,
    placeholder: String = ""
) {
    var bmp by remember(rel) { mutableStateOf<ImageBitmap?>(null) }
    LaunchedEffect(rel) {
        bmp = if (rel.isBlank()) null
        else withContext(Dispatchers.IO) { AiStore.loadMsgImage(ctx, rel) }
    }
    val b = bmp
    if (b != null) {
        Image(bitmap = b, contentDescription = null, modifier = modifier, contentScale = contentScale)
    } else {
        Box(
            modifier = modifier.background(PanelBgSoft),
            contentAlignment = Alignment.Center
        ) { if (placeholder.isNotBlank()) Text(placeholder, color = TextFaint, fontSize = 10.sp) }
    }
}

/* ============================ 时间分隔 ============================ */

/** 第一条之前必给一条；之后只在隔了 [AI_TIME_GAP_MS] 以上时再给 */
private fun needTimeSeparator(msgs: List<AiChatMsg>, index: Int): Boolean {
    if (index == 0) return true
    val prev = msgs.getOrNull(index - 1) ?: return true
    return msgs[index].ts - prev.ts >= AI_TIME_GAP_MS
}

private fun sepText(ts: Long): String {
    val now = System.currentTimeMillis()
    val d = Dates.stamp(ts)
    return when (d) {
        Dates.stamp(now) -> Dates.hhmm(ts)
        Dates.stamp(now - 86_400_000L) -> "昨天 " + Dates.hhmm(ts)
        else -> d + " " + Dates.hhmm(ts)
    }
}

/* ============================== 一条消息气泡 ============================== */

/**
 * 「滚到最底」用的偏移量。
 *
 * ⚠️ scrollToItem(index) 只是把**那一条的顶端**对到视口顶端 ——
 *    如果这一条本身比视口还高（长回复、或者带深度思考块的那种），
 *    它的底端就落到屏幕下面去了，看着像「没滚到底」，其实滚的位置是"对"的。
 *    给一个远超任何内容高度的偏移，LazyListState 会把越界的位置夹回最大值 ——
 *    那个最大值才是内容真正的末尾，也就是我们要的「底端贴着输入框」。
 */
private const val SCROLL_TO_END = 100_000

/** 「继续生成」时额外塞给模型的一句（只进上下文，不显示在界面上） */
private const val CONTINUE_NUDGE = "（接着上面那条继续说下去，不要重复已经说过的内容）"

/** 气泡下那排小动作：字号小、颜色淡，不抢正文的视线 */
@Composable
private fun BubbleAct(label: String, onClick: () -> Unit) {



    Text(
        label, color = TextFaint, fontSize = 11.sp,
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .clickable { onClick() }
            .padding(horizontal = 6.dp, vertical = 3.dp)
    )
}

@Composable
private fun AiBubble(
    ctx: Context,
    msg: AiChatMsg,
    persona: AiPersona?,
    busy: Boolean,
    mine: Boolean,
    herAvatar: ImageBitmap?,
    myAvatar: ImageBitmap?,
    maxWidth: androidx.compose.ui.unit.Dp,
    /** 「重新生成」：把这一条丢掉，按同一段历史重问一次 */
    onRegenerate: () -> Unit = {}, onContinue: () -> Unit = {}, onExpand: () -> Unit = {}
) {
    val tint = persona?.tint() ?: PinkSoft
    // 「AI 助手」：字号收一档 —— 它是工具，一屏里能多看几行更要紧。
    // 依据的是**这张卡**是不是助手，不是「哪个设置开着」，所以别的设置改不到它。
    val tool = persona?.assistant == true

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (mine) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {
        if (!mine && herAvatar != null) {
            Image(
                bitmap = herAvatar,
                contentDescription = null,
                modifier = Modifier.size(28.dp).clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(6.dp))
        }
        if (msg.kind == "file") {
            // 发过来的文件：和「分享」一样只画一张两行小卡片。
            // 整份文件正文（msg.content）照常进上下文、照常存盘，只是不铺满屏幕 ——
            // 铺出来会把整段聊天记录冲得看不见。
            //
            // 卡片**上面**那句是他当时顺手打的（例如「帮我总结一下」），单独存在 msg.head ——
            // content 里混了整份正文，回头分不出哪句是他说的，所以必须单独存一份。
            Column(horizontalAlignment = if (mine) Alignment.End else Alignment.Start) {
                if (msg.head.isNotBlank()) {
                    Text(msg.head, color = TextMain, fontSize = 14.sp)
                    Spacer(Modifier.height(4.dp))
                }
                Column(
                    modifier = Modifier
                        .widthIn(max = maxWidth * 0.72f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(PanelBgSoft)
                        .border(1.dp, tint.copy(alpha = 0.45f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 7.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("📄", color = tint, fontSize = 12.sp)
                        Spacer(Modifier.width(6.dp))
                        Text("已发送文件", color = tint, fontSize = 11.sp)
                    }
                    Spacer(Modifier.height(2.dp))
                    Text(
                        msg.label.ifBlank { "文件" },
                        color = TextMuted, fontSize = 11.sp,
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                }
            }
        } else if (msg.kind == "share") {
            // 分享过来的：只画一张两行的小卡片，别把整篇日记铺满屏幕 ——
            // 正文（msg.content）照常进上下文、照常存盘，只是不显示而已。
            Column(
                modifier = Modifier
                    .widthIn(max = maxWidth * 0.66f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(PanelBgSoft)
                    .border(1.dp, tint.copy(alpha = 0.45f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 10.dp, vertical = 7.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("▤", color = tint, fontSize = 12.sp)
                    Spacer(Modifier.width(6.dp))
                    Text("已分享", color = tint, fontSize = 11.sp)
                    Spacer(Modifier.weight(1f))
                    Text("·", color = TextFaint, fontSize = 10.sp)
                    Spacer(Modifier.weight(1f))
                }
                Spacer(Modifier.height(2.dp))
                Text(
                    msg.label.ifBlank { "一条记录" },
                    color = TextMuted, fontSize = 11.sp,
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
            }
        } else if (msg.kind == "sticker" && msg.img.isNotBlank()) {
            // 表情包：不套气泡、不加内边距，就是一张图直接贴出来（跟聊天软件一样）。
            // 如果发送时输入框里有字，那些字也要显示出来 —— 它本来就发给模型了。
            Column(horizontalAlignment = if (mine) Alignment.End else Alignment.Start) {
                AiMsgImage(
                    ctx = ctx, rel = msg.img,
                    modifier = Modifier.size(120.dp).clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Fit,
                    placeholder = "［表情包］"
                )
                if (msg.content.isNotBlank()) {
                    Spacer(Modifier.height(3.dp))
                    Text(msg.content, color = TextMain, fontSize = 14.sp)
                }
            }
        } else if (msg.img.isNotBlank()) {
            // 带照片的消息：图在上、字在下，同一个气泡里
            Column(
                modifier = Modifier
                    .widthIn(max = maxWidth)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        if (mine) Brush.linearGradient(
                            listOf(PurpleDeep.copy(alpha = 0.42f), PurpleDeep.copy(alpha = 0.28f))
                        ) else Brush.linearGradient(listOf(PanelBg, PanelBgSoft))
                    )
                    .border(
                        1.dp,
                        if (mine) PurpleMain.copy(alpha = 0.22f)
                        else if (msg.failed) Color(0x66FF6B8A) else tint.copy(alpha = 0.28f),
                        RoundedCornerShape(14.dp)
                    )
                    .padding(6.dp)
            ) {
                AiMsgImage(
                    ctx = ctx, rel = msg.img,
                    modifier = Modifier.widthIn(max = maxWidth - 12.dp).clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Fit,
                    placeholder = "［图片］"
                )
                if (msg.content.isNotBlank()) {
                    Spacer(Modifier.height(5.dp))
                    Text(
                        text = msg.content,
                        color = if (msg.failed) Color(0xFFFFB3C1) else TextMain,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    // widthIn + 不加 fillMaxWidth：宽度跟着文字走，超过上限才折行
                    .widthIn(max = maxWidth)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        if (mine) Brush.linearGradient(
                            listOf(PurpleDeep.copy(alpha = 0.42f), PurpleDeep.copy(alpha = 0.28f))
                        ) else Brush.linearGradient(listOf(PanelBg, PanelBgSoft))
                    )
                    .border(
                        1.dp,
                        if (mine) PurpleMain.copy(alpha = 0.22f)
                        else if (msg.failed) Color(0x66FF6B8A) else tint.copy(alpha = 0.28f),
                        RoundedCornerShape(14.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 9.dp)
            ) {
                // 「框选文本」开着的时候，把正文包进 SelectionContainer —— 可以按住拖选一段再复制，
                // 而不是只能整条复制。默认是关的：开着时长按是选字，日常更容易误触。
                // 「深度思考」：思维链单独一块，颜色更淡、字号更小，和正文明显分开。
                // 它只在这里显示 —— 不进上下文，也不当成她说的话。
                if (msg.think.isNotBlank()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 6.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(PanelBgSoft.copy(alpha = 0.55f))
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Text("深度思考", color = TextFaint, fontSize = 10.sp)
                        Spacer(Modifier.height(3.dp))
                        Text(msg.think, color = TextFaint, fontSize = 12.sp)
                    }
                }
                val body: @Composable () -> Unit = {

                    Text(
                        text = if (msg.content.isNotBlank()) msg.content
                        else if (busy) "…" else "（空）",
                        color = if (msg.failed) Color(0xFFFFB3C1) else TextMain,
                        fontSize = if (tool) 13.sp else 14.sp
                    )
                }
                body()

                // 气泡下那排动作只给「AI 助手」：它是工具，重问一次、摘一段、整条复制都是常态。
                if (tool && !mine && !busy && msg.content.isNotBlank()) {
                    Spacer(Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        BubbleAct("框选") { onExpand() }
                        BubbleAct("复制") {
                            val cm = ctx.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            cm.setPrimaryClip(ClipData.newPlainText("Sympnoia 对话", msg.content))
                        }
                        BubbleAct("继续") { onContinue() }
                        BubbleAct("重新生成") { onRegenerate() }

                    }
                }
            }
        }
        if (mine && myAvatar != null) {
            Spacer(Modifier.width(6.dp))
            Image(
                bitmap = myAvatar,
                contentDescription = null,
                modifier = Modifier.size(28.dp).clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }
    }
}
