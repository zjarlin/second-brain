package com.addzero.web.ui.hooks.table.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.addzero.web.ui.hooks.UseHook

/**
 * confirm确认提示
 */
class UseDeletedConfirmDialog<E>(
    private val onDismiss: () -> Unit = {},
    private val onConfirm: () -> Unit = {},
) : UseHook<UseDeletedConfirmDialog<E>> {

    /**
     *是否显示
     */
    var showFlag by mutableStateOf(false)

    override val render: @Composable () -> Unit = {
        if (showFlag) {
            AlertDialog(
                onDismissRequest = onDismiss,
                title = { Text("确认删除") },
                text = { Text("确定删除这条记录吗？") },
                confirmButton = {
                    TextButton(onClick = {
                        showFlag = false
                        onConfirm()
                    }) {
                        Text("确认")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showFlag = false
                        onDismiss()
                    }) {
                        Text("取消")
                    }
                })

        }


    }
}


