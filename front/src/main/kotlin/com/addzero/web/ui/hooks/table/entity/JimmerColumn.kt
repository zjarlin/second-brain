package com.addzero.web.ui.hooks.table.entity

import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import cn.hutool.core.util.ObjUtil
import com.addzero.common.kt_util.FieldMetadata
import com.addzero.common.kt_util.toNotBlankStr
import com.addzero.web.infra.jimmer.copy
import com.addzero.web.ui.hooks.table.entity.RenderType.BOOL_SWITCH
import com.addzero.web.ui.hooks.table.entity.RenderType.CUSTOM
import com.addzero.web.ui.hooks.table.entity.RenderType.TEXT
import org.babyfish.jimmer.Formula
import kotlin.reflect.full.hasAnnotation


/**
 * 表格列定义类
 * @param E 数据实体类型
 */
class JimmerColumn<E : Any>(
    override var title: String,
    override val getFun: (E) -> Any?,
    override val setFun: (E, IColumn<E>, Any?) -> E = { e, c, value ->
        e.copy(c.fieldName, value)!!
    },
) : IColumn<E> {

    override var fieldName: String = ""
    var currentField: FieldMetadata<E>? = null


    val property = currentField?.property

    override var renderType: RenderType
        get() {
            val property1 = currentField?.property
            return when {
                property1?.returnType?.classifier == String::class -> TEXT
                property1?.returnType?.classifier == Boolean::class -> BOOL_SWITCH
                property?.hasAnnotation<Transient>() == true || property?.hasAnnotation<Formula>() == true -> CUSTOM
                else -> super.renderType
            }
        }
        set(value) = value.run {}


    /** 自定义列表渲染函数 */
    @OptIn(ExperimentalMaterial3Api::class)
    override var customRender: @Composable (E) -> Unit = {
        val currentItem = getFun(it)

        val content = currentItem.toNotBlankStr()
        TooltipBox(
            positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(), tooltip = {
                PlainTooltip {
                    Text(content)
                }
            }, state = rememberTooltipState()
        ) {
            SelectionContainer {
                val displayText = if (content.length > 30) {
                    content.take(30) + "..."
                } else {
                    content
                }
                Text(text = displayText)
            }
        }
    }

    /** 自定义验证函数 */
    /** 自定义验证函数 */
    override val validator: (E?) -> Boolean = {
        val any = it?.let { it1 -> getFun(it1) }
        if (required) {
            ObjUtil.isNotEmpty(any)
        }
        //如果是手机号
        //如果是身份证号
        true
    }


}


