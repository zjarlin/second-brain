package com.addzero.web.modules.demo
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.ui.unit.sp
import com.addzero.web.ui.components.system.dynamicroute.MetaSpec
import com.addzero.web.ui.components.system.dynamicroute.RouteMetadata
import com.addzero.web.ui.hooks.UseCounter

class CountDemo : MetaSpec {

    override val metadata: RouteMetadata
        get() = RouteMetadata(
            parentName = "测试demo",
            title = "测试计数",
            visible = true,
        )

    @Composable
    override fun render() {
        val counter = UseCounter().render()

        Column {
            DisplayCount(count = counter.count1)
            DisplayCount(count = counter.count2)
            DisplayCount(count = counter.count3)
        }
    }

    @Composable
    private fun DisplayCount(count: Int) {
        Column {
            Text(text = "从另一个组件接收到的计数：${count}", fontSize = 20.sp)
        }
    }
}
