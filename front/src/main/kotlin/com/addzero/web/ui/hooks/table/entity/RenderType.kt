package com.addzero.web.ui.hooks.table.entity

import java.sql.JDBCType
import kotlin.reflect.KClass

enum class RenderType {
    // 文本类型
    TEXT,               // 普通文本
    TEXT_PASSWORD,      // 密码
    TEXT_EMAIL,         // 邮箱
    TEXT_PHONE,         // 手机号
    TEXT_URL,           // URL
    TEXT_CODE,          // 代码
    TEXT_HTML,          // HTML
    TEXT_TAG,           // 标签
    TEXT_AREA,          // 长文本

    // 数字类型
    NUMBER,             // 普通数字
    NUMBER_MONEY,       // 金额
    NUMBER_CURRENCY,    // 货币
    NUMBER_PERCENT,     // 百分比
    NUMBER_BAR,         // 进度条

    // 日期类型
    DATE,               // 日期
    DATE_TIME,          // 日期时间

    // 选择类型
    SELECT,             // 单选
    SELECT_MULTI,       // 多选
    SELECT_TREE,        // 树形选择
    SELECT_CASCADE,     // 级联选择
    SELECT_AUTO,        // 自动完成

    // 布尔类型
    BOOL_CHECKBOX,      // 复选框
    BOOL_SWITCH,        // 开关

    // 其他类型
    IMAGE,              // 图片
    FILE,               // 文件
    COMPUTED,           // 计算字段
    RADIO, //视频
    CUSTOM          ;

    companion object {
        fun fromJdbcType(jdbcType: JDBCType): RenderType {
            return when (jdbcType) {
                JDBCType.VARCHAR, JDBCType.CHAR -> TEXT
                JDBCType.LONGVARCHAR, JDBCType.CLOB -> TEXT_AREA
                JDBCType.INTEGER, JDBCType.BIGINT, JDBCType.DECIMAL, JDBCType.FLOAT, JDBCType.DOUBLE, JDBCType.NUMERIC, JDBCType.REAL -> NUMBER
                JDBCType.BOOLEAN, JDBCType.BIT -> BOOL_CHECKBOX
                JDBCType.DATE -> DATE
                JDBCType.TIME, JDBCType.TIMESTAMP -> DATE_TIME
                JDBCType.BLOB, JDBCType.BINARY, JDBCType.VARBINARY, JDBCType.LONGVARBINARY -> IMAGE
                JDBCType.OTHER -> TEXT_CODE
                JDBCType.ARRAY -> SELECT_MULTI
                else -> TEXT
            }
        }

        fun fromKClass(kClass: KClass<*>): RenderType = when (kClass) {
            String::class -> TEXT
            Int::class, Long::class, Float::class, Double::class -> NUMBER
            Boolean::class -> BOOL_CHECKBOX
            java.util.Date::class, java.time.LocalDate::class -> DATE
            java.time.LocalDateTime::class -> DATE_TIME
            List::class, Array::class -> SELECT_MULTI
            else -> TEXT
        }
    }
}