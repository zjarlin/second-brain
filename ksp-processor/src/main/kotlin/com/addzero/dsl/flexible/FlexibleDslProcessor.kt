package com.addzero.dsl.flexible

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.validate

/**
 * FlexibleDsl注解处理器
 * 负责处理带有@FlexibleDsl注解的类，生成基于属性委托的DSL构建器代码
 */
class FlexibleDslProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {

    companion object {
        private const val DSL_ANNOTATION = "com.addzero.dsl.flexible.FlexibleDsl"
        private const val REQUIRED_DELEGATE = "com.addzero.dsl.flexible.required"
        private const val OPTIONAL_DELEGATE = "com.addzero.dsl.flexible.optional"
        private const val REQUIRED_PROPERTY = "com.addzero.dsl.flexible.RequiredProperty"
        
        private val DEFAULT_VALUES = mapOf(
            "kotlin.String" to "\"\"",
            "kotlin.Int" to "0",
            "kotlin.Long" to "0L",
            "kotlin.Float" to "0f",
            "kotlin.Double" to "0.0",
            "kotlin.Boolean" to "false",
            "kotlin.Char" to "'\u0000'",
            "kotlin.Byte" to "0.toByte()",
            "kotlin.Short" to "0.toShort()",
            "java.time.LocalDate" to "java.time.LocalDate.now()",
            "java.time.LocalDateTime" to "java.time.LocalDateTime.now()",
            "java.time.LocalTime" to "java.time.LocalTime.now()",
            "java.util.Date" to "java.util.Date()",
            "java.time.Instant" to "java.time.Instant.now()"
        )
    }

    // 缓存已处理的类型
    private val processedTypes = mutableSetOf<String>()
    
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(DSL_ANNOTATION)
        val ret = symbols.filterNot { it.validate() }.toList()

        symbols
            .filterIsInstance<KSClassDeclaration>()
            .forEach { klass ->
                try {
                    processClass(klass, resolver)
                } catch (e: Exception) {
                    logger.error("Error processing ${klass.qualifiedName?.asString()}: ${e.message}", klass)
                }
            }

        return ret
    }

    private fun processClass(klass: KSClassDeclaration, resolver: Resolver) {
        val qualifiedName = klass.qualifiedName?.asString() ?: return
        if (qualifiedName in processedTypes) return
        processedTypes.add(qualifiedName)

        try {
            generateFlexibleDslBuilder(klass, resolver)
            processNestedClasses(klass, resolver)
        } catch (e: Exception) {
            logger.error("Error processing class $qualifiedName: ${e.message}", klass)
        }
    }

    private fun processNestedClasses(klass: KSClassDeclaration, resolver: Resolver, parentClasses: List<KSClassDeclaration> = emptyList()) {
        klass.declarations
            .filterIsInstance<KSClassDeclaration>()
            .filter { it.annotations.any { ann -> ann.shortName.asString() == "FlexibleDsl" } }
            .forEach { nestedClass ->
                try {
                    generateFlexibleDslBuilder(nestedClass, resolver, parentClasses + klass)
                    processNestedClasses(nestedClass, resolver, parentClasses + klass)
                } catch (e: Exception) {
                    logger.error("Error processing nested class: ${e.message}", nestedClass)
                }
            }
    }

    private fun getFullClassName(klass: KSClassDeclaration, parentClasses: List<KSClassDeclaration> = emptyList()): String {
        return if (parentClasses.isEmpty()) {
            "${klass.packageName.asString()}.${klass.simpleName.asString()}"
        } else {
            buildString {
                append(parentClasses.first().qualifiedName?.asString() ?: "")
                parentClasses.drop(1).forEach { parent ->
                    append(".${parent.simpleName.asString()}")
                }
                append(".${klass.simpleName.asString()}")
            }
        }
    }

    private fun getTypeWithGenerics(type: KSTypeReference, parentClasses: List<KSClassDeclaration> = emptyList()): String {
        val resolvedType = type.resolve()
        val declaration = resolvedType.declaration
        
        val typeName = when {
            declaration is KSClassDeclaration && declaration.containingFile == null -> {
                // 内部类处理
                val containingClass = declaration.parentDeclaration as? KSClassDeclaration
                containingClass?.let { 
                    "${getFullClassName(it)}.${declaration.simpleName.asString()}"
                } ?: declaration.qualifiedName?.asString() ?: ""
            }
            else -> declaration.qualifiedName?.asString() ?: ""
        }

        return buildString {
            append(typeName)
            
            // 处理泛型参数
            if (resolvedType.arguments.isNotEmpty()) {
                append("<")
                append(resolvedType.arguments.joinToString(", ") { arg ->
                    arg.type?.let { getTypeWithGenerics(it, parentClasses) } ?: "*"
                })
                append(">")
            }

            if (resolvedType.isMarkedNullable) append("?")
        }
    }

    private fun getDefaultValueForType(type: KSTypeReference, className: String, resolver: Resolver): String {
        val resolvedType = type.resolve()
        val typeName = resolvedType.declaration.qualifiedName?.asString() ?: ""
        
        return when {
            resolvedType.isMarkedNullable -> "null"
            typeName in DEFAULT_VALUES -> DEFAULT_VALUES[typeName]!!
            isCollectionType(typeName) -> getDefaultCollectionValue(type)
            typeName.endsWith(className) -> "${className}FlexibleBuilder().build()"
            else -> tryGetDefaultConstructorValue(typeName, resolver)
        }
    }

    private fun isCollectionType(typeName: String): Boolean = typeName in setOf(
        "kotlin.collections.List",
        "kotlin.collections.MutableList",
        "kotlin.collections.Set",
        "kotlin.collections.MutableSet",
        "kotlin.collections.Map",
        "kotlin.collections.MutableMap",
        "kotlin.Array"
    )

    private fun getDefaultCollectionValue(type: KSTypeReference): String {
        val typeName = type.resolve().declaration.qualifiedName?.asString() ?: ""
        return when (typeName) {
            "kotlin.collections.List" -> "emptyList()"
            "kotlin.collections.MutableList" -> "mutableListOf()"
            "kotlin.collections.Set" -> "emptySet()"
            "kotlin.collections.MutableSet" -> "mutableSetOf()"
            "kotlin.collections.Map" -> "emptyMap()"
            "kotlin.collections.MutableMap" -> "mutableMapOf()"
            "kotlin.Array" -> "emptyArray()"
            else -> "emptyList()"
        }
    }

    private fun tryGetDefaultConstructorValue(typeName: String, resolver: Resolver): String {
        return try {
            val typeDecl = resolver.getClassDeclarationByName(
                resolver.getKSNameFromString(typeName)
            )
            
            if (typeDecl?.primaryConstructor?.parameters?.isEmpty() == true) {
                "${typeName.substringAfterLast('.')}()"
            } else {
                "null"
            }
        } catch (e: Exception) {
            logger.warn("Cannot determine default value for type $typeName", null)
            "null"
        }
    }

    private fun generateFlexibleDslBuilder(
        klass: KSClassDeclaration,
        resolver: Resolver,
        parentClasses: List<KSClassDeclaration> = emptyList()
    ) {
        val packageName = klass.packageName.asString()
        val className = klass.simpleName.asString()
        val fileName = "${className}FlexibleDslExt"
        val fullClassName = getFullClassName(klass, parentClasses)

        val constructor = klass.primaryConstructor ?: run {
            logger.warn("Class $className has no primary constructor, skipping DSL generation", klass)
            return
        }

        val dslConfig = getDslConfig(klass)
        val builderContent = generateBuilderContent(
            className = className,
            fullClassName = fullClassName,
            constructor = constructor,
            parentClasses = parentClasses,
            resolver = resolver,
            dslConfig = dslConfig
        )

        try {
            codeGenerator.createNewFile(
                dependencies = Dependencies(true),
                packageName = packageName,
                fileName = fileName
            ).use { it.write(builderContent.toByteArray()) }
        } catch (e: Exception) {
            logger.error("Error generating DSL builder: ${e.message}", klass)
            throw e
        }
    }

    private data class DslConfig(
        val genCollectionDslBuilder: Boolean,
        val customDslName: String,
        val removePrefix: String,
        val removeSuffix: String
    )

    private fun getDslConfig(klass: KSClassDeclaration): DslConfig {
        val annotation = klass.annotations.find { it.shortName.asString() == "FlexibleDsl" }
        return DslConfig(
            genCollectionDslBuilder = annotation?.arguments?.find { it.name?.asString() == "genCollectionDslBuilder" }?.value as? Boolean ?: false,
            customDslName = annotation?.arguments?.find { it.name?.asString() == "value" }?.value as? String ?: "",
            removePrefix = annotation?.arguments?.find { it.name?.asString() == "removePrefix" }?.value as? String ?: "",
            removeSuffix = annotation?.arguments?.find { it.name?.asString() == "removeSuffix" }?.value as? String ?: ""
        )
    }

    private fun generateBuilderContent(
        className: String,
        fullClassName: String,
        constructor: KSFunctionDeclaration,
        parentClasses: List<KSClassDeclaration>,
        resolver: Resolver,
        dslConfig: DslConfig
    ): String {
        val builderProperties = generateBuilderProperties(constructor, className, resolver, parentClasses)
        val buildParams = constructor.parameters.joinToString(", ") { it.name?.asString() ?: "" }
        val validationCode = generateValidationCode(constructor)
        val dslFunctionName = generateDslFunctionName(className, dslConfig)

        return if (dslConfig.genCollectionDslBuilder) {
            generateCollectionDslContent(
                className = className,
                fullClassName = fullClassName,
                builderProperties = builderProperties,
                validationCode = validationCode,
                buildParams = buildParams,
                dslFunctionName = dslFunctionName
            )
        } else {
            generateSimpleDslContent(
                className = className,
                fullClassName = fullClassName,
                builderProperties = builderProperties,
                validationCode = validationCode,
                buildParams = buildParams,
                dslFunctionName = dslFunctionName
            )
        }
    }

    private fun generateBuilderProperties(
        constructor: KSFunctionDeclaration,
        className: String,
        resolver: Resolver,
        parentClasses: List<KSClassDeclaration>
    ): String = constructor.parameters.joinToString("\n    ") { param ->
        val paramName = param.name?.asString() ?: ""
        val paramType = getTypeWithGenerics(param.type, parentClasses)
        val delegateType = if (param.type.resolve().isMarkedNullable) OPTIONAL_DELEGATE else REQUIRED_DELEGATE
        
        """
        |    private val _$paramName = $delegateType<$paramType>()
        |
        |    var $paramName: $paramType
        |        get() = _$paramName.getValue(this, ::$paramName)
        |        set(value) = _$paramName.setValue(this, ::$paramName, value)
        """.trimMargin()
    }

    private fun generateValidationCode(constructor: KSFunctionDeclaration): String =
        constructor.parameters
            .filter { !it.type.resolve().isMarkedNullable }
            .joinToString("\n        ") { param ->
                val paramName = param.name?.asString() ?: ""
                "_$paramName.validate(\"$paramName\")"
            }

    private fun generateDslFunctionName(className: String, config: DslConfig): String =
        when {
            config.customDslName.isNotBlank() -> config.customDslName
            else -> {
                var name = className
                if (config.removePrefix.isNotBlank() && name.startsWith(config.removePrefix)) {
                    name = name.substring(config.removePrefix.length)
                }
                if (config.removeSuffix.isNotBlank() && name.endsWith(config.removeSuffix)) {
                    name = name.substring(0, name.length - config.removeSuffix.length)
                }
                name.replaceFirstChar { it.lowercase() }
            }
        }

    private fun generateCollectionDslContent(
        className: String,
        fullClassName: String,
        builderProperties: String,
        validationCode: String,
        buildParams: String,
        dslFunctionName: String
    ): String {
        val outerClassChain = fullClassName.substringAfter("${fullClassName.substringBeforeLast('.').substringBeforeLast('.')}.")
        
        return """
        |package ${fullClassName.substringBeforeLast('.').substringBeforeLast('.')}
        |
        |import ${fullClassName.substringBeforeLast('.')}
        |import com.addzero.dsl.flexible.RequiredProperty
        |import com.addzero.dsl.flexible.required
        |import com.addzero.dsl.flexible.optional
        |
        |class ${className}FlexibleBuilder {
        |    $builderProperties
        |
        |    fun validate() {
        |        $validationCode
        |    }
        |
        |    fun build(): $fullClassName {
        |        validate()
        |        return $outerClassChain($buildParams)
        |    }
        |}
        |
        |class ${className}FlexibleCollectionBuilder {
        |    private val items = mutableListOf<$fullClassName>()
        |
        |    fun $dslFunctionName(block: ${className}FlexibleBuilder.() -> Unit) {
        |        items.add(${className}FlexibleBuilder().apply(block).build())
        |    }
        |
        |    fun build(): List<$fullClassName> = items.toList()
        |}
        |
        |fun ${dslFunctionName}Flexible(block: ${className}FlexibleBuilder.() -> Unit): $fullClassName =
        |    ${className}FlexibleBuilder().apply(block).build()
        |
        |fun ${dslFunctionName}sFlexible(block: ${className}FlexibleCollectionBuilder.() -> Unit): List<$fullClassName> =
        |    ${className}FlexibleCollectionBuilder().apply(block).build()
        |""".trimMargin()
    }

    private fun generateSimpleDslContent(
        className: String,
        fullClassName: String,
        builderProperties: String,
        validationCode: String,
        buildParams: String,
        dslFunctionName: String
    ): String {
        val outerClassChain = fullClassName.substringAfter("${fullClassName.substringBeforeLast('.').substringBeforeLast('.')}.")
        
        return """
        |package ${fullClassName.substringBeforeLast('.').substringBeforeLast('.')}
        |
        |import ${fullClassName.substringBeforeLast('.')}
        |import com.addzero.dsl.flexible.RequiredProperty
        |import com.addzero.dsl.flexible.required
        |import com.addzero.dsl.flexible.optional
        |
        |class ${className}FlexibleBuilder {
        |    $builderProperties
        |
        |    fun validate() {
        |        $validationCode
        |    }
        |
        |    fun build(): $fullClassName {
        |        validate()
        |        return $outerClassChain($buildParams)
        |    }
        |}
        |
        |fun ${dslFunctionName}Flexible(block: ${className}FlexibleBuilder.() -> Unit): $fullClassName =
        |    ${className}FlexibleBuilder().apply(block).build()
        |""".trimMargin()
    }
}

class FlexibleDslProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return FlexibleDslProcessor(
            environment.codeGenerator,
            environment.logger
        )
    }
}