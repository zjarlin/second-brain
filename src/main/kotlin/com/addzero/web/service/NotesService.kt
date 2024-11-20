package com.addzero.web.service

import com.addzero.web.model.notes.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class NotesService {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }

    private val baseUrl = "http://localhost:12344/notes"

    // 知识库管理
    suspend fun getNotes(parentId: String? = null): List<Note> {
        return client.get("$baseUrl/list") {
            url {
                parentId?.let { parameters.append("parentId", it) }
            }
        }.body()
    }

    suspend fun createNote(note: Note): Note {
        return client.post("$baseUrl/create") {
            contentType(ContentType.Application.Json)
            setBody(note)
        }.body()
    }

    suspend fun updateNote(note: Note): Note {
        return client.put("$baseUrl/update") {
            contentType(ContentType.Application.Json)
            setBody(note)
        }.body()
    }

    suspend fun deleteNote(id: String) {
        client.delete("$baseUrl/delete/$id")
    }

    suspend fun uploadFile(file: ByteArray, filename: String): String {
        return client.post("$baseUrl/upload") {
            setBody(MultiPartFormDataContent(
                formData {
                    append("file", file, Headers.build {
                        append(HttpHeaders.ContentDisposition, "filename=$filename")
                    })
                }
            ))
        }.body()
    }

    // RAG 问答
    suspend fun askQuestion(question: String): Answer {
        return client.post("$baseUrl/qa/ask") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("question" to question))
        }.body()
    }

    suspend fun getQuestionHistory(): List<Question> {
        return client.get("$baseUrl/qa/history").body()
    }

    // 知识图谱
    suspend fun getKnowledgeGraph(query: String? = null): KnowledgeGraph {
        return client.get("$baseUrl/graph") {
            url {
                query?.let { parameters.append("query", it) }
            }
        }.body()
    }
} 