package com.addzero.web.base

import com.addzero.web.model.PageResult
import kotlinx.serialization.Serializable

interface BaseService<T : @Serializable Any> {
    suspend fun getItems(
        params: Map<String, Any?> = emptyMap(),
        page: Int = 0,
        size: Int = 20
    ): PageResult<T>

    suspend fun createItem(item: T): T
    suspend fun updateItem(item: T): T
    suspend fun deleteItem(id: String)
}