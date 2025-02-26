package com.addzero.web.ui.hooks

import androidx.compose.material3.*
import androidx.compose.runtime.*

data class User(
    val id: Int, val name: String, val age: Int, val enabled: Boolean
)

@Composable
fun UseTableExample() {
    // 分页状态
    val myPageNo =1
    val mypageSize=10
    // 模拟数据
    val users = List(1000) { index ->
        User(
            id = index + 1, name = "用户${index + 1}", age = 20 + (index % 20), enabled = index % 2 == 0
        )
    }

    // 计算当前页数据
    val startIndex = (myPageNo - 1) * mypageSize
    val endIndex = minOf(startIndex + mypageSize, users.size)
    val currentPageData = users.subList(startIndex, endIndex)



    // 定义表格列
    val columns = listOf(
        AddColumn(title = "ID", key = "id") { Text(it.id.toString()) },
        AddColumn(title = "姓名", key = "name") { user -> Text(user.name) },
        AddColumn(title = "年龄", key = "age") { user -> Text(user.age.toString()) },
        AddColumn<User>(title = "状态", key = "enabled") { user ->
            Switch(
                checked = user.enabled, onCheckedChange = null // 实际使用时这里可以添加状态更新逻辑
            )
        })

    // 使用表格组件
    val render = UseTable(
        columns = columns,
        dataList = currentPageData,
        totalPages = users.size,
    ).render().apply {
        pageNo=myPageNo
        pageSize=mypageSize

    }
}
