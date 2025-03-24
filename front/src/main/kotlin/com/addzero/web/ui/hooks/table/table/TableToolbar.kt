package com.addzero.web.ui.hooks.table.table

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
 * @param onAddSubmit 点击新增按钮时的回调
 * @param onBatchDeleteSubmit 批量删除的回调
 * @param onImport 导入的回调
 * @param onExport 导出的回调
 */
class UseTableToolBar<E>(
    val onAddSubmit: () -> Unit,
    val onBatchDeleteSubmit: (List<E>) -> Unit,
    val onImport: () -> Unit,
    val onExport: () -> Unit,
) : UseHook<UseTableToolBar<E>> {

    // 状态变量
    var showInsertFormFlag by mutableStateOf(false)
    var isEditMode by mutableStateOf(false)

    var selectedItems by mutableStateOf<List<E>>(emptyList())
    var dataList by mutableStateOf<List<E>>(emptyList())

    val useCheckedButton = UseCheckedButton<E>().apply {
        this.selectedItems = selectedItems
        this.isEditMode = isEditMode
    }


    @OptIn(ExperimentalMaterial3Api::class)
    override val render: @Composable () -> Unit
        get() = {
            val useCheckedButton = useCheckedButton.getState()

            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                //编辑模式
                useCheckedButton.render()
                // 新增按钮
                ToolButton(
                    label = "新增",
                    onClick ={
                        showInsertFormFlag=true
                    } ,
                    icon = Icons.Default.Add,
                )

                // 批量删除按钮 - 仅在编辑模式下且有选中项时可用
                ToolButton(
                    showFlag = isEditMode,
                    enabled = selectedItems.isNotEmpty(),
                    label = "批量删除",
                    onClick = onExport,
                    icon = Icons.Default.DeleteSweep,
                    tint = if (selectedItems.isEmpty()) MaterialTheme.colorScheme.onSurfaceVariant.copy(
                        alpha = 0.38f
                    ) else MaterialTheme.colorScheme.error
                )


                ToolButton(
                    label = "导入",
                    onClick = onImport,
                    icon = Icons.Default.FileUpload,
                )



                ToolButton(
                    enabled = dataList.isNotEmpty(),
                    label = "导出",
                    onClick = onExport,
                    icon = Icons.Default.FileDownload,
                    tint = if (dataList.isEmpty()) MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f) else MaterialTheme.colorScheme.primary
                )

            }
        }


}
