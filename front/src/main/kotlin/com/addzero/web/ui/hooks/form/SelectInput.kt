package com.addzero.web.ui.hooks.form

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.addzero.web.ui.hooks.table.entity.IAddColumn

@Composable
fun <E : Any> SelectInput(
    value: String,
    onValueChange: (Any?) -> Unit,
    column: IAddColumn<E>,
    error: String?
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        OutlinedTextField(
            value = value,
            onValueChange = { onValueChange(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(column.placeholder) },
            isError = error != null,
            singleLine = true,
            readOnly = true,
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
            column.options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.second) },
                    onClick = {
                        onValueChange(option.first)
                        expanded = false
                    }
                )
            }
        }
    }
}