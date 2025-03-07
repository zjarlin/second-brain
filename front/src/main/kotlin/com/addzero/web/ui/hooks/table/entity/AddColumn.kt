package com.addzero.web.ui.hooks.table.entity

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cn.hutool.core.util.ReflectUtil
import com.addzero.common.kt_util.toNotBlankStr
import com.addzero.web.infra.jackson.parseObject
import com.addzero.web.infra.jackson.toJson
import com.addzero.web.modules.sys.area.SysArea
import com.addzero.web.ui.hooks.table.entity.RenderType.*
import com.alibaba.fastjson2.JSON
import com.alibaba.fastjson2.parseObject
import org.babyfish.jimmer.DraftObjects
import org.babyfish.jimmer.ImmutableObjects
import org.babyfish.jimmer.meta.ImmutableType
import org.babyfish.jimmer.runtime.DraftSpi
import org.babyfish.jimmer.runtime.Internal
import org.babyfish.jimmer.runtime.Internal.currentDraftContext
import kotlin.reflect.KClass



/**
 * 表格列定义类
 * @param E 数据实体类型
 */
data class AddColumn<E : Any>(

    /** 字段标题 */
    var title: String,
    var fieldName: String="",

    /** 获取列渲染类型的函数，默认为文本类型 */
    val getRenderType: (E) -> RenderType = { TEXT },

    /** 自定义验证函数 */
    val validator: (E) -> Boolean = { true },

    /** 验证错误提示信息 */
    val errorMessage: String? = null,
    var clazz: KClass<E>?=null,

    /** 获取列数据的函数 */
    val getFun: (E) -> Any?,

    /** 设置值的函数，用于表单编辑 */
    val setFun: (E,String, Any?) -> E = { e,fieldName, value ->
        setImmutableObj(e, fieldName, value)
    },

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
}

private fun <E : Any> setImmutableObj(e: E, fieldName: String, value: Any?): E {
    //先写死clazz测试
    val toJson = e.toJson()
    val parseObject = JSON.parseObject(toJson)
    parseObject.put(fieldName, value)
    val toJson1 = parseObject.toJson()
    val fromString = ImmutableObjects.fromString(e.javaClass, toJson1)
    return fromString


//    return setImmuInternal(e, fieldName, value)

}

private fun <E : Any> setImmuInternal(e: E, fieldName: String, value: Any?): E {
    var obj: E? = null
    Internal.produce(ImmutableType.get(e.javaClass), e, { d ->
        val toJson = d.toJson()
        val parseObject = JSON.parseObject(toJson)
        parseObject.put(fieldName, value)
        val toJson1 = parseObject.toJson()
        val fromString = ImmutableObjects.fromString(e.javaClass, toJson1)
        obj = fromString
    })
    return obj!!
}
