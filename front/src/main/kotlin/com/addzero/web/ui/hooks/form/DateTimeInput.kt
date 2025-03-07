package com.addzero.web.ui.hooks.form

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.addzero.web.ui.hooks.table.entity.AddColumn

@Composable
fun <E : Any> DateTimeInput(
    value: Any?,
    onValueChange: (Any?) -> Unit,
    column: AddColumn<E>,
    error: String?
) {
    OutlinedTextField(
        value = formatDateTime(value),
        onValueChange = { onValueChange(parseDateTime(it)) },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(column.placeholder) },
        isError = error != null,
        singleLine = true,
        trailingIcon = {
            IconButton(onClick = { /* 显示日期时间选择器 */ }) {
                Icon(Icons.Default.Schedule, contentDescription = "选择日期和时间")
            }
        }
    )
}