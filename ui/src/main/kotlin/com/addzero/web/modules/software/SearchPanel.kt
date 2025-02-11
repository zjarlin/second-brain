package com.addzero.web.modules.software

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.web.model.enums.OsType
import com.addzero.web.model.enums.PlatformType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchPanel(
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
        modifier = Modifier.fillMaxWidth(), tonalElevation = 1.dp, color = MaterialTheme.colorScheme.surface
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // 搜索框
            OutlinedTextField(value = searchText, onValueChange = onSearchTextChange, label = { Text("搜索软件") }, modifier = Modifier.fillMaxWidth(), trailingIcon = {
                Row {
                    IconButton(onClick = onAiSearch) {
                        Icon(
                            imageVector = Icons.Default.Person, contentDescription = "AI搜索"
                        )
                    }
                    IconButton(onClick = onSearch) {
                        Icon(
                            imageVector = Icons.Default.Search, contentDescription = "搜索"
                        )
                    }
                }
            })

            Spacer(modifier = Modifier.height(8.dp))

            // 平台选择
            Text("平台类型", style = MaterialTheme.typography.titleSmall)
            Row(
                modifier = Modifier.padding(vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PlatformType.values().forEach { platform ->
                    FilterChip(selected = selectedPlatform == platform, onClick = { onPlatformChange(platform) }, label = { Text(platform.label) })
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 操作系统选择
            Text("操作系统", style = MaterialTheme.typography.titleSmall)
            Row(
                modifier = Modifier.padding(vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OsType.values().forEach { osType ->
                    FilterChip(selected = if (osType == OsType.ALL) {
                        selectedOsTypes.isEmpty()
                    } else {
                        selectedOsTypes.contains(osType)
                    }, onClick = {
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
                    }, label = { Text(osType.label) })
                }
            }
        }
    }
}