package com.addzero.common.util.excel

import cn.hutool.core.collection.CollUtil
import cn.hutool.core.io.FileUtil
import cn.hutool.core.io.resource.ClassPathResource
import cn.hutool.core.util.IdUtil
import cn.idev.excel.EasyExcel
import cn.idev.excel.enums.WriteDirectionEnum
import cn.idev.excel.write.metadata.fill.FillConfig
import com.addzero.web.infra.spring.SprCtxUtil
import jakarta.servlet.ServletOutputStream
import org.apache.commons.collections4.map.LinkedMap
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.*

private fun extracted() {
    val sheet1Data = getSheet1Data()
    val sheet2Data = getSheet2Data()
    val stream = ClassPathResource("temp.xlsx").stream
    val outFile = "/Users/zjarlin/Desktop/${IdUtil.getSnowflakeNextIdStr()}" + ".xlsx"
    val outputStream1 = FileUtil.getOutputStream(outFile)
    templateFill(sheet1Data, sheet2Data, stream, outputStream1)
}

fun main() {
    extracted()
}

private fun getSheet1Data(): Map<String, String> {
    val mapOf = mapOf("name" to "zhangsan", "age" to "17", "userName" to "自来也", "sex" to "男")
    return mapOf
}

private fun getSheet2Data(): List<Map<String, String>>? {
    return listOf(
        mapOf("deptName" to "研发一部", "groupName" to "一部小组1", "userName" to "自来也", "sex" to "男"), mapOf("deptName" to "研发二部", "groupName" to "二部小组1", "userName" to "雏田", "sex" to "女"), mapOf("deptName" to "研发二部", "groupName" to "二部小组1", "userName" to "小樱", "sex" to "女"), mapOf("deptName" to "研发一部", "groupName" to "一部小组2", "userName" to "鸣人", "sex" to "男"), mapOf("deptName" to "研发一部", "groupName" to "一部小组2", "userName" to "佐助", "sex" to "男"), mapOf("deptName" to "研发三部", "groupName" to "三部小组", "userName" to "拓海", "sex" to "男")
    )
}

fun templateFill(vo: Map<String, Any>, dtos: List<Map<String, Any>>?, templateStream: InputStream, outputStream: OutputStream) {
    val writer = EasyExcel.write(outputStream).withTemplate(templateStream).build()
    val workbook = writer.writeContext().writeWorkbookHolder().workbook
    workbook.forceFormulaRecalculation = true

    val sheet = EasyExcel.writerSheet(0).build()
    val fillConfig = FillConfig.builder().forceNewRow(true).direction(WriteDirectionEnum.VERTICAL).build()

    writer.fill(vo, sheet)
    if (CollUtil.isNotEmpty(dtos)) {
        writer.fill(dtos, fillConfig, sheet)
    }
    writer.finish()
    outputStream.flush()
}

private fun res(fileName: String): ServletOutputStream {
    val response = SprCtxUtil.httpServletResponse
    response.contentType = "multipart/form-data"
    response.characterEncoding = "UTF-8"
    response.setHeader("Content-disposition", "attachment;filename=$fileName")
    return response.outputStream
}

fun mergeExcel(dataList: List<Map<String, String?>>, outFilePath: String) {
    val deptMap = LinkedMap<String, Int>()
    val groupMap = LinkedMap<String, Int>()
    var lastValue = ""
    var groupIndex = 0
    var groupCount = 1

    dataList.forEach { map ->
        val deptName = map["deptName"]
        val groupName = map["groupName"] ?: ""
        deptMap[deptName] = deptMap.getOrDefault(deptName, 0) + 1

        if (lastValue.isNotEmpty() && groupName == lastValue) {
            groupCount++
            groupMap["$groupIndex$groupName"] = groupCount
        } else {
            groupIndex++
            groupCount = 1
            groupMap["$groupIndex$groupName"] = groupCount
            lastValue = groupName
        }
    }

    val deptNameCellRangeList = mutableListOf<String>()
    val groupNameCellRangeList = mutableListOf<String>()
    var initDeptNameRowIndex = 1
    var initGroupNameRowIndex = 1

    deptMap.forEach { (_, count) ->
        val cellRangeRow = initDeptNameRowIndex + count - 1
        deptNameCellRangeList.add("$initDeptNameRowIndex-$cellRangeRow")
        initDeptNameRowIndex = cellRangeRow + 1
    }

    groupMap.forEach { (_, count) ->
        val cellRangeRow = initGroupNameRowIndex + count - 1
        groupNameCellRangeList.add("$initGroupNameRowIndex-$cellRangeRow")
        initGroupNameRowIndex = cellRangeRow + 1
    }

    try {
        val inputStream = FileInputStream(outFilePath)
        val workbook: Workbook = XSSFWorkbook(inputStream)
        val outputStream = FileOutputStream(outFilePath)
        val sheet = workbook.getSheetAt(1)

        deptNameCellRangeList.forEach { range ->
            val (firstRow, lastRow) = range.split("-").map { it.toInt() }
            if (firstRow != lastRow) {
                sheet.addMergedRegion(CellRangeAddress(firstRow, lastRow, 0, 0))
            }
        }

        groupNameCellRangeList.forEach { range ->
            val (firstRow, lastRow) = range.split("-").map { it.toInt() }
            if (firstRow != lastRow) {
                sheet.addMergedRegion(CellRangeAddress(firstRow, lastRow, 1, 1))
            }
        }

        workbook.write(outputStream)
    } catch (e: IOException) {
        e.printStackTrace()
    }
}