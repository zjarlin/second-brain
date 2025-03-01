package com.addzero.web.ui.hooks.table.common

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.web.ui.hooks.UseHook

/**
 *搜索区 Hook
 */
data class UseSearch(
    val onSearch: () -> Unit  ,
) : UseHook<UseSearch> {
    /**
     * 搜索框里的文字
     */
    var searchText by mutableStateOf("")

    override val render: @Composable () -> Unit
        get() = {
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
                    onClick = onSearch, modifier = Modifier.height(56.dp)
                ) {
                    Text("搜索")
                }
            }

        }


}

