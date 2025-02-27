package com.addzero.web.ui.hooks.table

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

data class AddColumn<E>(
    val title: String,
    val getFun: (E) -> Any?,
    val customRender: @Composable (String) -> Unit = { Text(it) }
) {
    companion object {
        fun <E> `+`(
        title: String, getFun: (E) -> Any?,
        customRender: @Composable (String) -> Unit = { Text(it) }
        ) = AddColumn(
            title = title,
            getFun = getFun,
            customRender = customRender
        )
    }
}
