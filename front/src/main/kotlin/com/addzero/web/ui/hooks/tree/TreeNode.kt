package com.addzero.web.ui.hooks.tree

import androidx.compose.runtime.Composable

// 树节点数据模型
data class TreeNode<T>(
    val id: String,
    val label: String,
    val data: T,
    val type: String = "default",
    val children: List<TreeNode<T>> = emptyList(),
    val level: Int = 0,
    val customRender: (@Composable (TreeNode<T>) -> Unit)? = null,
    val isEditable: Boolean = true
)

// 预定义节点类型
object NodeTypes {
    const val DEFAULT = "default"
    const val FOLDER = "folder"
    const val FILE = "file"
    const val DOCUMENT = "document"
    const val IMAGE = "image"
    const val VIDEO = "video"
    const val AUDIO = "audio"
    const val LINK = "link"
    const val TAG = "tag"
    const val TASK = "task"
    const val NOTE = "note"
}