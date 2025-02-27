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
import androidx.compose.ui.unit.dp
import io.swagger.v3.oas.annotations.media.Schema

// 默认的排除字段


val DEFAULT_EXCLUDE_FIELDS = setOf(
    "createTime", "createdBy", "updateTime", "updatedBy", "id"
)

// 表格样式配置
object DataTableStyle {
    val headerElevation = 2.dp
    val indexColumnWidth = 60.dp
    val operationColumnWidth = 96.dp
    val horizontalPadding = 16.dp
    val verticalPadding = 12.dp
    val operationSpacing = 8.dp
    val alternateRowAlpha = 0.3f
}

@Composable
inline fun <reified T : Any> TableHeader(
    excludeFields: Set<String>,
    modifier: Modifier = Modifier
) {
    val java = T::class.java
    val fields = java.declaredFields

    Surface(
        modifier = modifier.fillMaxWidth(),
        tonalElevation = DataTableStyle.headerElevation,
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(
                horizontal = DataTableStyle.horizontalPadding,
                vertical = DataTableStyle.verticalPadding
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 序号列
            Text(
                text = "序号",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.width(DataTableStyle.indexColumnWidth)
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
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.weight(1f)
                    )
                }

            // 操作列
            Text(
                text = "操作",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.width(DataTableStyle.operationColumnWidth)
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
        modifier = modifier.fillMaxWidth(),
        color = if (index % 2 == 0) {
            MaterialTheme.colorScheme.surface
        } else {
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = DataTableStyle.alternateRowAlpha)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = DataTableStyle.verticalPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 序号列
            Text(
                text = (startIndex + index + 1).toString(),
                modifier = Modifier.width(DataTableStyle.indexColumnWidth)
            )

            // 字段值
            fields
//                .filter { excludeFields.con }
                .forEach { field ->
                    field.isAccessible=true
                    Text(
                        text = field.get(item)?.toString() ?: "",
                        modifier = Modifier.weight(1f)
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
        modifier = modifier.width(DataTableStyle.operationColumnWidth),
        horizontalArrangement = Arrangement.spacedBy(DataTableStyle.operationSpacing)
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
            modifier = Modifier.fillMaxSize().padding(horizontal = DataTableStyle.horizontalPadding)
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
