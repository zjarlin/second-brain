package com.addzero.web.ui.hooks.demo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class CounterViewModel {
    var count1 by mutableStateOf(0)
    var count2 by mutableStateOf(0)
    var count3 by mutableStateOf(0)
    
    fun increment() {
        count1 += 1
        count2 += 2
        count3 += 3
    }
}