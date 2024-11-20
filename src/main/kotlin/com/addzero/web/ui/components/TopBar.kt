import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.addzero.web.config.AppConfig

@Composable
fun TopBar() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(
                brush = Brush.horizontalGradient(
                    colors = AppConfig.Theme.primaryGradient.map { Color(it) }
                )
            ),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Dotfiles 管理系统",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = { /* 打开通知 */ }) {
                    Icon(
                        Icons.Default.Notifications,
                        contentDescription = "通知",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
                
                IconButton(onClick = { /* 打开用户菜单 */ }) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "用户",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
} 