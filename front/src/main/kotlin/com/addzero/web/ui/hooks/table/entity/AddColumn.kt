package com.addzero.web.ui.hooks.table.entity

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.addzero.common.kt_util.toNotBlankStr
import com.addzero.web.ui.hooks.table.entity.RenderType.*


/**
 * 表格列定义类
 * @param E 数据实体类型
 */
data class AddColumn<E>(

    /** 字段标题 */
    val title: String,

    /** 获取列渲染类型的函数，默认为文本类型 */
    val getRenderType: (E) -> RenderType = { TEXT },

    /** 自定义验证函数 */
    val validator: (E) -> Boolean = { true },

    /** 验证错误提示信息 */
    val errorMessage: String? = null,

    /** 获取列数据的函数 */
    val getFun: (E) -> Any?,
    
    /** 设置值的函数，用于表单编辑 */
    val setFun: (E, Any?) -> E = { e, _ -> e },
    
    /** 是否必填 */
    val required: Boolean = false,
    
    /** 占位文本 */
    val placeholder: String = "",
    
    /** 指定渲染类型，如果为null则自动推导 */
    val renderTypeOverride: RenderType? = null,
    
    /** 选项列表，用于下拉选择等 */
    val options: List<Pair<Any, String>> = emptyList(),

    /** 自定义渲染函数 */
    val customRender: @Composable (E) -> Unit = {
        val renderType = getRenderType(it)
        val fieldValue = getFun(it)
        val text = fieldValue.toNotBlankStr()
        when (renderType) {
            TEXT -> {
                Text(text = text)
            }

            IMAGE -> {
                Text(text)
            }

            CUSTOM -> {
                Text(text)
            }

            TEXTAREA -> {
                Text(text)
            }

            SWITCH -> {
                Text(text)
            }

            TAG -> {
                Text(text)
            }

            NUMBER -> TODO()
            LINK -> TODO()
            DATE -> TODO()
            DATETIME -> TODO()
            SELECT -> TODO()
            MULTISELECT -> TODO()
            CHECKBOX -> TODO()
            RADIO -> TODO()
            CODE -> TODO()
            HTML -> TODO()
            MONEY -> TODO()
            CURRENCY -> TODO()
            PERCENT -> TODO()
            BAR -> TODO()
            TREE -> TODO()
            COMPUTED -> TODO()
            AUTO_COMPLETE -> TODO()
        }


    }
) {
    lateinit var fieldName: String
}
