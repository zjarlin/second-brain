package com.addzero.web.ui.hooks.tree

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.addzero.Dsl
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*


@Dsl
data class User(
   val id: String ,
    val name: String,
   val age: Int,
   val age1: Double,
   val age2: Long,
   val date1: LocalDateTime,
   val date2: LocalDate,
   val date3: LocalTime,
   val date4: Date,
   val testPropty1: Byte,
   val testPropty2: Short,
   val testPropty3: Float,
   val testPropty4: Double,
   val testPropty5: Char,
   val testPropty6: String?,
   val testPropty7: Int?,
   val testPropty8: Long?,
   val self: User,
   val children: List<User>,
)

fun main() {
    User {
       testPropty1=0.toByte()
    }
}



@Composable
fun TreeSelectEnhancedExample() {
    // 创建示例数据，使用预定义的节点类型
    val treeData = remember {
        listOf(
            TreeNode(
                id = "1",
                label = "我的文档",
                data = "docs",
                type = NodeTypes.FOLDER,
                children = listOf(
                    TreeNode(
                        id = "1-1",
                        label = "技术文档",
                        data = "tech",
                        type = NodeTypes.FOLDER,
                        children = listOf(
                            TreeNode(id = "1-1-1", label = "React学习笔记", data = "react", type = NodeTypes.DOCUMENT),
                            TreeNode(id = "1-1-2", label = "Vue教程", data = "vue", type = NodeTypes.DOCUMENT),
                            TreeNode(id = "1-1-3", label = "架构设计图", data = "architecture", type = NodeTypes.IMAGE)
                        )
                    ),
                    TreeNode(
                        id = "1-2",
                        label = "多媒体资源",
                        data = "media",
                        type = NodeTypes.FOLDER,
                        children = listOf(
                            TreeNode(id = "1-2-1", label = "产品演示视频", data = "demo", type = NodeTypes.VIDEO),
                            TreeNode(id = "1-2-2", label = "会议录音", data = "meeting", type = NodeTypes.AUDIO),
                            TreeNode(id = "1-2-3", label = "项目资源链接", data = "resources", type = NodeTypes.LINK)
                        )
                    )
                )
            ),
            TreeNode(
                id = "2",
                label = "项目管理",
                data = "project",
                type = NodeTypes.FOLDER,
                children = listOf(
                    TreeNode(id = "2-1", label = "待办任务", data = "todo", type = NodeTypes.TASK),
                    TreeNode(id = "2-2", label = "重要标签", data = "important", type = NodeTypes.TAG),
                    // 使用自定义渲染函数的节点
                    TreeNode(
                        id = "2-3", 
                        label = "自定义节点", 
                        data = "custom",
                        customRender = { node ->
                            Text(
                                text = "⭐ ${node.label} (自定义渲染)",
                                color = Color.Blue,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    )
                )
            )
        )
    }

    // 创建默认展开的节点集合
    val initialExpandedNodes = remember {
        setOf("1", "2")
    }

    // 创建视图模型，设置默认展开的节点
    val viewModel = remember { 
        TreeSelectViewModel(
            initialNodes = treeData,
            isMultiSelect = true,
            initialExpandedNodes = initialExpandedNodes
        )
    }

    Surface(modifier = Modifier.padding(16.dp)) {
        Column {
            Text(
                text = "增强型树形选择示例",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
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