package com.addzero.web.infra.exception_advice

import Res
import com.addzero.web.infra.config.log
import fail
import failByCode
import jakarta.validation.ConstraintViolationException
import org.babyfish.jimmer.sql.exception.ExecutionException
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@Order(2)
class GlobalExceptionAdvice() {


//    @ExceptionHandler(BizException::class)
//    fun handleBusinessException(e: BizException): ResponseEntity<Res<Nothing>> {
//        log.error("业务异常", e)
//        val errorEnum = ErrorEnum.BUSINESS_LOGIC_ERROR
//        val fail = errorEnum.fail()
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(fail)
//    }


    @ExceptionHandler(ConstraintViolationException::class)
    fun handleValidateException(e: ConstraintViolationException): ResponseEntity<Res<Nothing>> {
        log.warn("校验异常", e)
        // 不合格的字段，可能有多个，只需要返回其中一个提示用户就行
        // 比如密码为空
        val constraintViolations = ArrayList(e.constraintViolations)
        val message = constraintViolations[0].message
        val code = ErrorEnum.INVALID_PARAMETER.code
        val failByCode = message.failByCode(code)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(failByCode)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidateExceptionForSpring(e: MethodArgumentNotValidException): ResponseEntity<Res<Nothing>> {
        log.warn("校验异常", e)
        val code = ErrorEnum.INVALID_PARAMETER.code
        val defaultMessage = e.bindingResult.allErrors[0].defaultMessage
        val failByCode = defaultMessage?.failByCode(code)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(failByCode)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception?): ResponseEntity<Res<Nothing>> {
        log.error("系统异常", e)
//        val message = e?.message
        val message = e?.message
        val stackTrace = e?.cause?.message ?: message
        val fail = stackTrace?.fail()
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(fail)
    }
}