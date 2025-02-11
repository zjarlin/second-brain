//package com.addzero.web.infra.exception_advice
//
//import Res
//import cn.dev33.satoken.exception.DisableServiceException
//import cn.dev33.satoken.exception.NotLoginException
//import cn.dev33.satoken.exception.NotRoleException
//import com.addzero.web.infra.config.log
//import com.addzero.web.infra.jimmer.base.exhandler.ExceptionStrategyExecutor
//import fail
//import failByCode
//import jakarta.validation.ConstraintViolationException
//import org.babyfish.jimmer.sql.exception.ExecutionException
//import org.springframework.core.annotation.Order
//import org.springframework.http.HttpStatus
//import org.springframework.http.ResponseEntity
//import org.springframework.jdbc.core.JdbcTemplate
//import org.springframework.web.bind.MethodArgumentNotValidException
//import org.springframework.web.bind.annotation.ExceptionHandler
//import org.springframework.web.bind.annotation.RestControllerAdvice
//
//@RestControllerAdvice
//@Order(2)
//class SatokenExceptionAdvice() {
//
//
//
//
//
//    @ExceptionHandler(NotLoginException::class)
//    fun handleNotLogin(e: NotLoginException?): ResponseEntity<Res<Nothing>> {
//        log.error("未登录", e)
//
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorEnum.USER_NOT_LOGGED_IN.fail())
//    }
//
//    @ExceptionHandler(NotRoleException::class)
//    fun handleNotRole(e: NotRoleException): ResponseEntity<Res<Nothing>> {
//        log.error("角色校验异常", e)
//
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorEnum.UNAUTHORIZED.fail())
//    }
//
//    @ExceptionHandler(DisableServiceException::class)
//    fun handleDisabledException(e: DisableServiceException?): ResponseEntity<Res<Nothing>> {
//        log.error("账号封禁", e)
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorEnum.ACCOUNT_BANNED.fail())
//
//    }
//
//
//}