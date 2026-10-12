package com.example.asephina

import android.content.Context
import android.net.Uri
import java.io.BufferedInputStream
import java.io.InputStream
import java.util.zip.Inflater
import java.util.zip.ZipInputStream

/**
 * 把用户挑的文档读成**纯文本**，好让「她」真的看得见内容。
 *
 * 为什么不把文件本身发出去：
 *   这里接的都是 OpenAI 兼容的**文本**接口，二进制塞不进去。所以文档要先在本地
 *   把文字抽出来，作为这一段对话的正文发过去。
 *
 * 为什么不把原文件也复制一份进数据目录：
 *   原文件可能是几十兆，而数据镜像每天都要把整个数据目录压一遍 ——
 *   复制进来就是让每天的备份白白变大。这里只留**抽出来的文字**（有上限，见 MAX_CHARS），
 *   原始文件仍留在用户自己的目录里，我们一个字节都不带走。
 *
 * 各类文件的处理：
 *   · .docx —— 它本身就是个 zip，正文在 word/document.xml 里，按 XML 取文字，可靠；
 *   · .pdf  —— 尽力而为：解开压缩流、从 Tj/TJ 这些排版指令里取字符串。
 *             扫描件（整页是图）和用嵌入字体的那种取不出来，**取不出来就明说**，
 *             只把文件名发过去 —— 宁可少给，也不能把一堆乱码当成正文喂给模型；
 *   · .txt / .md / .json / .csv / .log —— 直接当 UTF-8 读。
 *
 * 所有解析失败都走「返回 null」这一条路，由调用方兜底，不往外抛异常。
 */
object DocText {

    /** 单个文件最多截多少字进对话。整本《三体》也就几十万字，这里够用了。 */
    const val MAX_CHARS = 20_000

    /** 挑完一个文件的结果。 */
    data class Picked(
        /** 显示在附件事条与气泡上的名字 */
        val title: String,
        /** 抽出来的正文；空串 = 没抽出来 */
        val text: String,
        /** 给用户看的一句话说明（抽到了多少字 / 为什么没抽到） */
        val note: String,
        /** 放进气泡卡片第二行的那句类型说明，例如「Word 文档 · 1.2 万字」 */
        val kindLabel: String
    )

    /** 支持的扩展名（挑文件时挡一道，让选择器尽量只列这些） */
    val MIME = arrayOf(
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .docx
        "application/pdf",
        "text/plain",
        "text/markdown"
    )

    /* ------------------------------------------------------------------ */

    fun pick(ctx: Context, uri: Uri): Picked {
        val name = displayName(ctx, uri)
        val ext = name.substringAfterLast('.', "").lowercase()

        val raw: String? = try {
            ctx.contentResolver.openInputStream(uri)?.use { ins -> readByExt(ext, ins) }
        } catch (e: Exception) {
            null
        }

        // 没抽到文字：照样能发，只是内容里只有一句「我发给你一个文件：xxx」
        if (raw.isNullOrBlank()) {
            val why = when (ext) {
                "pdf" -> "这个 PDF 里读不出文字（可能是扫描件、整页是图，或者用了嵌入字体）"
                "docx" -> "这个 docx 读不出文字（文件可能损坏，或者是老式的 .doc）"
                "" -> "认不出这是什么类型的文件"
                else -> "这种格式（.$ext）暂时读不出文字"
            }
            return Picked(name, "", "$why —— 只把文件名发过去了", typeLabel(ext, 0))
        }

        val clipped = raw.length > MAX_CHARS
        val body = if (clipped) raw.substring(0, MAX_CHARS) else raw
        val note = if (clipped)
            "已读出 ${raw.length} 字，只把前 ${MAX_CHARS} 字发过去"
        else
            "已读出 ${body.length} 字"
        return Picked(name, body, note, typeLabel(ext, body.length))
    }

    /**
     * 单个文件最多读多少字节。挑文件的是系统选择器，用户完全可能挑中一个几百兆的文件 ——
     * 无上限的 readBytes() 会直接把内存撑爆。超出部分丢掉：反正后面还要按字数再截一次。
     */
    private const val MAX_BYTES = 8 * 1024 * 1024

    /** 有上限地读完一个流：到上限就停，不抛异常。 */
    private fun readCapped(ins: InputStream, cap: Int = MAX_BYTES): ByteArray {
        val out = java.io.ByteArrayOutputStream()
        val buf = ByteArray(64 * 1024)
        while (out.size() < cap) {
            val n = ins.read(buf, 0, minOf(buf.size, cap - out.size()))
            if (n <= 0) break
            out.write(buf, 0, n)
        }
        return out.toByteArray()
    }

    private fun readByExt(ext: String, ins: InputStream): String? = when (ext) {
        "docx" -> docxText(ins)
        "pdf" -> pdfText(ins)
        "txt", "md", "markdown", "json", "csv", "log", "xml" ->
            readCapped(ins).toString(Charsets.UTF_8)
        else -> null
    }

    /** 气泡卡片第二行：告诉用户这是什么、多大 */
    private fun typeLabel(ext: String, chars: Int): String {
        val t = when (ext) {
            "docx" -> "Word 文档"
            "pdf" -> "PDF"
            "txt" -> "文本"
            "md", "markdown" -> "Markdown"
            "json" -> "JSON"
            "csv" -> "表格文本"
            "xml" -> "XML"
            else -> if (ext.isBlank()) "文件" else ".$ext 文件"
        }
        if (chars <= 0) return t
        val size = if (chars >= 10_000) "${chars / 10_000}.${(chars % 10_000) / 1_000} 万字" else "$chars 字"
        return "$t · $size"
    }

    /**
     * 把文件内容拼成要发给模型的那一段。
     * 放在用户自己打的字**后面** —— 他说「看看这个」的时候，指的是下面这段。
     */
    fun asMessageTail(p: Picked): String {
        if (p.text.isBlank()) return "\n\n【我发给你一个文件：${p.title}】"
        return "\n\n【我发给你一个文件：${p.title}】\n" +
                "（下面是从文件里读出来的正文）\n" +
                p.text
    }

    /* ============================ docx ============================ */

    /**
     * docx 的 token 扫描：一次遍历同时认「文字」「分段」「换行」「制表符」。
     * 用一条正则而不是先删标签再取文字 —— 后者会把段落之间的换行一起删掉，
     * 结果整篇挤成一行。
     */
    private val DOCX_TOKEN = Regex(
        "<w:t(?:\\s[^>]*)?>(.*?)</w:t>|</w:p>|<w:br\\s*/?>|<w:tab\\s*/?>",
        RegexOption.DOT_MATCHES_ALL
    )

    private fun docxText(ins: InputStream): String? {
        ZipInputStream(BufferedInputStream(ins)).use { zip ->
            var e = zip.nextEntry
            while (e != null) {
                if (e.name == "word/document.xml") {
                    val xml = readCapped(zip).toString(Charsets.UTF_8)
                    // 第一步：把「文字 / 段落 / 换行 / 制表」按出现顺序取出来
                    //         （<w:t> 标签本身在这一步就被吃掉了）
                    val pulled = DOCX_TOKEN.replace(xml) { m ->
                        val v = m.value
                        when {
                            v == "</w:p>" -> "\n"
                            v.startsWith("<w:br") -> "\n"
                            v.startsWith("<w:tab") -> "\t"
                            else -> m.groupValues[1]
                        }
                    }
                    // 第二步：**剩下的标签一律扔掉**。
                    //         少了这一步，抽出来的东西里会夹着 <w:r>、<w:body> 这些壳 ——
                    //         编译能过、看着也像有文字，实际把 XML 当正文喂给模型了。
                    // 第三步：把 XML 实体还原成人看的字符 —— 否则正文里会赫然写着 &amp; 和 &lt;
                    return tidy(unescapeXml(pulled.replace(Regex("<[^>]*>"), "")))
                }
                e = zip.nextEntry
            }
        }
        return null
    }

    /* ============================ pdf ============================ */

    private fun pdfText(ins: InputStream): String? {
        val bytes = readCapped(ins)
        val out = StringBuilder()
        var i = 0
        while (i < bytes.size) {
            val s = indexOfAscii(bytes, "stream", i)
            if (s < 0) break
            var p = s + 6
            // stream 后面允许跟 \r\n 或 \n
            if (p < bytes.size && bytes[p] == 13.toByte()) p++
            if (p < bytes.size && bytes[p] == 10.toByte()) p++
            val e = indexOfAscii(bytes, "endstream", p)
            if (e < 0) break
            val chunk = bytes.copyOfRange(p, e)
            val plain = inflateOrRaw(chunk)
            if (plain != null) out.append(contentStreamText(plain))
            i = e + 9
        }
        val t = tidy(out.toString())
        // ⚠️ 关键：PDF 的排版指令里捞出来的东西经常是乱码（嵌入字体、CID 编码）。
        //    乱码比空手而归更糟 —— 模型会把乱码当正文理解。所以这里必须验一道。
        return if (looksLikeProse(t)) t else null
    }

    private fun inflateOrRaw(chunk: ByteArray): String? {
        // 先试解压（绝大多数正文流是 FlateDecode）
        try {
            val inf = Inflater()
            inf.setInput(chunk)
            val buf = ByteArray(64 * 1024)
            val sb = java.io.ByteArrayOutputStream()
            while (!inf.finished()) {
                val n = inf.inflate(buf)
                if (n <= 0) break
                sb.write(buf, 0, n)
                if (sb.size() > 8 * 1024 * 1024) break   // 单流上限，防炸内存
            }
            inf.end()
            if (sb.size() > 0) return sb.toByteArray().toString(Charsets.ISO_8859_1)
        } catch (e: Exception) {
            // 落到下面按原文处理
        }
        // 没压缩的流：直接用
        return if (indexOfAscii(chunk, "BT", 0) >= 0) chunk.toString(Charsets.ISO_8859_1) else null
    }

    /**
     * 从 PDF 的内容流里把「画字」的字符串捞出来。
     * 只认最普通的那几种写法：(...) Tj、[...] TJ、(...) '、(...) "。
     * 复杂排版（每行一个 Tm 矩阵、字距调整数组）也能出个大概，反正后面还要验。
     */
    private fun contentStreamText(s: String): String {
        val sb = StringBuilder()
        var i = 0
        val n = s.length
        while (i < n) {
            val c = s[i]
            when {
                c == '(' -> {
                    val (str, next) = readPdfString(s, i)
                    sb.append(str)
                    i = next
                }
                c == '[' -> {
                    // TJ 数组：里面可能混着数字（字距），只取其中的字符串
                    i++
                    while (i < n && s[i] != ']') {
                        if (s[i] == '(') {
                            val (str, next) = readPdfString(s, i)
                            sb.append(str)
                            i = next
                        } else i++
                    }
                }
                c == 'T' && i + 1 < n && (s[i + 1] == 'd' || s[i + 1] == 'D' || s[i + 1] == '*') -> {
                    sb.append('\n'); i += 2
                }
                c == 'E' && i + 2 < n && s[i + 1] == 'T' -> { sb.append('\n'); i += 2 }
                else -> i++
            }
        }
        return sb.toString()
    }

    /** 读一个 PDF 字符串字面量，处理括号嵌套与 \\ 转义，返回 (内容, 下一个下标) */
    private fun readPdfString(s: String, start: Int): Pair<String, Int> {
        val sb = StringBuilder()
        var i = start + 1
        var depth = 1
        while (i < s.length) {
            val c = s[i]
            when {
                c == '\\' -> {
                    val d = if (i + 1 < s.length) s[i + 1] else ' '
                    when {
                        d == 'n' -> { sb.append('\n'); i += 2 }
                        d == 'r' -> { sb.append('\r'); i += 2 }
                        d == 't' -> { sb.append('\t'); i += 2 }
                        d in '0'..'7' -> {
                            // 八进制，最多三位
                            var k = i + 1
                            var v = 0
                            var cnt = 0
                            while (k < s.length && cnt < 3 && s[k] in '0'..'7') {
                                v = v * 8 + (s[k] - '0'); k++; cnt++
                            }
                            sb.append(v.toChar()); i = k
                        }
                        else -> { sb.append(d); i += 2 }
                    }
                }
                c == '(' -> { depth++; sb.append(c); i++ }
                c == ')' -> {
                    depth--
                    if (depth == 0) return sb.toString() to (i + 1)
                    sb.append(c); i++
                }
                else -> { sb.append(c); i++ }
            }
        }
        return sb.toString() to s.length
    }

    /** 抽出来的东西像不像正常文字。不像就宁可当作没抽到。 */
    private fun looksLikeProse(t: String): Boolean {
        if (t.length < 12) return false
        var good = 0
        var han = 0
        for (c in t) {
            when {
                c == '\n' || c == '\r' || c == '\t' -> good++
                c.isLetterOrDigit() -> { good++; if (c.code > 0x2E80) han++ }
                c.isWhitespace() -> good++
                c in "，。、；：？！（）【】《》“”‘’—…·.,;:?!()[]{}<>\"'/-_+=%&@#*" -> good++
            }
        }
        val ratio = good.toDouble() / t.length
        // 要么「干净」，要么「中文够多」—— 嵌入字体捞出来的乱码两条都不满足
        return ratio >= 0.85 || (ratio >= 0.6 && han >= 20)
    }

    /* ============================ 公共小工具 ============================ */

    /**
     * XML 实体还原。
     * 引号写成 \u0022 / \u0027 是为了不在源码字符串里再嵌引号。
     * 注意 & 必须最后换：不然 &lt; 那类写法会先被拆一层、再多解一层。
     */
    private fun unescapeXml(s: String): String {
        var r = s
        r = r.replace("&lt;", "<")
        r = r.replace("&gt;", ">")
        r = r.replace("&quot;", "\u0022")
        r = r.replace("&apos;", "\u0027")
        r = r.replace("&amp;", "&")
        return r
    }

    private fun tidy(s: String): String = s
        .replace('\u0000', ' ')
        .replace(Regex("[ \\t]+\\n"), "\n")
        .replace(Regex("\\n{3,}"), "\n\n")
        .trim()

    private fun indexOfAscii(b: ByteArray, needle: String, from: Int): Int {
        val n = needle.toByteArray(Charsets.ISO_8859_1)
        var i = from.coerceAtLeast(0)
        outer@ while (i <= b.size - n.size) {
            for (j in n.indices) if (b[i + j] != n[j]) { i++; continue@outer }
            return i
        }
        return -1
    }

    /** 从系统文件选择器给的 uri 上取显示名 */
    private fun displayName(ctx: Context, uri: Uri): String {
        try {
            ctx.contentResolver.query(uri, null, null, null, null)?.use { c ->
                val i = c.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                if (i >= 0 && c.moveToFirst()) {
                    val n = c.getString(i)
                    if (!n.isNullOrBlank()) return n
                }
            }
        } catch (e: Exception) {
            // 查不到就退回用 uri 的最后一段
        }
        return uri.lastPathSegment?.substringAfterLast('/')?.takeIf { it.isNotBlank() } ?: "文件"
    }
}
