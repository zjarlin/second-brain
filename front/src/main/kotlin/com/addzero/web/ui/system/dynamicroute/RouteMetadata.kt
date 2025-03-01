package com.addzero.web.ui.system.dynamicroute

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * 路由元数据实现类
 */
data class RouteMetadata(
    val title: String,
    val parentName: String? = null,
    val icon: ImageVector? = Icons.Default.Apps,
    val visible: Boolean = true,
    val permissions: List<String> = emptyList(),
    val order: Double = 0.0, // 添加order字段用于菜单排序
) {
}
