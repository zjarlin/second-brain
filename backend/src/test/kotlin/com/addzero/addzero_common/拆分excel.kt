package com.addzero.addzero_common

import cn.hutool.core.bean.BeanUtil
import com.addzero.common.util.excel.ExcelUtil
import com.addzero.web.infra.jackson.toJson
import com.addzero.web.modules.second_brain.dotfiles.*
import com.addzero.web.modules.second_brain.dotfiles.dto.BizDotfilesSpec
import com.addzero.web.modules.second_brain.tag.BizTag
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import java.io.File
import cn.hutool.poi.excel.ExcelWriter
import cn.idev.excel.EasyExcel
import cn.hutool.poi.excel.ExcelUtil as HuExcelUtil

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class 拆分excel(
) {
    @Test
    fun test(): Unit {
        data class Dasda(
            val 报告编号: String,
            val 当前数据状态: String,
            val 样品_项目名称: String,
            val 项目名称: String,
            val 规格型号: String,
            val 委托日期: String,
            val 工程名称: String,
            val 工程部位: String,
            val 检测参数: String,
        )

        // 读取Excel数据
        EasyExcel.read("/Users/zjarlin/Desktop/副本洛阳文博体育公园项目（商业及地下车库综合空间）.xls")
        val readMap1 = ExcelUtil.readMap("/Users/zjarlin/Desktop/副本洛阳文博体育公园项目（商业及地下车库综合空间）.xls")
        val dataList = readMap1.map {
            val mapToBean = BeanUtil.mapToBean(it, Dasda::class.java, true)
            mapToBean
        }

        // 创建输出目录
        val outDir = File("/Users/zjarlin/Desktop/out")
        if (!outDir.exists()) {
            outDir.mkdirs()
        }

        // 按样品_项目名称分组
        val groupedData = dataList.groupBy { it.样品_项目名称 }

        // 创建新的Excel文件，每个分组写入一个sheet
        val writer = HuExcelUtil.getWriter("/Users/zjarlin/Desktop/out/拆分结果.xlsx")
        
        groupedData.forEach { (projectName, items) ->
            // 切换到新sheet
            writer.setSheet(projectName)
            
            // 写入表头
            writer.writeRow(listOf(
                "报告编号", "当前数据状态", "样品_项目名称", "项目名称",
                "规格型号", "委托日期", "工程名称", "工程部位", "检测参数"
            ))
            
            // 写入数据
            items.forEach { item ->
                writer.writeRow(listOf(
                    item.报告编号, item.当前数据状态, item.样品_项目名称, item.项目名称,
                    item.规格型号, item.委托日期, item.工程名称, item.工程部位, item.检测参数
                ))
            }
        }
        
        writer.close()
        println("Excel拆分完成，文件保存在：/Users/zjarlin/Desktop/out/拆分结果.xlsx")
    }
}
//}

