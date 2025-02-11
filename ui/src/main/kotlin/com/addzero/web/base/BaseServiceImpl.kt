package com.addzero.web.base

import com.addzero.web.config.AppConfig
import com.addzero.web.infra.jimmer.base.pagefactory.PageResult
import com.alibaba.fastjson2.JSON
import com.alibaba.fastjson2.TypeReference
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*

import kotlinx.serialization.json.Json
import java.io.File

private const val pageRestUtl = "/page"

private const val saveUrl = "/save"
private const val deleteUrl = "/delete"

private const val updateUrl = "/update"

private const val listAllUrl = "/listAll"

private const val PARENT_ID = "parentId"

private const val importUrl = "/import"
private const val exportUrl = "/export"

private const val importSimgleUrl = "/upload"

abstract class BaseServiceImpl<T : Any> : BaseService<T> {
//    private val type: Type = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0]

    protected val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }

    protected abstract val restPath: String


    protected val thisUrl: String get() = "${AppConfig.API_BASE_URL}$restPath"


    suspend fun listAll(): List<T> {
        val body = client.get("$thisUrl$listAllUrl").body<List<T>>()
        return body
    }

    override suspend fun page(
        params: Map<String, Any?>,
        page: Int,
        size: Int,
    ): PageResult<T> {
        val get = client.get("$thisUrl$pageRestUtl") {
            url {
                params[PARENT_ID]?.let { parameters.append(PARENT_ID, it.toString()) }
                parameters.append("page", page.toString())
                parameters.append("size", size.toString())
            }
        }
//        val body = get.body<PageResult<T>>()

        val responseBody = get.bodyAsText()
//        // 手动反序列化 JSON 字符串为对象
//        val result  : PageResult<BizEnvVars> = Json.decodeFromString(responseBody)
        val typeReference = object : TypeReference<PageResult<T>>() {}
        val parseObject = JSON.parseObject(responseBody, typeReference)
        return parseObject

//        return body
    }

    override suspend fun save(item: Any): Int {
        val body = client.post("$thisUrl$saveUrl") {
            contentType(ContentType.Application.Json)
            setBody(item)
        }.body<Int>()
        return body
    }

    override suspend fun update(item: Any): Int {
        val body = client.put("$thisUrl$updateUrl") {
            contentType(ContentType.Application.Json)
            setBody(item)
        }.body<Int>()
        return body
    }

    override suspend fun delete(id: String) {
        client.delete("$thisUrl$deleteUrl/$id")
    }

    override suspend fun upload(file: ByteArray, filename: String): String {
        val body = client.post("$thisUrl$importSimgleUrl") {
            val parts = formData {
                append("file", file, Headers.build {
                    append(HttpHeaders.ContentDisposition, "filename=$filename")
                })
            }
            setBody(MultiPartFormDataContent(parts))
        }.body<String>()
        return body
    }


    override suspend fun import(files: List<File>): Any {

        val response = client.submitFormWithBinaryData(
            url = "$thisUrl$importUrl",
            formData = formData {
                files.forEach { file ->
                    append("file", file.readBytes(), Headers.build {
                        append(HttpHeaders.ContentDisposition, "filename=${file.name}")
                    })
                }
            }
        )
        val message = response.bodyAsText()
        return message
    }

    override suspend fun export(): ByteArray {
        return client.get("$thisUrl$exportUrl").body()
    }


}
