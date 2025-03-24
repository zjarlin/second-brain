package com.addzero.web.modules.demo

import androidx.compose.runtime.Composable
import com.addzero.web.ui.lowcode.examples.CompleteCrudExample
import com.addzero.web.ui.system.dynamicroute.MetaSpec
import com.addzero.web.ui.system.dynamicroute.RouteMetadata


class LowTableDemo : MetaSpec {

    override val metadata: RouteMetadata
        get() = RouteMetadata(
            parentName = "测试demo",
            title = "测试低代码表格",
//            icon = Icons.Filled.Apps,
            visible = true,
//            permissions = emptyList()
        )

    @Composable
    override fun render() {
        CompleteCrudExample()
    }
}
