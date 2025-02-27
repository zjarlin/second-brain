package com.addzero.common.kt_util

import io.swagger.v3.oas.annotations.media.Schema
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

/**
 * 类的元数据信息
 */
data class ClassMetadata<T>(
    /** 类名 */
    val className: String,
    /** 类描述（来自@Schema注解） */
    val description: String?,
    /** 字段列表 */
    val fields: List<FieldMetadata<T>>
)

/**
 * 字段的元数据信息
 */
data class FieldMetadata<T>(
    /** 字段名 */
    val name: String,

    val property: KProperty1<T, *>,

    /** 字段类型 */
    val type: String,
    /** 字段描述（来自@Schema注解） */
    val description: String?,
    /** 示例值 */
    val example: String?,
    /** 是否可空 */
    val nullable: Boolean
)







/**
 * 获取类的元数据信息的扩展函数
 */
fun <T : Any> KClass<T>.getMetadata(): ClassMetadata<T> {
    // 获取类名
    val className = this.simpleName ?: ""

    // 获取类的@Schema注解信息
    val classSchema = this.findAnnotation<Schema>()
    val classDescription = classSchema?.description

    // 获取所有字段的元数据，包括接口中声明的属性
    val fields = this
        .memberProperties
//        .filterIsInstance<kotlin.reflect.KProperty<*>>()
        .map { property ->
            // 获取属性的getter
            val getter = property.getter
            val schema = getter.annotations.find { it is Schema } as? Schema

            // 优先查找getter上的@Schema注解
            val findAnnotation = getter.findAnnotation<Schema>()
            val propertySchema = findAnnotation ?: property.findAnnotation<Schema>()

            val description = propertySchema?.description
            FieldMetadata(
                property = property,
                name = property.name,
                type = property.returnType.toString(),
                description = description,
                example = propertySchema?.example,
                nullable = property.returnType.isMarkedNullable
            )
        }

    return ClassMetadata(
        className = className,
        description = classDescription,
        fields = fields
    )
}
