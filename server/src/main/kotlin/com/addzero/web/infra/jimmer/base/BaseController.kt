package com.addzero.web.infra.jimmer.base

import cn.hutool.core.util.TypeUtil
import cn.hutool.extra.spring.SpringUtil
import com.addzero.web.infra.constant.RestConsts.deleteUrl
import com.addzero.web.infra.constant.RestConsts.listAllUrl
import com.addzero.web.infra.constant.RestConsts.saveUrl
import com.addzero.web.infra.constant.RestConsts.updateUrl
import com.addzero.web.infra.jimmer.list
import io.swagger.v3.oas.annotations.Operation
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.springframework.web.bind.annotation.*
import kotlin.reflect.KClass

interface BaseController<
        T : Any,
//, Spec : KSpecification<T>
//, V : View<T>
//        ,K Any
//        ,Any
        > {

    //interface Noting
    val idName: String
        get() {
            return "id"
        }

    // 懒加载 sqlClient，确保只初始化一次并缓存结果
    val sql: KSqlClient get() = lazySqlClient
//    val fetcher: Fetcher<T> get() = newFetcher(CLASS()).by{
//        allScalarFields()
//    }

    fun CLASS(): KClass<T> {
        val typeArgument = TypeUtil.getTypeArgument(this.javaClass, 0)
        val type = typeArgument as Class<T>
        return type.kotlin
    }

//    fun VCLASS(): KClass<V> {
//        val typeArgument = TypeUtil.getTypeArgument(this.javaClass, 2)
//        val type = typeArgument as? Class<V> ?: NothingView::class.java
//        return type.kotlin as KClass<V>
//    }

//    @GetMapping("/page")
//    @Operation(summary = "分页查询")
//    fun page(
//        spec: T,
//        @RequestParam(defaultValue = "1") pageNum: Int,
//        @RequestParam(defaultValue = "10") pageSize: Int,
//    ): Page<*> {
//        var pageNum = pageNum
//        // 这里需要实现分页查询逻辑
//        // 示例代码省略
//        pageNum -= 1
//        val createQuery = sql.createQuery(CLASS()) {
//            orderBy(table.makeOrders("$idName desc"))
//            select(
//                table
//            )
//        }
//        val fetchPage = createQuery.fetchPage(pageNum, pageSize, con = null)
//        return fetchPage
//    }

    @GetMapping(listAllUrl)
    @Operation(summary = "查询所有")
    fun list(
    ): List<Any> {
        val entityType = CLASS()
        val execute1 = sql.list(entityType)
        return execute1
    }


    @PostMapping("/saveBatch")
    @Operation(summary = "批量保存")
    fun saveBatch(
        @RequestBody input: List<T>,
    ): Int {
        val saveEntities = sql.saveEntities(input)
        return saveEntities.totalAffectedRowCount
    }


    @GetMapping("/findById")
    @Operation(summary = "id查询单条")
    fun findById(id: String): T? {
        val byId = sql.findById(CLASS(), id)
        return byId
    }

    @DeleteMapping(deleteUrl)
    @Operation(summary = "批量删除")
    fun deleteByIds(@RequestParam vararg ids: String): Int {
        val affectedRowCountMap = sql.deleteByIds(CLASS(), listOf(*ids)).totalAffectedRowCount
        return affectedRowCountMap
    }

    @PostMapping(saveUrl)
    @Operation(summary = "保存")
    fun save(@RequestBody input: T): Int {
        val modifiedEntity = sql.save(input).totalAffectedRowCount
        return modifiedEntity
    }

    @PutMapping(updateUrl)
    @Operation(summary = "编辑")
    fun edit(@RequestBody e: T): Int {
        val update = sql.update(e).totalAffectedRowCount
        return update
    }


    companion object {
        private val lazySqlClient: KSqlClient by lazy {
            SpringUtil.getBean(KSqlClient::class.java)
        }
    }
}