package com.addzero.web.ui.hooks.form

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.addzero.web.infra.jimmer.fromMap
import com.addzero.web.ui.hooks.UseHook
import com.addzero.web.ui.hooks.table.entity.IColumn

class UseDynamicForm<E : Any>(
    /**
     * 几栏显示
     */
    private val columnCount: Int = 2,
) : UseHook<UseDynamicForm<E>> {

    var currentSelectItem by mutableStateOf(null as E?)
    var columns by mutableStateOf(listOf<IColumn<E>>())
    
    // 表单验证错误信息
    private var validationErrors by mutableStateOf(mutableStateMapOf<String, String>())
    
    // 存储修改过的属性值
    private var modifiedFields by mutableStateOf(mutableStateMapOf<String, Any?>())

    /**
     * 更新表单项，优化重组逻辑
     */
    fun updateFormItem(newItem: E?) {
        currentSelectItem = null
        currentSelectItem = newItem
        modifiedFields.clear()
    }

    fun validate(): Boolean {
        columns.forEach { column ->
            if (!column.validator(currentSelectItem!!)) {
                validationErrors[column.title] = column.errorMessage
            }
        }
        return validationErrors.isEmpty()
    }

    /**
     * 批量更新表单项属性
     */
    fun batchUpdateFields(updates: Map<String, Any?>) {
        val currentItem = currentSelectItem ?: return
        val newItem = currentItem.fromMap(updates) { fieldName, value ->
            modifiedFields[fieldName] = value
        }
        currentSelectItem = newItem
    }

    /**
     * 获取修改过的字段
     */
    fun getModifiedFields(): Map<String, Any?> = modifiedFields.toMap()

    override val render: @Composable () -> Unit
        get() = {
            val useDynamicForm = this.getState()
            MultiColumnContainer(
                columnCount = columnCount,
                size = columns.size,
                horizontalSpacing = 16,
                verticalSpacing = 8
            ) { index ->
                FormItem(columns[index], useDynamicForm, currentSelectItem)
            }
        }
}

