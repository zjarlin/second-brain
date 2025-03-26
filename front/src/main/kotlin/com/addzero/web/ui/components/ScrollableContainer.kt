package com.addzero.web.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay

/**
 * 可滚动容器高阶组件
 * 根据参数控制滚动方向（水平或垂直），并在滚动时显示滚动条
 *
 * @param orientation 滚动方向，"horizontal"为水平滚动，"vertical"为垂直滚动
 * @param modifier 可选的修饰符
 * @param showScrollbar 是否显示滚动条
 * @param content 内容组件
 */
@Composable
fun ScrollableContainer(
    orientation: Orientation = Orientation.Vertical,
    modifier: Modifier = Modifier,
    showScrollbar: Boolean = true,
    content: @Composable () -> Unit
) {
    val scrollState = rememberScrollState()
    var isScrollbarVisible by remember { mutableStateOf(false) }

    // 监听滚动状态变化
    LaunchedEffect(scrollState.value) {
        isScrollbarVisible = true
        delay(1500) // 延迟1.5秒后隐藏滚动条
        isScrollbarVisible = false
    }

    Box(modifier = modifier) {
        // 根据方向应用不同的滚动修饰符
        val contentModifier = when (orientation) {
            Orientation.Horizontal -> Modifier.horizontalScroll(scrollState)
            else -> Modifier.verticalScroll(scrollState)
        }

        // 渲染内容
        Box(modifier = contentModifier.fillMaxWidth().fillMaxHeight()) {
            content()
        }

        // 显示滚动条（如果启用）
        if (showScrollbar) {
            AnimatedVisibility(
                visible = isScrollbarVisible && scrollState.maxValue > 0,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = when (orientation) {
                    Orientation.Horizontal -> Modifier.align(Alignment.BottomCenter)
                    else -> Modifier.align(Alignment.CenterEnd)
                }
            ) {
                when (orientation) {
                    Orientation.Horizontal -> {
                        HorizontalScrollbar(
                            modifier = Modifier.fillMaxWidth(),
                            adapter = rememberScrollbarAdapter(scrollState)
                        )
                    }
                    else -> {
                        VerticalScrollbar(
                            modifier = Modifier.fillMaxHeight(),
                            adapter = rememberScrollbarAdapter(scrollState)
                        )
                    }
                }
            }
        }
    }
}