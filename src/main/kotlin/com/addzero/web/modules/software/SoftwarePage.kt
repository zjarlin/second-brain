package com.addzero.web.modules.software

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.addzero.web.model.enums.OsType
import com.addzero.web.model.enums.PlatformType
import com.addzero.web.ui.components.system.dynamicroute.MetaSpec
import com.addzero.web.ui.components.system.dynamicroute.RouteMetadata

class SoftwarePage : MetaSpec {

    override val metadata: RouteMetadata
        get() = RouteMetadata(
            refPath = "/software",
            parentRefPath = "管理",
            title = "软件管理",
            icon = Icons.Filled.Apps,
            visible = true ,
            permissions = emptyList()
        )


    @Composable
    override fun render() {
        val scope = rememberCoroutineScope()
        val viewModel = remember { SoftwareViewModel(scope) }
        val pageResult = viewModel.pageResult
        val softwareList = pageResult?.content ?: emptyList()

        // 搜索条件状态
        var searchText by remember { mutableStateOf("") }
        var selectedPlatform by remember { mutableStateOf(PlatformType.ALL) }
        var selectedOsTypes by remember { mutableStateOf(setOf<OsType>()) }

        Column {
            // 搜索区域
            SearchPanel(
                searchText = searchText,
                onSearchTextChange = { searchText = it },
                selectedPlatform = selectedPlatform,
                onPlatformChange = { selectedPlatform = it },
                selectedOsTypes = selectedOsTypes,
                onOsTypesChange = { selectedOsTypes = it },
                onSearch = {
                    viewModel.searchSoftware(
                        keyword = searchText,
                        platform = selectedPlatform,
                        osTypes = selectedOsTypes,
                        useAI = false,
                        page = 0
                    )
                },
                onAiSearch = {
                    viewModel.searchSoftware(
                        keyword = searchText,
                        platform = selectedPlatform,
                        osTypes = selectedOsTypes,
                        useAI = true,
                        page = 0
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 加载状态
            if (viewModel.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            // 错误提示
            viewModel.error?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }

            // 软件列表
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 300.dp),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(softwareList) { software ->
                    SoftwareCard(software = software)
                }
            }

            // 分页控制
            pageResult?.let { result ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { viewModel.previousPage() },
                        enabled = !result.isFirst
                    ) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, "上一页")
                    }

                    Text(
                        "${result.pageNumber + 1}/${result.totalPages}",
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    IconButton(
                        onClick = { viewModel.nextPage() },
                        enabled = !result.isLast
                    ) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, "下一页")
                    }

                    Text(
                        "共 ${result.totalElements} 条",
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        }
    }
}