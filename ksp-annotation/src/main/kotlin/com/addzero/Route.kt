package com.addzero

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class Route(
    val value: String = "默认分组",
    val path: String = "",
    val title: String="",
    val parent: String = "",
    val icon: String = "Apps",
    val visible: Boolean = true,
    val order: Double = 0.0,
    val permissions: String = "",

    val declarationQulifiedName : String=""


)