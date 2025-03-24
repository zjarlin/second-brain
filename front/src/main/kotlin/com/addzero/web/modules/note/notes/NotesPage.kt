package com.addzero.web.modules.note.notes

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NoteAdd
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.unit.dp
import com.addzero.web.ui.system.dynamicroute.MetaSpec
import com.addzero.web.ui.system.dynamicroute.RouteMetadata
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.addzero.web.modules.note.knowlagegraph.ComposeKnowledgeGraphView
import com.addzero.web.modules.note.knowlagegraph.KnowledgeNode
import com.addzero.web.modules.note.knowlagegraph.KnowledgeEdge

class NotesPage : MetaSpec {
    private val focusRequester = FocusRequester()

    override val metadata: RouteMetadata
        get() = RouteMetadata(
            parentName = "RAG",
            title = "我的知识",
            icon = Icons.AutoMirrored.Filled.NoteAdd,
            visible = true,
            permissions = emptyList(),
            order = 10.0
        )

    @Composable
    override fun render() {
        val nodes = remember { mutableStateOf(listOf(
            KnowledgeNode(
                "1", "人工智能",
                type = "domain",
                properties = mapOf(
                    "description" to "人工智能是计算机科学的一个分支，致力于开发能够模拟人类智能的系统",
                    "established" to "1956",
                    "field" to "计算机科学"
                )
            ),
            KnowledgeNode(
                "2", "机器学习",
                type = "technology",
                properties = mapOf(
                    "description" to "机器学习是人工智能的一个子领域，专注于让计算机系统从经验中学习",
                    "paradigm" to "监督学习、无监督学习、强化学习",
                    "applications" to "图像识别、自然语言处理、推荐系统"
                )
            ),
            KnowledgeNode(
                "3", "深度学习",
                type = "technology",
                properties = mapOf(
                    "description" to "深度学习是机器学习的一个分支，使用多层神经网络进行特征学习",
                    "key_concepts" to "神经网络、反向传播、激活函数",
                    "frameworks" to "TensorFlow、PyTorch、Keras"
                )
            )
        )) }
        
        val edges = remember { mutableStateOf(listOf(
            KnowledgeEdge(
                source = "1",
                target = "2",
                label = "包含",
                properties = mapOf(
                    "relationship_type" to "is_part_of",
                    "confidence" to "0.95"
                )
            ),
            KnowledgeEdge(
                source = "2",
                target = "3",
                label = "包含",
                properties = mapOf(
                    "relationship_type" to "is_part_of",
                    "confidence" to "0.90"
                )
            )
        )) }

        val selectedNode = remember { mutableStateOf<KnowledgeNode?>(null) }

        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            // 顶部搜索框
            OutlinedTextField(
                value = "",
                onValueChange = { },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("向AI提问...") },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 主要内容区域
            Row(modifier = Modifier.fillMaxSize()) {
                // 知识图谱展示区域
                Surface(
                    modifier = Modifier.weight(0.7f).fillMaxHeight(),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 1.dp
                ) {
                    ComposeKnowledgeGraphView(
                        nodes = nodes.value,
                        edges = edges.value,
                        onNodeClick = { node ->
                            selectedNode.value = node
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // 节点详情和文件上传区域
                Surface(
                    modifier = Modifier.weight(0.3f).fillMaxHeight(),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 1.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        selectedNode.value?.let { node ->
                            Text(
                                text = "节点详情",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Text(
                                text = "标签：${node.label}",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            Text(
                                text = "类型：${node.type}",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            Text(
                                text = "属性：",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            node.properties.forEach { (key, value) ->
                                Text(
                                    text = "$key: $value",
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(start = 8.dp, bottom = 2.dp)
                                )
                            }
                            Divider(modifier = Modifier.padding(vertical = 16.dp))
                        }

                        Text(
                            text = "上传文件",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = "支持PDF、Word、Markdown、TXT等格式",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        // TODO: 实现文件上传功能
                    }
                }
            }
        }
    }
}
