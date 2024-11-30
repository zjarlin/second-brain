package com.addzero.web.base

import androidx.compose.runtime.*
import com.addzero.web.model.PageResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

abstract class BaseViewModel<T : @Serializable Any, S : BaseService<T>>(
    protected val service: S
) {
    protected val coroutineScope = CoroutineScope(Dispatchers.IO)

    var items by mutableStateOf<List<T>>(emptyList())
        private set

    var currentItem by mutableStateOf<T?>(null)
        protected set

    var isLoading by mutableStateOf(false)
        protected set

    var error by mutableStateOf<String?>(null)
        protected set

    var currentPage by mutableStateOf(0)
        private set

    var pageSize by mutableStateOf(20)
        private set

    var totalPages by mutableStateOf(0)
        private set

    var totalElements by mutableStateOf(0L)
        private set

    fun loadItems(params: Map<String, Any?> = emptyMap()) {
        coroutineScope.launch {
            try {
                isLoading = true
                error = null
                val result = service.page(
                    params = params,
                    page = currentPage,
                    size = pageSize
                )
                handlePageResult(result)
            } catch (e: Exception) {
                error = "加载失败: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun createItem(item: T) {
        coroutineScope.launch {
            try {
                isLoading = true
                error = null
                service.save(item)
                loadItems()
            } catch (e: Exception) {
                error = "创建失败: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun updateItem(item: T) {
        coroutineScope.launch {
            try {
                isLoading = true
                error = null
                service.update(item)
                loadItems()
            } catch (e: Exception) {
                error = "更新失败: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun deleteItem(id: String) {
        coroutineScope.launch {
            try {
                isLoading = true
                error = null
                service.delete(id)
                loadItems()
            } catch (e: Exception) {
                error = "删除失败: ${e.message}"
            } finally {
                isLoading = false
            }
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