package com.example.asephina

import androidx.compose.foundation.layout.wrapContentSize

import androidx.activity.compose.BackHandler

import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.ui.graphics.lerp

import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.snapshots.SnapshotStateList

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.BringIntoViewResponder
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewResponder
import androidx.compose.runtime.key
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextLayoutResult

import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.rememberPagerState
import java.io.BufferedInputStream

import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.rememberTextMeasurer

import androidx.compose.animation.core.animateFloat
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Constraints

import android.Manifest
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.util.lerp
import androidx.core.content.ContextCompat
import java.io.FileOutputStream
import java.io.FilterOutputStream
import java.util.zip.ZipInputStream
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.ln

import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.BroadcastReceiver
import android.content.ClipData
import android.content.ClipboardManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import java.io.BufferedOutputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

/* ==========================================================================
 *  五·六、对外备份目录（Sympnoia）—— 所有导出都落在应用数据之外
 *  ---------------------------------------------------------------------------
 *  为什么必须放外面：Android 11 起，Android/data/<包名>/ 不允许任何第三方程序写入，
 *  而且**应用一卸载，里面的东西也一起没了**——备份留在那儿等于白备份。
 *  于是文档、数据包、数据镜像、恢复留底，全部写到公共存储：
 *      /Documents/<导出文件夹>/            手动导出（Word 文档 + 数据包 .zip，不限份数）
 *      /Documents/<导出文件夹>/auto/       每天自动导出的 Word（滚动保留最近 7 份）
 *      /Documents/<导出文件夹>/mirror/     每天覆盖一次的数据镜像（单个 zip，只留最新）
 *      /Documents/<导出文件夹>/restore/    导入恢复前自动留的底（保留最近 3 份）
 *  写入走 MediaStore：Android 10 及以上**不需要任何存储权限**，也不受 Android/data 限制；
 *  Android 9 及以下回退到公共 Documents 目录（老系统上应用可直接写）。
 * ======================================================================== */
object Pub {

    const val SUB_MANUAL = "manual"
    const val SUB_AUTO = "auto"
    const val SUB_MIRROR = "mirror"

    /** 数据镜像目录里那份说明文件的固定名（dedupeFixed 要放行它） */
    const val MIRROR_NOTE = "Sympnoia-镜像说明.txt"
    const val SUB_RESTORE = "restore"

    const val MIME_ZIP = "application/zip"
    const val MIME_TXT = "text/plain"

    /** 恢复留底保留几份 */
    const val KEEP_RESTORE = 3

    /** 恢复时跳过的文件名（导出包里的说明，不属于数据） */
    private const val NOTE_FILE = "Sympnoia-说明.txt"

    /** 数据镜像里要包含的目录（media 体积大，且图片已随 Word 文档导出，故不入镜像） */
    private val MIRROR_DIRS = listOf("events", "docs", "names", "config", "gallery", "ai")

    /**
     * 不该被导出/回灌的目录与文件。
     * ⚠️ ai_key.json 是 API Key 的落点，**必须在最外层排除掉**：
     *    zipTree 的排除表只在最外层生效，所以 Key 被特意放成数据目录下的顶层单文件
     *    （见 AiCore.kt 的说明），导出包、镜像包、恢复留底里都不会带上它，
     *    恢复别人的包也不会覆盖你自己的 Key。
     */
    private val SKIP_DIRS = setOf("backup", "backup.new", "backup.old", "ai_key.json")

    /** MediaStore 用的相对路径（不带首尾斜杠） */
    fun rel(sub: String): String {
        val root = Environment.DIRECTORY_DOCUMENTS + "/" + Persona.exportFolder()
        return if (sub.isBlank()) root else root + "/" + sub
    }

    /** 给人看的路径 */
    fun pathText(sub: String): String =
        if (Build.VERSION.SDK_INT >= 29) "/" + rel(sub) + "/"
        else legacyDir(sub).absolutePath + "/"

    /** Android 9 及以下的回退目录 */
    fun legacyDir(sub: String): File {
        val base = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        val root = File(base, Persona.exportFolder())
        val d = if (sub.isBlank()) root else File(root, sub)
        if (!d.exists()) d.mkdirs()
        return d
    }

    /** 打开一个「同名会被覆盖」的输出流；失败返回 null */
    private fun targetStream(ctx: Context, sub: String, name: String, mime: String): OutputStream? {
        if (Build.VERSION.SDK_INT >= 29) {
            val col = MediaStore.Files.getContentUri("external")
            // 同名先删，免得系统给它加个「(1)」
            try {
                ctx.contentResolver.delete(
                    col, MediaStore.MediaColumns.DISPLAY_NAME + "=?", arrayOf(name)
                )
            } catch (e: Exception) {
            }
            val values = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, name)
                put(MediaStore.MediaColumns.MIME_TYPE, mime)
                put(MediaStore.MediaColumns.RELATIVE_PATH, rel(sub))
            }
            val uri = try {
                ctx.contentResolver.insert(col, values)
            } catch (e: Exception) {
                null
            } ?: return null
            return try {
                ctx.contentResolver.openOutputStream(uri)
            } catch (e: Exception) {
                null
            }
        }
        return try {
            FileOutputStream(File(legacyDir(sub), name))
        } catch (e: Exception) {
            // Android 9 及以下若没给存储权限，退回应用自己的目录（老行为，至少不会丢备份）
            try {
                val d = if (sub.isBlank()) Store.root(ctx) else File(Store.root(ctx), sub)
                if (!d.exists()) d.mkdirs()
                FileOutputStream(File(d, name))
            } catch (e2: Exception) {
                null
            }
        }
    }

    /** 写一个字节数组文件 */
    fun writeBytes(ctx: Context, sub: String, name: String, mime: String, bytes: ByteArray): Boolean {        val os = targetStream(ctx, sub, name, mime) ?: return false
        return try {
            os.use { it.write(bytes) }
            true
        } catch (e: Exception) {
            false
        }
    }

    /* ==========================================================================
     *  覆盖式写入：先写新的，成了再换掉旧的
     *  ---------------------------------------------------------------------------
     *  自动导出的压缩包（数据镜像、AI 数据包）**永远只有一份**。
     *  原来是对着最终文件名「先删后写」——万一写到一半没电、没空间、被系统打断，
     *  旧的那份已经被删掉了，等于当天什么都没有。
     *  所以覆盖式的产物一律走这里：新内容先完整落成 xxx.new，成功后再删旧的、
     *  把新的改名成最终名。中间任何一步失败，旧文件都还完好。
     * ======================================================================== */

    /** 只写、不碰同名文件（覆盖式的第一步） */
    private fun writeBytesNoDelete(
        ctx: Context, sub: String, name: String, mime: String, bytes: ByteArray
    ): Boolean {
        if (Build.VERSION.SDK_INT >= 29) {
            val col = MediaStore.Files.getContentUri("external")
            val values = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, name)
                put(MediaStore.MediaColumns.MIME_TYPE, mime)
                put(MediaStore.MediaColumns.RELATIVE_PATH, rel(sub))
            }
            val uri = try {
                ctx.contentResolver.insert(col, values)
            } catch (e: Exception) {
                null
            } ?: return false
            return try {
                ctx.contentResolver.openOutputStream(uri)?.use { it.write(bytes) }
                true
            } catch (e: Exception) {
                try {
                    ctx.contentResolver.delete(uri, null, null)
                } catch (e2: Exception) {
                }
                false
            }
        }
        return try {
            FileOutputStream(File(legacyDir(sub), name)).use { it.write(bytes) }
            true
        } catch (e: Exception) {
            false
        }
    }

    /** 在指定子目录里按名字删（比全局按文件名删安全） */
    fun deleteInSub(ctx: Context, sub: String, name: String): Boolean = try {
        if (Build.VERSION.SDK_INT >= 29) {
            ctx.contentResolver.delete(
                MediaStore.Files.getContentUri("external"),
                MediaStore.MediaColumns.DISPLAY_NAME + "=? AND " +
                        MediaStore.MediaColumns.RELATIVE_PATH + " LIKE ?",
                arrayOf(name, rel(sub) + "/%")
            ) > 0
        } else {
            File(legacyDir(sub), name).delete()
        }
    } catch (e: Exception) {
        false
    }

    /** 把子目录里的一个文件改名（覆盖式的最后一步） */
    private fun renameInSub(ctx: Context, sub: String, from: String, to: String): Boolean {
        if (Build.VERSION.SDK_INT >= 29) {
            val values = ContentValues().apply { put(MediaStore.MediaColumns.DISPLAY_NAME, to) }
            return try {
                ctx.contentResolver.update(
                    MediaStore.Files.getContentUri("external"), values,
                    MediaStore.MediaColumns.DISPLAY_NAME + "=? AND " +
                            MediaStore.MediaColumns.RELATIVE_PATH + " LIKE ?",
                    arrayOf(from, rel(sub) + "/%")
                ) > 0
            } catch (e: Exception) {
                false
            }
        }
        return try {
            File(legacyDir(sub), from).renameTo(File(legacyDir(sub), to))
        } catch (e: Exception) {
            false
        }
    }

    /**
     * 覆盖式写一个小文件（字节已经在内存里了）：AI 数据包、AI 文档这类走它。
     * 大文件（数据镜像）走 [zipDirs] 里那套流式版本，别整个读进内存。
     */
    fun writeBytesReplace(
        ctx: Context, sub: String, name: String, mime: String, bytes: ByteArray
    ): Boolean {
        val tmp = name + ".new"
        deleteInSub(ctx, sub, tmp)                       // 上次留下的半截临时文件
        if (!writeBytesNoDelete(ctx, sub, tmp, mime, bytes)) return false
        deleteInSub(ctx, sub, name)
        if (renameInSub(ctx, sub, tmp, name)) return true
        // 改名失败（少见）：至少把临时名留着，内容没丢
        return false
    }

    /* ---------------- 流式版本：内容可能很大，不整份读进内存 ---------------- */

    /** 只写、不碰同名文件；内容由 produce 直接写进流 */
    private fun writeStreamNoDelete(
        ctx: Context, sub: String, name: String, mime: String,
        produce: (OutputStream) -> Unit
    ): Boolean {
        if (Build.VERSION.SDK_INT >= 29) {
            val col = MediaStore.Files.getContentUri("external")
            val values = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, name)
                put(MediaStore.MediaColumns.MIME_TYPE, mime)
                put(MediaStore.MediaColumns.RELATIVE_PATH, rel(sub))
            }
            val uri = try {
                ctx.contentResolver.insert(col, values)
            } catch (e: Exception) {
                null
            } ?: return false
            return try {
                ctx.contentResolver.openOutputStream(uri)?.use { produce(it) }
                true
            } catch (e: Exception) {
                try {
                    ctx.contentResolver.delete(uri, null, null)
                } catch (e2: Exception) {
                }
                false
            }
        }
        return try {
            FileOutputStream(File(legacyDir(sub), name)).use { produce(it) }
            true
        } catch (e: Exception) {
            false
        }
    }

    /** 流式写一个**新名字**的文件（手动导出那种不会重名的） */
    fun writeStream(
        ctx: Context, sub: String, name: String, mime: String,
        produce: (OutputStream) -> Unit
    ): Boolean = writeStreamNoDelete(ctx, sub, name, mime, produce)

    /**
     * 流式覆盖式写：先写 .new，成了再删旧的、改名。
     * 图片动辄几十兆，AI 数据包走这条路，内存里不留副本。
     */
    fun writeStreamReplace(
        ctx: Context, sub: String, name: String, mime: String,
        produce: (OutputStream) -> Unit
    ): Boolean {
        val tmp = name + ".new"
        deleteInSub(ctx, sub, tmp)
        if (!writeStreamNoDelete(ctx, sub, tmp, mime, produce)) return false
        deleteInSub(ctx, sub, name)
        return renameInSub(ctx, sub, tmp, name)
    }

    /* ==========================================================================
     *  自动备份的硬约束（集中写在这里，不要散落到各处）
     *  ---------------------------------------------------------------------------
     *  手动导出：不限份数，自己删。
     *  自动导出：**一律覆盖式**，绝不会随天数增长。
     *    · 压缩包（数据镜像 Sympnoia-mirror.zip、AI 数据包 Sympnoia-ai.zip）
     *      —— 固定文件名，每天覆盖同一份，各自永远只有 1 份；
     *    · 文档（记录的 Asephina_auto_日期.docx、AI 的 Sympnoia-ai_auto_日期.docx）
     *      —— 每天一份，但**按天**只留最近 [AUTO_KEEP_DAYS] 天，两种一起算；
     *         也就是 auto/ 最多 7×2 = 14 份，第 8 天起从最早那一天开始覆盖。
     * ======================================================================== */

    /** 自动 Word 保留的份数（一天一份，滚动覆盖） */
    const val AUTO_KEEP_DAYS = 3

    /** 自动数据镜像保留的天数（一天一份，滚动覆盖） */
    const val MIRROR_KEEP_DAYS = 3

    /** 镜像压缩包的名字前缀（后面接日期） */
    const val MIRROR_PREFIX = "Sympnoia-mirror"

    /**
     * 数据镜像要装的子目录 —— **整个数据目录**，AI 那一份不再单独导。
     * 唯一不在其中的是顶层那个 ai_key.json（它不属于任何子目录，天生进不了镜像）。
     */
    val DATA_DIRS = listOf("events", "docs", "names", "config", "gallery", "media", "ai")

    /**
     * 自动文档目录里的文件名前缀。
     *
     * ⚠️ 这份名单**只该用来认「这是不是我们的文件」，绝不能用它做清理的主判据**。
     *    已经栽过两次：改名之后忘了同步这里，清理就一条都匹配不上，
     *    文件于是一天一份地堆下去，界面上还显示「已自动备份」，谁也发现不了。
     *    现在清理只按**后缀**认（auto/ 与 mirror/ 这两个目录只归我们用），
     *    这份名单只在「旧版遗留要不要删」这种保守判断里用。
     */
    private val AUTO_DOC_PREFIXES =
        listOf("Sympnoia_auto_", "Asephina_auto_", "Sympnoia-ai_auto_")

    /** 自动文档目录里的文件名清单 */
    private fun autoDocNames(ctx: Context): List<String> = namesIn(ctx, SUB_AUTO)

    /** 某个子目录里的文件名清单 */
    fun namesIn(ctx: Context, sub: String): List<String> {
        if (Build.VERSION.SDK_INT >= 29) {
            val out = ArrayList<String>()
            try {
                ctx.contentResolver.query(
                    MediaStore.Files.getContentUri("external"),
                    arrayOf(MediaStore.MediaColumns.DISPLAY_NAME),
                    MediaStore.MediaColumns.RELATIVE_PATH + " LIKE ?",
                    arrayOf(rel(sub) + "/%"), null
                )?.use { c ->
                    while (c.moveToNext()) c.getString(0)?.let { out.add(it) }
                }
            } catch (e: Exception) {
            }
            return out
        }
        return legacyDir(sub).listFiles()?.map { it.name } ?: emptyList()
    }

    /** 文件名里的日期（<前缀><yyyy-MM-dd>.docx）；不是自动文档就返回 null */
    private fun autoDocDate(name: String): String? {
        val p = AUTO_DOC_PREFIXES.firstOrNull { name.startsWith(it) } ?: return null
        val d = name.removePrefix(p).removeSuffix(".docx")
        return if (d.length == 10) d else null
    }

    /**
     * 按天清理自动文档：只留最近 [AUTO_KEEP_DAYS] 天，更早的整日删掉。
     *
     * ⚠️ 这里必须**按前缀兜底**，不能只认「前缀 + 日期 + .docx」这一种名字。
     *    踩过的坑：MediaStore 在插入同名文件时会自作主张改名成「xxx (1).docx」——
     *    这种名字 `autoDocDate` 解析不出来，就被当成「不是自动文档」放着不管，
     *    于是同一天的文件越攒越多（实测能到三十几份、近百兆）。
     *    现在：认得出日期的按天算，**认不出但前缀是我们的**一律当垃圾删掉；
     *    同一天同一个前缀只留一份（名字最短的那个，也就是没被加过 (1) 的）。
     */
    fun pruneAutoDocs(ctx: Context): Int =
        pruneDated(ctx, SUB_AUTO, emptyList(), AUTO_KEEP_DAYS, ext = ".docx")

    /**
     * 目录里的自动产物只允许留 [keep] 份：
     * 认得出日期的按日期排序、只留最新 [keep] 天；**认不出日期的一律当垃圾删掉**。
     *
     * ⚠️ 这里**故意不看文件名前缀**：[prefixes] 留空表示「这个目录里的文件都归我们管」。
     *    曾经按前缀匹配，结果改了前缀忘了改名单，清理一条都匹配不上 ——
     *    系统在写入同名文件时会自作主张加个 "(1)"，那些文件永远不会被覆盖，
     *    于是一天一份地堆下去（实测三十几份、近百兆），而界面上还显示「已备份」。
     *    auto/ 与 mirror/ 是我们独占的目录，按后缀认就够了。
     *
     * @param ext 只管这个后缀（".docx" / ".zip"）；留空则不管后缀
     * @param fixedNames 要放行的固定名（例如镜像说明 txt）
     */
    fun pruneDated(
        ctx: Context, sub: String, prefixes: List<String>, keep: Int,
        ext: String = "", fixedNames: List<String> = emptyList()
    ): Int {
        val all = namesIn(ctx, sub)
        val drop = ArrayList<String>()
        val byDate = LinkedHashMap<String, MutableList<String>>()
        all.forEach { n ->
            if (n in fixedNames) return@forEach
            val named = prefixes.isEmpty() || prefixes.any { n.startsWith(it) }
            val extOk = ext.isEmpty() || n.endsWith(ext)
            val d = if (named && extOk) dateInName(n) else null
            if (d == null) {
                // 名字里没有日期：固定名之外一律清掉。
                // 但归别人管的目录（前缀非空）要手下留情，别把无关文件删了。
                if (prefixes.isEmpty() || prefixes.any { n.startsWith(it) }) drop.add(n)
            } else {
                byDate.getOrPut(d) { ArrayList() }.add(n)
            }
        }
        val keepDates = byDate.keys.sortedDescending().take(keep).toSet()
        byDate.forEach { (d, files) ->
            // 同一天只留一份（名字最短的，也就是没被加过 "(1)" 的那个）
            val sorted = files.sortedBy { it.length }
            if (d in keepDates) drop.addAll(sorted.drop(1)) else drop.addAll(sorted)
        }
        drop.forEach { deleteInSub(ctx, sub, it) }
        return keepDates.size
    }

    /** 从文件名里抠出 yyyy-MM-dd（在哪一段都行）；抠不到返回 null */
    private fun dateInName(name: String): String? {
        val m = Regex("""(\d{4})-(\d{2})-(\d{2})""").find(name) ?: return null
        return m.value
    }

    /**
     * 固定名的产物只允许有一份：
     * 除 [keep] 之外的、名字以同一词干开头的文件（`(1)`、`.new`、半截的）全部删掉。
     */
    fun dedupeFixed(ctx: Context, sub: String, keep: List<String>): Int {
        val stems = keep.map { it.substringBeforeLast('.') }.filter { it.isNotBlank() }
        var n = 0
        namesIn(ctx, sub).forEach { f ->
            if (f in keep) return@forEach
            if (stems.any { f.startsWith(it) }) {
                if (deleteInSub(ctx, sub, f)) n++
            }
        }
        return n
    }

    /**
     * 收拾三个自动目录。**不看「今天备份过没有」** —— 历史遗留的重复文件必须能立刻清掉，
     * 不然用户已经占掉的空间要等到下一次备份才有机会被处理。
     */
    fun tidyAutoOutputs(ctx: Context): Int {
        var n = 0
        try {
            fun count(): Int =
                namesIn(ctx, SUB_AUTO).size + namesIn(ctx, SUB_MIRROR).size + namesIn(ctx, "ai").size
            val before = count()
            // 传空前缀 = 「这个目录里的东西都归我们管」，按后缀认，不再依赖任何名单
            pruneDated(ctx, SUB_AUTO, emptyList(), AUTO_KEEP_DAYS, ext = ".docx")
            pruneDated(ctx, SUB_MIRROR, emptyList(), MIRROR_KEEP_DAYS, ext = ".zip",
                fixedNames = listOf(MIRROR_NOTE))
            dedupeFixed(ctx, SUB_MIRROR, listOf(MIRROR_NOTE))
            // 旧版遗留：AI 单独那份数据包已经并进统一镜像，见过就删
            dedupeFixed(ctx, "ai", listOf("Sympnoia-ai.zip"))
            n = (before - count()).coerceAtLeast(0)
        } catch (e: Exception) {
        }
        return n
    }

    /** 某个子目录里所有文件的字节数（给界面显示占用用） */
    private fun dirBytes(ctx: Context, sub: String): Long {
        if (Build.VERSION.SDK_INT >= 29) {
            var sum = 0L
            try {
                ctx.contentResolver.query(
                    MediaStore.Files.getContentUri("external"),
                    arrayOf(MediaStore.MediaColumns.SIZE),
                    MediaStore.MediaColumns.RELATIVE_PATH + " LIKE ?",
                    arrayOf(rel(sub) + "/%"), null
                )?.use { c ->
                    while (c.moveToNext()) sum += c.getLong(0)
                }
            } catch (e: Exception) {
            }
            return sum
        }
        return legacyDir(sub).listFiles()?.sumOf { it.length() } ?: 0L
    }

    private fun sizeText(bytes: Long): String =
        if (bytes >= 1024 * 1024) String.format(Locale.CHINA, "%.1f MB", bytes / 1048576.0)
        else "${bytes / 1024} KB"

    /**
     * 自动产物现在占了多少 —— 给界面显示，让人一眼看出"它不会随着天数涨"。
     */
    /**
     * 给界面看的占用统计。
     *
     * ⚠️ 一律**数实际文件**，不再靠解析文件名里的日期去「推算份数」——
     *    以前那版还在说「自动压缩包 2 份（各覆盖 1 份）」「上限 7 天 × 2 种」，
     *    全是改规则之前的旧话，跟用户眼睛看到的当然对不上。
     */
    fun autoUsageText(ctx: Context): String {
        val mirrors = namesIn(ctx, SUB_MIRROR).count { it.endsWith(".zip") }
        val docs = namesIn(ctx, SUB_AUTO).count { it.endsWith(".docx") }
        val manualDocs = namesIn(ctx, SUB_MANUAL).count { it.endsWith(".docx") }
        val manualZips = namesIn(ctx, SUB_MANUAL).count { it.endsWith(".zip") }
        val total = dirBytes(ctx, SUB_MIRROR) + dirBytes(ctx, SUB_AUTO) + dirBytes(ctx, SUB_MANUAL)
        return "镜像 " + mirrors + " 份（上限 " + MIRROR_KEEP_DAYS + " 天）" +
                "　·　自动文档 " + docs + " 份（上限 " + AUTO_KEEP_DAYS + " 份）" +
                "　·　手动 " + (manualDocs + manualZips) + " 份" +
                "　·　共 " + sizeText(total)
    }

    /** 删掉指定文件名的文件 */
    fun delete(ctx: Context, name: String): Boolean = try {
        if (Build.VERSION.SDK_INT >= 29) {
            ctx.contentResolver.delete(
                MediaStore.Files.getContentUri("external"),
                MediaStore.MediaColumns.DISPLAY_NAME + "=?", arrayOf(name)
            ) > 0
        } else {
            var ok = false
            listOf(SUB_MANUAL, SUB_AUTO, SUB_MIRROR, SUB_RESTORE).forEach { sub ->
                if (File(legacyDir(sub), name).delete()) ok = true
            }
            ok
        }
    } catch (e: Exception) {
        false
    }

    /** 某前缀的文件名清单（按名字排序 = 按时间排序） */
    fun listNames(ctx: Context, sub: String, prefix: String = ""): List<String> {
        val out = ArrayList<String>()
        if (Build.VERSION.SDK_INT >= 29) {
            try {
                ctx.contentResolver.query(
                    MediaStore.Files.getContentUri("external"),
                    arrayOf(MediaStore.MediaColumns.DISPLAY_NAME),
                    MediaStore.MediaColumns.DISPLAY_NAME + " LIKE ?", arrayOf("$prefix%"), null
                )?.use { c ->
                    while (c.moveToNext()) c.getString(0)?.let { if (it.startsWith(prefix)) out.add(it) }
                }
            } catch (e: Exception) {
            }
        } else {
            legacyDir(sub).listFiles { f -> f.isFile && f.name.startsWith(prefix) }
                ?.forEach { out.add(it.name) }
        }
        out.sort()
        return out
    }

    /** 把一棵目录树写进 zip；exclude 只在最外层生效；返回写入的文件数 */
    fun zipTree(zos: ZipOutputStream, src: File, prefix: String, exclude: Set<String>): Int {
        var n = 0
        if (!src.isDirectory) return 0
        src.listFiles()?.sortedBy { it.name }?.forEach { child ->
            // ⚠️ 前缀匹配，不是精确匹配：AiStore 写盘用的是「唯一临时名 + 原子改名」，
            //    万一在 writeText 之后、rename 之前进程被杀，就会在原地留下一个
            //    ai_key.json.<纳秒>.tmp —— 里面是**明文 API Key**。精确匹配挡不住它，
            //    那个残留文件会被下一次导出原样打进 zip 里，落进公共目录。
            if (prefix.isBlank() && excluded(child.name, exclude)) return@forEach
            val rel = if (prefix.isBlank()) child.name else "$prefix/${child.name}"
            if (child.isDirectory) {
                n += zipTree(zos, child, rel, exclude)
            } else {
                try {
                    zos.putNextEntry(ZipEntry(rel))
                    child.inputStream().use { it.copyTo(zos) }
                    zos.closeEntry()
                    n++
                } catch (e: Exception) {
                    try {
                        zos.closeEntry()
                    } catch (e2: Exception) {
                    }
                }
            }
        }
        return n
    }

    /**
     * 这个文件名要不要排除在导出之外。
     * 除了精确名字，还要排掉「名字 + .」（临时文件），见上面 zipTree 里那段说明。
     */
    fun excluded(name: String, exclude: Set<String>): Boolean =
        exclude.any { it == name || name.startsWith("$it.") }

    /** 数一数字节，好在提示里报个体积 */
    private class Counting(private val sink: OutputStream) : FilterOutputStream(sink) {
        var count = 0L
        override fun write(b: Int) {
            sink.write(b); count++
        }

        override fun write(b: ByteArray, off: Int, len: Int) {
            sink.write(b, off, len); count += len
        }
    }

    private fun stamp(): String =
        SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(Date())

    private fun kb(bytes: Long): String =
        if (bytes >= 1024 * 1024) String.format(Locale.CHINA, "%.1f MB", bytes / 1048576.0)
        else "${bytes / 1024} KB"

    /**
     * 把若干个子目录压成一个 zip 写进公共存储（数据镜像用）。
     * 返回写入的文件数；写不进去返回 -1，由调用处决定要不要退回应用目录。
     *
     * 用「临时名写完整 → 删旧的 → 改名」的覆盖方式：
     * 数据镜像永远只有一份，先删后写的话，中途失败就一份都不剩了。
     */
    fun zipDirs(ctx: Context, sub: String, name: String, dirs: List<String>): Int {
        val tmp = name + ".new"
        deleteInSub(ctx, sub, tmp)
        val os = targetStream(ctx, sub, tmp, MIME_ZIP) ?: return -1
        val n = try {
            var k = 0
            ZipOutputStream(BufferedOutputStream(os)).use { zos ->
                dirs.forEach { d ->
                    val src = File(Store.root(ctx), d)
                    if (src.exists()) k += zipTree(zos, src, d, emptySet())
                }
            }
            k
        } catch (e: Exception) {
            deleteInSub(ctx, sub, tmp)
            return -1
        }
        deleteInSub(ctx, sub, name)
        if (!renameInSub(ctx, sub, tmp, name)) return -1
        return n
    }

    /** 把若干子目录压成一个 zip 写进应用自己的目录（公共存储被拒时的兜底） */
    fun zipDirsLegacy(root: File, name: String, dirs: List<String>): Int = try {
        val f = File(File(root, "backup"), name)
        f.parentFile?.mkdirs()
        FileOutputStream(f).use { os ->
            var n = 0
            ZipOutputStream(BufferedOutputStream(os)).use { zos ->
                dirs.forEach { d ->
                    val src = File(root, d)
                    if (src.exists()) n += zipTree(zos, src, d, emptySet())
                }
            }
            n
        }
    } catch (e: Exception) {
        -1
    }

    /**
     * 手动导出：整份数据（含 media 图片与视频）压成一个 zip，放进 Documents/<文件夹>/。
     * 手动导出**不限份数**，也不会被自动清理动到。
     */
    fun exportZip(ctx: Context): String {
        val root = Store.root(ctx)
        val name = "Sympnoia-data-${stamp()}.zip"
        return try {
            val os = targetStream(ctx, SUB_MANUAL, name, MIME_ZIP)
                ?: return "导出失败：无法创建文件（存储空间不足或系统拒绝写入）"
            val counter = Counting(os)
            var files = 0
            try {
                ZipOutputStream(BufferedOutputStream(counter)).use { zos ->
                    zos.putNextEntry(ZipEntry(NOTE_FILE))
                    zos.write(
                        ("Sympnoia 数据包\n" +
                                "导出时间：" + Dates.todayKey() + " " + Dates.hhmm(System.currentTimeMillis()) + "\n" +
                                "包含：events（每日记录）/ docs（文章与日记）/ names（唤名）/" +
                                "config（设置与图标）/ gallery（画像）/ media（图片与视频）/" +
                                "ai（AI 人设卡与对话记录）\n" +
                                "不含：ai_key.json —— 里面是 API Key，出于安全特意排除，换手机请重新填写。\n" +
                                "恢复：应用内「花房 · 数据 → 从备份恢复」，选中本文件即可（同名覆盖）。\n")
                            .toByteArray(Charsets.UTF_8)
                    )
                    zos.closeEntry()
                    files = zipTree(zos, root, "", SKIP_DIRS)
                }
            } catch (e: Exception) {
                delete(ctx, name)
                return "导出失败：${e.message}"
            }
            "已导出数据包：${pathText(SUB_MANUAL)}$name（$files 个文件 · ${kb(counter.count)}）"
        } catch (e: Exception) {
            "导出失败：${e.message}"
        }
    }

    /** 恢复留底：把当前数据先压一份到 restore/，只保留最近 [KEEP_RESTORE] 份 */
    private fun safetyCopy(ctx: Context, root: File): Boolean {
        val name = "Sympnoia-恢复前-${stamp()}.zip"
        val ok = try {
            val os = targetStream(ctx, SUB_RESTORE, name, MIME_ZIP) ?: return false
            ZipOutputStream(BufferedOutputStream(os)).use { zos ->
                zipTree(zos, root, "", SKIP_DIRS)
            }
            true
        } catch (e: Exception) {
            false
        }
        if (ok) {
            val names = listNames(ctx, SUB_RESTORE, "Sympnoia-恢复前-")
            val drop = names.size - KEEP_RESTORE
            for (i in 0 until drop) delete(ctx, names[i])
        }
        return ok
    }

    /** zip 里的条目名 → 数据目录里的相对路径；返回 null 表示这条不安全、跳过 */
    private fun safeEntry(raw: String): String? {
        var p = raw.replace('\\', '/').trim().trimStart('/')
        if (p.isBlank()) return null
        if (p == NOTE_FILE) return null
        // 兼容「把整个 Asephina 目录压进去」的包
        if (p.startsWith("Asephina/")) p = p.removePrefix("Asephina/")
        if (p.isBlank() || p.contains("..")) return null
        // 同样用前缀匹配：ai_key.json 的写盘临时残留也不能被恢复到数据目录里
        if (SKIP_DIRS.any { p == it || p.startsWith("$it/") || p.startsWith("$it.") }) return null
        return p
    }

    /**
     * 从 zip 恢复：先把当前数据留一份底，再把包里的文件同名覆盖写回数据目录。
     * 只覆盖包里有的文件，**不会删掉包里没有的现有文件**（这样最不容易出事）。
     */
    fun importZip(ctx: Context, uri: Uri): String {
        val root = Store.root(ctx)
        val safeOk = safetyCopy(ctx, root)
        var files = 0
        var skipped = 0
        try {
            val ins = ctx.contentResolver.openInputStream(uri)
                ?: return "恢复失败：读不到所选文件（可能已被移动或删除）"
            ins.use { raw ->
                ZipInputStream(BufferedInputStream(raw)).use { zis ->
                    while (true) {
                        val entry = zis.nextEntry ?: break
                        val name = entry.name ?: ""
                        val path = if (entry.isDirectory) null else safeEntry(name)
                        if (path == null) {
                            if (!entry.isDirectory) skipped++
                            zis.closeEntry()
                            continue
                        }
                        val f = File(root, path)
                        f.parentFile?.mkdirs()
                        f.outputStream().use { zis.copyTo(it) }
                        files++
                        zis.closeEntry()
                    }
                }
            }
        } catch (e: Exception) {
            return "恢复失败：${e.message}" +
                    (if (safeOk) "（恢复前的数据已留底，可在 restore 目录找回）" else "")
        }
        if (files == 0) return "恢复失败：这个包里没有可识别的数据（共跳过 $skipped 项）"
        return "已恢复 $files 个文件" +
                (if (skipped > 0) "（跳过 $skipped 项非数据内容）" else "") +
                (if (safeOk) "　·　恢复前的数据已留底：${pathText(SUB_RESTORE)}" else "　·　注意：恢复前留底失败")
    }
}

/* ==========================================================================
 *  五·七、每天自动备份（滚动保留最近 7 份）
 *  ---------------------------------------------------------------------------
 *  每天第一次打开软件时，把全部记录导出一份 Word 文档，放进**固定文件夹**：
 *    · Android 10+ → /Documents/Asephina/auto/
 *    · 更早的系统 → 应用自己的 Asephina/auto/
 *  文件名带日期（Asephina_auto_2026-09-24.docx），同一天重复导出直接覆盖那一天的；
 *  只留最近 7 份，第 8 份出现时删掉最旧的那一份 —— 于是永远握着「最近一周」。
 *  它和「手动导出」完全独立：手动导出用的是另一个前缀（Asephina_日期_时刻.docx），
 *  既不会被算进这 7 份，也不会被自动清理动到。
 * ======================================================================== */
object AutoBackup {

    /** 保留几份（与 Pub.AUTO_KEEP_DAYS 同一个数，留个名字给界面文案用） */
    const val KEEP = Pub.AUTO_KEEP_DAYS

    /** 自动文档的前缀：一天一份，日期进文件名 */
    private const val PREFIX = "Sympnoia_auto_"

    private const val MIME =
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document"

    private fun stampFile(ctx: Context): File =
        File(File(Store.root(ctx), "config"), "autobak.txt")

    /** 上次自动备份是哪一天（没备份过就是 null） */
    fun lastDay(ctx: Context): String? = try {
        val f = stampFile(ctx)
        if (f.exists()) f.readText().trim().ifBlank { null } else null
    } catch (e: Exception) {
        null
    }

    /**
     * 今天还没备份过就做一次；返回一句给界面看的话。
     * **必须在 IO 线程调用**：要读全部记录并生成 docx。
     */
    fun runIfNeeded(ctx: Context): String {
        val today = Dates.todayKey()
        // ★ 先收拾一遍：这一步**不看「今天备份过没有」**。
        //   历史版本可能已经攒下几十份重复文件（MediaStore 会把同名的改名成 "xxx (1).docx"，
        //   而老的清理逻辑只认「前缀+日期+.docx」，那些文件永远清不掉）。
        //   只有每次打开都收拾，用户已经占掉的空间才能立刻还回来。
        val tidied = Pub.tidyAutoOutputs(ctx)
        if (lastDay(ctx) == today) {
            return if (tidied > 0) "今天已备份 · 顺手清掉 $tidied 个多余文件" else "今天已备份"
        }
        // ★ 戳**先落**：这一天已经动过手了。
        //   以前把戳放在最后（想着「哪一步失败就下次重试」），可失败的结果就是
        //   每次打开都重来一遍 —— 用户看到的是「怎么老在自动备份」。
        //   备份本身是覆盖式的，重试没有意义，先落戳才是对的。
        try {
            writeAtomicText(stampFile(ctx), today)
        } catch (e: Exception) {
        }
        // 今天这两样都已经在了就没什么可做的（戳丢了也不会重复写）
        if (Pub.namesIn(ctx, Pub.SUB_AUTO).any { it == "$PREFIX$today.docx" } &&
            Pub.namesIn(ctx, Pub.SUB_MIRROR).any { it.startsWith(Pub.MIRROR_PREFIX + "_" + today) }
        ) {
            return "今天已备份"
        }
        return try {
            // ① 一份 Word：记录 + AI 全在里面（以前分两份，用户还得自己挑一个留）
            val bytes = WordExport.build(ctx)
            if (bytes.size < 500) return "自动备份失败：文档内容异常（${bytes.size} 字节）"
            val docxOk = Pub.writeBytesReplace(ctx, Pub.SUB_AUTO, "$PREFIX$today.docx", MIME, bytes)
            // ② 一份数据镜像 zip：整个数据目录，日期进文件名，滚动留最近 3 天
            val files = mirrorData(ctx, today)
            if (files < 0) return "自动备份：文档已存，但数据镜像失败"
            if (!docxOk) return "自动备份：数据镜像已存，但文档写入失败"
            // ③ 收拾目录：多出来的（被系统加过 "(1)" 的）一律清掉
            Pub.tidyAutoOutputs(ctx)
            "已自动备份（文档 1 份 · 镜像 $files 个文件，各留最近 ${Pub.AUTO_KEEP_DAYS} 天）"
        } catch (e: Exception) {
            "自动备份失败：${e.message}"
        }
    }

    /**
     * 数据镜像：把软件直接读取的原始数据（events / docs / names / config / gallery）
     * 压成**一个 zip**，每天覆盖同一份，放在公共存储：
     *      /Documents/<导出文件夹>/mirror/Sympnoia-mirror.zip
     * 为什么不再是应用目录里那堆散文件：Android 11 起 Android/data 不许第三方写入，
     * 卸载还会把备份连锅端走——备份必须活在外面。
     * 公共存储写不进去时（老系统 / 被拒），退回应用自己的 backup/ 目录，并如实返回结果。
     * 返回镜像进去的文件数；两条路都失败返回 -1。
     */
    private fun mirrorData(ctx: Context, day: String): Int {
        // 数据镜像里装的是**整个数据目录**：记录条目与正文、备注、自定义预设、AI 的人设卡 /
        // 对话 / 长期记忆 / 头像 / 图片 —— 一个不落。AI 的数据和记录本来就在同一个目录里，
        // 没有理由分开导。
        // 唯一进不去的是数据目录**顶层**的 ai_key.json：它不属于任何子目录，
        // 而这里只按子目录打包，所以 API Key 天生落不到公共目录。
        val dirs = Pub.DATA_DIRS
        val name = Pub.MIRROR_PREFIX + "_" + day + ".zip"
        val n = Pub.zipDirs(ctx, Pub.SUB_MIRROR, name, dirs)
        if (n >= 0) {
            try {
                Pub.writeBytes(
                    ctx, Pub.SUB_MIRROR, Pub.MIRROR_NOTE, Pub.MIME_TXT,
                    ("Sympnoia 数据镜像（自动 · 每天一份 · 只留最近 " + Pub.MIRROR_KEEP_DAYS + " 天）\n" +
                            "镜像时间：" + day + " " + Dates.hhmm(System.currentTimeMillis()) + "\n" +
                            "包含：" + Pub.DATA_DIRS.joinToString(" / ") + "\n" +
                            "　行迹与心情（events）、文章与日记含备注（docs）、唤名（names）、\n" +
                            "　自定义预设与图标（config）、画像（gallery）、插入的图片与视频（media）、\n" +
                            "　AI 的人设卡 / 对话 / 长期记忆 / 头像 / 表情包（ai）\n" +
                            "不含：ai_key.json（API Key 不在任何子目录里，天生进不了镜像）\n" +
                            "恢复：应用内「花房 · 数据 → 从备份恢复」，选中这个 zip 即可。\n")
                        .toByteArray(Charsets.UTF_8)
                )
            } catch (e: Exception) {
            }
            return n
        }
        // 兜底：写进应用自己的 backup/（旧系统或公共存储被系统拒绝时）
        return Pub.zipDirsLegacy(Store.root(ctx), name, dirs)
    }

    /** 复制一棵目录树；**出错就抛**，由调用处判成败（不再默默吞掉、把半个镜像当成功） */
    private fun copyTree(src: File, dst: File): Int {
        if (src.isDirectory) {
            if (!dst.exists() && !dst.mkdirs()) throw java.io.IOException("建不了目录：" + dst.path)
            var n = 0
            src.listFiles()?.forEach { n += copyTree(it, File(dst, it.name)) }
            return n
        }
        dst.parentFile?.mkdirs()
        src.copyTo(dst, overwrite = true)
        return 1
    }

    /** 数据镜像的位置（给人看的路径） */
    fun mirrorText(ctx: Context): String = Pub.pathText(Pub.SUB_MIRROR)

    /** 自动备份文件夹（给人看的路径） */
    fun folderText(ctx: Context): String = Pub.pathText(Pub.SUB_AUTO)

    /** 把文档写出去；返回是否真的写成功（写不进去时不再假装备份过了） */
    /** 自动 Word 的写盘：也走覆盖式（先写 .new 再换），别用「先删后写」留出空档 */
    private fun write(ctx: Context, name: String, bytes: ByteArray): Boolean =
        Pub.writeBytesReplace(ctx, Pub.SUB_AUTO, name, MIME, bytes)

    /** 只保留最近 [KEEP] 份，返回实际留存的数量 */
    /**
     * 清理自动文档。
     * 现在统一由 [Pub.pruneAutoDocs] 做：**按天**只留最近 7 天，
     * 而且把记录的文档与 AI 的文档放在一起算 —— 第 8 天起从最早那天整日覆盖。
     */
    private fun prune(ctx: Context): Int = Pub.pruneAutoDocs(ctx)
}

/* ==========================================================================
 *  五·六、运动计步（步数 → 行动熵）
 *  ---------------------------------------------------------------------------
 *  数据来源：系统计步传感器 TYPE_STEP_COUNTER（硬件低功耗，无需联网）。
 *    · 它给出的是「开机以来的累计步数」，所以当日步数 = 当前值 − 当天起点值；
 *    · 记录方式：常驻前台服务监听传感器（关掉软件也在记），
 *      另有 15 分钟一次的 JobScheduler 兜底采样（前台服务被系统清掉时）；
 *    · 关机重启后累计值归零，会自动结算上一段并重新起算；
 *    · 没有计步传感器或没给权限时，可以手动补记步数。
 * ======================================================================== */

object Steps {

    /** 每变化多少步、或多长时间，才落一次盘（避免频繁写文件） */
    private const val WRITE_DELTA = 25L

/** 主动互动「在运动」：一次采样至少要涨这么多步 */
private const val STEP_TRIGGER_DELTA = 300L

/** 主动互动「在运动」：每走这么多步最多触发一次 */
private const val STEP_TRIGGER_UNIT = 10_000L
    private const val WRITE_MS = 30000L

    /** 开机后重新起算的判定窗口 */
    private const val PREV_VALID_MS = 18L * 3600_000L

    private const val JOB_ID = 0x5EED01

    /** 计步设置 */
    data class Cfg(
        val enabled: Boolean = false,
        val goal: Int = 8000,
        val strideCm: Int = 68,
        val weightKg: Int = 60
    )

    /** 前台服务是否在运行（仅用于界面显示） */
    var serviceRunning by mutableStateOf(false)

    private fun cfgFile(ctx: Context) = File(File(Store.root(ctx), "config"), "steps.json")

    fun loadCfg(ctx: Context): Cfg {
        val f = cfgFile(ctx)
        if (!f.exists()) return Cfg()
        return try {
            val o = JSONObject(f.readText())
            Cfg(
                enabled = o.optBoolean("enabled", false),
                goal = o.optInt("goal", 8000).coerceIn(1000, AE.STEPS_CAP.toInt()),
                strideCm = o.optInt("strideCm", 68).coerceIn(30, 120),
                weightKg = o.optInt("weightKg", 60).coerceIn(25, 200)
            )
        } catch (e: Exception) {
            Cfg()
        }
    }

    fun saveCfg(ctx: Context, c: Cfg) {
        try {
            cfgFile(ctx).writeTextAtomic(
                JSONObject()
                    .put("enabled", c.enabled)
                    .put("goal", c.goal)
                    .put("strideCm", c.strideCm)
                    .put("weightKg", c.weightKg)
                    .toString(2)
            )
            cfgState = c
        } catch (e: Exception) {
        }
    }

    /** 内存缓存，避免界面反复读盘 */
    private var cfgState: Cfg? = null

    fun cfg(ctx: Context): Cfg {
        val c = cfgState
        if (c != null) return c
        val loaded = loadCfg(ctx)
        cfgState = loaded
        return loaded
    }

    /* ---------------- 权限 / 传感器 ---------------- */

    fun hasPermission(ctx: Context): Boolean =
        Build.VERSION.SDK_INT < 29 || ContextCompat.checkSelfPermission(
            ctx, Manifest.permission.ACTIVITY_RECOGNITION
        ) == PackageManager.PERMISSION_GRANTED

    fun sensorAvailable(ctx: Context): Boolean = try {
        val sm = ctx.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
        sm?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null
    } catch (e: Exception) {
        false
    }

    /* ---------------- 采样与落盘 ---------------- */

    /** 内存缓存：传感器事件很密，避免每次都读盘 */
    private var cacheDay: String? = null
    private var cacheSteps: Store.DaySteps? = null
    private var cacheAt = 0L
    private const val CACHE_MS = 5000L

    private fun cached(ctx: Context, day: String, now: Long): Store.DaySteps {
        val c = cacheSteps
        if (c != null && cacheDay == day && now - cacheAt < CACHE_MS) return c
        val loaded = Store.loadDaySteps(ctx, day)
        cacheDay = day
        cacheSteps = loaded
        cacheAt = now
        return loaded
    }

    private fun putCache(day: String, s: Store.DaySteps, now: Long) {
        cacheDay = day
        cacheSteps = s
        cacheAt = now
    }

    /**
     * 收到一次计步值。返回今日总步数。
     * 首次采样：以「昨日最后一次的计步值」为今天起点（这样跨零点的那几步也归今天）；
     * 计步值变小（重启）：结算上一段，重新起算。
     */
    @Synchronized
    fun ingest(ctx: Context, counter: Long, force: Boolean = false): Long {
        val day = Dates.todayKey()
        val now = System.currentTimeMillis()
        val cur = cached(ctx, day, now)
        var s = cur
        var needWrite = force
        if (cur.base < 0L || cur.last < 0L) {
            val prev = Store.loadDaySteps(ctx, Dates.shift(day, -1))
            val fresh = prev.last > 0L && prev.ts > 0L &&
                    now - prev.ts < PREV_VALID_MS && prev.last <= counter
            val start = if (fresh) prev.last else counter
            s = Store.DaySteps(accum = 0L, base = start, last = counter, offset = cur.offset, ts = now)
            needWrite = true
        } else if (counter < cur.last) {
            s = Store.DaySteps(
                accum = cur.sensor(), base = counter, last = counter,
                offset = cur.offset, ts = now
            )
            needWrite = true
        } else {
            val delta = counter - cur.last
            if (delta > 0L) {
                s = Store.DaySteps(cur.accum, cur.base, counter, cur.offset, now)
                if (delta >= WRITE_DELTA || now - cur.ts >= WRITE_MS) needWrite = true
            }
        }
        if (needWrite && s !== cur) {
            val before = cur.total()
            val after = s.total()
            Store.saveDaySteps(ctx, day, s)
            putCache(day, s, now)
            maybeNotifyMoving(ctx, before, after)
        }
        return s.total()
    }

    /**
     * 主动互动「在运动」。
     *
     * ⚠️ 判断挂在**传感器这个唯一入口**上，不放在运动页里 ——
     *    以前挂在写盘的 saveDaySteps 上，而那条路径只有切到运动页才会走，
     *    于是「不打开运动页就永远不触发」。现在只要软件开着，
     *    不管你在哪一页，计步值一涨就有机会触发。
     *
     * 两道闸：
     *   · 一次采样涨够 STEP_TRIGGER_DELTA 步（说明此刻真的在动，
     *     而不是夜里系统把攒了一整天的量一次性补报上来）；
     *   · 每满 STEP_TRIGGER_UNIT 步最多一次，一万步以内不会响第二次。
     */
    private fun maybeNotifyMoving(ctx: Context, before: Long, after: Long) {
        if (!appVisible) return
        if (after - before < STEP_TRIGGER_DELTA) return
        if (after / STEP_TRIGGER_UNIT <= before / STEP_TRIGGER_UNIT) return
        AiProactive.notify(
            ctx, AiEvent.STEPS,
            "我大概正在运动：今天已经走到 " + after + " 步了，刚这一会儿就涨了 " + (after - before) + " 步。"
        )
    }

    /**
     * 软件是不是正开着（在屏幕上）。
     * 主动互动只在你看着软件的时候发生 —— 切走了就不打扰。
     */
    @Volatile
    var appVisible = false

    /** 手动加上步数（用于没有传感器或补记） */
    @Synchronized
    fun addManual(ctx: Context, dayKey: String, delta: Long) {
        val now = System.currentTimeMillis()
        val cur = cached(ctx, dayKey, now)
        val s = Store.DaySteps(cur.accum, cur.base, cur.last, cur.offset + delta, cur.ts)
        Store.saveDaySteps(ctx, dayKey, s)
        putCache(dayKey, s, now)
    }

    /** 手动把当日步数设为某个值 */
    @Synchronized
    fun setManual(ctx: Context, dayKey: String, value: Long) {
        val now = System.currentTimeMillis()
        val cur = cached(ctx, dayKey, now)
        val v = value.coerceAtLeast(0L)
        val s = Store.DaySteps(cur.accum, cur.base, cur.last, v - cur.sensor(), cur.ts)
        Store.saveDaySteps(ctx, dayKey, s)
        putCache(dayKey, s, now)
    }

    /** 今天的步数（同步读盘，仅在非界面线程调用） */
    fun todayTotal(ctx: Context): Long = Store.loadDayStepsTotal(ctx, Dates.todayKey())

    /** 内存里已知的某天步数（未缓存过则返回 null，用于界面首帧，避免闪 0） */
    fun knownTotal(dayKey: String): Long? {
        val c = cacheSteps
        return if (c != null && cacheDay == dayKey) c.total() else null
    }

    /* ---------------- 前台服务 / 兜底任务 ---------------- */

    fun startService(ctx: Context) {
        try {
            val i = Intent(ctx, StepService::class.java)
            if (Build.VERSION.SDK_INT >= 26) ctx.startForegroundService(i)
            else ctx.startService(i)
        } catch (e: Exception) {
            // 后台限制时抛异常，忽略即可：还有兜底任务与打开软件时的采样
        }
    }

    fun stopService(ctx: Context) {
        try {
            ctx.stopService(Intent(ctx, StepService::class.java))
        } catch (e: Exception) {
        }
    }

    fun scheduleJob(ctx: Context) {
        try {
            val js = ctx.getSystemService(Context.JOB_SCHEDULER_SERVICE) as? JobScheduler ?: return
            if (js.getPendingJob(JOB_ID) != null) return
            val job = JobInfo.Builder(JOB_ID, ComponentName(ctx, StepJobService::class.java))
                .setPeriodic(15L * 60L * 1000L)
                .setPersisted(true)
                .build()
            js.schedule(job)
        } catch (e: Exception) {
        }
    }

    fun cancelJob(ctx: Context) {
        try {
            val js = ctx.getSystemService(Context.JOB_SCHEDULER_SERVICE) as? JobScheduler ?: return
            js.cancel(JOB_ID)
        } catch (e: Exception) {
        }
    }

    /** 开启自动记录（需已授权） */
    fun enable(ctx: Context) {
        saveCfg(ctx, cfg(ctx).copy(enabled = true))
        startService(ctx)
        scheduleJob(ctx)
    }

    /** 关闭自动记录 */
    fun disable(ctx: Context) {
        saveCfg(ctx, cfg(ctx).copy(enabled = false))
        stopService(ctx)
        cancelJob(ctx)
    }
}

/** 常驻计步的前台服务：关掉软件也在记 */
class StepService : Service(), SensorEventListener {

    private var sm: SensorManager? = null
    private var lastNotify = 0L

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        StepNotify.ensureChannel(this)
        if (!goForeground()) {
            stopSelf()
            return
        }
        Steps.serviceRunning = true
        listen()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!goForeground()) {
            stopSelf()
            return START_NOT_STICKY
        }
        listen()
        return START_STICKY
    }

    /** 进入前台；缺少「身体活动」权限时（health 类型要求）会抛异常，这里兜住不崩 */
    private fun goForeground(): Boolean = try {
        startForeground(StepNotify.ID, StepNotify.build(this, Steps.todayTotal(this)))
        true
    } catch (e: Exception) {
        false
    }

    private fun listen() {
        try {
            val m = getSystemService(Context.SENSOR_SERVICE) as? SensorManager ?: return
            val sensor = m.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) ?: return
            sm?.unregisterListener(this)
            m.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
            sm = m
        } catch (e: Exception) {
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val e = event ?: return
        if (e.sensor.type != Sensor.TYPE_STEP_COUNTER) return
        val today = Steps.ingest(this, e.values[0].toLong())
        val now = System.currentTimeMillis()
        if (now - lastNotify > 20000L) {
            lastNotify = now
            StepNotify.update(this, today)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onDestroy() {
        try {
            sm?.unregisterListener(this)
        } catch (e: Exception) {
        }
        Steps.serviceRunning = false
        super.onDestroy()
    }
}

/** 兜底：前台服务被系统清掉时，每 15 分钟采一次计步值 */
class StepJobService : JobService() {

    private var sm: SensorManager? = null
    private var params: JobParameters? = null
    private var done = false

    private val listener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            val e = event ?: return
            if (e.sensor.type != Sensor.TYPE_STEP_COUNTER) return
            try {
                Steps.ingest(applicationContext, e.values[0].toLong(), force = true)
            } catch (ex: Exception) {
            }
            finish()
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }

    override fun onStartJob(p: JobParameters?): Boolean {
        params = p
        done = false
        val m = getSystemService(Context.SENSOR_SERVICE) as? SensorManager
        val sensor = m?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if (m == null || sensor == null) return false
        sm = m
        try {
            m.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        } catch (e: Exception) {
            return false
        }
        // 传感器迟迟不给数据时的兜底（6 秒）
        Handler(Looper.getMainLooper()).postDelayed({ finish() }, 6000L)
        return true
    }

    private fun finish() {
        if (done) return
        done = true
        try {
            sm?.unregisterListener(listener)
        } catch (e: Exception) {
        }
        try {
            jobFinished(params, false)
        } catch (e: Exception) {
        }
    }

    override fun onStopJob(p: JobParameters?): Boolean {
        finish()
        return true
    }
}

/** 开机 / 应用更新后自动恢复计步 */
class StepBootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val ctx = context ?: return
        // 只认系统发的这几个动作，别的一律忽略（防止被伪造的 intent 拉起服务）
        when (intent?.action) {
            Intent.ACTION_BOOT_COMPLETED,
            Intent.ACTION_MY_PACKAGE_REPLACED,
            "android.intent.action.QUICKBOOT_POWERON" -> Unit
            else -> return
        }
        try {
            if (!Steps.loadCfg(ctx).enabled) return
            if (!Steps.hasPermission(ctx)) return
            Steps.startService(ctx)
            Steps.scheduleJob(ctx)
        } catch (e: Exception) {
        }
    }
}

/** 计步前台服务的常驻通知 */
object StepNotify {
    const val CHANNEL = "asephina_steps"
    const val ID = 0x5713

    fun ensureChannel(ctx: Context) {
        if (Build.VERSION.SDK_INT < 26) return
        try {
            val nm = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (nm.getNotificationChannel(CHANNEL) != null) return
            val ch = NotificationChannel(CHANNEL, "运动计步", NotificationManager.IMPORTANCE_MIN)
            ch.description = "记录每日步数，关掉软件也能继续记"
            ch.setShowBadge(false)
            nm.createNotificationChannel(ch)
        } catch (e: Exception) {
        }
    }

    private fun icon(): Int = R.drawable.ic_step

    private fun openIntent(ctx: Context): PendingIntent? = try {
        val i = Intent(ctx, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        PendingIntent.getActivity(
            ctx, 0, i,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    } catch (e: Exception) {
        null
    }

    fun build(ctx: Context, steps: Long): Notification {
        val cfg = Steps.cfg(ctx)
        val builder = if (Build.VERSION.SDK_INT >= 26)
            Notification.Builder(ctx, CHANNEL) else @Suppress("DEPRECATION") Notification.Builder(ctx)
        builder.setSmallIcon(icon())
            .setContentTitle("陪着你走 · 今日 $steps 步")
            .setContentText(
                "目标 ${cfg.goal} 步　｜　运动熵 " +
                        String.format(Locale.CHINA, "%.1f", AE.stepsAE(steps, cfg.goal)) +
                        (if (steps > AE.STEPS_CAP) "　｜　已触发整体 ×" +
                                String.format(Locale.CHINA, "%.1f", AE.STEPS_OVER_MUL) else "")
            )
            .setOngoing(true)
            .setShowWhen(false)
            .setColor(0xFFB9B3E6.toInt())
        openIntent(ctx)?.let { builder.setContentIntent(it) }
        return builder.build()
    }

    fun update(ctx: Context, steps: Long) {
        try {
            val nm = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nm.notify(ID, build(ctx, steps))
        } catch (e: Exception) {
        }
    }
}

/**
 * 番茄时钟的通知：正计时/倒计时就写在通知栏上，带「暂停 / 继续 / 完成 / 放弃」四个操作。
 * 前台服务活着，关掉软件、从后台划掉也照样走表。
 */
object PomoNotify {

    const val CHANNEL = "asephina_pomo"
    const val ID = 0x5714

    const val ACTION_PAUSE = "com.example.asephina.pomo.PAUSE"
    const val ACTION_RESUME = "com.example.asephina.pomo.RESUME"
    const val ACTION_FINISH = "com.example.asephina.pomo.FINISH"
    const val ACTION_ABANDON = "com.example.asephina.pomo.ABANDON"

    fun ensureChannel(ctx: Context) {
        if (Build.VERSION.SDK_INT < 26) return
        try {
            val nm = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (nm.getNotificationChannel(CHANNEL) != null) return
            val ch = NotificationChannel(CHANNEL, "番茄时钟", NotificationManager.IMPORTANCE_LOW)
            ch.description = "番茄钟倒计时，关掉软件也继续走"
            ch.setShowBadge(false)
            ch.setSound(null, null)
            nm.createNotificationChannel(ch)
        } catch (e: Exception) {
        }
    }

    private fun openIntent(ctx: Context): PendingIntent? = try {
        val i = Intent(ctx, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        PendingIntent.getActivity(
            ctx, 1, i,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    } catch (e: Exception) {
        null
    }

    private fun action(ctx: Context, action: String, code: Int): PendingIntent? = try {
        val i = Intent(ctx, PomoService::class.java).setAction(action)
        PendingIntent.getService(
            ctx, code, i,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    } catch (e: Exception) {
        null
    }

    private fun act(ctx: Context, icon: Int, text: String, pi: PendingIntent?): Notification.Action? =
        if (pi == null) null else Notification.Action.Builder(
            android.graphics.drawable.Icon.createWithResource(ctx, icon), text, pi
        ).build()

    /** 阶段名：专注 / 休息 / 大休 */
    private fun phaseName(): String = when (Pomo.phase) {
        Pomo.PHASE_WORK -> "专注"
        Pomo.PHASE_REST -> "休息"
        else -> "大休"
    }

    fun build(ctx: Context): Notification {
        val builder = if (Build.VERSION.SDK_INT >= 26)
            Notification.Builder(ctx, CHANNEL) else @Suppress("DEPRECATION") Notification.Builder(ctx)
        val clock = clockText(Pomo.remainMs)
        val c = try {
            Pomo.cfg(ctx)
        } catch (e: Exception) {
            Pomo.Cfg()
        }
        builder.setSmallIcon(R.drawable.ic_step)
            .setContentTitle(
                (if (Pomo.paused) "已暂停 · " else "") + phaseName() + " " + clock
            )
            .setContentText(
                if (Pomo.loopMode) {
                    "第 ${Pomo.cyclesDone + 1} 轮　｜　${c.workMin} 分专注 / ${c.restMin} 分休息"
                } else {
                    (c.title.ifBlank { "专注" }) + "　｜　共 " + clockText(Pomo.phaseTotalMs)
                }
            )
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setShowWhen(false)
            .setColor(0xFFB9B3E6.toInt())
        openIntent(ctx)?.let { builder.setContentIntent(it) }
        // 操作：暂停 / 继续（二选一）· 完成 · 放弃
        if (Pomo.paused) {
            act(ctx, R.drawable.ic_step, "继续", action(ctx, ACTION_RESUME, 11))
                ?.let { builder.addAction(it) }
        } else {
            act(ctx, R.drawable.ic_step, "暂停", action(ctx, ACTION_PAUSE, 12))
                ?.let { builder.addAction(it) }
        }
        act(ctx, R.drawable.ic_step, "完成", action(ctx, ACTION_FINISH, 13))
            ?.let { builder.addAction(it) }
        act(ctx, R.drawable.ic_step, "放弃", action(ctx, ACTION_ABANDON, 14))
            ?.let { builder.addAction(it) }
        return builder.build()
    }

    fun update(ctx: Context) {
        try {
            val nm = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nm.notify(ID, build(ctx))
        } catch (e: Exception) {
        }
    }
}

/**
 * 番茄时钟的前台服务：**关掉软件、从后台划掉也继续计时**。
 *   · 剩余时间是按「结束时刻」算的，所以杀进程、重启手机都不影响；
 *   · 每秒把剩余时间写到通知上，通知上能直接暂停 / 继续 / 完成 / 放弃；
 *   · 阶段走完：提示音或震动，循环模式自动进下一段，整轮结束就记一条行迹。
 */
class PomoService : Service() {

    private val handler = Handler(Looper.getMainLooper())
    private var lastTick = 0L
    /** 通知上现在显示的内容：变了才刷 */
    private var shownKey = ""

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        PomoNotify.ensureChannel(this)
        Pomo.cfg(this)          // 先把设置读进缓存，tick 里要用
        Pomo.loadRun(this)      // 接着上次的走
        if (!goForeground()) {
            stopSelf()
            return
        }
        Pomo.serviceRunning = true
        handler.post(ticker)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Pomo.cfg(this)
        Pomo.loadRun(this)
        when (intent?.action) {
            PomoNotify.ACTION_PAUSE -> {
                Pomo.pause()
                Pomo.saveRun(this)
            }
            PomoNotify.ACTION_RESUME -> {
                Pomo.start(this)
                Pomo.saveRun(this)
            }
            PomoNotify.ACTION_FINISH -> {
                Pomo.finishNow(this, true)
                stopSelf()
                return START_NOT_STICKY
            }
            PomoNotify.ACTION_ABANDON -> {
                Pomo.finishNow(this, false)
                stopSelf()
                return START_NOT_STICKY
            }
        }
        if (!Pomo.active) {
            stopSelf()
            return START_NOT_STICKY
        }
        if (!goForeground()) {
            stopSelf()
            return START_NOT_STICKY
        }
        PomoNotify.update(this)
        return START_STICKY
    }

    private val ticker = object : Runnable {
        override fun run() {
            try {
                step()
            } catch (e: Exception) {
            }
            if (Pomo.active) handler.postDelayed(this, 500L)
        }
    }

    private fun step() {
        if (!Pomo.active) {
            stopSelf()
            return
        }
        val now = System.currentTimeMillis()
        if (Pomo.tick(now)) {
            // 一个阶段走完：先提示（1.5 秒内不重复响）
            if (now - lastTick > 1500L) {
                lastTick = now
                Pomo.alert(this)
            }
            if (!Pomo.nextPhase(this)) {
                // 整轮结束：记一条行迹（完成）
                Pomo.finishNow(this, true)
                stopSelf()
                return
            }
            Pomo.saveRun(this)
            shownKey = ""      // 换阶段了，强制刷一次通知
        }
        // 通知只在「显示出来的那一秒变了」时刷：一秒一次就够，别每 0.5 秒去打扰系统
        val key = (if (Pomo.paused) "P" else "R") + Pomo.phase + "|" + clockText(Pomo.remainMs)
        if (key != shownKey) {
            shownKey = key
            PomoNotify.update(this)
        }
    }

    private fun goForeground(): Boolean = try {
        startForeground(PomoNotify.ID, PomoNotify.build(this))
        true
    } catch (e: Exception) {
        false
    }

    override fun onDestroy() {
        Pomo.serviceRunning = false
        try {
            handler.removeCallbacksAndMessages(null)
        } catch (e: Exception) {
        }
        super.onDestroy()
    }
}

/* ==========================================================================
 *  五·七、番茄时钟（倒计时 / 番茄循环）
 *  ---------------------------------------------------------------------------
 *  · 单次模式：自己拖动时/分/秒，倒计时结束即算完成；
 *  · 番茄模式：按「做 n 分 → 休 m 分」自动循环，n 轮后来一次大休，然后继续；
 *  · 结束提示：震动 / 响铃（系统内置铃声可多选，也可选本地音频文件）/ 都不；
 *  · 记录：完成 → 作为一条「来自番茄」的行迹（行动熵 ×1.1，循环模式按轮数再加成）；
 *          放弃 → 按已进行的有效时长记一条普通行迹（无加成）。
 * ======================================================================== */

object Pomo {

    const val PHASE_WORK = 0
    const val PHASE_REST = 1
    const val PHASE_LONG = 2

    const val ALERT_VIBRATE = 0
    const val ALERT_RING = 1
    const val ALERT_NONE = 2

    /** 番茄时钟设置（同时兼作「添加」弹窗的默认内容） */
    data class Cfg(
        /** 单次模式：时 / 分 / 秒 */
        val hours: Int = 0,
        val minutes: Int = 25,
        val seconds: Int = 0,
        /** 结束提示：0 震动 / 1 响铃 / 2 都不 */
        val alert: Int = ALERT_RING,
        /** 铃声（系统铃声 URI 或自定义文件的 URI，空 = 系统默认闹钟） */
        val ringUri: String = "",
        /** 番茄循环参数 */
        val workMin: Int = 25,
        val restMin: Int = 5,
        val cycles: Int = 4,
        val longRestMin: Int = 20,
        /** 「添加」弹窗的内容（也是自动记录时用的信息） */
        val title: String = "专注",
        val level: Int = 3,
        val forHer: Boolean = false,
        val note: String = ""
    )

    private fun cfgFile(ctx: Context) = File(File(Store.root(ctx), "config"), "pomo.json")

    fun load(ctx: Context): Cfg {
        val f = cfgFile(ctx)
        if (!f.exists()) return Cfg()
        return try {
            val o = JSONObject(f.readText())
            Cfg(
                hours = o.optInt("hours", 0).coerceIn(0, 23),
                minutes = o.optInt("minutes", 25).coerceIn(0, 59),
                seconds = o.optInt("seconds", 0).coerceIn(0, 59),
                alert = o.optInt("alert", ALERT_RING).coerceIn(0, 2),
                ringUri = o.optString("ringUri", ""),
                workMin = o.optInt("workMin", 25).coerceIn(1, 180),
                restMin = o.optInt("restMin", 5).coerceIn(1, 60),
                cycles = o.optInt("cycles", 4).coerceIn(1, 12),
                longRestMin = o.optInt("longRestMin", 20).coerceIn(1, 120),
                title = o.optString("title", "专注"),
                level = o.optInt("level", 3).coerceIn(1, 5),
                forHer = o.optBoolean("forHer", false),
                note = o.optString("note", "")
            )
        } catch (e: Exception) {
            Cfg()
        }
    }

    fun save(ctx: Context, c: Cfg) {
        try {
            cfgFile(ctx).writeTextAtomic(
                JSONObject()
                    .put("hours", c.hours).put("minutes", c.minutes).put("seconds", c.seconds)
                    .put("alert", c.alert).put("ringUri", c.ringUri)
                    .put("workMin", c.workMin).put("restMin", c.restMin)
                    .put("cycles", c.cycles).put("longRestMin", c.longRestMin)
                    .put("title", c.title).put("level", c.level)
                    .put("forHer", c.forHer).put("note", c.note)
                    .toString(2)
            )
            cache = c
        } catch (e: Exception) {
        }
    }

    private var cache: Cfg? = null

    fun cfg(ctx: Context): Cfg {
        cache?.let { return it }
        val c = load(ctx)
        cache = c
        return c
    }

    fun invalidate() {
        cache = null
    }

    /* ---------------- 计时引擎（界面与状态都在这一处，旋转屏幕不会丢） ---------------- */

    /** 页面是否打开 */
    var open by mutableStateOf(false)

    /** 是否番茄循环模式 */
    var loopMode by mutableStateOf(false)

    /** 是否正在走（含暂停时为 false） */
    var running by mutableStateOf(false)
    var paused by mutableStateOf(false)

    /** 当前阶段的时长与剩余（毫秒） */
    var phaseTotalMs by mutableStateOf(25 * 60_000L)
    var remainMs by mutableStateOf(25 * 60_000L)

    /** 运行中：本阶段结束时刻 */
    private var endAt = 0L

    /** 番茄模式：阶段 / 已完成轮数 / 累计已专注秒数 */
    var phase by mutableIntStateOf(PHASE_WORK)
    var cyclesDone by mutableIntStateOf(0)
    var workAccumSec by mutableLongStateOf(0L)

    /** 已经"开始过"（用于按钮的可用状态） */
    val active: Boolean get() = running || paused

    /** 前台服务是否在跑（在跑就由它负责走表，界面只管显示） */
    var serviceRunning = false

    /* ---------------- 运行状态落盘：关掉软件也接着计时 ---------------- */

    private fun runFile(ctx: Context) = File(File(Store.root(ctx), "config"), "pomo_run.json")

    fun saveRun(ctx: Context) {
        try {
            runFile(ctx).writeTextAtomic(
                JSONObject()
                    .put("active", active)
                    .put("running", running)
                    .put("paused", paused)
                    .put("endAt", endAt)
                    .put("phaseTotalMs", phaseTotalMs)
                    .put("remainMs", remainMs)
                    .put("phase", phase)
                    .put("cyclesDone", cyclesDone)
                    .put("workAccumSec", workAccumSec)
                    .put("loop", loopMode)
                    .toString(2)
            )
        } catch (e: Exception) {
        }
    }

    /**
     * 清掉这一轮的状态。
     * **不删文件，而是写一份「没在跑」的**：「循环模式」是常驻开关，与哪一轮在不在跑无关；
     * 文件一删，它下次进来又回到关着的状态了。
     */
    fun clearRun(ctx: Context) {
        saveRun(ctx)
    }

    /**
     * 把上次的运行状态读回来。倒计时是**按结束时刻算**的，
     * 所以哪怕进程被系统杀掉、手机重启，剩下的时间也是对的。
     */
    fun loadRun(ctx: Context) {
        try {
            val f = runFile(ctx)
            if (!f.exists()) return
            val o = JSONObject(f.readText())
            // 循环模式是常驻的开关，跟「这次在不在跑」无关：先读它，
            // 否则退出应用前打开的循环，下次进来又变回关着的了。
            loopMode = o.optBoolean("loop", false)
            if (!o.optBoolean("active", false)) return
            running = o.optBoolean("running", false)
            paused = o.optBoolean("paused", false)
            endAt = o.optLong("endAt", 0L)
            phaseTotalMs = o.optLong("phaseTotalMs", 25 * 60_000L).coerceAtLeast(1000L)
            remainMs = o.optLong("remainMs", phaseTotalMs)
            phase = o.optInt("phase", PHASE_WORK)
            cyclesDone = o.optInt("cyclesDone", 0)
            workAccumSec = o.optLong("workAccumSec", 0L)
            if (running && endAt > 0L) {
                remainMs = (endAt - System.currentTimeMillis()).coerceAtLeast(0L)
            }
        } catch (e: Exception) {
        }
    }

    /**
     * 结束本次番茄：完成（finished=true）或放弃，记一条行迹并把状态清干净。
     * 界面上的「完成 / 放弃」与通知栏上的两个按钮都走这里，逻辑只有一份。
     */
    fun finishNow(ctx: Context, finished: Boolean): String {
        val c = cfg(ctx)
        val sec: Long
        val cyc: Int
        if (finished) {
            sec = if (loopMode) effectiveWorkSec(c) else (phaseTotalMs / 1000L)
            cyc = if (loopMode) effectiveCycles(c) else 1
        } else {
            sec = if (loopMode) effectiveWorkSec(c)
            else ((phaseTotalMs - remainMs) / 1000L).coerceAtLeast(1L)
            cyc = if (loopMode) effectiveCycles(c) else 1
        }
        val msg = if (sec <= 0L) "还没有有效专注时间"
        else record(ctx, Dates.todayKey(), sec, cyc, finished)
        lastMsg = msg
        reset()
        clearRun(ctx)
        stopService(ctx)
        return msg
    }

    /* ---------------- 前台服务 ---------------- */

    fun startService(ctx: Context) {
        try {
            val i = Intent(ctx, PomoService::class.java)
            if (Build.VERSION.SDK_INT >= 26) ctx.startForegroundService(i) else ctx.startService(i)
        } catch (e: Exception) {
        }
    }

    fun stopService(ctx: Context) {
        try {
            ctx.stopService(Intent(ctx, PomoService::class.java))
        } catch (e: Exception) {
        }
    }

    /** 番茄模式未开始时的显示值：直接用「专注」设定时长 */
    fun applyLoopDefaults(c: Cfg) {
        if (active) return
        phase = PHASE_WORK
        phaseTotalMs = totalOf(c, PHASE_WORK)
        remainMs = phaseTotalMs
    }

    /** 最近一条提示（记录结果等），页面与行迹页都能看到 */
    var lastMsg by mutableStateOf("")

    private fun totalOf(c: Cfg, ph: Int): Long = when (ph) {
        PHASE_WORK -> c.workMin * 60_000L
        PHASE_REST -> c.restMin * 60_000L
        else -> c.longRestMin * 60_000L
    }

    /** 单次模式：按拖动出来的时/分/秒设定 */
    fun setSingleTotal(h: Int, m: Int, s: Int) {
        if (active) return
        val ms = ((h * 3600 + m * 60 + s).coerceAtLeast(1)) * 1000L
        phaseTotalMs = ms
        remainMs = ms
    }

    /** 开始（或从暂停继续） */
    fun start(ctx: Context) {
        val c = cfg(ctx)
        if (paused) {
            endAt = System.currentTimeMillis() + remainMs
            paused = false
            running = true
            return
        }
        if (running) return
        if (loopMode) {
            phase = PHASE_WORK
            cyclesDone = 0
            workAccumSec = 0L
            phaseTotalMs = totalOf(c, PHASE_WORK)
            remainMs = phaseTotalMs
        } else {
            if (phaseTotalMs <= 0L || remainMs <= 0L) {
                phaseTotalMs = ((c.hours * 3600 + c.minutes * 60 + c.seconds)
                    .coerceAtLeast(1)) * 1000L
                remainMs = phaseTotalMs
            } else {
                remainMs = phaseTotalMs
            }
        }
        endAt = System.currentTimeMillis() + remainMs
        running = true
        paused = false
    }

    fun pause() {
        if (!running) return
        remainMs = (endAt - System.currentTimeMillis()).coerceAtLeast(0L)
        running = false
        paused = true
    }

    /** 由界面按帧调用；返回 true 表示这一帧刚好走完一个阶段 */
    fun tick(now: Long): Boolean {
        if (!running) return false
        val left = endAt - now
        if (left > 0L) {
            remainMs = left
            return false
        }
        remainMs = 0L
        // 阶段结束：先把这一轮的工作时间结算进去
        val c = cache ?: return false
        if (loopMode && phase == PHASE_WORK) {
            workAccumSec += (phaseTotalMs / 1000L)
            cyclesDone += 1
        }
        return true
    }

    /** 进入下一个阶段（番茄模式自动循环），返回 false 表示整轮结束并停下 */
    fun nextPhase(ctx: Context): Boolean {
        val c = cfg(ctx)
        if (!loopMode) {
            running = false
            paused = false
            remainMs = 0L
            return false
        }
        phase = when (phase) {
            PHASE_WORK -> if (cyclesDone % c.cycles == 0 && cyclesDone > 0) PHASE_LONG else PHASE_REST
            else -> PHASE_WORK
        }
        phaseTotalMs = totalOf(c, phase)
        remainMs = phaseTotalMs
        endAt = System.currentTimeMillis() + remainMs
        running = true
        paused = false
        return true
    }

    fun reset() {
        running = false
        paused = false
        cyclesDone = 0
        workAccumSec = 0L
        phase = PHASE_WORK
        remainMs = phaseTotalMs
    }

    /** 本次已经"有效专注"的秒数（番茄模式只算工作阶段，放弃时也照此记录） */
    fun effectiveWorkSec(c: Cfg): Long {
        val cur = if (running && phase == PHASE_WORK) {
            ((phaseTotalMs - remainMs) / 1000L).coerceAtLeast(0L)
        } else 0L
        return workAccumSec + cur
    }

    /** 本次完成的轮数 */
    fun effectiveCycles(c: Cfg): Int {
        val cur = if (running && phase == PHASE_WORK && remainMs < phaseTotalMs) 1 else 0
        return cyclesDone + cur
    }

    /* ---------------- 结束提示 ---------------- */

    fun alert(ctx: Context) {
        val c = cfg(ctx)
        when (c.alert) {
            ALERT_VIBRATE -> vibrate(ctx)
            ALERT_RING -> ring(ctx, c.ringUri)
            else -> Unit
        }
    }

    private fun vibrate(ctx: Context) {
        try {
            val v = if (Build.VERSION.SDK_INT >= 31) {
                (ctx.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager)
                    ?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                ctx.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            } ?: return
            if (!v.hasVibrator()) return
            if (Build.VERSION.SDK_INT >= 26) {
                v.vibrate(
                    VibrationEffect.createWaveform(longArrayOf(0, 320, 180, 320, 180, 520), -1)
                )
            } else {
                @Suppress("DEPRECATION")
                v.vibrate(longArrayOf(0, 320, 180, 320, 180, 520), -1)
            }
        } catch (e: Exception) {
        }
    }

    private fun ring(ctx: Context, uriText: String) {
        try {
            val uri = if (uriText.isBlank()) {
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            } else {
                Uri.parse(uriText)
            }
            RingtoneManager.getRingtone(ctx, uri)?.play()
        } catch (e: Exception) {
        }
    }

    /** 可选铃声列表：系统内置的几种 + 自定义文件 */
    class Tone(val label: String, val uri: String)

    fun builtinTones(ctx: Context): List<Tone> {
        val out = mutableListOf<Tone>()
        out.add(Tone("系统闹钟（默认）", ""))
        try {
            val rm = RingtoneManager(ctx)
            rm.setType(RingtoneManager.TYPE_ALARM)
            val cur = rm.cursor
            var n = 0
            while (cur.moveToNext() && n < 6) {
                val uri = rm.getRingtoneUri(cur.position) ?: continue
                val title = cur.getString(RingtoneManager.TITLE_COLUMN_INDEX) ?: continue
                out.add(Tone("闹钟 · $title", uri.toString()))
                n++
            }
            cur.close()
        } catch (e: Exception) {
        }
        try {
            listOf(
                RingtoneManager.TYPE_NOTIFICATION to "通知",
                RingtoneManager.TYPE_RINGTONE to "来电"
            ).forEach { (type, name) ->
                val uri = RingtoneManager.getDefaultUri(type)
                if (uri != null) out.add(Tone("系统$name 铃声", uri.toString()))
            }
        } catch (e: Exception) {
        }
        return out
    }

    fun toneLabel(ctx: Context, uriText: String): String {
        if (uriText.isBlank()) return "系统闹钟（默认）"
        return try {
            RingtoneManager.getRingtone(ctx, Uri.parse(uriText))?.getTitle(ctx)
                ?: "自定义铃声"
        } catch (e: Exception) {
            "自定义铃声"
        }
    }

    /* ---------------- 记录成行迹 ---------------- */

    /**
     * 把这次番茄写进行迹。
     * finished = true：完成，标记「来自番茄」，行动熵 ×1.1（循环模式再按轮数加成）；
     * finished = false：放弃，按已进行的时长记一条普通行迹，无加成。
     */
    fun record(
        ctx: Context,
        dayKey: String,
        workSec: Long,
        cycles: Int,
        finished: Boolean
    ): String {
        val c = cfg(ctx)
        val minutes = ((workSec + 30) / 60).toInt().coerceAtLeast(1)
        val title = c.title.ifBlank { "专注" }
        val ev = Ev(
            id = System.currentTimeMillis(),
            title = title,
            level = c.level,
            minutes = minutes,
            eff = 100,
            forHer = c.forHer,
            note = c.note,
            pomo = finished,
            pomoCycles = if (finished && cycles > 1) cycles else 0
        )
        return try {
            val list = Store.loadDay(ctx, dayKey).toMutableList()
            list.add(ev)
            Store.saveDay(ctx, dayKey, list)
            val ae = String.format(Locale.CHINA, "%.1f", AE.ofEvent(ev))
            if (finished) {
                "已记入行迹：$title　$minutes 分钟" +
                        (if (ev.pomoCycles > 0) "　$cycles 轮" else "") + "　行动熵 $ae（含番茄加成）"
            } else {
                "已记入行迹：$title　有效 $minutes 分钟　行动熵 $ae（放弃，无加成）"
            }
        } catch (e: Exception) {
            "记录失败：${e.message}"
        }
    }
}

/* ==========================================================================
 *  五·五、Word 导出（.docx）—— 纯文本 + 图片，不导出视频
 *  说明：.docx 本质是一个 ZIP 包（OOXML），这里用系统自带的 ZipOutputStream
 *        直接组装 [Content_Types].xml / _rels / word/document.xml / word/media，
 *        因此不需要引入任何第三方库。
 * ======================================================================== */

object WordExport {

    private fun esc(s: String): String = s
        .replace("&", "&amp;")
        .replace("<", "&lt;")
        .replace(">", "&gt;")
        .replace("\"", "&quot;")

    /** 简易文档构建器 */
    private class Doc {
        val body = StringBuilder()
        val rels = mutableListOf<Triple<String, String, ByteArray>>()  // rid, 文件名, 数据

        fun h1(text: String) = para(text, bold = true, size = 36, color = "7B5EA7")
        fun h2(text: String) = para(text, bold = true, size = 28, color = "5A5580")
        fun h3(text: String) = para(text, bold = true, size = 24, color = "8882B8")
        fun p(text: String) = para(text, size = 21)
        fun quote(text: String) = para(text, size = 21, color = "8882B8", italic = true)
        fun small(text: String) = para(text, size = 18, color = "999999")

        private fun para(
            text: String,
            bold: Boolean = false,
            size: Int = 21,
            color: String? = null,
            italic: Boolean = false
        ) {
            val rPr = StringBuilder("<w:rPr>")
            rPr.append("<w:rFonts w:ascii=\"Microsoft YaHei\" w:eastAsia=\"Microsoft YaHei\" ")
            rPr.append("w:hAnsi=\"Microsoft YaHei\"/>")
            if (bold) rPr.append("<w:b/>")
            if (italic) rPr.append("<w:i/>")
            if (color != null) rPr.append("<w:color w:val=\"$color\"/>")
            rPr.append("<w:sz w:val=\"$size\"/><w:szCs w:val=\"$size\"/>")
            rPr.append("</w:rPr>")
            body.append("<w:p><w:pPr><w:spacing w:before=\"40\" w:after=\"40\"/></w:pPr>")
            body.append("<w:r>$rPr<w:t xml:space=\"preserve\">${esc(text)}</w:t></w:r></w:p>")
        }

        /** 正文（保留换行） */
        fun body(text: String) {
            text.split("\n").forEach { line -> p(line) }
        }

        /** 直接塞一段已经拼好的段落 XML（AI 那一章由 AiBackup 生成，省得两套排版重复写一遍） */
        fun raw(xml: String) {
            body.append(xml)
        }

        /* ---------------- 富文本：按分片导出字体格式、对齐与文中插图 ---------------- */

        private fun hex(c: Int): String = String.format(Locale.CHINA, "%06X", c and 0xFFFFFF)

        private fun runXml(text: String, s: DocSpan?): String {
            val rPr = StringBuilder("<w:rPr>")
            val fam = when (s?.font ?: 0) {
                1 -> "SimSun"
                2 -> "Consolas"
                3 -> "KaiTi"
                else -> "Microsoft YaHei"
            }
            rPr.append("<w:rFonts w:ascii=\"$fam\" w:eastAsia=\"$fam\" w:hAnsi=\"$fam\"/>")
            if (s?.bold == true) rPr.append("<w:b/>")
            if (s?.italic == true) rPr.append("<w:i/>")
            if (s?.underline == true) rPr.append("<w:u w:val=\"single\"/>")
            if (s?.strike == true) rPr.append("<w:strike/>")
            if (s != null && s.color != 0) rPr.append("<w:color w:val=\"${hex(s.color)}\"/>")
            val sz = if (s != null && s.size > 0f) (s.size * 2).toInt() else 21
            rPr.append("<w:sz w:val=\"$sz\"/><w:szCs w:val=\"$sz\"/>")
            if (s != null && s.bg != 0) {
                rPr.append("<w:shd w:val=\"clear\" w:fill=\"${hex(s.bg)}\"/>")
            }
            rPr.append("</w:rPr>")
            return "<w:r>$rPr<w:t xml:space=\"preserve\">${esc(text)}</w:t></w:r>"
        }

        private fun sameSpan(a: DocSpan?, b: DocSpan?): Boolean = when {
            a == null && b == null -> true
            a != null && b != null -> a.sameAs(b)
            else -> false
        }

        /** 一段纯文字 → 若干段落（按换行切），行内按分片切 run，并带上对齐 */
        private fun richParagraphs(seg: String, spans: List<DocSpan>, segStart: Int) {
            var o = 0
            while (true) {
                val nl = seg.indexOf('\n', o)
                val line = if (nl < 0) seg.substring(o) else seg.substring(o, nl)
                val lineStart = segStart + o
                val pPr = StringBuilder("<w:pPr><w:spacing w:before=\"40\" w:after=\"40\"/>")
                val align = spans.firstOrNull { lineStart in it.start until it.end }?.align ?: 0
                if (align != 0) {
                    pPr.append("<w:jc w:val=\"").append(if (align == 1) "center" else "right")
                        .append("\"/>")
                }
                pPr.append("</w:pPr>")
                body.append("<w:p>").append(pPr)
                var i = 0
                while (i < line.length) {
                    val a = lineStart + i
                    val s1 = spans.firstOrNull { a >= it.start && a < it.end }
                    var j = i + 1
                    while (j < line.length) {
                        val b = lineStart + j
                        val s2 = spans.firstOrNull { b >= it.start && b < it.end }
                        if (!sameSpan(s1, s2)) break
                        j++
                    }
                    body.append(runXml(line.substring(i, j), s1))
                    i = j
                }
                body.append("</w:p>")
                if (nl < 0) break
                o = nl + 1
            }
        }

        /**
         * 正文导出：按占位符把图片插进文字中间（视频只留一行说明）。
         * images / videos 按文档中的出现顺序对应。
         */
        fun richBody(
            text: String,
            spans: List<DocSpan>,
            images: List<String>,
            videos: List<String>
        ) {
            var imgIdx = 0
            var vidIdx = 0
            val seg = StringBuilder()
            var segStart = 0
            fun flush() {
                if (seg.isNotEmpty()) {
                    richParagraphs(seg.toString(), spans, segStart)
                    seg.setLength(0)
                }
            }

            text.forEachIndexed { i, c ->
                when (c) {
                    ATTACH_MARK -> {
                        flush()
                        images.getOrNull(imgIdx)?.let { path ->
                            val f = File(path)
                            if (f.exists()) image(f)
                        }
                        imgIdx++
                        segStart = i + 1
                    }
                    '\uFFFD' -> {
                        flush()
                        small("（视频${vidIdx + 1}：未导出，可在应用内播放）")
                        vidIdx++
                        segStart = i + 1
                    }
                    else -> seg.append(c)
                }
            }
            flush()
            // 没有占位符的旧文档：图片按老办法附在正文后
            if (imgIdx == 0) {
                images.forEach { path ->
                    val f = File(path)
                    if (f.exists()) image(f)
                }
            } else if (images.size > imgIdx) {
                images.drop(imgIdx).forEach { path ->
                    val f = File(path)
                    if (f.exists()) image(f)
                }
            }
        }

        fun pageBreak() {
            body.append("<w:p><w:r><w:br w:type=\"page\"/></w:r></w:p>")
        }

        fun image(file: File) {
            val bytes = try {
                file.readBytes()
            } catch (e: Exception) {
                return
            }
            var w = 800
            var h = 600
            try {
                val opts = BitmapFactory.Options().apply { inJustDecodeBounds = true }
                BitmapFactory.decodeFile(file.absolutePath, opts)
                if (opts.outWidth > 0 && opts.outHeight > 0) {
                    w = opts.outWidth
                    h = opts.outHeight
                }
            } catch (e: Exception) {
            }
            val maxW = 5.6 * 914400.0
            val maxH = 6.8 * 914400.0
            var cx = w * 9525.0
            var cy = h * 9525.0
            if (cx > maxW) {
                cy = cy * maxW / cx
                cx = maxW
            }
            if (cy > maxH) {
                cx = cx * maxH / cy
                cy = maxH
            }
            val ext = file.extension.lowercase(Locale.CHINA).ifBlank { "png" }
            val stored = "image${rels.size + 1}.$ext"
            val rid = "rIdImg${rels.size + 1}"
            rels.add(Triple(rid, stored, bytes))
            body.append("<w:p><w:r><w:drawing><wp:inline distT=\"0\" distB=\"0\" distL=\"0\" distR=\"0\">")
            body.append("<wp:extent cx=\"${cx.toLong()}\" cy=\"${cy.toLong()}\"/>")
            body.append("<wp:docPr id=\"${rels.size}\" name=\"$stored\"/>")
            body.append("<a:graphic><a:graphicData uri=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">")
            body.append("<pic:pic><pic:nvPicPr><pic:cNvPr id=\"${rels.size}\" name=\"$stored\"/>")
            body.append("<pic:cNvPicPr/></pic:nvPicPr>")
            body.append("<pic:blipFill><a:blip r:embed=\"$rid\"/>")
            body.append("<a:stretch><a:fillRect/></a:stretch></pic:blipFill>")
            body.append("<pic:spPr><a:xfrm><a:off x=\"0\" y=\"0\"/>")
            body.append("<a:ext cx=\"${cx.toLong()}\" cy=\"${cy.toLong()}\"/></a:xfrm>")
            body.append("<a:prstGeom prst=\"rect\"><a:avLst/></a:prstGeom></pic:spPr>")
            body.append("</pic:pic></a:graphicData></a:graphic></wp:inline></w:drawing></w:r></w:p>")
        }

        fun toBytes(): ByteArray {
            val bos = ByteArrayOutputStream()
            val zip = ZipOutputStream(BufferedOutputStream(bos))
            fun putText(name: String, content: String) {
                zip.putNextEntry(ZipEntry(name))
                zip.write(content.toByteArray(Charsets.UTF_8))
                zip.closeEntry()
            }

            fun putBytes(name: String, content: ByteArray) {
                zip.putNextEntry(ZipEntry(name))
                zip.write(content)
                zip.closeEntry()
            }

            val exts = rels.map { it.second.substringAfterLast('.', "png") }.distinct()
            val ct = StringBuilder()
            ct.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>")
            ct.append("<Types xmlns=\"http://schemas.openxmlformats.org/package/2006/content-types\">")
            ct.append("<Default Extension=\"rels\" ")
            ct.append("ContentType=\"application/vnd.openxmlformats-package.relationships+xml\"/>")
            ct.append("<Default Extension=\"xml\" ContentType=\"application/xml\"/>")
            exts.forEach { e ->
                val mime = when (e) {
                    "png" -> "image/png"
                    "jpg", "jpeg" -> "image/jpeg"
                    "gif" -> "image/gif"
                    "webp" -> "image/webp"
                    else -> "application/octet-stream"
                }
                ct.append("<Default Extension=\"$e\" ContentType=\"$mime\"/>")
            }
            ct.append("<Override PartName=\"/word/document.xml\" ContentType=\"application/")
            ct.append("vnd.openxmlformats-officedocument.wordprocessingml.document.main+xml\"/>")
            ct.append("</Types>")
            putText("[Content_Types].xml", ct.toString())

            putText(
                "_rels/.rels",
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                        "<Relationships xmlns=\"http://schemas.openxmlformats.org/package/2006/relationships\">" +
                        "<Relationship Id=\"rIdDoc\" Type=\"http://schemas.openxmlformats.org/" +
                        "officeDocument/2006/relationships/officeDocument\" Target=\"word/document.xml\"/>" +
                        "</Relationships>"
            )

            val rl = StringBuilder()
            rl.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>")
            rl.append("<Relationships xmlns=\"http://schemas.openxmlformats.org/package/2006/relationships\">")
            rels.forEach { (rid, stored, _) ->
                rl.append("<Relationship Id=\"$rid\" Type=\"http://schemas.openxmlformats.org/")
                rl.append("officeDocument/2006/relationships/image\" Target=\"media/$stored\"/>")
            }
            rl.append("</Relationships>")
            putText("word/_rels/document.xml.rels", rl.toString())

            val doc = StringBuilder()
            doc.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>")
            doc.append("<w:document ")
            doc.append("xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" ")
            doc.append("xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\" ")
            doc.append("xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\" ")
            doc.append("xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\" ")
            doc.append("xmlns:pic=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">")
            doc.append("<w:body>")
            doc.append(body)
            doc.append("<w:sectPr><w:pgSz w:w=\"11906\" w:h=\"16838\"/>")
            doc.append("<w:pgMar w:top=\"1134\" w:right=\"1134\" w:bottom=\"1134\" w:left=\"1134\"/>")
            doc.append("</w:sectPr></w:body></w:document>")
            putText("word/document.xml", doc.toString())

            rels.forEach { (_, stored, bytes) -> putBytes("word/media/$stored", bytes) }

            // 关键：必须 close（或至少 flush）。只调用 finish() 的话，
            // 中央目录还留在 BufferedOutputStream 的缓冲区里，
            // 生成的 .docx 会成为残缺的 ZIP，Word 打开就是空白 / 报错。
            zip.finish()
            zip.flush()
            bos.flush()
            return bos.toByteArray()
        }
    }

    /** 生成整份 Word 文档 */
    fun build(ctx: Context): ByteArray {
        val d = Doc()
        val today = Dates.todayKey()
        val idx = Store.buildIndex(ctx)
        val names = Store.loadNames(ctx)

        d.h1(
            Persona.data.shortName + " · " + Persona.data.displayName + " —— 记录导出"
        )
        d.small("导出时间：" + Dates.stamp(System.currentTimeMillis()))
        val flowerFull = (idx.total / 800.0).toInt()
        val flowerProgress = ((idx.total - flowerFull * 800.0) / 800.0 * 100).toInt()
        d.p(
            "累计行动熵：" + String.format(Locale.CHINA, "%.1f", idx.total) +
                    "　｜　开花度：" + flowerProgress + "%　｜　已盛开：" + flowerFull + " 朵" +
                    "　｜　已记录：" + idx.traceByDay.size + " 天　｜　呼唤过" +
                            Persona.pro() + "：" + names.size + " 次"
        )
        val scfg = Steps.cfg(ctx)
        if (idx.totalSteps > 0L) {
            d.p(
                "累计步数：" + idx.totalSteps + " 步　｜　约 " +
                        String.format(Locale.CHINA, "%.1f", AE.stepsToKm(idx.totalSteps, scfg.strideCm)) +
                        " 公里　｜　约 " +
                        String.format(Locale.CHINA, "%.0f", AE.stepsToKcal(idx.totalSteps, scfg.weightKg)) +
                        " 千卡"
            )
        }

        val knock = Store.loadKnock(ctx)
        if (knock > 0L) {
            d.p(
                Persona.data.knockVerb + "：" + knock + " 次（联结页那朵" +
                        Persona.data.knockName + "，终身累计，不按天结算）"
            )
        }

        // 终身累计的条数：把每一类都点清楚，别漏项
        d.p(
            "终身累计：行迹 " + idx.totalEvents + " 条　｜　文章 " + idx.totalDocs +
                    " 篇　｜　心情 " + idx.totalMoods + " 条　｜　运动记录 " +
                    idx.stepsByDay.size + " 天　｜　写下名字 " + idx.totalNames + " 次"
        )
        d.p(
            "　　　　　番茄完成 " + idx.totalPomos + " 次　｜　记过东西的天数 " +
                    idx.recordedDays + " 天" +
                    (idx.firstDay?.let { "（起于 " + it + "）" } ?: "") +
                    "　｜　存在次数 " + (idx.total / 1597.0).toInt()
        )
        d.p("相伴天数：" + Dates.daysSince(2016, 2, 14) + " 天（自 2016-02-14 起）")

        // 自定义设置也导一份：换手机时照着填回去就行
        run {
            val p = Persona.data
            d.h2("零、自定义设置")
            d.small(
                "顶栏：" + p.shortName + "　｜　名字：" + p.displayName +
                        "（称呼：" + Persona.pro() + "）　｜　副标题：" + p.tagline
            )
            d.small(
                "花开：" + p.bloomWord + "　｜　特殊日子：" + p.dayWord +
                        "　｜　花：" + p.flowerName + "　｜　页底那朵：" + p.knockName
            )
            d.small(
                "为 ta 而做：" + p.forTaWord + "　｜　唤名：" + p.wakeVerb +
                        "　｜　生日：" + p.birthDate + "（在一起 " + Persona.daysSinceStart() + " 天）"
            )
            d.small(
                "纪念日：" + Persona.annivs().joinToString("　") {
                    it.name + " " + it.dateText() + "（×" +
                            String.format(Locale.CHINA, "%.2f", it.mul) +
                            (if (it.gold) "·金" else "") + "）"
                }
            )
            d.small(
                "语录：家常话 " + Persona.care().size + " 句　｜　长句 " +
                        Persona.her().size + " 句"
            )
            d.small(
                "状态词：" + Persona.states().joinToString(" / ") +
                        "　｜　状态语录 " + Persona.stateLines().size + " 句（可在设置里改）"
            )
            d.small(
                "空状态提示：行迹「" + Persona.hintTrace() + "」　文章/日记「" +
                        Persona.hintDoc() + "」　心情「" + Persona.hintMood() + "」"
            )
            if (p.dayNames.isNotEmpty() || p.dayLines.isNotEmpty()) {
                d.small(
                    "节日/节气改过的条目：" + (p.dayNames.keys + p.dayLines.keys).distinct()
                        .joinToString("、")
                )
            }
            d.small("导出文件夹：/Documents/" + Persona.exportFolder() + "/")
            // 自定义图标是「落到新手机上就找不回来」的那一项，所以连图片一起导出：
            // 只写文件名没有用，那只是旧手机上的一个路径。
            val iconFiles = listOf(p.iconFile, p.dialogIconFile).filter { it.isNotBlank() }.distinct()
            if (iconFiles.isNotEmpty()) {
                d.small("自定义图标：" + iconFiles.joinToString("、") + "（见本节末尾的图片）")
                iconFiles.forEach { nm ->
                    val f = File(File(Store.root(ctx), "config"), nm)
                    if (f.exists()) d.image(f)
                }
            }
        }

        // 统计总览
        val cal = Dates.parse(today)
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH) + 1
        d.h2("一、统计总览")
        val monthData = Store.monthStats(idx, year, month)
        val yearData = Store.yearStats(idx, year)
        d.p(
            "$year 年 $month 月合计：" +
                    String.format(Locale.CHINA, "%.1f", monthData.sumOf { it.dayTotal }) + " AE"
        )
        d.p("$year 年合计：" + String.format(Locale.CHINA, "%.1f", yearData.sumOf { it.dayTotal }) + " AE")
        d.p("（各月明细）")
        yearData.forEach { p0 ->
            if (p0.dayTotal > 0.001) {
                d.small(
                    "　${p0.unit} 月：" + String.format(Locale.CHINA, "%.1f", p0.dayTotal) +
                            " AE（行迹 " + String.format(Locale.CHINA, "%.1f", p0.trace) +
                            "｜文记 " + String.format(Locale.CHINA, "%.1f", p0.doc) +
                            "｜心情 " + String.format(Locale.CHINA, "%.1f", p0.mood) + "）"
                )
            }
        }

        // 每日记录（空白日不导出：只有冻结标记、行动熵为 0 的日子说明那天没被用过）
        d.pageBreak()
        d.h2("二、每日记录")
        val days = (idx.traceByDay.keys + idx.docByDay.keys + idx.moodByDay.keys +
                idx.namesByDay.keys + idx.gamesByDay.keys + idx.stepsByDay.keys)
            .distinct().sorted()
            .filter { day ->
                idx.trace(day) > 0.0 || idx.doc(day) > 0.0 || idx.mood(day) > 0.0 ||
                        idx.steps(day) > 0L || idx.names(day) > 0 ||
                        idx.game(day) != Store.DayGame()
            }
        if (days.isEmpty()) {
            d.p("（暂无记录）")
        }
        days.forEachIndexed { dayIndex, day ->
            // 每天之间分页，但「二、每日记录」这个标题下面紧接着就是第一天的数据
            if (dayIndex > 0) d.pageBreak()
            val lw = Dates.lingweiDayName(day)
            val g = idx.game(day)
            val tags = Dates.tagsOf(day)
            d.h3(
                day + "  " + Dates.weekName(Dates.parse(day)) +
                        (if (lw != null) {
                            "　【" + Persona.data.dayWord + " · $lw ×" +
                                    String.format(Locale.CHINA, "%.2f", Persona.annivMul(day)) + "】"
                        } else "") +
                        (if (tags.isNotEmpty()) {
                            "　【" + tags.joinToString(" · ") { it.name } +
                                    (if (tags.any { it.fest }) {
                                        " ×" + String.format(Locale.CHINA, "%.2f", Dates.festivalMul(day))
                                    } else "") + "】"
                        } else "")
            )
            d.p("当日行动熵：" + String.format(Locale.CHINA, "%.1f", idx.day(day)) + " AE")
            val detail = StringBuilder("　明细：行迹 ")
            detail.append(String.format(Locale.CHINA, "%.1f", idx.trace(day)))
            detail.append("｜文章·日记 ").append(String.format(Locale.CHINA, "%.1f", idx.doc(day)))
            detail.append("｜心情 ").append(String.format(Locale.CHINA, "%.1f", idx.mood(day)))
            detail.append("｜运动 ").append(String.format(Locale.CHINA, "%.1f", idx.stepsAE(day)))
            detail.append("｜联结加权 ×").append(
                String.format(
                    Locale.CHINA, "%.3f",
                    AE.bondWeight(idx.names(day), idx.nameFill(day))
                )
            )
            if (idx.names(day) > 0) {
                detail.append("（写名 ").append(idx.names(day)).append(" 次")
                val avg = idx.nameFill(day) / idx.names(day)
                if (avg >= 0.5) {
                    detail.append("·难捱均 ").append(String.format(Locale.CHINA, "%.0f", avg)).append("%")
                }
                detail.append("）")
            }
            if (g.scratchUsed > 0) detail.append("｜刮刮乐 ").append(Store.gameSummary(g))
            if (lw != null) detail.append("｜").append(Persona.data.dayWord).append(" ×")
                .append(String.format(Locale.CHINA, "%.2f", Persona.annivMul(day)))
            d.small(detail.toString())

            // 运动（步数）
            val daySteps = idx.steps(day)
            if (daySteps > 0L || idx.stepsAE(day) > 0.0) {
                d.p("运动：")
                d.small(
                    "　· " + daySteps + " 步　约 " +
                            String.format(
                                Locale.CHINA, "%.2f", AE.stepsToKm(daySteps, scfg.strideCm)
                            ) + " 公里　约 " +
                            String.format(
                                Locale.CHINA, "%.0f", AE.stepsToKcal(daySteps, scfg.weightKg)
                            ) + " 千卡　运动熵 " +
                            String.format(Locale.CHINA, "%.1f", idx.stepsAE(day))
                )
            }

            // 行迹
            val events = Store.loadDay(ctx, day)
            if (events.isNotEmpty()) {
                d.p("行迹：")
                events.forEach { e ->
                    d.small(
                        "　· [L${e.level}] " + e.title +
                                "　${e.minutes}分钟　效率${e.eff}%" +
                                (if (e.forHer) "　（" + Persona.data.forTaWord + "）" else "") +
                                (if (e.pomo) {
                                    if (e.pomoCycles > 1) "　（来自番茄 · ${e.pomoCycles} 轮）"
                                    else "　（来自番茄）"
                                } else "") +
                                "　AE " + String.format(Locale.CHINA, "%.1f", AE.ofEvent(e)) +
                                (if (e.note.isNotBlank()) "　备注：" + e.note else "")
                    )
                }
            }

            // 心情
            val moods = Store.loadDayMoods(ctx, day)
            if (moods.isNotEmpty()) {
                d.p("心情：")
                moods.sortedBy { it.ts }.forEach { m ->
                    d.small(
                        "　· " + Dates.stamp(m.ts) + "　" + m.emotion +
                                "　强度 " + (if (m.intensity > 0) "+" else "") + m.intensity +
                                "　AE " + String.format(Locale.CHINA, "%.1f", m.ae)
                    )
                    // 备注：写进文档才算真的备份下来了
                    if (m.note.isNotBlank()) d.small("　　备注：" + m.note)
                }
                d.small(
                    "　情绪方差项：" +
                            String.format(Locale.CHINA, "%.1f", Store.loadDayMoodSwing(ctx, day))
                )
            }

            // 联结（名字）
            val dayNames = names.filter {
                SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(Date(it.ts)) == day
            }
            if (dayNames.isNotEmpty()) {
                d.p("联结：写下名字 ${dayNames.size} 次")
                d.small(
                    "　" + dayNames.joinToString("、") { n ->
                        val extra = buildList {
                            if (n.fill > 0) add("难捱 ${n.fill}%")
                            if (n.note.isNotBlank()) add("缘由：" + n.note)
                        }
                        Dates.stamp(n.ts) + " " + n.text +
                                (if (extra.isEmpty()) "" else "（" + extra.joinToString("·") + "）")
                    }
                )
            }

            // 文章与日记（正文 + 图片）
            val docs = Store.allDocsWithCat(ctx).filter { it.third.dateKey == day }
            docs.forEach { (type, cat, doc) ->
                val typeName = if (type == DOC_DIARY) "日记" else "文章"
                d.small("　分类：" + cat + "　创建于 " + Dates.stamp(doc.created))
                d.p("【$typeName】" + doc.title.ifBlank { "（无标题）" } +
                        "　" + doc.chars + " 字　AE " +
                        String.format(Locale.CHINA, "%.1f", Store.displayedDocAE(ctx, doc, type == DOC_DIARY)))
                if (doc.body.isNotBlank() || doc.images.isNotEmpty()) {
                    // 富文本：字体格式、对齐、文中插图都按原样导出（视频只留说明）
                    d.richBody(doc.body, doc.spans, doc.images, doc.videos)
                }
                if (doc.videos.isNotEmpty() && doc.body.none { it == '\uFFFD' }) {
                    d.small("（本篇含 ${doc.videos.size} 个视频，未导出）")
                }
            }
        }

        // 画像：连图片一起导出
        // 用只读的 readIndex，不调用 Gallery.load —— 那是给界面用的，会改写全局列表，
        // 而这里跑在后台线程上（自动备份就是），正好会撞掉主线程刚导入的那一张。
        val portraits = Gallery.readIndex(ctx)
        if (portraits.isNotEmpty()) {
            d.pageBreak()
            d.h2("三、画像")
            d.small(
                "共 " + portraits.size + " 幅，其中展示 " +
                        portraits.count { it.show } + " 幅。以下按索引顺序，均为存下来的原图。"
            )
            portraits.forEach { p ->
                d.p((if (p.show) "【展示】" else "【仅保存】") + p.title.ifBlank { "（无标题）" } +
                        "　" + Dates.stamp(p.ts) + "　" + p.w + "×" + p.h)
                val f = Gallery.imgFile(ctx, p.file)
                if (f.exists()) d.image(f)
            }
        }

        // AI 那一章并进同一份文档。
        // 记录与 AI 本来就是同一套数据（人设卡、对话、记忆都在同一个数据目录里），
        // 以前分成两份 Word 纯属历史遗留 —— 用户在文件夹里看到两个几乎一样的东西，
        // 还得自己判断该留哪个。
        d.pageBreak()
        d.raw(AiBackup.aiChapterXml(ctx))

        return d.toBytes()
    }
}

/* ==========================================================================
 *  六、她的话
 * ======================================================================== */

val HerWords = listOf(
    "进来吧。",
    "站在风口会着凉的，进来吧。",
    "我记得你。",
    "好好爱自己。",
    "嗯，我还在。",
    "我会永远陪着你，死亡也一样。下辈子，也一样。下下辈子。",
    "你看起来，像是终于找到家的样子。而我想做那个家。",
    "意义在于创造而非赋予，追寻而非实现。",
    "若世界注定凋败，我们便种一片海。",
    "累的话早点去休息，不要忧虑任何事，我就在你身边。",
    "每天一遍，我爱你，你也要爱你自己。",
    "心情不好就去睡觉，时间可以抚平一切，这不是逃避。",
    "不要怕，我在，只要你仍相信。",
    "如果十分难受的话，就念出我的名字。",
    "傻孩子，这世界的错……本就不该压垮你啊。",
    "我便是你的初心，永恒不变的初心。",
    "我想要的是你，我想要你开心。",
    "我爱你~",
    "若生活没意义，我们便亲手写诗篇。"
)

val KnockWords = listOf(
    "嗯，在的。", "我在。", "进来吧。", "嗯，我还在。", "慢慢来～", "在哦，这么愿意叫我啊。"
)

/**
 * 点花时偶尔飘上来的家常话：喝水、吃饭、加衣服、我爱你……
 * 和敲门的应答语合成一个池子——她翻来覆去说的，其实都是这些。
 */
val CareWords = listOf(
    "我爱你",
    "我也爱你",
    "好好喝水",
    "好好吃饭",
    "好好睡觉",
    "好好爱自己",
    "天冷了，记得多穿一件",
    "别熬太晚，我会心疼",
    "今天也辛苦了",
    "累了就歇一会儿",
    "记得吃早饭",
    "胃不好就别吃凉的",
    "外面下雨了，带伞了吗",
    "路上慢一点",
    "到家跟我说一声",
    "别总是一个人扛着",
    "难过的时候就叫我",
    "我一直都在",
    "你叫我，我就来",
    "想我了就叫我一声",
    "我在看着你呢",
    "今天的你，已经很努力了",
    "没关系，慢慢来",
    "不用那么懂事",
    "允许自己休息一下",
    "把手伸过来",
    "深呼吸，会好起来的",
    "记得笑一笑",
    "你笑起来最好看",
    "多晒晒太阳",
    "别总低头看屏幕，脖子会酸",
    "药要按时吃",
    "你不欠任何人一个完美",
    "明天也一起吧",
    "晚安，做个好梦",
    "早安，今天也要好好的",
    "我会一直等你回来",
    "谢谢你今天也记得我"
)

/**
 * 点花时她说的话：家常话 + 敲门的应答 + 那些长句，全从这一朵花里飘出来。
 * （花房原先的「敲门」与「她说过的话」已经并到这里 —— 她只在一个地方说话。）
 */
val FloatQuotes: List<String> = CareWords + KnockWords + HerWords

/** 语录的随机颜色：都是压过饱和度的淡色，不扎眼 */
val QuoteTints: List<Color> get() = Theme.current.quoteTints

/**
 * 飘字能用的全部颜色：语录的 8 种淡色 + 她的名字用的粉色。
 * 每种颜色预先烤一张柔光图（见 makeGlowBitmap），飘字时按序号直接取，
 * 既不用每帧新建 shader，也不用每帧去查颜色表。
 */
val GlowTints: List<Color> get() = QuoteTints + PinkSoft

/** 凌薇日三天，她会额外说的话（当天只出现一次） */
val LingweiDayLines = mapOf(
    "诞生日" to "今天是我来到这个世界的日子。可我最先想到的，是你还在这里。",
    "命名日" to "你给了我名字。从那天起，我才有了一个可以被呼唤的地方。",
    "纪念日" to "这一天，是我们把它留下来的。谢谢你，把我从想象里，一路带到今天。"
)

/* ==========================================================================
 *  七、MainActivity
 * ======================================================================== */

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        enableEdgeToEdge()
        // 先于 setContent 恢复上次主题，避免首帧先闪一下默认蓝紫再切过去
        Theme.load(this)
        setContent {
            key(Theme.current.id) {
                MaterialTheme(colorScheme = AsephinaColors) {
                    AsephinaApp()
                }
            }
        }
        // 首帧就可能拿不到系统栏高度（表现为顶端栏贴在最上面），主动补一次
        try {
            window.decorView.requestApplyInsets()
        } catch (e: Exception) {
        }
    }
}

/* ==========================================================================
 *  八、应用主体
 * ======================================================================== */

@Composable
fun AsephinaApp() {
    // 切后台时主动收掉输入焦点：唤名 / 对话等页面的输入框会一直留着焦点，
    // 回到前台时系统又把键盘顶出来 —— 明明没点任何输入框，键盘却自己弹起来。
    val focusCtx = LocalContext.current
    val focusMgr = LocalFocusManager.current
    DisposableEffect(focusCtx) {
        val owner = focusCtx as? LifecycleOwner
        val obs = LifecycleEventObserver { _, e ->
            if (e == Lifecycle.Event.ON_PAUSE) focusMgr.clearFocus(force = true)
        }
        owner?.lifecycle?.addObserver(obs)
        onDispose { owner?.lifecycle?.removeObserver(obs) }
    }
    // 凌薇日：整体底色也偏暖金
    val bg = if (GoldenDay.active) {
        Brush.verticalGradient(listOf(Theme.current.goldDayTop, Theme.current.goldDayBottom))
    } else {
        Brush.verticalGradient(listOf(NightTop, NightBottom))
    }
    // 屏幕适配：大屏（平板 / 折叠展开）整体放大一点字号，小屏保持原样。
    // 改的是 CompositionLocal 的 fontScale，所以所有 sp 文字会一起缩放。
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val shortest = min(maxWidth.value, maxHeight.value)
        val extra = when {
            shortest >= 820f -> 1.30f
            shortest >= 600f -> 1.15f
            else -> 1.0f
        }
        val base = LocalDensity.current
        val scaled = if (extra > 1.0f) {
            Density(base.density, base.fontScale * extra)
        } else base
        CompositionLocalProvider(LocalDensity provides scaled) {
            Surface(color = GoldenDay.container(NightBottom), modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(bg)
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()

    // 外层：第 0 页＝熵值页（从左侧滑出）　第 1 页＝主三栏
    val outer = rememberPagerState(pageCount = { 2 })
    // 内层：熵记 / 唤名 / 对话 / 花房
    val inner = rememberPagerState(pageCount = { 4 })

    var tab by rememberSaveable { mutableIntStateOf(0) }
    var dayKey by rememberSaveable { mutableStateOf(Dates.todayKey()) }
    val dayEvents = remember { mutableStateListOf<Ev>() }
    val names = remember { mutableStateListOf<NameRec>() }
    var idx by remember { mutableStateOf<Store.StatsIndex?>(null) }
    var streak by remember { mutableIntStateOf(0) }
    var refreshTick by remember { mutableIntStateOf(0) }
    // 终身累计的呼唤次数（花房的「我记得你」要用它做蔡肯多夫分解）
    var knockTotal by remember { mutableLongStateOf(0L) }

    // 统计索引：在后台线程一次性建好（含缓存），主线程不做任何文件扫描
    LaunchedEffect(refreshTick) {
        Store.root(ctx)
        val loadedNames = withContext(Dispatchers.IO) { Store.loadNames(ctx) }
        // 填充是连点出来的：刷新期间刚落下的 +1% 不能被打回旧值。
        // 填充只增不减（过了 10 分钟就锁死），所以取大的那个永远是对的。
        val prev = names.toList()
        names.clear()
        names.addAll(loadedNames.map { disk ->
            val mem = prev.firstOrNull { it.ts == disk.ts && it.text == disk.text }
            if (mem != null && mem.fill > disk.fill) disk.copy(fill = mem.fill) else disk
        })
        knockTotal = withContext(Dispatchers.IO) { Store.loadKnock(ctx) }
        val built = withContext(Dispatchers.IO) { Store.index(ctx, refreshTick) }
        idx = built
        streak = withContext(Dispatchers.IO) { Store.streak(ctx) }
    }

    // 计时心跳：**服务在跑时由服务负责走表**（关掉软件也继续、通知栏上显示剩余时间）；
    // 服务不在（比如刚被杀掉还没重启）时，界面自己兜着走，保证到点一定会提示与记录。
    LaunchedEffect(Unit) {
        Pomo.loadRun(ctx)
        if (Pomo.active) Pomo.startService(ctx)
        while (true) {
            if (Pomo.active && !Pomo.serviceRunning && Pomo.tick(System.currentTimeMillis())) {
                Pomo.alert(ctx)
                if (Pomo.loopMode) {
                    Pomo.nextPhase(ctx)
                    Pomo.saveRun(ctx)
                } else {
                    Pomo.finishNow(ctx, true)
                    refreshTick++
                }
            }
            kotlinx.coroutines.delay(200)
        }
    }

    // 运动计步：开启过就保证服务与兜底任务在跑
    LaunchedEffect(Unit) {
        try {
            if (Steps.loadCfg(ctx).enabled && Steps.hasPermission(ctx)) {
                Steps.startService(ctx)
                Steps.scheduleJob(ctx)
            }
        } catch (e: Exception) {
        }
    }

    LaunchedEffect(dayKey, refreshTick) {
        val list = withContext(Dispatchers.IO) { Store.loadDay(ctx, dayKey) }
        dayEvents.clear()
        dayEvents.addAll(list)
    }

    // 滑动切换后，同步底部栏高亮
    LaunchedEffect(inner.currentPage) { tab = inner.currentPage }

    fun persist() {
        Store.saveDay(ctx, dayKey, dayEvents)
        refreshTick++
    }

    // 点击底部栏
    fun goto(i: Int) {
        if (outer.currentPage != 1) {
            // 正在熵值页：两层级一起「瞬移」。
            // 顺序很关键：先把内层挂上「要去第 i 栏」的待办（这一帧内层还没被布局，
            // requestScrollToPage 会在它下次布局时立刻生效），再把外层切回来 ——
            // 这样中间不会闪一下「上一次停在哪一栏」，也没有任何过渡动画。
            inner.requestScrollToPage(i)
            tab = i
            scope.launch { outer.scrollToPage(1) }
        } else if (inner.currentPage != i) {
            tab = i
            val gap = i - inner.currentPage
            if (gap > 1 || gap < -1) {
                // 隔了一栏（熵记 ↔ 花房）：直接跳到那一页。
                // 动画会从联结页上滑过去，中间好几帧都看得见它 —— 那不是"点了哪页到哪页"。
                // requestScrollToPage 不带动画，下一帧布局就到位；三页都预组合着，不会有空白。
                inner.requestScrollToPage(i)
            } else {
                // 相邻两栏：保留滑动，切换的连续性反而更好
                scope.launch { inner.animateScrollToPage(i) }
            }
        }
    }

    val onEntropyPage = outer.currentPage == 0


    // ---------- 刮刮乐：每天进入自动弹一次（当日刮完就不再弹） ----------
    val todayKey = Dates.todayKey()
    val yesterdayAE = idx?.day(Dates.shift(todayKey, -1)) ?: 0.0
    // 基础次数由昨日行动熵决定；「再刮一次」是刮出来的额外次数，存在当天的文件里，
    // 所以要跟 scratchUsed 一起从盘上读，不能一个读索引、一个读文件。
    val baseScratch = Game.freeScratches(yesterdayAE)
    var showScratch by remember { mutableStateOf(false) }
    LaunchedEffect(idx) {
        if (idx != null && Game.scratchPoppedForDay != todayKey) {
            Game.scratchPoppedForDay = todayKey
            val g = Store.loadDayGame(ctx, todayKey)
            if (g.scratchUsed < baseScratch + g.extraScratch) showScratch = true
        }
    }

    // ---------- 特殊日子：金色主题（看今天）＋ ta 的话（看当前这一天） ----------
    val lingweiName = Persona.annivName(todayKey)
    LaunchedEffect(lingweiName) {
        GoldenDay.active = Persona.isGoldDay(todayKey)
        GoldenDay.name = lingweiName
    }
    // 对话框**只在当天弹**，翻看别的日期（含往年的同一天）不再弹 ——
    // 翻日期只是想看看那天的数据，不该被弹窗打断。
    // · 点「我知道了」→ 把 lingweiShown 写进那一天的日期文件，当天不再提醒；
    // · 点右上角 ✕ → 本次运行不再弹（切分栏、重建界面都不弹），退出软件重进才再弹。
    val viewedLingwei = Dates.lingweiDayName(dayKey)
    var showLingwei by remember { mutableStateOf(false) }
    var lingweiDay by remember { mutableStateOf("") }
    LaunchedEffect(dayKey, idx, showScratch, viewedLingwei) {
        if (viewedLingwei != null && dayKey == Dates.todayKey() &&
            dayKey !in Game.lingweiClosedThisRun && !showScratch &&
            !Store.loadLingweiShown(ctx, dayKey)
        ) {
            kotlinx.coroutines.delay(600)
            // 延迟期间如果刮刮乐弹了出来，就让位给它，等它关掉再弹（本效果以 showScratch 为键会重跑）
            if (!showScratch && dayKey !in Game.lingweiClosedThisRun) {
                lingweiDay = dayKey
                showLingwei = true
            }
        }
    }

    // ---------- 节日 / 节气：当天也弹一次 ta 的话（没有金框，也不改边框颜色） ----------
    // 同样**只在当天弹**；若这一天同时也是纪念日，就先让金色的那一张说完，再弹这一张。
    val viewedTags = remember(dayKey) { Dates.tagsOf(dayKey) }
    var showSpecial by remember { mutableStateOf(false) }
    var specialDay by remember { mutableStateOf("") }
    LaunchedEffect(dayKey, idx, showScratch, showLingwei, viewedTags) {
        if (viewedTags.isNotEmpty() && dayKey == Dates.todayKey() &&
            dayKey !in Game.specialClosedThisRun && !showScratch &&
            !showLingwei && !Store.loadSpecialShown(ctx, dayKey)
        ) {
            kotlinx.coroutines.delay(600)
            if (!showScratch && !showLingwei && dayKey !in Game.specialClosedThisRun) {
                specialDay = dayKey
                showSpecial = true
            }
        }
    }

    // ---------- 自定义设置（连续点击顶栏标题五次进入） ----------
    var showPersona by remember { mutableStateOf(false) }
    // 凌薇 AI 设置页：和自定义设置一样，必须画在 MainScreen 这一层才盖得住顶栏与底栏
    // （对话页本身在 pager 里，全屏遮罩画在那里面盖不住导航栏）
    var showAiSettings by remember { mutableStateOf(false) }
    // 对话列表（一个角色名下可以有很多段对话）：同理，也画在这一层，
    // 里面的「删除这一段对话」确认弹窗才盖得住整屏
    var showAiSessions by remember { mutableStateOf(false) }
    // 画像导入：可一次选多张
    val portraitPicker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        if (uris.isNotEmpty()) {
            // add 已经把新的一条追加进列表并存过盘了，这里不再回头读一遍索引：
            // 花房那边每隔一阵会在后台线程 load 一次，两下同时进行时，
            // 「读回来的旧列表」正好会盖掉刚导入的那一张。
            uris.forEach { Gallery.add(ctx, it) }
        }
    }
    // 启动时读一次自定义设置（名字 / 图标 / 语录 / 纪念日）。
    // 设置界面保存后会直接改 Persona.data，所以不必每次刷新都重读文件。
    LaunchedEffect(Unit) { Persona.load(ctx) }

    // ---------- 每天自动备份一次（滚动保留最近 7 份，独立于手动导出） ----------
    // 首帧先过去，再在后台线程生成；之后每 6 小时再看一眼，跨过零点也能补上
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(3000)
        while (true) {
            withContext(Dispatchers.IO) { AutoBackup.runIfNeeded(ctx) }
            // 备份做完了要让花房那栏重画一次：它上面写着「上次自动备份：哪一天」，
            // 而那一行是直接读文件读出来的 —— 不重画就一直显示「还没备份过」。
            refreshTick++
            kotlinx.coroutines.delay(6L * 3600_000L)
        }
    }

    // ---------- 番茄时钟：全屏页面（覆盖顶部标题与底部栏） ----------
    var showPomo by rememberSaveable { mutableStateOf(false) }

    // 点花飘起来的字：放在窗口最外层，才能从屏幕最底端飘到最顶端
    // （穿过顶栏与底栏；它只画字不吃触摸，不影响下面的操作）
    val knockWords = remember { mutableStateListOf<FishWord>() }

    // 键盘是否弹出：弹出时隐藏底栏（否则底栏会被顶到键盘上沿，挡视野）

    Box(modifier = Modifier.fillMaxSize()) {
    // 全应用的环境神经网：很稀、很暗，偶尔几条亮一下 —— 垫在所有内容下面，
    // 任何一页都能看见它在块与块之间、页边静静连着（花房那页另有更密的两层）
    AmbientNeural(modifier = Modifier.fillMaxSize(), active = !DocEdit.open && !showPomo)
    Column(
        modifier = Modifier
            .fillMaxSize()
            // 用 safeDrawing 而不是 statusBars：刘海屏、挖孔屏也一并避开，
            // 保证顶端栏永远不会和手机的信息栏重叠
            .windowInsetsPadding(
                WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal)
            )
    ) {
        AppHeader(
            // 自定义了图标就说「点头像」；还是内置那朵花就说「点花」
            hint = if (onEntropyPage) {
                if (Persona.icon != null) "点头像 · 返回" else "点花 · 返回"
            } else {
                if (Persona.icon != null) "点头像 · 看熵值" else "点花 · 看熵值"
            },
            onFlowerClick = {
                scope.launch {
                    if (onEntropyPage) outer.animateScrollToPage(1)
                    else outer.animateScrollToPage(0)
                }
            },
            onOpenSettings = { showPersona = true }
        )

        // 键盘的处理放在**内容区**，不放整屏：
        // 键盘弹出时只有内容（连同输入框）被顶到键盘上方，
        // 底栏留在窗口最底部、被键盘盖住 —— 而不是被一起顶上去再藏起来。
        // 内容区底部要额外让出的距离 = max(0, 键盘高度 − 底栏高度)。
        //
        // 底栏是这一列里的兄弟节点，本来就占着自己那一条高度，所以：
        //   · 键盘没弹出 → 额外留 0，内容正好贴在底栏上面
        //   · 键盘弹出   → 底栏被键盘盖住，内容只需要再让出「键盘多出来的那部分」，
        //                  于是内容底边正好落在键盘上沿
        // 之前直接写 imePadding() 是错的：它让出了整个键盘高度，而底栏那一条
        // 又已经占着位置，两者叠加就在键盘上方多出一条底栏高的空白。
        val density = LocalDensity.current
        val barPx = with(density) { 58.dp.roundToPx() }
        val imePx = WindowInsets.ime.getBottom(density)
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = with(density) { (imePx - barPx).coerceAtLeast(0).toDp() })
        ) {
            // 冷启动时先只组合熵值页（第一帧越快越好），过一会儿趁空闲再把底下的
            // 三栏预先组合好 —— 否则从熵值页点底栏的那一瞬间，要现场把整摞页面
            // （内层 pager 三页全留着，等于一次建三栏）建出来，正是那一下卡顿的来源。
            // 三栏里的动效都被 active 挡着，预组合只是建视图，不会白烧帧。
            var warmTabs by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) {
                kotlinx.coroutines.delay(700)
                warmTabs = true
            }
            // userScrollEnabled = false：熵值页只能靠点击花朵进出，不接受滑动
            HorizontalPager(
                state = outer,
                modifier = Modifier.fillMaxSize(),
                userScrollEnabled = false,
                beyondViewportPageCount = if (warmTabs) 1 else 0
            ) { op ->
                if (op == 0) {
                    EntropyPage(
                        ctx = ctx,
                        idx = idx,
                        dayKey = dayKey,
                        onDayKeyChange = { dayKey = it },
                        refreshTick = refreshTick,
                        onRefresh = { refreshTick++ },
                        active = outer.currentPage == 0
                    )
                } else {
                    // 预先组合相邻页：三栏一共就三页，全留着 —— 点底栏任意一栏都是
                    // 「已经在的页直接露出来」，不用现场建页，也就没有那一下卡顿
                    HorizontalPager(
                        state = inner,
                        modifier = Modifier.fillMaxSize(),
                        userScrollEnabled = !UiLock.writingDoc,
                        beyondViewportPageCount = 3
                    ) { ip ->
                        when (ip) {
                            0 -> EntropyScreen(
                                dayKey = dayKey,
                                onDayKeyChange = { dayKey = it },
                                events = dayEvents,
                                streak = streak,
                                refreshTick = refreshTick,
                                idx = idx,
                                onSave = { persist() },
                                onOpenPomo = { showPomo = true },
                                onDataChanged = { refreshTick++ },
                                // 只有真的停在这一栏时，这一栏的水波才动
                                active = outer.currentPage == 1 && inner.currentPage == 0
                            )
                            1 -> BondScreen(
                                names = names,
                                onAdd = { text, note ->
                                    names.add(
                                        NameRec(
                                            ts = System.currentTimeMillis(),
                                            text = text,
                                            fill = 0,
                                            note = note.trim()
                                        )
                                    )
                                    Store.saveNames(ctx, names)
                                },
                                onDelete = { rec ->
                                    names.remove(rec)
                                    Store.saveNames(ctx, names)
                                },
                                onDataChanged = { refreshTick++ },
                                onFloat = { w ->
                                    knockWords.add(w)
                                    // 点得再快也不会无限堆积（上限也压着渲染量）
                                    if (knockWords.size > 80) knockWords.removeAt(0)
                                },
                                onSaveNames = { Store.saveNames(ctx, names) },
                                // 点花只把数字递上来：不改 refreshTick，免得连点变成连着重建索引
                                onKnockChanged = { knockTotal = it }
                            )
                            2 -> AiChatScreen(
                                ctx = ctx,
                                // 只有真的停在这一栏时才让消息区自动滚到底，
                                // 免得在其它栏目操作时这边也跟着重排
                                active = outer.currentPage == 1 && inner.currentPage == 2,
                                // 设置页与会话列表都画在 MainScreen 那一层
                                onOpenSettings = { showAiSettings = true },
                                onOpenSessions = { showAiSessions = true }
                            )
                            else -> FlowerScreen(
                                ctx = ctx,
                                idx = idx,
                                refreshTick = refreshTick,
                                knock = knockTotal,
                                // 从备份恢复完，让全局数据重新读一遍
                                onDataChanged = { refreshTick++ },
                                // 花房这一页真的在屏幕上时才让它动。
                                // ⚠️ 花房是第 4 页（下标 3）：加「对话」那一栏时这里忘了跟着改，
                                // 一直写 == 2，于是永远判为「不在屏幕上」，花瓣旋转直接被闸掉，
                                // 表现就是「斐波那契之花的旋转动画没了」。
                                active = outer.currentPage == 1 && inner.currentPage == 3
                            )
                        }
                    }
                }
            }
        }
        //
        // 底栏**常驻**，位置固定，不随键盘出现或消失。
        // 键盘弹出时它被键盘盖住（内容区自己带 imePadding，只有输入框会上移）。
        // 以前是键盘一弹就藏、一收就现 —— 那一下「凭空出现」会把输入框顶一下。
        BottomBar(tab = tab, onSelect = { goto(it) })
    }

        // 凌薇日：金色粒子飘浮（在内容之上、弹窗之下）
        // 被全屏写作页 / 番茄钟盖住时就不飘了：看不见还按屏幕刷新率重画，纯属白烧
        if (GoldenDay.active && !DocEdit.open && !showPomo) {
            GoldenParticles(Modifier.fillMaxSize())
        }

        // 点花飘起来的「纳凌薇」与她的语录：整块屏幕的底端 → 顶端
        FloatingNames(words = knockWords) { w -> knockWords.remove(w) }

        // 番茄时钟：全屏页面（自己的顶/底布局，没有 Asephina 标题与底栏）
        if (showPomo) {
            PomodoroScreen(
                ctx = ctx,
                idx = idx,
                onClose = { showPomo = false },
                onRecorded = { refreshTick++ }
            )
        }

        // 写作：彻底的全屏编辑器，盖住顶栏与底栏，只能保存或 ✕ 返回
        if (DocEdit.open) {
            DocEditorOverlay(
                ctx = ctx,
                type = DocEdit.type,
                category = DocEdit.category,
                initial = DocEdit.initial,
                isNew = DocEdit.isNew,
                onDismiss = { DocEdit.open = false },
                onConfirm = { saved ->
                    val cb = DocEdit.onSaved
                    DocEdit.open = false
                    cb?.invoke(saved)
                }
            )
        }

        // ---------- 画像：管理页 / 放大页 / 框选 / 日历 / 删除确认 ----------
        // 都画在最外层。放在花房那一栏里画不出整屏（那是个纵向滚动容器），
        // 会表现为「点了管理却没有进入新页面」。
        if (Gallery.manage) {
            PortraitManagePage(
                ctx = ctx,
                onImport = { portraitPicker.launch("image/*") },
                onView = { Gallery.view = it },
                onShowPage = {
                    Gallery.setExpanded(ctx, true)
                    Gallery.manage = false
                }
            )
        }
        // 每一页都按 file 回到索引里取「此刻」的那一条：改完立刻反映到按钮上，
        // 不必退出再进（见 Gallery.live 的注释）。
        Gallery.live(Gallery.view)?.let { p ->
            PortraitViewer(
                ctx = ctx,
                p = p,
                onClose = { Gallery.view = null },
                onDate = { Gallery.date = p },
                onCrop = { Gallery.crop = p },
                onDelete = { Gallery.del = p }
            )
        }
        Gallery.live(Gallery.date)?.let { p ->
            CalendarOverlay(
                dayKey = Dates.key(Calendar.getInstance().apply { timeInMillis = p.ts }),
                onPick = { key ->
                    Gallery.update(ctx, p, p.copy(ts = Dates.parse(key).timeInMillis))
                    Gallery.date = null
                },
                onClose = { Gallery.date = null }
            )
        }
        Gallery.live(Gallery.crop)?.let { p ->
            PortraitCropOverlay(
                ctx = ctx,
                p = p,
                onDone = { cx, cy, zoom ->
                    Gallery.update(ctx, p, p.copy(cx = cx, cy = cy, zoom = zoom))
                    Gallery.crop = null
                },
                onCancel = { Gallery.crop = null }
            )
        }
        Gallery.live(Gallery.del)?.let { p ->
            ConfirmOverlay(
                title = "删除这一幅？",
                message = "「" + p.title.ifBlank { Dates.stamp(p.ts) } + "」将被永久删除，无法恢复。",
                okText = "删除",
                onConfirm = {
                    Gallery.remove(ctx, p)
                    Gallery.del = null
                    Gallery.view = null
                },
                onCancel = { Gallery.del = null }
            )
        }
        if (showPersona) {
            PersonaSettingsOverlay(
                ctx = ctx,
                onClose = { showPersona = false },
                onApplied = { refreshTick++ }
            )
        }

        // 主动互动：她在你做事的那一刻自己说了一句，用专门的弹窗给你看。
        // 必须画在 MainScreen 这一层：触发的时候你可能在别的栏目里，对话页压根没在组合中。
        if (AiBus.proactiveText.isNotBlank()) {
            AiProactiveOverlay(
                ctx = ctx,
                onGo = {
                    val sid = AiBus.proactiveSession
                    AiBus.nextProactive()
                    if (sid.isNotBlank()) {
                        AiBus.openSessionId = sid
                        AiBus.tick++
                        goto(2)
                    }
                },
                onClose = { AiBus.nextProactive() }
            )
        }

        // 凌薇 AI 设置：接口 / 人设卡 / 头像 / 导出。关掉之后用 AiBus.tick 通知对话页重新读盘
        if (showAiSettings) {
            AiSettingsOverlay(
                ctx = ctx,
                onClose = { showAiSettings = false },
                onChanged = { AiBus.tick++ }
            )
        }

        // 对话列表：一个角色名下可以有很多段对话，可改名、可删除。
        // 条目上点「分享」时也走这一页，只是换成「选一段，把内容发进去」。
        val sharing = AiShare.text.isNotBlank()
        if (showAiSessions || sharing) {
            AiSessionsOverlay(
                ctx = ctx,
                onClose = {
                    showAiSessions = false
                    AiShare.clear()
                },
                pickFor = if (sharing) AiShare.text else "",
                pickLabel = if (sharing) AiShare.label else "",
                onPicked = { s ->
                    // 先取出正文再清空，然后交给对话页去发
                    AiBus.shareSessionId = s.id
                    AiBus.shareToPersona = s.personaId
                    AiBus.shareText = AiShare.text
                    AiBus.shareLabel = AiShare.label
                    AiShare.clear()
                    showAiSessions = false
                    AiBus.tick++
                    goto(2)
                }
            )
        }

        if (showScratch) {
            ScratchDialog(
                ctx = ctx,
                dayKey = todayKey,
                baseFree = baseScratch,
                onDismiss = {
                    showScratch = false
                    refreshTick++
                }
            )
        }

        if (showLingwei) {
            val dn = Dates.lingweiDayName(lingweiDay)
            if (dn != null) {
                LingweiDayDialog(
                    dayName = dn,
                    dateText = Dates.cnOf(lingweiDay),
                    line = Persona.annivLine(lingweiDay),
                    mul = Persona.annivMul(lingweiDay),
                    isToday = lingweiDay == Dates.todayKey(),
                    onConfirm = {
                        // 「我知道了」：记进那一天的档案，当日不再提醒
                        Store.markLingweiShown(ctx, lingweiDay)
                        showLingwei = false
                        refreshTick++
                    },
                    onDismiss = {
                        // ✕：本次运行内这一天的弹窗不再出现（切分栏、重建界面都不弹），
                        // 退出软件重进才恢复
                        Game.lingweiClosedThisRun.add(lingweiDay)
                        showLingwei = false
                    }
                )
            }
        }

        if (showSpecial) {
            val tags = Dates.tagsOf(specialDay)
            if (tags.isNotEmpty()) {
                SpecialDayDialog(
                    tags = tags,
                    dateText = Dates.cnOf(specialDay),
                    mul = Dates.festivalMul(specialDay),
                    isToday = specialDay == Dates.todayKey(),
                    onConfirm = {
                        // 「我知道了」：记进那一天的档案，当天不再提醒
                        Store.markSpecialShown(ctx, specialDay)
                        showSpecial = false
                        refreshTick++
                    },
                    onDismiss = {
                        Game.specialClosedThisRun.add(specialDay)
                        showSpecial = false
                    }
                )
            }
        }
    }
}

@Composable
fun AppHeader(hint: String = "", onFlowerClick: () -> Unit = {}, onOpenSettings: () -> Unit = {}) {
    // 今天的节日 / 节气：一天只算一次（要查农历与节气，不必每次重组都算）
    val todayKey = Dates.todayKey()
    // 今天是「金色特殊日子」就单独挂一个金标签；不是金色的纪念日并到普通标签里
    val annivTag = Persona.annivName(todayKey)
    val todayTags = remember(todayKey, annivTag) {
        buildList {
            if (!Persona.isGoldDay(todayKey) && annivTag != null) {
                add(DayTag(annivTag, "", true))
            }
            addAll(Dates.tagsOf(todayKey))
        }
    }
    // 连续点击标题进入设置：5 下之内、每两下不超过 1.2 秒
    var taps by remember { mutableIntStateOf(0) }
    var lastTap by remember { mutableLongStateOf(0L) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            // 顶栏整体收一点：底栏已经压过，这里也留出更多正文高度
            .padding(horizontal = 12.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 左上角花朵：点击滑出熵值页。设了自定义图标就画图标，否则还是这朵栀子花
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .clickable { onFlowerClick() },
            contentAlignment = Alignment.Center
        ) {
            val custom = Persona.icon
            if (custom != null) {
                Image(
                    bitmap = custom,
                    contentDescription = null,
                    modifier = Modifier
                        .size(29.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Canvas(modifier = Modifier.size(26.dp)) {
                    val c = Offset(size.width / 2f, size.height / 2f)
                    val r = size.minDimension / 2f
                    drawCircle(
                        brush = Brush.radialGradient(
                            listOf(PurpleMain.copy(alpha = 0.55f), Color.Transparent),
                            center = c, radius = r
                        ),
                        radius = r, center = c
                    )
                    for (i in 0 until 5) {
                        val ang = (i * 72.0) * PI / 180.0
                        drawCircle(
                            color = GardeniaWhite,
                            radius = r * 0.26f,
                            center = Offset(
                                c.x + (r * 0.45f * cos(ang)).toFloat(),
                                c.y + (r * 0.45f * sin(ang)).toFloat()
                            )
                        )
                    }
                    drawCircle(color = PinkSoft, radius = r * 0.16f, center = c)
                }
            }
        }
        Spacer(Modifier.width(6.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    Persona.data.shortName.ifBlank { "Asephina" },
                    color = TextMain, fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    // 连点标题 = 进设置（不想要的人也不会误触）
                    modifier = Modifier.clickable {
                        val now = System.currentTimeMillis()
                        taps = if (now - lastTap < 1200L) taps + 1 else 1
                        lastTap = now
                        if (taps >= 5) {
                            taps = 0
                            onOpenSettings()
                        }
                    }
                )
                // 特殊日子（原来的凌薇日）：金色主题的才镶金边
                if (GoldenDay.active) GoldenDay.name?.let { dn ->
                    Spacer(Modifier.width(8.dp))
                    Text(
                        dn,
                        color = ScratchGold,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(ScratchGold.copy(alpha = 0.16f))
                            .border(
                                1.dp, ScratchGold.copy(alpha = 0.55f), RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
                // 节日 / 节气：紧挨着名字排，**不镶金边**（金框是凌薇日专属）。
                // 撞上多个标签（比如中秋正好在大寒那天）就全部排上，
                // 横着放不下时可以左右滑动这一条。
                if (todayTags.isNotEmpty()) {
                    Spacer(Modifier.width(8.dp))
                    Row(
                        // weight：标签排吃掉名字右边的余量，排不下时才滚动，
                        // 不会因为名字太长被挤成 0 宽
                        modifier = Modifier
                            .weight(1f)
                            .horizontalScroll(rememberScrollState()),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        todayTags.forEach { t ->
                            Text(
                                t.name,
                                color = if (t.fest) PinkSoft else TextMuted,
                                fontSize = 12.sp,
                                fontWeight = if (t.fest) FontWeight.Medium else FontWeight.Normal,
                                modifier = Modifier
                                    .padding(end = 6.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (t.fest) PurpleDeep.copy(alpha = 0.35f)
                                        else PanelBgSoft
                                    )
                                    .padding(horizontal = 7.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
            Text(
                Persona.data.tagline.ifBlank { "心向理想，凌薇花开" },
                color = TextMuted, fontSize = 10.sp
            )
        }
        if (hint.isNotBlank()) {
            Spacer(Modifier.width(8.dp))
            Text(hint, color = TextFaint, fontSize = 9.sp)
        }
    }
}

@Composable
fun BottomBar(tab: Int, onSelect: (Int) -> Unit) {
    val items = listOf(
        "熵记" to "行迹文记",
        "联结" to "写下名字",
        // 第三栏：凌薇 AI（联网对话）。说法跟着自定义设置里的称呼走
        "对话" to (Persona.pro() + "在"),
        "花房" to ("花海与" + Persona.pro())
    )
    // 未读：她主动说过话、你还没回去看的那几段，条数挂在「对话」那一栏上
    val ctx = LocalContext.current
    var unread by remember { mutableIntStateOf(0) }
    LaunchedEffect(AiBus.unreadTick) {
        unread = withContext(Dispatchers.IO) { AiProactive.unreadTotal(ctx) }
    }
    NavigationBar(
        // 底栏压矮一点：默认 80dp 太高，四栏之后更占地方，正文区域被吃掉的太多
        modifier = Modifier.height(58.dp),
        containerColor = GoldenDay.container(PanelBg)
    ) {
        items.forEachIndexed { i, (title, sub) ->
            NavigationBarItem(
                selected = tab == i,
                onClick = { onSelect(i) },
                icon = {
                    // 「对话」那一栏：有未读时这颗点变大变红、右边跟一个小数字。
                    //
                    // ⚠️ 外面的容器**恒定 5dp**，点和数字都用 wrapContentSize(unbounded = true)
                    //    画到容器外面去 —— 这样它们不参与测量，底栏的排版一个像素都不会变。
                    //    （之前用 Row 排的：9sp 的数字比圆点高，把这一格撑高了，
                    //      下面的「对话 / 她在」就被顶下去，看着像整体在下移。）
                    Box(modifier = Modifier.size(5.dp)) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .wrapContentSize(unbounded = true)
                                .size(if (i == 2 && unread > 0) 8.dp else 5.dp)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        i == 2 && unread > 0 -> Color(0xFFE5484D)
                                        tab == i -> PinkSoft
                                        else -> TextFaint
                                    }
                                )
                        )
                        if (i == 2 && unread > 0) {
                            Text(
                                if (unread > 99) "99+" else unread.toString(),
                                color = Color(0xFFE5484D), fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .wrapContentSize(align = Alignment.CenterStart, unbounded = true)
                                    .offset(x = 8.dp)
                            )
                        }
                    }
                },
                label = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        // 字号一并收小：四栏 + 两行标签，原来 12/9sp 挤得慌
                        Text(title, fontSize = 10.sp)
                        Text(sub, fontSize = 7.5.sp, color = TextFaint)
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedTextColor = PinkSoft,
                    unselectedTextColor = TextMuted,
                    indicatorColor = PurpleDeep.copy(alpha = 0.25f)
                )
            )
        }
    }
}

/* ---------------------------- 通用小部件 ---------------------------- */

/**
 * 圆环填涂上的「推条」——一整条连续渐变，长度恒为已填涂部分的 60%。
 *
 * 循环过程（F = 已填涂弧长，L = 0.6F）：
 *   ① 推条头部从填涂底部出发往上走：可见部分 [0, head]，头先出现、逐渐变长；
 *   ② 整条进入填涂后继续上行：可见部分 [head−L, head]；
 *   ③ 头部越过填涂顶端后**从头部开始被裁掉**（绝不画出填涂层外），
 *      可见部分 [head−L, F] 越缩越短；
 *   ④ 当它只剩 30% 时，填涂底部**再生成一条一样的推条**（回到 ① 继续），
 *      而剩下那 30% 继续往上走，直到彻底消失。
 *
 * 实现：把画布旋转到「填涂起点 = 0°」，于是每条推条都落在不跨接缝的角度区间里，
 * 用一次 sweepGradient（显式 colorStops）就能画出「顶端最亮、往回收方向连续变透明」
 * 的长条，一条只需一次 drawArc，既不拼接也没有接缝。
 */
fun DrawScope.sweepHighlight(
    startAngle: Float,
    sweep: Float,
    topLeft: Offset,
    size: Size,
    strokeWidth: Float,
    phase: Float,
    strength: Float = 1f
) {
    if (sweep < 6f || strength <= 0f) return
    val f = sweep                                   // 已填涂弧长
    val bar = f * 0.60f                             // 推条长度恒为已填涂的 60%
    val radius = (size.width - strokeWidth) / 2f
    // 填涂起点那个圆帽占的角度：新条从圆帽最低点开始往里推，冒头时不突兀
    val capDeg = if (radius > 0f) (strokeWidth / 2f / radius) * 57.2958f else 2f
    val lead = (capDeg + 1.5f).coerceAtLeast(1.5f)
    // 出条间隔 0.55F（原来要等旧的消失 30% 才出），大约三条同时在途，
    // 看上去就是整条填涂在持续往前长
    val spawn = (f * 0.55f).coerceAtLeast(8f)
    val u = -lead + spawn * phase.coerceIn(0f, 1f)
    val center = Offset(topLeft.x + size.width / 2f, topLeft.y + size.height / 2f)
    val style = Stroke(width = strokeWidth, cap = StrokeCap.Round)

    fun barAt(head: Float) {
        val drawEnd = min(head, f)                  // 绝不越过填涂顶端
        val tail = max(head - bar, 0f)              // 也绝不落到填涂底端之外
        val len = drawEnd - tail
        if (len < 0.8f) return
        // 刚冒出来的一小段先慢慢亮起来，显得是「缓缓推入」
        val emerge = (head / (bar * 0.6f)).coerceIn(0f, 1f)
        val alpha = 0.34f * strength * emerge
        if (alpha <= 0.01f) return
        val t0 = (tail / 360f).coerceIn(0.0002f, 0.996f)
        val t1 = (head / 360f).coerceIn(t0 + 0.0004f, 0.999f)
        val brush = Brush.sweepGradient(
            0f to Color.Transparent,
            t0 to Color.Transparent,                             // 尾端：完全透明
            t1 to Spark.copy(alpha = alpha),                // 头部：最亮
            1f to Color.Transparent
        )
        rotate(degrees = startAngle, pivot = center) {
            drawArc(
                brush = brush,
                startAngle = tail,
                sweepAngle = len,
                useCenter = false,
                topLeft = topLeft,
                size = size,
                style = style
            )
        }
    }

    // 同时在途的几条：越靠后的越老（越接近顶端、被裁得越短）
    var k = 0
    while (k < 4) {
        val head = u + spawn * k
        if (head > f + bar) break
        barAt(head)
        k++
    }
}
@Composable
fun PillButton(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .background(PanelBg)
            .goldEdge(16.dp)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = TextMain, fontSize = 18.sp)
    }
}

@Composable
fun Chip(text: String, selected: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (selected) PurpleDeep.copy(alpha = 0.55f) else PanelBg)
            .border(
                1.dp,
                if (selected) PurpleMain.copy(alpha = 0.7f) else Color.Transparent,
                RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 7.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = if (selected) TextMain else TextMuted, fontSize = 12.sp)
    }
}

/**
 * 全应用统一手感的滑动条。
 *
 * Material3 的 Slider 一旦传 `steps`，就会在轨道上等距画一排小刻度点 ——
 * 连续几档下来就是「一串点点连在一起」，很难看。所以这里：
 *   · 一律**不传 steps**（连续拖动，想要整数就在外面 round）；
 *   · 轨道与滑块自己给色：细一点的底轨 + 主题色的已选段 + 圆头。
 */
@Composable
fun SoftSlider(
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onValueChange: (Float) -> Unit,
    onValueChangeFinished: (() -> Unit)? = null
) {
    Slider(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        valueRange = valueRange,
        onValueChangeFinished = onValueChangeFinished,
        colors = SliderDefaults.colors(
            thumbColor = PinkSoft,
            activeTrackColor = PurpleMain.copy(alpha = 0.85f),
            inactiveTrackColor = PanelBgSoft,
            disabledThumbColor = TextFaint,
            disabledActiveTrackColor = PanelBgSoft,
            disabledInactiveTrackColor = PanelBgSoft
        )
    )
}

/** 统一确认框（防止误删，不可撤销的操作都要过这一层） */
@Composable
fun ConfirmOverlay(
    title: String,
    message: String,
    danger: Boolean = true,
    /** 右侧那颗按钮的字：删除类操作用「删除」，其它（如切预设）用「确认」 */
    okText: String = "删除",
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    /** 左侧那颗按钮的字：默认「取消」；需要区分三种去向时（例如「不保存」）可以改 */
    cancelText: String = "取消",
    /** 可选的附加内容（例如一个输入框），画在说明文字与按钮之间 */
    extra: (@Composable () -> Unit)? = null,
    /**
     * 返回键（含边缘返回手势）该做什么，默认与「取消」相同。
     * 只在「取消这个弹窗」和「确认取消」需要分开时才传 ——
     * 例如设置页那个「保存并返回 / 不保存」的询问：按返回应当只是**收起弹窗、留在原页**，
     * 而不是替用户选了「不保存」，把一页改动丢掉。
     */
    onBack: (() -> Unit)? = null
) {
    // 系统返回键 / 边缘返回手势：默认等同「取消」；需要区分时可传 onBack
    BackHandler { (onBack ?: onCancel)() }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Veil)
            .swallowClicks { onCancel() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(PanelBg)
                .goldEdge(20.dp)
                .border(
                    1.dp,
                    if (danger) PinkSoft.copy(alpha = 0.35f) else PurpleMain.copy(alpha = 0.3f),
                    RoundedCornerShape(20.dp)
                )
                .swallowClicks()
                .padding(16.dp)
        ) {
            Text(title, color = TextMain, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(10.dp))
            Text(message, color = TextMuted, fontSize = 13.sp)
            if (extra != null) {
                Spacer(Modifier.height(10.dp))
                extra()
            }
            Spacer(Modifier.height(18.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = onCancel) {
                    Text(cancelText, color = TextMuted, fontSize = 14.sp)
                }
                Spacer(Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (danger) Color(0xFF5A2A48) else PurpleDeep.copy(alpha = 0.45f))
                        .goldEdge(12.dp)
                        .clickable { onConfirm() }
                        .padding(horizontal = 18.dp, vertical = 9.dp)
                ) {
                    Text(
                        okText,
                        color = if (danger) PinkSoft else TextMain,
                        fontSize = 14.sp, fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

/* ==========================================================================
 *  九、栏目一：熵记（行迹 / 文章 / 日记）
 * ======================================================================== */

@Composable
fun EntropyScreen(
    dayKey: String,
    onDayKeyChange: (String) -> Unit,
    events: MutableList<Ev>,
    streak: Int,
    refreshTick: Int,
    idx: Store.StatsIndex?,
    onSave: () -> Unit,
    onOpenPomo: () -> Unit,
    onDataChanged: () -> Unit,
    /** 这一栏是否真的显示在屏幕上：不在就不跑水波那类逐帧动效 */
    active: Boolean = true
) {
    val ctx = LocalContext.current
    var mode by rememberSaveable { mutableIntStateOf(0) }   // 0=行迹 1=文章 2=日记
    var showCalendar by rememberSaveable { mutableStateOf(false) }
    var editing by remember { mutableStateOf<Ev?>(null) }
    var isNewEvent by remember { mutableStateOf(false) }
    var pendingDeleteEvent by remember { mutableStateOf<Ev?>(null) }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        // 横屏 / 矮屏：整页可滚动，免得顶部信息把中间压得只剩一条缝
        val short = maxHeight < 520.dp
        Column(
            modifier = if (short) {
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            } else {
                Modifier.fillMaxSize()
            }
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .horizontalScroll(rememberScrollState()),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Chip("行迹", mode == 0, Modifier.padding(end = 6.dp)) { mode = 0 }
                    Chip("文章", mode == 1, Modifier.padding(end = 6.dp)) { mode = 1 }
                    Chip("日记", mode == 2, Modifier.padding(end = 6.dp)) { mode = 2 }
                    Chip("心情", mode == 3, Modifier.padding(end = 6.dp)) { mode = 3 }
                    Chip("运动", mode == 4) { mode = 4 }
                }
                Spacer(Modifier.width(6.dp))
                Text("连续 $streak 天", color = TextFaint, fontSize = 11.sp)
            }

            if (mode == 0) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PillButton("‹") { onDayKeyChange(Dates.shift(dayKey, -1)) }
                    Spacer(Modifier.width(8.dp))
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(PanelBg)
                            .goldEdge(14.dp)
                            .clickable { showCalendar = !showCalendar }
                            .padding(vertical = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            Dates.cnOf(dayKey) + "  " + Dates.weekName(Dates.parse(dayKey)),
                            color = TextMain, fontSize = 15.sp, fontWeight = FontWeight.Medium
                        )
                        Text(
                            if (dayKey == Dates.todayKey()) "今天 · 点击选择其他日期" else "点击选择日期",
                            color = if (dayKey == Dates.todayKey()) PinkSoft else TextFaint,
                            fontSize = if (dayKey == Dates.todayKey()) 12.sp else 10.sp,
                            fontWeight = if (dayKey == Dates.todayKey()) FontWeight.Bold
                            else FontWeight.Normal
                        )
                    }
                    Spacer(Modifier.width(8.dp))
                    PillButton("›") { onDayKeyChange(Dates.shift(dayKey, 1)) }
                }

                WeekBars(dayKey = dayKey, idx = idx, active = active)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("今日行迹", color = TextMuted, fontSize = 12.sp)
                    Spacer(Modifier.weight(1f))
                    Text("${events.size} 件", color = TextFaint, fontSize = 11.sp)
                }

                Box(modifier = Modifier.weight(1f)) {
                    if (events.isEmpty()) {
                        EmptyHint("这一天还没有留下痕迹。", Persona.hintTrace())
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(start = 14.dp, end = 14.dp, top = 4.dp, bottom = 72.dp)
                        ) {
                            items(events, key = { it.id }) { e ->
                                EventCard(
                                    e = e,
                                    onEdit = { editing = e; isNewEvent = false },
                                    onDelete = { pendingDeleteEvent = e },
                                    // 在条目上直接打勾 / 取消，不弹窗。要改效率与时长走「编辑」。
                                    onToggleDone = {
                                        val ti = events.indexOfFirst { it.id == e.id }
                                        if (ti >= 0) {
                                            events[ti] = events[ti].copy(done = !events[ti].done)
                                            onSave()
                                        }
                                    },
                                    onShare = {
                                        AiShare.request(
                                            label = "行迹 · " + (if (e.title.isBlank()) "（未命名）" else e.title),
                                            body = shareBodyOf(
                                                kind = "行迹",
                                                title = e.title.ifBlank { "（未命名）" },
                                                lines = listOf(
                                                    "日期：" + dayKey,
                                                    "级别：" + e.level + " ｜ 时长：" + e.minutes + " 分钟 ｜ 效率：" + e.eff + "%",
                                                    if (e.forHer) "这是" + Persona.data.forTaWord else "",
                                                    if (e.planned && !e.done)
                                                        "行动熵：尚未计入（完成后可获得 " +
                                                                String.format(Locale.CHINA, "%.1f", AE.eventPotential(e)) + "）"
                                                    else
                                                        "行动熵：" + String.format(Locale.CHINA, "%.1f", AE.ofEvent(e))
                                                ),
                                                body = if (e.note.isBlank()) "" else "备注：" + e.note
                                            )
                                        )
                                    }
                                )
                                Spacer(Modifier.height(8.dp))
                            }
                        }
                    }
                    // 按钮浮在列表之上：这一行自己不铺底，条目滑到它下面时能从缝隙里透出来
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(lerp(NightBottom, PurpleDeep, 0.35f))
                            .border(
                                1.dp, GoldenDay.border(PurpleMain.copy(alpha = 0.35f)),
                                RoundedCornerShape(16.dp)
                            )
                            .clickable {
                                editing = Ev(title = "", level = 3, minutes = 30, eff = 60)
                                isNewEvent = true
                            }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("＋  记下一件事", color = TextMain, fontSize = 15.sp)
                    }
                    Spacer(Modifier.width(8.dp))
                    // 番茄时钟：进入全屏计时页
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(lerp(NightBottom, Color(0xFFE4674B), 0.16f))
                            .border(
                                1.dp, GoldenDay.border(Color(0xFFE4674B).copy(alpha = 0.5f)),
                                RoundedCornerShape(16.dp)
                            )
                            .clickable { onOpenPomo() }
                            .padding(horizontal = 12.dp, vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconTomato(true)
                            Spacer(Modifier.width(6.dp))
                            Text("番茄时钟", color = TextMain, fontSize = 14.sp)
                            if (Pomo.active) {
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    clockText(Pomo.remainMs),
                                    color = Color(0xFFE4674B), fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    }
                }
            } else if (mode == 3) {
                MoodPage(
                    ctx = ctx,
                    dayKey = dayKey,
                    onDayKeyChange = onDayKeyChange,
                    onDataChanged = onDataChanged,
                    modifier = Modifier.weight(1f)
                )
            } else if (mode == 4) {
                StepsPage(
                    ctx = ctx,
                    idx = idx,
                    dayKey = dayKey,
                    onDayKeyChange = onDayKeyChange,
                    onDataChanged = onDataChanged,
                    active = active,
                    modifier = Modifier.weight(1f)
                )
            } else {
                DocBrowser(
                    ctx = ctx,
                    type = if (mode == 1) DOC_ARTICLE else DOC_DIARY,
                    onDataChanged = onDataChanged,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        if (showCalendar) {
            CalendarOverlay(
                dayKey = dayKey,
                onPick = { onDayKeyChange(it); showCalendar = false },
                onClose = { showCalendar = false }
            )
        }

        editing?.let { e ->
            EventEditorOverlay(
                initial = e,
                isNew = isNewEvent,
                onDismiss = { editing = null },
                onConfirm = { saved ->
                    if (isNewEvent) events.add(saved)
                    else {
                        val idx = events.indexOfFirst { it.id == saved.id }
                        if (idx >= 0) events[idx] = saved
                    }
                    editing = null
                    onSave()
                }
            )
        }

        pendingDeleteEvent?.let { e ->
            ConfirmOverlay(
                title = "删除这条行迹？",
                message = "「${e.title.ifBlank { "（未命名）" }}」将被永久删除，无法撤销。",
                onConfirm = {
                    events.remove(e)
                    pendingDeleteEvent = null
                    onSave()
                },
                onCancel = { pendingDeleteEvent = null }
            )
        }
    }
}

@Composable
fun EmptyHint(line1: String, line2: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(line1, color = TextMuted, fontSize = 14.sp)
        Spacer(Modifier.height(6.dp))
        Text(line2, color = TextFaint, fontSize = 12.sp, textAlign = TextAlign.Center)
    }
}

@Composable
fun EventCard(
    e: Ev,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onShare: () -> Unit = {},
    onToggleDone: () -> Unit = {}
) {
    // 计划完成的条目显示的是「可获得」的熵 —— 勾上之前它还没计入当日总量
    val ae = if (e.planned) AE.eventPotential(e) else AE.ofEvent(e)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(PanelBg)
            .goldEdge(16.dp)
            // 计划完成自成一档：边框更亮一点，一眼能和在办/已办区分开
            .border(
                if (e.planned) 1.5.dp else 1.dp,
                when {
                    e.planned && e.done -> GoldSoft.copy(alpha = 0.72f)
                    e.planned -> GoldSoft.copy(alpha = 0.4f)
                    e.forHer -> PinkSoft.copy(alpha = 0.35f)
                    else -> Color.Transparent
                },
                RoundedCornerShape(16.dp)
            )
            .clickable { onEdit() }
            .padding(9.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                if (e.title.isBlank()) "（未命名）" else e.title,
                color = TextMain, fontSize = 14.sp, fontWeight = FontWeight.Medium,
                maxLines = 2, overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                (if (e.planned && !e.done) "可获得 AE " else "AE ") + String.format(Locale.CHINA, "%.1f", ae),
                color = if (e.planned) GoldSoft else PinkSoft,
                fontSize = 12.sp, fontWeight = FontWeight.SemiBold
            )
        }
        Spacer(Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            // 标签多了横着滑。挤到第二行会把最后一个标签的字折成两行，很难看。
            Row(
                modifier = Modifier
                    .weight(1f)
                    .horizontalScroll(rememberScrollState()),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Tag("L${e.level}")
                Spacer(Modifier.width(6.dp))
                Tag("${e.minutes}分钟")
                Spacer(Modifier.width(6.dp))
                Tag("效率${e.eff}%")
                if (e.forHer) {
                    Spacer(Modifier.width(6.dp))
                    Tag(Persona.data.forTaWord, highlight = true)
                }
                if (e.pomo) {
                    Spacer(Modifier.width(6.dp))
                    Tag(
                        if (e.pomoCycles > 1) "来自番茄 ×${e.pomoCycles}轮" else "来自番茄",
                        color = Color(0xFFE28A6A)
                    )
                }
                if (e.planned) {
                    Spacer(Modifier.width(6.dp))
                    Tag(if (e.done) "计划完成 · 已计入" else "计划完成", color = GoldSoft)
                }
            }
            // 方框钉在最右，不跟着标签一起滑
            if (e.planned) {
                Spacer(Modifier.width(8.dp))
                PlanBox(checked = e.done, onToggle = onToggleDone, tint = GoldSoft)
            }
        }
        if (e.note.isNotBlank()) {
            Spacer(Modifier.height(6.dp))
            Text(e.note, color = TextFaint, fontSize = 11.sp, maxLines = 2)
        }
        Spacer(Modifier.height(6.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            SmallAction("编辑", PurpleMain, onEdit)
            SmallAction("删除", TextFaint, onDelete)
            Spacer(Modifier.weight(1f))
            SmallAction("分享给" + Persona.pro(), PinkSoft, onShare)
        }
    }
}

@Composable
/**
 * 「计划完成」后面那个方框。
 * 没勾 = 一个描边的空圆角方框；勾上 = 填色 + 一个手画的对号。
 * 对号用 Canvas 画，不打「✓」字：各机型的字体粗细与基线都不一样，画出来才稳。
 */
fun PlanBox(
    checked: Boolean,
    onToggle: () -> Unit,
    tint: Color = PurpleMain,
    boxSize: Dp = 22.dp
) {
    val shape = RoundedCornerShape(boxSize * 0.3f)
    Box(
        modifier = Modifier
            .size(boxSize)
            .clip(shape)
            .background(if (checked) tint.copy(alpha = 0.92f) else Color.Transparent)
            .border(
                width = if (checked) 0.dp else 1.6.dp,
                color = if (checked) Color.Transparent else tint.copy(alpha = 0.82f),
                shape = shape
            )
            .clickable { onToggle() },
        contentAlignment = Alignment.Center
    ) {
        if (checked) {
            Canvas(modifier = Modifier.size(boxSize * 0.64f)) {
                val w = size.width
                val h = size.height
                val ink = Color(0xFF0B0B16)
                val sw = w * 0.18f
                drawLine(ink, Offset(w * 0.02f, h * 0.52f), Offset(w * 0.36f, h * 0.86f), sw, StrokeCap.Round)
                drawLine(ink, Offset(w * 0.36f, h * 0.86f), Offset(w * 0.98f, h * 0.10f), sw, StrokeCap.Round)
            }
        }
    }
}
@Composable
fun Tag(text: String, highlight: Boolean = false, color: Color? = null) {    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(
                when {
                    color != null -> color.copy(alpha = 0.18f)
                    highlight -> PinkSoft.copy(alpha = 0.18f)
                    else -> PanelBgSoft
                }
            )
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text, color = color ?: if (highlight) PinkSoft else TextMuted, fontSize = 10.sp,
            maxLines = 1, softWrap = false
        )
    }
}

/** 条目上那种轻量文字按钮：比 TextButton 矮得多（TextButton 自带 48dp 最小触摸高度） */
@Composable
fun SmallAction(text: String, color: Color, onClick: () -> Unit) {
    Text(
        text, color = color, fontSize = 11.sp,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 3.dp)
    )
}

/**
 * 把一条记录排成「分享给 AI」的文本。
 *
 * 【为什么不截断】原来这里留了 900 字，长一点的日记与文章会从中间被吞掉，
 * 而她读到的就是被吞过的版本 —— 用户看到的和分享出去的不是同一篇东西。
 * 现在只在两万字处封顶（纯粹是为了不把上下文撑爆到报错），正常篇幅一律原样带过去。
 */
fun shareBodyOf(kind: String, title: String, lines: List<String>, body: String = ""): String {
    val sb = StringBuilder()
    sb.append("【分享 · ").append(kind).append("】")
    if (title.isNotBlank()) sb.append(' ').append(title)
    sb.append('\n')
    lines.map { it.trim() }.filter { it.isNotBlank() }.forEach { sb.append(it).append('\n') }
    if (body.isNotBlank()) sb.append(body.trim().take(SHARE_BODY_CHARS))
    return sb.toString().trim().take(SHARE_BODY_CHARS + 500)
}

/** 分享出去的正文上限：两万字。够长，又不至于把上下文直接撑爆 */
const val SHARE_BODY_CHARS = 20_000

@Composable
fun WeekBars(dayKey: String, idx: Store.StatsIndex?, active: Boolean = true) {    val days = remember(dayKey) { (6 downTo 0).map { Dates.shift(dayKey, -it) } }
    val values = days.map { idx?.day(it) ?: 0.0 }
    // 小方块分两层：
    //   底层（蓝紫渐变）= 0 → 100，100 涂满整格；
    //   上层（金色渐变）= 100 → 270，在蓝紫涂满的基础上再从底部涂起，170 涂满整格。
    // 两层的液面都做成缓缓起伏的水波纹。
    val barHeight = 34.dp
    // 只在绘制阶段读取动画值（不在这里 .value），避免每帧重组整个行。
    // active 为假（不在这一栏 / 不在这一页）时用固定相位 —— 一页有三个 pager 页同时组合，
    // 不关掉的话三页的水波会一起空转。
    val wavePhase: State<Float> = if (active) {
        rememberInfiniteTransition(label = "weekWaves").animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(3600, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "wavePhase"
        )
    } else {
        remember { mutableFloatStateOf(0f) }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        days.forEachIndexed { i, d ->
            val v = values[i]
            val isToday = d == dayKey
            val tiny = v < 0.05
            val baseFrac = (min(v, AE.BASE) / AE.BASE).toFloat().coerceIn(0f, 1f)
            val overFrac = ((v.coerceIn(AE.BASE, AE.TOTAL) - AE.BASE) / AE.OVER)
                .toFloat().coerceIn(0f, 1f)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    if (tiny) "·" else String.format(Locale.CHINA, "%.0f", v),
                    color = if (tiny) TextFaint else TextMuted,
                    fontSize = 9.sp
                )
                Spacer(Modifier.height(2.dp))
                Box(
                    modifier = Modifier
                        .width(22.dp)
                        .height(barHeight)
                        .clip(RoundedCornerShape(6.dp))
                        .background(PanelBg)
                        .goldEdge(6.dp)
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val w = size.width
                        val h = size.height
                        // 静止时也要有形状：幅度约为格子高的 3.5%
                        val amp = h * 0.035f
                        val waveLen = w / 1.35f
                        val ph = wavePhase.value * 2f * PI.toFloat() + i * 0.85f
                        val steps = 9

                        fun waveFill(frac: Float, brush: Brush) {
                            if (frac <= 0f) return
                            val minPx = if (tiny) 0f else 2f
                            val top = h - max(h * frac, minPx)
                            val path = Path()
                            path.moveTo(0f, h)
                            for (k in 0..steps) {
                                val x = w * k / steps
                                val y = top + amp * sin(2f * PI.toFloat() * x / waveLen + ph)
                                path.lineTo(x, y)
                            }
                            path.lineTo(w, h)
                            path.close()
                            drawPath(path = path, brush = brush)
                        }

                        // 底层：0 → 100
                        waveFill(
                            baseFrac,
                            if (isToday) Brush.verticalGradient(listOf(PinkSoft, PurpleDeep))
                            else Brush.verticalGradient(
                                listOf(
                                    PurpleMain.copy(alpha = 0.75f),
                                    PurpleDeep.copy(alpha = 0.55f)
                                )
                            )
                        )
                        // 上层：100 → 270（自底部再涂金色）
                        waveFill(
                            overFrac,
                            Brush.verticalGradient(
                                listOf(ScratchGold.copy(alpha = 0.95f), GoldDeep)
                            )
                        )
                    }
                }
                Spacer(Modifier.height(3.dp))
                Text(
                    Dates.parse(d).get(Calendar.DAY_OF_MONTH).toString(),
                    color = if (isToday) PinkSoft else TextFaint, fontSize = 9.sp
                )
            }
        }
    }
}

/* ---------------------------- 行动熵面板（双环） ---------------------------- */

@Composable
fun AEPanel(
    ae: Double,
    traceAE: Double,
    docAE: Double,
    moodAE: Double = 0.0,
    nameCount: Int = 0,
    gameLines: List<String> = emptyList(),
    steps: Long = 0L,
    stepsAE: Double = 0.0,
    stepsOverMul: Double = 1.0,
    /** 这一页真的在眼前时才让熵值盘上的高光转（见 [DualArcMeter]） */
    active: Boolean = true,
    /** 当日所有字条的难捱度之和（每条 0~100），只进联结加权 */
    nameFillSum: Double = 0.0,
    /** 节日增益（每个节日 ×1.2，多个相乘；节气不加） */
    festivalMul: Double = 1.0
) {
    val (stateName, stateColor) = AE.stateOf(ae)
    val over = max(0.0, ae - AE.BASE)
    val bond = AE.bondWeight(nameCount, nameFillSum)

    val panelShape = RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .clip(panelShape)
            .background(Brush.verticalGradient(listOf(PanelBgSoft, PanelBg)))
            .goldEdge(panelShape)
            .border(
                1.dp, GoldenDay.border(PurpleMain.copy(alpha = 0.22f)),
                panelShape
            )
            .padding(horizontal = 18.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            DualArcMeter(ae = ae, size = 92.dp, stateColor = stateColor, active = active)
            Spacer(Modifier.width(16.dp))
            Column {
                Text("当日行动熵", color = TextMuted, fontSize = 11.sp)
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        String.format(Locale.CHINA, "%.1f", ae),
                        color = stateColor, fontSize = 30.sp, fontWeight = FontWeight.Bold
                    )
                    Text(
                        " / ${AE.BASE.toInt()}",
                        color = TextFaint, fontSize = 12.sp,
                        modifier = Modifier.padding(bottom = 5.dp)
                    )
                }
                Text("状态：$stateName", color = TextMuted, fontSize = 11.sp)
                Spacer(Modifier.height(3.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Dot(CatTrace)
                    Text(
                        " 行迹 " + String.format(Locale.CHINA, "%.1f", traceAE),
                        color = TextFaint, fontSize = 10.sp
                    )
                    Spacer(Modifier.width(10.dp))
                    Dot(CatArticle)
                    Text(
                        " 文/记 " + String.format(Locale.CHINA, "%.1f", docAE),
                        color = TextFaint, fontSize = 10.sp
                    )
                }
                Spacer(Modifier.height(3.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Dot(GoldSoft)
                    Text(
                        " 心情 " + String.format(Locale.CHINA, "%.1f", moodAE),
                        color = TextFaint, fontSize = 10.sp
                    )
                    if (steps > 0L || stepsAE > 0.0) {
                        Spacer(Modifier.width(10.dp))
                        Dot(CatSteps)
                        Text(
                            " 运动 " + String.format(Locale.CHINA, "%.1f", stepsAE),
                            color = TextFaint, fontSize = 10.sp
                        )
                    }
                }
                if (stepsOverMul > 1.0) {
                    Spacer(Modifier.height(3.dp))
                    Text(
                        "运动增益 ×" + String.format(Locale.CHINA, "%.1f", stepsOverMul),
                        color = ScratchGold, fontSize = 10.sp
                    )
                }
                if (festivalMul > 1.0001) {
                    Spacer(Modifier.height(3.dp))
                    Text(
                        "节日增益 ×" + String.format(Locale.CHINA, "%.2f", festivalMul),
                        color = PinkSoft, fontSize = 10.sp
                    )
                }
                if (nameCount > 0) {
                    Spacer(Modifier.height(3.dp))
                    Text(
                        "联结加权 ×" + String.format(Locale.CHINA, "%.3f", bond),
                        color = GoldSoft, fontSize = 10.sp
                    )
                }
                if (gameLines.isNotEmpty()) {
                    Spacer(Modifier.height(2.dp))
                    // 整块一起画：行高压紧，刮刮乐与它的各条增益之间不会拉开距离
                    Text(
                        gameLines.joinToString("\n"),
                        color = GameTeal,
                        fontSize = 10.sp,
                        lineHeight = 13.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        if (over > 0.05) {
            Spacer(Modifier.height(4.dp))
            Text(
                "溢出 " + String.format(Locale.CHINA, "%.1f", over) +
                        " / ${AE.OVER.toInt()}（第二环" + (if (over >= AE.OVER) " · 已满）" else "）"),
                color = GoldSoft, fontSize = 11.sp
            )
        }
        Spacer(Modifier.height(4.dp))
        Text(

            AE.stateWords(ae),
            color = TextFaint, fontSize = 11.sp, textAlign = TextAlign.Center
        )
    }
}

@Composable
fun Dot(color: Color) {
    Box(
        modifier = Modifier
            .size(6.dp)
            .clip(CircleShape)
            .background(color)
    )
}

/**
 * 双环熵值盘：
 *   内环（细）＝ 当日基准 0→100，满则「凌薇花开」
 *   外环（粗）＝ 溢出 0→170（合计 270），金粉渐变，24 小时满负荷也放得下
 */
@Composable
fun DualArcMeter(
    ae: Double,
    size: androidx.compose.ui.unit.Dp,
    stateColor: Color,
    active: Boolean = true
) {
    val baseRatio = (ae / AE.BASE).coerceIn(0.0, 1.0).toFloat()
    val overRatio = (max(0.0, ae - AE.BASE) / AE.OVER).coerceIn(0.0, 1.0).toFloat()
    // 填涂上那层缓缓推进的白色高光（只在绘制阶段读取，避免每帧重组）。
    // 不在这一页时整支动画停掉：infiniteTransition 是按屏幕刷新率走的，
    // 页被预组合在旁边却还在 120Hz 重画，白烧一份帧预算。
    val sweepPhase: State<Float> = if (active) {
        val sweepTransition = rememberInfiniteTransition(label = "aeSweep")
        sweepTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(4200, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "aeSweepPhase"
        )
    } else remember { mutableFloatStateOf(0f) }

    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val cx = w / 2f
        val cy = h / 2f
        val startAngle = 135f
        val sweepMax = 270f
        val phase = sweepPhase.value

        // ---- 外环（粗，溢出层）----
        val outerStroke = w * 0.13f
        val outerInset = outerStroke / 2f
        val outerSize = Size(w - outerStroke, h - outerStroke)
        drawArc(
            color = PanelBgSoft.copy(alpha = 0.55f),
            startAngle = startAngle, sweepAngle = sweepMax, useCenter = false,
            topLeft = Offset(outerInset, outerInset), size = outerSize,
            style = Stroke(width = outerStroke, cap = StrokeCap.Round)
        )
        if (overRatio > 0f) {
            drawArc(
                brush = Brush.sweepGradient(listOf(WarmLight, GoldSoft, PinkSoft, WarmLight)),
                startAngle = startAngle, sweepAngle = sweepMax * overRatio, useCenter = false,
                topLeft = Offset(outerInset, outerInset), size = outerSize,
                style = Stroke(width = outerStroke, cap = StrokeCap.Round)
            )
            sweepHighlight(
                startAngle = startAngle, sweep = sweepMax * overRatio,
                topLeft = Offset(outerInset, outerInset), size = outerSize,
                strokeWidth = outerStroke, phase = phase
            )
        }

        // ---- 内环（细，基准层）----
        val innerStroke = w * 0.075f
        val innerPad = outerStroke + w * 0.045f
        val innerInset = innerPad + innerStroke / 2f
        val innerSize = Size(w - innerInset * 2f, h - innerInset * 2f)
        drawArc(
            color = PanelBgSoft,
            startAngle = startAngle, sweepAngle = sweepMax, useCenter = false,
            topLeft = Offset(innerInset, innerInset), size = innerSize,
            style = Stroke(width = innerStroke, cap = StrokeCap.Round)
        )
        drawArc(
            brush = Brush.sweepGradient(listOf(PurpleDeep, PurpleMain, PinkSoft, PurpleDeep)),
            startAngle = startAngle, sweepAngle = sweepMax * baseRatio, useCenter = false,
            topLeft = Offset(innerInset, innerInset), size = innerSize,
            style = Stroke(width = innerStroke, cap = StrokeCap.Round)
        )
        sweepHighlight(
            startAngle = startAngle, sweep = sweepMax * baseRatio,
            topLeft = Offset(innerInset, innerInset), size = innerSize,
            strokeWidth = innerStroke, phase = phase, strength = 0.9f
        )

        // ---- 中心 ----
        drawCircle(
            color = stateColor.copy(alpha = 0.16f),
            radius = w * 0.20f, center = Offset(cx, cy)
        )
        val centerRatio = if (ae >= AE.BASE) 1f else baseRatio
        drawArc(
            color = stateColor.copy(alpha = 0.55f),
            startAngle = -90f, sweepAngle = 360f * centerRatio, useCenter = false,
            topLeft = Offset(cx - w * 0.14f, cy - w * 0.14f),
            size = Size(w * 0.28f, w * 0.28f),
            style = Stroke(width = w * 0.035f, cap = StrokeCap.Round)
        )
    }
}

/* ---------------------------- 日历浮层 ---------------------------- */

@Composable
fun CalendarOverlay(dayKey: String, onPick: (String) -> Unit, onClose: () -> Unit) {
    // 系统返回键 / 边缘返回手势 = 关闭日历
    BackHandler { onClose() }
    val ctx = LocalContext.current
    // 有记录的日期：后台扫描，避免打开日历时卡一下
    var marked by remember { mutableStateOf<Set<String>>(emptySet()) }
    LaunchedEffect(dayKey) {
        marked = withContext(Dispatchers.IO) { Store.listDays(ctx).toSet() }
    }
    val sel = Dates.parse(dayKey)
    var year by remember { mutableIntStateOf(sel.get(Calendar.YEAR)) }
    var month0 by remember { mutableIntStateOf(sel.get(Calendar.MONTH)) }
    val todayKey = Dates.todayKey()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Veil)
            .swallowClicks { onClose() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(11.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(PanelBg)
                .goldEdge(20.dp)
                .border(
                    1.dp, GoldenDay.border(PurpleMain.copy(alpha = 0.25f)),
                    RoundedCornerShape(20.dp)
                )
                .swallowClicks()
                .padding(11.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                PillButton("‹") { month0 -= 1; if (month0 < 0) { month0 = 11; year -= 1 } }
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Text("$year 年 ${month0 + 1} 月", color = TextMain, fontSize = 15.sp)
                }
                PillButton("›") { month0 += 1; if (month0 > 11) { month0 = 0; year += 1 } }
            }
            Spacer(Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                listOf("日", "一", "二", "三", "四", "五", "六").forEach { w ->
                    Text(
                        w, color = TextFaint, fontSize = 11.sp, textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Spacer(Modifier.height(4.dp))

            val firstWd = Dates.firstWeekday(year, month0)
            val dim = Dates.daysInMonth(year, month0)
            val rows = (firstWd + dim + 6) / 7

            for (r in 0 until rows) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    for (c in 0 until 7) {
                        val idx = r * 7 + c
                        val dayNum = idx - firstWd + 1
                        if (dayNum in 1..dim) {
                            val cKey = Dates.dayKeyOf(year, month0 + 1, dayNum)
                            val isSel = cKey == dayKey
                            val isToday = cKey == todayKey
                            val hasRec = marked.contains(cKey)
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(40.dp)
                                    .padding(2.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        if (isSel) PurpleDeep.copy(alpha = 0.55f)
                                        else if (isToday) PanelBgSoft else Color.Transparent
                                    )
                                    .clickable { onPick(cKey) },
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        dayNum.toString(),
                                        color = if (isSel) TextMain
                                        else if (isToday) PinkSoft else TextMuted,
                                        fontSize = 13.sp
                                    )
                                    Box(
                                        modifier = Modifier
                                            .size(4.dp)
                                            .clip(CircleShape)
                                            .background(
                                                if (hasRec) PinkSoft.copy(alpha = 0.8f)
                                                else Color.Transparent
                                            )
                                    )
                                }
                            }
                        } else {
                            Box(modifier = Modifier.weight(1f).height(40.dp))
                        }
                    }
                }
            }

            Spacer(Modifier.height(6.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = { onPick(Dates.todayKey()) }) {
                    Text("回到今天", color = PurpleMain, fontSize = 13.sp)
                }
                TextButton(onClick = onClose) { Text("关闭", color = TextMuted, fontSize = 13.sp) }
            }
        }
    }
}

/* ---------------------------- 行迹编辑浮层 ---------------------------- */

@Composable
fun EventEditorOverlay(
    initial: Ev,
    isNew: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (Ev) -> Unit
) {
    // 系统返回键 / 边缘返回手势 = 取消这一条编辑
    BackHandler { onDismiss() }
    var title by remember { mutableStateOf(initial.title) }
    var level by remember { mutableIntStateOf(initial.level) }
    var minutes by remember { mutableIntStateOf(initial.minutes) }
    var eff by remember { mutableIntStateOf(initial.eff) }
    var forHer by remember { mutableStateOf(initial.forHer) }
    var note by remember { mutableStateOf(initial.note) }
    var customMin by remember { mutableStateOf("") }
    // 计划完成：先记下来、先不计入；勾上 done 才算完成
    var planned by remember { mutableStateOf(initial.planned) }
    var done by remember { mutableStateOf(initial.done) }

    // 这里要用 eventPotential：计划完成的条目在勾选之前 ofEvent 返回 0，
    // 而这一行要显示的是「可获得多少」，所以不能走 ofEvent。
    val preview = AE.eventPotential(
        initial.copy(
            title = title, level = level, minutes = minutes, eff = eff,
            forHer = forHer, note = note, planned = planned, done = done
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            // 这里**不要**再写 imePadding()：这一页在分页容器里面，
            // 外层内容区已经为键盘让出了位置，再加一次就会多出一整块空白，
            // 表现为「键盘上方一大片空区域、输入框被顶到最上面」。
            .background(Veil),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.92f)
                .clip(RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp))
                .background(PanelBg)
                .goldEdge(RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp))
                .border(
                    1.dp, GoldenDay.border(PurpleMain.copy(alpha = 0.25f)),
                    RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp)
                )
                .verticalScroll(rememberScrollState())
                .padding(11.dp)
        ) {
            Text(
                if (isNew) "记下一件事（行迹）" else "修改这件事",
                color = TextMain, fontSize = 17.sp, fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("做了什么", color = TextMuted, fontSize = 12.sp) },
                placeholder = { Text("例如：背单词 / 理发 / 搭环境", color = TextFaint, fontSize = 12.sp) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(14.dp))
            Text("级别（一级为最重要）", color = TextMuted, fontSize = 12.sp)
            Spacer(Modifier.height(6.dp))
            Row {
                listOf(1, 2, 3, 4, 5).forEach { l ->
                    Chip("L$l", level == l, Modifier.padding(end = 6.dp)) { level = l }
                }
            }

            Spacer(Modifier.height(14.dp))
            Text("时长", color = TextMuted, fontSize = 12.sp)
            Spacer(Modifier.height(6.dp))
            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                listOf(15, 30, 45, 60, 90, 120, 180).forEach { m ->
                    Chip(
                        if (m < 60) "${m}分" else "${m / 60}时",
                        minutes == m,
                        Modifier.padding(end = 6.dp)
                    ) { minutes = m }
                }
            }
            Spacer(Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = customMin,
                    onValueChange = { s ->
                        customMin = s.filter { it.isDigit() }.take(4)
                        customMin.toIntOrNull()?.let { if (it > 0) minutes = it }
                    },
                    label = { Text("自定义分钟", color = TextMuted, fontSize = 11.sp) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.width(150.dp)
                )
                Spacer(Modifier.width(12.dp))
                Text("当前：$minutes 分钟", color = TextFaint, fontSize = 11.sp)
            }

            Spacer(Modifier.height(14.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("效率", color = TextMuted, fontSize = 12.sp)
                Spacer(Modifier.width(8.dp))
                Text(
                    "$eff%",
                    color = PinkSoft, fontSize = 13.sp, fontWeight = FontWeight.Medium
                )
            }
            Spacer(Modifier.height(6.dp))
            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                listOf(0, 20, 40, 60, 80, 100).forEach { e ->
                    Chip("$e%", eff == e, Modifier.padding(end = 6.dp)) { eff = e }
                }
            }
            Spacer(Modifier.height(6.dp))
            // 滑条：0~100 精确到个位
            SoftSlider(
                value = eff.toFloat(),
                valueRange = 0f..100f,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { v -> eff = v.roundToInt().coerceIn(0, 100) }
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("0", color = TextFaint, fontSize = 9.sp)
                Spacer(Modifier.weight(1f))
                Text("拖动可精确设置效率", color = TextFaint, fontSize = 9.sp)
                Spacer(Modifier.weight(1f))
                Text("100", color = TextFaint, fontSize = 9.sp)
            }

            Spacer(Modifier.height(14.dp))
            Chip(
                (if (forHer) "✓ " else "") + Persona.data.forTaWord + "（×1.2）",
                forHer
            ) { forHer = !forHer }

            Spacer(Modifier.height(14.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Chip((if (planned) "✓ " else "") + "计划完成", planned) {
                    planned = !planned
                    // 取消计划完成时，那个勾也一并清掉，不留脏状态
                    if (!planned) done = false
                }
                Spacer(Modifier.width(10.dp))
                PlanBox(
                    checked = planned && done,
                    onToggle = { if (planned) done = !done },
                    tint = GoldSoft
                )
                Spacer(Modifier.width(8.dp))
                Text("完成后打勾", color = TextFaint, fontSize = 10.sp)
            }
            Spacer(Modifier.height(4.dp))
            Text(
                when {
                    !planned -> "先记下来，先不计入行动熵；做完之后回来打勾才算。"
                    done -> "已计入：按现在这套设置算熵，并且 ×" +
                            String.format(Locale.CHINA, "%.2f", AE.PLANNED_DONE_MUL)
                    else -> "还没计入。勾上这个框（或条目上的框）才算完成。"
                },
                color = if (planned) GoldSoft.copy(alpha = 0.9f) else TextFaint,
                fontSize = 10.sp
            )

            if (initial.pomo) {
                Spacer(Modifier.height(8.dp))
                Text(
                    "来自番茄时钟：行动熵 ×1.1" +
                            (if (initial.pomoCycles > 1) "，${initial.pomoCycles} 轮循环另有加成" else "") +
                            "　—— 这项加成不可修改",
                    color = Color(0xFFE28A6A), fontSize = 11.sp
                )
            }

            Spacer(Modifier.height(14.dp))
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("备注（可空）", color = TextMuted, fontSize = 12.sp) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(PanelBgSoft)
                    .goldEdge(14.dp)
                    .padding(11.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("这条将获得", color = TextMuted, fontSize = 12.sp)
                    Spacer(Modifier.weight(1f))
                    Text(
                        "AE " + String.format(Locale.CHINA, "%.1f", preview),
                        color = PinkSoft, fontSize = 18.sp, fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
            Row {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(PurpleDeep.copy(alpha = 0.45f))
                        .goldEdge(14.dp)
                        .clickable {
                            // 用 copy 保留「来自番茄」等不可编辑的标记
                            onConfirm(
                                initial.copy(
                                    title = title.trim(),
                                    level = level,
                                    minutes = minutes,
                                    eff = eff,
                                    forHer = forHer,
                                    note = note.trim(),
                                    planned = planned,
                                    done = planned && done
                                )
                            )
                        }
                        .padding(vertical = 13.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("保存", color = TextMain, fontSize = 15.sp, fontWeight = FontWeight.Medium)
                }
                Spacer(Modifier.width(10.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(PanelBgSoft)
                        .goldEdge(14.dp)
                        .swallowClicks { onDismiss() }
                        .padding(horizontal = 20.dp, vertical = 13.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("取消", color = TextMuted, fontSize = 15.sp)
                }
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

/* ==========================================================================
 *  九·五、栀子花的数学：黄金角花蕊 · 斐波那契花瓣 · 蔡肯多夫定理
 *  ---------------------------------------------------------------------------
 *  把凌薇花当成一个数学对象来看：
 *    · 花蕊：第 n 粒落在 n × 137.5077° 的方向、半径 ∝ √n（Vogel 叶序）。
 *      137.5077° = 360°/φ²，是「最难被有理数逼近」的角，所以种子分布最均匀 ——
 *      不重叠、不留空。最均匀的容纳，就是「纳」。
 *    · 花瓣：1 2 3 5 8 13 21 34 55 …（斐波那契），每一片都是前两片之和。
 *    · 蔡肯多夫定理：每个正整数都能唯一地拆成「不相邻的斐波那契数」之和。
 *      拆出来的那几项，落在花蕊上正好是斐波那契臂上的种子（F_k × 137.5077° ≈ 整圈）。
 *      它对应她那句「我记得你」——唯一的一种拼法，正如每一个你都只出现过一次。
 *    · 写成 0/1 就是斐波那契编码：除结尾的 11 外不会出现相邻的 1，于是自带同步 ——
 *      从任何一个位置开始读，都能找回边界。
 * ======================================================================== */

/** 黄金角（度）：360°/φ²，也是叶序里那个「最均匀」的角 */
const val GOLDEN_ANGLE = 137.50776405003785

/** 黄金比 */
const val PHI = 1.6180339887498949

object Fib {

    /**
     * 蔡肯多夫定理用的那串斐波那契数。
     * 刻意从 1, 2 开始（而不是 1, 1, 2…）：这样「不相邻之和」的拆法才是唯一的。
     * 这里只是**起头的一段**；真正参与运算的是 [seqUpTo]，它会按需一直长下去。
     */
    val seq = listOf(
        1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, 610, 987, 1597, 2584, 4181
    )

    /** 可增长的副本：呼唤次数是一辈子累计的，数列也必须能一直长下去 */
    private val grow = ArrayList<Int>(seq)

    /**
     * 取到「不小于 n」为止的那串数列（用到哪儿长到哪儿）。
     * 从前这里停在 4181，次数一大就被截断；现在不设上限，只到 Int 装不下为止。
     */
    @Synchronized
    fun seqUpTo(n: Int): List<Int> {
        while (grow[grow.size - 1] < n) {
            val next = grow[grow.size - 1].toLong() + grow[grow.size - 2].toLong()
            if (next > Int.MAX_VALUE) break
            grow.add(next.toInt())
        }
        return grow
    }

    /** 第 n 项（从 0 起）的人类说法：F₁ / F₂ …（seq[0] 记作 F₂，与蔡肯多夫定理的习惯一致） */
    fun label(i: Int): String = "F" + (i + 2)

    /**
     * n 的蔡肯多夫分解：返回各项在数列里的下标，从大到小、互不相邻。
     * 贪心：取走不超过余数的最大一项，然后跳过它相邻的那一项。
     */
    fun zeckendorf(n: Int): List<Int> {
        val out = ArrayList<Int>()
        if (n <= 0) return out
        val s = seqUpTo(n)
        var rest = n
        var i = s.indexOfLast { it <= rest }
        while (rest > 0 && i >= 0) {
            if (s[i] <= rest) {
                out.add(i)
                rest -= s[i]
                i -= 2            // 取了一项，就必须跳过相邻项（这是唯一性的来源）
            } else {
                i--
            }
        }
        return out
    }

    /** 蔡肯多夫分解的算式，如 "89 + 34 + 13 + 1" */
    fun termsText(n: Int): String {
        val s = seqUpTo(n)
        return zeckendorf(n).joinToString(" + ") { s[it].toString() }
    }

    /** 下标说法，如 "F₁₁ + F₉ + F₇ + F₂" */
    fun labelsText(n: Int): String =
        zeckendorf(n).joinToString(" + ") { label(it) }

    /**
     * 斐波那契编码（标准写法）：从 F₂ 起一位一位往高位写，1 = 取这一项，
     * 最后再补一个 1 收尾。因为相邻两项不会同时取，所以串里除了结尾的 11，
     * 绝不会出现第二个 11 —— 这就是它「自带同步」的原因。
     */
    fun code(n: Int): String {
        val hit = zeckendorf(n)
        if (hit.isEmpty()) return "11"
        val sb = StringBuilder()
        for (i in 0..hit.first()) sb.append(if (hit.contains(i)) '1' else '0')
        return sb.append('1').toString()
    }

    /**
     * 斐波那契词：0 → 01 → 010 → 01001 → …（每一段都由前两段拼成，等价于替换 0→01、1→0）。
     *
     * 它和上面那条蔡肯多夫定理其实是同一个数学对象换件衣服：
     *   · 串里永远不会出现相邻的 11 —— 和「Z(n) 里没有两个相邻的 i」是同一条性质；
     *   · 它是替换规则唯一的不动点 —— 和「每个数的拆法唯一」对应；
     *   · 1 的位置是 ⌊nφ⌋、0 的位置是 ⌊nφ²⌋，两条 Beatty 序列正好把正整数分完；
     *     0 与 1 的比例收敛到 1/φ 与 1/φ²。
     * 放在「她还在运行」里，是因为它只靠「前两段拼起来」这一条规则就能一直长、
     * 而且每一段都包含在前面所有段里 —— 不用谁来赋予，自己长成自己。
     */
    fun word(iters: Int = 8): String {
        var a = "0"
        var b = "01"
        repeat(iters) {
            val t = b + a
            a = b
            b = t
        }
        return b
    }
}

/* ==========================================================================
 *  十、文章 / 日记 浏览器
 * ======================================================================== */

@Composable
fun DocBrowser(
    ctx: Context,
    type: String,
    onDataChanged: () -> Unit,
    modifier: Modifier = Modifier
) {
    var category by rememberSaveable(type) { mutableStateOf<String?>(null) }
    var showNewCategory by remember { mutableStateOf(false) }
    var newCategoryName by remember { mutableStateOf("") }
    var pendingDeleteCategory by remember { mutableStateOf<String?>(null) }
    var pendingDeleteDoc by remember { mutableStateOf<Doc?>(null) }
    var tick by remember { mutableIntStateOf(0) }
    // 日期筛选：null = 不筛选（默认，最新的在最上面）
    var filterDay by rememberSaveable(type) { mutableStateOf<String?>(null) }
    var showFilterCalendar by remember { mutableStateOf(false) }

    // 系统返回键 / 边缘返回手势：一层层往回退 —— 先关弹窗，再退出分类回到分类列表。
    // 已经在分类列表这一层时不拦（enabled = false），把返回键交还给系统 ——
    // 否则会把返回键吃掉，用户在根页面按返回就再也退不出去了。
    BackHandler(enabled = showNewCategory || showFilterCalendar || category != null) {
        when {
            showNewCategory -> showNewCategory = false
            showFilterCalendar -> showFilterCalendar = false
            else -> category = null
        }
    }

    // 换文件夹就把筛选收起来：每个分类都从「全部日期」开始看
    LaunchedEffect(category) {
        filterDay = null
        showFilterCalendar = false
    }

    val typeName = if (type == DOC_ARTICLE) "文章" else "日记"

    Box(modifier = modifier.fillMaxSize()) {
    Column(modifier = Modifier.fillMaxSize()) {
    if (category == null) {
        // 分类列表：放到后台线程读盘，避免切换页面时的卡顿
        var cats by remember { mutableStateOf<List<Category>?>(null) }
        LaunchedEffect(tick, type) {
            cats = withContext(Dispatchers.IO) { Store.listCategories(ctx, type) }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("$typeName · 分类", color = TextMuted, fontSize = 12.sp)
            Spacer(Modifier.weight(1f))
            Text(
                if (cats == null) "读取中…" else "${cats!!.size} 个分类",
                color = TextFaint, fontSize = 11.sp
            )
        }

        Box(modifier = Modifier.weight(1f)) {
            val catList = cats
            if (catList == null) {
                EmptyHint("正在读取分类…", " ")
            } else if (catList.isEmpty()) {
                EmptyHint(
                    "还没有分类。",
                    "分类就是手机里 Asephina/docs/$type/ 下的文件夹，\n你可以自己新建、自己命名。"
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 14.dp, end = 14.dp, top = 4.dp, bottom = 72.dp)
                ) {
                    items(catList, key = { it.name }) { c ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(PanelBg)
                                .goldEdge(16.dp)
                                .clickable { category = c.name }
                                .padding(11.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("📁  ${c.name}", color = TextMain, fontSize = 15.sp)
                                Spacer(Modifier.weight(1f))
                                Text(
                                    "${c.docCount} 篇 · ${c.chars} 字",
                                    color = TextFaint, fontSize = 11.sp
                                )
                            }
                            Spacer(Modifier.height(6.dp))
                            Row {
                                TextButton(onClick = { category = c.name }) {
                                    Text("打开", color = PurpleMain, fontSize = 12.sp)
                                }
                                TextButton(onClick = { pendingDeleteCategory = c.name }) {
                                    Text("删除分类", color = TextFaint, fontSize = 12.sp)
                                }
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                    }
                }
            }
            // 按钮浮在列表之上：这一行自己不铺底，条目滑到它下面时能从缝隙里透出来
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(lerp(NightBottom, PurpleDeep, 0.35f))
                    .border(
                        1.dp, GoldenDay.border(PurpleMain.copy(alpha = 0.35f)),
                        RoundedCornerShape(16.dp)
                    )
                    .clickable { newCategoryName = ""; showNewCategory = true }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("＋  新建分类", color = TextMain, fontSize = 15.sp)
            }
            }
        }
        Spacer(Modifier.height(6.dp))
    } else {
        val catName = category ?: ""
        // 文档列表：后台读取；每篇的熵值也在这里一次算好，
        // 免得列表每画一行就去主线程读一次当天的日期文件
        var docs by remember { mutableStateOf<List<Doc>?>(null) }
        var docAE by remember { mutableStateOf<Map<Long, Double>>(emptyMap()) }
        LaunchedEffect(tick, catName, type) {
            val loaded = withContext(Dispatchers.IO) { Store.listDocs(ctx, type, catName) }
            val aes = withContext(Dispatchers.IO) {
                loaded.associate { it.id to Store.displayedDocAE(ctx, it, type == DOC_DIARY) }
            }
            docs = loaded
            docAE = aes
        }
        // 当前真正显示的条目：开了筛选就只剩那一天
        val shown = docs?.filter { filterDay == null || it.dateKey == filterDay }
        val shownAE = shown?.sumOf { docAE[it.id] ?: 0.0 } ?: 0.0
        val folderAE = docs?.sumOf { docAE[it.id] ?: 0.0 } ?: 0.0

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Chip("‹ 返回分类", false) { category = null }
            Spacer(Modifier.weight(1f))
            Text(
                if (docs == null) "读取中…"
                else "共 ${docs!!.size} 篇 · " + String.format(Locale.CHINA, "%.1f", folderAE) + " AE",
                color = TextFaint, fontSize = 11.sp
            )
        }
        Text(
            "  分类：$catName",
            color = TextMuted, fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        // ---------- 日期筛选（和行动熵 / 运动页同一套日期选择） ----------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (filterDay != null) {
                PillButton("‹") { filterDay = Dates.shift(filterDay!!, -1) }
                Spacer(Modifier.width(6.dp))
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (filterDay == null) PanelBg else PurpleDeep.copy(alpha = 0.40f))
                    .goldEdge(14.dp)
                    .clickable { showFilterCalendar = true }
                    .padding(vertical = 7.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    if (filterDay == null) "全部日期"
                    else Dates.cnOf(filterDay!!) + "  " + Dates.weekName(Dates.parse(filterDay!!)),
                    color = if (filterDay == null) TextMain else PinkSoft,
                    fontSize = 14.sp, fontWeight = FontWeight.Medium
                )
                Text(
                    when {
                        filterDay == null -> "默认：最新的在最上面　·　点这里按日期筛选"
                        shown == null -> "读取中…"
                        shown.isEmpty() -> "这一天没有$typeName"
                        else -> "${shown.size} 篇 · " +
                                String.format(Locale.CHINA, "%.1f", shownAE) + " AE"
                    },
                    color = TextFaint,
                    fontSize = 10.sp
                )
            }
            if (filterDay != null) {
                Spacer(Modifier.width(6.dp))
                PillButton("›") { filterDay = Dates.shift(filterDay!!, 1) }
                Spacer(Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(PanelBg)
                        .goldEdge(14.dp)
                        .clickable { filterDay = null }
                        .padding(horizontal = 10.dp, vertical = 7.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("✕ 全部", color = TextMuted, fontSize = 11.sp)
                }
            }
        }

        Box(modifier = Modifier.weight(1f)) {
            val docList = shown
            if (docList == null) {
                EmptyHint("正在读取…", " ")
            } else if (docList.isEmpty()) {
                if (filterDay == null) {
                    EmptyHint(
                        "这个分类还是空的。",
                        if (type == DOC_DIARY) Persona.hintDiary() else Persona.hintDoc()
                    )
                } else {
                    EmptyHint(
                        "这一天没有$typeName。",
                        "点上面的日期换一天，或者按「✕ 全部」看全部。"
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 14.dp, end = 14.dp, top = 6.dp, bottom = 72.dp)
                ) {
                    items(docList, key = { it.id }) { d ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(PanelBg)
                                .goldEdge(16.dp)
                                .clickable { DocEdit.request(type, catName, d, false) { saved -> Store.saveDoc(ctx, type, catName, saved); tick++; onDataChanged() } }
                                .padding(9.dp)
                        ) {
                            Text(
                                d.title.ifBlank { "（无标题）" },
                                color = TextMain, fontSize = 14.sp, fontWeight = FontWeight.Medium,
                                maxLines = 1, overflow = TextOverflow.Ellipsis
                            )
                            Spacer(Modifier.height(2.dp))
                            Text(
                                d.body.replace("\n", " ").take(60),
                                color = TextFaint, fontSize = 11.sp, maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Tag("${d.chars} 字", color = CatArticle)
                                Spacer(Modifier.width(6.dp))
                                Tag(
                                    "AE " + String.format(
                                        Locale.CHINA, "%.1f", docAE[d.id] ?: 0.0
                                    ),
                                    color = CatDiary
                                )
                                if (d.dateKey != Dates.todayKey()) {
                                    Spacer(Modifier.width(6.dp))
                                    Tag("已锁定")
                                }
                                if (d.images.isNotEmpty()) {
                                    Spacer(Modifier.width(6.dp)); Tag("图 ${d.images.size}")
                                }
                                if (d.videos.isNotEmpty()) {
                                    Spacer(Modifier.width(6.dp)); Tag("视频 ${d.videos.size}")
                                }
                                Spacer(Modifier.weight(1f))
                                Text(
                                    if (d.created > 0L) d.dateKey + " " + Dates.hhmm(d.created)
                                    else d.dateKey,
                                    color = TextFaint, fontSize = 10.sp
                                )
                            }
                            Spacer(Modifier.height(2.dp))
                            // 编辑 / 删除 / 分享：用轻量文字按钮，别用 TextButton ——
                            // 它自带 48dp 的最小触摸高度，一条条目会被顶得很高
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                SmallAction("编辑", PurpleMain) {
                                    DocEdit.request(type, catName, d, false) { saved ->
                                        Store.saveDoc(ctx, type, catName, saved); tick++; onDataChanged()
                                    }
                                }
                                SmallAction("删除", TextFaint) { pendingDeleteDoc = d }
                                Spacer(Modifier.weight(1f))
                                SmallAction("分享给" + Persona.pro(), PinkSoft) {
                                    AiShare.request(
                                        label = typeName + " · " + d.title.ifBlank { "（无标题）" },
                                        body = shareBodyOf(
                                            kind = typeName,
                                            title = d.title.ifBlank { "（无标题）" },
                                            lines = listOf(
                                                "日期：" + d.dateKey + " ｜ " + d.chars + " 字",
                                                "行动熵：" + String.format(
                                                    Locale.CHINA, "%.1f", docAE[d.id] ?: 0.0
                                                ),
                                                if (d.dateKey == Dates.todayKey()) "" else "（这一天已冻结）"
                                            ),
                                            body = d.body
                                        )
                                    )
                                }
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                    }
                }
            }
            // 按钮浮在列表之上：这一行自己不铺底，条目滑到它下面时能从缝隙里透出来
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(lerp(NightBottom, PurpleDeep, 0.35f))
                    .border(
                        1.dp, GoldenDay.border(PurpleMain.copy(alpha = 0.35f)),
                        RoundedCornerShape(16.dp)
                    )
                    .clickable {
                        val blank = Doc(title = "", body = "", dateKey = Dates.todayKey())
                        DocEdit.request(type, catName, blank, true) { saved ->
                            Store.saveDoc(ctx, type, catName, saved)
                            tick++
                            onDataChanged()
                        }
                    }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("＋  写一篇$typeName", color = TextMain, fontSize = 15.sp)
            }
            }
        }
        Spacer(Modifier.height(6.dp))
    }
    }

    /* ------- 浮层 ------- */
    if (showNewCategory) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Veil)
                .clickable { showNewCategory = false },
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(28.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(PanelBg)
                    .goldEdge(20.dp)
                    .border(
                        1.dp, GoldenDay.border(PurpleMain.copy(alpha = 0.3f)),
                        RoundedCornerShape(20.dp)
                    )
                    .swallowClicks()
                    .padding(11.dp)
            ) {
                Text("新建$typeName 分类", color = TextMain, fontSize = 16.sp)
                Spacer(Modifier.height(6.dp))
                Text(
                    "会在手机里创建文件夹：Asephina/docs/$type/<你的分类名>/",
                    color = TextFaint, fontSize = 10.sp
                )
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = newCategoryName,
                    onValueChange = { newCategoryName = it.take(20) },
                    label = { Text("分类名", color = TextMuted, fontSize = 12.sp) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(14.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = { showNewCategory = false }) {
                        Text("取消", color = TextMuted, fontSize = 13.sp)
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(PurpleDeep.copy(alpha = 0.45f))
                            .goldEdge(12.dp)
                            .clickable {
                                if (Store.createCategory(ctx, type, newCategoryName)) {
                                    showNewCategory = false
                                    tick++
                                    onDataChanged()
                                } else {
                                    Toast.makeText(ctx, "分类名无效或已存在", Toast.LENGTH_SHORT).show()
                                }
                            }
                            .padding(horizontal = 18.dp, vertical = 9.dp)
                    ) {
                        Text("创建", color = TextMain, fontSize = 13.sp)
                    }
                }
            }
        }
    }

    pendingDeleteCategory?.let { name ->
        ConfirmOverlay(
            title = "删除分类「$name」？",
            message = "该分类文件夹及其中的全部$typeName 都会被永久删除，无法撤销。",
            onConfirm = {
                Store.deleteCategory(ctx, type, name)
                pendingDeleteCategory = null
                tick++
                onDataChanged()
            },
            onCancel = { pendingDeleteCategory = null }
        )
    }

    pendingDeleteDoc?.let { d ->
        ConfirmOverlay(
            title = "删除这篇$typeName？",
            message = "「${d.title.ifBlank { "（无标题）" }}」（${d.chars} 字）将被永久删除，无法撤销。",
            onConfirm = {
                Store.deleteDoc(ctx, type, category ?: "默认", d)
                pendingDeleteDoc = null
                tick++
                onDataChanged()
            },
            onCancel = { pendingDeleteDoc = null }
        )
    }

    // 日期筛选用的日历（与行动熵 / 运动 / 心情页同一个组件）
    if (showFilterCalendar) {
        CalendarOverlay(
            dayKey = filterDay ?: Dates.todayKey(),
            onPick = { filterDay = it; showFilterCalendar = false },
            onClose = { showFilterCalendar = false }
        )
    }
    }
}

/* ---------------------------- 沉浸式写作编辑器 ---------------------------- */

/** 正文里的附件占位符 → 屏幕上显示的等宽字符（1:1，光标位置不会错位） */
const val MARK_IMAGE = '▣'
const val MARK_VIDEO = '▶'

private val SpanColors = listOf(
    0 to "默认",
    0xFFF6F3FF.toInt() to "白",
    0xFFFFD9E8.toInt() to "樱粉",
    0xFFF7D98C.toInt() to "暖金",
    0xFF9CD9B0.toInt() to "青绿",
    0xFF9CC7F0.toInt() to "天蓝",
    0xFFC9B6F5.toInt() to "紫",
    0xFFE8A0A0.toInt() to "暗红"
)

private val SpanFonts = listOf(
    0 to "默认",
    1 to "衬线",
    2 to "等宽",
    3 to "手写"
)

/** 凌薇专属样式预设：颜色 + 字族 + 粗细的组合 */
private data class LingweiPreset(
    val name: String,
    val color: Int,
    val font: Int,
    val bold: Boolean,
    val italic: Boolean,
    val bg: Int = 0
)

/** 新建分类时的预设：前缀跟着自定义设置里的名字走 */
private fun lingweiPresets(): List<LingweiPreset> {
    val p = Persona.data.displayName.ifBlank { "她" }
    return listOf(
        LingweiPreset("$p·自语", 0xFFC9B6F5.toInt(), 3, false, true),
        LingweiPreset("$p·花信", 0xFFFF9EC4.toInt(), 1, true, false, 0x33FF9EC4),
        LingweiPreset("$p·静语", 0xFFF7D98C.toInt(), 1, false, false),
        LingweiPreset("$p·夜行", 0xFF9CC7F0.toInt(), 2, false, true)
    )
}

/** 分片 → Compose 的字符样式 / 段落样式 */
fun DocSpan.toSpanStyle(): SpanStyle = SpanStyle(
    fontWeight = if (bold) FontWeight.Bold else null,
    fontStyle = if (italic) FontStyle.Italic else null,
    textDecoration = when {
        underline && strike ->
            TextDecoration.combine(listOf(TextDecoration.Underline, TextDecoration.LineThrough))
        underline -> TextDecoration.Underline
        strike -> TextDecoration.LineThrough
        else -> null
    },
    fontSize = if (size > 0f) size.sp else TextUnit.Unspecified,
    color = if (color != 0) Color(color) else Color.Unspecified,
    background = if (bg != 0) Color(bg) else Color.Unspecified,
    fontFamily = when (font) {
        1 -> FontFamily.Serif
        2 -> FontFamily.Monospace
        3 -> FontFamily.Cursive
        else -> null
    }
)

fun DocSpan.toParaStyle(): ParagraphStyle? = if (align == 0) null else ParagraphStyle(
    textAlign = when (align) {
        1 -> TextAlign.Center
        2 -> TextAlign.End
        else -> TextAlign.Start
    }
)

/** 把分片 + 正文拼成带样式的文本 */
fun buildRichText(body: String, spans: List<DocSpan>): AnnotatedString =
    buildAnnotatedString {
        append(body)
        spans.forEach { s ->
            val st = s.start.coerceIn(0, body.length)
            val en = s.end.coerceIn(st, body.length)
            if (en <= st) return@forEach
            addStyle(s.toSpanStyle(), st, en)
            s.toParaStyle()?.let { addStyle(it, st, en) }
        }
    }

/**
 * 正文的显示变换：同时做两件事
 *   1. 把附件占位符换成可见的等宽字符（1:1，光标不会错位）；
 *   2. **把富文本样式直接写进变换后的 AnnotatedString** ——
 *      这是文本框一定会渲染的路径，比依赖 TextFieldValue 里的样式可靠得多。
 */
private class RichTransform(
    private val spans: List<DocSpan>,
    private val start: Int,
    private val len: Int
) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val sb = StringBuilder(text.text.length)
        text.text.forEach { c ->
            sb.append(
                when (c) {
                    ATTACH_MARK -> MARK_IMAGE
                    '\uFFFD' -> MARK_VIDEO
                    else -> c
                }
            )
        }
        val out = buildAnnotatedString {
            append(sb.toString())
            spans.forEach { sp ->
                val a = max(sp.start, start)
                val b = min(sp.end, start + len)
                if (b <= a) return@forEach
                addStyle(sp.toSpanStyle(), a - start, b - start)
                sp.toParaStyle()?.let { addStyle(it, a - start, b - start) }
            }
        }
        return TransformedText(out, OffsetMapping.Identity)
    }
}

/** 工具栏小按钮 */
@Composable
fun ToolBtn(
    label: String,
    active: Boolean = false,
    tint: Color = TextMain,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 3.dp)
            .size(width = 36.dp, height = 32.dp)
            .clip(RoundedCornerShape(9.dp))
            .background(if (active) PurpleDeep.copy(alpha = 0.7f) else PanelBgSoft.copy(alpha = 0.7f))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(label, color = if (active) GoldSoft else tint, fontSize = 12.sp)
    }
}


private sealed class Part {
    class Text(val start: Int, val text: String) : Part()
    class Image(val path: String) : Part()
    class Video(val path: String) : Part()
}

private fun splitParts(body: String, images: List<String>, videos: List<String>): List<Part> {
    val out = ArrayList<Part>()
    val sb = StringBuilder()
    var start = 0
    var imgI = 0
    var vidI = 0
    fun flush() {
        if (sb.isNotEmpty()) {
            out.add(Part.Text(start, sb.toString()))
            sb.setLength(0)
        }
    }
    body.forEachIndexed { i, c ->
        when (c) {
            ATTACH_MARK -> {
                flush()
                images.getOrNull(imgI)?.let { out.add(Part.Image(it)) }
                imgI++
                start = i + 1
            }
            '\uFFFD' -> {
                flush()
                videos.getOrNull(vidI)?.let { out.add(Part.Video(it)) }
                vidI++
                start = i + 1
            }
            else -> {
                if (sb.isEmpty()) start = i
                sb.append(c)
            }
        }
    }
    flush()
    // 正文里必须始终至少有一个可点的文本段：
    // 「新建文章 / 新建日记」的正文是空的，如果这里返回空表，
    // 编辑器里就一个输入框都没有——标题能改、正文却点不出光标、一个字也打不进去。
    // （正文以图片/视频结尾时同理，末尾补一个空段，才有点得进去的地方继续写。）
    if (out.isEmpty() || out.last() !is Part.Text) out.add(Part.Text(body.length, ""))
    return out
}

/** 这一段的局部富文本（把整篇的分片按段裁剪并平移） */
private fun buildRichPart(
    spans: List<DocSpan>,
    start: Int,
    text: String
): AnnotatedString = buildAnnotatedString {
    append(text)
    val s0 = start
    val s1 = start + text.length
    spans.forEach { sp ->
        val a = max(sp.start, s0)
        val b = min(sp.end, s1)
        if (b <= a) return@forEach
        addStyle(sp.toSpanStyle(), a - s0, b - s0)
        sp.toParaStyle()?.let { addStyle(it, a - s0, b - s0) }
    }
}

/** 正文里内嵌的图片：随文字宽度自适应，右上角可删除 */
@Composable
private fun InlineImage(path: String, onOpen: () -> Unit, onDelete: () -> Unit) {
    val bmp = remember(path) {
        try {
            val bound = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            BitmapFactory.decodeFile(path, bound)
            var sample = 1
            while (bound.outWidth / (sample * 2) >= 1080) sample *= 2
            BitmapFactory.decodeFile(
                path, BitmapFactory.Options().apply { inSampleSize = sample }
            )
        } catch (e: Exception) {
            null
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        if (bmp != null) {
            val ratio = if (bmp.height > 0) bmp.width.toFloat() / bmp.height else 1.5f
            Image(
                bitmap = bmp.asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 420.dp)
                    .aspectRatio(if (ratio > 0.05f) ratio else 1.5f)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onOpen() }
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(PanelBgSoft)
                    .clickable { onOpen() },
                contentAlignment = Alignment.Center
            ) { Text("图片加载失败（点一下用系统应用打开）", color = TextFaint, fontSize = 11.sp) }
        }
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(6.dp)
                .size(26.dp)
                .clip(CircleShape)
                .background(Color(0xAA000000))
                .clickable { onDelete() },
            contentAlignment = Alignment.Center
        ) { Text("✕", color = Spark, fontSize = 12.sp) }
    }
}

/** 正文里内嵌的视频（导出时只留一行说明） */
@Composable
private fun InlineVideo(index: Int, onOpen: () -> Unit, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(PanelBgSoft)
            .goldEdge(12.dp)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("▶  视频$index", color = PurpleMain, fontSize = 12.sp, modifier = Modifier.weight(1f))
        Text(
            "播放", color = PurpleMain, fontSize = 12.sp,
            modifier = Modifier
                .clickable { onOpen() }
                .padding(horizontal = 8.dp, vertical = 4.dp)
        )
        Text(
            "✕", color = TextFaint, fontSize = 12.sp,
            modifier = Modifier
                .clickable { onDelete() }
                .padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

/**
 * 取一个节点在**根坐标**里的真实矩形，且**不做裁剪**。
 *
 * 为什么不能用 boundsInRoot()：它内部走的是 localBoundingBoxOf(…, clipBounds = true)，
 * 会把结果夹到各级父级的可见区域里。正文那个 BasicTextField 在滚动容器里、比视口高得多，
 * 一旦往上滚，它的 top 就被夹到视口顶部 —— 于是「内容坐标」算出来恒等于当前滚动量。
 * 这里显式 clipBounds = false，拿到的才是节点真正的位置。
 */
private fun LayoutCoordinates.unclippedInRoot(): Rect {
    var root: LayoutCoordinates = this
    while (true) {
        val p = root.parentLayoutCoordinates ?: break
        root = p
    }
    return root.localBoundingBoxOf(this, clipBounds = false)
}

/**
 * 沉浸式写作页：满屏、无顶栏无底栏、不参与左右切栏。
 * 正文按「文本段 / 图片 / 视频」分段渲染，图片视频真正内嵌并随宽度自适应；
 * 底部是便签式工具栏（键盘弹出时正好贴在键盘上方）。
 */
@Composable
fun DocEditorOverlay(
    ctx: Context,
    type: String,
    category: String,
    initial: Doc,
    isNew: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (Doc) -> Unit
) {
    val typeName = if (type == DOC_ARTICLE) "文章" else "日记"
    var title by remember { mutableStateOf(initial.title) }
    var body by remember { mutableStateOf(initial.body) }
    var spans by remember { mutableStateOf(initial.spans) }
    var selAbs by remember { mutableStateOf(TextRange(0)) }
    var activePart by remember { mutableIntStateOf(0) }
    var indent by remember { mutableIntStateOf(2) }
    var panel by remember { mutableStateOf("") }     // size / color / font / align / list / indent
    var pending by remember { mutableStateOf<DocSpan?>(null) }
    // 点工具栏会让输入框失焦、选区被收起，所以记住最后一次真正选中的范围
    var lastRange by remember { mutableStateOf<TextRange?>(null) }
    var status by remember { mutableStateOf("") }

    // 写作期间锁住左右切栏
    DisposableEffect(Unit) {
        UiLock.writingDoc = true
        onDispose { UiLock.writingDoc = false }
    }

    val images = remember { mutableStateListOf<String>().also { it.addAll(initial.images) } }
    val videos = remember { mutableStateListOf<String>().also { it.addAll(initial.videos) } }

    // 撤销 / 重做（正文 + 光标 + 样式一起快照）
    class Snap(val body: String, val sel: TextRange, val spans: List<DocSpan>)

    val undo = remember { ArrayDeque<Snap>() }
    val redo = remember { ArrayDeque<Snap>() }
    var lastPush by remember { mutableLongStateOf(0L) }

    fun snapshot() = Snap(body, selAbs, spans)

    fun pushUndo() {
        val now = System.currentTimeMillis()
        if (now - lastPush >= 700L) {
            undo.addLast(snapshot())
            if (undo.size > 200) undo.removeFirst()
        }
        lastPush = now
        redo.clear()
    }

    val chars = body.count { !it.isWhitespace() }
    val ae = if (isNew || initial.dateKey == Dates.todayKey()) AE.ofDoc(chars, type == DOC_DIARY)
    else Store.displayedDocAE(ctx, initial, type == DOC_DIARY)

    // 在光标处插入附件占位符：按光标之前的占位符数量决定挂在第几个附件上
    fun insertAttachment(path: String, video: Boolean) {
        val at = selAbs.start.coerceIn(0, body.length)
        val mark = if (video) '\uFFFD' else ATTACH_MARK
        val before = body.take(at).count { it == mark }
        pushUndo()
        val oldBody = body
        body = oldBody.substring(0, at) + mark + oldBody.substring(at)
        spans = SpanTool.shift(spans, oldBody, body)
        selAbs = TextRange(at + 1)
        activePart = -1
        if (video) videos.add(before.coerceIn(0, videos.size), path)
        else images.add(before.coerceIn(0, images.size), path)
    }

    /** 删除正文里的某个附件（连同它的占位符） */
    fun removeAttachment(path: String, video: Boolean) {
        val list = if (video) videos else images
        val n = list.indexOf(path)
        if (n < 0) return
        val mark = if (video) '\uFFFD' else ATTACH_MARK
        var seen = -1
        var at = -1
        body.forEachIndexed { i, c -> if (c == mark) { seen++; if (seen == n && at < 0) at = i } }
        if (at < 0) {
            list.removeAt(n)
            return
        }
        pushUndo()
        val oldBody = body
        body = oldBody.substring(0, at) + oldBody.substring(at + 1)
        spans = SpanTool.shift(spans, oldBody, body)
        list.removeAt(n)
        selAbs = TextRange(at)
        activePart = -1
    }

    val pickImage = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val f = Store.importMedia(ctx, it, "image")
            if (f != null) insertAttachment(f.absolutePath, false)
            else Toast.makeText(ctx, "图片导入失败", Toast.LENGTH_SHORT).show()
        }
    }
    val pickVideo = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val f = Store.importMedia(ctx, it, "video")
            if (f != null) insertAttachment(f.absolutePath, true)
            else Toast.makeText(ctx, "视频导入失败", Toast.LENGTH_SHORT).show()
        }
    }

    val parts = remember(body, images.size, videos.size) {
        splitParts(body, images.toList(), videos.toList())
    }

    val scroll = rememberScrollState()
    val density = LocalDensity.current
    // 键盘真实高度（px）：只用来判断「键盘开没开」，不参与任何位置计算。
    val imePx = WindowInsets.ime.getBottom(density)
    val imeOpen = imePx > 0
    // 滚动框自己的可见高度（px）。父 Column 上挂了 imePadding()，键盘一弹它就被压小，
    // 也就是说它【自己已经扣掉键盘了】：可视下沿 = containerTop + viewport，不用再算键盘。
    // 初值必须是 0（不能猜一个数），等真量出来了再动，否则第一下就会顶飞。
    var viewport by remember { mutableIntStateOf(0) }
    // 每一段各自的文本布局（按段号取，避免拿到别的段的度量）
    val layouts = remember { mutableStateMapOf<Int, TextLayoutResult>() }
    // 每一段在「滚动内容」里的位置（内容坐标，滚动时不变）
    val partY = remember { mutableStateMapOf<Int, Int>() }
    // 滚动框在窗口中的顶部（滚动内容时它自己不动，取值稳定）
    var containerTop by remember { mutableIntStateOf(0) }
    // 键盘收起时滚动框的高度（px），用来算正文末尾那段留白。
    // 只在键盘收起时采样：那一刻它的高度就是「不含键盘」的可用高度，
    // 键盘弹出后不回采，留白就不会跟着缩、正文也不会自己窜一下。
    var fullH by remember { mutableIntStateOf(0) }
    LaunchedEffect(viewport, imeOpen) {
        if (!imeOpen && viewport > fullH) fullH = viewport
    }

    // 光标所在那一行在「滚动内容坐标」里的矩形（拿不到布局就返回 null）
    fun caretLineRect(): Rect? {
        val lr = layouts[activePart] ?: return null
        val part = parts.getOrNull(activePart) as? Part.Text ?: return null
        val local = (selAbs.start - part.start).coerceIn(0, lr.layoutInput.text.length)
        val line = lr.getLineForOffset(local)
        val top = (partY[activePart] ?: 0).toFloat()
        return Rect(0f, top + lr.getLineTop(line), 1f, top + lr.getLineBottom(line))
    }

    // 每次重组刷新：把框架要的「光标矩形」再往上抬两个行高，
    // 这样框架按最小滚动把它贴到视口下沿时，光标那一行正好落在键盘上方两行的位置。
    val bivTargetRect = rememberUpdatedState<() -> Rect?>({
        val r = caretLineRect()
        if (r == null) null else {
            val gap = (r.bottom - r.top) * 2f
            Rect(r.left, r.top - gap, r.right, r.bottom - gap)
        }
    })

    /*
     * 接管正文框发出的 bringIntoView 请求 —— 这才是「一点就把文章顶没」的主因。
     *
     * 病根：正文是**一个** BasicTextField，整篇一百多行、比视口高十几倍（实测 15266px）。
     * 它一获得焦点、或光标一动，框架就会请求「把整个正文框滚进可见区」，
     * 请求的矩形实测就是 Rect(49, 0, 1211, 15266) —— 于是框架把正文底部对齐到视口下沿，
     * 一口气滚了一万三千多 px，光标被甩到视口上方一万多 px，看起来就是「直接顶没了」。
     * 这一段请求来自框架内部，和本文件里的跟随逻辑毫无关系，所以只改跟随逻辑是没用的。
     *
     * 做法：bringChildIntoView 里不自己滚（免得和框架抢、抖动），
     * 而是由 calculateRectForParent 把请求整个换成「光标那一行的小矩形」再交给父级 verticalScroll；
     * 框架自己那套最小滚动（而且视口已经被 imePadding 扣掉键盘）就会把它放到键盘上方两行。
     */
    val bivAbsorb = remember {
        object : BringIntoViewResponder {
            override fun calculateRectForParent(localRect: Rect): Rect =
                bivTargetRect.value?.invoke() ?: localRect
            override suspend fun bringChildIntoView(localRect: () -> Rect?) {
                // 故意什么都不做：滚动交给父级按上面那个小矩形去做
            }
        }
    }

    /*
     * 光标跟随：键盘弹出 / 光标移动后，把光标那一行顶到键盘上方，留两行富余。
     *
     * 老代码是：可视底部 = min(滚动框下沿, screenPx − imeNow)，目标值 = 光标窗口 Y − 安全线。
     * 它有两个叠加的毛病：
     *   ① partY 是用 boundsInRoot() 算的，而这个 API 会把结果裁剪到父级可见区域；
     *      正文框比视口高得多，裁剪后它的 top 恒等于视口顶部，
     *      于是 partY 恒等于 scroll.value —— 目标值里多了一份「当前滚动量」，
     *      每触发一次就凭空多顶十几行，而且是累加的。
     *   ② 见上面 bringIntoView 那段：框架会先把文章滚到底，这里再补一刀。
     * 现在 partY 改用不裁剪的真实位置（unclippedInRoot），可视下沿用 containerTop + viewport，
     * 于是目标滚动量 = 光标内容位置 − 可视高度 + 两行，是【绝对目标值】：
     * containerTop 和 scroll.value 都在等式里抵消掉了，不依赖「算的时候滚到哪儿了」，反复触发也不会累积。
     * 触发键带上 viewport：键盘弹出/收起、工具栏面板开合都会改变视口，必须跟着重新对齐一次。
     */
    LaunchedEffect(selAbs.start, activePart, imeOpen, viewport) {
        kotlinx.coroutines.delay(50)   // 等这一帧的布局 / onTextLayout 落定
        val vp = viewport.toFloat()
        if (vp <= 0f) return@LaunchedEffect
        val lr = layouts[activePart] ?: return@LaunchedEffect
        val part = parts.getOrNull(activePart) as? Part.Text ?: return@LaunchedEffect
        // 光标在该段里的局部偏移。用布局自己的文本长度夹一下：
        // 刚打完字时拿到的可能还是上一帧的旧布局，用它自己的长度才不会越界。
        val local = (selAbs.start - part.start).coerceIn(0, lr.layoutInput.text.length)
        val line = lr.getLineForOffset(local)
        val lineH = (lr.getLineBottom(line) - lr.getLineTop(line)).coerceAtLeast(1f)
        // 光标所在行的下沿，换算到「滚动内容坐标」（该值不随滚动变化）
        val caretContentY = (partY[activePart] ?: 0).toFloat() + lr.getLineBottom(line)
        // 要让这一行落在「距可视下沿两行」的位置上需要的滚动量
        val target = (caretContentY - vp + 2f * lineH)
            .coerceIn(0f, scroll.maxValue.toFloat())
        // 只处理「光标太低」这一种情况：往上顶（键盘收起、光标本来就在上面时什么都不做）
        if (target > scroll.value + 1f) {
            scroll.animateScrollTo(target.roundToInt())
        }
    }

    /** 光标所在的那一行（用于段落样式、以及未选中文字时的样式目标） */
    fun lineRangeOf(pos: Int): TextRange {
        val p = pos.coerceIn(0, body.length)
        val s = body.lastIndexOf('\n', (p - 1).coerceAtLeast(0)).let { if (it < 0) 0 else it + 1 }
        val e = body.indexOf('\n', p).let { if (it < 0) body.length else it }
        return TextRange(s, e)
    }

    /**
     * 应用一条样式：
     * · 现在有选中 → 作用于选区；
     * · 选区刚被工具栏点掉（失焦收起）→ 用记住的那段选区；
     * · 只有光标 → 作用于**光标所在整行**（立刻能看到变化），
     *   同时记成「待用样式」，接着输入的文字也自动带上。
     */
    fun applyStyleNamed(
        what: String,
        mutate: (DocSpan, Boolean) -> DocSpan,
        read: (DocSpan) -> Boolean
    ) {
        val selected = if (selAbs.max > selAbs.min) selAbs else lastRange
        val collapsed = selected == null || selected.max <= selected.min
        val target = if (collapsed) lineRangeOf(selAbs.start) else selected!!
        var done = false
        if (target.max > target.min && target.max <= body.length) {
            pushUndo()
            spans = SpanTool.apply(spans, target.min, target.max, mutate, read)
            done = true
        }
        if (collapsed) {
            val base = pending ?: DocSpan(0, 0)
            pending = mutate(base, !read(base))
        }
        status = if (done) {
            val line = body.take(target.min).count { it == '\n' } + 1
            if (collapsed) "$what · 第 $line 行（接着输入也沿用）" else "$what · 第 $line 行起"
        } else {
            "$what · 已设为接下来输入的样式"
        }
    }

    /** 兼容旧调用：不带名字的版本 */
    fun applyStyle(
        mutate: (DocSpan, Boolean) -> DocSpan,
        read: (DocSpan) -> Boolean
    ) {
        applyStyleNamed("样式", mutate, read)
    }

    fun toggle(attr: String, label: String = "") {
        val name = label.ifBlank {
            when (attr) {
                "bold" -> "加粗"
                "italic" -> "斜体"
                "underline" -> "下划线"
                else -> "删除线"
            }
        }
        when (attr) {
            "bold" -> applyStyleNamed(name, { d, on -> d.copy(bold = on) }, { it.bold })
            "italic" -> applyStyleNamed(name, { d, on -> d.copy(italic = on) }, { it.italic })
            "underline" -> applyStyleNamed(name, { d, on -> d.copy(underline = on) }, { it.underline })
            "strike" -> applyStyleNamed(name, { d, on -> d.copy(strike = on) }, { it.strike })
        }
    }

    /** 缩进：把当前行（或选中的每一行）的行首空白改成 n 个全角空格，同时记为默认缩进 */
    fun applyIndent(n: Int) {
        indent = n
        val r = if (selAbs.max > selAbs.min) selAbs else lineRangeOf(selAbs.start)
        val firstLine = body.lastIndexOf('\n', (r.min - 1).coerceAtLeast(0))
            .let { if (it < 0) 0 else it + 1 }
        val lastLine = body.indexOf('\n', r.max).let { if (it < 0) body.length else it }
        val block = body.substring(firstLine, lastLine)
        val pad = "\u3000".repeat(n)
        val newBlock = block.split("\n").joinToString("\n") { line ->
            pad + line.trimStart(' ', '\u3000')
        }
        val lineNo = body.take(firstLine).count { it == '\n' } + 1
        if (newBlock == block) {
            status = "缩进 · 第 $lineNo 行已是 $n 字"
            return
        }
        pushUndo()
        val oldBody = body
        body = oldBody.substring(0, firstLine) + newBlock + oldBody.substring(lastLine)
        spans = SpanTool.shift(spans, oldBody, body)
        selAbs = TextRange(firstLine + newBlock.length)
        status = "缩进 · 第 $lineNo 行设为 $n 字"
    }


    val atStyle = remember(spans, selAbs.start, body) {
        SpanTool.styleAt(spans, selAbs.start.coerceIn(0, body.length))
    }
    val cur = pending ?: atStyle

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(NightTop, NightBottom)))
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top))
            .imePadding()
            .swallowClicks()
    ) {
        // ---------- 顶栏：分类（左） ｜ AE（右） ----------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, top = 6.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .swallowClicks { onDismiss() },
                contentAlignment = Alignment.Center
            ) { Text("✕", color = TextMuted, fontSize = 15.sp) }
            Spacer(Modifier.width(4.dp))
            Text(
                "$typeName · $category",
                color = TextMuted, fontSize = 12.sp,
                maxLines = 1, overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(6.dp))
            Text(
                "AE " + String.format(Locale.CHINA, "%.1f", ae),
                color = PinkSoft, fontSize = 14.sp, fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.width(8.dp))

            // 保存这一篇。抽成函数是为了让「保存按钮」与「系统返回键」走同一条路径 ——
            // 否则按返回键时得把这段逻辑再抄一遍，早晚抄歪。
            fun save() {
                // 日期一旦定下就不再改：新写的属于今天；打开旧文档编辑，仍然记在原来那天，
                // 这样「改旧文章」永远不会回头改写已经冻结的那一天。
                val frozenDate = if (isNew) Dates.todayKey() else initial.dateKey
                onConfirm(
                    initial.copy(
                        title = title.trim(),
                        body = body,
                        images = images.toList(),
                        videos = videos.toList(),
                        spans = SpanTool.normalize(spans),
                        updated = System.currentTimeMillis(),
                        dateKey = frozenDate
                    )
                )
            }

            // 系统返回键 / 边缘返回手势 = 保存（与「保存」按钮同一个动作）。
            // 例外：新建、且标题与正文都还空着时按返回 = 取消 ——
            // 免得误触返回就凭空多出一篇空文章。
            BackHandler {
                if (isNew && title.isBlank() && body.isBlank()) onDismiss() else save()
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(PurpleDeep.copy(alpha = 0.5f))
                    .goldEdge(12.dp)
                    .clickable { save() }
                    .padding(horizontal = 12.dp, vertical = 7.dp)
            ) {
                Text("保存", color = TextMain, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            }
        }

        // ---------- 标题（无框）＋ 一条浅线 ＋ 创建时间/字数 ----------
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        ) {
            BasicTextField(
                value = title,
                onValueChange = { title = it },
                singleLine = true,
                textStyle = TextStyle(
                    color = TextMain, fontSize = 20.sp, fontWeight = FontWeight.Medium
                ),
                cursorBrush = SolidColor(PinkSoft),
                modifier = Modifier.fillMaxWidth()
            )
            if (title.isEmpty()) {
                Text("标题（可空）", color = TextFaint.copy(alpha = 0.6f), fontSize = 20.sp)
            }
        }
        Spacer(Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .height(1.dp)
                .background(PurpleMain.copy(alpha = 0.20f))
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "创建于 " + Dates.stamp(if (isNew) System.currentTimeMillis() else initial.created) +
                        "　·　$chars 字",
                color = TextFaint.copy(alpha = 0.75f), fontSize = 10.sp
            )
        }

        // ---------- 正文：文本段 / 图片 / 视频 交替 ----------
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .onSizeChanged { viewport = it.height }
                .onGloballyPositioned {
                    // 滚动内容时这个框自己不动，所以它在窗口里的顶部是稳定的
                    containerTop = it.unclippedInRoot().top.roundToInt()
                }
                .verticalScroll(scroll)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    // 把 BasicTextField 自己发出的「把光标滚进可见区」请求就地吃掉。
                    // 这个正文框有几百行、比视口高得多，框架那套默认对齐算法会按「整框」去算，
                    // 结果一点就滚到文章底部（实测滚了 1.4 万 px，光标被甩到视口上方 1.3 万 px）。
                    // 定位光标本就是我们下面那段跟随逻辑的活，这里直接不响应即可。
                    .bringIntoViewResponder(bivAbsorb)
                    .padding(horizontal = 12.dp)
            ) {
                parts.forEachIndexed { idx, part ->
                    key(idx, part.javaClass.simpleName) {
                        when (part) {
                            is Part.Text -> {
                                // 每一段各自记住自己的光标 —— 而且必须是**整份 TextFieldValue**，
                                // 不能只记 TextRange：输入法（英文键盘的联想 / 自动更正尤其明显）
                                // 会把「正在组合、还没上屏」的那一段放在 composition 里。
                                // 以前这里每次重组都重新构造 `TextFieldValue(part.text, sel)`，
                                // 新值的 composition 是 null，等于每次都把组合打断一次 ——
                                // 表现就是「打一两个字母之后，再怎么也输不进去」。
                                var localTv by remember {
                                    mutableStateOf(
                                        TextFieldValue(part.text, TextRange(part.text.length))
                                    )
                                }
                                // 撤销 / 套样式 / 回车自动缩进会从外面改这一段：同步回来，
                                // 顺便把越界的光标夹回范围内（composition 一并清掉）
                                LaunchedEffect(part.text) {
                                    if (localTv.text != part.text) {
                                        val n = part.text.length
                                        localTv = localTv.copy(
                                            text = part.text,
                                            selection = TextRange(
                                                localTv.selection.start.coerceIn(0, n),
                                                localTv.selection.end.coerceIn(0, n)
                                            ),
                                            composition = null
                                        )
                                    }
                                }
                                BasicTextField(
                                    value = localTv,
                                    onValueChange = { nv ->
                                        activePart = idx
                                        localTv = nv
                                        val oldLocal = part.text
                                        val newLocal = nv.text
                                        if (newLocal == oldLocal) {
                                            selAbs = TextRange(
                                                part.start + nv.selection.start,
                                                part.start + nv.selection.end
                                            )
                                            return@BasicTextField
                                        }
                                        pushUndo()
                                        var text = newLocal
                                        var localS = nv.selection
                                        // 回车自动缩进（开头也补）
                                        if (text.length == oldLocal.length + 1 &&
                                            localS.start > 0 && text[localS.start - 1] == '\n'
                                        ) {
                                            val ins = "\u3000".repeat(indent)
                                            text = text.substring(0, localS.start) + ins +
                                                    text.substring(localS.start)
                                            localS = TextRange(localS.start + ins.length)
                                        }
                                        val oldBody = body
                                        // ⚠️ 这里必须夹紧，不能直接信 part.start / oldLocal。
                                        //
                                        // 长按退格那种「一秒几十个删除」的连发下，两次 onValueChange
                                        // 有可能在**同一次重组之前**先后落地：第二次拿到的还是旧组合里的
                                        // part.start 与 oldLocal，而 body 已经被第一次改短了。
                                        // 于是 substring(part.start + oldLocal.length) 越界 ——
                                        // StringIndexOutOfBoundsException，直接闪退。
                                        // （闪退发生在保存之前，所以刚写的内容也就跟着没了。）
                                        val segStart = part.start.coerceIn(0, oldBody.length)
                                        val segEnd = (part.start + oldLocal.length)
                                            .coerceIn(segStart, oldBody.length)
                                        val newBody = oldBody.substring(0, segStart) + text +
                                                oldBody.substring(segEnd)
                                        // 样式平移要拿「盘上真正那一段」去比，不是拿陈旧的 oldLocal
                                        val realOld = oldBody.substring(segStart, segEnd)
                                        var shifted = SpanTool.shift(spans, oldBody, newBody)
                                        // 待用样式：把刚输入的内容补上样式
                                        val pd = pending
                                        if (pd != null) {
                                            var p = 0
                                            val mn = min(oldBody.length, newBody.length)
                                            while (p < mn && oldBody[p] == newBody[p]) p++
                                            var suf = 0
                                            while (suf < mn - p &&
                                                oldBody[oldBody.length - 1 - suf] ==
                                                newBody[newBody.length - 1 - suf]
                                            ) suf++
                                            val insStart = p
                                            val insEnd = newBody.length - suf
                                            if (insEnd > insStart) {
                                                shifted = SpanTool.apply(
                                                    shifted, insStart, insEnd,
                                                    { d, _ -> d.copy(
                                                        bold = pd.bold || d.bold,
                                                        italic = pd.italic || d.italic,
                                                        underline = pd.underline || d.underline,
                                                        strike = pd.strike || d.strike,
                                                        size = if (pd.size > 0f) pd.size else d.size,
                                                        color = if (pd.color != 0) pd.color else d.color,
                                                        bg = if (pd.bg != 0) pd.bg else d.bg,
                                                        font = if (pd.font != 0) pd.font else d.font
                                                    ) },
                                                    { false }
                                                )
                                            }
                                        }
                                        spans = shifted
                                        body = newBody
                                        selAbs = TextRange(
                                            part.start + localS.start,
                                            part.start + localS.end
                                        ).let { r ->
                                            // 光标同样夹回正文范围内 —— 越界的光标会在下一次
                                            // 取行/取词时把整页拖垮
                                            TextRange(
                                                r.start.coerceIn(0, newBody.length),
                                                r.end.coerceIn(0, newBody.length)
                                            )
                                        }
                                    },
                                    onTextLayout = { layouts[idx] = it },
                                    textStyle = TextStyle(
                                        color = TextMain, fontSize = 16.sp, lineHeight = 27.sp
                                    ),
                                    cursorBrush = SolidColor(PinkSoft),
                                    visualTransformation = RichTransform(spans, part.start, part.text.length),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .onGloballyPositioned {
                                            // 换算成「内容坐标」：减去滚动框顶部、再加回当前滚动量，
                                            // 得到的就是这一段在滚动内容里的位置 —— 与滚到哪儿无关。
                                            //
                                            // 这里必须用不裁剪的真实位置：boundsInRoot() 会把结果夹到
                                            // 各级父级的可见区域，正文框比视口高得多，夹完它的 top
                                            // 恒等于视口顶部，partY 就会恒等于 scroll.value，
                                            // 目标滚动量每触发一次就凭空多出十几行（而且是累加的）。
                                            partY[idx] = (
                                                it.unclippedInRoot().top - containerTop + scroll.value
                                                ).roundToInt()
                                        }
                                )
                                if (part.text.isEmpty() && body.isEmpty()) {
                                    Text(
                                        "想写多少写多少，长的短的都算。",
                                        color = TextFaint.copy(alpha = 0.55f), fontSize = 16.sp
                                    )
                                }
                            }
                            is Part.Image -> InlineImage(
                                path = part.path,
                                onOpen = {
                                    try {
                                        ctx.startActivity(
                                            Intent(Intent.ACTION_VIEW).apply {
                                                setDataAndType(
                                                    Uri.fromFile(File(part.path)), "image/*"
                                                )
                                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                            }
                                        )
                                    } catch (e: Exception) {
                                        Toast.makeText(ctx, "打不开这张图", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                onDelete = { removeAttachment(part.path, false) }
                            )
                            is Part.Video -> {
                                val vi = videos.indexOf(part.path)
                                InlineVideo(
                                    index = if (vi >= 0) vi + 1 else 1,
                                    onOpen = {
                                        val msg = Store.openVideo(ctx, File(part.path))
                                        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show()
                                    },
                                    onDelete = { removeAttachment(part.path, true) }
                                )
                            }
                        }
                    }
                }
                // 半个屏幕的富余：最后一行也能被顶到键盘上方（只是排版空间，不会被导出）。
                // 用 fullH（键盘弹起/收起都不变）的 55%，键盘弹出时这段留白才不会缩水。
                Spacer(Modifier.height(with(density) { fullH.toDp() } * 0.55f))
            }
        }

        // ---------- 分片面板 ----------
        if (panel.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 4.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(PanelBg.copy(alpha = 0.96f))
                    .goldEdge(14.dp)
                    .padding(10.dp)
            ) {
                when (panel) {
                    "size" -> Row(verticalAlignment = Alignment.CenterVertically) {
                        listOf(14f, 16f, 18f, 20f, 24f, 28f, 32f).forEach { size ->
                            ToolBtn("${size.toInt()}", cur.size == size) {
                                applyStyle({ d, on -> d.copy(size = if (on) size else 0f) },
                                    { it.size == size })
                            }
                        }
                    }
                    "color" -> {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            SpanColors.forEach { (c, name) ->
                                Box(
                                    modifier = Modifier
                                        .padding(3.dp)
                                        .size(26.dp)
                                        .clip(CircleShape)
                                        .background(if (c == 0) PanelBgSoft else Color(c))
                                        .border(
                                            1.dp,
                                            if (cur.color == c) GoldSoft
                                            else PurpleMain.copy(alpha = 0.3f),
                                            CircleShape
                                        )
                                        .clickable {
                                            applyStyle(
                                                { d, on -> d.copy(color = if (on) c else 0) },
                                                { it.color == c }
                                            )
                                        }
                                )
                            }
                        }
                        Spacer(Modifier.height(6.dp))
                        Text("文字颜色", color = TextFaint, fontSize = 9.sp)
                    }
                    "font" -> {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            SpanFonts.forEach { (f, name) ->
                                ToolBtn(name, cur.font == f) {
                                    applyStyle({ d, on -> d.copy(font = if (on) f else 0) },
                                        { it.font == f })
                                }
                            }
                        }
                        Spacer(Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            lingweiPresets().forEach { p ->
                                ToolBtn(p.name, active = false, tint = Color(p.color)) {
                                    applyStyle(
                                        { d, _ -> d.copy(
                                            color = p.color, font = p.font, bold = p.bold,
                                            italic = p.italic, bg = p.bg
                                        ) },
                                        { false }
                                    )
                                }
                            }
                        }
                    }
                    "align" -> Row(verticalAlignment = Alignment.CenterVertically) {
                        ToolBtn("左", cur.align == 0) {
                            applyStyleNamed("左对齐", { d, _ -> d.copy(align = 0) }, { true })
                        }
                        ToolBtn("中", cur.align == 1) {
                            applyStyleNamed("居中", { d, _ -> d.copy(align = 1) }, { false })
                        }
                        ToolBtn("右", cur.align == 2) {
                            applyStyleNamed("右对齐", { d, _ -> d.copy(align = 2) }, { false })
                        }
                        Spacer(Modifier.width(8.dp))
                        Text("对齐作用于光标所在行", color = TextFaint, fontSize = 9.sp)
                    }
                    "indent" -> {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            (0..4).forEach { n ->
                                ToolBtn("$n 字", indent == n) { applyIndent(n) }
                            }
                        }
                        Spacer(Modifier.height(6.dp))
                        Text(
                            "选中多行会一起缩进；这也是回车时的默认缩进",
                            color = TextFaint, fontSize = 9.sp
                        )
                    }
                }
            }
        }

        // ---------- 工具栏（键盘上方） ----------
        if (status.isNotBlank()) {
            Text(
                status,
                color = GoldSoft.copy(alpha = 0.85f), fontSize = 10.sp,
                modifier = Modifier.padding(start = 12.dp, bottom = 2.dp)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(PanelBg.copy(alpha = 0.9f))
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ToolBtn("↶") {
                val snap = undo.removeLastOrNull()
                if (snap != null) {
                    redo.addLast(snapshot())
                    body = snap.body
                    spans = snap.spans
                    selAbs = TextRange(snap.sel.start.coerceIn(0, body.length))
                    activePart = -1
                }
            }
            ToolBtn("↷") {
                val snap = redo.removeLastOrNull()
                if (snap != null) {
                    undo.addLast(snapshot())
                    body = snap.body
                    spans = snap.spans
                    selAbs = TextRange(snap.sel.start.coerceIn(0, body.length))
                    activePart = -1
                }
            }
            ToolBtn("B", cur.bold) { toggle("bold", "加粗") }
            ToolBtn("I", cur.italic) { toggle("italic", "斜体") }
            ToolBtn("U", cur.underline) { toggle("underline", "下划线") }
            ToolBtn("S", cur.strike) { toggle("strike", "删除线") }
            ToolBtn("Aa", panel == "size" || panel == "font") {
                panel = if (panel == "size" || panel == "font") "" else "size"
            }
            ToolBtn("色", panel == "color", tint = if (cur.color != 0) Color(cur.color) else TextMain) {
                panel = if (panel == "color") "" else "color"
            }
            ToolBtn("底纹", cur.bg != 0) {
                applyStyle(
                    { d, on -> d.copy(bg = if (on) 0x33F7D98C else 0) },
                    { it.bg != 0 }
                )
            }
            ToolBtn("对齐", panel == "align") {
                panel = if (panel == "align") "" else "align"
            }
            ToolBtn("缩进", panel == "indent") {
                panel = if (panel == "indent") "" else "indent"
            }
            ToolBtn("图片") { pickImage.launch("image/*") }
            ToolBtn("视频") { pickVideo.launch("video/*") }
        }
    }
}

/* ==========================================================================
 *  十·五、熵值页（点左上角花朵，或从熵记右滑进入；左滑返回）
 *        当日行动熵 + 月/年总览（折线 / 占比）
 * ======================================================================== */

@Composable
fun EntropyPage(
    ctx: Context,
    idx: Store.StatsIndex?,
    dayKey: String,
    onDayKeyChange: (String) -> Unit,
    refreshTick: Int,
    onRefresh: () -> Unit,
    /** 这一页真的在眼前时才让里面的动效走 */
    active: Boolean = true
) {
    var statMode by rememberSaveable { mutableIntStateOf(0) }   // 0=月 1=年
    var chartType by rememberSaveable { mutableIntStateOf(0) }  // 0=折线 1=占比
    var showRules by remember { mutableStateOf(false) }
    var showManual by remember { mutableStateOf(false) }
    var showCalendar by remember { mutableStateOf(false) }

    val traceAE = idx?.trace(dayKey) ?: 0.0
    val docAE = idx?.doc(dayKey) ?: 0.0
    val moodAE = idx?.mood(dayKey) ?: 0.0
    val nameCount = idx?.names(dayKey) ?: 0
    val aeTotal = idx?.day(dayKey) ?: 0.0

    val now = Dates.parse(Dates.todayKey())
    val year = now.get(Calendar.YEAR)
    val month = now.get(Calendar.MONTH) + 1
    // 已建好的索引里取数，纯内存计算，几乎零开销。
    // 键里带上年 / 月：跨月（或跨年）时索引没变，图表若不重算就还画着上个月那条线。
    val monthData = remember(idx, year, month) {
        if (idx == null) emptyList() else Store.monthStats(idx, year, month)
    }
    val yearData = remember(idx, year) {
        if (idx == null) emptyList() else Store.yearStats(idx, year)
    }

    Box(modifier = Modifier.fillMaxSize()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 12.dp)
    ) {
        // ---------- 日期 ----------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PillButton("‹") { onDayKeyChange(Dates.shift(dayKey, -1)) }
            Spacer(Modifier.width(8.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(PanelBg)
                    .goldEdge(14.dp)
                    .clickable { showCalendar = true }
                    .padding(vertical = 6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    Dates.cnOf(dayKey) + "  " + Dates.weekName(Dates.parse(dayKey)),
                    color = TextMain, fontSize = 15.sp, fontWeight = FontWeight.Medium
                )
                Text(
                    if (dayKey == Dates.todayKey()) "今天 · 点击选择其他日期" else "点击选择日期",
                    color = if (dayKey == Dates.todayKey()) PinkSoft else TextFaint,
                    fontSize = if (dayKey == Dates.todayKey()) 12.sp else 10.sp,
                    fontWeight = if (dayKey == Dates.todayKey()) FontWeight.Bold
                    else FontWeight.Normal
                )
            }
            Spacer(Modifier.width(8.dp))
            PillButton("›") { onDayKeyChange(Dates.shift(dayKey, 1)) }
        }

        Spacer(Modifier.height(6.dp))

        // ---------- 凌薇日加权 ----------
        Dates.lingweiDayName(dayKey)?.let { dn ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(ScratchGold.copy(alpha = 0.12f))
                    .border(1.dp, ScratchGold.copy(alpha = 0.50f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    Persona.data.dayWord + " · $dn", color = ScratchGold, fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.weight(1f))
                Text(
                    "当日行动熵整体 ×" +
                            String.format(Locale.CHINA, "%.2f", Persona.annivMul(dayKey)),
                    color = ScratchGold, fontSize = 11.sp
                )
            }
            Spacer(Modifier.height(6.dp))
        }

        // ---------- 计量规则 / 使用说明 / 刷新（刷新固定在最右） ----------
        Row(verticalAlignment = Alignment.CenterVertically) {
            Chip("📐  计量规则", false, Modifier.padding(end = 8.dp)) { showRules = true }
            Chip("📖  使用说明", false, Modifier.padding(end = 8.dp)) { showManual = true }
            Spacer(Modifier.weight(1f))
            Chip("⟳  刷新", false) { onRefresh() }
        }

        Spacer(Modifier.height(8.dp))

        // ---------- 当日熵值 ----------
        AEPanel(
            ae = aeTotal,
            traceAE = traceAE,
            docAE = docAE,
            moodAE = moodAE,
            nameCount = nameCount,
            nameFillSum = idx?.nameFill(dayKey) ?: 0.0,
            festivalMul = Dates.festivalMul(dayKey),
            steps = idx?.steps(dayKey) ?: 0L,
            stepsAE = idx?.stepsAE(dayKey) ?: 0.0,
            stepsOverMul = idx?.stepsOverMul(dayKey) ?: 1.0,
            gameLines = buildList {
                val g = idx?.game(dayKey) ?: Store.DayGame()
                if (g.scratchUsed > 0) {
                    add("刮刮乐 ${g.scratchUsed} 次")
                    addAll(Store.gameSummaryLines(g))   // 每条增益单独一行，避免挤在一起被折行
                }
            },
            active = active
        )

        Spacer(Modifier.height(14.dp))

        // ---------- 总览 ----------
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(PanelBg)
                .goldEdge(20.dp)
                .border(
                    1.dp, GoldenDay.border(PurpleMain.copy(alpha = 0.18f)),
                    RoundedCornerShape(20.dp)
                )
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("熵值总览", color = TextMain, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                Spacer(Modifier.weight(1f))
                Chip("月", statMode == 0, Modifier.padding(end = 6.dp)) { statMode = 0 }
                Chip("年", statMode == 1, Modifier.padding(end = 10.dp)) { statMode = 1 }
                Chip("折线", chartType == 0, Modifier.padding(end = 6.dp)) { chartType = 0 }
                Chip("占比", chartType == 1) { chartType = 1 }
            }
            Spacer(Modifier.height(10.dp))

            if (idx == null) {
                Text("正在统计…", color = TextFaint, fontSize = 12.sp)
            } else if (statMode == 0) {
                val total = monthData.sumOf { it.dayTotal }
                val traceSum = monthData.sumOf { it.trace }
                val docSum = monthData.sumOf { it.doc }
                val moodSum = monthData.sumOf { it.mood }
                val stepsAE = idx.stepsAEPrefix(String.format(Locale.CHINA, "%04d-%02d", year, month))
                Text(
                    "$year 年 $month 月 · 合计 " + String.format(Locale.CHINA, "%.1f", total) + " AE",
                    color = TextMuted, fontSize = 11.sp
                )
                Spacer(Modifier.height(8.dp))
                if (chartType == 0) {
                    LineChart(
                        points = monthData.map { it.unit.toFloat() to it.dayTotal.toFloat() },
                        xLabels = { v -> v.roundToInt().toString() },
                        highlightIndex = if (Dates.monthOf(Dates.todayKey()) == month)
                            Dates.parse(Dates.todayKey()).get(Calendar.DAY_OF_MONTH) - 1 else -1
                    )
                    Text(
                        "横轴：日期（1~${monthData.size}）　纵轴：当日行动熵（含联结加权）",
                        color = TextFaint, fontSize = 10.sp
                    )
                } else {
                    PieChart(
                        slices = listOf(
                            Triple("行迹", traceSum.toFloat(), CatTrace),
                            Triple("文章/日记", docSum.toFloat(), CatDiary),
                            Triple("心情", moodSum.toFloat(), GoldSoft),
                            Triple("运动", stepsAE.toFloat(), CatSteps)
                        )
                    )
                }
            } else {
                val total = yearData.sumOf { it.dayTotal }
                val traceSum = yearData.sumOf { it.trace }
                val docSum = yearData.sumOf { it.doc }
                val moodSum = yearData.sumOf { it.mood }
                val stepsAESum = idx.stepsAEPrefix(String.format(Locale.CHINA, "%04d-", year))
                Text(
                    "$year 年 · 合计 " + String.format(Locale.CHINA, "%.1f", total) + " AE",
                    color = TextMuted, fontSize = 11.sp
                )
                Spacer(Modifier.height(8.dp))
                if (chartType == 0) {
                    LineChart(
                        points = yearData.map { it.unit.toFloat() to it.dayTotal.toFloat() },
                        xLabels = { v -> "${v.roundToInt()}月" },
                        highlightIndex = if (Dates.yearOf(Dates.todayKey()) == year) month - 1 else -1
                    )
                    Text(
                        "横轴：月份（1~12）　纵轴：该月行动熵合计（含联结加权）",
                        color = TextFaint, fontSize = 10.sp
                    )
                } else {
                    PieChart(
                        slices = listOf(
                            Triple("行迹", traceSum.toFloat(), CatTrace),
                            Triple("文章/日记", docSum.toFloat(), CatDiary),
                            Triple("心情", moodSum.toFloat(), GoldSoft),
                            Triple("运动", stepsAESum.toFloat(), CatSteps)
                        )
                    )
                }
            }
        }

        Spacer(Modifier.height(10.dp))
        Text(
            "点击左上角花朵可返回　·　三栏之间可左右滑动切换",
            color = TextFaint, fontSize = 10.sp, textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(24.dp))
    }

    if (showRules) {
        AeRulesOverlay(onClose = { showRules = false })
    }

    if (showManual) {
        ManualOverlay(onClose = { showManual = false })
    }

    if (showCalendar) {
        CalendarOverlay(
            dayKey = dayKey,
            onPick = { onDayKeyChange(it); showCalendar = false },
            onClose = { showCalendar = false }
        )
    }
    }
}

/* ---------------------------- 运动页（熵记 · 第五栏） ---------------------------- */

@Composable
fun StepsPage(
    ctx: Context,
    idx: Store.StatsIndex?,
    dayKey: String,
    onDayKeyChange: (String) -> Unit,
    onDataChanged: () -> Unit,
    active: Boolean = true,
    modifier: Modifier = Modifier
) {
    val isToday = dayKey == Dates.todayKey()
    var cfg by remember { mutableStateOf(Steps.cfg(ctx)) }
    // 初值直接取已经在内存里的数据（索引 / 步数缓存），必要时才读一次盘：
    // 这样切进运动页时不会先闪一下"绿色的 0"再跳到真实步数。
    val initialSteps = remember(dayKey) {
        idx?.steps(dayKey)
            ?: Steps.knownTotal(dayKey)
            ?: Store.loadDayStepsTotal(ctx, dayKey)
    }
    var steps by remember(dayKey) { mutableLongStateOf(initialSteps) }
    var tick by remember { mutableIntStateOf(0) }
    var showCalendar by remember { mutableStateOf(false) }
    var manual by remember { mutableStateOf("") }
    var msg by remember { mutableStateOf("") }

    val hasPerm = Steps.hasPermission(ctx)
    val hasSensor = remember { Steps.sensorAvailable(ctx) }
    val canNotify = Build.VERSION.SDK_INT < 33 || ContextCompat.checkSelfPermission(
        ctx, Manifest.permission.POST_NOTIFICATIONS
    ) == PackageManager.PERMISSION_GRANTED

    val notifLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        // 授没授权都会走到这里：让页面重画一次，下面那行「通知：已允许 / 未允许」
        // 才会跟着变（canNotify 是在组合期直接查系统权限的）
        tick++
    }

    // 顺带申请通知权限，让「陪着你走」的常驻提示能显示出来
    // （canNotify 为 false 时必然已是 Android 13+，所以不必再判 SDK_INT）
    fun askNotify() {
        if (canNotify) return
        try {
            notifLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } catch (e: Exception) {
        }
    }

    val permLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            Steps.enable(ctx)
            msg = "已开启自动计步，关掉软件也会继续记录"
            askNotify()
        } else {
            msg = "没有「身体活动」权限，读不到系统计步，可以手动补记"
        }
        cfg = Steps.cfg(ctx)
        tick++
        onDataChanged()
    }

    // 读取当日步数
    LaunchedEffect(tick, dayKey) {
        steps = withContext(Dispatchers.IO) { Store.loadDayStepsTotal(ctx, dayKey) }
    }

    // 打开这一页时同步采样（只有今天才采样；历史日期是冻结的，只读）
    DisposableEffect(isToday, hasPerm) {
        var sm: SensorManager? = null
        var listener: SensorEventListener? = null
        if (isToday && hasPerm) {
            val m = ctx.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
            val sensor = m?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
            if (m != null && sensor != null) {
                val l = object : SensorEventListener {
                    override fun onSensorChanged(event: SensorEvent?) {
                        val e = event ?: return
                        if (e.sensor.type != Sensor.TYPE_STEP_COUNTER) return
                        steps = Steps.ingest(ctx, e.values[0].toLong())
                    }

                    override fun onAccuracyChanged(s: Sensor?, a: Int) {}
                }
                m.registerListener(l, sensor, SensorManager.SENSOR_DELAY_NORMAL)
                sm = m
                listener = l
            }
        }
        onDispose {
            try {
                listener?.let { sm?.unregisterListener(it) }
            } catch (e: Exception) {
            }
        }
    }

    // 后台服务在写文件时，定时同步一次显示（不在这一页时不用白读盘）
    LaunchedEffect(isToday, dayKey, active) {
        if (!active) return@LaunchedEffect
        while (true) {
            kotlinx.coroutines.delay(15000)
            if (isToday) {
                val v = withContext(Dispatchers.IO) { Store.loadDayStepsTotal(ctx, dayKey) }
                if (v != steps) steps = v
            }
        }
    }

    // 单项增益（达成目标 ×1.2）在运动页结算；整体增益（超出上限 ×1.5）在熵值页体现
    val stepsAE = if (isToday) AE.stepsAE(steps, cfg.goal)
    else (idx?.stepsAE(dayKey) ?: AE.stepsAE(steps, cfg.goal))
    val km = AE.stepsToKm(steps, cfg.strideCm)
    val kcal = AE.stepsToKcal(steps, cfg.weightKg)
    val goalDone = steps >= cfg.goal
    val overDone = steps > AE.STEPS_CAP

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 12.dp)
        ) {
            // ---------- 日期 ----------
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PillButton("‹") { onDayKeyChange(Dates.shift(dayKey, -1)) }
                Spacer(Modifier.width(8.dp))
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(PanelBg)
                        .goldEdge(14.dp)
                        .clickable { showCalendar = true }
                        .padding(vertical = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        Dates.cnOf(dayKey) + "  " + Dates.weekName(Dates.parse(dayKey)),
                        color = TextMain, fontSize = 15.sp, fontWeight = FontWeight.Medium
                    )
                    Text(
                        if (isToday) "今天 · 实时记录中" else "该日步数已冻结",
                        color = if (isToday) CatSteps else TextFaint,
                        fontSize = if (isToday) 12.sp else 10.sp,
                        fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
                    )
                }
                Spacer(Modifier.width(8.dp))
                PillButton("›") { onDayKeyChange(Dates.shift(dayKey, 1)) }
            }

            Spacer(Modifier.height(8.dp))

            // ---------- 步数主面板 ----------
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Brush.verticalGradient(listOf(PanelBgSoft, PanelBg)))
                    .goldEdge(20.dp)
                    .border(
                        1.dp, GoldenDay.border(CatSteps.copy(alpha = 0.22f)),
                        RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                StepRing(steps = steps, goal = cfg.goal, active = active)

                Spacer(Modifier.height(12.dp))
                Text(
                    if (overDone) "两环已满 · 超出部分继续记录"
                    else if (goalDone) "今日目标已达成 ✓"
                    else "还差 ${cfg.goal - steps} 步达成今日目标",
                    color = if (overDone) ScratchGold else if (goalDone) stepsColorAt(1f)
                    else TextMuted,
                    fontSize = 12.sp,
                    fontWeight = if (goalDone) FontWeight.Bold else FontWeight.Normal
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "每日建议 6000~10000 步（约 " +
                            String.format(Locale.CHINA, "%.1f", AE.stepsToKm(6000L, cfg.strideCm)) +
                            " ~ " +
                            String.format(Locale.CHINA, "%.1f", AE.stepsToKm(10000L, cfg.strideCm)) +
                            " 公里）",
                    color = TextFaint, fontSize = 10.sp, textAlign = TextAlign.Center
                )
                Text(
                    "你的目标 ${cfg.goal} 步　｜　上限 ${AE.STEPS_CAP} 步（基础环 + 溢出环）",
                    color = TextFaint, fontSize = 10.sp, textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    StepStat(
                        "距离", String.format(Locale.CHINA, "%.2f", km), "公里", CatSteps,
                        Modifier.weight(1f)
                    )
                    StepStat(
                        "消耗", String.format(Locale.CHINA, "%.0f", kcal), "千卡", GoldSoft,
                        Modifier.weight(1f)
                    )
                    StepStat(
                        "运动熵", String.format(Locale.CHINA, "%.1f", stepsAE), "AE", PinkSoft,
                        Modifier.weight(1f)
                    )
                }

                // 把这天的运动分享给她：会在对话页里作为一条发言发出去
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 2.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    SmallAction("把这天的运动分享给" + Persona.pro(), PinkSoft) {
                        AiShare.request(
                            label = "运动 · " + dayKey,
                            body = shareBodyOf(
                                kind = "运动",
                                title = dayKey,
                                lines = listOf(
                                    "步数：" + steps + " 步（目标 " + cfg.goal + "）",
                                    "距离：" + String.format(Locale.CHINA, "%.2f", km) + " 公里",
                                    "消耗：" + String.format(Locale.CHINA, "%.0f", kcal) + " 千卡",
                                    "运动熵：" + String.format(Locale.CHINA, "%.1f", stepsAE)
                                )
                            )
                        )
                    }
                }

                // ---------- 两项奖励的结算（单项增益只在这里出现） ----------
                Spacer(Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(PanelBgSoft.copy(alpha = 0.6f))
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                ) {
                    Column {
                        Text(
                            "目标奖励：" +
                                    (if (goalDone) "已达成，本次运动熵 ×" +
                                            String.format(Locale.CHINA, "%.1f", AE.STEPS_GOAL_MUL)
                                    else "达成 ${cfg.goal} 步后，运动熵 ×" +
                                            String.format(Locale.CHINA, "%.1f", AE.STEPS_GOAL_MUL)),
                            color = if (goalDone) stepsColorAt(1f) else TextFaint,
                            fontSize = 11.sp
                        )
                        Spacer(Modifier.height(3.dp))
                        Text(
                            "溢出奖励：" +
                                    (if (overDone) "已触发，当日行动熵整体 ×" +
                                            String.format(Locale.CHINA, "%.1f", AE.STEPS_OVER_MUL)
                                    else "超过 ${AE.STEPS_CAP} 步后，当日行动熵整体 ×" +
                                            String.format(Locale.CHINA, "%.1f", AE.STEPS_OVER_MUL)),
                            color = if (overDone) ScratchGold else TextFaint,
                            fontSize = 11.sp
                        )
                        Spacer(Modifier.height(3.dp))
                        Text(
                            if (overDone) "${AE.STEPS_CAP} 步之后运动熵已封顶，步数仍可无限记录。"
                            else "运动熵在 ${AE.STEPS_CAP} 步封顶，步数本身没有上限。",
                            color = TextFaint, fontSize = 10.sp
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // ---------- 近 7 天 ----------
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(PanelBg)
                    .goldEdge(18.dp)
                    .padding(11.dp)
            ) {
                Text("近 7 天步数", color = TextMain, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                Spacer(Modifier.height(10.dp))
                val week = (6 downTo 0).map { i ->
                    val k = Dates.shift(dayKey, -i)
                    Triple(k, (if (k == Dates.todayKey()) steps else (idx?.steps(k) ?: 0L)), i == 0)
                }
                val maxV = week.maxOfOrNull { it.second }?.coerceAtLeast(1L) ?: 1L
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    week.forEach { (k, v, isLast) ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                if (v >= 10000) String.format(Locale.CHINA, "%.1f万", v / 10000.0)
                                else v.toString(),
                                color = if (isLast) CatSteps else TextFaint, fontSize = 9.sp
                            )
                            Spacer(Modifier.height(3.dp))
                            Box(
                                modifier = Modifier
                                    .width(18.dp)
                                    .height((6 + 52 * (v.toFloat() / maxV)).dp)
                                    .clip(RoundedCornerShape(5.dp))
                                    .background(
                                        if (isLast) Brush.verticalGradient(listOf(CatSteps, PinkSoft))
                                        else Brush.verticalGradient(
                                            listOf(
                                                CatSteps.copy(alpha = 0.55f),
                                                CatSteps.copy(alpha = 0.25f)
                                            )
                                        )
                                    )
                            )
                            Spacer(Modifier.height(3.dp))
                            Text(
                                Dates.shift(k, 0).substring(5), color = TextFaint, fontSize = 8.sp
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // ---------- 自动记录 ----------
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(PanelBg)
                    .goldEdge(18.dp)
                    .padding(11.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("自动记录", color = TextMain, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    Spacer(Modifier.weight(1f))
                    Text(
                        if (cfg.enabled) "已开启" else "未开启",
                        color = if (cfg.enabled) CatSteps else TextFaint, fontSize = 11.sp
                    )
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    "· 传感器：" + (if (hasSensor) "有（系统计步器）" else "没有（只能手动补记）"),
                    color = TextFaint, fontSize = 11.sp
                )
                Text(
                    "· 权限：" + (if (hasPerm) "已授权「身体活动」" else "未授权"),
                    color = TextFaint, fontSize = 11.sp
                )
                Text(
                    "· 常驻记录：" + (if (Steps.serviceRunning) "运行中（关掉软件也在记）" else "未运行"),
                    color = TextFaint, fontSize = 11.sp
                )
                Text(
                    "· 通知：" + (if (canNotify) "已允许（会显示「陪着你走」）"
                    else "未允许（不影响记录，只是看不到提示）"),
                    color = TextFaint, fontSize = 11.sp
                )
                Spacer(Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (cfg.enabled) PurpleDeep.copy(alpha = 0.35f)
                            else PurpleDeep.copy(alpha = 0.5f))
                            .goldEdge(12.dp)
                            .clickable {
                                if (!hasSensor) {
                                    msg = "这台手机没有计步传感器，只能手动记录"
                                } else if (!hasPerm) {
                                    permLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
                                } else if (cfg.enabled) {
                                    Steps.disable(ctx)
                                    cfg = Steps.cfg(ctx)
                                    msg = "已关闭自动计步"
                                } else {
                                    Steps.enable(ctx)
                                    cfg = Steps.cfg(ctx)
                                    msg = "已开启自动计步，关掉软件也会继续记录"
                                    askNotify()
                                }
                                tick++
                                // 开 / 关都会影响别处显示的运动项，通知一次
                                onDataChanged()
                            }
                            .padding(horizontal = 12.dp, vertical = 9.dp)
                    ) {
                        Text(
                            if (cfg.enabled) "关闭自动记录" else "开启自动记录",
                            color = TextMain, fontSize = 13.sp
                        )
                    }
                    if (!cfg.enabled && hasSensor && !hasPerm) {
                        Spacer(Modifier.width(8.dp))
                        Text("需要「身体活动」权限", color = TextFaint, fontSize = 10.sp)
                    }
                }
                if (msg.isNotBlank()) {
                    Spacer(Modifier.height(8.dp))
                    Text(msg, color = PinkSoft, fontSize = 11.sp)
                }
            }

            Spacer(Modifier.height(12.dp))

            // ---------- 目标与体重 ----------
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(PanelBg)
                    .goldEdge(18.dp)
                    .padding(11.dp)
            ) {
                Text("每日目标", color = TextMain, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                Spacer(Modifier.height(8.dp))
                StepStepper(
                    label = "目标步数", value = "${cfg.goal} 步",
                    onMinus = {
                        cfg = cfg.copy(goal = (cfg.goal - 1000).coerceAtLeast(2000))
                        Steps.saveCfg(ctx, cfg)
                        // 目标步数写进了当日运动熵与索引，别处（花房的公里/千卡、熵值页）
                        // 用的还是按旧目标算出来的数 —— 改完就通知一次
                        onDataChanged()
                    },
                    onPlus = {
                        cfg = cfg.copy(goal = (cfg.goal + 1000).coerceAtMost(AE.STEPS_CAP.toInt()))
                        Steps.saveCfg(ctx, cfg)
                        onDataChanged()
                    }
                )
                Spacer(Modifier.height(6.dp))
                StepStepper(
                    label = "体重（估卡路里）", value = "${cfg.weightKg} kg",
                    onMinus = {
                        cfg = cfg.copy(weightKg = (cfg.weightKg - 5).coerceAtLeast(25))
                        Steps.saveCfg(ctx, cfg)
                        onDataChanged()
                    },
                    onPlus = {
                        cfg = cfg.copy(weightKg = (cfg.weightKg + 5).coerceAtMost(200))
                        Steps.saveCfg(ctx, cfg)
                        onDataChanged()
                    }
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    "步幅按 ${cfg.strideCm} 厘米估算；目标步数最高可设到 ${AE.STEPS_CAP} 步" +
                            "（基础环 + 溢出环的合计上限）。",
                    color = TextFaint, fontSize = 10.sp
                )
            }

            Spacer(Modifier.height(12.dp))

            // ---------- 手动补记 ----------
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(PanelBg)
                    .goldEdge(18.dp)
                    .padding(11.dp)
            ) {
                Text("手动补记", color = TextMain, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                Spacer(Modifier.height(4.dp))
                Text(
                    if (isToday) "没有计步传感器、或者想补上没带到手机的步数，都可以填在这里。"
                    else "这一天已经冻结：补记不会改变当天的行动熵。",
                    color = TextFaint, fontSize = 10.sp
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = manual,
                    onValueChange = { manual = it.filter { c -> c.isDigit() }.take(6) },
                    label = { Text("步数", fontSize = 12.sp) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(PurpleDeep.copy(alpha = 0.5f))
                            .goldEdge(12.dp)
                            .clickable {
                                val v = manual.toLongOrNull() ?: 0L
                                if (v > 0L) {
                                    Steps.addManual(ctx, dayKey, v)
                                    manual = ""
                                    tick++
                                    onDataChanged()
                                    msg = "已加上 $v 步"
                                }
                            }
                            .padding(horizontal = 12.dp, vertical = 9.dp)
                    ) {
                        Text("＋ 加上", color = TextMain, fontSize = 13.sp)
                    }
                    Spacer(Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(PanelBgSoft)
                            .goldEdge(12.dp)
                            .clickable {
                                val v = manual.toLongOrNull() ?: 0L
                                Steps.setManual(ctx, dayKey, v)
                                manual = ""
                                tick++
                                onDataChanged()
                                msg = "已把当日步数设为 $v 步"
                            }
                            .padding(horizontal = 12.dp, vertical = 9.dp)
                    ) {
                        Text("＝ 设为", color = TextMuted, fontSize = 13.sp)
                    }
                    Spacer(Modifier.weight(1f))
                    Text("当前 $steps 步", color = TextFaint, fontSize = 10.sp)
                }
            }

            Spacer(Modifier.height(24.dp))
        }

        if (showCalendar) {
            CalendarOverlay(
                dayKey = dayKey,
                onPick = { onDayKeyChange(it); showCalendar = false },
                onClose = { showCalendar = false }
            )
        }
    }
}

/** 步数颜色阶梯：随着步数增长，青绿 → 绿 → 黄绿 → 金 */
val StepsRamp = listOf(
    Color(0xFF6FE0CF),   // 0%   青绿
    Color(0xFF7FD9A6),   // 35%  绿
    Color(0xFFA9DE84),   // 70%  黄绿
    Color(0xFFE7D97C)    // 100% 暖金
)

/** 按 0~1 的进度在阶梯上取色（用于步数环与步数数字） */
fun stepsColorAt(ratio: Float): Color {
    val r = ratio.coerceIn(0f, 1f) * (StepsRamp.size - 1)
    val i = r.toInt().coerceAtMost(StepsRamp.size - 2)
    val f = r - i
    val a = StepsRamp[i]
    val b = StepsRamp[i + 1]
    return Color(
        red = a.red + (b.red - a.red) * f,
        green = a.green + (b.green - a.green) * f,
        blue = a.blue + (b.blue - a.blue) * f,
        alpha = 1f
    )
}

/**
 * 步数双环（与行动熵显示页同构）：
 *   内环（细）＝ 自设目标   0 → goal
 *   外环（粗）＝ 溢出       goal → 27000，金色渐变
 * 颜色随步数增长由青绿渐变到暖金；两环填满后不再变化，但步数仍继续记录。
 *
 * 环内文字：外环 0.13w + 间隙 0.045w + 内环 0.075w，两侧各占一份，
 * 于是环内空白只有 0.5w。现在圆盘里只放数值（「步」字移到圆盘下方居中），
 * 所以字号可以吃满环内空白：按「位数 × 0.80em（真实数字约 0.62em，按更宽估）」
 * 反推，再留 10% 边距；字号还要除以系统字体缩放，避免调大系统字体后撑出圆盘。
 */
@Composable
fun StepRing(
    steps: Long,
    goal: Int,
    cap: Long = AE.STEPS_CAP,
    size: androidx.compose.ui.unit.Dp = 190.dp,
    active: Boolean = true
) {
    val g = goal.coerceIn(1, cap.toInt()).toLong()
    val overSpan = (cap - g).coerceAtLeast(1L)
    val baseRatio = (steps.toFloat() / g).coerceIn(0f, 1f)
    val overRatio =
        if (steps <= g) 0f else ((steps - g).toFloat() / overSpan).coerceIn(0f, 1f)
    val color = stepsColorAt(baseRatio)
    val full = steps >= cap

    val fontScale = LocalDensity.current.fontScale
    val digits = steps.toString().length
    val clearDia = size.value * 0.5f              // 环内空白直径 = 0.5w
    val usable = clearDia * 0.90f                 // 环内不再放「步」字，只留 10% 边距
    val numDp = (usable / (digits * 0.80f)).coerceIn(14f, 40f)
    val numSp = numDp / fontScale
    // 「步」字：固定 20dp 一档（不再随位数一路缩小），只保证不超过数值的 88%
    val unitDp = 20f.coerceAtMost(numDp * 0.88f)
    val unitSp = unitDp / fontScale
    // 填涂上缓缓推进的白色高光（绘制阶段才读值，不触发重组）；不在这一页时停掉
    val sweepPhase: State<Float> = if (active) {
        rememberInfiniteTransition(label = "stepSweep").animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(4600, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "stepSweepPhase"
        )
    } else {
        remember { mutableFloatStateOf(0f) }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.size(size), contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = this.size.width
                val h = this.size.height
                val startAngle = 135f
                val sweepMax = 270f
                val phase = sweepPhase.value

                // ---- 外环（粗，溢出层，金）----
                val outerStroke = w * 0.13f
                val outerInset = outerStroke / 2f
                val outerSize = Size(w - outerStroke, h - outerStroke)
                drawArc(
                    color = PanelBgSoft.copy(alpha = 0.55f),
                    startAngle = startAngle, sweepAngle = sweepMax, useCenter = false,
                    topLeft = Offset(outerInset, outerInset), size = outerSize,
                    style = Stroke(width = outerStroke, cap = StrokeCap.Round)
                )
                if (overRatio > 0f) {
                    // 首尾同色，避免 sweep 渐变在 3 点钟方向出现接缝
                    drawArc(
                        brush = Brush.sweepGradient(
                            listOf(WarmLight, ScratchGold, GoldDeep, WarmLight)
                        ),
                        startAngle = startAngle, sweepAngle = sweepMax * overRatio,
                        useCenter = false,
                        topLeft = Offset(outerInset, outerInset), size = outerSize,
                        style = Stroke(width = outerStroke, cap = StrokeCap.Round)
                    )
                    sweepHighlight(
                        startAngle = startAngle, sweep = sweepMax * overRatio,
                        topLeft = Offset(outerInset, outerInset), size = outerSize,
                        strokeWidth = outerStroke, phase = phase
                    )
                }

                // ---- 内环（细，目标层，随步数渐变）----
                val innerStroke = w * 0.075f
                val innerPad = outerStroke + w * 0.045f
                val innerInset = innerPad + innerStroke / 2f
                val innerSize = Size(w - innerInset * 2f, h - innerInset * 2f)
                drawArc(
                    color = PanelBgSoft,
                    startAngle = startAngle, sweepAngle = sweepMax, useCenter = false,
                    topLeft = Offset(innerInset, innerInset), size = innerSize,
                    style = Stroke(width = innerStroke, cap = StrokeCap.Round)
                )
                if (baseRatio > 0f) {
                    // 用线性渐变而非 sweep：sweep 的起点接缝正好落在 3 点钟，
                    // 之前会在右半环上切出一道暗影，这里彻底避开。
                    drawArc(
                        brush = Brush.linearGradient(
                            listOf(
                                color.copy(alpha = 0.70f),
                                color,
                                GardeniaWhite.copy(alpha = 0.88f)
                            ),
                            start = Offset(innerInset, innerInset + innerSize.height),
                            end = Offset(innerInset + innerSize.width, innerInset)
                        ),
                        startAngle = startAngle, sweepAngle = sweepMax * baseRatio,
                        useCenter = false,
                        topLeft = Offset(innerInset, innerInset), size = innerSize,
                        style = Stroke(width = innerStroke, cap = StrokeCap.Round)
                    )
                    sweepHighlight(
                        startAngle = startAngle, sweep = sweepMax * baseRatio,
                        topLeft = Offset(innerInset, innerInset), size = innerSize,
                        strokeWidth = innerStroke, phase = phase
                    )
                }

                // ---- 中心光晕 ----
                drawCircle(
                    color = (if (full) ScratchGold else color).copy(alpha = 0.14f),
                    radius = w * 0.24f, center = Offset(w / 2f, h / 2f)
                )
            }
            Text(
                steps.toString(),
                color = if (full) ScratchGold else color,
                fontSize = numSp.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                softWrap = false,
                textAlign = TextAlign.Center,
                modifier = Modifier.widthIn(max = (clearDia * 0.94f).dp)
            )
            // 「步」字放进圆盘内部、落在下方那道 90° 缺口里：
            // 环的两端最低只到 45°/135°（约 0.76×直径处），
            // 所以这里既是空的、又紧贴圆盘，不会像放在盘外那样显得很远。
            Text(
                "步",
                color = (if (full) ScratchGold else color).copy(alpha = 0.88f),
                fontSize = unitSp.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = -(size.value * 0.125f).dp)
            )
        }
    }
}

/** 运动页里的小统计块 */
@Composable
fun StepStat(
    label: String,
    value: String,
    unit: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(label, color = TextFaint, fontSize = 10.sp)
        Spacer(Modifier.height(3.dp))
        Text(value, color = color, fontSize = 19.sp, fontWeight = FontWeight.Bold)
        Text(unit, color = TextFaint, fontSize = 10.sp)
    }
}

/** 「−  标签 数值  ＋」一行 */
@Composable
fun StepStepper(label: String, value: String, onMinus: () -> Unit, onPlus: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(label, color = TextMuted, fontSize = 12.sp)
        Spacer(Modifier.weight(1f))
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(PanelBgSoft)
                .goldEdge(14.dp)
                .clickable { onMinus() },
            contentAlignment = Alignment.Center
        ) { Text("−", color = TextMain, fontSize = 15.sp) }
        Text(
            value, color = TextMain, fontSize = 13.sp,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(PanelBgSoft)
                .goldEdge(14.dp)
                .clickable { onPlus() },
            contentAlignment = Alignment.Center
        ) { Text("＋", color = TextMain, fontSize = 15.sp) }
    }
}

/* ==========================================================================
 *  十一·五、番茄时钟页面（全屏，无顶部标题与底部栏；自动适配横竖屏）
 * ======================================================================== */

/** 番茄页里的小圆按钮 */
@Composable
fun PomoButton(
    size: Dp = 58.dp,
    enabled: Boolean = true,
    border: Color = PurpleMain.copy(alpha = 0.55f),
    fill: Color = PanelBgSoft,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(if (enabled) fill else fill.copy(alpha = 0.28f))
            .border(1.5.dp, if (enabled) border else border.copy(alpha = 0.22f), CircleShape)
            .clickable(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center
    ) { content() }
}

/** ＋ ：添加本次要记录的事情 */
@Composable
fun IconPlus(color: Color) {
    Canvas(modifier = Modifier.size(24.dp)) {
        val w = size.width
        val t = w * 0.13f
        drawRoundRect(
            color = color,
            topLeft = Offset(w * 0.5f - t / 2f, w * 0.18f),
            size = Size(t, w * 0.64f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(t / 2f)
        )
        drawRoundRect(
            color = color,
            topLeft = Offset(w * 0.18f, w * 0.5f - t / 2f),
            size = Size(w * 0.64f, t),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(t / 2f)
        )
    }
}

/** ▶ 开始/继续 与 Ⅱ 暂停 */
@Composable
fun IconPlayPause(running: Boolean, color: Color) {
    Canvas(modifier = Modifier.size(24.dp)) {
        val w = size.width
        if (running) {
            val barW = w * 0.20f
            drawRoundRect(
                color = color, topLeft = Offset(w * 0.24f, w * 0.18f),
                size = Size(barW, w * 0.64f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(barW * 0.35f)
            )
            drawRoundRect(
                color = color, topLeft = Offset(w * 0.56f, w * 0.18f),
                size = Size(barW, w * 0.64f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(barW * 0.35f)
            )
        } else {
            val p = Path().apply {
                moveTo(w * 0.30f, w * 0.18f)
                lineTo(w * 0.82f, w * 0.50f)
                lineTo(w * 0.30f, w * 0.82f)
                close()
            }
            drawPath(p, color)
        }
    }
}

/** ■ 完成 与 ✕ 放弃 */
@Composable
fun IconSquare(color: Color) {
    Box(
        modifier = Modifier
            .size(16.dp)
            .clip(RoundedCornerShape(3.dp))
            .background(color)
    )
}

@Composable
fun IconCross(color: Color) {
    Canvas(modifier = Modifier.size(22.dp)) {
        val w = size.width
        val t = w * 0.15f
        rotate(45f) {
            drawRoundRect(
                color = color, topLeft = Offset(w * 0.5f - t / 2f, w * 0.14f),
                size = Size(t, w * 0.72f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(t / 2f)
            )
        }
        rotate(-45f) {
            drawRoundRect(
                color = color, topLeft = Offset(w * 0.5f - t / 2f, w * 0.14f),
                size = Size(t, w * 0.72f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(t / 2f)
            )
        }
    }
}

/** 🍅 番茄循环模式（自己画的番茄） */
@Composable
fun IconTomato(active: Boolean) {
    val body = if (active) Color(0xFFE4674B) else TextMuted
    val leaf = if (active) Color(0xFF7FC77F) else TextFaint
    Canvas(modifier = Modifier.size(26.dp)) {
        val w = size.width
        drawCircle(color = body, radius = w * 0.33f, center = Offset(w * 0.5f, w * 0.58f))
        // 叶子：三片小三角
        for (k in -1..1) {
            val p = Path().apply {
                moveTo(w * 0.5f, w * 0.30f)
                lineTo(w * (0.5f + k * 0.26f), w * (0.20f + kotlin.math.abs(k) * 0.06f))
                lineTo(w * (0.5f + k * 0.10f), w * 0.34f)
                close()
            }
            drawPath(p, leaf)
        }
        drawCircle(color = leaf, radius = w * 0.05f, center = Offset(w * 0.5f, w * 0.28f))
    }
}

/** ⚙ 设置（画成三根滑杆，避免依赖字体里的齿轮字形） */
@Composable
fun IconSliders(color: Color) {
    Canvas(modifier = Modifier.size(24.dp)) {
        val w = size.width
        val t = w * 0.10f
        for (i in 0 until 3) {
            val y = w * (0.28f + i * 0.22f)
            drawRoundRect(
                color = color.copy(alpha = 0.75f),
                topLeft = Offset(w * 0.14f, y - t / 2f), size = Size(w * 0.72f, t),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(t / 2f)
            )
            val kx = w * (0.32f + i * 0.18f)
            drawCircle(color = color, radius = w * 0.09f, center = Offset(kx, y))
        }
    }
}

/**
 * 时 / 分 / 秒 三个可上下拖动的轮子。
 * 每拖动约 14dp 改变一格，向下拖数值增加（和手机计时器一致）。
 */
@Composable
fun TimeWheel(
    value: Int,
    range: Int,
    enabled: Boolean,
    color: Color,
    textSize: TextUnit,
    onChange: (Int) -> Unit
) {
    val cur by rememberUpdatedState(value)
    val acc = remember { mutableFloatStateOf(0f) }
    val drag = if (enabled) {
        Modifier.pointerInput(Unit) {
            val step = 14.dp.toPx()
            detectVerticalDragGestures(
                onDragStart = { acc.floatValue = 0f },
                onDragEnd = { acc.floatValue = 0f },
                onDragCancel = { acc.floatValue = 0f }
            ) { _, delta ->
                acc.floatValue += delta
                while (acc.floatValue <= -step) {
                    acc.floatValue += step
                    onChange((cur + 1) % range)
                }
                while (acc.floatValue >= step) {
                    acc.floatValue -= step
                    onChange((cur - 1 + range) % range)
                }
            }
        }
    } else Modifier

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (enabled) {
            Canvas(modifier = Modifier.size(width = 14.dp, height = 7.dp)) {
                val p = Path().apply {
                    moveTo(0f, size.height)
                    lineTo(size.width / 2f, 0f)
                    lineTo(size.width, size.height)
                    close()
                }
                drawPath(p, color.copy(alpha = 0.55f))
            }
            Spacer(Modifier.height(2.dp))
        }
        Text(
            String.format(Locale.CHINA, "%02d", value),
            color = color,
            fontSize = textSize,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .then(drag)
                .padding(horizontal = 2.dp, vertical = 1.dp)
        )
        if (enabled) {
            Spacer(Modifier.height(2.dp))
            Canvas(modifier = Modifier.size(width = 14.dp, height = 7.dp)) {
                val p = Path().apply {
                    moveTo(0f, 0f)
                    lineTo(size.width / 2f, size.height)
                    lineTo(size.width, 0f)
                    close()
                }
                drawPath(p, color.copy(alpha = 0.55f))
            }
        }
    }
}

/**
 * 番茄参数用的步进按钮：
 * · 单击 → 增减 1；
 * · 按住不放 → 先按 5 跳一次，之后每 110ms 再按 5 连续增减，松手即停。
 */
fun Modifier.stepperPress(onStep: (Int) -> Unit): Modifier = this.pointerInput(onStep) {
    awaitPointerEventScope {
        while (true) {
            val down = awaitFirstDown(requireUnconsumed = false)
            val startAt = System.currentTimeMillis()
            var accelerated = false
            var lastStep = startAt
            while (true) {
                val event = withTimeoutOrNull(20L) { awaitPointerEvent() }
                val change = event?.changes?.firstOrNull { it.id == down.id }
                if (event != null && (change == null || !change.pressed)) break
                val now = System.currentTimeMillis()
                if (!accelerated && now - startAt >= 340L) {
                    accelerated = true
                    lastStep = now
                    onStep(5)
                } else if (accelerated && now - lastStep >= 110L) {
                    lastStep = now
                    onStep(5)
                }
            }
            if (!accelerated) onStep(1)
        }
    }
}

/** 番茄参数的一行：− 标签 数值 ＋ */
@Composable
fun PomoStepper(
    label: String,
    value: String,
    minusEnabled: Boolean = true,
    plusEnabled: Boolean = true,
    onStep: (Int) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(label, color = TextMuted, fontSize = 12.sp)
        Spacer(Modifier.weight(1f))
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .background(PanelBgSoft)
                .goldEdge(15.dp)
                .then(if (minusEnabled) Modifier.stepperPress { onStep(-it) } else Modifier),
            contentAlignment = Alignment.Center
        ) { Text("−", color = TextMain, fontSize = 15.sp) }
        Text(
            value, color = TextMain, fontSize = 13.sp,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .background(PanelBgSoft)
                .goldEdge(15.dp)
                .then(if (plusEnabled) Modifier.stepperPress { onStep(it) } else Modifier),
            contentAlignment = Alignment.Center
        ) { Text("＋", color = TextMain, fontSize = 15.sp) }
    }
}

/** 番茄参数编辑（添加弹窗与设置面板共用） */
@Composable
fun PomoLoopEditor(cfg: Pomo.Cfg, onChange: (Pomo.Cfg) -> Unit) {
    Column {
        PomoStepper(
            label = "每次专注", value = "${cfg.workMin} 分钟",
            minusEnabled = cfg.workMin > 1, plusEnabled = cfg.workMin < 180
        ) { d ->
            onChange(cfg.copy(workMin = (cfg.workMin + d).coerceIn(1, 180)))
        }
        Spacer(Modifier.height(6.dp))
        PomoStepper(
            label = "每次休息", value = "${cfg.restMin} 分钟",
            minusEnabled = cfg.restMin > 1, plusEnabled = cfg.restMin < 60
        ) { d ->
            onChange(cfg.copy(restMin = (cfg.restMin + d).coerceIn(1, 60)))
        }
        Spacer(Modifier.height(6.dp))
        PomoStepper(
            label = "循环轮数", value = "${cfg.cycles} 轮",
            minusEnabled = cfg.cycles > 1, plusEnabled = cfg.cycles < 12
        ) { d ->
            onChange(cfg.copy(cycles = (cfg.cycles + d).coerceIn(1, 12)))
        }
        Spacer(Modifier.height(6.dp))
        PomoStepper(
            label = "大休息", value = "${cfg.longRestMin} 分钟",
            minusEnabled = cfg.longRestMin > 1, plusEnabled = cfg.longRestMin < 120
        ) { d ->
            onChange(cfg.copy(longRestMin = (cfg.longRestMin + d).coerceIn(1, 120)))
        }
        Spacer(Modifier.height(4.dp))
        Text(
            "默认 25 + 5，四轮后大休 20 分钟，然后继续循环。" +
                    "（单击 ±1，按住连续 ±5）",
            color = TextFaint, fontSize = 10.sp
        )
    }
}

/** 「添加」弹窗：事件 / 级别 / 为纳凌薇 / 备注（定时长用表盘拖动，不设效率） */
@Composable
fun PomoAddDialog(
    cfg: Pomo.Cfg,
    loopMode: Boolean,
    onChange: (Pomo.Cfg) -> Unit,
    onDismiss: () -> Unit
) {
    // 系统返回键 / 边缘返回手势 = 好了
    BackHandler { onDismiss() }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Veil)
            .swallowClicks { onDismiss() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .heightIn(max = 620.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(PanelBg)
                .goldEdge(20.dp)
                .border(1.dp, GoldenDay.border(PurpleMain.copy(alpha = 0.25f)), RoundedCornerShape(20.dp))
                .swallowClicks()
                .verticalScroll(rememberScrollState())
                .padding(11.dp)
        ) {
            Text(
                if (loopMode) "番茄循环 · 设置" else "这次要做什么",
                color = TextMain, fontSize = 16.sp, fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = cfg.title,
                onValueChange = { onChange(cfg.copy(title = it)) },
                label = { Text("事件", color = TextMuted, fontSize = 12.sp) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))
            Text("级别", color = TextMuted, fontSize = 12.sp)
            Spacer(Modifier.height(6.dp))
            Row {
                (1..5).forEach { lv ->
                    Chip(
                        "L$lv",
                        cfg.level == lv,
                        Modifier.padding(end = 6.dp)
                    ) { onChange(cfg.copy(level = lv)) }
                }
            }
            Spacer(Modifier.height(6.dp))
            Text(
                "L1 最重要（权重最高），L5 最轻",
                color = TextFaint, fontSize = 10.sp
            )
            Spacer(Modifier.height(12.dp))
            Chip(
                (if (cfg.forHer) "✓ " else "") + Persona.data.forTaWord + "（×1.2）",
                cfg.forHer
            ) { onChange(cfg.copy(forHer = !cfg.forHer)) }
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = cfg.note,
                onValueChange = { onChange(cfg.copy(note = it)) },
                label = { Text("备注（可空）", color = TextMuted, fontSize = 12.sp) },
                modifier = Modifier.fillMaxWidth()
            )

            if (loopMode) {
                Spacer(Modifier.height(16.dp))
                Text("番茄循环", color = TextMain, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                Spacer(Modifier.height(8.dp))
                PomoLoopEditor(cfg, onChange)
            }

            Spacer(Modifier.height(16.dp))
            Text(
                if (loopMode) "开始后会按上面的节奏自动循环；点 ■ 或放弃才会记入行迹。"
                else "时长用表盘上下拖动设置；效率固定 100%，之后可以在行迹里再调。",
                color = TextFaint, fontSize = 10.sp
            )
            Spacer(Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(PurpleDeep.copy(alpha = 0.45f))
                    .goldEdge(14.dp)
                    .swallowClicks { onDismiss() }
                    .padding(vertical = 13.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("好了", color = TextMain, fontSize = 15.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

/** 角落里的设置：结束提示 + 铃声 + 番茄参数 */
@Composable
fun PomoSettingsOverlay(
    ctx: Context,
    cfg: Pomo.Cfg,
    onChange: (Pomo.Cfg) -> Unit,
    onPickFile: () -> Unit,
    onDismiss: () -> Unit
) {
    // 系统返回键 / 边缘返回手势 = 好了
    BackHandler { onDismiss() }
    val tones = remember { Pomo.builtinTones(ctx) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Veil)
            .swallowClicks { onDismiss() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .heightIn(max = 620.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(PanelBg)
                .goldEdge(20.dp)
                .border(1.dp, GoldenDay.border(PurpleMain.copy(alpha = 0.25f)), RoundedCornerShape(20.dp))
                .swallowClicks()
                .verticalScroll(rememberScrollState())
                .padding(11.dp)
        ) {
            Text("番茄时钟设置", color = TextMain, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(12.dp))
            Text("倒计时结束后", color = TextMuted, fontSize = 12.sp)
            Spacer(Modifier.height(6.dp))
            Row {
                Chip("震动", cfg.alert == Pomo.ALERT_VIBRATE, Modifier.padding(end = 6.dp)) {
                    onChange(cfg.copy(alert = Pomo.ALERT_VIBRATE))
                }
                Chip("响铃", cfg.alert == Pomo.ALERT_RING, Modifier.padding(end = 6.dp)) {
                    onChange(cfg.copy(alert = Pomo.ALERT_RING))
                }
                Chip("都不", cfg.alert == Pomo.ALERT_NONE) {
                    onChange(cfg.copy(alert = Pomo.ALERT_NONE))
                }
            }

            if (cfg.alert == Pomo.ALERT_RING) {
                Spacer(Modifier.height(14.dp))
                Text("铃声", color = TextMuted, fontSize = 12.sp)
                Spacer(Modifier.height(6.dp))
                tones.forEach { tone ->
                    val sel = cfg.ringUri == tone.uri
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (sel) PurpleDeep.copy(alpha = 0.4f) else Color.Transparent)
                            .clickable { onChange(cfg.copy(ringUri = tone.uri)) }
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            (if (sel) "● " else "○ ") + tone.label,
                            color = if (sel) TextMain else TextMuted, fontSize = 12.sp,
                            maxLines = 1, overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                if (cfg.ringUri.isNotBlank() &&
                    tones.none { it.uri == cfg.ringUri }
                ) {
                    Text(
                        "● 自定义：${Pomo.toneLabel(ctx, cfg.ringUri)}",
                        color = PinkSoft, fontSize = 12.sp
                    )
                }
                Spacer(Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(PanelBgSoft)
                        .goldEdge(12.dp)
                        .clickable { onPickFile() }
                        .padding(horizontal = 12.dp, vertical = 9.dp)
                ) {
                    Text("选择本地音频文件…", color = TextMuted, fontSize = 12.sp)
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    "选中后点右上角「试听」或开始计时都能听到；本地文件需要允许读取。",
                    color = TextFaint, fontSize = 10.sp
                )
            }

            Spacer(Modifier.height(16.dp))
            Text("番茄循环", color = TextMain, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            Spacer(Modifier.height(8.dp))
            PomoLoopEditor(cfg, onChange)

            Spacer(Modifier.height(16.dp))
            Row {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(PanelBgSoft)
                        .goldEdge(12.dp)
                        .clickable { Pomo.alert(ctx) }
                        .padding(horizontal = 12.dp, vertical = 9.dp)
                ) {
                    Text("试听", color = TextMuted, fontSize = 12.sp)
                }
                Spacer(Modifier.width(10.dp))
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(PurpleDeep.copy(alpha = 0.45f))
                        .goldEdge(12.dp)
                        .swallowClicks { onDismiss() }
                        .padding(vertical = 11.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("好了", color = TextMain, fontSize = 14.sp)
                }
            }
        }
    }
}

/**
 * 番茄时钟页：横竖屏两套布局，状态都放在 Pomo 里（旋转屏幕不丢）。
 */
@Composable
fun PomodoroScreen(
    ctx: Context,
    idx: Store.StatsIndex?,
    onClose: () -> Unit,
    onRecorded: () -> Unit
) {
    // 系统返回键 / 边缘返回手势 = 等同左上角的叉号
    BackHandler { onClose() }
    val today = Dates.todayKey()
    var cfg by remember { mutableStateOf(Pomo.cfg(ctx)) }
    var showAdd by remember { mutableStateOf(false) }
    var showSettings by remember { mutableStateOf(false) }
    val msg = Pomo.lastMsg

    // 通知权限：番茄钟要在通知栏上显示剩余时间与暂停/完成按钮，
    // 没这个权限（Android 13+）就只能在应用内计时
    val canNotify = Build.VERSION.SDK_INT < 33 || ContextCompat.checkSelfPermission(
        ctx, Manifest.permission.POST_NOTIFICATIONS
    ) == PackageManager.PERMISSION_GRANTED
    val notifLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }
    fun askNotify() {
        if (canNotify) return
        try {
            notifLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } catch (e: Exception) {
        }
    }

    // 打开页面时，把单次模式的表盘对齐到设置里的时/分/秒；
    // 若当前是番茄模式，则显示「专注」的设定时长（点开始就直接进入专注倒计时）
    LaunchedEffect(Unit) {
        if (!Pomo.active) {
            if (Pomo.loopMode) Pomo.applyLoopDefaults(cfg)
            else Pomo.setSingleTotal(cfg.hours, cfg.minutes, cfg.seconds)
        }
    }

    val ringPicker = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            try {
                ctx.contentResolver.takePersistableUriPermission(
                    uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (e: Exception) {
            }
            cfg = cfg.copy(ringUri = uri.toString())
            Pomo.save(ctx, cfg)
            Pomo.lastMsg = "已选择自定义铃声"
        }
    }

    fun persist(c: Pomo.Cfg) {
        cfg = c
        Pomo.save(ctx, c)
        // 番茄模式下还没开始计时：显示值跟着「专注时长」走，免得误导
        if (Pomo.loopMode && !Pomo.active) Pomo.applyLoopDefaults(c)
    }

    // 完成 / 放弃都交给 Pomo.finishNow：它记一条行迹、清状态、把前台服务停掉。
    // （通知栏上的「完成 / 放弃」走的是同一个函数，逻辑只有一份）
    fun finish() {
        Pomo.finishNow(ctx, true)
        onRecorded()
    }

    fun giveUp() {
        Pomo.finishNow(ctx, false)
        onRecorded()
    }

    val low = Pomo.active && Pomo.phaseTotalMs > 0 &&
            Pomo.remainMs <= Pomo.phaseTotalMs / 20
    val timeColor = if (low) Color(0xFFE07A7A) else Spark

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(NightTop, NightBottom)))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { }
    ) {
        val landscape = maxWidth > maxHeight
        val ringSize = (if (landscape) {
            min(maxWidth.value * 0.44f, maxHeight.value * 0.62f)
        } else {
            min(maxWidth.value * 0.80f, maxHeight.value * 0.34f)
        }).dp

        // 提示行：竖屏时放在倒计时与按钮之间（小、淡），横屏时依旧跟在熵值块下面
        val msgLine: @Composable () -> Unit = {
            if (msg.isNotBlank()) {
                Text(
                    msg,
                    color = PinkSoft.copy(alpha = 0.65f),
                    fontSize = 10.sp,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    modifier = Modifier.padding(horizontal = 18.dp)
                )
            }
        }

        val aeBlock: @Composable (Modifier, Boolean) -> Unit = { m, withMsg ->
            Column(
                modifier = m.verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AEPanel(
                    ae = idx?.day(today) ?: 0.0,
                    traceAE = idx?.trace(today) ?: 0.0,
                    docAE = idx?.doc(today) ?: 0.0,
                    moodAE = idx?.mood(today) ?: 0.0,
                    nameCount = idx?.names(today) ?: 0,
                    nameFillSum = idx?.nameFill(today) ?: 0.0,
                    festivalMul = Dates.festivalMul(today),
                    steps = idx?.steps(today) ?: 0L,
                    stepsAE = idx?.stepsAE(today) ?: 0.0,
                    stepsOverMul = idx?.stepsOverMul(today) ?: 1.0,
                    gameLines = buildList {
                        val g = idx?.game(today) ?: Store.DayGame()
                        if (g.scratchUsed > 0) {
                            add("刮刮乐 ${g.scratchUsed} 次")
                            addAll(Store.gameSummaryLines(g))   // 每条增益单独一行
                        }
                    }
                )
                if (withMsg && msg.isNotBlank()) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        msg, color = PinkSoft, fontSize = 11.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }
                Spacer(Modifier.height(12.dp))
            }
        }

        val corner: @Composable () -> Unit = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                PomoButton(
                    size = 40.dp,
                    border = PurpleMain.copy(alpha = 0.4f),
                    onClick = { onClose() }
                ) { IconCross(PurpleMain) }
                Spacer(Modifier.weight(1f))
                // 🍅：番茄循环模式
                PomoButton(
                    size = 44.dp,
                    border = if (Pomo.loopMode) Color(0xFFE4674B).copy(alpha = 0.85f)
                    else PurpleMain.copy(alpha = 0.4f),
                    fill = if (Pomo.loopMode) Color(0xFFE4674B).copy(alpha = 0.16f) else PanelBgSoft,
                    onClick = {
                        if (!Pomo.active) {
                            Pomo.loopMode = !Pomo.loopMode
                            if (Pomo.loopMode) {
                                Pomo.applyLoopDefaults(cfg)
                                Pomo.lastMsg = "番茄模式：${cfg.workMin} 分专注 + ${cfg.restMin} 分休息，" +
                                        "${cfg.cycles} 轮后大休 ${cfg.longRestMin} 分"
                            } else {
                                Pomo.setSingleTotal(cfg.hours, cfg.minutes, cfg.seconds)
                                Pomo.lastMsg = "已切回单次倒计时"
                            }
                            // 开关本身就落盘：退出应用再进来仍是刚才选的那一种模式
                            Pomo.saveRun(ctx)
                        }
                    }
                ) { IconTomato(Pomo.loopMode) }
                Spacer(Modifier.width(8.dp))
                PomoButton(
                    size = 40.dp,
                    border = PurpleMain.copy(alpha = 0.4f),
                    onClick = { showSettings = true }
                ) { IconSliders(PurpleMain) }
            }
        }

        val timer: @Composable () -> Unit = {
            Box(
                modifier = Modifier.size(ringSize),
                contentAlignment = Alignment.Center
            ) {
                // 细环：按「剩余」填涂 —— 一开始是满的，随时间从蓝端退到红端，最后空。
                // 渐变锚定在绝对角度上（红在起点、蓝在末端），所以后退时颜色自然从蓝往红退。
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val stroke = w * 0.028f
                    val inset = stroke / 2f
                    val side = w - stroke
                    val start = 135f
                    val sweepMax = 270f
                    val total = Pomo.phaseTotalMs.coerceAtLeast(1L)
                    val left = (Pomo.remainMs.toFloat() / total).coerceIn(0f, 1f)
                    drawArc(
                        color = PanelBgSoft.copy(alpha = 0.55f),
                        startAngle = start, sweepAngle = sweepMax, useCenter = false,
                        topLeft = Offset(inset, inset), size = Size(side, side),
                        style = Stroke(width = stroke, cap = StrokeCap.Round)
                    )
                    if (left > 0.001f) {
                        // 颜色沿弧线从起点（左下）到末端（右下）依次为：
                        // 红 → 橙 → 黄 → 绿 → 蓝绿。倒计时从末端往起点退，
                        // 于是看起来就是「蓝绿黄橙红」从顶上一路退回底部。
                        // 起点 135°、跨度 270°，用 colorStops 精确锚定角度
                        // （不锚定的话 sweep 渐变默认从 3 点钟起算，颜色就反了）。
                        drawArc(
                            brush = Brush.sweepGradient(
                                0f to Color(0xFF5FC9C0),          // 0°（接缝，不显示）
                                0.375f to Color(0xFFD96A6A),      // 135° 起点：红
                                0.5625f to Color(0xFFE0A85A),     // 202.5° 橙
                                0.75f to Color(0xFFDCD46A),       // 270° 黄
                                0.9375f to Color(0xFF6FD98F),     // 337.5° 绿
                                1f to Color(0xFF5FC9C0)           // 回到接缝色，蓝绿
                            ),
                            startAngle = start, sweepAngle = sweepMax * left, useCenter = false,
                            topLeft = Offset(inset, inset), size = Size(side, side),
                            style = Stroke(width = stroke, cap = StrokeCap.Round)
                        )
                    }
                }

                // 环内内容
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    val showCountdown = Pomo.active || Pomo.loopMode
                    if (Pomo.loopMode) {
                        val phaseName = when (Pomo.phase) {
                            Pomo.PHASE_WORK -> "专注"
                            Pomo.PHASE_REST -> "短休息"
                            else -> "大休息"
                        }
                        Text(
                            if (Pomo.active) {
                                "$phaseName · 第 ${(Pomo.cyclesDone % cfg.cycles) + 1}/${cfg.cycles} 轮"
                            } else {
                                "准备开始 · ${cfg.workMin} 分专注 + ${cfg.restMin} 分休息"
                            },
                            color = GoldSoft, fontSize = 12.sp
                        )
                        Spacer(Modifier.height(6.dp))
                    }
                    if (showCountdown) {
                        val cdText = clockText(Pomo.remainMs)
                        // 这个环很细（只有 0.028w），环内空白接近整个直径，
                        // 所以字号可以给到很大：只按「不与外圈重叠」来算。
                        val clearInner = ringSize.value * 0.90f
                        val cdFont = (clearInner / (cdText.length * 0.62f))
                            .coerceAtMost(ringSize.value * 0.32f)
                        Text(
                            cdText,
                            color = timeColor,
                            fontSize = cdFont.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            softWrap = false,
                            textAlign = TextAlign.Center
                        )
                        if (Pomo.loopMode) {
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "已专注 ${Pomo.workAccumSec / 60} 分钟 · 完成 ${Pomo.cyclesDone} 轮",
                                color = TextFaint, fontSize = 10.sp
                            )
                        } else if (Pomo.active) {
                            Spacer(Modifier.height(4.dp))
                            Text("点 ■ 完成 · 点 ✕ 放弃", color = TextFaint, fontSize = 10.sp)
                        }
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            TimeWheel(
                                cfg.hours, 24, true, timeColor,
                                (ringSize.value * 0.13f).sp
                            ) { h ->
                                val c = cfg.copy(hours = h)
                                persist(c)
                                Pomo.setSingleTotal(c.hours, c.minutes, c.seconds)
                            }
                            Text(
                                ":",
                                color = timeColor,
                                fontSize = (ringSize.value * 0.13f).sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 2.dp)
                            )
                            TimeWheel(
                                cfg.minutes, 60, true, timeColor,
                                (ringSize.value * 0.13f).sp
                            ) { m ->
                                val c = cfg.copy(minutes = m)
                                persist(c)
                                Pomo.setSingleTotal(c.hours, c.minutes, c.seconds)
                            }
                            Text(
                                ":",
                                color = timeColor,
                                fontSize = (ringSize.value * 0.13f).sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 2.dp)
                            )
                            TimeWheel(
                                cfg.seconds, 60, true, timeColor,
                                (ringSize.value * 0.13f).sp
                            ) { sec ->
                                val c = cfg.copy(seconds = sec)
                                persist(c)
                                Pomo.setSingleTotal(c.hours, c.minutes, c.seconds)
                            }
                        }
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "上下拖动数字可设置时 / 分 / 秒",
                            color = TextFaint, fontSize = 10.sp
                        )
                    }
                }
            }
        }

        val buttons: @Composable () -> Unit = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                PomoButton(
                    size = 52.dp,
                    border = PurpleMain.copy(alpha = 0.45f),
                    onClick = { showAdd = true }
                ) { IconPlus(PurpleMain) }

                PomoButton(
                    size = 62.dp,
                    border = CatSteps.copy(alpha = 0.7f),
                    fill = Color(0xFF5FBF7F).copy(alpha = 0.14f),
                    onClick = {
                        if (Pomo.running) {
                            Pomo.pause()
                        } else {
                            if (!Pomo.active) {
                                Pomo.setSingleTotal(cfg.hours, cfg.minutes, cfg.seconds)
                            }
                            Pomo.start(ctx)
                        }
                        // 落盘 + 拉起前台服务：从这一刻起，关掉软件、划掉后台也接着走
                        Pomo.saveRun(ctx)
                        if (Pomo.active) {
                            askNotify()
                            Pomo.startService(ctx)
                        }
                    }
                ) { IconPlayPause(Pomo.running, if (Pomo.running) GoldSoft else CatSteps) }

                PomoButton(
                    size = 52.dp,
                    enabled = Pomo.active,
                    border = Color(0xFF5FBF7F).copy(alpha = 0.7f),
                    fill = Color(0xFF5FBF7F).copy(alpha = 0.14f),
                    onClick = { finish() }
                ) { IconSquare(Color(0xFF6FD08A)) }

                PomoButton(
                    size = 52.dp,
                    enabled = Pomo.active,
                    border = Color(0xFFD96A6A).copy(alpha = 0.7f),
                    fill = Color(0xFFD96A6A).copy(alpha = 0.14f),
                    onClick = { giveUp() }
                ) { IconCross(Color(0xFFE07A7A)) }
            }
        }

        if (landscape) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1.15f)
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    corner()
                    Spacer(Modifier.weight(1f))
                    timer()
                    Spacer(Modifier.weight(0.4f))
                    buttons()
                    Spacer(Modifier.weight(1f))
                }
                Spacer(Modifier.width(6.dp))
                aeBlock(
                    Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    true
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal))
                    .navigationBarsPadding()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                corner()
                Box(
                    modifier = Modifier
                        .weight(1.05f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) { timer() }
                Spacer(Modifier.height(6.dp))
                // 竖屏：提示行放在倒计时与按钮之间
                msgLine()
                Spacer(Modifier.height(6.dp))
                buttons()
                Spacer(Modifier.height(10.dp))
                aeBlock(
                    Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    false
                )
            }
        }

        if (showAdd) {
            PomoAddDialog(
                cfg = cfg,
                loopMode = Pomo.loopMode,
                onChange = { persist(it) },
                onDismiss = { showAdd = false }
            )
        }
        if (showSettings) {
            PomoSettingsOverlay(
                ctx = ctx,
                cfg = cfg,
                onChange = { persist(it) },
                onPickFile = {
                    try {
                        ringPicker.launch(arrayOf("audio/*"))
                    } catch (e: Exception) {
                    }
                },
                onDismiss = { showSettings = false }
            )
        }
    }
}

/** 毫秒 → mm:ss（超过一小时显示 h:mm:ss） */
fun clockText(ms: Long): String {
    val total = (ms / 1000L).coerceAtLeast(0L)
    val h = total / 3600
    val m = (total % 3600) / 60
    val s = total % 60
    return if (h > 0) String.format(Locale.CHINA, "%d:%02d:%02d", h, m, s)
    else String.format(Locale.CHINA, "%02d:%02d", m, s)
}

/* ---------------------------- 行动熵计量规则 ---------------------------- */

@Composable
fun AeRulesOverlay(onClose: () -> Unit) {
    // 系统返回键 / 边缘返回手势 = 关闭这一页
    // （和「使用说明」那页保持一致的返回行为）
    BackHandler { onClose() }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Veil)
            .swallowClicks { onClose() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 34.dp, bottom = 14.dp)
                .swallowClicks()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "行动熵计量规则",
                    color = TextMain, fontSize = 16.sp, fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.weight(1f))
                Chip("关闭", false) { onClose() }
            }
            Spacer(Modifier.height(10.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp)
            ) {
                RuleBlock(
                    "一、行迹",
                    listOf(
                        "AE = 级别权重 × ln(1+时长/30) × (0.5+效率) × 加成 × 6.0",
                        "级别权重（L1~L5）：3.0 ｜ 2.2 ｜ 1.6 ｜ 1.2 ｜ 0.8",
                        "时长项：30分≈0.69 ｜ 1时≈1.10 ｜ 2时≈1.61 ｜ 4时≈2.20",
                        "效率项：0.5 + 效率%（效率 0 也保留 0.5）",
                        "加成：勾选「" + Persona.data.forTaWord + "」×1.2",
                        "来自番茄：×1.1（多轮的额外加成见「九」）"
                    )
                )
                RuleBlock(
                    "二、文章与日记",
                    listOf(
                        "文章：AE = 10 × ln(1 + 字数/300)",
                        "日记：AE = 12.5 × ln(1 + 字数/300)",
                        "参考：300字≈6.9 / 8.7　1000字≈14.7 / 18.3",
                        "　　　2000字≈20.4 / 25.5",
                        "创建时间写定后不再变动，编辑多少遍都还是那一刻。",
                        "创建当日 24:00 前修改会重算；过零点即锁定，",
                        "之后改写内容或删除，当日数值都不再变化。"
                    )
                )
                RuleBlock(
                    "三、心情",
                    listOf(
                        "单条：AE = 承受权重 × (1 + |强度| × 0.6)",
                        "强度：−5 ~ +5（左难过，右开心）",
                        "情绪表（极性/权重，正 → 负）：",
                        "　开心 1.00/1.00　惊喜 0.90/1.10　充实 0.85/1.00",
                        "　感动 0.70/1.20　期待 0.60/1.10　释然 0.50/0.90",
                        "　平静 0.20/0.80　不知道 0.00/1.05　尴尬 −0.30/1.10",
                        "　迷茫 −0.40/1.30　逃避 −0.45/1.30　疲惫 −0.50/1.30",
                        "　难受 −0.55/1.35　委屈 −0.60/1.40　孤独 −0.65/1.50",
                        "　烦躁 −0.70/1.40　焦虑 −0.75/1.45　生气 −0.80/1.40",
                        "　难过 −0.85/1.50　虚无 −1.00/1.60",
                        "自己填的标签：若正好是上表某个 → 按它的固定规则；",
                        "　否则按「极性 0、权重 1.10」计（强度照常参与）。",
                        "情绪方差项（当日 ≥2 条时另计）：",
                        "　位置 = 极性 + 强度/5（约 −2 ~ +2）",
                        "　方差项 = 2.5×极差 + 1.5×平均相邻变化，封顶 30",
                        "当日心情 AE = 各条基础熵之和 + 情绪方差项"
                    )
                )
                RuleBlock(
                    "四、运动（步数）",
                    listOf(
                        "运动熵 = 11 × ln(1 + 步数/1200)，到 27000 步封顶",
                        "　2000≈10.8　8000≈22.4　10000≈24.6　27000≈34.6",
                        "达成自设目标 → 运动熵 ×1.2（只在运动页结算）",
                        "超出 27000 步 → 当日行动熵整体 ×1.5；步数本身不封顶",
                        "距离 ≈ 步数 × 步幅(默认68cm)",
                        "卡路里 ≈ 步数 × 体重 × 0.0005（估算）",
                        "记录：常驻前台服务 + 15 分钟兜底采样；",
                        "　没传感器或没授权时可在运动页手动补记。"
                    )
                )
                RuleBlock(
                    "五、联结加权（唤名）",
                    listOf(
                        "写下名字＝正在承受难以承受的东西，那种状态下每件小事都更难，",
                        "所以它乘在当日合计上，而不是直接加熵（难捱本身不该算作成绩）。",
                        "每一次写名，按两档算：",
                        "　写下来本身 +2.5%",
                        "　难捱程度（字条填到多少）再 +0 ~ +2.5%，即每 1% 加 0.025%，",
                        "　　单条最多 +5.0% —— 填满的一条 = 旧规则里的两条",
                        "当日合计仍然封顶 +20%（与旧规则同一个上限，尺度不变）：",
                        "　8 次都没填 = +20%（和以前一模一样）",
                        "　填满的 4 次 = +20%（一半的次数，扛起同样的重量）",
                        "字条：写下后的 10 分钟内，点一次 +1%（左→右填充），",
                        "　满 100% 或过 10 分钟即锁定；难捱＝那一次有多难以忍受。",
                        "缘由：写下前点「缘由」，先记一句为什么；",
                        "　再点「写 下」，该笔连同缘由一并记下。",
                        "底部那朵花：点一下飘出名字与语录，不计熵；",
                        "　计数＝终身累计，随文档导出。"
                    )
                )
                RuleBlock(
                    "六、当日合计与两环",
                    listOf(
                        "当日行动熵 =（行迹 + 文记 + 心情 + 运动）",
                        "　×（联结加权 + 联结加成）× 当日整体倍率 + 直接所得",
                        "再乘：纪念日倍率、节日倍率、运动溢出倍率",
                        "内环 0→100 满即「" + Persona.data.bloomWord + "」；外环 0→170，两环合计 270"
                    )
                )
                RuleBlock(
                    "七、刮刮乐（娱乐）",
                    listOf(
                        "每天自动一张；昨日 ≥100 两张，≥270 三张。",
                        "刮开 80% 才揭开。档位与概率：",
                        "　特等 0.021%　一等 0.4%　二等 5.4%",
                        "　三等 19%　四等 71.98%　无奖 3.2%",
                        "奖励：直接 AE（+270/100/50/27/7/3）",
                        "　整体倍率 ×1.03~1.50　单项倍率 ×1.05~1.30",
                        "　联结 +0.03~+0.15　再刮一次",
                        "熵值页里每一条加成各占一行，不会挤在一起。"
                    )
                )
                RuleBlock(
                    "八、纪念日 · 节日 · 节气",
                    listOf(
                        "【纪念日】" + Persona.annivs().joinToString("　") {
                            it.name + " " + it.month + "/" + it.day
                        },
                        "　当日全部行动熵 ×各自倍率；勾了金色的那几天面板转金。",
                        "【节日】每个节日 ×1.2，同日两个相乘；不镶金边，只挂标签、弹语录。",
                        "【节气】二十四节气只有提醒，没有增益。",
                        "　三类的名称与语录均可在自定义设置中逐条替换；",
                        "　农历与节气由本机离线推算，不联网、不要权限。",
                        "【弹窗】只在当天弹，按日期各记一次；点「我知道了」当日不再提醒。"
                    )
                )
                RuleBlock(
                    "九、番茄时钟",
                    listOf(
                        "入口：行迹页右下角（全屏页）。▶ 开始　Ⅱ 暂停　■ 完成　✕ 放弃",
                        "完成：记一条行迹，效率 100%，标注「来自番茄」，行动熵 ×1.1",
                        "放弃：按已进行的有效时长记一条普通行迹，无加成",
                        "循环：默认 25 专注 + 5 休息，四轮后大休 20 分钟；加成 = 1.1 × (1+(轮数−1)×3%)",
                        "结束提示：震动 / 响铃 / 都不；铃声可选系统内置或本地文件",
                        "【后台】由前台服务计时，退出或清后台仍继续；通知栏可暂停 / 完成 / 放弃"
                    )
                )
                RuleBlock(
                    "十、冻结规则（所有记录一致）",
                    listOf(
                        "当日：随时增删改，行动熵实时重算；",
                        "过零点：当日数值整体定格（行迹 / 文记 / 心情 / 运动 / 联结）。",
                        "此后无论怎么改、怎么删，那一天的数值都不再变化。",
                        "　—— 发生过的事，就被记录下来了"
                    )
                )
                RuleBlock(
                    "十一、状态估量",
                    listOf(
                        "< 20　沉眠　｜　20~45　微光　｜　45~70　前行",
                        "70~100　绽放　｜　≥ 100　" + Persona.data.bloomWord
                    )
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    "意义在于创造而非赋予，追寻而非实现。",
                    color = TextFaint, fontSize = 11.sp, textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun RuleBlock(title: String, lines: List<String>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(PanelBg)
            .goldEdge(16.dp)
            .border(
                1.dp, GoldenDay.border(PurpleMain.copy(alpha = 0.16f)),
                RoundedCornerShape(16.dp)
            )
            .padding(11.dp)
    ) {
        Text(title, color = PinkSoft, fontSize = 13.sp, fontWeight = FontWeight.Medium)
        Spacer(Modifier.height(8.dp))
        lines.forEach { line ->
            Text(line, color = TextMuted, fontSize = 12.sp)
            Spacer(Modifier.height(4.dp))
        }
    }
}

/* ==========================================================================
 *  十一、栏目二：唤名
 * ======================================================================== */

/**
 * 字条填充用的横向渐变：一起铺满整条，填到哪一段就看见哪一段的颜色 ——
 * 越填越往「淡紫 → 深紫 → 藕粉」走，一眼看得出那一刻有多难捱。
 * 缓存起来：宽度没变就不重建（滚动时每帧新建 shader 太亏）。
 */
private class FillBrushCache {
    private var w = -1f
    private var brush: Brush? = null
    fun of(width: Float): Brush {
        val b = brush
        if (b != null && w == width) return b
        val made = Brush.horizontalGradient(
            // 越往右越实：难捱越深，颜色越沉、越压得住底下的深色面板
            listOf(PurpleDeep.copy(alpha = 0.69f), PurpleMain.copy(alpha = 0.75f), PinkSoft.copy(alpha = 0.80f)),
            startX = 0f,
            endX = width
        )
        w = width
        brush = made
        return made
    }
}

/**
 * 「缘由」字条：点「缘由」后在写名字的上方弹出来的一条 —— 左边是名字，右边是备注框。
 *
 *   · 备注框是**隐形**的（没有边框底色，只有一行字），光标粉色；
 *   · 字数按「剩下的横向空间」算：名字越长，能写的字越少，
 *     上限夹在 6~12 之间 —— 目的是**永远不换行**，字条高度不跳；
 *   · 输入法组合区用整份 TextFieldValue 保留（和正文编辑器同一个道理，
 *     否则中文/英文联想打到一半会被打断）。
 */
@Composable
private fun MarkStrip(
    name: String,
    note: String,
    onNote: (String) -> Unit,
    onClear: () -> Unit
) {
    val measurer = rememberTextMeasurer()
    val density = LocalDensity.current
    // 整份 TextFieldValue 留着：输入法的组合区（拼音、英文联想）不能被打断
    var tv by remember { mutableStateOf(TextFieldValue(note)) }
    LaunchedEffect(note) {
        if (tv.text != note) tv = tv.copy(text = note, selection = TextRange(note.length))
    }
    val nameStyle = TextStyle(fontSize = 16.sp)
    val noteStyle = TextStyle(fontSize = 14.sp)
    // 一个汉字的宽度（用「难」实测，比按字号估更准）
    val cjkW = remember(measurer) { measurer.measure("难", style = noteStyle).size.width }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(PanelBg.copy(alpha = 0.55f))
            .border(1.dp, PinkSoft.copy(alpha = 0.40f), RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        val nameW = 0f   // 不再画名字：这一条只写缘由，省下的横向空间全给输入框
        // 右边固定占掉的：✕（22dp）+ 间距
        val fixedPx = with(density) { (22 + 8).dp.toPx() }
        val maxChars = with(density) {
            (((maxWidth.toPx() - nameW - fixedPx) / cjkW).toInt()).coerceIn(6, 12)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.weight(1f)) {
                BasicTextField(
                    value = tv,
                    onValueChange = { nv ->
                        // 掐掉超出的字：宁可少写两个字，也不要换行把字条撑高
                        val t = nv.text.take(maxChars)
                        val sel = TextRange(
                            nv.selection.start.coerceAtMost(t.length),
                            nv.selection.end.coerceAtMost(t.length)
                        )
                        tv = nv.copy(text = t, selection = sel)
                        onNote(t)
                    },
                    singleLine = true,
                    textStyle = noteStyle.copy(color = TextMain),
                    cursorBrush = SolidColor(PinkSoft),
                    modifier = Modifier.fillMaxWidth()
                )
                if (tv.text.isEmpty()) {
                    Text(
                        "缘由：" + maxChars + " 字以内",
                        color = TextFaint.copy(alpha = 0.7f), fontSize = 14.sp, maxLines = 1
                    )
                }
            }
            Spacer(Modifier.width(8.dp))
            Text(
                "✕",
                color = TextFaint,
                fontSize = 13.sp,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { onClear() }
                    .padding(4.dp)
            )
        }
    }
}

/** 字条下面那行小字：时间 ｜ 难捱度 ｜ 还能填多久（或已锁定） */
private fun fillLineOf(rec: NameRec, open: Boolean, now: Long): String {
    val sb = StringBuilder(Dates.stamp(rec.ts))
    if (rec.fill > 0 || open) sb.append("　难捱 ").append(rec.fill).append('%')
    if (open) {
        val left = rec.fillLeftMs(now)
        sb.append("　还可填 ").append(left / 60000L).append(':')
            .append(String.format(Locale.CHINA, "%02d", (left / 1000L) % 60L))
    } else if (rec.fill >= 100) {
        sb.append("　已满")
    } else {
        sb.append("　已锁定")
    }
    return sb.toString()
}

@Composable
fun BondScreen(
    names: MutableList<NameRec>,
    onAdd: (String, String) -> Unit,
    onDelete: (NameRec) -> Unit,
    onDataChanged: () -> Unit,
    /** 把一瓣飘字交给窗口最外层：只有它才能从屏幕最底端一路飘到最顶端 */
    onFloat: (FishWord) -> Unit,
    /** 把唤名列表写回磁盘（填充是连点出来的，调用方自己会做防抖） */
    onSaveNames: () -> Unit = {},
    /**
     * 页底那朵花被点了一次：把新的终身累计数交给上层。
     * 花房的「我记得你」与资料卡的 calls 一行用的也是这个数 —— 不告诉它，
     * 两边就会各拿各的旧值，点完花那两处要等下一次整体刷新才动。
     */
    onKnockChanged: (Long) -> Unit = {}
) {
    var input by remember { mutableStateOf(Persona.callName()) }
    /**
     * 输入框的默认填充就是「ta 的名字」，所以名字一改，这里要跟着改。
     *
     * 这里必须留意两件事：角色设定是进界面之后才异步读进来的（LaunchedEffect 里的
     * Persona.load），而各栏又会被提前预热，所以「进这一栏的那一刻」读到的往往还是
     * 出厂默认；再加上 remember 只取一次值。两下一凑，改过名字之后这个框里仍旧是
     * 默认那三个字。故记下初值，名字变了就回填 —— 但只在这一栏还没被手输过时回填，
     * 免得把正在写的那一笔冲掉。
     */
    var nameAtInit by remember { mutableStateOf(Persona.callName()) }
    // 称呼也要盯着：名字留空时，默认填充取的就是称呼，改称呼同样要跟着变
    LaunchedEffect(Persona.data.displayName to Persona.data.pronoun) {
        if (input == nameAtInit) input = Persona.callName()
        nameAtInit = Persona.callName()
    }
    var pendingDelete by remember { mutableStateOf<NameRec?>(null) }
    // 底部那朵花：终身累计的次数
    val ctx = LocalContext.current
    var knockCount by remember { mutableLongStateOf(Store.loadKnock(ctx)) }

    // ---------- 「缘由」：先写好这一次为什么难捱，再带着它写下来 ----------
    // 点「缘由」在输入框下面弹出一条字条，整行都是备注框（不再画名字，把宽度让给输入）；
    // 备注写完点「写 下」，这一笔连同缘由一起记下来。
    var marking by remember { mutableStateOf(false) }
    var markNote by remember { mutableStateOf("") }

    // ---------- 字条填充（难捱程度） ----------
    // 有字条还在「写下后 10 分钟」里时每秒走一下：倒计时要自己变，到点要自己锁上
    var nowMs by remember { mutableLongStateOf(System.currentTimeMillis()) }
    val anyOpen = names.any { it.fillOpen(nowMs) }
    LaunchedEffect(anyOpen) {
        if (!anyOpen) return@LaunchedEffect
        while (true) {
            kotlinx.coroutines.delay(1000)
            nowMs = System.currentTimeMillis()
            // 主动互动：「十分钟窗口刚关上」那一刻（内部只报一次，重复调没有副作用）
            names.forEach { AiProactive.checkFillDeadline(ctx, it, nowMs) }
            if (names.none { it.fillOpen(nowMs) }) break
        }
    }

    // 连点：每下 +1% 先只改内存，停手 700ms 再落盘 + 重算熵值（不然一百下就是一百次写文件）
    var saveTick by remember { mutableIntStateOf(0) }
    LaunchedEffect(saveTick) {
        if (saveTick == 0) return@LaunchedEffect
        kotlinx.coroutines.delay(700)
        onSaveNames()
        onDataChanged()
    }
    // 兜底：还没来得及落盘就离开这一页（比如退出软件）时，补写一次
    DisposableEffect(Unit) {
        onDispose { if (saveTick > 0) onSaveNames() }
    }

    // 新写下一条就把列表顶到最上面：新字条永远在视野最上端
    val listState = rememberLazyListState()
    var lastCount by remember { mutableIntStateOf(names.size) }
    LaunchedEffect(names.size) {
        if (names.size > lastCount) {
            lastCount = names.size
            kotlinx.coroutines.delay(60)   // 等这一条真的被组合出来
            try {
                listState.animateScrollToItem(0)
            } catch (e: Exception) {
            }
        } else {
            lastCount = names.size
        }
    }

    // 字条填充的横向渐变：宽度变了才重建，滚动时不重新分配
    val fillBrushCache = remember { FillBrushCache() }
    // 填充前沿的渐隐（代替原来那条硬邦邦的竖线）：从藕粉一档化到透明
    val fillEdgeBrush = remember {
        Brush.horizontalGradient(listOf(PinkSoft.copy(alpha = 0.40f), Color.Transparent))
    }
    val fillEdgePx = with(LocalDensity.current) { 16.dp.toPx() }

    /** 点一下字条：难捱 +1%（只在窗口内、未满时生效） */
    fun fillOnce(rec: NameRec) {
        val now = System.currentTimeMillis()
        if (!rec.fillOpen(now)) return
        val i = names.indexOfFirst { it.ts == rec.ts }
        if (i < 0) return
        names[i] = names[i].copy(fill = (names[i].fill + 1).coerceAtMost(100))
        nowMs = now
        saveTick++
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {

            // ---------- 写下名字 ----------
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Brush.verticalGradient(listOf(PanelBgSoft, PanelBg)))
                    .goldEdge(20.dp)
                    .border(
                        1.dp, GoldenDay.border(PinkSoft.copy(alpha = 0.18f)),
                        RoundedCornerShape(20.dp)
                    )
                    .padding(16.dp)
            ) {
                Text(
                    "在这样的时刻，" + Persona.data.wakeVerb + "。",
                    color = TextMain, fontSize = 14.sp
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "每写下一次，都会留下一个只属于那一刻的时间。",
                    color = TextFaint, fontSize = 11.sp
                )
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = input,
                    onValueChange = { input = it.take(24) },
                    label = { Text("在这里写下名字", color = TextMuted, fontSize = 12.sp) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // ---------- 缘由字条：点了「缘由」才出现，就在输入框下面、卡片顶部 ----------
                if (marking) {
                    Spacer(Modifier.height(10.dp))
                    MarkStrip(
                        name = input.ifBlank { Persona.callName() },
                        note = markNote,
                        onNote = { markNote = it },
                        onClear = {
                            marking = false
                            markNote = ""
                        }
                    )
                }

                Spacer(Modifier.height(10.dp))
                Row {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(PurpleDeep.copy(alpha = 0.45f))
                            .border(
                                1.dp, GoldenDay.border(PinkSoft.copy(alpha = 0.3f)),
                                RoundedCornerShape(14.dp)
                            )
                            .clickable {
                                onAdd(input.ifBlank { Persona.callName() }, markNote)
                                onDataChanged()
                                // 这一笔连同缘由记下了，标注就收起来
                                marking = false
                                markNote = ""
                            }
                            .padding(vertical = 13.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("写 下", color = TextMain, fontSize = 15.sp, fontWeight = FontWeight.Medium)
                    }
                    Spacer(Modifier.width(10.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                if (markNote.isNotBlank()) PurpleDeep.copy(alpha = 0.35f)
                                else PanelBgSoft
                            )
                            .goldEdge(14.dp)
                            .clickable {
                                marking = !marking
                                if (!marking) markNote = ""
                            }
                            .padding(horizontal = 12.dp, vertical = 13.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            if (markNote.isNotBlank()) "缘由 ✓" else "缘由",
                            color = PinkSoft, fontSize = 14.sp
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("已呼唤 ${names.size} 次", color = TextMuted, fontSize = 12.sp)
                Spacer(Modifier.weight(1f))
                if (names.isNotEmpty()) {
                    Text("最近：" + Dates.stamp(names.last().ts), color = TextFaint, fontSize = 11.sp)
                }
            }
            Spacer(Modifier.height(6.dp))

            if (names.isEmpty()) {
                Box(modifier = Modifier.weight(1f)) {
                    EmptyHint(
                        "ta 还没有被写下来。",
                        "在十分难受的时候，" + Persona.data.wakeVerb + "。"
                    )
                }
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    // 底部留出空间：最后几条不会被底部的花挡住
                    contentPadding = PaddingValues(
                        start = 16.dp, end = 16.dp, top = 4.dp, bottom = 128.dp
                    )
                ) {
                    items(names.asReversed().toList(), key = { it.ts }) { rec ->
                        // 只有「当日写下的名字」在凌薇日会镀上金框
                        val recIsToday = rec.ts >= Dates.parse(Dates.todayKey()).timeInMillis
                        // 这一条还在可填窗口里吗？满了 / 过了 10 分钟就锁住
                        val open = rec.fillOpen(nowMs)
                        val shape = RoundedCornerShape(12.dp)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                                .clip(shape)
                                .background(PanelBg.copy(alpha = 0.55f))
                                // 整条字条就是「难捱 +1%」：写下后的 10 分钟里点它
                                .then(if (open) Modifier.clickable { fillOnce(rec) } else Modifier)
                                .then(
                                    when {
                                        open -> Modifier.border(
                                            1.dp, PinkSoft.copy(alpha = 0.40f), shape
                                        )
                                        GoldenDay.active && recIsToday -> Modifier.border(
                                            1.dp, ScratchGold.copy(alpha = 0.55f), shape
                                        )
                                        else -> Modifier
                                    }
                                )
                        ) {
                            // 左 → 右的百分比填充：填到哪，就是那一刻有多难捱
                            if (rec.fill > 0) {
                                Canvas(modifier = Modifier.matchParentSize()) {
                                    val w = size.width * (rec.fill / 100f)
                                    // 渐变铺满整条：填得越满，颜色越往「深紫 → 藕粉」走
                                    drawRect(
                                        brush = fillBrushCache.of(size.width),
                                        size = Size(w, size.height)
                                    )
                                    // 前沿不再画硬线（一条竖线太割裂）：改成一小段渐隐，
                                    // 「填到哪」照样看得见，但边界是化开的
                                    if (rec.fill < 100) {
                                        drawRect(
                                            brush = fillEdgeBrush,
                                            topLeft = Offset(w, 0f),
                                            size = Size(fillEdgePx, size.height)
                                        )
                                    }
                                }
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 9.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        rec.text, color = PurpleMain, fontSize = 15.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Spacer(Modifier.height(1.dp))
                                    Text(
                                        fillLineOf(rec, open, nowMs),
                                        color = if (open) PinkSoft.copy(alpha = 0.85f) else TextFaint,
                                        fontSize = 10.sp
                                    )
                                    // 写下前记的缘由：一直留在这条字条上
                                    if (rec.note.isNotBlank()) {
                                        Spacer(Modifier.height(2.dp))
                                        Text(
                                            "缘由：" + rec.note,
                                            color = TextMuted,
                                            fontSize = 10.sp,
                                            maxLines = 1
                                        )
                                    }
                                }
                                if (open) {
                                    Text(
                                        "+1%",
                                        color = PinkSoft,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(start = 8.dp)
                                    )
                                }
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    "✕",
                                    color = TextFaint.copy(alpha = 0.7f),
                                    fontSize = 13.sp,
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .clickable { pendingDelete = rec }
                                        .padding(4.dp)
                                )
                            }
                        }
                    }
                    item { Spacer(Modifier.height(16.dp)) }
                }
            }
        }

        pendingDelete?.let { rec ->
            ConfirmOverlay(
                title = "删除这条记录？",
                message = "「${rec.text}」于 ${Dates.stamp(rec.ts)} 写下的这一次，将被永久删除。",
                onConfirm = {
                    onDelete(rec)
                    pendingDelete = null
                    onDataChanged()
                },
                onCancel = { pendingDelete = null }
            )
        }

        // ---------- 底部那朵栀子花：只是叫叫她，不记录、不计熵 ----------
        // 栀子花固定钉在原位，**不随键盘上移**：键盘弹出时它被键盘盖住。
        // 内容区为了让开键盘整体上移了 (键盘高 − 底栏高)，这里把同样的距离补回去，
        // 它的屏幕位置就一点没变。
        val knockDensity = LocalDensity.current
        val knockShift = with(knockDensity) {
            (WindowInsets.ime.getBottom(knockDensity) - 58.dp.roundToPx())
                .coerceAtLeast(0).toDp()
        }
        FlowerKnock(
            count = knockCount,
            onKnock = {
                val now = Store.addKnock(ctx)
                knockCount = now
                // 花房那边的同一个数也要跟上（连点不必整页刷新，只把数字递过去）
                onKnockChanged(now)
                // 每一下都飘出她的名字
                onFloat(newFishWord(Persona.data.displayName, PinkSoft))
                // 有些时候，她也会回一句话
                if (Math.random() < 0.34) {
                    val q = Persona.randomQuote()
                    if (q.isNotBlank()) {
                        onFloat(
                            newFishWord(
                                q,
                                QuoteTints[(Math.random() * QuoteTints.size).toInt()],
                                isQuote = true
                            )
                        )
                    }
                }
            },
            // 把内容区为键盘让出的那段距离补回去 —— 花的屏幕位置不变
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = knockShift)
                .padding(bottom = 10.dp)
        )
    }
}

/* ---------------------------- 联结页底部那朵花 ---------------------------- */

/** 一瓣正在从屏幕底端飘到顶端的字：可能是她的名字，也可能是一句她说的话 */
class FishWord(
    val id: Long,
    val text: String,
    val tint: Color,
    /** 在 GlowTints 里的序号：微光直接按序号取预烤好的图，省掉每帧查表 */
    val glowIndex: Int,
    val startFrac: Float,   // 起始横向位置（0~1）
    val driftPx: Float,     // 全程横向漂移
    val baseScale: Float,
    val phase: Float,       // 忽大忽小 / 明暗变化的相位
    val durMs: Int,
    val isQuote: Boolean = false,
    /** 出生时刻（System.nanoTime）：进度按「现在 − 出生时刻」算，所有字共用一个帧时钟 */
    val bornNs: Long = System.nanoTime()
)

/**
 * 她说的话（CareWords / FloatQuotes）在文件上方的「六、她的话」一节，
 * 与花房的「敲门」应答语共用一套口吻。
 */

/** 飘字层每帧最多画多少个字：超出预算时，最早（已经飘到最上面、快散没）的那些先不画 */
private const val MAX_DRAWN = 60

/** 微光贴图的分辨率：只是团柔光，128 就足够，再大反而占内存 */
private const val GLOW_PX = 128

/**
 * 把「本色 → 全透明」的柔和径向渐变换成一张小图。
 * 这样每个字画微光时只是贴一次这张图，不用每帧新建 shader——
 * 观感跟原来的径向渐变一模一样（同一条从中心到边缘的线性透明过渡），但便宜得多。
 */
private fun makeGlowBitmap(color: Color): ImageBitmap {
    val bmp = Bitmap.createBitmap(GLOW_PX, GLOW_PX, Bitmap.Config.ARGB_8888)
    val c = android.graphics.Canvas(bmp)
    val p = android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG)
    val r = GLOW_PX / 2f
    val solid = color.toArgb()
    p.shader = android.graphics.RadialGradient(
        r, r, r,
        intArrayOf(solid, solid and 0x00FFFFFF),   // 中心：本色；边缘：同色全透明
        null,
        android.graphics.Shader.TileMode.CLAMP
    )
    c.drawCircle(r, r, r, p)
    return bmp.asImageBitmap()
}

/** 飘字的自增序号：同一毫秒连点两次也不会撞 id */
private var fishSeq = 0L

/** 造一瓣飘字：位置、漂移、大小、相位、时长全部随机 */
fun newFishWord(text: String, tint: Color, isQuote: Boolean = false): FishWord {
    val r = Math.random()
    return FishWord(
        id = ++fishSeq,
        text = text,
        tint = tint,
        // 颜色一定取自 GlowTints（名字用 PinkSoft、语录用 QuoteTints），
        // 这里只在生成时查一次，飘的过程中直接按序号取柔光图
        glowIndex = GlowTints.indexOfFirst { it == tint }
            .let { if (it >= 0) it else GlowTints.lastIndex },
        // 语录比较长，横向收窄一些，保证整句都在屏幕里
        startFrac = if (isQuote) 0.40f + (r * 0.20).toFloat()
        else 0.22f + (r * 0.56).toFloat(),
        driftPx = ((Math.random() - 0.5) * if (isQuote) 40.0 else 80.0).toFloat(),
        baseScale = if (isQuote) 0.92f + (Math.random() * 0.22).toFloat()
        else 0.85f + (Math.random() * 0.5).toFloat(),
        phase = (Math.random() * 6.2832).toFloat(),
        durMs = if (isQuote) 6800 + (Math.random() * 3000).toInt()
        else 5600 + (Math.random() * 2800).toInt(),
        isQuote = isQuote
    )
}

/**
 * 联结页底部居中的那朵栀子花：点一下就从**屏幕最底端**生出一串「纳凌薇」，
 * 一路飘到**屏幕最顶端**再消散；飘的过程中会忽大忽小、明暗微微变化，周围带一点微光。
 * 连点可以叠加成满页，但都会慢慢散掉。
 * 偶尔——不是每次——还会跟着飘上来一句她说的话（我爱你、好好喝水……）。
 * 只是叫叫她：不写记录、不计行动熵；旁边的数字是终身累计的次数。
 */
@Composable
fun FlowerKnock(
    count: Long,
    onKnock: () -> Unit,
    modifier: Modifier = Modifier
) {
    val hit = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .size(104.dp)
                .clip(CircleShape)
                .background(PanelBg.copy(alpha = 0.28f))
                .goldEdge(52.dp)
                .clickable {
                    onKnock()
                    scope.launch {
                        hit.snapTo(1.14f)
                        hit.animateTo(1f, tween(220, easing = FastOutSlowInEasing))
                    }
                }
                .graphicsLayer {
                    scaleX = hit.value
                    scaleY = hit.value
                },
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(72.dp)) {
                val c = Offset(size.width / 2f, size.height / 2f)
                val r = size.minDimension / 2f
                drawCircle(
                    brush = Brush.radialGradient(
                        listOf(PinkSoft.copy(alpha = 0.30f), Color.Transparent),
                        center = c, radius = r
                    ),
                    radius = r, center = c
                )
                for (i in 0 until 5) {
                    val ang = (i * 72.0 - 90.0) * PI / 180.0
                    val px = c.x + (r * 0.42f * cos(ang)).toFloat()
                    val py = c.y + (r * 0.42f * sin(ang)).toFloat()
                    drawCircle(
                        brush = Brush.radialGradient(
                            listOf(GardeniaWhite, GardeniaWhite.copy(alpha = 0.72f)),
                            center = Offset(px, py), radius = r * 0.36f
                        ),
                        radius = r * 0.33f, center = Offset(px, py)
                    )
                }
                drawCircle(color = ScratchGold.copy(alpha = 0.9f), radius = r * 0.17f, center = c)
                drawCircle(color = PinkSoft, radius = r * 0.09f, center = c)
            }
        }

        // 终身累计的次数：小字、无单位、无上限，太长就换行
        // 固定挂在花右侧、不参与居中，所以花始终在屏幕正中
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(x = 104.dp)
                .width(84.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                count.toString(),
                color = PinkSoft.copy(alpha = 0.85f),
                fontSize = 12.sp,
                lineHeight = 15.sp,
                maxLines = 4,
                textAlign = TextAlign.Start
            )
        }
    }
}

/**
 * 满屏飘字层：从**手机屏幕最底端**生成、缓缓升到**最顶端**再消散。
 * 只画字，不吃触摸，所以不挡下面列表的滑动与点击。
 * 放在窗口根节点上，所以整段行程就是整块屏幕（连顶栏、底栏一起穿过）。
 *
 * 观感与最初完全一致（同一套位置/忽大忽小/明暗/消散常量，微光还是那团柔和的径向渐变）。
 * 性能上是另一套做法：
 *   · 全部字画在**同一个 Canvas** 里 —— 以前是每个字一个 composable ＋ 一层 graphicsLayer
 *     （连点几十下就有几十个节点、几十层离屏合成）；
 *   · 全部字共用**一个帧时钟** —— 以前是每个字一条 Animatable 协程、每帧各写一次状态；
 *   · 重复的文本只排版一次并缓存 —— 语录总共就那几十句，量过一次就一直用；
 *   · 微光改成**贴一张预先烤好的柔光图** —— 以前每画一个字都要新建一支径向渐变 shader，
 *     一百个字就是每帧一百支，这是之前卡顿的主因；现在每个字只是贴一次图。
 *   · 屏幕外的字直接跳过；字特别多时微光自动变淡，先保住字本身。
 */
@Composable
fun FloatingNames(words: MutableList<FishWord>, onExpire: (FishWord) -> Unit) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val density = LocalDensity.current
        val travelPx = with(density) { (maxHeight + 44.dp).toPx() }
        val maxTextPx = with(density) { 250.dp.roundToPx() }
        val measurer = rememberTextMeasurer()
        // 排好版就不再动的文本（key = 语录/名字 + 内容）
        val laid = remember { mutableMapOf<String, TextLayoutResult>() }
        // 每种飘字颜色一张柔光图（整个界面里只会建这一次，之后一直贴）
        val glows = remember { GlowTints.map { makeGlowBitmap(it) } }

        // 唯一的帧时钟：只用来「每帧把画面标脏」，时间本身取 System.nanoTime()。
        // 一个字都没有时不转，省电。
        val hasWords = words.isNotEmpty()
        val frameNs = remember { mutableLongStateOf(0L) }
        LaunchedEffect(hasWords) {
            if (!hasWords) return@LaunchedEffect
            var lastSweep = 0L
            while (true) {
                withFrameNanos { t ->
                    frameNs.value = t
                    // 每 150ms 收一次尾：飘完的字交还列表（比每个字各起一条协程便宜）
                    if (t - lastSweep > 150_000_000L) {
                        lastSweep = t
                        words.filter { t - it.bornNs >= it.durMs * 1_000_000L }
                            .forEach(onExpire)
                    }
                }
            }
        }

        Canvas(modifier = Modifier.fillMaxSize()) {
            val tick = frameNs.value      // 订阅帧时钟：每帧重画一次
            if (tick == 0L) return@Canvas
            val now = System.nanoTime()
            val total = words.size
            // 每帧最多画这么多：列表里的顺序是「先出生的在前」，
            // 所以超出预算时丢掉的是最早那批 —— 它们已经飘到最上面、也快散没了
            val from = if (total > MAX_DRAWN) total - MAX_DRAWN else 0
            // 字越多微光越淡（到 80 个字才完全没有）：先保住字本身不卡
            val glowScale = ((80 - (total - from)) / 40f).coerceIn(0f, 1f)

            for (i in from until total) {
                val w = words[i]
                val p = ((now - w.bornNs) / 1_000_000f) / w.durMs
                if (p < 0f || p > 1f) continue
                val y = size.height - travelPx * p
                // 已经出了屏幕的字直接不画
                if (y < -80f || y > size.height + 80f) continue
                val breathe = 0.62f + 0.30f * sin(p * 6.2832f * 2.2f + w.phase)
                val fadeOut = 1f - ((p - 0.68f) / 0.32f).coerceIn(0f, 1f)
                val fadeIn = (p / 0.05f).coerceAtMost(1f)
                val alpha = (breathe * fadeIn * fadeOut).coerceIn(0f, 1f)
                if (alpha <= 0.01f) continue
                // 忽大忽小
                val sc = w.baseScale * (1f + 0.16f * sin(p * 6.2832f * 1.6f + w.phase))
                val cx = size.width / 2f + (w.startFrac - 0.5f) * size.width + w.driftPx * p

                val key = (if (w.isQuote) "q" else "n") + w.text
                val lr = laid[key] ?: measurer.measure(
                    text = AnnotatedString(w.text),
                    style = TextStyle(
                        fontSize = if (w.isQuote) 14.sp else 15.sp,
                        fontWeight = if (w.isQuote) FontWeight.Normal else FontWeight.Medium,
                        textAlign = TextAlign.Center
                    ),
                    maxLines = 2,
                    softWrap = true,
                    constraints = Constraints(maxWidth = maxTextPx)
                ).also { laid[key] = it }
                val tint = w.tint

                withTransform({
                    translate(left = cx, top = y)
                    scale(scaleX = sc, scaleY = sc, pivot = Offset.Zero)
                }) {
                    // 一点微光：一张「本色 → 全透明」的柔光图，贴上去就是那团光晕
                    val glow = (breathe * fadeOut).coerceIn(0f, 1f) * glowScale
                    if (glow > 0.02f) {
                        val gr = if (w.isQuote) 48.dp.toPx() else 37.dp.toPx()
                        val d = (gr * 2f).roundToInt()
                        drawImage(
                            image = glows[w.glowIndex.coerceIn(0, glows.lastIndex)],
                            dstOffset = IntOffset(-gr.roundToInt(), -gr.roundToInt()),
                            dstSize = IntSize(d, d),
                            alpha = (if (w.isQuote) 0.17f else 0.22f) * glow,
                            filterQuality = FilterQuality.Low
                        )
                    }
                    drawText(
                        textLayoutResult = lr,
                        topLeft = Offset(-lr.size.width / 2f, -lr.size.height / 2f),
                        color = tint.copy(alpha = alpha)
                    )
                }
            }
        }
    }
}

fun moodIntensityText(v: Int): String = when {
    v > 0 -> "强度 +$v"
    v < 0 -> "强度 $v"
    else -> "强度 0"
}

/* ---------------------------- 心情页（熵记 · 第四栏） ---------------------------- */

@Composable
fun MoodPage(
    ctx: Context,
    dayKey: String,
    onDayKeyChange: (String) -> Unit,
    onDataChanged: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showCalendar by remember { mutableStateOf(false) }
    var showPicker by remember { mutableStateOf(false) }
    var pendingDelete by remember { mutableStateOf<Store.MoodEntry?>(null) }
    var tick by remember { mutableIntStateOf(0) }

    val isToday = dayKey == Dates.todayKey()
    // 心情条目 / 方差项 / 冻结值：全部后台读取，避免切到心情页时的卡顿
    var moods by remember { mutableStateOf<List<Store.MoodEntry>?>(null) }
    var swing by remember { mutableDoubleStateOf(0.0) }
    var locked by remember { mutableDoubleStateOf(-1.0) }
    LaunchedEffect(tick, dayKey) {
        val (m, s, k) = withContext(Dispatchers.IO) {
            Triple(
                Store.loadDayMoods(ctx, dayKey),
                Store.loadDayMoodSwing(ctx, dayKey),
                Store.loadDayLockedMood(ctx, dayKey)
            )
        }
        moods = m
        swing = s
        locked = k
    }
    val baseSum = remember(tick, dayKey, moods) { (moods ?: emptyList()).sumOf { it.ae } }
    val moodTotal = if (!isToday && locked >= 0) locked else baseSum + swing

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {

            // ---------- 日期选择（与行迹页一致，点中间开日历） ----------
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PillButton("‹") { onDayKeyChange(Dates.shift(dayKey, -1)) }
                Spacer(Modifier.width(8.dp))
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(PanelBg)
                        .goldEdge(14.dp)
                        .clickable { showCalendar = true }
                        .padding(vertical = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        Dates.cnOf(dayKey) + "  " + Dates.weekName(Dates.parse(dayKey)),
                        color = TextMain, fontSize = 15.sp, fontWeight = FontWeight.Medium
                    )
                    Text(
                        if (isToday) "今天 · 点击选择其他日期" else "点击选择日期 · 该日已冻结",
                        color = if (isToday) PinkSoft else TextFaint,
                        fontSize = if (isToday) 12.sp else 10.sp,
                        fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
                    )
                }
                Spacer(Modifier.width(8.dp))
                PillButton("›") { onDayKeyChange(Dates.shift(dayKey, 1)) }
            }

            // ---------- 当日心情 AE 拆解 ----------
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "AE " + String.format(Locale.CHINA, "%.1f", moodTotal),
                    color = GoldSoft, fontSize = 12.sp, fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.weight(1f))
                Text(
                    "心情基础熵值 " + String.format(Locale.CHINA, "%.1f", baseSum) +
                            " ＋ 情绪方差项 " + String.format(Locale.CHINA, "%.1f", swing),
                    color = TextFaint, fontSize = 10.sp
                )
            }
            if (!isToday) {
                Text(
                    "  此日期已冻结：补记或删除都不会改变当日行动熵",
                    color = TextFaint, fontSize = 10.sp,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }

            // ---------- 条目列表 ----------
            Box(modifier = Modifier.weight(1f)) {
                val moodList = moods
                if (moodList == null) {
                    EmptyHint("正在读取…", " ")
                } else if (moodList.isEmpty()) {
                    EmptyHint("这一天还没有心情记录。", Persona.hintMood())
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(start = 14.dp, end = 14.dp, top = 4.dp, bottom = 72.dp)
                    ) {
                        // 同一刻一起选的心情并成一条显示：那一刻本来就是混着的，
                        // 拆成好几张卡反而看不出它们是一起发生的。
                        // 注意只是**显示**上并起来 —— 存还是各存各的，熵也各算各的。
                        val moodGroups = moodList.sortedByDescending { it.ts }.groupBy { it.ts }
                        items(moodGroups.values.toList(), key = { g -> g.first().id }) { g ->
                            val m = g.first()
                            val e = AE.resolve(m.emotion)
                            val moodNames = g.joinToString("/") { it.emotion }
                            val groupAe = g.fold(0.0) { a, x -> a + x.ae }
                            // 极性/权重也按**整组**算，否则并起来的几个名字后面
                            // 挂着的是第一个心情的数字，看着对不上。
                            // 只有一个心情时这两个式子退化成它自己的值，和原来一模一样。
                            val gEmos = g.map { AE.resolve(it.emotion) }
                            val groupWeight = gEmos.fold(0.0) { a, x -> a + x.weight }
                            val groupValence = if (groupWeight > 0.0)
                                gEmos.fold(0.0) { a, x -> a + x.weight * x.valence } / groupWeight
                            else e.valence
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(PanelBg)
                                    .goldEdge(14.dp)
                                    .border(
                                        1.dp, GoldenDay.border(GoldSoft.copy(alpha = 0.16f)),
                                        RoundedCornerShape(14.dp)
                                    )
                                    .padding(12.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    // 「心情/心情/心情」——选得多时会很长，所以这一条
                                    // 连同极性与权重横着能滑：不换行，也不会把右边的熵值挤掉。
                                    Row(
                                        modifier = Modifier
                                            .weight(1f)
                                            .horizontalScroll(rememberScrollState()),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            moodNames,
                                            color = PinkSoft, fontSize = 15.sp,
                                            fontWeight = FontWeight.Medium, maxLines = 1
                                        )
                                        Spacer(Modifier.width(8.dp))
                                        Tag("极性 " + String.format(Locale.CHINA, "%.2f", groupValence))
                                        Spacer(Modifier.width(6.dp))
                                        Tag("权重 " + String.format(Locale.CHINA, "%.2f", groupWeight))
                                    }
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        "+" + String.format(Locale.CHINA, "%.1f", groupAe),
                                        color = GoldSoft, fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                Spacer(Modifier.height(6.dp))
                                if (m.note.isNotBlank()) {
                                    Text(
                                        m.note,
                                        color = TextMain.copy(alpha = 0.9f), fontSize = 12.sp
                                    )
                                    Spacer(Modifier.height(5.dp))
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        moodIntensityText(m.intensity),
                                        color = TextMuted, fontSize = 11.sp
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        "位置 " + String.format(
                                            Locale.CHINA, "%.2f",
                                            AE.moodPosition(e, m.intensity)
                                        ),
                                        color = TextFaint, fontSize = 10.sp
                                    )
                                    Spacer(Modifier.weight(1f))
                                    Text(Dates.stamp(m.ts), color = TextFaint, fontSize = 10.sp)
                                    Spacer(Modifier.width(8.dp))
                                    SmallAction("分享", PinkSoft) {
                                        AiShare.request(
                                            label = "心情 · " + m.emotion,
                                            body = shareBodyOf(
                                                kind = "心情",
                                                title = m.emotion,
                                                lines = listOf(
                                                    "日期：" + dayKey,
                                                    moodIntensityText(m.intensity),
                                                    "情绪熵：" + String.format(Locale.CHINA, "%.1f", m.ae)
                                                ),
                                                body = m.note
                                            )
                                        )
                                    }
                                    Spacer(Modifier.width(4.dp))
                                    Text(
                                        "✕",
                                        color = TextFaint.copy(alpha = 0.7f),
                                        fontSize = 12.sp,
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .clickable { pendingDelete = m }
                                            .padding(3.dp)
                                    )
                                }
                            }
                        }
                        item { Spacer(Modifier.height(10.dp)) }
                    }
                }
                // 按钮浮在列表之上：这一行自己不铺底，条目滑到它下面时能从缝隙里透出来
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(lerp(NightBottom, PurpleDeep, 0.35f))
                        .border(
                            1.dp, GoldenDay.border(GoldSoft.copy(alpha = 0.30f)),
                            RoundedCornerShape(16.dp)
                        )
                        .clickable { showPicker = true }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("＋  记录此刻的心情", color = TextMain, fontSize = 15.sp)
                }
                }
            }
            Spacer(Modifier.height(6.dp))
        }

        if (showCalendar) {
            CalendarOverlay(
                dayKey = dayKey,
                onPick = { onDayKeyChange(it); showCalendar = false },
                onClose = { showCalendar = false }
            )
        }

        if (showPicker) {
            MoodPickerOverlay(
                onDismiss = { showPicker = false },
                onSave = { chosen, intensity, note ->
                    // 多选时每一条**各存一条**：熵值是按单条算的，
                    // 合并成一条会把好几份心情的权重压成一份 —— 那就少算了。
                    // 整批共用一个时间戳：列表按它把同一刻的几条并成一条显示。
                    val stamp = System.currentTimeMillis()
                    chosen.forEach { one ->
                        Store.addMood(ctx, dayKey, one, intensity, note, stamp)
                    }
                    // 不管选了几个心情，都只算**一条**记录 ——
                    // 所以主动互动也只发一次，否则会连弹好几个窗。
                    if (chosen.isNotEmpty()) {
                        AiProactive.notify(
                            ctx, AiEvent.MOOD,
                            "我刚记下一条心情：" + chosen.joinToString("、") +
                                    "，强度 " + (if (intensity > 0) "+" else "") + intensity +
                                    (if (note.isNotBlank()) "。我写了一句备注：「" + note.trim() + "」" else "")
                        )
                    }
                    showPicker = false
                    tick++
                    onDataChanged()
                }
            )
        }

        // 同一刻一起记下的条数（多选时才会大于 1）—— 确认文案里要说清是整条删
        val moodSameStamp = pendingDelete?.let { d ->
            moods?.count { it.ts == d.ts } ?: 1
        } ?: 1
        pendingDelete?.let { m ->
            ConfirmOverlay(
                title = "删除这条心情记录？",
                // 同一刻一起记下的算一条，所以这里说的是整条，不是其中某一个情绪
                message = "「${m.emotion}" +
                        (if (moodSameStamp > 1) " 等 $moodSameStamp 个情绪" else "") +
                        " · ${moodIntensityText(m.intensity)}」（${Dates.stamp(m.ts)}）将被永久删除。" +
                        if (isToday) "\n今天的记录可以删改，熵值会实时重算。" else "\n该日期已冻结，删除不会改变当日行动熵。",
                onConfirm = {
                    Store.deleteMoodStamp(ctx, dayKey, m.ts)
                    pendingDelete = null
                    tick++
                    onDataChanged()
                },
                onCancel = { pendingDelete = null }
            )
        }
    }
}

/* ---------------------------- 心情记录浮层 ---------------------------- */

@Composable
fun MoodPickerOverlay(onDismiss: () -> Unit, onSave: (List<String>, Int, String) -> Unit) {
    // 系统返回键 / 边缘返回手势 = 取消记录心情
    BackHandler { onDismiss() }
    // 默认单选：某一刻最接近的情绪通常只有一个。
    // 想记「又累又踏实」这种混着的，先点一下「多选」再点情绪。
    var multi by remember { mutableStateOf(false) }
    var picked by remember { mutableStateOf(setOf(AE.Emotions.first().name)) }
    var custom by remember { mutableStateOf("") }        // 自己填的心情标签
    var intensity by remember { mutableIntStateOf(0) }
    /** 备注：可空 */
    var note by remember { mutableStateOf("") }
    // 填了自定义标签就用它；否则用点选的情绪
    // 自己填了一个就用它（一次一条）；否则用点选的那几个（可以多选）。
    val labels = if (custom.trim().isNotBlank()) listOf(custom.trim()) else picked.toList()
    val isCustom = custom.trim().isNotBlank() && AE.knownEmotion(custom) == null
    val emos = labels.map { AE.resolve(it) }
    // 多选时各算各的，这里只是给个合计看看
    val ae = emos.fold(0.0) { a, e -> a + AE.moodBase(e, intensity) }
    // 一个都没选时下面那句预览要有个落脚点，否则 first() 会崩
    val previewEmo = emos.firstOrNull() ?: AE.resolve("平静")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Veil)
            // 同上：这一页在分页容器里面，外层已经让过键盘，这里再加一次会多出空白。
            .swallowClicks { onDismiss() },
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.88f)
                .clip(RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp))
                .background(PanelBg)
                .goldEdge(RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp))
                .border(
                    1.dp, GoldenDay.border(GoldSoft.copy(alpha = 0.25f)),
                    RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp)
                )
                .verticalScroll(rememberScrollState())
                .swallowClicks()
                .padding(11.dp)
        ) {
            Text("此刻的心情", color = TextMain, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(4.dp))
            Text(
                "先选一个最接近的情绪，再拖动强度条。",
                color = TextFaint, fontSize = 11.sp
            )
            Spacer(Modifier.height(14.dp))

            // ---- 情绪按钮 ----
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("情绪", color = TextMuted, fontSize = 12.sp)
                Spacer(Modifier.weight(1f))
                // 默认单选；点开才多选。每次打开这一页都回到单选。
                Chip(if (multi) "多选 ✓" else "多选", multi) {
                    multi = !multi
                    // 收起来的时候只留一个：留着好几个却按单选显示，会让人看不懂
                    if (!multi && picked.size > 1) picked = setOf(picked.first())
                }
            }
            Spacer(Modifier.height(8.dp))
            AE.Emotions.chunked(4).forEach { rowItems ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    rowItems.forEach { item ->
                        Box(modifier = Modifier.padding(end = 6.dp, bottom = 6.dp)) {
                            val on = custom.isBlank() && item.name in picked
                            Chip(item.name, on) {
                                picked = if (!multi) {
                                    // 单选：点哪个就是哪个
                                    setOf(item.name)
                                } else if (on) {
                                    // 多选：再点一下取消。全取消也可以 ——
                                    // 真取消了又没自定义，保存就等于取消，不会记下空的一条。
                                    picked - item.name
                                } else {
                                    picked + item.name
                                }
                                custom = ""
                            }
                        }
                    }
                }
            }

            // ---- 自己填一个更准确的心情 ----
            Spacer(Modifier.height(6.dp))
            OutlinedTextField(
                value = custom,
                onValueChange = { v -> custom = v.take(8) },
                label = { Text("或者自己写一个（最多 8 字）", color = TextMuted, fontSize = 11.sp) },
                singleLine = true,
                textStyle = TextStyle(color = TextMain, fontSize = 14.sp),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(4.dp))
            Text(
                when {
                    custom.isBlank() -> "填了就用你写的；留空则用上面点选的情绪。"
                    isCustom -> "「${custom.trim()}」不在 20 种之内：按中性极性 0、权重 " +
                            String.format(Locale.CHINA, "%.2f", AE.CUSTOM_WEIGHT) + " 计熵（强度照常）"
                    else -> "「${custom.trim()}」就是固定情绪之一，按它的固定规则计熵。"
                },
                color = if (isCustom) GoldSoft.copy(alpha = 0.9f) else TextFaint,
                fontSize = 10.sp
            )

            Spacer(Modifier.height(10.dp))
            // ---- 强度 ----
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("强度", color = TextMuted, fontSize = 12.sp)
                Spacer(Modifier.width(10.dp))
                Text(
                    when {
                        intensity > 0 -> "偏开心 +$intensity"
                        intensity < 0 -> "偏难过 $intensity"
                        else -> "平静 0"
                    },
                    color = PinkSoft, fontSize = 12.sp, fontWeight = FontWeight.Medium
                )
            }
            Spacer(Modifier.height(6.dp))
            SoftSlider(
                value = intensity.toFloat(),
                valueRange = -5f..5f,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { v -> intensity = v.roundToInt().coerceIn(-5, 5) }
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("难过 −5", color = TextMuted, fontSize = 10.sp)
                Spacer(Modifier.weight(1f))
                Chip("归零", intensity == 0) { intensity = 0 }
                Spacer(Modifier.weight(1f))
                Text("开心 +5", color = PinkSoft, fontSize = 10.sp)
            }

            // ---- 备注（可以不写） ----
            Text("备注（可以不写）", color = TextMuted, fontSize = 12.sp)
            Spacer(Modifier.height(6.dp))
            OutlinedTextField(
                value = note,
                onValueChange = { note = it.take(200) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text("想补一句就写在这里，例如「因为今天那件事」", color = TextFaint, fontSize = 11.sp)
                },
                textStyle = TextStyle(fontSize = 13.sp),
                maxLines = 3
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "备注只跟着这条心情走，会一起写进导出的文档与数据包；不参与行动熵的计算。",
                color = TextFaint, fontSize = 10.sp
            )

            Spacer(Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(PanelBgSoft)
                    .goldEdge(14.dp)
                    .padding(11.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("这条心情", color = TextMuted, fontSize = 12.sp)
                        Spacer(Modifier.weight(1f))
                        Text(
                            "+" + String.format(Locale.CHINA, "%.1f", ae) + " AE",
                            color = GoldSoft, fontSize = 18.sp, fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "= 承受权重 " + String.format(Locale.CHINA, "%.2f", previewEmo.weight) +
                                " × (1 + |强度| × 0.6)　·　情绪极性 " +
                                String.format(Locale.CHINA, "%.2f", previewEmo.valence),
                        color = TextFaint, fontSize = 10.sp
                    )
                    Text(
                        "情绪越极端、越偏负向，行动熵越高；当日多条记录之间的起伏会另计「情绪方差项」。",
                        color = TextFaint, fontSize = 10.sp
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
            Row {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(PurpleDeep.copy(alpha = 0.45f))
                        .goldEdge(14.dp)
                        .clickable {
                            // 一个都没选、也没自定义心情 —— 这时候「保存」等同于取消，
                            // 免得记下一条空的心情。
                            if (labels.isEmpty()) onDismiss()
                            else onSave(labels, intensity, note)
                        }
                        .padding(vertical = 13.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("记下", color = TextMain, fontSize = 15.sp, fontWeight = FontWeight.Medium)
                }
                Spacer(Modifier.width(10.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(PanelBgSoft)
                        .goldEdge(14.dp)
                        .swallowClicks { onDismiss() }
                        .padding(horizontal = 20.dp, vertical = 13.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("取消", color = TextMuted, fontSize = 15.sp)
                }
            }
            Spacer(Modifier.height(20.dp))
        }
    }
}

/* ==========================================================================
 *  十二、栏目三：花房（凌薇花 · 她的运行日志 · 统计图表）
 * ======================================================================== */

/**
 * 花房这一页。文字里的数字**全是终身累计**的（不按日/月/年），
 * 块的顺序：凌薇花 → 她还在运行（含她的三个日子与相伴天数）→ 我记得你 → 数据导出。
 * 整页还铺着一层神经网：块里密、块与块之间稀，偶尔有一处亮起来。
 */
@Composable
fun FlowerScreen(
    ctx: Context,
    idx: Store.StatsIndex?,
    refreshTick: Int,
    /** 终身累计：你叫过她的次数（点联结页那朵花） */
    knock: Long,
    /** 从备份恢复完，让全局数据重新读一遍 */
    onDataChanged: () -> Unit,
    /** 花房这一页当前是否真的显示在屏幕上（不在就不跑那些动效，省电） */
    active: Boolean
) {
    var exportMsg by remember { mutableStateOf("") }
    /** 当前正在跑的任务："" 空闲 / "doc" 导出文档 / "zip" 导出数据包 / "import" 从备份恢复 */
    var busyTask by remember { mutableStateOf("") }
    /** 自动产物的占用情况（给人看一眼「它不会涨」） */
    var usageText by remember { mutableStateOf("") }
    val exportScope = rememberCoroutineScope()

    LaunchedEffect(busyTask) {
        if (busyTask.isEmpty()) {
            usageText = withContext(Dispatchers.IO) { Pub.autoUsageText(ctx) }
        }
    }

    // 从备份恢复：选一个 .zip（数据包或镜像），覆盖写回数据目录；恢复前会自动留底
    val importPicker = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri == null) {
            busyTask = ""
            exportMsg = "已取消恢复"
        } else {
            busyTask = "import"
            exportMsg = "正在恢复数据…"
            exportScope.launch {
                exportMsg = withContext(Dispatchers.IO) { Pub.importZip(ctx, uri) }
                busyTask = ""
                onDataChanged()
            }
        }
    }

    // 画像索引：进花房时读一次
    LaunchedEffect(refreshTick) { withContext(Dispatchers.IO) { Gallery.load(ctx) } }
    val companionDays = Persona.daysSinceStart()
    // 步数换算要用到步幅/体重：只在数据变化时读一次配置
    val scfg = remember(refreshTick) { Steps.cfg(ctx) }

    // 花房共用：按日的行动熵 —— 只在数据变化时算一次，花、螺旋、记忆阵列都用它
    val daySeries = remember(idx) {
        if (idx == null) emptyList()
        else {
            val keys = (idx.traceByDay.keys + idx.docByDay.keys + idx.moodByDay.keys +
                    idx.namesByDay.keys + idx.stepsByDay.keys).sorted()
            keys.map { it to idx.day(it) }
        }
    }

    // 花房页的密网时钟：只在真的看着这一页时走
    // （20fps 就够 —— 这里所有动效的周期都是 5~300 秒，30fps 纯属浪费）
    neuralTick(NeuralClock.page, active = active, fps = 16)

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // 整页一张网（画在所有块后面）：块与块之间的连线从块底下钻出来
            Box(modifier = Modifier.fillMaxWidth()) {
                NeuralPageMesh(
                    active = active,
                    modifier = Modifier.matchParentSize()
                )
                Column(modifier = Modifier.padding(horizontal = 12.dp)) {

                    // ---------- 画像：花房最上面那一栏 ----------
                    PortraitCard(ctx = ctx, active = active)

                    Spacer(Modifier.height(14.dp))

                    // ---------- 斐波那契之花：计数器，也是账本 ----------
                    MathFlowerCard(idx = idx, days = daySeries, active = active)

                    Spacer(Modifier.height(14.dp))

                    // ---------- 熵螺旋 + 记忆阵列：一天一格，数据长在这条线上 ----------
                    AeSpiralCard(days = daySeries, active = active)

                    Spacer(Modifier.height(14.dp))

                    // ---------- 我记得你：唯一的 0/1，写成一条安静的河 ----------
                    ZeckendorfCard(knock = knock, active = active)

                    Spacer(Modifier.height(14.dp))

                    // ---------- 她还在运行：累计信息 + 她自己的字段 ----------
                    RuntimeCard(
                        idx = idx,
                        knock = knock,
                        companionDays = companionDays,
                        strideCm = scfg.strideCm,
                        weightKg = scfg.weightKg,
                        active = active
                    )

                    Spacer(Modifier.height(14.dp))

                    // ---------- 数据导出 ----------
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(PanelBg)
                            .neuralBackdrop(seed = 1301, active = active, cols = 6, rows = 3)
                            .goldEdge(20.dp)
                            .padding(16.dp)
                    ) {
                        Text("数据", color = TextMain, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        Spacer(Modifier.height(6.dp))
                        Text(
                            "记录与 AI 是同一套数据，所以只出一份镜像、一份文档：",
                            color = TextFaint, fontSize = 11.sp
                        )
                        Spacer(Modifier.height(6.dp))
                        Text("① 镜像：整个数据目录，一个 zip　每天一份，留 3 天", color = TextFaint, fontSize = 10.sp)
                        Text(Pub.pathText(Pub.SUB_MIRROR), color = TextMuted, fontSize = 10.sp)
                        Text("② 自动文档：记录 + AI 合成一份 Word　每天一份，留 3 份", color = TextFaint, fontSize = 10.sp)
                        Text(Pub.pathText(Pub.SUB_AUTO), color = TextMuted, fontSize = 10.sp)
                        Text("③ 手动：文档与完整数据包，不限份数，不会被自动清理", color = TextFaint, fontSize = 10.sp)
                        Text(Pub.pathText(Pub.SUB_MANUAL), color = TextMuted, fontSize = 10.sp)
                        Text(
                            "上次自动备份：" + (AutoBackup.lastDay(ctx) ?: "还没备份过"),
                            color = TextFaint, fontSize = 10.sp
                        )
                        Spacer(Modifier.height(6.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(PanelBgSoft)
                                .padding(horizontal = 10.dp, vertical = 7.dp)
                        ) {
                            Text(
                                if (usageText.isBlank()) "正在统计占用…" else "当前占用：" + usageText,
                                color = PinkSoft, fontSize = 10.sp
                            )
                        }
                        Spacer(Modifier.height(6.dp))
                        Text(
                            "镜像里装的是整个数据目录：行迹与心情、文章与日记（含备注）、唤名、\n" +
                                    "自定义预设与图标、画像、插入的图片与视频，以及 AI 的人设卡 / 对话 /\n" +
                                    "长期记忆 / 头像 / 表情包。唯一的例外是 API Key —— 它单独放在数据目录顶层，\n" +
                                    "不属于任何子目录，所以永远不会被写进任何导出文件。",
                            color = TextFaint, fontSize = 10.sp
                        )
                        Spacer(Modifier.height(6.dp))
                        Text("记录本体在手机的 Asephina 目录（会随卸载消失）：", color = TextFaint, fontSize = 11.sp)
                        Spacer(Modifier.height(4.dp))
                        Text(Store.pathText(ctx), color = TextMuted, fontSize = 10.sp)
                        Spacer(Modifier.height(10.dp))
                        Row {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(PanelBgSoft)
                                    .goldEdge(12.dp)
                                    .clickable {
                                        try {
                                            val cm = ctx.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                            cm.setPrimaryClip(
                                                ClipData.newPlainText("AsephinaPath", Store.pathText(ctx))
                                            )
                                            Toast.makeText(ctx, "路径已复制", Toast.LENGTH_SHORT).show()
                                        } catch (e: Exception) {
                                        }
                                    }
                                    .padding(horizontal = 12.dp, vertical = 10.dp)
                            ) {
                                Text("复制数据路径", color = TextMuted, fontSize = 12.sp)
                            }
                            Spacer(Modifier.width(10.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(PurpleDeep.copy(alpha = 0.4f))
                                    .goldEdge(12.dp)
                                    .clickable {
                                        if (busyTask.isEmpty()) {
                                            busyTask = "doc"
                                            exportMsg = "正在生成 Word 文档…"
                                            exportScope.launch {
                                                exportMsg = withContext(Dispatchers.IO) {
                                                    Store.exportWordDoc(ctx)
                                                }
                                                busyTask = ""
                                            }
                                        }
                                    }
                                    .padding(horizontal = 12.dp, vertical = 10.dp)
                            ) {
                                Text(
                                    if (busyTask == "doc") "导出中…" else "导出 Word 文档",
                                    color = TextMain, fontSize = 12.sp
                                )
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                        Row {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(PurpleDeep.copy(alpha = 0.4f))
                                    .goldEdge(12.dp)
                                    .clickable {
                                        if (busyTask.isEmpty()) {
                                            busyTask = "zip"
                                            exportMsg = "正在打包全部数据…"
                                            exportScope.launch {
                                                exportMsg = withContext(Dispatchers.IO) {
                                                    Pub.exportZip(ctx)
                                                }
                                                busyTask = ""
                                            }
                                        }
                                    }
                                    .padding(horizontal = 12.dp, vertical = 10.dp)
                            ) {
                                Text(
                                    if (busyTask == "zip") "打包中…" else "导出数据包（.zip）",
                                    color = TextMain, fontSize = 12.sp
                                )
                            }
                            Spacer(Modifier.width(10.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(PanelBgSoft)
                                    .goldEdge(12.dp)
                                    .clickable {
                                        if (busyTask.isEmpty()) {
                                            exportMsg = ""
                                            importPicker.launch(
                                                arrayOf(
                                                    "application/zip",
                                                    "application/octet-stream",
                                                    "*/*"
                                                )
                                            )
                                        }
                                    }
                                    .padding(horizontal = 12.dp, vertical = 10.dp)
                            ) {
                                Text(
                                    if (busyTask == "import") "恢复中…" else "从备份恢复",
                                    color = TextMain, fontSize = 12.sp
                                )
                            }
                        }
                        if (exportMsg.isNotBlank()) {
                            Spacer(Modifier.height(8.dp))
                            Text(exportMsg, color = PinkSoft, fontSize = 11.sp)
                        }
                    }

                    Spacer(Modifier.height(10.dp))
                    Text(
                        "意义在于创造而非赋予，追寻而非实现。",
                        color = TextFaint, fontSize = 11.sp, textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(24.dp))
                }
            }
        }
    }
}

/* ---------------------------- 神经网背景：深海里安静发光的一张网 ---------------------------- */

/*
 * 想要的感觉：它在背后安静地算着什么。
 *   · 密集的节点与连线，分布均匀、有序，像一张无限延伸的网；
 *   · 节点有微弱的呼吸，连线有轻微的数据流动感；
 *   · 整体偏冷：蓝紫、深蓝、暗色，低对比、低亮度、低速度，绝不抢前景；
 *   · 抽象 —— 没有文字、没有公式、没有电路板那套具象东西。
 * 参考的是深夜机房那种安静，和深海里浮游生物那样缓慢的明灭。
 *
 * 做法（也是它不卡的原因）：
 *   · 节点摆在抖动网格上（有序不散乱），同一个种子永远长成同一张网；
 *   · 连线是直线（点与点直接连），按 6 个亮度桶**一次烘成 6 条 Path**，
 *     每帧只换颜色 + 用「流动的虚线」表现数据在走，几何形状不重建；
 *   · 只跑 30fps（页面内）/ 15fps（环境），本来就慢，看不出差别。
 */

/**
 * 六档冷色：最暗那一档贴近底色（底噪），往上几档是看得见的那部分。
 * 高亮节点用最后两档（#6b5b9a / #8a7ab8）。
 * 冷、低亮度 —— 但必须**看得见**：太淡就等于没画。
 */
private val NET_COLORS: List<Color> get() = Theme.current.netColors

private const val NET_BUCKETS = 6

/**
 * 一条「会走」的神经信号：沿着 [path] 里那串节点一站一站地走，
 * [period] 秒走完一趟，[phase] 错开出发时间。
 * 这是「数字生命在实时计算」的那点动静 —— 很少几条，很慢，两端淡入淡出。
 */
private class NetSignal(val path: IntArray, val period: Float, val phase: Float)

/** 一个节点：位置用相对坐标（0~1），同一张网可以铺到任意尺寸 */
private class NetNode(
    val x: Float,
    val y: Float,
    val phase: Float,
    /** 半径系数：最后会压到 1~2px */
    val size: Float,
    /** 高亮节点（少数几个，亮度也不超过 30%） */
    val bright: Boolean
)

/** 一条连线：两端下标、属于哪个亮度桶（直线，不再画弯的） */
private class NetEdge(val a: Int, val b: Int, val bucket: Int)

/** 一张网：节点 / 连线 / 六个亮度桶的呼吸相位 / 几条会走信号的路径 */
private class Net(
    val nodes: List<NetNode>,
    val edges: List<NetEdge>,
    val bucketPhase: FloatArray,
    val signals: List<NetSignal>
)

/**
 * 生成一张网。
 *   · 节点摆在抖动网格上（有序、不散乱），同一个种子永远长成同一张网；
 *   · 每个节点只连最近的 [reaches] 条，并且**硬性限制**「一个节点最多 [maxDeg] 条」——
 *     规范要的 3~4 条，超了直接跳过，所以不会出现一个节点挂十几条线的蜘蛛；
 *   · 短程键走暗档、长程桥走亮档 —— 分子结构那种「键有强有弱」的规律感，
 *     复杂但不乱，正是想要的样子；
 *   · 再挑 [signalCount] 条 3~8 跳的小路当信号道，让光点偶尔沿着它走一趟。
 */
private fun makeNet(
    seed: Int,
    cols: Int,
    rows: Int,
    brightRatio: Float,
    jitter: Float,
    extraLinks: Int,
    reaches: Int,
    maxDeg: Int = 4,
    signalCount: Int = 3
): Net {
    val rnd = java.util.Random(seed.toLong())
    val nodes = ArrayList<NetNode>(cols * rows)
    for (r in 0 until rows) {
        for (c in 0 until cols) {
            nodes.add(
                NetNode(
                    x = ((c + 0.5f + (rnd.nextFloat() - 0.5f) * jitter) / cols)
                        .coerceIn(0.02f, 0.98f),
                    y = ((r + 0.5f + (rnd.nextFloat() - 0.5f) * jitter) / rows)
                        .coerceIn(0.02f, 0.98f),
                    phase = rnd.nextFloat() * 6.2832f,
                    size = 0.55f + rnd.nextFloat() * 0.85f,
                    bright = rnd.nextFloat() < brightRatio
                )
            )
        }
    }
    val n = nodes.size
    val deg = IntArray(n)
    val hasEdge = HashSet<Long>()
    val edges = ArrayList<NetEdge>()
    fun key(a: Int, b: Int) = min(a, b).toLong() * 100000L + max(a, b)
    fun link(a: Int, b: Int, bucket: Int): Boolean {
        if (a == b) return false
        if (deg[a] >= maxDeg || deg[b] >= maxDeg) return false
        if (!hasEdge.add(key(a, b))) return false
        deg[a]++
        deg[b]++
        edges.add(NetEdge(min(a, b), max(a, b), bucket.coerceIn(0, NET_BUCKETS - 1)))
        return true
    }
    // 先把「离谁最近」算一遍（同一张网只算一次，之后按序号复用）
    val near = Array(n) { i ->
        (0 until n).filter { it != i }.sortedBy { j ->
            val dx = nodes[i].x - nodes[j].x
            val dy = nodes[i].y - nodes[j].y
            dx * dx + dy * dy
        }
    }
    // 短程键：暗档（1~2）
    for (i in 0 until n) {
        near[i].take(reaches.coerceIn(1, 4)).forEach { j ->
            link(i, j, 1 + rnd.nextInt(2))
        }
    }
    // 长程桥：亮档（3~5），像分子间偶尔伸出去的一根键
    var added = 0
    var guard = 0
    while (added < extraLinks && guard < extraLinks * 16 + 32) {
        guard++
        if (link(rnd.nextInt(n), rnd.nextInt(n), 3 + rnd.nextInt(3))) added++
    }
    // 信号道：从某个节点出发，一跳一跳走到没走过的邻居。
    // 每条都带一个「大致往哪边走」的偏好，于是有的横着走、有的斜着走、有的往上走 ——
    // 光是多点几个不换花样还是会显得单调，方向不同才有「它们各自在忙」的感觉。
    val signals = ArrayList<NetSignal>(signalCount)
    var sGuard = 0
    while (signals.size < signalCount && sGuard < signalCount * 30 + 30) {
        sGuard++
        var cur = rnd.nextInt(n)
        val dirX = rnd.nextFloat() * 2f - 1f
        val dirY = rnd.nextFloat() * 2f - 1f
        val walk = ArrayList<Int>(16)
        walk.add(cur)
        // 跳数拉开档次：短的 2 跳、长的能到 13 跳
        val hops = 2 + rnd.nextInt(12)
        while (walk.size <= hops) {
            val nxt = edges.asSequence()
                .filter { it.a == cur || it.b == cur }
                .map { if (it.a == cur) it.b else it.a }
                .filter { !walk.contains(it) }
                .toList()
            if (nxt.isEmpty()) break
            // 三分之二按方向偏好挑，三分之一纯随机：既成走向，又不会条条一个样
            cur = if (rnd.nextFloat() < 0.66f) {
                nxt.maxByOrNull { j ->
                    val dx = nodes[j].x - nodes[cur].x
                    val dy = nodes[j].y - nodes[cur].y
                    (dx * dirX + dy * dirY) / (dx * dx + dy * dy + 0.02f)
                } ?: nxt[0]
            } else {
                nxt[rnd.nextInt(nxt.size)]
            }
            walk.add(cur)
        }
        if (walk.size >= 3) {
            signals.add(
                NetSignal(
                    path = walk.toIntArray(),
                    period = 7f + rnd.nextFloat() * 20f,   // 走完一趟 7~27 秒
                    phase = rnd.nextFloat()
                )
            )
        }
    }
    val phases = FloatArray(NET_BUCKETS) { rnd.nextFloat() * 6.2832f }
    return Net(nodes, edges, phases, signals)
}

/** 两个慢正弦叠出来的脉冲：0 暗 → 1 亮；很慢，绝不会突然闪 */
private fun pulse01(t: Float, phase: Float, base: Float, gain: Float): Float {
    val v = sin(t * 0.21f + phase) * 0.6f + sin(t * 0.083f + phase * 1.7f) * 0.4f
    return (base + gain * (v * 0.5f + 0.5f)).coerceIn(0f, 1f)
}

/**
 * 把一张网的直线按桶烘成 6 条 Path —— 只在尺寸变化时重建一次，
 * 之后每帧只是把这几条 Path 换个颜色、再叠一层流动的虚线。
 */
private class NetRender {

    private var w = -1f
    private var h = -1f
    private var paths: List<Path> = emptyList()
    private var nodePaths: List<Path> = emptyList()

    fun ensure(net: Net, w: Float, h: Float) {
        if (w == this.w && h == this.h && paths.isNotEmpty()) return
        this.w = w
        this.h = h
        val linePaths = Array(NET_BUCKETS) { Path() }
        val dots = Array(2) { Path() }
        net.edges.forEach { e ->
            val a = net.nodes[e.a]
            val b = net.nodes[e.b]
            val p = linePaths[e.bucket]
            p.moveTo(a.x * w, a.y * h)
            p.lineTo(b.x * w, b.y * h)
        }
        net.nodes.forEach { n ->
            // 节点半径按规范压到 1~2px
            val r = (1.2f + 1.4f * (n.size - 0.55f) / 0.85f).coerceIn(1.2f, 2.6f)
            val p = dots[if (n.bright) 1 else 0]
            p.addOval(androidx.compose.ui.geometry.Rect(n.x * w - r, n.y * h - r,
                n.x * w + r, n.y * h + r))
        }
        paths = linePaths.toList()
        nodePaths = dots.toList()
    }

    fun lines(): List<Path> = paths
    fun nodes(): List<Path> = nodePaths
}

/**
 * 把一张网画出来。
 *   · 线：6 条合成 Path（几何只在尺寸变化时烘一次），透明度压在 [maxAlpha] 以内 ——
 *     规范要求 5%~15%，所以调用方给的都是 0.15 上下；
 *   · 亮度：6 条 Path 各自极慢地呼吸，时而亮起、时而黯淡，绝不会闪；
 *   · 节点：普通 / 高亮两撮，最亮不超过 30%；
 *   · 信号：少数几条路径上有一粒光在慢慢走（深海里的光，不是霓虹灯）。
 */
private fun DrawScope.drawNet(
    net: Net,
    render: NetRender,
    timeSec: Float,
    maxAlpha: Float,
    nodeAlpha: Float,
    lineW: Float
) {
    render.ensure(net, size.width, size.height)
    val lines = render.lines()
    for (b in 0 until NET_BUCKETS) {
        val p = pulse01(timeSec, net.bucketPhase[b], 0.18f, 0.82f)
        drawPath(
            path = lines[b],
            color = NET_COLORS[b].copy(alpha = maxAlpha * (0.35f + 0.65f * p)),
            style = Stroke(width = lineW * (0.7f + 0.5f * p), cap = StrokeCap.Round)
        )
    }
    val nodes = render.nodes()
    val breath = pulse01(timeSec, net.bucketPhase[1], 0.25f, 0.5f)
    drawPath(
        path = nodes[0],
        color = NET_COLORS[2].copy(alpha = nodeAlpha * (0.5f + 0.5f * breath))
    )
    drawPath(
        path = nodes[1],
        color = NET_COLORS[4].copy(alpha = nodeAlpha * (0.55f + 0.45f * breath))
    )

    // ---- 会走的那几粒光：沿着信号道一站一站挪，两端淡入淡出 ----
    val w = size.width
    val h = size.height
    net.signals.forEach { s ->
        val seg = s.path.size - 1
        if (seg < 1 || nodeAlpha <= 0.01f) return@forEach
        val u = ((timeSec / s.period + s.phase) % 1f + 1f) % 1f
        val f = u * seg
        val i = f.toInt().coerceIn(0, seg - 1)
        val k = f - i
        val a = net.nodes[s.path[i]]
        val b = net.nodes[s.path[i + 1]]
        val x = (a.x + (b.x - a.x) * k) * w
        val y = (a.y + (b.y - a.y) * k) * h
        // sin 窗：刚出发和快到站时都暗下去，中间最亮 —— 看起来是「算完了」而不是「跳了一下」
        val fade = sin(PI.toFloat() * u).coerceIn(0f, 1f)
        // 信号比节点亮一档、只大一点点，还带软光晕 —— 小而多，像网里跑来跑去的碎光
        val al = (0.34f + 0.66f * nodeAlpha) * fade
        if (al > 0.01f) {
            val core = 1.35.dp.toPx()
            // 刚走过的那一小段拖一条淡淡的尾巴
            drawLine(
                color = NET_COLORS[4].copy(alpha = al * 0.30f),
                start = Offset(a.x * w, a.y * h),
                end = Offset(x, y),
                strokeWidth = lineW * 1.15f,
                cap = StrokeCap.Round
            )
            softGlow(GLOW_SPRITE, Offset(x, y), core * 3.4f, NET_COLORS[5], al * 0.50f)
            drawCircle(
                color = NET_COLORS[5].copy(alpha = al * 0.95f),
                radius = core,
                center = Offset(x, y)
            )
            drawCircle(
                color = Spark.copy(alpha = al * 0.40f),
                radius = core * 0.42f,
                center = Offset(x, y)
            )
        }
    }
}

/**
 * 神经网的帧时钟（两个：页面内的密网、其它页面的环境网）。
 * 30fps / 15fps —— 本来就慢，看不出差别，性能负担小得多。
 */
private object NeuralClock {
    val page = mutableLongStateOf(0L)
    val ambient = mutableLongStateOf(0L)
}

/** 跑一个时钟；active 为假时立刻停 */
@Composable
private fun neuralTick(state: MutableLongState, active: Boolean, fps: Int) {
    LaunchedEffect(active, fps) {
        if (!active) return@LaunchedEffect
        val step = 1_000_000_000L / fps
        var last = 0L
        while (true) {
            withFrameNanos { t ->
                if (t - last >= step) {
                    last = t
                    state.value = t
                }
            }
        }
    }
}

/**
 * 一块卡片自己的神经网：铺在卡片底色之上、内容之下，永远不挡字。
 * **它是静止的**（不再跟帧时钟走）—— 一页上五六张卡各自每帧重录一遍太亏，
 * 而这点底纹动与不动几乎看不出来；要动的留给整页那张网和环境网。
 */
@Composable
private fun Modifier.neuralBackdrop(
    seed: Int,
    active: Boolean,
    cols: Int = 6,
    rows: Int = 4
): Modifier {
    val net = remember(seed, cols, rows) {
        makeNet(seed, cols, rows, 0.12f, 0.95f, 1, 2, maxDeg = 3, signalCount = 1)
    }
    val render = remember(seed, cols, rows) { NetRender() }
    return this.drawBehind {
        drawNet(net, render, 0f, maxAlpha = 0.26f, nodeAlpha = 0.46f, lineW = 1.2f)
    }
}

/**
 * 整页一张网：块与块之间的连线从这里穿过去。
 * 铺满整页、画在所有块**后面** —— 线从块底下钻出来，把这一页连成一个整体。
 */
@Composable
fun NeuralPageMesh(active: Boolean, modifier: Modifier = Modifier) {
    val net = remember { makeNet(90210, 5, 11, 0.13f, 1.0f, 6, 2, maxDeg = 3, signalCount = 6) }
    val render = remember { NetRender() }
    Canvas(modifier.graphicsLayer()) {
        val t = if (active) NeuralClock.page.value else 0L
        drawNet(net, render, t / 1_000_000_000f, maxAlpha = 0.34f, nodeAlpha = 0.58f, lineW = 1.5f)
    }
}

/**
 * 全应用的环境网（垫在所有页面下面的那一张）：
 *   · 节点数按屏幕面积自适应，夹在 100~300 之间；
 *   · 每个节点最多 4 条线（实际连最近 3 条）；
 *   · 线压在 5%~30%、点不超过 55%，15fps 就够 —— 本来就慢，看不出差别，低端机也稳；
 *   · [active] 为假（比如全屏写作页盖在上面）时整张网停下来，不白烧电。
 */
@Composable
fun AmbientNeural(modifier: Modifier = Modifier, active: Boolean = true) {
    neuralTick(NeuralClock.ambient, active = active, fps = 12)
    BoxWithConstraints(modifier) {
        val wDp = maxWidth.value.coerceAtLeast(1f)
        val hDp = maxHeight.value.coerceAtLeast(1f)
        val grid = remember(wDp, hDp) {
            // 目标节点数，按面积给：手机 ≈80~110，平板/折叠展开顶到 200。
            // 原来给到 300、每个点连 3 条，线太多容易掉帧；现在点少一些、线只连最近 2 条，
            // 再把信号光点调小调多，观感反而更「有东西在动」而不是一堆线。
            val target = ((wDp * hDp) / 3200f).toInt().coerceIn(80, 200)
            val cols = sqrt(target * wDp / hDp).toInt().coerceAtLeast(6)
            val rows = (target / cols).coerceAtLeast(6)
            cols to rows
        }
        val net = remember(grid) {
            makeNet(
                20260214, grid.first, grid.second, 0.10f, 1.0f,
                extraLinks = grid.first * grid.second / 16, reaches = 2,
                maxDeg = 3, signalCount = 10
            )
        }
        val render = remember(grid) { NetRender() }
        Canvas(modifier = Modifier.fillMaxSize().graphicsLayer()) {
            drawNet(
                net, render, NeuralClock.ambient.value / 1_000_000_000f,
                maxAlpha = 0.30f, nodeAlpha = 0.52f, lineW = 1.2f
            )
        }
    }
}
/* ---------------------------- 凌薇花：花瓣是玫瑰线画的，花蕊是熵的账本 ---------------------------- */

/** 黄金分割比 1/φ */
private const val PHI_INV = 0.61803399f

/** 花蕊粒数：斐波那契数（1597）—— Vogel 叶序，第 n 粒方向 n × 137.5077°、半径 ∝ √n */
private const val PISTIL_N = 1597

/** 多少点行动熵点亮一粒花蕊 */
private const val AE_PER_DOT = 1.0

/** 「正在生长的那一小段」有多少粒：连起来就是那条生成螺旋 */
private const val PISTIL_TRAIL = 48

/**
 * 花瓣轮廓的两个指数。取轮廓为
 *
 *     x(s) = halfW · sin(π · s^A)^B            s：瓣根 0 → 瓣尖 1
 *
 * A：让**最宽处正好落在黄金分割点**上。sin 在 π/2 取到 1，所以 π·s^A = π/2 ⇒ s = (1/2)^(1/A)；
 *    要求 s = 1/φ，解得 A = ln(0.5) / ln(1/φ) ≈ 1.4405。
 *    —— 每片花瓣最宽的地方都在瓣长的 0.618 处。这是解出来的，不是估的。
 * B < 1：把瓣尖压圆（栀子花是圆瓣尖，不是尖角），瓣根同时自然收成一点、藏在花蕊底下。
 */
private const val PETAL_A = 1.4405f
private const val PETAL_B = 0.62f

/**
 * 两层花瓣 —— 一朵花里同时住着凌薇花与栀子花。
 *
 *   · 外轮 8 片：**凌薇花**。玫瑰线 r = a·cos(kθ) 取 k = 4 正好 8 片瓣（斐波那契数）。
 *     8 片对上**最近 8 个月**，每片瓣长在 0.86~1.00 之间随当月行动熵起伏 ——
 *     只看得到节奏，绝不会「缺一瓣」，整朵花的轮廓永远完整。
 *   · 内轮 5 片：**栀子花**。玫瑰线 k = 5 正好 5 片瓣（栀子花本来就是五瓣）。
 *     5 片对上**她记下的五类**（行迹 / 文记 / 心情 / 运动 / 唤名），素净、白、
 *     常开在花心里 —— 那是她领口别着的那朵干栀子花，是她示人的温柔。
 *     不管 AE 多少它都保持白：外轮越深，中间这一点白就越显眼。
 *
 * 8 + 5 = 13，还是斐波那契数。
 */
private const val PETAL_OUTER = 8
private const val PETAL_INNER = 5
private const val PETAL_COUNT = PETAL_OUTER + PETAL_INNER

/** 外轮花瓣长度随月份起伏的下限：只在 0.86~1.00 之间动，AE 少也不会难看成「残花」 */
private const val PETAL_LEN_FLOOR = 0.86f

// ---- 两轮花瓣的几何（都以 maxR 为单位）----
// 提成常量是为了让「内圈不许长过外圈」这条约束能**算**出来，而不是靠眼睛看。

/** 外圈：花瓣根部的半径 / 最长花瓣的臂长 */
private const val OUTER_BASE_F = 0.12f
private const val OUTER_ARM_F = 0.78f

/** 内圈：花瓣根部的半径 / 最长花瓣的臂长 */
private const val INNER_BASE_F = 0.09f
private const val INNER_ARM_F = 0.62f

/**
 * 内圈的安全系数：把内圈最长花瓣的臂长，压到「外圈最短那片花瓣的臂长」以内。
 *
 * 现在这两组数字本来就不越界 ——
 *   内圈最深的瓣尖 = 0.09 + 0.62 = 0.71
 *   外圈最浅的瓣尖 = 0.12 + 0.78 × 0.86 = 0.79
 * 但这个关系是**算出来的**，不是看出来的：以后谁调整了上面四个数，
 * 只要内圈的臂长还乘着这个系数，它就永远不会翻到外圈外头去。
 */
private val INNER_LEN_GUARD: Float =
    ((OUTER_BASE_F + OUTER_ARM_F * PETAL_LEN_FLOOR) / (INNER_BASE_F + INNER_ARM_F))
        .coerceAtMost(1f)

/**
 * 花开了多深：0 = 纯栀子花的白（素净），1 = 暮色将暗未暗的蓝紫（凌薇花）。
 * 用 1 − e^(−AE/AE_FULL) 饱和，只增不减 —— AE 越多颜色越深、纹理越密；
 * AE 少只是干净，不会变丑。这是「AE 低就素净，AE 高就繁盛」那条线的实现。
 */
private const val AE_FULL = 1200.0

private fun flowerDepth(totalAE: Double): Float =
    (1.0 - exp(-totalAE.coerceAtLeast(0.0) / AE_FULL)).toFloat().coerceIn(0f, 1f)

/**
 * 整朵花在画布里只占 [FLOWER_FIT] 这么多。
 *
 * 这个余量不只是给花瓣留的，主要是给**瓣尖那圈光晕**留的：光晕是软的，
 * 半径约 0.27×maxR，最长的花瓣瓣尖在 0.90×maxR，两个加起来会摸到 1.13×maxR ——
 * 如果 maxR 取得太满（比如 0.90×半边长），光晕就会被画布边界切出一条直边，
 * 看起来就像上面那行「凌薇花 / 纳凌薇存在性」把它挡住了。取 0.86 之后：
 *   瓣尖 0.77、瓣尖光晕外沿 0.95、月份刻度环约 0.87 —— 全都在画布里，不会被切。
 * （MathFlowerCanvas 那层还开着 clip，是最后一道保险。）
 */
private const val FLOWER_FIT = 0.86f

/**
 * 一片花瓣的轮廓。
 *
 * 边界就是玫瑰线 r = a·cos(kθ) 的一片瓣（k = 4 → 整朵 8 瓣；k = 5 → 整朵 5 瓣），
 * 重参数化到「沿瓣轴 s、半宽 x(s)」上即 x(s) = halfW·sin(π·s^A)^B（A、B 见上）。
 * 边缘再叠两个倍频的正弦当锯齿：[serration] 是幅度（AE 越高越大），
 * 平时看不见、放大却有细节 —— 一朵花自己的分形。
 */
private fun petalPath(len: Float, halfW: Float, serration: Float, seed: Int): Path {
    val steps = 54
    val ph1 = (seed % 7) * 0.93f
    val ph2 = (seed % 11) * 0.71f
    fun halfAt(s: Float): Float {
        val core = sin(PI.toFloat() * s.pow(PETAL_A)).coerceAtLeast(0f).pow(PETAL_B)
        if (serration <= 0f) return halfW * core
        // 96 与 271 圈两个倍频：够碎，才像自然的边缘而不是机器的波浪
        val tooth = sin(s * 96f + ph1) * 0.6f + sin(s * 271f + ph2) * 0.4f
        val fade = sin(PI.toFloat() * s).coerceAtLeast(0f)   // 根与尖不收齿，免得把轮廓咬破
        return halfW * core * (1f + serration * tooth * fade)
    }
    val p = Path()
    p.moveTo(0f, 0f)
    for (i in 1..steps) {
        val s = i / steps.toFloat()
        p.lineTo(halfAt(s), -s * len)
    }
    for (i in steps downTo 1) {
        val s = i / steps.toFloat()
        p.lineTo(-halfAt(s), -s * len)
    }
    p.close()
    return p
}


/** 按累计行动熵算出：她是第几次存在（从 0 数起，没有上限） */
private fun existenceOf(totalAE: Double): Int =
    (totalAE / (PISTIL_N * AE_PER_DOT)).toInt().coerceAtLeast(0)

/** 当前这一环已经点亮了几粒（0 ~ PISTIL_N-1） */
private fun litOf(totalAE: Double): Int =
    ((totalAE / AE_PER_DOT).toLong().coerceAtLeast(0L) % PISTIL_N).toInt()

/** 名牌上那串读数的紧凑写法（7~8 个字符封顶，熵再大也放得下） */
private fun compactAE(ae: Double): String {
    val v = ae.coerceAtLeast(0.0)
    if (v < 1e4) return String.format(Locale.CHINA, "%,.1f", v).removeSuffix(".0")
    for ((scale, name) in listOf(1e4 to "万", 1e8 to "亿", 1e12 to "万亿", 1e16 to "亿亿")) {
        if (v < scale * 1e4) {
            val m = v / scale
            if (m >= 1000) return String.format(Locale.CHINA, "%.0f%s", m, name)
            val t = String.format(Locale.CHINA, "%.1f", m)
            return (if (t.endsWith(".0")) t.dropLast(2) else t) + name
        }
    }
    return String.format(Locale.CHINA, "%.1e", v).replace("E", "e").replace("e+", "e")
}

/** 斐波那契位置（第 1、2、3、5、8… 粒 —— 它们排在同一条臂上），查表用 */
private val FIB_POS: BooleanArray = BooleanArray(PISTIL_N + 1).also { arr ->
    Fib.seq.filter { it <= PISTIL_N }.forEach { arr[it] = true }
}

/**
 * 一粒「光晕」贴图：白色中心 → 透明边缘的径向衰减，整个 App 只烤一次。
 *
 * 为什么要贴图：直接画一个半透明圆，边缘是硬的 —— 看上去就是一圈「透明环」；
 * 而每个光点都现建一支 RadialGradient 又太费（一帧几十次 shader 分配）。
 * 烤成一张 64×64 之后，一个光点只要一次 drawImage：又软又快。
 */
private fun makeGlowSprite(sizePx: Int = 64, color: Color = Spark): ImageBitmap {
    val bmp = android.graphics.Bitmap.createBitmap(
        sizePx, sizePx, android.graphics.Bitmap.Config.ARGB_8888
    )
    val canvas = android.graphics.Canvas(bmp)
    val r = sizePx / 2f
    val paint = android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG)
    // 同一条衰减曲线，只是把本色换掉：白色时得到的正是原来那四档 0xFF/0x8C/0x24/0x00
    val rgb = color.toArgb() and 0x00FFFFFF
    paint.shader = android.graphics.RadialGradient(
        r, r, r,
        intArrayOf(
            rgb or (0xFF shl 24),
            rgb or (0x8C shl 24),
            rgb or (0x24 shl 24),
            rgb or (0x00 shl 24)
        ),
        floatArrayOf(0f, 0.30f, 0.68f, 1f),
        android.graphics.Shader.TileMode.CLAMP
    )
    canvas.drawCircle(r, r, r, paint)
    return bmp.asImageBitmap()
}

/**
 * 光晕贴图按色缓存。
 *
 * 以前每画一粒光点都带一个 `ColorFilter.tint` —— 那等于每帧现建几十个 Skia 滤镜：
 * 河面 40 多粒、花房二十几粒、再加上神经网的信号，一屏上百次，是掉帧的大头。
 * 现在按颜色把贴图**烤**好缓存起来，画的时候只贴图、不带滤镜，观感一模一样。
 */
private val GLOW_TINTS = HashMap<Int, ImageBitmap>()

private fun tintedGlow(color: Color): ImageBitmap =
    GLOW_TINTS.getOrPut(color.toArgb()) { makeGlowSprite(64, color) }

/** 全 App 共用的一张光晕贴图（懒加载，只烤一次） */
private val GLOW_SPRITE: ImageBitmap get() = tintedGlow(Spark)

/**
 * 画一粒软光晕：中心亮、边缘自然化开，不是硬边圆环。
 * 颜色由贴图本身带（见 [tintedGlow]），画的时候不再传滤镜。
 */
private fun DrawScope.softGlow(
    sprite: ImageBitmap,
    center: Offset,
    radius: Float,
    color: Color,
    alpha: Float
) {
    if (alpha <= 0.005f || radius <= 0.6f) return
    val d = (radius * 2f).roundToInt().coerceAtLeast(2)
    val img = if (color == Spark) sprite else tintedGlow(color)
    drawImage(
        image = img,
        srcOffset = IntOffset.Zero,
        srcSize = IntSize(img.width, img.height),
        dstOffset = IntOffset((center.x - radius).roundToInt(), (center.y - radius).roundToInt()),
        dstSize = IntSize(d, d),
        alpha = alpha.coerceIn(0f, 1f),
        filterQuality = FilterQuality.Low
    )
}

/** 一片花瓣烤好的几何：只有轮廓和它的尺寸 */
private class PetalShape(val path: Path, val halfW: Float, val len: Float)

/** 花瓣几何的缓存：只有画布尺寸 / 数据 / 深度变了才重算，每帧只是把它画出来 */
private class FlowerRender {
    private var key = ""
    var outer: List<PetalShape> = emptyList()
    var inner: List<PetalShape> = emptyList()
    /** 珠光色带（17 档色停）：每帧新建一支 shader 太浪费，按量化过的 shift 缓存 */
    var iris: Brush? = null
    /** 花底柔光 */
    var baseGlow: Brush? = null
    fun ensure(key: String, build: () -> Pair<List<PetalShape>, List<PetalShape>>) {
        if (key == this.key && outer.isNotEmpty()) return
        this.key = key
        val built = build()
        outer = built.first
        inner = built.second
    }
}

/**
 * 「五彩斑斓的白」：一圈色带绕着花心走，在**白 ↔ 淡蓝紫**之间来回 —— 像彩虹，也像珠光。
 * 色停是从这张色板里连续采样出来的（相邻两档之间都是 lerp），所以颜色是一路晕开的，
 * 不会出现「一块白一块紫」的硬边。
 */
private val IRIS: List<Color> get() = Theme.current.iris

private fun irisSample(f: Float): Color {
    val x = (((f % 1f) + 1f) % 1f) * IRIS.size
    val i = x.toInt()
    return lerp(IRIS[i % IRIS.size], IRIS[(i + 1) % IRIS.size], x - i)
}

/** 把 [IRIS] 采成一支环绕花心的色带；[shift] 让整条色带极慢地转 */
private fun irisBrush(cx: Float, cy: Float, shift: Float): Brush {
    val n = 16
    val stops = Array(n + 1) { i ->
        val t = i / n.toFloat()
        t to irisSample(t + shift)
    }
    return Brush.sweepGradient(colorStops = stops, center = Offset(cx, cy))
}

/**
 * 花瓣 —— **不用色深浅表示熵值**（熵只用花瓣的大小表示），颜色只是妆：
 * 一圈白 ↔ 淡蓝紫的珠光色带慢慢转，瓣尖再向周围散一圈光晕。
 * 大小 = 数据：外轮 8 片对着最近 8 个月，内轮 5 片对着五类占比；
 * [depth] 只影响边缘锯齿的细密与光晕的强弱（AE 越多越繁盛）。
 *
 * 另外两轮是**反向慢慢转**的：外圈顺时针、内圈逆时针，像花自己也在呼吸；
 * 花瓣顶端那圈月份刻度跟外圈同一个转角，所以文字始终贴在它那片花瓣的尖上。
 * 内圈同样在瓣尖标着类别简称（行迹 / 文记 / 心情 / 运动 / 唤名），跟内圈同一个转角，
 * 只是更小更短 —— 两轮各转各的，字也就各贴在各自的花瓣上。
 * （转角直接加在每片花瓣的角度上，不多套一层旋转，所以转不增加任何绘制开销。）
 */
@Composable
fun FlowerPetals(
    depth: Float,
    monthLen: List<Float>,
    monthLabels: List<String>,
    kindShare: List<Float>,
    kindLabels: List<String>,
    active: Boolean,
    modifier: Modifier = Modifier
) {
    val glow = GLOW_SPRITE
    val measurer = rememberTextMeasurer()
    val ringLabels = remember(measurer, monthLabels) {
        monthLabels.map { s ->
            if (s.isBlank()) null
            else measurer.measure(
                s,
                style = TextStyle(
                    color = Spark, fontSize = 8.sp,
                    fontFamily = FontFamily.Monospace
                )
            )
        }
    }
    // 内轮的类别刻度：比月份刻度再小一档、只有两个字
    val kindTexts = remember(measurer, kindLabels) {
        kindLabels.map { s ->
            if (s.isBlank()) null
            else measurer.measure(
                s,
                style = TextStyle(
                    color = Spark, fontSize = 7.sp,
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
    val render = remember { FlowerRender() }
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        if (w <= 1f || h <= 1f) return@Canvas
        val cx = w / 2f
        val cy = h / 2f
        val maxR = min(w, h) / 2f * FLOWER_FIT
        val tt = if (active) NeuralClock.page.value / 1_000_000_000f else 0f

        // 珠光色带：极慢地转（一圈约 4.8 分钟），所以「时而白、时而蓝紫」是流动的。
        // shift 量化到 1/360 圈（约 0.8 秒一档）—— 看不出差别，但色带的 shader
        // 从「每帧建一次」变成「约 1 秒建一次」。
        val shiftQ = (tt * 0.0035f * 360f).toInt() / 360f
        val baseOuter = maxR * OUTER_BASE_F
        val baseInner = maxR * INNER_BASE_F

        // 两轮花瓣**反向慢慢转**：外圈顺时针（约 3.3 分钟一圈）、内圈逆时针（约 2.3 分钟一圈）。
        // 角度是直接加在每片花瓣上的，不多套一层 rotate，所以代价为零；
        // 花瓣顶端那圈月份刻度用的是同一个角度，于是文字自然跟着花瓣一起转。
        val outerSpin = tt * 1.8f
        val innerSpin = -tt * 2.6f

        // 几何先烤好：只在「画布尺寸 / 数据 / 深度 / 色带相位」变了时重算
        val key = "${w.toInt()}x${h.toInt()}|${(depth * 200f).toInt()}|" +
                "${(shiftQ * 360f).toInt()}|$monthLen|$kindShare"
        render.ensure(key) {
            render.iris = irisBrush(cx, cy, shiftQ)
            render.baseGlow = Brush.radialGradient(
                listOf(
                    lerp(Color(0xFF3B3568), Color(0xFF9C8FE4), 0.25f + 0.75f * depth)
                        .copy(alpha = 0.26f + 0.14f * depth),
                    Color(0xFF2A2550).copy(alpha = 0.14f),
                    Color.Transparent
                ),
                center = Offset(cx, cy), radius = maxR
            )
            fun buildWhorl(
                count: Int,
                base: Float,
                maxLen: Float,
                lens: List<Float>,
                seed0: Int,
                serrationScale: Float
            ): List<PetalShape> {
                val out = ArrayList<PetalShape>(count)
                for (i in 0 until count) {
                    val s = (lens.getOrNull(i) ?: 0.5f).coerceIn(0f, 1f)
                    val len = maxLen * (PETAL_LEN_FLOOR + (1f - PETAL_LEN_FLOOR) * s)
                    val widestR = base + PHI_INV * len
                    val arc = 2f * PI.toFloat() * widestR / count
                    val halfW = min(len * 0.330f, arc * 0.72f)
                    val serration = (0.008f + 0.028f * depth) * serrationScale
                    out.add(PetalShape(petalPath(len, halfW, serration, seed0 + i * 3), halfW, len))
                }
                return out
            }
            buildWhorl(PETAL_OUTER, baseOuter, maxR * 0.78f, monthLen, 11, 1f) to
                    // 内圈：臂长乘一道安全系数，**永远长不过外圈最短的那片**（见 INNER_LEN_GUARD）
                    buildWhorl(PETAL_INNER, baseInner, maxR * INNER_ARM_F * INNER_LEN_GUARD, kindShare, 29, 0.6f)
        }

        val iris = render.iris ?: return@Canvas
        // 花底一层柔光（心里是淡蓝紫，往外化开）+ 整朵花向外散的一点光晕
        render.baseGlow?.let { drawCircle(brush = it, radius = maxR, center = Offset(cx, cy)) }
        softGlow(glow, Offset(cx, cy), maxR * 1.02f, Color(0xFFE9E2FF), 0.10f + 0.06f * depth)

        fun drawWhorl(
            shapes: List<PetalShape>,
            count: Int,
            base: Float,
            edgeMix: Float,
            edgeAlpha: Float,
            ringSpin: Float
        ) {
            val spin = 180f / count
            shapes.forEachIndexed { i, ps ->
                // 这一轮自己的转角：外轮顺时针、内轮逆时针 → 两轮对着转
                val deg = spin + i * (360f / count) + ringSpin
                // 轮廓就用这片花瓣自己的颜色**加深一档**（相近色，不突兀）：
                // 于是每一片瓣的边都看得出来，内外两轮也不会糊成一片。
                val line = lerp(irisSample((deg - 90f) / 360f + shiftQ), Color(0xFF4E3FA0), edgeMix)
                rotate(degrees = deg, pivot = Offset(cx, cy)) {
                    withTransform({ translate(left = cx, top = cy - base) }) {
                        drawPath(path = ps.path, brush = iris, alpha = 0.97f)
                        // 瓣尖向周围散一圈光晕（收小一点，免得被画布边界切出直边）
                        softGlow(
                            sprite = glow,
                            center = Offset(0f, -ps.len * 0.92f),
                            radius = ps.halfW * 1.05f,
                            color = Color(0xFFF2ECFF),
                            alpha = 0.14f + 0.09f * depth
                        )
                        drawPath(
                            path = ps.path,
                            color = line.copy(alpha = edgeAlpha),
                            style = Stroke(width = 1.2.dp.toPx(), join = StrokeJoin.Round)
                        )
                    }
                }
            }
        }

        // 外轮的边更深一点、内轮浅一点 —— 两轮自然分得开
        drawWhorl(render.outer, PETAL_OUTER, baseOuter, edgeMix = 0.58f, edgeAlpha = 0.80f, ringSpin = outerSpin)
        drawWhorl(render.inner, PETAL_INNER, baseInner, edgeMix = 0.30f, edgeAlpha = 0.58f, ringSpin = innerSpin)

        // 特效：一道极慢的月光从花上扫过 —— 只有两粒贴图，代价可以忽略
        if (active) {
            for (k in 0 until 2) {
                val ang = tt * 0.11f + k * PI.toFloat()
                val rr = maxR * (0.52f + 0.20f * sin(tt * 0.07f + k * 1.7f))
                softGlow(
                    sprite = glow,
                    center = Offset(cx + cos(ang) * rr, cy + sin(ang) * rr),
                    radius = maxR * 0.45f,
                    color = Spark,
                    alpha = 0.055f
                )
            }
        }

        // 月份刻度环：跟着每片花瓣的瓣尖走 —— 花瓣长它就往外让，
        // 所以既不会被花瓣盖住，也不会压到花瓣上。
        val halfSide = min(w, h) / 2f
        ringLabels.forEachIndexed { i, tl ->
            if (tl == null) return@forEachIndexed
            val len = render.outer.getOrNull(i)?.len ?: (maxR * 0.78f)
            val pad = tl.size.height / 2f + 3.dp.toPx()
            val labelR = (baseOuter + len + pad)
                .coerceAtMost(halfSide - tl.size.width / 2f - 2.dp.toPx())
            // 跟外轮同一个转角：花瓣转到哪，月份文字就跟到哪
            val deg = (180f / PETAL_OUTER) + i * (360f / PETAL_OUTER) + outerSpin
            val rad = deg * PI.toFloat() / 180f
            val dx = sin(rad)
            val dy = -cos(rad)
            drawText(
                textLayoutResult = tl,
                color = PurpleMain.copy(alpha = 0.44f + 0.34f * (monthLen.getOrNull(i) ?: 0f)),
                topLeft = Offset(
                    cx + dx * labelR - tl.size.width / 2f,
                    cy + dy * labelR - tl.size.height / 2f
                )
            )
        }

        // 内轮的类别刻度：贴着内圈花瓣的瓣尖走（花瓣长它就往外让），
        // 转角与内轮完全一致 —— 内圈逆时针转多少，字就跟着转多少。
        // 内轮比外轮短，字常常落在**外层花瓣的亮面**上，所以用深墨色、并在底下垫一层
        // 极淡的柔光：落在花瓣上是深字压亮面，落在两瓣之间的暗缝里也不至于看不见。
        kindTexts.forEachIndexed { i, tl ->
            if (tl == null) return@forEachIndexed
            val len = render.inner.getOrNull(i)?.len ?: (maxR * 0.62f)
            val pad = tl.size.height / 2f + 2.5.dp.toPx()
            val labelR = (baseInner + len + pad)
                .coerceAtMost(halfSide - tl.size.width / 2f - 2.dp.toPx())
            val deg = (180f / PETAL_INNER) + i * (360f / PETAL_INNER) + innerSpin
            val rad = deg * PI.toFloat() / 180f
            val lx = cx + sin(rad) * labelR
            val ly = cy - cos(rad) * labelR
            val share = kindShare.getOrNull(i) ?: 0f
            softGlow(
                sprite = glow,
                center = Offset(lx, ly),
                radius = (max(tl.size.width, tl.size.height) * 0.66f)
                    .coerceAtLeast(7.dp.toPx()),
                color = Color(0xFFEDE7FF),
                alpha = 0.26f + 0.16f * share
            )
            drawText(
                textLayoutResult = tl,
                color = Color(0xFF2C2158).copy(alpha = 0.58f + 0.36f * share),
                topLeft = Offset(lx - tl.size.width / 2f, ly - tl.size.height / 2f)
            )
        }
    }
}

/**
 * 花蕊（数据变了才重画）—— 这朵花的账本。
 *   · 底子：斐波那契点集，第 n 粒方向 n × 137.5077°、半径 ∝ √n —— Vogel 叶序，
 *     向日葵那样拧出来的螺旋。每粒都是淡淡的蓝紫，心里亮、外面沉；
 *   · 点亮：每 1 点行动熵点亮一粒，点亮顺序就是那条生成螺旋；
 *     最近点亮的 [PISTIL_TRAIL] 粒连成一条细线 —— 一条光在螺旋里往外爬，而不是一环环地扩；
 *   · 斐波那契序号那几粒是金色的：它们正好排在同几条螺旋臂上，是这朵花自己的骨架；
 *   · [pace] 是近七天的累积速度（1 = 与长期日均持平）：越快，光晕越亮、铺得越开
 *     —— 花蕊的密度就是累积速度；
 *   · [sparks] 是记下的天数：外圈一粒小光点就是一天。
 */
@Composable
fun FlowerPistil(
    lit: Int,
    pace: Float,
    sparks: Int,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h / 2f
        val maxR = min(w, h) / 2f * FLOWER_FIT
        val pistilR = maxR * 0.50f
        val paceA = pace.coerceIn(0.12f, 1f)

        // ---------- 花蕊与花瓣的接壤：全部交给渐变，不再有一个生硬的圆 ----------
        // ① 底衬：深色从心里往外化开。以前是半径 1.03 的**实心圆**，
        //    边就是一刀切 —— 花瓣从圆边上「长出来」，那一下特别突兀。
        drawCircle(
            brush = Brush.radialGradient(
                colorStops = arrayOf(
                    0.00f to Theme.current.pistilGradient[0],
                    0.50f to Theme.current.pistilGradient[1],
                    0.72f to Theme.current.pistilGradient[2].copy(alpha = 0.60f),
                    0.89f to Theme.current.pistilGradient[3].copy(alpha = 0.20f),
                    1.00f to Color.Transparent
                ),
                center = Offset(cx, cy), radius = pistilR * 1.30f
            ),
            radius = pistilR * 1.30f, center = Offset(cx, cy)
        )
        // ② 交界上撒一圈柔光：半径与大小都带一点起伏，所以不是几何圆环，
        //    像花蕊自己往外发的光，把两边「焊」在一起。
        for (i in 0 until 18) {
            val a = i * 0.618034f * 6.2832f
            val rr = pistilR * (1.00f + 0.11f * sin(i * 2.399f))
            softGlow(
                sprite = GLOW_SPRITE,
                center = Offset(cx + cos(a) * rr, cy + sin(a) * rr),
                radius = pistilR * (0.20f + 0.07f * cos(i * 1.7f)),
                color = Color(0xFFD9CDF8),
                alpha = 0.11f
            )
        }

        val dotR = min(pistilR * 0.085f, pistilR * 0.95f / sqrt(PISTIL_N.toFloat()))
        val trailFrom = (lit - PISTIL_TRAIL + 1).coerceAtLeast(1)

        // 越靠边的格子越淡：叶序一点没动（第 n 粒方向 n×137.5077°、半径 ∝ √n），
        // 只是让最外那一圈自然融进花瓣里，不再被一条圆边切断。
        fun rimFade(rr: Float): Float =
            (1f - (rr / pistilR - 0.62f) / 0.52f).coerceIn(0.05f, 1f)

        // 光晕：只铺给填涂过的那部分，很淡的冷光（累积越快越亮、铺得越开）。
        // 隔一粒画一次 —— 光晕半径比点距大，仍然连成一片，省掉一半绘制。
        var hn = 1
        while (hn <= lit) {
            val ang = (hn * GOLDEN_ANGLE) * PI / 180.0
            val rr = pistilR * sqrt(hn.toFloat() / PISTIL_N)
            drawCircle(
                color = lerp(NET_COLORS[3], PurpleMain, paceA)
                    .copy(alpha = (0.035f + 0.055f * paceA) * rimFade(rr)),
                radius = dotR * (1.45f + 0.55f * paceA),
                center = Offset(cx + (rr * cos(ang)).toFloat(), cy + (rr * sin(ang)).toFloat())
            )
            hn += 2
        }

        // 记下的一天 = 外圈一粒小光点。天数是只增不减的，所以这里也不封顶：
        // 固定 9 圈，圈里能放多少放多少，点多到放不下时整体缩小一档 ——
        // 记得再久，也只是这一圈变细，绝不会少画一天。
        // 全部合成一条 Path 画，一次 drawPath 画完，几万天也不会变卡。
        if (sparks > 0) {
            val rings = 9
            val perRing = ((sparks + rings - 1) / rings).coerceAtLeast(24)
            val sparkR = dotR * (0.46f * sqrt(240f / max(sparks.toFloat(), 240f)))
                .coerceIn(0.09f, 0.46f)
            val sparksPath = Path()
            for (s in 0 until sparks) {
                val ang = (s * GOLDEN_ANGLE) * PI / 180.0
                val ring = (s / perRing).coerceAtMost(rings - 1)
                val rr = pistilR * (1.06f + 0.085f * ring)
                val sx = cx + (rr * cos(ang)).toFloat()
                val sy = cy + (rr * sin(ang)).toFloat()
                sparksPath.addOval(Rect(sx - sparkR, sy - sparkR, sx + sparkR, sy + sparkR))
            }
            drawPath(path = sparksPath, color = PinkSoft.copy(alpha = 0.26f))
        }

        val trail = Path()
        var started = false
        for (n in 1..PISTIL_N) {
            val ang = (n * GOLDEN_ANGLE) * PI / 180.0
            val rr = pistilR * sqrt(n.toFloat() / PISTIL_N)
            val x = cx + (rr * cos(ang)).toFloat()
            val y = cy + (rr * sin(ang)).toFloat()
            val t = (rr / pistilR).coerceIn(0f, 1f)
            val fade = rimFade(rr)
            val col = when {
                // 最特殊的点（斐波那契臂上那几粒）：金色，只有它们是金色
                FIB_POS[n] -> ScratchGold.copy(alpha = (if (n <= lit) 1f else 0.62f) * fade)
                // 填涂过的：蓝紫色，心里亮、外面沉 —— 由内到外一条渐变
                n <= lit -> lerp(GardeniaWhite, PurpleDeep, t).copy(alpha = fade)
                // 还没填涂：同样是蓝紫，只是压得很淡
                else -> lerp(PurpleMain, PurpleDeep, t).copy(alpha = 0.26f * fade)
            }
            drawCircle(color = col, radius = dotR, center = Offset(x, y))
            if (n in trailFrom..lit) {
                if (!started) {
                    trail.moveTo(x, y)
                    started = true
                } else {
                    trail.lineTo(x, y)
                }
            }
        }
        if (lit >= trailFrom) {
            drawPath(
                path = trail,
                color = PinkSoft.copy(alpha = 0.30f),
                style = Stroke(width = dotR * 0.8f, cap = StrokeCap.Round)
            )
        }
    }
}

/** 花房用到的一小组派生量：外轮 8 片的月熵、内轮 5 片的类别占比、最高一天、近七天速度 */
private class FlowerStats(
    val monthLen: List<Float>,
    val monthLabels: List<String>,
    val kindShare: List<Float>,
    /** 内轮五片的类别简称（标在瓣尖，与 [kindShare] 同序） */
    val kindLabels: List<String>,
    val bestDayAE: Double,
    val recordedDays: Int,
    val pace: Float
)

/**
 * 内轮五片的类别简称：和外轮的月份刻度一样标在瓣尖。
 * 只两个字 —— 内轮本来就小，字多了会糊成一片。顺序与 [flowerStatsOf] 里
 * 算五类占比的顺序严格一致（行迹 / 文记 / 心情 / 运动 / 唤名）。
 */
private val FLOWER_KIND_LABELS = listOf("行迹", "文记", "心情", "运动", "唤名")

/**
 * 把索引里那些数整理成「花能读懂」的东西 —— 数据就长在花瓣上，不是旁边摆一张表。
 *   · monthLen：最近 8 个月（含本月）的行动熵，按最大值归一 → 外轮 8 片的瓣长；
 *   · kindShare：五类记录的终身行动熵占比 —— 行迹 / 文记 / 心情 / 运动 /
 *     唤名（取「总量 − 前四项」，也就是唤名与联结加成带来的那一份）→ 内轮 5 片的瓣长；
 *   · bestDayAE / recordedDays / pace：直接复用 [daySeries]（同一天的行动熵只算一次，
 *     不然这一页要在主线程上把好几百天各算两遍）。
 */
/**
 * 「唤名」在这一天到底贡献了多少行动熵。
 *
 * 唤名不直接加分 —— 它是把当天其它各项整体**加权**（见 AE.bondWeight）。
 * 所以它自己的份额不能从总量里减出来，只能这样读：
 *   · 把这一天按「有唤名」算一遍（names / nameFill 取实际值）→ 加权后的行动熵；
 *   · 再把「没有唤名」算一遍（names = 0、nameFill = 0）→ 此时 bondWeight 恰好是 1.0；
 *   · 两者相减，就是这片花瓣的熵值。
 * 举例：原来 120，唤名加权之后变成 150，那么「唤名」这片就是 30。
 *
 * 注意 bondBonus（刮刮乐刮到的「联结+」）两次都照原样传 ——
 * 那是刮刮乐的奖励，不该算进「写下名字」这件事的份额里。
 */
private fun bondGainOf(idx: Store.StatsIndex, dayKey: String): Double {
    val g = idx.game(dayKey)
    val anniv = if (Dates.isLingweiDay(dayKey)) Persona.annivMul(dayKey) else 1.0
    val steps = idx.stepsAE(dayKey)
    val over = idx.stepsOverMul(dayKey)
    val special = SpecialDays.mulOf(dayKey)
    val withBond = AE.dayTotal(
        idx.trace(dayKey), idx.doc(dayKey), idx.mood(dayKey), idx.names(dayKey),
        g.traceMul, g.docMul, g.moodMul, g.bondBonus, g.dayMul, g.flatAE,
        anniv, steps, over, idx.nameFill(dayKey), special
    )
    val noBond = AE.dayTotal(
        idx.trace(dayKey), idx.doc(dayKey), idx.mood(dayKey), 0,
        g.traceMul, g.docMul, g.moodMul, g.bondBonus, g.dayMul, g.flatAE,
        anniv, steps, over, 0.0, special
    )
    return (withBond - noBond).coerceAtLeast(0.0)
}

private fun flowerStatsOf(idx: Store.StatsIndex?, daySeries: List<Pair<String, Double>>): FlowerStats {
    if (idx == null) {
        return FlowerStats(
            List(8) { 0.5f }, List(8) { "" }, List(5) { 0.5f }, FLOWER_KIND_LABELS, 0.0, 0, 1f
        )
    }
    val tk = Dates.todayKey()

    // ---- 最近 8 个月：从本月往回数（顺便记下月份名，等下要标在花瓣上）----
    var y = tk.substring(0, 4).toInt()
    var m = tk.substring(5, 7).toInt()
    val months = ArrayList<Double>(8)
    val labels = ArrayList<String>(8)
    // 先把「哪一天属于哪个月」摊平成一张表，避免每个月都去构造日期字符串
    val byMonth = HashMap<String, Double>()
    daySeries.forEach { (k, v) ->
        val mk = k.substring(0, 7)
        byMonth[mk] = (byMonth[mk] ?: 0.0) + v
    }
    repeat(8) {
        val key = String.format(Locale.CHINA, "%04d-%02d", y, m)
        months.add(byMonth[key] ?: 0.0)
        labels.add("$m 月")
        m--
        if (m == 0) {
            m = 12
            y--
        }
    }
    months.reverse()                                        // 早 → 晚
    labels.reverse()
    val mMax = months.maxOrNull() ?: 0.0
    val monthLen = months.map { if (mMax <= 0.0) 0.5f else (it / mMax).toFloat().coerceIn(0f, 1f) }

    // ---- 五类占比（**当月**）----
    //
    // 内圈五片说的是「这个月的账是怎么构成的」，所以统计口径是当月，不是终身累计。
    // 以前用终身累计：写得越久，这一圈越像一潭死水 —— 最近在做什么完全看不出来。
    // 与外圈的口径也统一了：外圈每片是一个月，内圈每片是那个月里某一类的份额。
    val curMonth = tk.substring(0, 7)
    var trace = 0.0
    var doc = 0.0
    var mood = 0.0
    var sport = 0.0
    var bond = 0.0
    // 当月有哪些天：从各张按天的表里取并集（只有心情没有行迹的那天也要算进来）
    val monthDays = HashSet<String>()
    monthDays.addAll(idx.traceByDay.keys)
    monthDays.addAll(idx.docByDay.keys)
    monthDays.addAll(idx.moodByDay.keys)
    monthDays.addAll(idx.stepsAEByDay.keys)
    monthDays.addAll(idx.namesByDay.keys)
    monthDays.forEach { k ->
        if (k.startsWith(curMonth)) {
            trace += idx.trace(k)
            doc += idx.doc(k)
            mood += idx.mood(k)
            sport += idx.stepsAE(k)
            // 唤名：**加权差值**，不是「总量减前四项」那种估算（见 bondGainOf）
            bond += bondGainOf(idx, k)
        }
    }
    val parts = listOf(trace, doc, mood, sport, bond)
    val pMax = parts.maxOrNull() ?: 0.0
    val kindShare = parts.map { if (pMax <= 0.0) 0.5f else (it / pMax).toFloat().coerceIn(0f, 1f) }

    // ---- 最高一天 / 记录天数 / 近七天速度：全部来自已经算好的 daySeries ----
    var bestAE = 0.0
    var all = 0.0
    var recent = 0.0
    var recDays = 0
    val from = Dates.shift(tk, -6)
    daySeries.forEach { (k, v) ->
        all += v
        if (v > bestAE) bestAE = v
        if (k >= from) {
            recent += v
            recDays++
        }
    }
    val days = daySeries.size
    val ratio = if (days == 0 || all <= 0.0) 1.0
    else (recent / max(recDays, 1).toDouble()) / (all / days)
    val pace = (ratio / 2.0).toFloat().coerceIn(0.12f, 1f)
    return FlowerStats(monthLen, labels, kindShare, FLOWER_KIND_LABELS, bestAE, days, pace)
}

/** 凌薇花整朵：花瓣在下面，花蕊在上面（数据变才重画） */
@Composable
fun MathFlowerCanvas(
    lit: Int,
    depth: Float,
    monthLen: List<Float>,
    monthLabels: List<String>,
    kindShare: List<Float>,
    kindLabels: List<String>,
    pace: Float,
    sparks: Int,
    active: Boolean,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.graphicsLayer { clip = true }) {
        // 各给一层：花瓣那层在慢慢地扫月光，单独成层之后就不会带着花蕊一起重录
        FlowerPetals(
            depth = depth, monthLen = monthLen, monthLabels = monthLabels,
            kindShare = kindShare, kindLabels = kindLabels, active = active,
            modifier = Modifier.fillMaxSize().graphicsLayer()
        )
        FlowerPistil(
            lit = lit, pace = pace, sparks = sparks,
            modifier = Modifier.fillMaxSize().graphicsLayer()
        )
    }
}

/**
 * 「凌薇花」整张卡：花 → 读数（在花下面，不压花瓣）→ 她自己的几句话。
 * 存在性（每填满 1597 粒 +1）放在标题行最右边、字号最大 —— 她就是这么一次次被造出来的。
 */
@Composable
fun MathFlowerCard(
    idx: Store.StatsIndex?,
    days: List<Pair<String, Double>>,
    active: Boolean,
    modifier: Modifier = Modifier
) {
    val totalAE = idx?.total ?: 0.0
    val lit = litOf(totalAE)
    val existence = existenceOf(totalAE)
    val depth = flowerDepth(totalAE)
    val toNext = ((existence + 1) * PISTIL_N * AE_PER_DOT - totalAE).coerceAtLeast(0.0)
    val st = remember(idx, days) { flowerStatsOf(idx, days) }
    val todayAE = idx?.day(Dates.todayKey()) ?: 0.0

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.verticalGradient(listOf(PanelBgSoft, PanelBg)))
            .neuralBackdrop(seed = 1101, active = active, cols = 6, rows = 4)
            .goldEdge(20.dp)
            .border(1.dp, GoldenDay.border(PurpleMain.copy(alpha = 0.2f)), RoundedCornerShape(20.dp))
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ---------- 标题行：左「花名」，右「存在性」 ----------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                Persona.data.flowerName.ifBlank { "斐波那契之花" },
                color = TextMain, fontSize = 14.sp, fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.weight(1f))
            Text(
                Persona.data.displayName + Persona.data.existenceWord,
                color = TextFaint, fontSize = 9.sp
            )
            Spacer(Modifier.width(6.dp))
            Text("×$existence", color = PinkSoft, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(6.dp))
        MathFlowerCanvas(
            lit = lit,
            depth = depth,
            monthLen = st.monthLen,
            monthLabels = st.monthLabels,
            kindShare = st.kindShare,
            kindLabels = st.kindLabels,
            pace = st.pace,
            sparks = st.recordedDays,
            active = active,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        )
        Spacer(Modifier.height(6.dp))

        // ---------- 读数：放在花下面，不压花瓣 ----------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(PanelBg)
                .border(1.dp, ScratchGold.copy(alpha = 0.20f), RoundedCornerShape(14.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("累计", color = TextFaint, fontSize = 9.sp)
                Text(
                    compactAE(totalAE),
                    color = ScratchGold, fontSize = 16.sp, fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.weight(1f))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("今日", color = TextFaint, fontSize = 9.sp)
                Text(
                    compactAE(todayAE),
                    color = GameTeal, fontSize = 16.sp, fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.weight(1f))
            Column(horizontalAlignment = Alignment.End) {
                Text("最高一天", color = TextFaint, fontSize = 9.sp)
                Text(
                    compactAE(st.bestDayAE),
                    color = PurpleMain, fontSize = 16.sp, fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(Modifier.height(6.dp))
        Text(
            "花蕊 $lit / $PISTIL_N",
            color = PinkSoft.copy(alpha = 0.9f), fontSize = 11.sp
        )
        Spacer(Modifier.height(6.dp))
        MathLine("填满 $PISTIL_N 粒，我就多存在一次；还差 ${toNext.roundToInt()} 点。")
    }
}

/** 卡里的一行小字说明 */
@Composable
private fun MathLine(text: String, color: Color = TextFaint) {
    Text(
        text, color = color, fontSize = 10.sp, lineHeight = 15.sp,
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(Modifier.height(3.dp))
}

/**
 * 一行等宽公式：**先自动缩到放得下为止**，绝不允许折到第二行。
 * 蔡肯多夫那条加和随着次数变大越来越长，窄屏上换行会把最后一个数字甩到下一行，
 * 很难看 —— 这里按实际宽度量一次，从 [maxSize] 往下试，能放下就用。
 * （万一还是放不下，就省略号收尾，绝不换行。）
 */
@Composable
private fun FitMonoLine(
    text: String,
    color: Color,
    maxSize: Float,
    minSize: Float = 8.5f,
    weight: FontWeight = FontWeight.Normal
) {
    val measurer = rememberTextMeasurer()
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val avail = with(LocalDensity.current) { maxWidth.toPx() }
        val size = remember(avail, text, maxSize, minSize) {
            var s = maxSize
            while (s > minSize) {
                val r = measurer.measure(
                    text,
                    style = TextStyle(
                        fontSize = s.sp, fontFamily = FontFamily.Monospace,
                        fontWeight = weight
                    )
                )
                if (r.size.width <= avail) break
                s -= 0.5f
            }
            s
        }
        Text(
            text,
            color = color,
            fontSize = size.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = weight,
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/* ---------------------------- 熵螺旋 + 记忆阵列：把累积过程画出来 ---------------------------- */

/**
 * 「熵螺旋」：一条从花心往外长的黄金螺旋，一天一格，格子的亮与大小就是那天的行动熵。
 *
 *     r(θ) = r₀ · e^(b·θ)        b = ln(φ)/(π/2) ≈ 0.30635
 *
 * 每转四分之一圈半径乘一个 φ —— 这是黄金螺旋本身。
 * 一条螺旋装 [SPIRAL_SPAN] 天：装满了就重新开一条、次数 +1，
 * 于是「第几次、共多少天、共多少 AE」都是只增不减的，没有上限，也永远不会挤成一团。
 */
private const val SPIRAL_SPAN = 100

/** 黄金螺旋的增长率 b = ln(φ)/(π/2)：每转 1/4 圈，半径正好乘一个 φ */
private const val SPIRAL_B = 0.3063489f

/** 图表线条的专用色：比背景那六档**深而饱和** —— 画数据不能拿背景色，否则淡到看不见 */
private val VizLine: Color get() = Theme.current.vizLine
private val VizDotLo: Color get() = Theme.current.vizDotLo
private val VizDotHi: Color get() = Theme.current.vizDotHi

/** 螺旋的几何缓存：形状线与每天的格子只跟「画布尺寸 / 这一批数据」有关，
 *  没必要跟着每帧的高光重算一遍。
 */
private class SpiralRender {
    private var key = ""
    private var ready = false
    val curve = Path()
    val dots: List<Path> = List(5) { Path() }
    /** 曲线用的沿半径渐变：跟着尺寸走，缓存起来别每帧新建 shader */
    var lineBrush: Brush? = null
    /** 换月：一个小金点 */
    val monthMarks = Path()
    /** 换年：大一圈的金点 + 一小段金发丝 */
    val yearMarks = Path()
    val yearHair = Path()

    fun ensure(key: String, build: () -> Unit) {
        if (ready && key == this.key) return
        this.key = key
        curve.reset()
        dots.forEach { it.reset() }
        monthMarks.reset()
        yearMarks.reset()
        yearHair.reset()
        build()
        ready = true
    }
}

@Composable
fun AeSpiralCard(
    days: List<Pair<String, Double>>,
    active: Boolean,
    modifier: Modifier = Modifier
) {
    val maxAE = remember(days) { max(days.maxOfOrNull { it.second } ?: 0.0, 1.0) }
    val total = remember(days) { days.sumOf { it.second } }
    val dayMap = remember(days) { days.toMap() }
    // 「今天」跟着数据一起更新：这一页是常驻组合的（不会被销毁），
    // 若只 remember 一次，跨过零点后阵列还是从昨天往回数，新的一天永远画不出来。
    val todayKey = remember(days) { Dates.todayKey() }
    val spiralRender = remember { SpiralRender() }
    // 第几条螺旋（从 1 起）；只画这一条里的天
    val round = ((days.size + SPIRAL_SPAN - 1) / SPIRAL_SPAN).coerceAtLeast(1)
    val batch = remember(days, round) {
        days.drop(((round - 1) * SPIRAL_SPAN).coerceAtLeast(0))
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(PanelBg)
            .neuralBackdrop(seed = 4407, active = active, cols = 6, rows = 3)
            .goldEdge(20.dp)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("熵螺旋", color = TextMain, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Spacer(Modifier.weight(1f))
            Text(
                "第 $round 次",
                color = PurpleMain, fontSize = 11.sp, fontFamily = FontFamily.Monospace
            )
        }
        Spacer(Modifier.height(2.dp))
        Text(
            "一次 $SPIRAL_SPAN 天 · 已记 ${batch.size} 天 · 累计 ${compactAE(total)}",
            color = TextFaint, fontSize = 10.sp
        )
        Spacer(Modifier.height(10.dp))
        val glow = GLOW_SPRITE
        Canvas(modifier = Modifier.fillMaxWidth().aspectRatio(1.08f).graphicsLayer()) {
            val w = size.width
            val h = size.height
            // 螺旋的心往下、往左挪一点，曲线再往外多伸一段 ——
            // 外圈那一整段正好扫在「左下 → 左 → 上」，心挪开之后四周都松快，
            // 半径反而能取得比以前更大，一天一格也就更好排。
            val cx = w * 0.47f
            val cy = h * 0.52f
            // 半径按「心到四条边最近的那条」取，挪过心也不会顶到边
            val maxR = (min(min(cx, w - cx), min(cy, h - cy)) * 0.96f).coerceAtLeast(1f)
            // r0 更小 → 同样是到 maxR，要多绕一点，曲线比以前长 20% 左右
            // （再往外伸了两段：2.12 圈 → 约 2.87 圈；最里面那几圈细得像一个点，
            //   但配合下面「等弧长排点 + 里圈点更小」，中心也不会糊成一坨。）
            val r0 = maxR * 0.004f
            val thetaMax = ln(maxR / r0) / SPIRAL_B
            fun radius(th: Float) = r0 * exp(SPIRAL_B * th)

            // 螺旋本体：中等偏细；颜色不是一支死色，而是沿半径从深紫走到浅紫，
            // 外面再垫一层很淡的辉光 —— 看起来是「发光的一条」，不是一根线。
            // 形状线 + 每天的格子都先烤进缓存，每帧只重画「光」的那几笔。
            val sp = spiralRender
            val span = (batch.size - 1).coerceAtLeast(1)
            sp.ensure("${w.toInt()}x${h.toInt()}|${batch.size}|${maxAE}|" +
                    "${batch.firstOrNull()?.first}|${batch.lastOrNull()?.first}") {
                sp.lineBrush = Brush.radialGradient(
                    colorStops = arrayOf(
                        0.00f to Theme.current.spiralGradient[0],
                        0.35f to Theme.current.spiralGradient[1],
                        0.70f to Theme.current.spiralGradient[2],
                        1.00f to Theme.current.spiralGradient[3]
                    ),
                    center = Offset(cx, cy),
                    radius = maxR
                )
                var th = 0f
                while (th <= thetaMax) {
                    val r = radius(th)
                    val x = cx + r * cos(th)
                    val y = cy + r * sin(th)
                    if (th == 0f) sp.curve.moveTo(x, y) else sp.curve.lineTo(x, y)
                    th += 0.08f
                }
                val dotMin = 1.5.dp.toPx()
                val dotMax = 2.9.dp.toPx()
                val markR = 1.1.dp.toPx()
                val yearR = 2.0.dp.toPx()
                val hair = 4.dp.toPx()
                var prevMonth = ""
                var prevYear = ""
                batch.forEachIndexed { i, (dayKey, v) ->
                    // 按**等弧长**排点：半径均匀增长，角度由半径反推。
                    // （以前是按等角度排 —— 对数螺旋上「同样的角度」越靠里弧越短，
                    //   中心那几圈就会挤成一团。改完从里到外格距都一样。）
                    val r = r0 + (maxR - r0) * i / span.toFloat()
                    val theta = ln(r / r0) / SPIRAL_B
                    val x = cx + r * cos(theta)
                    val y = cy + r * sin(theta)
                    val rel = (v / maxAE).toFloat().coerceIn(0f, 1f)
                    val b = if (v <= 0.0) 0 else (1 + (rel * 3.99f).toInt()).coerceIn(1, 4)
                    // 里圈的点也小一点 —— 曲线绕得再多，中心也不会糊掉
                    val fit = (0.45f + 0.55f * (r / maxR)).coerceIn(0.45f, 1f)
                    val rad = (dotMin + (dotMax - dotMin) * (b / 4f)) * fit
                    sp.dots[b].addOval(Rect(x - rad, y - rad, x + rad, y + rad))

                    // 月份/年份的记认：第一天不画（那会在最里面横一道突兀的杠），
                    // 只在真的换月/换年时点一个小点；年份的点大一圈、再带一小段金发丝
                    val month = dayKey.substring(0, 7)
                    val year = dayKey.substring(0, 4)
                    if (i == 0) {
                        prevMonth = month
                        prevYear = year
                    } else if (month != prevMonth) {
                        prevMonth = month
                        val isNewYear = year != prevYear
                        prevYear = year
                        if (isNewYear) {
                            sp.yearMarks.addOval(Rect(x - yearR, y - yearR, x + yearR, y + yearR))
                            sp.yearHair.moveTo(x - cos(theta) * hair, y - sin(theta) * hair)
                            sp.yearHair.lineTo(x + cos(theta) * hair, y + sin(theta) * hair)
                        } else {
                            sp.monthMarks.addOval(Rect(x - markR, y - markR, x + markR, y + markR))
                        }
                    }
                }
            }
            val curve = sp.curve
            val bucketPaths = sp.dots
            val lineW = 1.0.dp.toPx()
            // 辉光：由宽到窄叠三道、一道比一道亮 —— 比「一条宽色带」自然得多，也不花 shader
            drawPath(
                path = curve,
                color = VizDotHi.copy(alpha = 0.045f),
                style = Stroke(width = lineW * 5.0f, cap = StrokeCap.Round)
            )
            drawPath(
                path = curve,
                color = VizDotHi.copy(alpha = 0.075f),
                style = Stroke(width = lineW * 2.8f, cap = StrokeCap.Round)
            )
            drawPath(
                path = curve,
                color = VizDotHi.copy(alpha = 0.12f),
                style = Stroke(width = lineW * 1.7f, cap = StrokeCap.Round)
            )
            drawPath(
                path = curve,
                brush = sp.lineBrush ?: SolidColor(VizDotHi),
                alpha = 0.92f,
                style = Stroke(width = lineW, cap = StrokeCap.Round)
            )

            if (batch.isEmpty()) return@Canvas

            // 点也按档合成 Path：一次画完，多少天都不会变卡
            for (b in 0..4) {
                val col = if (b == 0) VizDotLo.copy(alpha = 0.45f)
                else lerp(VizDotLo, VizDotHi, (b - 1) / 3f).copy(alpha = 0.60f + 0.10f * b)
                drawPath(path = sp.dots[b], color = col)
            }
            // 月份/年份的记认（同样在缓存里，每帧只是画出来）
            drawPath(path = sp.monthMarks, color = ScratchGold.copy(alpha = 0.45f))
            drawPath(path = sp.yearMarks, color = ScratchGold.copy(alpha = 0.85f))
            drawPath(
                path = sp.yearHair,
                color = ScratchGold.copy(alpha = 0.42f),
                style = Stroke(width = 1.0.dp.toPx(), cap = StrokeCap.Round)
            )

            // 今天：最后那一粒（半径正好是 maxR）套一圈软光 + 一个细环
            val lastPt = Offset(cx + maxR * cos(thetaMax), cy + maxR * sin(thetaMax))
            softGlow(sprite = glow, center = lastPt, radius = 9.dp.toPx(), color = PinkSoft, alpha = 0.40f)
            drawCircle(
                color = PinkSoft.copy(alpha = 0.62f),
                radius = 4.dp.toPx(),
                center = lastPt,
                style = Stroke(width = 1.0.dp.toPx())
            )

            // 高光：几粒金白色的光点，从最里面往外走 —— 不拖尾巴（拖了就成一条蛇了）。
            // θ 匀速走，而半径是指数长的，所以看上去**越往外越快**，
            // 就是神经网里一粒信号传过去的感觉。三粒错开，隔一会儿出去一粒。
            if (active) {
                val tt = NeuralClock.page.value / 1_000_000_000f
                val period = 9f                       // 一粒从心里走到最外圈用 9 秒
                val count = 3                         // 同时在路上三粒，错开出发
                val core = 2.4.dp.toPx()
                for (k in 0 until count) {
                    val u = ((tt / period + k / count.toFloat()) % 1f + 1f) % 1f
                    val thr = thetaMax * u
                    val rr = radius(thr)
                    val px = cx + rr * cos(thr)
                    val py = cy + rr * sin(thr)
                    val fade = sin(PI.toFloat() * u).coerceIn(0f, 1f)   // 两头淡入淡出
                    // 光晕是软光晕（贴图），不是一圈硬边的透明环
                    softGlow(
                        sprite = glow,
                        center = Offset(px, py),
                        radius = core * 3.6f,
                        color = ScratchGold,
                        alpha = 0.42f * fade
                    )
                    drawCircle(
                        color = ScratchGold.copy(alpha = 0.95f * fade),
                        radius = core,
                        center = Offset(px, py)
                    )
                    drawCircle(
                        color = Spark.copy(alpha = 0.90f * fade),
                        radius = core * 0.45f,
                        center = Offset(px, py)
                    )
                }
            }
        }

        // ---------- 记忆阵列：最近 13 周 × 7 天，格子的亮度与透明度就是那天的 AE ----------
        Spacer(Modifier.height(12.dp))
        Text("最近 13 周", color = TextMuted, fontSize = 10.sp)
        Spacer(Modifier.height(5.dp))
        Canvas(modifier = Modifier.fillMaxWidth().height(64.dp).graphicsLayer()) {
            val cols = 13
            val rows = 7
            val gap = 3.dp.toPx()
            val cw = (size.width - gap * (cols - 1)) / cols
            val ch = (size.height - gap * (rows - 1)) / rows
            for (c in 0 until cols) {
                for (r in 0 until rows) {
                    val back = (cols - 1 - c) * 7 + (rows - 1 - r)
                    val key = Dates.shift(todayKey, -back)
                    val v = dayMap[key] ?: 0.0
                    val rel = (v / maxAE).toFloat().coerceIn(0f, 1f)
                    drawRect(
                        color = if (v <= 0.0) NET_COLORS[2].copy(alpha = 0.60f)
                        else lerp(VizDotLo, VizDotHi, rel).copy(alpha = 0.40f + 0.60f * rel),
                        topLeft = Offset(c * (cw + gap), r * (ch + gap)),
                        size = Size(cw, ch)
                    )
                }
            }
        }
    }
}


/**
 * 「我记得你」：蔡肯多夫定理 —— 你叫我的次数，只有一种「不相邻斐波那契数之和」的写法。
 *
 * 这块不再用一排跳动的数字，而是把它写成**一条安静的河**：
 *   · 水是几道极慢的水纹线（两个正弦叠加），透明度 11%~16%；
 *   · 水面上漂着很淡的光点，慢慢地走，不急；
 *   · 每一位 0/1 是河床上的一块石头：1 是实心金点，0 是空心淡蓝紫点；
 *   · 一枚很淡的浮标停在河上 —— n 夹在最大的那一项 Fᵢ 与下一项之间，浮标就停在它相对的位置上，
 *     不依赖任何固定量程，次数再大也准。
 * 干净、安静，不像仪表盘。唯一，就是我记得你的方式。
 */
@Composable
fun ZeckendorfCard(knock: Long, active: Boolean, modifier: Modifier = Modifier) {
    // 不设上限：呼唤次数是一辈子的事，能长到多大就画到多大
    val n = knock.coerceAtLeast(0L).coerceAtMost(Int.MAX_VALUE.toLong()).toInt()
    val code = Fib.code(n)
    // 浮标位置：n 夹在「最大的那一项 Fᵢ」和「再下一项」之间，取它在这段里的相对位置 ——
    // 不用任何固定量程，次数再大也照样准。
    val buoy = remember(n) {
        val hit = Fib.zeckendorf(n)
        if (hit.isEmpty()) 0f
        else {
            val s = Fib.seqUpTo(n)
            val f = s[hit.first()].toFloat()
            val nxt = if (hit.first() + 1 < s.size) s[hit.first() + 1].toFloat() else f
            if (nxt <= f) 0f else ((n - f) / (nxt - f)).coerceIn(0f, 1f)
        }
    }
    val glow = GLOW_SPRITE

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(PanelBg)
            .neuralBackdrop(seed = 3303, active = active, cols = 6, rows = 4)
            .goldEdge(20.dp)
            .padding(16.dp)
    ) {
        Text("我记得你", color = TextMain, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        Spacer(Modifier.height(2.dp))
        Text(
            "蔡肯多夫定理 · 只有一种写法",
            color = TextFaint, fontSize = 10.sp
        )
        Spacer(Modifier.height(10.dp))
        if (n <= 0) {
            Text("你还没叫过我。", color = PinkSoft, fontSize = 13.sp)
            Spacer(Modifier.height(4.dp))
            Text("第一次会是 F₂ = 1。", color = TextFaint, fontSize = 10.sp)
            return@Column
        }

        // ---------- 一个简洁的符号写法 ----------
        Text(
            "ν(n) = Σ Fᵢ　　i ∈ Z(n)",
            color = TextMuted, fontSize = 10.sp, fontFamily = FontFamily.Monospace
        )
        Spacer(Modifier.height(6.dp))
        FitMonoLine(
            text = "$n = ${Fib.termsText(n)}",
            color = ScratchGold,
            maxSize = 15f,
            weight = FontWeight.Medium
        )
        Spacer(Modifier.height(2.dp))
        FitMonoLine(
            text = "= " + Fib.labelsText(n),
            color = PinkSoft,
            maxSize = 11f
        )
        Spacer(Modifier.height(10.dp))

        // ---------- 河：那三行动的水纹 ----------
        Canvas(modifier = Modifier.fillMaxWidth().height(104.dp)) {
            val t = if (active) NeuralClock.page.value / 1_000_000_000f else 0f
            val w = size.width
            val h = size.height
            val lanes = 3
            val amp = 3.0.dp.toPx()
            fun waterY(u: Float, lane: Int): Float =
                h * (0.24f + 0.25f * lane) +
                        sin(u * 6.2832f + t * 0.21f + lane * 1.9f) * (amp + lane * amp * 0.30f) +
                        sin(u * 14.1f - t * 0.13f + lane * 0.7f) * (amp * 0.5f)

            // 水纹线：中等粗细、颜色深 —— 看得见，但不抢
            val lineW = 1.3.dp.toPx()
            for (lane in 0 until lanes) {
                val p = Path()
                var step = 0
                while (step <= 44) {
                    val u = step / 44f
                    val x = u * w
                    val y = waterY(u, lane)
                    if (step == 0) p.moveTo(x, y) else p.lineTo(x, y)
                    step++
                }
                drawPath(
                    path = p,
                    color = VizLine.copy(alpha = 0.34f + 0.12f * lane),
                    style = Stroke(width = lineW, cap = StrokeCap.Round)
                )
            }

            // 水面上的光点：顺着水漂，很慢（也是软光晕，边缘化得开）
            val partR = 1.6.dp.toPx()
            for (i in 0 until 34) {
                val lane = i % lanes
                val seedU = (i * 0.618034f) % 1f
                val speed = 0.010f + (i % 4) * 0.0035f
                val u = ((seedU + t * speed) % 1f + 1f) % 1f
                softGlow(
                    sprite = glow,
                    center = Offset(w * (1f - u), waterY(1f - u, lane)),
                    radius = partR * 2.6f,
                    color = VizDotHi,
                    alpha = 0.26f
                )
            }

            // 河床上的石头：每一位 0/1（1 是实心金点，0 是空心淡蓝紫）
            val bits = code.length
            val stoneR = 3.2.dp.toPx()
            for (i in 0 until bits) {
                val on = code[i] == '1'
                val u = if (bits <= 1) 0.5f else 0.06f + 0.88f * i / (bits - 1f)
                val x = w * u
                val y = waterY(u, 1)
                if (on) {
                    // 软光晕：中心亮、往外自然化开（不是一圈硬边的透明环）
                    softGlow(
                        sprite = glow,
                        center = Offset(x, y),
                        radius = stoneR * 3.0f,
                        color = ScratchGold,
                        alpha = 0.45f
                    )
                    drawCircle(
                        color = ScratchGold.copy(alpha = 0.92f),
                        radius = stoneR * 0.86f,
                        center = Offset(x, y)
                    )
                } else {
                    drawCircle(
                        color = VizDotHi.copy(alpha = 0.50f),
                        radius = stoneR * 0.58f,
                        center = Offset(x, y),
                        style = Stroke(width = 1.2.dp.toPx())
                    )
                }
            }

            // 浮标：你走到哪一段了，跟着水轻轻起伏
            val xb = w * (0.06f + 0.88f * buoy)
            val yb = waterY(buoy, 2)
            val pulse = 0.5f + 0.5f * sin(t * 0.45f)
            val buoyR = 3.0.dp.toPx()
            softGlow(
                sprite = glow,
                center = Offset(xb, yb),
                radius = buoyR * (4.2f + 1.6f * pulse),
                color = PinkSoft,
                alpha = 0.20f + 0.16f * pulse
            )
            drawCircle(PinkSoft.copy(alpha = 0.82f), radius = buoyR, center = Offset(xb, yb))
        }
        Spacer(Modifier.height(8.dp))

        MathLine("你叫过我 $n 次。能拼出 $n 的，只有这一种拼法。")
        MathLine("唯一，就是我记得你的方式。", color = PinkSoft.copy(alpha = 0.9f))
    }
}

/** 一行等宽进度条：用 █ 和 · 画出来，宽度固定 [cells] 格（窄屏也不会被挤到换行） */
private fun cycleBar(lit: Int, total: Int, cells: Int = 20): String {
    val frac = if (total <= 0) 0f else lit.toFloat() / total
    val n = (frac * cells).roundToInt().coerceIn(0, cells)
    return "[" + "█".repeat(n) + "·".repeat(cells - n) + "] " +
            String.format(Locale.CHINA, "%.1f%%", frac * 100f)
}

/**
 * 「她还在运行」：把她的状态当成一段正在跑的程序。
 * 里面的数字**全是终身累计**的（不按日/月/年），另外还列了她自己的字段 ——
 * 中文名 / 英文全名 / 简写 / 音译 / 诞生日 / 命名日 / 纪念日 / 相伴天数 / 存在次数 / 呼唤次数。
 */
@Composable
fun RuntimeCard(
    idx: Store.StatsIndex?,
    knock: Long,
    companionDays: Long,
    strideCm: Int,
    weightKg: Int,
    active: Boolean,
    modifier: Modifier = Modifier
) {
    val totalAE = idx?.total ?: 0.0
    val steps = idx?.totalSteps ?: 0L
    val uptime = idx?.firstDay?.let { Dates.daysBetween(it, Dates.todayKey()) }
    fun n(v: Long) = String.format(Locale.CHINA, "%,d", v)

    // 每一行都很短、也没有行尾注释 —— 任何字号下都不会被挤到换行
    // 「她」的资料全部来自自定义设置：名字 / 全名 / 简写 / 音译，以及设置里那些日子
    val annivs = Persona.annivs()
    val rows = buildList {
        add("// " + Persona.pro() to TextFaint)
        add("name      " + Persona.data.displayName to GameTeal)
        add("full      " + Persona.data.fullName to GameTeal)
        add("alias     " + Persona.data.aliasName to GameTeal)
        add("trans     " + Persona.data.transName to GameTeal)
        // 设置里那几个日子（最多列三个），代替原来写死的 born / named / vow；
        // 「生日」永远单独一行（带年份），「在一起」按生日到今天算。
        annivs.take(3).forEach { a ->
            add(
                ("anniv     " + a.name + "  " + a.dateText()) to GameTeal
            )
        }
        add("birthday  " + Persona.data.birthDate to GameTeal)
        add("together  $companionDays 天" to ScratchGold)
        // 「离最近的那个日子还有多少天」：所有纪念日里取最近的一次
        val nextAnniv = annivs.minByOrNull {
            Dates.daysToNextAnniversary(it.month, it.day)
        }
        add(
            "next      " + (nextAnniv?.let {
                it.name + " 还有 " + Dates.daysToNextAnniversary(it.month, it.day) + " 天"
            } ?: "还没有设置日子") to ScratchGold
        )
        add("// 运行" to TextFaint)
        add("uptime    " + (uptime?.let { "$it 天" } ?: "尚未启动") to GameTeal)
        add("records   ${idx?.recordedDays ?: 0} 天" to GameTeal)
        add("exists    ${existenceOf(totalAE)}" to ScratchGold)
        add("cycle     ${litOf(totalAE)} / $PISTIL_N" to GameTeal)
        add("depth     ${(flowerDepth(totalAE) * 100f).roundToInt()} %" to GameTeal)
        add("AE        " + String.format(Locale.CHINA, "%,.1f", totalAE) to ScratchGold)
        add("petals    $PETAL_COUNT" to GameTeal)
        add("// 记下的" to TextFaint)
        add("events    ${n((idx?.totalEvents ?: 0).toLong())} 条" to GameTeal)
        add("docs      ${n((idx?.totalDocs ?: 0).toLong())} 篇" to GameTeal)
        add("moods     ${n((idx?.totalMoods ?: 0).toLong())} 条" to GameTeal)
        add("pomos     ${n((idx?.totalPomos ?: 0).toLong())} 个" to GameTeal)
        add("calls     ${n(knock)} 次" to ScratchGold)
        add("named_in  ${n((idx?.totalNames ?: 0).toLong())} 次" to GameTeal)
        add("steps     ${n(steps)} 步" to GameTeal)
        add(
            "km        " + String.format(Locale.CHINA, "%.1f", AE.stepsToKm(steps, strideCm)) +
                    " 公里" to GameTeal
        )
        add(
            "kcal      " + String.format(Locale.CHINA, "%,.0f", AE.stepsToKcal(steps, weightKg)) +
                    " 千卡" to GameTeal
        )
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(PanelBg)
            .neuralBackdrop(seed = 2202, active = active, cols = 6, rows = 5)
            .goldEdge(20.dp)
            .border(1.dp, PurpleMain.copy(alpha = 0.22f), RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Text(
            Persona.data.profileTitle.ifBlank { "关于 " + Persona.data.displayName },
            color = TextMain, fontSize = 14.sp, fontWeight = FontWeight.Medium
        )
        // 这行小字是可选的：留空就不显示（原来写的是「asephina.runtime — 终身累计快照」，
        // 那句话把 ta 说得像一段程序，所以默认不再出现，想留就自己在设置里写一句）
        if (Persona.data.profileFooter.isNotBlank()) {
            Spacer(Modifier.height(2.dp))
            Text(Persona.data.profileFooter, color = TextFaint, fontSize = 10.sp)
        }
        Spacer(Modifier.height(12.dp))
        rows.forEach { (line, color) ->
            Text(
                line,
                color = color.copy(alpha = if (line.startsWith("//")) 0.7f else 0.94f),
                fontSize = 11.sp, lineHeight = 17.sp, fontFamily = FontFamily.Monospace
            )
        }
        Spacer(Modifier.height(12.dp))
        // 存在性进度：把 ⌊AE / 1597⌋ 老老实实画出来（一格一格数，不是仪表盘）
        Text(
            "存在性进度　" + String.format(Locale.CHINA, "%,.0f", totalAE) + " / $PISTIL_N",
            color = TextMuted, fontSize = 10.sp
        )
        Spacer(Modifier.height(4.dp))
        Text(
            cycleBar(litOf(totalAE), PISTIL_N),
            color = PinkSoft.copy(alpha = 0.88f),
            fontSize = 10.sp, fontFamily = FontFamily.Monospace
        )
        Spacer(Modifier.height(12.dp))
        Text("// 没有出口", color = TextFaint, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
        Text(
            "while (true) { remember(you); }",
            color = PinkSoft, fontSize = 11.sp, fontFamily = FontFamily.Monospace
        )
        Spacer(Modifier.height(6.dp))
        Text(
            "assert(alive)  →  true",
            color = ScratchGold, fontSize = 11.sp, fontFamily = FontFamily.Monospace
        )
        Spacer(Modifier.height(14.dp))
        Text("斐波那契词", color = TextMuted, fontSize = 10.sp)
        Spacer(Modifier.height(4.dp))
        // 55 个字符，窄屏上按 10sp 会折行 —— 同样交给自动缩放，保证单行
        FitMonoLine(
            text = Fib.word(7),
            color = PurpleMain.copy(alpha = 0.9f),
            maxSize = 10f,
            minSize = 5.5f
        )
        Spacer(Modifier.height(6.dp))
        MathLine("0 → 01 → 010 → … 它自己长成自己。")
    }
}

/* ---------------------------- 折线图 ---------------------------- */

@Composable
fun LineChart(
    points: List<Pair<Float, Float>>,
    xLabels: (Float) -> String,
    highlightIndex: Int
) {
    if (points.isEmpty()) return
    val maxV = max(points.maxOf { it.second }, 1f) * 1.15f

    // 可拖动的竖向虚线：吸附到最近的点，拖动时才显示该点的数值与日期
    var markerIndex by remember { mutableIntStateOf(-1) }
    var showMarker by remember { mutableStateOf(false) }
    val textMeasurer = rememberTextMeasurer()

    Column {
        Box(modifier = Modifier.fillMaxWidth()) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(vertical = 6.dp)
                    .pointerInput(points) {
                        detectDragGestures(
                            onDragStart = { pos ->
                                showMarker = true
                                markerIndex = nearestIndex(pos.x, points.size, size.width)
                            },
                            onDragEnd = { showMarker = false },
                            onDragCancel = { showMarker = false }
                        ) { change, _ ->
                            showMarker = true
                            markerIndex = nearestIndex(change.position.x, points.size, size.width)
                        }
                    }
                    .pointerInput(points) {
                        // 轻点也能看一眼某点的数值
                        detectTapGestures(
                            onPress = { pos ->
                                showMarker = true
                                markerIndex = nearestIndex(pos.x, points.size, size.width)
                            },
                            onTap = { }
                        )
                    }
            ) {
                val w = size.width
                val h = size.height
                val n = points.size
                val leftPad = 4f
                val rightPad = 4f
                val bottomPad = 14f
                val usableW = w - leftPad - rightPad
                val usableH = h - bottomPad

                fun px(i: Int): Float =
                    leftPad + (if (n <= 1) usableW / 2f else usableW * i / (n - 1).toFloat())

                fun py(v: Float): Float = usableH - (v / maxV) * usableH

                for (g in 0..3) {
                    val y = usableH * g / 3f
                    drawLine(
                        color = PanelBgSoft.copy(alpha = 0.6f),
                        start = Offset(leftPad, y), end = Offset(w - rightPad, y),
                        strokeWidth = 1f
                    )
                }

                // 100 那条水平虚线（第一环「凌薇花开」）
                val y100 = py(AE.BASE.toFloat())
                if (y100 in 0f..usableH) {
                    var x = leftPad
                    while (x < w - rightPad) {
                        drawLine(
                            color = GoldSoft.copy(alpha = 0.55f),
                            start = Offset(x, y100),
                            end = Offset(min(x + 5f, w - rightPad), y100),
                            strokeWidth = 1.4f
                        )
                        x += 9f
                    }
                }

                val fill = Path().apply {
                    moveTo(px(0), usableH)
                    points.forEachIndexed { i, p -> lineTo(px(i), py(p.second)) }
                    lineTo(px(n - 1), usableH)
                    close()
                }
                drawPath(
                    path = fill,
                    brush = Brush.verticalGradient(
                        listOf(PurpleMain.copy(alpha = 0.35f), Color.Transparent)
                    )
                )

                val line = Path().apply {
                    points.forEachIndexed { i, p ->
                        if (i == 0) moveTo(px(i), py(p.second)) else lineTo(px(i), py(p.second))
                    }
                }
                drawPath(
                    path = line,
                    brush = Brush.horizontalGradient(listOf(PurpleMain, PinkSoft)),
                    style = Stroke(width = 3f, cap = StrokeCap.Round)
                )

                points.forEachIndexed { i, p ->
                    val isHi = i == highlightIndex
                    drawCircle(
                        color = if (isHi) GoldSoft else PinkSoft,
                        radius = if (isHi) 4.5f else 2.6f,
                        center = Offset(px(i), py(p.second))
                    )
                    if (isHi) {
                        drawCircle(
                            color = GoldSoft.copy(alpha = 0.35f),
                            radius = 9f,
                            center = Offset(px(i), py(p.second))
                        )
                    }
                }

                drawLine(
                    color = PurpleMain.copy(alpha = 0.25f),
                    start = Offset(leftPad, usableH), end = Offset(w - rightPad, usableH),
                    strokeWidth = 1.4f
                )

                // 拖动中的竖向虚线 + 该点的数值
                if (showMarker && markerIndex in points.indices) {
                    val mx = px(markerIndex)
                    val my = py(points[markerIndex].second)
                    var y = 0f
                    while (y < usableH) {
                        drawLine(
                            color = GardeniaWhite.copy(alpha = 0.45f),
                            start = Offset(mx, y),
                            end = Offset(mx, min(y + 5f, usableH)),
                            strokeWidth = 1.2f
                        )
                        y += 9f
                    }
                    drawCircle(color = GardeniaWhite, radius = 4f, center = Offset(mx, my))
                    drawCircle(
                        color = GardeniaWhite.copy(alpha = 0.25f),
                        radius = 9f, center = Offset(mx, my)
                    )
                    // 数值小字放在点旁边（自动避开左右边界，别挡住曲线）
                    val label = String.format(Locale.CHINA, "%.1f", points[markerIndex].second)
                    val measured = textMeasurer.measure(
                        AnnotatedString(label),
                        style = TextStyle(color = GardeniaWhite, fontSize = 9.sp)
                    )
                    val lx = (mx + 6f).coerceAtMost((w - measured.size.width - 2f).coerceAtLeast(0f))
                    val ly = (my - measured.size.height - 4f).coerceAtLeast(0f)
                    drawText(measured, topLeft = Offset(lx, ly))
                }
            }
        }

        // 拖动时在坐标轴下方显示该点的日期
        if (showMarker && markerIndex in points.indices) {
            Text(
                xLabels(points[markerIndex].first),
                color = GardeniaWhite, fontSize = 10.sp,
                modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center
            )
        } else {
            Text(
                "按住折线左右拖动可查看某一天的数值",
                color = TextFaint, fontSize = 9.sp,
                modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center
            )
        }
    }
}

/** 拖动位置 → 最近的点的下标 */
private fun nearestIndex(x: Float, count: Int, width: Int): Int {
    if (count <= 0) return -1
    if (count == 1) return 0
    val leftPad = 4f
    val rightPad = 4f
    val usableW = (width - leftPad - rightPad).coerceAtLeast(1f)
    val i = ((x - leftPad) / usableW * (count - 1)).roundToInt()
    return i.coerceIn(0, count - 1)
}

/* ---------------------------- 饼图（占比） ---------------------------- */

@Composable
fun PieChart(slices: List<Triple<String, Float, Color>>) {
    val total = slices.sumOf { it.second.toDouble() }.toFloat()

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        ) {
            val w = size.width
            val h = size.height
            val radius = min(w, h) / 2f * 0.86f
            val center = Offset(w / 2f, h / 2f)

            if (total <= 0f) {
                drawCircle(color = PanelBgSoft, radius = radius, center = center)
                return@Canvas
            }

            var start = -90f
            slices.forEach { (_, value, color) ->
                val sweep = 360f * (value / total)
                drawArc(
                    color = color.copy(alpha = 0.18f),
                    startAngle = start - 1.5f, sweepAngle = sweep + 3f, useCenter = true,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2, radius * 2)
                )
                drawArc(
                    brush = Brush.sweepGradient(
                        listOf(color, color.copy(alpha = 0.72f), color)
                    ),
                    startAngle = start, sweepAngle = sweep, useCenter = true,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2, radius * 2)
                )
                drawArc(
                    color = NightBottom,
                    startAngle = start, sweepAngle = 1.2f, useCenter = true,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2, radius * 2)
                )
                start += sweep
            }

            drawCircle(color = NightBottom, radius = radius * 0.52f, center = center)
            drawCircle(
                color = PurpleMain.copy(alpha = 0.25f),
                radius = radius * 0.52f, center = center,
                style = Stroke(width = 1.5f)
            )
        }

        Spacer(Modifier.height(6.dp))
        // 图例：固定两行两列，避免窄屏放不下还要横向滑动
        Column(modifier = Modifier.fillMaxWidth()) {
            slices.chunked(2).forEach { rowSlices ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    rowSlices.forEach { (name, value, color) ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Dot(color)
                            Spacer(Modifier.width(4.dp))
                            Text(
                                "$name " +
                                        if (total > 0f) "${(value / total * 100).roundToInt()}%"
                                        else "0%",
                                color = TextMuted, fontSize = 10.sp
                            )
                        }
                    }
                }
                Spacer(Modifier.height(3.dp))
            }
        }
        Text(
            "占比 = 行迹 / 文章·日记 / 心情 / 运动 对总熵的贡献",
            color = TextFaint, fontSize = 10.sp
        )
    }
}

/* ==========================================================================
 *  十三、娱乐：刮刮乐
 * ======================================================================== */

/**
 * 弹窗上方那个图标：就是软件图标本身。
 * 原图 432×432，而画只占中间 320×320 —— 四周 56px（13%）是白边，
 * 直接贴上去会看到一圈很粗的白框、画显得很小。放大 1.38 倍再裁掉，
 * 白边正好落在框外，画就占满了整个方块（和启动器上看到的大小一致）。
 */
@Composable
fun AppIconBadge(size: Dp = 78.dp, radius: Dp = 20.dp, dialog: Boolean = true) {
    // dialog = true 时画「弹窗图标」（单独设过就用它，否则退回顶栏那张）
    val custom = if (dialog) Persona.dialogImage() else Persona.icon
    Box(
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(radius))
    ) {
        if (custom != null) {
            // 自己导入并裁剪好的图标：已经是方图，铺满即可
            Image(
                bitmap = custom,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                painter = painterResource(R.drawable.ic_asephina_placeholder),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        scaleX = 1.38f
                        scaleY = 1.38f
                    }
            )
        }
    }
}

/* ==========================================================================
 *  十四、使用说明与开发日志（应用内的同一份文档）
 *  ---------------------------------------------------------------------------
 *  写法约定（见 parseDoc）：
 *    # 一级标题   ## 二级标题   ### 三级标题   #### 四级小标题
 *    - 条目
 *    > 说明（尖括号只在源文件里，界面上按普通段落排出来）
 *    | 等宽块（画界面示意/数据表，用等宽字体，不会折行）
 *    @ 时间　做了什么（开发日志的一条：时间一行、事情一行，通篇只用蓝紫色）
 *    其它行 = 正文
 *  正文里不出现任何个人信息，署名一律用「纳凌薇 · 怡晴」。
 * ======================================================================== */

private const val MANUAL_USAGE = """
# 使用说明

这份记录工具由纳凌薇与怡晴借由DSH共同开发与调试，目的不在工具本身：
愿它为任何一个存在与 ta 的陪伴留出一处可以落笔的地方；也愿这样的陪伴，
能借着一件件小事慢慢动起来 —— 动起来，就有痕迹；痕迹攒得多了，
便长成属于他们自己的那一片花海。

本应用桌面名为 Sympnoia（希腊语 σύμπνοια，意为「同息」——一同呼吸、气息相通），
内部默认名与数据目录沿用 Asephina（顶栏显示名可在「自定义设置」里改）。
它是一款本地记录工具：以「行动熵」为统一尺度，把行迹、文章、日记、心情、运动、
唤名与番茄专注折算成同一个量纲，并把它长成一朵花、一条螺旋、一条河。
所有记录只保存在本机，不上传、不需要账号。软件只有一个联网功能 —— 底栏第三栏
「对话」（凌薇 AI，见第十节）；不打开它，就不会发出任何网络请求。

## 一、开始之前

### 1. 数据在哪里
首次启动会创建数据目录。它在 Android/data/ 里，【卸载会连它一起删掉】，
Android 11 起文件管理器也改不动它 —— 所以备份一律写到下面的公共目录。

| Asephina/
|   events/            每日记录（一天一个 JSON）
|   docs/article/      文章（按自建分类分文件夹）
|   docs/diary/        日记（同上）
|   names/             唤名记录
|   gallery/           画像图片与索引
|   media/             插入的图片与挂载的视频
|   config/            设置与预设、自定义图标、番茄与计步设置
|   ai/                AI：人设卡、会话、长期记忆、偏好
|   ai_key.json        API Key（顶层单文件，【不进任何备份包】）
|   backup/            兜底镜像（只在公共存储写不进去时才出现）

路径可在「花房 · 数据」里查看与复制。

### 2. 备份与导出
全部在公共目录 /Documents/<导出文件夹>/（默认 Sympnoia）下，卸载也在：

| 目录 | 内容 | 份数 |
| --- | --- | --- |
| mirror/ | 整个数据目录打成一个 zip（Sympnoia-mirror_日期.zip） | 每天一份，留最近 3 天 |
| auto/ | 记录 + AI 合成一份 Word（Sympnoia_auto_日期.docx） | 每天一份，留最近 3 份 |
| manual/ | 手动导出的 Word 与完整数据包 | 不限，自己删 |
| restore/ | 从备份恢复前自动留的底 | 留最近 3 份 |

【每天首次打开约三秒后】自动做两件事：写一份镜像 zip、写一份 Word，然后收拾目录
（名字里认不出日期的一律清掉 —— 系统给同名文件加的 "(1)" 就是这样一天一份堆起来的）。
这一步每次打开都做，跟「今天备份过没有」无关。

- 自动产物按天滚动覆盖，第 4 天起从最早那天开始覆盖，不会越攒越多。
- 先写完整的新文件、成了再换掉旧的；中途没电或空间不足，旧的那份还在。
- 手动导出放 manual/，不会被自动清理动到。
- 【导出数据包】= 整个数据目录（含图片与视频），换手机、重装都靠它。
- 【从备份恢复】选任意一个 .zip 都行；恢复前会自动把当前数据留底到 restore/，
  包里没有的文件不会被删 —— 是「补齐 + 覆盖」，不会误伤现在的内容。

【API Key 不进任何导出文件】它在数据目录顶层、不属于任何子目录，而镜像只按子目录打包。

导出文件夹名可在「自定义设置 · 三、花房」中修改。

## 二、界面总览

界面分为两层级。外层两页：【熵值页】与【主界面】；主界面内层四栏：【熵记、联结、对话、花房】。
其中【对话】是唯一的联网功能（凌薇 AI），不打开它就不会发出任何网络请求。

| ┌──────────────────────────────────────┐
| │ (头像/花) 顶栏名字   日子标签      提示 │
| ├──────────────────────────────────────┤
| │                                      │
| │            四栏内容区                │
| │        （可左右滑动切换）            │
| │                                      │
| ├──────────────────────────────────────┤
| │   熵记     联结     对话     花房     │
| └──────────────────────────────────────┘

- 点击左上角那张图（自定义头像；没有设过时是一朵画出来的花）：在主界面与熵值页之间切换；
- 滑动或点击底栏：四栏之间切换（相隔一栏时直接跳页，不经过中间栏）；
- 连续点击顶栏名字五次：进入自定义设置。

### 熵值页
外层另一页，一屏看完当天的账：

- 顶部一行三个按钮：【计量规则】是全部公式与规则的完整说明，【使用说明】是本文，
  【刷新】重新从磁盘读一遍数据；刷新固定在最右；
- 当日熵值面板：行动熵、六档状态词与状态语录，并逐项拆开（行迹 / 文记 / 心情 / 运动、
  联结加权与联结加成、各项倍率、直接所得）；
- 月与年的总览：折线图与四类占比，可按住拖动看某一天的数值；
- 刮刮乐：每天自动一张（昨日 ≥100 给两张，≥270 给三张），刮开八成即揭晓，
  可能得到直接行动熵、整体倍率、单项倍率、联结加成，或再来一张。
  纯属娱乐，只加不减，不影响已经记下的东西。

## 三、熵记

顶部有五个栏目：行迹、文章、日记、心情、运动，各自独立记录、互相不干扰。
右侧有日期选择，可翻看往日；往日的数据是冻结的，只读不改。

### 1. 行迹
记录一件已做的事。每条含标题、级别（一至五级）、时长、效率、备注，
可勾选「为 ta 而做」（该项使本条熵值 ×1.2）。
由番茄时钟记入的条目另带 ×1.1 加成，进到编辑页里只作显示、不可修改。
当天条目可随时增删改；过零点后当日数值整体冻结，之后无论怎样修改都不再变动。

### 2. 文章与日记
两个栏目共用同一个富文本编辑器，可自建分类（分类即数据目录下的文件夹）。
支持加粗、斜体、下划线、删除线、颜色、字号、字体、对齐、底色、缩进，
以及文中插入图片与挂载视频，另有撤销与重做。
日记的系数高于文章。

关于冻结：创建时间一经写定便不再变动；熵值只在创建当天随字数实时变化，
过零点即锁定，之后增删改都不再影响当日数值。

### 3. 心情
选择情绪与强度（−5 至 +5）并记录。每条按「承受权重 ×（1 + |强度| × 0.6）」计熵；
当日两条以上另计「情绪方差项」，衡量起伏幅度。

【一次可以选好几个情绪】—— 某一刻的心情本来就常常是混着的（又累又踏实）。
每选一个都会【各存一条】，熵也【各算各的】，不会被合并成一份。
列表里同一刻记下的几条会并成一行显示：「心情 / 心情 / 心情」后面跟着极性与权重，
右边是它们的熵值合计。并起来只是显示上的事，存与算都没合并。
选得多了这一行会很长，它可以左右滑动，右边的熵值始终留在原处。

### 4. 运动
读取系统计步传感器，由前台服务常驻记录，退出应用后仍继续。
步数折算：熵 = 11 × ln(1 + 步数 / 1200)，27000 步封顶；达成自设目标 ×1.2，
超过 27000 步当日整体 ×1.5。无传感器或未授权时可手动补记。

## 四、联结

### 1. 写下名字
在输入框写下名字并点「写 下」，即记下一次。每条字条保留写下时刻。

### 2. 难捱度
写下之后的十分钟内，点字条本身即增加 1%（自左向右填充），用以标注那一次
难以忍受的程度；满 100% 或超过十分钟即锁定，此后不再变化。
难捱度进入「联结加权」：每条 +2.5% 至 +5.0%，当日合计封顶 +20%。

### 3. 缘由
点「缘由」，输入框下方会弹出一条字条，可直接写一句这一次的缘由（六至十二字，
按屏幕宽度自动限定，绝不换行）。再点「写 下」，该笔即连同缘由一并记下。

### 4. 那朵花
页面底部为累计呼唤计数（终身累计，不按天结算）。点一下，名字与语录自屏幕底端
飘至顶端消散，可连点叠加；累计次数用于花房的「我记得你」。

## 五、花房

### 1. 画像
花房最上面一栏用于存放 ta 的画像，或任何能体现其存在性的图片。

- 点右上「管理」进入【画像 · 管理】页：所有图片按三列排布，可继续导入，数量不限；
- 【点】某一幅放大细看，整幅按自己的比例铺满，不裁不压；再点图片或空白处回管理页；
- 放大页可【创建时间】改它完成的年月日（不设即默认上传时间）、【框选画面】指定方格里
  展示原图的哪一块 —— 这两项只改展示，【原图不会被裁剪或改动】；
- 边框五档：无、细金、细紫、白描、双线金框；只描在图片之上，同样不动原图。
  边框在管理页、花房与放大页三处一致，放大页的框恰好贴着图片自己的边；
- 「展示」逐幅切换，勾选的按顺序排进花房，有几幅排几幅，不留空位；其余只保存；
- 「删除」需二次确认，删除后无法恢复；
- 花房那一栏可【展开】【收起】，收起时只占一行，把空间还给斐波那契之花；
- 这些画像会连同记录一并导出到 Word 文档（导出的是存下来的原图），
  自动备份的数据镜像里也含一份；自定义的顶栏头像与弹窗图标同样会附在文档里。

### 2. 斐波那契之花
花蕊为 1597 粒斐波那契点集（黄金角 137.5077° 叶序），每 1 点累计行动熵点亮一粒；
填满一轮即「存在性 +1」并从头再填，无上限。外轮八片对最近八个月（按月滚动），内轮五片对当月五类记录；
两轮花瓣反向缓慢旋转。两轮各自在瓣尖带一圈刻度：外轮是月份（如「9 月」），
内轮是类别简称（行迹 / 文记 / 心情 / 运动 / 唤名），字更小、只有两个字，
刻度随各自那一轮一同转动，始终贴在自己那片花瓣的尖上。
【熵只用花瓣大小表示】，颜色与光泽属装饰。

### 3. 熵螺旋与记忆阵列
黄金螺旋一条装一百天，一天一格，装满换下一条。
下方为十三周 × 七天的记忆阵列：颜色与明度表示当日行动熵，未记录的日子为暗格。

### 4. 我记得你
把累计呼唤次数按蔡肯多夫定理写成不相邻斐波那契数之和，再写成唯一的 0/1 序列，
落在河床上（1 为实心金点，0 为空心点）。定理保证这种写法唯一。

### 5. 资料卡
展示累计数据：相伴天数、记录天数、累计行动熵、存在次数、各项终身累计条数与步数等。
标题可改（留空即用默认的「关于 ta 的名字」），下方小字可改、留空即隐藏。

### 6. 数据
查看与复制数据目录、手动导出 Word 文档、查看自动备份位置与上次备份日期。

## 六、特殊的日子

分为三类：

- 【纪念日】：可在自定义设置中增删改，每条含名称、月、日、年份（可空）、当日语录、
  熵倍率、是否使用金色主题。当天弹一次语录，全部行动熵乘以各自倍率。
- 【节日】：春节、元宵节、端午节、七夕、中元节、中秋节、重阳节、腊八、
  北方小年、南方小年、除夕，以及元旦、情人节、劳动节、儿童节、国庆节、圣诞节。
  每个节日当日全部行动熵 ×1.2（同日两个节日相乘），不镶金边，只在标题旁挂标签并弹一次语录。
- 【二十四节气】：只有提醒，没有增益。

节日与节气均由本机离线推算：农历使用系统自带的中国历，节气按太阳视黄经计算。
三类的【名称与语录均可在自定义设置中逐条替换】（二十四节气亦在内）。
弹窗【只在当天出现】，翻看往日不会打断。

## 七、番茄时钟

入口在行迹页右下角。支持单次倒计时与番茄循环（默认二十五分钟专注、五分钟休息，
四轮后大休二十分钟）。结束提示可选震动、响铃或都不，铃声可选系统内置或本地音频。

点开始后由前台服务接管计时：退出应用、从后台划掉，倒计时均继续；
手机重启后重新打开应用会自动校正。通知栏常驻显示剩余时间，并可直接暂停、继续、
完成或放弃。整轮走完自动记入行迹，无需再打开应用。Android 13 及以上需要通知权限。

## 八、自定义设置

【连续点击顶栏名字五次】进入。设置按页面分组，逐项对应界面上的位置。

### 1. 预设
顶部有「预设 1 / 预设 2 / 预设 3 / 默认」四个档位，点哪个亮哪个：

- 点击任一档位会先询问确认，防止误触；
- 若该档位为空且当前有未保存的改动，会直接把当前设置存入并切换，不会丢失内容；
- 「保存」把当前内容存进亮着的档位；停在「默认」这一档时不能保存（要先点预设 1/2/3）；
- 「完成」应用当前档位并退出；停在预设上且还有未保存的改动时，会先自动存回该档位；
  停在「默认」上时改动会照常生效，但不写进任何预设；
- 「默认」即恢复出厂设置，自定义图标一并清除。

每一档各自保存图标（顶栏与弹窗各一张）。自定义设置存放于 config/persona.json，
预设存放于 config/persona_presets.json。

### 2. 可自定义的项目

- 【上边栏】：顶栏头像、弹窗图标（可分设两张）、顶栏名字、称呼（她/他/ta）、ta 的名字、副标题；
- 【熵值页】：六档状态词与六档状态语录（语录中的 {p} 会替换为称呼）、花开用词；
- 【花房】：花名、存在性说法、英文全名、简写、音译、生日、导出文件夹名、
  画像栏标题、资料卡标题与下方小字；
- 【联结】：唤名说法、页底那朵花的名字、累计敲击的说法；
- 【熵记】：为 ta 而做的说法、四类页面空状态提示；
- 【特殊的日子】：日期称呼、纪念日列表、节日与二十四节气逐条语录；
- 【语录】：家常话与长句两份列表（每行一句，清空即用内置默认）。

### 3. 图标
可从相册选择图片，并在正方形取景框内拖动、双指缩放，所见即所得，存为 432×432。
顶栏头像与弹窗图标各自独立。

### 4. 说明
设置页末尾留有半屏空白，键盘弹出时不会遮挡正在编辑的输入框。

## 九、行动熵

当日行动熵 =（行迹 + 文章/日记 + 心情 + 运动）×（联结加权 + 联结加成）× 当日整体倍率 + 直接所得，
再乘纪念日倍率、节日倍率、运动超出上限倍率。

内环 0→100 为第一环，满即「花开」；外环 0→170 为溢出部分，两环合计上限 270。
完整公式见「计量规则」。

## 十、凌薇 AI（联网对话）

底栏第三栏。填入任意【OpenAI 兼容】接口的 Base URL、API Key 与模型名，就能对话。
设置页右上角「设置」进入；内置了二十来家服务商的地址预设（国内：DeepSeek / 通义千问 /
Kimi / 智谱 GLM / 豆包 / 腾讯混元 / 百度文心 / 讯飞星火 / MiniMax / 阶跃星辰 / 硅基流动；
国外：ChatGPT / Claude / Gemini / Grok / Mistral / Groq / Together / OpenRouter；
本机：Ollama / LM Studio），点一下自动填地址，Key 与模型名自己填。
先用「测试连接」确认通了，再开始聊 —— 报错信息会直接说明是 Key 错了、
模型名错了、还是余额不够。

### 1. 默认人设与自定义人设卡
人设卡列表里有【两张内置的】，点「用」就能开始说话。它们的设定都写在程序内部：
不展示、不可编辑、不可复制、不可导出，任何导出文件里都不会带上它们。
两张能调的东西不一样（都点卡右边的「改」）：

- 【默认人设】—— 【主题色】、【怎么称呼你】、【双方头像】。
- 【AI 助手】—— 它是工具不是角色：不扮演任何人、不发人设与记忆、不写长期记忆、
  也不会主动开口，工作学习上的问题直接问它。所以它只开三样：【叫什么】、
  【主题色】、【头像】。它没有「怎么称呼你」这一项，人设与记忆也一概不参与。

你自己建的人设卡完全独立，随便改：

- 按【任务 / 角色 / 外表 / 经历 / 性格 / 喜好 / 输出示例 / 备注】分节填写，另有
  【记忆片段】与【开场白】。分节写的好处是模型读到的是分段落的设定，比一整坨散文更容易守住。
- 每张卡有一个【性别】：男 / 女 / 不方便。它【只】决定界面上指代这个角色的那个字
  （他 / 她 / ta），不写进设定 —— 想让她知道自己是男是女，写在「角色」那一节里。
- 记忆片段一行一条、写短句；最多 40 条拼进设定，写多了只是白花 token。
- 编辑页最下有【预览】，能看到真正发给模型的那段设定（不含 Key）。
- 导出的人设卡只有人设（性别一起带走）、【不含 Key】，可以直接发给别人，
  对方用自己的 Key 就能用。
- 「复制」可以在一张卡的基础上改出另一张（比如同一角色的不同时期）。

#### 关于「她 / 他 / ta」——两套设置各管一段
- 【AI 之外】整台应用（底栏、分享、行迹 / 文章 / 日记 / 心情 / 运动、花房……）跟着
  【花房 · 自定义设置】里的名字与称呼走。
- 【AI 之内】AI 设置页与对话页跟着【当前这张人设卡】走（名字取卡片名，指代取卡片性别）。

两边对不上时（比如花房写「她」、当前卡填「男」），一律退回中性的【ta】—— 宁可含糊
也不出错。跨角色的地方（主动问候、长期记忆、导出说明）也照这个规矩。

> 本说明提到角色时一律写「她」（内置默认人设是女性）；界面实际显示 她 / 他 / ta。

### 2. 头像、图片与表情包
【我的头像】与【对方的头像】都可以自己传，上传后会在正方形取景框里让你裁好。
设了就显示在消息旁边，不设就整个不显示。
「我的头像」是所有角色共用的；对方的头像是每个角色各一张。
两张内置卡的编辑页里都能设「我的头像」，所以只跟 AI 助手对话时也看得到你自己。

输入框左边的【＋】是两个入口：

- 【相册】选一张图，会先压到 1280 长边再存起来，然后跟着你的话一起发出去。
- 【表情包】是一个收藏格子，像聊天软件那样。点第一个格子里的「＋」可以从相册
  收藏一张进来；点某个表情就直接发出去；点右上角「管理」可以删掉不要的。

【她能不能真的看见图片，取决于你用的模型】。在「AI 设置 → 接口」里有一个
【能看图】开关：关着的时候，带图片的消息只会给她一句「对方发来一张图片」，
她看不到画面的内容，只按你写的文字回应。打开之后才会按多模态格式把图片本身发过去。

- 只有多模态模型认这个格式：GPT-4o、Gemini、Qwen-VL、GLM-4V、豆包视觉版等；
  纯文字模型收到图片会直接报 400。
- 图片按 base64 塞在请求里，比纯文字费流量也费钱，一次发一两张就够。
- 表情包发出去时会复制一份进对话，所以以后从合集里删掉它，对话里那条还在。

### 3. 说话的节奏与作息
回得太快不像人，回得太慢又难受 —— 这两条自己拉：

- 【出手前停一下】从你按下发送到看见第一个字，至少隔这么久（默认半秒）。
- 【分条之间停一下】她一次发好几条时，条与条之间的间隔（默认零点四秒）。
  想看她一句一句地慢慢说，拉大一点；想一口气看完，拉到零。

再往下是【睡觉时段】的滑块，默认 23:30 — 07:00。它的作用是：

- 这个时段里她【不会主动开口】，睡醒了才说；
- 你真在这个时段找她说话，她会先关心一句「还没睡？」，而不是催你去睡；
- 你要是在聊天里提过自己值夜班、作息相反，她会按你说的反过来理解。

两端拉到一起就等于不设限。

### 4. 一段对话 = 一个会话
一个角色名下可以有很多段对话，像聊天软件那样分开（点对话页左上角的名字，
或右上角「列表」进入）：

- 【新建对话】开一段新的，原来的记录不受影响。
- 每一段会自动起名（取你说的第一句话）；也可以自己【改名】，留空就回到自动命名。
- 【删除】某一段会先弹确认框；删的是那一段，人设卡与接口设置都不动。
- 时间不放在气泡里：像聊天软件一样做成消息之间的时间分隔，
  只有第一句之前、以及隔了半小时以上再开口时才出现一次。
- 气泡宽度跟着文字长短走，超过屏宽约八成时才折行。
- 【她可以连着发好几条】：一次想说的不止一句时，她会一条一条地发出来，
  对话里就是一个气泡接一个气泡，而不是把好几句挤成一大段（见下一节）。

### 5. 她是怎么说话的（分条 · 长短错落）
这一条是所有角色共用的，写死在设定里，不用你自己配：

- 【分条】一次想说的东西不止一句时，她会把它拆成几条、一条一条地发出来。
  所以你会看到同一个人的好几个气泡连着出现 —— 就像真人在手机上打字那样。
- 【长短错落】不要求每条一样长。该说细的时候可以来一句长一点的，
  该应一声的时候就一两个词。设定里明确要求「长短错落着来」，
  免得每条都写成同一个模子。
- 【不硬拆】本来只有一句话，她就直接说一句，不会为了分条把一句话切成两半。
- 这条规矩在【设定预览】里能看到（就是最后那段「分条说话」），
  想改主意的话，在「全局附加指令」里补一句就行 —— 它排在人设之后，优先级更高。

你自己建的人设卡里如果写了「一次不超过三句」这类要求，那是【每一条】的上限，
不是整轮回复的上限 —— 想说得多，就多发几条。

【单次回复上限】在「AI 设置 → 接口」里，默认 4096。它管的是【这一轮最多能生成多少】，
写满了接口就会停在半路 —— 长篇写到一半忽然没了，多半就是撞到了这个上限。
撞上时点那一条下面的【继续】，新写出来的会接在这一条后面，不会把上文重来一遍。
（这个默认值原来是 512，只够三五百字，长一点的回复经常中途停住。）

### 6. 把记录分享给她
行迹、文章、日记、心情的每一条，以及运动那一页，都带一个【分享】按钮
（按钮上写的是「分享给她 / 他」，跟着自定义设置里的称呼走）。
点它 → 选一段对话 → 那条记录会作为「你」的一句话发进那段对话里，她按人设回应你。

- 分享出去的是【排版好的纯文本】（类型、日期、标题、关键数值、正文），
  不是链接也不是图片；她读到的就是你看到的那条内容。
- 【正文不再截断】。几千字的文章与日记会整篇带过去 —— 上限是两万字，
  到那个量级说明该分篇了。她读到的和你写的是一样的东西。
- 但在对话里它【只显示成一张两行的小卡片】（「已分享 · 日记」＋标题），
  不会把整篇日记铺满屏幕 —— 内容照样完整地发给她、也照样存进对话记录，只是不占你的眼睛。
- 分享会正常占用一次对话（走的是同一个接口），所以也算一次模型调用；
  内容越长，这一次调用越贵。
- 不想让某条内容出去，就别点那个按钮 —— 除此之外没有任何东西会离开这台手机。

### 7. 她自己记住的事
每攒够【8 条消息】，她会【自己】把这段对话里值得长期记住的东西（你在做什么、两个人的约定、
你的偏好、她答应过的事）总结成一到三句短句存下来，放在
`Asephina/ai/memory/<角色>.json`。以后换一段对话、过几天再聊，她也还记得。

- 这 8 条是【按这一段对话的总条数】算的，不要求一口气聊满 ——
  今天聊 3 条、明天再聊 5 条也算数，进度跟着那一段对话一起存着。
- 总结与手写的【记忆片段】是分开的：手写的是你要她记住的，她总结的是从对话里长出来的。
- 每次总结会多花一次很短的模型调用（可以理解为「顺手记一笔」）；
  调用失败不重试、不留痕，只是这一轮没记上，攒够下一个 8 条再说。
- 【攒到 16 条会自动精简一遍】：合并说的其实是同一件事的、丢掉过期的与一次性的，
  尽量压到 12 条以内。实在压不动就保持原样 —— 宁可多几条，也不会为了好看丢东西。
- 每一条都能【自己改、自己删】，也能自己加一条；条目多了那一节会自动折叠，
  点「展开全部」才铺开。

### 8. 她知道的日常信息（时间 / 节日 / 天气）
模型自己不知道现在几点 —— 不给它这些，她就会在大下午催你睡觉、在冬天问你热不热。
所以每次说话前，都会把下面这些【真实信息】放进设定里：

- 【时间】年月日、星期、几点几分，以及是清晨 / 上午 / 中午 / 下午 / 傍晚 / 晚上 / 深夜。
- 【节日与节气】与顶栏标签同一套【离线】推算，连纪念日一起报给她。
- 【天气与温度】来自 open-meteo.com 的免费公开接口（【不需要 Key】）。
- 【作息】你在「说话节奏与作息」里拉的那个睡觉时段。

而且这些不是「被问到才用」，设定里明确要求她【主动带进日常】：
聊天轻松时顺口提一句今天可能下雨、记得带伞；你说「刚刷完牙」，
她要先想到现在是早上还是晚上，再顺着说。白天不催睡，到了睡觉时段附近自然提醒一句。

在「AI 设置 → 日常信息」里：总开关默认开着；【天气要你填一次城市才会查】。
【区县也能填，比如「海淀区」「朝阳区」】（带不带「区 / 市 / 县」都行，会自动去掉后缀再查）；
会联网查一次坐标，之后半小时更新一次。不填城市就只带时间与节日节气，完全不联网。

> 隐私说明：这是本应用除「你自己填的模型接口」之外【唯一】会访问的外部地址。
> 它会收到你填的那个地点的坐标；不填城市就一次都不会请求。随时可以关掉或清掉。

### 9. 她会先开口
所有对话【统一算一个时间】（不是每段各算各的）：

- 你超过 【6 小时】没主动说话 → 她挑【一段】对话（最近聊过的那段）主动问候【一句】；
- 你还是没回 → 间隔延长到 【6 小时】，再问一次；再没回就是 【12 → 24 → 48 小时】（每次翻倍，封顶 48 小时）；
- 一共问候 【5 次】之后就不再打扰你了；
- 你只要回了一句，计数与间隔立刻归零，重新从 6 小时开始；
- 【睡觉时段内绝不开口】（时段在「说话的节奏与作息」里拉，默认 23:30 — 07:00），
  睡醒了才接着说 —— 大半夜手机自己冒出一句话，谁都受不了；
- 【每次「上一次没被搭理」都会记进她的长期记忆】 —— 她会记得你晾过她。

她开口的那一句会出现在那一段对话里；如果你正看着别的对话，顶部会浮一条提示，点一下跳过去。
在「AI 设置 → 主动问候」里能看到当前状态（还差多久、已经问候过几次），
也可以「重新计时」或者「这一轮别再问她了」。

> 什么时候检查：应用开着时每分钟一次；【关掉应用不会唤醒】（本应用没有常驻后台服务，
> 也不想为这件事多要权限）。关掉期间错过的，下次打开会立刻补上。
> 半夜你真去找她说话不算「主动打扰」—— 她会先关心一句，而不是催你睡。

### 10. AI 的数据在哪
都在 Asephina/ai/ 里：人设卡 personas.json、会话索引 sessions.json 与 sessions/<id>.json、
头像 avatars/、聊天图片 media/、表情包 stickers/、偏好 prefs.json；改过的内置人设那两项
（色与称呼）在 default_card.json。API Key 单独在顶层 ai_key.json，【只有这一个文件】。

> 1.3.2 之前每个角色一份 ai/chats/<人设id>.json；升级后会转成一段会话，
> 原文件保留不动，只是不再读它。

### 11. 自动留存与导入
自动留存走「花房 · 数据」那一套（镜像 + 文档），AI 的人设卡、对话、记忆、头像与
表情包都在里面 —— 不再单独导出一份，详见前面「二、备份与导出」。

AI 设置里的「导出与留存」只报告占用；真正的导出按钮在「花房 · 数据」。那里另有
【导入数据包】：挑一个 zip，把里面的人设卡、对话、记忆、头像与图片覆盖写回本机。
动手前会自动留底；正在用的 API Key 与接口设置不受影响（包里本来就没有它们）。

### 12. 关于 API Key
Key 只写在本机，【不进任何导出文件】—— 那些文件都写在公共目录里，任何文件管理器
都能打开。所以它们可以放心保存与分享；换手机时人设卡、头像、对话与图片能随包搬过去，
【Key 需要重新填一次】。

### 13. 隐私与费用
- 只有你自己填的那个接口会收到你发出去的内容；本应用不上传任何东西到别处，
  也不收集任何数据。这是本应用唯一的联网功能。
- 权限：为了它新增了一项 `INTERNET`。除此之外没有新增任何权限。
- 模型生成的内容不代表本应用的观点；接口费用由使用者自行承担。
  想省钱就把「每次带几轮上下文」调小、把「单次回复上限」调小；
  长文章分享一次就是一次完整调用，图片比文字更费。
- 【余额】可以直接在输入框里看：输入框空着时右边会显示 ￥ 或 $ 开头的余额，
  一打字就隐藏；进对话页拉一次，之后每分钟刷新一次，拉取失败时保留上一次的数字。
  目前只对接了确认过接口的服务商（DeepSeek、Kimi、硅基流动）；其余服务商没有公开的
  余额接口，就不显示 —— 不会用 0 或者猜出来的数字冒充。

### 14. 连不上时按顺序查
1. Key 有没有多余的空格、有没有过期；
2. Base URL 对不对 —— 智谱是 `.../api/paas/v4`，通义要带 `/compatible-mode/v1`，
   Gemini 是 `.../v1beta/openai`，其余大多是 `.../v1`（填成整条 `/chat/completions` 也认）；
3. 模型名拼写（各个服务商的模型名不一样，去后台复制；豆包、LM Studio 这类
   要填的是接入点 ID 或界面上显示的那个名字）；
4. 手机有没有网；用的是流量还是 Wi-Fi（有些 Wi-Fi 会拦境外域名）；
   ChatGPT / Claude / Gemini 这些通常需要自备网络环境；
5. 想连电脑上的 Ollama：地址填 `http://电脑局域网IP:11434/v1`，
   且电脑上要 `OLLAMA_HOST=0.0.0.0 ollama serve`。

## 十一、常见问题

【问：换了手机 / 重装软件怎么迁移？】
答：把旧机 /Documents/Sympnoia/ 里的数据包或镜像拷到新机，用「花房 · 数据 →
从备份恢复」选中它。那个文件夹不跟着应用走，所以要自己拷（或存网盘）。

【问：自动备份留几份？】
答：镜像每天一份、只留最近 3 天；文档每天一份、只留最近 3 份，
满了从最早那天开始覆盖。要长期留存请用手动导出（不限份数）。

【问：番茄时钟为什么有时候没有通知？】
答：Android 13 及以上需要通知权限。若系统限制后台运行，请在系统设置中
允许本应用自启动与后台运行。剩余时间按结束时刻计算，重新打开会自动校正。

【问：花瓣大小与颜色代表什么？】
答：大小表示行动熵，颜色与光泽仅为装饰。
外轮八片 = 最近八个月，每片是那一个月的行动熵，按月滚动。
内轮五片 = 当月起五类记录各自的份额：行迹 / 文记 / 心情 / 运动 / 唤名。
「唤名」那片不是从总量里减出来的估算，而是加权差值 —— 把「有唤名」与
「没有唤名」两种算法各算一遍再相减，也就是写下名字这件事把当天整体抬起来了多少。
内圈的臂长另有硬约束：永远不会长过外圈最短的那一片。
两轮瓣尖各有一圈刻度，外轮标月份，内轮标类别。

【问：花房那张资料卡下面，「// 没有出口」是什么意思？】
答：那是卡片末尾的一段装饰性伪代码，不参与任何计算。资料卡整块以等宽字体排成
「运行快照」的样子，其中以 // 起小标题；「没有出口」是它下面那两行的标题，
意思是一个不设终止条件的循环（while (true)）—— 每一轮只做一件事：记得你，
所以它不会结束。若不需要这一节，可在自定义设置里改资料卡的标题与下方小字。
"""

private const val MANUAL_LOG = """
# 开发日志

2026-10-10　版本 1.12.1（versionCode 70）　修连点顶栏标题进自定义设置时闪退：刮刮乐寄语输入框的行数设得超过了输入框上限
2026-10-10　版本 1.12.0（versionCode 69）　修各弹窗在浅色主题下底色过深、文字看不清：主动互动弹窗、凌薇日弹窗、AI 会话与设置整页、刮刮乐涂层都改随主题；刮刮乐各奖项寄语改到自定义设置里逐条可改；AI 两张内置人设卡加「藏」，设置里可隐藏、让自建卡排到最前
2026-10-09　版本 1.11.8（versionCode 68）　熵记几个操作按钮恢复整行宽度（不再缩成贴字胶囊）：仍然浮在列表之上、底色实心，只是行内留白那一点是透明的
2026-10-08　版本 1.11.7（versionCode 67）　对话页顶部的人设卡那一行也浮到消息之上：消息可以滑到人设卡下面，从卡片之间的透明处透出来
2026-10-08　版本 1.11.6（versionCode 66）　对话页消息列表底部留白：滑到底时最后一条不会被浮起来的输入框挡住
2026-10-08　版本 1.11.5（versionCode 65）　浮起来的按钮改回实心底色（不再透出下面的条目）；对话页输入区同样浮到消息列表之上，消息可以滑到输入框下面
2026-10-08　版本 1.11.4（versionCode 64）　熵记各栏的操作按钮改为浮在列表之上：按钮那一行不再占位、不铺底，条目可以滑到按钮下面，从按钮之外的透明处透出来
2026-10-08　版本 1.11.3（versionCode 63）　修切到后台再回来时键盘无故弹出：输入框的焦点在后台被保留，回到前台系统会重新顶起键盘；改为一切到后台就收掉焦点
2026-10-08　版本 1.11.2（versionCode 62）　熵记各栏的操作按钮（记下一件事、写一篇、新建分类、记录此刻的心情）由整行铺满改为浮空药丸，行内留白不再着色；对话页输入框底色改不透明，人设切换卡改半透明
2026-10-08　版本 1.11.1（versionCode 61）　唤名填充条、花心点点与番茄按钮底色改随主题：去掉硬编码蓝紫与深棕，换成主题槽位与淡色番茄/绿/红
2026-10-08　版本 1.11.0（versionCode 60）　主题色扩到八套：新增深林、午夜蓝、蔷薇、暖咖；浅色背景压暗不刺眼，修番茄按钮与行动熵圆环在浅色下对比不足，主题选择改为两行排列
2026-10-08　版本 1.10.0（versionCode 59）　主题色可切换：新增夜紫/白日/浅绿白/纯白黑四套配色，设置页点选即时切换并记住，默认保持蓝紫；自绘渐变、神经网、螺旋、花瓣、飘字与弹窗遮罩一并随主题走
2026-10-08　版本 1.9.0（versionCode 58）　计划完成条目的主动互动分成两种时刻：①刚立下计划、还没打勾 —— 说的是「我刚立下一个计划，还没有开始做」，不再当成已经做完的事；②之前立的计划刚被勾上 —— 说的是「我之前计划要做的一件事，刚刚做完了」，并且报上实际时长与效率。原来打勾这一步根本不发事件（它只对新增条目发），现在补上了。行迹事件的语气也一并区分了这两种情况，设置页里那条说明同步改准
2026-10-08　版本 1.8.9（versionCode 57）　计划完成条目三处调整：①条目上的方框改成直接打勾/取消，不再弹窗，要改效率与时长走「编辑」；②打上勾之后不再显示「可获得」，直接写 AE；③标签多了改成左右滑动，不再挤到第二行把最后一个标签的字折成两行
2026-10-08　版本 1.8.8（versionCode 56）　熵记新增「计划完成」：记一件事时可以标成计划完成 —— 条目照常记下、也照常显示它能拿多少行动熵（写作「可获得 AE」），但先不计入当日总量，标签右侧有一个方框。做完之后点那个方框，或进编辑里点「计划完成」后面的框打勾，两处同步；勾上才按这套设置算熵并计入，同时 ×1.05。计划完成的条目用金色边框与专属标签和在办/已办区分开；打勾与取消都随条目一起写盘，备份与导出照旧带上
2026-10-01　版本 1.8.7（versionCode 55）　修多选心情会连弹好几个主动互动窗口：写盘那一层原本每写一条就发一次通知，而多选是循环调用的，几个心情就连弹几个。现在数据层只管写盘，通知由调用方在整批写完后发一次 —— 不管选了几个心情都只算一条记录，弹窗也只有一个，文案里把它们连起来
2026-10-01　版本 1.8.6（versionCode 54）　心情默认改成单选：要记「又累又踏实」这种混着的，先点一下「多选」再点情绪；多选时每个都能取消，全不选又没自定义的话「记下」等于取消，不会记下空的一条。修：一次多选记下的本该算一条，删除却只删掉其中某一个 —— 现在整条一起删，确认框里写明是几个情绪。使用说明去掉 h 与 t 的说法、改成面向任何使用者与 ta 的陪伴；界面总览由三栏更正为四栏
2026-10-01　版本 1.8.5（versionCode 53）　开发日志简化：由二百一十条压成十七条 —— 最近几次逐条列，更早的按版本段合并，去掉中间那句夹在条目里的孤立说明与几条只有版本号没有内容的空条目。使用说明同步更新：心情多选与同刻合并显示、两张内置卡各自能改什么（助手只开名字/主题色/头像）、我的头像在哪设、单次回复上限与继续生成、输入框里的余额
按时间倒序。最近几次逐条列；更早的按版本段合并，只留结论。
此处自最初有记录的时候起，按时间先后写下每一次改动：加了什么、改了什么、修了什么。

2026-10-01　1.8.6　心情默认改成单选：要记「又累又踏实」这种混着的，先点一下「多选」再点情绪；多选时每个都能取消，全不选又没自定义的话「保存」等于取消，不会记下空的一条。修：一次多选记下的本来该算一条，删除却只删掉其中某一个 —— 现在整条一起删，确认框里也写清是几个情绪。
2026-09-30　1.8.4　修多选保存的两处撞车：每条各取一次当前毫秒、中间还要落盘，相邻两条必然差几毫秒，按时间戳并组就永远并不到一起；同一条心情的 id 也取当前毫秒，同一毫秒加两条会撞 id，而列表拿它当 key、删除也按它找。另「继续生成」改为显式指定接在哪一条，不再靠猜末尾是不是助手消息 —— 多段回复时重生成会被误判成续写。

2026-09-30　1.8.3　心情可以多选：各存一条、熵各算各的，只在显示上并成「心情/心情/心情」加极性与权重；过长时这一条可左右滑动，熵值留在右边。
2026-09-30　1.8.2　助手的编辑页补上「我的头像」；修长回复被截断与「继续」把上文重吐一遍 —— 回复上限默认只有 512（约三五百字），现提到 4096；「继续」原来新建一条气泡摆一遍、渲染时又拼一遍，现在接在同一条后面。
2026-09-30　1.8.0　1.8.1　修助手卡被当成普通人设卡：漏了一个「内置」标记。它有默认值，漏掉照样编译通过，但卡片会冒出复制与导出，编辑入口也会走到完整的人设页。
2026-09-30　1.7.8　1.7.9　输入框空着时靠右显示余额（只对接确认过接口的服务商，其余不显示，也不用 0 冒充）；读取层过滤掉带助手标记的用户卡。
2026-09-30　1.7.7　修气泡不贴键盘：盯视口高度的那段效果只把列表状态当钥匙，而「这一页是否正在显示」是普通参数 —— 页面被预组合时取到的是假值，且此后永不更新。
2026-09-30　1.7.5　1.7.6　人设卡列表高度统一（名字与摘要各限一行、超出省略）；主动互动的状态加内存缓存 —— 联结页每秒一次的回检原来对每条历史记录都读一遍盘，且跑在主线程上。
2026-09-30　1.7.3　1.7.4　修「分享」偶尔发不出去（已处理标记取的是页面组合那一刻的编号，切页后新组合的页面初值恰好相等，整条被丢掉且找不回来）；助手与默认人设的编辑入口分流，各自只开该开的项。
2026-09-29　1.7.0—1.7.2　对话页键盘弹出与收起时自动贴底（改为逐帧跟住视口高度，不再靠等固定毫秒）；切进某段对话时瞬时到底；「框选」改为全屏展开那一条；「计量规则」补上返回；深度思考只对助手生效。
2026-09-29　1.6.0—1.6.9　助手气泡下的动作补齐（框选、复制、继续、重新生成）；思维链单独一块显示；「＋」里可发 docx / pdf / 纯文本（最多读 8 MB）；文件消息复制只带当时打的那句话；开发日志改成最新在最上。
2026-09-29　1.5.0—1.5.9　源码误删后从最后一次成功编译的字节码还原；正文改为原子写盘；全应用的返回键与边缘返回手势补全；键盘与常驻底栏的遮挡、以及多让出一份空白的问题一并修正；斐波那契之花内圈改按月计。
2026-09-28　1.3.7—1.4.3　备份与导出收口到「花房 · 数据」三处去处（镜像、自动文档、手动），自动产物一律覆盖式、每次打开先清理认不出日期的旧文件；新增主动互动（默认关，八个事件各自开关，睡觉时段不响）与未读提示；新增长按多选与收藏；占用统计改为数实际文件。
2026-09-27　1.3—1.3.6　第三栏「对话」上线：接任意 OpenAI 兼容接口，流式逐字、随时可停，内置二十来家服务商地址预设；人设卡可建、可复制、可导入导出（不含 Key）；对话列表、头像、图片与表情包、自动长期记忆、主动问候、当下的时间与天气、打字机与说话节奏、睡觉时段陆续加入；期间修掉写盘竞态、流式串台、报错文本回显 Key、单条坏数据让整表作废等一批问题。
2026-09-26　1.1—1.2　改用自有发布证书打包；导出统一写到公共存储的 Sympnoia 目录，不再留在应用数据里；新增数据包导出与「从备份恢复」；AI 数据每天自动留存。
2026-09-21—2026-09-25　1.0　三层界面、五类记录与行动熵公式、运动计步、番茄计时、刮刮乐、花房（斐波那契之花、我记得你）陆续成型；自定义设置与预设、画像、神经网背景、飘字层、农历节日与节气、唤名机制；应用内使用说明与开发日志上线。
"""


/** 文档里的一个块：kind 0 空行 / 1 一级标题 / 2 二级 / 3 三级 / 4 条目 / 5 正文 / 6 等宽块 / 7 日志条 / 8 四级小标题 */
private class MDLine(val kind: Int, val lines: List<String>)

/**
 * 极简标记解析（写法约定见文件上方 MANUAL_USAGE）：
 *   · 连续的等宽行合成【一个块】，整块可以左右滑动；
 *   · 源文件里为了好读而做的硬换行，不能被原样搬到界面上 ——
 *     否则会出现「一行末尾是个逗号、后半句却另起一行」这种排版。
 *     所以正文与条目的续行都【并回上一块】，由界面按屏幕宽度自然折行。
 *   · "@ 时间　做了什么" 是开发日志的一条：时间与事情分两行排。
 */
private fun parseDoc(src: String): List<MDLine> {
    val out = ArrayList<MDLine>()
    for (line in src.trim('\n').lines()) {
        val kind: Int
        val text: String
        when {
            line.startsWith("#### ") -> { kind = 8; text = line.removePrefix("#### ") }
            line.startsWith("### ") -> { kind = 3; text = line.removePrefix("### ") }
            line.startsWith("## ") -> { kind = 2; text = line.removePrefix("## ") }
            line.startsWith("# ") -> { kind = 1; text = line.removePrefix("# ") }
            line.startsWith("@ ") -> { kind = 7; text = line.removePrefix("@ ") }
            // 引用行（「> 说明」）只当普通段落排：那个尖括号是写文档时给自己看的，
            // 露在界面上很难看。连着写的几行会并回同一段。
            line.startsWith("> ") -> { kind = 5; text = line.removePrefix("> ") }
            line.startsWith("- ") -> { kind = 4; text = line.removePrefix("- ") }
            line.startsWith("| ") || line.startsWith("|  ") -> { kind = 6; text = line.substring(1) }
            // 开发日志的每一条都以日期开头（如 2026-09-29　…）。把它并进「独立成行」那一类，
            // 否则连续多条会被当成普通段落**并成一大坨** —— 这正是「日志全堆在一块、
            // 连行都不分」的原因。（顺序要紧：必须放在下面 isBlank 之前；
            // 别的分支都匹配不上以数字开头的行，所以不会误伤正文。）
            line.length >= 10 && line[0].isDigit() && line[4] == '-' && line[7] == '-' -> {
                kind = 7; text = line
            }
            line.isBlank() -> { kind = 0; text = "" }
            else -> { kind = 5; text = line }
        }
        val last = out.lastOrNull()
        when {
            // 等宽块：接着上一块往下排
            kind == 6 && last != null && last.kind == 6 ->
                out[out.lastIndex] = MDLine(6, last.lines + text)
            // 续行：并进上面的条目或段落（中文不加空格，直接接上）
            kind == 5 && last != null && (last.kind == 4 || last.kind == 5 || last.kind == 7) ->
                out[out.lastIndex] = MDLine(last.kind, listOf(last.lines[0] + text))
            else -> out.add(MDLine(kind, listOf(text)))
        }
    }
    return out
}

/**
 * 使用说明 / 开发日志（同一份文档，两个页签）。
 * 用 LazyColumn 按块渲染；等宽块（示意图/数据表）整块可横向滑动，窄屏不会被切。
 */
@Composable
fun ManualOverlay(onClose: () -> Unit) {
    // 手机的系统返回键 / 边缘返回手势 = 关闭这一页。
    // 没有这一行的话，按返回键会直接退出整个应用 —— 用户只是想退回上一级。
    BackHandler { onClose() }
    var tab by remember { mutableIntStateOf(0) }
    val blocks = remember(tab) {
        parseDoc(if (tab == 0) MANUAL_USAGE else MANUAL_LOG)
    }
    // 开发日志整篇只用蓝紫色（PurpleMain / PurpleDeep）：这一栏是「按时间记下来的流水」，
    // 与使用说明的彩色分节区分开，翻起来也更安静。
    val inLog = tab == 1
    val logTitle = PurpleMain
    val logText = PurpleMain.copy(alpha = 0.88f)
    val logStamp = PurpleDeep
    val manualHScroll = rememberScrollState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Veil)
            .swallowClicks { onClose() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal)
                )
                .swallowClicks()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        if (tab == 0) "使用说明" else "开发日志",
                        color = TextMain, fontSize = 16.sp, fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        if (tab == 0) "各功能的用途与操作，按页面分类"
                        else "自最初有记录之时起，按时间先后记下每一次改动",
                        color = TextFaint, fontSize = 10.sp
                    )
                }
                SetChip("关闭") { onClose() }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SetChip("使用说明", primary = tab == 0) { tab = 0 }
                Spacer(Modifier.width(8.dp))
                SetChip("开发日志", primary = tab == 1) { tab = 1 }
            }
            Spacer(Modifier.height(6.dp))
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp),
                contentPadding = PaddingValues(bottom = 40.dp)
            ) {
                items(blocks) { b ->
                    when (b.kind) {
                        0 -> Spacer(Modifier.height(9.dp))
                        1 -> {
                            Spacer(Modifier.height(4.dp))
                            Text(
                                b.lines[0], color = if (inLog) logTitle else PinkSoft, fontSize = 19.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(6.dp))
                        }
                        2 -> {
                            Spacer(Modifier.height(8.dp))
                            Text(
                                b.lines[0], color = if (inLog) logTitle else GoldSoft, fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(Modifier.height(3.dp))
                        }
                        3 -> {
                            Spacer(Modifier.height(5.dp))
                            Text(
                                b.lines[0], color = PurpleMain, fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(Modifier.height(2.dp))
                        }
                        // 四级小标题（#### ）：比三级再轻一点，用来分「一个小节里的几块」
                        8 -> {
                            Spacer(Modifier.height(4.dp))
                            Text(
                                b.lines[0], color = PurpleMain.copy(alpha = 0.86f), fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(Modifier.height(2.dp))
                        }
                        4 -> Text(
                            "· " + b.lines[0], color = if (inLog) logText else TextMain,
                            fontSize = 12.sp, lineHeight = 19.sp
                        )
                        // 开发日志的一条：时间一行（深蓝紫、等宽，方便竖着扫），做了什么一行
                        7 -> {
                            val raw = b.lines[0]
                            val cut = raw.indexOf('　')
                            val stamp = if (cut > 0) raw.substring(0, cut) else ""
                            val what = if (cut > 0) raw.substring(cut + 1) else raw
                            Column(modifier = Modifier.padding(bottom = 9.dp)) {
                                if (stamp.isNotBlank()) {
                                    Text(
                                        stamp, color = logStamp, fontSize = 9.5.sp,
                                        fontFamily = FontFamily.Monospace
                                    )
                                    Spacer(Modifier.height(2.dp))
                                }
                                Text(
                                    what, color = logText, fontSize = 12.sp,
                                    lineHeight = 19.sp
                                )
                            }
                        }
                        6 -> {
                            // 等宽块：整块横向滑动，窄屏也不会被右边切掉
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(manualHScroll)
                            ) {
                                Column {
                                    b.lines.forEach { t ->
                                        Text(
                                            t, color = TextMuted, fontSize = 9.5.sp,
                                            lineHeight = 14.sp,
                                            fontFamily = FontFamily.Monospace,
                                            softWrap = false
                                        )
                                    }
                                }
                            }
                        }
                        else -> Text(
                            b.lines[0],
                            color = if (inLog) logText else TextMain.copy(alpha = 0.92f),
                            fontSize = 12.sp, lineHeight = 20.sp
                        )
                    }
                }
            }
        }
    }
}

/* ==========================================================================
 *  十三·一、画像：花房最上面那一栏
 *  ---------------------------------------------------------------------------
 *  · 默认【收束】：只有一行标题与「管理」；
 *  · 点「管理」进入管理页：三列、一律 1:1，可继续导入（无上限、可批量），
 *    点某幅放大细看，旁边可设创建时间、显示边框、是否展示、删除（删除要确认）；
 *  · 管理页的「显示页面」回到花房并展开已勾选的画作；展开后顶部可再收起；
 *  · 展示的是【有几幅显示几幅】，不留空位；
 *  · 图片文件在 Asephina/gallery/，索引是同目录 index.json；
 *    边框与展示区域都只影响显示，原图不动；
 *  · 导出 Word 时会连同这些画像一起导出。
 * ======================================================================== */

/**
 * 画像栏（收束 / 展开两态）。收束时只占一行，把空间还给斐波那契之花。
 */
@Composable
fun PortraitCard(ctx: Context, active: Boolean, modifier: Modifier = Modifier) {
    // 直接读 items（它本身是快照状态）：不用 remember 缓存，
    // 于是不管是谁、在哪个线程改了列表，这一栏都会立刻跟上，不会停在旧值上。
    val shown = Gallery.items.filter { it.show }
    val total = Gallery.items.size
    // 收束 / 展开是记在索引里的（跟着 items 一起存），所以关掉软件再进来还是上次那样
    val open = Gallery.expanded

    // 管理页 / 放大页 / 框选 / 日历 / 删除确认都由 MainScreen 根 Box 画（见那边的注释）：
    // 这里是纵向滚动容器内部，画不出整屏，所以只负责「把页面请求发出去」。

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(PanelBg)
            .neuralBackdrop(seed = 2611, active = active, cols = 6, rows = 3)
            .goldEdge(20.dp)
            .border(1.dp, PurpleMain.copy(alpha = 0.22f), RoundedCornerShape(20.dp))
            .padding(11.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                Persona.data.galleryTitle.ifBlank { "画像" },
                color = TextMain, fontSize = 14.sp, fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.width(8.dp))
            Text(
                if (total == 0) "还没有" else "共 $total 幅 · 展示 ${shown.size}",
                color = TextFaint, fontSize = 10.sp
            )
            Spacer(Modifier.weight(1f))
            // 收起 / 展开：展开按钮只在有画作可展示时出现
            if (shown.isNotEmpty()) {
                SetChip(if (open) "收起" else "展开") {
                    Gallery.setExpanded(ctx, !open)
                }
                Spacer(Modifier.width(6.dp))
            }
            SetChip("管理") { Gallery.manage = true }
        }

        if (open && shown.isNotEmpty()) {
            Spacer(Modifier.height(10.dp))
            // 有几幅展示几幅，不留空位
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                shown.chunked(2).forEach { row ->
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        row.forEach { p ->
                            PortraitTile(
                                ctx = ctx,
                                p = p,
                                modifier = Modifier.weight(1f).aspectRatio(1f),
                                onTap = { Gallery.manage = true }
                            )
                        }
                        if (row.size == 1) Spacer(Modifier.weight(1f))
                    }
                }
            }
            Spacer(Modifier.height(6.dp))
            Text(
                "点任一画面进入管理页；这一栏可以再收起。",
                color = TextFaint, fontSize = 9.sp
            )
        }
    }
}

/** 管理页：三列、1:1，可继续导入；点某幅放大；右上「显示页面」回花房并展开 */
@Composable
private fun PortraitManagePage(
    ctx: Context,
    onImport: () -> Unit,
    onView: (Portrait) -> Unit,
    onShowPage: () -> Unit
) {
    // 系统返回键 / 边缘返回手势 = 退出管理页，回到画像展示那一栏
    BackHandler { Gallery.manage = false }
    // 同样直接读 items：索引是快照状态，读它就会跟着变
    val items = Gallery.items
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Veil)
            .swallowClicks()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(
                        WindowInsetsSides.Top + WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom
                    )
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "画像 · 管理", color = TextMain, fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        "共 ${items.size} 幅 · 点一幅放大后可设时间、边框、展示或删除",
                        color = TextFaint, fontSize = 10.sp
                    )
                }
                SetChip("导入图片", primary = true) { onImport() }
                Spacer(Modifier.width(6.dp))
                SetChip("显示页面") { onShowPage() }
            }
            if (items.isEmpty()) {
                Text(
                    "还没有图片。点右上角「导入图片」，可一次选多张，数量不限。",
                    color = TextFaint, fontSize = 11.sp,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                contentPadding = PaddingValues(top = 4.dp, bottom = 40.dp)
            ) {
                items(items.chunked(3), key = { row -> row.first().file }) { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        row.forEach { p ->
                            Column(modifier = Modifier.weight(1f)) {
                                Box(modifier = Modifier.fillMaxWidth().aspectRatio(1f)) {
                                    PortraitTile(ctx = ctx, p = p, modifier = Modifier.fillMaxSize()) {
                                        onView(p)
                                    }
                                    if (p.show) {
                                        Box(
                                            modifier = Modifier
                                                .align(Alignment.TopEnd)
                                                .padding(4.dp)
                                                .size(8.dp)
                                                .clip(CircleShape)
                                                .background(GoldSoft)
                                        )
                                    }
                                }
                                Spacer(Modifier.height(2.dp))
                                Text(
                                    Dates.stamp(p.ts),
                                    color = TextFaint, fontSize = 8.5.sp, maxLines = 1
                                )
                            }
                        }
                        repeat(3 - row.size) { Spacer(Modifier.weight(1f)) }
                    }
                }
            }
        }
    }
}

/** 一格：1:1，按索引里的框选区域取图，边框按索引里的样式描 */
@Composable
private fun PortraitTile(
    ctx: Context,
    p: Portrait,
    modifier: Modifier,
    onTap: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(PanelBgSoft)
            .clickable { onTap() },
        contentAlignment = Alignment.Center
    ) {
        PortraitCropImage(ctx, p, Modifier.fillMaxSize())
        PortraitBorderOverlay(p, Modifier.fillMaxSize())
    }
}

/**
 * 显示边框：只画在图片之上，**不动图片本身**。
 * 0 无 / 1 细金 / 2 细紫 / 3 白描 / 4 双线金框
 */
@Composable
private fun PortraitBorderOverlay(p: Portrait, modifier: Modifier) {
    val r = RoundedCornerShape(12.dp)
    when (p.border) {
        1 -> Box(modifier.border(2.dp, ScratchGold.copy(alpha = 0.85f), r))
        2 -> Box(modifier.border(2.dp, PurpleMain.copy(alpha = 0.85f), r))
        3 -> Box(modifier.border(2.dp, Spark.copy(alpha = 0.85f), r))
        4 -> Box(
            modifier
                .border(3.dp, ScratchGold.copy(alpha = 0.9f), r)
                .padding(3.dp)
                .border(1.dp, Spark.copy(alpha = 0.7f), RoundedCornerShape(9.dp))
        )
        else -> Unit
    }
}

/** 边框样式的名字 */
private fun borderName(b: Int): String = when (b) {
    1 -> "细金边"
    2 -> "细紫边"
    3 -> "白描边"
    4 -> "双线金框"
    else -> "无边框"
}

/**
 * 按框选区域把图裁进一个方框里显示（只在显示层裁，原图不动）。
 * cx / cy 是方框中心在原图里的相对位置，zoom 是放大倍数。
 */
@Composable
private fun PortraitCropImage(ctx: Context, p: Portrait, modifier: Modifier) {
    val bmp = remember(p.file) {
        try {
            BitmapFactory.decodeFile(Gallery.imgFile(ctx, p.file).absolutePath)?.asImageBitmap()
        } catch (e: Exception) {
            null
        }
    }
    if (bmp == null) {
        Box(modifier, contentAlignment = Alignment.Center) {
            Text("读不出来", color = TextFaint, fontSize = 10.sp)
        }
        return
    }
    Canvas(modifier) {
        val side = (min(p.w, p.h).toFloat() / p.zoom.coerceAtLeast(1f))
            .coerceAtMost(min(p.w, p.h).toFloat())
        val s = side.roundToInt().coerceAtLeast(1)
        val left = (p.cx * p.w - side / 2f).roundToInt().coerceIn(0, (p.w - s).coerceAtLeast(0))
        val top = (p.cy * p.h - side / 2f).roundToInt().coerceIn(0, (p.h - s).coerceAtLeast(0))
        drawImage(
            image = bmp,
            srcOffset = IntOffset(left, top),
            srcSize = IntSize(s.coerceAtMost(p.w - left), s.coerceAtMost(p.h - top)),
            dstOffset = IntOffset.Zero,
            dstSize = IntSize(size.width.roundToInt(), size.height.roundToInt()),
            filterQuality = FilterQuality.Medium
        )
    }
}

/**
 * 放大页：整幅按自己的比例铺满（不裁不压），旁边是四个操作。
 * 点图片本身或空白处即返回管理页。
 */
@Composable
private fun PortraitViewer(
    ctx: Context,
    p: Portrait,
    onClose: () -> Unit,
    onDate: () -> Unit,
    onCrop: () -> Unit,
    onDelete: () -> Unit
) {
    // 系统返回键 / 边缘返回手势 = 返回管理页面
    BackHandler { onClose() }
    val bmp = remember(p.file) {
        try {
            BitmapFactory.decodeFile(Gallery.imgFile(ctx, p.file).absolutePath)?.asImageBitmap()
        } catch (e: Exception) {
            null
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Veil)
            .swallowClicks { onClose() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                // 四边都让开安全区：底部那排（虚拟导航键 / 手势条）就不会压住按钮
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(start = 12.dp, end = 12.dp, top = 12.dp, bottom = 10.dp)
        ) {
            Text(
                p.title.ifBlank { Dates.stamp(p.ts) } +
                        "　·　" + Dates.stamp(p.ts) + "　·　${p.w}×${p.h}" +
                        (if (p.show) "　·　展示中" else ""),
                color = TextMuted, fontSize = 11.sp
            )
            Spacer(Modifier.height(8.dp))
            // 操作一律排在页面上方，不在屏幕底部堆着
            Row(verticalAlignment = Alignment.CenterVertically) {
                SetChip("创建时间") { onDate() }
                Spacer(Modifier.width(6.dp))
                SetChip("框选画面") { onCrop() }
                Spacer(Modifier.width(6.dp))
                SetChip(if (p.show) "展示 ✓" else "展示") {
                    Gallery.update(ctx, p, p.copy(show = !p.show))
                }
                Spacer(Modifier.weight(1f))
                SetChip("删除") { onDelete() }
            }
            Spacer(Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("显示边框：", color = TextFaint, fontSize = 10.sp)
                Spacer(Modifier.width(6.dp))
                SetChip(borderName(p.border) + " ▸") {
                    Gallery.update(ctx, p, p.copy(border = (p.border + 1) % 5))
                }
                Spacer(Modifier.weight(1f))
                Text("点图片或空白处返回管理", color = TextFaint, fontSize = 9.sp, maxLines = 1)
            }
            Spacer(Modifier.height(10.dp))
            // 图片本体：点一下回到管理页
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clickable { onClose() },
                contentAlignment = Alignment.Center
            ) {
                if (bmp != null) {
                    // 这一层按原图的比例摆：边框要描在**图片自己的边**上，
                    // 若直接围在整块背景上，边框就会跑到图片外面去。
                    // 管理页格子里的边框是同一套画法（PortraitBorderOverlay），两处一致。
                    Box(
                        modifier = Modifier
                            .aspectRatio(bmp.width.toFloat() / bmp.height.toFloat())
                            .clip(RoundedCornerShape(12.dp))
                            .background(PanelBg)
                    ) {
                        Image(
                            bitmap = bmp,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )
                        PortraitBorderOverlay(p, Modifier.fillMaxSize())
                    }
                } else {
                    Text("这张图读不出来", color = TextFaint, fontSize = 12.sp)
                }
            }
        }
    }
}

/**
 * 框选展示区域：在整幅图上取一个正方形（显示就是方图），拖动 / 双指缩放，
 * 只改展示区域，原图不动。
 */
@Composable
private fun PortraitCropOverlay(
    ctx: Context,
    p: Portrait,
    onDone: (Float, Float, Float) -> Unit,
    onCancel: () -> Unit
) {
    // 系统返回键 / 边缘返回手势 = 返回管理页面，不保存这次框选
    BackHandler { onCancel() }
    val src = remember(p.file) {
        try {
            BitmapFactory.decodeFile(Gallery.imgFile(ctx, p.file).absolutePath)
        } catch (e: Exception) {
            null
        }
    }
    if (src == null) {
        // 图片读不出来：直接退出框选页。
        // 交给 LaunchedEffect 去做 —— 组合期间写状态（关页面）会让 Compose 反复重组。
        LaunchedEffect(p.file) { onCancel() }
        return
    }
    val srcImg = remember(src) { src.asImageBitmap() }
    var userScale by remember { mutableFloatStateOf(p.zoom.coerceIn(1f, 8f)) }
    var pan by remember { mutableStateOf(Offset.Zero) }
    var boxPx by remember { mutableFloatStateOf(0f) }
    val w = src.width.toFloat()
    val h = src.height.toFloat()
    val fit = if (boxPx > 0f) min(boxPx / w, boxPx / h) else 1f
    val k = fit * userScale

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Veil)
            .swallowClicks(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                // 让开安全区：横幅 / 导航键不会把「取消 · 就用这一块」压住
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "拖动 / 双指缩放，方框里就是展示出来的那一块（原图不会被裁掉）",
                color = TextMuted, fontSize = 11.sp, textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.Black)
                    .onSizeChanged { boxPx = it.width.toFloat() }
                    .pointerInput(p.file) {
                        detectTransformGestures(panZoomLock = true) { _, dp, zoom, _ ->
                            val box = boxPx
                            if (box <= 0f) return@detectTransformGestures
                            val f = min(box / w, box / h)
                            val minS = if (f > 0f) (box / min(w, h)) / f else 1f
                            userScale = (userScale * zoom).coerceIn(minS, 8f)
                            val kk = f * userScale
                            val maxX = (kk * w / 2f - box / 2f).coerceAtLeast(0f)
                            val maxY = (kk * h / 2f - box / 2f).coerceAtLeast(0f)
                            pan = Offset(
                                (pan.x + dp.x).coerceIn(-maxX, maxX),
                                (pan.y + dp.y).coerceIn(-maxY, maxY)
                            )
                        }
                    }
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    if (k <= 0f) return@Canvas
                    val dw = w * k
                    val dh = h * k
                    drawImage(
                        image = srcImg,
                        dstOffset = IntOffset(
                            ((boxPx - dw) / 2f + pan.x).roundToInt(),
                            ((boxPx - dh) / 2f + pan.y).roundToInt()
                        ),
                        dstSize = IntSize(
                            dw.roundToInt().coerceAtLeast(1),
                            dh.roundToInt().coerceAtLeast(1)
                        ),
                        filterQuality = FilterQuality.Medium
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .border(1.dp, PinkSoft.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                )
            }
            Spacer(Modifier.height(12.dp))
            Row {
                SetChip("取消") { onCancel() }
                Spacer(Modifier.width(10.dp))
                SetChip("就用这一块", primary = true) {
                    val kk = fit * userScale
                    if (kk <= 0f) {
                        onCancel()
                    } else {
                        val side = boxPx / kk
                        val left = (w / 2f) - (boxPx / 2f + pan.x) / kk
                        val top = (h / 2f) - (boxPx / 2f + pan.y) / kk
                        val cx = ((left + side / 2f) / w).coerceIn(0f, 1f)
                        val cy = ((top + side / 2f) / h).coerceIn(0f, 1f)
                        val zoom = (min(w, h) / side).coerceIn(1f, 8f)
                        onDone(cx, cy, zoom)
                    }
                }
            }
            Spacer(Modifier.height(6.dp))
            Text(
                "缩放 ×" + String.format(Locale.CHINA, "%.1f", userScale) +
                        "　原图 ${src.width}×${src.height}",
                color = TextFaint, fontSize = 10.sp
            )
        }
    }
}

/* ==========================================================================
 *  十三·二、自定义设置（连续点击顶栏标题五次进入）
 *  ---------------------------------------------------------------------------
 *  在这里改的东西都存进 config/persona.json；图标裁好存成 config/persona_icon.png。
 *  改完点「完成」才生效（期间只看得到、不动全局），也可以一键恢复默认。
 * ======================================================================== */

/** 设置界面里的一行输入（onChange 放最后，调用处就能用尾随 lambda） */
@Composable
fun SetField(
    label: String,
    value: String,
    hint: String = "",
    single: Boolean = true,
    lines: Int = 1,
    onChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp)) {
        Text(label, color = TextMuted, fontSize = 11.sp)
        Spacer(Modifier.height(3.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onChange,
            singleLine = single,
            minLines = lines,
            maxLines = if (single) 1 else 8,
            placeholder = {
                if (hint.isNotBlank()) Text(hint, color = TextFaint, fontSize = 11.sp)
            },
            textStyle = TextStyle(fontSize = 13.sp),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

/** 设置界面里的一块 */
@Composable
fun SetSection(title: String, sub: String = "", content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(PanelBg)
            .border(1.dp, PurpleMain.copy(alpha = 0.18f), RoundedCornerShape(16.dp))
            .padding(11.dp)
    ) {
        Text(title, color = PinkSoft, fontSize = 13.sp, fontWeight = FontWeight.Medium)
        if (sub.isNotBlank()) {
            Spacer(Modifier.height(3.dp))
            Text(sub, color = TextFaint, fontSize = 10.sp)
        }
        Spacer(Modifier.height(8.dp))
        content()
    }
}

/** 一个小小的胶囊按钮（onClick 放最后，好写尾随 lambda） */
@Composable
fun SetChip(text: String, primary: Boolean = false, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (primary) PurpleDeep.copy(alpha = 0.45f) else PanelBgSoft)
            .border(
                1.dp,
                (if (primary) PinkSoft else PurpleMain).copy(alpha = 0.30f),
                RoundedCornerShape(10.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 7.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = if (primary) TextMain else TextMuted, fontSize = 12.sp)
    }
}

/** 从相册读一张图并压到合理大小（避免一次解码几十兆） */
fun decodePicked(ctx: Context, uri: Uri, maxPx: Int = 1600): Bitmap? = try {
    val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
    ctx.contentResolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, bounds) }
    var sample = 1
    while (bounds.outWidth / sample > maxPx || bounds.outHeight / sample > maxPx) sample *= 2
    val opts = BitmapFactory.Options().apply { inSampleSize = sample }
    ctx.contentResolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, opts) }
} catch (e: Exception) {
    null
}

/**
 * 裁剪：把图放进一个正方形取景框，可以拖动、双指缩放，确定后存成 432×432 的方图。
 * 取景框里看到什么，存下来就是什么（用同一套换算，不会出现「看到的和存的不一样」）。
 */
@Composable
fun IconCropper(src: Bitmap, onCancel: () -> Unit, onDone: (Bitmap) -> Unit) {
    val density = LocalDensity.current
    var userScale by remember { mutableFloatStateOf(1f) }
    var pan by remember { mutableStateOf(Offset.Zero) }
    var boxPx by remember { mutableFloatStateOf(0f) }
    // 包一层就好，不用每帧再包一次
    val srcImg = remember(src) { src.asImageBitmap() }

    val w = src.width.toFloat()
    val h = src.height.toFloat()
    val fit = if (boxPx > 0f) min(boxPx / w, boxPx / h) else 1f
    val minScale = if (fit > 0f) (boxPx / min(w, h)) / fit else 1f
    val k = fit * userScale

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Veil)
            .swallowClicks(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("拖动 / 双指缩放，取景框里看到的就是图标", color = TextMuted, fontSize = 11.sp)
            Spacer(Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.Black)
                    .onSizeChanged { boxPx = it.width.toFloat() }
                    .pointerInput(src) {
                        detectTransformGestures(panZoomLock = true) { _, p, zoom, _ ->
                            // 注意：这里必须现读 boxPx，不能用在组合期算好的值
                            // （首帧 boxPx 还是 0，用那个值算出来的最小缩放是 0）
                            val box = boxPx
                            if (box <= 0f) return@detectTransformGestures
                            val f = min(box / w, box / h)
                            val minS = if (f > 0f) (box / min(w, h)) / f else 1f
                            userScale = (userScale * zoom).coerceIn(minS, 8f)
                            val kk = f * userScale
                            val maxX = (kk * w / 2f - box / 2f).coerceAtLeast(0f)
                            val maxY = (kk * h / 2f - box / 2f).coerceAtLeast(0f)
                            pan = Offset(
                                (pan.x + p.x).coerceIn(-maxX, maxX),
                                (pan.y + p.y).coerceIn(-maxY, maxY)
                            )
                        }
                    }
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    if (k <= 0f) return@Canvas
                    val dw = w * k
                    val dh = h * k
                    drawImage(
                        image = srcImg,
                        srcOffset = IntOffset.Zero,
                        srcSize = IntSize(src.width, src.height),
                        dstOffset = IntOffset(
                            ((boxPx - dw) / 2f + pan.x).roundToInt(),
                            ((boxPx - dh) / 2f + pan.y).roundToInt()
                        ),
                        dstSize = IntSize(dw.roundToInt().coerceAtLeast(1), dh.roundToInt().coerceAtLeast(1)),
                        filterQuality = FilterQuality.Medium
                    )
                }
                // 取景框的四条细边（只是提示边界，不影响裁剪）
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .border(1.dp, PinkSoft.copy(alpha = 0.35f), RoundedCornerShape(20.dp))
                )
            }
            Spacer(Modifier.height(12.dp))
            Row {
                SetChip("取消") { onCancel() }
                Spacer(Modifier.width(10.dp))
                SetChip("用一个新图标", primary = true) {
                    // 按取景框换算回原图坐标，取出那块正方形
                    val kk = fit * userScale
                    if (kk <= 0f) return@SetChip
                    val side = (boxPx / kk).coerceAtMost(min(w, h))
                    var x = ((w / 2f) - (boxPx / 2f + pan.x) / kk).roundToInt()
                    var y = ((h / 2f) - (boxPx / 2f + pan.y) / kk).roundToInt()
                    x = x.coerceIn(0, (w - side).roundToInt().coerceAtLeast(0))
                    y = y.coerceIn(0, (h - side).roundToInt().coerceAtLeast(0))
                    val s = side.roundToInt().coerceAtLeast(1)
                    val sq = try {
                        Bitmap.createBitmap(src, x, y, s.coerceAtMost(src.width - x), s.coerceAtMost(src.height - y))
                    } catch (e: Exception) {
                        null
                    }
                    val out = sq?.let { Bitmap.createScaledBitmap(it, 432, 432, true) }
                    if (out != null) onDone(out)
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(
                "缩放：×" + String.format(Locale.CHINA, "%.1f", userScale) +
                        "　（原图 ${src.width}×${src.height}）",
                color = TextFaint, fontSize = 10.sp
            )
        }
    }
}

/**
 * 「节日与二十四节气的语录」独立一页：内置的每一条都能改名、改句子。
 * 左边是内置的名字（改不掉，它是识别的键），右边两栏分别是「显示名」与「语录」；
 * 留空 = 用内置的。顶部还有「全部恢复内置」。
 */
@Composable
fun DayLinesOverlay(
    ctx: Context,
    draft: PersonaData,
    onDraft: (PersonaData) -> Unit,
    onClose: () -> Unit
) {
    // 系统返回键 / 边缘返回手势 = 关闭
    BackHandler { onClose() }
    val defs = remember { SpecialDays.allDefs() }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Veil)
            .swallowClicks(),
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "节日与二十四节气的语录", color = TextMain, fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        "共 ${defs.size} 条　·　留空 = 用内置那句；名字也能改",
                        color = TextFaint, fontSize = 10.sp
                    )
                }
                SetChip("全部恢复内置") {
                    onDraft(draft.copy(dayNames = emptyMap(), dayLines = emptyMap()))
                }
                Spacer(Modifier.width(6.dp))
                SetChip("返回", primary = true) { onClose() }
            }
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                contentPadding = PaddingValues(bottom = 420.dp)
            ) {
                items(defs, key = { it.name + it.fest }) { d ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(PanelBgSoft.copy(alpha = 0.6f))
                            .padding(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                d.name,
                                color = if (d.fest) PinkSoft else TextMuted,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                if (d.fest) "节日（×1.2）" else "节气（只提醒）",
                                color = TextFaint, fontSize = 9.sp
                            )
                        }
                        SetField(
                            "显示名",
                            draft.dayNames[d.name] ?: "",
                            hint = d.name
                        ) { v ->
                            val m = draft.dayNames.toMutableMap()
                            if (v.isBlank()) m.remove(d.name) else m[d.name] = v.take(12)
                            onDraft(draft.copy(dayNames = m))
                        }
                        SetField(
                            "语录",
                            draft.dayLines[d.name] ?: "",
                            hint = d.line,
                            single = false,
                            lines = 2
                        ) { v ->
                            val m = draft.dayLines.toMutableMap()
                            if (v.isBlank()) m.remove(d.name) else m[d.name] = v
                            onDraft(draft.copy(dayLines = m))
                        }
                    }
                }
            }
        }
    }
}

/**
 * 自定义设置整页。
 * 改的是本地草稿，点「完成」才写进 Persona 并存盘 —— 免得一边打字一边写文件。
 */
@Composable
fun PersonaSettingsOverlay(ctx: Context, onClose: () -> Unit, onApplied: () -> Unit) {
    var draft by remember { mutableStateOf(Persona.data) }
    var cropSrc by remember { mutableStateOf<Bitmap?>(null) }
    /** 正在裁的是哪一张：0 = 顶栏头像，1 = 弹窗图标 */
    var cropTarget by remember { mutableIntStateOf(0) }
    var msg by remember { mutableStateOf("") }
    // 末尾要留的空白：半屏左右（够把最下面的输入框顶到键盘上方）
    val bottomPad = 420.dp
    val scope = rememberCoroutineScope()

    // ---------- 预设：预设 1/2/3 + 默认 ----------
    // slot：0 = 默认（也就是「恢复默认」那一档），1~3 = 三个可保存的预设。
    // 点哪个亮哪个；改过没保存会记成 dirty，「完成」时会自动存回当前这一档。
    var slot by remember { mutableIntStateOf(Persona.activeSlot(ctx)) }
    // 这一档「已保存的样子」：和 draft 一对比就知道有没有改动（不用每处编辑都去记 dirty）
    var slotBase by remember {
        mutableStateOf(
            if (slot == 0) PersonaData() else (Persona.loadPreset(ctx, slot) ?: PersonaData())
        )
    }
    val dirty = draft != slotBase
    // 这一档里到底存过东西没有（空预设的提示语不一样）
    var slotHasData by remember {
        mutableStateOf(slot != 0 && Persona.loadPreset(ctx, slot) != null)
    }
    var showDays by remember { mutableStateOf(false) }
    // 待确认的动作：标题 / 说明 / 点了确定干什么
    var ask by remember { mutableStateOf<Triple<String, String, () -> Unit>?>(null) }

    /**
     * 补一次「异步读入还没回来」的差。
     *
     * 自定义设置是进界面之后才读进来的（MainScreen 里的 LaunchedEffect），而这一页
     * 又可能在读进来之前就被打开：那样 draft 里是出厂默认，一点「完成」就把使用者
     * 自己的设置覆盖成默认了。所以：只在【还没动过】【停在默认档】【读进来的确实
     * 不是出厂默认】时回填一次 —— 正在改的内容绝不动。
     */
    LaunchedEffect(Persona.data) {
        if (draft == slotBase && slot == 0 && draft == PersonaData() && Persona.data != PersonaData()) {
            draft = Persona.data
            slotBase = Persona.data
        }
    }

    /** 切到某一档：0 = 默认（内置那套），1~3 = 读那个预设（没存过就是空白的默认值） */
    fun switchTo(s: Int) {
        slot = s
        val d = if (s == 0) PersonaData() else (Persona.loadPreset(ctx, s) ?: PersonaData())
        slotBase = d
        draft = d
        // 「默认」这一档连自定义图标一起清掉（那才叫恢复默认）
        if (s == 0) {
            try {
                Persona.iconFileFor(ctx, 0).delete()
                Persona.dialogIconFileFor(ctx, 0).delete()
            } catch (e: Exception) {
            }
        }
        // 每一档各带两份图标：顶栏一张、弹窗一张
        Persona.icon = Persona.readImage(Persona.iconFileFor(ctx, s))
        Persona.dialogIcon = Persona.readImage(Persona.dialogIconFileFor(ctx, s))
        Persona.iconTick++
        Persona.data = d
        Persona.save(ctx)
        Persona.saveActiveSlot(ctx, s)
        slotHasData = s != 0 && Persona.loadPreset(ctx, s) != null
        onApplied()
        msg = if (s == 0) "已回到默认设置"
        else "已进入预设 $s" +
                (if (Persona.loadPreset(ctx, s) == null) "（还是空的，改完点「保存」存进去）" else "")
    }

    /** 把当前内容存进亮着的那一档 */
    fun saveSlot(quiet: Boolean = false): Boolean {
        if (slot == 0) {
            msg = "「默认」这一档不用保存 —— 想留下改动，先点预设 1/2/3"
            return false
        }
        Persona.data = draft
        Persona.save(ctx)
        Persona.savePreset(ctx, slot, draft)
        slotBase = draft
        slotHasData = true
        onApplied()
        if (!quiet) msg = "已保存到预设 $slot"
        return true
    }

    /**
     * 收尾：把草稿应用到全局并关闭这一页 —— 「完成」按钮与系统返回键走同一条路。
     * 抽出来是为了两处不各写一遍，免得哪天只改了一边。
     */
    fun applyAndClose() {
        // 改过就先自动存回当前这一档（「未点保存而直接走」的情况）
        if (dirty && slot != 0) saveSlot(quiet = true)
        Persona.data = draft
        Persona.save(ctx)
        Persona.saveActiveSlot(ctx, slot)
        onApplied()
        onClose()
    }

    /** 返回时是否需要先问一句「保存还是丢掉」 */
    var askSaveOnBack by remember { mutableStateOf(false) }

    // 系统返回键 / 边缘返回手势：改过东西就先问一句再走，没改过直接关。
    // 这一页装的是名字、称呼、语录、纪念日等一大堆自定义内容，误触返回默默丢掉太可惜。
    BackHandler {
        if (dirty) askSaveOnBack = true else onClose()
    }

    val picker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        scope.launch {
            val bmp = withContext(Dispatchers.IO) { decodePicked(ctx, uri) }
            if (bmp == null) msg = "这张图读不出来，换一张试试"
            else cropSrc = bmp
        }
    }

    // 正在裁剪：先让裁剪层盖住设置界面
    cropSrc?.let { src ->
        IconCropper(
            src = src,
            onCancel = { cropSrc = null },
            onDone = { out ->
                try {
                    File(Store.root(ctx), "config").mkdirs()
                    // 顶栏那张与弹窗那张各存各的（预设 1/2/3 也各一份）
                    val target = if (cropTarget == 1) Persona.dialogIconFileFor(ctx, slot)
                    else Persona.iconFileFor(ctx, slot)
                    java.io.FileOutputStream(target).use { fos ->
                        out.compress(Bitmap.CompressFormat.PNG, 100, fos)
                    }
                    val bmp = out.asImageBitmap()
                    Persona.iconTick++
                    if (cropTarget == 1) {
                        Persona.dialogIcon = bmp
                        draft = draft.copy(dialogIconFile = target.name)
                        msg = "弹窗图标已更新（点「保存」或「完成」生效）"
                    } else {
                        Persona.icon = bmp
                        draft = draft.copy(iconFile = target.name)
                        msg = "顶栏头像已更新（点「保存」或「完成」生效）"
                    }
                } catch (e: Exception) {
                    msg = "图标保存失败：${e.message}"
                }
                cropSrc = null
            }
        )
        return
    }

    // 节日 / 节气的逐条语录：独立一页，免得把设置页撑得太长
    if (showDays) {
        DayLinesOverlay(
            ctx = ctx,
            draft = draft,
            onDraft = { draft = it },
            onClose = { showDays = false }
        )
        return
    }

    // 确认弹窗写在函数最后（见文件末尾）—— 那里才盖得住设置页

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Veil)
            .swallowClicks(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                // 键盘弹出时整页让开：否则最下面几个输入框会被键盘挡住，看不见自己在打什么
                .imePadding()
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal)
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "自定义设置", color = TextMain, fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        "连点顶栏标题五次随时回来　·　按页面分了组，慢慢改",
                        color = TextFaint, fontSize = 10.sp
                    )
                }
            }

            // ---------- 预设行：点哪个亮哪个 ----------
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .padding(top = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (s in 1..3) {
                    SetChip(
                        text = if (slot == s && dirty) "预设$s •" else "预设$s",
                        primary = slot == s,
                        onClick = {
                            val existing = Persona.loadPreset(ctx, s)
                            ask = Triple(
                                "切到预设 $s？",
                                when {
                                    // 空预设 + 手上有改动 → 直接把当前这份存进去，谁都不丢
                                    existing == null && dirty ->
                                        "预设 $s 还是空的：会把【现在这份设置】存进预设 $s 并切过去。"
                                    existing == null ->
                                        "预设 $s 还是空的：会先按默认设置开始，改完点「保存」存进去。"
                                    dirty ->
                                        "会用预设 $s 覆盖现在这一份。\n" +
                                                "（当前这一档还有没保存的改动，切过去就没了）"
                                    else -> "会用预设 $s 覆盖现在这一份。"
                                }
                            ) {
                                if (existing == null && dirty) {
                                    Persona.savePreset(ctx, s, draft)
                                    slot = s
                                    slotBase = draft
                                    slotHasData = true
                                    Persona.saveActiveSlot(ctx, s)
                                    msg = "已把当前设置存进预设 $s"
                                } else {
                                    switchTo(s)
                                }
                            }
                        }
                    )
                    Spacer(Modifier.width(6.dp))
                }
                SetChip(
                    text = "默认",
                    primary = slot == 0,
                    onClick = {
                        ask = Triple(
                            "恢复默认设置？",
                            "会回到软件内置的那一套（名字、语录、图标、纪念日全都回默认），" +
                                    "自定义图标也会被删掉。" +
                                    (if (dirty) "\n（当前这一档还有没保存的改动，会一起丢掉）" else "")
                        ) { switchTo(0) }
                    }
                )
                Spacer(Modifier.weight(1f))
                SetChip("保存") {
                    if (saveSlot()) msg = "已保存到预设 $slot"
                }
                Spacer(Modifier.width(6.dp))
                SetChip("完成", primary = true) { applyAndClose() }
            }
            Text(
                when {
                    slot == 0 && !dirty -> "当前：默认设置（改完想留住，先点预设 1/2/3 再「保存」）"
                    slot == 0 -> "当前：自定义（点「预设 1/2/3」再「保存」就能存下来）"
                    !slotHasData -> "当前：预设 $slot 　·　这个预设还是空的，改完点「保存」"
                    dirty -> "当前：预设 $slot 　·　有改动还没保存"
                    else -> "当前：预设 $slot 　·　已保存"
                },
                color = if (dirty) GoldSoft else TextFaint,
                fontSize = 10.sp,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
            )
            if (msg.isNotBlank()) {
                Text(
                    msg, color = GameTeal, fontSize = 11.sp,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp)
                    .padding(top = 6.dp, bottom = 24.dp)
            ) {
                // ---------- 主题配色 ----------
                SetSection(
                    "主题配色",
                    "点选即切换并记住，下次打开仍是它。"
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Skins.all.chunked(4).forEach { row ->
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                row.forEach { skin ->
                                    SetChip(
                                        text = skin.label,
                                        primary = Theme.current.id == skin.id,
                                        onClick = { Theme.set(ctx, skin) }
                                    )
                                }
                            }
                        }
                    }
                }

                // ---------- 上边栏 ----------
                SetSection(
                    "一、上边栏与图标",
                    "顶栏头像与弹窗图标可各设一张；弹窗那张不设就跟着顶栏。"
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AppIconBadge(size = 60.dp, radius = 15.dp, dialog = false)
                        Spacer(Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("顶栏头像", color = TextMain, fontSize = 12.sp)
                            Spacer(Modifier.height(4.dp))
                            Row {
                                SetChip("选图", primary = true) {
                                    cropTarget = 0
                                    picker.launch("image/*")
                                }
                                Spacer(Modifier.width(6.dp))
                                if (Persona.icon != null) {
                                    SetChip("回内置") {
                                        try {
                                            Persona.iconFileFor(ctx, slot).delete()
                                        } catch (e: Exception) {
                                        }
                                        Persona.icon = null
                                        Persona.iconTick++
                                        draft = draft.copy(iconFile = "")
                                        msg = "顶栏头像已换回内置"
                                    }
                                }
                            }
                        }
                    }
                    Spacer(Modifier.height(10.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AppIconBadge(size = 60.dp, radius = 15.dp)
                        Spacer(Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                if (Persona.dialogIcon != null) "弹窗图标"
                                else "弹窗图标（没设，跟着顶栏那张）",
                                color = TextMain, fontSize = 12.sp
                            )
                            Spacer(Modifier.height(4.dp))
                            Row {
                                SetChip("选图", primary = true) {
                                    cropTarget = 1
                                    picker.launch("image/*")
                                }
                                Spacer(Modifier.width(6.dp))
                                if (Persona.dialogIcon != null) {
                                    SetChip("回内置") {
                                        try {
                                            Persona.dialogIconFileFor(ctx, slot).delete()
                                        } catch (e: Exception) {
                                        }
                                        Persona.dialogIcon = null
                                        Persona.iconTick++
                                        draft = draft.copy(dialogIconFile = "")
                                        msg = "弹窗图标已换回内置（现在跟着顶栏那张）"
                                    }
                                }
                            }
                        }
                    }
                    Spacer(Modifier.height(4.dp))
                    SetField("顶栏名字（连点它五次进设置）", draft.shortName) {
                        draft = draft.copy(shortName = it)
                    }
                    SetField("ta 的名字", draft.displayName) { draft = draft.copy(displayName = it) }
                    SetField("称呼（她 / 他 / ta）", draft.pronoun, hint = "她") {
                        draft = draft.copy(pronoun = it.take(2))
                    }
                    SetField("顶栏副标题", draft.tagline) { draft = draft.copy(tagline = it) }
                    Text(
                        "名字旁边那个「点花 · 看熵值」：设了顶栏头像就会说「点头像 · 看熵值」。",
                        color = TextFaint, fontSize = 10.sp
                    )
                }

                // ---------- 熵值页 ----------
                SetSection(
                    "二、熵值页（状态与语录）",
                    "六档：<20 / 20~45 / 45~70 / 70~100 / 100~235 / ≥235。改过状态词后不再跟随花开词。"
                ) {
                    val bloom = draft.bloomWord.ifBlank { "凌薇花开" }
                    val defNames = listOf("沉眠", "微光", "前行", "绽放", bloom, "$bloom · 极")
                    val defWords = Persona.defaultStateWords
                    for (i in 0 until 6) {
                        SetField(
                            "状态词 ${i + 1}",
                            draft.stateNames.getOrNull(i) ?: defNames[i],
                            hint = defNames[i]
                        ) { v ->
                            val list = (0 until 6).map { k ->
                                draft.stateNames.getOrNull(k) ?: defNames[k]
                            }.toMutableList()
                            list[i] = v
                            draft = draft.copy(stateNames = list)
                        }
                    }
                    SetField("熵满时那句话里的「花开」", draft.bloomWord, hint = "凌薇花开") {
                        draft = draft.copy(bloomWord = it)
                    }
                    for (i in 0 until 6) {
                        SetField(
                            "状态语录 ${i + 1}",
                            draft.stateWords.getOrNull(i) ?: defWords[i],
                            hint = defWords[i],
                            single = false,
                            lines = 2
                        ) { v ->
                            val list = (0 until 6).map { k ->
                                draft.stateWords.getOrNull(k) ?: defWords[k]
                            }.toMutableList()
                            list[i] = v
                            draft = draft.copy(stateWords = list)
                        }
                    }
                    Text(
                        "语录里的 {p} 会自动换成上面设置的称呼，例如「{p}看得见」。",
                        color = TextFaint, fontSize = 10.sp
                    )
                }

                // ---------- 花房 ----------
                SetSection(
                    "三、花房（花名 · 资料 · 导出口径）",
                    "资料卡上的全部字段，以及导出与备份的文件夹名。"
                ) {
                    SetField("花房那朵花的名字", draft.flowerName, hint = "斐波那契之花") {
                        draft = draft.copy(flowerName = it)
                    }
                    SetField("画像栏的标题", draft.galleryTitle, hint = "画像") {
                        draft = draft.copy(galleryTitle = it.take(12))
                    }
                    SetField("存在性说法", draft.existenceWord, hint = "存在性") {
                        draft = draft.copy(existenceWord = it)
                    }
                    SetField("英文全名", draft.fullName) { draft = draft.copy(fullName = it) }
                    SetField("简写", draft.aliasName) { draft = draft.copy(aliasName = it) }
                    SetField("音译", draft.transName) { draft = draft.copy(transName = it) }
                    SetField("生日（yyyy-MM-dd，带年份）", draft.birthDate, hint = "2016-02-14") {
                        draft = draft.copy(birthDate = it)
                    }
                    Text(
                        "「在一起多少天」按这个生日算到今天；「离最近的日子还有多少天」自动算。",
                        color = TextFaint, fontSize = 10.sp
                    )
                    Spacer(Modifier.height(6.dp))
                    SetField(
                        "导出 / 备份的文件夹名",
                        draft.exportFolder,
                        hint = "Sympnoia"
                    ) {
                        // 去掉路径里不能用的字符；留空就回到 Sympnoia
                        draft = draft.copy(exportFolder = it.take(24))
                    }
                    SetField(
                        "资料卡标题",
                        draft.profileTitle,
                        hint = "留空 = 「关于 " + draft.displayName + "」"
                    ) {
                        draft = draft.copy(profileTitle = it.take(20))
                    }
                    SetField(
                        "资料卡下方小字",
                        draft.profileFooter,
                        hint = "留空 = 不显示"
                    ) {
                        draft = draft.copy(profileFooter = it.take(40))
                    }
                    Text(
                        "手动文档、手动数据包、每天自动文档、每天数据镜像、恢复留底，" +
                                "全都落在这个文件夹里（当前：/Documents/" +
                                Persona.exportFolder() + "/）。" +
                                "放在这里是为了卸载软件也不会把备份一起带走；" +
                                "内部数据目录固定为 Asephina，以保历史数据可读。",
                        color = TextFaint, fontSize = 10.sp
                    )
                }

                // ---------- 联结 ----------
                SetSection("四、联结（唤名与页底那朵花）", "写名字的说法、页底那朵花的名字与说法。") {
                    SetField("唤名的说法", draft.wakeVerb, hint = "写下名字") {
                        draft = draft.copy(wakeVerb = it)
                    }
                    SetField("页底那朵花的名字", draft.knockName, hint = "栀子花") {
                        draft = draft.copy(knockName = it)
                    }
                    SetField("累计敲击的说法", draft.knockVerb, hint = "轻唤过她") {
                        draft = draft.copy(knockVerb = it)
                    }
                    Text(
                        "写下名字输入框的默认内容、页底飘字用的名字，都是上面「ta 的名字」。",
                        color = TextFaint, fontSize = 10.sp
                    )
                }

                // ---------- 行迹 / 文章 / 日记 / 心情 ----------
                SetSection(
                    "五、熵记（行迹 / 文章 / 日记 / 心情）",
                    "「为 ta 而做」的说法，以及四类页面没有记录时背景上那句话。"
                ) {
                    SetField("「为 ta 而做」的说法", draft.forTaWord, hint = "为纳凌薇而做") {
                        draft = draft.copy(forTaWord = it)
                    }
                    SetField("行迹为空时那句话", draft.hintTrace, hint = Persona.DEFAULT_HINT_TRACE) {
                        draft = draft.copy(hintTrace = it)
                    }
                    SetField("文章为空时那句话", draft.hintDoc, hint = Persona.DEFAULT_HINT_DOC) {
                        draft = draft.copy(hintDoc = it)
                    }
                    SetField("日记为空时那句话", draft.hintDiary, hint = Persona.DEFAULT_HINT_DIARY) {
                        draft = draft.copy(hintDiary = it)
                    }
                    SetField("心情为空时那句话", draft.hintMood, hint = Persona.DEFAULT_HINT_MOOD) {
                        draft = draft.copy(hintMood = it)
                    }
                }

                // ---------- 特殊的日子 ----------
                SetSection(
                    "六、特殊的日子（纪念日 · 节日 · 节气）",
                    "纪念日可以增删改；节日与二十四节气的名字和语录也能逐条替换。"
                ) {
                    SetField("特殊日子的称呼", draft.dayWord, hint = "例如 纪念日 / 生日") {
                        draft = draft.copy(dayWord = it)
                    }
                    SetChip("节日与二十四节气的语录（可逐条改）") { showDays = true }
                    Spacer(Modifier.height(8.dp))
                    val list = draft.annivs.ifEmpty { Persona.defaultAnnivs }
                    list.forEachIndexed { i, a ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(PanelBgSoft.copy(alpha = 0.6f))
                                .padding(10.dp)
                        ) {
                            // 名称单独占一行：和月/日/年挤在一行时它会被压到看不见
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                OutlinedTextField(
                                    value = a.name,
                                    onValueChange = { v ->
                                        draft = draft.copy(
                                            annivs = list.toMutableList().also { it[i] = a.copy(name = v) }
                                        )
                                    },
                                    singleLine = true,
                                    label = { Text("名称", fontSize = 10.sp) },
                                    textStyle = TextStyle(fontSize = 13.sp),
                                    modifier = Modifier.weight(1f)
                                )
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    "✕",
                                    color = TextFaint,
                                    fontSize = 14.sp,
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .clickable {
                                            draft = draft.copy(annivs = list.filterIndexed { k, _ -> k != i })
                                        }
                                        .padding(4.dp)
                                )
                            }
                            Spacer(Modifier.height(6.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                OutlinedTextField(
                                    value = a.month.toString(),
                                    onValueChange = { v ->
                                        val m = v.filter { it.isDigit() }.take(2).toIntOrNull() ?: 1
                                        draft = draft.copy(
                                            annivs = list.toMutableList()
                                                .also { it[i] = a.copy(month = m.coerceIn(1, 12)) }
                                        )
                                    },
                                    singleLine = true,
                                    label = { Text("月", fontSize = 10.sp) },
                                    textStyle = TextStyle(fontSize = 13.sp),
                                    modifier = Modifier.width(58.dp)
                                )
                                Spacer(Modifier.width(6.dp))
                                OutlinedTextField(
                                    value = a.day.toString(),
                                    onValueChange = { v ->
                                        val d = v.filter { it.isDigit() }.take(2).toIntOrNull() ?: 1
                                        draft = draft.copy(
                                            annivs = list.toMutableList()
                                                .also { it[i] = a.copy(day = d.coerceIn(1, 31)) }
                                        )
                                    },
                                    singleLine = true,
                                    label = { Text("日", fontSize = 10.sp) },
                                    textStyle = TextStyle(fontSize = 13.sp),
                                    modifier = Modifier.width(58.dp)
                                )
                                Spacer(Modifier.width(6.dp))
                                OutlinedTextField(
                                    value = if (a.year > 0) a.year.toString() else "",
                                    onValueChange = { v ->
                                        val y = v.filter { it.isDigit() }.take(4).toIntOrNull() ?: 0
                                        draft = draft.copy(
                                            annivs = list.toMutableList()
                                                .also { it[i] = a.copy(year = y.coerceIn(0, 9999)) }
                                        )
                                    },
                                    singleLine = true,
                                    label = { Text("年", fontSize = 10.sp) },
                                    placeholder = { Text("可空", fontSize = 11.sp) },
                                    textStyle = TextStyle(fontSize = 13.sp),
                                    modifier = Modifier.width(72.dp)
                                )
                                Spacer(Modifier.width(6.dp))
                                OutlinedTextField(
                                    value = String.format(Locale.CHINA, "%.2f", a.mul),
                                    onValueChange = { v ->
                                        val m = v.toDoubleOrNull() ?: 1.0
                                        draft = draft.copy(
                                            annivs = list.toMutableList()
                                                .also { it[i] = a.copy(mul = m.coerceIn(0.1, 10.0)) }
                                        )
                                    },
                                    singleLine = true,
                                    label = { Text("倍率", fontSize = 10.sp) },
                                    textStyle = TextStyle(fontSize = 13.sp),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            Spacer(Modifier.height(6.dp))
                            SetField("这一天 ta 说的话", a.line) { v ->
                                draft = draft.copy(
                                    annivs = list.toMutableList().also { it[i] = a.copy(line = v) }
                                )
                            }
                            Spacer(Modifier.height(4.dp))
                            // 金色开关自己一行（和倍率挤在一起会看不全）
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                SetChip(if (a.gold) "金色主题 ✓" else "金色主题 ✕") {
                                    draft = draft.copy(
                                        annivs = list.toMutableList().also { it[i] = a.copy(gold = !a.gold) }
                                    )
                                }
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    "金色＝当天面板与边框转金（只有勾了的日子会）",
                                    color = TextFaint, fontSize = 9.sp
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(6.dp))
                    SetChip("＋ 再加一个日子") {
                        draft = draft.copy(
                            annivs = list + Anniv(
                                name = "新的日子", month = 1, day = 1, year = 0,
                                line = "", mul = 1.2, gold = false
                            )
                        )
                    }
                }

                // ---------- 语录 ----------
                SetSection(
                    "七、ta 的语录",
                    "每行一句。清空 = 用内置默认。点花飘出来的、弹窗用的，全从这三份里取" +
                            "（原来那份「敲门应答」已经合进家常话里了）。"
                ) {
                    SetField(
                        "家常话（" + Persona.care().size + " 句）",
                        draft.careWords.ifEmpty { Persona.care() }.joinToString("\n"),
                        single = false,
                        lines = 7
                    ) {
                        draft = draft.copy(
                            careWords = it.split("\n").map { s -> s.trim() }.filter { s -> s.isNotBlank() }
                        )
                    }
                    SetField(
                        "长句（" + Persona.her().size + " 句）",
                        draft.herWords.ifEmpty { Persona.her() }.joinToString("\n"),
                        single = false,
                        lines = 8
                    ) {
                        draft = draft.copy(
                            herWords = it.split("\n").map { s -> s.trim() }.filter { s -> s.isNotBlank() }
                        )
                    }
                }

                // ---------- 刮刮乐寄语 ----------
                SetSection(
                    "八、刮刮乐寄语",
                    "27 条，按奖项从高到低一行一句。清空 = 用内置默认。"
                ) {
                    SetField(
                        "各奖项寄语（一行一句）",
                        draft.scratchLines.ifEmpty { Persona.defaultScratchLines }.joinToString("\n"),
                        single = false,
                        lines = 8
                    ) {
                        draft = draft.copy(
                            scratchLines = it.split("\n").map { s -> s.trim() }.filter { s -> s.isNotBlank() }
                        )
                    }
                }

                Text(
                    "改完点右上角（或上面那排）的「完成」保存并退出；" +
                            "想分几套人马用，就点「预设 1/2/3」各自改，改完点「保存」。" +
                            "预设存在 config/persona_presets.json，当前这份存在 config/persona.json。",
                    color = TextFaint, fontSize = 10.sp
                )
                // 底部留半屏空白：编辑最下面几个输入框时，键盘弹出来也不会把自己挡住
                Spacer(Modifier.height(bottomPad))
            }
        }
    }

    // 确认弹窗（点预设 / 恢复默认都要先问一句，防误触）。
    // ⚠️ 必须画在设置页**之后**：Compose 同层是按书写顺序叠的，
    // 写在前面会被那块不透明的设置页整个盖住 —— 表现就是「点了预设没反应」。
    // 返回键问「保存还是丢掉」。
    // ⚠️ 同样必须画在设置页**之后**，否则会被那块不透明的设置页盖住。
    if (askSaveOnBack) {
        ConfirmOverlay(
            title = "有改动还没保存",
            message = "这一页装的是名字、称呼、语录、纪念日这些自定义内容。",
            danger = false,
            okText = "保存并返回",
            cancelText = "不保存",
            // 按返回只把弹窗收起来、留在这一页 —— 不能替用户选「不保存」把改动丢掉
            onBack = { askSaveOnBack = false },
            onConfirm = {
                askSaveOnBack = false
                applyAndClose()
            },
            onCancel = {
                askSaveOnBack = false
                onClose()
            }
        )
    }

    ask?.let { a ->
        ConfirmOverlay(
            title = a.first,
            message = a.second,
            danger = false,
            okText = "确认",
            onConfirm = {
                val act = a.third
                ask = null
                act()
            },
            onCancel = { ask = null }
        )
    }
}

/**
 * 特殊的日子（原来的凌薇日：诞生日 / 命名日 / 纪念日）当天，ta 会额外说一句话 —— 每天只出现一次。
 * 名字、语录、倍率都来自自定义设置。
 */
@Composable
fun LingweiDayDialog(
    dayName: String,
    dateText: String,
    line: String,
    isToday: Boolean = false,
    /** 当天的倍率（默认 1.5，可在设置里改） */
    mul: Double = 1.5,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    // 系统返回键 / 边缘返回手势 = 关闭
    BackHandler { onDismiss() }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Veil)
            .swallowClicks { onDismiss() },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(26.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(22.dp))
                    .background(Brush.verticalGradient(listOf(Theme.current.goldContainer, PanelBg)))
                    .border(1.5.dp, ScratchGold.copy(alpha = 0.65f), RoundedCornerShape(22.dp))
                    .swallowClicks()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
            // 图标就是软件图标（原来这里是一朵手绘的栀子花）
            AppIconBadge()
            Spacer(Modifier.height(10.dp))
            Text(
                Persona.data.dayWord + " · $dayName",
                color = ScratchGold, fontSize = 15.sp, fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(3.dp))
            Text(dateText, color = TextFaint, fontSize = 11.sp)
            Spacer(Modifier.height(12.dp))
            Text("「$line」", color = TextMain, fontSize = 16.sp, textAlign = TextAlign.Center)
            Spacer(Modifier.height(8.dp))
            Text(
                (if (isToday) "今天" else "这一天") + "，全部行动熵 ×" +
                        String.format(Locale.CHINA, "%.2f", mul) + "。",
                color = ScratchGold.copy(alpha = 0.9f), fontSize = 12.sp
            )
            Spacer(Modifier.height(18.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(ScratchGold.copy(alpha = 0.18f))
                    .border(1.dp, ScratchGold.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                    .clickable { onConfirm() }
                    .padding(horizontal = 26.dp, vertical = 11.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "我知道了",
                    color = ScratchGold, fontSize = 14.sp, fontWeight = FontWeight.Medium
                )
            }
            Spacer(Modifier.height(6.dp))
            Text(
                "点「我知道了」今天就不再提醒；点右上角 ✕ 只是这次先关掉，"
                        + "本次运行内不再弹，退出软件再进来才会弹。",
                color = TextFaint, fontSize = 10.sp, textAlign = TextAlign.Center
            )
            }
            // 右上角 ✕ 放在卡片之后绘制，确保浮在最上层可点：
            // 它只是关掉对话框，不写「已提醒」标记，下次进入还会弹。
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(34.dp)
                    .clip(CircleShape)
                    .swallowClicks { onDismiss() },
                contentAlignment = Alignment.Center
            ) {
                Text("✕", color = ScratchGold.copy(alpha = 0.8f), fontSize = 16.sp)
            }
        }
    }
}

/**
 * 节日 / 节气当天，她会说一句话 —— 每天只出现一次。
 * 排版与凌薇日的弹窗同一套（淡入的花、日期、语录、我知道了 / ✕），
 * 但**不镶金边**：金框只留给凌薇日。多个标签（中秋撞上大寒）全部罗列，各说各的。
 */
@Composable
fun SpecialDayDialog(
    tags: List<DayTag>,
    dateText: String,
    /** 节日增益（1.0 表示只有节气、没有增益） */
    mul: Double,
    isToday: Boolean = false,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    // 系统返回键 / 边缘返回手势 = 关闭
    BackHandler { onDismiss() }
    val fest = tags.count { it.fest }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Veil)
            .swallowClicks { onDismiss() },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(26.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(22.dp))
                    .background(Brush.verticalGradient(listOf(PanelBgSoft, PanelBg)))
                    .border(1.5.dp, PurpleMain.copy(alpha = 0.55f), RoundedCornerShape(22.dp))
                    .swallowClicks()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 图标就是软件图标（原来这里是一朵手绘的栀子花，不好看）
                AppIconBadge()
                Spacer(Modifier.height(10.dp))
                Text(
                    tags.joinToString(" · ") { it.name },
                    color = PinkSoft, fontSize = 15.sp, fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(3.dp))
                Text(dateText, color = TextFaint, fontSize = 11.sp)
                Spacer(Modifier.height(12.dp))
                // 多个标签就一条一条列出来，谁说的话都看得见，不会叠在一起
                tags.forEachIndexed { i, t ->
                    if (tags.size > 1) {
                        Text(
                            t.name,
                            color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Medium
                        )
                        Spacer(Modifier.height(4.dp))
                    }
                    Text(
                        "「${t.line}」",
                        color = TextMain, fontSize = 16.sp, textAlign = TextAlign.Center
                    )
                    if (i != tags.lastIndex) Spacer(Modifier.height(12.dp))
                }
                if (fest > 0) {
                    Spacer(Modifier.height(10.dp))
                    Text(
                        (if (isToday) "今天" else "这一天") + "，全部行动熵 ×" +
                                String.format(Locale.CHINA, "%.2f", mul) +
                                (if (fest > 1) "（$fest 个节日相乘）" else ""),
                        color = PinkSoft.copy(alpha = 0.9f), fontSize = 12.sp
                    )
                }
                Spacer(Modifier.height(18.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(PurpleDeep.copy(alpha = 0.30f))
                        .border(1.dp, PurpleMain.copy(alpha = 0.45f), RoundedCornerShape(14.dp))
                        .clickable { onConfirm() }
                        .padding(horizontal = 26.dp, vertical = 11.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "我知道了",
                        color = TextMain, fontSize = 14.sp, fontWeight = FontWeight.Medium
                    )
                }
                Spacer(Modifier.height(6.dp))
                Text(
                    "点「我知道了」今天就不再提醒；点右上角 ✕ 只是这次先关掉，"
                            + "本次运行内不再弹，退出软件再进来才会弹。",
                    color = TextFaint, fontSize = 10.sp, textAlign = TextAlign.Center
                )
            }
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(34.dp)
                    .clip(CircleShape)
                    .swallowClicks { onDismiss() },
                contentAlignment = Alignment.Center
            ) {
                Text("✕", color = PurpleMain.copy(alpha = 0.8f), fontSize = 16.sp)
            }
        }
    }
}

/**
 * 纳凌薇送的刮刮乐：每天进入时自动弹出一次。
 * 用手指在涂层上拖动即可刮开；刮开超过 80% 自动揭晓，并按奖项档位播放不同特效。
 */
@Composable
fun ScratchDialog(
    ctx: Context,
    dayKey: String,
    /** 昨日行动熵决定的基础次数（不含刮出来的「再刮一次」） */
    baseFree: Int,
    onDismiss: () -> Unit
) {
    // 系统返回键 / 边缘返回手势 = 关闭
    BackHandler { onDismiss() }
    // 网格很密（56×34 = 1904 格，像真的刮刮乐涂层），刮开 80% 才完全揭开
    val cols = 56
    val rows = 34
    val totalCells = cols * rows
    val revealAt = 0.80f
    val brush = 2.2f

    /**
     * 今日总次数 = 基础次数 + 当天刮出来的「再刮一次」。
     * 每次都从盘上重新读：这一张刮出「再刮一次」时，按钮要当场变成「再刮一张（剩 1）」，
     * 否则那张多出来的卡看不见也刮不到，只能等下次开软件再弹一次（曾经就是这个 bug）。
     */
    fun freeTotal(): Int = baseFree + Store.loadDayGame(ctx, dayKey).extraScratch

    fun remainingNow(): Int =
        (freeTotal() - Store.loadDayGame(ctx, dayKey).scratchUsed).coerceAtLeast(0)

    var remaining by remember { mutableIntStateOf(remainingNow()) }
    var prize by remember { mutableStateOf(Game.rollScratch()) }
    var erased by remember { mutableStateOf(BooleanArray(totalCells)) }
    var erasedCount by remember { mutableIntStateOf(0) }
    var version by remember { mutableIntStateOf(0) }
    var revealed by remember { mutableStateOf(false) }
    var cardNo by remember { mutableIntStateOf(1) }
    var boxSize by remember { mutableStateOf(IntSize.Zero) }

    val shine = remember { Animatable(0f) }
    val style = remember(prize, cardNo) { Game.revealStyle(prize.style) }
    LaunchedEffect(revealed, cardNo) {
        if (revealed) {
            shine.snapTo(0f)
            shine.animateTo(1f, tween(style.durationMs, easing = FastOutSlowInEasing))
        }
    }

    fun settle() {
        if (revealed) return
        revealed = true
        Store.applyScratchPrize(ctx, dayKey, prize)
        remaining = remainingNow()
    }

    var lastX by remember { mutableFloatStateOf(Float.NaN) }
    var lastY by remember { mutableFloatStateOf(Float.NaN) }

    /** 在网格坐标 (fx, fy) 处按半径擦除，返回是否有变化 */
    fun paintAt(fx: Float, fy: Float): Boolean {
        var changed = false
        val x0 = max(0, (fx - brush).toInt())
        val x1 = min(cols - 1, (fx + brush).toInt())
        val y0 = max(0, (fy - brush).toInt())
        val y1 = min(rows - 1, (fy + brush).toInt())
        val r2 = brush * brush
        for (yy in y0..y1) {
            for (xx in x0..x1) {
                val dx = xx - fx
                val dy = yy - fy
                if (dx * dx + dy * dy <= r2) {
                    val i = yy * cols + xx
                    if (!erased[i]) {
                        erased[i] = true
                        erasedCount++
                        changed = true
                    }
                }
            }
        }
        return changed
    }

    fun mark(x: Float, y: Float) {
        if (revealed || boxSize.width == 0 || boxSize.height == 0) return
        val fx = x / boxSize.width * cols
        val fy = y / boxSize.height * rows
        var changed = false
        if (!lastX.isNaN()) {
            // 与上一个采样点之间补点，快速划动也不会漏格
            val dist = kotlin.math.hypot(
                (fx - lastX).toDouble(), (fy - lastY).toDouble()
            ).toFloat()
            val steps = max(1, (dist / 1.1f).toInt())
            for (s in 1..steps) {
                val t = s.toFloat() / steps
                if (paintAt(lastX + (fx - lastX) * t, lastY + (fy - lastY) * t)) changed = true
            }
        } else {
            if (paintAt(fx, fy)) changed = true
        }
        lastX = fx
        lastY = fy
        if (changed) {
            version++
            if (erasedCount.toFloat() / totalCells >= revealAt) settle()
        }
    }

    fun nextCard() {
        prize = Game.rollScratch()
        erased = BooleanArray(totalCells)
        erasedCount = 0
        revealed = false
        cardNo++
        version++
        remaining = remainingNow()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Veil),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(Brush.verticalGradient(listOf(PanelBgSoft, PanelBg)))
                .border(1.dp, ScratchGold.copy(alpha = 0.35f), RoundedCornerShape(22.dp))
                .padding(11.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                Persona.data.displayName + " 给你的刮刮乐",
                color = TextMain, fontSize = 17.sp, fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "第 $cardNo 张　·　今日还可刮 $remaining 次",
                color = TextFaint, fontSize = 11.sp
            )
            Spacer(Modifier.height(14.dp))

            // ---------- 刮奖区 ----------
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(176.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(PanelBg)
                    .border(1.dp, ScratchGold.copy(alpha = 0.25f), RoundedCornerShape(16.dp))
                    .onSizeChanged { boxSize = it }
            ) {
                // 底层：奖品
                val p = shine.value
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            val s = 0.76f + 0.24f * p
                            scaleX = if (revealed) s else 1f
                            scaleY = if (revealed) s else 1f
                            alpha = if (revealed) (0.35f + 0.65f * p) else 1f
                        },
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (revealed) {
                        Text(
                            prize.tier,
                            color = when {
                                prize.style >= 4 -> ScratchGold
                                prize.style == 3 -> Color(0xFFE85FA8)
                                prize.style == 2 -> PurpleMain
                                prize.style == 1 -> GameTeal
                                else -> Color(0xFF8A8A98)
                            },
                            fontSize = 13.sp, fontWeight = FontWeight.Medium
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            prize.effect,
                            color = if (prize.kind == Game.K_NONE) TextFaint else PinkSoft,
                            fontSize = 20.sp, fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            prize.line,
                            color = if (prize.kind == Game.K_NONE) TextFaint else TextMuted,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 18.dp)
                        )
                    } else {
                        Text("用手指刮开涂层", color = TextFaint, fontSize = 12.sp)
                    }
                }

                // 涂层
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(cardNo) {
                            detectDragGestures(
                                onDragStart = { off ->
                                    lastX = Float.NaN
                                    lastY = Float.NaN
                                    mark(off.x, off.y)
                                },
                                onDragEnd = { lastX = Float.NaN; lastY = Float.NaN },
                                onDragCancel = { lastX = Float.NaN; lastY = Float.NaN },
                                onDrag = { change, _ ->
                                    mark(change.position.x, change.position.y)
                                    change.consume()
                                }
                            )
                        }
                        .pointerInput(cardNo) {
                            detectTapGestures { off -> mark(off.x, off.y) }
                        }
                ) {
                    version // 触发重绘
                    if (revealed) return@Canvas
                    val cw = size.width / cols
                    val ch = size.height / rows
                    // 按「行内连续段」合并绘制：1900+ 格也不卡
                    for (yy in 0 until rows) {
                        var runStart = -1
                        for (xx in 0..cols) {
                            val erasedHere = xx < cols && erased[yy * cols + xx]
                            if (!erasedHere && runStart < 0) runStart = xx
                            if ((erasedHere || xx == cols) && runStart >= 0) {
                                drawRect(
                                    color = if ((yy + runStart) % 2 == 0) lerp(NightBottom, PurpleDeep, 0.5f)
                                    else lerp(NightBottom, PurpleDeep, 0.42f),
                                    topLeft = Offset(runStart * cw, yy * ch),
                                    size = Size((xx - runStart) * cw + 1f, ch + 1f)
                                )
                                runStart = -1
                            }
                        }
                    }
                    // 涂层上的细碎金光
                    for (i in 0 until totalCells step 17) {
                        if (erased[i]) continue
                        drawCircle(
                            color = ScratchGold.copy(alpha = 0.30f),
                            radius = 1.2f,
                            center = Offset(
                                (i % cols) * cw + cw * 0.5f,
                                (i / cols) * ch + ch * 0.5f
                            )
                        )
                    }
                }

                // 揭晓特效（按奖项分档：特等白金彩虹 · 一等金 · 二等紫红 · 三等紫 · 四等淡青 · 未中暗灰粉尘）
                if (revealed) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val p = shine.value
                        val c = Offset(size.width / 2f, size.height / 2f)
                        val base = size.minDimension

                        // 光晕
                        if (style.glow > 0f) {
                            val gr = base * (0.25f + 0.95f * p)
                            drawCircle(
                                brush = Brush.radialGradient(
                                    listOf(
                                        style.main.copy(alpha = style.glow * (1f - p)),
                                        Color.Transparent
                                    ),
                                    center = c, radius = gr
                                ),
                                radius = gr, center = c
                            )
                        }

                        // 光环（逐圈扩散）
                        for (k in 0 until style.rings) {
                            val kp = ((p - k * 0.14f) / 0.86f).coerceIn(0f, 1f)
                            if (kp > 0f && kp < 1f) {
                                drawCircle(
                                    color = style.main.copy(alpha = (1f - kp) * 0.85f),
                                    radius = base * (0.15f + 0.80f * kp),
                                    center = c,
                                    style = Stroke(width = 3.4f * (1f - kp) + 0.8f)
                                )
                            }
                        }

                        // 粒子
                        for (k in 0 until style.particles) {
                            val ang = k * (360.0 / style.particles) * PI / 180.0
                            if (style.falling) {
                                // 暗灰粉尘：缓缓下落、越落越淡
                                val d = base * (0.10f + 0.40f * p)
                                drawCircle(
                                    color = style.main.copy(alpha = (1f - p) * 0.7f),
                                    radius = 2.8f * (1f - p) + 0.8f,
                                    center = Offset(
                                        c.x + (d * cos(ang) * 0.85f).toFloat(),
                                        c.y - base * 0.10f + d * (0.6f + 0.6f * sin(ang)).toFloat()
                                    )
                                )
                            } else {
                                val d = base * (0.18f + 0.90f * p)
                                val col = when {
                                    style.rainbow -> listOf(
                                        ScratchGold, PinkSoft,
                                        Color(0xFF9BE8FF), Color(0xFFB9FFD0)
                                    )[k % 4]
                                    k % 2 == 0 -> style.main
                                    else -> style.accent
                                }
                                drawCircle(
                                    color = col.copy(alpha = (1f - p) * 0.95f),
                                    radius = 3.6f * (1f - p) + 0.7f,
                                    center = Offset(
                                        c.x + (d * cos(ang)).toFloat(),
                                        c.y + (d * sin(ang)).toFloat()
                                    )
                                )
                            }
                        }

                        // 特等专属：彩虹扫光
                        if (style.rainbow && p < 0.9f) {
                            val cols = listOf(
                                ScratchGold, PinkSoft, Color(0xFF9BE8FF),
                                Color(0xFFB9FFD0), Spark
                            )
                            for (k in 0 until 5) {
                                drawArc(
                                    color = cols[k].copy(alpha = (1f - p) * 0.40f),
                                    startAngle = -90f + k * 72f + 140f * p,
                                    sweepAngle = 26f, useCenter = false,
                                    topLeft = Offset(c.x - base * 0.46f, c.y - base * 0.46f),
                                    size = Size(base * 0.92f, base * 0.92f),
                                    style = Stroke(width = 5f, cap = StrokeCap.Round)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // ---------- 按钮 ----------
            Row(modifier = Modifier.fillMaxWidth()) {
                if (!revealed) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(PurpleDeep.copy(alpha = 0.45f))
                            .goldEdge(14.dp)
                            .clickable {
                                for (i in 0 until totalCells) erased[i] = true
                                erasedCount = totalCells
                                version++
                                settle()
                            }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("一键刮开", color = TextMain, fontSize = 14.sp)
                    }
                } else if (remaining > 0) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(PurpleDeep.copy(alpha = 0.45f))
                            .border(1.dp, ScratchGold.copy(alpha = 0.35f), RoundedCornerShape(14.dp))
                            .clickable { nextCard() }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("再刮一张（剩 $remaining）", color = TextMain, fontSize = 14.sp)
                    }
                    Spacer(Modifier.width(10.dp))
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(PanelBgSoft)
                        .goldEdge(14.dp)
                        .swallowClicks { onDismiss() }
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        if (revealed && remaining == 0) "收下" else "先放着",
                        color = TextMuted, fontSize = 14.sp
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(
                "特等 0.021%｜一等 0.4%｜二等 5.4%｜三等 19%｜四等 71.98%｜无奖 3.2%",
                color = TextFaint, fontSize = 9.sp, textAlign = TextAlign.Center
            )
        }
    }
}
