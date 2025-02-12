package com.addzero.web.infra.jimmer.base

import cn.hutool.core.util.TypeUtil
import cn.hutool.extra.spring.SpringUtil
import com.addzero.web.infra.constant.RestConsts
import com.addzero.web.infra.jimmer.base.pagefactory.PageResult
import com.addzero.web.infra.jimmer.base.pagefactory.createPageFactory
import io.swagger.v3.oas.annotations.Operation
import org.babyfish.jimmer.Input
import org.babyfish.jimmer.View
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.babyfish.jimmer.sql.kt.ast.query.specification.KSpecification
import org.babyfish.jimmer.sql.kt.ast.table.makeOrders
import org.springframework.web.bind.annotation.*
import kotlin.reflect.KClass


interface BaseCrudController<T : Any, Spec : KSpecification<T>, SaveInputDTO : Input<T>, UpdateInputDTO : Input<T>, V : View<T>> {

   private val idName: String
        get() = "id"

    // 懒加载 sqlClient，确保只初始化一次并缓存结果
    val sql: KSqlClient get() = lazySqlClient

    @GetMapping("/page")
    @Operation(summary = "分页查询")
    fun page(
        spec: Spec,
        @RequestParam(defaultValue = "0") pageNum: Int,
        @RequestParam(defaultValue = "10") pageSize: Int,
    ): PageResult<V> {
//        var pageNum = pageNum
        // 这里需要实现分页查询逻辑
        // 示例代码省略
//        pageNum -= 1
        val createQuery = sql.createQuery(CLASS()) {
            where(spec)
            orderBy(table.makeOrders("$idName desc"))
            select(
                table.fetch(VCLASS())
            )
        }
        val createPageFactory = createPageFactory<V>()

        val fetchPage = createQuery.fetchPage(pageNum, pageSize,null,createPageFactory)
//        val fetchPage = createQuery.fetchSpringPage(pageNum, pageSize)
        return fetchPage
    }

    @GetMapping(RestConsts.listAllUrl)
    @Operation(summary = "查询所有")
    fun list(
    ): List<Any> {
        val createQuery = sql.createQuery(CLASS()) {
            select(table.fetch(VCLASS()))
        }
        val execute = createQuery.execute()
        return execute
    }


    @PostMapping("/saveBatch")
    @Operation(summary = "批量保存")
    fun saveBatch(
        @RequestBody input: List<SaveInputDTO>,
    ): Int {
        val toList = input.map { it.toEntity() }.toList()
        val saveEntities = sql.saveEntities(toList)
        return saveEntities.totalAffectedRowCount
    }

    @GetMapping("/findById")
    @Operation(summary = "id查询单条")
    fun findById(id: String): T? {
        val byId = sql.findById(CLASS(), id)
        return byId
    }

    @DeleteMapping(RestConsts.deleteUrl)
    @Operation(summary = "批量删除")
    fun deleteByIds(@RequestParam vararg ids: String): Int {
        val affectedRowCountMap = sql.deleteByIds(CLASS(), listOf(*ids)).totalAffectedRowCount
        return affectedRowCountMap
    }

    @PostMapping(RestConsts.saveUrl)
    @Operation(summary = "保存")
    fun save(@RequestBody inputDTO: SaveInputDTO): Int {
        val modifiedEntity = sql.save(inputDTO).totalAffectedRowCount
        return modifiedEntity
    }

    @PostMapping(RestConsts.updateUrl)
    @Operation(summary = "编辑")
    fun edit(@RequestBody inputDTO: UpdateInputDTO): Int {
        val update = sql.update(inputDTO).totalAffectedRowCount
        return update
    }

    companion object {
        private val lazySqlClient: KSqlClient by lazy {
            SpringUtil.getBean(KSqlClient::class.java)
        }
    }

    fun CLASS(): KClass<T> {
        return (TypeUtil.getTypeArgument(javaClass, 0) as Class<T>).kotlin
    }

    fun SpecCLASS(): KClass<Spec> {
        return (TypeUtil.getTypeArgument(javaClass, 1) as Class<Spec>).kotlin
    }

    fun SaveInputDTOCLASS(): KClass<SaveInputDTO> {
        return (TypeUtil.getTypeArgument(javaClass, 2) as Class<SaveInputDTO>).kotlin
    }

    fun UpdateInputDTOCLASS(): KClass<UpdateInputDTO> {
        return (TypeUtil.getTypeArgument(javaClass, 3) as Class<UpdateInputDTO>).kotlin
    }

    fun VCLASS(): KClass<V> {
        return (TypeUtil.getTypeArgument(javaClass, 4) as Class<V>).kotlin
    }
}