package com.addzero.web.ui.hooks.table.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.addzero.web.ui.hooks.UseHook
import com.addzero.web.ui.hooks.table.entity.AddColumn

/**
 * 表格布局Hook
 */
class UseTableLayout<E : Any> : UseHook<UseTableLayout<E>> {
    var columns by mutableStateOf<List<AddColumn<E>>>(emptyList())
    var dataList by mutableStateOf<List<E>>(emptyList())
    var onEdit: (E) -> Unit = {}
    var onDelete: (E) -> Unit = {}
    var getIdFun: (E) -> Any = { it.hashCode() }
    
    private var showEditDialog by mutableStateOf(false)
    private var currentEditItem by mutableStateOf<E?>(null)

    override val render: @Composable () -> Unit
        get() = {
            Column(modifier = modifier) {
                // 表头
                Row(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    columns.forEach { column ->
                        Text(
                            text = column.title,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.weight(1f).padding(horizontal = 4.dp)
                        )
                    }
                    // 操作列表头
                    Text(
                        text = "操作",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.width(120.dp).padding(horizontal = 4.dp)
                    )
                }

                // 数据行
                LazyColumn {
                    items(dataList, key = { getIdFun(it) }) { item ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // 数据列
                            columns.forEach { column ->
                                Box(modifier = Modifier.weight(1f).padding(horizontal = 4.dp)) {
                                    column.customRender(item)
                                }
                            }

                            // 操作列
                            Row(
                                modifier = Modifier.width(120.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                IconButton(onClick = {
                                    currentEditItem = item
                                    showEditDialog = true
                                }) {
                                    Icon(Icons.Default.Edit, contentDescription = "编辑")
                                }
                                IconButton(onClick = { onDelete(item) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "删除")
                                }
                            }
                        }
                        HorizontalDivider()
                    }
                }

                // 编辑对话框
                if (showEditDialog && currentEditItem != null) {
                    Dialog(onDismissRequest = { showEditDialog = false }) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.medium
                        ) {

                            println("currentEditItem: $currentEditItem")
                        }
                    }
                }
            }
        }
}