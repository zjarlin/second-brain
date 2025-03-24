package com.addzero.web.ui.hooks.table.table

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/**
 * 工具按钮
 * @param [label]
 * @param [onClick]
 * @param [icon]
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolButton(
    showFlag: Boolean=true,
    enabled: Boolean=true,
    label: String, onClick: () -> Unit, icon: ImageVector = Icons.Default
        .Add, tint: Color=MaterialTheme.colorScheme.primary
) {
    if (!showFlag) return
    TooltipBox(
        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
        tooltip = {
            PlainTooltip { Text(label) }
        },
        state = rememberTooltipState()
    ) {
        FilledTonalIconButton(
            onClick = onClick,
            enabled = enabled,
            modifier = Modifier.Companion.padding(end = 8.dp)
        ) {
            Icon(
                icon,
                contentDescription = label,
                tint = tint
            )
        }
    }

}





