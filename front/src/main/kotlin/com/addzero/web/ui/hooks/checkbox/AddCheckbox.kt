package com.addzero.web.ui.hooks.checkbox

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 复选框组件的函数式实现
 * @param viewModel 复选框视图模型
 * @param items 选项列表
 * @param isMultiSelect 是否多选
 * @param getLabel 获取选项标签的函数
 * @param modifier 修饰符
 * @param onToggle 切换选中状态的回调函数
 */
@Composable
fun <T> Checkbox(
    viewModel: CheckboxViewModel<T>,
    items: List<T>,
    isMultiSelect: Boolean,
    getLabel: (T) -> String = { it.toString() },
    modifier: Modifier = Modifier,
    onToggle: (T) -> Unit
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        viewModel.title?.let { title ->
            Text(text = title, modifier = Modifier.padding(end = 8.dp))
        }
        items.forEach { item ->
            Row(
                modifier = Modifier.padding(end = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = viewModel.selected.contains(item),
                    onCheckedChange = { onToggle(item) }
                )
                Text(
                    text = getLabel(item),
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }
    }
}