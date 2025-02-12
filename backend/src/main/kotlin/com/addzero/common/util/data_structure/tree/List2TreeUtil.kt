package com.addzero.common.util.data_structure.tree

import cn.hutool.core.collection.CollUtil
import cn.hutool.core.util.ObjUtil
import com.addzero.common.kt_util.add
import com.addzero.common.kt_util.addAll
import java.util.*
import java.util.function.*
import java.util.function.Function

/**
 * @author addzero
 */
object List2TreeUtil {
    fun <T> list2Tree(
        source: List<T>,
        idFun: Function<T, *>,
        pidFun: Function<T, *>,
        getChildFun: Function<T, List
<T>>,
        setChildFun: BiConsumer<T, List<T>>,
    ): List<T> {
        return list2Tree(source, {
            val apply = pidFun.apply { it }
            ObjUtil.isEmpty(apply)
        }, idFun, pidFun, getChildFun, setChildFun)
    }

    /**
     * 支持任意实体 List转Tree
     *
     * @param source      源对象
     * @param isRoot      根节点判断e->e.getPid==null或者e->e.getPid==0
     * @param idFun       T::getId
     * @param pidFun      T::getPId
     * @param getChildFun T::getChildren
     * @param setChildFun T::setChildren
     * @param <T>
     * @return
    </T> */
    fun <T> list2Tree(
        source: List<T>,
        isRoot: Predicate<T>,
        idFun: Function<T, *>,
        pidFun: Function<T, *>,
        getChildFun: Function<T, List<T>>,
        setChildFun: BiConsumer<T, List<T>>,
    ): List<T> {
        if (CollUtil.isEmpty(source)) {
            return ArrayList()
        }

        val ret: List
<T> = ArrayList()
        val map: MutableMap<Any, T> = HashMap()

        source.forEach(Consumer<T> { t: T ->
            Optional.ofNullable<Predicate<T>>(isRoot).map<Predicate<T>> { r: Predicate<T> ->
                if (isRoot.test(t)) {
                    ret.add(t)
                }
                r
            }.orElseGet {
                Optional.ofNullable(pidFun.apply(t)).orElseGet {
                    ret.add(t)
                    null
                }
                null
            }
            map[idFun.apply(t)] = t
        })

        source.forEach(Consumer { t: T ->
            map.computeIfPresent(pidFun.apply(t)) { k: Any, v: T ->
                val ts = Optional.ofNullable(getChildFun.apply(v)).orElseGet(Supplier<List
<T>> {
                    val list: List<T> = ArrayList()
                    setChildFun.accept(v, list)
                    list.toMutableList()
                })
                ts.add(t)
                v
            }
        })
        return ret
    }

    /**
     * 树形数据转list
     *
     * @param treeMenu
     * @return
     */
    fun <T> tree2List(
        treeMenu: List<T>,
        getChildFun: Function<T, List<T>>,
        setChildFun: BiConsumer<T, List<T>>,
    ): List<T> {
        val listMenu: List
<T> = ArrayList()
        for (TreeNode in treeMenu) {
            val child = getChildFun.apply(TreeNode)
            listMenu.add(TreeNode)
            if (CollUtil.isNotEmpty(child)) {
                listMenu.addAll(tree2List(child, getChildFun, setChildFun))
                setChildFun.accept(TreeNode, emptyList())
            }
        }
        return listMenu
    }


}