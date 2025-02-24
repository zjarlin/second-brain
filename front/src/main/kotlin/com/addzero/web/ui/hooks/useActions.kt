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
class Actions<T> {
    private var showAddDialog by mutableStateOf(false)
    private var showEditDialog by mutableStateOf(false)
    private var showDeleteDialog by mutableStateOf(false)
    private var selectedItem by mutableStateOf<T?>(null)

    private val onAddClick = {
        showAddDialog = true
        onAdd()
    }

    private val onEditClick = { item: T ->
        selectedItem = item
        showEditDialog = true
        onEdit(item)
    }

    private val onDeleteClick = { item: T ->
        selectedItem = item
        showDeleteDialog = true
        onDelete(item)
    }

    private val onDismissDialog = {
        showAddDialog = false
        showEditDialog = false
        showDeleteDialog = false
        selectedItem = null
    }

    private val renderActions: @Composable () -> Unit = {
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

    private var onAdd: () -> Unit = {}
    private var onEdit: (T) -> Unit = {}
    private var onDelete: (T) -> Unit = {}

    fun setCallbacks(
        onAdd: () -> Unit = {},
        onEdit: (T) -> Unit = {},
        onDelete: (T) -> Unit = {}
    ) {
        this.onAdd = onAdd
        this.onEdit = onEdit
        this.onDelete = onDelete
    }

    fun getState(): ActionState<T> = ActionState(
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

@Composable
fun <T> useActions(
    onAdd: () -> Unit = {},
    onEdit: (T) -> Unit = {},
    onDelete: (T) -> Unit = {}
): ActionState<T> {
    val actions = remember { Actions<T>() }
    actions.setCallbacks(onAdd, onEdit, onDelete)
    return actions.getState()
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
