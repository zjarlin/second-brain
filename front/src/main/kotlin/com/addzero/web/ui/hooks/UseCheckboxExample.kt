package com.addzero.web.ui.hooks

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.web.modules.second_brain.dotfiles.EnumOsType
import com.addzero.web.modules.second_brain.dotfiles.EnumStatus

@Composable
fun UseCheckboxExample() {
    // 单选示例
    val singleSelect = UseCheckbox(
        title = "状态选择（单选）",
        items = EnumStatus.entries.toList(),
        isMultiSelect = false
    ) { it.desc }

    // 多选示例
    val osselectbox = UseCheckbox(
        title = "操作系统（多选）",
        items = EnumStatus.entries.toList(),
        isMultiSelect = true
    ) { it.desc }


    Column(modifier = Modifier.padding(16.dp)) {
        val render = singleSelect.render()
        val selectedValue = render.selected
        val joinToString = selectedValue.map { it.desc }.joinToString()
        Text(
            text = "已选中: $joinToString",
            modifier = Modifier.padding(top = 8.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        val render1 = osselectbox.render()
        val selectedValue1 = render1.selected

        val joinToString1 = selectedValue1.map { it.desc }.joinToString()
        Text(
            text = "已选中: $joinToString1",
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}
