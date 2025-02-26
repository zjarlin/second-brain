package com.addzero.web.ui.hooks

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.babyfish.jimmer.client.ApiIgnore

data class AddColumn<T>(
    val title: String, val key: String, val render: (@Composable (T) -> Unit)? = null
)

class UseTable<T>(
    private val columns: List<AddColumn<T>>,
    private val dataList: List<T>,
    private val totalPages: Int,
    private val modifier: Modifier = Modifier
) : UseHook<UseTable<T>> {
    var pageNo by mutableStateOf(1)
    var pageSize by mutableStateOf(10)

    private val onPageChange: (Int, Int) -> Unit = { page, size ->
        pageNo = page
        pageSize = size
    }

    @Composable
    private fun renderCustomPageSize() {
        OutlinedTextField(
            value = pageSize.toString(),
            onValueChange = { s: String -> },
            modifier = Modifier.width(80.dp).padding(horizontal = 4.dp),
            singleLine = true,
            placeholder = { Text("自定义pageSize") })

        OutlinedButton(
            onClick = {}, enabled = pageSize.toString().isNotBlank(), modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            Text("确定")
        }
    }

    @Composable
    private fun renderPageNavigation() {
        Row(
            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text("页码: ")
            renderLastPageNo()
            renderCurrentBili()
            renderNextPageNo()
        }
    }

    @Composable
    private fun renderTableContent() {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(end = 12.dp)
        ) {
            items(dataList) { item ->
                renderTableRow(item)
            }
        }
    }

    @Composable
    private fun renderTableRow(item: T) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            columns.forEach { column: AddColumn<T> ->
                Box(modifier = Modifier.weight(1f)) {
                    if (column.render != null) {
                        column.render.invoke(item)
                    } else {
                        Text(item.toString())
                    }
                }
            }
        }
        HorizontalDivider()
    }

    @Composable
    private fun renderFooter() {
        Box(modifier = Modifier) {
            Row(
                modifier = modifier.fillMaxWidth().padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                renderPageNavigation()
                renderMultiSelectOutlinedButton()
                renderCustomPageSize()
            }
        }
    }

    @Composable
    private fun renderMultiSelectOutlinedButton() {
        Text("每页显示: ")
        listOf(10, 30, 50, 100).forEach { size ->
            OutlinedButton(
                onClick = { onPageChange(1, size) }, modifier = Modifier.padding(horizontal = 4.dp)
            ) {
                Text("$size")
            }
        }
    }

    @Composable
    private fun renderNextPageNo() {
        OutlinedButton(
            onClick = {
                if (pageNo < totalPages) {
                    onPageChange(pageNo + 1, pageSize)
                }
            }, enabled = pageNo < totalPages
        ) {
            Text("下一页")
        }
    }

    @Composable
    private fun renderCurrentBili() {
        Text(
            "$pageNo/$totalPages", modifier = Modifier.padding(horizontal = 8.dp)
        )
    }

    @Composable
    private fun renderLastPageNo() {
        OutlinedButton(
            onClick = {
                if (pageNo > 1) {
                    onPageChange(pageNo - 1, pageSize)
                }
            }, enabled = pageNo > 1
        ) {
            Text("上一页")
        }
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
                    modifier = Modifier.fillMaxWidth().weight(1f, fill = false)
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(end = 12.dp)
                    ) {
                        items(dataList) { item ->
                            renderTableRow(item)
                        }
                    }
                }
            }

            // 分页控件
            renderFooter()
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
