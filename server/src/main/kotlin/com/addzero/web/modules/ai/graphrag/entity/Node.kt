package com.addzero.web.modules.ai.graphrag.entity

import com.fasterxml.jackson.annotation.JsonPropertyDescription

data class Node(
    @field:JsonPropertyDescription("实体id")
    var nodeId: String,
    @field:JsonPropertyDescription("实体名称") var nodeName: String,
    @field:JsonPropertyDescription("实体上下文类型") var nodeType: String
)