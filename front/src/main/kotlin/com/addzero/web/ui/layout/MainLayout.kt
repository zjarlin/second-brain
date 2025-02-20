package com.addzero.web.ui.layout

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.web.modules.dotfiles.DotfilesPage
import com.addzero.web.ui.components.system.Breadcrumb
import com.addzero.web.ui.components.system.MainContent
import com.addzero.web.ui.components.system.SideMenu
import com.addzero.web.ui.components.system.TopBar

@Composable
fun MainLayout() {
    var currentRoute by remember { mutableStateOf(DotfilesPage::class.qualifiedName!!) }

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
                Breadcrumb(currentRouteRefPath = currentRoute)

                // 主要内容
                Surface(
                    modifier = Modifier.weight(1f),
                    tonalElevation = 0.dp
                ) {
                    MainContent(
                        currentRoute = currentRoute,
                    )
                }
            }
        }
    }
}
