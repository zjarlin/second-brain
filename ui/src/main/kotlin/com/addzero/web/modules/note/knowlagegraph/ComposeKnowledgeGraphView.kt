package com.addzero.web.modules.note.knowlagegraph

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.text.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.*

@Composable
fun ComposeKnowledgeGraphView(
    nodes: List<KnowledgeNode>,
    edges: List<KnowledgeEdge>,
    onNodeClick: (KnowledgeNode) -> Unit,
    modifier: Modifier = Modifier
) {
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var draggedNodeId by remember { mutableStateOf<String?>(null) }
    var nodePositions by remember { mutableStateOf(mutableMapOf<String, Offset>()) }

    val textMeasurer = rememberTextMeasurer()

    LaunchedEffect(nodes) {
        nodePositions = calculateInitialPositions(nodes)
    }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    scale *= zoom
                    offset += pan
                }
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { tapPosition ->
                        // 检查点击的是否是节点
                        val clickedNodeId = findNodeAtPosition(tapPosition, nodePositions)
                        clickedNodeId?.let { id ->
                            nodes.find { it.id == id }?.let { node ->
                                onNodeClick(node)
                            }
                        }
                    }
                )
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { position ->
                        draggedNodeId = findNodeAtPosition(position, nodePositions)
                    },
                    onDrag = { change, dragAmount ->
                        draggedNodeId?.let { id ->
                            nodePositions[id] = nodePositions[id]!! + dragAmount
                        }
                    },
                    onDragEnd = {
                        draggedNodeId = null
                    }
                )
            }
    ) {
        scale(scale, scale, center) {
            translate(offset.x, offset.y) {
                // 绘制边
                edges.forEach { edge ->
                    val sourcePos = nodePositions[edge.source] ?: return@forEach
                    val targetPos = nodePositions[edge.target] ?: return@forEach

                    // 绘制边的连线
                    drawLine(
                        color = Color.Gray,
                        start = sourcePos,
                        end = targetPos,
                        strokeWidth = 2f
                    )

                    // 绘制边标签
                    val midPoint = Offset(
                        (sourcePos.x + targetPos.x) / 2,
                        (sourcePos.y + targetPos.y) / 2
                    )

                    val textLayoutResult = textMeasurer.measure(
                        text = edge.label,
                        style = TextStyle(
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                    )

                    drawText(
                        textLayoutResult = textLayoutResult,
                        topLeft = Offset(
                            midPoint.x - textLayoutResult.size.width / 2,
                            midPoint.y - textLayoutResult.size.height / 2
                        )
                    )
                }

                // 绘制节点
                nodes.forEach { node ->
                    val position = nodePositions[node.id] ?: return@forEach
                    val isBeingDragged = node.id == draggedNodeId

                    // 节点背景
                    drawCircle(
                        color = if (isBeingDragged) Color(0xFF4CAF50) else Color(0xFF69B3A2),
                        radius = if (isBeingDragged) 22f else 20f,
                        center = position
                    )

                    // 节点标签
                    val textLayoutResult = textMeasurer.measure(
                        text = node.label,
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 14.sp
                        )
                    )

                    drawText(
                        textLayoutResult = textLayoutResult,
                        topLeft = Offset(
                            position.x - textLayoutResult.size.width / 2,
                            position.y + 30f
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun NodeDetailsDialog(
    node: KnowledgeNode,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(
                    node.label,
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    "类型: ${node.type}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        text = {
            Column {
                node.properties.forEach { (key, value) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = key,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.weight(0.4f)
                        )
                        Text(
                            text = value,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(0.6f)
                        )
                    }
                    if (node.properties.entries.last().key != key) {
                        Divider(modifier = Modifier.padding(vertical = 4.dp))
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("关闭")
            }
        }
    )
}

private fun calculateInitialPositions(
    nodes: List<KnowledgeNode>
): MutableMap<String, Offset> {
    val positions = mutableMapOf<String, Offset>()
    val centerX = 500f
    val centerY = 500f
    val radius = 200f

    nodes.forEachIndexed { index, node ->
        val angle = (index.toFloat() / nodes.size) * 2 * PI
        val x = centerX + radius * cos(angle).toFloat()
        val y = centerY + radius * sin(angle).toFloat()
        positions[node.id] = Offset(x, y)
    }

    return positions
}

private fun findNodeAtPosition(
    position: Offset,
    nodePositions: Map<String, Offset>
): String? {
    val threshold = 20f
    return nodePositions.entries.firstOrNull { (_, pos) ->
        (position - pos).getDistance() < threshold
    }?.key
}