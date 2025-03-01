package com.addzero.web.ui.system.dynamicroute

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.runtime.Composable
import cn.hutool.core.util.ClassUtil
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.functions
import kotlin.reflect.full.hasAnnotation

/**
 * 路由扫描器，用于扫描带有@Router注解的类和函数
 */
object RouterScanner {
    /**
     * 存储所有路由组件及其元数据
     */
    private val routeComponents: MutableMap<String, RouteInfo> = mutableMapOf()

    /**
     * 路由信息类，包含路由元数据和渲染函数
     */
    data class RouteInfo(
        val metadata: RouteMetadata,
        val renderFunction: @Composable () -> Unit
    )

    /**
     * 初始化路由扫描器，扫描指定包下的所有带有@Router注解的类和函数
     */
    fun init(packageName: String) {
        // 扫描实现了MetaSpec接口的类
        scanMetaSpecClasses(packageName)
        
        // 扫描带有@Router注解的类
        scanAnnotatedClasses(packageName)
        
        // 扫描带有@Router注解的函数
        scanAnnotatedFunctions(packageName)
    }

    /**
     * 扫描实现了MetaSpec接口的类
     */
    private fun scanMetaSpecClasses(packageName: String) {
        val metaSpecClasses = ClassUtil.scanPackageBySuper(packageName, MetaSpec::class.java)
        
        metaSpecClasses.forEach { clazz ->
            try {
                val instance = clazz.getDeclaredConstructor().newInstance() as MetaSpec
                val metadata = instance.metadata
                val refPath = instance.refPath
                
                routeComponents[refPath] = RouteInfo(
                    metadata = metadata,
                    renderFunction = { instance.render() }
                )
            } catch (e: Exception) {
                println("初始化MetaSpec类失败: ${clazz.name}, 错误: ${e.message}")
            }
        }
    }

    /**
     * 扫描带有@Router注解的类
     */
    private fun scanAnnotatedClasses(packageName: String) {
        val allClasses = ClassUtil.scanPackage(packageName)
        
        allClasses.forEach { clazz ->
            val kotlinClass = clazz.kotlin
            
            if (kotlinClass.hasAnnotation<Router>()) {
                val annotation = kotlinClass.findAnnotation<Router>()!!
                val metadata = RouteMetadata(
                    title = annotation.title,
                    parentName = annotation.parentName.takeIf { it.isNotEmpty() },
                    visible = annotation.visible,
                    order = annotation.order
                )
                
                // 检查类是否有render方法
                val renderMethod = kotlinClass.functions.find { it.name == "render" }
                if (renderMethod != null) {
                    try {
                        val instance = clazz.getDeclaredConstructor().newInstance()
                        val refPath = clazz.name
                        
                        routeComponents[refPath] = RouteInfo(
                            metadata = metadata,
                            renderFunction = { 
                                renderMethod.call(instance)
                            }
                        )
                    } catch (e: Exception) {
                        println("初始化带注解的类失败: ${clazz.name}, 错误: ${e.message}")
                    }
                }
            }
        }
    }

    /**
     * 扫描带有@Router注解的函数
     */
    private fun scanAnnotatedFunctions(packageName: String) {
        val allClasses = ClassUtil.scanPackage(packageName)
        
        allClasses.forEach { clazz ->
            val kotlinClass = clazz.kotlin
            
            kotlinClass.functions.forEach { function ->
                if (function.hasAnnotation<Router>()) {
                    val annotation = function.findAnnotation<Router>()!!
                    val metadata = RouteMetadata(
                        title = annotation.title,
                        parentName = annotation.parentName.takeIf { it.isNotEmpty() },
                        visible = annotation.visible,
                        order = annotation.order
                    )
                    
                    // 检查函数是否有@Composable注解
                    if (function.annotations.any { it.annotationClass.simpleName == "Composable" }) {
                        try {
                            val instance = if (!kotlinClass.isCompanion) {
                                clazz.getDeclaredConstructor().newInstance()
                            } else {
                                // 获取伴生对象实例
                                val companionField = clazz.getDeclaredField("Companion")
                                companionField.isAccessible = true
                                companionField.get(null)
                            }
                            
                            val refPath = "${clazz.name}#${function.name}"
                            
                            routeComponents[refPath] = RouteInfo(
                                metadata = metadata,
                                renderFunction = { 
                                    function.call(instance)
                                }
                            )
                        } catch (e: Exception) {
                            println("初始化带注解的函数失败: ${clazz.name}#${function.name}, 错误: ${e.message}")
                        }
                    }
                }
            }
        }
    }

    /**
     * 获取所有路由组件
     */
    fun getAllRoutes(): Map<String, RouteInfo> = routeComponents

    /**
     * 获取所有可见的路由组件（用于菜单渲染），按order字段排序
     */
    fun getVisibleRoutes(): List<Pair<String, RouteInfo>> = routeComponents.entries
        .filter { it.value.metadata.visible }
        .sortedBy { it.value.metadata.order }
        .map { it.key to it.value }

    /**
     * 根据路径获取路由组件
     */
    fun getRouteByPath(path: String): RouteInfo? = routeComponents[path]

    /**
     * 获取面包屑路径
     */
    fun getBreadcrumbPath(path: String): List<RouteMetadata> {
        val result = mutableListOf<RouteMetadata>()
        var currentPath = path
        
        while (currentPath.isNotEmpty()) {
            val component = routeComponents[currentPath]
            if (component != null) {
                result.add(0, component.metadata)
                currentPath = component.metadata.parentName ?: ""
            } else {
                break
            }
        }
        
        return result
    }
}