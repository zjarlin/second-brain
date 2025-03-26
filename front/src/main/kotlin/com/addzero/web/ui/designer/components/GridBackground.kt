package com.addzero.web.ui.designer.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp

/**
 * 表单网格背景
 * 显示表单列的网格布局
 */
@Composable
fun GridBackground(
    columnCount: Int,
    modifier: Modifier = Modifier,
    isDragging: Boolean = false,
    dragPosition: Offset = Offset.Zero
) {
    val rowHeight = 80.dp
    val columnWidth = 1f / columnCount

    // 计算当前拖拽位置对应的网格单元
    val row = (dragPosition.y / rowHeight.value).toInt().coerceAtLeast(0)
    val column = (dragPosition.x / columnWidth).toInt().coerceIn(0, columnCount - 1)

    Box(modifier = modifier) {
        // 列网格
        Row(
            modifier = Modifier.fillMaxWidth(),
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
                    Divider(
                        modifier = Modifier
                            .width(1.dp)
                            .fillMaxHeight()
                            .align(Alignment.CenterEnd),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    )
                }
            }
            Box(modifier = Modifier.weight(1f))
        }
        
        // 拖拽放置指示器
        if (isDragging) {
            val cellWidth = (modifier.fillMaxWidth().toString().toFloat() / columnCount)
            
            // 网格单元高亮
            Box(
                modifier = Modifier
                    .offset(
                        x = (column * cellWidth).dp,
                        y = (row * rowHeight.value).dp
                    )
                    .width(cellWidth.dp)
                    .height(rowHeight)
                    .background(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                        shape = MaterialTheme.shapes.small
                    )
            )
            
            // 吸附指示线
            Box(
                modifier = Modifier
                    .offset(
                        x = (column * cellWidth).dp,
                        y = 0.dp
                    )
                    .width(2.dp)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
            )
            
            Box(
                modifier = Modifier
                    .offset(
                        x = 0.dp,
                        y = (row * rowHeight.value).dp
                    )
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
            )
        }
    }
} 