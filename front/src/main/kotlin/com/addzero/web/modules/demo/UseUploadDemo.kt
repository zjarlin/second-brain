package com.addzero.web.modules.demo

import androidx.compose.runtime.Composable
import com.addzero.web.ui.hooks.dialog.UseUploadDialogExample
import com.addzero.web.ui.system.dynamicroute.MetaSpec
import com.addzero.web.ui.system.dynamicroute.RouteMetadata


class UseUploadDemo : MetaSpec {

    override val metadata: RouteMetadata
        get() = RouteMetadata(
            parentName = "测试demo",
            title = "测试上传",
//            icon = Icons.Filled.Apps,
            visible = true,
//            permissions = emptyList()
        )

    @Composable
    override fun render() {
        UseUploadDialogExample()
    }
}
