package com.addzero.web.modules.sys.dict

import com.addzero.web.infra.jimmer.base.BaseCrudController
import com.addzero.web.infra.jimmer.base.BaseFastExcelApi
import com.addzero.web.modules.sys.dict.dto.SysDictSaveDTO
import com.addzero.web.modules.sys.dict.dto.SysDictSpec
import com.addzero.web.modules.sys.dict.dto.SysDictUpdateDTO
import com.addzero.web.modules.sys.dict.dto.SysDictView
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/sysDict")
class SysDictController(
    private val kSqlClient: KSqlClient,
) : BaseCrudController<SysDict, SysDictSpec, SysDictSaveDTO, SysDictUpdateDTO, SysDictView>, BaseFastExcelApi<SysDict, SysDictSpec, SysDictExcelDTO> {
    override fun toExcelWriteDTO(entity: SysDict): SysDictExcelDTO {
        TODO("Not yet implemented")
    }

    override fun toEntity(excelWriteDTO: SysDictExcelDTO): SysDict {
        TODO("Not yet implemented")
    }

}