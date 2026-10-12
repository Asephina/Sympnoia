package com.example.asephina

import androidx.activity.compose.BackHandler

/*
 * ============================================================================
 *  凌薇 AI —— 设置页（接口 / 人设卡 / 对话）
 *  ---------------------------------------------------------------------------
 *  一屏管三件事：
 *    ① 接口：服务商预设、Base URL、API Key、模型、温度、长度、上下文轮数、超时、流式
 *    ② 人设卡：新建 / 编辑 / 复制 / 删除 / 导入 / 导出（导出的是**不含 Key** 的纯人设卡）
 *    ③ 对话：显示时间、清空当前人设的聊天记录、看数据落在哪
 *
 *  【Key 的去向】只写进 Asephina/ai_key.json 这一个文件，而 Pub 的导出排除表把这个
 *  文件挡在外面 —— 所以「导出数据包」拿到的 zip 里没有 Key，恢复别人的包也不会覆盖
 *  你自己的 Key。人设卡单独导出走系统文件选择器，用户自己挑位置。
 * ========================================================================== */

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Slider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/** 人设卡的主题色板 */
private val AI_TINTS = listOf(
    0xFFFBC2EB.toInt(), 0xFFC9B6F5.toInt(), 0xFFF7D98C.toInt(), 0xFF9CC7F0.toInt(),
    0xFFB8E6C1.toInt(), 0xFFFFB3A7.toInt(), 0xFFE0E0E0.toInt(), 0xFFB0A8D8.toInt()
)

@Composable
fun AiSettingsOverlay(ctx: Context, onClose: () -> Unit, onChanged: () -> Unit) {

    // 系统返回键 / 边缘返回手势 = 退出设置页（等同于「完成」）。
    // 设置是改一项存一项的，所以退出不需要再问要不要保存。
    // 返回键的处理在 saveAll 定义之后再挂（Kotlin 要求先声明后用）

    val scope = rememberCoroutineScope()

    var cfg by remember { mutableStateOf(AiApiCfg()) }
    var prefs by remember { mutableStateOf(AiPrefs()) }
    var activeId by remember { mutableStateOf("") }
    val personas = remember { mutableStateListOf<AiPersona>() }

    var msg by remember { mutableStateOf("") }
    var testing by remember { mutableStateOf(false) }
    var listing by remember { mutableStateOf(false) }
    /** 从接口拉回来的模型名（空 = 用内置预设那几个） */
    var liveModels by remember { mutableStateOf<List<String>>(emptyList()) }
    var showKey by remember { mutableStateOf(false) }
    var showAdvanced by remember { mutableStateOf(false) }
    var editing by remember { mutableStateOf<AiPersona?>(null) }
    var editingNew by remember { mutableStateOf(false) }
    /** 正在改内置默认卡（只允许改主题色、称呼、头像） */
    var editingBuiltin by remember { mutableStateOf(false) }
    /** 正在编辑「AI 助手」那张卡（名字 / 主题色 / 头像） */
    var editingAssistant by remember { mutableStateOf(false) }

    var ask by remember { mutableStateOf<Triple<String, String, () -> Unit>?>(null) }
    var pendingExport by remember { mutableStateOf<AiPersona?>(null) }

    // 头像：选图 → 裁剪 → 存盘。裁剪层由本页统一画在最上面（见下面那个 return）
    var cropSrc by remember { mutableStateOf<Bitmap?>(null) }
    var cropKey by remember { mutableStateOf("") }
    var avatarTick by remember { mutableIntStateOf(0) }
    /** 她自动记住的事（每聊够几轮由模型自己总结），改一次 +1 重新读 */
    var autoMem by remember { mutableStateOf<List<String>>(emptyList()) }
    var memTick by remember { mutableIntStateOf(0) }
    /** 数据目录的绝对路径（异步取，见下面那个 LaunchedEffect） */
    var dataPath by remember { mutableStateOf("") }

    /* ---- 日常信息（城市 / 天气） ---- */
    var cityDraft by remember { mutableStateOf("") }
    var dailyNow by remember { mutableStateOf("") }
    var dailyTick by remember { mutableIntStateOf(0) }
    var looking by remember { mutableStateOf(false) }
    /** 查到的候选地名：同名的地方太多，让用户自己点一个 */
    var geoHits by remember { mutableStateOf<List<AiDaily.GeoHit>>(emptyList()) }
    /** 主动问候的状态文本 */
    var greetNow by remember { mutableStateOf("") }
    var greetTick by remember { mutableIntStateOf(0) }

    /* ---- 主动互动 ---- */
    var pro by remember { mutableStateOf(AiProactiveState()) }
    var proStatus by remember { mutableStateOf("") }
    var proTick by remember { mutableIntStateOf(0) }

    fun proSave() {
        val s = pro
        scope.launch {
            withContext(Dispatchers.IO) { AiProactive.save(ctx, s) }
            proTick++
        }
    }

    LaunchedEffect(proTick) {
        val s = withContext(Dispatchers.IO) { AiProactive.load(ctx) }
        // 只覆盖设置那几项，未读与「已报过」别被这一页的旧副本盖掉
        pro = pro.copy(
            enabled = s.enabled, chance = s.chance, on = s.on,
            gapMin = s.gapMin, lastAt = s.lastAt, unread = s.unread, fillDone = s.fillDone
        )
        proStatus = withContext(Dispatchers.IO) { AiProactive.statusText(ctx) }
    }
    val avatarPicker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri == null) return@rememberLauncherForActivityResult
        scope.launch {
            val bmp = withContext(Dispatchers.IO) { decodePicked(ctx, uri, 800) }
            if (bmp == null) msg = "这张图读不出来，换一张试试" else cropSrc = bmp
        }
    }

    /** 给某个头像位选图（key = 人设 id，「我的头像」用 AiStore.AVATAR_ME） */
    fun pickAvatar(key: String) {
        cropKey = key
        avatarPicker.launch("image/*")
    }

    /** 清掉某个头像位。删文件走 IO，别在点击回调里同步做。 */
    fun clearAvatar(key: String, done: String) {
        scope.launch {
            withContext(Dispatchers.IO) { AiStore.deleteAvatar(ctx, key) }
            avatarTick++
            onChanged()
            msg = done
        }
    }

    LaunchedEffect(Unit) {
        cfg = withContext(Dispatchers.IO) { AiStore.loadCfg(ctx) }
        prefs = withContext(Dispatchers.IO) { AiStore.loadPrefs(ctx) }
        // allPersonas：内置的「纳凌薇」默认卡在最前，后面才是用户自建的
        val ps = withContext(Dispatchers.IO) { AiStore.ensure(ctx); AiStore.allPersonas(ctx) }
        personas.clear()
        personas.addAll(ps)
        activeId = if (ps.any { it.id == prefs.activeId }) prefs.activeId else ps.firstOrNull()?.id.orEmpty()
    }

    /** 给「记住的事」显示进度用：当前这一段还差多少条才会总结一次 */
    var memProgress by remember { mutableStateOf("") }
    /** 「记住的事」条目展开 / 折叠；条目多于 MEM_COLLAPSE_AT 时默认只露最近几条 */
    var memExpand by remember { mutableStateOf(false) }
    /** 正在改的是第几条（-1 = 新加一条）；null = 没在改 */
    var memEditingIndex by remember { mutableStateOf(-1) }
    var memEditingText by remember { mutableStateOf("") }
    var memEditing by remember { mutableStateOf(false) }
    var memBusy by remember { mutableStateOf(false) }

    // 读当前角色「她自己记住的事」。
    // AiBus.memTick 也当钥匙：对话页刚总结完新的，这一页开着也能立刻看到。
    LaunchedEffect(activeId, memTick, AiBus.memTick) {
        autoMem = if (activeId.isBlank()) emptyList()
        else withContext(Dispatchers.IO) { AiStore.loadMemories(ctx, activeId) }
        // 顺手把「还差几条」算出来 —— 以前这里只显示一个光秃秃的 0，
        // 让人以为功能坏了，其实是还没攒够 8 条
        memProgress = withContext(Dispatchers.IO) {
            if (activeId.isBlank()) ""
            else {
                val sid = AiStore.loadPrefs(ctx).activeSession
                val s = AiStore.loadSessions(ctx).firstOrNull { it.id == sid && it.personaId == activeId }
                if (s == null) ""
                else {
                    // ⚠️ 要和对话页的触发条件用同一把尺子：那边数的是**有正文的**消息，
                    //    而索引里的 count 把「只发了一张图」的也算了进去 ——
                    //    不统一的话，发几个表情包就会显示成「已经攒够」却什么也不发生。
                    val n = AiStore.loadSessionMsgs(ctx, sid).count { it.content.isNotBlank() }
                    val left = (8 - (n - s.memMark)).coerceAtLeast(0)
                    if (left <= 0) "当前这一段已经攒够，你说下一句时就会总结一次"
                    else "当前这一段 " + n + " 条，还差 " + left + " 条才会总结一次"
                }
            }
        }
    }

    // 数据目录的路径异步取回来：AiStore.dir 会 mkdirs，
    // 不该在组合期间（Text 的参数里）干这件事
    LaunchedEffect(Unit) {
        dataPath = withContext(Dispatchers.IO) { AiStore.dir(ctx).absolutePath }
    }

    // 读城市与天气缓存
    LaunchedEffect(dailyTick) {
        val d = withContext(Dispatchers.IO) { AiDaily.load(ctx) }
        if (cityDraft.isBlank()) cityDraft = d.city
        val w = withContext(Dispatchers.IO) { AiDaily.weatherLine(ctx) }
        dailyNow = if (d.city.isBlank()) "" else if (w.isBlank()) "城市：" + d.city + "（还没有天气数据）"
        else "城市：" + d.city + "　·　" + w
    }

    // 读主动问候的状态
    LaunchedEffect(greetTick, prefs.sleepFromMin, prefs.sleepToMin) {
        greetNow = withContext(Dispatchers.IO) { AiGreet.statusText(ctx, prefs.sleepFromMin, prefs.sleepToMin) }
    }

    fun saveAll(quiet: Boolean = false) {
        val list = personas.toList()
        val c = cfg
        val pf = prefs.copy(activeId = activeId)
        // 三个文件一起写（其中包括装着 API Key 的那个），别占着主线程做文件 IO。
        // savePersonas 内部会把内置卡滤掉：它不落盘，也就不进任何导出。
        //
        // ⚠️ NonCancellable 是必需的：右上角「完成」是「先 saveAll 再 onClose」，
        //    而这个 scope 属于本页 —— 页面一离开 composition 它就被取消了，
        //    于是刚改的 Base URL / API Key / 模型名可能**根本没写进盘**（甚至毫无提示）。
        //    加上它之后，哪怕已经点了关闭，这几个文件也一定写完。
        scope.launch {
            withContext(NonCancellable + Dispatchers.IO) {
                AiStore.saveCfg(ctx, c)
                AiStore.savePersonas(ctx, list)
                AiStore.savePrefs(ctx, pf)
            }
            onChanged()
            if (!quiet) msg = "已保存"
        }
    }

    /** 隐藏 / 显示内置卡：改标志、重建列表；若把当前卡藏了就自动切到下一张可见卡 */
    fun setHidden(id: String, hide: Boolean) {
        prefs = when (id) {
            AiDefaultPersona.ID -> prefs.copy(hideLingwei = hide)
            AiDefaultPersona.ASSISTANT_ID -> prefs.copy(hideAssistant = hide)
            else -> prefs
        }
        AiDefaultPersona.hideLingwei = prefs.hideLingwei
        AiDefaultPersona.hideAssistant = prefs.hideAssistant
        val ps = AiStore.allPersonas(ctx)
        personas.clear()
        personas.addAll(ps)
        if (ps.none { it.id == activeId }) activeId = ps.firstOrNull()?.id.orEmpty()
        saveAll(quiet = true)
    }

    // 系统返回键 / 边缘返回手势：与「完成」走同一条路 —— 先存盘再关。
    // 之前这里只 onClose，等于把刚改的 Base URL / Key / 模型名悄悄丢掉。
    // 必须挂在 saveAll 之后：Kotlin 的局部函数要求先声明后使用。
    BackHandler { saveAll(quiet = true); onClose() }


    /* ------------------------- 人设卡导入 / 导出（SAF） ------------------------- */

    val exportLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument("application/json")
    ) { uri: Uri? ->
        val p = pendingExport
        pendingExport = null
        if (uri == null || p == null) return@rememberLauncherForActivityResult
        scope.launch {
            val ok = withContext(Dispatchers.IO) {
                try {
                    ctx.contentResolver.openOutputStream(uri)?.use {
                        it.write(AiStore.exportPersonaJson(p).toByteArray(Charsets.UTF_8))
                    }
                    true
                } catch (e: Exception) {
                    false
                }
            }
            msg = if (ok) "人设卡「${p.name}」已导出（不含任何接口信息）" else "导出失败"
        }
    }

    val importLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri == null) return@rememberLauncherForActivityResult
        scope.launch {
            val text = withContext(Dispatchers.IO) {
                try {
                    ctx.contentResolver.openInputStream(uri)?.bufferedReader(Charsets.UTF_8)
                        ?.use { it.readText() }
                } catch (e: Exception) {
                    null
                }
            }
            if (text.isNullOrBlank()) {
                msg = "这个文件读不出来"
                return@launch
            }
            try {
                val p = withContext(Dispatchers.IO) { AiStore.importPersonaJson(text) }
                personas.add(p)
                saveAll(quiet = true)
                msg = "已导入「${p.name}」"
            } catch (e: Exception) {
                msg = "这不是一份人设卡（${e.message ?: "格式不对"}）"
            }
        }
    }

    /* ------------------------------ 头像裁剪层 ------------------------------ */

    // 裁剪层要盖住整页，所以放在所有分支之前统一处理（写在这里，下面两个分支都不用管）
    cropSrc?.let { b ->
        IconCropper(
            src = b,
            onCancel = { cropSrc = null },
            onDone = { out ->
                cropSrc = null
                // PNG 压缩 + 写盘放到 IO：这一步是几十毫秒到几百毫秒的活，
                // 在主线程上做就是一次可见的卡顿（大图更久）
                scope.launch {
                    val ok = withContext(NonCancellable + Dispatchers.IO) {
                        AiStore.saveAvatar(ctx, cropKey, out)
                    }
                    avatarTick++
                    onChanged()
                    msg = if (ok) "头像已更新" else "头像保存失败"
                }
            }
        )
        return
    }

    /* --------------------- 内置默认卡：只能改这三样 --------------------- */

    if (editingBuiltin) {
        AiBuiltinEditor(
            ctx = ctx,
            avatarTick = avatarTick,
            onPickAvatar = { pickAvatar(AiDefaultPersona.ID) },
            onClearAvatar = { clearAvatar(AiDefaultPersona.ID, "已清除头像") },
            onPickMyAvatar = { pickAvatar(AiStore.AVATAR_ME) },
            onClearMyAvatar = { clearAvatar(AiStore.AVATAR_ME, "已清除我的头像") },
            onSave = { color, call ->
                // 让列表和对话页立刻看到新的色与称呼
                val i = personas.indexOfFirst { it.id == AiDefaultPersona.ID }
                val fresh = AiDefaultPersona.build(AiDefaultOverride(color = color, call = call))
                if (i >= 0) personas[i] = fresh else personas.add(0, fresh)
                scope.launch {
                    withContext(Dispatchers.IO) {
                        AiStore.saveDefaultOverride(ctx, AiDefaultOverride(color = color, call = call))
                    }
                    saveAll(quiet = true)
                }
                editingBuiltin = false
                msg = "默认人设已更新"
            },
            onCancel = { editingBuiltin = false }
        )
        return
    }
        if (editingAssistant) {
            AiAssistantEditor(
                ctx = ctx,
                avatarTick = avatarTick,
                curName = prefs.assistantName,
                curColor = prefs.assistantColor,
                onPickAvatar = { pickAvatar(AiDefaultPersona.ASSISTANT_ID) },
                onClearAvatar = { clearAvatar(AiDefaultPersona.ASSISTANT_ID, "已清除头像") },
                // 「我的头像」是所有角色共用的那一张。以前它只在纳凌薇那页能设，
                // 所以只用助手的人永远设不上、对话里也就一直看不到自己。
                onPickMyAvatar = { pickAvatar(AiStore.AVATAR_ME) },
                onClearMyAvatar = { clearAvatar(AiStore.AVATAR_ME, "已清除头像") },
                onSave = { newName, newColor ->
                    prefs = prefs.copy(assistantName = newName.take(12), assistantColor = newColor)
                    // 立刻换掉列表里那张卡，不必等下一次读盘
                    val i = personas.indexOfFirst { it.id == AiDefaultPersona.ASSISTANT_ID }
                    val fresh = AiDefaultPersona.buildAssistant()
                    if (i >= 0) personas[i] = fresh
                    scope.launch {
                        withContext(Dispatchers.IO) {
                            AiStore.savePrefs(
                                ctx,
                                prefs.copy(assistantName = newName.take(12), assistantColor = newColor)
                            )
                        }
                        saveAll(quiet = true)
                    }
                    editingAssistant = false
                    msg = "AI 助手已更新"
                },
                onCancel = { editingAssistant = false }
            )
            return
        }


    /* ------------------------------ 人设卡编辑器 ------------------------------ */

    if (editing != null) {
        AiPersonaEditor(
            ctx = ctx,
            src = editing!!,
            isNew = editingNew,
            avatarTick = avatarTick,
            myAvatarTick = avatarTick,
            onPickAvatar = { pickAvatar(editing!!.id) },
            onClearAvatar = { clearAvatar(editing!!.id, "已清除头像") },
            onPickMyAvatar = { pickAvatar(AiStore.AVATAR_ME) },
            onClearMyAvatar = { clearAvatar(AiStore.AVATAR_ME, "已清除我的头像") },
            onCancel = { editing = null },
            onDelete = if (editingNew) null else ({
                val target = editing!!
                editing = null
                ask = Triple(
                    "删除这张人设卡？",
                    "「${target.name}」名下所有对话也会一起删掉，找不回来。"
                ) {
                    val i = personas.indexOfFirst { it.id == target.id }
                    if (i >= 0) personas.removeAt(i)
                    if (activeId == target.id) activeId = personas.firstOrNull()?.id.orEmpty()
                    // 删一堆文件：放 IO，而且用 NonCancellable 兜住 ——
                    // 确认框点完就可能退出这一页，别让删到一半的活被取消掉
                    scope.launch {
                        withContext(NonCancellable + Dispatchers.IO) {
                            AiStore.deleteSessionsOfPersona(ctx, target.id)
                            AiStore.deleteAvatar(ctx, target.id)
                        }
                        saveAll(quiet = true)
                        msg = "已删除"
                    }
                }
            }),
            onSave = { out ->
                val i = personas.indexOfFirst { it.id == out.id }
                val stamped = out.copy(updatedAt = System.currentTimeMillis())
                if (i >= 0) personas[i] = stamped else personas.add(stamped)
                if (activeId.isBlank()) activeId = stamped.id
                editing = null
                saveAll(quiet = true)
                msg = if (editingNew) "已新建「${stamped.name}」" else "已保存「${stamped.name}」"
            }
        )
        return
    }

    /* --------------------------------- 主页 --------------------------------- */

    // 这一页里指代角色的字，跟着【当前这张人设卡自己的性别】走（男→他 / 女→她 / 其余→ta）。
    // 而 AI 之外的整台应用跟着「花房 · 自定义设置」里的称呼走 —— 两套设置各管一段。
    val active = personas.firstOrNull { it.id == activeId }
    val ap = active?.pronoun() ?: "ta"
    // 精简记忆要拿角色的名字去拼指令
    fun curPersonaOrNull(): AiPersona? = personas.firstOrNull { it.id == activeId }
    // 跨角色的那几节（主动问候 / 自动记忆 / 导出物）同时牵扯到好几张卡，
    // 也和花房那边的称呼有关：两处对不上就统称 ta，别让同一个人被叫成两种性别。
    val gp = Pron.merge(Persona.pro(), ap)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Veil)
            .swallowClicks()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal)
                )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        active?.name?.trim()?.takeIf { it.isNotBlank() }?.let { it + "　·　AI 设置" }
                            ?: "AI 设置",
                        color = TextMain, fontSize = 16.sp, fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        "接口 · 人设卡 · 对话　·　全部内容只存在这台手机里",
                        color = TextFaint, fontSize = 10.sp
                    )
                }
                TextButton(onClick = { saveAll(quiet = true); onClose() }) {
                    Text("完成", color = PinkSoft, fontSize = 14.sp)
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp)
            ) {

                /* ============================ ① 接口 ============================ */
                SetSection("接口", "任何 OpenAI 兼容的服务都能用。先「测试连接」再聊。") {
                    Row(
                        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        AI_PROVIDERS.forEach { pr ->
                            SetChip(pr.label, primary = cfg.provider == pr.id) {
                                cfg = cfg.copy(
                                    provider = pr.id,
                                    baseUrl = pr.baseUrl.ifBlank { cfg.baseUrl },
                                    model = pr.models.firstOrNull() ?: cfg.model,
                                    // 换一家就把「能看图」跟着这家的常见情况给个建议值，
                                    // 省得用户自己猜（也可以随时手动改）
                                    vision = pr.vision
                                )
                                liveModels = emptyList()
                                msg = pr.hint
                            }
                        }
                    }

                    Spacer(Modifier.height(8.dp))
                    SetField(
                        label = "Base URL",
                        value = cfg.baseUrl,
                        hint = "https://api.deepseek.com/v1",
                        onChange = { cfg = cfg.copy(baseUrl = it, provider = "custom") }
                    )
                    if (cfg.baseUrl.trim().startsWith("http://")) {
                        Text(
                            "⚠️ 这是明文 http：API Key 与聊天内容是裸着在网线上跑的。" +
                                    "连自己电脑上的本地模型没问题；连公网服务请换成 https。",
                            color = Color(0xFFFFC178), fontSize = 10.sp
                        )
                    }

                    AiSecretField(
                        label = "API Key",
                        value = cfg.apiKey,
                        visible = showKey,
                        onToggleVisible = { showKey = !showKey },
                        onChange = { cfg = cfg.copy(apiKey = it) }
                    )

                    SetField(
                        label = "模型名",
                        value = cfg.model,
                        hint = "deepseek-flash / qwen3-max / glm-4.6 …",
                        onChange = { cfg = cfg.copy(model = it) }
                    )

                    // 内置的模型名一定会过时 —— 各家改名的速度比软件更新的速度快得多。
                    // 所以给一个「直接从接口拉当前可用名字」的入口，永远是准的。
                    Row(
                        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        SetChip(if (listing) "拉取中…" else "拉取模型列表", primary = true) {
                            if (!listing) {
                                listing = true
                                msg = ""
                                scope.launch {
                                    val got = try {
                                        AiClient.listModels(cfg)
                                    } catch (e: Exception) {
                                        emptyList()
                                    }
                                    listing = false
                                    if (got.isEmpty()) {
                                        msg = "这个接口没有返回模型列表，自己填模型名就行（控制台里能查到）"
                                    } else {
                                        liveModels = got
                                        msg = "拉到 " + got.size + " 个模型，点一个换上"
                                    }
                                }
                            }
                        }
                        SetChip("清空列表") { liveModels = emptyList() }
                    }

                    val candidates = if (liveModels.isNotEmpty()) liveModels
                    else AI_PROVIDERS.firstOrNull { it.id == cfg.provider }?.models.orEmpty()
                    if (candidates.isNotEmpty()) {
                        Spacer(Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            candidates.forEach { m ->
                                SetChip(m, primary = cfg.model == m) { cfg = cfg.copy(model = m) }
                            }
                        }
                        Spacer(Modifier.height(4.dp))
                    }

                    Spacer(Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("温度", color = TextMuted, fontSize = 11.sp)
                        Spacer(Modifier.width(8.dp))
                        SoftSlider(
                            value = cfg.temperature,
                            valueRange = 0f..1.5f,
                            modifier = Modifier.weight(1f),
                            onValueChange = { cfg = cfg.copy(temperature = it) }
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            String.format(java.util.Locale.CHINA, "%.2f", cfg.temperature),
                            color = TextMain, fontSize = 11.sp
                        )
                    }
                    Text(
                        "0.7 以下更稳，0.9 左右活一点，1.2 以上容易跑飞。人设对话一般 0.8~0.95。",
                        color = TextFaint, fontSize = 10.sp
                    )

                    Spacer(Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        SetChip(
                            if (cfg.stream) "流式：开" else "流式：关",
                            primary = cfg.stream
                        ) { cfg = cfg.copy(stream = !cfg.stream) }
                        SetChip(
                            if (cfg.vision) "能看图：开" else "能看图：关",
                            primary = cfg.vision
                        ) { cfg = cfg.copy(vision = !cfg.vision) }
                        SetChip(
                            // 这个开关只对「AI 助手」生效 —— 名字里点明，
                            // 免得以为改了会影响别的人设卡。
                            if (cfg.thinking) "助手先想再答：开" else "助手先想再答：关",

                            primary = cfg.thinking
                        ) { cfg = cfg.copy(thinking = !cfg.thinking) }

                        SetChip("高级", primary = showAdvanced) { showAdvanced = !showAdvanced }
                        SetChip(if (testing) "测试中…" else "测试连接", primary = false) {
                            if (!testing) {
                                testing = true
                                msg = ""
                                scope.launch {
                                    try {
                                        msg = AiClient.ping(cfg)
                                    } catch (e: Exception) {
                                        msg = e.message ?: "测试失败"
                                    } finally {
                                        testing = false
                                    }
                                }
                            }
                        }
                        SetChip("保存", primary = true) { saveAll() }
                    }
                    if (cfg.vision) {
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "「能看图」打开后，带图片的消息按多模态格式发出去。\n" +
                                    "只有多模态模型认这个格式：deepseek-flash、Qwen-VL、GLM-4.6V、GPT、Gemini…；\n" +
                                    "纯文字模型收到会直接报 400，不确定就关着。",
                            color = TextFaint, fontSize = 10.sp
                        )
                    }
                    if (cfg.thinking) {
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "只对「AI 助手」生效：别的人设卡一律不思考，那样只会变慢变怪。\n" +
                                    "开着时助手会先想一遍再答 —— 更稳，但更慢更贵；\n" +
                                    "思考过程会单独显示在回复上方，不会混进正文。默认关。",

                            color = TextFaint, fontSize = 10.sp
                        )
                    }

                    if (showAdvanced) {
                        Spacer(Modifier.height(8.dp))
                        SetField(
                            label = "单次回复上限（tokens）",
                            value = cfg.maxTokens.toString(),
                            hint = "512",
                            onChange = { cfg = cfg.copy(maxTokens = it.filter { c -> c.isDigit() }.toIntOrNull() ?: cfg.maxTokens) }
                        )
                        SetField(
                            label = "每次带几轮上下文",
                            value = cfg.historyRounds.toString(),
                            hint = "12",
                            onChange = { cfg = cfg.copy(historyRounds = it.filter { c -> c.isDigit() }.toIntOrNull() ?: cfg.historyRounds) }
                        )
                        SetField(
                            label = "超时（秒）",
                            value = cfg.timeoutSec.toString(),
                            hint = "60（可填 5~600）",
                            onChange = { v ->
                                val n = v.filter { c -> c.isDigit() }.toIntOrNull()
                                // 夹到 5~600：填 0 是「永不超时」，配上流式读取不好取消就是永久卡死
                                cfg = cfg.copy(timeoutSec = (n ?: cfg.timeoutSec).coerceIn(5, 600))
                            }
                        )
                        SetField(
                            label = "全局附加指令",
                            value = cfg.extraSystem,
                            hint = "接在所有角色之后的话，比如「回复尽量短」",
                            single = false,
                            lines = 3,
                            onChange = { cfg = cfg.copy(extraSystem = it) }
                        )
                        Text(
                            "轮数越大越记得住前文，也越费钱；上下文快满时服务商会直接报错（413 / 400）。",
                            color = TextFaint, fontSize = 10.sp
                        )
                    }
                }

                /* =========================== ② 人设卡 =========================== */
                SetSection(
                    "人设卡（" + personas.size + "）",
                    "换卡就换人。第一张内置、不可改；下面自己建的随便改。"
                ) {
                    personas.forEach { one ->
                        val on = one.id == activeId
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (on) one.tint().copy(alpha = 0.14f) else PanelBgSoft)
                                .border(
                                    1.dp,
                                    if (on) one.tint().copy(alpha = 0.5f) else PurpleMain.copy(alpha = 0.15f),
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier.size(8.dp).clip(CircleShape).background(one.tint())
                            )
                            Spacer(Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                // 卡片高度要一致，所以名字与摘要都只占一行、超出用省略号。
                                // 摘要来自人设正文（summary()），写得多的人会折成两三行，
                                // 于是列表里每张卡高矮不一 —— 看着很乱，也不好像这样对齐。
                                Text(
                                    one.name.ifBlank { "未命名" } +
                                            (if (one.assistant) "　·　工具"
                                            else if (one.builtin) "　·　默认" else "") +
                                            (if (on) "　·　当前" else ""),
                                    color = TextMain, fontSize = 13.sp,
                                    maxLines = 1,
                                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                )
                                Text(
                                    if (one.assistant) "不做角色扮演，只当工具用；人设与记忆都不发"
                                    else if (one.builtin) "内置人设，设定不在此展示，也不可修改"
                                    else one.summary(),
                                    color = TextFaint, fontSize = 10.sp,
                                    maxLines = 1,
                                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                )

                            }
                            SetChip("用", primary = on) {
                                activeId = one.id
                                prefs = prefs.copy(activeId = one.id)
                                saveAll(quiet = true)
                                msg = "当前对话对象：${one.name}"
                            }
                            // 内置卡都不落盘、不导出。两张卡能改的东西不一样：
                            //   · 「AI 助手」是工具，没有「人设」可言 —— 只开三样：
                            //     叫什么、主题色、头像；
                            //   · 「纳凌薇」那张保留原来的三样：主题色、怎么称呼你、头像。
                            // ⚠️ 两者的编辑页完全不同，不能共用一个入口 ——
                            //    之前不管点哪张卡都走 editingBuiltin，而那个页面里写死了
                            //    纳凌薇的 id，所以点助手的「改」会打开纳凌薇的设置。
                            if (one.builtin) {
                                Spacer(Modifier.width(4.dp))
                                SetChip("改") {
                                    if (one.id == AiDefaultPersona.ASSISTANT_ID) editingAssistant = true
                                    else editingBuiltin = true
                                }
                                Spacer(Modifier.width(4.dp))
                                SetChip("藏") {
                                    setHidden(one.id, true)
                                    msg = if (one.assistant) "已隐藏「AI 助手」" else "已隐藏「纳凌薇」"
                                }
                            } else {

                                Spacer(Modifier.width(4.dp))
                                SetChip("改") { editing = one; editingNew = false }
                                Spacer(Modifier.width(4.dp))
                                SetChip("复制") {
                                    val copy = one.copy(
                                        id = AiStore.newId(),
                                        name = one.name + "（副本）",
                                        builtin = false,
                                        // ⚠️ 必须一并清掉 assistant：那是内置工具的专用标记。
                                        //    不清的话，副本会带着它、看着就是助手，
                                        //    而且下一次再复制还会继续传下去。
                                        assistant = false,
                                        createdAt = System.currentTimeMillis(),
                                        updatedAt = System.currentTimeMillis()
                                    )
                                    personas.add(copy)

                                    saveAll(quiet = true)
                                    msg = "已复制"
                                }
                                Spacer(Modifier.width(4.dp))
                                SetChip("导出") {
                                    pendingExport = one
                                    exportLauncher.launch(safeFileName(one.name) + ".json")
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        SetChip("＋ 新建", primary = true) {
                            editing = AiStore.blankPersona()
                                .copy(color = AI_TINTS[personas.size % AI_TINTS.size])
                            editingNew = true
                        }
                        SetChip("导入人设卡") { importLauncher.launch(arrayOf("application/json", "text/plain", "*/*")) }
                        SetChip("导出当前") {
                            val one = personas.firstOrNull { it.id == activeId } ?: personas.firstOrNull()
                            if (one == null) msg = "还没有人设卡"
                            else if (one.builtin) msg = "默认人设不支持导出"
                            else {
                                pendingExport = one
                                exportLauncher.launch(safeFileName(one.name) + ".json")
                            }
                        }
                    }
                    if (prefs.hideLingwei || prefs.hideAssistant) {
                        Spacer(Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("已隐藏：", color = TextFaint, fontSize = 11.sp)
                            if (prefs.hideLingwei) {
                                SetChip("纳凌薇 · 显示") {
                                    setHidden(AiDefaultPersona.ID, false)
                                    msg = "已显示「纳凌薇」"
                                }
                            }
                            if (prefs.hideAssistant) {
                                SetChip("AI 助手 · 显示") {
                                    setHidden(AiDefaultPersona.ASSISTANT_ID, false)
                                    msg = "已显示「AI 助手」"
                                }
                            }
                        }
                    }
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "人设卡里只有名字、称呼、性格、语气这些内容，【不含 API Key】，" +
                                "导出后可以直接发给别人。别人导入后用自己的 Key 就能用。",
                        color = TextFaint, fontSize = 10.sp
                    )
                }


                /* ============================ ③ 头像 ============================ */
                SetSection("头像", "设了就显示在气泡旁，不设不显示。") {
                    AiAvatarRow(
                        ctx = ctx,
                        label = "我的头像（所有角色共用）",
                        avatarKey = AiStore.AVATAR_ME,
                        tick = avatarTick,
                        onPick = { pickAvatar(AiStore.AVATAR_ME) },
                        onClear = { clearAvatar(AiStore.AVATAR_ME, "已清除我的头像") }
                    )
                    Spacer(Modifier.height(6.dp))
                    AiAvatarRow(
                        ctx = ctx,
                        label = "「" + (personas.firstOrNull { it.id == activeId }?.name ?: "当前角色") + "」的头像",
                        avatarKey = activeId,
                        tick = avatarTick,
                        onPick = { if (activeId.isNotBlank()) pickAvatar(activeId) },
                        onClear = {
                            if (activeId.isNotBlank()) clearAvatar(activeId, "已清除" + ap + "的头像")
                        }
                    )
                }

                /* ======================= ③ 记住的事 ======================= */
                SetSection(
                    "记住的事（" + autoMem.size + "）",
                    "每聊够 8 条消息（分几次也算），" + ap +
                            "会自己把这段对话里值得长期记住的东西总结成短句存下来，" +
                            "以后换哪一段对话都还记得。总结只在这一台手机里，也可以随时清空。"
                ) {
                    if (memProgress.isNotBlank()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(PanelBgSoft)
                                .padding(horizontal = 10.dp, vertical = 7.dp)
                        ) {
                            Text(memProgress, color = PinkSoft, fontSize = 10.sp)
                        }
                        Spacer(Modifier.height(6.dp))
                    }
                    if (autoMem.isEmpty()) {
                        Text(
                            "还没有。这一段再聊满 8 条（含" + ap + "的回复）就会总结一次；\n" +
                                    "也可以先把行迹 / 文章 / 日记分享进对话，很快就有东西可记了。",
                            color = TextFaint, fontSize = 10.sp
                        )
                    } else {
                        // 条目一多就折叠：默认只露最近几条，免得这一节越占越长
                        val shown = if (memExpand || autoMem.size <= MEM_COLLAPSE_AT) autoMem
                        else autoMem.takeLast(MEM_COLLAPSE_AT)
                        shown.asReversed().forEach { one ->
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 1.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(
                                    "· " + one,
                                    color = TextMuted, fontSize = 11.sp,
                                    modifier = Modifier.weight(1f)
                                )
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    "改", color = PinkSoft, fontSize = 10.sp,
                                    modifier = Modifier.clickable {
                                        memEditingIndex = autoMem.indexOf(one)
                                        memEditingText = one
                                        memEditing = true
                                    }
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    "删", color = Color(0xFFFFB3C1), fontSize = 10.sp,
                                    modifier = Modifier.clickable {
                                        val i = autoMem.indexOf(one)
                                        if (i >= 0) {
                                            val next = autoMem.toMutableList().also { it.removeAt(i) }
                                            scope.launch {
                                                withContext(Dispatchers.IO) {
                                                    AiStore.saveMemories(ctx, activeId, next)
                                                }
                                                memTick++
                                                msg = "已删掉这一条"
                                            }
                                        }
                                    }
                                )
                            }
                        }
                        if (autoMem.size > MEM_COLLAPSE_AT) {
                            Spacer(Modifier.height(2.dp))
                            Text(
                                if (memExpand) "收起（共 " + autoMem.size + " 条）"
                                else "展开全部（共 " + autoMem.size + " 条）",
                                color = PinkSoft, fontSize = 10.sp,
                                modifier = Modifier.clickable { memExpand = !memExpand }
                            )
                        }
                    }
                    Spacer(Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        SetChip("＋ 自己加一条", primary = true) {
                            memEditingIndex = -1
                            memEditingText = ""
                            memEditing = true
                        }
                        SetChip(if (memBusy) "精简中…" else "精简一遍") {
                            if (!memBusy && autoMem.isNotEmpty()) {
                                val p = curPersonaOrNull()
                                if (p == null || !cfg.ready()) {
                                    msg = "要先配好接口，精简是一次模型调用"
                                } else {
                                    memBusy = true
                                    val c = cfg
                                    val all = autoMem
                                    scope.launch {
                                        try {
                                            val got = withContext(Dispatchers.IO) {
                                                AiClient.consolidate(c, p.name.ifBlank { p.pronoun() }, all)
                                            }
                                            if (got.isEmpty()) {
                                                msg = "这次没精简出结果，稍后再试"
                                            } else if (got.size >= all.size) {
                                                // 压不动就留着 —— 宁可多几条，也别为了好看丢东西
                                                msg = "已经够精简了（" + all.size + " 条），没动"
                                            } else {
                                                withContext(Dispatchers.IO) {
                                                    AiStore.saveMemories(ctx, activeId, got)
                                                }
                                                memTick++
                                                msg = "已从 " + all.size + " 条精简到 " + got.size + " 条"
                                            }
                                        } catch (e: Exception) {
                                            msg = "精简失败：" + (e.message ?: "接口没通")
                                        } finally {
                                            memBusy = false
                                        }
                                    }
                                }
                            }
                        }
                        SetChip("刷新") { memTick++ }
                        SetChip("清空" + ap + "记住的事") {
                            ask = Triple(
                                "清空「" + (active?.name ?: "") + "」记住的事？",
                                ap + "会忘掉这些总结出来的记忆。手写的人设卡与对话记录都不受影响。"
                            ) {
                                scope.launch {
                                    withContext(Dispatchers.IO) { AiStore.clearMemories(ctx, activeId) }
                                    memTick++
                                    msg = "已清空"
                                }
                            }
                        }
                    }
                }

                /* ===================== ④ 日常信息（时间 / 天气） ===================== */
                SetSection(
                    "日常信息",
                    "把真实时间、节日与天气放进设定，免得" + ap + "在大下午催你睡觉。"
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        SetChip(
                            if (prefs.daily) "把日常信息告诉" + ap + "：开" else "把日常信息告诉" + ap + "：关",
                            primary = prefs.daily
                        ) {
                            prefs = prefs.copy(daily = !prefs.daily)
                            saveAll(quiet = true)
                        }
                    }
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "时间与节日节气完全离线算；天气要填了城市才去查，半小时一次。\n" +
                                "天气来自 open-meteo.com（免费、不需要 Key）。不填城市就完全不联网。",
                        color = TextFaint, fontSize = 10.sp
                    )
                    Spacer(Modifier.height(8.dp))
                    SetField(
                        label = "城市 / 区县（填了就查天气，留空就不查）",
                        value = cityDraft,
                        hint = "例：北京 / 海淀区 / 朝阳区（带不带「区市县」都行）",
                        onChange = { cityDraft = it }
                    )
                    Spacer(Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        SetChip(if (looking) "查询中…" else "查一下", primary = true) {
                            if (!looking && cityDraft.isNotBlank()) {
                                looking = true
                                scope.launch {
                                    try {
                                        val hits = withContext(Dispatchers.IO) {
                                            AiDaily.geocodeHits(cityDraft)
                                        }
                                        geoHits = hits
                                        msg = if (hits.isEmpty()) {
                                            "没查到这个地名，换个写法试试（也可以填拼音）"
                                        } else {
                                            "查到 " + hits.size + " 个候选，点一个合适的 —— " +
                                                    "同名的地方不少，别选错了"
                                        }
                                    } catch (e: Exception) {
                                        msg = "查询失败：" + (e.message ?: "网络不通")
                                    } finally {
                                        looking = false
                                    }
                                }
                            }
                        }
                        SetChip("刷新天气") {
                            scope.launch {
                                // 输入框里改了地名但没点候选时，别装作刷新成功了 ——
                                // 刷新用的始终是**已保存**的那个城市
                                val saved = withContext(Dispatchers.IO) { AiDaily.load(ctx).city }
                                if (saved.isBlank()) {
                                    msg = "还没设城市：先在上面填一个，再点「查一下」从候选里选"
                                } else if (cityDraft.trim().isNotEmpty() && cityDraft.trim() != saved) {
                                    msg = "刷新的是已保存的「" + saved + "」。要换成「" +
                                            cityDraft.trim() + "」，先点上面的「查一下」再选一个候选"
                                } else {
                                    val ok = withContext(Dispatchers.IO) {
                                        AiDaily.refreshWeather(ctx) != null
                                    }
                                    dailyTick++
                                    msg = if (ok) "天气已更新" else "没取到天气（网络不通，或服务端暂时没回）"
                                }
                            }
                        }
                        SetChip("不查天气了") {
                            scope.launch {
                                withContext(Dispatchers.IO) { AiDaily.clear(ctx) }
                                cityDraft = ""
                                geoHits = emptyList()
                                dailyTick++
                                msg = "已清掉城市与天气"
                            }
                        }
                    }

                    // 候选地名：点一个即生效（同名的地方太多，必须让用户挑）
                    if (geoHits.isNotEmpty()) {
                        Spacer(Modifier.height(6.dp))
                        Text("选一个（同名的地方很多，括号里是上级行政区）：", color = TextMuted, fontSize = 10.sp)
                        Spacer(Modifier.height(4.dp))
                        geoHits.forEach { hit ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 2.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(PanelBgSoft)
                                    .clickable {
                                        scope.launch {
                                            withContext(Dispatchers.IO) {
                                                AiDaily.setCity(ctx, hit.name, hit.lat, hit.lon)
                                                AiDaily.refreshWeather(ctx)
                                            }
                                            geoHits = emptyList()
                                            cityDraft = hit.name
                                            dailyTick++
                                            msg = "已设为「" + hit.label + "」"
                                        }
                                    }
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("📍", fontSize = 11.sp)
                                Spacer(Modifier.width(6.dp))
                                Text(hit.label, color = TextMain, fontSize = 11.sp, modifier = Modifier.weight(1f))
                                Text("选它", color = PinkSoft, fontSize = 10.sp)
                            }
                        }
                    }
                    Spacer(Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(PanelBgSoft)
                            .padding(horizontal = 10.dp, vertical = 7.dp)
                    ) {
                        Text(
                            dailyNow.ifBlank { "城市：未设置（只带时间与节日节气）" },
                            color = PinkSoft, fontSize = 10.sp
                        )
                    }
                }

                /* ===================== ⑤ 主动问候 ===================== */
                SetSection(
                    "主动问候",
                    "你超过 3 小时没说话，" + gp + "挑最近那一段问候一句；你不理，" +
                            "间隔翻倍到 6 / 12 / 24 / 48 小时，最多 5 次。回一句即归零。"
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(PanelBgSoft)
                            .padding(horizontal = 10.dp, vertical = 7.dp)
                    ) {
                        Text(
                            if (greetNow.isBlank()) "正在读取…" else greetNow,
                            color = PinkSoft, fontSize = 10.sp
                        )
                    }
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "· 只在应用开着时检查（每分钟一次），关掉期间错过的下次打开补上。\n" +
                                "· 睡觉时段内不会主动开口，睡醒了再说（时段见上一节）。\n" +
                                "· 每次「没被搭理」都记进长期记忆；问候只一句话，调用很短。",
                        color = TextFaint, fontSize = 10.sp
                    )
                    Spacer(Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        SetChip("刷新状态") { greetTick++ }
                        SetChip("重新计时（当作刚聊过）") {
                            scope.launch {
                                withContext(Dispatchers.IO) { AiGreet.onUserSpoke(ctx) }
                                greetTick++
                                msg = "已重新计时，3 小时后才可能主动开口"
                            }
                        }
                        SetChip("这一轮别再问" + gp + "了") {
                            scope.launch {
                                withContext(Dispatchers.IO) { AiGreet.reset(ctx) }
                                greetTick++
                                msg = "已停用主动问候（下次你说话时会自动恢复计时）"
                            }
                        }
                    }
                }

                /* ===================== ⑥ 主动互动 ===================== */
                SetSection("主动互动", "你做事的那一刻她可能自己说一句。全默认关。") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(PanelBgSoft)
                            .padding(horizontal = 10.dp, vertical = 7.dp)
                    ) {
                        Text(
                            if (proStatus.isBlank()) "正在读取…" else proStatus,
                            color = PinkSoft, fontSize = 10.sp
                        )
                    }
                    Spacer(Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        SetChip(if (pro.enabled) "总开关：开" else "总开关：关", primary = pro.enabled) {
                            pro = pro.copy(enabled = !pro.enabled)
                            proSave()
                        }
                        SetChip("全不选") {
                            pro = pro.copy(on = emptyMap())
                            proSave()
                        }
                        SetChip("刷新状态") { proTick++ }
                    }
                    Spacer(Modifier.height(6.dp))
                    AiEvent.entries.chunked(2).forEach { row ->
                        Row(modifier = Modifier.fillMaxWidth()) {
                            row.forEach { e ->
                                Box(modifier = Modifier.weight(1f).padding(end = 6.dp, bottom = 6.dp)) {
                                    SetChip(
                                        (if (pro.isOn(e)) "✓ " else "") + e.label,
                                        primary = pro.isOn(e)
                                    ) {
                                        val m = pro.on.toMutableMap()
                                        m[e.id] = !(m[e.id] ?: false)
                                        pro = pro.copy(on = m)
                                        proSave()
                                    }
                                }
                            }
                            if (row.size == 1) Spacer(Modifier.weight(1f))
                        }
                    }
                    AiSliderRow(
                        label = "命中概率",
                        value = pro.chance.toFloat(),
                        range = 0f..100f,
                        text = pro.chance.toString() + "%",
                        onChange = { pro = pro.copy(chance = it.toInt()) },
                        onDone = { proSave() }
                    )
                    AiSliderRow(
                        label = "最短间隔",
                        value = pro.gapMin.toFloat(),
                        range = 0f..180f,
                        text = pro.gapMin.toString() + " 分",
                        onChange = { pro = pro.copy(gapMin = it.toInt()) },
                        onDone = { proSave() }
                    )
                    AiSliderRow(
                        label = "回应长度上限",
                        value = pro.replyMaxTokens.toFloat(),
                        range = 20f..400f,
                        // 中文大约 1 token ≈ 1.5 字，给个心理预期就行
                        text = pro.replyMaxTokens.toString() + " token（约 " +
                                (pro.replyMaxTokens * 3 / 2) + " 字内）",
                        onChange = { pro = pro.copy(replyMaxTokens = it.toInt()) },
                        onDone = { proSave() }
                    )
                    Text(
                        "· 每次先掷骰子（上面那个概率），过了再看间隔。\n" +
                                "· 睡觉时段内不响（时段见上一节）。\n" +
                                "· 只有新建才触发：回头改一条旧的再保存，不算。\n" +
                                "· 文章与日记整篇发过去；她说的话进最近那一段对话，没看到算未读。\n" +
                                "· **无论上限拉多大、她都只回一句、也不分条** —— 收到之后会再掐一次，" +
                                "不会出现一个弹窗还没读完又冒一个。",
                        color = TextFaint, fontSize = 10.sp
                    )
                }

                /* ===================== ⑦ 说话的节奏与作息 ===================== */
                SetSection("说话的节奏与作息", "快慢自己调。") {
                    AiSliderRow(
                        label = "出手前停一下",
                        value = prefs.replyDelayMs.toFloat(),
                        range = 0f..3000f,
                        text = if (prefs.replyDelayMs == 0) "立刻" else String.format(java.util.Locale.CHINA, "%.1f 秒", prefs.replyDelayMs / 1000f),
                        onChange = { prefs = prefs.copy(replyDelayMs = it.toInt()) },
                        onDone = { saveAll(quiet = true) }
                    )
                    AiSliderRow(
                        label = "打字机速度",
                        value = prefs.typeSpeedMs.toFloat(),
                        range = 0f..200f,
                        text = if (prefs.typeSpeedMs == 0) "关闭" else prefs.typeSpeedMs.toString() + " 毫秒/字",
                        onChange = { prefs = prefs.copy(typeSpeedMs = it.toInt()) },
                        onDone = { saveAll(quiet = true) }
                    )
                    AiSliderRow(
                        label = "分条之间停一下",
                        value = prefs.partDelayMs.toFloat(),
                        range = 0f..2000f,
                        text = if (prefs.partDelayMs == 0) "不停" else String.format(java.util.Locale.CHINA, "%.1f 秒", prefs.partDelayMs / 1000f),
                        onChange = { prefs = prefs.copy(partDelayMs = it.toInt()) },
                        onDone = { saveAll(quiet = true) }
                    )

                    Spacer(Modifier.height(8.dp))
                    Text("平时的睡觉时段", color = TextMuted, fontSize = 11.sp)
                    Spacer(Modifier.height(2.dp))
                    // ⚠️ 这里刻意用两个**单向**滑块，而不是一个 RangeSlider：
                    //    RangeSlider 不允许起点越过终点，而「23:30 睡、07:00 起」恰恰是起点大于终点。
                    //    硬塞进去的话两个滑块会显示成反的，一碰就被吸附成同一个值 ——
                    //    也就是默认那种跨零点的时段根本设不出来。
                    AiSliderRow(
                        label = "几点睡",
                        value = prefs.sleepFromMin.toFloat() / 60f,
                        range = 0f..24f,
                        text = AiDaily.hhmmOfMin(prefs.sleepFromMin),
                        onChange = { prefs = prefs.copy(sleepFromMin = (it * 60).toInt().coerceIn(0, 1440)) },
                        onDone = { saveAll(quiet = true) }
                    )
                    AiSliderRow(
                        label = "几点起",
                        value = prefs.sleepToMin.toFloat() / 60f,
                        range = 0f..24f,
                        text = AiDaily.hhmmOfMin(prefs.sleepToMin),
                        onChange = { prefs = prefs.copy(sleepToMin = (it * 60).toInt().coerceIn(0, 1440)) },
                        onDone = { saveAll(quiet = true) }
                    )
                    Text(
                        "睡觉时段：" + AiDaily.hhmmOfMin(prefs.sleepFromMin) + " — " +
                                AiDaily.hhmmOfMin(prefs.sleepToMin) +
                                if (prefs.sleepFromMin == prefs.sleepToMin) "　（两端一样 = 不设限）"
                                else "　（跨过零点也算，比如 23:30 睡、07:00 起）",
                        color = TextMain, fontSize = 11.sp
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "· 这个时段里" + gp + "不会主动开口，你说过话也不算数，睡醒了再说。\n" +
                                "· 你真在这个时段找" + gp + "说话，" + gp + "会先关心一句你还没睡，而不是催你去睡。\n" +
                                "· 会话里提过自己在值夜班、作息相反的话，" + gp + "会按你说的反过来理解。",
                        color = TextFaint, fontSize = 10.sp
                    )
                }

                /* ============================ ⑦ 对话 ============================ */
                SetSection("对话", "在「对话列表」里翻。") {

                    Row(

                        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        SetChip(
                            if (prefs.showTime) "时间分隔：开" else "时间分隔：关",
                            primary = prefs.showTime
                        ) {
                            prefs = prefs.copy(showTime = !prefs.showTime)
                            saveAll(quiet = true)
                        }
                        SetChip("删除「" + (personas.firstOrNull { it.id == activeId }?.name ?: "") + "」的全部对话") {
                            val one = personas.firstOrNull { it.id == activeId }
                            ask = Triple(
                                "删除「${one?.name ?: ""}」的全部对话？",
                                "这个角色名下所有对话都会被永久删除。人设卡与接口设置都不动。"
                            ) {
                                // 删一堆文件 + 建一段新的：放 IO，并用 NonCancellable 兜住
                                // （确认框点完页面可能就关了，别让删到一半的活被取消）
                                scope.launch {
                                    withContext(NonCancellable + Dispatchers.IO) {
                                        AiStore.deleteSessionsOfPersona(ctx, activeId)
                                        AiStore.createSession(ctx, activeId)
                                    }
                                    msg = "已删除"
                                    onChanged()
                                }
                            }
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    Text("数据落在：", color = TextMuted, fontSize = 11.sp)
                    Text(dataPath, color = TextFaint, fontSize = 10.sp)
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "· ai/personas.json 人设卡　· ai/sessions/ 对话　· ai/prefs.json 偏好\n" +
                                "· ai_key.json 只放 API Key，【已被排除在「导出数据包」之外】，" +
                                "所以备份包可以放心存到公共目录，也不会在恢复别人的包时被覆盖。\n" +
                                "· 换手机时要重新填一次 Key，人设卡与对话可以随数据包一起搬。",
                        color = TextFaint, fontSize = 10.sp
                    )
                }

                if (msg.isNotBlank()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(PanelBgSoft)
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(msg, color = PinkSoft, fontSize = 11.sp)
                    }
                }

                Spacer(Modifier.height(10.dp))
                Text(
                    "隐私：对话只存在本机，发出去的内容只有你选定的那个接口能收到。",
                    color = TextFaint, fontSize = 10.sp
                )
                Spacer(Modifier.height(48.dp))
            }
        }
    }

    // 确认弹窗必须画在最后：Compose 同层按书写顺序叠，写在前面会被设置页整个盖住
    ask?.let { a ->
        ConfirmOverlay(
            title = a.first,
            message = a.second,
            danger = true,
            okText = "确认",
            onConfirm = {
                val act = a.third
                ask = null
                act()
            },
            onCancel = { ask = null }
        )
    }

    /* ---- 「记住的事」的单条编辑：改一条 / 自己加一条 ---- */
    if (memEditing) {
        ConfirmOverlay(
            title = if (memEditingIndex >= 0) "改这一条" else "加一条要" + ap + "记住的事",
            message = "写短一点、写具体一点。这一条会跟着设定一起发给她。",
            danger = false,
            okText = "保存",
            onConfirm = {
                val text = memEditingText.replace("\n", " ").trim().take(AiStore.MAX_MEM_CHARS)
                memEditing = false
                if (text.length >= 2) {
                    val next = autoMem.toMutableList()
                    if (memEditingIndex in next.indices) next[memEditingIndex] = text else next.add(text)
                    scope.launch {
                        withContext(Dispatchers.IO) { AiStore.saveMemories(ctx, activeId, next) }
                        memTick++
                        msg = "已保存"
                    }
                }
            },
            onCancel = { memEditing = false },
            extra = {
                OutlinedTextField(
                    value = memEditingText,
                    onValueChange = { memEditingText = it.take(AiStore.MAX_MEM_CHARS) },
                    singleLine = false,
                    maxLines = 3,
                    textStyle = TextStyle(fontSize = 13.sp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        )
    }
}

/** 「记住的事」超过这个条数就折叠起来，只露最近几条 */
private const val MEM_COLLAPSE_AT = 6

/* ============================== 人设卡编辑器 ============================== */

@Composable
private fun AiPersonaEditor(
    ctx: Context,
    src: AiPersona,
    isNew: Boolean,
    avatarTick: Int,
    myAvatarTick: Int,
    onPickAvatar: () -> Unit,
    onClearAvatar: () -> Unit,
    onPickMyAvatar: () -> Unit,
    onClearMyAvatar: () -> Unit,
    onCancel: () -> Unit,
    onDelete: (() -> Unit)?,
    onSave: (AiPersona) -> Unit
) {
    var d by remember { mutableStateOf(src) }
    // 这一页里指代角色的字跟着正在编辑的这张卡自己的性别走（男→他 / 女→她 / 其余→ta）
    val pp = d.pronoun()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Veil)
            .swallowClicks()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal)
                )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        if (isNew) "新建人设卡" else "编辑人设卡",
                        color = TextMain, fontSize = 16.sp, fontWeight = FontWeight.SemiBold
                    )
                    Text(pp + "是谁、怎么说话，都在这里", color = TextFaint, fontSize = 10.sp)
                }
                TextButton(onClick = onCancel) { Text("取消", color = TextMuted, fontSize = 13.sp) }
                TextButton(onClick = { onSave(d.copy(name = d.name.trim().ifBlank { "未命名" })) }) {
                    Text("保存", color = PinkSoft, fontSize = 14.sp)
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp)
            ) {
                /* ---- 分节按「人设 1.2」那份文件的布局来：任务 / 角色 / 外表 / 经历 /
                       性格 / 喜好 / 输出示例 / 备注，另加记忆片段。写起来有地方可放，
                       模型读到的也是分段的，比一整坨散文更容易守住人设。 ---- */

                SetSection("基本", "名字显示在切换条上。导出不含 Key。") {
                    SetField("名字", d.name, hint = "凌薇", onChange = { d = d.copy(name = it) })
                    SetField(
                        pp + "怎么称呼你", d.call, hint = "你 / 您 / 主人 / 小名",
                        onChange = { d = d.copy(call = it) }
                    )
                    Spacer(Modifier.height(6.dp))
                    Text("性别", color = TextMuted, fontSize = 11.sp)
                    Spacer(Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Pron.OPTIONS.forEach { g ->
                            // 「不方便」存成空串：老卡读进来没有这一项，也归到这里
                            val on = if (g == Pron.UNSURE) d.gender.isBlank() else d.gender == g
                            SetChip(g, primary = on) {
                                d = d.copy(gender = if (g == Pron.UNSURE) "" else g)
                            }
                        }
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "只决定界面上指代这个角色的那个字：" + Pron.of(Pron.MALE) + " / " +
                                Pron.of(Pron.FEMALE) + " / " + Pron.of(Pron.UNSURE) +
                                "。它【不会】写进设定 —— 想让角色本人知道性别，写进下面的「角色」那一节。",
                        color = TextFaint, fontSize = 10.sp
                    )
                    Spacer(Modifier.height(6.dp))
                    Text("主题色", color = TextMuted, fontSize = 11.sp)
                    Spacer(Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AI_TINTS.forEach { c ->
                            Box(
                                modifier = Modifier
                                    .size(26.dp)
                                    .clip(CircleShape)
                                    .background(Color(c))
                                    .border(
                                        if (d.color == c) 2.dp else 1.dp,
                                        if (d.color == c) TextMain else Color.Transparent,
                                        CircleShape
                                    )
                                    .clickable { d = d.copy(color = c) }
                            )
                        }
                    }
                }

                SetSection("头像", "设了就显示在气泡旁。上传的图会先让你裁成方图。") {
                    AiAvatarRow(
                        ctx = ctx, label = pp + "的头像", avatarKey = src.id, tick = avatarTick,
                        onPick = onPickAvatar, onClear = onClearAvatar
                    )
                    Spacer(Modifier.height(6.dp))
                    AiAvatarRow(
                        ctx = ctx, label = "我的头像（所有角色共用）", avatarKey = AiStore.AVATAR_ME,
                        tick = myAvatarTick, onPick = onPickMyAvatar, onClear = onClearMyAvatar
                    )
                }

                SetSection("任务", pp + "要做什么。一两句就够，别写成说明书。") {
                    SetField(
                        "任务", d.task,
                        hint = "例：陪对方说话。情绪不好时先接住情绪，再给一个今天就能做的小步骤。",
                        single = false, lines = 3,
                        onChange = { d = d.copy(task = it) }
                    )
                }

                SetSection("角色", pp + "是谁、和对方什么关系。名字与称呼写在上面的「基本」里。") {
                    SetField(
                        "角色", d.role,
                        hint = "例：" + pp + "不太爱解释自己，也从不主动提起自己的来历。",
                        single = false, lines = 5,
                        onChange = { d = d.copy(role = it) }
                    )
                }

                SetSection("外表", "长相、衣着、给人的感觉。写细节比堆形容词管用。") {
                    SetField(
                        "外表", d.appearance,
                        hint = "例：头发总是松松挽着，几缕碎发垂在耳边。说话很慢，看人不急。",
                        single = false, lines = 5,
                        onChange = { d = d.copy(appearance = it) }
                    )
                }

                SetSection("经历", pp + "的来历，以及你们一起经历过的事。") {
                    SetField(
                        "经历", d.history,
                        hint = "例：" + pp + "陪对方走过一段见不到光的日子；记得对方说过的每一句话。",
                        single = false, lines = 6,
                        onChange = { d = d.copy(history = it) }
                    )
                }

                SetSection("性格", pp + "的脾气。矛盾的那一面也写出来，像「温柔但不软弱」。") {
                    SetField(
                        "性格", d.personality,
                        hint = "例：静默的温柔。疏离但不冷漠。温柔但不软弱。偶尔会突然使坏。",
                        single = false, lines = 6,
                        onChange = { d = d.copy(personality = it) }
                    )
                }

                SetSection("喜好", pp + "喜欢什么 —— 会影响" + pp + "主动聊什么。") {
                    SetField(
                        "喜好", d.likes,
                        hint = "例：栀子花、茶、星空；喜欢听对方讲琐碎的事。",
                        single = false, lines = 3,
                        onChange = { d = d.copy(likes = it) }
                    )
                }

                SetSection("输出示例", "一行一组「你：…／" + pp + "：…」，写三五组。最定语气。") {
                    SetField(
                        "输出示例", d.examples,
                        hint = "你：今天好累。\n" + pp + "：那就别撑着了，早点休息。",
                        single = false, lines = 6,
                        onChange = { d = d.copy(examples = it) }
                    )
                }

                SetSection("备注", "说话方式与禁忌。排在最后，模型最容易守住。") {
                    SetField(
                        "备注", d.notes,
                        hint = "例：短句、口语，一次 1~3 句；不要说自己是 AI；不要用 Markdown。",
                        single = false, lines = 6,
                        onChange = { d = d.copy(notes = it) }
                    )
                }

                SetSection(
                    "记忆片段与开场白",
                    "记忆片段一行一条、写短句。最多把 " + AiPersona.MAX_MEMORIES +
                            " 条拼进设定，写太多只是白花 token。"
                ) {
                    SetField(
                        "记忆片段", d.memories.joinToString("\n"),
                        hint = "例：" + pp + "泡的栀子花茶是清苦的，要喝到第二口才觉得甜。",
                        single = false, lines = 6,
                        onChange = { v ->
                            d = d.copy(
                                memories = v.split("\n").map { s -> s.trim() }.filter { s -> s.isNotBlank() }
                            )
                        }
                    )
                    SetField(
                        "开场白", d.greeting,
                        hint = "进入对话页、还没说话时" + pp + "先说的那一句；留空就不说。",
                        single = false, lines = 2,
                        onChange = { d = d.copy(greeting = it) }
                    )
                }

                SetSection("预览", "这就是真正发给模型的设定（不含 Key），空段自动省略。") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(PanelBgSoft)
                            .padding(10.dp)
                    ) {
                        Text(
                            d.copy(name = d.name.trim().ifBlank { "未命名" }).systemPrompt(""),
                            color = TextMuted, fontSize = 10.sp
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "小提示：名字与称呼只写在「基本」里，别写进「角色」 —— " +
                                "正文里的名字写死了，换个人设就会串味。",
                        color = TextFaint, fontSize = 10.sp
                    )
                }

                if (onDelete != null) {
                    Spacer(Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        SetChip("删除这张人设卡") { onDelete() }
                    }
                }

                Spacer(Modifier.height(48.dp))
            }
        }
    }
}

/* ================================ 小部件 ================================ */

/** 一行「说明 + 滑块 + 读数」：节奏那几项都用它，省得每处重写一遍排版 */
@Composable
private fun AiSliderRow(
    label: String,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    text: String,
    /** 拖动中：只改界面状态，不写盘 */
    onChange: (Float) -> Unit,
    /** 松手：这时候才落盘 —— 拖动中每帧写一次文件纯属浪费 */
    onDone: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = TextMuted, fontSize = 11.sp)
        Spacer(Modifier.width(8.dp))
        SoftSlider(
            value = value,
            valueRange = range,
            modifier = Modifier.weight(1f),
            onValueChange = onChange,
            onValueChangeFinished = onDone
        )
        Spacer(Modifier.width(8.dp))
        Text(text, color = TextMain, fontSize = 11.sp, modifier = Modifier.width(52.dp))
    }
}

/* ============================== 头像一行 ============================== */

/** 一个头像位：左边是圆形预览，点它或「选择」都能挑图（挑完会进裁剪）；设过才给「清除」 */
@Composable
private fun AiAvatarRow(
    ctx: Context,
    label: String,
    avatarKey: String,
    tick: Int,
    onPick: () -> Unit,
    onClear: () -> Unit
) {
    // ⚠️ 别在 remember 里直接读盘：那是在**组合期间**做文件 IO + 解码位图，
    //    小图也能卡一下，大图就是明显的掉帧。改成进组合后异步读。
    var bmp by remember { mutableStateOf<ImageBitmap?>(null) }
    LaunchedEffect(avatarKey, tick) {
        bmp = if (avatarKey.isBlank()) null
        else withContext(Dispatchers.IO) { AiStore.loadAvatar(ctx, avatarKey) }
    }
    // 委派属性没法智能转换，先取出来
    val shown = bmp
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(PanelBgSoft)
                .border(1.dp, PurpleMain.copy(alpha = 0.25f), CircleShape)
                .clickable { onPick() },
            contentAlignment = Alignment.Center
        ) {
            if (shown != null) {
                Image(
                    bitmap = shown,
                    contentDescription = null,
                    modifier = Modifier.size(42.dp).clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text("＋", color = TextFaint, fontSize = 16.sp)
            }
        }
        Spacer(Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(label, color = TextMain, fontSize = 12.sp)
            Text(
                if (shown == null) "未设置（不显示头像）" else "已设置",
                color = TextFaint, fontSize = 10.sp
            )
        }
        SetChip("选择") { onPick() }
        if (shown != null) {
            Spacer(Modifier.width(4.dp))
            SetChip("清除") { onClear() }
        }
    }
}

/* ========================= 内置默认卡：受限编辑 ========================= */

/**
 * 内置「纳凌薇」的编辑页 —— 只开放三样：主题色、对用户的称呼、双方头像。
 * 设定正文一概不在这里出现，也就无从改起。
 */
@Composable
/* ------------- 「AI 助手」自己的编辑页：只开三样 ------------- */

/**
 * 助手那张卡也是内置的（不落盘、不导出、不参与分享），但它和「纳凌薇」不同 ——
 * 它没有「人设」可言，所以能改的就三样：**叫什么、主题色、头像**。
 *
 * 命名放在这里，而不是像上一版那样在「对话」里单列一块：
 * 名字是这张卡的属性，就该在哪张卡上改，不用跑到别处找。
 */
private fun AiAssistantEditor(
    ctx: Context,
    avatarTick: Int,
    curName: String,
    curColor: Int,
    onPickAvatar: () -> Unit,
    onClearAvatar: () -> Unit,
    onPickMyAvatar: () -> Unit,
    onClearMyAvatar: () -> Unit,
    onSave: (String, Int) -> Unit,
    onCancel: () -> Unit
) {
    var name by remember { mutableStateOf(curName) }
    var color by remember {
        mutableIntStateOf(if (curColor != 0) curColor else AiDefaultPersona.ASSISTANT_COLOR)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Veil)
            .swallowClicks()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal)
                )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "AI 助手",
                        color = TextMain, fontSize = 16.sp, fontWeight = FontWeight.SemiBold
                    )
                    Text("这一张只开三样：叫什么、主题色、头像", color = TextFaint, fontSize = 10.sp)
                }
                TextButton(onClick = onCancel) { Text("取消", color = TextMuted, fontSize = 13.sp) }
                TextButton(onClick = { onSave(name.trim(), color) }) {
                    Text("保存", color = PinkSoft, fontSize = 14.sp)
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp)
            ) {
                SetSection(
                    "为什么只能改这些",
                    "助手是一把工具，不是角色：它没有人设、不写记忆、不主动开口。" +
                            "所以这里只开三样 —— 把它调成你看得顺眼、叫得顺口的样子。"
                ) {
                    SetField("叫什么", name, hint = "AI 助手", onChange = { name = it.take(12) })
                    Spacer(Modifier.height(10.dp))
                    Text("主题色", color = TextMuted, fontSize = 11.sp)
                    Spacer(Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AI_TINTS.forEach { c ->
                            Box(
                                modifier = Modifier
                                    .size(26.dp)
                                    .clip(CircleShape)
                                    .background(Color(c))
                                    .border(
                                        if (color == c) 2.dp else 1.dp,
                                        if (color == c) TextMain else Color.Transparent,
                                        CircleShape
                                    )
                                    .clickable { color = c }
                            )
                        }
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "列表里那个圆点、气泡边框都跟着它走。",
                        color = TextFaint, fontSize = 10.sp
                    )
                }

                SetSection("头像", "设了就显示在气泡旁。上传的图会先让你裁成方图。") {
                    AiAvatarRow(
                        ctx = ctx, label = "助手的头像", avatarKey = AiDefaultPersona.ASSISTANT_ID,
                        tick = avatarTick, onPick = onPickAvatar, onClear = onClearAvatar
                    )
                    Spacer(Modifier.height(6.dp))
                    AiAvatarRow(
                        ctx = ctx, label = "我的头像（所有角色共用）", avatarKey = AiStore.AVATAR_ME,
                        tick = avatarTick, onPick = onPickMyAvatar, onClear = onClearMyAvatar
                    )
                }

                Spacer(Modifier.height(40.dp))
            }
        }
    }
}


@Composable
private fun AiBuiltinEditor(

    ctx: Context,
    avatarTick: Int,
    onPickAvatar: () -> Unit,
    onClearAvatar: () -> Unit,
    onPickMyAvatar: () -> Unit,
    onClearMyAvatar: () -> Unit,
    onSave: (Int, String) -> Unit,
    onCancel: () -> Unit
) {
    // 原来这里是 remember { AiStore.loadDefaultOverride(ctx) } —— 那是**组合期间**读盘。
    // 挪到进组合之后再读；读回来之前先用内置默认值顶上。
    var color by remember { mutableIntStateOf(AiDefaultPersona.DEFAULT_COLOR) }
    var call by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        val cur = withContext(Dispatchers.IO) { AiStore.loadDefaultOverride(ctx) }
        color = if (cur.color != 0) cur.color else AiDefaultPersona.DEFAULT_COLOR
        call = cur.call
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Veil)
            .swallowClicks()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal)
                )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "默认人设 · 纳凌薇",
                        color = TextMain, fontSize = 16.sp, fontWeight = FontWeight.SemiBold
                    )
                    Text("这一张只能改外观相关的三样", color = TextFaint, fontSize = 10.sp)
                }
                TextButton(onClick = onCancel) { Text("取消", color = TextMuted, fontSize = 13.sp) }
                TextButton(onClick = { onSave(color, call.trim()) }) {
                    Text("保存", color = PinkSoft, fontSize = 14.sp)
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp)
            ) {
                SetSection(
                    "为什么只能改这些",
                    "这张卡的设定写在程序内部：不展示、不可改、不可复制、" +
                            "不会随任何导出流出去。这里只开一个口子，把它调成你看得顺眼的样子。"
                ) {
                    Text(
                        "主题色",
                        color = TextMuted, fontSize = 11.sp
                    )
                    Spacer(Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AI_TINTS.forEach { c ->
                            Box(
                                modifier = Modifier
                                    .size(26.dp)
                                    .clip(CircleShape)
                                    .background(Color(c))
                                    .border(
                                        if (color == c) 2.dp else 1.dp,
                                        if (color == c) TextMain else Color.Transparent,
                                        CircleShape
                                    )
                                    .clickable { color = c }
                            )
                        }
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "列表里那个圆点、气泡边框、还没说话时那句开场白的颜色，都跟着它走。",
                        color = TextFaint, fontSize = 10.sp
                    )
                    Spacer(Modifier.height(8.dp))
                    SetField(
                        "她怎么称呼你",
                        call,
                        hint = "留空 = 用「你」",
                        onChange = { call = it }
                    )
                }

                SetSection("头像", "设了就显示在气泡旁。上传的图会先让你裁成方图。") {
                    AiAvatarRow(
                        ctx = ctx, label = "她的头像", avatarKey = AiDefaultPersona.ID,
                        tick = avatarTick, onPick = onPickAvatar, onClear = onClearAvatar
                    )
                    Spacer(Modifier.height(6.dp))
                    AiAvatarRow(
                        ctx = ctx, label = "我的头像（所有角色共用）", avatarKey = AiStore.AVATAR_ME,
                        tick = avatarTick, onPick = onPickMyAvatar, onClear = onClearMyAvatar
                    )
                }

                Spacer(Modifier.height(40.dp))
            }
        }
    }
}

/** API Key 输入框：默认打码，旁边一颗「显示 / 隐藏」 */
@Composable
private fun AiSecretField(
    label: String,
    value: String,
    visible: Boolean,
    onToggleVisible: () -> Unit,
    onChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(label, color = TextMuted, fontSize = 11.sp)
            Spacer(Modifier.width(8.dp))
            Text(
                if (visible) "隐藏" else "显示",
                color = PinkSoft, fontSize = 10.sp,
                modifier = Modifier.clickable { onToggleVisible() }
            )
        }
        Spacer(Modifier.height(3.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onChange,
            singleLine = true,
            visualTransformation = if (visible) VisualTransformation.None
            else PasswordVisualTransformation(),
            placeholder = { Text("sk-…", color = TextFaint, fontSize = 11.sp) },
            textStyle = TextStyle(fontSize = 13.sp),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

/** 把名字变成安全的文件名（给导出的 .json 用） */
private fun safeFileName(name: String): String {
    val s = name.trim().map { c ->
        if (c.isLetterOrDigit() || c == '-' || c == '_') c else '_'
    }.joinToString("")
    return "persona-" + (if (s.isBlank()) "untitled" else s.take(24))
}
