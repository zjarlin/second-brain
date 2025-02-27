package com.addzero.web.modules.demo

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.addzero.web.ui.components.system.dynamicroute.MetaSpec
import com.addzero.web.ui.components.system.dynamicroute.RouteMetadata
import com.addzero.web.ui.hooks.demo.UseCounter

class CountDemo : MetaSpec {

    override val metadata: RouteMetadata
        get() = RouteMetadata(
            parentName = "测试demo",
            title = "测试计数",
            visible = true,
        )

    @Composable
    override fun render() {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            val state = UseCounter().render()
            val state2 = UseCounter().render()
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly) {
                Column {
                    Text("计数器1", style = MaterialTheme.typography.titleMedium)
                    DisplayCount(count = state.count1)
                    DisplayCount(count = state.count2)
                    DisplayCount(count = state.count3)
                }
                
                Column {
                    Text("计数器2", style = MaterialTheme.typography.titleMedium)
                    DisplayCount(count = state2.count1)
                    DisplayCount(count = state2.count2)
                    DisplayCount(count = state2.count3)
                }
            }
        }
    }

    @Composable
    private fun DisplayCount(count: Int) {
            Text(text = "从另一个组件接收到的计数：${count}", fontSize = 20.sp)
    }
}
