package com.addzero.web.modules.note.knowlagegraph

data class KnowledgeNode(
    val id: String,
    val label: String,
    val type: String = "default",
    val properties: Map<String, String> = emptyMap()
)