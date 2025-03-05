package com.addzero.web.ui.hooks.form

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.web.ui.hooks.table.entity.AddColumn
import com.addzero.web.ui.hooks.table.entity.RenderType

/**
 * 表单项组件
 * @param column 表单列配置
 * @param data 表单数据
 * @param onValueChange 值变更回调
 * @param error 错误信息
 * @param customRender 自定义渲染函数
 */
@Composable
fun <E : Any> FormItem(
    column: AddColumn<E>,
    data: E,
    onValueChange: (Any?) -> Unit,
    error: String? = null,
    customRender: (@Composable (E, AddColumn<E>, (Any?) -> Unit) -> Unit)? = null
) {
    // 如果有自定义渲染函数，则使用自定义渲染
    if (customRender != null) {
        customRender(data, column, onValueChange)
        return
    }

    // 获取当前值
    val currentValue = column.getFun(data)

    // 确定渲染类型
    val renderType = inferRenderType(column, data)

    Column {
        // 标题和必填标记
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = column.title)
            if (column.required) {
                Text(text = " *", color = MaterialTheme.colorScheme.error)
            }
        }

        // 根据渲染类型渲染不同的输入控件
        when (renderType) {
            RenderType.TEXT -> TextInput(
                value = currentValue?.toString() ?: "",
                onValueChange = onValueChange,
                column = column,
                error = error
            )

            RenderType.TEXTAREA -> TextAreaInput(
                value = currentValue?.toString() ?: "",
                onValueChange = onValueChange,
                column = column,
                error = error
            )

            RenderType.DATE -> DateInput(
                value = currentValue,
                onValueChange = onValueChange,
                column = column,
                error = error
            )

            RenderType.DATETIME -> DateTimeInput(
                value = currentValue,
                onValueChange = onValueChange,
                column = column,
                error = error
            )

            RenderType.IMAGE -> ImageInput(
                value = currentValue?.toString() ?: "",
                onValueChange = onValueChange,
                column = column,
                error = error
            )

            RenderType.NUMBER -> NumberInput(
                value = currentValue?.toString() ?: "",
                onValueChange = onValueChange,
                column = column,
                error = error
            )

            RenderType.SELECT -> SelectInput(
                value = currentValue?.toString() ?: "",
                onValueChange = onValueChange,
                column = column,
                error = error
            )

            RenderType.TREE -> TreeInput(
                value = currentValue?.toString() ?: "",
                onValueChange = onValueChange,
                column = column,
                error = error
            )

            RenderType.AUTO_COMPLETE -> AutoCompleteInput(
                value = currentValue?.toString() ?: "",
                onValueChange = onValueChange,
                column = column,
                error = error
            )

            RenderType.COMPUTED -> ComputedInput(
                value = currentValue?.toString() ?: "",
                onValueChange = onValueChange,
                column = column,
                error = error
            )

            else -> TextInput(
                value = currentValue?.toString() ?: "",
                onValueChange = onValueChange,
                column = column,
                error = error
            )
        }
    }
}

@Composable
private fun <E : Any> TextInput(
    value: String,
    onValueChange: (Any?) -> Unit,
    column: AddColumn<E>,
    error: String?
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(column.placeholder) },
        isError = error != null,
        singleLine = true
    )
}

@Composable
private fun <E : Any> TextAreaInput(
    value: String,
    onValueChange: (Any?) -> Unit,
    column: AddColumn<E>,
    error: String?
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        modifier = Modifier.fillMaxWidth().height(120.dp),
        placeholder = { Text(column.placeholder) },
        isError = error != null,
        maxLines = 5
    )
}

@Composable
private fun <E : Any> DateInput(
    value: Any?,
    onValueChange: (Any?) -> Unit,
    column: AddColumn<E>,
    error: String?
) {
    OutlinedTextField(
        value = formatDate(value),
        onValueChange = { onValueChange(parseDate(it)) },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(column.placeholder) },
        isError = error != null,
        singleLine = true,
        trailingIcon = {
            IconButton(onClick = { /* 显示日期选择器 */ }) {
                Icon(Icons.Default.DateRange, contentDescription = "选择日期")
            }
        }
    )
}

@Composable
private fun <E : Any> DateTimeInput(
    value: Any?,
    onValueChange: (Any?) -> Unit,
    column: AddColumn<E>,
    error: String?
) {
    OutlinedTextField(
        value = formatDateTime(value),
        onValueChange = { onValueChange(parseDateTime(it)) },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(column.placeholder) },
        isError = error != null,
        singleLine = true,
        trailingIcon = {
            IconButton(onClick = { /* 显示日期时间选择器 */ }) {
                Icon(Icons.Default.Schedule, contentDescription = "选择日期和时间")
            }
        }
    )
}

@Composable
private fun <E : Any> ImageInput(
    value: String,
    onValueChange: (Any?) -> Unit,
    column: AddColumn<E>,
    error: String?
) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = { onValueChange(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(column.placeholder) },
            isError = error != null,
            singleLine = true,
            readOnly = true
        )
        Button(
            onClick = { /* 显示文件选择器 */ },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("上传文件")
        }
    }
}

@Composable
private fun <E : Any> NumberInput(
    value: String,
    onValueChange: (Any?) -> Unit,
    column: AddColumn<E>,
    error: String?
) {
    OutlinedTextField(
        value = value,
        onValueChange = {
            val number = it.toDoubleOrNull()
            if (number != null || it.isEmpty()) {
                onValueChange(number)
            }
        },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(column.placeholder) },
        isError = error != null,
        singleLine = true
    )
}

@Composable
private fun <E : Any> SelectInput(
    value: String,
    onValueChange: (Any?) -> Unit,
    column: AddColumn<E>,
    error: String?
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        OutlinedTextField(
            value = value,
            onValueChange = { onValueChange(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(column.placeholder) },
            isError = error != null,
            singleLine = true,
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "展开选项")
                }
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            column.options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.second) },
                    onClick = {
                        onValueChange(option.first)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun <E : Any> TreeInput(
    value: String,
    onValueChange: (Any?) -> Unit,
    column: AddColumn<E>,
    error: String?
) {
    // 简化为基本的下拉选择
    SelectInput(value, onValueChange, column, error)
}

@Composable
private fun <E : Any> AutoCompleteInput(
    value: String,
    onValueChange: (Any?) -> Unit,
    column: AddColumn<E>,
    error: String?
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        OutlinedTextField(
            value = value,
            onValueChange = {
                onValueChange(it)
                expanded = true
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(column.placeholder) },
            isError = error != null,
            singleLine = true,
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "展开选项")
                }
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            // 过滤选项
            val filteredOptions = column.options.filter {
                it.second.contains(value, ignoreCase = true)
            }

            filteredOptions.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.second) },
                    onClick = {
                        onValueChange(option.first)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun <E : Any> ComputedInput(
    value: String,
    onValueChange: (Any?) -> Unit,
    column: AddColumn<E>,
    error: String?
) {
    // 简化为基本的下拉选择
    SelectInput(value, onValueChange, column, error)
}