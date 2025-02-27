package com.addzero.web.ui.hooks.table

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun PageNavigation(
    currentPage: Long,
    totalPages: Long,
    onPreviousPage: () -> Unit,
    onNextPage: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        OutlinedButton(
            onClick = onPreviousPage,
            enabled = currentPage > 0,
            modifier = Modifier.height(36.dp),
            contentPadding = PaddingValues(horizontal = 12.dp)
        ) {
            Text("上一页", style = MaterialTheme.typography.bodyMedium)
        }

        Text(
            "$currentPage/$totalPages",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        OutlinedButton(
            onClick = onNextPage,
            enabled = currentPage < totalPages,
            modifier = Modifier.height(36.dp),
            contentPadding = PaddingValues(horizontal = 12.dp)
        ) {
            Text("下一页", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun PageSizeSelector(
    currentPageSize: Long,
    onPageSizeChange: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(
            "每页显示: ",
            style = MaterialTheme.typography.bodyMedium
        )
        listOf(10L, 30L, 50L, 100L).forEach { size ->
            OutlinedButton(
                onClick = { onPageSizeChange(size) },
                modifier = Modifier.padding(horizontal = 4.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text("$size")
            }
        }
    }
}

@Composable
fun CustomPageSizeInput(
    currentPageSize: Long,
    onPageSizeChange: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = currentPageSize.toString(),
        onValueChange = { newValue ->
            if (newValue.isNotBlank() && newValue.all { it.isDigit() }) {
                val newSize = newValue.toLongOrNull()
                if (newSize != null && newSize > 0) {
                    onPageSizeChange(newSize)
                }
            }
        },
        modifier = modifier.width(80.dp).padding(horizontal = 4.dp),
        singleLine = true,
        textStyle = MaterialTheme.typography.bodyMedium,
        placeholder = { Text("自定义", style = MaterialTheme.typography.bodyMedium) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}
