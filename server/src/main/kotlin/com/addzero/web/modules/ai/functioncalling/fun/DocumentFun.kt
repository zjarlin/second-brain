package com.addzero.web.modules.ai.functioncalling.`fun`

import cn.hutool.core.io.FileUtil
import com.addzero.web.modules.ai.functioncalling.entity.FunctionCallingResponse
import com.fasterxml.jackson.annotation.JsonPropertyDescription
import org.springframework.ai.reader.tika.TikaDocumentReader
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Description
import org.springframework.core.io.FileSystemResource
import java.util.function.Function

/**
 * 通过@Description描述函数的用途，这样ai在多个函数中可以根据描述进行选择。
 *
 * @author zjarlin
 */
@Configuration
class DocumentFun {
    @Bean
    @Description("解析文档内容")
    fun docAnalysis(): Function<Request, FunctionCallingResponse> {
        val requestResponseFunction = Function { request: Request ->
            // ai解析用户的提问得到path参数，使用tika读取本地文件获取内容。把读取到的内容再返回给ai作为上下文去回答用户的问题。
            val path = request.path
            if (FileUtil.isDirectory(path)) {
                return@Function FunctionCallingResponse("路径必须是文件，不能是目录.不满足function call无需function calling 正常响应即可")
            }
            val tikaDocumentReader = TikaDocumentReader(FileSystemResource(path))
            val read = tikaDocumentReader.read()
            FunctionCallingResponse(read[0].content)
        }
        return requestResponseFunction
    }

    data class Request(@field:JsonPropertyDescription("需要解析的文件路径") val path: String)
}