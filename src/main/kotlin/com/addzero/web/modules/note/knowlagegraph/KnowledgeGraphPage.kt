package com.addzero.web.modules.note.knowlagegraph
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.web.modules.note.notes.NotesViewModel
import com.addzero.web.modules.note.notes.NotesService
import com.addzero.web.ui.components.system.dynamicroute.MetaSpec
import com.addzero.web.ui.components.system.dynamicroute.RouteMetadata

class KnowledgeGraphPage :MetaSpec {
    override val metadata: RouteMetadata
        get() {
            val routeMetadata = RouteMetadata(
                refPath = this.javaClass.name,
                title = "知识图谱",
                icon = Icons.Default.AccountTree,
                visible = true,
                permissions = emptyList(),
                order = 30.0
            )
            return routeMetadata
        }

    @Composable
    override fun render() {
        val viewModel = remember { NotesViewModel(NotesService()) }

        var searchQuery by remember { mutableStateOf("") }
        var selectedNode by remember { mutableStateOf<KnowledgeNode?>(null) }

        // 初始加载知识图谱
        LaunchedEffect(Unit) {
            viewModel.loadKnowledgeGraph()
        }

        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text("知识图谱", style = MaterialTheme.typography.headlineMedium)

            Spacer(Modifier.height(16.dp))

            // 搜索框
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    viewModel.loadKnowledgeGraph(it.takeIf { it.isNotBlank() })
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("搜索知识点") },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "搜索",
                        modifier = Modifier.padding(8.dp)
                    )
                }
            )

            Spacer(Modifier.height(16.dp))

            // 加载状态
            if (viewModel.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            // 错误状态
            viewModel.error?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            // 图谱展示
            viewModel.knowledgeGraph?.let { graph ->
                if (graph.nodes.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("没有找到相关的知识点")
                    }
                } else {
                    ComposeKnowledgeGraphView(
                        nodes = graph.nodes,
                        edges = graph.edges,
                        onNodeClick = { node -> selectedNode = node },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        // 显示节点详情对话框
        selectedNode?.let { node ->
            NodeDetailsDialog(
                node = node,
                onDismiss = { selectedNode = null }
            )
        }
    }

}
//fun KnowledgeGraphPage() {

//}