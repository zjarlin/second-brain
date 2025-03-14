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
import com.addzero.web.modules.sys.area.dto.SysAreaSpec
import com.addzero.web.ui.hooks.table.entity.JimmerColumn
import com.addzero.web.ui.hooks.table.generic.dialog.UseDialog
import org.babyfish.jimmer.Page
import org.babyfish.jimmer.sql.kt.ast.query.KConfigurableRootQuery
import org.babyfish.jimmer.sql.kt.ast.query.KMutableRootQuery
import org.babyfish.jimmer.sql.kt.ast.table.makeOrders

@Composable
fun GenericTableExample() {
// 自定义列配置
    val addColumn = JimmerColumn<SysArea>("名字是否有黑", getFun = { it.blackFlag }).apply {
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
                    val searchText = it.useSearch.searchText
                    val sysAreaSpec = SysAreaSpec(
                        keyword = searchText
                    )
                    val createQuery = selectArea(sysAreaSpec, it)
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
    spec: SysAreaSpec, useTable: UseTable<SysArea>
): Page<SysArea> {

    val block: KMutableRootQuery<SysArea>.() -> KConfigurableRootQuery<SysArea, SysArea> = { ->
        where( spec )
        orderBy(table.makeOrders("sid asc"))
        select(table)
    }


    val createQuery = sql.createQuery(SysArea::class, block)
        .fetchPage(useTable.useTablePagination.pageNo - 1, useTable.useTablePagination.pageSize)
    return createQuery
}

