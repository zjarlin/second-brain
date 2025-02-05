package com.addzero.web.ui.components.system.dynamicroute

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

/**
 * 路由初始化器
 * 在应用启动时扫描并注册所有路由组件
 */
object RouteInitializer {
    private var initialized = false

    /**
     * 初始化路由组件
     * 在应用的根Composable中调用
     */
    @Composable
    fun Initialize() {
        LaunchedEffect(Unit) {
            if (!initialized) {
                // 扫描主包下的所有路由组件
                RouteUtil.init("com.addzero.web.modules")
                initialized = true
            }
        }
    }
}