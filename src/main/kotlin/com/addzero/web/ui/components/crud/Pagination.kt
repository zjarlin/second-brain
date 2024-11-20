package com.addzero.web.ui.components.crud

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.web.model.PageResult

@Composable
fun <T> Pagination(
    pageResult: PageResult<T>,
    onPrevious: () -> Unit,
    onNext: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onPrevious,
            enabled = !pageResult.isFirst
        ) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, "上一页")
        }

        Text(
            "${pageResult.pageNumber + 1}/${pageResult.totalPages}",
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        IconButton(
            onClick = onNext,
            enabled = !pageResult.isLast
        ) {
            Icon(Icons.Default.KeyboardArrowRight, "下一页")
        }

        Text(
            "共 ${pageResult.totalElements} 条",
            modifier = Modifier.padding(start = 16.dp)
        )
    }
} 