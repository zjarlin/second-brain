package com.addzero.web.modules.second_brain.dotfiles

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import org.babyfish.jimmer.sql.EnumItem

/**
 * /**
 *  操作系统
 *  win=win
 * linux-linux
 * mac mac
 *null.不限
*/
 *
 * @author AutoDDL
 * @date 2025-02-11 10:14:36
 */
enum class EnumOsType(
    val code: String?, val desc: String
) {
    /**
     * win
     */
    @EnumItem(ordinal = 1)
    WIN("1", "win系统"),

    /**
     * linux
     */
    @EnumItem(ordinal = 2)
    LINUX("2", "linux系统"),

    /**
     * mac
     */
    @EnumItem(ordinal = 3)
    MAC("3", "mac系统"),

    /**
     * 不限
     */
    @EnumItem(ordinal = 0)
    BUXIAN("0", "不限");


    @JsonValue
    fun getValue(): String {
        //向前端序列化为描述,jimmer这里没有走@EnumItem的逻辑
        return desc
    }

    companion object {
        @JsonCreator
        fun fromCode(code: String?): EnumOsType? = entries.find { it.code == code }
    }
}
