package com.addzero.web.ui.hooks.form

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.addzero.common.kt_util.toNotBlankStr
import com.addzero.web.ui.hooks.UseHook
import com.addzero.web.ui.hooks.table.common.UseTableContent
import com.addzero.web.ui.hooks.table.entity.AddColumn
import com.addzero.web.ui.hooks.table.entity.RenderType.*
import org.babyfish.jimmer.Formula
import kotlin.reflect.full.hasAnnotation

@Composable
fun <E : Any> FormItem(columnMeta: AddColumn<E>, useDynamicForm: UseDynamicForm<E>?) {
    val currentFormItem = useDynamicForm?.currentFormItem
    val renderType = columnMeta.renderType
    val getFun = columnMeta.getFun
    val setFun = columnMeta.setFun

    val fieldValue = currentFormItem?.let { getFun(it) }

    val validRes = currentFormItem?.let { columnMeta.validator(it) }
    //如果是计算属性
    val property = columnMeta.currentField.property
    val iscacle = property.hasAnnotation<Transient>() || property.hasAnnotation<Formula>()

    val text = fieldValue.toNotBlankStr()

    Column(modifier = Modifier.fillMaxWidth()) {
//        Text(
//            text = columnMeta.title,
//            style = MaterialTheme.typography.bodySmall,
//            modifier = Modifier.fillMaxWidth(),
//            textAlign = androidx.compose.ui.text.style.TextAlign.Center
//        )
//        Spacer(modifier = Modifier.height(4.dp))
        when (renderType) {
            TEXT -> {
                OutlinedTextField(
                    enabled = !iscacle,
                    label = {
                        renderLabel(columnMeta)

                    },
                    value = text,
                    onValueChange = { newval ->
                        useDynamicForm ?: return@OutlinedTextField
                        val newItem = setFun(currentFormItem, columnMeta, newval)
                        useDynamicForm.currentFormItem = newItem
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
                    enabled = !iscacle,
                    label = {
                        renderLabel(columnMeta)
                    },
                    value = text,
                    onValueChange = { newval ->
                        useDynamicForm ?: return@OutlinedTextField
                        val newItem = setFun(currentFormItem, columnMeta, newval)
                        useDynamicForm.currentFormItem = newItem
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text(columnMeta.placeholder) },
                    isError = validRes == false,
                    //                singleLine = true,
                    maxLines = 5,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )

            }

            SWITCH -> {
                Column {
                    Text(columnMeta.title)
                    Switch(
                        enabled = !iscacle,
                        checked = fieldValue == true,
                        onCheckedChange = {
                            useDynamicForm ?: return@Switch
                            val newItem = setFun(currentFormItem, columnMeta, it)
                            useDynamicForm.currentFormItem = newItem
                        }
                    )

                }
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
}

@Composable
private fun <E : Any> renderLabel(columnMeta: AddColumn<E>) {
    if (columnMeta.required) {
        Text("* ${columnMeta.title}")
    } else {
        Text(columnMeta.title)
    }
}


class UseDynamicForm<E : Any>(
    private val useTableContent: UseTableContent<E>,
    private val columnCount: Int = 2,
) : UseHook<UseDynamicForm<E>> {
    // 表单验证错误信息
    private var validationErrors by mutableStateOf(mutableStateMapOf<String, String>())

    var currentFormItem by mutableStateOf(useTableContent.currentSelectItem)
    var currentColumn: AddColumn<E>? by mutableStateOf(null)

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
            val useDynamicForm = this.getState()
            val useTableContent = useTableContent.getState()
            val columns = useTableContent.columns
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
                            currentColumn = columns[colIndex]
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth()
                            ) {
                                FormItem(currentColumn!!, useDynamicForm)
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

