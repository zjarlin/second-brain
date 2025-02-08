package com.addzero.web.ui.components.system.rbac

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.addzero.web.ui.components.system.dynamicroute.MetaSpec
import com.addzero.web.ui.components.system.dynamicroute.RouteMetadata


class DepartmentManagementSpec : MetaSpec {
    override val refPath: String = "system/rbac/department"
    override val metadata: RouteMetadata = RouteMetadata(
        parentName = "系统管理",
        title = "部门管理",
        icon = Icons.Default.AccountTree,
        visible = true,
        permissions = listOf("system:department:view")
    )

    @Composable
    override fun render() {
        Text("部门管理")
    }
}