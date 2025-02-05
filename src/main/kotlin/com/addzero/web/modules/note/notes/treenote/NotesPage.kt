package com.addzero.web.modules.note.notes.treenote

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NoteAdd
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.addzero.web.modules.note.notes.NotesService
import com.addzero.web.modules.note.notes.NotesViewModel
import com.addzero.web.ui.components.common.UploadDialog
import com.addzero.web.ui.components.system.dynamicroute.MetaSpec
import com.addzero.web.ui.components.system.dynamicroute.RouteMetadata

class NotesPage : MetaSpec {


    override val metadata: RouteMetadata
        get() = RouteMetadata(
            refPath = this.javaClass.name,

//            refPath = "/note",
//            parentRefPath = "",
            title = "我的笔记",
            icon = Icons.Filled.Apps,
            visible = true,
            permissions = emptyList()
        )

    @Composable()
    override fun render() {
        val viewModel = remember { NotesViewModel(NotesService()) }
        var showUploadDialog by remember { mutableStateOf(false) }
        var showNewNoteEditor by remember { mutableStateOf(false) }
        var content by remember { mutableStateOf("") }
        var lastSavedContent by remember { mutableStateOf("") }
        val focusRequester = remember { FocusRequester() }
        LaunchedEffect(Unit) {
            viewModel.loadItems()
        }

        Row(modifier = Modifier.fillMaxSize()) {
            // 工具栏
            Surface(
                modifier = Modifier.fillMaxWidth(), tonalElevation = 1.dp
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(onClick = {
                        showNewNoteEditor = true
                        content = ""
                        lastSavedContent = ""
                        viewModel.createNote(null) // 创建根级别的新笔记
                        focusRequester.requestFocus()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.NoteAdd, "新建笔记")
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
                modifier = Modifier.fillMaxWidth(), tonalElevation = 0.dp
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
                        "标题", style = MaterialTheme.typography.titleSmall, modifier = Modifier.weight(1f)
                    )
                    // 操作按钮占位
                    Spacer(Modifier.width(96.dp)) // 两个按钮的宽度
                }
            }

            Divider()

            // 左侧笔记列表
            Surface(
                modifier = Modifier.width(300.dp).fillMaxHeight(), tonalElevation = 1.dp
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    viewModel.items.forEach { note ->
                        NoteTreeItem(note = note, selected = note == viewModel.currentItem, onSelect = {
                            viewModel.currentItem = it
                            content = it.content
                            lastSavedContent = it.content
                        }, onDelete = { viewModel.deleteItem(it.id) }, onAddChild = { parent ->
                            viewModel.createNote(parent.id)
                        })
                    }
                }
            }

            // 右侧编辑区域
            Surface(
                modifier = Modifier.weight(1f).fillMaxHeight(), tonalElevation = 0.dp
            ) {
                Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    OutlinedTextField(
                        value = content,
                        onValueChange = { content = it },
                        placeholder = { Text("Enter text") },
                        label = { Text("Label") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text, imeAction = ImeAction.Done
                        ),
                        modifier = Modifier.focusRequester(focusRequester)
                    )
                }
            }
        }

        // 上传对话框
        if (showUploadDialog) {
            UploadDialog(onDismiss = { showUploadDialog = false }, onUpload = { bytes, filename ->
                viewModel.uploadFile(bytes, filename)
                showUploadDialog = false
            })
        }
    }


}
