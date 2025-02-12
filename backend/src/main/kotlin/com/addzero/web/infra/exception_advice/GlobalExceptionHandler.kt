package com.addzero.web.infra.exception_advice

import com.addzero.web.infra.config.log
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.ErrorResponseException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@ControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {

    @ResponseBody
    @ExceptionHandler(BizException::class)
    fun handleBusinessException(
        exception: BizException,
        request: WebRequest,
    ): ResponseEntity<*> {
        log.error("Business Error Handled ===> ${exception.message}")
        val errorResponseException = ErrorResponseException(
            HttpStatus.INTERNAL_SERVER_ERROR, buildProblemDetail(exception, errorCode = exception.code), exception.cause
        )
        return handleExceptionInternal(
            errorResponseException, null, errorResponseException.headers, errorResponseException.statusCode, request
        )!!
    }

    private fun buildProblemDetail(
        exception: Exception,
        status: HttpStatusCode? = null,
        errorCode: Int? = null,
        detail: String? = null,
    ) = ProblemDetail.forStatusAndDetail(
        status ?: HttpStatus.INTERNAL_SERVER_ERROR, detail ?: exception.message ?: "Internal Server Error"
    ).apply {
        errorCode?.let { setProperty("errorCode", it) }
        setProperty("timestamp", LocalDateTime.now().format(format))
    }

    companion object {
        private val format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
    }
}