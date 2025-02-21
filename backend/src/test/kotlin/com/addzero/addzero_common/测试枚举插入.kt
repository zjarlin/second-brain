package com.addzero.addzero_common

import com.addzero.web.modules.second_brain.dotfiles.*
import com.addzero.web.modules.second_brain.dotfiles.dto.BizDotfilesSpec
import com.addzero.web.modules.second_brain.tag.BizTag
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import kotlin.test.todo

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class 测试枚举插入(
    val sql: KSqlClient,
) {
    @Test
    fun 测试枚举插入(): Unit {
        val bizDotfiles = BizDotfiles {
           osType = listOf(BizTag { name = "mac" }, BizTag { name = "win" })

            osStructure = Enumplatforms.ARM64
            defType = EnumDefType.FUNCTION
            name = "visual-studio-code"
            value = "oxxxxxxxxxxxx"
            describtion = "vscode编辑器"
            status = EnumStatus.QIYONG
            fileUrl = null
            location = "/Applications/Visual Studio Code.app"
        }
        val bizDotfilesSpec = BizDotfilesSpec(

            )
        val save = sql.save(bizDotfiles)
        println()
    }
    @Test
    fun 测试枚举查询() {
        val execute = sql.createQuery(BizDotfiles::class) {
//            where(table.osStructure `enumValueIn?` enumOsStructures) /*枚举in,  空了不走这个where*/
//            where(table.osType `enumValueIntersection?` enumOsTypes)  /* 枚举有交集的查出来, 空了不走这个where*/
            select(table)
        }.execute()
        println()
    }

//    @Test
//    fun 测试枚举多选查询(): Unit {
//        val enumOsTypes = listOf(EnumOsType.LINUX, EnumOsType.MAC)
//        val listOf1 = listOf(EnumOsType.MAC)
//        val listOf2 = listOf(EnumOsType.WIN)
//        val listOf3 = listOf(EnumOsType.LINUX)
//
//
//        val enumOsStructures = listOf(EnumOsStructure.ARM64, EnumOsStructure.X86)
//        val execute = sql.createQuery(BizDotfiles::class) {
//            where(table.osStructure `enumValueIn?` enumOsStructures) /*枚举in,  空了不走这个where*/
//            where(table.osType `enumValueIntersection?` enumOsTypes)  /* 枚举有交集的查出来, 空了不走这个where*/
//            select(table)
//        }.execute()
//        println()
//    }


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

