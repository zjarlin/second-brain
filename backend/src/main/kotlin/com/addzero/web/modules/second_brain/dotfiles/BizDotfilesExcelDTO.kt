package com.addzero.web.modules.second_brain.dotfiles;

import cn.idev.excel.annotation.ExcelProperty;
import com.addzero.web.modules.second_brain.dotfiles.BizDotfiles

fun BizDotfiles.toExcelDTO(): BizDotfilesExcelDTO {
    var entity = BizDotfilesExcelDTO()

    entity.osStructure = this.osStructure


    entity.defType = this.defType


    entity.name = this.name


    entity.value = this.value


    entity.describtion = this.describtion


    entity.status = this.status


    entity.fileUrl = this.fileUrl


    entity.location = this.location

    return entity
}

public open class BizDotfilesExcelDTO {

    @ExcelProperty("系统架构")
    var osStructure: Enumplatforms? = null


    @ExcelProperty("定义类型 alias=alias export=export function=function sh=sh var=var")
    var defType: EnumDefType? = null


    @ExcelProperty("名称")
    var name: String? = null


    @ExcelProperty("值")
    var value: String? = null


    @ExcelProperty("注释")
    var describtion: String? = null


    @ExcelProperty("状态 1= 启用 0= 未启用")
    var status: EnumStatus? = null


    @ExcelProperty("文件地址")
    var fileUrl: String? = null


    @ExcelProperty("文件位置")
    var location: String? = null

    fun toEntity(): BizDotfiles {
        val that = this

        return BizDotfiles {

            osStructure = that.osStructure


            defType = that.defType!!


            name = that.name.toString()


            value = that.value.toString()


            describtion = that.describtion


            status = that.status!!


            fileUrl = that.fileUrl


            location = that.location

        }
    }
}
