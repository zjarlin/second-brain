package com.addzero.web.modules.ai.functioncalling.`fun`

import com.addzero.web.modules.ai.graphrag.entity.GraphPO
import com.addzero.web.modules.ai.util.ai.ai_abs_builder.toGraphQuestion
import com.fasterxml.jackson.annotation.JsonPropertyDescription
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Description
import java.util.function.Function

/**
 * 通过@Description描述函数的用途，这样ai在多个函数中可以根据描述进行选择。
 *
 * @author zjarlin
 */
@Configuration

class GraphFun {
    @Bean
    @Description("解析文本为知识图谱")
    fun graphRagFun(): Function<Request, GraphPO> {
        val requestResponseFunction: Function<Request, GraphPO> = Function { request: Request ->
            // ai解析用户的提问得到path参数，使用tika读取本地文件获取内容。把读取到的内容再返回给ai作为上下文去回答用户的问题。
            request.text.toGraphQuestion()
        }
        return requestResponseFunction
    }

    data class Request(@field:JsonPropertyDescription("需要解析为知识图谱的文本") val text: String)
}