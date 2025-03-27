package com.addzero.web.ui.system.animation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onAnimationComplete: () -> Unit
) {
    var isAnimationComplete by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        delay(2000) // 等待2秒，确保动画完整播放
        isAnimationComplete = true
        onAnimationComplete()
    }
    
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            MonsterEyeAnimation()
        }
    }
}