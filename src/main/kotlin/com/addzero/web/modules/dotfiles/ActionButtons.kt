package com.addzero.web.modules.dotfiles

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

@Composable
internal fun ActionButtons(
    scope: CoroutineScope,
    viewModel: DotfilesViewModel,
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(onClick = { /* 显示添加对话框 */ }) {
            Text("添加")
        }

        Button(onClick = {
            scope.launch {
                val fileChooser = JFileChooser().apply {
                    fileFilter = FileNameExtensionFilter("环境变量文件", "env", "sh")
                    isMultiSelectionEnabled = true
                }

                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    val files = fileChooser.selectedFiles.map { it.readBytes() }
                    viewModel.importDotfiles(files)
                }
            }
        }) {
            Text("导入")
        }

        Button(onClick = {
            scope.launch {
                viewModel.exportDotfiles()?.let { bytes ->
                    val fileChooser = JFileChooser().apply {
                        fileFilter = FileNameExtensionFilter("环境变量文件", "env")
                        selectedFile = File("dotfiles_export.env")
                    }

                    if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                        fileChooser.selectedFile.writeBytes(bytes)
                    }
                }
            }
        }) {
            Text("导出")
        }

        Button(onClick = {
            scope.launch {
                viewModel.generateConfig()?.let { bytes ->
                    val fileChooser = JFileChooser().apply {
                        fileFilter = FileNameExtensionFilter("Shell 脚本", "sh")
                        selectedFile = File("env_config.sh")
                    }

                    if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                        fileChooser.selectedFile.writeBytes(bytes)
                    }
                }
            }
        }) {
            Text("生成配置")
        }
    }
}