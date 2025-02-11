package com.addzero.web.modules.dotfiles

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

// 先声明所有子组件
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchPanel(
    searchName: String,
    onSearchNameChange: (String) -> Unit,
    selectedPlatforms: Set<String>,
    onPlatformSelectionChange: (Set<String>) -> Unit,
    selectedOSTypes: Set<String>,
    onOSTypeSelectionChange: (Set<String>) -> Unit,
    onSearch: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(), tonalElevation = 1.dp, color = MaterialTheme.colorScheme.surface
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