package com.addzero.web.ui.hooks.form

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.addzero.common.kt_util.toNotBlankStr
import com.addzero.web.ui.hooks.table.entity.IColumn
import com.addzero.web.ui.hooks.table.entity.RenderType
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <E : Any> FormItem(
    icolumn: IColumn<E>,
    useDynamicForm: UseDynamicForm<E>,
    currentSelectItem: E?
) {
    val currentFormItem = currentSelectItem
    val renderType = icolumn.renderType
    val getFun = icolumn.getFun
    val setFun = icolumn.setFun

    val fieldValue = currentFormItem?.let { getFun(it) }
    val validRes = currentFormItem?.let { icolumn.validator(it) }
    val text = fieldValue.toNotBlankStr()

    // 通用标签生成函数
    val label: @Composable () -> Unit = {
        if (icolumn.required) {
            Text("* ${icolumn.title}")
        } else {
            Text(icolumn.title)
        }
    }

    // 通用值更新函数
    fun updateValue(newValue: Any?) {
        val oldValue = currentFormItem?.let { icolumn.getFun(it) }
        // 只有当值真正发生变化时才更新表单
        if (oldValue != newValue) {
            val newItem = currentFormItem?.let { setFun(it, icolumn, newValue) }
            useDynamicForm.updateFormItem(newItem)
        }
    }

    when (renderType) {
        RenderType.TEXT -> {
            OutlinedTextField(
                enabled = icolumn.enabled,
                label = label,
                value = text,
                onValueChange = { updateValue(it) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(icolumn.placeholder) },
                isError = validRes == false,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
        }

        RenderType.TEXTAREA -> {
            OutlinedTextField(
                enabled = icolumn.enabled,
                label = label,
                value = text,
                onValueChange = { updateValue(it) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(icolumn.placeholder) },
                isError = validRes == false,
                maxLines = 5,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
        }

        RenderType.NUMBER -> {
            OutlinedTextField(
                enabled = icolumn.enabled,
                label = label,
                value = text,
                onValueChange = { newValue ->
                    try {
                        // 尝试将输入转换为数字
                        if (newValue.isEmpty()) {
                            updateValue(null)
                        } else {
                            val numValue = when (fieldValue) {
                                is Int -> newValue.toIntOrNull()
                                is Long -> newValue.toLongOrNull()
                                is Float -> newValue.toFloatOrNull()
                                is Double -> newValue.toDoubleOrNull()
                                else -> newValue.toDoubleOrNull()
                            }
                            if (numValue != null) {
                                updateValue(numValue)
                            }
                        }
                    } catch (e: Exception) {
                        // 转换失败时不更新
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(icolumn.placeholder) },
                isError = validRes == false,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        RenderType.SWITCH -> {
            Column {
                Text(icolumn.title)
                Switch(
                    enabled = icolumn.enabled,
                    checked = fieldValue == true,
                    onCheckedChange = { updateValue(it) }
                )
            }
        }

        RenderType.CHECKBOX -> {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    enabled = icolumn.enabled,
                    checked = fieldValue == true,
                    onCheckedChange = { updateValue(it) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(icolumn.title)
            }
        }

        RenderType.SELECT -> {
            var expanded by remember { mutableStateOf(false) }
            // 直接从IColumn获取选项
            val options = icolumn.options ?: emptyList()
            
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(icolumn.title, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(4.dp))
                
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {
                    OutlinedTextField(
                        value = text,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth().menuAnchor(),
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        placeholder = { Text(icolumn.placeholder) },
                        isError = validRes == false,
                        enabled = icolumn.enabled
                    )
                    
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        options.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option.label) },
                                onClick = {
                                    updateValue(option.value)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        }

        RenderType.DATE -> {
            var showDatePicker by remember { mutableStateOf(false) }
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    enabled = icolumn.enabled,
                    label = label,
                    value = if (fieldValue is Date) dateFormat.format(fieldValue) else text,
                    onValueChange = { /* 只读 */ },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text(icolumn.placeholder) },
                    isError = validRes == false,
                    singleLine = true,
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(Icons.Default.DateRange, contentDescription = "选择日期")
                        }
                    }
                )
                
                if (showDatePicker) {
                    val datePickerState = rememberDatePickerState()
                    
                    DatePickerDialog(
                        onDismissRequest = { showDatePicker = false },
                        confirmButton = {
                            TextButton(onClick = {
                                datePickerState.selectedDateMillis?.let { millis ->
                                    val date = Date(millis)
                                    updateValue(date)
                                }
                                showDatePicker = false
                            }) {
                                Text("确定")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDatePicker = false }) {
                                Text("取消")
                            }
                        }
                    ) {
                        DatePicker(state = datePickerState)
                    }
                }
            }
        }

        RenderType.RADIO -> {
            // 直接从IColumn获取选项
            val options = icolumn.options ?: emptyList()
            
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(icolumn.title, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(4.dp))
                
                options.forEach { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    ) {
                        RadioButton(
                            selected = fieldValue == option.value,
                            onClick = { updateValue(option.value) },
                            enabled = icolumn.enabled
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(option.label)
                    }
                }
            }
        }

        RenderType.MONEY -> {
            val currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())
            
            OutlinedTextField(
                enabled = icolumn.enabled,
                label = label,
                value = text,
                onValueChange = { newValue ->
                    try {
                        if (newValue.isEmpty()) {
                            updateValue(null)
                        } else {
                            // 移除货币符号和逗号等格式化字符
                            val cleanValue = newValue.replace(Regex("[^\\d.]"), "")
                            val numValue = cleanValue.toDoubleOrNull()
                            if (numValue != null) {
                                updateValue(numValue)
                            }
                        }
                    } catch (e: Exception) {
                        // 转换失败时不更新
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(icolumn.placeholder) },
                isError = validRes == false,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                prefix = { Text("¥") }
            )
        }

        RenderType.PERCENT -> {
            OutlinedTextField(
                enabled = icolumn.enabled,
                label = label,
                value = text,
                onValueChange = { newValue ->
                    try {
                        if (newValue.isEmpty()) {
                            updateValue(null)
                        } else {
                            // 移除百分号等格式化字符
                            val cleanValue = newValue.replace(Regex("[^\\d.]"), "")
                            val numValue = cleanValue.toDoubleOrNull()
                            if (numValue != null) {
                                // 存储为小数形式 (50% -> 0.5)
                                updateValue(numValue / 100)
                            }
                        }
                    } catch (e: Exception) {
                        // 转换失败时不更新
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(icolumn.placeholder) },
                isError = validRes == false,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                suffix = { Text("%") }
            )
        }

        RenderType.MULTISELECT -> {
            var expanded by remember { mutableStateOf(false) }
            // 直接从IColumn获取选项
            val options = icolumn.options ?: emptyList()
            val selectedValues = (fieldValue as? List<*>) ?: emptyList<Any?>()
            
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(icolumn.title, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(4.dp))
                
                OutlinedTextField(
                    value = selectedValues.joinToString(", ") { value ->
                        options.find { it.value == value }?.label ?: value.toString()
                    },
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = { expanded = !expanded }) {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        }
                    },
                    placeholder = { Text(icolumn.placeholder) },
                    isError = validRes == false,
                    enabled = icolumn.enabled
                )
                
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    options.forEach { option ->
                        val isSelected = selectedValues.contains(option.value)
                        
                        DropdownMenuItem(
                            text = { Text(option.label) },
                            onClick = {
                                val newSelectedValues = if (isSelected) {
                                    selectedValues.filter { it != option.value }
                                } else {
                                    selectedValues + option.value
                                }
                                updateValue(newSelectedValues)
                            },
                            leadingIcon = {
                                Checkbox(
                                    checked = isSelected,
                                    onCheckedChange = null
                                )
                            }
                        )
                    }
                }
            }
        }

        RenderType.IMAGE -> {
            // 简单显示图片URL，实际应用中可能需要图片上传组件
            OutlinedTextField(
                enabled = icolumn.enabled,
                label = label,
                value = text,
                onValueChange = { updateValue(it) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("输入图片URL") },
                isError = validRes == false,
                singleLine = true
            )
        }

        RenderType.LINK -> {
            OutlinedTextField(
                enabled = icolumn.enabled,
                label = label,
                value = text,
                onValueChange = { updateValue(it) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("输入链接URL") },
                isError = validRes == false,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri)
            )
        }

        RenderType.CUSTOM -> {
            // 自定义渲染由外部提供
        }

        // 对于其他未实现的类型，使用默认文本输入
        else -> {
            OutlinedTextField(
                enabled = icolumn.enabled,
                label = label,
                value = text,
                onValueChange = { updateValue(it) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(icolumn.placeholder) },
                isError = validRes == false,
                singleLine = true
            )
        }
    }
}