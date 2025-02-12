package com.addzero.common.util.data_structure.tree

import cn.hutool.core.util.StrUtil
import com.addzero.common.kt_util.add
import com.addzero.common.kt_util.addAll
import com.addzero.common.kt_util.removeAt
import java.util.*
import java.util.function.Function
import java.util.function.Predicate
import java.util.stream.Stream

/**
 * @author addzero
 * @since 2022/10/11 11:05 PM
 */
object TreeSearch {
    /**
     * 查找所有节点
     *
     * @param currentNode
     * @param allNodes
     * @param getIdFun
     * @param getPidFun
     * @return [List]<[T]>
     */
    fun <T> getAllLeafNodes(
        currentNode: T,
        allNodes: List<T>,
        getIdFun: Function<T, String>,
        getPidFun: Function<T, String>,
    ): List<T> {
        val leafNodes: List<T> = ArrayList()
        leafNodes.add(currentNode) // 添加当前节点

        // 递归查找所有子节点的 T 对象
        for (childNode in allNodes) {
            if (StrUtil.equals(getPidFun.apply(childNode), getIdFun.apply(currentNode))) {
                leafNodes.addAll(getAllLeafNodes(childNode, allNodes, getIdFun, getPidFun))
            }
        }
        return leafNodes
    }

    /**
     * 递归搜索树保留父节点
     *
     * @param trees          树
     * @param getChildrenFun
     */
    fun <T> preserveParentNode(
        trees: List<T>,
        getChildrenFun: Function<T, List<T>>,
        getKeyFun: Function<T, String>,
        key: String,
    ) {
        Stream.iterate(trees.size - 1) { index -> index - 1 }.limit(trees.size.toLong()).forEach { i ->
            if (!sonIsContainsStr(trees[i], getChildrenFun, getKeyFun, key)) {
                trees.removeAt(i)
            }
        }
    }

    /**
     * 递归搜索树保留父节点
     *
     * @param trees          树
     * @param getChildrenFun
     * @param predicate
     */
    fun <T> preserveParentNode(
        trees: List<T>,
        getChildrenFun: Function<T, List<T>>,
        predicate: Predicate<T>,
    ) {
        if (Objects.isNull(predicate)) {
            return
        }
        Stream.iterate(trees.size - 1) { index -> index - 1 }.limit(trees.size.toLong()).forEach { i ->
            if (!sonIsContainsStr(trees[i], getChildrenFun, predicate)) {
                trees.removeAt(i)
            }
        }
    }

    fun <T> sonIsContainsStr(
        node: T,
        getChildrenFun: Function<T, List<T>>,
        getKeyFun: Function<T, String>,
        key: String,
    ): Boolean {
        val predicate = Predicate { e: T -> StrUtil.containsIgnoreCase(getKeyFun.apply(e), key) }
        return sonIsContainsStr(node, getChildrenFun, predicate)
    }

    fun <T> sonIsContainsStr(node: T, getChildrenFun: Function<T, List<T>>, predicate: Predicate<T>): Boolean {
        if (node == null) {
            return false
        }
        val contains = predicate.test(node)

        return Optional.ofNullable(getChildrenFun.apply(node)).map { children ->
            val iterator = children
                .toMutableList()
                .listIterator(children.size)
            while (iterator.hasPrevious()) {
                val childNode = iterator.previous()
                if (sonIsContainsStr(childNode, getChildrenFun, predicate)) {
                    return@map true // 如果找到符合条件的子节点，直接返回 true
                } else {
                    iterator.remove() // 修复的删除操作
                }
            }
            contains
        }.orElse(contains)
    }

    /**
     * 递归搜索(筛选)树保留父子节点
     *
     * @param trees          树
     * @param getChildrenFun
     */
    fun <T> preserveParentAndChildNode(
        trees: List<T>,
        getChildrenFun: Function<T, List<T>>,
        getKeyFun: Function<T, String>,
        key: String,
    ) {
        if (StrUtil.isBlank(key)) {
            return
        }
        Stream.iterate(trees.size - 1) { index -> index - 1 }.limit(trees.size.toLong()).forEach { i ->
            val node = trees[i]
            if (!sonAndFatherIsContains(node, getChildrenFun, getKeyFun, key)) {
                trees.removeAt(i)
            }
        }
    }

    /**
     * 递归搜索(筛选)树保留父子节点
     *
     * @param trees          树
     * @param getChildrenFun
     * @param predicate
     */
    fun <T> preserveParentAndChildNode(
        trees: List<T>,
        getChildrenFun: Function<T, List<T>>,
        predicate: Predicate<T>,
    ) {
        if (Objects.isNull(predicate)) {
            return
        }
        Stream.iterate(trees.size - 1) { index -> index - 1 }.limit(trees.size.toLong()).forEach { i ->
            val node = trees[i]
            if (!sonAndFatherIsContains(node, getChildrenFun, predicate)) {
                trees.toMutableList().remove(node)
            }
        }
    }

    /**
     * 父子节点都保留的判断条件
     *
     * @param node           节点
     * @param getChildrenFun
     * @return boolean
     * @author zjarlin
     * @since 2023/03/03
     */
    fun <T> sonAndFatherIsContains(
        node: T,
        getChildrenFun: Function<T, List<T>>,
        getKeyFun: Function<T, String>,
        key: String,
    ): Boolean {
        val predicate = Predicate { e: T -> StrUtil.containsIgnoreCase(getKeyFun.apply(e), key) }
        return sonAndFatherIsContains(node, getChildrenFun, predicate)
    }

    /**
     * 父子节点都保留的判断条件
     *
     * @param node      节点
     * @param predicate
     * @return boolean
     * @author zjarlin
     * @since 2023/03/03
     */
    fun <T> sonAndFatherIsContains(
        node: T,
        getChildrenFun: Function<T, List<T>>,
        predicate: Predicate<T>,
    ): Boolean {
        if (node == null) {
            return false
        }
        var curFlag = predicate.test(node)
        val children = getChildrenFun.apply(node)
        if (children != null) {
            for (i in children.indices.reversed()) {
                val childNode = children[i]
                val childFlag = sonAndFatherIsContains(childNode, getChildrenFun, predicate)
                if (childFlag || (children.all { !predicate.test(it) } && curFlag)) {
                    curFlag = true
                } else {
                    children.removeAt(i)
                }
            }
        }
        return curFlag
    }

    /**
     * 树自定义排序 根节点在前
     *
     * @author zjarlin
     * @see Comparator
     *
     * @since 2023/12/26
     */
    private class NodeComparator<T>(
        private val getPidFun: Function<T, String>,
        private val isRoot: Predicate<T> = Predicate { t -> getPidFun.apply(t) == null },
    ) : Comparator<T> {

        override fun compare(node1: T, node2: T): Int {
            // 如果 node1 的 pid 为空，而 node2 的 pid 不为空，则将 node1 排在 node2 前面
            return if (isRoot.test(node1) && getPidFun.apply(node2) != null) {
                -1
            } else if (getPidFun.apply(node1) != null && isRoot.test(node2)) {
                1
            } else {
                0
            }
        }
    }
}