package com.addzero

import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration

private const val PKG = "com.addzero.ksp.route"
private const val FILE_NAME = "RouteTable"

class RouteProcessor(
    environment: SymbolProcessorEnvironment
) : AbsProcessor<KspRouteMeta>() {

    private val codeGenerator = environment.codeGenerator
    private val logger = environment.logger

    override fun getAnnotationName(): String = Route::class.qualifiedName!!

    override fun extractMetaData(declaration: KSDeclaration, annotation: KSAnnotation): KspRouteMeta {
        val declarationSimpleName = declaration.simpleName.asString()
        //限定类名
        val declarationQulifiedName = declaration.qualifiedName?.asString() ?: ""
        //包含类名
        val containingClassName =
            (declaration.parentDeclaration as? KSClassDeclaration)?.qualifiedName?.asString() ?: ""
        val path = getAnnoProperty(annotation, "path", String::class).ifBlank { declarationQulifiedName }
        val title = getAnnoProperty(annotation, "title", String::class)
        val parent = getAnnoProperty(annotation, "parent", String::class)
        val icon = getAnnoProperty(annotation, "icon", String::class)

        val visible = getAnnoProperty(annotation, "visible", Boolean::class)
        val order = getAnnoProperty(annotation, "order", Double::class)
        val permissions = getAnnoProperty(annotation, "permissions", String::class)

        val className = when (declaration) {
            is KSClassDeclaration -> {
                declarationQulifiedName
            }

            is KSFunctionDeclaration -> {
                "$containingClassName#$declarationSimpleName"
            }

            else -> ""
        }

        val kspRouteMeta = KspRouteMeta(
            declarationQulifiedName = declarationQulifiedName,
            path = path,
            title = title,
            parent = parent,
            icon = icon,
            visible = visible,
            order = order,
            permissions = permissions

        )
        return kspRouteMeta
    }


    override fun generateCode( metaList: List<KspRouteMeta>) {
        logger.info("Generating route table")
        if (metaList.isNotEmpty()) {

            val routeTableContent = """
        package $PKG
        import com.addzero.KspRouteMeta
        import androidx.compose.runtime.Composable

        object RouteTable {
            val routes = mapOf(
                ${
                metaList.joinToString(",\n") { meta ->
                    """
                    KspRouteMeta(
                        path = "${meta.path}",
                        title = "${meta.title}",
                        parent = "${meta.parent}",
                        icon = "${meta.icon}",
                        visible = ${meta.visible},
                        order = ${meta.order},
                        declarationQulifiedName = "${meta.declarationQulifiedName}", 
                        permissions = "${meta.permissions}" 
                    ) to @Composable { ${meta.declarationQulifiedName}() }
                    """.trimIndent()
                }
            }
            )
        }
    """.trimIndent()

                        codeGenerator.createNewFile(
                            dependencies = Dependencies(
                                aggregating = false,
//                                metaList.toList().toTypedArray()
                            ),

//                            dependencies = Dependencies(
//            false
//                            ),

                            packageName = PKG,
                            fileName = FILE_NAME
                        ).use { output ->



//            codeGenerator.createNewFile(
//                dependencies = Dependencies(true
//                , declaration = metaList.map { it.declarationQulifiedName })
//                , packageName = PKG
//                , fileName = FILE_NAME
//            ).use { output ->


                output.write(routeTableContent.toByteArray())
            }
        }
    }

    override fun onError() {
        logger.error("Error occurred during route processing")
    }
}