package com.addzero.web.modules.second_brain.tag

import com.addzero.web.infra.jimmer.base.baseentity.BaseEntity
import org.babyfish.jimmer.sql.*

/**
 * 标签实体类，用于管理笔记的标签系统
 * 该实体类映射到数据库表 `biz_tag`
 */
@Entity
@Table(name = "biz_tag")
interface BizTag : BaseEntity {

    /**
     * 标签名称
     */
    @Key
    val name: String

    /**
     * 标签描述
     */
    val description: String?

}
