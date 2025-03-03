package com.addzero.web.ui.hooks.checkbox

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class CheckboxViewModel<T> {
    var title by mutableStateOf<String?>(null)
    var selected by mutableStateOf<Set<T>>(emptySet())
    
    fun toggle(value: T, isMultiSelect: Boolean) {
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
}