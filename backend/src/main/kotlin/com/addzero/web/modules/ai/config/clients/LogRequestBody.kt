package com.addzero.web.modules.ai.config.clients

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

class LogRequestBody {
    @JsonProperty("model")
    private val model: String? = null

    @JsonProperty("messages")
    private val messages: List<MessagesDTO>? = null

    @JsonProperty("stream")
    private val stream: Boolean? = null

    @JsonProperty("tools")
    private val tools: List<*>? = null

    @JsonProperty("options")
    private val options: OptionsDTO? = null

    class OptionsDTO

    class MessagesDTO {
        @JsonProperty("role")
        private val role: String? = null

        @JsonProperty("content")
        private val content: String? = null

        @JsonProperty("images")
        @JsonIgnore
        private val images: List<String>? = null
    }
}