package com.addzero.web.ui.hooks.table.generic

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.common.consts.sql
import com.addzero.web.modules.sys.area.SysArea
import com.addzero.web.modules.sys.area.name
import com.addzero.web.ui.hooks.table.entity.AddColumn
import org.babyfish.jimmer.sql.kt.ast.expression.`ilike?`
import org.babyfish.jimmer.sql.kt.ast.table.makeOrders

// 示例数据实体
data class User(
    val id: Int,
    val name: String,
    val age: Int,
    val email: String,
    val active: Boolean
)

@Composable
fun GenericTableExample() {
    // 自定义列配置
    val addColumn = AddColumn<SysArea>(
//        key = TODO(),
        title = "名字是否有黑",
//        placeholder = TODO(),
//        defaultValue = TODO(),
//        getRenderType = TODO(),
//        required = TODO(),
//        validator = TODO(),
//        errorMessage = TODO(),
//        dependsOn = TODO(),
//        order = TODO(),
        getFun = { it.blackFlag },
        customRender = {
            val blackFlag = it.blackFlag!!
            if (blackFlag) {

                Text("名字包含黑")
            } else {
                Text("名字不包含黑")

            }

        }
    )

    Surface(modifier = Modifier.padding(16.dp)) {
        Column {
            GenericTable<SysArea>(
                columns = listOf(addColumn),
                onSearch = { viewModel ->
                    // 模拟搜索功能
                    val keyword = viewModel.searchText.lowercase()

                    val createQuery = sql.createQuery(SysArea::class) {
                        where(table.name `ilike?` keyword)
                        orderBy(table.makeOrders("sid asc"))
                        select(table)
                    }.fetchPage(viewModel.pageNo, viewModel.pageSize)
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
                getIdFun = { it.sid }
            )
        }
    }
}
