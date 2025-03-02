package com.addzero.web.ui.hooks.table.generic

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.web.ui.hooks.table.entity.AddColumn
import com.addzero.web.ui.hooks.table.entity.RenderType

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
    // 模拟数据
    var users by remember { mutableStateOf(listOf(
        User(1, "张三", 25, "zhangsan@example.com", true),
        User(2, "李四", 30, "lisi@example.com", false),
        User(3, "王五", 28, "wangwu@example.com", true)
    )) }

    // 自定义列配置
    val columns = listOf(
        AddColumn<User>(
            key = "name",
            title = "姓名",
            getFun = { it.name }
        ),
        AddColumn<User>(
            key = "age",
            title = "年龄",
            getFun = { it.age.toString() }
        ),
        AddColumn<User>(
            key = "email",
            title = "邮箱",
            getFun = { it.email }
        ),
        AddColumn<User>(
            key = "active",
            title = "状态",
            getRenderType = { RenderType.TAG },
            getFun = { if (it.active) "激活" else "禁用" }
        )
    )

    Surface(modifier = Modifier.padding(16.dp)) {
        Column {
            GenericTable(
                columns = columns,
                onSearch = { viewModel ->
                    // 模拟搜索功能
                    val keyword = viewModel.searchText.lowercase()
                    viewModel.dataList = users.filter { user ->
                        user.name.lowercase().contains(keyword) ||
                        user.email.lowercase().contains(keyword)
                    }
                },
                onEdit = { user ->
                    // 处理编辑操作
                    println("编辑用户: $user")
                },
                onDelete = { user ->
                    // 处理删除操作
                    users = users.filter { it.id != user.id }
                },
                getIdFun = { it.id }
            )
        }
    }
}