package com.addzero.web.ui.utils

import io.swagger.v3.oas.annotations.media.Schema
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation

/**
 * 类的元数据信息
 */
data class ClassMetadata(
    /** 类名 */
    val className: String,
    /** 类描述（来自@Schema注解） */
    val description: String?,
    /** 字段列表 */
    val fields: List<FieldMetadata>
)

/**
 * 字段的元数据信息
 */
data class FieldMetadata(
    /** 字段名 */
    val name: String,
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
 * 元数据提供者接口
 */
interface MetadataProvider<T : Any> {
    /**
     * 获取类的元数据信息
     */
    fun getMetadata(clazz: KClass<T>): ClassMetadata
}

/**
 * 默认的元数据提供者实现，基于@Schema注解
 */
class DefaultMetadataProvider<T : Any> : MetadataProvider<T> {
    override fun getMetadata(clazz: KClass<T>): ClassMetadata {
        // 获取类名
        val className = clazz.simpleName ?: ""
        
        // 获取类的@Schema注解信息
        val classSchema = clazz.findAnnotation<Schema>()
        val classDescription = classSchema?.description
        
        // 获取所有字段的元数据
        val fields = clazz.declaredMemberProperties.map { property ->
            val propertySchema = property.findAnnotation<Schema>()
            
            FieldMetadata(
                name = property.name,
                type = property.returnType.toString(),
                description = propertySchema?.description,
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
}

/**
 * 获取类的元数据信息的扩展函数
 */
fun <T : Any> KClass<T>.getMetadata(provider: MetadataProvider<T> = DefaultMetadataProvider()): ClassMetadata {
    // 获取类名
    val className = this.simpleName ?: ""
    
    // 获取类的@Schema注解信息
    val classSchema = this.findAnnotation<Schema>()
    val classDescription = classSchema?.description
    
    // 获取所有字段的元数据
    val fields = this.declaredMemberProperties.map { property ->
        val propertySchema = property.findAnnotation<Schema>()
        
        FieldMetadata(
            name = property.name,
            type = property.returnType.toString(),
            description = propertySchema?.description,
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