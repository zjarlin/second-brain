package com.addzero.web.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import java.awt.FileDialog
import java.awt.Frame
import java.io.File

/**
 * @param [onDismiss]
 * @param [onUpload]
 */
@Composable
fun UploadDialog(
    onDismiss: () -> Unit,
    onUpload: (ByteArray, String) -> Unit,
) {
    var selectedFile by remember { mutableStateOf<File?>(null) }

    AlertDialog(onDismissRequest = onDismiss, title = { Text("上传文档") }, text = {
        Column {
            Button(onClick = {
                val fileDialog = FileDialog(Frame())
                fileDialog.isVisible = true
                fileDialog.file?.let { filename ->
                    val file = File(fileDialog.directory, filename)
                    selectedFile = file
                }
            }) {
                Text("选择文件")
            }

            selectedFile?.let {
                Text("已选择: ${it.name}")
            }
        }
    }, confirmButton = {
        Button(
            onClick = {
                selectedFile?.let {
                    onUpload(it.readBytes(), it.name)
                }
            }, enabled = selectedFile != null
        ) {
            Text("上传")
        }
    }, dismissButton = {
        TextButton(onClick = onDismiss) {
            Text("取消")
        }
    })
}