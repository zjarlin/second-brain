package com.addzero.web.ui.system

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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


    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            nagive(currentRoute)
        }
    }
}

