package com.addzero.web.modules.dotfiles

import androidx.compose.runtime.Composable
import com.addzero.web.ui.components.table.DataTable

@Composable
internal fun DotfilesTable(
    items: List<BizDotFiles>,
    onEdit: (BizDotFiles) -> Unit,
    onDelete: (BizDotFiles) -> Unit,
    currentPage: Int,
    totalPages: Int,
    onPageChange: (Int) -> Unit,
    startIndex: Int = currentPage * 10 // 假设每页10条数据
) {
    DataTable(
        items = items,
        onEdit = onEdit,
        onDelete = onDelete,
        startIndex = startIndex,
        currentPage = currentPage,
        totalPages = totalPages,
        onPageChange = onPageChange
    )
}
