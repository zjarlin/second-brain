package com.addzero.web.ui.designer.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.ToggleOn
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlin.math.roundToInt

/**
 * 组件类型定义
 */
data class ComponentType(
    val id: String,
    val name: String,
    val icon: @Composable () -> Unit,
    val description: String
)

/**
 * 可用组件列表
 */
val availableComponents = listOf(
    ComponentType(
        id = "text",
        name = "文本输入",
        icon = { Icon(Icons.Default.TextFields, contentDescription = "文本输入") },
        description = "单行文本输入"
    ),
    ComponentType(
        id = "textarea",
        name = "多行文本",
        icon = { Icon(Icons.AutoMirrored.Filled.Notes, contentDescription = "多行文本") },
        description = "多行文本输入"
    ),
    ComponentType(
        id = "number",
        name = "数字输入",
        icon = { Icon(Icons.Default.Numbers, contentDescription = "数字输入") },
        description = "数字输入框"
    ),
    ComponentType(
        id = "select",
        name = "下拉选择",
        icon = { Icon(Icons.Default.ArrowDropDown, contentDescription = "下拉选择") },
        description = "下拉选择框"
    ),
    ComponentType(
        id = "checkbox",
        name = "复选框",
        icon = { Icon(Icons.Default.CheckBox, contentDescription = "复选框") },
        description = "复选框"
    ),
    ComponentType(
        id = "switch",
        name = "开关",
        icon = { Icon(Icons.Default.ToggleOn, contentDescription = "开关") },
        description = "开关控件"
    ),
    ComponentType(
        id = "date",
        name = "日期选择",
        icon = { Icon(Icons.Default.DateRange, contentDescription = "日期选择") },
        description = "日期选择器"
    ),
    ComponentType(
        id = "radio",
        name = "单选框",
        icon = { Icon(Icons.Default.RadioButtonChecked, contentDescription = "单选框") },
        description = "单选框组"
    ),
    ComponentType(
        id = "multiselect",
        name = "多选框",
        icon = { Icon(Icons.Default.CheckBox, contentDescription = "多选框") },
        description = "多选框组"
    )
)

/**
 * 根据类型ID获取组件
 */
fun getComponentByType(typeId: String): ComponentType? {
    return availableComponents.find { it.id == typeId }
}

/**
 * 组件面板
 * 显示可用的表单组件
 */
@Composable
fun ComponentPanel(
    onComponentDrag: (String) -> Unit,
    onComponentDragStart: (String) -> Unit = {},
    onComponentDragMove: (Offset) -> Unit = {},
    onComponentDragEnd: (Boolean) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var isDragging by remember { mutableStateOf(false) }
    var draggedComponent by remember { mutableStateOf("") }
    var dragPosition by remember { mutableStateOf(Offset.Zero) }
    
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(8.dp)
    ) {
        Text(
            "组件",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        
        // 组件分类
        Text(
            "基础组件",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )
        
        // 组件列表
        availableComponents.forEach { component ->
            DraggableComponent(
                type = component.id,
                component = component,
                onDragStart = {
                    isDragging = true
                    draggedComponent = component.id
                    onComponentDragStart(component.id)
                },
                onDragEnd = { dropped ->
                    isDragging = false
                    onComponentDragEnd(dropped)
                },
                onDragMove = { offset ->
                    dragPosition = offset
                    onComponentDragMove(offset)
                },
                onClick = { onComponentDrag(component.id) }
            )
        }
    }
    
    // 如果正在拖拽，显示悬浮预览
    if (isDragging) {
        Box(
            modifier = Modifier
                .offset {
                    IntOffset(
                        (dragPosition.x + 10).roundToInt(),
                        (dragPosition.y + 10).roundToInt()
                    )
                }
                .zIndex(100f)
                .shadow(8.dp, MaterialTheme.shapes.small)
                .background(MaterialTheme.colorScheme.surface)
                .padding(8.dp)
                .width(200.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(0.85f)
            ) {
                val component = getComponentByType(draggedComponent)
                if (component != null) {
                    FormComponent(
                        field = com.addzero.web.ui.designer.models.FormField(
                            id = "preview",
                            name = "preview",
                            label = component.name,
                            type = component.id
                        ),
                        component = component
                    )
                }
            }
        }
    }
}

@Composable
fun DraggableComponent(
    type: String,
    component: ComponentType,
    onDragStart: () -> Unit,
    onDragEnd: (Boolean) -> Unit,
    onDragMove: (Offset) -> Unit,
    onClick: () -> Unit
) {
    var isDragging by remember { mutableStateOf(false) }
    
    // 添加悬停和拖拽状态动画
    val elevation by animateDpAsState(
        targetValue = if (isDragging) 8.dp else 1.dp,
        label = "componentElevation"
    )
    
    val scale by animateFloatAsState(
        targetValue = if (isDragging) 0.95f else 1f,
        label = "componentScale"
    )
    
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .scale(scale) // 缩放效果
            .shadow(elevation) // 阴影效果
            .pointerInput(type) {
                detectDragGestures(
                    onDragStart = {
                        isDragging = true
                        onDragStart()
                    },
                    onDragEnd = {
                        // 检测是否拖放到了有效区域
                        val dropped = true // 这里需要实际检测是否在有效区域
                        isDragging = false
                        onDragEnd(dropped)
                    },
                    onDragCancel = {
                        isDragging = false
                        onDragEnd(false)
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        onDragMove(change.position)
                    }
                )
            },
        onClick = onClick,
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .padding(end = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                component.icon()
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                component.name,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

/**
 * 组件项
 * 显示单个组件
 */
@Composable
fun ComponentItem(
    component: ComponentType,
    onDrag: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        onClick = onDrag,
        shape = MaterialTheme.shapes.small
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                component.icon()
            }
            
            Column(modifier = Modifier.padding(start = 8.dp)) {
                Text(
                    component.name,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    component.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * 表单组件预览
 */
@Composable
fun FormComponent(
    field: com.addzero.web.ui.designer.models.FormField,
    component: ComponentType,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        when (component.id) {
            "text" -> OutlinedTextField(
                value = field.defaultValue,
                onValueChange = { },
                label = { Text(field.label) },
                placeholder = { Text(field.placeholder) },
                modifier = Modifier.fillMaxWidth(),
                enabled = false
            )
            "textarea" -> OutlinedTextField(
                value = field.defaultValue,
                onValueChange = { },
                label = { Text(field.label) },
                placeholder = { Text(field.placeholder) },
                modifier = Modifier.fillMaxWidth().height(100.dp),
                enabled = false
            )
            "number" -> OutlinedTextField(
                value = field.defaultValue,
                onValueChange = { },
                label = { Text(field.label) },
                placeholder = { Text(field.placeholder) },
                modifier = Modifier.fillMaxWidth(),
                enabled = false
            )
            "select" -> OutlinedTextField(
                value = "下拉选择",
                onValueChange = { },
                label = { Text(field.label) },
                modifier = Modifier.fillMaxWidth(),
                enabled = false,
                trailingIcon = {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }
            )
            "checkbox" -> Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = false,
                    onCheckedChange = { },
                    enabled = false
                )
                Text(field.label, modifier = Modifier.padding(start = 8.dp))
            }
            "switch" -> Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(field.label)
                Switch(
                    checked = false,
                    onCheckedChange = { },
                    enabled = false
                )
            }
            "date" -> OutlinedTextField(
                value = "选择日期",
                onValueChange = { },
                label = { Text(field.label) },
                modifier = Modifier.fillMaxWidth(),
                enabled = false,
                trailingIcon = {
                    Icon(Icons.Default.DateRange, contentDescription = null)
                }
            )
            "radio" -> Column {
                Text(field.label)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = true,
                        onClick = { },
                        enabled = false
                    )
                    Text("选项1", modifier = Modifier.padding(start = 4.dp))
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = false,
                        onClick = { },
                        enabled = false
                    )
                    Text("选项2", modifier = Modifier.padding(start = 4.dp))
                }
            }
            "multiselect" -> Column {
                Text(field.label)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = true,
                        onCheckedChange = { },
                        enabled = false
                    )
                    Text("选项1", modifier = Modifier.padding(start = 4.dp))
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = false,
                        onCheckedChange = { },
                        enabled = false
                    )
                    Text("选项2", modifier = Modifier.padding(start = 4.dp))
                }
            }
            else -> Text("未知组件: ${component.id}")
        }
    }
} 