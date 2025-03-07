package com.addzero.web.ui.hooks.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.web.ui.hooks.table.entity.AddColumn

@Composable
fun <E : Any> ImageInput(
    value: String,
    onValueChange: (Any?) -> Unit,
    column: AddColumn<E>,
    error: String?
) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = { onValueChange(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(column.placeholder) },
            isError = error != null,
            singleLine = true,
            readOnly = true
        )
        Button(
            onClick = { /* 显示文件选择器 */ },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("上传文件")
        }
    }
}