package com.addzero.web.ui.system.layout

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.web.modules.demo.TableDemo
import com.addzero.web.ui.system.*

@Composable
fun MainLayout() {
    //首页设置
    var currentRoute by remember { mutableStateOf(TableDemo::class .qualifiedName!!) }

    // 使用ErrorHandler包装整个应用内容
    ErrorHandler {
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
}
