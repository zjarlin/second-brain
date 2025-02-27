package com.addzero.web.ui.hooks.table

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

data class AddColumn<E>(
    val title: String,
    val getFun: (E) -> Any?,
    val customRender: @Composable (String) -> Unit = { Text(it) }
)
