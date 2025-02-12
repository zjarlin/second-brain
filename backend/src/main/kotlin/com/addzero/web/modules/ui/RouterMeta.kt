package com.addzero.web.modules.ui

class RouterMeta(
    val projectName: String,
    val routers: List<Router>,
    val timestamp: String,
)

data class Router(
    val fullPath: String,
    val path: String,
    val type: String,
)