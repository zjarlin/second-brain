package com.addzero.web.ui.components.system.dynamicroute

import cn.hutool.core.util.ClassUtil
import kotlin.reflect.KClass

/**
 * 路由扫描器
 */
object RouteUtil {
    /**
     * 存储所有路由组件及其元数据
     */
    private val routeComponents: Map<KClass<*>, MetaSpec> by lazy {
        init("com.addzero.web.modules")
    }

    /**
     * 扫描指定包下的所有路由组件
     */
    private fun init(packageName: String): MutableMap<KClass<*>, MetaSpec> {
        val scanPackageBySuper = ClassUtil.scanPackageBySuper(packageName, MetaSpec::class.java)

        return scanPackageBySuper.associate {
            val kotlin = it.kotlin
            // 使用反射创建实例
            val instance = it.getDeclaredConstructor().newInstance()
            kotlin to (instance as MetaSpec)
        } as MutableMap<KClass<*>, MetaSpec>
    }

    /**
     * 获取所有路由组件
     */
    fun getAllRouteComponents(): Map<KClass<*>, MetaSpec> = routeComponents

    fun getAllSpec(): List<RouteMetadata> = routeComponents.map { it.value.metadata }

    /**
     * 获取所有可见的路由组件（用于菜单渲染）
     */
    fun getVisibleRouteComponents(): List<MetaSpec> = routeComponents.values.filter { it.metadata.visible }

    /**
     * 根据路径获取路由组件
     */
    fun getRouteComponentByPath(path: String): Pair<KClass<*>, MetaSpec>? =
        routeComponents.entries.find { it.value.metadata.refPath == path }?.toPair()

    /**
     * 获取面包屑路径
     */
    fun getBreadcrumbPath(currentPath: String): List<Pair<String, String>> {
        val result = mutableListOf<Pair<String, String>>()
        var currentSpec = getRouteComponentByPath(currentPath)?.second

        while (currentSpec != null) {
            result.add(
                0, Pair(
                    currentSpec.metadata.refPath, currentSpec.metadata.title
                )
            )
            currentSpec = currentSpec.metadata.parentRefPath?.let { parentPath ->
                getRouteComponentByPath(parentPath)?.second
            }
        }

        return result
    }
}