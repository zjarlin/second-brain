package com.addzero.web.modules.demo

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.addzero.web.modules.second_brain.dotfiles.EnumDefType
import com.addzero.web.modules.second_brain.dotfiles.EnumOsType
import com.addzero.web.modules.second_brain.dotfiles.EnumStatus
import com.addzero.web.modules.second_brain.dotfiles.Enumplatforms
import com.addzero.web.ui.system.dynamicroute.Router


class DyRouteDemo {

    @Composable
    @Router(parentName = "测试demo", title = "xxxxxxxxxxx")
    fun ExampleComposable() {
        // 这里可以是具体的 Compose 代码
        Text("Hello, World!")
    }

}


/**
 *todo 测试动态路由
 */
@Composable
@Router(parentName = "测试demo", title = "xxxxxxxxxxxxxx")
fun render222() {
    val mapOf = mapOf(
        "定义类型" to EnumDefType.entries to false,
        "操作系统" to EnumOsType.entries to true,
        "系统架构" to Enumplatforms.entries to false,
        "定义类型" to EnumStatus.entries to false,
    )
}

