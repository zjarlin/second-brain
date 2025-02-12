package com.addzero.web.infra.jimmer.base.baseentity

import cn.hutool.core.lang.generator.UUIDGenerator
import com.addzero.web.infra.jimmer.SnowflakeIdGenerator
import com.addzero.web.infra.jimmer.base.basedatetime.BaseDateTime
import com.addzero.web.modules.sys.user.SysUser
import org.babyfish.jimmer.sql.*
import org.babyfish.jimmer.sql.meta.UUIDIdGenerator


@MappedSuperclass
interface BaseEntity : BaseDateTime {
    @Id
    @GeneratedValue(generatorType = SnowflakeIdGenerator::class)
    val id: Long

    @ManyToOne
    @OnDissociate(DissociateAction.DELETE)
    @JoinColumn(foreignKeyType = ForeignKeyType.FAKE, name = "update_by")
    val updateBy: SysUser?

    @ManyToOne
    @JoinColumn(foreignKeyType = ForeignKeyType.FAKE, name = "create_by")
    @OnDissociate(DissociateAction.DELETE)
    val createBy: SysUser?
}