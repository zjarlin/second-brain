package com.addzero

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSTypeReference
import com.google.devtools.ksp.validate

class DslProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation("com.addzero.Dsl")
        val ret = symbols.filterNot { it.validate() }.toList()

        symbols
            .filterIsInstance<KSClassDeclaration>()
            .forEach { klass ->
                try {
                    generateDslBuilder(klass, resolver)
                } catch (e: Exception) {
                    logger.error("Error processing ${klass.qualifiedName?.asString()}: ${e.message}")
                }
            }

        return ret
    }

    private fun generateDslBuilder(klass: KSClassDeclaration, resolver: Resolver) {
        val packageName = klass.packageName.asString()
        val className = klass.simpleName.asString()
        val fileName = "${className}DslExt"

        // 获取@Dsl注解实例
        val dslAnnotation = klass.annotations.find { it.shortName.asString() == "Dsl" }

        // 获取isCollection属性值，默认为false
        val genCollectionDslBuilder = dslAnnotation?.arguments?.find { it.name?.asString() == "genCollectionDslBuilder" }?.value as? Boolean ?: false

        // 获取自定义DSL函数名称
        val customDslName = dslAnnotation?.arguments?.find { it.name?.asString() == "value" }?.value as? String ?: ""
        val removePrefix = dslAnnotation?.arguments?.find { it.name?.asString() == "removePrefix" }?.value as? String ?: ""
        val removeSuffix = dslAnnotation?.arguments?.find { it.name?.asString() == "removeSuffix" }?.value as? String ?: ""

        val constructor = klass.primaryConstructor ?: run {
            logger.warn("Class $className has no primary constructor, skipping DSL generation")
            return
        }

        // 收集所有需要导入的类型
        val imports = mutableSetOf<String>().apply {
            add("${packageName}.${className}")
            constructor.parameters.forEach { param ->
                collectImportsFromType(param.type.resolve(), this)
            }
        }

        // 生成Builder属性
        val builderProperties = constructor.parameters.joinToString("\n    ") { param ->
            val paramName = param.name?.asString() ?: ""
            val paramType = getTypeWithGenerics(param.type)
            val defaultValue = getDefaultValueForType(param.type, className, resolver)
            "var $paramName: $paramType = $defaultValue"
        }

        // 生成build()方法参数
        val buildParams = constructor.parameters.joinToString(", ") {
            it.name?.asString() ?: ""
        }

        // 生成empty方法参数
        val emptyParams = constructor.parameters.joinToString(", ") { param ->
            val paramName = param.name?.asString() ?: ""
            val defaultValue = getDefaultValueForType(param.type, className, resolver)
            "$paramName = $defaultValue"
        }

        // 生成DSL函数名称
        val dslFunctionName = when {
            customDslName.isNotBlank() -> customDslName
            else -> {
                var name = className
                if (removePrefix.isNotBlank() && name.startsWith(removePrefix)) {
                    name = name.substring(removePrefix.length)
                }
                if (removeSuffix.isNotBlank() && name.endsWith(removeSuffix)) {
                    name = name.substring(0, name.length - removeSuffix.length)
                }
                name.replaceFirstChar { it.lowercase() }
            }
        }

        // 生成集合DSL函数名称（默认为单数形式+s）
        val collectionDslFunctionName = "${dslFunctionName}s"

        val fileContent = if (genCollectionDslBuilder) {
            // 为集合类型生成DSL
            """
            |package $packageName
            |
            |${imports.joinToString("\n") { "import $it" }}
            |
            |class ${className}Builder {
            |    $builderProperties
            |
            |    fun build(): $className = $className(
            |        $buildParams
            |    )
            |
            |    companion object {
            |        fun empty$className(): $className = $className(
            |            $emptyParams
            |        )
            |    }
            |}
            |
            |class ${className}CollectionBuilder {
            |    private val items = mutableListOf<$className>()
            |
            |    fun $dslFunctionName(block: ${className}Builder.() -> Unit) {
            |        items.add(${className}Builder().apply(block).build())
            |    }
            |
            |    fun build(): List<$className> = items.toList()
            |}
            |
            |/**
            | * DSL扩展函数，用于构建[$className]实例
            | */
            |fun $dslFunctionName(block: ${className}Builder.() -> Unit): $className {
            |    return ${className}Builder().apply(block).build()
            |}
            |
            |/**
            | * DSL扩展函数，用于构建[$className]集合
            | */
            |fun $collectionDslFunctionName(block: ${className}CollectionBuilder.() -> Unit): List<$className> {
            |    return ${className}CollectionBuilder().apply(block).build()
            |}
            |""".trimMargin()
        } else {
            // 为普通类型生成DSL
            """
            |package $packageName
            |
            |${imports.joinToString("\n") { "import $it" }}
            |
            |class ${className}Builder {
            |    $builderProperties
            |
            |    fun build(): $className = $className(
            |        $buildParams
            |    )
            |
            |    companion object {
            |        fun empty$className(): $className = $className(
            |            $emptyParams
            |        )
            |    }
            |}
            |
            |/**
            | * DSL扩展函数，用于构建[$className]实例
            | */
            |fun $dslFunctionName(block: ${className}Builder.() -> Unit): $className {
            |    return ${className}Builder().apply(block).build()
            |}
            |""".trimMargin()
        }

        codeGenerator.createNewFile(
            dependencies = Dependencies(true),
            packageName = packageName,
            fileName = fileName
        ).use { it.write(fileContent.toByteArray()) }
    }

    private fun collectImportsFromType(type: KSType, imports: MutableSet<String>) {
        val declaration = type.declaration
        val qualifiedName = declaration.qualifiedName?.asString() ?: return

        if (qualifiedName.isNotBlank() && !qualifiedName.startsWith("kotlin.") &&
            !qualifiedName.startsWith("java.")) {
            imports.add(qualifiedName)
        }

        type.arguments.forEach { arg ->
            arg.type?.resolve()?.let { collectImportsFromType(it, imports) }
        }
    }

    private fun getTypeWithGenerics(type: KSTypeReference): String {
        val resolvedType = type.resolve()
        val isNullable = resolvedType.isMarkedNullable
        val typeName = buildString {
            append(resolvedType.declaration.qualifiedName?.asString() ?: "")

            if (resolvedType.arguments.isNotEmpty()) {
                append("<")
                append(resolvedType.arguments.joinToString(", ") { arg ->
                    arg.type?.let { getTypeWithGenerics(it) } ?: "*"
                })
                append(">")
            }

            if (isNullable) append("?")
        }

        return typeName
    }

    private fun getDefaultValueForType(type: KSTypeReference, className: String, resolver: Resolver): String {
        val resolvedType = type.resolve()
        val isNullable = resolvedType.isMarkedNullable
        val typeName = resolvedType.declaration.qualifiedName?.asString() ?: ""
        val simpleType = typeName.substringAfterLast('.')
        val fullType = getTypeWithGenerics(type)

        return when {
            isNullable -> "null"
            simpleType == "String" -> "\"\""
            simpleType == "Int" -> "0"
            simpleType == "Long" -> "0L"
            simpleType == "Float" -> "0f"
            simpleType == "Double" -> "0.0"
            simpleType == "Boolean" -> "false"
            simpleType == "Char" -> "'\u0000'"
            simpleType == "Byte" -> "0.toByte()"
            simpleType == "Short" -> "0.toShort()"
            simpleType == "LocalDate" -> "java.time.LocalDate.now()"
            simpleType == "LocalDateTime" -> "java.time.LocalDateTime.now()"
            simpleType == "LocalTime" -> "java.time.LocalTime.now()"
            simpleType == "Date" -> "java.util.Date()"
            simpleType == "Instant" -> "java.time.Instant.now()"
            fullType.contains("List<") -> "emptyList()"
            fullType.contains("MutableList<") -> "mutableListOf()"
            fullType.contains("Set<") -> "emptySet()"
            fullType.contains("MutableSet<") -> "mutableSetOf()"
            fullType.contains("Map<") -> "emptyMap()"
            fullType.contains("MutableMap<") -> "mutableMapOf()"
            fullType.contains("Array<") -> "emptyArray()"
            simpleType == className -> "${className}Builder.empty$className()"
            else -> {
                try {
                    val typeDecl = resolver.getClassDeclarationByName(
                        resolver.getKSNameFromString(typeName)
                    ) ?: return "${simpleType}()"

                    if (typeDecl.primaryConstructor?.parameters?.isEmpty() == true) {
                        "${simpleType}()"
                    } else {
                        "null"
                    }
                } catch (e: Exception) {
                    logger.warn("Cannot determine default value for type $typeName: ${e.message}")
                    "null"
                }
            }
        }
    }
}

class DslProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return DslProcessor(
            environment.codeGenerator,
            environment.logger
        )
    }
}
