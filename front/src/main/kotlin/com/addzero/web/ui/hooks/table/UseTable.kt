package com.addzero.web.ui.hooks.table

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cn.hutool.core.util.NumberUtil
import com.addzero.common.kt_util.add
import com.addzero.common.kt_util.toNotBlankStr
import com.addzero.web.ui.hooks.UseHook
import org.babyfish.jimmer.client.ApiIgnore


data class AddColumn<T>(
    val title: String, val getFun: (T) -> Any? ,  val customRender: @Composable (String) -> Unit = {
        Text(it)
    }
)

class UseTable<T>(
    private val modifier: Modifier = Modifier, private val onValueChange: ((state: UseTable<T>) -> Unit) = {}
) : UseHook<UseTable<T>>() {
    var columns by mutableStateOf<List<AddColumn<T>>>(listOf())
    var dataList by mutableStateOf<List<T>>(listOf())
    var pageNo by mutableStateOf(0L)
    var pageSize by mutableStateOf(10L)
    var totalPages: Long = 0
    var searchText by mutableStateOf("")

    fun column(
        title: String,
        getFun: (T) -> Any?,
        customRender: @Composable (String) -> Unit = { Text(it) }
    ) {
        val currentNode = AddColumn(title, getFun, customRender)
        columns.add(currentNode)
    }



    @Composable
    private fun renderSearchBar() {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = state.searchText,
                onValueChange = { state.searchText = it },
                modifier = Modifier.weight(1f).padding(end = 8.dp),
                placeholder = { Text("请输入搜索关键词") },
                singleLine = true
            )
            Button(
                onClick = {
                    state.pageNo = 0
                    onValueChange(state)
                }, modifier = Modifier.height(56.dp)
            ) {
                Text("搜索")
            }
        }
    }

    @Composable
    private fun renderPageNavigation() {
        Row(
            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = {
                    if (state.pageNo > 0) {
                        state.pageNo -= 1
                    }
                },
                enabled = state.pageNo > 0,
                modifier = Modifier.height(36.dp),
                contentPadding = PaddingValues(horizontal = 12.dp)
            ) {
                Text("上一页", style = MaterialTheme.typography.bodyMedium)
            }

            Text(
                "${state.pageNo}/${state.totalPages} ",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            OutlinedButton(
                onClick = {
                    if (state.pageNo < state.totalPages) {
                        state.pageNo += 1
                    }
                },
                enabled = state.pageNo < state.totalPages,
                modifier = Modifier.height(36.dp),
                contentPadding = PaddingValues(horizontal = 12.dp)
            ) {
                Text("下一页", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }

    /**
     * 渲染每一行
     * @param [item]
     */
    @Composable
    private fun renderTableRow(item: T) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            state.columns.forEach { column: AddColumn<T> ->
                Box(modifier = Modifier.weight(1f)) {
                    val any = column.getFun(item)
                    val toNotBlankStr = any.toNotBlankStr()
                        column.customRender(toNotBlankStr)
                }
            }
        }
        HorizontalDivider()
    }

    @Composable
    private fun renderFooter() {
        Surface(
            modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.surface
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                renderPageNavigation()
                Spacer(modifier = Modifier.width(16.dp))
                renderMultiSelectOutlinedButton()
                renderCustomPageSizeBar()
            }
        }
    }

    @Composable
    private fun renderMultiSelectOutlinedButton() {
        Text(
            "每页显示: ", style = MaterialTheme.typography.bodyMedium
        )
        listOf(10, 30, 50, 100).forEach { size ->
            OutlinedButton(
                onClick = {
                    state.pageNo = 1
                    state.pageSize = size.toLong()
                },
                modifier = Modifier.padding(horizontal = 4.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text("$size")
            }
        }
    }

    @Composable
    private fun renderCustomPageSizeBar() {
        OutlinedTextField(
            value = state.pageSize.toString(),
            onValueChange = { newValue ->
                if (newValue.isNotBlank() && NumberUtil.isNumber(newValue)) {
                    val newSize = newValue.toInt()
                    if (newSize > 0) {
                        state.pageNo = 1
                        state.pageSize = newSize.toLong()
                    }
                }
            },
            modifier = Modifier.width(80.dp).padding(horizontal = 4.dp),
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium,
            placeholder = { Text("自定义", style = MaterialTheme.typography.bodyMedium) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            keyboardActions = KeyboardActions(onDone = {
                onValueChange
            })
        )
    }


    @Composable
    @ApiIgnore
    override fun show(state: UseTable<T>) {

        LaunchedEffect(state.pageNo, state.pageSize, state.dataList) {
            onValueChange(state)
        }

        Box(modifier = modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                // 搜索栏
                renderSearchBar()

                // 表格头部
                renderHeader()

                //分割线
                HorizontalDivider()

                // 表格内容
                Box(
                    modifier = Modifier.fillMaxWidth().weight(1f)
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(end = 12.dp)
                    ) {
                        items(state.dataList) { item ->
                            renderTableRow(item)
                        }
                    }
                }

                // 分页控件
                renderFooter()
            }
        }
    }

    @Composable
    private fun renderHeader() {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            state.columns.forEach { column ->
                Text(
                    text = column.title, style = MaterialTheme.typography.titleSmall, modifier = Modifier.weight(1f)
                )
            }
        }
    }

//    fun <T> defaultColumns(clazz: Class<T>, excludeFields: Set<String> = setOf()) {
//        val fields = clazz.declaredFields.filter { !excludeFields.contains(it.name) }
//        val columnList = mutableListOf<AddColumn<T>>()
//
//        val map = fields.map { field ->
//            field.isAccessible = true
//            val schema = field.getAnnotation(Schema::class.java)
//            val title = schema?.description ?: field.name.replaceFirstChar { it.uppercase() }
//
//            val element = AddColumn<T>(
//                title = title,
//                getFun = {
//                    field.get(it)
//                }
//            )
//            element
//        }.toList()
//
//
//        columns = mutableStateOf(map)
//    }
}
