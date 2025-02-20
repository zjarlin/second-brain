package com.addzero.web.ui.components.crud

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <T> SelectionPanel(
    title: String,
    items: List<T>,
    value: Set<T>? = null,
    onValueChange: (Set<T>) -> Unit = {},
    isSingleSelect: Boolean = false,
    getLabel: (T) -> String,
    defaultItem: T? = null,
    defaultSelection: Set<T> = emptySet(),
    modifier: Modifier = Modifier
): State<Set<T>> {
    var currentValue by remember { mutableStateOf(value ?: emptySet()) }

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

    return remember { derivedStateOf { currentValue } }
}
