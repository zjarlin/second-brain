package com.addzero.web.ui.hooks.form

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.common.kt_util.getMetadata
import com.addzero.common.kt_util.toNotBlankStr

@Composable
inline fun <reified E : Any> DynamicForm(
    item: E,
    crossinline onSave: (E) -> Unit,
    noinline onCancel: () -> Unit
) {
    val clazz = E::class
    val metadata = clazz.getMetadata()

    Column(modifier = Modifier.padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            // 左列
            Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                metadata.fields.filterIndexed { index, _ -> index % 2 == 0 }.forEach { field ->
                    val description = field.description.toNotBlankStr()
                    if (description.isNotBlank()) {
                        val property = field.property
                        val value = property.getter.call(item)?.toString() ?: ""

                        OutlinedTextField(
                            value = value,
                            onValueChange = { /* TODO: 实现字段值更新逻辑 */ },
                            label = { Text(description) },
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                        )
                    }
                }
            }

            // 右列
            Column(modifier = Modifier.weight(1f).padding(start = 8.dp)) {
                metadata.fields.filterIndexed { index, _ -> index % 2 == 1 }.forEach { field ->
                    val description = field.description.toNotBlankStr()
                    if (description.isNotBlank()) {
                        val property = field.property
                        val value = property.getter.call(item)?.toString() ?: ""

                        OutlinedTextField(
                            value = value,
                            onValueChange = { /* TODO: 实现字段值更新逻辑 */ },
                            label = { Text(description) },
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onCancel) {
                Text("取消")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { onSave(item) }) {
                Text("保存")
            }
        }
    }
}
