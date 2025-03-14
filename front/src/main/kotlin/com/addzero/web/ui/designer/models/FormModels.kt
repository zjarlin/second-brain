package com.addzero.web.ui.designer.models

import com.addzero.web.ui.hooks.table.entity.OptionItem
import com.addzero.web.ui.hooks.table.entity.RenderType
import com.addzero.web.ui.lowcode.metadata.FieldMetadata
import com.alibaba.fastjson2.JSON
import com.alibaba.fastjson2.JSONObject

/**
 * 表单配置
 */
data class FormConfig(
    val id: String = "form_${System.currentTimeMillis()}",
    val name: String = "新建表单",
    val title: String = "表单标题",
    val description: String = "",
    val columnCount: Int = 1,
    val fields: List<FormField> = emptyList(),
    val properties: Map<String, String> = emptyMap()
) {
    // 添加一个方法来转换为JSON字符串
    fun toJson(): String {
        return JSON.toJSONString(this)
    }
    
    companion object {
        // 从JSON字符串解析
        fun fromJson(jsonString: String): FormConfig {
            return try {
                JSON.parseObject(jsonString, FormConfig::class.java)
            } catch (e: Exception) {
                // 发生错误时返回默认配置
                FormConfig()
            }
        }
    }
}

/**
 * 表单字段
 */
data class FormField(
    val id: String = "field_${System.currentTimeMillis()}",
    val name: String,
    val label: String,
    val type: String, // "text", "number", "select", "checkbox", "date", etc.
    val placeholder: String = "",
    val defaultValue: String = "",
    val required: Boolean = false,
    val disabled: Boolean = false,
    val hidden: Boolean = false,
    val options: List<FieldOption> = emptyList(),
    val validators: List<FieldValidator> = emptyList(),
    val properties: Map<String, String> = emptyMap()
)

/**
 * 字段选项
 */
data class FieldOption(
    val value: String,
    val label: String
)

/**
 * 字段验证器
 */
data class FieldValidator(
    val type: String, // "required", "regex", "min", "max", etc.
    val message: String,
    val params: Map<String, String> = emptyMap()
)

/**
 * 将FormField转换为FieldMetadata
 */
fun <E : Any> FormField.toFieldMetadata(
    getValue: (E) -> Any?,
    setValue: (E, Any?) -> E
): FieldMetadata<E> {
    val renderType = when (this.type) {
        "text" -> RenderType.TEXT
        "textarea" -> RenderType.TEXTAREA
        "number" -> RenderType.NUMBER
        "select" -> RenderType.SELECT
        "checkbox" -> RenderType.CHECKBOX
        "switch" -> RenderType.SWITCH
        "date" -> RenderType.DATE
        "radio" -> RenderType.RADIO
        "multiselect" -> RenderType.MULTISELECT
        else -> RenderType.TEXT
    }
    
    return FieldMetadata(
        name = this.name,
        title = this.label,
        renderType = renderType,
        getValue = getValue,
        setValue = setValue,
        required = this.required,
        placeholder = this.placeholder,
        enabled = !this.disabled,
        visible = !this.hidden,
        options = this.options.map { OptionItem(it.value, it.label) },
        showInSearch = true,
        showInTable = true,
        showInForm = true
    )
} 