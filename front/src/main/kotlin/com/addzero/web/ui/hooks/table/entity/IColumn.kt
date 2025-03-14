package com.addzero.web.ui.hooks.table.entity

import androidx.compose.runtime.Composable
import com.addzero.common.kt_util.containsAnyIgnoreCase
import com.addzero.web.ui.hooks.table.entity.RenderType.*

/**
 * 表格列定义接口
 * @param E 数据实体类型
 */
interface IColumn<E : Any> {
    /** 字段标题 */
    var title: String

    /** 字段名称 */
    var fieldName: String

    /** 渲染类型 */
    var renderType: RenderType
        get() = when {
            fieldName.containsAnyIgnoreCase("url,file") -> FILE
            fieldName.containsAnyIgnoreCase("image") -> IMAGE
            fieldName.containsAnyIgnoreCase("date") && !fieldName.containsAnyIgnoreCase("time") -> DATE
            fieldName.containsAnyIgnoreCase("time", "datetime") -> DATETIME
            fieldName.contains("description") || fieldName.contains("content") || fieldName.contains("text") -> TEXTAREA
            else -> CUSTOM
        }
        set(value) = value.run {}

    /** 是否必填 */
    var required: Boolean
        get() = false
        set(value) {
            value.run { }
        }

    /** 占位文本 */
    var placeholder: String
        get() = "请输入${this.title}"
        set(value) {
            value.run { }
        }

    /** 验证错误提示信息 */
    val errorMessage: String
        get() = "${this.title}的值非法"

    /** 自定义列表渲染函数 */
    var customRender: @Composable ((E) -> Unit)

    /** 验证函数 */
    val validator: (E?) -> Boolean

    /** 获取列数据的函数 */
    val getFun: (E) -> Any?

    /** 设置值的函数 */
    val setFun: (E, IColumn<E>, Any?) -> E

    /** 是否可以编辑 */
    val enabled: Boolean
        get() = renderType != CUSTOM


    /** 是否可以编辑 */
    val showInSearch: Boolean
        get() = false

    /**
     * 选项列表，用于下拉选择、单选按钮等
     * 返回选项列表，可以是静态列表或动态生成的列表
     */
    var options: List<OptionItem>?
        get() = null
        set(value) {
            value?.run { }
        }

}