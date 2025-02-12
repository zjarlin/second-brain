package com.addzero.web.infra.jimmer.base.low

import com.addzero.web.modules.sys.user.SysUser
import io.swagger.v3.oas.annotations.media.Schema
import org.babyfish.jimmer.sql.Column
import org.babyfish.jimmer.sql.MappedSuperclass
import org.babyfish.jimmer.sql.OneToOne


@MappedSuperclass
interface BaseShenHeEntity {

    @get: Schema(description = "审批状态")
    val approvalState: String?

    @get:Schema(description = "录入人员")
    @OneToOne
    val sysUser: SysUser?

    val dataSids: String?
    val formSids: String?
    val formSidName: String?

}