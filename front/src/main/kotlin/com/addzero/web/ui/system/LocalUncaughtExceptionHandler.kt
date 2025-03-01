package com.addzero.web.ui.system

import androidx.compose.runtime.compositionLocalOf

/**
 * 全局未捕获异常处理器的CompositionLocal
 */
val LocalUncaughtExceptionHandler = compositionLocalOf<Thread.UncaughtExceptionHandler?> {
    // 默认返回null，表示没有设置异常处理器
    null
}