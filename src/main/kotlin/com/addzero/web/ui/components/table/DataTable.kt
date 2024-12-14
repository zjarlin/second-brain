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
import java.awt.SystemColor.text
import java.util.LinkedList
import kotlin.reflect.KProperty1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.memberExtensionProperties
import kotlin.reflect.jvm.internal.impl.descriptors.runtime.structure.ReflectClassUtilKt

// 默认的排除字段
val DEFAULT_EXCLUDE_FIELDS = setOf(
    "createTime",
    "createdBy",
    "updateTime",
    "updatedBy",
    "id"
)

@Composable
fun <T : Any> DataTable(
    items: List<T>,
    onEdit: (T) -> Unit,
    onDelete: (T) -> Unit,
    excludeFields: Set<String> = DEFAULT_EXCLUDE_FIELDS,
    startIndex: Int = 0,
    currentPage: Int,
    totalPages: Int,
    onPageChange: (Int) -> Unit
) {
    // 使用declaredMemberProperties保持定义顺序
    val fields = items.firstOrNull()?.let { item ->

//        item::class.memberExtensionProperties
        val declaredMemberProperties = item::class.java.declaredFields
        declaredMemberProperties
//            .filterIsInstance<KProperty1<T, *>>()
            .filter { field ->
                !java.lang.reflect.Modifier.isStatic(field.modifiers) && field.name !in excludeFields
            }
            .toList()
    } ?: emptyList()

    Column(modifier = Modifier.fillMaxSize()) {
        // 表头
        Surface(
            modifier = Modifier.fillMaxWidth(),
            tonalElevation = 1.dp
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 序号列
                Text(
                    text = "序号",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.width(60.dp)
                )

                // 按声明顺序显示字段
                fields.forEach{ field ->
                    field.isAccessible = true
                    val columnName = field.getAnnotation(ColumnName::class.java)?.value
                        ?: field.name.replaceFirstChar { it.uppercase() }
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
                    modifier = Modifier.width(96.dp)
                )
            }
        }

        Divider()

        // 数据行
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)
        ) {
            itemsIndexed(items) { index, item ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 序号列
                    Text(
                        text = (startIndex + index + 1).toString(),
                        modifier = Modifier.width(60.dp)
                    )

                    // 按声明顺序显示字段值
                    fields.forEach { field ->
                        Text(
                            text = field.get(item)?.toString() ?: "",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // 操作按钮
                    Row(
                        modifier = Modifier.width(96.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(onClick = { onEdit(item) }) {
                            Icon(Icons.Default.Edit, "编辑")
                        }
                        IconButton(onClick = { onDelete(item) }) {
                            Icon(Icons.Default.Delete, "删除")
                        }
                    }
                }
                Divider()
            }
        }

        // 添加分页控件
        Pagination(
            currentPage = currentPage,
            totalPages = totalPages,
            onPageChange = onPageChange
        )
    }
}
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class ColumnName(val value: String)