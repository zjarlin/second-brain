package com.addzero.web.ui.hooks.table.common

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 搜索区组件的函数式实现
 * @param onSearch 搜索按钮点击回调
 * @param modifier 修饰符
 * @param initialSearchText 初始搜索文本
 * @return 当前搜索文本
 */
@Composable
fun SearchHook(
    onSearch: () -> Unit,
    modifier: Modifier = Modifier,
    initialSearchText: String = ""
): String {
    // 使用remember管理搜索文本状态
    var searchText by remember { mutableStateOf(initialSearchText) }
    
    Row(
        modifier = modifier.fillMaxWidth().padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            modifier = Modifier.weight(1f).padding(end = 8.dp),
            placeholder = { Text("请输入搜索关键词") },
            singleLine = true
        )
        Button(
            onClick = onSearch, 
            modifier = Modifier.height(56.dp)
        ) {
            Text("搜索")
        }
    }
    
    return searchText
}