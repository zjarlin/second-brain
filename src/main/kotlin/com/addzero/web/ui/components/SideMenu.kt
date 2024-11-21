package com.addzero.web.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.web.model.enums.Route

@Composable
fun SideMenu(
    currentRoute: Route,
    onRouteChange: (Route) -> Unit
) {
    Column(modifier = Modifier.width(240.dp)) {
        // 知识库相关功能
        NavigationDrawerItem(
            icon = { Icon(Icons.Filled.Book, contentDescription = "知识库") },
            label = { Text("知识库管理") },
            selected = currentRoute == Route.NOTES,
            onClick = { onRouteChange(Route.NOTES) },
            modifier = Modifier.padding(vertical = 4.dp)
        )
        
        NavigationDrawerItem(
            icon = { Icon(Icons.Filled.QuestionAnswer, contentDescription = "问答") },
            label = { Text("知识库问答") },
            selected = currentRoute == Route.NOTES_QA,
            onClick = { onRouteChange(Route.NOTES_QA) },
            modifier = Modifier.padding(vertical = 4.dp)
        )
        
        NavigationDrawerItem(
            icon = { Icon(Icons.Filled.AccountTree, contentDescription = "图谱") },
            label = { Text("知识图谱") },
            selected = currentRoute == Route.KNOWLEDGE_GRAPH,
            onClick = { onRouteChange(Route.KNOWLEDGE_GRAPH) },
            modifier = Modifier.padding(vertical = 4.dp)
        )
        
        Divider(modifier = Modifier.padding(vertical = 8.dp))
        
        // 系统管理功能
        NavigationDrawerItem(
            icon = { Icon(Icons.Filled.Settings, contentDescription = "Dotfiles") },
            label = { Text("Dotfiles 管理") },
            selected = currentRoute == Route.DOTFILES,
            onClick = { onRouteChange(Route.DOTFILES) },
            modifier = Modifier.padding(vertical = 4.dp)
        )

        NavigationDrawerItem(
            icon = { Icon(Icons.Filled.Apps, contentDescription = "软件") },
            label = { Text("软件管理") },
            selected = currentRoute == Route.SOFTWARE,
            onClick = { onRouteChange(Route.SOFTWARE) },
            modifier = Modifier.padding(vertical = 4.dp)
        )
    }
}