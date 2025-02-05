package com.addzero.web.modules.dotfiles

import BizDotFiles
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.addzero.web.ui.components.system.dynamicroute.RouteMetadata
import com.addzero.web.ui.components.system.dynamicroute.MetaSpec
import com.addzero.web.ui.components.crud.CrudLayout
import com.addzero.web.ui.components.crud.Pagination

class DotfilesPage : MetaSpec {

    override val metadata: RouteMetadata
        get() = RouteMetadata(
            refPath = "/dotfiles",
            parentRefPath = "管理",
            title = "Dotfiles管理",
            icon = Icons.Filled.Apps,
            visible = true,
            permissions = emptyList()
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