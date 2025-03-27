package com.addzero.web.ui.system.animation

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun FishboneAnimation(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF2196F3),
    onAnimationComplete: () -> Unit = {}
) {
    var animationPlayed by remember { mutableStateOf(false) }
    
    val infiniteTransition = rememberInfiniteTransition()
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    
    val scale by animateFloatAsState(
        targetValue = if (animationPlayed) 1f else 0f,
        animationSpec = tween(1000, easing = EaseOutQuart),
        finishedListener = { onAnimationComplete() }
    )
    
    LaunchedEffect(Unit) {
        animationPlayed = true
    }
    
    Canvas(modifier = modifier.fillMaxSize()) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        val radius = minOf(size.width, size.height) * 0.3f * scale
        
        // 绘制主干
        val mainBonePath = Path().apply {
            moveTo(centerX - radius, centerY)
            lineTo(centerX + radius, centerY)
        }
        
        drawPath(
            path = mainBonePath,
            color = color,
            style = Stroke(width = 4f, cap = StrokeCap.Round)
        )
        
        // 绘制侧骨
        val sideBonesCount = 6
        val sideBonesLength = radius * 0.5f
        
        for (i in 0 until sideBonesCount) {
            val angle = Math.toRadians(30.0 + i * 30.0 + rotationAngle)
            val startX = centerX + (i - sideBonesCount/2 + 0.5f) * (radius / (sideBonesCount/2))
            val startY = centerY
            
            val sideBonePath = Path().apply {
                moveTo(startX, startY)
                lineTo(
                    startX + (sideBonesLength * cos(angle) * scale).toFloat(),
                    startY + (sideBonesLength * sin(angle) * scale).toFloat()
                )
            }
            
            drawPath(
                path = sideBonePath,
                color = color,
                style = Stroke(width = 2f, cap = StrokeCap.Round)
            )
        }
        
        // 绘制圆点作为Logo
        drawCircle(
            color = color,
            radius = 8f * scale,
            center = Offset(centerX, centerY)
        )
    }
}