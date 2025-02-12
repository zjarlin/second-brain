package com.addzero.web.modules.ai.chat

import com.addzero.web.modules.ai.util.ai.Promts
import com.addzero.web.modules.ai.util.ai.Promts.DEFAULT_SYSTEM
import com.fasterxml.jackson.annotation.JsonIgnore


data class ChatDTO(
    val modelName: String = "ollamaChatModel",
    val prompt: String="",
    val sessionId: String="",
    val enableVectorStore: Boolean = false,
    val enableFunctionCalling: Boolean = false,
    val cosplay: String = DEFAULT_SYSTEM,

    val fomatJson: String?="",
    val jsonComment: String?="",
) {

    @JsonIgnore
    val jsonPromt: String = """
   ${Promts.JSON_PATTERN_PROMPT}
       $fomatJson
        ------------------
       以下是json数据的注释：
       $jsonComment 
    """.trimIndent()
}