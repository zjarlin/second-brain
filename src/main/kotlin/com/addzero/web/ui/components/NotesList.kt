package com.addzero.web.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.web.modules.note.notes.Note

@Composable
fun NotesList(
    notes: List<Note>,
    onNoteClick: (Note) -> Unit,
    onDeleteClick: (Note) -> Unit
) {
    Column {
        notes.forEach { note ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = note.title,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = note.content.take(100) + if (note.content.length > 100) "..." else "",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Row {
                        IconButton(onClick = { onNoteClick(note) }) {
                            Icon(Icons.Default.Edit, "编辑")
                        }
                        IconButton(onClick = { onDeleteClick(note) }) {
                            Icon(Icons.Default.Delete, "删除")
                        }
                    }
                }
            }
        }
    }
}