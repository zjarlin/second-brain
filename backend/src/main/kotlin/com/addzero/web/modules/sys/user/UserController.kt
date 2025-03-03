//package com.addzero.web.modules.sys.user
//
//import cn.hutool.core.util.ReflectUtil
//import com.addzero.web.infra.jimmer.base.BaseController
//import org.babyfish.jimmer.sql.kt.KSqlClient
//import org.babyfish.jimmer.sql.kt.fetcher.newFetcher
//import org.springframework.web.bind.annotation.RequestMapping
//import org.springframework.web.bind.annotation.RestController
//
//fun main() {
//    val mapOf = mapOf("phone" to false, "nickname" to true)
//
//    val creator = newFetcher(SysUser::class)
//    val fetcher = creator.by {}
//    mapOf.forEach({ (key, value) ->
//        if (value) {
//            fetcher.add(key)
//        }
//    })
//    println(fetcher)
//}
//
//@RestController
//@RequestMapping("/user")
//open class UserController : BaseController<SysUser> {
//    override val sql: KSqlClient
//        get() = super.sql
//
//    fun testojdoiasjdo(): Unit {
//        val showPhone = false
//        val newFetcher1 = newFetcher(SysUser::class)
//        val newFetcher = newFetcher1.by {}
//        newFetcher.add("phone")
//        val fieldMap = newFetcher.fieldMap
//        val get = fieldMap.get("phone")
//
//
//        val fields = ReflectUtil.getField(SysUserFetcherDsl::class.java, "fetcher")
//
//        val by = newFetcher1.by {
//            gender()
//            phone(showPhone)
//        }
//
//        val query = sql.createQuery(SysUser::class) {
//            val selection = table.fetchBy {
//                gender()
//                if (showPhone) {
//                    phone()
//                }
//            }
//            select(table.fetch(by))
//        }
//        val result = query.execute()
//        println(result)
//    }
//
//}
////open class UserController : BaseController<SysUser, UserSpec, UserVO>
