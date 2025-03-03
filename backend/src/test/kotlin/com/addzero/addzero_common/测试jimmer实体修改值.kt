package com.addzero.addzero_common

import cn.hutool.core.util.ReflectUtil
import com.addzero.web.modules.sys.area.SysArea
import com.addzero.web.modules.sys.area.city
import org.babyfish.jimmer.ImmutableObjects
import org.babyfish.jimmer.Page
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.babyfish.jimmer.sql.kt.ast.expression.KExpression
import org.babyfish.jimmer.sql.kt.ast.expression.`ilike?`
import org.babyfish.jimmer.sql.kt.ast.table.makeOrders
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class 测试jimmer实体修改值(
    val sql: KSqlClient,
) {


    fun selectArea(
        keyword: String,
        pageNo: Int,
        pageSize: Int
    ): Page<SysArea> {
        val createQuery = com.addzero.common.consts.sql.createQuery(SysArea::class) {
            where(table.city `ilike?` keyword)
            orderBy(table.makeOrders("sid asc"))
            select(table)
        }.fetchPage(pageNo - 1, pageSize)
        return createQuery
    }


    private inline fun <reified E : Any>selectGeneric(
        keyword: String,
        getXxx: KExpression<String>?,
        pageNo: Int,
        pageSize: Int
    ): List<E> {

        val createQuery = sql.createQuery(E::class) {
//            where(getXxx `ilike?` keyword)
            orderBy(table.makeOrders("sid asc"))
            select(table)
        }.fetchPage(pageNo - 1, pageSize)
        val rows = createQuery.rows

        val toList = rows.map {
            val get = ImmutableObjects.get(it, "sid")
            val fieldsValue = ReflectUtil.getFieldValue(it, "__sidValue")
            val fieldsValue11 = ReflectUtil.setFieldValue(it, "__sidValue",
                "111")
//            DraftObjects.set(it,"__sidValue","111")
//            val fieldValue = ReflectUtil.getFieldValue(it, "sid")
//            val fieldValue1 = ReflectUtil.getFieldValue(it, "_sid")

            it
        }.toList()


        return toList
    }



    @Test
    fun test(): Unit {
        val selectGeneric = selectGeneric<SysArea>("", null, 1, 10)

//        val createQuery = selectArea("", 1, 10)
//        val rows = createQuery.rows
//        val toList = rows.map {
//
//
//            val block: SysAreaDraft.() -> Unit = {
//                city = "111"
//            }
//            it.copy(block)
//        }.toList()
        println(selectGeneric)


    }


}


//infix fun <T : Enum<T>> KExpression<T>.`enumValueIn?`(
//    values: Collection<T>?
//): KNonNullExpression<Boolean>? = values?.let {
//    InCollectionPredicate(nullable = false, negative = false, this, it)
//}


//infix fun <T : Enum<T>> KExpression<Collection<T>>.`enumValueIntersection?`(
//    values: Collection<T>?
//): KNonNullExpression<Boolean>? = values?.let {
//    InCollectionPredicate(nullable = false, negative = false, this, it)
//}


//private infix fun  KExpression<String>.contains(target: KExpression<String>): KNonNullExpression<Boolean> {
//    val sql = sql(Boolean::class, "%e like '%'|| %e ||'%'") {
//        expression(this@like)
//        expression(target)
//    }
//    return sql
//}

//class AJoibB<G : Any,B : Any>:KWeakJoin<G,B>(){
//    override fun on(source: KNonNullTable<G>, target: KNonNullTable<B>): KNonNullExpression<Boolean>? {
//        return super.on(source, target)
//    }


//}

