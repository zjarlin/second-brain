package com.addzero.web.ui.hooks.table

import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import com.addzero.common.consts.SpringVars
import com.addzero.web.modules.sys.area.entity.SysArea
import com.addzero.web.modules.sys.area.entity.city
import org.babyfish.jimmer.sql.kt.ast.expression.`like?`
import org.babyfish.jimmer.sql.kt.ast.table.makeOrders
import androidx.compose.ui.graphics.Color

@Composable
fun UseTableExample() {
    // 定义表格列
    val columnsData = listOf(
        AddColumn(title = "主键", getFun = { it.sid }),
        AddColumn(title = "上级", getFun = { it.parentsid }),
        AddColumn(title = "城市", getFun = { it.city }),
        AddColumn(title = "是否包含黑开关样式", getFun = { it.blackFlag }) {
            val contains = it.contains("true")
            Switch(
                checked = contains, onCheckedChange = { newVal -> newVal.not() })
        },
        AddColumn<SysArea>(title = "城市名字是否有黑", getFun = { it.blackFlag }) {
            if (it.contains("true")) {
                Text("名字包含黑", color = Color.Black)
            } else {
                Text("名字不包含黑", color = Color.Red)
            }

        },
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
