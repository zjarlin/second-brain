package com.addzero.web.modules.sys.area

import io.swagger.v3.oas.annotations.media.Schema
import org.babyfish.jimmer.Formula
import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.Id
import org.babyfish.jimmer.sql.Table


/**
 * <p>
 *  区域表

 * </p>
 *
 * @author zjarlin
 * @date 2025-02-26
 */
@Entity
@Table(name = "sys_area")
@Schema(description = "区域")

public interface SysArea  {

    @get:Schema(description = "名字是否有黑")
    @Formula(dependencies = ["delflag"])
    val blackFlag: Boolean?
        get() = name?.contains("黑") == true || city?.contains("黑") == true

    /**
     *  主键
     */
    @Id
    @get:Schema(description = "主键")
    val sid: String

    /**
     *
上级
     */
    @get:Schema(description = "上级")
    val parentsid: String?

    /**
     *  leveltype

     */
    @get:Schema(description = "级别")
    val leveltype: String?

    /**
     *  name

     */
    @get:Schema(description = "名称")
    val name: String?

    /**
     *  shortname

     */
    @get:Schema(description = "短名称")
    val shortname: String?

    /**

     */
    @get:Schema(description = "上级路径")
    val parentpath: String?

    /**
     *  province

     */
    @get:Schema(description = "省")
    val province: String?

    /**
     *  city

     */
    @get:Schema(description = "市")
    val city: String?

    /**
     *  district

     */
    @get:Schema(description = "区")
    val district: String?

    /**
     *  provincepinyin

     */
    @get:Schema(description = "省拼音")
    val provincepinyin: String?

    /**
     *  citypinyin

     */
    @get:Schema(description = "市拼音")
    val citypinyin: String?

    /**
     *  districtpinyin

     */
    @get:Schema(description = "区拼音")
    val districtpinyin: String?

    /**
     *  citycode

     */
    @get:Schema(description = "城市代码")
    val citycode: String?

    /**
     *  pinyin

     */
    @get:Schema(description = "拼音")
    val pinyin: String?

    /**
     *  jianpin

     */
    @get:Schema(description = "简拼")
    val jianpin: String?

    /**
     *  firstchar

     */
    @get:Schema(description = "开头字母")
    val firstchar: String?

    /**
     *  delflag

     */
    @get:Schema(description = "删除标识")
    val delflag: Int?

    /**
     *  orderid

     */
    @get:Schema(description = "排序id")
    val orderid: Int?

    /**
     *  createuse

     */
    @get:Schema(description = "创建者")
    val createuse: String?

}
