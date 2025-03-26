package com.addzero

/**
 * 路由元数据
 */
data class KspRouteMeta(
    val declarationQulifiedName: String,
    val path: String,
    val title: String,
    val parent: String,
    val icon: String = "Apps",
    val visible: Boolean = true,
    val order: Double = 0.0,
    val permissions: String = "",
)