//package com.addzero.web.ui.components.crud
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
//import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import com.addzero.web.infra.jimmer.base.pagefactory.PageResult
//
//
//class UsePage<T> {
//    var pageResult by remember { mutableStateOf(PageResult.empty<T>()) }
//
//    val onLast: () -> Unit = {}
//    val onNext: () -> Unit = {}
//    val onChangePageSize: (Int) -> Unit = {}
//
//    private var expanded by remember { mutableStateOf(false) }
//    private val pageSizes = listOf(10, 20, 50, 100)
//
//    @Composable
//    fun render(): Unit {
//        Row(
//            modifier = Modifier.fillMaxWidth().padding(16.dp),
//            horizontalArrangement = Arrangement.Center,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            IconButton(
//                onClick = onLast, enabled = !pageResult.isFirst
//            ) {
//                Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, "上一页")
//            }
//
//            Text(
//                "${pageResult.pageIndex + 1}/${pageResult.totalPages}", modifier = Modifier.padding(horizontal = 16.dp)
//            )
//
//            IconButton(
//                onClick = onNext, enabled = !pageResult.isLast
//            ) {
//                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, "下一页")
//            }
//
//            Text(
//                "共 ${pageResult.totalElements} 条", modifier = Modifier.padding(start = 16.dp)
//            )
//
//            Box(modifier = Modifier.padding(start = 16.dp)) {
//                OutlinedButton(onClick = { expanded = true }) {
//                    Text("${pageResult.pageSize}条/页")
//                }
//                DropdownMenu(
//                    expanded = expanded, onDismissRequest = { expanded = false }) {
//                    pageSizes.forEach { size ->
//                        DropdownMenuItem(text = { Text("${size}条/页") }, onClick = {
//                            onChangePageSize(size)
//                            expanded = false
//                        })
//                    }
//                }
//            }
//        }
//
//    }
//}
//
//
