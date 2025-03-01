package com.addzero.web.modules.sys.rbac

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.addzero.web.ui.system.dynamicroute.MetaSpec
import com.addzero.web.ui.system.dynamicroute.RouteMetadata


class DepartmentManagementSpec : MetaSpec {
//    override val refPath: String = "system/rbac/department"
    override val metadata: RouteMetadata = RouteMetadata(
        parentName = "系统管理",
        title = "部门管理",
        icon = Icons.Default.AccountTree,
        visible = false,
//        permissions = listOf("system:department:view")
    )

    @Composable
    override fun render() {
        Text("部门管理")
    }
}
