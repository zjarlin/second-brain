package com.addzero.common.util.data_structure.predicate.ex

import cn.hutool.core.util.ObjUtil
import com.addzero.common.util.data_structure.predicate.AbstractPredicateBuilder
import com.addzero.common.util.data_structure.tree.List2TreeUtil
import com.addzero.common.util.data_structure.tree.TreeSearch
import java.util.function.BiConsumer
import java.util.function.Function
import java.util.function.Predicate

class TreePredicateBuilder<T : Any> : AbstractPredicateBuilder<T, TreePredicateBuilder<T>> {
    private var list: List<T>
    var pidPredicate: Predicate<T>
    private var getId: Function<T, *>
    private var getPid: Function<T, *>

    private constructor(
        list: List<T>,
        pidPredicate: Predicate<T>,
        getId: Function<T, *>,
        getPid: Function<T, *>,
        getChildren: Function<T, List<T>>,
        setChildren: BiConsumer<T, List<T>>
    ) {
        this.list = list
        this.pidPredicate = pidPredicate
        this.getId = getId
        this.getPid = getPid
        this.getChildren = getChildren
        this.setChildren = setChildren
    }

    private var getChildren: Function<T, List<T>>
    private var setChildren: BiConsumer<T, List<T>>
    private var trees: List<T> = emptyList()


    private constructor(
        list: List<T>,
        getId: Function<T, *>,
        getPid: Function<T, *>,
        getChildren: Function<T, List<T>>,
        setChildren: BiConsumer<T, List<T>>
    ) {
        this.list = list
        this.getId = getId
        this.getPid = getPid
        this.getChildren = getChildren
        this.setChildren = setChildren
        this.pidPredicate = Predicate<T> { e: T -> ObjUtil.isEmpty(getPid.apply(e)) }
    }

    //    public static <T> TreePredicateBuilder<T> lambdaQuery(List<T> trees, Function<T, List<T>> getChildrenFun) {
    //        return new TreePredicateBuilder<>(trees, getChildrenFun);
    //    }
    //    public static <T> TreePredicateBuilder<T> lambdaQuery(List<T> trees, Function<T, List<T>> getChildrenFun) {
    //        return new TreePredicateBuilder<>(trees, getChildrenFun);
    //    }
    private fun buildTree(): TreePredicateBuilder<T> {
        val tree: List<T> = List2TreeUtil.list2Tree(list, pidPredicate, getId, getPid, getChildren, setChildren)
        this.trees = tree
        return this
    }

    fun tree(): List
<T> {
        return buildTree().trees.toMutableList()
    }


    fun treeOnlyFather(): List
<T> {
        TreeSearch.preserveParentNode(trees, getChildren, predicate)
        return trees
    }

    fun treeSonAndFather(): List
<T> {
        TreeSearch.preserveParentAndChildNode(buildTree().trees, getChildren, predicate)
        return trees
    }


    companion object {
        fun <T : Any> lambdaQuery(
            list: List<T>,
            getId: Function<T, *>,
            getPid: Function<T, *>,
            getChildren: Function<T, List<T>>,
            setChildren: BiConsumer<T, List<T>>
        ): TreePredicateBuilder<T> {
            return TreePredicateBuilder(list, getId, getPid, getChildren, setChildren)
        }

        fun <T : Any> lambdaQuery(
            list: List<T>,
            pidPredicate: Predicate<T>,
            getId: Function<T, *>,
            getPid: Function<T, *>,
            getChildren: Function<T, List<T>>,
            setChildren: BiConsumer<T, List<T>>
        ): TreePredicateBuilder<T> {
            return TreePredicateBuilder(list, pidPredicate, getId, getPid, getChildren, setChildren)
        }
    }
}