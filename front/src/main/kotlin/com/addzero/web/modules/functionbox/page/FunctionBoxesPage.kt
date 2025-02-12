package com.addzero.web.modules.functionbox.page

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.hutool.core.util.ClassUtil
import com.addzero.web.modules.functionbox.model.FunctionBoxSpec
import com.addzero.web.ui.components.system.dynamicroute.MetaSpec
import com.addzero.web.ui.components.system.dynamicroute.RouteMetadata

@Composable
fun <F : FunctionBoxSpec> FunctionBoxItem(
    function: F,
    showContent: Boolean,
    onShowContentChange: (Boolean) -> Unit
) {
    val colors = remember {
        listOf(
            Color(0xFF007AFF),
            Color(0xFF34C759),
            Color(0xFFFF9500),
            Color(0xFFFF2D55),
            Color(0xFF5856D6),
            Color(0xFFAF52DE)
        )
    }
    val startColor = remember { colors.random() }
    val endColor = remember { colors.random() }

    Card(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .aspectRatio(1.0f)
            .clickable { onShowContentChange(true) },
        elevation = 2.dp,
        backgroundColor = Color.Transparent,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = androidx.compose.ui.graphics.Brush.linearGradient(
                        colors = listOf(startColor, endColor)
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = function.icon,
                    contentDescription = function.name,
                    modifier = Modifier.size(32.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = function.description,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = 13.dp.value.sp,
                        lineHeight = 18.dp.value.sp
                    )
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
            title = "功能盒子",
            icon = Icons.Filled.Apps,
            visible = true,
            permissions = listOf("function_box:view"),
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