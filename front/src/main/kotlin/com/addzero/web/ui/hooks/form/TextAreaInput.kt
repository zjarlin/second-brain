package com.addzero.web.ui.hooks.form

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.web.ui.hooks.table.entity.AddColumn

@Composable
fun <E : Any> TextAreaInput(
    value: String,
    onValueChange: (Any?) -> Unit,
    column: AddColumn<E>,
    error: String?
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        modifier = Modifier.fillMaxWidth().height(120.dp),
        placeholder = { Text(column.placeholder) },
        isError = error != null,
        maxLines = 5
    )
}