import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.web.viewmodel.BizEnvVars

@Composable
fun DotfilesScreen() {
    val viewModel = remember { DotfilesViewModel() }
    val dotfilesList = viewModel.dotfilesList

    Column {
        // 工具栏
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = { /* 添加新记录 */ }) {
                Text("添加")
            }
            Button(onClick = { /* 导入功能 */ }) {
                Text("导入")
            }
            Button(onClick = { /* 导出功能 */ }) {
                Text("导出")
            }
        }

        // 数据表格
        DotfilesTable(
            items = dotfilesList,
            onEdit = { /* 编辑记录 */ },
            onDelete = { viewModel.deleteDotfile(it.id) }
        )
    }
}

@Composable
private fun DotfilesTable(
    items: List<BizEnvVars>,
    onEdit: (BizEnvVars) -> Unit,
    onDelete: (BizEnvVars) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        items(items) { item ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
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
                    Icon(Icons.Default.Edit, "编辑")
                }
                IconButton(onClick = { onDelete(item) }) {
                    Icon(Icons.Default.Delete, "删除")
                }
            }
            Divider()
        }
    }
}