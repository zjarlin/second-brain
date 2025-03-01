package com.addzero.web.modules.note.notes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NoteAdd
import androidx.compose.runtime.Composable
import androidx.compose.ui.focus.FocusRequester
import com.addzero.web.ui.system.dynamicroute.MetaSpec
import com.addzero.web.ui.system.dynamicroute.RouteMetadata

class NotesPage : MetaSpec {
    private val focusRequester = FocusRequester()

    override val metadata: RouteMetadata
        get() = RouteMetadata(
            parentName = "RAG",
            title = "我的知识",
            icon = Icons.AutoMirrored.Filled.NoteAdd,
            visible = true,
            permissions = emptyList(),
            order = 10.0
        )

    @Composable
    override fun render() {

    }


}
