package com.addzero.web.modules.sys.user

import cn.idev.excel.annotation.ExcelProperty
import com.addzero.web.infra.jimmer.SnowflakeIdGenerator
import com.addzero.web.infra.jimmer.base.basedatetime.BaseDateTime
import com.addzero.web.modules.sys.dept.Dept
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Pattern
import org.babyfish.jimmer.sql.*

/**
 *
 * @author zjarlin
 * @date 2024/11/03
 * @constructor 创建[SysUser]
 *
 */
@Entity
@Table(name = "sys_user")
interface SysUser : BaseDateTime {
    @Id
    @GeneratedValue(generatorType = SnowflakeIdGenerator::class)
    val id: Long

    /** 手机号
     */
    @Key
    @get:Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    val phone: String

    /**
     * 密码
     */
    @get:Schema(description = "密码")
    @get:Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$", message = "密码至少8位，至少1个大写字母，1个小写字母，1个数字")
//    @get:ExcelProperty("密码")
    val password: String

    /**
     * 头像
     */
    val avatar: String?


    /**
     * 昵称
     */
    val nickname: String?

    /**
     * 性别  0：男
      1=女
      2-未知
     */
    val gender: EnumGender?

    @ManyToOne
    val dept: Dept?
}