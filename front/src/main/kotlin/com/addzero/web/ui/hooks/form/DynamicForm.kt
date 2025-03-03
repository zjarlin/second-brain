package com.addzero.web.ui.hooks.form

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * 动态表单组件
 * @param E 数据实体类型
 * @param columns 表单列配置
 * @param data 表单数据
 * @param onDataChange 数据变更回调
 * @param columnCount 表单列数，默认为2
 * @param customRenders 自定义渲染函数映射
 */
@Composable
fun <E : Any> DynamicFormComponent(
    columns: List<AddColumn<E>>,
    data: E,
    onDataChange: (E) -> Unit,
    columnCount: Int = 2,
    customRenders: Map<String, @Composable (E, AddColumn<E>, (Any?) -> Unit) -> Unit> = emptyMap()
) {
    // 表单数据的可变状态
    var formData by remember { mutableStateOf(data) }

    // 表单验证错误信息
    var validationErrors by remember { mutableStateOf(mapOf<String, String>()) }

    // 表单是否有效
    val isValid = validationErrors.isEmpty() && columns.all { column ->
        column.validator(formData)
    }

    // 验证表单
    fun validate(): Boolean {
        val errors = mutableMapOf<String, String>()

        columns.forEach { column ->
            if (!column.validator(formData)) {
                errors[column.title] = column.errorMessage ?: "${column.title}验证失败"
            }
        }

        validationErrors = errors
        return errors.isEmpty()
    }

    // 提交表单
    fun submit() {
        if (isValid) {
            onDataChange(formData)
        }
    }

    // 重置表单
    fun reset() {
        formData = data
        validationErrors = emptyMap()
    }

    // 渲染表单
    DynamicForm(
        columns = columns,
        data = formData,
        onDataChange = { newData ->
            formData = newData
        },
        validationErrors = validationErrors,
        columnCount = columnCount,
        customRenders = customRenders
    )
}

// 使用AddColumn类作为表单列配置

/**
 * 根据列配置和数据推断渲染类型
 */
private fun <E : Any> inferRenderType(column: AddColumn<E>, data: E): RenderType {
    // 如果明确指定了渲染类型，则使用指定的类型
    if (column.renderTypeOverride != null) {
        return column.renderTypeOverride
    }

    // 获取渲染类型
    val renderType = column.getRenderType(data)

    // 如果已经有明确的渲染类型，则直接返回
    if (renderType != RenderType.CUSTOM) {
        return renderType
    }

    // 根据字段名称推断
    val fieldName = column.title.lowercase()
    return when {
        fieldName.contains("url") || fieldName.contains("file") || fieldName.contains("image") -> RenderType.IMAGE
        fieldName.contains("date") && !fieldName.contains("time") -> RenderType.DATE
        fieldName.contains("time") || fieldName.contains("datetime") -> RenderType.DATETIME
        fieldName.contains("description") || fieldName.contains("content") || fieldName.contains("text") -> RenderType.TEXTAREA
        else -> RenderType.TEXT
    }
}

// 日期格式化工具函数
private fun formatDate(value: Any?): String {
    return when (value) {
        is LocalDate -> value.format(DateTimeFormatter.ISO_LOCAL_DATE)
        is LocalDateTime -> value.toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE)
        is java.util.Date -> java.time.Instant.ofEpochMilli(value.time)
            .atZone(java.time.ZoneId.systemDefault())
            .toLocalDate()
            .format(DateTimeFormatter.ISO_LOCAL_DATE)

        else -> value?.toString() ?: ""
    }
}

// 日期时间格式化工具函数
private fun formatDateTime(value: Any?): String {
    return when (value) {
        is LocalDateTime -> value.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        is LocalDate -> value.atStartOfDay().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        is java.util.Date -> java.time.Instant.ofEpochMilli(value.time)
            .atZone(java.time.ZoneId.systemDefault())
            .toLocalDateTime()
            .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)

        else -> value?.toString() ?: ""
    }
}

// 解析日期字符串
private fun parseDate(dateStr: String): LocalDate? {
    return try {
        LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE)
    } catch (e: Exception) {
        null
    }
}

// 解析日期时间字符串
private fun parseDateTime(dateTimeStr: String): LocalDateTime? {
    return try {
        LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    } catch (e: Exception) {
        null
    }
}

/**
 * 动态表单组件
 * @param columns 表单列配置
 * @param data 表单数据
 * @param onDataChange 数据变更回调
 * @param validationErrors 验证错误信息
 * @param columnCount 表单列数，默认为2
 * @param customRenders 自定义渲染函数映射
 */
@Composable
fun <E : Any> DynamicForm(
    columns: List<AddColumn<E>>,
    data: E,
    onDataChange: (E) -> Unit,
    validationErrors: Map<String, String> = emptyMap(),
    columnCount: Int = 2,
    customRenders: Map<String, @Composable (E, AddColumn<E>, (Any?) -> Unit) -> Unit> = emptyMap()
) {
    // 计算每行的列数和总行数
    val itemsPerRow = columnCount
    val rowCount = (columns.size + itemsPerRow - 1) / itemsPerRow
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // 按行渲染表单项
        for (rowIndex in 0 until rowCount) {
            Row(modifier = Modifier.fillMaxWidth()) {
                // 计算当前行的起始和结束索引
                val startIndex = rowIndex * itemsPerRow
                val endIndex = minOf(startIndex + itemsPerRow, columns.size)

                // 渲染当前行的表单项
                for (colIndex in startIndex until endIndex) {
                    val column = columns[colIndex]
                    Box(modifier = Modifier.weight(1f).padding(8.dp)) {
                        FormItem(
                            column = column,
                            data = data,
                            onValueChange = { newValue ->
                                val updatedData = column.setFun(data, newValue)
                                onDataChange(updatedData)
                            },
                            error = validationErrors[column.title],
                            customRender = customRenders[column.title]
                        )
                    }
                }

                // 如果当前行的列数不足，添加空白占位
                if (endIndex - startIndex < itemsPerRow) {
                    for (i in 0 until (itemsPerRow - (endIndex - startIndex))) {
                        Box(modifier = Modifier.weight(1f).padding(8.dp))
                    }
                }
            }
        }
    }
}

/**
 * 表单项组件
 * @param column 表单列配置
 * @param data 表单数据
 * @param onValueChange 值变更回调
 * @param error 错误信息
 * @param customRender 自定义渲染函数
 */
@Composable
private fun <E : Any> FormItem(
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
            RenderType.TEXT -> {
                OutlinedTextField(
                    value = currentValue?.toString() ?: "",
                    onValueChange = { onValueChange(it) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text(column.placeholder) },
                    isError = error != null,
                    singleLine = true,
                    readOnly = false  // 确保字段可编辑
                )
            }

            RenderType.TEXTAREA -> {
                OutlinedTextField(
                    value = currentValue?.toString() ?: "",
                    onValueChange = { onValueChange(it) },
                    modifier = Modifier.fillMaxWidth().height(120.dp),
                    placeholder = { Text(column.placeholder) },
                    isError = error != null,
                    maxLines = 5,
                    readOnly = false  // 确保字段可编辑
                )
            }

            RenderType.DATE -> {
                // 日期选择器实现
                OutlinedTextField(
                    value = formatDate(currentValue),
                    onValueChange = { onValueChange(parseDate(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text(column.placeholder) },
                    isError = error != null,
                    singleLine = true,
                    readOnly = false,  // 确保字段可编辑
                    // 这里可以添加日期选择器的图标和点击事件
                    trailingIcon = {
                        IconButton(onClick = { /* 显示日期选择器 */ }) {
                            Icon(Icons.Default.DateRange, contentDescription = "选择日期")
                        }
                    }
                )
            }

            RenderType.DATETIME -> {
                // 日期时间选择器实现
                OutlinedTextField(
                    value = formatDateTime(currentValue),
                    onValueChange = { onValueChange(parseDateTime(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text(column.placeholder) },
                    isError = error != null,
                    singleLine = true,
                    readOnly = false,  // 确保字段可编辑
                    // 这里可以添加日期时间选择器的图标和点击事件
                    trailingIcon = {
                        IconButton(onClick = { /* 显示日期时间选择器 */ }) {
                            Icon(Icons.Default.Schedule, contentDescription = "选择日期和时间")
                        }
                    }
                )
            }

            RenderType.IMAGE -> {
                // 文件上传组件实现
                Column {
                    OutlinedTextField(
                        value = currentValue?.toString() ?: "",
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

            RenderType.NUMBER -> {
                // 数字输入框实现
                OutlinedTextField(
                    value = currentValue?.toString() ?: "",
                    onValueChange = {
                        val number = it.toDoubleOrNull()
                        if (number != null || it.isEmpty()) {
                            onValueChange(number)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text(column.placeholder) },
                    isError = error != null,
                    singleLine = true,
                    readOnly = false  // 确保字段可编辑
                )
            }

            RenderType.SELECT -> {
                // 下拉选择框实现
                var expanded by remember { mutableStateOf(false) }
                Box {
                    OutlinedTextField(
                        value = currentValue?.toString() ?: "",
                        onValueChange = { onValueChange(it) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text(column.placeholder) },
                        isError = error != null,
                        singleLine = true,
                        readOnly = false,  // 确保字段可编辑
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

            RenderType.TREE -> {
                // 树形选择器实现
                // 简化为基本的下拉选择
                var expanded by remember { mutableStateOf(false) }
                Box {
                    OutlinedTextField(
                        value = currentValue?.toString() ?: "",
                        onValueChange = { onValueChange(it) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text(column.placeholder) },
                        isError = error != null,
                        singleLine = true,
                        readOnly = false,  // 确保字段可编辑
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

            RenderType.AUTO_COMPLETE -> {
                // 自动完成组件实现
                var expanded by remember { mutableStateOf(false) }
                Box {
                    OutlinedTextField(
                        value = currentValue?.toString() ?: "",
                        onValueChange = {
                            onValueChange(it)
                            expanded = true
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text(column.placeholder) },
                        isError = error != null,
                        singleLine = true,
                        readOnly = false,  // 确保字段可编辑
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
                        val filterText = currentValue?.toString() ?: ""
                        val filteredOptions = column.options.filter {
                            it.second.contains(filterText, ignoreCase = true)
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

            RenderType.COMPUTED -> {
                // 级联选择器实现
                // 简化为基本的下拉选择
                var expanded by remember { mutableStateOf(false) }
                Box {
                    OutlinedTextField(
                        value = currentValue?.toString() ?: "",
                        onValueChange = { onValueChange(it) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text(column.placeholder) },
                        isError = error != null,
                        singleLine = true,
                        readOnly = false,  // 确保字段可编辑
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

            RenderType.MULTISELECT -> {
                // 多选框实现
                // 简化为基本的下拉选择
                var expanded by remember { mutableStateOf(false) }
                Box {
                    OutlinedTextField(
                        value = currentValue?.toString() ?: "",
                        onValueChange = { onValueChange(it) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text(column.placeholder) },
                        isError = error != null,
                        singleLine = true,
                        readOnly = false,  // 确保字段可编辑
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

            else -> {
                // 默认文本输入框
                OutlinedTextField(
                    value = currentValue?.toString() ?: "",
                    onValueChange = { onValueChange(it) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text(column.placeholder) },
                    isError = error != null,
                    singleLine = true,
                    readOnly = false  // 确保字段可编辑
                )
            }
        }
    }
}
