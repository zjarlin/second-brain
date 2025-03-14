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
import com.addzero.web.ui.designer.components.EmptyFieldPlaceholder
import com.addzero.web.ui.designer.components.FormComponent
import com.addzero.web.ui.designer.components.GridBackground
import com.addzero.web.ui.designer.components.getComponentByType
import com.addzero.web.ui.designer.editor.FormPropertiesEditor
import com.addzero.web.ui.designer.editor.PropertiesEditor
import com.addzero.web.ui.designer.models.FormConfig
import com.addzero.web.ui.designer.models.FormField
import com.addzero.web.ui.designer.preview.FormPreview
import com.addzero.web.ui.hooks.table.entity.RenderType
import com.addzero.web.ui.lowcode.metadata.FieldMetadata
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.detectDragGestures

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
    
    // 拖拽相关状态
    var draggingFieldId by remember { mutableStateOf<String?>(null) }
    var dragOffset by remember { mutableStateOf(Offset.Zero) }
    var initialDragPosition by remember { mutableStateOf(Offset.Zero) }
    
    // 判断当前是否有字段在拖拽中
    val isDragging = draggingFieldId != null
    
    // 拖拽操作处理函数
    val handleDragStart = { fieldId: String, position: Offset ->
        draggingFieldId = fieldId
        initialDragPosition = position
        dragOffset = Offset.Zero
    }
    
    val handleDragEnd = {
        // 根据最终位置计算新的字段顺序
        val draggedField = formConfig.fields.find { it.id == draggingFieldId }
        if (draggedField != null) {
            // 根据最终拖拽位置确定应该插入的索引
            // 这部分逻辑可以根据UI布局和需求进行调整
            val fromIndex = formConfig.fields.indexOf(draggedField)
            val toIndex = calculateDropIndex(dragOffset, formConfig.fields, formConfig.columnCount)
            
            if (fromIndex != toIndex && toIndex >= 0) {
                // 移动字段
                val newFields = formConfig.fields.toMutableList()
                newFields.removeAt(fromIndex)
                newFields.add(toIndex.coerceAtMost(newFields.size), draggedField)
                formConfig = formConfig.copy(fields = newFields)
            }
        }
        
        // 重置拖拽状态
        draggingFieldId = null
        dragOffset = Offset.Zero
    }
    
    val handleDragUpdate = { amount: Offset ->
        dragOffset += amount
    }
    
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
                                },
                                onUpdateFormConfig = { updatedConfig ->
                                    formConfig = updatedConfig
                                },
                                draggingFieldId = draggingFieldId,
                                dragOffset = dragOffset,
                                onDragStart = handleDragStart,
                                onDragEnd = handleDragEnd,
                                onDragUpdate = handleDragUpdate
                            )
                        }
                    }
                }
                
                // 属性编辑器
                Surface(
                    modifier = Modifier.width(320.dp),
                    tonalElevation = 1.dp
                ) {
                    if (selectedField != null) {
                        // 显示字段属性编辑器
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
                            onClose = { 
                                // 取消选择字段，返回到表单配置
                                selectedField = null 
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        // 显示表单属性编辑器
                        FormPropertiesEditor(
                            formConfig = formConfig,
                            onFormConfigUpdated = { updatedConfig ->
                                formConfig = updatedConfig
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
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
    onFieldMoved: (Int, Int) -> Unit,
    onUpdateFormConfig: (FormConfig) -> Unit,
    draggingFieldId: String?,
    dragOffset: Offset,
    onDragStart: (String, Offset) -> Unit,
    onDragEnd: () -> Unit,
    onDragUpdate: (Offset) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant)
            .padding(16.dp)
    ) {
        // 显示网格背景 (现在我们使用 GridBackground 组件)
        if (formConfig.columnCount > 1) {
            GridBackground(
                columnCount = formConfig.columnCount,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(top = 80.dp) // 为顶部信息腾出空间
            )
        }
        
        // 表单内容
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            // 表单配置信息
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    tonalElevation = 0.dp,
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(
                            formConfig.title,
                            style = MaterialTheme.typography.titleMedium
                        )
                        
                        if (formConfig.description.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                formConfig.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "列数: ${formConfig.columnCount}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        
                        Divider(modifier = Modifier.padding(top = 8.dp))
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
            
            // 表单字段列表 - 当列数大于1时，使用Grid布局渲染
            item {
                if (formConfig.columnCount > 1 && formConfig.fields.isNotEmpty()) {
                    DesignGrid(
                        formConfig = formConfig,
                        selectedField = selectedField,
                        onFieldSelected = onFieldSelected,
                        onFieldRemoved = onFieldRemoved,
                        draggingFieldId = draggingFieldId,
                        dragOffset = dragOffset,
                        onDragStart = onDragStart,
                        onDragEnd = onDragEnd,
                        onDragUpdate = onDragUpdate,
                        onAddField = { rowIndex, colIndex ->
                            // 计算新字段应该插入的位置
                            val insertPosition = rowIndex * formConfig.columnCount + colIndex
                            
                            // 确保插入位置有效
                            val validPosition = insertPosition.coerceIn(0, formConfig.fields.size)
                            
                            // 创建新字段
                            val newField = FormField(
                                id = "field_${System.currentTimeMillis()}",
                                name = "field_${formConfig.fields.size + 1}",
                                label = "字段 ${formConfig.fields.size + 1}",
                                type = "text" // 默认类型，可以根据需要修改
                            )
                            
                            // 插入新字段到指定位置
                            val updatedFields = formConfig.fields.toMutableList()
                            updatedFields.add(validPosition, newField)
                            
                            // 使用回调更新表单配置，而不是直接修改
                            onUpdateFormConfig(formConfig.copy(fields = updatedFields))
                            
                            // 选中新添加的字段
                            onFieldSelected(newField)
                        },
                        onUpdateFormConfig = onUpdateFormConfig
                    )
                } else {
                    // 单列布局时直接使用垂直布局
                    Column {
                        formConfig.fields.forEach { field ->
                            FieldItem(
                                field = field,
                                isSelected = selectedField?.id == field.id,
                                onFieldSelected = onFieldSelected,
                                onFieldRemoved = onFieldRemoved,
                                draggingFieldId = draggingFieldId,
                                dragOffset = dragOffset,
                                onDragStart = onDragStart,
                                onDragEnd = onDragEnd,
                                onDragUpdate = onDragUpdate
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * 设计网格
 * 以网格方式显示表单字段
 */
@Composable
fun DesignGrid(
    formConfig: FormConfig,
    selectedField: FormField?,
    onFieldSelected: (FormField) -> Unit,
    onFieldRemoved: (FormField) -> Unit,
    draggingFieldId: String?,
    dragOffset: Offset,
    onDragStart: (String, Offset) -> Unit,
    onDragEnd: () -> Unit,
    onDragUpdate: (Offset) -> Unit,
    onAddField: (Int, Int) -> Unit,
    onUpdateFormConfig: (FormConfig) -> Unit
) {
    // 将字段分组为行
    val rows = formConfig.fields.chunked(formConfig.columnCount)
    
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // 渲染每一行
        rows.forEachIndexed { rowIndex, rowFields ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                // 渲染行中的每个字段
                for (colIndex in 0 until formConfig.columnCount) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 2.dp)
                    ) {
                        if (colIndex < rowFields.size) {
                            // 有字段时渲染字段
                            FieldItem(
                                field = rowFields[colIndex],
                                isSelected = selectedField?.id == rowFields[colIndex].id,
                                onFieldSelected = onFieldSelected,
                                onFieldRemoved = onFieldRemoved,
                                modifier = Modifier.fillMaxWidth(),
                                draggingFieldId = draggingFieldId,
                                dragOffset = dragOffset,
                                onDragStart = onDragStart,
                                onDragEnd = onDragEnd,
                                onDragUpdate = onDragUpdate
                            )
                        } else {
                            // 没有字段时渲染改进后的空占位符
                            EmptyFieldPlaceholder(
                                onClick = { 
                                    // 通过回调添加字段
                                    onAddField(rowIndex, colIndex)
                                }
                            )
                        }
                    }
                }
            }
        }
        
        // 添加额外的空行，让用户可以在末尾继续添加字段
        if (formConfig.fields.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                for (colIndex in 0 until formConfig.columnCount) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 2.dp)
                    ) {
                        EmptyFieldPlaceholder(
                            onClick = { 
                                // 通过回调添加字段
                                val nextRowIndex = rows.size
                                onAddField(nextRowIndex, colIndex)
                            }
                        )
                    }
                }
            }
        }
    }
}

/**
 * 字段项
 * 显示单个字段
 */
@Composable
fun FieldItem(
    field: FormField,
    isSelected: Boolean,
    onFieldSelected: (FormField) -> Unit,
    onFieldRemoved: (FormField) -> Unit,
    modifier: Modifier = Modifier,
    draggingFieldId: String? = null,
    dragOffset: Offset = Offset.Zero,
    onDragStart: (String, Offset) -> Unit = { _, _ -> },
    onDragEnd: () -> Unit = {},
    onDragUpdate: (Offset) -> Unit = {}
) {
    // 计算当前字段是否正在被拖拽
    val isDragging = field.id == draggingFieldId
    
    Surface(
        modifier = modifier
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
        onClick = { if (!isDragging) onFieldSelected(field) }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // 字段拖动手柄
            Box(
                modifier = Modifier
                    .pointerInput(field.id) {
                        detectDragGestures(
                            onDragStart = { offset ->
                                // 开始拖拽时传递字段ID和初始位置
                                onDragStart(field.id, offset)
                            },
                            onDragEnd = {
                                onDragEnd()
                            },
                            onDragCancel = {
                                onDragEnd()
                            },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                onDragUpdate(dragAmount)
                            }
                        )
                    }
                    .padding(end = 8.dp)
            ) {
                Icon(
                    Icons.Default.DragIndicator,
                    contentDescription = "拖动",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            
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

// 根据拖拽位置计算应该插入的索引
private fun calculateDropIndex(
    offset: Offset,
    fields: List<FormField>,
    columnCount: Int
): Int {
    // 这个函数需要根据UI布局实际情况实现
    // 以下是简化的实现示例
    
    // 向下拖动较多时，放在后面
    return if (offset.y > 100) {
        fields.size
    } else {
        // 向上拖动较多时，放在前面
        0
    }
    
    // 注意：实际应用中应该更精确地计算放置位置
    // 例如根据每个字段的位置判断最近的放置点
} 