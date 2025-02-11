package com.addzero.web.modules.note.notes.treenote

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun MarkdownEditor(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isPreview by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        // 工具栏
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                TextButton(onClick = { isPreview = false }) {
                    Text("编辑")
                }
                TextButton(onClick = { isPreview = true }) {
                    Text("预览")
                }
            }
        }

        // 编辑器/预览区
        if (isPreview) {
            // TODO: 添加Markdown渲染
            Text(value)
        } else {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxSize(),
                textStyle = TextStyle(
                    fontFamily = MaterialTheme.typography.bodyLarge.fontFamily
                )
            )
        }
    }
}