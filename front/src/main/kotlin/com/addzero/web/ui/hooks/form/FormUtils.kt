package com.addzero.web.ui.hooks.form

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.addzero.common.kt_util.toNotBlankStr
import com.addzero.web.ui.hooks.table.entity.AddColumn
import com.addzero.web.ui.hooks.table.entity.RenderType.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


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

