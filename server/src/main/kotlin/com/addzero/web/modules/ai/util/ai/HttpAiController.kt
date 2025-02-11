package com.addzero.web.modules.ai.util.ai

import com.addzero.web.modules.ai.util.ai.ai_abs_builder.AiUtil
import com.addzero.web.modules.ai.config.DefaultCtx
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/aiutil")
class HttpAiController {
    @GetMapping(value = ["ask"])
    @Operation(summary = "提问")
    fun ask(
        @RequestParam question: String,
        @RequestParam promptTemplate: String = "",
        @RequestParam chatModel: String = DefaultCtx.defaultChatModelName,
        @RequestParam formatJson: String = "",
        @RequestParam formatJsonComment: String = "",
    ): String {
        val ask = AiUtil(chatModel ,question, promptTemplate).ask( formatJson,formatJsonComment)
        return ask
    }
}