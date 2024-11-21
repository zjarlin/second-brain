package com.addzero.web.service

import com.addzero.web.model.notes.*

class MockNotesService : NotesService {
    override suspend fun getKnowledgeGraph(query: String?): KnowledgeGraph {
        // Mock 知识图谱数据
        val nodes = listOf(
            KnowledgeNode(
                id = "1",
                label = "Kotlin",
                type = "Language",
                properties = mapOf("category" to "Programming")
            ),
            KnowledgeNode(
                id = "2",
                label = "Compose",
                type = "Framework",
                properties = mapOf("category" to "UI")
            ),
            KnowledgeNode(
                id = "3",
                label = "Ktor",
                type = "Framework",
                properties = mapOf("category" to "Backend")
            ),
            KnowledgeNode(
                id = "4",
                label = "Coroutines",
                type = "Library",
                properties = mapOf("category" to "Async")
            ),
            KnowledgeNode(
                id = "5",
                label = "Multiplatform",
                type = "Feature",
                properties = mapOf("category" to "Platform")
            )
        )

        val edges = listOf(
            KnowledgeEdge(
                source = "1",
                target = "2",
                label = "uses",
                properties = mapOf("strength" to "strong")
            ),
            KnowledgeEdge(
                source = "1",
                target = "3",
                label = "uses",
                properties = mapOf("strength" to "strong")
            ),
            KnowledgeEdge(
                source = "1",
                target = "4",
                label = "includes",
                properties = mapOf("strength" to "strong")
            ),
            KnowledgeEdge(
                source = "1",
                target = "5",
                label = "supports",
                properties = mapOf("strength" to "strong")
            ),
            KnowledgeEdge(
                source = "2",
                target = "5",
                label = "supports",
                properties = mapOf("strength" to "medium")
            ),
            KnowledgeEdge(
                source = "3",
                target = "5",
                label = "supports",
                properties = mapOf("strength" to "medium")
            )
        )

        // 如果有搜索查询，过滤节点和边
        return if (query != null) {
            val filteredNodes = nodes.filter { 
                it.label.contains(query, ignoreCase = true) 
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

    // 实现其他必要的方法...
    override suspend fun getNotes(parentId: String?): List<Note> = emptyList()
    override suspend fun createNote(note: Note): Note = note
    override suspend fun updateNote(note: Note): Note = note
    override suspend fun deleteNote(id: String) {}
    override suspend fun uploadFile(file: ByteArray, filename: String): String = ""
    override suspend fun askQuestion(question: String): Answer = Answer("", "", "", emptyList(), 0L)
    override suspend fun getQuestionHistory(): List<Question> = emptyList()
} 