package com.addzero.web.modules.sys.area.entity

import com.addzero.web.infra.jimmer.base.baseentity.BaseEntity
import org.babyfish.jimmer.sql.*


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
public interface SysArea  {

/**
*  主键
*/
    @Id
    val sid: String

/**
*  parentsid

*/
    val parentsid: String?

/**
*  leveltype

*/
    val leveltype: String?

/**
*  name

*/
    val name: String?

/**
*  shortname

*/
    val shortname: String?

/**
*  parentpath

*/
    val parentpath: String?

/**
*  province

*/
    val province: String?

/**
*  city

*/
    val city: String?

/**
*  district

*/
    val district: String?

/**
*  provincepinyin

*/
    val provincepinyin: String?

/**
*  citypinyin

*/
    val citypinyin: String?

/**
*  districtpinyin

*/
    val districtpinyin: String?

/**
*  citycode

*/
    val citycode: String?

/**
*  pinyin

*/
    val pinyin: String?

/**
*  jianpin

*/
    val jianpin: String?

/**
*  firstchar

*/
    val firstchar: String?

/**
*  delflag

*/
    val delflag: Int?

/**
*  orderid

*/
    val orderid: Int?

/**
*  createuse

*/
    val createuse: String?

}
