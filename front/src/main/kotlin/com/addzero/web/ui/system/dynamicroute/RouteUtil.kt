package com.addzero.web.ui.system.dynamicroute

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.Composable
import cn.hutool.core.util.ClassUtil
import cn.hutool.core.util.StrUtil
import com.addzero.common.kt_util.isNotBlank
import com.addzero.common.kt_util.isNotNull
import com.addzero.common.kt_util.toNotBlankStr
import com.addzero.common.util.data_structure.tree.List2TreeUtil
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.functions


/**
 * 路由扫描器
 */
object RouteUtil {
    private const val META_PKG = "com.addzero.web.modules"

    /**
     * 将扁平的路由列表转换为树形结构
     */
    fun convertRoutesToTree(routes: List<RouteMetadata>): List<RouteMetadata> {
        return List2TreeUtil.list2Tree(
            source = routes,
            idFun = { it.title },
            pidFun = { it.parentName },
            getChildFun = { it.children },
            setChildFun = { route, children ->
                // 由于RouteMetadata是不可变的，我们需要创建一个新的实例
                val newRoute = route.copy(children = children)
                // 在实际使用中，这里会返回新对象
            })
    }


    /**
     * 扫描指定包下的所有路由组件
     */
    fun scanMetas(packageName: String = META_PKG): List<RouteMetadata> {

        // 合并所有路由
        val allRoutes = ArrayList<RouteMetadata>()


        val metaSpecRoutes = getMetaSpecMetaData(packageName)
        val functionRoutes = getFunctionsRouteMetaData(packageName)

        allRoutes.addAll(functionRoutes)
        allRoutes.addAll(metaSpecRoutes)


        // 处理上级名称为空的路由,把父类当做虚拟路由添加到列表中
        val virtualParentRoutes =
            allRoutes.filter { it.visible }.filter { it.parentName.isNotBlank() }.distinctBy { it.parentName }

                .map {
                    val parentName = it.parentName.toNotBlankStr()
                    RouteMetadata(
                        routerPath = "$parentName/${it.routerPath}",
                        title = parentName,
                        parentName = null,
                        icon = Icons.Default.KeyboardArrowDown,
                        visible = true,
                        permissions = emptyList(),
                        order = 0.0,
                        children = emptyList()
                    )
                }

        allRoutes.addAll(virtualParentRoutes)

        // 过滤和排序
        val sortedBy = allRoutes.filter { it.visible }.sortedBy { it.title }.sortedBy { it.order }
        return sortedBy
    }

    private fun getFunctionsRouteMetaData(packageName: String): List<RouteMetadata> {


        // 获取所有带有 @Composable 注解的方法


        val scanPackage = ClassUtil.scanPackage(packageName, {
            val functions = it.kotlin.functions
            val hasRouter = functions.any {
                it.annotations.any {
                    val b = it.annotationClass == Router::class
                    b
                }
            }
            hasRouter
        })
        val toList1 = scanPackage.flatMap {
            val kotlin = it.kotlin
            val qualifiedName = kotlin.qualifiedName
            val functions = kotlin.declaredFunctions
            functions
                .mapNotNull {
                    //函数限定名作为路由路径
                    val refPath = "$qualifiedName#${it.name}"

                    val routorAnno = it.findAnnotation<Router>() ?: return@mapNotNull null
                    val routerPath = routorAnno.routerPath
                    val firstNonNullRouterPath = StrUtil.firstNonBlank(routerPath, refPath)

                    RouteMetadata(
                        routerPath = firstNonNullRouterPath,
                        title = routorAnno.title,
                        parentName = routorAnno.parentName,
                        icon = Icons.Default.Apps,
                        visible = true,
                        permissions = emptyList(),
                        order = 0.0,
                        children = emptyList(),
                        func = { it.call() },
                        clazz = kotlin
                    )


                }
        }.toList()


        return toList1
    }

    private fun getMetaSpecMetaData(packageName: String): List<RouteMetadata> {
        val scanPackageBySuper = ClassUtil.scanPackageBySuper(packageName, MetaSpec::class.java)


        val toList = scanPackageBySuper.map {
            val kClass = it.kotlin
            // 使用反射创建实例
            val instance = it.getDeclaredConstructor().newInstance()
            val metaSpec = instance as MetaSpec
            val refPath = kClass.qualifiedName!!
            val routeMetadata = metaSpec.metadata
            //采用类的全限定类名作为路由路径
            routeMetadata.routerPath = refPath
            routeMetadata.clazz = kClass
            routeMetadata

        }.toList()
        return toList
    }


    /**
     * 根据路径查找路由节点
     * @param path 路由路径
     * @return 找到的路由节点，如果未找到则返回null
     */
    fun findRouteByPath(path: String): RouteMetadata? {
        val allRoutes = scanMetas()
        return allRoutes.find { it.routerPath == path }
    }

    /**
     * 获取面包屑路径,从树形路由从叶子结点遍历标题直到根节点返回标题拼接即可
     * @param path 当前路由路径
     * @return 面包屑路径列表，从根路由到当前路由
     */
    fun getBreadcrumbPath(path: String): List<Pair<String, String?>> {
        val allRoutes = scanMetas()
        val currentRoute = findRouteByPath(path) ?: return listOf(path to "未知页面")

        // 使用TreeSearch查找从根节点到当前节点的路径
        val breadcrumbPath = mutableListOf<Pair<String, String?>>()
        // 添加当前路由
        breadcrumbPath.add(currentRoute.routerPath to currentRoute.title)

        // 递归查找父级路由
        var parentName = currentRoute.parentName
        while (parentName != null) {
            // 查找父级路由
            val parentRoute = allRoutes.find { it.title == parentName }
            if (parentRoute != null) {
                // 将父级路由添加到面包屑路径的开头
                breadcrumbPath.add(0, parentRoute.routerPath to parentRoute.title)
                parentName = parentRoute.parentName
            } else {
                // 如果找不到父级路由，则添加一个虚拟的父级路由
                breadcrumbPath.add(0, "#" to parentName)
                break
            }
        }

        return breadcrumbPath
    }

    /**
     * 根据路径获取路由组件
     * @param path 路由路径
     * @return 路由组件类和实例
     */
    fun getRouteComponentByPath(path: String): RouteMetadata? {
        val scanMetas = scanMetas()
        val associate = scanMetas.associateBy { it.routerPath }
        val routeMetadata = associate[path]
        return routeMetadata
    }

    @Composable
    fun nagive(currentRoute: String) {
        val routeComponentByPath = RouteUtil.getRouteComponentByPath(currentRoute)
        val clazz = routeComponentByPath?.clazz
        val func = routeComponentByPath?.func
        if (func.isNotNull()) {
            // 处理函数组件路由
            func?.let { it() }
        } else if (clazz.isNotNull()) {
            //处理类组件路由
            val createInstance = clazz?.createInstance()
            if (createInstance != null) {
                val metaSpec = createInstance as MetaSpec
                metaSpec.render()
            }
        }
    }

}
