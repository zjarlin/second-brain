package com.addzero.web.modules.sys.dict

import com.addzero.web.infra.jimmer.base.BaseDeletedEntity
import com.addzero.web.infra.jimmer.base.baseentity.BaseEntity
import com.addzero.web.infra.jimmer.base.BaseTenantEntity
import org.babyfish.jimmer.sql.*


/**
 * <p>
 * sys_dict
 * </p>
 * @author zjarlin
 * @date 2024/11/27
 * @constructor 创建[SysDict]
 */
@Entity
@Table(name = "sys_dict")
interface SysDict : BaseEntity, BaseTenantEntity,BaseDeletedEntity {

    /**
     *  字典名称
     */
    @Column(name = "dict_name")
    val dictName: String

    /**
     *  字典编码
     */
    @Column(name = "dict_code")
    val dictCode: String

    /**
     *  描述
     */
    val description: String?


    /**
     *  字典类型0为string,1为number
     */
    val type: Long?


    @OneToMany(mappedBy = "sysDict")
    val sysDictItems: List<SysDictItem>


}