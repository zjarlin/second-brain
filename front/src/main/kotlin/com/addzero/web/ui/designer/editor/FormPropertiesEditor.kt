package com.addzero.web.ui.designer.editor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.addzero.web.ui.designer.models.FormConfig

/**
 * 表单属性编辑器
 * 编辑表单级别的属性
 */
@Composable
fun FormPropertiesEditor(
    formConfig: FormConfig,
    onFormConfigUpdated: (FormConfig) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            "表单属性",
            style = MaterialTheme.typography.titleMedium
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 表单名称
        OutlinedTextField(
            value = formConfig.name,
            onValueChange = { onFormConfigUpdated(formConfig.copy(name = it)) },
            label = { Text("表单名称") },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // 表单标题
        OutlinedTextField(
            value = formConfig.title,
            onValueChange = { onFormConfigUpdated(formConfig.copy(title = it)) },
            label = { Text("表单标题") },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // 表单描述
        OutlinedTextField(
            value = formConfig.description,
            onValueChange = { onFormConfigUpdated(formConfig.copy(description = it)) },
            label = { Text("表单描述") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 列数配置
        Text(
            "布局配置",
            style = MaterialTheme.typography.titleSmall
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // 列数滑块
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("列数：", 
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(end = 8.dp)
            )
            
            // 数字输入框
            OutlinedTextField(
                value = formConfig.columnCount.toString(),
                onValueChange = { 
                    val newValue = it.toIntOrNull()
                    if (newValue != null && newValue in 1..4) {
                        onFormConfigUpdated(formConfig.copy(columnCount = newValue))
                    }
                },
                modifier = Modifier.width(80.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                singleLine = true
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // 列数滑块
            Slider(
                value = formConfig.columnCount.toFloat(),
                onValueChange = { 
                    val newColumnCount = it.toInt().coerceIn(1, 4)
                    onFormConfigUpdated(formConfig.copy(columnCount = newColumnCount))
                },
                valueRange = 1f..4f,
                steps = 2,
                modifier = Modifier.weight(1f)
            )
        }
        
        // 列数快速选择按钮
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            for (cols in 1..4) {
                OutlinedButton(
                    onClick = { onFormConfigUpdated(formConfig.copy(columnCount = cols)) },
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 2.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (formConfig.columnCount == cols) 
                            MaterialTheme.colorScheme.primaryContainer 
                        else 
                            MaterialTheme.colorScheme.surface
                    )
                ) {
                    Text(
                        "$cols 列",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
        
        // 预览
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            "布局预览",
            style = MaterialTheme.typography.titleSmall
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // 简单的布局预览
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            tonalElevation = 1.dp,
            shape = MaterialTheme.shapes.small
        ) {
            Row(
                modifier = Modifier.padding(8.dp)
            ) {
                for (col in 1..formConfig.columnCount) {
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(4.dp),
                        tonalElevation = 2.dp,
                        color = MaterialTheme.colorScheme.secondaryContainer
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("列 $col")
                        }
                    }
                }
            }
        }
    }
} 