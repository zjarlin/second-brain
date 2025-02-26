package com.addzero.web.modules.demo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cn.hutool.core.util.ReflectUtil
import com.addzero.web.modules.second_brain.dotfiles.EnumDefType
import com.addzero.web.modules.second_brain.dotfiles.EnumOsType
import com.addzero.web.modules.second_brain.dotfiles.EnumStatus
import com.addzero.web.modules.second_brain.dotfiles.Enumplatforms
import com.addzero.web.ui.components.system.dynamicroute.MetaSpec
import com.addzero.web.ui.components.system.dynamicroute.RouteMetadata
import com.addzero.web.ui.hooks.UseCheckbox


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
        Column(modifier = Modifier.padding(16.dp)) {
            mapOf.onEachIndexed() { index, it ->
                if (index > 0) {
                    Divider(modifier = Modifier.padding(vertical = 16.dp))
                }

                val key = it.key
                val title = key.first
                val items = key.second.toList()
                val multiSelectFlag = it.value

                val useCheckbox = UseCheckbox(
                    title = title, items = items, isMultiSelect = multiSelectFlag
                ) { item -> ReflectUtil.getFieldValue(item, "desc").toString() }
                val joinToString = useCheckbox.render().selected.joinToString()

                Text(
                    text = "已选中: $joinToString", modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}
