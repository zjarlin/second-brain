package com.addzero.web.ui.components.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.swing.JFileChooser
import javax.swing.SwingUtilities

@Composable
fun UploadDialog(
    onDismiss: () -> Unit,
    onUpload: (ByteArray, String) -> Unit
) {
    var selectedFile by remember { mutableStateOf<File?>(null) }
    var isSelectingFile by remember { mutableStateOf(false) }

    LaunchedEffect(isSelectingFile) {
        if (isSelectingFile) {
            withContext(Dispatchers.IO) {
                val fileChooser = JFileChooser().apply {
                    dialogTitle = "选择文件"
                    fileSelectionMode = JFileChooser.FILES_ONLY
                }

                SwingUtilities.invokeLater {
                    val result = fileChooser.showOpenDialog(null)
                    if (result == JFileChooser.APPROVE_OPTION) {
                        selectedFile = fileChooser.selectedFile
                    }
                    isSelectingFile = false
                }
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("上传文档") },
        text = {
            Column {
                Button(
                    onClick = { isSelectingFile = true },
                    enabled = !isSelectingFile
                ) {
                    Text(if (isSelectingFile) "选择中..." else "选择文件")
                }

                selectedFile?.let {
                    Text(
                        "已选择: ${it.name}",
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    selectedFile?.let {
                        onUpload(it.readBytes(), it.name)
                    }
                },
                enabled = selectedFile != null
            ) {
                Text("上传")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}
