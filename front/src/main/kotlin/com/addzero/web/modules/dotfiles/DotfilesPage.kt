package com.addzero.web.modules.dotfiles

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import com.addzero.common.util.excel.ExcelUtil
import com.addzero.web.infra.jimmer.base.pagefactory.PageResult
import com.addzero.web.modules.second_brain.dotfiles.*
import com.addzero.web.modules.second_brain.dotfiles.dto.BizDotfilesSpec
import com.addzero.web.modules.second_brain.dotfiles.dto.BizDotfilesView
import com.addzero.web.ui.components.crud.CrudLayout
import com.addzero.web.ui.components.crud.Pagination
import com.addzero.web.ui.components.crud.SearchPanel
import com.addzero.web.ui.components.system.dynamicroute.MetaSpec
import com.addzero.web.ui.components.system.dynamicroute.RouteMetadata
import com.addzero.web.ui.components.table.DataTable
import org.apache.poi.ss.formula.functions.T

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
        var searchName by remember { mutableStateOf("请输入内容") }
        var selectedPlatforms by remember { mutableStateOf(setOf<String>()) }
        var selectedOSTypes by remember { mutableStateOf(setOf<String>()) }

        val value by remember {
            val bizDotfiles = BizDotfiles {
                id = 1
                //todo 在这个lambda里alt+回车,把属性都列出来,需要这个idea意图
                osType = listOf(EnumOsType.LINUX, EnumOsType.MAC)
                osStructure = Enumplatforms.ARM64
                defType = EnumDefType.ALIAS
                name = "vi"
                value = "nvim $@"
                describtion = "neovim别名"
                status = EnumStatus.QIYONG
            }
            val bizDotfilesView = BizDotfilesView(bizDotfiles)

            val listOf = listOf(bizDotfilesView)

            val pageResult = PageResult(
                content = listOf,
                totalElements = 0,
                totalPages = 0,
                pageIndex = 0,
                pageSize = 10,
                isFirst = true,
                isLast = true
            )
            mutableStateOf(pageResult)
        }

        var isLoading by remember { mutableStateOf(false) }
        var error by remember { mutableStateOf("") }

        CrudLayout<BizDotfiles>(
            // 搜索区域插槽
            searchBar = {
                SearchPanel(searchText = searchName, onSearchTextChange = { searchName = it }, onSearch = {
                    viewModel.page()
                }, filters = {}, actions = {})
            },
            // 操作按钮插槽
            actionButtons = {

            },
            // 主内容区插槽
            content = {
                val pageIndex = value.pageIndex
                val pageSize = value.pageSize
                val bizDotfilesSpec = BizDotfilesSpec()
                bizDotfilesSpec.defType
                DataTable(records = value.content, onEdit = { /* 处理编辑 */ }, onDelete = {
                    viewModel.deleteByIds(listOf(it.id))
                }, pageIndex = pageIndex, totalPages = value.totalPages, onPageChange = { newPage ->
                    viewModel.page(
                        spec = bizDotfilesSpec, pageNum = pageIndex, pageSize = newPage
                    )
                })
            },
            // 分页控制插槽
            pagination = {
                Pagination(pageResult = value, onLast = {}, onNext = {}, onChangePageSize = {})
            })
    }

}
