package com.addzero.web.ui.lowcode.table

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.web.ui.lowcode.metadata.FieldMetadata

/**
 * 数据表格
 * 用于显示数据列表
 */
@Composable
fun <E : Any> DataTable(
    data: List<E>,
    fields: List<FieldMetadata<E>>,
    onRowClick: (E) -> Unit = {}
) {
    val tableFields = fields.filter { it.showInTable }
    
    Column(modifier = Modifier.fillMaxWidth()) {
        // 表头
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            tableFields.forEach { field ->
                Text(
                    text = field.title,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        
        Divider()
        
        // 表格内容
        LazyColumn {
            items(data) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable { onRowClick(item) },
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    tableFields.forEach { field ->
                        Box(modifier = Modifier.weight(1f)) {
                            val value = field.getValue(item)
                            if (field.customRender != null) {
                                field.customRender.invoke(value)
                            } else {
                                Text(
                                    text = value?.toString() ?: "",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
                
                Divider()
            }
        }
    }
} 