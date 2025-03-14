package com.addzero.web.ui.lowcode.core

import androidx.compose.runtime.*
import com.addzero.web.ui.lowcode.metadata.FieldMetadata

/**
 * 基础动态表单
 * 不包含任何样式，只负责处理表单逻辑
 */
@Composable
fun <E : Any> DynamicForm(
    // 表单数据
    data: E,
    // 字段元数据列表
    fields: List<FieldMetadata<E>>,
    // 表单项渲染函数
    renderField: RenderField<E>,
    // 值变更回调
    onDataChange: (E) -> Unit = {}
) {
    // 表单状态
    var formData by remember { mutableStateOf(data) }
    var errors by remember { mutableStateOf(mapOf<String, String>()) }
    
    // 当外部数据变化时更新表单数据
    LaunchedEffect(data) {
        formData = data
    }
    
    // 渲染表单项
    fields.forEach { field ->
        if (field.visible) {
            val value = field.getValue(formData)
            val error = errors[field.name]
            
            // 渲染表单项
            renderField(
                field,
                value,
                { newValue ->
                    // 更新表单数据
                    val newData = field.setValue(formData, newValue)
                    formData = newData
                    onDataChange(newData)
                    
                    // 验证
                    val isValid = field.validator(newValue)
                    if (!isValid) {
                        errors = errors + (field.name to (field.errorMessage ?: "${field.title}的值非法"))
                    } else {
                        errors = errors - field.name
                    }
                },
                error
            )
        }
    }
}

/**
 * 验证表单
 */
fun <E : Any> validateForm(data: E, fields: List<FieldMetadata<E>>): Map<String, String> {
    val errors = mutableMapOf<String, String>()
    
    fields.forEach { field ->
        val value = field.getValue(data)
        
        // 必填验证
        if (field.required && (value == null || value.toString().isBlank())) {
            errors[field.name] = "${field.title}不能为空"
            return@forEach
        }
        
        // 自定义验证
        if (!field.validator(value)) {
            errors[field.name] = field.errorMessage ?: "${field.title}的值非法"
        }
    }
    
    return errors
} 