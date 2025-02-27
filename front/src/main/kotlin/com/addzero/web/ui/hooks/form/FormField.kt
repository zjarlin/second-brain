package com.addzero.web.ui.hooks.form

import androidx.compose.runtime.Composable

/**
 * 表单字段元数据定义
 */
data class FormField<T>(
    /** 字段名称 */
    val name: String,
    /** 字段标题 */
    val title: String,
    /** 组件类型 */
    val componentType: ComponentType,
    /** 默认值 */
    val defaultValue: T? = null,
    /** 是否必填 */
    val required: Boolean = false,
    /** 占位符文本 */
    val placeholder: String? = null,
    /** 校验规则 */
    val validator: ((T?) -> Boolean)? = null,
    /** 校验失败提示文本 */
    val errorMessage: String? = null,
    /** 子节点字段名（用于树形选择器） */
    val childrenField: String? = null,
    /** 级联选项（用于级联选择器） */
    val cascadeOptions: List<T>? = null,
    /** 依赖的字段名（用于级联选择器） */
    val dependsOn: String? = null,
    /** 自定义渲染函数 */
    val customRender: (@Composable (value: T?, onValueChange: (T?) -> Unit, options: List<T>?, context: Map<String, Any?>) -> Unit)? = null
)