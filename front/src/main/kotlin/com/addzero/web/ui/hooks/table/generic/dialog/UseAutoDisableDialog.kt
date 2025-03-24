package com.addzero.web.ui.hooks.table.generic.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import com.addzero.web.ui.hooks.UseHook
import kotlinx.coroutines.delay

class UseAutoDisableDialog(val text: String) : UseHook<UseAutoDisableDialog> {
    // 使用 LaunchedEffect 实现 2 秒后自动关闭
    var showFlag by mutableStateOf(false)

    override val render: @Composable () -> Unit
        get() = {

            LaunchedEffect(Unit) {
                delay(2000) // 延迟 2 秒
                showFlag = false
            }
            if (showFlag) {
                AlertDialog(
                    onDismissRequest = { showFlag = false }, // 用户手动关闭时也调用 onDismiss
                    title = { Text(text = "提示") },
                    text = { Text(text = text) },
                    confirmButton = {}, // 不需要确认按钮
                    modifier = modifier
                )
            }


        }
}