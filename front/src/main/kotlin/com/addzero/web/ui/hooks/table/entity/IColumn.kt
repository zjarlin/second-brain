package com.addzero.web.ui.hooks.table.entity

import androidx.compose.runtime.Composable
import com.addzero.common.kt_util.containsAnyIgnoreCase
import com.addzero.common.kt_util.isNotEmpty
import com.addzero.web.ui.hooks.table.entity.RenderType.CUSTOM
import com.addzero.web.ui.hooks.table.entity.RenderType.DATE
import com.addzero.web.ui.hooks.table.entity.RenderType.DATETIME
import com.addzero.web.ui.hooks.table.entity.RenderType.FILE
import com.addzero.web.ui.hooks.table.entity.RenderType.IMAGE
import com.addzero.web.ui.hooks.table.entity.RenderType.SWITCH
import com.addzero.web.ui.hooks.table.entity.RenderType.TEXT
import com.addzero.web.ui.hooks.table.entity.RenderType.TEXTAREA
import org.babyfish.jimmer.Formula
import kotlin.contracts.Returns
import kotlin.reflect.full.hasAnnotation

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
        set(value) = value.run{}

    /** 是否必填 */
    val required: Boolean
        get() = false

    /** 占位文本 */
    val placeholder: String
        get() = "请输入${this.title}"

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
}