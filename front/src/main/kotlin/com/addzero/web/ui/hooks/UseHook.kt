package com.addzero.web.ui.hooks

import androidx.compose.runtime.*

interface UseHook<T : UseHook<T>> {
       // 使用 State 封装状态
//    var modelValue by mutableStateOf(defaultModelValue)


    /**
     * 渲染组件并返回状态
     */
    @Suppress("UNCHECKED_CAST")
    @Composable
    fun render(): T {
        // 使用 remember 保留实例
        val state = remember { this as T }
        show(state)
        return state
    }

    /**
     * 显示组件UI
     */
    @Composable
    abstract fun show(state: T)

}
