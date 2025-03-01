//package com.addzero.web.modules.second_brain.note
//
//import io.swagger.v3.oas.annotations.media.Schema
//import org.apache.poi.ss.formula.functions.T
//import org.babyfish.jimmer.Formula
//import org.babyfish.jimmer.sql.ManyToOne
//import org.babyfish.jimmer.sql.MappedSuperclass
//import org.babyfish.jimmer.sql.OneToMany
//
//@MappedSuperclass
//
//interface BaseTreeNode<T : BaseTreeNode<T>> {
//
//    @get:Schema(description = "是否是叶子节点")
//    @Formula(dependencies = ["children"])
//    val leafFlag: Boolean
//        get() = children.isNotEmpty()
//
//
//    @get:Schema(description = "子")
//    @OneToMany(mappedBy = "parent")
//    val children: List<T>
//
//    @get:Schema(description = "父")
//    @ManyToOne
//    val parent: T?
//
//
//}
