package com.addzero.web.ui.hooks.form

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.web.ui.hooks.UseHook
import com.addzero.web.ui.hooks.table.common.UseTableContent

 class UseDynamicForm<E : Any>(
    private val useTableContent: UseTableContent<E>,
    private val columnCount: Int = 2,
) : UseHook<UseDynamicForm<E>> {

    // 表单验证错误信息
    private var validationErrors by mutableStateOf(mutableStateMapOf<String, String>())

//    var currentFormItem by mutableStateOf(currentSelectItem)


//    private var currentColumn: IColumn<E>? by mutableStateOf(null)

    /**
     * 更新表单项，优化重组逻辑
     * 通过先设置为null再设置新值，强制触发一次更新
     */
    fun updateFormItem(newItem: E?) {
        // 直接赋值可能不会触发所有依赖该状态的组件重组
        println(useTableContent.currentSelectItem.hashCode())
        useTableContent.currentSelectItem = null
        // 设置新值
        useTableContent.currentSelectItem = newItem
        // 打印日志，确认更新
        println("更新表单项: ${useTableContent.currentSelectItem}")
    }

    fun validate(): Boolean {
        useTableContent.columns.forEach { column ->
            if (!column.validator(useTableContent.currentSelectItem!!)) {
                validationErrors[column.title] = column.errorMessage
            }
        }
        return validationErrors.isEmpty()
    }


    override val render: @Composable () -> Unit
        get() = {
            val useDynamicForm = this.getState()
            val useTableContent = useTableContent.getState()
            val columns = useTableContent.columns
//            .filter { it.currentField != null }
            // 计算每行的列数和总行数
            val itemsPerRow = columnCount
            val rowCount = (columns.size + itemsPerRow - 1) / itemsPerRow

            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                // 按行渲染表单项
                for (rowIndex in 0..<rowCount) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // 计算当前行的起始和结束索引
                        val startIndex = rowIndex * itemsPerRow
                        val endIndex = minOf(startIndex + itemsPerRow, columns.size)

                        // 渲染当前行的表单项
                        for (colIndex in startIndex until endIndex) {
                            val addColumn = columns[colIndex]
//                            currentColumn = addColumn
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth()
                            ) {
                                FormItem(addColumn!!, useDynamicForm, useTableContent)
                            }
                        }

                        // 如果当前行的列数不足，添加空的占位Box以保持对齐
                        if (endIndex - startIndex < itemsPerRow) {
                            repeat(itemsPerRow - (endIndex - startIndex)) {
                                Box(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
}

