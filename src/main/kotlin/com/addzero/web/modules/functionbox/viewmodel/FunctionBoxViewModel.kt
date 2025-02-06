package com.addzero.web.modules.functionbox.viewmodel

import androidx.compose.runtime.mutableStateOf
import com.addzero.web.modules.functionbox.model.FunctionBoxSpec
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FunctionBoxViewModel {
    private val _functions = MutableStateFlow<List<FunctionBoxSpec>>(emptyList())
    val functions: StateFlow<List<FunctionBoxSpec>> = _functions.asStateFlow()

    fun registerFunction(function: FunctionBoxSpec) {
        _functions.value = _functions.value + function
    }

    fun unregisterFunction(function: FunctionBoxSpec) {
        _functions.value = _functions.value - function
    }
}