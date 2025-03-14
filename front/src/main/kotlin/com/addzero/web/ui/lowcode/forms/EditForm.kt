package com.addzero.web.ui.lowcode.forms

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.web.ui.lowcode.core.DynamicForm
import com.addzero.web.ui.lowcode.core.RenderField
import com.addzero.web.ui.lowcode.core.validateForm
import com.addzero.web.ui.lowcode.metadata.FieldMetadata
import com.addzero.web.ui.lowcode.renderers.DefaultFieldRenderer

/**
 * 编辑表单
 * 用于新增和修改数据
 */
@Composable
fun <E : Any> EditForm(
    data: E,
    fields: List<FieldMetadata<E>>,
    onSave: (E) -> Unit,
    onCancel: () -> Unit = {},
    title: String = "编辑",
    submitText: String = "保存",
    cancelText: String = "取消",
    columnCount: Int = 1,
    renderField: RenderField<E> = { field, value, onChange, error -> 
        DefaultFieldRenderer(field, value, onChange, error)
    }
) {
    var formData by remember { mutableStateOf(data) }
    var errors by remember { mutableStateOf(mapOf<String, String>()) }
    
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 表单标题
        Text(text = title, style = androidx.compose.material3.MaterialTheme.typography.headlineSmall)
        
        // 表单内容
        val formFields = fields.filter { it.showInForm }
        
        // 使用Grid布局
        val rows = (formFields.size + columnCount - 1) / columnCount
        for (row in 0 until rows) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                for (col in 0 until columnCount) {
                    val index = row * columnCount + col
                    if (index < formFields.size) {
                        Box(modifier = Modifier.weight(1f)) {
                            DynamicForm(
                                data = formData,
                                fields = listOf(formFields[index]),
                                renderField = renderField,
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
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text(cancelText)
            }
            
            Button(
                onClick = {
                    // 验证表单
                    val validationErrors = validateForm(formData, formFields)
                    if (validationErrors.isEmpty()) {
                        onSave(formData)
                    } else {
                        errors = validationErrors
                    }
                }
            ) {
                Text(submitText)
            }
        }
    }
} 