package com.addzero.web.ui.hooks

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.addzero.web.infra.jackson.toJson

data class User(
    val id: Int, val name: String, val age: Int, var enabled: Boolean
)

@Composable
fun UseTableExample() {
    // 分页状态
    var myPageNo by remember { mutableStateOf(1) }
    var mypageSize by remember { mutableStateOf(10) }

    // 模拟数据 - 使用remember和mutableStateListOf创建响应式列表
    val users = remember {
        mutableStateListOf<User>().apply {
            addAll(List(1000) { index ->
                User(
                    id = index + 1,
                    name = "用户${index + 1}",
                    age = 20 + (index % 20),
                    enabled = index % 2 == 0
                )
            })
        }
    }

    // 计算当前页数据
    val startIndex = (myPageNo - 1) * mypageSize
    val endIndex = minOf(startIndex + mypageSize, users.size)
    val currentPageData = remember(myPageNo, mypageSize) {
        users.subList(startIndex, endIndex)
    }

    // 定义表格列
    val columnsData = listOf(
        AddColumn(title = "ID", key = "id"),
        AddColumn(title = "姓名", key = "name"),
        AddColumn(title = "年龄", key = "age"),
        AddColumn<User>(title = "状态", key = "enabled") { col, user ->
            //是否启用这个字段采用自定义渲染
            Switch(
                checked = user.enabled,
                onCheckedChange = { newValue ->
                    val index = users.indexOf(user)
                    if (index != -1) {
                        users[index] = user.copy(enabled = newValue)
                    }
                }
            )
        }
    )

    // 使用表格组件
    val useTable = UseTable<User>(
        totalPages = users.size,
        onRefresh = {
            // 模拟刷新数据
            users.clear()
            users.addAll(List(1000) { index ->
                User(
                    id = index + 1,
                    name = "用户${index + 1}",
                    age = 20 + (index % 20),
                    enabled = index % 2 == 0
                )
            })
        }
    ).apply {
        pageNo = myPageNo
        pageSize = mypageSize
        columns = columnsData
        dataList = currentPageData
    }

    Column(modifier = Modifier) {
        val render = useTable.render()
        val toJson = render.toJson()
        Text(toJson)
    }
}
