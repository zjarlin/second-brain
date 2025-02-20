package com.addzero.web.ui.hooks

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 筛选区Hook，用于管理筛选相关的状态和操作
 *
 * @param initialSearchText 初始搜索文本
 * @param onSearch 搜索回调函数
 * @return FilterState 包含筛选相关的状态和操作
 */
@Composable
fun useFilters(
    initialSearchText: String = "",
    onSearch: (searchText: String) -> Unit
): FilterState {
    var searchText by remember { mutableStateOf(initialSearchText) }

    val onSearchTextChange = { text: String ->
        searchText = text
    }

    val onSearchSubmit = {
        onSearch(searchText)
    }

    val renderFilters: @Composable (modifier: Modifier, filters: @Composable ColumnScope.() -> Unit, actions: @Composable RowScope.() -> Unit) -> Unit = { modifier, filters, actions ->
        Column(modifier = modifier) {
            // 搜索框和操作按钮行
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 搜索框
                OutlinedTextField(
                    value = searchText,
                    onValueChange = onSearchTextChange,
                    modifier = Modifier.weight(1f),
                    label = { Text("搜索") },
                    singleLine = true,
                    trailingIcon = {
                        IconButton(onClick = onSearchSubmit) {
                            Icon(Icons.Default.Search, contentDescription = "搜索")
                        }
                    }
                )
                
                Spacer(modifier = Modifier.width(16.dp))
                
                // 操作按钮区域
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    actions()
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            
            // 过滤器区域
            filters()
        }
    }

    return remember(searchText) {
        FilterState(
            searchText = searchText,
            onSearchTextChange = onSearchTextChange,
            onSearchSubmit = onSearchSubmit,
            renderFilters = renderFilters
        )
    }
}

/**
 * 筛选状态数据类
 */
data class FilterState(
    val searchText: String,
    val onSearchTextChange: (String) -> Unit,
    val onSearchSubmit: () -> Unit,
    val renderFilters: @Composable (modifier: Modifier, filters: @Composable ColumnScope.() -> Unit, actions: @Composable RowScope.() -> Unit) -> Unit
)