package com.addzero.web.ui.hooks.form

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.web.ui.hooks.UseHook
import java.time.LocalDate
import java.time.LocalDateTime
import org.babyfish.jimmer.client.ApiIgnore

/**
 * 表单Hook
 */
class UseForm<T>(
    private val fields: List<FormField<*>>,
    private val modifier: Modifier = Modifier,
    private val onSubmit: (Map<String, Any?>) -> Unit = {},
    private val onCancel: () -> Unit = {}
) : UseHook<UseForm<T>>() {

    // 存储所有字段的值
    private var fieldValues by mutableStateOf(mutableMapOf<String, Any?>())
    // 存储字段验证状态
    private var fieldErrors by mutableStateOf(mutableMapOf<String, String?>())

    init {
        // 初始化默认值
        fields.forEach { field ->
            fieldValues[field.name] = field.defaultValue
        }
    }

    // 验证表单
    private fun validate(): Boolean {

        return true
    }

    // 提交表单
    private fun submit() {
        if (validate()) {
            onSubmit(fieldValues.toMap())
        }
    }

    // 重置表单
    fun reset() {
        fields.forEach { field ->
            fieldValues[field.name] = field.defaultValue
        }
        fieldErrors.clear()
    }

    @Composable
    private fun renderField(field: FormField<*>) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (field.required) "* ${field.title}" else field.title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (field.required) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                )
            }

            Box(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                @Suppress("UNCHECKED_CAST")
                when (field.componentType) {
                    ComponentType.TEXT -> renderTextField(field as FormField<String>)
                    ComponentType.TEXTAREA -> renderTextArea(field as FormField<String>)
                    ComponentType.DATE -> renderDatePicker(field as FormField<LocalDate>)
                    ComponentType.DATETIME -> renderDateTimePicker(field as FormField<LocalDateTime>)
                    ComponentType.UPLOAD -> renderUpload(field)
                    ComponentType.TREE_SELECT -> renderTreeSelect(field)
                    ComponentType.CASCADE_SELECT -> renderCascadeSelect(field)
                    ComponentType.AUTO_COMPLETE -> renderAutoComplete(field as FormField<String>)
                }
            }

            // 显示错误信息
            fieldErrors[field.name]?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }

    @Composable
    private fun renderTextField(field: FormField<String>) {
        OutlinedTextField(
            value = fieldValues[field.name] as? String ?: "",
            onValueChange = { fieldValues[field.name] = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { field.placeholder?.let { Text(it) } },
            isError = fieldErrors[field.name] != null,
            singleLine = true
        )
    }

    @Composable
    private fun renderTextArea(field: FormField<String>) {
        OutlinedTextField(
            value = fieldValues[field.name] as? String ?: "",
            onValueChange = { fieldValues[field.name] = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { field.placeholder?.let { Text(it) } },
            isError = fieldErrors[field.name] != null,
            maxLines = 5
        )
    }

    @Composable
    private fun renderDatePicker(field: FormField<LocalDate>) {
        // 实现日期选择器
        OutlinedTextField(
            value = (fieldValues[field.name] as? LocalDate)?.toString() ?: "",
            onValueChange = { fieldValues[field.name] = LocalDate.parse(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { field.placeholder?.let { Text(it) } },
            isError = fieldErrors[field.name] != null
        )
    }

    @Composable
    private fun renderDateTimePicker(field: FormField<LocalDateTime>) {
        // 实现日期时间选择器
        OutlinedTextField(
            value = (fieldValues[field.name] as? LocalDateTime)?.toString() ?: "",
            onValueChange = { fieldValues[field.name] = LocalDateTime.parse(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { field.placeholder?.let { Text(it) } },
            isError = fieldErrors[field.name] != null
        )
    }

    @Composable
    private fun renderUpload(field: FormField<*>) {
        // 实现文件上传组件
        Button(
            onClick = { /* 实现文件选择逻辑 */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(field.placeholder ?: "选择文件")
        }
    }

    @Composable
    private fun renderTreeSelect(field: FormField<*>) {
        // 实现树形选择器
        // TODO:
    }

    @Composable
    private fun renderCascadeSelect(field: FormField<*>) {
        // 实现级联选择器
        // TODO:
    }

    @Composable
    private fun renderAutoComplete(field: FormField<String>) {
        // 实现自动完成输入框
        OutlinedTextField(
            value = fieldValues[field.name] as? String ?: "",
            onValueChange = { fieldValues[field.name] = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { field.placeholder?.let { Text(it) } },
            isError = fieldErrors[field.name] != null
        )
    }

    @Composable
    @ApiIgnore
    override fun show(state: UseForm<T>) {
        Column(
            modifier = modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 渲染表单字段
            fields.forEach { field ->
                renderField(field)
            }

            // 操作按钮
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onCancel) {
                    Text("取消")
                }
                Button(
                    onClick = { state.submit() },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("提交")
                }
            }
        }
    }
}
