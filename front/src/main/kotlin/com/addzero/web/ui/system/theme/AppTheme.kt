package com.addzero.web.ui.system.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.addzero.web.config.AppConfig

private val LightColorScheme = lightColorScheme(
    primary = Color(AppConfig.Theme.primaryGradient[1]),
    onPrimary = Color.White,
    surface = Color.White.copy(alpha = 0.95f),
    background = Color(AppConfig.Theme.backgroundGradient[0])
)

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}
