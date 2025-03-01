package com.addzero.web.ui.hooks.demo

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.sp
import com.addzero.web.ui.hooks.UseHook

class UseCounter : UseHook<UseCounter> {

    var count1 by mutableStateOf(0)
    var count2 by mutableStateOf(0)
    var count3 by mutableStateOf(0)
    override val render: @Composable () -> Unit
        get() = {
            Button(onClick = {
                count1 += 1
                count2 += 2
                count3 += 3
            }) {
                Text("增加计数1: ${count1}", fontSize = 18.sp)
                Text("增加计数2: ${count2}", fontSize = 18.sp)
                Text("增加计数3: ${count3}", fontSize = 18.sp)
            }
        }
}
