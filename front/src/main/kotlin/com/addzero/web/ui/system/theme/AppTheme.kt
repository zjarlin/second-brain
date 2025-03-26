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
    background = Color(AppConfig.Theme.backgroundGradient[0]),
    primaryContainer = Color.White.copy(alpha = 0.95f),
    surfaceVariant = Color.White
)



// 自定义颜色主题
//val customColors = lightColorScheme(
//    primary = Color(0xFF6200EE), // 自定义主色调（紫色）
////    primaryVariant = Color(0xFF3700B3), // 主色调变体
//    secondary = Color(0xFF03DAC6), // 次要色调（青色）
//    background = Color(0xFF5F5F5F), // 背景色（浅灰）
//    surface = Color.White, // 表面颜色
//    error = Color(0xFF80C020), // 错误颜色
//    onPrimary = Color.White, // 主色调上的文字颜色
//    onSecondary = Color.Black, // 次要色调上的文字颜色
//    onBackground = Color.Black, // 背景上的文字颜色
//    onSurface = Color.Black, // 表面上的文字颜色
//    onError = Color.White // 错误颜色上的文字颜色
//)

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}
