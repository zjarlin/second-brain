package com.addzero.web.modules.note.notes.treenote

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.web.modules.note.notes.NotesViewModel
import com.addzero.web.ui.components.UploadDialog

@Composable
fun NotesPage(viewModel: NotesViewModel) {
    var showUploadDialog by remember { mutableStateOf(false) }
    var showNewNoteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadItems()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // 工具栏
        Surface(
            modifier = Modifier.fillMaxWidth(),
            tonalElevation = 1.dp
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = { showNewNoteDialog = true }) {
                    Icon(Icons.Default.NoteAdd, "新建笔记")
                    Spacer(Modifier.width(8.dp))
                    Text("新建笔记")
                }
                
                IconButton(onClick = { showUploadDialog = true }) {
                    Icon(Icons.Default.Upload, "上传文档")
                }
            }
        }

        // 表头
        Surface(
            modifier = Modifier.fillMaxWidth(),
            tonalElevation = 0.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 展开图标占位
                Spacer(Modifier.width(40.dp))
                // 文件图标占位
                Spacer(Modifier.width(32.dp))
                // 标题列
                Text(
                    "标题",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.weight(1f)
                )
                // 操作按钮占位
                Spacer(Modifier.width(96.dp)) // 两个按钮的宽度
            }
        }

        Divider()

        // 笔记列表
        Surface(
            modifier = Modifier.weight(1f),
            tonalElevation = 0.dp
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                viewModel.items.forEach { note ->
                    NoteTreeItem(
                        note = note,
                        selected = note == viewModel.currentItem,
                        onSelect = { viewModel.currentItem = it },
                        onDelete = { viewModel.deleteItem(it.id) },
                        onAddChild = { parent -> 
                            viewModel.createNote(parent.id)
                        }
                    )
                }
            }
        }
    }

    // 对话框
    if (showUploadDialog) {
        UploadDialog(
            onDismiss = { showUploadDialog = false },
            onUpload = { bytes, filename ->
                viewModel.uploadFile(bytes, filename)
                showUploadDialog = false
            }
        )
    }

    if (showNewNoteDialog) {
        NewNoteDialog(
            onDismiss = { showNewNoteDialog = false },
            onCreate = { title ->
                viewModel.createNote(title = title)
                showNewNoteDialog = false
            }
        )
    }
}

@Composable
private fun NewNoteDialog(
    onDismiss: () -> Unit,
    onCreate: (String) -> Unit
) {
    var title by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("新建笔记") },
        text = {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("标题") }
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onCreate(title) },
                enabled = title.isNotBlank()
            ) {
                Text("创建")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}