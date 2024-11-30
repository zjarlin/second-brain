package com.addzero.web.modules.note.notes

import com.addzero.web.base.BaseService
import com.addzero.web.modules.note.knowlagegraph.KnowledgeGraph
import com.addzero.web.modules.note.qa.Answer
import com.addzero.web.modules.note.qa.Question


interface NotesService : BaseService<Note> {
    suspend fun deleteNote(id: String)
    suspend fun askQuestion(question: String): Answer
    suspend fun getQuestionHistory(): List<Question>
    suspend fun getKnowledgeGraph(query: String? = null): KnowledgeGraph
    suspend fun uploadFile(file: ByteArray, filename: String): String
}