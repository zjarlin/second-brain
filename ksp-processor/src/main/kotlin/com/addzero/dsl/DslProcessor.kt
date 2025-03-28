package com.addzero.dsl

import com.addzero.AbsProcessor
import com.addzero.dsl.generator.DslGenerator
import com.addzero.dsl.model.DslMeta
import com.addzero.dsl.model.toDslMeta
import com.addzero.getAnnoProperty
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration

/**
 * DSL注解处理器
 * 负责处理带有@Dsl注解的类，生成基于属性委托的DSL构建器代码
 */
class DslProcessor(
    environment: SymbolProcessorEnvironment
) : AbsProcessor<DslMeta>(environment) {

    private val dslGenerator = DslGenerator(codeGenerator, logger)

    override fun getAnnotationName(): String = Dsl::class.qualifiedName!!

    override fun extractMetaData(declaration: KSDeclaration, annotation: KSAnnotation): DslMeta {
        val klass = declaration as? KSClassDeclaration ?: throw IllegalStateException("Only class declarations are supported")
        val constructor = klass.primaryConstructor ?: throw IllegalStateException("Class must have a primary constructor")

        return klass.toDslMeta(
            constructor = constructor,
            parentClasses = getParentClasses(klass),
            genCollectionDslBuilder = getAnnoProperty(annotation, "genCollectionDslBuilder", Boolean::class),
            customDslName = getAnnoProperty(annotation, "value", String::class),
            removePrefix = getAnnoProperty(annotation, "removePrefix", String::class),
            removeSuffix = getAnnoProperty(annotation, "removeSuffix", String::class)
        )
    }

    override fun generateCode(resolver: Resolver, metaList: List<DslMeta>) {
        dslGenerator.generateAll(metaList)
    }

    private fun getParentClasses(klass: KSClassDeclaration): List<KSClassDeclaration> {
        val parents = mutableListOf<KSClassDeclaration>()
        var current = klass.parentDeclaration
        while (current is KSClassDeclaration) {
            parents.add(current)
            current = current.parentDeclaration
        }
        return parents.reversed()
    }
}

class DslProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return DslProcessor(environment)
    }
}
