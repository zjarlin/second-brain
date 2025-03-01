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
    /** 字段名 */
    val key: String = "",

    /** 字段标题 */
    val title: String,

    /** 占位符文本 */
    val placeholder: String = "请输入$title",

    /** 列默认值 */
    val defaultValue: Any? = null,


    /** 获取列渲染类型的函数，默认为文本类型 */
    val getRenderType: (E) -> RenderType = { TEXT },


    /** 是否必填 */
    val required: Boolean = false,


    /** 自定义验证函数 */
    val validator: (E) -> Boolean = { true },


    /** 验证错误提示信息 */
    val errorMessage: String? = null,

    /** 级联选择器依赖字段 */
    val dependsOn: String? = null,


    /** 排序 */
    val order: Double = 0.0,

    /** 获取列数据的函数 */
    val getFun: (E) -> Any? = {
        // 如果 E 是 Map 类型，则尝试通过 key 获取值
        when (it) {
            is Map<*, *> -> it[key] ?: defaultValue ?: ""
            else -> defaultValue ?: ""
        }
    },

    /** 自定义渲染函数 */
    val customRender: @Composable (E) -> Unit = {
        val renderType = getRenderType(it)
        when (renderType) {
            TEXT -> {
                Text(text = getFun(it).toNotBlankStr())
            }

            IMAGE -> {
                Text(getFun(it).toNotBlankStr())
            }

            CUSTOM -> {
                Text(getFun(it).toNotBlankStr())
            }

            TEXTAREA -> {
                Text(getFun(it).toNotBlankStr())
            }

            SWITCH -> {
                Text(getFun(it).toNotBlankStr())
            }

            TAG -> {
                Text(getFun(it).toNotBlankStr())
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
        }


    }
)
