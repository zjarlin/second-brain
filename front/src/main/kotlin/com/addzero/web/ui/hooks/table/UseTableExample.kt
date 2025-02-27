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

/**
 * UseTable组件使用示例
 *
 * 本示例展示了如何使用UseTable组件来展示Jimmer实体数据，包括：
 * 1. 基础表格展示
 * 2. 自定义列渲染
 * 3. 搜索功能
 * 4. 分页功能
 */
@Composable
fun UseTableExample() {
    // 指定要展示的实体类
    val clazz = SysArea::class

    val columns = listOf<AddColumn<SysArea>>(
        // 基础文本列
        AddColumn(title = "主键", getFun = { it.sid }),
        AddColumn(title = "上级", getFun = { it.parentsid }),
        AddColumn(title = "城市", getFun = { it.city }),

        // 自定义开关组件列
        AddColumn(
            title = "开关样式",
            getFun = { it.blackFlag },
            customRender = { value ->
                val checked = value.contains("true")
                Switch(
                    checked = checked,
                    onCheckedChange = { /* 处理开关状态变化 */ }
                )
            }
        ),

        // 自定义条件渲染列
        AddColumn(
            title = "城市名字是否有黑",
            getFun = { it.blackFlag },
            customRender = { value ->
                if (value.contains("true")) {
                    Text("名字包含黑", color = Color.Black)
                } else {
                    Text("名字不包含黑", color = Color.Red)
                }
            }
        )
    )
    val useTable = UseTable(
        clazz = clazz,
        columns = columns,
        onValueChange = { state ->
            // 获取搜索关键字
            val keyword = state.searchText
            val sql = SpringVars.sql

            // 创建查询
            val query = sql.createQuery(clazz) {
                // 根据城市名称模糊搜索
                where(table.city `like?` keyword)
                // 按sid升序排序
                orderBy(table.makeOrders("sid asc"))
                select(table)
            }

            // 获取分页参数
            val pageIndex = state.pageNo.toInt()
            val pageSize = state.pageSize.toInt()

            // 执行分页查询
            val fetchPage = query.fetchPage(pageIndex, pageSize = pageSize)
            val rows = fetchPage.rows
            val totalPageCount = fetchPage.totalPageCount

            // 更新表格数据和总页数
            state.dataList = rows
            state.totalPages = totalPageCount
        }
    )

}

