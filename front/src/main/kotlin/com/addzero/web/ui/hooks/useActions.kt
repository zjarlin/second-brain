package com.addzero.web.ui.hooks

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 操作区Hook，用于管理操作按钮相关的状态和操作
 *
 * @param onAdd 新增操作回调函数
 * @param onEdit 编辑操作回调函数
 * @param onDelete 删除操作回调函数
 * @return ActionState 包含操作相关的状态和操作
 */
@Composable
fun <T> useActions(
    onAdd: () -> Unit = {},
    onEdit: (T) -> Unit = {},
    onDelete: (T) -> Unit = {}
): ActionState<T> {
    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf<T?>(null) }

    val onAddClick = {
        showAddDialog = true
        onAdd()
    }

    val onEditClick = { item: T ->
        selectedItem = item
        showEditDialog = true
        onEdit(item)
    }

    val onDeleteClick = { item: T ->
        selectedItem = item
        showDeleteDialog = true
        onDelete(item)
    }

    val onDismissDialog = {
        showAddDialog = false
        showEditDialog = false
        showDeleteDialog = false
        selectedItem = null
    }

    val renderActions: @Composable () -> Unit = {
        Button(
            onClick = onAddClick,
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = "新增",
                modifier = Modifier.padding(end = 4.dp)
            )
            Text("新增")
        }
    }

    return remember(showAddDialog, showEditDialog, showDeleteDialog, selectedItem) {
        ActionState(
            showAddDialog = showAddDialog,
            showEditDialog = showEditDialog,
            showDeleteDialog = showDeleteDialog,
            selectedItem = selectedItem,
            onAddClick = onAddClick,
            onEditClick = onEditClick,
            onDeleteClick = onDeleteClick,
            onDismissDialog = onDismissDialog,
            renderActions = renderActions
        )
    }
}

/**
 * 操作状态数据类
 */
data class ActionState<T>(
    val showAddDialog: Boolean,
    val showEditDialog: Boolean,
    val showDeleteDialog: Boolean,
    val selectedItem: T?,
    val onAddClick: () -> Unit,
    val onEditClick: (T) -> Unit,
    val onDeleteClick: (T) -> Unit,
    val onDismissDialog: () -> Unit,
    val renderActions: @Composable () -> Unit
)