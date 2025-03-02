package com.addzero.web.ui.system

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.common.kt_util.isNotNull
import com.addzero.web.ui.system.dynamicroute.MetaSpec
import com.addzero.web.ui.system.dynamicroute.RouteUtil
import kotlin.reflect.full.createInstance

/**
 * 主要内容
 * @param [currentRoute]
 */
@Composable
fun MainContent(
    currentRoute: String,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            val routeComponentByPath = RouteUtil.getRouteComponentByPath(currentRoute)
            val clazz = routeComponentByPath?.clazz
            val func = routeComponentByPath?.func
            if (func.isNotNull()) {
                // 处理函数组件路由
                func?.let { it() }
            } else if (clazz.isNotNull()) {
                //处理类组件路由
                val createInstance = clazz?.createInstance()
                if (createInstance != null) {
                    val metaSpec = createInstance as MetaSpec
                    metaSpec.render()
                }
            }
        }
    }
}
