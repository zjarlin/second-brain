package com.addzero.web.base

import com.addzero.web.model.PageResult
import kotlinx.serialization.Serializable
import java.io.File

interface BaseService<T : @Serializable Any> {
    suspend fun page(
        params: Map<String, Any?> = emptyMap(),
        page: Int = 1,
        size: Int = 20
    ): PageResult<T>

    suspend fun save(item: Any): Int
    suspend fun update(item: Any):Int
    suspend fun delete(id: String)
    suspend fun upload(file: ByteArray, filename: String): String
    suspend fun import(files: List<File>): Any
    suspend fun export(): ByteArray
}