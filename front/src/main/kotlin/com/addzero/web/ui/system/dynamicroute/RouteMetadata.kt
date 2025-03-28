package com.addzero.web.ui.system.dynamicroute

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import kotlin.reflect.KClass

/**
 * 路由元数据实现类
 */

data class RouteMetadata(


    //会有默认实现 ,函数式则ref#functionName,类Spec则全限定类名
    var routerPath: String ="",
    //id
    val title: String,
    //pid
    val parentName: String? = null,
    val icon: ImageVector? = Icons.Default.Apps,
    val visible: Boolean = true,
    val permissions: List<String> = emptyList(),
    val order: Double = 0.0, // 添加order字段用于菜单排序
    val children: List<RouteMetadata> = emptyList(),
    var clazz: KClass<*>? =null,
    val func: ( @Composable () -> Unit)? = null
)
