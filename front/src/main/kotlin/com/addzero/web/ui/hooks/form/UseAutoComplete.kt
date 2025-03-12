package com.addzero.web.ui.hooks.form

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.addzero.web.ui.hooks.UseHook

class UseAutoComplete<T>(
    val title: String,
    val options: List<T>,
    val getLabelFun: (T) -> String,
) : UseHook<UseAutoComplete<T>> {
    var inputValue by mutableStateOf("")
    var expanded by mutableStateOf(false)
    var isError by mutableStateOf(false)

    override val render: @Composable () -> Unit
        get() = {
            Box {
                OutlinedTextField(
                    value = inputValue,
                    onValueChange = { newValue ->
                        inputValue = newValue
                        expanded = true
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("请输入$title") },
                    isError = isError,
                    singleLine = true,
                    trailingIcon = {
                        IconButton(onClick = { expanded = !expanded }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = "展开选项")
                        }
                    }
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // 过滤选项
                    val filteredOptions = options.filter {
                        getLabelFun(it).contains(inputValue, ignoreCase = true)
                    }

                    filteredOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(getLabelFun(option)) },
                            onClick = {
                                inputValue = getLabelFun(option)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
}
