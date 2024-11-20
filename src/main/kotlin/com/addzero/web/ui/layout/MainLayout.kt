import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.addzero.web.config.AppConfig

@Composable
fun MainLayout() {
    var isLoggedIn by remember { mutableStateOf(!AppConfig.ENABLE_LOGIN) }
    
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
                    SideMenu()
                    Column {
                        TopBar()
                        Breadcrumb()
                        MainContent()
                    }
                }
            }
        }
    }
}