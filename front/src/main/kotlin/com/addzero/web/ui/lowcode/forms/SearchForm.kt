package com.addzero.web.ui.lowcode.forms

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.web.ui.hooks.table.entity.RenderType
import com.addzero.web.ui.lowcode.core.DynamicForm
import com.addzero.web.ui.lowcode.core.RenderField
import com.addzero.web.ui.lowcode.metadata.FieldMetadata
import com.addzero.web.ui.lowcode.renderers.DefaultFieldRenderer

/**
 * 搜索表单
 * 用于构建搜索条件
 */
@Composable
fun <E : Any> SearchForm(
    initialData: E,
    fields: List<FieldMetadata<E>>,
    onSearch: (E) -> Unit,
    onReset: () -> Unit = {},
    columnCount: Int = 3,
    renderField: RenderField<E> = { field, value, onChange, error -> 
        DefaultFieldRenderer(field, value, onChange, error)
    },
    compact: Boolean = true, // 控制是否使用紧凑样式
    transparent: Boolean = true // 控制是否使用透明背景
) {
    var formData by remember { mutableStateOf(initialData) }
    
    Column(
        modifier = Modifier.fillMaxWidth().padding(
            horizontal = if (compact) 8.dp else 16.dp,
            vertical = if (compact) 4.dp else 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(if (compact) 2.dp else 8.dp)
    ) {
        // 表单内容
        val searchFields = fields.filter { it.showInSearch }
        
        // 使用Grid布局
        val rows = (searchFields.size + columnCount - 1) / columnCount
        for (row in 0 until rows) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(if (compact) 4.dp else 16.dp)
            ) {
                for (col in 0 until columnCount) {
                    val index = row * columnCount + col
                    if (index < searchFields.size) {
                        Box(modifier = Modifier.weight(1f)) {
                            // 使用自定义的紧凑型渲染器
                            val compactRenderField: RenderField<E> = if (compact) {
                                { field, value, onChange, error ->
                                    CompactFieldRenderer(field, value, onChange, error, transparent)
                                }
                            } else {
                                renderField
                            }
                            
                            DynamicForm(
                                data = formData,
                                fields = listOf(searchFields[index]),
                                renderField = compactRenderField,
                                onDataChange = { formData = it }
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
        
        // 按钮行
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = if (compact) 2.dp else 8.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = {
                    formData = initialData
                    onReset()
                },
                modifier = Modifier.padding(end = 4.dp),
                contentPadding = PaddingValues(
                    horizontal = if (compact) 12.dp else 16.dp,
                    vertical = if (compact) 6.dp else 8.dp
                )
            ) {
                Text("重置", style = MaterialTheme.typography.bodySmall)
            }
            
            Button(
                onClick = { onSearch(formData) },
                contentPadding = PaddingValues(
                    horizontal = if (compact) 12.dp else 16.dp,
                    vertical = if (compact) 6.dp else 8.dp
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "搜索",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("搜索", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

/**
 * 紧凑型字段渲染器
 * 用于搜索表单的紧凑显示
 */
@Composable
private fun <E : Any> CompactFieldRenderer(
    field: FieldMetadata<E>,
    value: Any?,
    onChange: (Any?) -> Unit,
    error: String?,
    transparent: Boolean = true
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 1.dp)) {
        // 如果有自定义渲染函数，使用自定义渲染
        if (field.customRender != null) {
            field.customRender.invoke(value)
            return
        }
        
        // 使用紧凑型的输入控件
        when (field.renderType) {
            RenderType.TEXT, RenderType.NUMBER, RenderType.DATE -> {
                OutlinedTextField(
                    value = value?.toString() ?: "",
                    onValueChange = { onChange(it) },
                    label = { 
                        Text(
                            text = field.title,
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    placeholder = { 
                        Text(
                            text = field.placeholder,
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    isError = error != null,
                    enabled = field.enabled,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodySmall,
                    colors = if (transparent) {
                        OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedContainerColor = MaterialTheme.colorScheme.surface
                        )
                    } else {
                        OutlinedTextFieldDefaults.colors()
                    }
                )
            }
            RenderType.SELECT -> {
                val options = field.options ?: emptyList()
                var expanded by remember { mutableStateOf(false) }
                val selectedOption = options.find { it.value == value }
                
                OutlinedTextField(
                    value = selectedOption?.label ?: "",
                    onValueChange = { },
                    readOnly = true,
                    label = { 
                        Text(
                            text = field.title,
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodySmall,
                    trailingIcon = {
                        IconButton(onClick = { expanded = true }) {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "展开",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    },
                    colors = if (transparent) {
                        OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedContainerColor = MaterialTheme.colorScheme.surface
                        )
                    } else {
                        OutlinedTextFieldDefaults.colors()
                    }
                )
                
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    options.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option.label, style = MaterialTheme.typography.bodySmall) },
                            onClick = {
                                onChange(option.value)
                                expanded = false
                            }
                        )
                    }
                }
            }
            RenderType.BOOL_SWITCH -> {
                Row(
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = field.title,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.weight(1f)
                    )
                    Switch(
                        checked = (value as? Boolean) ?: false,
                        onCheckedChange = { onChange(it) },
                        enabled = field.enabled
                    )
                }
            }
            else -> {
                // 对于其他类型，使用默认渲染器
                DefaultFieldRenderer(field, value, onChange, error)
            }
        }
    }
} 