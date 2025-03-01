package com.addzero.web.ui.hooks.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.web.ui.hooks.UseHook
import java.io.File
import javax.swing.JFileChooser
import javax.swing.SwingUtilities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 上传对话框Hook
 * @param [onDismiss] 关闭对话框回调
 * @param [onUpload] 上传文件回调
 * @param [dialogTitle] 对话框标题
 * @param [selectButtonText] 选择文件按钮文本
 * @param [uploadButtonText] 上传按钮文本
 * @param [cancelButtonText] 取消按钮文本
 */
class UseUploadDialog(
    private val onDismiss: () -> Unit = {},
    private val onUpload: (ByteArray, String) -> Unit = { _, _ -> },
    private val dialogTitle: String = "上传文档",
    private val selectButtonText: String = "选择文件",
    private val selectingText: String = "选择中...",
    private val uploadButtonText: String = "上传",
    private val cancelButtonText: String = "取消"
) : UseHook<UseUploadDialog> {

    var showFlag by mutableStateOf(false)
    var selectedFile by mutableStateOf<File?>(null)
    var isSelectingFile by mutableStateOf(false)

    override val render: @Composable () -> Unit = {
        if (showFlag) {
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
                title = { Text(dialogTitle) },
                text = {
                    Column {
                        Button(
                            onClick = { isSelectingFile = true },
                            enabled = !isSelectingFile
                        ) {
                            Text(if (isSelectingFile) selectingText else selectButtonText)
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
                                showFlag = false
                                selectedFile = null
                            }
                        },
                        enabled = selectedFile != null
                    ) {
                        Text(uploadButtonText)
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        onDismiss()
                        showFlag = false
                        selectedFile = null
                    }) {
                        Text(cancelButtonText)
                    }
                }
            )
        }
    }

    /**
     * 打开上传对话框
     */
    fun open() {
        showFlag = true
    }

    /**
     * 关闭上传对话框
     */
    fun close() {
        showFlag = false
        selectedFile = null
    }
}

/**
 * 创建上传对话框Hook的便捷函数
 */
@Composable
fun useUploadDialog(
    onDismiss: () -> Unit = {},
    onUpload: (ByteArray, String) -> Unit = { _, _ -> },
    dialogTitle: String = "上传文档",
    selectButtonText: String = "选择文件",
    selectingText: String = "选择中...",
    uploadButtonText: String = "上传",
    cancelButtonText: String = "取消"
): UseUploadDialog {
    return remember {
        UseUploadDialog(
            onDismiss = onDismiss,
            onUpload = onUpload,
            dialogTitle = dialogTitle,
            selectButtonText = selectButtonText,
            selectingText = selectingText,
            uploadButtonText = uploadButtonText,
            cancelButtonText = cancelButtonText
        )
    }.getState()
}