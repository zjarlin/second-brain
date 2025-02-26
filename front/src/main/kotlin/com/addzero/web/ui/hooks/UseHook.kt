package com.addzero.web.ui.hooks

import androidx.compose.runtime.*
import org.checkerframework.checker.units.qual.t

interface UseHook<T : UseHook<T>> {
    /**
     * 渲染组件并返回状态
     */
    @Suppress("UNCHECKED_CAST")
    @Composable
    fun render(): T {
        // 使用 remember 保留实例
        val state = remember {  this as T }
        show(state)
        return state
    }
    @Composable
    abstract fun show(state: T)

}
