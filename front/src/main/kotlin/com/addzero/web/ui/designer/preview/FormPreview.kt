package com.addzero.web.ui.designer.preview

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.web.infra.jackson.toJson
import com.addzero.web.ui.designer.models.FormConfig
import com.addzero.web.ui.designer.models.FormField
import com.addzero.web.ui.designer.models.toFieldMetadata
import com.addzero.web.ui.hooks.table.entity.OptionItem
import com.addzero.web.ui.hooks.table.entity.RenderType
import com.addzero.web.ui.lowcode.forms.EditForm
import com.addzero.web.ui.lowcode.metadata.FieldMetadata
import com.alibaba.fastjson2.JSONObject

/**
 * 表单预览
 * 预览表单的实际效果
 */
@Composable
fun FormPreview(
    formConfig: FormConfig
) {
    // 创建一个动态数据模型来承载表单数据
    val formData = remember { mutableStateMapOf<String, Any?>() }
    
    // 初始化默认值
    formConfig.fields.forEach { field ->
        if (field.defaultValue.isNotEmpty()) {
            formData[field.name] = field.defaultValue
        }
    }
    
    // 数据类包装器
    class FormDataWrapper(val data: MutableMap<String, Any?> = mutableMapOf()) {
        fun toJson(): String {
            val toJson = data.toJson()
            return toJson
        }
        
        private fun formatValueForJson(value: Any?): String {
            return when (value) {
                null -> "null"
                is String -> "\"$value\""
                is Boolean, is Number -> "$value"
                is List<*> -> value.joinToString(prefix = "[", postfix = "]") { formatValueForJson(it) }
                else -> "\"${value}\""
            }
        }
    }
    
    var wrapper by remember { mutableStateOf(FormDataWrapper(formData)) }
    
    Surface(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // 表单标题
            Text(
                formConfig.title,
                style = MaterialTheme.typography.headlineMedium
            )
            
            if (formConfig.description.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    formConfig.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // 将FormField转换为FieldMetadata
            val fields = formConfig.fields.map { field ->
                field.toFieldMetadata<FormDataWrapper>(
                    getValue = { wrapper.data[field.name] },
                    setValue = { obj, value ->
                        val newData = obj.data.toMutableMap()
                        newData[field.name] = value
                        FormDataWrapper(newData.toMutableMap())
                    }
                )
            }
            
            // 使用EditForm来展示表单
            EditForm(
                data = wrapper,
                fields = fields,
                onSave = { newData -> 
                    wrapper = newData as FormDataWrapper
                },
                onCancel = { },
                title = "",
                submitText = "提交",
                cancelText = "重置",
                columnCount = formConfig.columnCount
            )
            
            // 显示当前表单数据
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                "表单数据",
                style = MaterialTheme.typography.titleMedium
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Surface(
                modifier = Modifier.fillMaxWidth(),
                tonalElevation = 2.dp,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    wrapper.toJson(),
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
            }
        }
    }
} 