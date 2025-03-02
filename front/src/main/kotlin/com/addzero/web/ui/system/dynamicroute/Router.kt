package com.addzero.web.ui.system.dynamicroute

/**
 * 路由注解，可以应用于类或函数
 * 支持指定parentName和title
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Router(

    val routerPath: String = "/customRoutes/default ",
    val parentName: String = "",
    val title: String = "标题",
    val visible: Boolean = true,
    val order: Double = 0.0,
)