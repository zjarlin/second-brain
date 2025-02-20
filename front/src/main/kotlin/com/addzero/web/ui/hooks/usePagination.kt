package com.addzero.web.ui.hooks

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 分页Hook，用于管理分页相关的状态和操作
 *
 * @param initialPageSize 初始每页显示数量
 * @param onPageChange 页码变化时的回调函数
 * @return PaginationState 包含分页相关的状态和操作，以及分页组件的渲染函数
 */
@Composable
fun usePagination(
    initialPageSize: Int = 10,
    onPageChange: (pageIndex: Int, pageSize: Int) -> Unit
): PaginationState {
    var pageSize by remember { mutableStateOf(initialPageSize) }
    var pageIndex by remember { mutableStateOf(0) }
    var expanded by remember { mutableStateOf(false) }
    val pageSizes = listOf(10, 20, 50, 100)

    val onLast = {
        if (pageIndex > 0) {
            pageIndex--
            onPageChange(pageIndex, pageSize)
        }
    }

    val onNext = {
        pageIndex++
        onPageChange(pageIndex, pageSize)
    }

    val onChangePageSize = { newSize: Int ->
        pageSize = newSize
        pageIndex = 0
        onPageChange(pageIndex, pageSize)
    }

    val renderPagination: @Composable (totalPages: Int, totalElements: Long) -> Unit = { totalPages, totalElements ->
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onLast,
                enabled = pageIndex > 0
            ) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, "上一页")
            }

            Text(
                "${pageIndex + 1}/$totalPages",
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            IconButton(
                onClick = onNext,
                enabled = pageIndex < totalPages - 1
            ) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, "下一页")
            }

            Text(
                "共 $totalElements 条",
                modifier = Modifier.padding(start = 16.dp)
            )

            Box(modifier = Modifier.padding(start = 16.dp)) {
                OutlinedButton(onClick = { expanded = true }) {
                    Text("${pageSize}条/页")
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    pageSizes.forEach { size ->
                        DropdownMenuItem(
                            text = { Text("${size}条/页") },
                            onClick = {
                                onChangePageSize(size)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }

    return remember(pageSize, pageIndex) {
        PaginationState(
            pageSize = pageSize,
            pageIndex = pageIndex,
            onLast = onLast,
            onNext = onNext,
            onChangePageSize = onChangePageSize,
            renderPagination = renderPagination
        )
    }
}

/**
 * 分页状态数据类
 */
data class PaginationState(
    val pageSize: Int,
    val pageIndex: Int,
    val onLast: () -> Unit,
    val onNext: () -> Unit,
    val onChangePageSize: (Int) -> Unit,
    val renderPagination: @Composable (totalPages: Int, totalElements: Long) -> Unit
)
