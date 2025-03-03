package com.addzero.web.ui.hooks.table.generic

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.addzero.common.kt_util.addAllIfAbsentByKey
import com.addzero.web.ui.hooks.table.entity.AddColumn

/**
 * 通用表格组件
 */
@Composable
inline fun <reified E : Any> GenericTable(
    excludeFields: MutableSet<String> = mutableSetOf(),
    columns: List<AddColumn<E>> = emptyList(),
    crossinline onSearch: (GenericTableViewModel<E>) -> Unit = {},
    noinline onEdit: (E) -> Unit = {},
    noinline onDelete: (E) -> Unit = {},
    noinline getIdFun: (E) -> Any = { it.hashCode() },
    modifier: Modifier = Modifier.fillMaxSize()
) {
    val viewModel = remember { GenericTableViewModel<E>() }
    var searchText by remember { mutableStateOf("") }

    // 初始化数据加载
//    LaunchedEffect(Unit) {
//        onSearch(viewModel)
//    }

    // 监听分页变化
    LaunchedEffect(viewModel.pageNo, viewModel.pageSize) {
        onSearch(viewModel)
    }

    LaunchedEffect(E::class) {
        val defaultColumns = viewModel.getDefaultColumns(E::class, excludeFields)
        val toMutableList = columns.toMutableList()
        defaultColumns.addAllIfAbsentByKey(toMutableList) { it.title }
        viewModel.columns = defaultColumns
    }

    Box {
        Column {
            // 搜索栏
            SearchBar(
                searchText = searchText,
                onSearchTextChange = { searchText = it },
                onSearch = {
                    viewModel.searchText = searchText
                    onSearch(viewModel)
                }
            )

            // 表格主体区域
            Surface(modifier = Modifier.weight(1f)) {
                TableContent(
                    columns = viewModel.columns,
                    dataList = viewModel.dataList,
                    onEdit = onEdit,
                    onDelete = onDelete,
                    getIdFun = getIdFun
                )
            }

            // 分页控件
            PaginationBar(
                pageNo = viewModel.pageNo,
                pageSize = viewModel.pageSize,
                totalPages = viewModel.totalPages,
                onPageChange = { viewModel.pageNo = it },
                onPageSizeChange = { viewModel.pageSize = it }
            )
        }
    }
}

@Composable
fun SearchBar(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = searchText,
            onValueChange = onSearchTextChange,
            modifier = Modifier.weight(1f).padding(end = 8.dp),
            placeholder = { Text("请输入搜索关键词") },
            singleLine = true
        )
        Button(
            onClick = onSearch,
            modifier = Modifier.height(56.dp)
        ) {
            Text("搜索")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <E : Any> TableContent(
    columns: List<AddColumn<E>>,
    dataList: List<E>,
    onEdit: (E) -> Unit,
    onDelete: (E) -> Unit,
    getIdFun: (E) -> Any
) {
    val horizontalScrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxWidth()) {
        Column {
            // 表头
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(horizontalScrollState)
            ) {
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .width(IntrinsicSize.Max),
                    horizontalArrangement = Arrangement.Start
                ) {
                    columns.forEach { column ->
                        Text(
                            text = column.title,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.width(150.dp).padding(horizontal = 4.dp)
                        )
                    }
                    Text(
                        text = "操作",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.width(120.dp).padding(horizontal = 4.dp)
                    )
                }
            }

            // 数据行
            LazyColumn(
                modifier = Modifier.fillMaxWidth().horizontalScroll(horizontalScrollState)
            ) {
                items(dataList, key = getIdFun) { item ->
                    Row(
                        modifier = Modifier
                            .width(IntrinsicSize.Max)
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        columns.forEach { column ->
                            Box(
                                modifier = Modifier
                                    .width(150.dp)
                                    .padding(horizontal = 4.dp)
                                    .height(IntrinsicSize.Min)
                            ) {
                                val content = column.getFun(item).toString()
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
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }
                        }
                        Row(
                            modifier = Modifier.width(120.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            IconButton(onClick = { onEdit(item) }) {
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
        }
    }
}

@Composable
fun PaginationBar(
    pageNo: Int,
    pageSize: Int,
    totalPages: Int,
    onPageChange: (Int) -> Unit,
    onPageSizeChange: (Int) -> Unit
) {
    Surface(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { onPageChange(pageNo - 1) },
                    enabled = pageNo > 1,
                    modifier = Modifier.height(36.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp)
                ) {
                    Text("上一页", style = MaterialTheme.typography.bodyMedium)
                }

                Text(
                    "$pageNo/$totalPages",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                OutlinedButton(
                    onClick = { onPageChange(pageNo + 1) },
                    enabled = pageNo < totalPages,
                    modifier = Modifier.height(36.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp)
                ) {
                    Text("下一页", style = MaterialTheme.typography.bodyMedium)
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(
                    "每页显示: ",
                    style = MaterialTheme.typography.bodyMedium
                )
                listOf(10, 30, 50, 100).forEach { size ->
                    OutlinedButton(
                        onClick = { onPageSizeChange(size) },
                        modifier = Modifier.padding(horizontal = 4.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text("$size")
                    }
                }

                OutlinedTextField(
                    value = pageSize.toString(),
                    onValueChange = { newValue ->
                        newValue.toIntOrNull()?.let { onPageSizeChange(it) }
                    },
                    modifier = Modifier.width(80.dp).padding(horizontal = 4.dp),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyMedium,
                    placeholder = { Text("自定义", style = MaterialTheme.typography.bodyMedium) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        }
    }
}

