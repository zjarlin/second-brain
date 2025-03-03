package com.addzero.web.ui.hooks.table.generic

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.common.consts.sql
import com.addzero.common.kt_util.add
import com.addzero.web.modules.sys.area.SysArea
import com.addzero.web.modules.sys.area.city
import com.addzero.web.ui.hooks.table.entity.AddColumn
import org.babyfish.jimmer.sql.kt.ast.expression.`ilike?`
import org.babyfish.jimmer.sql.kt.ast.table.makeOrders

@Composable
fun GenericTableExample() {
    // 自定义列配置
    val addColumn = AddColumn<SysArea>("名字是否有黑", getFun = { it.blackFlag }) {
        val blackFlag = it.blackFlag
        if (blackFlag == true) {
            Text("名字包含黑")
        } else {
            Text("名字不包含黑")
        }
    }
    val toList = (1..100)
        .map {
            val addColumn1 = AddColumn<SysArea>(
                title = it.toString(),
                getFun = { it.toString() },
            )
            addColumn1
        }.toList()
    toList.add(addColumn)


    Surface(modifier = Modifier.padding(16.dp)) {
        Column {
            GenericTable<SysArea>(
                columns =toList,
                onSearch = { viewModel ->
                    // 模拟搜索功能
                    val keyword = viewModel.searchText.lowercase()
                    val createQuery = sql.createQuery(SysArea::class) {
                        where(table.city `ilike?` keyword)
                        orderBy(table.makeOrders("sid asc"))
                        select(table)
                    }.fetchPage(viewModel.pageNo - 1, viewModel.pageSize)
                    viewModel.dataList = createQuery.rows
                    viewModel.totalPages = createQuery.totalPageCount.toInt()

                },
                onEdit = { user ->
                    // 处理编辑操作
                    println("编辑用户: $user")
                },
                onDelete = { user ->
                    // 处理删除操作

                },
            )
        }
    }
}
