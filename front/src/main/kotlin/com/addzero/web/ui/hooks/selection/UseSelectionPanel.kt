package com.addzero.web.ui.hooks.selection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.web.ui.hooks.UseHook

/**
 * 选择面板Hook
 * @param [title] 标题
 * @param [items] 选项列表
 * @param [value] 初始选中值
 * @param [onValueChange] 值变化回调
 * @param [isSingleSelect] 是否单选
 * @param [getLabel] 获取选项标签的函数
 * @param [defaultItem] 默认选项
 * @param [defaultSelection] 默认选中项
 */
class UseSelectionPanel<T>(
    val title: String,
    val items: List<T>,
    initialValue: Set<T>? = null,
    val onValueChange: (Set<T>) -> Unit = {},
    val isSingleSelect: Boolean = false,
    val getLabel: (T) -> String,
    val defaultItem: T? = null,
    val defaultSelection: Set<T> = emptySet(),
    override val modifier: Modifier = Modifier
) : UseHook<UseSelectionPanel<T>> {

    var currentValue by mutableStateOf(initialValue ?: defaultSelection)

    override val render: @Composable () -> Unit = {
        Column(modifier = modifier) {
            Text(title, style = MaterialTheme.typography.titleSmall)
            Row(
                modifier = Modifier.padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items.forEach { item ->
                    val isSelected = if (defaultItem != null && item == defaultItem) {
                        currentValue.isEmpty()
                    } else {
                        currentValue.contains(item)
                    }
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            val newSelection = when {
                                defaultItem != null && item == defaultItem -> emptySet()
                                isSingleSelect -> setOf(item)
                                else -> {
                                    currentValue.toMutableSet().apply {
                                        if (isSelected) remove(item) else add(item)
                                    }
                                }
                            }
                            currentValue = newSelection
                            onValueChange(newSelection)
                        },
                        label = { Text(getLabel(item)) }
                    )
                }
            }
        }
    }

    /**
     * 获取当前选中值
     * @return 当前选中值
     */
    fun getValue(): Set<T> = currentValue

    /**
     * 设置选中值
     * @param value 要设置的值
     */
    fun setValue(value: Set<T>) {
        currentValue = value
        onValueChange(value)
    }

    /**
     * 清除选择
     */
    fun clear() {
        setValue(emptySet())
    }
}

/**
 * 创建选择面板Hook的便捷函数
 */
@Composable
fun <T> useSelectionPanel(
    title: String,
    items: List<T>,
    initialValue: Set<T>? = null,
    onValueChange: (Set<T>) -> Unit = {},
    isSingleSelect: Boolean = false,
    getLabel: (T) -> String,
    defaultItem: T? = null,
    defaultSelection: Set<T> = emptySet(),
    modifier: Modifier = Modifier
): UseSelectionPanel<T> {
    return remember {
        UseSelectionPanel(
            title = title,
            items = items,
            initialValue = initialValue,
            onValueChange = onValueChange,
            isSingleSelect = isSingleSelect,
            getLabel = getLabel,
            defaultItem = defaultItem,
            defaultSelection = defaultSelection,
            modifier = modifier
        )
    }.getState()
}