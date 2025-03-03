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
import com.addzero.common.kt_util.toNotBlankStr
import com.addzero.web.ui.hooks.form.DynamicFormComponent
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
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf<E?>(null) }

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
                    viewModel.pageNo = 1  // 重置页码为1
                    onSearch(viewModel)
                }
            )

            // 表格主体区域
            Surface(modifier = Modifier.weight(1f)) {
                TableContent(
                    columns = viewModel.columns,
                    dataList = viewModel.dataList,
                    onEdit = { item ->
                        selectedItem = item
                        showEditDialog = true
                    },
                    onDelete = { item ->
                        selectedItem = item
                        showDeleteDialog = true
                    },
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

        // 编辑对话框
        if (showEditDialog && selectedItem != null) {
            AlertDialog(
                onDismissRequest = { showEditDialog = false },
                title = { Text("编辑") },
                text = {
                    DynamicFormComponent(
                        columns = viewModel.columns.map { column ->
                            // 直接使用AddColumn，不需要转换
                            column.copy(
                                setFun = { item, value ->
                                    item
                                    // 使用Jimmer的Draft机制修改不可变实体
                                    // 1. 获取属性名（尝试多种匹配方式）

//                                    val getFun = column.getFun
//
////                                        咋把item变成draft呀
//                                        val draftObj = item.toDraft()
//
//                                        val toTypedProp = getFun.toTypedProp()
//
//                                        DraftObjects.set(draftObj, toTypedProp, value)
//
//                                        //再把draft换成jimmer对象
//                                        val updatedItem = draftObj.toEntity()
//                                        // 5. 返回新的不可变实体
//                                        return@copy updatedItem
//                                        println("使用Jimmer Draft修改实体失败: ${e.message}")
//                                        // 如果Draft修改失败，返回原始对象
//                                        return@copy item
                                }
                            )
                        },
                        data = selectedItem as E,
                        onDataChange = { updatedItem ->
                            selectedItem = updatedItem
                            // 触发数据更新
                            onSearch(viewModel)
                        }
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            selectedItem?.let { onEdit(it) }
                            showEditDialog = false
                        }
                    ) {
                        Text("确定")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showEditDialog = false }) {
                        Text("取消")
                    }
                }
            )
        }

        // 删除确认对话框
        if (showDeleteDialog && selectedItem != null) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("确认删除") },
                text = { Text("确定要删除这条记录吗？") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            selectedItem?.let { onDelete(it) }
                            showDeleteDialog = false
                        }
                    ) {
                        Text("确定")
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

    Column(modifier = Modifier.fillMaxWidth()) {
        // 表头区域
        Row(modifier = Modifier.fillMaxWidth()) {
            // 序号表头
            Box(
                modifier = Modifier.width(60.dp).padding(8.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "序号",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
            
            // 数据列表头
            Box(
                modifier = Modifier
                    .weight(1f)
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
                }
            }
            
            // 操作列表头
            Box(
                modifier = Modifier.width(120.dp).padding(8.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "操作",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        }
        
        // 使用单个LazyColumn渲染所有数据行
        LazyColumn {
            items(dataList, key = getIdFun) { item ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    // 序号列
                    Box(
                        modifier = Modifier.width(60.dp).padding(8.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = (dataList.indexOf(item) + 1).toString(),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                    }
                    
                    // 数据列
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .horizontalScroll(horizontalScrollState)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(8.dp)
                                .width(IntrinsicSize.Max),
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
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    
                    // 操作列
                    Box(
                        modifier = Modifier.width(120.dp).padding(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
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
                }
                HorizontalDivider()
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

