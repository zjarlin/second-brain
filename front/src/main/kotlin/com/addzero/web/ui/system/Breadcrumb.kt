package com.addzero.web.ui.system

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.web.ui.system.dynamicroute.RouteUtil

/**
 * 面包屑导航组件
 * @param currentRouteRefPath 当前路由路径
 * @param onPathClick 路径点击回调
 */
@Composable
fun Breadcrumb(
    currentRouteRefPath: String, onPathClick: (String) -> Unit = {}
) {
    Surface(
        modifier = Modifier.fillMaxWidth().height(48.dp), color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val breadcrumbPath = RouteUtil.getBreadcrumbPath(currentRouteRefPath)

            breadcrumbPath.forEachIndexed { index, (path, title) ->
                if (index > 0) {
                    Text(
                        text = " / ",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                title?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (index == breadcrumbPath.lastIndex) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        },
                        modifier = Modifier.clickable(enabled = index != breadcrumbPath.lastIndex) {
                            onPathClick(path)
                        })
                }
            }
        }
    }
}
