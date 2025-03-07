package com.addzero.web.ui.hooks.form

import androidx.compose.runtime.Composable
import com.addzero.web.ui.hooks.table.entity.AddColumn

@Composable
fun <E : Any> TreeInput(
    value: String,
    onValueChange: (Any?) -> Unit,
    column: AddColumn<E>,
    error: String?
) {
    // 简化为基本的下拉选择
    SelectInput(value, onValueChange, column, error)
}