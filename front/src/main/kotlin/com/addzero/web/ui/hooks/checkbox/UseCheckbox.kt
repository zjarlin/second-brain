package com.addzero.web.ui.hooks.checkbox

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.web.ui.hooks.UseHook

class UseCheckbox<T>(
    val title: String? = null,
    val options: List<T>,
    val isMultiSelect: Boolean = true,
    val getLabel: (T) -> String = { it.toString() },
    val onToggle: (T) -> Unit
) : UseHook<UseCheckbox<T>> {
    var selected by mutableStateOf<List<T>>(emptyList())

    override val render: @Composable () -> Unit
        get() = {
            Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
                title?.let { title ->
                    Text(text = title, modifier = Modifier.padding(end = 8.dp))
                }
                options.forEach { item ->
                    Row(
                        modifier = Modifier.padding(end = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = selected.contains(item),
                            onCheckedChange = { 
                                selected = if (isMultiSelect) {
                                    if (it) selected + item else selected - item
                                } else {
                                    if (it) listOf(item) else emptyList()
                                }
                                onToggle(item)
                            }
                        )
                        Text(
                            text = getLabel(item),
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
            }
        }
}

