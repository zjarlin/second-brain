import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.addzero.web.model.Software
import com.addzero.web.model.enums.OsType
import com.addzero.web.model.enums.PlatformType
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SoftwareScreen() {
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
                    Icon(Icons.Default.KeyboardArrowRight, "下一页")
                }

                Text(
                    "共 ${result.totalElements} 条",
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchPanel(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    selectedPlatform: PlatformType,
    onPlatformChange: (PlatformType) -> Unit,
    selectedOsTypes: Set<OsType>,
    onOsTypesChange: (Set<OsType>) -> Unit,
    onSearch: () -> Unit,
    onAiSearch: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        tonalElevation = 1.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // 搜索框
            OutlinedTextField(
                value = searchText,
                onValueChange = onSearchTextChange,
                label = { Text("搜索软件") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    Row {
                        IconButton(onClick = onAiSearch) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "AI搜索"
                            )
                        }
                        IconButton(onClick = onSearch) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "搜索"
                            )
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 平台选择
            Text("平台类型", style = MaterialTheme.typography.titleSmall)
            Row(
                modifier = Modifier.padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PlatformType.values().forEach { platform ->
                    FilterChip(
                        selected = selectedPlatform == platform,
                        onClick = { onPlatformChange(platform) },
                        label = { Text(platform.label) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 操作系统选择
            Text("操作系统", style = MaterialTheme.typography.titleSmall)
            Row(
                modifier = Modifier.padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OsType.values().forEach { osType ->
                    FilterChip(
                        selected = if (osType == OsType.ALL) {
                            selectedOsTypes.isEmpty()
                        } else {
                            selectedOsTypes.contains(osType)
                        },
                        onClick = {
                            when (osType) {
                                OsType.ALL -> onOsTypesChange(emptySet())
                                else -> {
                                    val newSelection = selectedOsTypes.toMutableSet()
                                    if (newSelection.contains(osType)) {
                                        newSelection.remove(osType)
                                    } else {
                                        newSelection.add(osType)
                                    }
                                    onOsTypesChange(newSelection)
                                }
                            }
                        },
                        label = { Text(osType.label) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SoftwareCard(software: Software) {
    val dateFormat = remember { SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()) }

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = { /* 点击查看详情 */ }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = software.name,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = software.version,
                    style = MaterialTheme.typography.labelMedium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = software.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("平台: ${software.platform}")
                    Text("系统: ${software.supportedOs.joinToString(", ")}")
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("下载量: ${software.downloads}")
                    Text("更新: ${dateFormat.format(Date(software.updateTime))}")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = { /* 查看详情 */ }) {
                    Text("详情")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { /* 下载 */ }) {
                    Text("下载")
                }
            }
        }
    }
}