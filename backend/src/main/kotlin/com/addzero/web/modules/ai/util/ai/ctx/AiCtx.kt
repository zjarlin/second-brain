package com.addzero.web.modules.ai.util.ai.ctx

import cn.hutool.core.util.ReflectUtil
import cn.hutool.core.util.StrUtil
import cn.hutool.extra.spring.SpringUtil
import com.addzero.common.kt_util.addPrefixIfNot
import com.addzero.common.kt_util.cleanBlank
import com.addzero.common.kt_util.isBlank
import com.addzero.common.kt_util.isNotBlank
import com.addzero.web.modules.ai.util.ai.Promts
import com.addzero.web.modules.ai.util.ai.ai_abs_builder.AiUtil.Companion.buildStructureOutPutPrompt
import com.alibaba.fastjson2.toJSONString
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor
import org.springframework.ai.chat.memory.ChatMemory
import org.springframework.ai.chat.model.ChatModel
import org.springframework.ai.chat.prompt.ChatOptions
import org.springframework.ai.ollama.OllamaChatModel
import org.springframework.ai.ollama.api.OllamaOptions
import javax.swing.Spring

object AiCtx {

    /**
     * 结构化输出上下文
     * @param [question] 问题
     * @param [promptTemplate] 提示模板
     * @param [clazz] 提供类以自动生成 formatJson 和 jsonComment（可选）
     * @return [StructureOutPutPrompt]
     */
    fun structuredOutputContext(
        question: String,
        promptTemplate: String,
        clazz: Class<*>,
    ): StructureOutPutPrompt {

        val finalFormatJson = ReflectUtil.newInstance(clazz).toJSONString()
        val finalJsonComment = buildStructureOutPutPrompt(clazz)
        // 生成结构化输出的内容
        return structuredOutputContext(
            question.cleanBlank(), promptTemplate.cleanBlank(), finalFormatJson, finalJsonComment
        )
    }


    fun structuredOutputContext(
        question: String,
        promptTemplate: String?,
        formatJson: String?,
        jsonComment: String?,
    ): StructureOutPutPrompt {


        val que = "{question}"
        if (promptTemplate.isBlank() && formatJson.isBlank()) {
            val promptTemplate1 = promptTemplate.cleanBlank()
            val promptTemplate2 = StrUtil.addPrefixIfNot(promptTemplate1, que)
            return StructureOutPutPrompt(promptTemplate2, mapOf("question" to question))
        }
        if (promptTemplate.isBlank() && formatJson.isNotBlank()) {
            val promptTemplate1 = promptTemplate.cleanBlank()
            val promptTemplate2 = StrUtil.addPrefixIfNot(promptTemplate1, que)
            return StructureOutPutPrompt(promptTemplate2, mapOf("question" to question))
        }

        val formatPrompt: String = """
       ${Promts.JSON_PATTERN_PROMPT}
           {formatJson}}
            ------------------
           以下是json数据的注释：
           {jsonComment}
        """.trimIndent()
        val newPromptTemplate = promptTemplate + formatPrompt
        val quesCtx = mapOf(
            "question" to question, "formatJson" to formatJson, "jsonComment" to jsonComment
        )
        return StructureOutPutPrompt(newPromptTemplate.addPrefixIfNot(que), quesCtx)
    }

    data class StructureOutPutPrompt(val newPrompt: String, val quesCtx: Map<String, String?>)


    /**
     *
     *  #  * * 0 = "dashScopeAiVLChatModel"
     *   #  * * 1 = "dashScopeAiChatModel"
     *   #  * * 2 = "moonshotChatModel"
     *   #  * * 3 = "ollamaChatModel"        should be a list in  spring yml ?
     *   #  *         ------qwen2.5:1.5b
     *   #  *         ------qwen2.5-coder:1.5b
     *
     *   #  * * 4 = "openAiChatModel"
     *   #  * * 5 = "zhiPuAiChatModel"
     *
     *
     *
     *If the user-defined model name cannot be found, go to the ollama model to find it
     *  @param [modelName]
     * @return [ChatClient?]
     */
    fun defaultChatClient(modelName: String): ChatClient {
        val defaultChatModel = defaultChatModel(modelName)
        /**
         *
        i mean , change the options settings in the model object here🙈

        cannot set this attribute like this:
        ---------------------------------------------------------
        model.defaultOptions = OllamaOptions.builder()
        .withModel(modelName)
        .build()
        -------------------------------------------------
         */
//    how  Change the options property of ChatModel before building the ChatClient instance
        /**
         *  The timing for changing options here is when calling, and can i use the chatModel constructor or method changeModel again?
         *
         *
         * @GetMapping("/chat/provider-options")
         *     String chatWithProviderOptions(@RequestParam(defaultValue = "What did Gandalf say to the Balrog?") String question) {
         *         return chatModel.call(new Prompt(question, OllamaOptions.create()
         *                         .withModel("llama3.2")
         *                         .withRepeatPenalty(1.5)))
         *                 .getResult().getOutput().getContent();
         *     }
         */
//        Can I change the options before building the ChatClient instance?

        var buildOpt = defaultChatModel.defaultOptions

        if (defaultChatModel is OllamaChatModel) {
            val ollamaOptions = buildOpt as OllamaOptions
            ollamaOptions.model = modelName
            buildOpt = ollamaOptions as ChatOptions
        }
//        defaultChatModel is dashsc

        val chatClientBuilder = ChatClient.builder(defaultChatModel).defaultAdvisors(


            SimpleLoggerAdvisor({
                val userText = it.userText()
                """
                        "request: " + $userText
                        "Custom model: " + $modelName
                        """.trimIndent()
            }, {
                "Custom response: " + it.result
            }, 1)
        )
        chatClientBuilder.defaultOptions(buildOpt)
        val chatClient = chatClientBuilder.build()
        return chatClient
    }

    private fun defaultChatModel(modelName: String): ChatModel {
//        val bean = SpringUtil.getBean<OllamaChatModel>(OllamaChatModel::class.java)
//        return bean


        SpringUtil.getBean(ChatModel::class.java)
        val bean = SpringUtil.getBeansOfType<ChatModel>(ChatModel::class.java)
        val model: ChatModel = bean[modelName] ?: SpringUtil.getBean<OllamaChatModel>(OllamaChatModel::class.java)
        return model
    }


    fun withMemory(chatClient: ChatClient.Builder): ChatClient {
        val chatMemory = SpringUtil.getBean(ChatMemory::class.java)
        val promptChatMemoryAdvisor = PromptChatMemoryAdvisor(chatMemory)
        return chatClient.defaultAdvisors(
            promptChatMemoryAdvisor
        ).build()
    }


}