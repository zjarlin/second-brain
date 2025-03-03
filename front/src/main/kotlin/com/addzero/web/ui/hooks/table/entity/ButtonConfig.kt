package com.addzero.web.ui.hooks.table.entity

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Upload
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * 表格按钮配置
 */
data class ButtonConfig<E>(
    /** 按钮文本 */
    val text: String,
    
    /** 按钮图标 */
    val icon: ImageVector,
    
    /** 按钮点击事件 */
    val onClick: (List<E>) -> Unit,
    
    /** 是否显示 */
    val visible: Boolean = true,
    
    /** 是否启用 */
    val enabled: Boolean = true,
    
    /** 按钮提示文本 */
    val tooltip: String = "",
    
    /** 确认对话框标题 */
    val confirmTitle: String = "",
    
    /** 确认对话框内容 */
    val confirmContent: String = "",
    
    /** 是否需要确认 */
    val needConfirm: Boolean = false
) {
    companion object {
        fun <E> defaultButtons(
            onAdd: () -> Unit = {},
            onImport: () -> Unit = {},
            onExport: (List<E>) -> Unit = {},
            onBatchDelete: (List<E>) -> Unit = {},
            onEditModeChange: (Boolean) -> Unit = {}
        ): List<ButtonConfig<E>> = listOf(
            ButtonConfig(
                text = "编辑模式",
                icon = Icons.Default.Edit,
                onClick = { _ -> onEditModeChange(true) },
                tooltip = "进入编辑模式"
            ),
            ButtonConfig(
                text = "新增",
                icon = Icons.Default.Add,
                onClick = { _ -> onAdd() }
            ),
            ButtonConfig(
                text = "导入",
                icon = Icons.Default.Upload,
                onClick = { _ -> onImport() }
            ),
            ButtonConfig(
                text = "导出",
                icon = Icons.Default.Download,
                onClick = onExport
            ),
            ButtonConfig(
                text = "批量删除",
                icon = Icons.Default.Delete,
                onClick = onBatchDelete,
                needConfirm = true,
                confirmTitle = "确认删除",
                confirmContent = "确定要删除选中的记录吗？"
            )
        )
    }
}