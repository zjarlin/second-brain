package com.addzero.web.modules.second_brain.dotfiles

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

/**
 * /**
 *  操作系统
 *  1=win
 * 2-linux
 *3 mac
 *null.不限
*/
 *
 * @author AutoDDL
 * @date 2025-01-27 14:40:42
 */
enum class EnumOsType(
    val code: String?,
    val desc: String
) {

    UNIX("3", "unix"),

    /**
     * win
     */

    WIN("1", "win"),

    /**
     * linux
     */

    LINUX("2", "linux"),

    /**
     * mac
     */

    MAC("3", "mac"),

    /**
     * 不限
     */

    BUXIAN(null, "不限");

    @JsonValue
    fun getValue(): String {
        return desc
    }

    companion object {
        @JsonCreator
        fun fromCode(code: String?): EnumOsType? = values().find { it.code == code }
    }
}