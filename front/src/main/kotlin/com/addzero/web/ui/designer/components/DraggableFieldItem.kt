package com.addzero.web.ui.designer.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DragIndicator
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.addzero.web.ui.designer.models.FormField
import kotlin.math.roundToInt

/**
 * 可拖拽字段项
 * 支持拖拽操作的表单字段
 */
@Composable
fun DraggableFieldItem(
    field: FormField,
    isSelected: Boolean,
    onFieldSelected: (FormField) -> Unit,
    onFieldRemoved: (FormField) -> Unit,
    onDragStart: () -> Unit,
    onDragEnd: () -> Unit,
    onDragUpdate: (Offset) -> Unit,
    isDragging: Boolean = false,
    dragOffset: Offset = Offset.Zero,
    modifier: Modifier = Modifier
) {
    // 字段位置跟踪
    var fieldPosition by remember { mutableStateOf(Offset.Zero) }
    
    Box(
        modifier = modifier
            .offset { 
                if (isDragging) {
                    IntOffset(
                        dragOffset.x.roundToInt(),
                        dragOffset.y.roundToInt()
                    )
                } else IntOffset.Zero
            }
            .zIndex(if (isDragging) 1f else 0f)
            .shadow(if (isDragging) 8.dp else 0.dp)
            .onGloballyPositioned { coordinates ->
                fieldPosition = coordinates.positionInRoot()
            }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .alpha(if (isDragging) 0.7f else 1f)
                .border(
                    width = if (isSelected) 2.dp else 1.dp,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                )
                .background(
                    if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)
                    else MaterialTheme.colorScheme.surface
                )
                .padding(8.dp),
            onClick = { if (!isDragging) onFieldSelected(field) }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // 拖动手柄
                Box(
                    modifier = Modifier
                        .pointerInput(field.id) {
                            detectDragGestures(
                                onDragStart = { offset ->
                                    onDragStart()
                                },
                                onDragEnd = {
                                    onDragEnd()
                                },
                                onDragCancel = {
                                    onDragEnd()
                                },
                                onDrag = { change, dragAmount ->
                                    change.consume()
                                    onDragUpdate(dragAmount)
                                }
                            )
                        }
                        .padding(end = 8.dp)
                ) {
                    Icon(
                        Icons.Default.DragIndicator,
                        contentDescription = "拖动",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                
                // 字段内容预览
                val component = getComponentByType(field.type)
                if (component != null) {
                    FormComponent(
                        field = field,
                        component = component,
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    Text(
                        "未知组件类型: ${field.type}",
                        modifier = Modifier.weight(1f)
                    )
                }
                
                // 删除按钮
                IconButton(onClick = { onFieldRemoved(field) }) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "删除",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
} 