package com.addzero.web.base

import com.addzero.web.model.PageResult
import kotlinx.serialization.Serializable

interface BaseService<T : @Serializable Any> {
    suspend fun page(
        params: Map<String, Any?> = emptyMap(),
        page: Int = 0,
        size: Int = 20
    ): PageResult<T>

    suspend fun save(item: Any): Int
    suspend fun update(item: Any):Int
    suspend fun delete(id: String)
    suspend fun upload(file: ByteArray, filename: String): String
    suspend fun uploadBatch(files: List<ByteArray>): List<T>
}