package com.addzero.web.ui.designer.editor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.web.ui.designer.models.FieldOption
import com.addzero.web.ui.designer.models.FieldValidator
import com.addzero.web.ui.designer.models.FormField

/**
 * 属性编辑器
 * 编辑选中字段的属性
 */
@Composable
fun PropertiesEditor(
    selectedField: FormField?,
    onFieldUpdated: (FormField) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        tonalElevation = 1.dp
    ) {
        if (selectedField == null) {
            // 未选中任何字段
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "请选择一个字段来编辑属性",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            // 编辑字段属性
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    "属性编辑",
                    style = MaterialTheme.typography.titleMedium
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // 基本属性
                Text(
                    "基本属性",
                    style = MaterialTheme.typography.titleSmall
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // 字段名称
                OutlinedTextField(
                    value = selectedField.name,
                    onValueChange = { onFieldUpdated(selectedField.copy(name = it)) },
                    label = { Text("字段名称") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // 字段标签
                OutlinedTextField(
                    value = selectedField.label,
                    onValueChange = { onFieldUpdated(selectedField.copy(label = it)) },
                    label = { Text("字段标签") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // 占位文本
                OutlinedTextField(
                    value = selectedField.placeholder,
                    onValueChange = { onFieldUpdated(selectedField.copy(placeholder = it)) },
                    label = { Text("占位文本") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // 默认值
                OutlinedTextField(
                    value = selectedField.defaultValue,
                    onValueChange = { onFieldUpdated(selectedField.copy(defaultValue = it)) },
                    label = { Text("默认值") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // 验证和约束
                Text(
                    "验证和约束",
                    style = MaterialTheme.typography.titleSmall
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // 必填
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "必填项",
                        modifier = Modifier.weight(1f)
                    )
                    Switch(
                        checked = selectedField.required,
                        onCheckedChange = { onFieldUpdated(selectedField.copy(required = it)) }
                    )
                }
                
                // 禁用
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "禁用",
                        modifier = Modifier.weight(1f)
                    )
                    Switch(
                        checked = selectedField.disabled,
                        onCheckedChange = { onFieldUpdated(selectedField.copy(disabled = it)) }
                    )
                }
                
                // 隐藏
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "隐藏",
                        modifier = Modifier.weight(1f)
                    )
                    Switch(
                        checked = selectedField.hidden,
                        onCheckedChange = { onFieldUpdated(selectedField.copy(hidden = it)) }
                    )
                }
                
                // 对于下拉框、单选框和多选框，显示选项编辑器
                if (selectedField.type in listOf("select", "radio", "multiselect")) {
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // 选项
                    Text(
                        "选项",
                        style = MaterialTheme.typography.titleSmall
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // 选项列表
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        selectedField.options.forEachIndexed { index, option ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                OutlinedTextField(
                                    value = option.value,
                                    onValueChange = { newValue ->
                                        val newOptions = selectedField.options.toMutableList()
                                        newOptions[index] = option.copy(value = newValue)
                                        onFieldUpdated(selectedField.copy(options = newOptions))
                                    },
                                    label = { Text("值") },
                                    modifier = Modifier.weight(1f)
                                )
                                
                                Spacer(modifier = Modifier.width(8.dp))
                                
                                OutlinedTextField(
                                    value = option.label,
                                    onValueChange = { newLabel ->
                                        val newOptions = selectedField.options.toMutableList()
                                        newOptions[index] = option.copy(label = newLabel)
                                        onFieldUpdated(selectedField.copy(options = newOptions))
                                    },
                                    label = { Text("标签") },
                                    modifier = Modifier.weight(1f)
                                )
                                
                                IconButton(
                                    onClick = {
                                        val newOptions = selectedField.options.toMutableList()
                                        newOptions.removeAt(index)
                                        onFieldUpdated(selectedField.copy(options = newOptions))
                                    }
                                ) {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "删除选项"
                                    )
                                }
                            }
                        }
                        
                        // 添加选项按钮
                        Button(
                            onClick = {
                                val newOptions = selectedField.options.toMutableList()
                                newOptions.add(FieldOption("option_${newOptions.size + 1}", "选项 ${newOptions.size + 1}"))
                                onFieldUpdated(selectedField.copy(options = newOptions))
                            },
                            modifier = Modifier.align(Alignment.End).padding(top = 8.dp)
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "添加选项",
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("添加选项")
                        }
                    }
                }
                
                // 验证器编辑
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    "验证规则",
                    style = MaterialTheme.typography.titleSmall
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // 验证器列表
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    selectedField.validators.forEachIndexed { index, validator ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = validator.type,
                                onValueChange = { newType ->
                                    val newValidators = selectedField.validators.toMutableList()
                                    newValidators[index] = validator.copy(type = newType)
                                    onFieldUpdated(selectedField.copy(validators = newValidators))
                                },
                                label = { Text("类型") },
                                modifier = Modifier.weight(1f)
                            )
                            
                            Spacer(modifier = Modifier.width(8.dp))
                            
                            OutlinedTextField(
                                value = validator.message,
                                onValueChange = { newMessage ->
                                    val newValidators = selectedField.validators.toMutableList()
                                    newValidators[index] = validator.copy(message = newMessage)
                                    onFieldUpdated(selectedField.copy(validators = newValidators))
                                },
                                label = { Text("消息") },
                                modifier = Modifier.weight(1f)
                            )
                            
                            IconButton(
                                onClick = {
                                    val newValidators = selectedField.validators.toMutableList()
                                    newValidators.removeAt(index)
                                    onFieldUpdated(selectedField.copy(validators = newValidators))
                                }
                            ) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "删除验证器"
                                )
                            }
                        }
                    }
                    
                    // 添加验证器按钮
                    Button(
                        onClick = {
                            val newValidators = selectedField.validators.toMutableList()
                            newValidators.add(FieldValidator("required", "此字段必填"))
                            onFieldUpdated(selectedField.copy(validators = newValidators))
                        },
                        modifier = Modifier.align(Alignment.End).padding(top = 8.dp)
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "添加验证器",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("添加验证器")
                    }
                }
            }
        }
    }
} 