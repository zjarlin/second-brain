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
    @EnumItem(name = "1")
    WIN("code_1", "win"),

    /**
     * linux
     */
    @EnumItem(name = "2")
    LINUX("code_2", "linux"),

    /**
     * mac
     */
    @EnumItem(name = "3")
    MAC("code_3", "mac"),

    /**
     * 不限
     */
    @EnumItem(name = "0")
    BUXIAN("code_0", "不限");



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
