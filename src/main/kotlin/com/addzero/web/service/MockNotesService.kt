package com.addzero.web.service

import com.addzero.web.model.PageResult
import com.addzero.web.model.notes.*

class MockNotesService : NotesService {
    override suspend fun getKnowledgeGraph(query: String?): KnowledgeGraph {
        // 模拟延迟
        kotlinx.coroutines.delay(500)

        // Mock 知识图谱数据
        val nodes = listOf(
            KnowledgeNode(
                id = "1",
                label = "Kotlin",
                type = "Language",
                properties = mapOf(
                    "category" to "Programming",
                    "paradigm" to "Object-Oriented, Functional",
                    "platform" to "JVM, Native, JS",
                    "creator" to "JetBrains",
                    "releaseYear" to "2016"
                )
            ),
            KnowledgeNode(
                id = "2",
                label = "Compose",
                type = "Framework",
                properties = mapOf(
                    "category" to "UI",
                    "platform" to "Android, Desktop, Web",
                    "language" to "Kotlin",
                    "architecture" to "Declarative",
                    "maintainer" to "JetBrains & Google"
                )
            ),
            KnowledgeNode(
                id = "3",
                label = "Ktor",
                type = "Framework",
                properties = mapOf(
                    "category" to "Backend",
                    "features" to "Async, HTTP, WebSocket",
                    "platform" to "JVM, Native",
                    "maintainer" to "JetBrains",
                    "architecture" to "Modular"
                )
            ),
            KnowledgeNode(
                id = "4",
                label = "Coroutines",
                type = "Library",
                properties = mapOf(
                    "category" to "Async",
                    "features" to "Structured Concurrency",
                    "platform" to "All Kotlin targets",
                    "paradigm" to "Sequential Programming",
                    "releaseYear" to "2018"
                )
            ),
            KnowledgeNode(
                id = "5",
                label = "Multiplatform",
                type = "Feature",
                properties = mapOf(
                    "category" to "Platform",
                    "targets" to "JVM, JS, Native, WASM",
                    "status" to "Production-Ready",
                    "maintainer" to "JetBrains",
                    "benefits" to "Code Sharing"
                )
            ),
            KnowledgeNode(
                id = "6",
                label = "Serialization",
                type = "Library",
                properties = mapOf(
                    "category" to "Data Processing",
                    "formats" to "JSON, CBOR, Protobuf",
                    "platform" to "All Kotlin targets",
                    "features" to "Compile-time safety",
                    "maintainer" to "JetBrains"
                )
            ),
            KnowledgeNode(
                id = "7",
                label = "Android",
                type = "Platform",
                properties = mapOf(
                    "category" to "Mobile",
                    "owner" to "Google",
                    "language" to "Kotlin, Java",
                    "marketShare" to "70%",
                    "type" to "Open Source"
                )
            )
        )

        val edges = listOf(
            KnowledgeEdge(
                source = "1",
                target = "2",
                label = "primary language",
                properties = mapOf("strength" to "strong", "type" to "uses")
            ),
            KnowledgeEdge(
                source = "1",
                target = "3",
                label = "built with",
                properties = mapOf("strength" to "strong", "type" to "uses")
            ),
            KnowledgeEdge(
                source = "1",
                target = "4",
                label = "core feature",
                properties = mapOf("strength" to "strong", "type" to "includes")
            ),
            KnowledgeEdge(
                source = "1",
                target = "5",
                label = "key feature",
                properties = mapOf("strength" to "strong", "type" to "supports")
            ),
            KnowledgeEdge(
                source = "2",
                target = "5",
                label = "built on",
                properties = mapOf("strength" to "medium", "type" to "uses")
            ),
            KnowledgeEdge(
                source = "3",
                target = "5",
                label = "supports",
                properties = mapOf("strength" to "medium", "type" to "feature")
            ),
            KnowledgeEdge(
                source = "2",
                target = "7",
                label = "primary platform",
                properties = mapOf("strength" to "strong", "type" to "targets")
            ),
            KnowledgeEdge(
                source = "1",
                target = "6",
                label = "official library",
                properties = mapOf("strength" to "medium", "type" to "provides")
            ),
            KnowledgeEdge(
                source = "6",
                target = "5",
                label = "supports",
                properties = mapOf("strength" to "medium", "type" to "feature")
            )
        )

        // 如果有搜索查询，过滤节点和边
        return if (!query.isNullOrBlank()) {
            val filteredNodes = nodes.filter { node ->
                node.label.contains(query, ignoreCase = true) ||
                node.type.contains(query, ignoreCase = true) ||
                node.properties.any { (key, value) ->
                    key.contains(query, ignoreCase = true) ||
                    value.contains(query, ignoreCase = true)
                }
            }
            val nodeIds = filteredNodes.map { it.id }.toSet()
            val filteredEdges = edges.filter {
                it.source in nodeIds && it.target in nodeIds
            }
            KnowledgeGraph(filteredNodes, filteredEdges)
        } else {
            KnowledgeGraph(nodes, edges)
        }
    }

    override suspend fun <T : Any> getNotes(parentId: String?, page: Int, size: Int): PageResult<T> {
        TODO("Not yet implemented")
    }

    // 实现其他必要的方法...
//    override suspend fun getNotes(
//        parentId: String?,
//        page: Int,
//        size: Int
//    ): PageResult<Note> {
//        return PageResult(
//            content = emptyList(),
//            totalElements = 0,
//            totalPages = 0,
//            pageNumber = page,
//            pageSize = size,
//            isFirst = true,
//            isLast = true
//        )
//    }
    override suspend fun createNote(note: Note): Note = note
    override suspend fun updateNote(note: Note): Note = note
    override suspend fun deleteNote(id: String) {}
    override suspend fun uploadFile(file: ByteArray, filename: String): String = ""
    override suspend fun askQuestion(question: String): Answer = Answer("", "", "", emptyList(), 0L)
    override suspend fun getQuestionHistory(): List<Question> = emptyList()
}