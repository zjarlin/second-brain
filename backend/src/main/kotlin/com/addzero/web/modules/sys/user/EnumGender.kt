package com.addzero.web.modules.sys.user

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import org.babyfish.jimmer.sql.EnumItem

/**
 * /**
 * 性别  0：男
1=女
2-未知
*/
 *
 * @author AutoDDL
 * @date 2025-01-13 21:36:51
 */
enum class EnumGender(
    val code: String,
    val desc: String
) {
    /**
     * 男
     */
    @EnumItem(name = "0")
    NAN("0", "男"),

    /**
     * 女
     */
    @EnumItem(name = "1")
    NU("1", "女"),

    /**
     * 未知
     */
    @EnumItem(name = "2")
    WEIZHI("2", "未知");

    @JsonValue
    fun getValue(): String {
        return desc
    }

    companion object {
        @JsonCreator
        fun fromCode(code: String): EnumGender? = values().find { it.code == code }
    }
}