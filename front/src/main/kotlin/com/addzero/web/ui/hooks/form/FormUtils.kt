package com.addzero.web.ui.hooks.form

import com.addzero.web.ui.hooks.table.entity.AddColumn
import com.addzero.web.ui.hooks.table.entity.RenderType
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * 根据列配置和数据推断渲染类型
 */
fun <E : Any> inferRenderType(column: AddColumn<E>, data: E): RenderType {
    // 如果明确指定了渲染类型，则使用指定的类型
    if (column.renderTypeOverride != null) {
        return column.renderTypeOverride
    }

    // 获取渲染类型
    val renderType = column.getRenderType(data)

    // 如果已经有明确的渲染类型，则直接返回
    if (renderType != RenderType.CUSTOM) {
        return renderType
    }

    // 根据字段名称推断
    val fieldName = column.title.lowercase()
    return when {
        fieldName.contains("url") || fieldName.contains("file") || fieldName.contains("image") -> RenderType.IMAGE
        fieldName.contains("date") && !fieldName.contains("time") -> RenderType.DATE
        fieldName.contains("time") || fieldName.contains("datetime") -> RenderType.DATETIME
        fieldName.contains("description") || fieldName.contains("content") || fieldName.contains("text") -> RenderType.TEXTAREA
        else -> RenderType.TEXT
    }
}

/**
 * 日期格式化工具函数
 */
fun formatDate(value: Any?): String {
    return when (value) {
        is LocalDate -> value.format(DateTimeFormatter.ISO_LOCAL_DATE)
        is LocalDateTime -> value.toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE)
        is java.util.Date -> java.time.Instant.ofEpochMilli(value.time)
            .atZone(java.time.ZoneId.systemDefault())
            .toLocalDate()
            .format(DateTimeFormatter.ISO_LOCAL_DATE)
        else -> value?.toString() ?: ""
    }
}

/**
 * 日期时间格式化工具函数
 */
fun formatDateTime(value: Any?): String {
    return when (value) {
        is LocalDateTime -> value.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        is LocalDate -> value.atStartOfDay().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        is java.util.Date -> java.time.Instant.ofEpochMilli(value.time)
            .atZone(java.time.ZoneId.systemDefault())
            .toLocalDateTime()
            .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        else -> value?.toString() ?: ""
    }
}

/**
 * 解析日期字符串
 */
fun parseDate(dateStr: String): LocalDate? {
    return try {
        LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE)
    } catch (e: Exception) {
        null
    }
}

/**
 * 解析日期时间字符串
 */
fun parseDateTime(dateTimeStr: String): LocalDateTime? {
    return try {
        LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    } catch (e: Exception) {
        null
    }
}