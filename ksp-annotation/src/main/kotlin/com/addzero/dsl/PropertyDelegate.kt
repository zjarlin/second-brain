package com.addzero.dsl

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * 属性委托基类，用于DSL构建器中的属性委托
 */
sealed class PropertyDelegate<T> : ReadWriteProperty<Any, T> {
    protected var _value: T? = null
    protected var _isInitialized = false

    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        if (!_isInitialized) {
            throw UninitializedPropertyAccessException("Property ${property.name} has not been initialized")
        }
        @Suppress("UNCHECKED_CAST")
        return _value as T
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        _value = value
        _isInitialized = true
    }

    /**
     * 检查属性是否已初始化
     */
    fun isInitialized(): Boolean = _isInitialized
}

/**
 * 必选属性委托，用于DSL构建器中的必选属性
 * 如果在构建时未设置该属性，将抛出异常
 */
class RequiredProperty<T> : PropertyDelegate<T>() {
    /**
     * 验证属性是否已初始化
     * @param propertyName 属性名称
     * @throws IllegalStateException 如果属性未初始化
     */
    fun validate(propertyName: String) {
        if (!_isInitialized) {
            throw IllegalStateException("Required property '$propertyName' has not been initialized")
        }
    }
}

/**
 * 可选属性委托，用于DSL构建器中的可选属性
 * 如果在构建时未设置该属性，将使用默认值
 */
class OptionalProperty<T>(private val defaultValue: T) : PropertyDelegate<T>() {
    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return if (_isInitialized) {
            @Suppress("UNCHECKED_CAST")
            _value as T
        } else {
            defaultValue
        }
    }
}

/**
 * 创建必选属性委托
 */
fun <T> required(): RequiredProperty<T> = RequiredProperty()

/**
 * 创建可选属性委托，带有默认值
 */
fun <T> optional(defaultValue: T): OptionalProperty<T> = OptionalProperty(defaultValue)
