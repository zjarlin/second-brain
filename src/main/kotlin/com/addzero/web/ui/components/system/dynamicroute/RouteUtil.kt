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
     * 获取所有路由组件，按order字段排序
     */
    fun getAllRouteComponents(): Map<KClass<*>, MetaSpec> = routeComponents.toList()
        .sortedBy { it.second.metadata.order }
        .toMap()

    fun getAllSpec(): List<RouteMetadata> = routeComponents
        .map { it.value.metadata }
        .sortedBy { it.order }

    /**
     * 获取所有可见的路由组件（用于菜单渲染），按order字段排序
     */
    fun getVisibleRouteComponents(): List<MetaSpec> = routeComponents.values
        .filter { it.metadata.visible }
        .sortedBy { it.metadata.order }

    /**
     * 根据路径获取路由组件
     */
    fun getRouteComponentByPath(path: String): Pair<KClass<*>, MetaSpec>? {
        //         .metadata.refPath
        val routeComponents1 = routeComponents
        return routeComponents1.entries.find { it.value.refPath == path }?.toPair()
    }

    /**
     * 获取面包屑路径
     */
    fun getBreadcrumbPath(path: String): List<RouteMetadata> {
        val result = mutableListOf<RouteMetadata>()
        var currentPath = path
        while (currentPath.isNotEmpty()) {
            val component = getRouteComponentByPath(currentPath)
            if (component != null) {
                result.add(0, component.second.metadata)
                currentPath = component.second.metadata.parentRefPath ?: ""
            } else {
                break
            }
        }
        return result
    }
}