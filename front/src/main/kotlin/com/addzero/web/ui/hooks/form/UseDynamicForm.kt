package com.addzero.web.ui.hooks.form

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.addzero.common.kt_util.toNotBlankStr
import com.addzero.web.ui.hooks.UseHook
import com.addzero.web.ui.hooks.table.common.UseTableContent
import com.addzero.web.ui.hooks.table.entity.AddColumn
import com.addzero.web.ui.hooks.table.entity.RenderType.*

@Composable
fun <E : Any> FormItem(columnMeta: AddColumn<E>, useDynamicForm: UseDynamicForm<E>?) {
    val currentFormItem = useDynamicForm?.currentFormItem

    val renderType = columnMeta.renderType
    val getFun = columnMeta.getFun
    val setFun = columnMeta.setFun

    val fieldValue = currentFormItem?.let { getFun(it) }

    val validRes = currentFormItem?.let { columnMeta.validator(it) }

    val text = fieldValue.toNotBlankStr()
    when (renderType) {
        TEXT -> {
            OutlinedTextField(
                value = text,
                onValueChange = { newval ->
                    useDynamicForm ?: return@OutlinedTextField
                    useDynamicForm.currentFormItem = setFun(currentFormItem, columnMeta, newval)
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(columnMeta.placeholder) },
                isError = validRes == false,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
        }

        IMAGE -> {
            Text(text)
        }

        CUSTOM -> {
        }

        TEXTAREA -> {
            OutlinedTextField(
                value = text,
                onValueChange = { newval ->

                    useDynamicForm ?: return@OutlinedTextField


                    useDynamicForm.currentFormItem = setFun(currentFormItem, columnMeta, newval)
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(columnMeta.placeholder) },
                isError = !columnMeta.validator(currentFormItem),
//                singleLine = true,
                maxLines = 5,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )

        }

        SWITCH -> {
            Switch(
                checked = fieldValue == true,
                onCheckedChange = {

                    useDynamicForm ?: return@Switch
                    useDynamicForm.currentFormItem = setFun(currentFormItem, columnMeta, it)
                }
            )
        }

        TAG -> {
            Text(text)
        }

        NUMBER -> {}
        LINK -> {}
        DATE -> {}
        DATETIME -> {}
        SELECT -> {}
        MULTISELECT -> {}
        CHECKBOX -> {}
        RADIO -> {}
        CODE -> {}
        HTML -> {}
        MONEY -> {}
        CURRENCY -> {}
        PERCENT -> {}
        BAR -> {}
        TREE -> {}
        COMPUTED -> {}
        AUTO_COMPLETE -> {}
        FILE -> {}
    }

}

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

