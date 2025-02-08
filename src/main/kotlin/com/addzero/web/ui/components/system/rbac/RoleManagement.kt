//package com.addzero.web.ui.components.system.rbac
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import com.addzero.web.modules.dotfiles.SearchPanel
//import com.addzero.web.ui.components.system.dynamicroute.MetaSpec
//import com.addzero.web.ui.components.system.dynamicroute.RouteMetadata
//import com.addzero.web.ui.components.table.DataTable
//import com.addzero.web.ui.components.table.ColumnName
//
//@Composable
//fun RoleManagement() {
//    var searchText by remember { mutableStateOf("") }
//    var showAddDialog by remember { mutableStateOf(false) }
//    var showEditDialog by remember { mutableStateOf(false) }
//    var showDeleteDialog by remember { mutableStateOf(false) }
//    var selectedRole by remember { mutableStateOf<Role?>(null) }
//    var currentPage by remember { mutableStateOf(1) }
//    val totalPages = 10 // TODO: 根据实际数据计算总页数
//
//    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
//        // 顶部工具栏
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//
//            SearchPanel(
//                searchName = searchText,
//                onSearchNameChange = { searchText = it },
//
//                selectedOSTypes = selectedOSTypes,
//                onOSTypeSelectionChange = { selectedOSTypes = it },
//                onSearch = {
//                    viewModel.loadData(
//                        name = searchName, platforms = selectedPlatforms, osTypes = selectedOSTypes
//                    )
//                })
//
//            Button(
//                onClick = { showAddDialog = true },
//                colors = ButtonDefaults.buttonColors()
//            ) {
//                Icon(Icons.Default.Add, contentDescription = "添加")
//                Spacer(Modifier.width(8.dp))
//                Text("新增角色")
//            }
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // 角色列表
//        DataTable(
//            items = listOf(), // TODO: 添加实际的角色数据
//            onEdit = { role ->
//                selectedRole = role
//                showEditDialog = true
//            },
//            onDelete = { role ->
//                selectedRole = role
//                showDeleteDialog = true
//            },
//            excludeFields = setOf("id"),
//            startIndex = (currentPage - 1) * 10,
//            currentPage = currentPage,
//            totalPages = totalPages,
//            onPageChange = { page ->
//                currentPage = page
//                // TODO: 加载对应页的数据
//            }
//        )
//    }
//
//    // 删除确认对话框
//    if (showDeleteDialog) {
//        ConfirmDialog(
//            title = "确认删除",
//            message = "确定要删除该角色吗？此操作不可恢复。",
//            onConfirm = {
//                // TODO: 实现删除逻辑
//                showDeleteDialog = false
//            },
//            onDismiss = { showDeleteDialog = false }
//        )
//    }
//}
//
//class RoleManagementSpec : MetaSpec {
//    override val refPath: String = "system/rbac/role"
//    override val metadata: RouteMetadata = RouteMetadata(
//        parentName = "系统管理",
//        title = "角色管理",
//        icon = Icons.Default.SupervisorAccount,
//        visible = true,
//        permissions = listOf("system:role:view")
//    )
//}
//
//data class Role(
//    val id: String,
//    @ColumnName("角色名称")
//    val name: String,
//    @ColumnName("角色标识")
//    val code: String,
//    @ColumnName("描述")
//    val description: String,
//    @ColumnName("创建时间")
//    val createTime: String
//)