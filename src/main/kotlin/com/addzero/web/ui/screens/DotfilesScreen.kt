import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.addzero.web.viewmodel.BizEnvVars
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

// 先声明所有子组件
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchPanel(
    searchName: String,
    onSearchNameChange: (String) -> Unit,
    selectedPlatforms: Set<String>,
    onPlatformSelectionChange: (Set<String>) -> Unit,
    selectedOSTypes: Set<String>,
    onOSTypeSelectionChange: (Set<String>) -> Unit,
    onSearch: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        tonalElevation = 1.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // 名称搜索
            OutlinedTextField(
                value = searchName, onValueChange = onSearchNameChange, label = { Text("名称搜索") }, modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 平台选择
            Text("平台类型", style = MaterialTheme.typography.titleSmall)
            Row(
                modifier = Modifier.padding(vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val platforms = listOf("不限", "arm64", "x86_64")
                platforms.forEach { platform ->
                    FilterChip(selected = if (platform == "不限") {
                        selectedPlatforms.isEmpty()
                    } else {
                        selectedPlatforms.contains(platform)
                    }, onClick = {
                        when (platform) {
                            "不限" -> onPlatformSelectionChange(emptySet())
                            else -> {
                                val newSelection = selectedPlatforms.toMutableSet()
                                if (newSelection.contains(platform)) {
                                    newSelection.remove(platform)
                                } else {
                                    newSelection.add(platform)
                                }
                                onPlatformSelectionChange(newSelection)
                            }
                        }
                    }, label = { Text(platform) })
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 操作系统选择
            Text("操作系统", style = MaterialTheme.typography.titleSmall)
            Row(
                modifier = Modifier.padding(vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val osTypes = listOf("不限", "MacOS", "Linux", "Windows")
                osTypes.forEach { osType ->
                    FilterChip(selected = if (osType == "不限") {
                        selectedOSTypes.isEmpty()
                    } else {
                        selectedOSTypes.contains(osType)
                    }, onClick = {
                        when (osType) {
                            "不限" -> onOSTypeSelectionChange(emptySet())
                            else -> {
                                val newSelection = selectedOSTypes.toMutableSet()
                                if (newSelection.contains(osType)) {
                                    newSelection.remove(osType)
                                } else {
                                    newSelection.add(osType)
                                }
                                onOSTypeSelectionChange(newSelection)
                            }
                        }
                    }, label = { Text(osType) })
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 搜索按钮
            Button(
                onClick = onSearch, modifier = Modifier.align(Alignment.End)
            ) {
                Icon(
                    imageVector = Icons.Default.Search as ImageVector, contentDescription = "搜索"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("搜索")
            }
        }
    }
}

@Composable
private fun ActionButtons(
    scope: CoroutineScope,
    viewModel: DotfilesViewModel,
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(onClick = { /* 显示添加对话框 */ }) {
            Text("添加")
        }

        Button(onClick = {
            scope.launch {
                val fileChooser = JFileChooser().apply {
                    fileFilter = FileNameExtensionFilter("环境变量文件", "env", "sh")
                    isMultiSelectionEnabled = true
                }

                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    val files = fileChooser.selectedFiles.map { it.readBytes() }
                    viewModel.importDotfiles(files)
                }
            }
        }) {
            Text("导入")
        }

        Button(onClick = {
            scope.launch {
                viewModel.exportDotfiles()?.let { bytes ->
                    val fileChooser = JFileChooser().apply {
                        fileFilter = FileNameExtensionFilter("环境变量文件", "env")
                        selectedFile = File("dotfiles_export.env")
                    }

                    if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                        fileChooser.selectedFile.writeBytes(bytes)
                    }
                }
            }
        }) {
            Text("导出")
        }

        Button(onClick = {
            scope.launch {
                viewModel.generateConfig()?.let { bytes ->
                    val fileChooser = JFileChooser().apply {
                        fileFilter = FileNameExtensionFilter("Shell 脚本", "sh")
                        selectedFile = File("env_config.sh")
                    }

                    if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                        fileChooser.selectedFile.writeBytes(bytes)
                    }
                }
            }
        }) {
            Text("生成配置")
        }
    }
}

@Composable
private fun DotfilesTable(
    items: List<BizEnvVars>,
    onEdit: (BizEnvVars) -> Unit,
    onDelete: (BizEnvVars) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        items(items) { item ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                Text(item.osType, modifier = Modifier.weight(1f))
                Text(item.osStructure, modifier = Modifier.weight(1f))
                Text(item.defType, modifier = Modifier.weight(1f))
                Text(item.name, modifier = Modifier.weight(1f))
                Text(item.value, modifier = Modifier.weight(1f))
                Text(item.describtion ?: "", modifier = Modifier.weight(1f))
                Text(item.status, modifier = Modifier.weight(1f))
                Text(item.fileUrl, modifier = Modifier.weight(1f))

                IconButton(onClick = { onEdit(item) }) {
                    Icon(
                        imageVector = Icons.Default.Edit as ImageVector, contentDescription = "编辑"
                    )
                }
                IconButton(onClick = { onDelete(item) }) {
                    Icon(
                        imageVector = Icons.Default.Delete as ImageVector, contentDescription = "删除"
                    )
                }
            }
            Divider()
        }
    }
}

// 最后声明主组件
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DotfilesScreen() {
    val scope = rememberCoroutineScope()
    val viewModel = remember { DotfilesViewModel(scope) }
    val pageResult = viewModel.pageResult
    val dotfilesList = pageResult?.content ?: emptyList()

    // 搜索条件状态
    var searchName by remember { mutableStateOf("") }
    var selectedPlatforms by remember { mutableStateOf(setOf<String>()) }
    var selectedOSTypes by remember { mutableStateOf(setOf<String>()) }

    Column {
        // 搜索区域
        SearchPanel(
            searchName = searchName,
            onSearchNameChange = { searchName = it },
            selectedPlatforms = selectedPlatforms,
            onPlatformSelectionChange = { selectedPlatforms = it },
            selectedOSTypes = selectedOSTypes,
            onOSTypeSelectionChange = { selectedOSTypes = it },
            onSearch = {
                viewModel.searchDotfiles(
                    name = searchName,
                    platforms = selectedPlatforms,
                    osTypes = selectedOSTypes,
                    page = 0 // 搜索时重置到第一页
                )
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 工具栏
        ActionButtons(scope, viewModel)

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

        // 数据表格
        DotfilesTable(
            items = dotfilesList,
            onEdit = { /* 显示编辑对话框 */ },
            onDelete = { viewModel.deleteDotfile(it.id) }
        )

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
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = "上一页"
                    )
                }

                Text(
                    "${result.pageNumber + 1}/${result.totalPages}",
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                IconButton(
                    onClick = { viewModel.nextPage() },
                    enabled = !result.isLast
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "下一页"
                    )
                }

                Text(
                    "共 ${result.totalElements} 条",
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}