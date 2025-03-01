package com.addzero.web.modules.sys.rbac

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.addzero.web.ui.system.dynamicroute.MetaSpec
import com.addzero.web.ui.system.dynamicroute.RouteMetadata


class MenuManagementSpec : MetaSpec {
    //    override val refPath: String = "system/rbac/menu"
    override val metadata: RouteMetadata = RouteMetadata(
        parentName = "系统管理",
        title = "菜单管理",
        icon = Icons.Default.Menu,
        visible = false,
//        permissions = listOf("system:menu:view")
    )

    @Composable
    override fun render() {
        Text("菜单管理")
    }

}
