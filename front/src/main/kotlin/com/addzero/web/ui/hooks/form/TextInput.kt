package com.addzero.web.ui.hooks.form

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun <E : Any> TextInput(
    value: String,
    onValueChange: (Any?) -> Unit,
    column: AddColumn<E>,
    error: String?
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(column.placeholder) },
        isError = error != null,
        singleLine = true
    )
}