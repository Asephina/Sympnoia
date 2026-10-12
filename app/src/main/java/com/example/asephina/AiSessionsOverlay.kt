package com.example.asephina

import androidx.activity.compose.BackHandler

/*
 * ============================================================================
 *  凌薇 AI —— 对话列表（会话管理）
 *  ---------------------------------------------------------------------------
 *  一个角色名下可以有很多段对话：这里按角色分组列出来（像聊天软件的会话列表）。
 *  · 点某一条 = 打开它，接着聊
 *  · 「改名」= 自己起标题；留空则回到自动命名（默认取第一句话）
 *  · 「删除」= 先弹确认框再删；删的是这一段的记录，人设卡与接口设置都不动
 *  · 「新建对话」= 给当前角色开一段新的
 *
 *  这一页画在 MainScreen 那一层（不是 pager 里面），所以里面的确认弹窗盖得住
 *  顶栏与底栏 —— 这也是当初把设置页提出去的原因。
 * ========================================================================== */

import android.content.Context
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 会话列表。两种用法：
 *   · 平时：管理用 —— 打开某一段、改名、删除；
 *   · 分享时（[pickFor] 非空）：变成「选一段对话，把内容发进去」，行上只有「分享到这里」。
 */
@Composable
fun AiSessionsOverlay(
    ctx: Context,
    onClose: () -> Unit,
    pickFor: String = "",
    pickLabel: String = "",
    onPicked: (AiSession) -> Unit = {}
) {
    // 系统返回键 / 边缘返回手势 = 关闭会话列表
    BackHandler { onClose() }
    val scope = rememberCoroutineScope()
    val picking = pickFor.isNotBlank()

    val personas = remember { mutableStateListOf<AiPersona>() }
    val sessions = remember { mutableStateListOf<AiSession>() }
    var renaming by remember { mutableStateOf<AiSession?>(null) }
    var renameText by remember { mutableStateOf("") }
    var ask by remember { mutableStateOf<Triple<String, String, () -> Unit>?>(null) }
    var msg by remember { mutableStateOf("") }

    suspend fun reload() {
        val ps = withContext(Dispatchers.IO) { AiStore.ensure(ctx); AiStore.allPersonas(ctx) }
        val ss = withContext(Dispatchers.IO) { AiStore.loadSessions(ctx) }
        personas.clear(); personas.addAll(ps)
        sessions.clear(); sessions.addAll(ss.sortedByDescending { it.updatedAt })
    }

    LaunchedEffect(Unit) { reload() }

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
                        if (picking) "分享到哪一段对话？" else "对话列表",
                        color = TextMain, fontSize = 16.sp, fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        if (picking) (pickLabel.ifBlank { "选一段，这条内容会作为你的发言发进去" })
                        else "按角色分组。点一条接着聊，可以改名，也可以删掉某一段。",
                        color = TextFaint, fontSize = 10.sp
                    )
                }
                TextButton(onClick = onClose) {
                    Text(if (picking) "取消" else "关闭", color = PinkSoft, fontSize = 14.sp)
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp)
            ) {
                if (picking && pickFor.isNotBlank()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(PanelBgSoft)
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            pickFor.take(160) + if (pickFor.length > 160) "…" else "",
                            color = TextMuted, fontSize = 10.sp, maxLines = 4
                        )
                    }
                    Spacer(Modifier.height(6.dp))
                }
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    SetChip(if (picking) "＋ 新建一段并分享进去" else "＋ 新建对话", primary = true) {
                        val pid = AiBus.currentPersonaId
                        if (pid.isBlank()) {
                            msg = "先回对话页选一个角色"
                        } else if (picking) {
                            msg = "新的一段要等对话页建好；这里先选一段已有的，或回对话页点「新对话」"
                        } else {
                            AiBus.requestNew++
                            AiBus.tick++
                            onClose()
                        }
                    }
                }
                Spacer(Modifier.height(10.dp))

                var shown = 0
                personas.forEach { p ->
                    val mine = sessions.filter { it.personaId == p.id }
                    // 默认人设名下的空列表也显示出来，免得让人以为「没有这个角色」
                    if (mine.isEmpty() && p.id != AiDefaultPersona.ID) return@forEach
                    shown++
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 6.dp, bottom = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(p.tint()))
                        Spacer(Modifier.width(8.dp))
                        Text(
                            p.name.ifBlank { "未命名" } +
                                    if (p.builtin) "　·　默认" else "",
                            color = PinkSoft, fontSize = 13.sp, fontWeight = FontWeight.Medium
                        )
                        Spacer(Modifier.width(6.dp))
                        Text("(" + mine.size + ")", color = TextFaint, fontSize = 10.sp)
                    }
                    if (mine.isEmpty()) {
                        Text(
                            "　还没有对话。回对话页说一句，就会自动开一段。",
                            color = TextFaint, fontSize = 10.sp
                        )
                    }
                    mine.forEach { s ->
                        AiSessionRow(
                            s = s,
                            tint = p.tint(),
                            current = s.id == AiBus.currentSessionId,
                            pickMode = picking,
                            onShare = { onPicked(s) },
                            onOpen = {
                                AiBus.openSessionId = s.id
                                AiBus.tick++
                                onClose()
                            },
                            onRename = {
                                renaming = s
                                renameText = s.title
                            },
                            onDelete = {
                                ask = Triple(
                                    "删除这一段对话？",
                                    "「" + s.display() + "」里的 " + s.count + " 条消息会被永久删除，" +
                                            "人设卡与接口设置都不受影响。"
                                ) {
                                    scope.launch {
                                        // NonCancellable：删完会顺手 onClose，
                                        // 这个 scope 属于本页，页面一走它就被取消 ——
                                        // 没有它的话「已删除」可能只是嘴上说说，文件还在
                                        withContext(NonCancellable + Dispatchers.IO) {
                                            AiStore.deleteSession(ctx, s.id)
                                        }
                                        if (s.id == AiBus.currentSessionId) AiBus.currentSessionId = ""
                                        reload()
                                        AiBus.tick++
                                        msg = "已删除"
                                    }
                                }
                            }
                        )
                    }
                }

                // 兜底：挂在未知/空角色名下的会话也照样列出来。
                // 之前正是这类「孤儿会话」被分组过滤掉，看起来就像对话凭空消失。
                val known = personas.map { it.id }.toSet()
                val orphans = sessions.filter { it.personaId !in known }
                if (orphans.isNotEmpty()) {
                    shown++
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp).clip(CircleShape).background(TextFaint)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "未归属（" + orphans.size + "）",
                            color = TextMuted, fontSize = 13.sp, fontWeight = FontWeight.Medium
                        )
                    }
                    Text(
                        "　这些对话的归属角色丢了（早期版本的毛病），点一下会挂回当前角色。",
                        color = TextFaint, fontSize = 10.sp
                    )
                    orphans.forEach { s ->
                        AiSessionRow(
                            s = s,
                            tint = TextFaint,
                            current = s.id == AiBus.currentSessionId,
                            pickMode = picking,
                            onShare = {
                                scope.launch {
                                    val pid = AiBus.currentPersonaId
                                    if (pid.isNotBlank()) {
                                        withContext(Dispatchers.IO) {
                                            AiStore.adoptOrphanSessions(ctx, pid)
                                        }
                                    }
                                    reload()
                                    onPicked(s.copy(personaId = pid.ifBlank { s.personaId }))
                                }
                            },
                            onOpen = {
                                scope.launch {
                                    val pid = AiBus.currentPersonaId
                                    if (pid.isNotBlank()) {
                                        withContext(Dispatchers.IO) {
                                            AiStore.adoptOrphanSessions(ctx, pid)
                                        }
                                    }
                                    reload()
                                    AiBus.openSessionId = s.id
                                    AiBus.tick++
                                    onClose()
                                }
                            },
                            onRename = {
                                renaming = s
                                renameText = s.title
                            },
                            onDelete = {
                                ask = Triple(
                                    "删除这一段对话？",
                                    "「" + s.display() + "」里的 " + s.count + " 条消息会被永久删除。"
                                ) {
                                    scope.launch {
                                        withContext(NonCancellable + Dispatchers.IO) {
                                            AiStore.deleteSession(ctx, s.id)
                                        }
                                        // 删掉的正好是当前这一段：把标记清掉，
                                        // 否则对话页回来时会以为「当前」还在（分支里漏过这一句）
                                        if (s.id == AiBus.currentSessionId) AiBus.currentSessionId = ""
                                        reload()
                                        AiBus.tick++
                                        msg = "已删除"
                                    }
                                }
                            }
                        )
                    }
                }

                if (shown == 0) {
                    Text("还没有任何对话。", color = TextFaint, fontSize = 11.sp)
                }

                if (msg.isNotBlank()) {
                    Spacer(Modifier.height(8.dp))
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

                Spacer(Modifier.height(16.dp))
                Text(
                    "所有对话都只存在这台手机里（Asephina/ai/sessions/），" +
                            "每天会自动打一份数据包，也可以随时手动导出。",
                    color = TextFaint, fontSize = 10.sp
                )
                Spacer(Modifier.height(40.dp))
            }
        }
    }

    /* ---------------- 改名 ---------------- */
    renaming?.let { s ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Veil)
                .swallowClicks { renaming = null },
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(28.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(PanelBg)
                    .goldEdge(20.dp)
                    .border(1.dp, PurpleMain.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                    .swallowClicks()
                    .padding(11.dp)
            ) {
                Text("给这段对话起个名字", color = TextMain, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(4.dp))
                Text(
                    "留空 = 回到自动命名（取第一句话）。",
                    color = TextFaint, fontSize = 10.sp
                )
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(
                    value = renameText,
                    onValueChange = { renameText = it },
                    singleLine = true,
                    placeholder = { Text("例：关于选课的讨论", color = TextFaint, fontSize = 11.sp) },
                    textStyle = TextStyle(fontSize = 13.sp),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = { renaming = null }) {
                        Text("取消", color = TextMuted, fontSize = 14.sp)
                    }
                    Spacer(Modifier.width(6.dp))
                    SetChip("保存", primary = true) {
                        val t = renameText.trim()
                        scope.launch {
                            withContext(Dispatchers.IO) { AiStore.renameSession(ctx, s.id, t) }
                            renaming = null
                            reload()
                            AiBus.tick++
                            msg = if (t.isBlank()) "已恢复自动命名" else "已改名为「$t」"
                        }
                    }
                }
            }
        }
        return
    }

    // 确认弹窗画在最后：Compose 同层按书写顺序叠，写在前面会被整页盖住
    ask?.let { a ->
        ConfirmOverlay(
            title = a.first,
            message = a.second,
            danger = true,
            okText = "删除",
            onConfirm = {
                val act = a.third
                ask = null
                act()
            },
            onCancel = { ask = null }
        )
    }
}

/* ============================== 收藏的对话 ============================== */
/* ========================= 主动互动的弹窗 ========================= */

/**
 * 她主动说的那一句话。
 * 头像的有无、叫什么名字，全按用户自己的设置走 —— 和对话页里那套一致。
 * 只显示第一句；分条剩下的收在「还有 N 条」里（去对话页看完整的）。
 */
@Composable
fun AiProactiveOverlay(
    ctx: Context,
    onGo: () -> Unit,
    onClose: () -> Unit
) {
    // 系统返回键 / 边缘返回手势 = 关掉这条弹窗（等同于「先不看」）
    BackHandler { onClose() }

    val name = AiBus.proactiveName
    val text = AiBus.proactiveText
    val tick = AiBus.proactiveTick

    var avatar by remember { mutableStateOf<androidx.compose.ui.graphics.ImageBitmap?>(null) }
    LaunchedEffect(name) {
        avatar = withContext(Dispatchers.IO) { AiStore.loadAvatar(ctx, "") }
    }

    // 「出现」的动画：整体从 0.86 放大 + 淡入，头像再晚一拍浮上来。
    // 每来一条（tick 变化）重播一次 —— 光秃秃地「啪」一下冒出来太生硬。
    var shown by remember(tick) { mutableStateOf(false) }
    LaunchedEffect(tick) {
        shown = false
        kotlinx.coroutines.delay(16)
        shown = true
    }
    val pop by animateFloatAsState(
        targetValue = if (shown) 1f else 0.86f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "pop"
    )
    val fade by animateFloatAsState(
        targetValue = if (shown) 1f else 0f,
        animationSpec = tween(220),
        label = "fade"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xB005060F).copy(alpha = 0.70f * fade))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClose() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.88f)
                .graphicsLayer {
                    scaleX = pop
                    scaleY = pop
                    alpha = fade
                }
                .clip(RoundedCornerShape(22.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(PanelBgSoft, PanelBg)
                    )
                )
                .border(1.dp, PinkSoft.copy(alpha = 0.35f), RoundedCornerShape(22.dp))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { }
                .padding(horizontal = 18.dp, vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 头像（没设就整个不显示，和对话页一致）
            if (avatar != null) {
                androidx.compose.foundation.Image(
                    bitmap = avatar!!,
                    contentDescription = null,
                    modifier = Modifier
                        .size(62.dp)
                        .clip(CircleShape)
                        .border(1.5.dp, PinkSoft.copy(alpha = 0.55f), CircleShape)
                )
                Spacer(Modifier.height(10.dp))
            }
            if (name.isNotBlank()) {
                Text(
                    name,
                    color = PinkSoft, fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp
                )
                Spacer(Modifier.height(10.dp))
            }
            // 一句话本身：两侧留白，字号比正文大一点，读起来像「她说的话」
            Text(
                text,
                color = TextMain, fontSize = 15.sp,
                lineHeight = 23.sp,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(6.dp))
            Text(
                "她主动说的",
                color = TextFaint, fontSize = 9.sp, letterSpacing = 0.5.sp
            )
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(PurpleDeep.copy(alpha = 0.18f))
                        .border(1.dp, PurpleMain.copy(alpha = 0.20f), RoundedCornerShape(14.dp))
                        .clickable { onClose() }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) { Text("先不看", color = TextMuted, fontSize = 13.sp) }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(PurpleDeep.copy(alpha = 0.75f), PinkSoft.copy(alpha = 0.45f))
                            )
                        )
                        .border(1.dp, PinkSoft.copy(alpha = 0.45f), RoundedCornerShape(14.dp))
                        .clickable { onGo() }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) { Text("去看看", color = TextMain, fontSize = 13.sp, fontWeight = FontWeight.Medium) }
            }
        }
    }
}

/* ============================== 收藏的对话 ============================== */

/**
 * 收藏列表：批量保存下来的对话快照。
 * 点一条展开看内容（最上方是那几条消息发生的时间），可以改标题、删掉。
 */
@Composable
fun AiFavOverlay(
    ctx: Context,
    tick: Int,
    onClose: () -> Unit,
    onChanged: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var favs by remember { mutableStateOf<List<AiFav>>(emptyList()) }
    var open by remember { mutableStateOf<AiFav?>(null) }
    var renaming by remember { mutableStateOf<AiFav?>(null) }
    var renameText by remember { mutableStateOf("") }
    var ask by remember { mutableStateOf<AiFav?>(null) }
    var localTick by remember { mutableIntStateOf(0) }

    // 系统返回键 / 边缘返回手势：正在看某一条就先退回列表，在列表里才关闭整页。
    // 一层一层退，符合用户的直觉。
    BackHandler {
        when {
            renaming != null -> renaming = null
            ask != null -> ask = null
            open != null -> open = null
            else -> onClose()
        }
    }

    LaunchedEffect(tick, localTick) {
        favs = withContext(Dispatchers.IO) { AiStore.loadFavs(ctx) }
        // 展开着的那条也要跟着刷新（改过标题之后）
        open = open?.let { o -> favs.firstOrNull { it.id == o.id } }
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
                        if (open == null) "收藏的对话（" + favs.size + "）"
                        else "收藏 · " + open!!.display(),
                        color = TextMain, fontSize = 16.sp, fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        // 预览时把这一次对话发生的时间放在最上方
                        if (open == null) "长按对话里的气泡可以多选，再点「保存」存到这里。"
                        else "这段对话发生在 " + Dates.stamp(open!!.fromAt) + " " + Dates.hhmm(open!!.fromAt),
                        color = TextFaint, fontSize = 10.sp
                    )
                }
                if (open != null) {
                    TextButton(onClick = { open = null }) { Text("返回", color = TextMuted, fontSize = 13.sp) }
                }
                TextButton(onClick = onClose) { Text("关闭", color = PinkSoft, fontSize = 14.sp) }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp)
            ) {
                if (open == null) {
                    if (favs.isEmpty()) Text("还没有收藏。", color = TextFaint, fontSize = 11.sp)
                    favs.forEach { f ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(PanelBgSoft)
                                .clickable { open = f }
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(PinkSoft))
                            Spacer(Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    f.display(),
                                    color = TextMain, fontSize = 13.sp, maxLines = 1,
                                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                )
                                // 标题下面是**保存**这条的时间
                                Text(
                                    "保存于 " + Dates.stamp(f.savedAt) + " " + Dates.hhmm(f.savedAt) +
                                            "　·　" + f.msgs.size + " 条",
                                    color = TextFaint, fontSize = 10.sp
                                )
                            }
                            SetChip("改名") { renaming = f; renameText = f.title }
                            Spacer(Modifier.width(4.dp))
                            SetChip("删") { ask = f }
                        }
                    }
                } else {
                    val f = open!!
                    f.msgs.forEach { m ->
                        val who = if (m.role == "assistant") f.persona.ifBlank { "ta" } else "你"
                        Text(
                            Dates.stamp(m.ts) + " " + Dates.hhmm(m.ts) + "　" + who,
                            color = TextFaint, fontSize = 9.sp,
                            modifier = Modifier.padding(top = 6.dp)
                        )
                        Text(
                            if (m.img.isNotBlank()) "［图片］" + m.content else m.content,
                            color = TextMain, fontSize = 13.sp
                        )
                    }
                }
                Spacer(Modifier.height(48.dp))
            }
        }
    }

    renaming?.let { f ->
        ConfirmOverlay(
            title = "改个标题",
            message = "留空就回到「角色名 · 保存日期」。",
            danger = false,
            okText = "保存",
            onConfirm = {
                val t = renameText
                renaming = null
                scope.launch {
                    withContext(Dispatchers.IO) { AiStore.renameFav(ctx, f.id, t) }
                    localTick++
                    onChanged()
                }
            },
            onCancel = { renaming = null },
            extra = {
                OutlinedTextField(
                    value = renameText,
                    onValueChange = { renameText = it.take(40) },
                    singleLine = true,
                    textStyle = TextStyle(fontSize = 13.sp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        )
    }

    ask?.let { f ->
        ConfirmOverlay(
            title = "删掉这条收藏？",
            message = "「" + f.display() + "」会被永久删除，对话本身不受影响。",
            danger = true,
            okText = "删除",
            onConfirm = {
                ask = null
                scope.launch {
                    withContext(Dispatchers.IO) { AiStore.deleteFav(ctx, f.id) }
                    if (open?.id == f.id) open = null
                    localTick++
                    onChanged()
                }
            },
            onCancel = { ask = null }
        )
    }
}

/* ============================== 列表里的一行 ============================== */

@Composable
private fun AiSessionRow(
    s: AiSession,
    tint: Color,
    current: Boolean,
    pickMode: Boolean,
    onShare: () -> Unit,
    onOpen: () -> Unit,
    onRename: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (current) tint.copy(alpha = 0.14f) else PanelBgSoft)
            .border(
                1.dp,
                if (current) tint.copy(alpha = 0.5f) else PurpleMain.copy(alpha = 0.15f),
                RoundedCornerShape(12.dp)
            )
            .clickable { if (pickMode) onShare() else onOpen() }
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                s.display() + if (current) "　·　当前" else "",
                color = TextMain, fontSize = 12.sp
            )
            Text(
                Dates.stamp(s.updatedAt) + " " + Dates.hhmm(s.updatedAt) + "　·　" + s.count + " 条",
                color = TextFaint, fontSize = 9.sp
            )
        }
        if (pickMode) {
            SetChip("分享到这里", primary = true) { onShare() }
        } else {
            SetChip("改名") { onRename() }
            Spacer(Modifier.width(4.dp))
            SetChip("删除") { onDelete() }
        }
    }
}
