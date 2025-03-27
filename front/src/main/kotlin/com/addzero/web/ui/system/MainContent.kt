package com.addzero.web.ui.system

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.web.ui.system.dynamicroute.RouteUtil.nagive

/**
 * 主要内容
 * @param [currentRoute]
 */
@Composable
fun MainContent(
    currentRoute: String,
) {
    // 直接使用Box来包装内容，避免与内部可能存在的LazyColumn嵌套
    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        nagive(currentRoute)
    }

}

