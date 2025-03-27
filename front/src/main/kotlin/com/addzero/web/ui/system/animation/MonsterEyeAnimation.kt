package com.addzero.web.ui.system.animation

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MonsterEyeAnimation() {
    val infiniteTransition = rememberInfiniteTransition()
    
    // 旋转动画 - 现在只用于加载指示器，不再用于眼睛
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    
    // 眼睛动画 - 更夸张的眼睛动画
    val eyeScale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    // 眼睛旋转动画
    val eyeRotation by infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val textAlpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        )
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Canvas(modifier = Modifier.size(150.dp)) {
            val centerX = size.width / 2
            val centerY = size.height / 2
            val headSize = size.minDimension / 2
            val eyeRadius = size.minDimension / 6
            
            // 绘制海绵宝宝头部（黄色方形）
            drawRect(
                color = Color(0xFFFFF59D),  // 黄色
                topLeft = Offset(centerX - headSize, centerY - headSize),
                size = androidx.compose.ui.geometry.Size(headSize * 2, headSize * 2)
            )
            
            // 绘制海绵宝宝的斑点
            val spotsCount = 12
            val spotRadius = headSize / 10
            repeat(spotsCount) { index ->
                val angle = (index * 360f / spotsCount) + rotation / 4
                val distance = headSize * 0.6f
                val x = centerX + kotlin.math.cos(Math.toRadians(angle.toDouble())).toFloat() * distance
                val y = centerY + kotlin.math.sin(Math.toRadians(angle.toDouble())).toFloat() * distance
                
                drawCircle(
                    color = Color(0xFFFFEB3B),  // 深黄色斑点
                    radius = spotRadius,
                    center = Offset(x, y)
                )
            }
            
            // 绘制左眼睛背景（白色）
            drawCircle(
                color = Color.White,
                radius = eyeRadius * 1.2f,
                center = Offset(centerX - headSize * 0.4f, centerY - headSize * 0.2f)
            )
            
            // 绘制右眼睛背景（白色）
            drawCircle(
                color = Color.White,
                radius = eyeRadius * 1.2f,
                center = Offset(centerX + headSize * 0.4f, centerY - headSize * 0.2f)
            )
            
            // 绘制左眼睛（更夸张的动画效果）
            rotate(eyeRotation, Offset(centerX - headSize * 0.4f, centerY - headSize * 0.2f)) {
                // 绘制眼睛外圈
                drawCircle(
                    color = Color(0xFF2196F3),  // 蓝色
                    radius = eyeRadius * eyeScale,
                    center = Offset(centerX - headSize * 0.4f, centerY - headSize * 0.2f),
                    style = Stroke(width = 4f)
                )
                
                // 绘制眼睛内部
                drawCircle(
                    color = Color(0xFF64B5F6),  // 浅蓝色
                    radius = eyeRadius * 0.7f * eyeScale,
                    center = Offset(centerX - headSize * 0.4f, centerY - headSize * 0.2f)
                )
                
                // 绘制瞳孔
                drawCircle(
                    color = Color.Black,
                    radius = eyeRadius * 0.3f * eyeScale,
                    center = Offset(centerX - headSize * 0.4f, centerY - headSize * 0.2f)
                )
            }
            
            // 绘制右眼睛（更夸张的动画效果）
            rotate(eyeRotation, Offset(centerX + headSize * 0.4f, centerY - headSize * 0.2f)) {
                // 绘制眼睛外圈
                drawCircle(
                    color = Color(0xFF2196F3),  // 蓝色
                    radius = eyeRadius * eyeScale,
                    center = Offset(centerX + headSize * 0.4f, centerY - headSize * 0.2f),
                    style = Stroke(width = 4f)
                )
                
                // 绘制眼睛内部
                drawCircle(
                    color = Color(0xFF64B5F6),  // 浅蓝色
                    radius = eyeRadius * 0.7f * eyeScale,
                    center = Offset(centerX + headSize * 0.4f, centerY - headSize * 0.2f)
                )
                
                // 绘制瞳孔
                drawCircle(
                    color = Color.Black,
                    radius = eyeRadius * 0.3f * eyeScale,
                    center = Offset(centerX + headSize * 0.4f, centerY - headSize * 0.2f)
                )
            }
            
            // 绘制鼻子
            drawCircle(
                color = Color(0xFFFFCC80),  // 橙色
                radius = eyeRadius * 0.5f,
                center = Offset(centerX, centerY + headSize * 0.1f)
            )
            
            // 绘制嘴巴（微笑）
            val mouthPath = Path().apply {
                moveTo(centerX - headSize * 0.4f, centerY + headSize * 0.3f)
                quadraticTo(
                    centerX,
                    centerY + headSize * 0.5f,
                    centerX + headSize * 0.4f,
                    centerY + headSize * 0.3f
                )
            }
            
            drawPath(
                path = mouthPath,
                color = Color(0xFF795548),  // 棕色
                style = Stroke(width = 5f)
            )
            
            // 绘制牙齿
            drawRect(
                color = Color.White,
                topLeft = Offset(centerX - headSize * 0.1f, centerY + headSize * 0.3f),
                size = androidx.compose.ui.geometry.Size(headSize * 0.2f, headSize * 0.15f)
            )
        }
        
        Spacer(modifier = Modifier.height(20.dp))
        
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "服务启动中...",
                fontSize = 18.sp,
                color = Color.Gray.copy(alpha = textAlpha),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.width(10.dp))
            
            // 添加旋转的加载指示器
            Canvas(modifier = Modifier.size(24.dp)) {
                val canvasCenterX = size.width / 2
                val canvasCenterY = size.height / 2
                val radius = size.minDimension / 2 - 4
                
                // 绘制旋转的圆环
                drawCircle(
                    color = Color(0xFF2196F3).copy(alpha = 0.3f),
                    radius = radius,
                    center = Offset(canvasCenterX, canvasCenterY)
                )
                
                // 绘制旋转的弧形
                rotate(rotation, Offset(canvasCenterX, canvasCenterY)) {
                    drawArc(
                        color = Color(0xFF2196F3),
                        startAngle = 0f,
                        sweepAngle = 270f,
                        useCenter = false,
                        topLeft = Offset(canvasCenterX - radius, canvasCenterY - radius),
                        size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2),
                        style = Stroke(width = 3f)
                    )
                }
            }
        }
    }
}