package com.addzero.web.modules.ai.config.clients

import com.alibaba.fastjson2.JSON
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse
import org.springframework.util.StreamUtils
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.stream.Collectors

/**
 * HTTP interceptor for logging requests and responses.
 */
class HttpLoggingInterceptor(private val logRequests: Boolean, private val logResponses: Boolean) :
    ClientHttpRequestInterceptor {
    @Throws(IOException::class)
    override fun intercept(
        request: HttpRequest, requestBody: ByteArray, execution: ClientHttpRequestExecution
    ): ClientHttpResponse {
        if (logRequests) {
            logRequest(request, requestBody)
        }

        return if (logResponses) {
            logResponse(request, requestBody, execution)
        } else {
            execution.execute(request, requestBody)
        }
    }

    private fun logRequest(request: HttpRequest, requestBody: ByteArray) {
        val entries: Set<Map.Entry<String, String>> = request.headers.toSingleValueMap().entries
        val s = String(requestBody, StandardCharsets.UTF_8)

        val logRequestBody = JSON.parseObject(s, LogRequestBody::class.java)

        //        List<LogRequestBody.MessagesDTO> messages = logRequestBody.getMessages();
        logger.info("Request.\n Method: {}.\n URI: {}.\n Headers: {}.\n Body: {}",
            request.method,
            request.uri,
            entries.stream()
                .filter { e: Map.Entry<String, String> -> e.key != HttpHeaders.AUTHORIZATION } //                            .filter(e -> !e.getKey().equals("images"))

                .map { e: Map.Entry<String, String> -> e.key + ":" + e.value }.collect(Collectors.joining(", ")),
            logRequestBody
        )
    }

    @Throws(IOException::class)
    private fun logResponse(
        request: HttpRequest, requestBody: ByteArray, execution: ClientHttpRequestExecution
    ): ClientHttpResponse {
        val response = execution.execute(request, requestBody)
        val responseBody = StreamUtils.copyToString(response.body, StandardCharsets.UTF_8)

        logger.info(
            "Response.\n Status Code: {}.\n Headers: {}.\n Body: {}",
            response.statusText,
            response.headers.toSingleValueMap().entries.stream()
                .map { e: Map.Entry<String, String> -> e.key + ":" + e.value }.collect(Collectors.joining(", ")),
            responseBody
        )

        return response
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(HttpLoggingInterceptor::class.java)
    }
}