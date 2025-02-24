package com.addzero.web.ui.hooks
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

class UseCounter {
    // 使用 State 封装状态
    var count1 by mutableStateOf(0)
    var count2 by mutableStateOf(0)
    var count3 by mutableStateOf(0)

    @Composable
    fun render(): UseCounter {
        // 使用 remember 保留实例
        val rememberedInstance = remember { this }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(onClick = {
                rememberedInstance.count1 += 1
                rememberedInstance.count2 += 2
                rememberedInstance.count3 += 3
            }) {
                Text("增加计数1: ${rememberedInstance.count1}", fontSize = 18.sp)
                Text("增加计数2: ${rememberedInstance.count2}", fontSize = 18.sp)
                Text("增加计数3: ${rememberedInstance.count3}", fontSize = 18.sp)
            }
        }

        return rememberedInstance
    }
}
