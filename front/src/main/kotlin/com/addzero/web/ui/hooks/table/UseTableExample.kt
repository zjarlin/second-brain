package com.addzero.web.ui.hooks.table

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.addzero.common.consts.SpringVars
import com.addzero.web.modules.sys.area.entity.SysArea
import com.addzero.web.modules.sys.area.entity.city
import com.addzero.web.ui.hooks.table.AddColumn.Companion.`+`
import org.babyfish.jimmer.sql.kt.ast.expression.`like?`
import org.babyfish.jimmer.sql.kt.ast.table.makeOrders

@Composable
fun UseTableExample() {
    val clazz = SysArea::class
    UseTable(
        clazz = clazz, excludeFields = setOf("createTime", "updateTime"), // 排除不需要显示的字段
        columns = {
            `+`<SysArea>(title = "主键", getFun = { it.sid })
            `+`<SysArea>(title = "城市", getFun = { it.city })
            `+`<SysArea>(title = "上级", getFun = { it.parentsid })
            `+`<SysArea>(title = "开关样式", getFun = { it.blackFlag }, customRender = {
                val checked = it.contains("true")
                Switch(
                    checked = checked, onCheckedChange = {})
            })
            `+`<SysArea>(title = "城市名字是否有黑", getFun = { it.blackFlag }) {
                val color = if (it.contains("true")) Color.Black else Color.Red
                val text = if (it.contains("true")) "名字包含黑" else "名字不包含黑"
                Text(
                    text = text, color = color, style = MaterialTheme.typography.bodyMedium
                )
            }
        }) { state ->
        // 获取搜索关键字和分页参数
        val keyword = state.searchText
        val pageIndex = state.pageNo.toInt()
        val pageSize = state.pageSize.toInt()

        // 创建并执行查询
        val query = SpringVars.sql.createQuery(clazz) {
            where(table.city `like?` keyword)
            orderBy(table.makeOrders("sid asc"))
            select(table)
        }

        // 执行分页查询并更新表格数据
        val fetchPage = query.fetchPage(pageIndex, pageSize = pageSize)
        state.updateData(
            data = fetchPage.rows, total = fetchPage.totalRowCount
        )
    }
}

