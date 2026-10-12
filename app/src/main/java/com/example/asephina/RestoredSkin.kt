/*
 * ============================================================================
 *  主题皮肤（可切换配色）
 *  ---------------------------------------------------------------------------
 *  【为什么单独一个文件】主题色的 20 个名字原本是 RestoredTheme.kt 里的编译期
 *  常量，改成「从当前主题取值」后，这些取值本身 —— 八套配色、几组自绘渐变、
 *  金日子（GoldenDay）的暖色、可视化三色、弹窗遮罩底色 —— 全部集中到这里。
 *  换主题、加主题都只改这一个文件，其余文件一个字都不用动。
 *
 *  【怎么用】
 *    · 引用处一律沿用旧名字（NightTop / PurpleMain / TextMain ……），
 *      它们现在都是 `get()`，从 `Theme.current` 取值 —— 859 个引用点不动。
 *    · 切换主题：`Theme.set(ctx, Skins.Forest)`，会立即触发重组并落盘。
 *    · 根组合里用 `key(Theme.current.id) { ... }` 包一层：切换瞬间整棵树重建一次，
 *      换来的是所有自绘（Canvas / 渐变）也一定跟着换。
 *
 *  【落盘】config/theme.json，只有一行 `{"theme":"night"}`，启动时恢复。
 * ========================================================================== */

package com.example.asephina

import android.content.Context
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import java.io.File
import org.json.JSONObject

/**
 * 一套完整配色。字段名沿用现有语义色命名（物象气质，不退化成一堆 primary/secondary）：
 * 底色四层、文字三档、主色与点缀、暖光与金、四类记录的分类色、几处点缀，
 * 再补几个浅色主题才需要的槽位（自绘最亮一笔 `spark`、弹窗底色 `veil`、
 * Material3 第二套表面 `onPrimary/onSecondary`），以及几组自绘渐变。
 */
data class Skin(
    val id: String,
    val label: String,
    val dark: Boolean,
    // —— 底色四层：天空顶 / 夜空底 / 卡片 / 软卡片 ——
    val nightTop: Color,
    val nightBottom: Color,
    val panelBg: Color,
    val panelBgSoft: Color,
    // —— 文字三档 ——
    val textMain: Color,
    val textMuted: Color,
    val textFaint: Color,
    // —— 主色与点缀 ——
    val purpleMain: Color,
    val purpleDeep: Color,
    val pinkSoft: Color,
    val gardeniaWhite: Color,
    // —— 暖光与金 ——
    val warmLight: Color,
    val goldSoft: Color,
    val goldDeep: Color,
    // —— 四类记录的分类色 ——
    val catTrace: Color,
    val catArticle: Color,
    val catDiary: Color,
    val catSteps: Color,
    // —— 点缀 ——
    val gameTeal: Color,
    val scratchGold: Color,
    // —— Material3 第二套表面 ——
    val onPrimary: Color,
    val onSecondary: Color,
    // —— 自绘里「最亮的一笔」：深色主题=白，浅色主题=深墨 ——
    val spark: Color,
    // —— 弹窗 / 遮罩的底色（近不透明）——
    val veil: Color,
    // —— 可视化三色（熵螺旋 / 折线图）——
    val vizLine: Color,
    val vizDotLo: Color,
    val vizDotHi: Color,
    // —— 金日子（GoldenDay）的暖色 ——
    val goldContainer: Color,
    val goldDayTop: Color,
    val goldDayBottom: Color,
    // —— 自绘渐变（花瓣珠光、环境神经网、螺旋、花蕊、飘字）——
    val iris: List<Color>,
    val netColors: List<Color>,
    val spiralGradient: List<Color>,
    val pistilGradient: List<Color>,
    val quoteTints: List<Color>,
) {
    /** Material3 配色方案（跟着主题走，是第二套表面） */
    fun scheme(): ColorScheme = if (dark) {
        darkColorScheme(
            primary = purpleMain, onPrimary = onPrimary,
            secondary = pinkSoft, onSecondary = onSecondary,
            background = nightBottom, onBackground = textMain,
            surface = panelBg, onSurface = textMain,
        )
    } else {
        lightColorScheme(
            primary = purpleMain, onPrimary = onPrimary,
            secondary = pinkSoft, onSecondary = onSecondary,
            background = nightBottom, onBackground = textMain,
            surface = panelBg, onSurface = textMain,
        )
    }
}

/** 内置主题目录。默认沿用蓝紫（夜紫），与网站 :root 那套同源。 */
object Skins {
    val Night = Skin(
        id = "night", label = "夜紫", dark = true,
        nightTop = Color(0xFF141433), nightBottom = Color(0xFF0A0E22),
        panelBg = Color(0xFF1C1C3C), panelBgSoft = Color(0xFF23234A),
        textMain = Color(0xFFE7E3FA), textMuted = Color(0xFF8882B8), textFaint = Color(0xFF5A5580),
        purpleMain = Color(0xFFB9B3E6), purpleDeep = Color(0xFF7B74C4), pinkSoft = Color(0xFFFBC2EB),
        gardeniaWhite = Color(0xFFF6F3FF),
        warmLight = Color(0xFFF0D9A8), goldSoft = Color(0xFFF7D98C), goldDeep = Color(0xFFE0A93C),
        catTrace = Color(0xFF9C93D8), catArticle = Color(0xFF7FC7E8),
        catDiary = Color(0xFFF0A8C8), catSteps = Color(0xFF7FD9A6),
        gameTeal = Color(0xFF6FE0CF), scratchGold = Color(0xFFFFD86B),
        onPrimary = Color(0xFF16162E), onSecondary = Color(0xFF16162E),
        spark = Color(0xFFFFFFFF), veil = Color(0xF005060F),
        vizLine = Color(0xFF7A66D8), vizDotLo = Color(0xFF6C58C8), vizDotHi = Color(0xFFD9CBF9),
        goldContainer = Color(0xFF2A2312), goldDayTop = Color(0xFF201B10), goldDayBottom = Color(0xFF16120A),
        iris = listOf(
            Color(0xFFFCFAFF), Color(0xFFE4DBFB), Color(0xFFB9A8EE), Color(0xFFD9CDF8),
            Color(0xFFF7F2FF), Color(0xFFA695E6), Color(0xFFCFC0F4), Color(0xFFFBF8FF),
        ),
        netColors = listOf(
            Color(0xFF2A2A56), Color(0xFF34346C), Color(0xFF43408C),
            Color(0xFF544CA8), Color(0xFF6B5B9A), Color(0xFF8A7AB8),
        ),
        spiralGradient = listOf(
            Color(0xFF5A48B4), Color(0xFF7A66D8), Color(0xFF9C8AEC), Color(0xFFCDBEF8),
        ),
        pistilGradient = listOf(
            Color(0xFF09081A), Color(0xFF0E0C1E), Color(0xFF110F24), Color(0xFF14122A),
        ),
        quoteTints = listOf(
            Color(0xFFE8C4D8), Color(0xFFC7BCE8), Color(0xFFA9C6E0), Color(0xFFB9D8C6),
            Color(0xFFE6D3A8), Color(0xFFD9C3B0), Color(0xFFD8B6D2), Color(0xFFCFC9E6),
        ),
    )

    /** 白日：浅色蓝紫，与网站 html[data-theme="daylight"] 同源，互相呼应 */
    val Daylight = Skin(
        id = "daylight", label = "白日", dark = false,
        nightTop = Color(0xFFF1EEF7), nightBottom = Color(0xFFE8E2F0),
        panelBg = Color(0xFFFCFBFE), panelBgSoft = Color(0xFFEFEAF7),
        textMain = Color(0xFF241F38), textMuted = Color(0xFF554D75), textFaint = Color(0xFF8B84A6),
        purpleMain = Color(0xFF6F5FA8), purpleDeep = Color(0xFF4C3F7D), pinkSoft = Color(0xFFC9749F),
        gardeniaWhite = Color(0xFF8A7AB8),
        warmLight = Color(0xFFC9A24A), goldSoft = Color(0xFFB08D3E), goldDeep = Color(0xFF8A6A22),
        catTrace = Color(0xFF6F5FA8), catArticle = Color(0xFF3F8FA8),
        catDiary = Color(0xFFC2607E), catSteps = Color(0xFF3F9C6E),
        gameTeal = Color(0xFF3F9C8F), scratchGold = Color(0xFFC99700),
        onPrimary = Color(0xFFFFFFFF), onSecondary = Color(0xFFFFFFFF),
        spark = Color(0xFF241F38), veil = Color(0xF0F1EEF7),
        vizLine = Color(0xFF6F5FA8), vizDotLo = Color(0xFF4C3F7D), vizDotHi = Color(0xFF9C8FE4),
        goldContainer = Color(0xFFF3E9D2), goldDayTop = Color(0xFFF5EDD8), goldDayBottom = Color(0xFFEADFC0),
        iris = listOf(
            Color(0xFF8A7AB8), Color(0xFFB9A8EE), Color(0xFF6F5FA8), Color(0xFFA695E6),
            Color(0xFF9C8FE4), Color(0xFF5A48B4), Color(0xFFB9A8EE), Color(0xFF8A7AB8),
        ),
        netColors = listOf(
            Color(0xFFD8D2EE), Color(0xFFC7BEE4), Color(0xFFB9A8EE),
            Color(0xFFA695E6), Color(0xFF8A7AB8), Color(0xFF6F5FA8),
        ),
        spiralGradient = listOf(
            Color(0xFF8A7AB8), Color(0xFF6F5FA8), Color(0xFF4C3F7D), Color(0xFF9C8FE4),
        ),
        pistilGradient = listOf(
            Color(0xFFEDE8F7), Color(0xFFE2DAF2), Color(0xFFD8CEEC), Color(0xFFCBC0E4),
        ),
        quoteTints = listOf(
            Color(0xFFC2607E), Color(0xFF6F5FA8), Color(0xFF3F8FA8), Color(0xFF3F9C6E),
            Color(0xFFB08D3E), Color(0xFFA07050), Color(0xFFA85578), Color(0xFF6F5FA8),
        ),
    )

    /** 浅绿白：浅色的栀子花园绿，主色偏新绿，层次靠描边与淡影 */
    val Green = Skin(
        id = "green", label = "浅绿白", dark = false,
        nightTop = Color(0xFFEEF3EC), nightBottom = Color(0xFFE3EBDE),
        panelBg = Color(0xFFFCFDFC), panelBgSoft = Color(0xFFEDF3EA),
        textMain = Color(0xFF1E2B22), textMuted = Color(0xFF4C5F50), textFaint = Color(0xFF82917F),
        purpleMain = Color(0xFF4F8A5B), purpleDeep = Color(0xFF3A6B46), pinkSoft = Color(0xFFD98BA0),
        gardeniaWhite = Color(0xFF5E9A6E),
        warmLight = Color(0xFFB8944A), goldSoft = Color(0xFFA88A3C), goldDeep = Color(0xFF7A6428),
        catTrace = Color(0xFF6B8FA0), catArticle = Color(0xFF4A8FC0),
        catDiary = Color(0xFFC06A90), catSteps = Color(0xFF4F9A6E),
        gameTeal = Color(0xFF3F9C8F), scratchGold = Color(0xFFC99700),
        onPrimary = Color(0xFFFFFFFF), onSecondary = Color(0xFFFFFFFF),
        spark = Color(0xFF1E2B22), veil = Color(0xF0EEF3EC),
        vizLine = Color(0xFF4F8A5B), vizDotLo = Color(0xFF3A6B46), vizDotHi = Color(0xFF9CC9A8),
        goldContainer = Color(0xFFF0E8CF), goldDayTop = Color(0xFFF3EBD2), goldDayBottom = Color(0xFFE8DCB8),
        iris = listOf(
            Color(0xFF7FA98F), Color(0xFFB9D8C6), Color(0xFF4F8A5B), Color(0xFF9CC9A8),
            Color(0xFFA9C6A0), Color(0xFF3A6B46), Color(0xFFB9D8C6), Color(0xFF7FA98F),
        ),
        netColors = listOf(
            Color(0xFFD5E4D6), Color(0xFFC2D8C6), Color(0xFFA9C6A0),
            Color(0xFF8FB68F), Color(0xFF6FA87B), Color(0xFF4F8A5B),
        ),
        spiralGradient = listOf(
            Color(0xFF7FA98F), Color(0xFF4F8A5B), Color(0xFF3A6B46), Color(0xFF9CC9A8),
        ),
        pistilGradient = listOf(
            Color(0xFFEDF2EA), Color(0xFFE2ECDF), Color(0xFFD6E4D2), Color(0xFFC8D9C4),
        ),
        quoteTints = listOf(
            Color(0xFFC06A90), Color(0xFF4A8FC0), Color(0xFF3F8FA8), Color(0xFF4F9A6E),
            Color(0xFFA88A3C), Color(0xFFA07050), Color(0xFFC06A90), Color(0xFF4F8A5B),
        ),
    )

    /** 纯白黑：白底黑字 + 冷灰点缀 —— 高对比，不靠颜色辨层次，只靠明暗 */
    val Mono = Skin(
        id = "mono", label = "纯白黑", dark = false,
        nightTop = Color(0xFFF4F4F4), nightBottom = Color(0xFFE9E9E9),
        panelBg = Color(0xFFFDFDFD), panelBgSoft = Color(0xFFECECEC),
        textMain = Color(0xFF141414), textMuted = Color(0xFF555555), textFaint = Color(0xFF8A8A8A),
        purpleMain = Color(0xFF1A1A1A), purpleDeep = Color(0xFF3A3A3A), pinkSoft = Color(0xFF6E6E6E),
        gardeniaWhite = Color(0xFF3A3A3A),
        warmLight = Color(0xFF777777), goldSoft = Color(0xFF5A5A5A), goldDeep = Color(0xFF3A3A3A),
        catTrace = Color(0xFF333333), catArticle = Color(0xFF4D4D4D),
        catDiary = Color(0xFF6E6E6E), catSteps = Color(0xFF8A8A8A),
        gameTeal = Color(0xFF5A5A5A), scratchGold = Color(0xFF7A7A7A),
        onPrimary = Color(0xFFFFFFFF), onSecondary = Color(0xFFFFFFFF),
        spark = Color(0xFF141414), veil = Color(0xF0F4F4F4),
        vizLine = Color(0xFF3A3A3A), vizDotLo = Color(0xFF1A1A1A), vizDotHi = Color(0xFF7A7A7A),
        goldContainer = Color(0xFFECECEC), goldDayTop = Color(0xFFF1F1F1), goldDayBottom = Color(0xFFE2E2E2),
        iris = listOf(
            Color(0xFF4A4A4A), Color(0xFF9A9A9A), Color(0xFF1A1A1A), Color(0xFF6E6E6E),
            Color(0xFFB0B0B0), Color(0xFF2A2A2A), Color(0xFF8A8A8A), Color(0xFF4A4A4A),
        ),
        netColors = listOf(
            Color(0xFFDDDDDD), Color(0xFFCCCCCC), Color(0xFFBBBBBB),
            Color(0xFFAAAAAA), Color(0xFF999999), Color(0xFF888888),
        ),
        spiralGradient = listOf(
            Color(0xFF666666), Color(0xFF3A3A3A), Color(0xFF1A1A1A), Color(0xFF7A7A7A),
        ),
        pistilGradient = listOf(
            Color(0xFFEEEEEE), Color(0xFFE6E6E6), Color(0xFFDADADA), Color(0xFFCCCCCC),
        ),
        quoteTints = listOf(
            Color(0xFF444444), Color(0xFF555555), Color(0xFF666666), Color(0xFF777777),
            Color(0xFF888888), Color(0xFF999999), Color(0xFF666666), Color(0xFF555555),
        ),
    )

    /** 深林：深夜的森林绿，主色偏青绿，金照旧做「贵重」点缀 */
    val Forest = Skin(
        id = "forest", label = "深林", dark = true,
        nightTop = Color(0xFF101812), nightBottom = Color(0xFF0A0F0C),
        panelBg = Color(0xFF1A241D), panelBgSoft = Color(0xFF223027),
        textMain = Color(0xFFE2EBE3), textMuted = Color(0xFF8FA696), textFaint = Color(0xFF5C6E62),
        purpleMain = Color(0xFF8FCB9B), purpleDeep = Color(0xFF4F7A5B), pinkSoft = Color(0xFFE8B4C4),
        gardeniaWhite = Color(0xFFEFF6F0),
        warmLight = Color(0xFFE4CE9A), goldSoft = Color(0xFFD9BE7E), goldDeep = Color(0xFFC29A4A),
        catTrace = Color(0xFF9CC5A8), catArticle = Color(0xFF7FC7E8),
        catDiary = Color(0xFFE8A8C0), catSteps = Color(0xFF8FD9A6),
        gameTeal = Color(0xFF7FE0CF), scratchGold = Color(0xFFFFD86B),
        onPrimary = Color(0xFF0F1410), onSecondary = Color(0xFF0F1410),
        spark = Color(0xFFFFFFFF), veil = Color(0xF00A0F0C),
        vizLine = Color(0xFF6FB585), vizDotLo = Color(0xFF4F8A63), vizDotHi = Color(0xFFC5E3CE),
        goldContainer = Color(0xFF2A2312), goldDayTop = Color(0xFF201B10), goldDayBottom = Color(0xFF16120A),
        iris = listOf(
            Color(0xFFEFF8F0), Color(0xFFCBE4D2), Color(0xFF8FCB9B), Color(0xFFB9DCC4),
            Color(0xFFE2F0E4), Color(0xFF6FB585), Color(0xFFA9D6B5), Color(0xFFEFF8F0),
        ),
        netColors = listOf(
            Color(0xFF1E3325), Color(0xFF26402E), Color(0xFF2F5438),
            Color(0xFF3A6845), Color(0xFF4F7A5B), Color(0xFF6FB585),
        ),
        spiralGradient = listOf(
            Color(0xFF4F8A5B), Color(0xFF6FB585), Color(0xFF8FCB9B), Color(0xFFB9DCC4),
        ),
        pistilGradient = listOf(
            Color(0xFF0B120D), Color(0xFF101A12), Color(0xFF142018), Color(0xFF182A1E),
        ),
        quoteTints = listOf(
            Color(0xFFE0C8B0), Color(0xFFA9C6B0), Color(0xFF9CC5C8), Color(0xFFB9D8C6),
            Color(0xFFD8C39A), Color(0xFFC8B0A0), Color(0xFFC8A8C0), Color(0xFFA9C0C8),
        ),
    )

    /** 午夜蓝：深夜的深海蓝，主色偏雾蓝，沉稳不刺眼 */
    val Ocean = Skin(
        id = "ocean", label = "午夜蓝", dark = true,
        nightTop = Color(0xFF0E1620), nightBottom = Color(0xFF080E16),
        panelBg = Color(0xFF182230), panelBgSoft = Color(0xFF1F2C3D),
        textMain = Color(0xFFDEE7F0), textMuted = Color(0xFF8CA0B8), textFaint = Color(0xFF59697D),
        purpleMain = Color(0xFF8FB6E0), purpleDeep = Color(0xFF4F7AA8), pinkSoft = Color(0xFFE8B4C4),
        gardeniaWhite = Color(0xFFEEF3F8),
        warmLight = Color(0xFFE4CE9A), goldSoft = Color(0xFFD9BE7E), goldDeep = Color(0xFFC29A4A),
        catTrace = Color(0xFF9CB8D8), catArticle = Color(0xFF7FC7E8),
        catDiary = Color(0xFFE8A8C0), catSteps = Color(0xFF8FD9A6),
        gameTeal = Color(0xFF7FE0CF), scratchGold = Color(0xFFFFD86B),
        onPrimary = Color(0xFF0E141C), onSecondary = Color(0xFF0E141C),
        spark = Color(0xFFFFFFFF), veil = Color(0xF0080E16),
        vizLine = Color(0xFF6FA0C8), vizDotLo = Color(0xFF4F7AA0), vizDotHi = Color(0xFFC5D8EC),
        goldContainer = Color(0xFF2A2312), goldDayTop = Color(0xFF201B10), goldDayBottom = Color(0xFF16120A),
        iris = listOf(
            Color(0xFFEEF3F8), Color(0xFFC5D4E4), Color(0xFF8FB6E0), Color(0xFFB0C8E0),
            Color(0xFFDCE6F0), Color(0xFF6F9CC8), Color(0xFFA0C0DC), Color(0xFFEEF3F8),
        ),
        netColors = listOf(
            Color(0xFF1B2A3A), Color(0xFF22384A), Color(0xFF2A4A60),
            Color(0xFF345E78), Color(0xFF4F7AA8), Color(0xFF6FA0C8),
        ),
        spiralGradient = listOf(
            Color(0xFF4F7AA8), Color(0xFF6FA0C8), Color(0xFF8FB6E0), Color(0xFFB0C8E0),
        ),
        pistilGradient = listOf(
            Color(0xFF0A1018), Color(0xFF0F1620), Color(0xFF121C28), Color(0xFF162230),
        ),
        quoteTints = listOf(
            Color(0xFFD8C0C8), Color(0xFF9CB0C8), Color(0xFF8FB6D0), Color(0xFF9CC8B8),
            Color(0xFFD0C09A), Color(0xFFB8A8A0), Color(0xFFC0A8C8), Color(0xFF9CB0C0),
        ),
    )

    /** 蔷薇：浅色的玫瑰粉，主色偏蔷薇红，温柔不浓烈 */
    val Rose = Skin(
        id = "rose", label = "蔷薇", dark = false,
        nightTop = Color(0xFFFAF3F5), nightBottom = Color(0xFFF2E6EA),
        panelBg = Color(0xFFFEFCFD), panelBgSoft = Color(0xFFF7EBEF),
        textMain = Color(0xFF3A2430), textMuted = Color(0xFF7A5560), textFaint = Color(0xFFA98A93),
        purpleMain = Color(0xFFC2607E), purpleDeep = Color(0xFF8A4058), pinkSoft = Color(0xFFE08FB0),
        gardeniaWhite = Color(0xFFC2607E),
        warmLight = Color(0xFFC9A24A), goldSoft = Color(0xFFB08D3E), goldDeep = Color(0xFF8A6A22),
        catTrace = Color(0xFFA87890), catArticle = Color(0xFF6A9CBB),
        catDiary = Color(0xFFC2607E), catSteps = Color(0xFF6BA87E),
        gameTeal = Color(0xFF3F9C8F), scratchGold = Color(0xFFC99700),
        onPrimary = Color(0xFFFFFFFF), onSecondary = Color(0xFFFFFFFF),
        spark = Color(0xFF3A2430), veil = Color(0xF0FAF3F5),
        vizLine = Color(0xFFC2607E), vizDotLo = Color(0xFF8A4058), vizDotHi = Color(0xFFE8B0C0),
        goldContainer = Color(0xFFF3E9D2), goldDayTop = Color(0xFFF5EDD8), goldDayBottom = Color(0xFFEADFC0),
        iris = listOf(
            Color(0xFFE8B0C0), Color(0xFFF0D0DC), Color(0xFFC2607E), Color(0xFFE0A0B8),
            Color(0xFFF5DEE6), Color(0xFF9A4A60), Color(0xFFE8C0D0), Color(0xFFE8B0C0),
        ),
        netColors = listOf(
            Color(0xFFEEDCE2), Color(0xFFE4CBD4), Color(0xFFD8B0BE),
            Color(0xFFCC9AAC), Color(0xFFC2607E), Color(0xFF9A4A60),
        ),
        spiralGradient = listOf(
            Color(0xFFC2607E), Color(0xFFE08FB0), Color(0xFF8A4058), Color(0xFFE8B0C0),
        ),
        pistilGradient = listOf(
            Color(0xFFF5E8EC), Color(0xFFEFDCE2), Color(0xFFE6CDD6), Color(0xFFDCC0CA),
        ),
        quoteTints = listOf(
            Color(0xFFC2607E), Color(0xFF6A9CBB), Color(0xFF3F8FA8), Color(0xFF6BA87E),
            Color(0xFFB08D3E), Color(0xFFA87890), Color(0xFFC2607E), Color(0xFF6A9CBB),
        ),
    )

    /** 暖咖：深夜的暖咖啡，主色偏琥珀金，暖而不燥 */
    val Coffee = Skin(
        id = "coffee", label = "暖咖", dark = true,
        nightTop = Color(0xFF1E1610), nightBottom = Color(0xFF120D08),
        panelBg = Color(0xFF2A2016), panelBgSoft = Color(0xFF332A1E),
        textMain = Color(0xFFEDE4D6), textMuted = Color(0xFFA89984), textFaint = Color(0xFF6E6150),
        purpleMain = Color(0xFFD9A86A), purpleDeep = Color(0xFF9A7440), pinkSoft = Color(0xFFE8B4C4),
        gardeniaWhite = Color(0xFFF3EADF),
        warmLight = Color(0xFFE8CE9A), goldSoft = Color(0xFFDDBE7E), goldDeep = Color(0xFFC29A4A),
        catTrace = Color(0xFFC8A878), catArticle = Color(0xFF7FB0C8),
        catDiary = Color(0xFFE8A0B8), catSteps = Color(0xFF8FD0A0),
        gameTeal = Color(0xFF7FD8C8), scratchGold = Color(0xFFFFD86B),
        onPrimary = Color(0xFF16100A), onSecondary = Color(0xFF16100A),
        spark = Color(0xFFFFFFFF), veil = Color(0xF0120D08),
        vizLine = Color(0xFFC8A070), vizDotLo = Color(0xFF9A7440), vizDotHi = Color(0xFFE8D8C0),
        goldContainer = Color(0xFF2A2312), goldDayTop = Color(0xFF201B10), goldDayBottom = Color(0xFF16120A),
        iris = listOf(
            Color(0xFFF3EADF), Color(0xFFE8D8C0), Color(0xFFD9A86A), Color(0xFFE0C8A0),
            Color(0xFFF0E0D0), Color(0xFFB88650), Color(0xFFE8D0B0), Color(0xFFF3EADF),
        ),
        netColors = listOf(
            Color(0xFF2E2318), Color(0xFF3A2E1E), Color(0xFF4A3A26),
            Color(0xFF5E4A30), Color(0xFF9A7440), Color(0xFFC8A070),
        ),
        spiralGradient = listOf(
            Color(0xFF9A7440), Color(0xFFC8A070), Color(0xFFD9A86A), Color(0xFFE8D8C0),
        ),
        pistilGradient = listOf(
            Color(0xFF140F0A), Color(0xFF1A140C), Color(0xFF201810), Color(0xFF281E14),
        ),
        quoteTints = listOf(
            Color(0xFFE0C0A8), Color(0xFFC8B890), Color(0xFFA8C0C8), Color(0xFFB8D8C0),
            Color(0xFFD8C08A), Color(0xFFC0A088), Color(0xFFD0A0B8), Color(0xFFB0C0A8),
        ),
    )

    val all: List<Skin> = listOf(Night, Daylight, Green, Mono, Forest, Ocean, Rose, Coffee)

    fun byId(id: String): Skin = all.firstOrNull { it.id == id } ?: Night
}

/** 当前主题：用 `mutableStateOf`，切主题时所有 `get()` 与根部的 `key(Theme.current.id)` 一起重组 */
object Theme {
    var current: Skin by mutableStateOf(Skins.Night)

    private fun cfgFile(ctx: Context): File = File(Store.root(ctx), "config/theme.json")

    /** 切主题并落盘。同一主题重复点就什么也不做。 */
    fun set(ctx: Context, skin: Skin) {
        if (skin.id == current.id) return
        current = skin
        try {
            writeAtomicText(cfgFile(ctx), JSONObject().put("theme", skin.id).toString())
        } catch (e: Exception) {
        }
    }

    /** 启动时恢复上次选择；文件没有或坏了就维持默认夜紫 */
    fun load(ctx: Context) {
        try {
            val f = cfgFile(ctx)
            if (!f.exists()) return
            val id = JSONObject(f.readText()).optString("theme", "night")
            if (id != current.id) current = Skins.byId(id)
        } catch (e: Exception) {
        }
    }
}
