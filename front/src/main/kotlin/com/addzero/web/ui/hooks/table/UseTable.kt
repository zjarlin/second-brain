package com.addzero.web.ui.hooks.table

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.common.kt_util.getMetadata
import com.addzero.common.kt_util.toNotBlankStr
import kotlin.reflect.KClass


@Composable
fun <E : Any> UseTable(
    clazz: KClass<E>,
    modifier: Modifier = Modifier,
    excludeFields: Set<String> = setOf(),
    columns: (List<AddColumn<E>>.() -> Unit)? = null,
    onValueChange: (TableState<E>) -> Unit = {}
) {
    val columnList: List<AddColumn<E>> = listOf()

    val state = remember {
        columns?.let { columnList.it() }
        val tableState = TableState(
            clazz = clazz, initialColumns = mergeColumns(clazz, columnList, excludeFields)
        )
        tableState
    }

    LaunchedEffect(state.pageNo, state.pageSize) {
        onValueChange(state)
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // 搜索栏
            SearchBar(searchText = state.searchText, onSearchTextChange = { state.searchText = it }, onSearch = {
                state.search()
                onValueChange(state)
            })

            // 表格头部
            TableHeader(state.columns)

            HorizontalDivider()

            // 表格内容
            Box(modifier = Modifier.weight(1f)) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(end = 12.dp)
                ) {
                    items(state.dataList) { item ->
                        TableRow(item, state.columns)
                    }
                }
            }

            // 分页控件
            Surface(
                modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.surface
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
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
                        currentPageSize = state.pageSize, onPageSizeChange = { state.updatePageSize(it) })
                    CustomPageSizeInput(
                        currentPageSize = state.pageSize, onPageSizeChange = { state.updatePageSize(it) })
                }
            }
        }
    }
}

@Composable
private fun TableHeader(
    columns: List<AddColumn<*>>, modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween
    ) {
        columns.forEach { column ->
            Text(
                text = column.title, style = MaterialTheme.typography.titleSmall, modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun <E> TableRow(
    item: E, columns: List<AddColumn<E>>, modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween
    ) {
        columns.forEach { column ->
            Box(modifier = Modifier.weight(1f)) {
                val value = column.getFun(item)
                val displayText = value.toNotBlankStr()
                column.customRender(displayText)
            }
        }
    }
}

private fun <T : Any> defaultColumns(clazz: KClass<T>, excludeFields: Set<String> = setOf()): List<AddColumn<T>> {
    val metadata = clazz.getMetadata()
    return metadata.fields.filter { !excludeFields.contains(it.property.name) }.map { field ->
        val getter = field.property.getter
        AddColumn<T>(
            title = field.description.toNotBlankStr(), getFun = { getter.call(it) })
    }.filter { it.title.isNotBlank() }
}

private fun <T : Any> mergeColumns(
    clazz: KClass<T>, customColumns: List<AddColumn<T>>, excludeFields: Set<String>
): List<AddColumn<T>> {
    val defaultCols = defaultColumns(clazz, excludeFields)
    val customColMap = customColumns.associateBy { it.title }

    return defaultCols.map { defaultCol ->
        customColMap[defaultCol.title] ?: defaultCol
    } + customColumns.filter { custom ->
        defaultCols.none { it.title == custom.title }
    }
}
