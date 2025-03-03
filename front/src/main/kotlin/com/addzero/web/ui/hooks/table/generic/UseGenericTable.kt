package com.addzero.web.ui.hooks.table.generic

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.addzero.common.kt_util.addAllIfAbsentByKey
import com.addzero.web.ui.hooks.table.common.UseSearch
import com.addzero.web.ui.hooks.table.common.UseTableLayout
import com.addzero.web.ui.hooks.table.common.UseTablePagination
import com.addzero.web.ui.hooks.table.entity.AddColumn

/**
 * 通用表格组件
 */
@Composable
inline fun <reified E : Any> GenericTable(
    excludeFields: MutableSet<String> = mutableSetOf() ,
    columns: List<AddColumn<E>> = emptyList(),
    crossinline onSearch: (GenericTableViewModel<E>) -> Unit = {},
    noinline onEdit: (E) -> Unit = {},
    noinline onDelete: (E) -> Unit = {},
    noinline getIdFun: (E) -> Any = { it.hashCode() }
) {
    val viewModel = remember { GenericTableViewModel<E>() }

    val useSearch = UseSearch(onSearch = {
    onSearch(viewModel)
    }).getState().apply {
       viewModel.searchText = searchText
    }

    val useTableLayout = UseTableLayout<E>() .apply {
       viewModel.dataList = dataList
    }

    val useTablePagination = UseTablePagination() .apply {
       viewModel.pageNo=pageNo
        viewModel.pageSize=pageSize
        viewModel.totalPages=totalPages
    }



    LaunchedEffect(E::class) {
        val defaultColumns = viewModel.getDefaultColumns(E::class, excludeFields)
        val toMutableList = columns.toMutableList()
         defaultColumns.addAllIfAbsentByKey(
            toMutableList
        ) {
            it.title
        }
        viewModel.columns = defaultColumns
    }

    // 初始化数据加载
    LaunchedEffect(Unit) {
        onSearch(viewModel)
    }

    Box {
        Column {
            // 搜索栏
            useSearch.render()

            // 表格主体区域
            Surface(modifier = Modifier.weight(1f)) {

                useTableLayout.apply {
                    this.columns = viewModel.columns
                    this.dataList = viewModel.dataList
                    this.onEdit = onEdit
                    this.onDelete = onDelete
                    this.getIdFun = getIdFun
                    render()
                }
            }

            // 分页控件
            useTablePagination.render()
        }
    }
}

