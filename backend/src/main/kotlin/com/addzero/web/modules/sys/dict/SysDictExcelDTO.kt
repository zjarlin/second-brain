package com.addzero.web.modules.sys.dict;

import cn.idev.excel.annotation.ExcelProperty


fun SysDict.toExcelDTO(): SysDictExcelDTO {
    var entity = SysDictExcelDTO()

    entity.dictName = this.dictName


    entity.dictCode = this.dictCode


    entity.description = this.description


    entity.type = this.type

    return entity
}

public open class SysDictExcelDTO {

    @ExcelProperty("字典名称")
    var dictName: String? = null


    @ExcelProperty("字典编码")
    var dictCode: String? = null


    @ExcelProperty("描述")
    var description: String? = null


    @ExcelProperty("字典类型0为string,1为number")
    var type: Long? = null

    fun toEntity(): SysDict {
        return SysDict {

            dictName = this.dictName


            dictCode = this.dictCode


            description = this.description


            type = this.type

        }
    }
}