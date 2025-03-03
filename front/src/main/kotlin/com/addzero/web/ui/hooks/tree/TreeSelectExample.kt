package com.addzero.web.ui.hooks.tree

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TreeSelectExample() {
    // 创建示例数据
    val treeData = remember {
        listOf(
            TreeNode(
                id = "1",
                label = "技术文档",
                data = "tech",
                children = listOf(
                    TreeNode(
                        id = "1-1",
                        label = "前端开发",
                        data = "frontend",
                        children = listOf(
                            TreeNode(id = "1-1-1", label = "React", data = "react"),
                            TreeNode(id = "1-1-2", label = "Vue", data = "vue"),
                            TreeNode(id = "1-1-3", label = "Angular", data = "angular")
                        )
                    ),
                    TreeNode(
                        id = "1-2",
                        label = "后端开发",
                        data = "backend",
                        children = listOf(
                            TreeNode(id = "1-2-1", label = "Java", data = "java"),
                            TreeNode(id = "1-2-2", label = "Python", data = "python"),
                            TreeNode(id = "1-2-3", label = "Node.js", data = "nodejs")
                        )
                    )
                )
            ),
            TreeNode(
                id = "2",
                label = "项目管理",
                data = "project",
                children = listOf(
                    TreeNode(id = "2-1", label = "需求文档", data = "requirements"),
                    TreeNode(id = "2-2", label = "设计文档", data = "design"),
                    TreeNode(id = "2-3", label = "测试文档", data = "testing")
                )
            )
        )
    }

    // 创建默认展开的节点集合
    val initialExpandedNodes = remember {
        setOf(
//            "1",      // 展开"技术文档"节点
            "2",
//            "1-1"     // 展开"前端开发"节点
        )
    }

    // 创建视图模型，设置默认展开的节点
    val viewModel = remember { 
        TreeSelectViewModel(
            initialNodes = treeData,
            isMultiSelect = true,
            initialExpandedNodes = initialExpandedNodes
        )
    }

    // 初始化选中一些节点
    remember {
        viewModel.toggleSelection("1-1-1") // 选中 React
        viewModel.toggleSelection("1-1-2") // 选中 Vue
    }

    Surface(modifier = Modifier.padding(16.dp)) {
        Column {
            TreeSelect(
                viewModel = viewModel,
                onNodeSelected = { selectedIds ->
                    // 处理节点选择事件
                    println("选中的节点: $selectedIds")
                }
            )
        }
    }
}