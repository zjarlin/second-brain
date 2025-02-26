package com.addzero.web.ui.hooks

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
import cn.hutool.core.convert.Convert
import cn.hutool.core.util.NumberUtil
import cn.hutool.core.util.ReflectUtil
import org.babyfish.jimmer.client.ApiIgnore

data class AddColumn<T>(
    val title: String, val key: String, val customRender: (@Composable (AddColumn<T>, T) -> Unit) = { col, item ->
        val fieldValue = ReflectUtil.getFieldValue(item, col.key)
        val toStr = Convert.toStr(fieldValue)
        Text(toStr)
    }
)

class UseTable<T>(
    private val totalPages: Int, private val modifier: Modifier = Modifier
) : UseHook<UseTable<T>> {
    var columns by mutableStateOf<List<AddColumn<T>>>(listOf())
    var dataList by mutableStateOf<List<T>>(listOf())
    var pageNo by mutableStateOf(1)
    var pageSize by mutableStateOf(10)


    @Composable
    private fun renderPageNavigation() {
        Row(
            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = {
                    if (pageNo > 1) {
                        pageNo - 1
                    }
                },
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
                onClick = {
                    if (pageNo < totalPages) {
                        pageNo + 1
                    }
                },
                enabled = pageNo < totalPages,
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
            columns.forEach { column: AddColumn<T> ->
                Box(modifier = Modifier.weight(1f)) {
                    column.customRender(column, item)
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
                renderCustomPageSize()
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
                    pageNo = 1
                    pageSize = size
                },
                modifier = Modifier.padding(horizontal = 4.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text("$size")
            }
        }
    }

    @Composable
    private fun renderCustomPageSize() {
        OutlinedTextField(
            value = pageSize.toString(),
            onValueChange = {
                if (it.isNotBlank() && NumberUtil.isNumber(it) && it.toInt() > 0) {
                    pageNo = 1
                    pageSize = it.toInt()
                }
            },
            modifier = Modifier.width(80.dp).padding(horizontal = 4.dp),
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium,
            placeholder = { Text("自定义", style = MaterialTheme.typography.bodyMedium) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            keyboardActions = KeyboardActions(onDone = {})
        )
    }


    @Composable
    @ApiIgnore
    override fun show(state: UseTable<T>) {
        Box(modifier = modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
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
                        items(dataList) { item ->
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
            columns.forEach { column ->
                Text(
                    text = column.title, style = MaterialTheme.typography.titleSmall, modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
