import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.web.model.enums.Route

@Composable
fun SideMenu(
    currentRoute: Route,
    onRouteChange: (Route) -> Unit
) {
    Surface(
        modifier = Modifier.width(240.dp).fillMaxHeight(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            NavigationDrawerItem(
                icon = { Icon(Icons.Default.Settings, "Dotfiles") },
                label = { Text("Dotfiles 管理") },
                selected = currentRoute == Route.DOTFILES,
                onClick = { onRouteChange(Route.DOTFILES) },
                modifier = Modifier.padding(vertical = 4.dp)
            )

            NavigationDrawerItem(
                icon = { Icon(Icons.Default.Menu, "软件") },
                label = { Text("软件管理") },
                selected = currentRoute == Route.SOFTWARE,
                onClick = { onRouteChange(Route.SOFTWARE) },
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}