package com.addzero.web.modules.ai.functioncalling.`fun`

import com.addzero.web.modules.ai.functioncalling.entity.FunctionCallingEmptyRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Description
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.function.Function

/**
 * 通过@Description描述函数的用途，这样ai在多个函数中可以根据描述进行选择。
 *
 * @author zjarlin
 */
@Configuration
class DayFunction {
    @Bean
    @Description("日期函数")
    fun dateFun(): Function<FunctionCallingEmptyRequest, LocalDate> {
        return Function { LocalDate.now() }
    }

    @Bean
    @Description("日期时间函数")
    fun datetimeFun(): Function<FunctionCallingEmptyRequest, LocalDateTime> {
        return Function { LocalDateTime.now() }
    }
}