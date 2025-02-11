package com.addzero.web.base

import com.addzero.web.infra.jimmer.base.pagefactory.PageResult

import java.io.File

interface BaseService<T :  Any> {
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