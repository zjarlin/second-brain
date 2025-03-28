package com.addzero

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.*
import kotlin.reflect.KClass



inline fun <reified T : Any>getAnnoProperty(annotation: KSAnnotation, propName: String="value", propType: KClass<T>): T {
    val value = annotation.arguments.first { it.name?.asString() == propName }.value as T
    return value
}

/**
 * 抽象注解处理器基类
 * @param Meta 注解元数据类型
 */




abstract class AbsProcessor<Meta>(
    environment: SymbolProcessorEnvironment

) : SymbolProcessor {
        protected val codeGenerator = environment.codeGenerator
    protected val logger = environment.logger



    // 存储收集到的元数据
    protected val metaList = mutableListOf<Meta>()

    override fun process(resolver: Resolver): List<KSAnnotated> {
        // 获取所有带有指定注解的类声明
        val symbols = resolver.getSymbolsWithAnnotation(getAnnotationName())

        // 处理类声明上的注解
        symbols.filterIsInstance<KSClassDeclaration>().forEach { processDeclaration(it) }

        // 处理函数声明上的注解
        symbols.filterIsInstance<KSFunctionDeclaration>().forEach { processDeclaration(it) }

        // 处理声明上的注解

        // 生成目标代码
        try {
            generateCode(resolver,metaList)
        } catch (e: Exception) {
            // 处理文件已存在异常
            logger.warn(e.stackTraceToString())
        }


        return emptyList()
    }
    private fun processDeclaration(declaration: KSDeclaration) {
        val annotation = declaration.annotations.find {
            it.shortName.asString() == getAnnotationName().substringAfterLast('.')
        }

        annotation?.let {
            // 解析注解属性为元数据
            val meta = extractMetaData(declaration, it)
            metaList.add(meta)
        }
    }


    override fun finish() {
    }

    /**
     * 获取要处理的注解全限定名
     */
    protected abstract fun getAnnotationName(): String

    /**
     * 从注解中提取元数据
     */
    protected abstract fun extractMetaData(declaration: KSDeclaration, annotation: KSAnnotation): Meta

    /**
     * 根据收集的元数据生成代码
     */
    protected abstract fun generateCode(resolver: Resolver, metaList: List<Meta>)

}
