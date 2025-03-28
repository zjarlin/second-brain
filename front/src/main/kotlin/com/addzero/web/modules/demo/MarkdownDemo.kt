package com.addzero.web.modules.demo

import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import cn.hutool.core.io.FileUtil
import com.addzero.Route
import com.addzero.web.ui.components.ScrollableContainer
import com.addzero.web.ui.hooks.ViewModel
import com.mikepenz.markdown.m3.Markdown
import java.io.File

@Composable
@Route
fun 测试markdown() {
    val absolutePath = File("").absolutePath
    val parent = FileUtil.getParent(absolutePath, 1)
    val initialContent = FileUtil.readUtf8String(parent + File.separator + "README.md")
    val render = UseMarkdown(initialContent).render
}

data class UseMarkdown(val initialContent: String) : ViewModel<UseMarkdown> {
    var content by mutableStateOf(initialContent)

    override val render: @Composable () -> Unit
        get() = {
            var isSourceMode by remember { mutableStateOf(false) }
            var isEditing by remember { mutableStateOf(false) }
            var editingSection by remember { mutableStateOf<String?>(null) }
            var editingSectionContent by remember { mutableStateOf("") }
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                // 顶部工具栏
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = when {
                            isSourceMode -> "源码模式"
                            isEditing -> "编辑模式"
                            else -> "预览模式"
                        }, style = MaterialTheme.typography.titleMedium
                    )

                    Row {
                        // 源码模式按钮
                        FloatingActionButton(
                            onClick = {
                                isSourceMode = !isSourceMode
                                if (isSourceMode) {
                                    isEditing = false
                                    editingSection = null
                                }
                            }, modifier = Modifier.size(48.dp).padding(end = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Code,
                                contentDescription = if (isSourceMode) "退出源码模式" else "切换到源码模式"
                            )
                        }

                        // 编辑/预览按钮
                        FloatingActionButton(
                            onClick = {
                                if (!isSourceMode) {
                                    isEditing = !isEditing
                                    editingSection = null
                                }
                            }, modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = if (isEditing) Icons.Default.Visibility else Icons.Default.Edit,
                                contentDescription = if (isEditing) "切换到预览" else "切换到编辑"
                            )
                        }
                    }
                }

                // 内容区域 - 使用动画过渡
                Box(modifier = Modifier.fillMaxSize()) {
                    // 源码模式
                    androidx.compose.animation.AnimatedVisibility(
                        visible = isSourceMode,
                        enter = fadeIn(animationSpec = tween(300)) + expandVertically(),
                        exit = fadeOut(animationSpec = tween(300)) + shrinkVertically()
                    ) {
                        OutlinedTextField(
                            value = content,
                            onValueChange = { content = it },
                            modifier = Modifier.fillMaxSize(),
                            textStyle = TextStyle(
                                fontFamily = MaterialTheme.typography.bodyLarge.fontFamily
                            )
                        )
                    }

                    // 预览模式（可点击编辑）
                    androidx.compose.animation.AnimatedVisibility(
                        visible = !isSourceMode && !isEditing && editingSection == null,
                        enter = fadeIn(animationSpec = tween<Float>(300)) + expandVertically(),
                        exit = fadeOut(animationSpec = tween<Float>(300)) + shrinkVertically()
                    ) {
                        ScrollableContainer {
                            // 将内容分成段落，每段可点击编辑
                            val paragraphs = content.split("\n\n")
                            Column {
                                paragraphs.forEachIndexed<String> { index, paragraph ->
                                    if (paragraph.isNotBlank()) {
                                        Box(
                                            modifier = Modifier.fillMaxWidth().clickable {
                                                editingSection = index.toString()
                                                editingSectionContent = paragraph
                                            }) {
                                            Markdown(content = paragraph)
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                    }
                                }
                            }
                        }
                    }

                    // 全局编辑模式
                    androidx.compose.animation.AnimatedVisibility(
                        visible = !isSourceMode && isEditing,
                        enter = fadeIn(animationSpec = tween(300)) + expandVertically(),
                        exit = fadeOut(animationSpec = tween(300)) + shrinkVertically()
                    ) {
                        OutlinedTextField(
                            value = content,
                            onValueChange = { content = it },
                            modifier = Modifier.fillMaxSize(),
                            textStyle = TextStyle(
                                fontFamily = MaterialTheme.typography.bodyLarge.fontFamily
                            )
                        )
                    }

                    // 段落编辑模式
                    androidx.compose.animation.AnimatedVisibility(
                        visible = !isSourceMode && !isEditing && editingSection != null,
                        enter = fadeIn(animationSpec = tween<Float>(300)) + expandVertically(),
                        exit = fadeOut(animationSpec = tween<Float>(300)) + shrinkVertically()
                    ) {
                        Column {
                            OutlinedTextField(
                                value = editingSectionContent,
                                onValueChange = { editingSectionContent = it },
                                modifier = Modifier.fillMaxWidth().weight(1f),
                                textStyle = TextStyle(
                                    fontFamily = MaterialTheme.typography.bodyLarge.fontFamily
                                )
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Button(
                                    onClick = { editingSection = null }) {
                                    Text("取消")
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(
                                    onClick = {
                                        val paragraphs = content.split("\n\n").toMutableList()
                                        val sectionIndex = editingSection?.toIntOrNull() ?: 0
                                        if (sectionIndex < paragraphs.size) {
                                            paragraphs[sectionIndex] = editingSectionContent
                                            content = paragraphs.joinToString("\n\n")
                                        }
                                        editingSection = null
                                    }) {
                                    Text("保存")
                                }
                            }
                        }
                    }
                }
            }
        }
}


