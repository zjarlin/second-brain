package com.addzero.web.modules.note.notes

import com.addzero.web.model.PageResult
import com.addzero.web.modules.note.knowlagegraph.KnowledgeGraph
import com.addzero.web.modules.note.qa.Answer
import com.addzero.web.modules.note.qa.Question
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class NotesServiceImpl : NotesService {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }





    override suspend fun askQuestion(question: String): Answer {
        return client.post("$baseUrl/qa/ask") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("question" to question))
        }.body()
    }

    override suspend fun getQuestionHistory(): List<Question> {
        return client.get("$baseUrl/qa/history").body()
    }

    override suspend fun getKnowledgeGraph(query: String?): KnowledgeGraph {
        return client.get("$baseUrl/graph") {
            url {
                query?.let { parameters.append("query", it) }
            }
        }.body()
    }
}

class ApiNotesService : NotesService {
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


    override suspend fun askQuestion(question: String): Answer {
        return client.post("$baseUrl/qa/ask") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("question" to question))
        }.body()
    }

    override suspend fun getQuestionHistory(): List<Question> {
        return client.get("$baseUrl/qa/history").body()
    }

    override suspend fun getKnowledgeGraph(query: String?): KnowledgeGraph {
        return client.get("$baseUrl/graph") {
            url {
                query?.let { parameters.append("query", it) }
            }
        }.body()
    }
}