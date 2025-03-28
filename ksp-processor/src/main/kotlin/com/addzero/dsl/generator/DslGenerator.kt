package com.addzero.dsl.generator

import com.addzero.dsl.model.DslMeta
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger

/**
 * DSL代码生成器
 */
class DslGenerator(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) {
    companion object {
        private const val REQUIRED_DELEGATE = "com.addzero.dsl.required"
        private const val OPTIONAL_DELEGATE = "com.addzero.dsl.optional"
    }

    /**
     * 批量生成DSL代码
     */
    fun generateAll(metaList: List<DslMeta>) {
        metaList.forEach { meta ->
            try {
                generate(meta)
            } catch (e: Exception) {
                logger.error("Error generating DSL for ${meta.qualifiedName}: ${e.message}", null)
            }
        }
    }

    /**
     * 生成单个类的DSL代码
     */
    private fun generate(meta: DslMeta) {
        val fileName = "${meta.className}DslExt"
        val content = generateContent(meta)

        codeGenerator.createNewFile(
            dependencies = Dependencies(true),
            packageName = meta.packageName,
            fileName = fileName
        ).use { output ->
            output.write(content.toByteArray())
        }
    }

    /**
     * 生成DSL内容
     */
    private fun generateContent(meta: DslMeta): String {
        val builderProperties = generateBuilderProperties(meta)
        val buildParams = meta.constructor.parameters.joinToString(", ") { it.name }
        val validationCode = generateValidationCode(meta)
        val dslFunctionName = generateDslFunctionName(meta)
        val builderClassName = "${meta.className}BuilderGen"

        return if (meta.genCollectionDslBuilder) {
            generateCollectionDslContent(meta, builderProperties, buildParams, validationCode, dslFunctionName, builderClassName)
        } else {
            generateSimpleDslContent(meta, builderProperties, buildParams, validationCode, dslFunctionName, builderClassName)
        }
    }

    /**
     * 生成构建器属性
     */
    private fun generateBuilderProperties(meta: DslMeta): String {
        return meta.constructor.parameters.joinToString("\n    ") { param ->
            val delegateType = if (param.isNullable) OPTIONAL_DELEGATE else REQUIRED_DELEGATE
            
            val delegateCall = if (param.isNullable) {
                "$delegateType<${param.fullTypeName}>(null)"
            } else {
                "$delegateType<${param.fullTypeName}>()"
            }
            
            """
            |    private val _${param.name} = $delegateCall
            |
            |    var ${param.name}: ${param.fullTypeName}
            |        get() = _${param.name}.getValue(this, ::${param.name})
            |        set(value) = _${param.name}.setValue(this, ::${param.name}, value)
            """.trimMargin()
        }
    }

    /**
     * 生成验证代码
     */
    private fun generateValidationCode(meta: DslMeta): String {
        return meta.constructor.parameters
            .filter { !it.isNullable }
            .joinToString("\n        ") { param ->
                "_${param.name}.validate(\"${param.name}\")"
            }
    }

    /**
     * 生成DSL函数名
     */
    private fun generateDslFunctionName(meta: DslMeta): String {
        return when {
            meta.customDslName.isNotBlank() -> meta.customDslName
            else -> {
                var name = meta.className
                if (meta.removePrefix.isNotBlank() && name.startsWith(meta.removePrefix)) {
                    name = name.substring(meta.removePrefix.length)
                }
                if (meta.removeSuffix.isNotBlank() && name.endsWith(meta.removeSuffix)) {
                    name = name.substring(0, name.length - meta.removeSuffix.length)
                }
                name.replaceFirstChar { it.lowercase() } + "Gen"
            }
        }
    }

    /**
     * 生成集合DSL内容
     */
    private fun generateCollectionDslContent(
        meta: DslMeta,
        builderProperties: String,
        buildParams: String,
        validationCode: String,
        dslFunctionName: String,
        builderClassName: String
    ): String {
        val outerClassChain = getOuterClassChain(meta)
        
        // 只导入需要的类，避免导入自己的包
        val imports = generateImports(meta)

        return """
        |package ${meta.packageName}
        |
        |$imports
        |
        |class $builderClassName {
        |    $builderProperties
        |
        |    fun validate() {
        |        $validationCode
        |    }
        |
        |    fun build(): ${meta.qualifiedName} {
        |        validate()
        |        return $outerClassChain($buildParams)
        |    }
        |}
        |
        |class ${meta.className}CollectionBuilderGen {
        |    private val items = mutableListOf<${meta.qualifiedName}>()
        |
        |    fun $dslFunctionName(block: $builderClassName.() -> Unit) {
        |        items.add($builderClassName().apply(block).build())
        |    }
        |
        |    fun build(): List<${meta.qualifiedName}> = items.toList()
        |}
        |
        |fun $dslFunctionName(block: $builderClassName.() -> Unit): ${meta.qualifiedName} =
        |    $builderClassName().apply(block).build()
        |
        |fun ${dslFunctionName}s(block: ${meta.className}CollectionBuilderGen.() -> Unit): List<${meta.qualifiedName}> =
        |    ${meta.className}CollectionBuilderGen().apply(block).build()
        |""".trimMargin()
    }

    /**
     * 生成简单DSL内容
     */
    private fun generateSimpleDslContent(
        meta: DslMeta,
        builderProperties: String,
        buildParams: String,
        validationCode: String,
        dslFunctionName: String,
        builderClassName: String
    ): String {
        val outerClassChain = getOuterClassChain(meta)
        
        // 只导入需要的类，避免导入自己的包
        val imports = generateImports(meta)

        return """
        |package ${meta.packageName}
        |
        |$imports
        |
        |class $builderClassName {
        |    $builderProperties
        |
        |    fun validate() {
        |        $validationCode
        |    }
        |
        |    fun build(): ${meta.qualifiedName} {
        |        validate()
        |        return $outerClassChain($buildParams)
        |    }
        |}
        |
        |fun $dslFunctionName(block: $builderClassName.() -> Unit): ${meta.qualifiedName} =
        |    $builderClassName().apply(block).build()
        |""".trimMargin()
    }

    /**
     * 获取外部类链
     */
    private fun getOuterClassChain(meta: DslMeta): String {
        return if (meta.parentClasses.isEmpty()) {
            meta.qualifiedName.substringAfter("${meta.packageName}.")
        } else {
            val parentChain = meta.parentClasses.first().qualifiedName +
                meta.parentClasses.drop(1).joinToString("") { ".${it.className}" }
            "$parentChain.${meta.className}"
        }
    }
    
    /**
     * 生成导入语句，避免导入自己的包
     */
    private fun generateImports(meta: DslMeta): String {
        // 如果类所在的包是其他包，才需要导入
        val outerClassImport = meta.qualifiedName.substringBeforeLast('.')
        
        return if (outerClassImport != meta.packageName) {
            "import $outerClassImport"
        } else {
            // 类在当前包中，不需要导入
            ""
        }
    }
} 