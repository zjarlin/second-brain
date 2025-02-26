package com.addzero.web.ui.hooks.demo

import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.addzero.web.ui.hooks.UseHook

class UseCounter : UseHook<UseCounter>() {
    // 使用 State 封装状态
    var count1 by mutableStateOf(0)
    var count2 by mutableStateOf(0)
    var count3 by mutableStateOf(0)

    @Composable
    override fun show(state: UseCounter) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(onClick = {
                state.count1 += 1
                state.count2 += 2
                state.count3 += 3
            }) {
                Text("增加计数1: ${state.count1}", fontSize = 18.sp)
                Text("增加计数2: ${state.count2}", fontSize = 18.sp)
                Text("增加计数3: ${state.count3}", fontSize = 18.sp)
            }
        }
    }
}
