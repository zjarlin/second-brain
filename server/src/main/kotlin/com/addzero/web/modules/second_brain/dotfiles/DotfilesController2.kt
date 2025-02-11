package com.addzero.web.modules.second_brain.dotfiles

import cn.idev.excel.FastExcel
import cn.idev.excel.cache.Ehcache.BATCH_COUNT
import cn.idev.excel.context.AnalysisContext
import cn.idev.excel.read.listener.ReadListener
import cn.idev.excel.util.ListUtils
import com.addzero.common.kt_util.isNotEmpty
import com.addzero.common.kt_util.isNotNew
import com.addzero.web.infra.upload.DownloadUtil
import com.addzero.web.modules.second_brain.dotfiles.dto.BizDotfilesSaveDTO
import com.addzero.web.modules.second_brain.dotfiles.dto.BizDotfilesSpec
import com.addzero.web.modules.second_brain.dotfiles.dto.BizDotfilesUpdateDTO
import com.addzero.web.modules.second_brain.dotfiles.dto.BizDotfilesView
import io.swagger.v3.oas.annotations.Operation
import org.babyfish.jimmer.Page
import org.babyfish.jimmer.sql.ast.mutation.AffectedTable
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.babyfish.jimmer.sql.kt.ast.table.makeOrders
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/dotfiles")
class DotfilesController2(
    private val sql: KSqlClient
) {
    @GetMapping("/page")
    @Operation(summary = "分页查询")
    fun page(
        spec: BizDotfilesSpec,
        @RequestParam(defaultValue = "0") pageNum: Int,
        @RequestParam(defaultValue = "10") pageSize: Int,
    ): Page<BizDotfilesView> {
        val createQuery = sql.createQuery(BizDotfiles::class) {
            where(spec)
            orderBy(table.makeOrders("id desc"))
            select(
                table.fetch(BizDotfilesView::class)
            )
        }
        val fetchPage = createQuery.fetchPage(pageNum, pageSize)
        return fetchPage
    }

    @GetMapping("listAll")
    @Operation(summary = "查询所有")
    fun list(): List<BizDotfilesView> {
        val createQuery = sql.createQuery(BizDotfiles::class) {
            select(table.fetch(BizDotfilesView::class))
        }
        val execute = createQuery.execute()
        return execute
    }

    @PostMapping("/saveBatch")
    @Operation(summary = "批量保存")
    fun saveBatch(
        @RequestBody input: List<BizDotfilesSaveDTO>,
    ): Int {
        val toList = input.map { it.toEntity() }.toList()
        val saveEntities = sql.saveEntities(toList)
        return saveEntities.totalAffectedRowCount
    }

    /**
     * id查询单条
     */
    @GetMapping("/findById")
    @Operation(summary = "id查询单条")
    fun findById(id: String): BizDotfiles? {
        val byId = sql.findById(BizDotfiles::class, id)
        return byId
    }

    /**
     *批量删除
     */
    @DeleteMapping("/delete")
    @Operation(summary = "批量删除")
    fun deleteByIds(@RequestParam vararg ids: String): Int {
        val affectedRowCountMap = sql.deleteByIds(
            BizDotfiles::class, listOf(*ids)
        ).totalAffectedRowCount
        return affectedRowCountMap
    }

    @PostMapping("/save")
    @Operation(summary = "保存")
    fun save(@RequestBody inputDTO: BizDotfilesSaveDTO): Int {
        val modifiedEntity = sql.save(inputDTO).totalAffectedRowCount
        return modifiedEntity
    }

    @PostMapping("/update")
    @Operation(summary = "编辑")
    fun edit(@RequestBody inputDTO: BizDotfilesUpdateDTO): Int {
        val update = sql.update(inputDTO).totalAffectedRowCount
        return update
    }


    @PostMapping("/import")
    fun import(@RequestPart file: MultipartFile): Map<AffectedTable, Int> {
        val dataDtoList = file.inputStream.use {
            val readListener = object : ReadListener<BizDotfilesExcelDTO> {
                /**
                 * 缓存的数据
                 */
                val caches: MutableList<BizDotfilesExcelDTO> = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT)

                /**
                 * 这个每一条数据解析都会来调用
                 */
                override fun invoke(input: BizDotfilesExcelDTO, context: AnalysisContext?) {
                    caches.add(input)
                    // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
                    if (caches.size >= BATCH_COUNT) {
                        // 存储完成清理 list
                        caches.clear()
                    }
                }

                /**
                 * 所有数据解析完成了 都会来调用
                 *
                 * @param context
                 */
                override fun doAfterAllAnalysed(context: AnalysisContext?) {
                    // 这里也要保存数据，确保最后遗留的数据也存储到数据库
                    println("所有数据解析完成")
                }
            }
            val excelWriteDTOCLASS = BizDotfilesExcelDTO::class.java
            FastExcel.read(it, excelWriteDTOCLASS, readListener).sheet().doRead()
            val caches = readListener.caches
            caches
        }

        val map = dataDtoList.filter { it.isNotEmpty() }.filter { it.isNotNew() }.map { it.toEntity() }
        val totalAffectedRowCount = sql.saveEntities(map).affectedRowCountMap
        return totalAffectedRowCount
    }

    @GetMapping("/export")
    fun exportExcel(
        fileName: String, spec: BizDotfilesSpec, sheetName: String = "sheet1"
    ) {
        val data = sql.createQuery(BizDotfiles::class) {
            where(spec)
            select(table.fetchBy {
                allTableFields()
            })
        }.execute()

        val toList = data.map { it.toExcelDTO() }.toList()

        DownloadUtil.downloadExcel(fileName) {
            FastExcel.write(it, BizDotfilesExcelDTO::class.java).sheet(sheetName).doWrite(toList)
        }
    }

}

