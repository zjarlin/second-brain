package com.addzero.web.modules.demo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cn.hutool.core.util.ReflectUtil
import com.addzero.web.modules.second_brain.dotfiles.EnumDefType
import com.addzero.web.modules.second_brain.dotfiles.EnumOsType
import com.addzero.web.modules.second_brain.dotfiles.EnumStatus
import com.addzero.web.modules.second_brain.dotfiles.Enumplatforms
import com.addzero.web.ui.hooks.checkbox.CheckboxGroup
import com.addzero.web.ui.hooks.checkbox.UseCheckbox
import com.addzero.web.ui.system.dynamicroute.MetaSpec
import com.addzero.web.ui.system.dynamicroute.RouteMetadata
import com.addzero.web.ui.system.dynamicroute.Router
import kotlin.enums.EnumEntries


class CheckboxDemo : MetaSpec {

    override val metadata: RouteMetadata
        get() = RouteMetadata(
            parentName = "测试demo",
            title = "测试复选框",
//            icon = Icons.Filled.Apps,
            visible = true,
//            permissions = emptyList()
        )

    @Composable
    override fun render() {
        val mapOf = mapOf(
            "定义类型" to EnumDefType.entries to false,
            "操作系统" to EnumOsType.entries to true,
            "系统架构" to Enumplatforms.entries to false,
            "定义类型" to EnumStatus.entries to false,
        )

        extracted(mapOf)


//        extracted(mapOf)
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
        extracted(mapOf)
    }




    @Composable
    private fun extracted1(mapOf: Map<Pair<String, EnumEntries<out Enum<*>>>, Boolean>) {
        var joinToString by mutableStateOf("")

        Column(modifier = Modifier.padding(16.dp)) {

            mapOf.forEach {
                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

                val key = it.key
                val title = key.first
                val items = key.second.toList()
                val multiSelectFlag = it.value

                CheckboxGroup(title, items, multiSelectFlag) { it ->
                    joinToString = it.map { it.name }.joinToString { System.lineSeparator() }
                    println("已选中: $joinToString")
                }


                Text(
                    text = "已选中: $joinToString", modifier = Modifier.padding(top = 8.dp)
                )
                Button(
                    onClick = { joinToString += "dasdasd" }) {
                    Text("点击+1")
                }


            }
        }
    }

    @Composable
    private fun extracted(mapOf: Map<Pair<String, EnumEntries<out Enum<*>>>, Boolean>) {


        Column(modifier = Modifier.padding(16.dp)) {

            mapOf.forEach {
                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

                val key = it.key
                val title = key.first
                val items = key.second.toList()
                val multiSelectFlag = it.value

                val useCheckbox = UseCheckbox(
                    title = title, items = items, isMultiSelect = multiSelectFlag
                ) { item -> ReflectUtil.getFieldValue(item, "desc").toString() }

                val render = useCheckbox.getState()


                val joinToString = render.selected.joinToString()
                render.render()


                Text(
                    text = "已选中: $joinToString", modifier = Modifier.padding(top = 8.dp)
                )
                Button(
                    onClick = { render.title += "dasdasd" }) {
                    Text("点击+1")
                }
            }
        }
    }
}
