package com.addzero.web.service

import com.addzero.web.model.notes.*

interface NotesService {
    suspend fun getNotes(parentId: String? = null): List<Note>
    suspend fun createNote(note: Note): Note
    suspend fun updateNote(note: Note): Note
    suspend fun deleteNote(id: String)
    suspend fun uploadFile(file: ByteArray, filename: String): String
    suspend fun askQuestion(question: String): Answer
    suspend fun getQuestionHistory(): List<Question>
    suspend fun getKnowledgeGraph(query: String? = null): KnowledgeGraph
} 