package com.addzero.web.ui.hooks.demo

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp

/**
 * 计数器组件的函数式实现
 * @param viewModel 计数器视图模型
 * @param onIncrement 点击增加按钮的回调函数
 */
@Composable
fun Counter(
    viewModel: CounterViewModel,
    onIncrement: () -> Unit
) {
    Button(onClick = onIncrement) {
        Text("增加计数1: ${viewModel.count1}", fontSize = 18.sp)
        Text("增加计数2: ${viewModel.count2}", fontSize = 18.sp)
        Text("增加计数3: ${viewModel.count3}", fontSize = 18.sp)
    }
}