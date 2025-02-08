package com.addzero.web.modules.dotfiles

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import com.addzero.web.ui.components.system.dynamicroute.MetaSpec
import com.addzero.web.ui.components.crud.CrudLayout
import com.addzero.web.ui.components.crud.Pagination
import com.addzero.web.ui.components.system.dynamicroute.RouteMetadata

class DotfilesPage : MetaSpec {

    override val metadata: RouteMetadata
        get() = RouteMetadata(
//            refPath = this.javaClass.name,
            parentName = "管理",
            title = "Dotfiles管理",
            icon = Icons.Filled.Settings,
            visible = true,
            permissions = emptyList()
            , order = 50.0
        )


    @Composable
    override fun render() {
        val scope = rememberCoroutineScope()
        val viewModel = remember { DotfilesViewModel(scope) }

        // 搜索条件状态
        var searchName by remember { mutableStateOf("") }
        var selectedPlatforms by remember { mutableStateOf(setOf<String>()) }
        var selectedOSTypes by remember { mutableStateOf(setOf<String>()) }

        CrudLayout<BizDotFiles>(
            isLoading = viewModel.isLoading, error = viewModel.error,
            // 搜索区域插槽
            searchBar = {
                SearchPanel(
                    searchName = searchName,
                    onSearchNameChange = { searchName = it },
                    selectedPlatforms = selectedPlatforms,
                    onPlatformSelectionChange = { selectedPlatforms = it },
                    selectedOSTypes = selectedOSTypes,
                    onOSTypeSelectionChange = { selectedOSTypes = it },
                    onSearch = {
                        viewModel.loadData(
                            name = searchName, platforms = selectedPlatforms, osTypes = selectedOSTypes
                        )
                    })
            },
            // 操作按钮插槽
            actionButtons = {
                ActionButtons(scope, viewModel)
            },
            // 主内容区插槽
            content = {
                DotfilesTable(
                    items = viewModel.pageResult?.content ?: emptyList(),
                    onEdit = { /* 处理编辑 */ },
                    onDelete = { viewModel.deleteDotfile(it.id) },
                    currentPage = viewModel.currentPage,
                    totalPages = viewModel.pageResult?.totalPages ?: 1,
                    onPageChange = { newPage ->
                        viewModel.loadData(page = newPage)
                    })
            },
            // 分页控制插槽
            pagination = {
                viewModel.pageResult?.let { result ->
                    Pagination(
                        pageResult = result, onPrevious = viewModel::previousPage, onNext = viewModel::nextPage
                    )
                }
            })
    }

}