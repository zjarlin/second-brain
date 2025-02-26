package com.addzero.web.modules.demo

import androidx.compose.runtime.*
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.ui.unit.sp
import com.addzero.web.ui.components.system.dynamicroute.MetaSpec
import com.addzero.web.ui.components.system.dynamicroute.RouteMetadata
import com.addzero.web.ui.hooks.UseCounter
import com.addzero.web.ui.hooks.UseTableExample

class PageDemo : MetaSpec {

    override val metadata: RouteMetadata
        get() = RouteMetadata(
            parentName = "测试demo",
            title = "测试表格",
            visible = true,
        )

    @Composable
    override fun render() {
        UseTableExample()
    }

}
