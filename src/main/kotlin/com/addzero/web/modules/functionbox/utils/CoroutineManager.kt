package com.addzero.web.modules.functionbox.utils

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CoroutineManager<T>(
    private val coroutineScope: CoroutineScope,
    private val initialState: T
) {
    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<T> = _state.asStateFlow()

    private val jobs = mutableMapOf<String, Job>()

    fun launch(
        key: String,
        block: suspend () -> Unit
    ) {
        jobs[key]?.cancel()
        jobs[key] = coroutineScope.launch {
            try {
                block()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateState(update: (T) -> T) {
        _state.value = update(_state.value)
    }

    fun cancelJob(key: String) {
        jobs[key]?.cancel()
        jobs.remove(key)
    }

    fun cancelAllJobs() {
        jobs.values.forEach { it.cancel() }
        jobs.clear()
    }

    fun dispose() {
        cancelAllJobs()
    }
}