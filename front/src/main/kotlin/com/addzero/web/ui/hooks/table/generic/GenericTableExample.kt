package com.addzero.web.ui.hooks.table.generic

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.common.consts.sql
import com.addzero.web.modules.sys.area.SysArea
import com.addzero.web.modules.sys.area.city
import com.addzero.web.modules.sys.area.name
import com.addzero.web.ui.hooks.table.entity.JimmerColumn
import com.addzero.web.ui.hooks.table.generic.dialog.UseDialog
import org.babyfish.jimmer.Page
import org.babyfish.jimmer.sql.kt.ast.expression.`ilike?`
import org.babyfish.jimmer.sql.kt.ast.expression.or
import org.babyfish.jimmer.sql.kt.ast.table.makeOrders

@Composable
fun GenericTableExample() {
// 自定义列配置
    val addColumn = JimmerColumn<SysArea>("名字是否有黑", getFun = { it.blackFlag })
        .apply {
        customRender = {
            val blackFlag = it.blackFlag
            val useDialog = UseDialog("点我干嘛")
            Switch(
                checked = blackFlag == true, onCheckedChange = {
                    useDialog.apply {
                        showFlag = true
                    }
                })
            useDialog.getState().render()
        }
    }
    //随机生成100个自定义列
    val toList = (1..100).map {
        val title = it.toString()
        val addColumn1 = JimmerColumn<SysArea>(
            title = title,
            getFun = { title },
        )
        addColumn1
    }.toMutableList()
    toList.add(addColumn)


    Surface(modifier = Modifier.padding(16.dp)) {
        Column {
            GenericTable<SysArea>(
                columns = toList,
                onLoadData = {
// 模拟搜索功能
                    val searchText = it.useSearch.searchText
                    val createQuery = selectArea(searchText, it)
                    createQuery
                },
                onSave = {
                    sql.save(it)
                },
                onDelete = {
                    sql.deleteById(SysArea::class, it)
                },
            )
        }
    }
}

private fun selectArea(
    keyword: String, useTable: UseTable<SysArea>
): Page<SysArea> {
    val createQuery = sql.createQuery(SysArea::class) {
        where(
            or(
                table.city `ilike?` keyword, table.name `ilike?` keyword
            )
        )
        orderBy(table.makeOrders("sid asc"))
        select(table)
    }.fetchPage(useTable.useTablePagination.pageNo - 1, useTable.useTablePagination.pageSize)
    return createQuery
}

