package com.addzero.web.ui.components.system.rbac

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.addzero.web.ui.components.system.dynamicroute.MetaSpec
import com.addzero.web.ui.components.system.dynamicroute.RouteMetadata


class MenuManagementSpec : MetaSpec {
    override val refPath: String = "system/rbac/menu"
    override val metadata: RouteMetadata = RouteMetadata(
        parentName = "系统管理",
        title = "菜单管理",
        icon = Icons.Default.Menu,
        visible = true,
        permissions = listOf("system:menu:view")
    )
    @Composable
    override fun render() {
        Text("菜单管理")
    }

}