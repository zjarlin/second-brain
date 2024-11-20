package com.addzero.web.model.notes

import kotlinx.serialization.Serializable

@Serializable
data class KnowledgeNode(
    val id: String,
    val label: String,
    val type: String,
    val properties: Map<String, String>
)

@Serializable
data class KnowledgeEdge(
    val source: String,
    val target: String,
    val label: String,
    val properties: Map<String, String>
)

@Serializable
data class KnowledgeGraph(
    val nodes: List<KnowledgeNode>,
    val edges: List<KnowledgeEdge>
) 