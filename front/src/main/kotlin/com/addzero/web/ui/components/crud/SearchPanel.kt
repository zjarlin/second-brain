package com.addzero.web.ui.components.crud

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 通用搜索面板组件
 * @param searchText 搜索文本
 * @param onSearchTextChange 搜索文本变化回调
 * @param onSearch 搜索按钮点击回调
 * @param modifier 修饰符
 * @param filters 过滤器插槽
 * @param actions 操作按钮插槽
 */
@Composable
fun SearchPanel(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier,
    filters: @Composable ColumnScope.() -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {}
) {
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
                    IconButton(onClick = onSearch) {
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