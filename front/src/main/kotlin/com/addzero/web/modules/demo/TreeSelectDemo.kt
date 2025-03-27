package com.addzero.web.modules.demo

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.runtime.Composable
import com.addzero.Route
import com.addzero.web.ui.hooks.tree.TreeSelectEnhancedExample


@Composable
@Route
fun 测试树形选择() {
    val apps = Icons.Default.Apps
    TreeSelectEnhancedExample()
}
