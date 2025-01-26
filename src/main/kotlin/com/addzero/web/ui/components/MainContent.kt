package com.addzero.web.ui.components

import DotfilesScreen
import KnowledgeGraphPage
import NotesQAPage
import SoftwareScreen
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.web.model.enums.Route
import com.addzero.web.modules.note.notes.NotesService
import com.addzero.web.modules.note.notes.NotesViewModel
import com.addzero.web.modules.note.notes.treenote.NotesPage

/**
 * 主要内容
 * @param [currentRoute]
 * @param [viewModel]
 */
@Composable
fun MainContent(
    currentRoute: Route,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        Box(modifier = Modifier.padding(16.dp)) {
            when (currentRoute) {
                Route.DOTFILES -> DotfilesScreen()
                Route.SOFTWARE -> SoftwareScreen()
                Route.NOTES -> NotesPage()
                Route.NOTES_QA -> NotesQAPage()
                Route.KNOWLEDGE_GRAPH -> KnowledgeGraphPage()
            }
        }
    }
}