package com.addzero.web.modules.note.qa

data class Question(
    val id: String,
    val content: String,
    val createdAt: Long,
    val createdBy: String
)

data class Answer(
    val id: String,
    val questionId: String,
    val content: String,
    val sources: List<AnswerSource>,
    val createdAt: Long
)

data class AnswerSource(
    val noteId: String,
    val title: String,
    val snippet: String,
    val relevance: Double
)
