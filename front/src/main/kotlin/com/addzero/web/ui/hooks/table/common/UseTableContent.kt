package com.addzero.web.ui.hooks.table.common

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
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
import com.addzero.common.kt_util.toNotBlankStr
import com.addzero.web.ui.hooks.UseHook
import com.addzero.web.ui.hooks.table.entity.AddColumn

class UseTableContent<E>(
//    val onSelectionChange: (List<E>) -> Unit = {},
    val getIdFun: (E) -> Any = { it.hashCode() }
) : UseHook<UseTableContent<E>> {

    var columns: List<AddColumn<E>> by mutableStateOf(listOf())
    var dataList: List<E> by mutableStateOf(listOf())
    var selectedItems: List<E> by mutableStateOf(listOf())
    val isEditMode by mutableStateOf(false)
    var currentSelectItem: E? by mutableStateOf(null)

    var showFormFlag: Boolean by mutableStateOf(false)
    var showDeleteFlag: Boolean by mutableStateOf(false)


    @OptIn(ExperimentalMaterial3Api::class)
    override val render: @Composable
        () -> Unit
        get() = {
            Column(modifier = Modifier.fillMaxSize()) {
                val horizontalScrollState = rememberScrollState()

                // 表头区域
                Row(
                    modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f))
                        .padding(vertical = 12.dp), verticalAlignment = Alignment.CenterVertically
                ) {
                    // 选择框表头
                    if (isEditMode) {
                        Box(
                            modifier = androidx.compose.ui.Modifier.width(48.dp).padding(horizontal = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Checkbox(
                                checked = selectedItems.size == dataList.size, onCheckedChange = {
                                    if (it) {
                                        selectedItems = dataList
                                    } else {
                                        selectedItems = emptyList()
                                    }
                                })
                        }
                    }

                    // 序号表头
                    Box(
                        modifier = androidx.compose.ui.Modifier.width(60.dp).padding(horizontal = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "序号",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // 数据列表头
                    Box(
                        modifier = Modifier.weight(1f).horizontalScroll(horizontalScrollState)
                    ) {
                        Row(
                            modifier = Modifier.width(IntrinsicSize.Max),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            columns.forEach { column ->
                                Text(
                                    text = column.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = androidx.compose.ui.Modifier.width(150.dp).padding(horizontal = 8.dp)
                                )
                            }
                        }
                    }

                    // 操作列表头
                    Box(
                        modifier = androidx.compose.ui.Modifier.width(120.dp).padding(horizontal = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "操作",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // 使用Box包装LazyColumn，并设置weight属性使其填充剩余空间
                Box(modifier = Modifier.weight(1f)) {
                    LazyColumn {
                        items(dataList, key = getIdFun) { item ->
                            Row(
                                modifier = Modifier.fillMaxWidth().height(56.dp).background(
                                    color = when {
                                        selectedItems.contains(item) -> MaterialTheme.colorScheme.primaryContainer.copy(
                                            alpha = 0.12f
                                        )

                                        dataList.indexOf(item) % 2 == 1 -> MaterialTheme.colorScheme.primary.copy(alpha = 0.04f)

                                        else -> MaterialTheme.colorScheme.surface
                                    }
                                ).padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically
                            ) {
                                // 选择框列
                                if (isEditMode) {
                                    Box(
                                        modifier = androidx.compose.ui.Modifier.width(48.dp).padding(horizontal = 8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Checkbox(
                                            checked = selectedItems.contains(item), onCheckedChange = { checked ->
                                                selectedItems = if (checked) {
                                                    selectedItems + item
                                                } else {
                                                    selectedItems - item
                                                }

                                            })
                                    }
                                }

                                // 序号列
                                Box(
                                    modifier = androidx.compose.ui.Modifier.width(60.dp).padding(horizontal = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = (dataList.indexOf(item) + 1).toString(),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }

                                // 数据列
                                Box(
                                    modifier = Modifier.weight(1f).horizontalScroll(horizontalScrollState)
                                ) {
                                    Row(
                                        modifier = Modifier.width(IntrinsicSize.Max),
                                        horizontalArrangement = Arrangement.Start,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        columns.forEach { column ->
                                            Box(
                                                modifier = androidx.compose.ui.Modifier.width(150.dp)
                                                    .padding(horizontal = 8.dp).height(IntrinsicSize.Min),
                                                contentAlignment = Alignment.CenterStart
                                            ) {
                                                val content = column.getFun(item).toNotBlankStr()
                                                val displayText = if (content.length > 30) {
                                                    content.take(30) + "..."
                                                } else {
                                                    content
                                                }

                                                TooltipBox(
                                                    positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                                                    tooltip = {
                                                        PlainTooltip {
                                                            Text(content)
                                                        }
                                                    },
                                                    state = rememberTooltipState()
                                                ) {
                                                    SelectionContainer {
                                                        Text(
                                                            text = displayText,
                                                            maxLines = 2,
                                                            style = MaterialTheme.typography.bodyMedium,
                                                            color = MaterialTheme.colorScheme.onSurface
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                // 操作列
                                Box(
                                    modifier = androidx.compose.ui.Modifier.width(120.dp).padding(horizontal = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        TooltipBox(
                                            positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                                            tooltip = {
                                                PlainTooltip { Text("编辑") }
                                            },
                                            state = rememberTooltipState()
                                        ) {
                                            IconButton(onClick = {
                                                currentSelectItem = item
                                                showFormFlag = true
                                            }) {
                                                Icon(
                                                    Icons.Default.Edit,
                                                    contentDescription = "编辑",
                                                    tint = MaterialTheme.colorScheme.primary
                                                )
                                            }
                                        }
                                        TooltipBox(
                                            positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                                            tooltip = {
                                                PlainTooltip { Text("删除") }
                                            },
                                            state = rememberTooltipState()
                                        ) {
                                            IconButton(onClick = {
                                                showDeleteFlag = true
                                                currentSelectItem = item
                                            }) {
                                                Icon(
                                                    Icons.Default.Delete,
                                                    contentDescription = "删除",
                                                    tint = MaterialTheme.colorScheme.error
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)
                            )
                        }
                    }
                }
            }
        }
}

