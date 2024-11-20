import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier

@Composable
fun SideMenu() {
    var selectedItem by remember { mutableStateOf(0) }

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
                selected = selectedItem == 0,
                onClick = { selectedItem = 0 },
                modifier = Modifier.padding(vertical = 4.dp)
            )

            // 可以添加更多菜单项
        }
    }
}