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

    Row(modifier = Modifier.fillMaxSize()) {
        // 左侧树状列表
        Surface(
            modifier = Modifier.width(300.dp).fillMaxHeight(),
            tonalElevation = 1.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // 工具栏
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
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

                Spacer(Modifier.height(16.dp))

                // 笔记树
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

        // 右侧编辑区
        Surface(
            modifier = Modifier.weight(1f).fillMaxHeight(),
            tonalElevation = 0.dp
        ) {
            viewModel.currentItem?.let { note ->
                MarkdownEditor(
                    value = note.content,
                    onValueChange = { newContent ->
                        viewModel.updateNote(note.copy(content = newContent))
                    },
                    modifier = Modifier.fillMaxSize().padding(16.dp)
                )
            } ?: run {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        "选择或创建一个笔记开始编辑",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.align(Alignment.Center)
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