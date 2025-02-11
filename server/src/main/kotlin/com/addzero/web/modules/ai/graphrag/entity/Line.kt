package com.addzero.web.modules.ai.graphrag.entity

import com.fasterxml.jackson.annotation.JsonPropertyDescription

data class Line(
@field:JsonPropertyDescription("from node id")    var from: String, @field:JsonPropertyDescription("to node id")    var to: String,@field:JsonPropertyDescription("类型或关系描述")    var relation: String?
)