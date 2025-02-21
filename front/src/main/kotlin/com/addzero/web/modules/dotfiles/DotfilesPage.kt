package com.addzero.web.modules.dotfiles

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import com.addzero.common.util.excel.ExcelUtil
import com.addzero.web.infra.jimmer.base.pagefactory.PageResult
import com.addzero.web.infra.jimmer.list
import com.addzero.web.modules.second_brain.dotfiles.*
import com.addzero.web.modules.second_brain.dotfiles.dto.BizDotfilesSpec
import com.addzero.web.modules.second_brain.dotfiles.dto.BizDotfilesView
import com.addzero.web.modules.second_brain.tag.BizTag
import com.addzero.web.modules.sys.user.SysUser
import com.addzero.web.ui.components.crud.CrudLayout
import com.addzero.web.ui.components.crud.Pagination
import com.addzero.web.ui.components.crud.SearchPanel
import com.addzero.web.ui.components.system.dynamicroute.MetaSpec
import com.addzero.web.ui.components.system.dynamicroute.RouteMetadata
import com.addzero.web.ui.components.table.DataTable
import java.time.LocalDateTime

fun main() {
    val readMap = ExcelUtil.readMap("/Users/zjarlin/dot.xlsx")
}

class DotfilesPage : MetaSpec {

    override val metadata: RouteMetadata
        get() = RouteMetadata(
            parentName = "管理",
            title = "Dotfiles管理",
            icon = Icons.Filled.Settings,
            visible = true,
            permissions = emptyList(),
            order = 50.0
        )


    @Composable
    override fun render() {
        val scope = rememberCoroutineScope()
        val viewModel = remember { DotfilesViewModel() }
        // 搜索条件状态
        var searchName by remember { mutableStateOf("请输入要搜索的内容") }
        var selectedPlatforms by remember { mutableStateOf(setOf<String>()) }
        var selectedOSTypes by remember { mutableStateOf(setOf<String>()) }


        val sql = viewModel.sql
        val list = sql.list(BizDotfiles::class)

        val pageResult by remember(mockData())
        var isLoading by remember { mutableStateOf(false) }
        var error by remember { mutableStateOf("") }

        CrudLayout<BizDotfiles>(
            // 搜索区域插槽
            searchBar = {

                SearchPanel(searchText = searchName, onSearchTextChange = { searchName = it }, onSearch = {
                    val page = viewModel.page()

                }, filters = {}, actions = {})
            },
            // 操作按钮插槽
            actionButtons = {

            },
            // 主内容区插槽
            content = {
                val pageIndex = pageResult.pageIndex
                val pageSize = pageResult.pageSize
                val bizDotfilesSpec = BizDotfilesSpec()
                bizDotfilesSpec.defType
                DataTable(records = pageResult.content, onEdit = {
                    /* 处理编辑 */
//                    弹出form

                }, onDelete = {
                    viewModel.deleteByIds(listOf(it.id))
                }, pageIndex = pageIndex, totalPages = pageResult.totalPages, onPageSizeChange = { newPage ->
                    viewModel.page(
                        spec = bizDotfilesSpec, pageNum = pageIndex, pageSize = newPage
                    )
                })
            },
            // 分页控制插槽
            pagination = {
                Pagination(pageResult = pageResult, onLast = {}, onNext = {}, onChangePageSize = {})
            })
    }

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

}
