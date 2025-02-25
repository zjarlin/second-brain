package com.addzero.web.modules.demo

import androidx.compose.runtime.Composable
import com.addzero.web.ui.components.system.dynamicroute.MetaSpec
import com.addzero.web.ui.components.system.dynamicroute.RouteMetadata
import com.addzero.web.ui.components.button.AddButtonDemo


class ButtonDemo : MetaSpec {

    override val metadata: RouteMetadata
        get() = RouteMetadata(
            parentName = "测试demo",
            title = "测试按钮",
//            icon = Icons.Filled.Apps,
            visible = true,
//            permissions = emptyList()
        )

    @Composable
    override fun render() {
        AddButtonDemo()
    }
}
