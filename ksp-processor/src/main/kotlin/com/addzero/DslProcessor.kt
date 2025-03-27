package com.addzero

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSTypeReference
import com.google.devtools.ksp.validate

class DslProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val options: Map<String, String>
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

        // 生成Builder类内容
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

        val fileContent = """
            |package $packageName
            |
            |${imports.joinToString("\n") { "import $it" }}
            |
            |class ${className}Builder {
            |    $builderProperties
            |
            |    fun build(): $className = $className($buildParams)
            |}
            |
            |/**
            | * DSL扩展函数，用于构建[$className]实例
            | */
            |fun $className(block: ${className}Builder.() -> Unit): $className {
            |    return ${className}Builder().apply(block).build()
            |}
            |""".trimMargin()

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
        val typeWithGenerics = getTypeWithGenerics(type)
        val resolvedType = type.resolve()
        val isNullable = resolvedType.isMarkedNullable
        val typeName = resolvedType.declaration.qualifiedName?.asString() ?: ""
        val simpleType = typeName.substringAfterLast('.')




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
            typeWithGenerics.contains("List<") -> "emptyList()"
            typeWithGenerics.contains("MutableList<") -> "mutableListOf()"
            typeWithGenerics.contains("Set<") -> "emptySet()"
            typeWithGenerics.contains("MutableSet<") -> "mutableSetOf()"
            typeWithGenerics.contains("Map<") -> "emptyMap()"
            typeWithGenerics.contains("MutableMap<") -> "mutableMapOf()"
            typeWithGenerics.contains("Array<") -> "emptyArray()"
            simpleType == className -> "${simpleType}Builder().build()"
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
            environment.logger,
            environment.options
        )
    }
}