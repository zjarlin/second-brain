package com.addzero.web.ui.hooks.table.entity

/**
 * 表示下拉选择、单选按钮等组件的选项项
 *
 * @param label 显示给用户的标签
 * @param value 选项的实际值
 */
data class OptionItem(
    val label: String,
    val value: Any
) 