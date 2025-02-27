package com.addzero.web.ui.hooks.table

import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.addzero.common.consts.SpringVars
import com.addzero.web.modules.sys.area.entity.SysArea
import com.addzero.web.modules.sys.area.entity.city
import org.babyfish.jimmer.sql.kt.ast.expression.`like?`
import org.babyfish.jimmer.sql.kt.ast.table.makeOrders

@Composable
fun UseTableExample() {
    // 使用表格组件
    val useTable = UseTable<SysArea>(
        onValueChange = {
            val keyword = it.searchText
            val sql = SpringVars.sql
            val query = sql.createQuery(SysArea::class) {
                where(table.city `like?` keyword)
                orderBy(table.makeOrders("sid asc"))
                select(table)
            }
            val pageIndex = it.pageNo.toInt()
            val pageSize = it.pageSize.toInt()
            val fetchPage = query.fetchPage(pageIndex, pageSize = pageSize)
            val rows = fetchPage.rows
            val totalPageCount = fetchPage.totalPageCount
            it.dataList = rows
            it.totalPages = totalPageCount
        }
    ).apply {
        column(title = "主键", getFun = { it.sid })
        column(title = "上级", getFun = { it.parentsid })
        column(title = "城市", getFun = { it.city })
        column(
            title = "是否包含黑开关样式",
            getFun = { it.blackFlag }
        ) {
            val contains = it.contains("true")
            Switch(checked = contains, onCheckedChange = { newVal -> newVal.not() })
        }
        column(
            title = "城市名字是否有黑",
            getFun = { it.blackFlag }
        ) { value ->
            if (value.contains("true")) {
                Text("名字包含黑", color = Color.Black)
            } else {
                Text("名字不包含黑", color = Color.Red)
            }
        }
    }

    useTable.render()

}

