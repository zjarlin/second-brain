package com.addzero.web.ui.hooks.table.entity

import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import cn.hutool.core.bean.BeanUtil
import cn.hutool.core.util.ObjUtil
import com.addzero.common.kt_util.*
import com.addzero.web.ui.hooks.table.entity.RenderType.*
import org.babyfish.jimmer.DraftObjects
import org.babyfish.jimmer.Formula
import org.babyfish.jimmer.meta.ImmutableType
import org.babyfish.jimmer.runtime.Internal
import org.babyfish.jimmer.spring.repo.PageParam
import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation


/**
 * 表格列定义类
 * @param E 数据实体类型
 */
data class JimmerColumn<E : Any>(
    override var title: String,
    override var fieldName: String = "",
    override val getFun: (E) -> Any?,
    override val setFun: (E, IColumn<E>, Any?) -> E = { e, c, value ->
        e.copy(c.fieldName, value)!!
    },

    override val required: Boolean = false,

    ) : IColumn<E> {

    var currentField: FieldMetadata<E>? = null

    /** 占位文本 */
    override val placeholder: String
        get() = "请输入${this.title}"


    /** 验证错误提示信息 */
    override val errorMessage: String
        get() = "${this.title}的值非法"


    /** 推测列渲染类型的函数，默认为文本类型 */
    override val renderType: RenderType
        get() {
            currentField ?: return TEXT
            val fieldName = this.fieldName
            val property = currentField!!.property
            val returnType = property.returnType
            val iscacle = property.hasAnnotation<Transient>() || property.hasAnnotation<Formula>()
            val renderType = when {
                iscacle -> {
                    CUSTOM
                }

                fieldName.containsAnyIgnoreCase("url,file") -> RenderType.FILE
                fieldName.containsAnyIgnoreCase("image") -> IMAGE
                fieldName.containsAnyIgnoreCase("date") && !fieldName.containsAnyIgnoreCase("time") -> DATE
                fieldName.containsAnyIgnoreCase("time", "datetime") -> DATETIME
                fieldName.contains("description") || fieldName.contains("content") || fieldName.contains("text") -> TEXTAREA
                returnType.classifier == String::class -> TEXT
                returnType.classifier == Boolean::class -> SWITCH
                else -> {
                    CUSTOM
                }
            }
            return renderType
        }

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
    override val validator: (E?) -> Boolean
        get() = {
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
    Internal.produce(ImmutableType.get(entity.javaClass), entity) { d ->
        DraftObjects.set(d, fieldName, value)
        d
    } as E
}

fun <E : Any> E?.toMap(): MutableMap<String, Any>? {
    if (this == null) {
        return null
    }
    val beanToMap = BeanUtil.beanToMap(this, false, true)
    return beanToMap

}
