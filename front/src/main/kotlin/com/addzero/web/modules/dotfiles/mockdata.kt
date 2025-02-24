import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.addzero.web.infra.jimmer.base.pagefactory.PageResult
import com.addzero.web.modules.second_brain.dotfiles.BizDotfiles
import com.addzero.web.modules.second_brain.dotfiles.EnumDefType
import com.addzero.web.modules.second_brain.dotfiles.EnumStatus
import com.addzero.web.modules.second_brain.dotfiles.Enumplatforms
import com.addzero.web.modules.second_brain.dotfiles.dto.BizDotfilesView
import com.addzero.web.modules.second_brain.tag.BizTag
import com.addzero.web.modules.sys.user.SysUser
import java.time.LocalDateTime

private fun mockData(): () -> MutableState<PageResult<BizDotfilesView>> = {
        val bizDotfiles = BizDotfiles {
            id = 1
            createBy = SysUser { id = 1 }
            updateBy = SysUser { id = 1 }
            createTime = LocalDateTime.now()
            updateTime = LocalDateTime.now()
            osType = listOf(BizTag { name = "linux" }, BizTag { name = "mac" })
            osStructure = Enumplatforms.ARM64
            defType = EnumDefType.ALIAS
            name = "vi"
            value = "nvim $@"
            describtion = "neovim别名"
            status = EnumStatus.QIYONG
            fileUrl = ""
            location = ""
        }
        val bizDotfilesView = BizDotfilesView(bizDotfiles)

        val result = generateSequence(0) { it + 1 }
            .take(30)
            .map { bizDotfilesView }
            .toList()

        val pageResult = PageResult(
            content = result,
            totalElements = 0,
            totalPages = 0,
            pageIndex = 0,
            pageSize = 10,
            isFirst = true,
            isLast = true
        )
        mutableStateOf(pageResult)
    }
