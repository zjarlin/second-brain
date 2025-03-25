package com.addzero.web.infra.jimmer

import cn.hutool.core.bean.BeanUtil
import cn.hutool.extra.spring.SpringUtil
import com.addzero.web.infra.jackson.toJson
import com.alibaba.fastjson2.parseObject
import org.babyfish.jimmer.DraftObjects
import org.babyfish.jimmer.ImmutableObjects
import org.babyfish.jimmer.meta.ImmutableType
import org.babyfish.jimmer.runtime.Internal
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.babyfish.jimmer.sql.kt.ast.query.specification.KSpecification
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

fun <E : Any> E?.copy(fieldName: String, value: Any?): E? = this?.let { entity ->
    val copy = this.copy {
        DraftObjects.set(it, fieldName, value)
    }
    return copy
}

fun <E : Any> E?.copy(block: (Any) -> Unit = {}): E? = this?.let { entity ->
    Internal.produce(ImmutableType.get(entity.javaClass), entity) { d ->
        block(d)
        d
    } as E
}

fun <E : Any> E.fromMap(updates: Map<String, Any?>, block: (String, Any?) -> Unit = { _, _ -> }): E? {
    val newItem = this.copy { draft ->
        updates.forEach { (fieldName, value) ->
            DraftObjects.set(draft, fieldName, value)
            block(fieldName, value)
        }
    }
    return newItem
}

fun <E : Any> E?.toMap(): MutableMap<String, Any>? {
    if (this == null) {
        return null
    }
    val beanToMap = BeanUtil.beanToMap(this, false, true)
    return beanToMap
}


inline fun <E : Any, reified Spec : KSpecification<E>> Spec.fromMap(updates: Map<String, Any?>): Spec? {
    val jsonObject = this.toJson().parseObject()
    jsonObject.putAll(updates)
    val fromString = ImmutableObjects.fromString(
        Spec::class.java,jsonObject
            .toJson()
    )
    return fromString


}