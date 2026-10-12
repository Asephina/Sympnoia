package com.example.asephina

/*
 * ============================================================================
 *  凌薇 AI —— 数据的定期导出
 *  ---------------------------------------------------------------------------
 *  和「记录」那部分一样的思路：只放在应用数据目录里的东西，卸载就没了，
 *  所以 AI 这边的自定义人设卡、记忆片段与对话记录也要定期送到外面去。
 *
 *  两件事：
 *    ① 数据包（覆盖式，只留一份）
 *         /Documents/<导出文件夹>/ai/Sympnoia-ai.zip
 *       里面是 personas.json（用户自建的人设卡，记忆片段在里面）+ chats 目录下的 .json。
 *       **不含 ai_key.json** —— Key 永远不会被写进任何导出物。
 *
 *    ② Word 文档（每天一份，滚动保留最近 7 份，与记录那边的规则一致）
 *         /Documents/<导出文件夹>/auto/Sympnoia-ai_auto_<日期>.docx
 *       给人看的版本：人设卡、记忆片段、按角色分节的完整聊天记录。
 *       前缀是 Sympnoia-ai_auto_，与记录那边的 Asephina_auto_ 互不干扰，
 *       两边各自数自己的 7 份。
 *
 *  手动导出（另存到 /Documents/<导出文件夹>/ 根下，不限份数）在设置页里点。
 *
 *  【内置的默认人设卡不参与导出】
 *   纳凌薇那张卡写死在 AiDefaultPersona 里，既不落盘也不进 zip；
 *   docx 里只提一句「内置默认人设：纳凌薇」，不展开它的设定内容。
 * ========================================================================== */

import android.content.Context
import android.os.Build
import android.provider.MediaStore
import org.json.JSONArray
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

object AiBackup {

    /** 自动 Word 的前缀：与记录那边的 Asephina_auto_ 分开，各自保留 7 份 */
    private const val PREFIX = "Sympnoia-ai_auto_"

    /** 覆盖式数据包的文件名：永远是这一个 */
    private const val BUNDLE = "Sympnoia-ai.zip"

    /** 自动 Word 保留几份 */
    private const val KEEP = 7

    private const val MIME_DOCX =
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document"

    private const val MIME_ZIP = "application/zip"

    /* ======================================================================
     *  一、给界面用的一句话结果（都在 IO 线程调用）
     * ==================================================================== */

    /**
     * 每天第一次打开应用时调一次，跟着统一备份一起跑。
     *
     * ⚠️ 现在这里**只负责收拾**：数据镜像与 Word 都已经由 [AutoBackup] 统一出一份，
     *    AI 的人设卡 / 对话 / 记忆 / 头像 / 图片本来就在数据目录里，
     *    会被那份镜像整份带走，Word 里也有一章。
     *    以前这里另写一份 AI 专用的 zip 与 docx，结果是同一个文件夹里躺着两套几乎一样的东西。
     */
    fun runDaily(ctx: Context): String {
        // 顺手清掉没有任何消息引用的聊天图片（删过对话之后它们就成孤儿了）
        val n = try {
            AiStore.pruneMedia(ctx)
        } catch (e: Exception) {
            0
        }
        // 旧版遗留：AI 单独那份数据包已经并进统一镜像，见过就删掉，别再占地方
        try {
            Pub.deleteInSub(ctx, "ai", BUNDLE)
        } catch (e: Exception) {
        }
        return if (n > 0) "AI 图片收拾：清掉 $n 张没人引用的图" else "AI 数据已随统一镜像与文档一起导出"
    }

    /** 给人看的路径 */
    fun bundlePathText(): String = Pub.pathText("ai") + BUNDLE

    fun autoFolderText(): String = Pub.pathText(Pub.SUB_AUTO)

    /* ======================================================================
     *  二、数据包（覆盖式）
     * ==================================================================== */

    /**
     * AI 数据的 zip：人设卡、对话、记忆、头像、图片。
     * **API Key 不在其中** —— 这个包会落到公共目录，任何文件管理器都能打开。
     *
     * ⚠️ 直接往传进来的流里写，不在内存里攒一份完整的包：
     *    聊天图片加起来可能几十兆，先 readBytes 再 toByteArray 会有两三份副本，
     *    每日自动备份一跑就是一次 OOM 风险。文件用流复制，不进内存。
     */
    private fun writeBundle(ctx: Context, rawOut: java.io.OutputStream) {
        ZipOutputStream(rawOut).use { zos ->
            fun put(name: String, bytes: ByteArray) {
                try {
                    zos.putNextEntry(ZipEntry(name))
                    zos.write(bytes)
                    zos.closeEntry()
                } catch (e: Exception) {
                    try {
                        zos.closeEntry()
                    } catch (e2: Exception) {
                    }
                }
            }

            /** 复制磁盘上的一个文件（图片这类大文件走它，不整份读进内存） */
            fun putFile(entry: String, f: java.io.File) {
                try {
                    zos.putNextEntry(ZipEntry(entry))
                    java.io.FileInputStream(f).use { it.copyTo(zos) }
                    zos.closeEntry()
                } catch (e: Exception) {
                    try {
                        zos.closeEntry()
                    } catch (e2: Exception) {
                    }
                }
            }

            put(
                // ⚠️ 条目名必须带 ai/ 前缀：这份包是给「花房 · 数据 → 从备份恢复」用的，
                //    那个恢复是按 zip 里的路径原样写回数据目录的。不带前缀的话，
                //    personas.json / sessions/ 会被写到 Asephina/ 根下，AI 数据等于没恢复，
                //    还会在根目录留下一堆垃圾。
                //    这个名字与 Pub.NOTE_FILE 一致，恢复时会被自动跳过。
                "Sympnoia-说明.txt",
                ("Sympnoia · 凌薇 AI 数据包\n" +
                        "导出时间：" + Dates.todayKey() + " " + Dates.hhmm(System.currentTimeMillis()) + "\n" +
                        "包含：ai/personas.json（你自建的人设卡与记忆片段）、" +
                        "ai/sessions/（全部对话，索引 + 每段一份）、ai/memory/（模型自己总结的长期记忆）、" +
                        "ai/avatars/（头像图片）、" +
                        "ai/default_card.json（内置默认人设你改过的色与称呼）\n" +
                        "不含：API Key（出于安全特意排除，换设备后需要重新填写）\n" +
                        "恢复：应用内「花房 · 数据 → 从备份恢复」，选中本文件即可。\n")
                    .toByteArray(Charsets.UTF_8)
            )

            val arr = JSONArray()
            AiStore.loadPersonas(ctx).forEach { arr.put(it.toJson()) }
            put("ai/personas.json", arr.toString(2).toByteArray(Charsets.UTF_8))

            // 内置卡允许改的那两项（色与称呼）
            put(
                "ai/default_card.json",
                org.json.JSONObject()
                    .put("color", AiStore.loadDefaultOverride(ctx).color)
                    .put("call", AiStore.loadDefaultOverride(ctx).call)
                    .toString(2).toByteArray(Charsets.UTF_8)
            )

            // 会话：一份索引 + 每段一份。
            // ⚠️ 索引要按 AiStore.saveSessions 的格式写（{"ver":2,"items":[…]})，
            //    写成裸数组的话恢复回去 loadSessions 会读到 0 段对话。
            val sessions = AiStore.loadSessions(ctx)
            val idx = JSONArray()
            sessions.forEach { idx.put(it.toJson()) }
            put(
                "ai/sessions.json",
                org.json.JSONObject().put("ver", 2).put("items", idx).toString(2)
                    .toByteArray(Charsets.UTF_8)
            )
            sessions.forEach { s ->
                val f = AiStore.sessionFile(ctx, s.id)
                if (f.exists()) {
                    try {
                        put("ai/sessions/${s.id}.json", f.readBytes())
                    } catch (e: Exception) {
                    }
                }
            }

            // 头像：有哪个带哪个（她的按人设 id 命名，我的是 me.png）
            (AiStore.allPersonas(ctx).map { it.id } + AiStore.AVATAR_ME).forEach { k ->
                val f = AiStore.avatarFile(ctx, k)
                if (f.exists()) {
                    try {
                        put("ai/avatars/${f.name}", f.readBytes())
                    } catch (e: Exception) {
                    }
                }
            }

            // 她自己总结出来的长期记忆（跨会话共用）
            AiStore.allPersonas(ctx).forEach { p ->
                val mem = AiStore.loadMemories(ctx, p.id)
                if (mem.isNotEmpty()) {
                    val a = JSONArray()
                    mem.forEach { a.put(it) }
                    try {
                        put(
                            "ai/memory/${p.id}.json",
                            org.json.JSONObject().put("ver", 1).put("items", a).toString(2)
                                .toByteArray(Charsets.UTF_8)
                        )
                    } catch (e: Exception) {
                    }
                }
            }

            // 图片：聊到一半发出去的照片与表情包。
            // 不带上它们的话，恢复出来的对话里只剩一句「［图片已不在］」。
            listOf("media", "stickers").forEach { sub ->
                File(AiStore.dir(ctx), sub).listFiles()?.forEach { f ->
                    if (f.isFile) putFile("ai/$sub/${f.name}", f)
                }
            }
        }
    }

    /**
     * 清理自动 Word。
     * 现在统一由 [Pub.pruneAutoDocs] 处理：记录与 AI 两种文档**一起按天算**，
     * 只保留最近 7 天，超出的整日覆盖。这里不再单独清理，
     * 免得两边各算各的、上限翻倍。
     */
    fun pruneAutoNow(ctx: Context): Int = Pub.pruneAutoDocs(ctx)

    private fun stamp(): String =
        SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(Date())

    /* ======================================================================
     *  三、Word 文档（手写最小 OOXML，不引第三方库）
     * ==================================================================== */

    private fun buildDocx(ctx: Context): ByteArray = aiChapterXml(ctx).toByteArray(Charsets.UTF_8)

    /**
     * AI 那一章的段落 XML。
     * 由主文档 [WordExport] 拼进去 —— 全应用只出一份 Word，
     * 记录与 AI 不再是两个几乎一样、还得让人挑一个的文件。
     */
    fun aiChapterXml(ctx: Context): String {
        val body = StringBuilder()

        fun para(text: String, bold: Boolean = false, size: Int = 0, indent: Boolean = false) {
            body.append("<w:p><w:pPr>")
            // ⚠️ OOXML 对 pPr 里的子元素顺序有要求：spacing 必须排在 ind 之前，
            //    写反了 Word / WPS 可能提示「内容有问题」并自动修复。
            body.append("<w:spacing w:after=\"40\"/>")
            if (indent) body.append("<w:ind w:left=\"260\"/>")
            body.append("</w:pPr>")
            body.append("<w:r><w:rPr>")
            if (bold) body.append("<w:b/>")
            if (size > 0) body.append("<w:sz w:val=\"").append(size).append("\"/>")
            body.append("</w:rPr><w:t xml:space=\"preserve\">").append(esc(text)).append("</w:t></w:r></w:p>")
        }

        val myCards = AiStore.loadPersonas(ctx)

        para("Sympnoia · 凌薇 AI 存档", bold = true, size = 36)
        para(
            "导出时间：" + Dates.todayKey() + " " + Dates.hhmm(System.currentTimeMillis()) +
                    "　·　自定义人设卡 " + myCards.size + " 张"
        )
        para("内置默认人设：纳凌薇（随应用内置，其设定不在本文档中展开）")
        para("")

        /* ---------------- 人设卡与记忆片段 ---------------- */
        para("一、人设卡与记忆片段", bold = true, size = 28)
        para("")
        if (myCards.isEmpty()) {
            para("（没有自建的人设卡）", indent = true)
        } else {
            myCards.forEach { p ->
                para(
                    "◆ " + p.name.ifBlank { "未命名" } +
                            "（称呼对方：" + p.call.ifBlank { "你" } + "）", bold = true
                )
                if (p.task.isNotBlank()) para("任务：" + p.task, indent = true)
                if (p.role.isNotBlank()) para("角色：" + p.role, indent = true)
                if (p.appearance.isNotBlank()) para("外表：" + p.appearance, indent = true)
                if (p.history.isNotBlank()) para("经历：" + p.history, indent = true)
                if (p.personality.isNotBlank()) para("性格：" + p.personality, indent = true)
                if (p.likes.isNotBlank()) para("喜好：" + p.likes, indent = true)
                if (p.examples.isNotBlank()) para("输出示例：" + p.examples, indent = true)
                if (p.notes.isNotBlank()) para("备注：" + p.notes, indent = true)
                if (p.greeting.isNotBlank()) para("开场白：" + p.greeting, indent = true)
                if (p.memories.isNotEmpty()) {
                    para("记忆片段（" + p.memories.size + " 条）：", indent = true)
                    p.memories.forEach { m -> para("· " + m, indent = true) }
                }
                val auto = AiStore.loadMemories(ctx, p.id)
                if (auto.isNotEmpty()) {
                    para(p.pronoun() + "记住的事（" + auto.size + " 条，从对话里总结出来的）：", indent = true)
                    auto.forEach { m -> para("· " + m, indent = true) }
                }
                para("")
            }
        }

        /* ---------------- 对话记录 ---------------- */
        para("二、对话记录", bold = true, size = 28)
        para("")
        val sessions = AiStore.loadSessions(ctx).sortedByDescending { it.updatedAt }
        var any = false
        // 按角色分组导出；角色的名字与第三人称用那张卡自己的（男→他 / 女→她 / 其余→ta）
        fun dumpGroup(title: String, mine: List<AiSession>, pronounOf: (String) -> String) {
            if (mine.isEmpty()) return
            any = true
            para("◆ " + title, bold = true)
            mine.forEach { s ->
                val msgs = AiStore.loadSessionMsgs(ctx, s.id)
                para(
                    "【" + s.display() + "】" + Dates.stamp(s.createdAt) + " 起，" + msgs.size + " 条",
                    indent = true
                )
                msgs.forEach { m ->
                    val who = if (m.role == "assistant") pronounOf(s.personaId) else "你"
                    // Word 里塞不进图片，至少标一句，免得恢复时看不出这里有张图
                    val imgNote = if (m.img.isNotBlank()) "［图片］" else ""
                    para(
                        Dates.stamp(m.ts) + " " + Dates.hhmm(m.ts) + "　" + who + "：" + imgNote + m.content,
                        indent = true
                    )
                }
                para("")
            }
        }
        val personas = AiStore.allPersonas(ctx)
        personas.forEach { p ->
            dumpGroup(p.name.ifBlank { "未命名" }, sessions.filter { it.personaId == p.id }) { _ ->
                p.name.ifBlank { p.pronoun() }
            }
        }
        // 兜底：归属角色丢了的「孤儿会话」也照样导出。
        // 以前只遍历人设卡，这些对话在 Word 里会整段消失（zip 里还在，但看文档的人以为丢了）。
        val known = personas.map { it.id }.toSet()
        dumpGroup("未归属", sessions.filter { it.personaId !in known }) { "ta" }
        if (!any) para("（还没有对话记录）", indent = true)

        /* ---------------- 收藏的对话 ---------------- */
        val favs = AiStore.loadFavs(ctx)
        if (favs.isNotEmpty()) {
            para("")
            para("三、收藏的对话（" + favs.size + " 条）", bold = true, size = 28)
            para("")
            favs.forEach { f ->
                para(
                    "◆ " + f.display() + "　（保存于 " + Dates.stamp(f.savedAt) + " " +
                            Dates.hhmm(f.savedAt) + "）", bold = true
                )
                f.msgs.forEach { m ->
                    val who = if (m.role == "assistant") f.persona.ifBlank { "ta" } else "你"
                    val imgNote = if (m.img.isNotBlank()) "［图片］" else ""
                    para(
                        Dates.stamp(m.ts) + " " + Dates.hhmm(m.ts) + "　" + who + "：" + imgNote + m.content,
                        indent = true
                    )
                }
                para("")
            }
        }

        para("")
        para("说明：API Key 不会被写入任何导出文件；换设备后请在「AI 设置」里重新填写。")
        para("说明：头像、人设卡、对话、记忆与图片一并存在数据镜像 / 手动数据包（.zip）里，Word 只放文字。")

        // 只回段落 XML：整份文档由 WordExport 统一组装（全应用只出一份 Word）
        return body.toString()
    }

    private fun esc(s: String): String {
        // XML 不允许的控制字符（除 \t \n \r）会让 Word 报「内容有问题」，
        // 聊天里粘进来一个就够毁掉整份文档 —— 先剔掉。
        val clean = s.filter { c -> c == '\t' || c == '\n' || c == '\r' || c.code >= 0x20 }
        return clean
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
    }
}
