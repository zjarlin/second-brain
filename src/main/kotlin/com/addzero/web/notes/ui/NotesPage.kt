package com.addzero.web.notes.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.web.notes.NotesViewModel
import com.addzero.web.notes.model.Note

@Composable
fun NotesPage(viewModel: NotesViewModel) {
    var showUploadDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadItems()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("知识库", style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(16.dp))

        // 工具栏
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { showUploadDialog = true }) {
                Text("上传文档")
            }
        }

        Spacer(Modifier.height(16.dp))

        // 笔记列表
        viewModel.items.forEach { note ->
            NoteCard(
                note = note,
                onClick = { viewModel.currentItem = note },
                onDelete = { viewModel.deleteItem(note.id) }
            )
        }

        // 分页控制
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { viewModel.changePage(viewModel.currentPage - 1) },
                enabled = viewModel.currentPage > 0
            ) {
                Text("上一页")
            }
            
            Text(
                "${viewModel.currentPage + 1}/${viewModel.totalPages}",
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            
            Button(
                onClick = { viewModel.changePage(viewModel.currentPage + 1) },
                enabled = viewModel.currentPage < viewModel.totalPages - 1
            ) {
                Text("下一页")
            }
        }

        // 上传对话框
        if (showUploadDialog) {
            UploadDialog(
                onDismiss = { showUploadDialog = false },
                onUpload = { bytes, filename -> 
                    viewModel.uploadFile(bytes, filename)
                    showUploadDialog = false
                }
            )
        }

        // 加载状态
        if (viewModel.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp)
            )
        }

        // 错误提示
        viewModel.error?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}

@Composable
private fun NoteCard(
    note: Note,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(note.title, style = MaterialTheme.typography.titleMedium)
                Text(
                    note.content.take(100) + if (note.content.length > 100) "..." else "",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "删除"
                )
            }
        }
    }
} 