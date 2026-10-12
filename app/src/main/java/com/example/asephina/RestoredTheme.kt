/*
 * ============================================================================
 *  二、主题色与通用小部件
 *  ---------------------------------------------------------------------------
 *  【这个文件是怎么来的】主题色、`goldEdge`、`swallowClicks`、`GoldenParticles`、
 *  `AsephinaColors`、`writeAtomicText` 与几个常量原本在 MainActivity.kt 的开头，
 *  源码被误删后从最后一次成功编译的字节码反编译还原。**原注释找不回来了**，
 *  下面的注释是事后补写的。
 *
 *  【为什么单独一个文件】还原时把这一批顶层声明放在这里，而不是塞回
 *  MainActivity.kt 的开头 —— 那样要在一个一万六千行的文件里做「整体位移」，
 *  风险太大（上一次事故正是这类操作造成的）。Kotlin 的顶层声明放在哪个文件
 *  都一样用，所以拆出来更安全。
 *
 *  【配色】整体是「夜紫 + 粉 + 金」：底色偏深夜紫，正文浅紫白，
 *  强调用粉，节日/开花之类的「贵重」时刻用金。
 * ========================================================================== */

package com.example.asephina

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.io.File
import kotlin.math.sin

/* 原 MainActivity.kt 开头一段（主题色 + 顶部小工具），随文件头一起丢失，此处按字节码恢复。
   这些声明原先与使用处同在一个文件里，AsephinaColors 与 writeAtomicText 当时是 private；
   现在另立一个文件，若仍写 private 便无法被 MainActivity.kt 引用，故放宽为公开，
   其余（颜色、goldEdge、swallowClicks、GoldenParticles、各 const）与原样一致。 */

val NightTop: Color get() = Theme.current.nightTop
val NightBottom: Color get() = Theme.current.nightBottom
val PanelBg: Color get() = Theme.current.panelBg
val PanelBgSoft: Color get() = Theme.current.panelBgSoft
val PurpleMain: Color get() = Theme.current.purpleMain
val PurpleDeep: Color get() = Theme.current.purpleDeep
val PinkSoft: Color get() = Theme.current.pinkSoft
val TextMain: Color get() = Theme.current.textMain
val TextMuted: Color get() = Theme.current.textMuted
val TextFaint: Color get() = Theme.current.textFaint
val WarmLight: Color get() = Theme.current.warmLight
val GoldSoft: Color get() = Theme.current.goldSoft
val GardeniaWhite: Color get() = Theme.current.gardeniaWhite
val CatTrace: Color get() = Theme.current.catTrace
val CatArticle: Color get() = Theme.current.catArticle
val CatDiary: Color get() = Theme.current.catDiary
val CatSteps: Color get() = Theme.current.catSteps
val GameTeal: Color get() = Theme.current.gameTeal
val ScratchGold: Color get() = Theme.current.scratchGold
val GoldDeep: Color get() = Theme.current.goldDeep

/* 自绘里的「最亮一笔」与弹窗遮罩底色：深色主题=白/近黑底，浅色主题=深墨/浅底 */
val Spark: Color get() = Theme.current.spark
val Veil: Color get() = Theme.current.veil

fun Modifier.goldEdge(radius: Dp = 16.dp): Modifier =
    if (GoldenDay.active) {
        border(1.dp, ScratchGold.copy(alpha = 0.45f), RoundedCornerShape(radius))
    } else {
        this
    }

fun Modifier.goldEdge(shape: Shape): Modifier =
    if (GoldenDay.active) {
        border(1.dp, ScratchGold.copy(alpha = 0.45f), shape)
    } else {
        this
    }

@Composable
fun Modifier.swallowClicks(onClick: () -> Unit = {}): Modifier = this.clickable(
    interactionSource = remember { MutableInteractionSource() },
    indication = null,
    onClick = onClick,
)

@Composable
fun GoldenParticles(modifier: Modifier = Modifier) {
    val t = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        while (true) {
            t.snapTo(0f)
            t.animateTo(1f, animationSpec = tween(5200, easing = LinearEasing))
        }
    }
    Canvas(modifier.fillMaxSize()) {
        val p = t.value
        for (k in 0 until 22) {
            val fx = k * 137.508 % 100.0 / 100.0
            val sp = 0.55f + (k * 53 % 100) / 100f * 0.9f
            val prog = (p * sp + k * 0.11f) % 1f
            val x = (fx * size.width).toFloat()
            val y = (1f - prog) * size.height
            val twinkle = (sin(p * 6.2832 + k * 1.7) * 0.35 + 0.55).toFloat()
            drawCircle(
                color = ScratchGold.copy(alpha = twinkle * 0.5f),
                radius = 1.4f + (k % 3) * 1.1f,
                center = Offset(x, y),
            )
        }
    }
}

val AsephinaColors: ColorScheme get() = Theme.current.scheme()

const val NAME_FILL_WINDOW_MS = 600_000L
const val ATTACH_MARK = '\uFFFC'
const val DOC_ARTICLE = "article"
const val DOC_DIARY = "diary"

/**
 * 原子写文本：先写同目录下的 `.tmp`，再改名覆盖目标。
 *
 * 为什么必须这样：直接 `File.writeText` 是先清空、再逐段写。
 * 进程一旦在写到一半时被杀（闪退、被系统回收、没电），
 * 留下的就是一个**截断的半个文件** —— JSON 解析失败，那条记录在界面上凭空消失。
 * 改名则是原子的：目标文件要么还是旧的完整版，要么已经是新的完整版。
 */
fun writeAtomicText(f: File, text: String) {
    try {
        f.parentFile?.mkdirs()
        val tmp = File(f.parentFile, f.name + ".tmp")
        tmp.writeText(text)
        // 先直接改名：POSIX 与 Android 的 rename(2) 会原子替换已存在的目标文件，
        // 全程不存在「目标文件不存在」的窗口。
        // 只有改名失败时（极少数文件系统）才退回「先删后改名」那条有窗口的路。
        if (!tmp.renameTo(f)) {
            if (f.exists() && !f.delete()) return
            if (!tmp.renameTo(f)) tmp.copyTo(f, overwrite = true)
        }
        tmp.delete()
    } catch (e: Exception) {
    }
}

/**
 * 原子写的扩展写法：`f.writeTextAtomic(s)` 读起来跟原来的 `f.writeText(s)` 一样，
 * 但不会留下半个文件。凡是「丢了就心疼」的数据都该走这条。
 */
fun File.writeTextAtomic(text: String) = writeAtomicText(this, text)
