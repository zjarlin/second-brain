package com.addzero.web.notes

import com.addzero.web.base.BaseServiceImpl
import com.addzero.web.notes.model.*
import io.ktor.client.request.forms.*
import io.ktor.http.*

class ApiNotesService : BaseServiceImpl<Note>(Note::class), NotesService {
    override val restPath = "/notes"

    override suspend fun upload(file: ByteArray, filename: String): String {
        return client.post("$thisUrl/upload") {
            setBody(MultiPartFormDataContent(
                formData {
                    append("file", file, Headers.build {
                        append(HttpHeaders.ContentDisposition, "filename=$filename")
                    })
                }
            ))
        }.body()
    }

    override suspend fun askQuestion(question: String): Answer {
        return postRequest("qa/ask", mapOf("question" to question))
    }

    override suspend fun getQuestionHistory(): List<Question> {
        return getRequest("qa/history")
    }

    override suspend fun getKnowledgeGraph(query: String?): KnowledgeGraph {
        return getRequest("graph", mapOf("query" to query))
    }
}