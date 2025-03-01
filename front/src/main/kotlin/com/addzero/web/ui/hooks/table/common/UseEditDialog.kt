package com.addzero.web.ui.hooks.table.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.addzero.web.ui.hooks.UseHook

/**
 * 编辑对话框Hook
 */
class UseEditDialog<E>(    
    private val onDismiss: () -> Unit = {},
    private val onConfirm: (E) -> Unit = {},
    private val title: String = "编辑",
    private val content: @Composable (E) -> Unit = { item ->
        println(item)
    }
) : UseHook<UseEditDialog<E>> {

    /**
     * 是否显示
     */
    var showFlag by mutableStateOf(false)

    /**
     * 当前编辑的项
     */
    var editItem by mutableStateOf<E?>(null)

    override val render: @Composable () -> Unit = {
        if (showFlag && editItem != null) {
            AlertDialog(
                onDismissRequest = { close() },
                title = { Text(title) },
                text = { content(editItem!!) },
                confirmButton = {},
                dismissButton = null
            )
        }
    }

    /**
     * 打开编辑对话框
     * @param item 要编辑的项
     */
    fun open(item: E) {
        editItem = item
        showFlag = true
    }

    /**
     * 关闭编辑对话框
     */
    fun close() {
        onDismiss()
        showFlag = false
        editItem = null
    }
}

