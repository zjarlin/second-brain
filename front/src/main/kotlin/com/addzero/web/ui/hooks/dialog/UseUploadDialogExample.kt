package com.addzero.web.ui.hooks.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * UseUploadDialog使用示例
 */
@Composable
fun UseUploadDialogExample() {
    // 记录上传状态
    var uploadedFileName by remember { mutableStateOf<String?>(null) }
    var uploadedFileSize by remember { mutableStateOf<Int?>(null) }
    var isUploading by remember { mutableStateOf(false) }
    
    // 创建上传对话框
    val uploadDialog = useUploadDialog(
        onDismiss = { 
            println("上传对话框已关闭") 
        },
        onUpload = { fileBytes, fileName ->
            // 模拟上传处理
            isUploading = true
            // 实际应用中这里会调用API上传文件
            // 这里仅做演示，记录文件信息
            uploadedFileName = fileName
            uploadedFileSize = fileBytes.size
            isUploading = false
            println("文件已上传: $fileName, 大小: ${fileBytes.size} 字节")
        },
        dialogTitle = "上传文件示例",
        selectButtonText = "浏览文件",
        selectingText = "正在选择...",
        uploadButtonText = "确认上传",
        cancelButtonText = "放弃上传"
    )
    
    // 渲染上传对话框
    uploadDialog.render()
    
    Column(modifier = Modifier.padding(16.dp)) {
        Text("UseUploadDialog 使用示例", style = MaterialTheme.typography.headlineSmall)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 显示上传状态
        if (uploadedFileName != null && uploadedFileSize != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("上传成功!", style = MaterialTheme.typography.titleMedium)
                    Text("文件名: $uploadedFileName")
                    Text("文件大小: $uploadedFileSize 字节")
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        // 操作按钮
        Button(
            onClick = { uploadDialog.open() },
            enabled = !isUploading
        ) {
            Text(if (isUploading) "上传中..." else "打开上传对话框")
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // 重置按钮
        if (uploadedFileName != null) {
            Button(
                onClick = { 
                    uploadedFileName = null
                    uploadedFileSize = null
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text("重置上传状态")
            }
        }
    }
}