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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Rect
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.ui.unit.IntSize

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
    
    // 在 FormDesigner 函数中添加额外状态来跟踪网格吸附
    var hoverGridRow by remember { mutableStateOf(-1) }
    var hoverGridColumn by remember { mutableStateOf(-1) }
    var isDraggingComponent by remember { mutableStateOf(false) }
    var draggedComponentType by remember { mutableStateOf("") }
    var dragPosition by remember { mutableStateOf(Offset.Zero) }
    
    // 添加放置成功的状态追踪
    var lastPlacedFieldId by remember { mutableStateOf<String?>(null) }
    var showPlacementAnimation by remember { mutableStateOf(false) }
    
    // 判断当前是否有字段在拖拽中
    val isDragging = draggingFieldId != null
    
    // 拖拽操作处理函数
    val handleDragStart = { fieldId: String, position: Offset ->
        draggingFieldId = fieldId
        initialDragPosition = position
        dragOffset = Offset.Zero
    }
    
    val handleDragEnd = {
        // 获取被拖拽的字段
        val draggedField = formConfig.fields.find { it.id == draggingFieldId }
        if (draggedField != null) {
            val fromIndex = formConfig.fields.indexOf(draggedField)
            
            // 计算拖拽后的新位置索引
            // 判断拖拽方向并估算目标位置
            val distance = kotlin.math.abs(dragOffset.y)
            
            // 只在拖拽距离超过阈值时才执行移动操作
            if (distance > 30) {
                val direction = if (dragOffset.y > 0) 1 else -1  // 下为正，上为负
                
                // 根据拖拽距离计算目标位置
                // 每70dp移动一个位置
                val moveCount = (distance / 70).toInt().coerceAtLeast(0)
                var toIndex = fromIndex + (direction * moveCount)
                
                // 确保目标索引在有效范围内
                toIndex = toIndex.coerceIn(0, formConfig.fields.size - 1)
                
                // 如果位置确实发生变化，更新字段顺序
                if (fromIndex != toIndex) {
                    val newFields = formConfig.fields.toMutableList()
                    val field = newFields.removeAt(fromIndex)
                    newFields.add(toIndex, field)
                    
                    // 更新表单配置
                    formConfig = formConfig.copy(fields = newFields)
                    
                    // 添加视觉或听觉反馈，表示排序成功
                    // 在实际应用中，您可能需要添加动画或声音提示
                }
            }
        }
        
        // 重置拖拽状态
        draggingFieldId = null
        dragOffset = Offset.Zero
    }
    
    val handleDragUpdate = { amount: Offset ->
        // 累加拖拽位移
        dragOffset += amount
    }
    
    // 组件面板中开始拖拽的处理函数
    val handleComponentDragStart = { componentType: String ->
        isDraggingComponent = true
        draggedComponentType = componentType
    }

    // 组件面板中拖拽移动的处理函数
    val handleComponentDragMove = { position: Offset ->
        dragPosition = position
        
        // 根据拖拽位置计算目标网格单元格
        // 这需要在实际设计区域中实现
    }

    // 组件面板中拖拽结束的处理函数
    val handleComponentDragEnd = { dropped: Boolean ->
        if (dropped && hoverGridRow >= 0 && hoverGridColumn >= 0) {
            // 计算插入位置
            val insertIndex = hoverGridRow * formConfig.columnCount + hoverGridColumn
            
            // 创建新字段
            val newField = FormField(
                id = "field_${System.currentTimeMillis()}",
                name = "field_${formConfig.fields.size + 1}",
                label = "字段 ${formConfig.fields.size + 1}",
                type = draggedComponentType
            )
            
            // 插入新字段到指定位置
            val updatedFields = formConfig.fields.toMutableList()
            
            // 确保插入位置有效
            val validInsertIndex = insertIndex.coerceIn(0, updatedFields.size)
            updatedFields.add(validInsertIndex, newField)
            
            // 更新表单配置
            formConfig = formConfig.copy(fields = updatedFields)
            
            // 选中新添加的字段
            selectedField = newField
            
            // 触发放置动画
            lastPlacedFieldId = newField.id
            showPlacementAnimation = true
            
            // 延迟重置动画状态
            MainScope().launch {
                // 先显示动画一段时间
                delay(800)
                showPlacementAnimation = false
                
                // 短暂延迟后滚动到新添加的组件位置（这部分需要在实际代码中实现）
                delay(200)
                // 可以添加滚动到新添加组件的逻辑
            }
        }
        
        // 重置拖拽状态
        isDraggingComponent = false
        draggedComponentType = ""
        dragPosition = Offset.Zero
        hoverGridRow = -1
        hoverGridColumn = -1
    }
    
    // 在 FormDesigner 函数中添加设计区域状态
    var isInDesignArea by remember { mutableStateOf(false) }
    
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
                    onComponentDrag = handleComponentDragStart,
                    onComponentDragStart = handleComponentDragStart,
                    onComponentDragMove = handleComponentDragMove,
                    onComponentDragEnd = handleComponentDragEnd,
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
                                isDragging = isDragging,
                                draggingFieldId = draggingFieldId,
                                dragOffset = dragOffset,
                                onDragStart = handleDragStart,
                                onDragEnd = handleDragEnd,
                                onDragUpdate = handleDragUpdate,
                                isDraggingComponent = isDraggingComponent,
                                draggedComponentType = draggedComponentType,
                                dragPosition = dragPosition,
                                hoverGridRow = hoverGridRow,
                                hoverGridColumn = hoverGridColumn,
                                onGridCellHover = { row, col ->
                                    hoverGridRow = row
                                    hoverGridColumn = col
                                },
                                showPlacementAnimation = showPlacementAnimation,
                                lastPlacedFieldId = lastPlacedFieldId,
                                isInDesignArea = isInDesignArea,
                                onDesignAreaUpdated = { inArea -> isInDesignArea = inArea }
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

    // 在 DesignGrid 函数中添加可拖放区域的视觉指示
    if (isDraggingComponent && isInDesignArea) {
        // 添加一个微弱的高亮背景，表示此区域可以放置组件
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.03f)
                )
        )
    }
}

/**
 * 设计画布
 * 可视化编辑表单字段
 */
@Composable
fun DesignCanvas(
    formConfig: FormConfig,
    selectedField: FormField?,
    onFieldSelected: (FormField) -> Unit,
    onFieldRemoved: (FormField) -> Unit,
    onFieldMoved: (Int, Int) -> Unit,
    onUpdateFormConfig: (FormConfig) -> Unit,
    isDragging: Boolean = false,
    draggingFieldId: String? = null,
    dragOffset: Offset = Offset.Zero,
    onDragStart: (String, Offset) -> Unit = { _, _ -> },
    onDragEnd: () -> Unit = {},
    onDragUpdate: (Offset) -> Unit = {},
    isDraggingComponent: Boolean = false,
    draggedComponentType: String = "",
    dragPosition: Offset = Offset.Zero,
    hoverGridRow: Int = -1,
    hoverGridColumn: Int = -1,
    onGridCellHover: (Int, Int) -> Unit = { _, _ -> },
    showPlacementAnimation: Boolean = false,
    lastPlacedFieldId: String? = null,
    isInDesignArea: Boolean = false,
    onDesignAreaUpdated: (Boolean) -> Unit = {}
) {
    // 添加可拖放区域的边界检测
    val designAreaBounds = remember { mutableStateOf(Rect.Zero) }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant)
            .padding(16.dp)
            .onGloballyPositioned { coordinates ->
                // 记录设计区域的边界
                val rect = Rect(
                    left = coordinates.positionInRoot().x,
                    top = coordinates.positionInRoot().y,
                    right = coordinates.positionInRoot().x + coordinates.size.width,
                    bottom = coordinates.positionInRoot().y + coordinates.size.height
                )
                designAreaBounds.value = rect
            }
    ) {
        // 显示网格背景
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
            // 表单标题信息
            item {
                Text(
                    formConfig.title.ifEmpty { "表单标题" },
                    style = MaterialTheme.typography.headlineSmall
                )
                if (formConfig.description.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        formConfig.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // 根据列数选择布局方式
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
                        onUpdateFormConfig = onUpdateFormConfig,
                        isDraggingComponent = isDraggingComponent,
                        draggedComponentType = draggedComponentType,
                        dragPosition = dragPosition,
                        hoverGridRow = hoverGridRow,
                        hoverGridColumn = hoverGridColumn,
                        onGridCellHover = onGridCellHover,
                        showPlacementAnimation = showPlacementAnimation,
                        lastPlacedFieldId = lastPlacedFieldId,
                        isInDesignArea = isInDesignArea,
                        onDesignAreaUpdated = onDesignAreaUpdated
                    )
                } else {
                    // 单列布局
                    Column {
                        formConfig.fields.forEach { field ->
                            FieldItem(
                                field = field,
                                isSelected = selectedField?.id == field.id,
                                onFieldSelected = onFieldSelected,
                                onFieldRemoved = onFieldRemoved,
                                modifier = Modifier.fillMaxWidth(),
                                draggingFieldId = draggingFieldId,
                                dragOffset = dragOffset,
                                onDragStart = onDragStart,
                                onDragEnd = onDragEnd,
                                onDragUpdate = onDragUpdate,
                                isNewlyPlaced = showPlacementAnimation && field.id == lastPlacedFieldId
                            )
                        }
                    }
                    
                    // 无字段时显示提示
                    if (formConfig.fields.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "从左侧拖拽组件到此处",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
        
        // 如果正在拖拽，显示拖拽指示器
        if (isDragging) {
            // 拖拽状态指示器 - 悬浮在右上角
            Surface(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 16.dp, end = 16.dp)
                    .shadow(4.dp, shape = MaterialTheme.shapes.small),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(
                        Icons.Default.DragIndicator,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        "正在重新排序...",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
        
        // 在 DesignCanvas 中优化拖放提示的显示逻辑
        // 提示只应在拖拽开始后的短暂延迟后显示，避免闪烁
        var showDropHint by remember { mutableStateOf(false) }

        LaunchedEffect(isDraggingComponent) {
            if (isDraggingComponent) {
                // 延迟显示提示，避免用户快速拖放时的闪烁
                delay(300)
                showDropHint = true
            } else {
                showDropHint = false
            }
        }

        // 如果正在拖拽组件但尚未悬停在任何有效网格上，显示提示
        if (isDraggingComponent && isInDesignArea && hoverGridRow < 0 && hoverGridColumn < 0 && showDropHint) {
            Surface(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp)
                    .alpha(0.9f),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 4.dp,
                shape = MaterialTheme.shapes.medium
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Default.TouchApp,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        "将组件拖放到表单中的任意位置",
                        style = MaterialTheme.typography.bodyMedium
                    )
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
    onUpdateFormConfig: (FormConfig) -> Unit,
    isDraggingComponent: Boolean = false,
    draggedComponentType: String = "",
    dragPosition: Offset = Offset.Zero,
    hoverGridRow: Int = -1,
    hoverGridColumn: Int = -1,
    onGridCellHover: (Int, Int) -> Unit = { _, _ -> },
    showPlacementAnimation: Boolean = false,
    lastPlacedFieldId: String? = null,
    isInDesignArea: Boolean = false,
    onDesignAreaUpdated: (Boolean) -> Unit = {}
) {
    // 将字段分组为行
    val rows = formConfig.fields.chunked(formConfig.columnCount)
    
    // 跟踪所有网格单元格的位置 - 修改为使用 IntSize 而不是 Size
    val gridCellPositions = remember { mutableStateMapOf<Pair<Int, Int>, Pair<Offset, IntSize>>() }
    
    // 添加可拖放区域的边界检测
    val designAreaBounds = remember { mutableStateOf(Rect.Zero) }
    
    // 在 DesignGrid 函数中增加设计区域状态检测
    var isInDesignArea by remember { mutableStateOf(false) }
    
    // 检测整个网格区域的位置
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .onGloballyPositioned { coordinates ->
                // 记录设计区域的边界
                val rect = Rect(
                    left = coordinates.positionInRoot().x,
                    top = coordinates.positionInRoot().y,
                    right = coordinates.positionInRoot().x + coordinates.size.width,
                    bottom = coordinates.positionInRoot().y + coordinates.size.height
                )
                designAreaBounds.value = rect
            }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // 当拖拽位置更新时检测单元格位置
            LaunchedEffect(dragPosition) {
                if (isDraggingComponent) {
                    // 检查拖拽位置是否在设计区域内
                    val inDesignArea = dragPosition.x >= designAreaBounds.value.left &&
                                      dragPosition.x <= designAreaBounds.value.right &&
                                      dragPosition.y >= designAreaBounds.value.top &&
                                      dragPosition.y <= designAreaBounds.value.bottom
                    
                    // 通知父组件更新状态
                    onDesignAreaUpdated(inDesignArea)
                    
                    if (inDesignArea) {
                        // 只有在设计区域内才执行网格检测
                        // 查找距离最近的网格单元格
                        var closestCell: Pair<Int, Int>? = null
                        var minDistance = Float.MAX_VALUE
                        
                        gridCellPositions.forEach { (cell, bounds) ->
                            val (cellOffset, cellSize) = bounds
                            val centerX = cellOffset.x + cellSize.width / 2
                            val centerY = cellOffset.y + cellSize.height / 2
                            
                            val distance = kotlin.math.sqrt(
                                (dragPosition.x - centerX) * (dragPosition.x - centerX) +
                                (dragPosition.y - centerY) * (dragPosition.y - centerY)
                            )
                            
                            if (distance < minDistance) {
                                minDistance = distance
                                closestCell = cell
                            }
                        }
                        
                        // 只有在足够接近网格时才高亮显示
                        if (minDistance < 200) { // 可以调整这个阈值
                            closestCell?.let { (row, col) ->
                                onGridCellHover(row, col)
                            }
                        } else {
                            onGridCellHover(-1, -1)
                        }
                    } else {
                        // 不在设计区域内，重置高亮
                        onGridCellHover(-1, -1)
                    }
                }
            }
            
            // 渲染每一行
            rows.forEachIndexed { rowIndex, rowFields ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    // 渲染行中的每个字段
                    for (colIndex in 0 until formConfig.columnCount) {
                        // 检查当前单元格是否为悬停目标
                        val isHoverTarget = rowIndex == hoverGridRow && colIndex == hoverGridColumn
                        
                        // 优化单元格高亮动画
                        val targetAlpha = if (isHoverTarget && isDraggingComponent) 0.2f else 0f
                        val highlightAlpha by animateFloatAsState(
                            targetValue = targetAlpha,
                            animationSpec = tween(150),
                            label = "highlightAlpha"
                        )

                        // 使用动画值构建单元格修饰符
                        val cellModifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 2.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primary.copy(alpha = highlightAlpha),
                                shape = MaterialTheme.shapes.small
                            )
                            .border(
                                width = if (isHoverTarget && isDraggingComponent) 2.dp else 0.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = MaterialTheme.shapes.small
                            )
                        
                        Box(
                            modifier = cellModifier.onGloballyPositioned { coordinates ->
                                // 记录每个网格单元格的位置
                                val cellOffset = coordinates.positionInRoot()
                                val cellSize = coordinates.size
                                gridCellPositions[Pair(rowIndex, colIndex)] = Pair(cellOffset, cellSize)
                            }
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
                                    onDragUpdate = onDragUpdate,
                                    isNewlyPlaced = showPlacementAnimation && rowFields[colIndex].id == lastPlacedFieldId
                                )
                            } else {
                                // 没有字段时渲染改进后的空占位符
                                EmptyFieldPlaceholder(
                                    onClick = { 
                                        onAddField(rowIndex, colIndex)
                                    },
                                    isHighlighted = isHoverTarget && isDraggingComponent
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

    // 在 DesignGrid 函数中添加可拖放区域的视觉指示
    if (isDraggingComponent && isInDesignArea) {
        // 添加一个微弱的高亮背景，表示此区域可以放置组件
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.03f)
                )
        )
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
    onDragUpdate: (Offset) -> Unit = {},
    isNewlyPlaced: Boolean = false
) {
    // 计算当前字段是否正在被拖拽
    val isDragging = field.id == draggingFieldId
    
    // 放置动画
    val placementScale by animateFloatAsState(
        targetValue = if (isNewlyPlaced) 1f else 0.95f,
        animationSpec = if (isNewlyPlaced) {
            spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessLow
            )
        } else {
            spring()
        },
        label = "placementScale"
    )
    
    val placementAlpha by animateFloatAsState(
        targetValue = if (isNewlyPlaced) 1f else 0f,
        animationSpec = tween(300),
        label = "placementAlpha"
    )
    
    // 增强拖拽时的视觉效果
    val elevation = if (isDragging) 8.dp else 0.dp
    val alpha = if (isDragging) 0.7f else 1f
    val borderWidth = if (isSelected) 2.dp else 1.dp
    val borderColor = if (isSelected) 
        MaterialTheme.colorScheme.primary 
    else if (isDragging)
        MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
    else 
        MaterialTheme.colorScheme.outline
    
    val backgroundColor = when {
        isDragging -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        isSelected -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)
        else -> MaterialTheme.colorScheme.surface
    }
    
    Surface(
        modifier = modifier
            .padding(vertical = 4.dp)
            .shadow(elevation)
            .alpha(alpha)
            // 应用放置动画效果
            .then(
                if (isNewlyPlaced) {
                    Modifier
                        .scale(placementScale)
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = placementAlpha),
                            shape = RoundedCornerShape(4.dp)
                        )
                } else {
                    Modifier.border(
                        width = borderWidth,
                        color = borderColor
                    )
                }
            )
            .background(backgroundColor)
            .padding(8.dp),
        onClick = { if (!isDragging) onFieldSelected(field) }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // 拖动手柄
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
                    .background(
                        color = if (isDragging) 
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                        else 
                            Color.Transparent,
                        shape = MaterialTheme.shapes.small
                    )
                    .padding(4.dp)
            ) {
                Icon(
                    Icons.Default.DragIndicator,
                    contentDescription = "拖动",
                    tint = if (isDragging) 
                        MaterialTheme.colorScheme.primary
                    else 
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
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
    // 计算偏移量的长度 - 使用勾股定理
    val offsetLength = kotlin.math.sqrt(offset.x * offset.x + offset.y * offset.y)
    
    // 如果没有字段或拖拽距离很小，保持原位置
    if (fields.isEmpty() || offsetLength < 20) {
        return -1
    }
    
    // 计算垂直移动的行数
    val rowOffset = (offset.y / 70).toInt()
    
    // 计算水平移动的列数
    val colOffset = (offset.x / 100).toInt()
    
    // 根据垂直和水平移动计算新的索引
    // 向下移动，索引增加
    if (offset.y > 0) {
        return (fields.size - 1).coerceAtMost(fields.size / 2 + rowOffset)
    } 
    // 向上移动，索引减少
    else if (offset.y < 0) {
        return 0.coerceAtLeast(fields.size / 2 + rowOffset)
    }
    
    return -1  // 没有明显移动，保持原位置
} 