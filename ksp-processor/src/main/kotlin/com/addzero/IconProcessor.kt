//package com.addzero
//
//import androidx.compose.material.icons.Icons
//import com.google.devtools.ksp.processing.Dependencies
//import com.google.devtools.ksp.processing.Resolver
//import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
//import com.google.devtools.ksp.symbol.KSAnnotation
//import com.google.devtools.ksp.symbol.KSDeclaration
//import androidx.compose.ui.graphics.vector.ImageVector
//import kotlin.jvm.java
//
//class IconProcessor(
//    environment: SymbolProcessorEnvironment
//) : AbsProcessor<String>(environment) {
//
//    override fun getAnnotationName(): String = Route::class.qualifiedName!!
//
//    override fun extractMetaData(declaration: KSDeclaration, annotation: KSAnnotation): String {
//        return ""
//    }
//
//    override fun generateCode(resolver: Resolver, metaList: List<String>) {
//        val iconMapContent = """
//            package com.addzero.ui.icons
//
//            import androidx.compose.material.icons.Icons
//            import androidx.compose.material.icons.filled.*
//            import androidx.compose.material.icons.outlined.*
//            import androidx.compose.material.icons.rounded.*
//            import androidx.compose.material.icons.sharp.*
//            import androidx.compose.material.icons.twotone.*
//            import androidx.compose.ui.graphics.vector.ImageVector
//
//            enum class FilledIcon {
//                ${Icons.Filled::class.java.declaredFields.joinToString(",\n                ") { field -> field.name }};
//
//                fun getIcon(): ImageVector {
//                    val field = Icons.Filled::class.java.getDeclaredField(this.name)
//                    field.isAccessible = true
//                    return field.get(null) as ImageVector
//                }
//            }
//
//            enum class OutlinedIcon {
//                ${Icons.Outlined.javaClass.declaredMethods.filter { it.name.startsWith("get") && it.returnType == ImageVector::class.java }.joinToString(",\n                ") { method -> method.name.removePrefix("get") }};
//
//                fun getIcon(): ImageVector {
//                    val method = Icons.Outlined.javaClass.getDeclaredMethod("get${this.name}")
//                    method.isAccessible = true
//                    return method.invoke(Icons.Outlined) as ImageVector
//                }
//            }
//
//            enum class RoundedIcon {
//                ${Icons.Rounded::class.java.declaredFields.joinToString(",\n                ") { field -> field.name }};
//
//                fun getIcon(): ImageVector {
//                    val field = Icons.Rounded::class.java.getDeclaredField(this.name)
//                    field.isAccessible = true
//                    return field.get(null) as ImageVector
//                }
//            }
//
//            enum class SharpIcon {
//                ${Icons.Sharp::class.java.declaredFields.joinToString(",\n                ") { field -> field.name }};
//
//                fun getIcon(): ImageVector {
//                    val field = Icons.Sharp::class.java.getDeclaredField(this.name)
//                    field.isAccessible = true
//                    return field.get(null) as ImageVector
//                }
//            }
//
//            enum class TwoToneIcon {
//                ${Icons.TwoTone::class.java.declaredFields.joinToString(",\n                ") { field -> field.name }};
//
//                fun getIcon(): ImageVector {
//                    val field = Icons.TwoTone::class.java.getDeclaredField(this.name)
//                    field.isAccessible = true
//                    return field.get(null) as ImageVector
//                }
//            }
//        """.trimIndent()
//
//        codeGenerator.createNewFile(
//            dependencies = Dependencies(aggregating = true),
//            packageName = "com.addzero.ui.icons",
//            fileName = "MaterialIcons"
//        ).use { output ->
//            output.write(iconMapContent.toByteArray())
//        }
//    }
//
//    override fun onError() {
//        logger.error("Error occurred during icon processing")
//    }
//}