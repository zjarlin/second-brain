package com.addzero.web.modules.note.knowlagegraph

data class KnowledgeEdge(
    val source: String,
    val target: String,
    val label: String,
    val properties: Map<String, String> = emptyMap()
)