package com.addzero.web.modules.second_brain.dotfiles
import com.addzero.common.anno.Shit
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
     * function
     */
    @EnumItem(name = "3")
    FUNCTION("3", "function"),

    /**
     * sh
     */
    @EnumItem(name = "4")
    SH("4", "sh"),

    /**
     * var
     */
    @EnumItem(name = "5")
    VAR("5", "var");

    @JsonValue
    fun getValue(): String {
        return desc
    }

    companion object {
        @JsonCreator
        fun fromCode(code: String?): EnumDefType? = entries.find { it.code == code }
        fun fromDesc(desc: String?): EnumDefType? = entries.find { it.desc == desc }
    }
}
