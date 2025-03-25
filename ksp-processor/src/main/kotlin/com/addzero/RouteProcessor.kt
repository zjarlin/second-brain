package com.addzero

import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration

private const val PKG = "com.addzero.ksp.route"
private const val FILE_NAME = "RouteTable"

class RouteProcessor(
    environment: SymbolProcessorEnvironment
) : AbsProcessor<KspRouteMeta>(environment) {

//    private val codeGenerator = environment.codeGenerator
//    private val logger = environment.logger

    override fun getAnnotationName(): String = Route::class.qualifiedName!!

    override fun extractMetaData(declaration: KSDeclaration, annotation: KSAnnotation): KspRouteMeta {
        val declarationSimpleName = declaration.simpleName.asString()
        val declarationQulifiedName = declaration.qualifiedName?.asString() ?: ""
        val containingClassName =
            (declaration.parentDeclaration as? KSClassDeclaration)?.qualifiedName?.asString() ?: ""
        val path = getAnnoProperty(annotation, "path", String::class).ifBlank { declarationQulifiedName }
        val title = getAnnoProperty(annotation, "title", String::class).ifBlank { declarationSimpleName }
        val parent = getAnnoProperty(annotation, "parent", String::class)
        val icon = getAnnoProperty(annotation, "icon", String::class)
        val visible = getAnnoProperty(annotation, "visible", Boolean::class)
        val order = getAnnoProperty(annotation, "order", Double::class)
        val permissions = getAnnoProperty(annotation, "permissions", String::class)

        return KspRouteMeta(
            declarationQulifiedName = declarationQulifiedName,
            path = path,
            title = title,
            parent = parent,
            icon = icon,
            visible = visible,
            order = order,
            permissions = permissions
        )
    }

    override fun generateCode(resolver: Resolver, metaList: List<KspRouteMeta>) {
        if (metaList.isEmpty()) return

        val dependencies = Dependencies(
            aggregating = true,
            sources = resolver.getAllFiles().toList().toTypedArray()
        )

        val routeTableContent = """
            package $PKG
            
            import com.addzero.KspRouteMeta
            import androidx.compose.runtime.Composable
            
            object RouteTable {
                val routes = mapOf(
                    ${metaList.joinToString(",\n                    ") { meta ->
                    """KspRouteMeta(
                        path = "${meta.path}",
                        title = "${meta.title}",
                        parent = "${meta.parent}",
                        icon = "${meta.icon}",
                        visible = ${meta.visible},
                        order = ${meta.order},
                        declarationQulifiedName = "${meta.declarationQulifiedName}",
                        permissions = "${meta.permissions}"
                    ) to @Composable { ${meta.declarationQulifiedName}() }"""
                    }}
                )
            }
        """.trimIndent()

        codeGenerator.createNewFile(
            dependencies = dependencies,
            packageName = PKG,
            fileName = FILE_NAME
        ).use { output ->
            output.write(routeTableContent.toByteArray())
        }
    }

    override fun onError() {
        logger.error("Error occurred during route processing")
    }
}