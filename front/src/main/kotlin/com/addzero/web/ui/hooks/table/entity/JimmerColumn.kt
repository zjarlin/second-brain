package com.addzero.web.ui.hooks.table.entity

import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import cn.hutool.core.bean.BeanUtil
import cn.hutool.core.util.ObjUtil
import com.addzero.common.kt_util.*
import com.addzero.web.modules.sys.area.SysArea
import com.addzero.web.ui.hooks.table.entity.RenderType.*
import org.babyfish.jimmer.DraftObjects
import org.babyfish.jimmer.Formula
import org.babyfish.jimmer.meta.ImmutableType
import org.babyfish.jimmer.runtime.Internal
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.set
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
                property1?.returnType?.classifier == Boolean::class -> SWITCH
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

fun <E : Any> E?.copy(fieldName: String, value: Any?): E? = this?.let { entity ->
    val copy = this.copy {
        DraftObjects.set(it, fieldName, value)
    }
    return copy
}

fun <E : Any> E?.copy(block: (Any) -> Unit = {}): E? = this?.let { entity ->
    Internal.produce(ImmutableType.get(entity.javaClass), entity) { d ->
        block(d)
        d
    } as E
}

fun <E : Any> E.fromMap(updates: Map<String, Any?>, block: (String, Any?) -> Unit = { _, _ -> }): E? {
    val newItem = this.copy { draft ->
        updates.forEach { (fieldName, value) ->
            DraftObjects.set(draft, fieldName, value)
            block(fieldName, value)
        }
    }
    return newItem
}

fun <E : Any> E?.toMap(): MutableMap<String, Any>? {
    if (this == null) {
        return null
    }
    val beanToMap = BeanUtil.beanToMap(this, false, true)
    return beanToMap
}
