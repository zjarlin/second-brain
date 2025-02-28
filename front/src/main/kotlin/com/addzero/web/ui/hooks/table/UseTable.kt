package com.addzero.web.ui.hooks.table

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.common.consts.DEFAULT_EXCLUDE_FIELDS
import com.addzero.common.kt_util.ClassMetadata
import com.addzero.common.kt_util.FieldMetadata
import com.addzero.common.kt_util.getMetadata
import com.addzero.common.kt_util.toNotBlankStr
import kotlin.reflect.KClass


@Composable
inline fun <reified E : Any> UseTable(
    modifier: Modifier = Modifier,
    excludeFields: Set<String> = DEFAULT_EXCLUDE_FIELDS,
    columns: List<AddColumn<E>> = listOf(),
    noinline onEdit: (E) -> Unit = {},
    noinline onDelete: (E) -> Unit = {},
    noinline onValueChange: (TableState<E>) -> Unit = {},
) {
    val clazz = E::class

    var showEditDialog by remember { mutableStateOf(false) }
    var currentEditItem by remember { mutableStateOf<E?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var currentDeleteItem by remember { mutableStateOf<E?>(null) }
    val state = remember {
        val mergedColumns = mergeColumns<E>(clazz, excludeFields, columns)
        TableState(
            initialColumns = mergedColumns
        )
    }
    LaunchedEffect(state.pageNo, state.pageSize) {
        onValueChange(state)
    }
    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // 搜索栏
            renderSearch<E>(state, onValueChange)

            // 表格头部和内容区域
            Surface(modifier = Modifier.weight(1f)) {
                Row(modifier = Modifier.fillMaxSize()) {
                    // 左侧数据区域（包含序号和数据列）
                    val scrollState = rememberScrollState()
                    Box(modifier = Modifier.weight(1f)) {
                        Column(modifier = Modifier.fillMaxSize()) {
                            // 表格头部
                            Surface(
                                tonalElevation = 2.dp,
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.95f)
                            ) {
                                Row(modifier = Modifier.horizontalScroll(scrollState)) {
                                    // 序号列
                                    Text(
                                        text = "序号",
                                        style = MaterialTheme.typography.titleSmall,
                                        modifier = Modifier.width(60.dp)
                                    )

                                    // 数据列
                                    state.columns.forEach { column ->
                                        Text(
                                            text = column.title,
                                            style = MaterialTheme.typography.titleSmall,
                                            modifier = Modifier.width(150.dp)
                                        )
                                    }
                                }
                            }

                            HorizontalDivider()

                            // 表格内容
                            LazyColumn(modifier = Modifier.fillMaxSize()) {
                                itemsIndexed(state.dataList) { index, item ->
                                    Row(modifier = Modifier.fillMaxWidth().horizontalScroll(scrollState)) {
                                        // 序号
                                        Text(
                                            text = (index + 1).toString(),
                                            modifier = Modifier.width(60.dp)
                                        )

                                        // 数据列
                                        state.columns.forEach { column ->
                                            Box(modifier = Modifier.width(150.dp)) {
                                                val value = column.getFun(item)
                                                val displayText = value.toNotBlankStr()
                                                column.customRender(displayText)
                                            }
                                        }
                                    }
//                                    if (index < state.dataList.size - 1) {
                                    HorizontalDivider()
//                                    }
                                }
                            }
                        }
                    }

                    // 右侧操作区域
                    Surface(modifier = Modifier.width(120.dp)) {
                        Column(modifier = Modifier.fillMaxHeight()) {
                            // 操作列表头
                            Text(
                                text = "操作",
                                style = MaterialTheme.typography.titleSmall
                            )

                            HorizontalDivider()

                            // 操作区内容
                            LazyColumn {
                                itemsIndexed(state.dataList) { index, item ->
                                    Row(modifier = Modifier.fillMaxWidth()) {
                                        IconButton(onClick = {
                                            currentEditItem = item
                                            showEditDialog = true
                                        }) {
                                            Icon(Icons.Default.Edit, "编辑")
                                        }
                                        IconButton(onClick = {
                                            currentDeleteItem = item
                                            showDeleteDialog = true
                                        }) {
                                            Icon(Icons.Default.Delete, "删除")
                                        }
                                    }
                                    if (index < state.dataList.size - 1) {
                                        HorizontalDivider()
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 分页控件
            Surface(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PageNavigation(
                        currentPage = state.pageNo,
                        totalPages = state.totalPages,
                        onPreviousPage = { state.previousPage() },
                        onNextPage = { state.nextPage() })
                    Spacer(modifier = Modifier.width(16.dp))
                    PageSizeSelector(
                        currentPageSize = state.pageSize,
                        onPageSizeChange = { state.updatePageSize(it) })
                    CustomPageSizeInput(
                        currentPageSize = state.pageSize,
                        onPageSizeChange = { state.updatePageSize(it) })
                }
            }

            // 编辑对话框
            if (showEditDialog && currentEditItem != null) {
                AlertDialog(
                    onDismissRequest = { showEditDialog = false },
                    title = { Text("编辑") },
                    text = {
                        DynamicForm(
                            item = currentEditItem!!,
                            onSave = { updatedItem ->
                                onEdit(updatedItem)
                                showEditDialog = false
                            },
                            onCancel = { showEditDialog = false }
                        )
                    },
                    confirmButton = { },
                    dismissButton = { }
                )
            }
            // 删除确认对话框
            if (showDeleteDialog && currentDeleteItem != null) {
                AlertDialog(
                    onDismissRequest = { showDeleteDialog = false },
                    title = { Text("确认删除") },
                    text = { Text("确定要删除这条记录吗？") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                onDelete(currentDeleteItem!!)
                                showDeleteDialog = false
                            }
                        ) {
                            Text("确认")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDeleteDialog = false }) {
                            Text("取消")
                        }
                    }
                )
            }
        }
    }
}

@Composable
inline fun <reified E : Any> renderSearch(
    state: TableState<E>,
    crossinline onValueChange: (TableState<E>) -> Unit
) {
    SearchBar(searchText = state.searchText, onSearchTextChange = { state.searchText = it }, onSearch = {
        state.search()
        onValueChange(state)
    })
}

inline fun <reified E : Any> mergeColumns(
    clazz: KClass<E>, excludeFields: Set<String>, columns: List<AddColumn<E>>
): List<AddColumn<E>> {
    val metadata = clazz.getMetadata()
    val defaultColumns = addColumns<E>(metadata, excludeFields)
    val mergedColumns = (defaultColumns + columns).distinctBy { it.title }
    return mergedColumns
}

inline fun <reified E : Any> addColumns(
    metadata: ClassMetadata<E>, excludeFields: Set<String>
): List<AddColumn<E>> {
    val defaultColumns = metadata.fields.filter<FieldMetadata<E>> { !excludeFields.contains(it.property.name) }
        .map<FieldMetadata<E>, AddColumn<E>> { field ->
            val getter = field.property.getter
            AddColumn<E>(
                title = field.description.toNotBlankStr(), getFun = { getter.call(it) })
        }.filter<AddColumn<E>> { it.title.isNotBlank() }
    return defaultColumns
}

