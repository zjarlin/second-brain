package com.addzero.web.modules.demo

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.addzero.Route


@Route(title = "测试ksp路由表", parent = "测试demo")
@Composable
fun render1111() {
    Column {
        Text("hello ksp route")
    }
}


@Route(parent = "测试demo")
@Composable
fun 测试ksp路由表2222() {
    Column {
        Text("hello ksp route2")
    }
}

