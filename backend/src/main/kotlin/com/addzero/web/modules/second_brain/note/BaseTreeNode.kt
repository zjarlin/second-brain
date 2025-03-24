//package com.addzero.web.modules.second_brain.note
//
//import com.addzero.common.anno.Shit
//import io.swagger.v3.oas.annotations.media.Schema
//import org.babyfish.jimmer.Formula
//import org.babyfish.jimmer.sql.ManyToOne
//import org.babyfish.jimmer.sql.MappedSuperclass
//import org.babyfish.jimmer.sql.OneToMany
//
//@MappedSuperclass
//
//@Shit
//interface BaseTreeNode<T : BaseTreeNode<T>> {
//
//    @get:Schema(description = "是否是叶子节点")
//    @Formula(dependencies = ["children"])
//    val leafFlag: Boolean
//        get() = children.isEmpty()
//
//
//    @OneToMany(mappedBy = "parent")
//    val children: List<T>
//
//    @ManyToOne
//    val parent: T?
//
//
//}
