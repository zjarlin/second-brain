package com.addzero.web.ui.hooks.table

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cn.hutool.core.util.NumberUtil
import com.addzero.common.kt_util.FieldMetadata
import com.addzero.common.kt_util.getMetadata
import com.addzero.common.kt_util.toNotBlankStr
import com.addzero.web.ui.hooks.UseHook
import org.babyfish.jimmer.client.ApiIgnore
import kotlin.reflect.KClass


data class AddColumn<E>(
    val title: String, val getFun: (E) -> Any?, val customRender: @Composable (String) -> Unit = {
        Text(it)
    }
)

private fun <T : Any> defaultColumns(clazz: KClass<T>, excludeFields: Set<String> = setOf()): List<AddColumn<T>> {
    val metadata = clazz.getMetadata()
    val filter = metadata.fields.filter { !excludeFields.contains(it.property.name) }
        .map<FieldMetadata<T>, AddColumn<T>> { field ->
            val getter = field.property.getter
            AddColumn(
                title = field.description.toNotBlankStr(), getFun = { getter.call(it) })
        }.filter {
            it.title.isNotBlank()
        }
    return filter
}


class UseTable<E : Any>(
    private val clazz: KClass<E>,
    private val modifier: Modifier = Modifier,
    private val excludeFields: Set<String> = setOf(),
    private var columns: List<AddColumn<E>> = defaultColumns(clazz, excludeFields),
    val onValueChange: (UseTable<E>) -> Unit = {}
    ) : UseHook<UseTable<E>>() {
    var dataList: List<E> by mutableStateOf(listOf())
    var pageNo by mutableStateOf(0L)
    var pageSize by mutableStateOf(10L)
    var totalPages: Long = 0
    var searchText by mutableStateOf("")

    fun column(
        title: String, getFun: (E) -> Any?, customRender: @Composable (String) -> Unit = { Text(it) }
    ) {
        columns = columns + AddColumn(title, getFun, customRender)
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
    private fun renderTableRow(item: E) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            columns.forEach { column: AddColumn<E> ->
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
                renderCustomPageSizeBar {
                    {}
                }
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
    private fun renderCustomPageSizeBar(onDone: KeyboardActionScope.() -> Unit={}) {
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
            keyboardActions = KeyboardActions(onDone = onDone)
        )
    }


    @Composable
    @ApiIgnore
    override fun show(state: UseTable<E>) {
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


}

