package com.addzero.web.ui.layout

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.web.model.enums.Route
import com.addzero.web.modules.note.notes.NotesViewModel
import com.addzero.web.ui.components.Breadcrumb
import com.addzero.web.ui.components.MainContent
import com.addzero.web.ui.components.SideMenu
import com.addzero.web.ui.components.TopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainLayout(

    notesViewModel: NotesViewModel,
) {
    var currentRoute by remember { mutableStateOf(Route.NOTES) }

    Scaffold(
        topBar = { TopBar() }
    ) { paddingValues ->
        Row(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            // 左侧菜单
            Surface(
                modifier = Modifier.width(IntrinsicSize.Max),
                tonalElevation = 1.dp
            ) {
                SideMenu(
                    currentRoute = currentRoute,
                    onRouteChange = { currentRoute = it }
                )
            }

            // 主内容区
            Column(modifier = Modifier.weight(1f)) {
                // 面包屑导航
                Breadcrumb(currentRoute = currentRoute)

                // 主要内容
                Surface(
                    modifier = Modifier.weight(1f),
                    tonalElevation = 0.dp
                ) {
                    MainContent(
                        currentRoute = currentRoute, notesViewModel =notesViewModel
                    )
                }
            }
        }
    }
}