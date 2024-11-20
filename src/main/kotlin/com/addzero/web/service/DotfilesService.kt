package com.addzero.web.service

import com.addzero.web.model.PageResult
import com.addzero.web.model.BizEnvVars
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class DotfilesService {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }

    private val baseUrl = "http://localhost:12344/dotfiles"

    suspend fun listDotfiles(): List<BizEnvVars> {
        val body = client.get("$baseUrl/list").body<List<BizEnvVars>>()
        return body
    }

    suspend fun addDotfile(item: BizEnvVars): BizEnvVars {
        return client.post("$baseUrl/add") {
            contentType(ContentType.Application.Json)
            setBody(item)
        }.body()
    }

    suspend fun updateDotfile(item: BizEnvVars): BizEnvVars {
        return client.post("$baseUrl/edit") {
            contentType(ContentType.Application.Json)
            setBody(item)
        }.body()
    }

    suspend fun deleteDotfile(id: String) {
        client.delete("$baseUrl/removebyid/$id")
    }

    suspend fun importDotfiles(files: List<ByteArray>): List<BizEnvVars> {
        return client.post("$baseUrl/import") {
            contentType(ContentType.MultiPart.FormData)
            setBody(MultiPartFormDataContent(
                formData {
                    files.forEachIndexed { index, bytes ->
                        append("file$index", bytes, Headers.build {
                            append(HttpHeaders.ContentDisposition, "filename=file$index.env")
                        })
                    }
                }
            ))
        }.body()
    }

    suspend fun exportDotfiles(): ByteArray {
        return client.get("$baseUrl/export").body()
    }

    suspend fun generateConfig(): ByteArray {
        return client.get("$baseUrl/generate-config").body()
    }

    suspend fun searchDotfiles(
        name: String = "",
        platforms: Set<String> = emptySet(),
        osTypes: Set<String> = emptySet(),
        page: Int = 0,
        size: Int = 10
    ): PageResult<BizEnvVars> {
        val body = client.get("$baseUrl/search") {
            url {
                parameters.append("page", page.toString())
                parameters.append("size", size.toString())
                if (name.isNotEmpty()) {
                    parameters.append("name", name)
                }
                platforms.forEach { platform ->
                    parameters.append("platforms", platform)
                }
                osTypes.forEach { osType ->
                    parameters.append("osTypes", osType)
                }
            }
        }.body<PageResult<BizEnvVars>>()
        return body
    }
}