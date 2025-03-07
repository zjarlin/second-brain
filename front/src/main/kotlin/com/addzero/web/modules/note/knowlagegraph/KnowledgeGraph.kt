package com.addzero.web.modules.note.knowlagegraph







data class KnowledgeGraph(
    val nodes: List<KnowledgeNode>,
    val edges: List<KnowledgeEdge>
)