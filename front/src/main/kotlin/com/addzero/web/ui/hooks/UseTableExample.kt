package com.addzero.web.ui.hooks

import androidx.compose.runtime.*
import com.addzero.common.consts.SpringVars
import com.addzero.web.modules.sys.area.entity.SysArea
import com.addzero.web.modules.sys.area.entity.city
import com.addzero.web.modules.sys.area.entity.name
import org.babyfish.jimmer.sql.kt.ast.expression.`ilike?`
import org.babyfish.jimmer.sql.kt.ast.expression.like
import org.babyfish.jimmer.sql.kt.ast.expression.`like?`
import org.babyfish.jimmer.sql.kt.ast.table.makeOrders

@Composable
fun UseTableExample() {
    // 定义表格列
    val columnsData = listOf(
        AddColumn<SysArea>(title = "主键", getColumnValue = { it.sid }),
        AddColumn(title = "上级", getColumnValue = { it.parentsid }),
        AddColumn(title = "城市", getColumnValue = { it.city }),
    )

    // 使用表格组件
    val useTable = UseTable(
        onValueChange = { loadData(it) }).apply {
        columns = columnsData
    }
    useTable.render()

}

fun loadData(state: UseTable<SysArea>) {
    val keyword = state.searchText
    val sql = SpringVars.sql
    val query = sql.createQuery(SysArea::class) {
         where(table.city `like?` keyword)
        orderBy(table.makeOrders("sid asc"))
        select(table)
    }
    val pageIndex = state.pageNo.toInt()
    val pageSize = state.pageSize.toInt()
    val fetchPage = query.fetchPage(pageIndex, pageSize = pageSize)
    val rows = fetchPage.rows
    val totalPageCount = fetchPage.totalPageCount


    state.dataList = rows
    state.totalPages = totalPageCount
}
