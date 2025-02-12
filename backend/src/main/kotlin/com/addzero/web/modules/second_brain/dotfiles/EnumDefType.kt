package com.addzero.web.modules.second_brain.dotfiles

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import org.babyfish.jimmer.sql.EnumItem

/**
 * /**
 *  定义类型
 *  alias=alias
 *  export=export
 * function=function
 * sh=sh
 * var=var
*/
 *
 * @author AutoDDL
 * @date 2025-02-06 17:25:19
 */
enum class EnumDefType(
    val code: String?,
    val desc: String
) {
    /**
     * alias
     */
    @EnumItem(name = "alias")
    ALIAS("alias", "alias"),

    /**
     * export
     */
    @EnumItem(name = "export")
    EXPORT("export", "export"),

    /**
     * function
     */
    @EnumItem(name = "function")
    FUNCTION("function", "function"),

    /**
     * sh
     */
    @EnumItem(name = "sh")
    SH("sh", "sh"),

    /**
     * var
     */
    @EnumItem(name = "var")
    VAR("var", "var");

    @JsonValue
    fun getValue(): String {
        return desc
    }

    companion object {
        @JsonCreator
        fun fromCode(code: String?): EnumDefType? = entries.find { it.code == code }
    }
}