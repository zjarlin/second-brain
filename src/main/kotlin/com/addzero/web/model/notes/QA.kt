package com.addzero.web.model.notes

import kotlinx.serialization.Serializable

@Serializable
data class Question(
    val id: String,
    val content: String,
    val createdAt: Long,
    val createdBy: String
)

@Serializable
data class Answer(
    val id: String,
    val questionId: String,
    val content: String,
    val sources: List<AnswerSource>,
    val createdAt: Long
)

@Serializable
data class AnswerSource(
    val noteId: String,
    val title: String,
    val snippet: String,
    val relevance: Double
) 