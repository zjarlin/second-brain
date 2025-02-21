package com.addzero.web.modules.sys.dict

import com.addzero.web.infra.jimmer.base.baseentity.BaseEntity
import org.babyfish.jimmer.sql.*


/**
 * <p>
 *  sys_dict_item

 * </p>
 *
 * @author zjarlin
 * @date 2024-09-16
 */
@Entity
@Table(name = "sys_dict_item")
interface SysDictItem : BaseEntity {


    /**
     *  字典项文本
     */
    @Column(name = "item_text")
    val itemText: String

    /**
     *  字典项值
     */
    @Column(name = "item_value")
    val itemValue: String

    /**
     *  描述
     */
    val description: String?

    /**
     *  排序
     */
    @Column(name = "sort_order")
    val sortOrder: Int?

    /**
     *  状态（1启用 0不启用）
     */
    val status: Int?
        get() = 1

    @ManyToOne
    @JoinColumn(name = "dictId")
    val sysDict: SysDict?

}
