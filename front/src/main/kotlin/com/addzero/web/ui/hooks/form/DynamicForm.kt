package com.addzero.web.ui.hooks.form

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.web.ui.hooks.table.entity.AddColumn

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
            // 当表单数据变更时，自动验证
            validate()
            onDataChange(newData)
        },
        validationErrors = validationErrors,
        columnCount = columnCount,
        customRenders = customRenders
    )
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
                                val updatedData = column.setFun(data, column.fieldName, newValue)
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
