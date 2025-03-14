package com.addzero.web.ui.lowcode.forms

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.web.ui.lowcode.core.DynamicForm
import com.addzero.web.ui.lowcode.core.RenderField
import com.addzero.web.ui.lowcode.metadata.FieldMetadata
import com.addzero.web.ui.lowcode.renderers.DefaultFieldRenderer

/**
 * 搜索表单
 * 用于构建搜索条件
 */
@Composable
fun <E : Any> SearchForm(
    initialData: E,
    fields: List<FieldMetadata<E>>,
    onSearch: (E) -> Unit,
    onReset: () -> Unit = {},
    columnCount: Int = 3,
    renderField: RenderField<E> = { field, value, onChange, error -> 
        DefaultFieldRenderer(field, value, onChange, error)
    }
) {
    var formData by remember { mutableStateOf(initialData) }
    
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 表单内容
        val searchFields = fields.filter { it.showInSearch }
        
        // 使用Grid布局
        val rows = (searchFields.size + columnCount - 1) / columnCount
        for (row in 0 until rows) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                for (col in 0 until columnCount) {
                    val index = row * columnCount + col
                    if (index < searchFields.size) {
                        Box(modifier = Modifier.weight(1f)) {
                            DynamicForm(
                                data = formData,
                                fields = listOf(searchFields[index]),
                                renderField = renderField,
                                onDataChange = { formData = it }
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
        
        // 按钮行
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = {
                    formData = initialData
                    onReset()
                },
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text("重置")
            }
            
            Button(
                onClick = { onSearch(formData) }
            ) {
                Text("搜索")
            }
        }
    }
} 