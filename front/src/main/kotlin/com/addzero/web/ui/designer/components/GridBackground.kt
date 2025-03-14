package com.addzero.web.ui.designer.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * 表单网格背景
 * 显示表单列的网格布局
 */
@Composable
fun GridBackground(
    columnCount: Int,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        // 列网格
        Row(
            modifier = Modifier.fillMaxWidth().height(50.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            for (i in 1..columnCount) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(horizontal = 2.dp)
                        .background(
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f),
                            shape = MaterialTheme.shapes.small
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "列 $i",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                    )
                }
            }
        }
        
        // 列分隔线
        Row(modifier = Modifier.fillMaxSize()) {
            for (i in 1 until columnCount) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    // 只在列之间添加分隔线
                    if (i < columnCount) {
                        Divider(
                            modifier = Modifier
                                .width(1.dp)
                                .fillMaxHeight()
                                .align(Alignment.CenterEnd),
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                        )
                    }
                }
            }
            // 最后一列
            Box(modifier = Modifier.weight(1f))
        }
    }
} 