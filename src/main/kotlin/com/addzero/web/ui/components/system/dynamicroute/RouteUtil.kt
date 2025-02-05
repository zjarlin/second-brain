package com.addzero.web.ui.components.system.dynamicroute

import cn.hutool.core.util.ClassUtil
import cn.hutool.core.util.ReflectUtil
import kotlin.reflect.KClass
import kotlin.reflect.full.*

/**
 * 路由扫描器
 */
object RouteUtil {
    /**
     * 存储所有路由组件及其元数据
     */
    private var routeComponents = mutableMapOf<KClass<*>, MetaSpec>()

    /**
     * 扫描指定包下的所有路由组件
     */
    fun init(packageName: String) {
        val scanPackageBySuper = ClassUtil.scanPackageBySuper(packageName, MetaSpec::class.java)

        val associate = scanPackageBySuper.associate {
            val kotlin = it.kotlin
            // 使用反射创建实例
            val instance = it.getDeclaredConstructor().newInstance()
            kotlin to (instance as MetaSpec)
        }
        routeComponents= associate as MutableMap<KClass<*>, MetaSpec>

    }

    /**
     * 获取所有路由组件
     */
    fun getAllRouteComponents(): Map<KClass<*>, MetaSpec> {

        RouteUtil.init("com.addzero.web.modules")
        return routeComponents
    }

    /**
     * 获取所有可见的路由组件（用于菜单渲染）
     */
    fun getVisibleRouteComponents(): List<MetaSpec> {
        RouteUtil.init("com.addzero.web.modules")

        return routeComponents.values.filter { it.metadata.visible }
    }

    /**
     * 根据路径获取路由组件
     */
    fun getRouteComponentByPath(path: String): Pair<KClass<*>, MetaSpec>? {
        // 扫描主包下的所有路由组件
        RouteUtil.init("com.addzero.web.modules")

        val find = routeComponents.entries.find { it.value.metadata.refPath == path }
        return find?.toPair()
    }

    /**
     * 获取面包屑路径
     */
    fun getBreadcrumbPath(currentPath: String): List<Pair<String, String>> {

        RouteUtil.init("com.addzero.web.modules")

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