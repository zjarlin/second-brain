package com.addzero.web.ui.hooks.table.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.web.ui.hooks.UseHook

/**
 * 通用表格工具栏组件
 * 提供通用的表格操作按钮
 * @param onAddClick 点击新增按钮时的回调
 * @param onBatchDelete 批量删除的回调
 * @param onImport 导入的回调
 * @param onExport 导出的回调
 */
class UseTableToolBar<E>(
    val onAddClick: () -> Unit,
    val onBatchDelete: (List<Any>) -> Unit,
    val onImport: () -> Unit,
    val onExport: () -> Unit,
    ) : UseHook<UseTableToolBar<E>> {
    
    // 状态变量
    var showInsertFormFlag by mutableStateOf(false)
    var isEditMode by mutableStateOf(false)
    var selectedItems by mutableStateOf<List<Any>>(emptyList())
    var dataList by mutableStateOf<List<E>>(emptyList())
    var showBatchDeleteFlag by mutableStateOf(false)
    var showImportFlag by mutableStateOf(false)
    var showExportFlag by mutableStateOf(false)
    
    /**
     * 更新状态
     * 供外部组件调用，保持状态同步
     */
    fun updateState(
        newEditMode: Boolean? = null,
        newSelectedItems: List<Any>? = null,
        newDataList: List<E>? = null
    ) {
        newEditMode?.let { isEditMode = it }
        newSelectedItems?.let { selectedItems = it }
        newDataList?.let { dataList = it }
    }
    
    /**
     * 切换编辑模式
     */
    fun toggleEditMode() {
        isEditMode = !isEditMode
        if (!isEditMode) {
            selectedItems = emptyList()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override val render: @Composable () -> Unit
        get() = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 编辑模式切换按钮
                TooltipBox(
                    positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                    tooltip = {
                        PlainTooltip { Text(if (isEditMode) "退出编辑" else "进入编辑") }
                    },
                    state = rememberTooltipState()
                ) {
                    FilledTonalIconToggleButton(
                        checked = isEditMode,
                        onCheckedChange = { toggleEditMode() },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = if (isEditMode) "退出编辑" else "进入编辑",
                            tint = if (isEditMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // 新增按钮
                TooltipBox(
                    positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                    tooltip = {
                        PlainTooltip { Text("新增") }
                    },
                    state = rememberTooltipState()
                ) {
                    FilledTonalIconButton(
                        onClick = onAddClick,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "新增",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                // 批量删除按钮 - 仅在编辑模式下且有选中项时可用
                if (isEditMode) {
                    TooltipBox(
                        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                        tooltip = {
                            PlainTooltip { Text("批量删除") }
                        },
                        state = rememberTooltipState()
                    ) {
                        FilledTonalIconButton(
                            onClick = {
                                if (selectedItems.isNotEmpty()) {
                                    showBatchDeleteFlag = true
                                    onBatchDelete(selectedItems)
                                }
                            },
                            enabled = selectedItems.isNotEmpty(),
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Icon(
                                Icons.Default.DeleteSweep,
                                contentDescription = "批量删除",
                                tint = if (selectedItems.isEmpty()) MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                    alpha = 0.38f
                                ) else MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }

                // 导入按钮
                TooltipBox(
                    positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                    tooltip = {
                        PlainTooltip { Text("导入") }
                    },
                    state = rememberTooltipState()
                ) {
                    FilledTonalIconButton(
                        onClick = { 
                            showImportFlag = true
                            onImport()
                        },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(
                            Icons.Default.FileUpload,
                            contentDescription = "导入",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                // 导出按钮 - 仅在有数据时可用
                TooltipBox(
                    positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                    tooltip = {
                        PlainTooltip { Text("导出") }
                    },
                    state = rememberTooltipState()
                ) {
                    FilledTonalIconButton(
                        onClick = { 
                            showExportFlag = true
                            onExport()
                        },
                        enabled = dataList.isNotEmpty(),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(
                            Icons.Default.FileDownload,
                            contentDescription = "导出",
                            tint = if (dataList.isEmpty()) MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f) else MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
}
