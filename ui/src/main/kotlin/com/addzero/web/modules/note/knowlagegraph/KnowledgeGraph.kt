package com.addzero.web.modules.note.knowlagegraph




data class KnowledgeNode(
    val id: String,
    val label: String,
    val type: String,
    val properties: Map<String, String>
)


data class KnowledgeEdge(
    val source: String,
    val target: String,
    val label: String,
    val properties: Map<String, String>
)


data class KnowledgeGraph(
    val nodes: List<KnowledgeNode>,
    val edges: List<KnowledgeEdge>
)