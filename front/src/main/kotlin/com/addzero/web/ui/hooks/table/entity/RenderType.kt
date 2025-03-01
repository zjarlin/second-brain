package com.addzero.web.ui.hooks.table.entity

import java.sql.JDBCType
import kotlin.reflect.KClass

enum class RenderType {
    TEXT,
    NUMBER,
    TEXTAREA,
    DATE,
    DATETIME,
    IMAGE,
    LINK,
    SELECT,
    MULTISELECT,
    CHECKBOX,
    RADIO,
    SWITCH,
    TAG,
    CODE,
    HTML,
    MONEY,
    CURRENCY,
    PERCENT,
    BAR,
    TREE,
    COMPUTED,
    CUSTOM;

    companion object {
        fun fromJdbcType(jdbcType: JDBCType): RenderType {
            return when (jdbcType) {
                JDBCType.VARCHAR, JDBCType.CHAR -> TEXT
                JDBCType.LONGVARCHAR, JDBCType.CLOB -> TEXTAREA
                JDBCType.INTEGER, JDBCType.BIGINT, JDBCType.DECIMAL, JDBCType.FLOAT, JDBCType.DOUBLE, JDBCType.NUMERIC, JDBCType.REAL -> NUMBER
                JDBCType.BOOLEAN, JDBCType.BIT -> SWITCH
                JDBCType.DATE -> DATE
                JDBCType.TIME, JDBCType.TIMESTAMP -> DATETIME
                JDBCType.BLOB, JDBCType.BINARY, JDBCType.VARBINARY, JDBCType.LONGVARBINARY -> IMAGE
                JDBCType.OTHER -> CODE
                JDBCType.ARRAY -> MULTISELECT
                else -> TEXT
            }
        }

        fun fromKClass(kClass: KClass<*>): RenderType = when (kClass) {
            String::class -> TEXT
            Int::class, Long::class, Float::class, Double::class -> NUMBER
            Boolean::class -> SWITCH
            java.util.Date::class, java.time.LocalDate::class -> DATE
            java.time.LocalDateTime::class -> DATETIME
            List::class, Array::class -> MULTISELECT
            else -> TEXT
        }
    }
}