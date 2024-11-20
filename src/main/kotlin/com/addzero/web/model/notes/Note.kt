package com.addzero.web.model.notes

import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class Note(
    val id: String,
    val title: String,
    val content: String,
    val type: NoteType,
    val tags: List<String>,
    val parentId: String?,
    val path: String,
    val fileUrl: String?,
    val createdAt: Long,
    val updatedAt: Long,
    val createdBy: String,
    val updatedBy: String
)

enum class NoteType {
    MARKDOWN,
    PDF,
    WORD,
    EXCEL,
    TXT
} 