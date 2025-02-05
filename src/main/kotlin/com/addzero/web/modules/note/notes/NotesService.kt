package com.addzero.web.modules.note.notes

import com.addzero.web.base.BaseServiceImpl
import com.addzero.web.modules.note.knowlagegraph.KnowledgeGraph
import com.addzero.web.modules.note.qa.Answer
import com.addzero.web.modules.note.qa.Question
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class NotesService : BaseServiceImpl<Note>() {
     override val restPath: String
        get() = "/note"


     suspend fun askQuestion(question: String): Answer {
        return client.post("$thisUrl/qa/ask") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("question" to question))
        }.body()
    }

     suspend fun getQuestionHistory(): List<Question> {
        return client.get("$thisUrl/qa/history").body()
    }

     suspend fun getKnowledgeGraph(query: String?): KnowledgeGraph {
        return client.get("$thisUrl/graph") {
            url { query?.let { parameters.append("query", it) } }
        }.body()


    }


}