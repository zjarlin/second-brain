package com.addzero.web.ui.hooks.checkbox

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.web.ui.hooks.UseHook

class UseCheckbox<T>(
    title: String? = null,
    private val items: List<T> = emptyList(),
    private val isMultiSelect: Boolean = false,
    private val getLabel: (T) -> String = { it.toString() },
) : UseHook<UseCheckbox<T>> {
    private var _title by mutableStateOf(title)
    var title: String?
        get() = _title
        set(value) {
            _title = value
        }

    private var _selected by mutableStateOf<Set<T>>(setOf())
    val selected: Set<T> get() = _selected

    /**
     * 切换选中状态
     * @param value 当前选项的值
     */
    private fun toggle(value: T) {
        _selected = if (isMultiSelect) {
            if (_selected.contains(value)) {
                _selected - value
            } else {
                _selected + value
            }
        } else {
            if (_selected.contains(value)) {
                emptySet()
            } else {
                setOf(value)
            }
        }
    }

    override val render: @Composable () -> Unit
        get() = {
            Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
                title?.let { title ->
                    Text(text = title, modifier = Modifier.padding(end = 8.dp))
                }
                items.forEach { item ->
                    Row(
                        modifier = Modifier.padding(end = 8.dp), verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = state.selected.contains(item), onCheckedChange = { state.toggle(item) })
                        Text(
                            text = getLabel(item), modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
            }
        }
}
