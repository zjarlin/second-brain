package com.addzero.web.modules.second_brain.dotfiles;

import cn.idev.excel.annotation.ExcelProperty

fun BizDotfiles.toExcelDTO(): BizDotfilesExcelDTO {
    var entity = BizDotfilesExcelDTO()

    entity.osType = this.osType.toString()


    entity.osStructure = this.osStructure.toString()


    entity.defType = this.defType.toString()


    entity.name = this.name


    entity.value = this.value


    entity.describtion = this.describtion


    entity.status = this.status


    entity.fileUrl = this.fileUrl


    entity.location = this.location

    return entity
}

public open class BizDotfilesExcelDTO {

    @ExcelProperty("操作系统 1=win 2-linux 3 mac null.不限")
    var osType: String? = null


    @ExcelProperty("系统架构 arm64=arm64 x86=x86")
    var osStructure: String? = null


    @ExcelProperty("定义类型 alias=alias export=export function=function sh=sh var=var")
    var defType: String? = null


    @ExcelProperty("名称")
    var name: String? = null


    @ExcelProperty("值")
    var value: String? = null


    @ExcelProperty("注释")
    var describtion: String? = null


    @ExcelProperty(
    "状态 1= 启用 0= 未启用"
//    , converter = BaseEnumConverter::class
    )
    var status: EnumStatus? = null


    @ExcelProperty("文件地址")
    var fileUrl: String? = null


    @ExcelProperty("文件位置")
    var location: String? = null

    fun toEntity(): BizDotfiles {
        val that = this

        return BizDotfiles {

            osType = listOf(EnumOsType.MAC)


            osStructure = Enumplatforms.X86


            defType =EnumDefType.VAR


            name = that.name.toString()


            value = that.value.toString()


            describtion = that.describtion


            status =EnumStatus.WEI_QIYONG


            fileUrl = that.fileUrl


            location = that.location

        }
    }
}