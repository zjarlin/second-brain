import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.addzero.web.modules.note.notes.NotesService
import com.addzero.web.modules.note.notes.NotesViewModel
import com.addzero.web.ui.layout.MainLayout

@Composable
@Preview
fun App() {
    AppTheme {
        MainLayout()
    }
}

fun main() = application {
    val windowState = rememberWindowState(
        width = 1200.dp, height = 800.dp
    )

    Window(
        onCloseRequest = ::exitApplication, title = "Compose Multiplatform", state = windowState
    ) {
        App()
    }
}