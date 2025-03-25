package com.addzero.web.ui.system.dynamicroute

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Apps
import cn.hutool.core.util.ClassUtil

val ICON_MAP = mapOf(
    "Apps" to Icons.Default.Apps,
    "Add" to Icons.Default.Add
)

fun main() {


    val scanPackage = ClassUtil.scanPackage("androidx.compose.material.icons.filled")
    val toList = scanPackage.flatMap {
        it.declaredFields.filter {
            it.isAccessible = true // 关键步骤：设置字段可访问
            !it.name .startsWith("_")
        }
    }
    println(scanPackage)
}