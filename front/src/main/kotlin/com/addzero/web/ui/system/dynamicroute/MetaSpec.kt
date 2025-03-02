package com.addzero.web.ui.system.dynamicroute

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.runtime.Composable

/**
 * 路由元数据规范接口
 */
interface MetaSpec {
    val refPath: String
        get() = this.javaClass.name

    val metadata: RouteMetadata
        get() = RouteMetadata(
            parentName = null,
            title = "标题",
            icon = Icons.Filled.Apps,
            visible = true,
            permissions = emptyList(),
            routerPath = refPath,
        )

    @Composable
    fun render(){
        throw UnsupportedOperationException("未实现")
    }
}
