package com.addzero

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration

private const val PKG = "com.addzero.ksp.route"
private const val FILE_NAME = "RouteTable"

class RouteProcessor(
    environment: SymbolProcessorEnvironment
) : AbsProcessor<Route>(environment) {

//    private val codeGenerator = environment.codeGenerator
//    private val logger = environment.logger

    override fun getAnnotationName(): String = Route::class.qualifiedName!!

    override fun extractMetaData(declaration: KSDeclaration, annotation: KSAnnotation): Route {
        val declarationSimpleName = declaration.simpleName.asString()
        val declarationQulifiedName = declaration.qualifiedName?.asString() ?: ""
        val containingClassName =
            (declaration.parentDeclaration as? KSClassDeclaration)?.qualifiedName?.asString() ?: ""
        val value = getAnnoProperty(annotation, "value", String::class)

        val path = getAnnoProperty(annotation, "path", String::class).ifBlank { declarationQulifiedName }

        val title = getAnnoProperty(annotation, "title", String::class).ifBlank { declarationSimpleName }
        val parent = getAnnoProperty(annotation, "parent", String::class)
        val icon = getAnnoProperty(annotation, "icon", String::class)
        val visible = getAnnoProperty(annotation, "visible", Boolean::class)
        val order = getAnnoProperty(annotation, "order", Double::class)
        val permissions = getAnnoProperty(annotation, "permissions", String::class)
        val homePageFlag = getAnnoProperty(annotation, "homePageFlag", Boolean::class)

        return Route(
            value = value,
            path = path,
            title = title,
            parent = parent,
            icon = icon,
            visible = visible,
            order = order,
            permissions = permissions,
            declarationQulifiedName = declarationQulifiedName,
            homePageFlag = homePageFlag
        )
    }

    override fun generateCode(resolver: Resolver, metaList: List<Route>) {
        if (metaList.isEmpty()) return

        val dependencies = Dependencies(
            aggregating = true,
            sources = resolver.getAllFiles().toList().toTypedArray()
        )

        val routeTableContent = """
            package $PKG
            import com.addzero.Route
            import androidx.compose.runtime.Composable
            
            object RouteTable {
                val routes = mapOf(
                    ${
            metaList.joinToString(",\n                    ") { meta ->

                val parent = meta.parent.ifBlank { meta.value }

                """Route(
                        value = "${meta.value}",
                        path = "${meta.path}",
                        title = "${meta.title}",
                        parent = "$parent",
                        icon = "${meta.icon}",
                        visible = ${meta.visible},
                        order = ${meta.order},
                        declarationQulifiedName = "${meta.declarationQulifiedName}",
                        homePageFlag =  ${meta.homePageFlag},
                        permissions = "${meta.permissions}"
                    ) to @Composable { ${meta.declarationQulifiedName}() }"""
            }
        }
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

class RouteProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return RouteProcessor(environment)
    }
}
