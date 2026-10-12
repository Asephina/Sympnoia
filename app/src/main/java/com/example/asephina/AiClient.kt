package com.example.asephina

/*
 * ============================================================================
 *  凌薇 AI —— 网络层
 *  ---------------------------------------------------------------------------
 *  · 只用 HttpURLConnection + org.json：**不引入任何三方库**，
 *    所以 `gradlew --offline` 依然能构建（不需要下载新的 gradle 依赖）。
 *  · 走的是 OpenAI 兼容协议 POST <baseUrl>/chat/completions，
 *    DeepSeek / 通义 / Kimi / 智谱 / 硅基流动 / OpenRouter / 本地 Ollama 全都适用。
 *  · 网络权限：AndroidManifest 里的 INTERNET。除此之外没有任何额外权限。
 *  · 所有请求都在 Dispatchers.IO 上跑，UI 线程不碰 socket。
 *  · 支持 SSE 流式（逐字出）与一次性返回两种模式，统一成 Flow<String> 交给界面。
 * ========================================================================== */

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.job
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL
import java.net.UnknownHostException
import javax.net.ssl.SSLException

/** 给用户看的错误：message 已经是人话，界面直接显示即可 */
class AiError(message: String) : Exception(message)

object AiClient {

    /** 单独发一次「测试连接」用的最小请求，返回模型回复（通常是一两个字） */
    suspend fun ping(cfg: AiApiCfg): String = withContext(Dispatchers.IO) {
        val c = cfg.copy(stream = false, maxTokens = 16)
        if (!c.ready()) throw AiError("先把接口地址、API Key、模型名填完再测")
        val body = buildBody(
            c,
            system = "你是一个测试用的助手。",
            history = listOf(AiChatMsg("user", "回复两个字：正常")),
            stream = false
        )
        val text = requestOnce(c, body)
        if (text.isBlank()) "通了（模型返回了空内容）" else "通了：" + text.trim().take(40)
    }

    /**
     * 把一段对话压成 1~3 条「值得长期记住」的短句 —— 这就是自动记忆的来源。
     * 走同一个接口，但要短输出、低温度，尽量少花时间和钱。
     * 任何一步出问题都返回空表：总结失败绝不能让对话本身出错。
     */
    suspend fun summarize(cfg: AiApiCfg, personaName: String, transcript: String): List<String> =
        withContext(Dispatchers.IO) {
            if (!cfg.ready() || transcript.isBlank()) return@withContext emptyList()
            val sc = cfg.copy(stream = false, maxTokens = 300, temperature = 0.3f)
            val sys = "你在帮一个角色扮演应用整理长期记忆。请把下面这段对话里【值得长期记住】的内容" +
                    "压缩成 1~3 条短句，每条 8~40 个字。只记这几类：对方的事实（在做什么、遇到什么事）、" +
                    "两个人的约定、对方的偏好与习惯、以及" + personaName + "答应过对方的事。" +
                    "不要记寒暄、不要记空泛的情绪、不要复述原句。" +
                    "【只输出那几条短句本身】：每行一条、行首用「- 」，" +
                    "不要编号、不要加粗、不要代码块、不要任何解释或前后缀。" +
                    "一条都不值得记就只输出「- 无」。"
            val body = buildBody(
                sc, sys,
                listOf(AiChatMsg("user", transcript.take(6000))),
                false
            )
            val text = try {
                requestOnce(sc, body)
            } catch (e: Exception) {
                return@withContext emptyList()
            }
            text.split("\n")
                // 去掉列表符号、编号、代码块围栏、引号 —— 各家模型爱怎么排就怎么排，
                // 这里统一收干净，别把「1. 」「**」「```」这些壳一起存进记忆里。
                // （先剥围栏再判空：整段包在一行 ``` 里时那行本身是有内容的，不能直接丢）
                .map { it.trim() }
                .map { it.removePrefix("```").removeSuffix("```").trim() }
                .map { it.removePrefix("```json").removePrefix("```").trim() }
                .map { it.trimStart('-', '·', '*', '•', '—', '–', ' ', '\t').trim() }
                .map { it.replace(Regex("""^\d+[.、)）]\s*"""), "").trim() }
                .map { it.trim('"', '\'', '「', '」', '“', '”').trim() }
                .filter { it.length >= 4 && !it.startsWith("无") }
                .take(3)
        }

    /**
     * 把一段图片字节包成 data URL（OpenAI 多模态格式里 image_url.url 要的就是这个）。
     * 用 NO_WRAP：带换行的 base64 有些服务商会解析失败。
     */
    fun dataUrl(bytes: ByteArray, mime: String): String =
        "data:" + mime + ";base64," +
                android.util.Base64.encodeToString(bytes, android.util.Base64.NO_WRAP)

    /**
     * 从接口拉当前可用的模型名（OpenAI 兼容的 GET /models）。
     *
     * 为什么要有这个：内置的那几个模型名一定会过时 —— 各家改名的速度比软件更新的速度快。
     * 让用户点一下直接从服务商那里取，永远是最准的。
     * 拉不到（有些服务商不提供这个端点）就返回空表，界面照旧用手填的。
     */
    suspend fun listModels(cfg: AiApiCfg): List<String> = withContext(Dispatchers.IO) {
        if (cfg.baseUrl.isBlank() || cfg.apiKey.isBlank()) return@withContext emptyList()
        val url = cfg.baseUrl.trim().trimEnd('/')
            .removeSuffix("/chat/completions") + "/models"
        val conn = try {
            URL(url).openConnection() as HttpURLConnection
        } catch (e: Exception) {
            return@withContext emptyList()
        }
        try {
            conn.requestMethod = "GET"
            conn.connectTimeout = 15_000
            conn.readTimeout = cfg.timeoutSec.coerceIn(5, 600) * 1000
            conn.setRequestProperty("Authorization", "Bearer " + cfg.apiKey.trim())
            conn.setRequestProperty("Accept", "application/json")
            if (conn.responseCode !in 200..299) return@withContext emptyList()
            val body = conn.inputStream.bufferedReader(Charsets.UTF_8).use { it.readText() }
            val arr = JSONObject(body).optJSONArray("data") ?: return@withContext emptyList()
            (0 until arr.length()).mapNotNull { i ->
                arr.optJSONObject(i)?.let { jstr(it, "id") }?.ifBlank { null }
            }.sorted()
        } catch (e: Exception) {
            emptyList()
        } finally {
            try {
                conn.disconnect()
            } catch (e: Exception) {
            }
        }
    }

    /**
     * 查一次余额。查不到就返回 null —— 界面据此保持不动或显示上一次的值。
     *
     * 只对 AI_PROVIDERS 里确认过接口的那几家发请求：balanceKind 为空的一律直接返回 null，
     * **不去猜路径**。猜错的代价是用户看到假的余额，那比不显示糟糕得多。
     */
    suspend fun fetchBalance(cfg: AiApiCfg): AiBalance? = withContext(Dispatchers.IO) {
        val p = AI_PROVIDERS.firstOrNull { it.id == cfg.provider } ?: return@withContext null
        val kind = p.balanceKind
        if (kind.isBlank()) return@withContext null
        if (cfg.apiKey.isBlank() || cfg.baseUrl.isBlank()) return@withContext null

        // 「根」= Base URL 去掉结尾的 /v1：这三家的余额接口都在 /v1 之外
        // （kimi 与 siliconflow 自己那一段里带着 /v1，所以它们的尾巴写全）。
        val root = cfg.baseUrl.trim().trimEnd('/').removeSuffix("/v1")
        val tail = when (kind) {
            "deepseek" -> "/user/balance"
            "kimi" -> "/v1/users/me/balance"
            "siliconflow" -> "/v1/user/info"
            else -> return@withContext null
        }
        val conn = try {
            URL(root + tail).openConnection() as HttpURLConnection
        } catch (e: Exception) {
            return@withContext null
        }
        try {
            conn.requestMethod = "GET"
            conn.connectTimeout = 10_000
            conn.readTimeout = 10_000
            conn.setRequestProperty("Authorization", "Bearer " + cfg.apiKey.trim())
            conn.setRequestProperty("Accept", "application/json")
            if (conn.responseCode !in 200..299) return@withContext null
            val body = conn.inputStream.bufferedReader(Charsets.UTF_8).use { it.readText() }
            parseBalance(kind, body)
        } catch (e: Exception) {
            null
        } finally {
            try {
                conn.disconnect()
            } catch (e: Exception) {
            }
        }
    }

    /**
     * 把余额响应拆成「金额 + 币种」。
     * 认不出来就返回 null —— **绝不返回 0 冒充**：那会让人以为钱没了。
     */
    private fun parseBalance(kind: String, body: String): AiBalance? = try {
        val o = JSONObject(body)
        when (kind) {
            "deepseek" -> {
                val arr = o.optJSONArray("balance_infos")
                val one = if (arr != null && arr.length() > 0) arr.optJSONObject(0) else null
                val v = one?.let { jstr(it, "total_balance") }?.toDoubleOrNull()
                if (v == null) null else AiBalance(v, one?.let { jstr(it, "currency") }.orEmpty())
            }
            "kimi" -> {
                val d = o.optJSONObject("data")
                val v = d?.optDouble("available_balance", Double.NaN) ?: Double.NaN
                if (v.isNaN()) null else AiBalance(v, "CNY")
            }
            else -> {
                val d = o.optJSONObject("data")
                var v = d?.optDouble("totalBalance", Double.NaN) ?: Double.NaN
                if (v.isNaN()) v = d?.optDouble("balance", Double.NaN) ?: Double.NaN
                if (v.isNaN()) null else AiBalance(v, "CNY")
            }
        }
    } catch (e: Exception) {
        null
    }



    /**
     * 把「记住的事」整体精简一遍：合并重复、丢掉没什么用的、留下真正要记的。
     *
     * 什么时候用：条目攒得太多时（见 AiStore.MEM_CONSOLIDATE_AT）。
     * 要求是「尽量压到 12 条以内，实在压不动的就保留」——
     * 所以调用方拿回来之后会对比条数，没变少就不用它，免得反而丢东西。
     */
    suspend fun consolidate(cfg: AiApiCfg, personaName: String, items: List<String>): List<String> =
        withContext(Dispatchers.IO) {
            if (!cfg.ready() || items.isEmpty()) return@withContext emptyList()
            val sc = cfg.copy(stream = false, maxTokens = 700, temperature = 0.2f)
            val sys = "下面是一个角色扮演应用里，" + personaName + "目前记住的全部条目。" +
                    "请把它整理成一份【更精炼】的记忆清单：\n" +
                    "1. 合并说的其实是同一件事的条目；\n" +
                    "2. 删掉过期的、一次性的、没有长期价值的（例如当时的寒暄、已经结束的临时状态）；\n" +
                    "3. 保留：对方长期的事实与处境、两个人的约定、对方的偏好与习惯、" +
                    personaName + "答应过对方的事、对理解对方很重要的经历；\n" +
                    "4. 尽量压到 12 条以内，每条不超过 40 个字，语义完整、能独立看懂；\n" +
                    "5. 如果确实压不动，就原样保留，不要为了凑数删掉有用的。\n" +
                    "【只输出整理后的条目】：每行一条、行首用「- 」，" +
                    "不要编号、不要加粗、不要代码块、不要任何解释。"
            val body = buildBody(
                sc, sys,
                listOf(AiChatMsg("user", items.joinToString("\n") { "- " + it }.take(8000))),
                false
            )
            val text = try {
                requestOnce(sc, body)
            } catch (e: Exception) {
                return@withContext emptyList()
            }
            text.split("\n")
                .map { it.trim() }
                .map { it.removePrefix("```").removeSuffix("```").trim() }
                .map { it.trimStart('-', '·', '*', '•', '—', '–', ' ', '\t').trim() }
                .map { it.replace(Regex("""^\d+[.、)）]\s*"""), "").trim() }
                .filter { it.length >= 4 }
                .take(24)
        }

    /**
     * 对话。cfg.stream = true 时逐字 emit；否则整段一次性 emit。
     * 调用方 collect 即可，取消收集 = 断开连接。
     *
     * @param imgData 把消息里的附图路径换成 data URL（只在 cfg.vision 打开时才会被调用）。
     *                做成回调是为了「只对真正发出去的那几条解码」——
     *                历史会被裁剪，提前把所有图都转成 base64 是白费力气。
     *                由调用方在 IO 线程上准备好，AiClient 自己不碰 Context。
     */
    fun chat(
        cfg: AiApiCfg,
        system: String,
        history: List<AiChatMsg>,
        imgData: ((String) -> String?)? = null,
        /**
         * 「深度思考」开着时，模型的思维链会一段段流出来。
         * 用一个回调单独送出去，而不是混进 Flow<String> 里 ——
         * 那样三个调用点都得跟着改类型，而且思考与正文本来就不该走同一条道。
         * 不传就当没有（默认），行为和以前完全一样。
         */
        onThink: ((String) -> Unit)? = null,         /** 这一次请求要不要开深度思考（只给「AI 助手」用，见 buildBody） */         deepThink: Boolean = false     ): Flow<String> = flow {


        if (!cfg.ready()) throw AiError("还没配置好接口：请到「AI 设置」里填 Base URL、API Key 与模型名")
        if (history.isEmpty()) throw AiError("没有要发送的内容")

        if (!cfg.stream) {
            val text = requestOnce(cfg, buildBody(cfg, system, history, false, imgData, deepThink))
            if (text.isBlank()) throw AiError("模型返回了空内容")
            emit(text)
            return@flow
        }

        val conn = open(cfg, buildBody(cfg, system, history, true, imgData, deepThink), stream = true)
        // ★ 光靠「行与行之间检查协程是否已取消」是不够的：readLine 是阻塞的，
        //   用户点「停止」时协程虽然被取消，读却还挂在 socket 上，最长要等 readTimeout
        //   （流式那条被抬到 120s）才回来 —— 这段时间连接一直开着、对方还在生成。
        //   挂一个完成回调，任务一结束就把连接掐掉，readLine 会立刻抛异常退出。
        currentCoroutineContext().job.invokeOnCompletion {
            try {
                conn.disconnect()
            } catch (e: Exception) {
            }
        }
        try {
            val code = conn.responseCode
            if (code !in 200..299) throw AiError(httpError(code, conn.errorText()))

            val raw = StringBuilder()          // 有些服务端会无视 stream，整段返回
            var got = 0
            conn.inputStream.bufferedReader(Charsets.UTF_8).use { r: BufferedReader ->
                while (true) {
                    currentCoroutineContext().ensureActive()
                    val line = r.readLine() ?: break
                    if (line.isBlank()) continue
                    if (!line.startsWith("data:")) {
                        // 心跳、注释、防盗链前缀（)]}', 之类）都不是正文：
                        // 只收看起来像 JSON 的行，免得这些垃圾被当成模型回复显示出来
                        if (line.trimStart().startsWith("{")) raw.append(line.trim())
                        continue
                    }
                    val payload = line.substring(5).trim()
                    if (payload == "[DONE]") break
                    // 思维链：单独送走，绝不混进正文 —— 上面那句「不该当成她的话」说的就是这个
                    val th = parseThink(payload)
                    // ⚠️ 这个回调会去写 Compose 的状态，而本 flow 的上游跑在 IO 上
                    //    （见函数末尾的 flowOn）—— 直接回调就是在后台线程写界面状态。
                    //    所以先切回主线程再回调。逐块切换的开销可以忽略（整轮也就几十上百次）。
                    if (th.isNotEmpty() && onThink != null) {
                        withContext(Dispatchers.Main) { onThink.invoke(th) }
                    }

                    val delta = parseDelta(payload)
                    if (delta.isNotEmpty()) {
                        got++
                        emit(delta)
                    }
                }
            }
            // 兜底：一个 SSE 片段都没收到，说明对方返回的是普通 JSON
            if (got == 0) {
                val whole = parseWhole(raw.toString())
                if (whole.isNotBlank()) emit(whole)
                else throw AiError("没有收到内容（对方可能不支持流式，试试关掉「流式」）")
            }
        } finally {
            try {
                conn.disconnect()
            } catch (e: Exception) {
            }
        }
    }.flowOn(Dispatchers.IO)

    /* ------------------------------ 单次请求 ------------------------------ */

    private fun requestOnce(cfg: AiApiCfg, body: String): String {
        val conn = open(cfg, body, stream = false)
        try {
            val code = conn.responseCode
            if (code !in 200..299) throw AiError(httpError(code, conn.errorText()))
            val text = conn.inputStream.bufferedReader(Charsets.UTF_8).use { it.readText() }
            return parseWhole(text)
        } finally {
            try {
                conn.disconnect()
            } catch (e: Exception) {
            }
        }
    }

    private fun open(cfg: AiApiCfg, body: String, stream: Boolean): HttpURLConnection {
        val conn = try {
            URL(cfg.endpoint()).openConnection() as HttpURLConnection
        } catch (e: Exception) {
            throw AiError("接口地址不对：${cfg.endpoint()}")
        }
        try {
            conn.requestMethod = "POST"
            conn.doOutput = true
            conn.connectTimeout = 20_000
            // 流式时每个片段之间可能隔很久，读超时要给足。
            // 超时值兜到 5~600 秒：填 0 就是「永不超时」，配上取消不灵就是永久卡死。
            val sec = cfg.timeoutSec.coerceIn(5, 600)
            conn.readTimeout = (if (stream) maxOf(sec, 120) else sec) * 1000
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8")
            conn.setRequestProperty("Authorization", "Bearer " + cfg.apiKey.trim())
            if (stream) {
                conn.setRequestProperty("Accept", "text/event-stream")
                conn.setRequestProperty("Accept-Encoding", "identity")
            } else {
                conn.setRequestProperty("Accept", "application/json")
            }
            conn.outputStream.use { it.write(body.toByteArray(Charsets.UTF_8)) }
        } catch (e: UnknownHostException) {
            try {
                conn.disconnect()
            } catch (e2: Exception) {
            }
            throw AiError("连不上服务器：域名解析失败。检查网络，或看看地址有没有拼错")
        } catch (e: SocketTimeoutException) {
            try {
                conn.disconnect()
            } catch (e2: Exception) {
            }
            throw AiError("连接超时。检查网络，或把「超时」调大一点")
        } catch (e: SSLException) {
            try {
                conn.disconnect()
            } catch (e2: Exception) {
            }
            throw AiError("HTTPS 握手失败：$e")
        } catch (e: AiError) {
            try {
                conn.disconnect()
            } catch (e2: Exception) {
            }
            throw e
        } catch (e: Exception) {
            // 失败也要把连接放掉，否则每次请求失败都漏一个
            try {
                conn.disconnect()
            } catch (e2: Exception) {
            }
            throw AiError("请求发不出去：${e.message ?: e.javaClass.simpleName}")
        }
        return conn
    }

    private fun HttpURLConnection.errorText(): String = try {
        (errorStream ?: inputStream)?.bufferedReader(Charsets.UTF_8)?.use { it.readText() }.orEmpty()
    } catch (e: Exception) {
        ""
    }

    /* ------------------------------ 报文构造 ------------------------------ */

    /**
     * 单条历史消息的字数上限。
     * 与「分享」那边的两万字上限对齐 —— 两边不一致的话，分享时留了全文、
     * 发请求时又被这里截掉，用户看到的仍然是被吞过的版本。
     */
    private const val HISTORY_MSG_CHARS = SHARE_BODY_CHARS

    /**
     * 一次请求里，历史对话的总字数预算（超出就从最旧的开始丢）。
     * 放得比原来宽：分享过来的文章 / 日记一写就是几千字，裁掉就等于她没读到；
     * 但也不能拉满 —— 中文一个字差不多就是一个 token，预算开到几万就等于把上下文顶满，
     * 小上下文的模型（8k 那一档、本地小模型）会直接报错。
     * （最近这一条无论如何都会整条带上，见下面那个 `picked.isNotEmpty()`。）
     */
    private const val HISTORY_CHAR_BUDGET = 24_000

    private fun buildBody(
        cfg: AiApiCfg,
        system: String,
        history: List<AiChatMsg>,
        stream: Boolean,
        imgData: ((String) -> String?)? = null,         /**          * 这一次请求要不要开「深度思考」。          * 由调用方按**人设卡**决定，而不是读全局开关 ——          * 那个开关是「AI 助手」专用的：别的人设卡就算开关开着，也不该开始自言自语。          */         deepThink: Boolean = false     ): String {

        val msgs = JSONArray()
        if (system.isNotBlank()) {
            msgs.put(JSONObject().put("role", "system").put("content", system))
        }
        // 两道闸：先按轮数取最近 N 轮，再按**总字数**从最新往回装 ——
        // 只按轮数是不够的：分享过几篇日记之后，12 轮也能堆出上万字。
        // 「有图没字」的消息也得留着（表情包就是这种）。
        val usable = history.filter { it.content.isNotBlank() || it.img.isNotBlank() }
        // ⚠️ 按**轮**取，不按条取：分条之后一次回复会变成好几条消息，
        //    如果按「最近 25 条」算，一段分了很多条的回复就能把真正的上下文全挤掉。
        //    这里从倒数第 historyRounds 条用户消息处开始留。
        val userIdx = usable.withIndex().filter { it.value.role != "assistant" }.map { it.index }
        // 轮数填 0 也要把「当前这一句」发出去，所以下限是最后一条用户消息
        val start = when {
            cfg.historyRounds <= 0 -> userIdx.lastOrNull() ?: 0
            userIdx.size > cfg.historyRounds -> userIdx[userIdx.size - cfg.historyRounds]
            else -> 0
        }
        val recent = usable.drop(start)
        val picked = ArrayList<Pair<AiChatMsg, String>>()
        var used = 0
        for (m in recent.asReversed()) {
            val body = if (m.content.length > HISTORY_MSG_CHARS)
                m.content.take(HISTORY_MSG_CHARS) + "……（后文略）"
            else m.content
            if (picked.isNotEmpty() && used + body.length > HISTORY_CHAR_BUDGET) break
            picked.add(m to body)
            used += body.length
        }
        // ★ 最老的那条不能是 assistant：调用方虽然做过一次「去掉开头的 assistant」，
        //   但上面这两道裁剪（按轮数 takeLast、按字数 break）是在那之后才发生的，
        //   裁剪完很可能又剩下一条 assistant 打头 —— 有些兼容端点见到这个直接 400。
        val ordered = picked.asReversed().toMutableList()
        while (ordered.isNotEmpty() && ordered.first().first.role == "assistant") {
            ordered.removeAt(0)
        }
        ordered.forEach { (m, body) ->
            val role = if (m.role == "assistant") "assistant" else "user"
            // 表情包：模型看到一张图往往会当成「照片/截图」认真分析，
            // 得明确告诉它这是表情包 —— 它要读的是情绪，不是画面内容。
            val sticker = m.kind == "sticker"
            // 带图的消息：只有开了「能看图」才按多模态格式发；
            // 没开的时候给一句中性的说明，免得她对着空内容答非所问。
            val data = if (m.img.isNotBlank() && cfg.vision) imgData?.invoke(m.img) else null
            if (data != null) {
                val parts = JSONArray()
                val head = listOf(
                    body,
                    if (sticker) "（这是我发的**表情包**，用来表达情绪，不是照片也不是截图 —— 领会它的意思就行，别去描述画面。）" else ""
                ).filter { it.isNotBlank() }.joinToString("\n")
                if (head.isNotBlank()) {
                    parts.put(JSONObject().put("type", "text").put("text", head))
                }
                parts.put(
                    JSONObject().put("type", "image_url")
                        .put("image_url", JSONObject().put("url", data))
                )
                msgs.put(JSONObject().put("role", role).put("content", parts))
            } else {
                val text = when {
                    body.isNotBlank() && sticker -> body + "\n（对方发来一张表情包）"
                    body.isNotBlank() -> body
                    sticker -> "（对方发来一张表情包，但当前模型看不到图片内容）"
                    m.img.isNotBlank() -> "（对方发来一张图片，但当前模型看不到图片内容）"
                    else -> ""
                }
                // 空正文又没有图：直接不进请求体，有些服务商见到空 content 会 400
                if (text.isNotBlank()) {
                    msgs.put(JSONObject().put("role", role).put("content", text))
                }
            }
        }
        val root = JSONObject()
            .put("model", cfg.model.trim())
            .put("messages", msgs)
            .put("temperature", cfg.temperature.toDouble())
            .put("top_p", cfg.topP.toDouble())
            .put("max_tokens", cfg.maxTokens)
            .put("stream", stream)
        // 关掉思考模式：现在的旗舰模型默认就是「先想再答」，聊天里会先吐一大段思维链，
        // 既慢又乱、还吃掉回复长度。只有认这个参数的那几家才带 ——
        // 别家收到不认识的参数会直接报错。
        if (cfg.providerSupportsThinking() && !deepThink) {
            root.put("thinking", JSONObject().put("type", "disabled"))
        }
        return root.toString()
    }

    /* ------------------------------ 响应解析 ------------------------------ */

    /**
     * 取 SSE 一片里的增量文本。
     *
     * ⚠️ reasoning_content **刻意丢掉**。
     *    现在的旗舰模型（DeepSeek V4 系列这类）默认就是「先想再答」，
     *    思考过程流式地吐出来，一段能顶几屏 —— 直接显示在聊天里又乱又慢，
     *    用户看到的就是「她怎么突然开始自言自语」。
     *    所以这里只取 content；请求那头另外用 thinking=disabled 把思考关掉（见 buildBody），
     *    关不掉的那些模型，思考也只是被这里滤掉，界面上不会看见。
     */
    /**
     * 安全地取一个字符串字段。
     *
     * ⚠️ 这里**不能**用 optString(name, "")：Android 上它的实现是
     *    `JSON.toString(opt(name))`，而 JSON 里的 null 会被 String.valueOf 变成
     *    **字符串 "null"** —— 兜底值根本轮不到。
     *    深度思考时服务商会大量下发 `"content": null`（那一片只有思考内容），
     *    于是这些 "null" 被当成正文一片片累积起来，正文前面就糊了一大段 null。
     */
    private fun jstr(o: JSONObject?, key: String): String {
        if (o == null) return ""
        val v = o.opt(key) ?: return ""
        if (v == JSONObject.NULL) return ""
        val s = v.toString()
        return if (s == "null") "" else s
    }

    private fun parseDelta(payload: String): String {

        return try {
            val o = JSONObject(payload)
            val choices = o.optJSONArray("choices") ?: return ""
            val first = choices.optJSONObject(0) ?: return ""
            val d = first.optJSONObject("delta")
            if (d != null) {
                val c = jstr(d, "content")
                if (c.isNotEmpty()) return c
                // 有些实现把增量放在 message 里
                val m = d.optJSONObject("message")
                if (m != null) return jstr(m, "content")
                return ""
            }
            jstr(first.optJSONObject("message"), "content")
        } catch (e: Exception) {
            ""
        }
    }

    /** 取完整响应里的正文 */
    private fun parseWhole(text: String): String {
        return try {
            val t = text.trim()
            if (t.isEmpty()) return ""
            val o = JSONObject(t)
            // 有些网关把错误塞在 200 里
            val err = o.optJSONObject("error")
            if (err != null) throw AiError(jstr(err, "message").ifBlank { "接口返回了一个错误" })
            // 有的网关把错误写成字符串：{"error":"invalid api key"}。
            // 不认它的话，这段 JSON 会被当成「模型的回复」显示出来、还会存盘并进导出物。
            val errAny = o.opt("error")
            if (errAny != null && errAny != JSONObject.NULL && errAny !is JSONObject) {
                throw AiError(errAny.toString().take(200))
            }
            val choices = o.optJSONArray("choices")
            if (choices != null && choices.length() > 0) {
                val first = choices.optJSONObject(0)
                val m = first?.optJSONObject("message")
                var c = jstr(m, "content")
                if (c.isBlank()) c = jstr(first?.optJSONObject("delta"), "content")
                // reasoning_content 刻意不要：那是思考过程，不该当成她的话
                return c
            }
            t
        } catch (e: AiError) {
            throw e
        } catch (e: Exception) {
            // 不是 JSON：原样吐出来反而有助于排查
            text.trim().take(300)
        }
    }

    /**
     * 取 SSE 一片里的**思维链**（reasoning_content）。
     *
     * 单独一个函数、走单独一条回调，是因为它和正文的去处完全不同：
     * 一个是「她要说的话」，一个是「她怎么想出来的」；界面上也要分开显示。
     * 另外这也意味着思维链**不会**被当成她的发言进上下文、进存盘、进导出。
     */
    private fun parseThink(payload: String): String {
        return try {
            val o = JSONObject(payload)
            val choices = o.optJSONArray("choices") ?: return ""
            val first = choices.optJSONObject(0) ?: return ""
            val d = first.optJSONObject("delta")
            if (d != null) {
                val t = jstr(d, "reasoning_content")
                if (t.isNotEmpty()) return t
                val m = d.optJSONObject("message")
                if (m != null) {
                    val t2 = jstr(m, "reasoning_content")
                    if (t2.isNotEmpty()) return t2
                }
            }
            jstr(first.optJSONObject("message"), "reasoning_content")
        } catch (e: Exception) {
            ""
        }
    }

    /** 把 HTTP 状态码翻译成人话 */
    private fun httpError(code: Int, body: String): String {
        val detail = extractMessage(body)
        val base = when (code) {
            400 -> "请求被拒绝（400）。多半是模型名写错了，或参数不合法"
            401, 403 -> "认证失败（$code）。API Key 不对、没填、或已过期"
            402 -> "余额不足（402）。去服务商后台看一下额度"
            404 -> "找不到接口或模型（404）。检查 Base URL 与模型名"
            413 -> "内容太长了（413）。把「上下文轮数」调小一点"
            422 -> "参数不合法（422）"
            429 -> "被限流了（429）。等一会儿再发，或降低频率"
            in 500..599 -> "服务端出错（$code）。多半是对方的问题，过会儿再试"
            else -> "请求失败（$code）"
        }
        return if (detail.isBlank()) base else "$base\n$detail"
    }

    private fun extractMessage(body: String): String = try {
        if (body.isBlank()) ""
        else {
            val o = JSONObject(body)
            val direct = jstr(o, "message")
            // error 可能是对象（取它的 message），也可能直接是个字符串
            val fromErr = when (val e = o.opt("error")) {
                null, JSONObject.NULL -> ""
                is JSONObject -> jstr(e, "message").ifBlank { e.toString() }
                else -> e.toString()
            }
            fromErr.ifBlank { direct }
        }.take(200)
    } catch (e: Exception) {
        body.trim().take(120)
    }
}
