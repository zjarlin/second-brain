package com.addzero.web.ui.components.table

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.addzero.common.consts.DEFAULT_EXCLUDE_FIELDS
import io.swagger.v3.oas.annotations.media.Schema

// 默认的排除字段


@Composable
inline fun <reified T : Any> TableHeader(
    excludeFields: Set<String>,
    modifier: Modifier = Modifier
) {
    val java = T::class.java
    val fields = java.declaredFields

    Surface(
        modifier = modifier.fillMaxWidth().height(56.dp),
        tonalElevation = 2.dp,
        color = Color(0xFF2196F3)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(
                horizontal = 16.dp,
                vertical = 12.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 序号列
            Text(
                text = "序号",
                style = Typography().titleMedium.copy(
                    fontWeight = FontWeight.Bold  // 加粗文字
                ),
                modifier = Modifier.width(60.dp),
                color = MaterialTheme.colorScheme.onPrimary
            )

            // 字段列
            fields
                .filter { !excludeFields.contains(it.name) }
                .forEach { field ->
                    field.isAccessible = true
                    val annotation = field.getAnnotation(Schema::class.java)
                    val columnName = annotation?.description ?: field.name.replaceFirstChar { it.uppercase() }
                    Text(
                        text = columnName,
                        style = Typography().titleMedium.copy(
                            fontWeight = FontWeight.Bold  // 加粗文字
                        ),
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }

            // 操作列
            Text(
                text = "操作",
                style = Typography().titleMedium.copy(
                    fontWeight = FontWeight.Bold  // 加粗文字
                ),
                modifier = Modifier.width(120.dp),
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
inline fun <reified T : Any> TableRow(
    item: T,
    index: Int,
    startIndex: Int,
    crossinline onEdit: (T) -> Unit,
    crossinline onDelete: (T) -> Unit,
    modifier: Modifier = Modifier
) {

    val fields = T::class.java.declaredFields
    Surface(
        modifier = modifier.fillMaxWidth().height(48.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(
                horizontal = 16.dp,
                vertical = 12.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 序号列
            Text(
                text = (startIndex + index + 1).toString(),
                modifier = Modifier.width(60.dp)
//                .wrapContentWidth(Alignment.Horizontal.Center)
            )

            // 字段值
            fields
//                .filter { excludeFields.con }
                .forEach { field ->
                    field.isAccessible=true
                    Text(
                        text = field.get(item)?.toString() ?: "",
                        modifier = Modifier.weight(1f)
//                        .wrapContentWidth(Alignment.Horizontal.Center)
                    )
                }

            // 操作按钮
            OperationButtons(
                onEdit = { onEdit(item) },
                onDelete = { onDelete(item) }
            )
        }
        HorizontalDivider()
    }
}

@Composable
fun OperationButtons(
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.width(96.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        IconButton(
            onClick = onEdit,
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(Icons.Default.Edit, "编辑")
        }
        IconButton(
            onClick = onDelete,
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = MaterialTheme.colorScheme.error
            )
        ) {
            Icon(Icons.Default.Delete, "删除")
        }
    }
}

@Composable
inline fun <reified T : Any> DataTable(
    records: List<T>,
    crossinline onEdit: (T) -> Unit,
    crossinline onDelete: (T) -> Unit,
    excludeFields: Set<String> = DEFAULT_EXCLUDE_FIELDS,
    startIndex: Int = 0,
    pageIndex: Int,
    totalPages: Int,
    noinline onPageSizeChange: (Int) -> Unit
) {

    Column(modifier = Modifier.fillMaxSize()) {
        TableHeader<T>(excludeFields)  // 添加泛型类型参数
        HorizontalDivider()

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)
        ) {
            itemsIndexed(records) { index, item ->
                TableRow(
                    item = item,
                    index = index,
                    startIndex = startIndex,
                    onEdit = onEdit,
                    onDelete = onDelete
                )
            }
        }
    }

    Pagination(
        currentPage = pageIndex,
        totalPages = totalPages,
        onPageChange = onPageSizeChange
    )
}
