package com.addzero.web.infra.jimmer.base.exhandler

import cn.hutool.core.util.StrUtil
import com.addzero.common.kt_util.isBlank
import com.addzero.common.kt_util.removeBlankOrQuotation
import com.addzero.common.util.metainfo.MetaInfoUtils.extractTableName
import com.addzero.web.infra.config.log
import fail
import org.babyfish.jimmer.sql.exception.ExecutionException
import org.babyfish.jimmer.sql.exception.SaveException
import org.postgresql.util.PSQLException
import org.springframework.core.annotation.Order
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import success

@RestControllerAdvice
@Order(1) // 优先级最高
class JimmerExceptionAdvice(

    private  val exceptionStrategyExecutor: ExceptionStrategyExecutor
) {
    @ExceptionHandler(SaveException.NeitherIdNorKey::class)
    fun handleException(e: SaveException.NeitherIdNorKey): Any? {
        log.error(e.message)
        val s =
            "不能接受既没有id也没有键的实体。有三种方法可以解决这个问题：1。为保存对象指定id属性“id”；2.使用注解“org.babyfish.jimmer.sql.Key”修饰实体类型中的一些标量或外键属性，或调用save命令的“setKeyProps”，指定“com.addzero.web.modules.dotfiles.BizEnvVars”的键属性，最后指定保存对象的键属性值；3.将save命令的聚合根保存模式指定为“INSERT_ONLY”（函数已更改）、“INSERT_IF_ABSENT”（函数更改）或“NON_IDEMPOTENT_UPSERT”"
        return s.fail()
    }

    @ExceptionHandler(PSQLException::class)
    fun handleExceptiois(e: PSQLException):Any ? {
        e.printStackTrace()
        val s = e.message
        val cause = e.cause?.message
        val handleException = exceptionStrategyExecutor.handleException(s, cause)
//        log.error(e)
        val success = "已自动添加列".success()
        return success
    }



}