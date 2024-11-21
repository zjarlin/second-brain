package com.addzero.web.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.web.model.enums.Route

@Composable
fun Breadcrumb(currentRoute: Route) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = when (currentRoute) {
                    Route.DOTFILES -> "首页 / Dotfiles 管理"
                    Route.SOFTWARE -> "首页 / 软件管理"
                    Route.NOTES -> "首页 / 知识库管理"
                    Route.NOTES_QA -> "首页 / 知识库问答"
                    Route.KNOWLEDGE_GRAPH -> "首页 / 知识图谱"
                },
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}