package com.addzero.web.modules.demo

import androidx.compose.runtime.*
import com.addzero.web.ui.components.system.dynamicroute.MetaSpec
import com.addzero.web.ui.components.system.dynamicroute.RouteMetadata
import com.addzero.web.ui.components.table.TableExample

class DataTableDemo : MetaSpec {

    override val metadata: RouteMetadata
        get() = RouteMetadata(
            parentName = "测试demo",
            title = "测试DataTable",
            visible = true,
        )

    @Composable
    override fun render() {
        TableExample()
    }

}
