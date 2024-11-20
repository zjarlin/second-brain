import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.web.model.enums.Route

@Composable
fun Breadcrumb(currentRoute: Route) {
    Surface(
        modifier = Modifier.fillMaxWidth().height(48.dp), color = MaterialTheme.colorScheme.surface, tonalElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Text("首页")
            Text(" / ")
            Text(when (currentRoute) {
                Route.DOTFILES -> "Dotfiles 管理"
                Route.SOFTWARE -> "软件管理"
            })
        }
    }
}