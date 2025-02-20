package com.addzero.web.modules.second_brain.dotfiles

import com.addzero.web.infra.jimmer.base.baseentity.BaseEntity
import io.swagger.v3.oas.annotations.media.Schema
import org.babyfish.jimmer.sql.*
//import org.babyfish.jimmer.sql.Key


/**
 * <p>
 *  环境变量管理

 * </p>
 *
 * @author zjarlin
 * @date 2024-10-20
 */
@Entity
@Table(name = "biz_dotfiles")
public interface BizDotfiles : BaseEntity {

    /**
     *  操作系统
     *  win=win
     * linux=linux
     * mac=mac
     *null=不限
     */
    @Key
    @Serialized
    @get:Schema(description = "操作系统")
    val osType: List<EnumOsType>?

    /**
     *  系统架构
     *  arm64=arm64
     *  x86=x86
     *  不限=不限
     */
//    @Column(name = "os_structure")
    @Key
    @get:Schema(description = "系统架构")
    val osStructure: Enumplatforms?

    /**
     *  定义类型
     *  alias=alias
     *  export=export
     * function=function
     * sh=sh
     * var=var
     */
    @Key
    val defType: EnumDefType

    /**
     *  名称
     */
    @get:Schema(description = "名称")
    @Key
    val name: String

    /**
     *  值
     */
    val value: String

    /**
     *  注释
     */
    val describtion: String?

    /**
     *  状态
     *  1= 启用
     *  0= 未启用
     */
    @Key
    @Default("1")
    val status: EnumStatus

    /** 文件地址 */
    val fileUrl: String?


    /** 文件位置 */
    val location: String?

}