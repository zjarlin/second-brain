import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

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
        onCloseRequest = ::exitApplication, title = "Dotfiles 管理系统", state = windowState
    ) {
        App()
    }
}