import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.web.model.enums.Route

@Composable
fun MainContent(currentRoute: Route) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            when (currentRoute) {
                Route.DOTFILES -> DotfilesScreen()
                Route.SOFTWARE -> SoftwareScreen()
            }
        }
    }
}