package com.addzero.web.modules.ai.chat

import cn.hutool.core.io.IoUtil
import cn.hutool.core.util.StrUtil
import cn.hutool.extra.spring.SpringUtil
import cn.hutool.poi.excel.ExcelUtil
import com.addzero.web.modules.ai.agent.dbdesign.FormDTO
import com.addzero.web.modules.ai.util.ai.Promts.DBASIMPLE_JSON_PATTERN_PROMPT_CHINESE
import com.addzero.web.modules.ai.util.ai.ctx.ChatModels
import com.addzero.web.modules.ai.util.ai.ai_abs_builder.AbsChatBuilder
import com.addzero.web.modules.ai.util.ai.ai_abs_builder.AiUtil
import com.addzero.web.infra.ocr.LxOcrUtil
import com.addzero.web.infra.upload.DownloadUtil
import io.swagger.v3.oas.annotations.Operation
import org.springframework.ai.chat.model.ChatModel
import org.springframework.ai.image.ImageGeneration
import org.springframework.ai.image.ImageModel
import org.springframework.ai.image.ImagePrompt
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.http.codec.ServerSentEvent
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import reactor.core.publisher.Flux


@RestController
@RequestMapping("/chat")
class ChatController(
    private val vectorStore: VectorStore,
    private val chatmodel :ChatModel
//    private val vectorStore: PgVectorStore,
//    private val imageModel: ImageModel,
) {
    @GetMapping("dbatest")
    @Operation(summary = "设计表test")
    fun jdaoisdj(): FormDTO? {
        val ask = AiUtil(ChatModels.QWEN2_5_CODER_0_5B, "设计一张用户表",  DBASIMPLE_JSON_PATTERN_PROMPT_CHINESE).ask(FormDTO::class.java)
        return ask
    }


    @PostMapping("ocr")
    @Operation(summary = "ocr图片文字识别")
    fun ocr(@RequestPart multipartFile: MultipartFile): String {
        val originalFilename = multipartFile.originalFilename
        val bytes = multipartFile.bytes
        val property = SpringUtil.getProperty("ocr.ip")
        return LxOcrUtil.ocr(property, bytes, originalFilename!!)
    }

    @PostMapping("dataExtractionExpert")
    @Operation(summary = "数据提取专家")
    fun dataExtractionExpert(
        @RequestParam modelName: String = "llava",
        @RequestParam question: String? = "",
        @RequestParam formatJson: String,
        @RequestParam formatComment: String,
        @RequestPart file: Array<MultipartFile>,
    ): String {
        val cosplay = "你是一个数据提取专家,请帮我提取上下文的数据"
        val map = file.map {
            try {
                val ocr = ocr(it)
                val qocr = if (StrUtil.isBlank(question)) {
                    ocr
                } else {
                    ocr + question
                }
                val ask = AiUtil(modelName, qocr, cosplay).ask(formatJson, formatComment)
                mapOf("question" to qocr, "answer" to ask)
            } catch (e: Exception) {
                mapOf("question" to question, "answer" to "识别出错")
            }
        }
//
//        val exportMap =
//        com.addzero.common.util.excel.ExcelUtil.getWriter()
//        // 添加示例数据写入操作
//        exportMap.writeCellValue("Sheet1", 0, 0, "Hello")
//        exportMap.writeCellValue("Sheet1", 0, 1, "World")
//        exportMap.finish()
//
//        val absolutePath = exportMap.absolutePath
//        DownloadUtil.downloadExcel("out.xlsx", { IoUtil.copy(exportMap.inputStream(), it) })
//        return absolutePath
        return TODO("提供返回值")
    }




    /**
     * * 0 = "dashScopeAiVLChatModel"
     * * 1 = "dashScopeAiChatModel"
     * * 2 = "moonshotChatModel"
     * * 3 = "ollamaChatModel"
     *         ------qwen2.5:1.5b
     *         qwen2.5-coder:1.5b
     *
     * * 4 = "openAiChatModel"
     * * 5 = "zhiPuAiChatModel"
     *
     * @param chatDTO@return [String]
     */
    @PostMapping(value = ["/chat"])
    @Operation(summary = "对话")
    fun chat( chatDTO: ChatDTO, @RequestPart file: Array<MultipartFile>?): String? {
        val chatModelPgVectorStoreAbsChatBuilder = AbsChatBuilder.of(
            chatDTO.modelName,
            vectorStore,
            chatDTO.prompt,
            chatDTO.sessionId,
            chatDTO.enableVectorStore,
            chatDTO.enableFunctionCalling,
            chatDTO.cosplay,
            file,
            chatDTO.fomatJson,
            chatDTO.jsonComment
        )
//        println(chatmodel)

        val str = chatModelPgVectorStoreAbsChatBuilder.buildToStr()
        return str
    }

    /**
     * * 0 = "dashScopeAiVLChatModel"
     * * 1 = "dashScopeAiChatModel"
     * * 2 = "moonshotChatModel"
     * * 3 = "ollamaChatModel"
     * * 4 = "openAiChatModel"
     * * 5 = "zhiPuAiChatModel"
     *
     * @param chatDTO@return [String]
     */
    @PostMapping(value = ["/chatStream"])
    @Operation(summary = "流式对话")
    fun chatStream(
        @RequestPart chatDTO: ChatDTO, @RequestPart file: Array<MultipartFile>?,
    ): Flux<ServerSentEvent<String>> {

        val of = AbsChatBuilder.of(
            chatDTO.modelName,
            vectorStore,
            chatDTO.prompt,
            chatDTO.sessionId,
            chatDTO.enableVectorStore,
            chatDTO.enableFunctionCalling,
            chatDTO.cosplay,
            file,
            chatDTO.fomatJson,
            chatDTO.jsonComment
        )
        val chatModelPgVectorStoreAbsChatBuilder: AbsChatBuilder<VectorStore> = of
        val serverSentEventFlux = chatModelPgVectorStoreAbsChatBuilder.buildToStream()
        return serverSentEventFlux
    }

//    @GetMapping("chat/image")
//    @Operation(summary = "文生图")
//    fun textToImageChat(@RequestParam input: String?): List<ImageGeneration> {
//        val call = imageModel.call(ImagePrompt(input))
//        val output = call.results
//        return output
//    }


    @GetMapping("hello")
    @Operation(summary = "前端测试接口")
    fun hello(@RequestParam string: String): String {
        return "hello"
    }

    @PostMapping("chatTest")
    @Operation(summary = "前端测试接口")
    fun chatTest(@RequestBody chatDTO: ChatDTO): String {
        return "hello"
    }
}