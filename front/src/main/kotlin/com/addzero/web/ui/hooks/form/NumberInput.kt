package com.addzero.web.ui.hooks.form

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.addzero.web.ui.hooks.table.entity.AddColumn

@Composable
fun <E : Any> NumberInput(
    value: String,
    onValueChange: (Any?) -> Unit,
    column: AddColumn<E>,
    error: String?
) {
    OutlinedTextField(
        value = value,
        onValueChange = {
            val number = it.toDoubleOrNull()
            if (number != null || it.isEmpty()) {
                onValueChange(number)
            }
        },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(column.placeholder) },
        isError = error != null,
        singleLine = true
    )
}