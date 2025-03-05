package com.addzero.web.ui.hooks.table.generic

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.web.modules.sys.user.User
import com.addzero.web.ui.hooks.table.entity.AddColumn
import com.addzero.web.ui.hooks.table.entity.RenderType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun UserTableExample() {
    // 自定义列配置
    val columns = listOf(
        AddColumn<User>(
            title = "ID",
            getFun = { it.id.toString() }
        ),
        AddColumn<User>(
            title = "用户名",
            getFun = { it.username }
        ),
        AddColumn<User>(
            title = "邮箱",
            getFun = { it.email }
        ),
        AddColumn<User>(
            title = "角色",
            getFun = { it.role }
        ),
        AddColumn<User>(
            title = "状态",
            getFun = { it.status },
            renderTypeOverride = RenderType.TAG
        ) { user ->
            if (user.status) {
                Text("启用")
            } else {
                Text("禁用")
            }
        },
        AddColumn<User>(
            title = "电话",
            getFun = { it.phone }
        ),
        AddColumn<User>(
            title = "地址",
            getFun = { it.address }
        ),
        AddColumn<User>(
            title = "部门",
            getFun = { it.department }
        ),
        AddColumn<User>(
            title = "职位",
            getFun = { it.position }
        ),
        AddColumn<User>(
            title = "薪资",
            getFun = { it.salary.toString() },
            renderTypeOverride = RenderType.MONEY
        ),
        AddColumn<User>(
            title = "入职日期",
            getFun = { formatDateTime(it.hireDate) },
            renderTypeOverride = RenderType.DATE
        ),
        AddColumn<User>(
            title = "最后登录",
            getFun = { formatDateTime(it.lastLoginTime) },
            renderTypeOverride = RenderType.DATETIME
        ),
        AddColumn<User>(
            title = "创建时间",
            getFun = { formatDateTime(it.createdTime) },
            renderTypeOverride = RenderType.DATETIME
        ),
        AddColumn<User>(
            title = "更新时间",
            getFun = { formatDateTime(it.updatedTime) },
            renderTypeOverride = RenderType.DATETIME
        ),
        AddColumn<User>(
            title = "备注",
            getFun = { it.remark },
            renderTypeOverride = RenderType.TEXTAREA
        )
    )

    // 模拟用户数据
    val users = (1..100).map { index ->
        User(
            id = index.toLong(),
            username = "user$index",
            email = "user$index@example.com",
            role = if (index % 3 == 0) "管理员" else "普通用户",
            status = index % 2 == 0,
            phone = "1380000${index.toString().padStart(4, '0')}",
            address = "北京市朝阳区第${index}号",
            department = "部门${index % 5 + 1}",
            position = "职位${index % 3 + 1}",
            salary = 8000.0 + (index * 100),
            hireDate = LocalDateTime.now().minusDays(index.toLong()),
            lastLoginTime = LocalDateTime.now().minusHours(index.toLong()),
            createdTime = LocalDateTime.now().minusDays(index.toLong()),
            updatedTime = LocalDateTime.now().minusHours(index.toLong()),
            remark = "这是用户${index}的备注信息"
        )
    }

    Surface(modifier = Modifier.padding(16.dp)) {
        Column {
            GenericTable<User>(
                columns = columns,
                onSearch = { viewModel ->
                    // 模拟搜索功能
                    val keyword = viewModel.searchText.lowercase()
                    val filteredUsers = users.filter { user ->
                        user.username.lowercase().contains(keyword) ||
                        user.email.lowercase().contains(keyword) ||
                        user.department.lowercase().contains(keyword) ||
                        user.position.lowercase().contains(keyword)
                    }
                    
                    // 分页处理
                    val start = (viewModel.pageNo - 1) * viewModel.pageSize
                    val end = minOf(start + viewModel.pageSize, filteredUsers.size)
                    viewModel.dataList = filteredUsers.subList(start, end)
                    viewModel.totalPages = (filteredUsers.size + viewModel.pageSize - 1) / viewModel.pageSize
                },
                onEdit = { user ->
                    // 处理编辑操作
                    println("编辑用户: $user")
                },
                onDelete = { user ->
                    // 处理删除操作
                    println("删除用户: $user")
                },
                getIdFun = { it.id }
            )
        }
    }
}

// 格式化日期时间
private fun formatDateTime(dateTime: LocalDateTime): String {
    return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
}