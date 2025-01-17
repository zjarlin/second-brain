package com.addzero.web.base

import androidx.compose.runtime.*
import com.addzero.web.model.PageResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable

abstract class BaseViewModel<T : @Serializable Any, S : BaseService<T>>(
    protected val service: S
) {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    var items by mutableStateOf<List<T>>(emptyList())

    var currentItem by mutableStateOf<T?>(null)

    var isLoading by mutableStateOf(false)
        protected set

    var error by mutableStateOf<String?>(null)
        protected set

    var currentPage by mutableStateOf(0)

    var pageSize by mutableStateOf(20)

    var totalPages by mutableStateOf(0)

    var totalElements by mutableStateOf(0L)

    private suspend fun executeWithLoading(block: suspend () -> Unit) {
        isLoading = true
        error = null
        try {
            block()
        } catch (e: Exception) {
            error = "操作失败: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    // 新增的DSL函数
    fun executeWithLoadingInDsl(block: suspend CoroutineScope.() -> Unit) {
        coroutineScope.launch {
            executeWithLoading {
                block()
            }
        }
    }

    fun loadItems(params: Map<String, Any?> = emptyMap()) {
        executeWithLoadingInDsl {
            val result = service.page(params = params, page = currentPage, size = pageSize)
            items = result.content
            totalPages = result.totalPages
            totalElements = result.totalElements
            currentPage = result.pageNumber
            pageSize = result.pageSize
        }
    }

    fun createItem(item: T) {
        executeWithLoadingInDsl {
            service.save(item)
            loadItems()
        }
    }

    fun updateItem(item: T) {
        executeWithLoadingInDsl {
            service.update(item)
            loadItems()
        }
    }

    fun deleteItem(id: String) {
        executeWithLoadingInDsl {
            service.delete(id)
            loadItems()
        }
    }

    fun changePage(newPage: Int) {
        if (newPage in 0 until totalPages) {
            currentPage = newPage
            loadItems()
        }
    }

    fun changePageSize(newSize: Int) {
        if (newSize > 0) {
            pageSize = newSize
            currentPage = 0
            loadItems()
        }
    }

    protected open fun handlePageResult(result: PageResult<T>) {
        items = result.content
        totalPages = result.totalPages
        totalElements = result.totalElements
        currentPage = result.pageNumber
        pageSize = result.pageSize
    }
}