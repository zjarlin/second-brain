package com.addzero.web.modules.dotfiles

import androidx.compose.runtime.*
import cn.hutool.extra.spring.SpringUtil
import com.addzero.web.base.BaseViewModel
import com.addzero.web.infra.jimmer.base.pagefactory.PageResult
import com.addzero.web.modules.second_brain.dotfiles.BizDotfiles
import com.addzero.web.modules.second_brain.dotfiles.BizDotfilesExcelDTO
import com.addzero.web.modules.second_brain.dotfiles.EnumOsType
import com.addzero.web.modules.second_brain.dotfiles.Enumplatforms
import com.addzero.web.modules.second_brain.dotfiles.dto.BizDotfilesSaveDTO
import com.addzero.web.modules.second_brain.dotfiles.dto.BizDotfilesSpec
import com.addzero.web.modules.second_brain.dotfiles.dto.BizDotfilesUpdateDTO
import com.addzero.web.modules.second_brain.dotfiles.dto.BizDotfilesView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File


class DotfilesViewModel : BaseViewModel<BizDotfiles, BizDotfilesSpec, BizDotfilesSaveDTO, BizDotfilesUpdateDTO, BizDotfilesView, BizDotfilesExcelDTO>() {

    fun generateConfig(byte: Byte): Any {
        TODO("Not yet implemented")
    }

    override fun toEntity(excelWriteDTO: BizDotfilesExcelDTO): BizDotfiles {
        TODO("Not yet implemented")
    }

    override fun toExcelWriteDTO(entity: BizDotfiles): BizDotfilesExcelDTO {
        TODO("Not yet implemented")
    }

}
