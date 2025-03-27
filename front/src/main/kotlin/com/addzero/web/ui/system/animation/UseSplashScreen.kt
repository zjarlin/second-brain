package com.addzero.web.ui.system.animation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.addzero.web.ui.hooks.ViewModel

class UseSplashScreen(
    other: @Composable () -> Unit
) : ViewModel<UseSplashScreen> {
    var showAnimation by mutableStateOf(true)
    override val render: @Composable (() -> Unit) = {
        if (showAnimation) {
            renderSplash()
        } else {
            other()
        }
    }
}


@Composable
fun renderSplash() {
    Surface(
        modifier = Modifier.fillMaxSize(), color = Color.White
    ) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            MonsterEyeAnimation()
        }
    }
}
