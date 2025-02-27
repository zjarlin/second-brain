package com.addzero.web.ui.hooks.table

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlin.reflect.KClass

/**
 * 表格状态管理类
 */
class TableState<E : Any>(
    val clazz: KClass<E>,
    initialColumns: List<AddColumn<E>> = listOf(),
    initialPageSize: Long = 10,
) {
    var columns by mutableStateOf(initialColumns)
    var dataList by mutableStateOf(listOf<E>())
    var pageNo by mutableStateOf(0L)
    var pageSize by mutableStateOf(initialPageSize)
    var totalPages by mutableStateOf(0L)
    var searchText by mutableStateOf("")

    fun addColumn(title: String, getFun: (E) -> Any?, customRender: @Composable (String) -> Unit = { Text(it) }) {
        columns = columns + AddColumn(title, getFun, customRender)
    }

    fun updateData(data: List<E>, total: Long) {
        dataList = data
        totalPages = total
    }

    fun nextPage() {
        if (pageNo < totalPages) {
            pageNo += 1
        }
    }

    fun previousPage() {
        if (pageNo > 0) {
            pageNo -= 1
        }
    }

    fun updatePageSize(size: Long) {
        pageSize = size
        pageNo = 0
    }

    fun search() {
        pageNo = 0
    }
}

