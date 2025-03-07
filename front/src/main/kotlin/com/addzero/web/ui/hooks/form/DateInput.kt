package com.addzero.web.ui.hooks.form

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.addzero.web.ui.hooks.table.entity.AddColumn

@Composable
fun <E : Any> DateInput(
    value: Any?,
    onValueChange: (Any?) -> Unit,
    column: AddColumn<E>,
    error: String?
) {
    OutlinedTextField(
        value = formatDate(value),
        onValueChange = { onValueChange(parseDate(it)) },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(column.placeholder) },
        isError = error != null,
        singleLine = true,
        trailingIcon = {
            IconButton(onClick = { /* 显示日期选择器 */ }) {
                Icon(Icons.Default.DateRange, contentDescription = "选择日期")
            }
        }
    )
}