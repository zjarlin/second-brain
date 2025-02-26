package com.addzero.web.ui.hooks

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.addzero.common.consts.SpringVars
import com.addzero.web.infra.jackson.toJson
import com.addzero.web.modules.sys.area.entity.SysArea

data class User(
    val id: Int, val name: String, val age: Int, var enabled: Boolean
)

@Composable
fun UseTableExample() {
    // 定义表格列
    val columnsData = listOf(
        AddColumn<SysArea>(title = "id", key = "id"),
        AddColumn(title = "parentsid", key = "parentsid"),
        AddColumn(title = "leveltype", key = "leveltype"),
        AddColumn(title = "name", key = "name"),
    )

    // 使用表格组件
    val useTable = UseTable<SysArea>(
    ).apply {
        columns = columnsData
    }
    val render = useTable.render()

    render.apply {
        val sql = SpringVars.sql
        val fetchPage = sql.createQuery(SysArea::class) {
            select(table)
        }.fetchPage(pageNo.toInt(), pageSize = pageSize.toInt())

        val rows = fetchPage.rows

        val totalRowCount = fetchPage.totalRowCount
        val totalPageCount = fetchPage.totalPageCount

        dataList = rows
        totalPages = totalPageCount
    }
//    Column(modifier = Modifier) {
//        val toJson = render.toJson()
//        Text(toJson)
//    }
}
