package com.addzero.web.ui.system

import androidx.compose.runtime.*
import com.addzero.web.log
import com.addzero.web.ui.components.dialog.AddDialog
import com.addzero.web.ui.components.dialog.DefaultDialogButton

/**
 * 全局异常状态管理
 */
object ErrorState {
    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    fun showError(message: String) {
        _errorMessage.value = message
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun showError(throwable: Throwable) {
        _errorMessage.value = throwable.message ?: "未知错误"
    }
}

/**
 * 全局异常处理组件
 * 用于捕获和显示应用中的未处理异常
 */
@Composable
fun ErrorHandler(content: @Composable () -> Unit) {
    // 设置全局未捕获异常处理器
    val currentHandler = LocalUncaughtExceptionHandler.current
    DisposableEffect(Unit) {
        val handler = Thread.UncaughtExceptionHandler { thread, throwable ->
            log.error("未捕获的异常: ${throwable.message}", throwable)
            ErrorState.showError(throwable)
            // 调用原始处理器
            currentHandler?.uncaughtException(thread, throwable)
        }
        Thread.setDefaultUncaughtExceptionHandler(handler)
        onDispose {
            // 恢复原始处理器
            Thread.setDefaultUncaughtExceptionHandler(currentHandler)
        }
    }

    // 错误弹窗
    val errorMessage by ErrorState.errorMessage
    if (errorMessage != null) {
        AddDialog(
            onDismissRequest = { ErrorState.clearError() },
            title = "错误提示",
            content = {
                androidx.compose.material3.Text(text = errorMessage ?: "")
            },
            confirmButton = {
                DefaultDialogButton(
                    onClick = { ErrorState.clearError() },
                    text = "确定"
                )
            }
        )
    }

    // 渲染内容
    content()
}