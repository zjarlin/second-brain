package com.addzero.web.ui.designer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.addzero.web.ui.designer.components.ComponentPanel
import com.addzero.web.ui.designer.components.FormComponent
import com.addzero.web.ui.designer.components.getComponentByType
import com.addzero.web.ui.designer.editor.PropertiesEditor
import com.addzero.web.ui.designer.models.FormConfig
import com.addzero.web.ui.designer.models.FormField
import com.addzero.web.ui.designer.preview.FormPreview
import com.addzero.web.ui.hooks.table.entity.RenderType
import com.addzero.web.ui.lowcode.metadata.FieldMetadata
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

/**
 * 表单设计器
 * 支持拖拽配置表单并生成JSON
 */
@Composable
fun FormDesigner() {
    var formConfig by remember { mutableStateOf(FormConfig()) }
    var selectedField by remember { mutableStateOf<FormField?>(null) }
    var jsonView by remember { mutableStateOf(false) }
    var previewMode by remember { mutableStateOf(false) }
    
    Column(modifier = Modifier.fillMaxSize()) {
        // 顶部工具栏
        Surface(
            modifier = Modifier.fillMaxWidth(),
            tonalElevation = 2.dp,
            color = MaterialTheme.colorScheme.surface
        ) {
            Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "表单设计器",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.weight(1f))
                
                // 模式切换按钮
                IconButton(onClick = { jsonView = !jsonView }) {
                    Icon(
                        if (jsonView) Icons.Default.Code else Icons.Default.Edit,
                        contentDescription = if (jsonView) "代码视图" else "设计视图"
                    )
                }
                
                // 预览按钮
                IconButton(onClick = { previewMode = !previewMode }) {
                    Icon(
                        if (previewMode) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = if (previewMode) "关闭预览" else "预览"
                    )
                }
                
                // 保存按钮
                Button(
                    onClick = { /* 保存配置 */ },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Icon(
                        Icons.Default.Save,
                        contentDescription = "保存",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("保存")
                }
            }
        }
        
        if (previewMode) {
            // 预览模式
            FormPreview(formConfig = formConfig)
        } else {
            // 设计器模式
            Row(modifier = Modifier.weight(1f)) {
                // 组件面板
                ComponentPanel(
                    onComponentDrag = { componentType ->
                        val newField = FormField(
                            id = "field_${System.currentTimeMillis()}",
                            name = "field_${formConfig.fields.size + 1}",
                            label = "字段 ${formConfig.fields.size + 1}",
                            type = componentType
                        )
                        formConfig = formConfig.copy(
                            fields = formConfig.fields + newField
                        )
                        selectedField = newField
                    },
                    modifier = Modifier.width(240.dp)
                )
                
                // 设计区域
                if (jsonView) {
                    // JSON编辑视图
                    val jsonString = formConfig.toJson()
                    Surface(
                        modifier = Modifier.weight(1f).padding(8.dp),
                        tonalElevation = 1.dp
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "JSON配置",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            TextField(
                                value = jsonString,
                                onValueChange = { newJson ->
                                    try {
                                        // 尝试解析JSON
                                        val newConfig = FormConfig.fromJson(newJson)
                                        formConfig = newConfig
                                    } catch (e: Exception) {
                                        // 解析错误，不更新配置
                                    }
                                },
                                modifier = Modifier.fillMaxSize(),
                                textStyle = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                } else {
                    // 可视化设计视图
                    Surface(
                        modifier = Modifier.weight(1f).padding(8.dp),
                        tonalElevation = 1.dp
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "设计区域",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            DesignCanvas(
                                formConfig = formConfig,
                                selectedField = selectedField,
                                onFieldSelected = { selectedField = it },
                                onFieldRemoved = { fieldToRemove ->
                                    formConfig = formConfig.copy(
                                        fields = formConfig.fields.filter { it.id != fieldToRemove.id }
                                    )
                                    if (selectedField?.id == fieldToRemove.id) {
                                        selectedField = null
                                    }
                                },
                                onFieldMoved = { fromIndex, toIndex ->
                                    val newFields = formConfig.fields.toMutableList()
                                    val field = newFields.removeAt(fromIndex)
                                    newFields.add(toIndex, field)
                                    formConfig = formConfig.copy(fields = newFields)
                                }
                            )
                        }
                    }
                }
                
                // 属性编辑器
                PropertiesEditor(
                    selectedField = selectedField,
                    onFieldUpdated = { updatedField ->
                        formConfig = formConfig.copy(
                            fields = formConfig.fields.map { 
                                if (it.id == updatedField.id) updatedField else it
                            }
                        )
                        selectedField = updatedField
                    },
                    modifier = Modifier.width(320.dp)
                )
            }
        }
    }
}

/**
 * 设计画布
 * 显示和编辑表单字段
 */
@Composable
fun DesignCanvas(
    formConfig: FormConfig,
    selectedField: FormField?,
    onFieldSelected: (FormField) -> Unit,
    onFieldRemoved: (FormField) -> Unit,
    onFieldMoved: (Int, Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant)
            .padding(16.dp)
    ) {
        items(formConfig.fields) { field ->
            val isSelected = selectedField?.id == field.id
            
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .border(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                    )
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)
                        else MaterialTheme.colorScheme.surface
                    )
                    .padding(8.dp),
                onClick = { onFieldSelected(field) }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // 字段拖动手柄
                    Icon(
                        Icons.Default.DragIndicator,
                        contentDescription = "拖动",
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    
                    // 字段内容预览
                    val component = getComponentByType(field.type)
                    if (component != null) {
                        FormComponent(
                            field = field,
                            component = component,
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        Text(
                            "未知组件类型: ${field.type}",
                            modifier = Modifier.weight(1f)
                        )
                    }
                    
                    // 删除按钮
                    IconButton(onClick = { onFieldRemoved(field) }) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "删除",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
        
        // 空表单提示
        if (formConfig.fields.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "从左侧拖拽组件到这里开始设计表单",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
} 