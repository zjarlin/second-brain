package com.addzero.web.ui.components.system

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.web.model.enums.Route
import com.addzero.web.modules.dotfiles.DotfilesPage
import com.addzero.web.modules.note.notes.treenote.NotesPage
import com.addzero.web.modules.software.SoftwarePage
import com.addzero.web.ui.components.system.dynamicroute.MetaSpec
import com.addzero.web.ui.components.system.dynamicroute.RouteUtil
import kotlin.reflect.full.createInstance

/**
 * 主要内容
 * @param [currentRoute]
 * @param [viewModel]
 */
@Composable
fun MainContent(
    currentRoute: String,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        Box(modifier = Modifier.padding(16.dp)) {
            val routeComponentByPath = RouteUtil.getRouteComponentByPath(currentRoute)
            val first = routeComponentByPath?.first
            // 使用反射创建实例
            val createInstance = first?.createInstance()
            val metaSpec = createInstance as MetaSpec
            metaSpec.render()
        }
    }
}