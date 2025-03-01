package com.addzero.web.ui.system

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.addzero.web.config.AppConfig

@Composable
fun TopBar() {
    Surface(modifier = Modifier.fillMaxWidth().height(64.dp).background(brush = Brush.horizontalGradient(colors = AppConfig.Theme.primaryGradient.map { Color(it) })), color = Color.Transparent) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "🤔", style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.Person, contentDescription = "用户", tint = Color.White
                    )
                }
            }
        }
    }
}
