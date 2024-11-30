import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.web.ui.components.NotesList
import com.addzero.web.ui.components.UploadDialog
import com.addzero.web.modules.note.notes.NotesViewModel

@Composable
fun NotesPage(viewModel: NotesViewModel) {
    var showUploadDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("知识库管理", style = MaterialTheme.typography.headlineMedium)
            Button(onClick = { showUploadDialog = true }) {
                Text("上传文档")
            }
        }

        Spacer(Modifier.height(16.dp))

        // 文档列表
        NotesList(
            notes = viewModel.notes,
            onNoteClick = { note ->
                viewModel.currentNote = note
            },
            onDeleteClick = { note ->
                viewModel.deleteNote(note.id)
            }
        )
    }

    // 上传对话框
    if (showUploadDialog) {
        UploadDialog(
            onDismiss = { showUploadDialog = false },
            onUpload = { file, filename ->
                viewModel.uploadFile(file, filename)
                showUploadDialog = false
            }
        )
    }
}