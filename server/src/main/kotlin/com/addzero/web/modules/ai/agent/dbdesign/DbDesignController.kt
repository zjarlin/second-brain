package com.addzero.web.modules.ai.agent.dbdesign

import com.addzero.web.modules.ai.util.ai.ai_abs_builder.AiUtil
import com.addzero.web.modules.ai.util.ai.ctx.ChatModels
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController(value = "/agent")
class DbDesignController {


    @GetMapping("dbdesign")
    fun dbdesign(@RequestParam modelName: String = ChatModels.OLLAMA, @RequestParam ques: String): FormDTO? {
        val template = """
           你是一个DBA专家,请根据用户的内容设计数据库
           以下是我给定的内容{question}
        """.trimIndent()
        val ask = AiUtil.ask(ques, template, modelName, FormDTO::class.java)
        return ask
    }
}