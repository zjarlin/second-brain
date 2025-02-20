//package com.addzero.web.modules.notes
//
//import androidx.compose.foundation.*
//import androidx.compose.foundation.layout.*
//import androidx.compose.material.*
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import cn.hutool.extra.spring.SpringUtil
//import com.addzero.web.modules.note.notes.MarkdownEditor
//
//@Composable
//fun NotesPage() {
//    val viewModel = remember { SpringUtil.getBean(NotesViewModel::class.java) }
//    val treeData by viewModel.treeData
//    val selectedNote by viewModel.selectedNote
//    val editorContent by viewModel.editorContent
//
//    Row(modifier = Modifier.fillMaxSize()) {
//        // 左侧树形结构
//        Box(
//            modifier = Modifier
//                .width(300.dp)
//                .fillMaxHeight()
//                .background(MaterialTheme.colors.surface)
//        ) {
//            Column {
//                // 工具栏
//                TopAppBar(
//                    title = { Text("笔记列表") },
//                    actions = {
//                        IconButton(onClick = {
//                            // TODO: 实现添加笔记功能
//                        }) {
//                            Icon(Icons.Default.Add, contentDescription = "添加笔记")
//                        }
//                    },
//                    backgroundColor = MaterialTheme.colors.surface,
//                    elevation = 0.dp
//                )
//
//                // 笔记树
//                TreeView(
//                    nodes = treeData,
//                    onNodeSelected = { node ->
//                        viewModel.selectNote(node)
//                    },
//                    nodeContent = { node ->
//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(horizontal = 8.dp, vertical = 4.dp),
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//                            Icon(
//                                imageVector = if (node.children.isEmpty()) Icons.Default.Note else Icons.Default.Folder,
//                                contentDescription = null,
//                                modifier = Modifier.size(20.dp)
//                            )
//                            Spacer(modifier = Modifier.width(8.dp))
//                            Text(node.title)
//                            Spacer(modifier = Modifier.weight(1f))
//                            IconButton(
//                                onClick = { viewModel.deleteNote(node.id) },
//                                modifier = Modifier.size(24.dp)
//                            ) {
//                                Icon(Icons.Default.Delete, contentDescription = "删除笔记")
//                            }
//                        }
//                    }
//                )
//            }
//        }
//
//        // 右侧编辑器
//        Box(
//            modifier = Modifier
//                .weight(1f)
//                .fillMaxHeight()
//        ) {
//            if (selectedNote != null) {
//                MarkdownEditor(
//                    value = editorContent,
//                    onValueChange = { viewModel.updateNoteContent(it) },
//                    modifier = Modifier.fillMaxSize()
//                )
//            } else {
//                Box(
//                    modifier = Modifier.fillMaxSize(),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text("请选择一个笔记进行编辑")
//                }
//            }
//        }
//    }
//}
