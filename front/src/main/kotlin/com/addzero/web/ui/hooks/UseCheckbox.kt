package com.addzero.web.ui.hooks

import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class UseCheckbox<T>(
    private val title: String? = null,
    private val items: List<T>? = emptyList(),
    private val isMultiSelect: Boolean = false,
    private val modifier: Modifier = Modifier,
    private val getLabel: (T) -> String = { it.toString() },
) : UseHook<UseCheckbox<T>> {

    // 使用 State 封装状态
    var selected by mutableStateOf<Set<T>>(setOf())

    /**
     * 切换选中状态
     * @param value 当前选项的值
     */
    private fun toggle(value: T) {
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
    }

    /**
     * 清空选中状态
     */
    fun clear() {
        selected = emptySet()
    }

    @Composable
    override fun show(state: UseCheckbox<T>) {
        Row(modifier = modifier) {
            Text("$title")

            items.forEach { item ->
                val isChecked = remember(selected) {
                    derivedStateOf { selected.contains(item) }
                }.value

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(4.dp)
                ) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { state.toggle(item) }
                    )
                    Text(
                        text = getLabel(item),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}
