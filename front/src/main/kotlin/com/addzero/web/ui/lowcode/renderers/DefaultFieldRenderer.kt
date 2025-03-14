package com.addzero.web.ui.lowcode.renderers

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.addzero.web.ui.lowcode.core.RenderField
import com.addzero.web.ui.lowcode.metadata.FieldMetadata
import com.addzero.web.ui.hooks.table.entity.RenderType
import java.text.SimpleDateFormat
import java.util.*

/**
 * 默认字段渲染器
 */
@Composable
fun <E : Any> DefaultFieldRenderer(
    field: FieldMetadata<E>,
    value: Any?,
    onChange: (Any?) -> Unit,
    error: String?
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        // 如果有自定义渲染函数，使用自定义渲染
        if (field.customRender != null) {
            field.customRender.invoke(value)
            return
        }
        
        // 根据渲染类型选择不同的表单控件
        when (field.renderType) {
            RenderType.TEXT -> TextFieldRenderer(field, value, onChange, error)
            RenderType.TEXTAREA -> TextAreaRenderer(field, value, onChange, error)
            RenderType.NUMBER -> NumberFieldRenderer(field, value, onChange, error)
            RenderType.SWITCH -> SwitchRenderer(field, value, onChange, error)
            RenderType.CHECKBOX -> CheckboxRenderer(field, value, onChange, error)
            RenderType.SELECT -> SelectRenderer(field, value, onChange, error)
            RenderType.DATE -> DateFieldRenderer(field, value, onChange, error)
            RenderType.RADIO -> RadioRenderer(field, value, onChange, error)
            RenderType.MULTISELECT -> MultiSelectRenderer(field, value, onChange, error)
            // 其他类型...
            else -> TextFieldRenderer(field, value, onChange, error)
        }
    }
}

// 文本输入渲染器
@Composable
private fun <E : Any> TextFieldRenderer(
    field: FieldMetadata<E>,
    value: Any?,
    onChange: (Any?) -> Unit,
    error: String?
) {
    OutlinedTextField(
        value = value?.toString() ?: "",
        onValueChange = { onChange(it) },
        label = { 
            if (field.required) {
                Text("* ${field.title}")
            } else {
                Text(field.title)
            }
        },
        placeholder = { Text(field.placeholder) },
        isError = error != null,
        enabled = field.enabled,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        supportingText = {
            if (error != null) {
                Text(error, color = MaterialTheme.colorScheme.error)
            }
        }
    )
}

// 文本区域渲染器
@Composable
private fun <E : Any> TextAreaRenderer(
    field: FieldMetadata<E>,
    value: Any?,
    onChange: (Any?) -> Unit,
    error: String?
) {
    OutlinedTextField(
        value = value?.toString() ?: "",
        onValueChange = { onChange(it) },
        label = { 
            if (field.required) {
                Text("* ${field.title}")
            } else {
                Text(field.title)
            }
        },
        placeholder = { Text(field.placeholder) },
        isError = error != null,
        enabled = field.enabled,
        modifier = Modifier.fillMaxWidth().height(120.dp),
        maxLines = 5,
        supportingText = {
            if (error != null) {
                Text(error, color = MaterialTheme.colorScheme.error)
            }
        }
    )
}

// 数字输入渲染器
@Composable
private fun <E : Any> NumberFieldRenderer(
    field: FieldMetadata<E>,
    value: Any?,
    onChange: (Any?) -> Unit,
    error: String?
) {
    OutlinedTextField(
        value = value?.toString() ?: "",
        onValueChange = { 
            val numberValue = it.toIntOrNull() ?: it.toDoubleOrNull()
            onChange(numberValue ?: it)
        },
        label = { 
            if (field.required) {
                Text("* ${field.title}")
            } else {
                Text(field.title)
            }
        },
        placeholder = { Text(field.placeholder) },
        isError = error != null,
        enabled = field.enabled,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        supportingText = {
            if (error != null) {
                Text(error, color = MaterialTheme.colorScheme.error)
            }
        }
    )
}

// 开关渲染器
@Composable
private fun <E : Any> SwitchRenderer(
    field: FieldMetadata<E>,
    value: Any?,
    onChange: (Any?) -> Unit,
    error: String?
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = if (field.required) "* ${field.title}" else field.title,
            style = MaterialTheme.typography.bodyMedium
        )
        
        Switch(
            checked = value as? Boolean ?: false,
            onCheckedChange = { onChange(it) },
            enabled = field.enabled
        )
    }
    
    if (error != null) {
        Text(error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
    }
}

// 复选框渲染器
@Composable
private fun <E : Any> CheckboxRenderer(
    field: FieldMetadata<E>,
    value: Any?,
    onChange: (Any?) -> Unit,
    error: String?
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = value as? Boolean ?: false,
            onCheckedChange = { onChange(it) },
            enabled = field.enabled
        )
        
        Text(
            text = if (field.required) "* ${field.title}" else field.title,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
    
    if (error != null) {
        Text(error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
    }
}

// 下拉选择渲染器
@Composable
private fun <E : Any> SelectRenderer(
    field: FieldMetadata<E>,
    value: Any?,
    onChange: (Any?) -> Unit,
    error: String?
) {
    var expanded by remember { mutableStateOf(false) }
    val options = field.options ?: emptyList()
    
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = if (field.required) "* ${field.title}" else field.title,
            style = MaterialTheme.typography.bodyMedium
        )
        
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(
                onClick = { expanded = true },
                modifier = Modifier.fillMaxWidth(),
                enabled = field.enabled
            ) {
                Text(
                    text = options.find { it.value == value }?.label ?: field.placeholder,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option.label) },
                        onClick = {
                            onChange(option.value)
                            expanded = false
                        }
                    )
                }
            }
        }
        
        if (error != null) {
            Text(error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }
    }
}

// 日期渲染器
@Composable
private fun <E : Any> DateFieldRenderer(
    field: FieldMetadata<E>,
    value: Any?,
    onChange: (Any?) -> Unit,
    error: String?
) {
    // 简化实现，实际应用中应该使用日期选择器
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val dateString = when (value) {
        is Date -> dateFormat.format(value)
        is java.time.LocalDate -> value.toString()
        else -> value?.toString() ?: ""
    }
    
    OutlinedTextField(
        value = dateString,
        onValueChange = { onChange(it) },
        label = { 
            if (field.required) {
                Text("* ${field.title}")
            } else {
                Text(field.title)
            }
        },
        placeholder = { Text(field.placeholder) },
        isError = error != null,
        enabled = field.enabled,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        supportingText = {
            if (error != null) {
                Text(error, color = MaterialTheme.colorScheme.error)
            }
        }
    )
}

// 单选按钮渲染器
@Composable
private fun <E : Any> RadioRenderer(
    field: FieldMetadata<E>,
    value: Any?,
    onChange: (Any?) -> Unit,
    error: String?
) {
    val options = field.options ?: emptyList()
    
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = if (field.required) "* ${field.title}" else field.title,
            style = MaterialTheme.typography.bodyMedium
        )
        
        options.forEach { option ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = option.value == value,
                    onClick = { onChange(option.value) },
                    enabled = field.enabled
                )
                
                Text(
                    text = option.label,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
        
        if (error != null) {
            Text(error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }
    }
}

// 多选渲染器
@Composable
private fun <E : Any> MultiSelectRenderer(
    field: FieldMetadata<E>,
    value: Any?,
    onChange: (Any?) -> Unit,
    error: String?
) {
    val options = field.options ?: emptyList()
    val selectedValues = when (value) {
        is List<*> -> value
        is Array<*> -> value.toList()
        else -> emptyList<Any?>()
    }
    
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = if (field.required) "* ${field.title}" else field.title,
            style = MaterialTheme.typography.bodyMedium
        )
        
        options.forEach { option ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = selectedValues.contains(option.value),
                    onCheckedChange = { checked ->
                        val newValues = if (checked) {
                            selectedValues + option.value
                        } else {
                            selectedValues.filter { it != option.value }
                        }
                        onChange(newValues)
                    },
                    enabled = field.enabled
                )
                
                Text(
                    text = option.label,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
        
        if (error != null) {
            Text(error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }
    }
} 