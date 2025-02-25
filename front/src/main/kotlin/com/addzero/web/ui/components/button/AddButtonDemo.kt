package com.addzero.web.ui.components.button

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cn.hutool.extra.spring.SpringUtil
import com.addzero.web.ui.components.layout.AddRow

@Composable
fun AddButtonDemo() {

    val activeProfile = SpringUtil.getActiveProfile()

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "$activeProfile",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // 默认样式按钮组
        AddRow {
            AddButton(
                displayName = "新建",
                icon = Icons.Default.Add,
                onClick = { println("点击新建按钮") }
            )
            AddButton(
                displayName = "编辑",
                icon = Icons.Default.Edit,
                onClick = { println("点击编辑按钮") },
                containerColor = MaterialTheme.colorScheme.secondary
            )
            AddButton(
                displayName = "删除",
                icon = Icons.Default.Delete,
                onClick = { println("点击删除按钮") },
                containerColor = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "常用操作按钮示例",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // 常用操作按钮组
        AddRow {
            AddButton(
                displayName = "查询",
                icon = Icons.Default.Search,
                onClick = { println("点击查询按钮") }
            )
            AddButton(
                displayName = "导入",
                icon = Icons.Default.Upload,
                onClick = { println("点击导入按钮") },
                containerColor = MaterialTheme.colorScheme.secondary
            )
            AddButton(
                displayName = "导出",
                icon = Icons.Default.Download,
                onClick = { println("点击导出按钮") },
                containerColor = MaterialTheme.colorScheme.tertiary
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        AddRow {
            AddButton(
                displayName = "批量新增",
                icon = Icons.Default.AddCircle,
                onClick = { println("点击批量新增按钮") }
            )
            AddButton(
                displayName = "批量删除",
                icon = Icons.Default.DeleteForever,
                onClick = { println("点击批量删除按钮") },
                containerColor = MaterialTheme.colorScheme.error
            )
            AddButton(
                displayName = "下载",
                icon = Icons.Default.FileDownload,
                onClick = { println("点击下载按钮") },
                containerColor = MaterialTheme.colorScheme.secondary
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "水平渐变示例",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // 水平渐变按钮组
        AddRow {
            AddButton(
                displayName = "蓝紫渐变",
                icon = Icons.Default.Add,
                onClick = { println("点击蓝紫渐变按钮") },
                containerColor = Color.Transparent,
                backgroundBrush = Brush.horizontalGradient(
                    colors = listOf(Color(0xFF4158D0), Color(0xFFC850C0))
                )
            )
            AddButton(
                displayName = "橙红渐变",
                icon = Icons.Default.Edit,
                onClick = { println("点击橙红渐变按钮") },
                containerColor = Color.Transparent,
                backgroundBrush = Brush.horizontalGradient(
                    colors = listOf(Color(0xFFFF512F), Color(0xFFDD2476))
                )
            )
            AddButton(
                displayName = "绿青渐变",
                icon = Icons.Default.Delete,
                onClick = { println("点击绿青渐变按钮") },
                containerColor = Color.Transparent,
                backgroundBrush = Brush.horizontalGradient(
                    colors = listOf(Color(0xFF11998E), Color(0xFF38EF7D))
                )
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "垂直渐变示例",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // 垂直渐变按钮组
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            AddButton(
                displayName = "日落渐变",
                icon = Icons.Default.Add,
                onClick = { println("点击日落渐变按钮") },
                modifier = Modifier.fillMaxWidth(),
                containerColor = Color.Transparent,
                backgroundBrush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFFE6B8B), Color(0xFFFF8E53))
                )
            )
            AddButton(
                displayName = "极光渐变",
                icon = Icons.Default.Upload,
                onClick = { println("点击极光渐变按钮") },
                modifier = Modifier.fillMaxWidth(),
                containerColor = Color.Transparent,
                backgroundBrush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF84FAB0), Color(0xFF8FD3F4))
                )
            )
            AddButton(
                displayName = "深海渐变",
                icon = Icons.Default.Download,
                onClick = { println("点击深海渐变按钮") },
                modifier = Modifier.fillMaxWidth(),
                containerColor = Color.Transparent,
                backgroundBrush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF2E3192), Color(0xFF1BFFFF))
                )
            )
        }
    }
}
