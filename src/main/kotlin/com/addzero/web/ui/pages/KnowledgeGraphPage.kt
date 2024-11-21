import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.web.viewmodel.NotesViewModel
import com.addzero.web.ui.components.ComposeKnowledgeGraphView

@Composable
fun KnowledgeGraphPage(viewModel: NotesViewModel) {
    var searchQuery by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("知识图谱", style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(16.dp))

        // 搜索框
        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                viewModel.loadKnowledgeGraph(it)
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("搜索知识点") }
        )

        Spacer(Modifier.height(16.dp))

        // 图谱展示
        viewModel.knowledgeGraph?.let { graph ->
            ComposeKnowledgeGraphView(
                nodes = graph.nodes,
                edges = graph.edges,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}