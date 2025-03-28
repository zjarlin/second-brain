package com.addzero.web.modules.functionbox.page

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cn.hutool.core.util.ClassUtil
import com.addzero.web.modules.functionbox.model.FunctionBoxSpec
import com.addzero.web.ui.system.dynamicroute.MetaSpec
import com.addzero.web.ui.system.dynamicroute.RouteMetadata

@Composable
fun <F : FunctionBoxSpec> FunctionBoxItem(
    function: F,
    showContent: Boolean,
    onShowContentChange: (Boolean) -> Unit
) {
    var isHovered by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val colors = remember {
        listOf(
            Color(0xFF4158D0),
            Color(0xFFC850C0),
            Color(0xFFFF512F),
            Color(0xFFDD2476),
            Color(0xFF11998E),
            Color(0xFF38EF7D)
        )
    }
    val startColor = remember { colors.random() }
    val endColor = remember { colors.random() }

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is HoverInteraction.Enter -> isHovered = true
                is HoverInteraction.Exit -> isHovered = false
            }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.2f)
            .clickable { onShowContentChange(true) }
            .hoverable(interactionSource)
            .graphicsLayer {
                scaleX = if (isHovered) 1.02f else 1f
                scaleY = if (isHovered) 1.02f else 1f
                alpha = if (isHovered) 0.95f else 1f
            },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 6.dp,
            hoveredElevation = 8.dp
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(startColor, endColor)
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = function.icon,
                    contentDescription = function.name,
                    modifier = Modifier
                        .size(48.dp)
                        .graphicsLayer {
                            scaleX = if (isHovered) 1.1f else 1f
                            scaleY = if (isHovered) 1.1f else 1f
                        },
                    tint = Color.White
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = function.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = function.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    if (showContent) {
        function.invoke()
    }
}

class FunctionBoxesPage : MetaSpec {
    override val metadata: RouteMetadata
        get() = RouteMetadata(
            parentName = null,
            title = "工具箱",
            icon = Icons.Filled.Apps,
            visible = true,
//            permissions = listOf("function_box:view"),
            order = 60.0
        )

    @Composable
    override fun render() {
        var functionBoxes by remember { mutableStateOf<List<FunctionBoxSpec>>(emptyList()) }
        var selectedFunctionClassName by remember { mutableStateOf<String?>(null) }

        LaunchedEffect(Unit) {
            val scanPackageBySuper = ClassUtil.scanPackageBySuper(
                "com.addzero.web.modules.functionbox",
                FunctionBoxSpec::class.java
            )
            functionBoxes = scanPackageBySuper.mapNotNull { clazz ->
                try {
                    clazz.getDeclaredConstructor().newInstance() as? FunctionBoxSpec
                } catch (e: Exception) {
                    null
                }
            }
        }

        Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Column {
                Text(
                    text = metadata.title,
                    modifier = Modifier.padding(bottom = 16.dp),
                    textAlign = TextAlign.Center
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(functionBoxes.size) { index ->
                        val function = functionBoxes[index]
                        FunctionBoxItem(
                            function = function,
                            showContent = selectedFunctionClassName == function.javaClass.name,
                            onShowContentChange = { show ->
                                selectedFunctionClassName = if (selectedFunctionClassName == function.javaClass.name) null else function.javaClass.name
                            }
                        )
                    }
                }
            }
        }
    }
}
