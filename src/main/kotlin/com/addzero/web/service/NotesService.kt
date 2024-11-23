package com.addzero.web.service

import com.addzero.web.model.PageResult
import com.addzero.web.model.notes.*
import kotlinx.serialization.Serializable

interface NotesService {
    suspend fun <T: @Serializable Any> getNotes(
        parentId: String? = null,
        page: Int = 0,
        size: Int = 20
    ): PageResult<T>
    suspend fun createNote(note: Note): Note
    suspend fun updateNote(note: Note): Note
    suspend fun deleteNote(id: String)
    suspend fun uploadFile(file: ByteArray, filename: String): String
    suspend fun askQuestion(question: String): Answer
    suspend fun getQuestionHistory(): List<Question>
    suspend fun getKnowledgeGraph(query: String? = null): KnowledgeGraph
} 