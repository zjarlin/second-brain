package com.addzero.web.ui.hooks.table.entity

import androidx.compose.runtime.Composable
import cn.hutool.core.util.ObjUtil
import com.addzero.common.kt_util.*
import com.addzero.web.infra.jackson.toJson
import com.addzero.web.ui.hooks.form.FormItem
import com.addzero.web.ui.hooks.table.entity.RenderType.*
import com.alibaba.fastjson2.JSON
import org.babyfish.jimmer.Formula
import org.babyfish.jimmer.ImmutableObjects
import org.babyfish.jimmer.meta.ImmutableType
import org.babyfish.jimmer.runtime.Internal
import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation


/**
 * 表格列定义类
 * @param E 数据实体类型
 */
data class AddColumn<E : Any>(
    /** 字段标题 */
    var title: String,
    var fieldName: String = "",

    var clazz: KClass<E>? = null,

    /** 获取列数据的函数 */
    val getFun: (E) -> Any?,

    /** 设置值的函数，用于表单编辑 */
    val setFun: (E?, AddColumn<E>, Any?) -> E? = { e, c, value ->
        setImmutableObj(e, c, value)
    },

    /** 是否必填 */
    val required: Boolean = false,

    /** 选项列表，用于下拉选择等 */
    val options: List<Pair<Any, String>> = emptyList(),

    ) {

    lateinit var currentField: FieldMetadata<E>

    /** 占位文本 */
    val placeholder: String
        get() = "请输入${this.title}"


    /** 验证错误提示信息 */
    val errorMessage: String
        get() = "${this.title}的值非法"


    /** 推测列渲染类型的函数，默认为文本类型 */
    val renderType: RenderType
        get() {
//            // 如果明确指定了渲染类型，则使用指定的类型
//            if (renderType.isNotNull()) {
//                return CUSTOM
//            }
            val fieldName = this.fieldName

            val property = currentField.property


            val returnType = property.returnType

            val renderType = when {
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
    var customRender: @Composable (E) -> Unit = {
        FormItem(this, null)
    }

    /** 自定义验证函数 */
    val validator: (E?) -> Boolean
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

private fun <E : Any> setImmutableObj(e: E?, addColumn: AddColumn<E>, value: Any?): E? {
    if (e.isNull()) {
        return e
    }
    //先写死clazz测试
    val toJson = e!!.toJson()
    val parseObject = JSON.parseObject(toJson)
    parseObject.put(addColumn.fieldName, value)
    val toJson1 = parseObject.toJson()
    val fromString = ImmutableObjects.fromString(e.javaClass, toJson1)
    return fromString
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
