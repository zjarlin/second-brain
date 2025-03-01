package com.addzero.web.ui.hooks.selection

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * UseSelectionPanel使用示例
 */
@Composable
fun UseSelectionPanelExample() {
    // 定义数据模型
    data class Category(val id: Int, val name: String)

    // 创建示例数据
    val categories = listOf(
        Category(1, "工作"),
        Category(2, "学习"),
        Category(3, "生活"),
        Category(4, "娱乐"),
        Category(5, "其他")
    )

    // 记录选中的类别
    var selectedCategories by remember { mutableStateOf<Set<Category>>(emptySet()) }

    // 使用useSelectionPanel创建选择面板
    val singleSelectionPanel = useSelectionPanel(
        title = "选择单个类别",
        items = categories,
        onValueChange = { selected ->
            println("单选面板选择了: ${selected.map { it.name }}")
        },
        isSingleSelect = true,
        getLabel = { it.name }
    )

    val multiSelectionPanel = useSelectionPanel(
        title = "选择多个类别",
        items = categories,
        initialValue = selectedCategories,
        onValueChange = { selected ->
            selectedCategories = selected
            println("多选面板选择了: ${selected.map { it.name }}")
        },
        isSingleSelect = false,
        getLabel = { it.name },
        defaultItem = categories.first()
    )

    Column(modifier = Modifier.padding(16.dp)) {
        // 渲染单选面板
        singleSelectionPanel.render()

        Spacer(modifier = Modifier.height(16.dp))

        // 渲染多选面板
        multiSelectionPanel.render()

        Spacer(modifier = Modifier.height(16.dp))

        // 显示当前选择的类别
        Text("当前选择的类别: ${selectedCategories.joinToString(", ") { it.name }}")

        Spacer(modifier = Modifier.height(16.dp))

        // 添加操作按钮
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { singleSelectionPanel.clear() }) {
                Text("清除单选")
            }

            Button(onClick = { multiSelectionPanel.clear() }) {
                Text("清除多选")
            }

            Button(onClick = {
                multiSelectionPanel.setValue(setOf(categories[2], categories[3]))
            }) {
                Text("预设选择")
            }
        }
    }
}
