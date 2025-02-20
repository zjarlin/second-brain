package com.addzero.web.modules.second_brain.note

import org.apache.poi.ss.formula.functions.T
import org.babyfish.jimmer.Formula
import org.babyfish.jimmer.sql.ManyToOne
import org.babyfish.jimmer.sql.MappedSuperclass
import org.babyfish.jimmer.sql.OneToMany

interface BaseTreeNode<T : BaseTreeNode<T>> {

    @Formula(dependencies = ["children"])
    val leafFlag: Boolean
        get() = children.isNotEmpty()

    /**
     * 笔记的子节点列表，表示当前笔记的子笔记。
     * 通过 {@link OneToMany} 注解与父笔记关联。
     *
     * @return 子笔记列表
     */
    @OneToMany(mappedBy = "parent")
    val children: List<T>

    /**
     * 笔记的父节点，表示当前笔记的父笔记。
     * 通过 {@link ManyToOne} 注解与子笔记关联。
     *
     * @return 父笔记，如果没有父笔记则返回 null
     */
    @ManyToOne
    val parent: T?


}
