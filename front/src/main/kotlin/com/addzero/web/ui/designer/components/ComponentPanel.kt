package com.addzero.web.ui.designer.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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
        icon = { Icon(Icons.Default.Notes, contentDescription = "多行文本") },
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
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        tonalElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                "组件",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(8.dp)
            )
            
            LazyColumn {
                items(availableComponents) { component ->
                    ComponentItem(
                        component = component,
                        onDrag = { onComponentDrag(component.id) }
                    )
                }
            }
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