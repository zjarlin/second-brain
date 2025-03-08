package com.addzero.web.ui.hooks.table.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.addzero.web.ui.hooks.UseHook

class UseSearch(
val onSearch: () -> Unit
) : UseHook<UseSearch> {
    var searchText by mutableStateOf("")

    override val render: @Composable
        () -> Unit
        get() = {
            Row(
                modifier = modifier,
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = {
                        searchText = it
                    },
                    modifier = Modifier.weight(1f).padding(end = 8.dp),
                    placeholder = { Text("请输入搜索关键词... 键入Enter可执行搜索") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { onSearch() })
                )
                OutlinedButton(
                    onClick = onSearch, modifier = Modifier.height(56.dp)
                ) {
                    Text("搜索")
                }
            }

        }
}

@Composable
fun SearchBar(
    searchText: String, onSearchTextChange: (String) -> Unit, onSearch: () -> Unit, modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = searchText,
            onValueChange = onSearchTextChange,
            modifier = Modifier.weight(1f).padding(end = 8.dp),
            placeholder = { Text("请输入搜索关键词... 键入Enter可执行搜索") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSearch() })
        )
        OutlinedButton(
            onClick = onSearch, modifier = Modifier.height(56.dp)
        ) {
            Text("搜索")
        }
    }
}