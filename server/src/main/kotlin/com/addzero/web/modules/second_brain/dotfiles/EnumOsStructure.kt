package com.addzero.web.modules.second_brain.dotfiles

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import org.babyfish.jimmer.sql.EnumItem

/**
 * /**
 *
 *  系统架构
 *  arm64=arm64
 *  x86=x86
*/
 *
 * @author AutoDDL
 * @date 2025-02-06 17:23:17
 */
enum class EnumOsStructure(
    val code: String?,
    val desc: String
) {
    /**
     * arm64
     */
    @EnumItem(name = "arm64")
    ARM64("arm64", "arm64"),

    /**
     * x86
     */
    @EnumItem(name = "x86")
    X86("x86", "x86");

    @JsonValue
    fun getValue(): String {
        return desc
    }

    companion object {
        @JsonCreator
        fun fromCode(code: String?): EnumOsStructure? = values().find { it.code == code }
    }
}