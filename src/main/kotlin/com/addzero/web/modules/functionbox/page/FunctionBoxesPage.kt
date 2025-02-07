package com.addzero.web.modules.functionbox.page

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
import cn.hutool.core.util.ClassUtil
import com.addzero.web.modules.common.dialog.CommonDialog
import com.addzero.web.modules.functionbox.model.FunctionBoxSpec
import com.addzero.web.ui.components.system.dynamicroute.MetaSpec
import com.addzero.web.ui.components.system.dynamicroute.RouteMetadata
import kotlinx.coroutines.launch

@Composable
fun <F : FunctionBoxSpec> FunctionBoxItem(
    function: F,
    showContent: Boolean,
    onShowContentChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable { onShowContentChange(true) },
        elevation = 4.dp
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
                tint = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = function.name,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = function.description,
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
        }
    }

    if (showContent) {
        function.invoke()
    }
}

class FunctionBoxesPage : MetaSpec {
    override val metadata: RouteMetadata
        get() = RouteMetadata(
            parentRefPath = null,
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