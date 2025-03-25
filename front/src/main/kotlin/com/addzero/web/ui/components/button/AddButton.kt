package com.addzero.web.ui.components.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun AddButton(
    displayName: String,
    icon: ImageVector = Icons.Default.Add,
    onClick: () -> Unit,
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    backgroundBrush: Brush? = null
) {





    Button(
        onClick = onClick,
        elevation = elevation,
        modifier = if (backgroundBrush != null) {
            modifier.background(brush = backgroundBrush, shape = MaterialTheme.shapes.small)
        } else {
            modifier
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = if (backgroundBrush != null) Color.Transparent else containerColor
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {


            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = displayName,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

