package com.addzero.web.modules.note.notes

import kotlinx.serialization.Serializable

@Serializable
data class Note(
    val id: String,
    val title: String,
    val content: String,
    val type: NoteType,
    val tags: List<String>,
    val children: List<Note>? = null,
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