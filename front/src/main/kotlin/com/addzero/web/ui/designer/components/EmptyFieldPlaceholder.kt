package com.addzero.web.ui.designer.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.foundation.background
import androidx.compose.ui.draw.shadow

/**
 * 空字段占位符
 * 在多列布局中显示一个空格占位符，提示用户可以添加字段
 */
@Composable
fun EmptyFieldPlaceholder(
    onClick: () -> Unit = {},
    isHighlighted: Boolean = false,
    modifier: Modifier = Modifier
) {
    // 增强的动画效果
    val highlightAlpha by animateFloatAsState(
        targetValue = if (isHighlighted) 0.2f else 0f,
        animationSpec = tween(150),
        label = "highlightAlpha"
    )
    
    val borderWidth by animateDpAsState(
        targetValue = if (isHighlighted) 4.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "borderWidth"
    )
    
    val elevation by animateDpAsState(
        targetValue = if (isHighlighted) 4.dp else 0.dp,
        label = "elevation"
    )
    
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(vertical = 4.dp)
            .shadow(elevation) // 添加阴影提升层次感
            .clip(RoundedCornerShape(4.dp))
            .border(
                width = borderWidth,
                color = if (isHighlighted) 
                    MaterialTheme.colorScheme.primary 
                else 
                    MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                shape = RoundedCornerShape(4.dp)
            ),
        color = if (isHighlighted)
            MaterialTheme.colorScheme.primary.copy(alpha = highlightAlpha)
        else
            MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
        onClick = onClick
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // 添加脉冲动画效果
            if (isHighlighted) {
                val infiniteTransition = rememberInfiniteTransition(label = "pulse")
                val scale by infiniteTransition.animateFloat(
                    initialValue = 0.97f,
                    targetValue = 1.03f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(800),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "pulse"
                )
                
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .scale(scale)
                        .background(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
                            shape = RoundedCornerShape(4.dp)
                        )
                )
            }
            
            // 文本和图标
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "添加字段",
                    tint = if (isHighlighted)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                    modifier = Modifier.size(24.dp)
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    if (isHighlighted) "放置在这里" else "添加字段",
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    color = if (isHighlighted)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                )
            }
        }
    }
} 