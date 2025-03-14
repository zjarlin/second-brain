package com.addzero.web.ui.lowcode.metadata

import androidx.compose.runtime.Composable
import com.addzero.web.ui.hooks.table.entity.OptionItem
import com.addzero.web.ui.hooks.table.entity.RenderType

/**
 * 字段元数据
 * 描述表单字段的所有属性
 */
data class FieldMetadata<E : Any>(
    // 基本属性
    val name: String,                                // 字段名称
    val title: String,                               // 字段标题
    val renderType: RenderType = RenderType.TEXT,    // 渲染类型
    
    // 数据访问
    val getValue: (E) -> Any?,                       // 获取值函数
    val setValue: (E, Any?) -> E,                    // 设置值函数
    
    // 验证和约束
    val required: Boolean = false,                   // 是否必填
    val validator: (Any?) -> Boolean = { true },     // 验证函数
    val errorMessage: String? = null,                // 错误消息
    
    // UI相关
    val placeholder: String = "",                    // 占位文本
    val enabled: Boolean = true,                     // 是否可编辑
    val visible: Boolean = true,                     // 是否可见
    val customRender: (@Composable (Any?) -> Unit)? = null, // 自定义渲染函数
    
    // 特殊属性
    val options: List<OptionItem>? = null,           // 选项列表（用于下拉框等）
    val showInSearch: Boolean = false,               // 是否在搜索表单中显示
    val showInTable: Boolean = true,                 // 是否在表格中显示
    val showInForm: Boolean = true,                  // 是否在编辑表单中显示
    
    // 扩展属性
    val properties: Map<String, Any> = emptyMap()    // 扩展属性
) 