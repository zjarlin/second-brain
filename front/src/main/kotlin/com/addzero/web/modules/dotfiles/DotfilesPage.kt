//package com.addzero.web.modules.dotfiles
//
//import androidx.compose.foundation.layout.Column
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Settings
//import androidx.compose.runtime.*
//import com.addzero.common.util.excel.ExcelUtil
//import com.addzero.web.infra.jimmer.base.pagefactory.PageResult
//import com.addzero.web.infra.jimmer.list
//import com.addzero.web.modules.second_brain.dotfiles.*
//import com.addzero.web.modules.second_brain.dotfiles.dto.BizDotfilesView
//import com.addzero.web.modules.second_brain.tag.BizTag
//import com.addzero.web.modules.sys.user.SysUser
//import com.addzero.web.ui.components.crud.Pagination
//import com.addzero.web.ui.components.crud.SearchPanel
//import com.addzero.web.ui.components.system.dynamicroute.MetaSpec
//import com.addzero.web.ui.components.system.dynamicroute.RouteMetadata
//import com.addzero.web.ui.components.table.DataTable
//import com.addzero.web.ui.hooks.useFilters
//import com.addzero.web.ui.hooks.usePagination
//import java.time.LocalDateTime
//
//
//class DotfilesPage : MetaSpec {
//    override val metadata: RouteMetadata
//        get() = RouteMetadata(
//            parentName = "管理",
//            title = "Dotfiles管理",
//            icon = Icons.Filled.Settings,
//            visible = true,
//            permissions = emptyList(),
//            order = 50.0
//        )
//
//    @Composable
//    override fun render() {
//        val viewModel = remember { DotfilesViewModel() }
//        var selectedPlatforms by remember { mutableStateOf(setOf<String>()) }
//        var selectedOSTypes by remember { mutableStateOf(setOf<String>()) }
//        val useFilters = useFilters("搜索test")
//
//
//    }
//
//}
//
