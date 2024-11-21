package com.addzero.web.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.addzero.web.model.notes.KnowledgeEdge
import com.addzero.web.model.notes.KnowledgeNode

@Composable
fun KnowledgeGraphView(
    nodes: List<KnowledgeNode>,
    edges: List<KnowledgeEdge>,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        // 这里是一个简单的实现，实际项目中可能需要使用专业的图形库
        val nodePositions = calculateNodePositions(nodes, size)
        
        // 绘制边
        edges.forEach { edge ->
            val sourcePos = nodePositions[edge.source] ?: return@forEach
            val targetPos = nodePositions[edge.target] ?: return@forEach
            drawLine(
                color = Color.Gray,
                start = sourcePos,
                end = targetPos,
                strokeWidth = 2f
            )
        }
        
        // 绘制节点
        nodes.forEach { node ->
            val position = nodePositions[node.id] ?: return@forEach
            drawCircle(
                color = Color.Blue,
                radius = 20f,
                center = position
            )
        }
    }
}

private fun DrawScope.calculateNodePositions(
    nodes: List<KnowledgeNode>,
    canvasSize: androidx.compose.ui.geometry.Size
): Map<String, Offset> {
    // 简单的环形布局
    val centerX = canvasSize.width / 2
    val centerY = canvasSize.height / 2
    val radius = minOf(centerX, centerY) * 0.8f
    
    return nodes.mapIndexed { index, node ->
        val angle = (index.toFloat() / nodes.size) * 2 * Math.PI
        val x = centerX + radius * kotlin.math.cos(angle).toFloat()
        val y = centerY + radius * kotlin.math.sin(angle).toFloat()
        node.id to Offset(x, y)
    }.toMap()
} 