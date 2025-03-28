package com.addzero.dsl.model

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSValueParameter

/**
 * DSL生成器的元数据模型
 */
data class DslMeta(
    // 类信息
    val className: String,
    val packageName: String,
    val qualifiedName: String,
    val isNested: Boolean,
    
    // 注解配置
    val genCollectionDslBuilder: Boolean,
    val customDslName: String,
    val removePrefix: String,
    val removeSuffix: String,
    
    // 构造函数信息
    val constructor: ConstructorMeta,
    
    // 父类信息
    val parentClasses: List<ParentClassMeta> = emptyList()
)

/**
 * 构造函数元数据
 */
data class ConstructorMeta(
    val parameters: List<ParameterMeta>
)

/**
 * 参数元数据
 */
data class ParameterMeta(
    val name: String,
    val type: String,
    val fullTypeName: String, // 完整类型名称，包括泛型信息
    val isRequired: Boolean,
    val hasDefault: Boolean,
    val defaultValue: String? = null,
    val isNullable: Boolean
)

/**
 * 父类元数据
 */
data class ParentClassMeta(
    val className: String,
    val qualifiedName: String
)

/**
 * 扩展函数，用于从KSClassDeclaration创建DslMeta
 */
fun KSClassDeclaration.toDslMeta(
    constructor: KSFunctionDeclaration,
    parentClasses: List<KSClassDeclaration> = emptyList(),
    genCollectionDslBuilder: Boolean,
    customDslName: String,
    removePrefix: String,
    removeSuffix: String
): DslMeta {
    return DslMeta(
        className = simpleName.asString(),
        packageName = packageName.asString(),
        qualifiedName = qualifiedName?.asString() ?: throw IllegalStateException("Class must have a qualified name"),
        isNested = parentDeclaration is KSClassDeclaration,
        genCollectionDslBuilder = genCollectionDslBuilder,
        customDslName = customDslName,
        removePrefix = removePrefix,
        removeSuffix = removeSuffix,
        constructor = constructor.toConstructorMeta(),
        parentClasses = parentClasses.map { it.toParentClassMeta() }
    )
}

/**
 * 扩展函数，用于从KSFunctionDeclaration创建ConstructorMeta
 */
private fun KSFunctionDeclaration.toConstructorMeta(): ConstructorMeta {
    return ConstructorMeta(
        parameters = parameters.map { it.toParameterMeta() }
    )
}

/**
 * 获取类型的完整字符串表示，包括泛型参数
 */
private fun KSType.getFullTypeName(): String {
    val baseType = declaration.qualifiedName?.asString() ?: "Any"
    val nullableSuffix = if (isMarkedNullable) "?" else ""
    
    // 如果没有泛型参数，直接返回基本类型
    if (arguments.isEmpty()) {
        return "$baseType$nullableSuffix"
    }
    
    // 处理泛型参数
    val genericArgs = arguments.joinToString(", ") { arg ->
        arg.type?.resolve()?.getFullTypeName() ?: "Any"
    }
    
    return "$baseType<$genericArgs>$nullableSuffix"
}

/**
 * 扩展函数，用于从KSValueParameter创建ParameterMeta
 */
private fun KSValueParameter.toParameterMeta(): ParameterMeta {
    val resolvedType = type.resolve()
    val baseTypeName = resolvedType.declaration.qualifiedName?.asString() 
        ?: throw IllegalStateException("Parameter type must have a qualified name")
    val fullTypeName = resolvedType.getFullTypeName()
    
    return ParameterMeta(
        name = name?.asString() ?: throw IllegalStateException("Parameter must have a name"),
        type = baseTypeName,
        fullTypeName = fullTypeName,
        isRequired = !hasDefault,
        hasDefault = hasDefault,
        defaultValue = null, // KSP不支持直接获取默认值
        isNullable = resolvedType.isMarkedNullable
    )
}

/**
 * 扩展函数，用于从KSClassDeclaration创建ParentClassMeta
 */
private fun KSClassDeclaration.toParentClassMeta(): ParentClassMeta {
    return ParentClassMeta(
        className = simpleName.asString(),
        qualifiedName = qualifiedName?.asString() ?: throw IllegalStateException("Parent class must have a qualified name")
    )
} 