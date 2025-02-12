package com.addzero.web.infra.jimmer.base

import com.addzero.common.consts.EnumOrm
import org.babyfish.jimmer.sql.Column
import org.babyfish.jimmer.sql.LogicalDeleted
import org.babyfish.jimmer.sql.MappedSuperclass


@MappedSuperclass
interface BaseDeletedEntity {
    @LogicalDeleted("1")
    @Column(name = "deleted")
    val deleted: Int
}