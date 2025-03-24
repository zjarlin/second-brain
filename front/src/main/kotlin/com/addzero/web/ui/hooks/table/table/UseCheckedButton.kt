package com.addzero.web.ui.hooks.table.table

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.web.ui.hooks.UseHook

class UseCheckedButton<E>( ): UseHook<UseCheckedButton<E>> {
    var selectedItems by mutableStateOf(emptyList<E>())
    var isEditMode by mutableStateOf(false)

    @OptIn(ExperimentalMaterial3Api::class)
    override val render: @Composable () -> Unit = {
        // 编辑模式切换按钮
        // 编辑模式切换按钮
        TooltipBox(
            positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
            tooltip = {
                PlainTooltip { Text(if (isEditMode) "退出编辑" else "进入编辑") }
            },
            state = rememberTooltipState()
        ) {
            FilledTonalIconToggleButton(
                checked = isEditMode,
                onCheckedChange = {
                    isEditMode = !isEditMode
                    if (!isEditMode) {
                        selectedItems = emptyList()
                    }
                },
                modifier = Modifier.Companion.padding(end = 8.dp)
            ) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = if (isEditMode) "退出编辑" else "进入编辑",
                    tint = if (isEditMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

    }
 }