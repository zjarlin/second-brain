package com.addzero.web.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PageResult<T>(
    @SerialName("content")
    val content: List<T> = emptyList(),
    
    @SerialName("totalElements")
    val totalElements: Long = 0L,
    
    @SerialName("totalPages")
    val totalPages: Int = 0,
    
    @SerialName("pageNumber")
    val pageNumber: Int = 0,
    
    @SerialName("pageSize")
    val pageSize: Int = 20,
    
    @SerialName("isFirst")
    val isFirst: Boolean = true,
    
    @SerialName("isLast")
    val isLast: Boolean = true
) {
    companion object {
        fun <T> empty(pageSize: Int = 20) = PageResult<T>(
            content = emptyList(),
            totalElements = 0,
            totalPages = 0,
            pageNumber = 0,
            pageSize = pageSize,
            isFirst = true,
            isLast = true
        )
    }
}