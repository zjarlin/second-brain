package com.addzero.web.modules.dotfiles

import BizEnvVars
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
internal fun DotfilesTable(
    items: List<BizEnvVars>,
    onEdit: (BizEnvVars) -> Unit,
    onDelete: (BizEnvVars) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        items(items) { item ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                Text(item.osType, modifier = Modifier.weight(1f))
                Text(item.osStructure, modifier = Modifier.weight(1f))
                Text(item.defType, modifier = Modifier.weight(1f))
                Text(item.name, modifier = Modifier.weight(1f))
                Text(item.value, modifier = Modifier.weight(1f))
                Text(item.describtion ?: "", modifier = Modifier.weight(1f))
                Text(item.status, modifier = Modifier.weight(1f))
                Text(item.fileUrl, modifier = Modifier.weight(1f))

                IconButton(onClick = { onEdit(item) }) {
                    Icon(
                        imageVector = Icons.Default.Edit as ImageVector, contentDescription = "编辑"
                    )
                }
                IconButton(onClick = { onDelete(item) }) {
                    Icon(
                        imageVector = Icons.Default.Delete as ImageVector, contentDescription = "删除"
                    )
                }
            }
            Divider()
        }
    }
}