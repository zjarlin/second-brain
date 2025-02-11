package com.addzero.web.modules.ai.graphrag.entity

import com.fasterxml.jackson.annotation.JsonPropertyDescription


data class GraphPO(
    @field:JsonPropertyDescription("节点列表") var nodes: List<Node> = ArrayList(),
    @field:JsonPropertyDescription("关系列表") var lines: List<Line> = ArrayList(),
    @field:JsonPropertyDescription("三元组属性列表") var spos: List<SPO> = ArrayList()
)


//}