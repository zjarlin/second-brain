package com.addzero.web.ui.components.system

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.web.ui.components.system.dynamicroute.RouteMetadata
import com.addzero.web.ui.components.system.dynamicroute.MetaSpec
import com.addzero.web.ui.components.system.dynamicroute.RouteUtil
import kotlin.reflect.KClass

@Composable
fun SideMenu(
    currentRoute: String, onRouteChange: (String) -> Unit, userPermissions: List<String> = emptyList()
) {
    Column(modifier = Modifier.width(240.dp)) {
        // 获取所有路由组件的元数据
        val routeMetadataMap = RouteUtil.getAllRouteComponents()

        // 转换路由数据
        val transform: (Map.Entry<KClass<*>, MetaSpec>) -> Pair<String?, MetaSpec> = { it.key.qualifiedName to it.value }

        // 提取父级路由
        val parentRoutes = routeMetadataMap
            .filter { it.value.metadata.parentName != null }
            .map { entry ->
                val parentName = entry.value.metadata.parentName
                val parentMetadata = RouteMetadata(
                    parentName = null,
                    title = parentName.toString(),
                    icon = Icons.Default.Apps,
                    visible = true,
                    permissions = emptyList()
                )
                parentName to object : MetaSpec {
                    override val refPath: String = ""
                    override val metadata: RouteMetadata = parentMetadata
                }
            }.distinctBy { it.first }

        // 转换实际路由
        val childRoutes = routeMetadataMap.map(transform)

        // 合并路由并按父级分组
        val allRoutes = (parentRoutes + childRoutes)
            .filter { it.second.metadata.visible }
            .distinctBy { it.first ?: it.second.metadata.title }

        renderTreeSider(allRoutes, currentRoute, onRouteChange)
    }
}

@Composable
private fun renderTreeSider(
    allRoutes: List<Pair<String?, MetaSpec>>,
    currentRoute: String,
    onRouteChange: (String) -> Unit
) {
    // 按父级路由分组
    val routeGroups = allRoutes.groupBy { it.second.metadata.parentName }

    // 渲染根级菜单项
    routeGroups[null]?.forEach { (qualifiedName, spec) ->
        val contentDescription = spec.metadata.title
        val isExpanded = remember { mutableStateOf(true) }

        Column {
            // 渲染当前菜单项
            NavigationDrawerItem(
                icon = {
                    val imageVector = spec.metadata.icon
                    imageVector?.let { Icon(it, contentDescription = contentDescription) }
                },
                label = { Text(contentDescription) },
                selected = currentRoute == qualifiedName,
                onClick = {
                    qualifiedName?.let { onRouteChange(it) }
                    isExpanded.value = !isExpanded.value
                },
                modifier = Modifier.padding(vertical = 4.dp)
            )

            // 如果存在子菜单项且展开状态，则渲染子菜单
            if (isExpanded.value) {
                val childRoutes = routeGroups[spec.metadata.title]
                childRoutes?.forEach { (childQualifiedName, childSpec) ->
                    if (childSpec.metadata.visible) {
                        Box(modifier = Modifier.padding(start = 16.dp)) {
                            NavigationDrawerItem(
                                icon = {
                                    val imageVector = childSpec.metadata.icon
                                    imageVector?.let { Icon(it, contentDescription = childSpec.metadata.title) }
                                },
                                label = { Text(childSpec.metadata.title) },
                                selected = currentRoute == childQualifiedName,
                                onClick = { childQualifiedName?.let { onRouteChange(it) } },
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun renderFlatSider(
    routeMetadataMap: Map<KClass<*>, MetaSpec>,
    currentRoute: String,
    onRouteChange: (String) -> Unit
) {
    routeMetadataMap.forEach {
        val route = it.key
        val spec = it.value
        val contentDescription = spec.metadata.title

        val qualifiedName = route.qualifiedName
        NavigationDrawerItem(
            icon = {
                val imageVector = spec.metadata.icon
                imageVector?.let { Icon(it, contentDescription = contentDescription) }
            },
            label = { Text(contentDescription) },
            selected = currentRoute == qualifiedName,
            onClick = { onRouteChange(qualifiedName!!) },
            modifier = Modifier.padding(vertical = 4.dp)
        )


    }
}