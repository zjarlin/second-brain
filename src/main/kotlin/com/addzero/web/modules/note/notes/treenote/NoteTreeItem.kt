package com.addzero.web.modules.note.notes.treenote

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.web.modules.note.notes.Note
import com.addzero.web.modules.note.notes.NoteType

@Composable
fun NoteTreeItem(
    note: Note,
    level: Int = 0,
    selected: Boolean = false,
    onSelect: (Note) -> Unit,
    onDelete: (Note) -> Unit,
    onAddChild: (Note) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onSelect(note) }
                .padding(start = (level * 16).dp, end = 8.dp, top = 4.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 展开/折叠图标
            if (note.children?.isNotEmpty() == true) {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        if (expanded) Icons.Default.ExpandMore else Icons.Default.ChevronRight,
                        contentDescription = if (expanded) "折叠" else "展开"
                    )
                }
            } else {
                Spacer(Modifier.width(40.dp))
            }

            // 笔记图标
            Icon(
                if (note.type == NoteType.MARKDOWN) Icons.Default.Folder else Icons.Default.Description,
                contentDescription = "类型",
                modifier = Modifier.padding(end = 8.dp)
            )

            // 标题
            Text(
                text = note.title,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )

            // 操作按钮
            Row {
                IconButton(onClick = { onAddChild(note) }) {
                    Icon(Icons.Default.Add, "添加子笔记")
                }
                IconButton(onClick = { onDelete(note) }) {
                    Icon(Icons.Default.Delete, "删除")
                }
            }
        }

        // 子节点
        AnimatedVisibility(visible = expanded) {
            Column(modifier = Modifier.padding(start = 16.dp)) {
                note.children?.forEach { child ->
                    NoteTreeItem(
                        note = child,
                        level = level + 1,
                        selected = selected,
                        onSelect = onSelect,
                        onDelete = onDelete,
                        onAddChild = onAddChild
                    )
                }
            }
        }
    }
}