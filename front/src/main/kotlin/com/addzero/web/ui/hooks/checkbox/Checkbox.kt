package com.addzero.web.ui.hooks.checkbox

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 使用状态提升的复选框组件示例
 */
@Composable
fun <T> StatefulCheckboxGroup(
    title: String? = null,
    items: List<T> = emptyList(),
    value: Set<T> = emptySet(),
    onValueChange: (Set<T>) -> Unit = {},
    isMultiSelect: Boolean = false,
    modifier: Modifier = Modifier,
    getLabel: (T) -> String = { it.toString() }
) {
    // 使用remember管理内部状态
    var selected by remember(value) { mutableStateOf(value) }

    Row(modifier = modifier) {
        title?.let { Text(it) }

        items.forEach { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(4.dp)
            ) {
                val checked = selected.contains(item)
                Checkbox(
                    checked = checked,
                    onCheckedChange = {
                        // 更新内部状态
                        selected = if (isMultiSelect) {
                            if (checked) selected - item else selected + item
                        } else {
                            if (checked) emptySet() else setOf(item)
                        }
                        // 通知外部状态变化
                        onValueChange(selected)
                    }
                )
                Text(
                    text = getLabel(item),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

class CheckboxViewModel<T>(
    private val isMultiSelect: Boolean = false,
    private val onSelectionChange: (Set<T>) -> Unit = {}
) {
    var selected by mutableStateOf<Set<T>>(setOf())
        private set

    fun toggle(value: T) {
        selected = if (isMultiSelect) {
            if (selected.contains(value)) {
                selected - value
            } else {
                selected + value
            }
        } else {
            if (selected.contains(value)) {
                emptySet()
            } else {
                setOf(value)
            }
        }
        onSelectionChange(selected)
    }

    fun clear() {
        selected = emptySet()
    }
}

@Composable
fun <T> CheckboxGroup(
    title: String? = null,
    items: List<T> = emptyList(),
    isMultiSelect: Boolean = false,
    modifier: Modifier = Modifier,
    getLabel: (T) -> String = { it.toString() },
    onSelectionChange: (Set<T>) -> Unit = {}
) {
    val viewModel = remember { CheckboxViewModel(isMultiSelect, onSelectionChange) }

    Row(modifier = modifier) {
        title?.let { Text(it) }

        items.forEach { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(4.dp)
            ) {
                val checked = viewModel.selected.contains(item)
                Checkbox(
                    checked = checked,
                    onCheckedChange = { viewModel.toggle(item) }
                )
                Text(
                    text = getLabel(item),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}
