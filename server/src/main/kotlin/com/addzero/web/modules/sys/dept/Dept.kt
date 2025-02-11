package com.addzero.web.modules.sys.dept

import com.addzero.web.infra.jimmer.base.baseentity.BaseEntity
import com.addzero.web.modules.sys.user.SysUser
import org.babyfish.jimmer.sql.*

@Entity
interface Dept : BaseEntity {

    /**部门编号  */
    val name: String

    @ManyToOne
    @Key
    @JoinColumn(name = "parent_id")
    val parent: Dept?

    @OneToMany(mappedBy = "parent")
    val children: List<Dept>

    @OneToMany(mappedBy = "dept")
    val users: List<SysUser>

}