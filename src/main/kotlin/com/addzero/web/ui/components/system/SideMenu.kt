package com.addzero.web.ui.components.system

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.web.ui.components.system.dynamicroute.RouteMetadata
import com.addzero.web.model.enums.Route
import com.addzero.web.ui.components.system.dynamicroute.RouteUtil

@Composable
fun SideMenu(
    currentRoute: String, onRouteChange: (String) -> Unit, userPermissions: List<String> = emptyList()
) {
    Column(modifier = Modifier.width(240.dp)) {
        // 获取所有路由的元数据

        val routeComponentByPath = RouteUtil.getRouteComponentByPath(currentRoute)
        val routeMetadataMap = if (routeComponentByPath != null) {
            mapOf(routeComponentByPath.first.qualifiedName!! to routeComponentByPath.second)
        } else {
            emptyMap()
        }


        // 按父级路由分组

        val groupedRoutes = routeMetadataMap.entries.filter { (_, spec) ->

                spec.metadata.visible
            }.groupBy { (_, metadata) -> metadata.metadata.parentRefPath }

        // 渲染菜单项
        groupedRoutes[""]?.forEach { (route, spec) ->
            val contentDescription = spec.metadata.title

            NavigationDrawerItem(
                icon = {
                    val imageVector = spec.metadata.icon
                    imageVector?.let { Icon(it, contentDescription = contentDescription) }
                },
                label = { Text(contentDescription) },
                selected = currentRoute == route,
                onClick = { onRouteChange(route) },
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}