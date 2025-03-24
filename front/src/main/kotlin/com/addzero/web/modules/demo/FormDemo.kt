package com.addzero.web.modules.demo

import androidx.compose.runtime.Composable
import com.addzero.web.ui.system.dynamicroute.MetaSpec
import com.addzero.web.ui.system.dynamicroute.RouteMetadata

class FormDemo : MetaSpec {

    override val metadata: RouteMetadata
        get() = RouteMetadata(
            parentName = "测试demo",
            title = "测试form",
            visible = true,
        )

    @Composable
    override fun render() {
    }

}
