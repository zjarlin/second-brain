package com.addzero.web.modules.second_brain.note;

import com.addzero.web.infra.jimmer.base.baseentity.BaseEntity
import com.addzero.web.modules.second_brain.tag.BizTag
import org.apache.poi.ss.formula.functions.T
import org.babyfish.jimmer.Formula
import org.babyfish.jimmer.sql.*

/**
 * 笔记实体类，用于表示笔记的基本信息和结构。
 * 该实体类映射到数据库表 `biz_note`。
 */
@Entity
@Table(name = "biz_note")
interface BizNote : BaseEntity {

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
    val children: List<BizNote>

    /**
     * 笔记的父节点，表示当前笔记的父笔记。
     * 通过 {@link ManyToOne} 注解与子笔记关联。
     *
     * @return 父笔记，如果没有父笔记则返回 null
     */
    @ManyToOne
    val parent: BizNote?


    /**
     * 标题
     *
     */
    @Key
    val title: String

    /**
     * 内容
     *
     */
    @Key
    val content: String

    /**
     * 类型
     * 1=markdown
     *2=pdf
     *3=word
     *4=excel
     *
     * @return 笔记类型
     */
    @Key
    val type: String?

    /**
     * 笔记的标签列表，用于分类和检索。
     * 通过中间表实现与标签的多对多关系
     *
     * @return 标签列表
     */
    @ManyToMany
    @JoinTable(
        name = "biz_note_tag",
        joinColumnName = "note_id",
        inverseJoinColumnName = "tag_id"
    )
    val tags: List<BizTag>

//    /**
//     * 笔记的子节点列表，表示当前笔记的子笔记。
//     * 通过 {@link OneToMany} 注解与父笔记关联。
//     *
//     * @return 子笔记列表
//     */
//    @OneToMany(mappedBy = "parent")
//    val children: List<BizNote>

//    /**
//     * 笔记的父节点，表示当前笔记的父笔记。
//     * 通过 {@link ManyToOne} 注解与子笔记关联。
//     *
//     * @return 父笔记，如果没有父笔记则返回 null
//     */
//    @ManyToOne
//    val parent: BizNote?

    /**
     * 笔记的路径
     *
     * @return 笔记路径
     */
    val path: String?

    /**
     * 笔记关联的文件链接（可选）。
     *
     */
    val fileUrl: String?

}
