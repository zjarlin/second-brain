package com.addzero.web.ui.hooks.table

import androidx.compose.material3.MaterialTheme
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
    UseTable<SysArea>(
        excludeFields = setOf("createTime", "updateTime"),
        columns = listOf(
            // 基础文本列
            AddColumn(
                title = "主键",
                getFun = { it.sid }
            ),
            AddColumn(
                title = "城市",
                getFun = { it.city }
            ),
            AddColumn(
                title = "上级",
                getFun = { it.parentsid }
            ),
            // 自定义渲染列 - 开关样式
            AddColumn(
                title = "开关样式",
                getFun = { it.blackFlag },
                customRender = { value ->
                    val checked = value.contains("true")
                    Switch(checked = checked, onCheckedChange = {})
                }
            ),
            // 自定义渲染列 - 带颜色文本
            AddColumn(
                title = "城市名字是否有黑",
                getFun = { it.blackFlag },
                customRender = { value ->
                    val color = if (value.contains("true")) Color.Black else Color.Red
                    val text = if (value.contains("true")) "名字包含黑" else "名字不包含黑"
                    Text(
                        text = text,
                        color = color,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            )
        )
    ) { state ->
        // 获取搜索关键字和分页参数
        val keyword = state.searchText
        val pageIndex = state.pageNo.toInt()
        val pageSize = state.pageSize.toInt()

        // 创建并执行查询
        val query = SpringVars.sql.createQuery(SysArea::class) {
            where(table.city `like?` keyword)
            orderBy(table.makeOrders("sid asc"))
            select(table)
        }

        // 执行分页查询并更新表格数据
        val fetchPage = query.fetchPage(pageIndex, pageSize = pageSize)
        state.updateData(
            data = fetchPage.rows,
            total = fetchPage.totalRowCount
        )
    }
}

