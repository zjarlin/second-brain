package com.addzero.web.base

import androidx.compose.runtime.*
import com.addzero.web.model.PageResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class CrudViewModel<T>(
    protected val coroutineScope: CoroutineScope
) {
    var pageResult by mutableStateOf<PageResult<T>?>(null)
        protected set

    var isLoading by mutableStateOf(false)
        protected set

    var error by mutableStateOf<String?>(null)
        protected set

    var currentPage by mutableStateOf(0)
        protected set

    var pageSize by mutableStateOf(10)
        protected set

    init {
        search()
    }

    abstract suspend fun fetchData(page: Int, size: Int): PageResult<T>

    fun search(page: Int = 0, size: Int = pageSize) {
        coroutineScope.launch {
            try {
                isLoading = true
                error = null
                pageResult = fetchData(page, size)
                currentPage = page
            } catch (e: Exception) {
                error = "加载失败: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun nextPage() {
        pageResult?.let {
            if (!it.isLast) {
                search(currentPage + 1)
            }
        }
    }

    fun previousPage() {
        if (currentPage > 0) {
            search(currentPage - 1)
        }
    }
}