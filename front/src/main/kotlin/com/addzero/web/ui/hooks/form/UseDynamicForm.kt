package com.addzero.web.ui.hooks.form

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.web.ui.hooks.UseHook
import com.addzero.web.ui.hooks.table.common.UseTableContent
import com.addzero.web.ui.hooks.table.entity.AddColumn

class UseDynamicForm<E : Any>(
    private val useTableContent: UseTableContent<E>,
    private val columnCount: Int = 2,
) : UseHook<UseDynamicForm<E>> {
    // 表单验证错误信息
    private var validationErrors by mutableStateOf(mutableStateMapOf<String, String>())

    var currentFormItem by mutableStateOf(useTableContent.currentSelectItem)

    fun validate(): Boolean {
        useTableContent.columns.forEach { column ->
            if (!column.validator(currentFormItem!!)) {
                validationErrors[column.title] = column.errorMessage
            }
        }
        return validationErrors.isEmpty()
    }

    fun refreshFromFieldValue(addColumn: AddColumn<E>, newval: Any?): Unit {
//        val currentSelectItem = useTableContent.currentSelectItem
        val setFun = addColumn.setFun
        currentFormItem = setFun(currentFormItem, addColumn, newval)
    }

    override val render: @Composable () -> Unit
        get() = {
            val useTableContent = useTableContent.getState()
            val columns = useTableContent.columns
            // 计算每行的列数和总行数
            val itemsPerRow = columnCount
            val rowCount = (columns.size + itemsPerRow - 1) / itemsPerRow


            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier.fillMaxWidth().verticalScroll(scrollState).padding(16.dp)
            ) {
                // 按行渲染表单项
                for (rowIndex in 0..<rowCount) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        // 计算当前行的起始和结束索引
                        val startIndex = rowIndex * itemsPerRow
                        val endIndex = minOf(startIndex + itemsPerRow, columns.size)

                        // 渲染当前行的表单项
                        for (colIndex in startIndex until endIndex) {
                            val column = columns[colIndex]
                            Box(modifier = Modifier.weight(1f).padding(8.dp)) {
                                Column {
                                    // 标题和必填标记
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        FormItem(column, getState())
                                    }
                                }
                            }

                            // 如果当前行的列数不足，添加空白占位
                            if (endIndex - startIndex < itemsPerRow) {
                                for (i in 0 until (itemsPerRow - (endIndex - startIndex))) {
                                    Box(modifier = Modifier.weight(1f).padding(8.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
}

