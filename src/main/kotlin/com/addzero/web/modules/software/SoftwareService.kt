package com.addzero.web.modules.software

import com.addzero.web.model.PageResult
import com.addzero.web.model.enums.OsType
import com.addzero.web.model.enums.PlatformType
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class SoftwareService {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }

    private val baseUrl = "http://localhost:12344/software"

    suspend fun searchSoftware(
        keyword: String = "",
        platform: PlatformType? = null,
        osTypes: List<OsType>? = null,
        useAI: Boolean = false,
        page: Int = 0,
        size: Int = 10
    ): PageResult<Software> {
        return client.get("$baseUrl/search") {
            url {
                parameters.append("page", page.toString())
                parameters.append("size", size.toString())
                if (keyword.isNotEmpty()) {
                    parameters.append("keyword", keyword)
                }
                platform?.let {
                    parameters.append("platform", it.name)
                }
                osTypes?.forEach { osType ->
                    parameters.append("osTypes", osType.name)
                }
                parameters.append("useAI", useAI.toString())
            }
        }.body()
    }

    suspend fun downloadSoftware(id: String): ByteArray {
        return client.get("$baseUrl/download/$id").body()
    }

    suspend fun getSoftwareDetails(id: String): Software {
        return client.get("$baseUrl/details/$id").body()
    }
}