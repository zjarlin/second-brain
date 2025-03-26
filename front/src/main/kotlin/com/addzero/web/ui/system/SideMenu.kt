package com.addzero.web.ui.system

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.web.ui.components.ScrollableContainer
import com.addzero.web.ui.system.dynamicroute.RouteMetadata
import com.addzero.web.ui.system.dynamicroute.RouteUtil


@Composable
fun SideMenu(
    currentRoute: String, onRouteChange: (String) -> Unit, userPermissions: List<String> = emptyList()
) {
    // 获取所有路由组件的元数据
    val allRoutes = RouteUtil.scanMetas()
        // 过滤可见的路由
        .filter { it.visible }
        // 如果需要，这里可以添加权限过滤
        // .filter { route -> userPermissions.containsAll(route.permissions) }
        // 按排序字段排序
        .sortedBy { it.order }

    ScrollableContainer(
        showScrollbar = true, modifier = Modifier.width(240.dp).fillMaxHeight()
    ) {
        Column {
            renderTreeSider(allRoutes, currentRoute, onRouteChange)
        }
    }
}

@Composable
private fun renderTreeSider(
    allRoutes: List<RouteMetadata>, currentRoute: String, onRouteChange: (String) -> Unit
) {
    // 按父级路由分组
    val routeGroups = allRoutes.groupBy { it.parentName }

    // 渲染根级菜单项
    routeGroups[null]?.forEach { route ->
        val contentDescription = route.title
        val isExpanded = remember { mutableStateOf(true) }

        Column {
            // 渲染当前菜单项
            NavigationDrawerItem(
                icon = {
                    val imageVector = route.icon
                    imageVector?.let { Icon(it, contentDescription = contentDescription) }
                }, label = { Text(contentDescription) }, badge = {
                    // 只有当存在子菜单时才显示箭头图标
                    if (routeGroups[route.title] != null) {
                        val arrowIcon =
                            if (isExpanded.value) Icons.Default.KeyboardArrowDown else Icons.AutoMirrored.Filled.KeyboardArrowRight
                        Icon(
                            imageVector = arrowIcon,
                            contentDescription = if (isExpanded.value) "折叠" else "展开",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }, selected = currentRoute == route.routerPath, onClick = {
                    onRouteChange(route.routerPath)
                    isExpanded.value = !isExpanded.value
                }, modifier = Modifier.padding(vertical = 4.dp), colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                    unselectedContainerColor = MaterialTheme.colorScheme.surface
                )
            )

            // 如果存在子菜单项且展开状态，则渲染子菜单
            if (isExpanded.value) {
                val childRoutes = routeGroups[route.title]
                childRoutes?.forEach { childRoute ->
                    if (childRoute.visible) {
                        Box(modifier = Modifier.padding(start = 16.dp)) {
                            NavigationDrawerItem(
                                icon = {
                                    val imageVector = childRoute.icon
                                    imageVector?.let { Icon(it, contentDescription = childRoute.title) }
                                },
                                label = { Text(childRoute.title) },
                                selected = currentRoute == childRoute.routerPath,
                                onClick = { onRouteChange(childRoute.routerPath) },
                                modifier = Modifier.padding(vertical = 4.dp),
                                colors = NavigationDrawerItemDefaults.colors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                                    unselectedContainerColor = MaterialTheme.colorScheme.surface
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
