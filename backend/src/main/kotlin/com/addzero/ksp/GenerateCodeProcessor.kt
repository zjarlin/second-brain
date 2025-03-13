import com.google.auto.service.AutoService
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.validate
import java.io.OutputStream

private const val annoRef = "com.addzero.web.ui.system.dynamicroute.Router"

private const val COM_EXAMPLE_GENERATED = "com.addzero.generated"

@AutoService(SymbolProcessor::class)
class RouteProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        // 设置日志级别为INFO
        logger.warn("Starting annotation processing")
        
        // 获取所有带有 @Route 注解的类
        val symbols = resolver.getSymbolsWithAnnotation(annoRef)
            .filterIsInstance<KSClassDeclaration>()
        
        logger.warn("Found ${symbols.count()} classes with @Route annotation")

        // 生成路由元数据
        val routes = symbols.mapNotNull { classDeclaration ->
            if (!classDeclaration.validate()) {
                logger.error("Invalid class: ${classDeclaration.qualifiedName?.asString()}")
                return@mapNotNull null
            }

            logger.info("Processing class: ${classDeclaration.qualifiedName?.asString()}")

            val routeAnnotation = classDeclaration.annotations
                .first { it.annotationType.resolve().declaration.qualifiedName?.asString() == annoRef }

            val path = routeAnnotation.arguments
                .first { it.name?.asString() == "routerPath" }
                .value as String

            val name = routeAnnotation.arguments
                .firstOrNull { it.name?.asString() == "title" }
                ?.value as? String ?: ""

            logger.info("Generated route metadata - path: $path, name: $name")

            RouteMetadata(
                path = path,
                name = name,
                className = classDeclaration.qualifiedName?.asString() ?: ""
            )
        }

        // 生成路由元数据文件
        generateRoutesFile(routes)

        return emptyList()
    }

private fun generateRoutesFile(routes: Sequence<RouteMetadata>) {
    // 将 Sequence 转换为 List
    val routesList = routes.toList()
    
    logger.info("Generating routes file with ${routesList.size} routes")

    val file = codeGenerator.createNewFile(
        dependencies = Dependencies(false),
        packageName = COM_EXAMPLE_GENERATED,
        fileName = "Routes"
    )

    logger.info("Created new file: $COM_EXAMPLE_GENERATED.Routes")

    file.write(
        """
        package $COM_EXAMPLE_GENERATED

        data class RouteMetadata(
            val path: String,
            val name: String,
            val className: String
        )

        val routes = listOf(
            ${routesList.joinToString(",\n") { route ->
                """
                RouteMetadata(
                    path = "${route.path}",
                    name = "${route.name}",
                    className = "${route.className}"
                )
                """.trimIndent()
            }}
        )
        """.trimIndent().toByteArray()
    )
}
    data class RouteMetadata(
        val path: String,
        val name: String,
        val className: String
    )
}