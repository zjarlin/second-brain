import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.web.modules.note.notes.NotesService
import com.addzero.web.modules.note.notes.NotesViewModel
import com.addzero.web.ui.components.system.dynamicroute.MetaSpec
import com.addzero.web.ui.components.system.dynamicroute.RouteMetadata

private const val 知识库问答 = "知识库问答"

class NotesQAPage :MetaSpec {
    override val metadata: RouteMetadata
        get() = RouteMetadata(
            refPath = this.javaClass.name,
            parentRefPath = "",
            title = 知识库问答,
            icon = Icons.Filled.Apps,
            visible = true,
            permissions = emptyList()
        )
    @Composable

    override fun render() {
        val viewModel = remember { NotesViewModel(NotesService()) }

        var question by remember { mutableStateOf("") }

        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text(知识库问答, style = MaterialTheme.typography.headlineMedium)

            Spacer(Modifier.height(16.dp))

            // 问答输入框
            OutlinedTextField(
                value = question,
                onValueChange = { question = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("请输入您的问题") }
            )

            Button(
                onClick = {
                    viewModel.askQuestion(question)
                    question = ""
                },
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text("提问")
            }

            // 显示答案
            viewModel.currentAnswer?.let { answer ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(answer.content)
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        Text("参考来源:", style = MaterialTheme.typography.titleSmall)
                        answer.sources.forEach { source ->
                            Text("• ${source.title}")
                        }
                    }
                }
            }
        }
    }

}

