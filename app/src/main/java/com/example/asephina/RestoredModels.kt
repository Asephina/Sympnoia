/*
 * ============================================================================
 *  五、数据结构与小工具
 *  ---------------------------------------------------------------------------
 *  【这个文件是怎么来的】`Gallery`、`Ev`、`NameRec`、`Doc`、`DocSpan`、`Portrait`、
 *  `Game`、`SpanTool`、`DocEdit` 原本在 MainActivity.kt 里，源码被误删后
 *  从最后一次成功编译的字节码反编译还原。**原注释找不回来了**，
 *  下面的注释是事后补写的。
 *
 *  【注意 WordExport.Doc 与这里的 Doc 不是一回事】
 *  前者是生成 .docx 用的段落构建器（嵌套在 WordExport 里），
 *  后者是一篇文章/日记的数据本体。
 *
 *  【SpanTool】把「富文本格式」表示成一串与正文分离的区间（DocSpan），
 *  每次编辑正文都要把区间跟着平移 —— 这是全项目最容易出微妙错误的地方：
 *  少移一位，加粗就会从这一句跑到下一句上。
 * ========================================================================== */

package com.example.asephina

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import java.io.File
import java.io.FileOutputStream
import kotlin.math.max
import kotlin.math.min
import org.json.JSONArray
import org.json.JSONObject

object Gallery {
    var manage by mutableStateOf(false)
    var view: Portrait? by mutableStateOf(null)
    var crop: Portrait? by mutableStateOf(null)
    var date: Portrait? by mutableStateOf(null)
    var del: Portrait? by mutableStateOf(null)

    fun closePages() {
        manage = false
        view = null
        crop = null
        date = null
        del = null
    }

    fun dir(ctx: Context): File = File(Store.root(ctx), "gallery")

    private fun indexFile(ctx: Context): File = File(dir(ctx), "index.json")

    var items: List<Portrait> by mutableStateOf(emptyList())
    var tick by mutableIntStateOf(0)
    var expanded by mutableStateOf(false)

    fun setExpanded(ctx: Context, v: Boolean) {
        expanded = v
        save(ctx)
    }

    fun imgFile(ctx: Context, name: String): File = File(dir(ctx), name)

    fun readIndex(ctx: Context): List<Portrait> = try {
        val f = indexFile(ctx)
        if (!f.exists()) {
            emptyList()
        } else {
            val arr = JSONObject(f.readText()).optJSONArray("items") ?: JSONArray()
            (0 until arr.length()).mapNotNull { i ->
                arr.optJSONObject(i)?.let { o ->
                    Portrait(
                        o.optString("file", ""),
                        o.optLong("ts", System.currentTimeMillis()),
                        o.optString("title", ""),
                        o.optBoolean("show", true),
                        o.optInt("w", 1000).coerceAtLeast(1),
                        o.optInt("h", 1000).coerceAtLeast(1),
                        o.optInt("border", 0).coerceIn(0, 4),
                        o.optDouble("cx", 0.5).toFloat().coerceIn(0f, 1f),
                        o.optDouble("cy", 0.5).toFloat().coerceIn(0f, 1f),
                        o.optDouble("zoom", 1.0).toFloat().coerceIn(1f, 8f)
                    )
                }
            }.filter { !it.file.isBlank() }
        }
    } catch (e: Exception) {
        emptyList()
    }

    fun readIndexForExport(ctx: Context): List<Portrait> {
        val byIndex = readIndex(ctx)
        if (byIndex.isNotEmpty()) return byIndex
        val files = try {
            dir(ctx).listFiles { f ->
                f.isFile && f.name != "index.json" && !f.name.endsWith(".tmp")
            }?.sortedBy { it.name } ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
        return files.mapNotNull { f ->
            val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            try {
                BitmapFactory.decodeFile(f.absolutePath, bounds)
            } catch (e: Exception) {
            }
            if (bounds.outWidth > 0 && bounds.outHeight > 0) {
                Portrait(
                    f.name,
                    f.lastModified(),
                    show = true,
                    w = bounds.outWidth.coerceAtLeast(1),
                    h = bounds.outHeight.coerceAtLeast(1)
                )
            } else {
                null
            }
        }
    }

    fun load(ctx: Context) {
        try {
            val io = indexFile(ctx)
            if (io.exists()) {
                expanded = JSONObject(io.readText()).optBoolean("open", false)
            }
        } catch (e: Exception) {
        }
        items = readIndex(ctx)
        tick++
    }

    fun save(ctx: Context) {
        try {
            if (!dir(ctx).exists()) {
                dir(ctx).mkdirs()
            }
            val arr = JSONArray()
            items.forEach { p ->
                arr.put(
                    JSONObject().put("file", p.file).put("ts", p.ts).put("title", p.title)
                        .put("show", p.show).put("w", p.w).put("h", p.h)
                        .put("cx", p.cx).put("cy", p.cy).put("zoom", p.zoom)
                        .put("border", p.border)
                )
            }
            writeAtomicText(
                indexFile(ctx),
                JSONObject().put("items", arr).put("open", expanded).toString(2)
            )
        } catch (e: Exception) {
        }
        tick++
    }

    fun shown(): List<Portrait> = items.filter { it.show }

    fun add(ctx: Context, uri: Uri): Portrait? {
        val bmp = try {
            val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            ctx.contentResolver.openInputStream(uri)?.use {
                BitmapFactory.decodeStream(it, null, bounds)
            }
            var sample = 1
            while (bounds.outWidth / sample > 1600 || bounds.outHeight / sample > 1600) sample *= 2
            val opts = BitmapFactory.Options().apply { inSampleSize = sample }
            ctx.contentResolver.openInputStream(uri)?.use {
                BitmapFactory.decodeStream(it, null, opts)
            }
        } catch (e: Exception) {
            null
        }
        if (bmp == null) return null
        return try {
            if (!dir(ctx).exists()) {
                dir(ctx).mkdirs()
            }
            val name = "p" + System.currentTimeMillis() + ".jpg"
            FileOutputStream(imgFile(ctx, name)).use {
                bmp.compress(Bitmap.CompressFormat.JPEG, 92, it)
            }
            val p = Portrait(name, w = bmp.width, h = bmp.height)
            items = items + p
            save(ctx)
            p
        } catch (e: Exception) {
            null
        }
    }

    fun update(ctx: Context, target: Portrait, changed: Portrait) {
        items = items.map { if (it.file == target.file) changed else it }
        save(ctx)
    }

    fun live(p: Portrait?): Portrait? = p?.let { old ->
        items.firstOrNull { it.file == old.file } ?: old
    }

    fun remove(ctx: Context, p: Portrait) {
        try {
            imgFile(ctx, p.file).delete()
        } catch (e: Exception) {
        }
        items = items.filter { it.file != p.file }
        save(ctx)
    }
}

data class Ev(
    val id: Long = System.currentTimeMillis(),
    val title: String = "",
    val level: Int = 3,
    val minutes: Int = 30,
    val eff: Int = 60,
    val forHer: Boolean = false,
    val note: String = "",
    val pomo: Boolean = false,
    val pomoCycles: Int = 0,
    /** 计划完成：先记下来，先不计入行动熵 */
    val planned: Boolean = false,
    /** 计划完成里「勾上了」的状态 —— 只有勾上才计入 */
    val done: Boolean = false
)

data class NameRec(
    val ts: Long = System.currentTimeMillis(),
    val text: String = "纳凌薇",
    val fill: Int = 0,
    val note: String = ""
) {
    fun fillLeftMs(now: Long = System.currentTimeMillis()): Long =
        (ts + NAME_FILL_WINDOW_MS - now).coerceAtLeast(0L)

    fun inWindow(now: Long = System.currentTimeMillis()): Boolean = fillLeftMs(now) > 0L

    fun fillOpen(now: Long = System.currentTimeMillis()): Boolean = fill < 100 && inWindow(now)
}

data class Doc(
    val id: Long = System.currentTimeMillis(),
    val title: String = "",
    val body: String = "",
    val images: List<String> = emptyList(),
    val videos: List<String> = emptyList(),
    val created: Long = System.currentTimeMillis(),
    val updated: Long = System.currentTimeMillis(),
    val dateKey: String = Dates.todayKey(),
    val spans: List<DocSpan> = emptyList()
) {
    val chars: Int get() = body.count { !it.isWhitespace() }
}

data class DocSpan(
    val start: Int,
    val end: Int,
    val bold: Boolean = false,
    val italic: Boolean = false,
    val underline: Boolean = false,
    val strike: Boolean = false,
    val size: Float = 0f,
    val color: Int = 0,
    val bg: Int = 0,
    val font: Int = 0,
    val align: Int = 0
) {
    val styled: Boolean
        get() = bold || italic || underline || strike || size > 0f ||
            color != 0 || bg != 0 || font != 0 || align != 0

    fun sameAs(o: DocSpan): Boolean = bold == o.bold && italic == o.italic &&
        underline == o.underline && strike == o.strike && size == o.size &&
        color == o.color && bg == o.bg && font == o.font && align == o.align
}

data class Portrait(
    val file: String = "",
    val ts: Long = System.currentTimeMillis(),
    val title: String = "",
    val show: Boolean = true,
    val w: Int = 1000,
    val h: Int = 1000,
    val border: Int = 0,
    val cx: Float = 0.5f,
    val cy: Float = 0.5f,
    val zoom: Float = 1.0f
)

object Game {
    const val K_FLAT = 0
    const val K_DAY_MUL = 1
    const val K_TRACE_MUL = 2
    const val K_DOC_MUL = 3
    const val K_MOOD_MUL = 4
    const val K_BOND = 5
    const val K_EXTRA = 6
    const val K_NONE = 7

    val ScratchTable: List<ScratchPrize> get() = listOf(
        ScratchPrize("特等", "当日所获行动熵 ×1.50", Persona.scratchLine(0), 10, 1, 1.5, 5),
        ScratchPrize("特等", "直接 +270 行动熵", Persona.scratchLine(1), 200, 0, 270.0, 5),
        ScratchPrize("一等", "当日所获行动熵 ×1.25", Persona.scratchLine(2), 500, 1, 1.25, 4),
        ScratchPrize("一等", "直接 +100 行动熵", Persona.scratchLine(3), 3000, 0, 100.0, 4),
        ScratchPrize("一等", "再刮两次", Persona.scratchLine(4), 500, 6, 2.0, 4),
        ScratchPrize("二等", "当日所获行动熵 ×1.15", Persona.scratchLine(5), 6000, 1, 1.15, 3),
        ScratchPrize("二等", "直接 +50 行动熵", Persona.scratchLine(6), 10000, 0, 50.0, 3),
        ScratchPrize("二等", "今日行迹 ×1.30", Persona.scratchLine(7), 8000, 2, 1.3, 3),
        ScratchPrize("二等", "今日文章/日记 ×1.30", Persona.scratchLine(8), 8000, 3, 1.3, 3),
        ScratchPrize("二等", "今日心情 ×1.30", Persona.scratchLine(9), 8000, 4, 1.3, 3),
        ScratchPrize("二等", "联结加权 +0.15", Persona.scratchLine(10), 6000, 5, 0.15, 3),
        ScratchPrize("二等", "再刮一次", Persona.scratchLine(11), 8000, 6, 1.0, 3),
        ScratchPrize("三等", "当日所获行动熵 ×1.08", Persona.scratchLine(12), 20000, 1, 1.08, 2),
        ScratchPrize("三等", "直接 +27 行动熵", Persona.scratchLine(13), 40000, 0, 27.0, 2),
        ScratchPrize("三等", "今日行迹 ×1.15", Persona.scratchLine(14), 30000, 2, 1.15, 2),
        ScratchPrize("三等", "今日文章/日记 ×1.15", Persona.scratchLine(15), 30000, 3, 1.15, 2),
        ScratchPrize("三等", "今日心情 ×1.15", Persona.scratchLine(16), 30000, 4, 1.15, 2),
        ScratchPrize("三等", "联结加权 +0.08", Persona.scratchLine(17), 25000, 5, 0.08, 2),
        ScratchPrize("三等", "再刮一次", Persona.scratchLine(18), 15000, 6, 1.0, 2),
        ScratchPrize("四等", "当日所获行动熵 ×1.03", Persona.scratchLine(19), 75000, 1, 1.03, 1),
        ScratchPrize("四等", "直接 +7 行动熵", Persona.scratchLine(20), 230000, 0, 7.0, 1),
        ScratchPrize("四等", "今日行迹 ×1.05", Persona.scratchLine(21), 61790, 2, 1.05, 1),
        ScratchPrize("四等", "今日文章/日记 ×1.05", Persona.scratchLine(22), 60000, 3, 1.05, 1),
        ScratchPrize("四等", "今日心情 ×1.05", Persona.scratchLine(23), 63000, 4, 1.05, 1),
        ScratchPrize("四等", "联结加权 +0.03", Persona.scratchLine(24), 60000, 5, 0.03, 1),
        ScratchPrize("四等", "直接 +3 行动熵", Persona.scratchLine(25), 170000, 0, 3.0, 1),
        ScratchPrize("谢谢参与", "行动熵 +0", Persona.scratchLine(26), 32000, 7, 0.0, 0)
    )

    fun rollScratch(): ScratchPrize {
        val total = ScratchTable.sumOf { it.weight }
        var r = (Math.random() * total).toLong()
        for (p in ScratchTable) {
            r -= p.weight
            if (r < 0L) return p
        }
        return ScratchTable.last()
    }

    fun freeScratches(yesterdayAE: Double): Int =
        if (yesterdayAE >= 270.0) 3 else if (yesterdayAE >= 100.0) 2 else 1

    var scratchPoppedForDay: String? = null

    val lingweiClosedThisRun: MutableSet<String> = LinkedHashSet()
    val specialClosedThisRun: MutableSet<String> = LinkedHashSet()

    fun revealStyle(style: Int): RevealStyle = when (style) {
        1 -> RevealStyle(CatArticle, GameTeal, 10, 1, 0.3f, 900)
        2 -> RevealStyle(PurpleMain, PinkSoft, 14, 1, 0.42f, 1000)
        3 -> RevealStyle(Color(0xFFE85FA8), PurpleMain, 18, 2, 0.55f, 1200)
        4 -> RevealStyle(ScratchGold, PinkSoft, 24, 2, 0.66f, 1400)
        5 -> RevealStyle(ScratchGold, Color.White, 30, 3, 0.85f, 1700, true)
        else -> RevealStyle(
            Color(0xFF6B6B7A), Color(0xFF3A3A44), 9, 0, 0.16f, 1200, falling = true
        )
    }

    class ScratchPrize(
        val tier: String,
        val effect: String,
        val line: String,
        val weight: Int,
        val kind: Int,
        val value: Double,
        val style: Int
    )

    class RevealStyle(
        val main: Color,
        val accent: Color,
        val particles: Int,
        val rings: Int,
        val glow: Float,
        val durationMs: Int,
        val rainbow: Boolean = false,
        val falling: Boolean = false
    )
}

object SpanTool {
    fun normalize(list: List<DocSpan>): List<DocSpan> {
        val clean = list.filter { it.end > it.start && it.styled }.sortedBy { it.start }
        val out = ArrayList<DocSpan>(clean.size)
        var cursor = -1
        clean.forEach { s ->
            val st = max(s.start, cursor).coerceAtLeast(0)
            if (s.end > st) {
                val piece = DocSpan(
                    st, s.end, s.bold, s.italic, s.underline, s.strike,
                    s.size, s.color, s.bg, s.font, s.align
                )
                val prev = out.lastOrNull()
                if (prev != null && prev.end == piece.start && prev.sameAs(piece)) {
                    out[out.size - 1] = DocSpan(
                        prev.start, piece.end, prev.bold, prev.italic, prev.underline,
                        prev.strike, prev.size, prev.color, prev.bg, prev.font, prev.align
                    )
                } else {
                    out.add(piece)
                }
                cursor = piece.end
            }
        }
        return out
    }

    private fun partition(spans: List<DocSpan>, start: Int, end: Int): List<DocSpan> {
        val bounds = sortedSetOf(start, end)
        spans.forEach { s ->
            if (s.start in (start + 1) until end) bounds.add(s.start)
            if (s.end in (start + 1) until end) bounds.add(s.end)
        }
        val pts = bounds.toList()
        val out = ArrayList<DocSpan>()
        for (i in 0 until pts.size - 1) {
            val a = pts[i]
            val b = pts[i + 1]
            if (b > a) {
                val cover = spans.firstOrNull { it.start <= a && it.end >= b }
                out.add(
                    if (cover != null) {
                        DocSpan(
                            a, b, cover.bold, cover.italic, cover.underline, cover.strike,
                            cover.size, cover.color, cover.bg, cover.font, cover.align
                        )
                    } else {
                        DocSpan(a, b)
                    }
                )
            }
        }
        return out
    }

    fun apply(
        spans: List<DocSpan>,
        start: Int,
        end: Int,
        mutate: (DocSpan, Boolean) -> DocSpan,
        read: (DocSpan) -> Boolean
    ): List<DocSpan> {
        if (end <= start) return normalize(spans)
        val pieces = partition(spans, start, end)
        val turnOn = !pieces.all { read(it) }
        val rebuilt = ArrayList<DocSpan>(spans.size + pieces.size)
        spans.forEach { s ->
            if (s.end > start && s.start < end) {
                if (s.start < start) {
                    rebuilt.add(
                        DocSpan(
                            s.start, start, s.bold, s.italic, s.underline, s.strike,
                            s.size, s.color, s.bg, s.font, s.align
                        )
                    )
                }
                if (s.end > end) {
                    rebuilt.add(
                        DocSpan(
                            end, s.end, s.bold, s.italic, s.underline, s.strike,
                            s.size, s.color, s.bg, s.font, s.align
                        )
                    )
                }
            } else {
                rebuilt.add(s)
            }
        }
        pieces.forEach { p ->
            rebuilt.add(mutate(p, turnOn))
        }
        return normalize(rebuilt)
    }

    fun shift(spans: List<DocSpan>, oldText: String, newText: String): List<DocSpan> {
        if (spans.isEmpty()) return spans
        val minLen = min(oldText.length, newText.length)
        var prefix = 0
        while (prefix < minLen && oldText[prefix] == newText[prefix]) prefix++
        var suffix = 0
        while (suffix < minLen - prefix &&
            oldText[oldText.length - 1 - suffix] == newText[newText.length - 1 - suffix]
        ) suffix++
        val removed = oldText.length - prefix - suffix
        val inserted = newText.length - prefix - suffix
        if (removed == 0 && inserted == 0) return spans

        fun mapPos(p: Int): Int = when {
            p <= prefix -> p
            p >= prefix + removed -> p + (inserted - removed)
            else -> prefix + (if (inserted > 0) inserted else 0)
        }

        val out = ArrayList<DocSpan>(spans.size)
        spans.forEach { s ->
            val a = mapPos(s.start)
            val b = mapPos(s.end)
            if (b > a) {
                out.add(
                    DocSpan(
                        a, b, s.bold, s.italic, s.underline, s.strike,
                        s.size, s.color, s.bg, s.font, s.align
                    )
                )
            }
        }
        return normalize(out)
    }

    fun styleAt(spans: List<DocSpan>, pos: Int): DocSpan =
        spans.firstOrNull { pos < it.end && it.start <= pos } ?: DocSpan(pos, pos)

    fun toJson(list: List<DocSpan>): JSONArray {
        val arr = JSONArray()
        list.forEach { s ->
            arr.put(
                JSONObject().put("s", s.start).put("e", s.end).put("b", s.bold)
                    .put("i", s.italic).put("u", s.underline).put("k", s.strike)
                    .put("z", s.size.toDouble()).put("c", s.color).put("g", s.bg)
                    .put("f", s.font).put("a", s.align)
            )
        }
        return arr
    }

    fun fromJson(arr: JSONArray?): List<DocSpan> {
        if (arr == null) return emptyList()
        val out = ArrayList<DocSpan>(arr.length())
        for (i in 0 until arr.length()) {
            val o = arr.optJSONObject(i)
            if (o != null) {
                out.add(
                    DocSpan(
                        o.optInt("s", 0),
                        o.optInt("e", 0),
                        o.optBoolean("b", false),
                        o.optBoolean("i", false),
                        o.optBoolean("u", false),
                        o.optBoolean("k", false),
                        o.optDouble("z", 0.0).toFloat(),
                        o.optInt("c", 0),
                        o.optInt("g", 0),
                        o.optInt("f", 0),
                        o.optInt("a", 0)
                    )
                )
            }
        }
        return normalize(out)
    }
}

object DocEdit {
    var open by mutableStateOf(false)
    var type by mutableStateOf("article")
    var category by mutableStateOf("默认")
    var initial by mutableStateOf(Doc())
    var isNew by mutableStateOf(true)
    var onSaved: ((Doc) -> Unit)? = null

    fun request(
        type: String,
        category: String,
        doc: Doc,
        isNew: Boolean,
        onSaved: (Doc) -> Unit
    ) {
        this.type = type
        this.category = category
        this.initial = doc
        this.isNew = isNew
        this.onSaved = onSaved
        open = true
    }
}
