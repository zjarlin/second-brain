import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.addzero.web.config.AppConfig
import com.addzero.web.model.enums.Route
import com.addzero.web.ui.components.Breadcrumb
import com.addzero.web.ui.components.SideMenu
import com.addzero.web.ui.components.TopBar
import com.addzero.web.viewmodel.NotesViewModel

@Composable
fun MainLayout() {
    var isLoggedIn by remember { mutableStateOf(!AppConfig.ENABLE_LOGIN) }
    var currentRoute by remember { mutableStateOf(Route.DOTFILES) }

    val notesViewModel = remember { NotesViewModel() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = AppConfig.Theme.backgroundGradient.map { Color(it) }
                )
            )
    ) {
        if (!isLoggedIn) {
            LoginScreen(onLoginSuccess = { isLoggedIn = true })
        } else {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.White.copy(alpha = AppConfig.Theme.MICA_OPACITY)
            ) {
                Row {
                    SideMenu(
                        currentRoute = currentRoute,
                        onRouteChange = { currentRoute = it }
                    )
                    Column {
                        TopBar()
                        Breadcrumb(currentRoute = currentRoute)
                        MainContent(
                            currentRoute = currentRoute,
                            viewModel = notesViewModel
                        )
                    }
                }
            }
        }
    }
}