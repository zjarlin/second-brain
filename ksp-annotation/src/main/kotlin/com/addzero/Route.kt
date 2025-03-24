package com.addzero

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class Route(
    val path: String = "",
    val title: String="",
    val parent: String = "",
    val icon: String = "",
    val visible: Boolean = true,
    val order: Double = 0.0,
    val permissions: String = ""

)