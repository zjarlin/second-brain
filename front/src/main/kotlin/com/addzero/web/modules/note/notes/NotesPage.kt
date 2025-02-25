package com.addzero.web.modules.note.notes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NoteAdd
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.addzero.web.ui.components.common.dialog.UploadDialog
import com.addzero.web.ui.components.system.dynamicroute.MetaSpec
import com.addzero.web.ui.components.system.dynamicroute.RouteMetadata

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
