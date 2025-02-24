package com.addzero.web.modules.second_brain.dotfiles

import com.addzero.web.infra.jimmer.base.baseentity.BaseEntity
import com.addzero.web.modules.second_brain.tag.BizTag
import com.addzero.web.modules.sys.dict.SysDict
import com.addzero.web.modules.sys.dict.SysDictItem
import io.swagger.v3.oas.annotations.media.Schema
import org.babyfish.jimmer.sql.*
import org.babyfish.jimmer.sql.JoinTable.*
import javax.swing.text.html.HTML.Tag

//import org.babyfish.jimmer.sql.Key

fun main() {
    BizDotfiles {

    }
}
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
    @get:Schema(description = "操作系统")
    @ManyToMany
    @JoinTable(
        name = "biz_mapping",
        joinColumnName = "from_id",
        inverseJoinColumnName = "to_id",
        filter = JoinTableFilter(
            columnName = "mapping_type",
            values = ["dotfiles_tag_mapping"]
        )
    )
    val osType: List<BizTag>

    /**
     *  系统架构
     *  arm64=arm64
     *  x86=x86
     *  不限=不限
     */
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
    @get:Schema(description = "定义类型")
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
    @get:Schema(description = "值")
    val value: String

    /**
     *  注释
     */
    @get:Schema(description = "注释")
    val describtion: String?

    /**
     *  状态
     *  1= 启用
     *  0= 未启用
     */
    @get:Schema(description = "状态")
    @Key
    @Default("1")
    val status: EnumStatus

    /** 文件地址 */
    @get:Schema(description = "文件地址")
    val fileUrl: String?


    /** 文件位置 */
    @get:Schema(description = "文件位置")
    val location: String?

}
