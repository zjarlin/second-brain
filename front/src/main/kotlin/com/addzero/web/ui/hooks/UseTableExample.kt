package com.addzero.web.ui.hooks

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.addzero.common.consts.SpringVars
import com.addzero.web.modules.sys.area.entity.SysArea

@Composable
fun UseTableExample() {
    // 定义表格列
    val columnsData = listOf(
        AddColumn<SysArea>(title = "id", key = "id"),
        AddColumn(title = "parentsid", key = "parentsid"),
        AddColumn(title = "leveltype", key = "leveltype"),
        AddColumn(title = "name", key = "name"),
    )

    // 使用表格组件
    val useTable = UseTable(
        onValueChange = { loadData(it) }).apply {
        columns = columnsData
    }

    val render = useTable.render()

}

fun loadData(state: UseTable<SysArea>) {
    val keyword = state.searchText
    val sql = SpringVars.sql
    val query = sql.createQuery(SysArea::class) {
        select(table)
    }
    val fetchPage = query.fetchPage(state.pageNo.toInt(), pageSize = state.pageSize.toInt())
    state.dataList = fetchPage.rows
    state.totalPages = fetchPage.totalPageCount
}
