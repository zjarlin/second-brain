package com.addzero.web.service

import com.addzero.web.model.PageResult
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
import com.intellij.database.dataSource.DatabaseConnection
import com.intellij.database.dataSource.DatabaseConnectionPoint
import com.intellij.database.psi.DbPsiFacade
import com.intellij.database.util.DbImplUtil
import com.intellij.openapi.project.Project

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
    override suspend fun <T : Any> getNotes(parentId: String?, page: Int, size: Int): PageResult<T> {
        return client.get("$baseUrl/list") {
            url {
//                parentId?.let { parameters.append("parentId", it) }
                parameters.append("page", page.toString())
                parameters.append("size", size.toString())
            }
        }.body()
    }

//    override suspend fun getNotes(
//        parentId: String?,
//        page: Int,
//        size: Int
//    ): PageResult<Note> {
//        return client.get("$baseUrl/list") {
//            url {
//                parentId?.let { parameters.append("parentId", it) }
//                parameters.append("page", page.toString())
//                parameters.append("size", size.toString())
//            }
//        }.body()
//    }

    override suspend fun createNote(note: Note): Note {
        return client.post("$baseUrl/create") {
            contentType(ContentType.Application.Json)
            setBody(note)
        }.body()
    }

    override suspend fun updateNote(note: Note): Note {
        return client.put("$baseUrl/update") {
            contentType(ContentType.Application.Json)
            setBody(note)
        }.body()
    }

    override suspend fun deleteNote(id: String) {
        client.delete("$baseUrl/delete/$id")
    }

    override suspend fun uploadFile(file: ByteArray, filename: String): String {
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

object SqlExecutor {
    /**
     * 执行SQL查询
     */
    fun executeQuery(
        project: Project,
        dataSource: DatabaseConnectionPoint,
        sql: String
    ): List<Map<String, Any?>> {
        val connection = getConnection(dataSource)
        return try {
            connection.executeQuery(sql) { resultSet ->
                val results = mutableListOf<Map<String, Any?>>()
                val metaData = resultSet.metaData
                val columnCount = metaData.columnCount

                while (resultSet.next()) {
                    val row = mutableMapOf<String, Any?>()
                    for (i in 1..columnCount) {
                        val columnName = metaData.getColumnName(i)
                        val value = resultSet.getObject(i)
                        row[columnName] = value
                    }
                    results.add(row)
                }
                results
            }
        } finally {
            connection.close()
        }
    }

    /**
     * 执行更新SQL（INSERT, UPDATE, DELETE等）
     */
    fun executeUpdate(
        project: Project,
        dataSource: DatabaseConnectionPoint,
        sql: String
    ): Int {
        val connection = getConnection(dataSource)
        return try {
            connection.executeUpdate(sql)
        } finally {
            connection.close()
        }
    }

    /**
     * 获取数据库连接
     */
    private fun getConnection(dataSource: DatabaseConnectionPoint): DatabaseConnection {
        return DbImplUtil.getConnection(dataSource)
            ?: throw IllegalStateException("无法获取数据库连接")
    }

    /**
     * 获取当前选中的数据源
     */
    fun getCurrentDataSource(project: Project): DatabaseConnectionPoint? {
        return DbPsiFacade.getInstance(project).dataSources.firstOrNull()
    }
}