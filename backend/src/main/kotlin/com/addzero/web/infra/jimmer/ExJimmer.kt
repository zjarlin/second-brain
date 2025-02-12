package com.addzero.web.infra.jimmer

import cn.hutool.extra.spring.SpringUtil
import org.apache.poi.ss.formula.functions.T
import org.babyfish.jimmer.sql.kt.KSqlClient
import kotlin.reflect.KClass

fun <T : Any> KSqlClient.list(entityType: KClass<T>): List<T> {
    val bean = SpringUtil.getBean(KSqlClient::class.java)
    val createQuery = bean.createQuery(entityType) {
        val select = select(
            table
        )
        where()
        select
    }
    val execute1 = createQuery.execute()
    return execute1
}

object ExJimmer {}