package com.addzero.web.ui.hooks.table.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardDoubleArrowLeft
import androidx.compose.material.icons.filled.KeyboardDoubleArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.addzero.common.kt_util.isNumber
import com.addzero.web.ui.hooks.UseHook

class UseTablePagination : UseHook<UseTablePagination> {

    private val initPageNo = 1
    private val defaultPageSize = 10

    private var _pageNo by mutableStateOf(initPageNo)

    var pageNo: Int
        get() = _pageNo
        set(value) {
            _pageNo = when {
                value < initPageNo -> initPageNo
                totalPages in initPageNo until value -> totalPages
                else -> value
            }
        }


    // 每页显示数量
    var pageSize by mutableStateOf(defaultPageSize)

    // 总页数
    var totalPages by mutableStateOf(1)

//    // 页码改变回调
//    var onPageChange: (Int) -> Unit by mutableStateOf({_->})
//
//    // 每页显示数量改变回调
//    var onPageSizeChange: (Int) -> Unit by mutableStateOf({_->})

    override val render: @Composable () -> Unit
        get() = {
            Surface(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedIconButton(
                            onClick = { pageNo = initPageNo }, enabled = pageNo > 1, modifier = Modifier.size(36.dp)
                        ) {
                            Icon(Icons.Default.KeyboardDoubleArrowLeft, contentDescription = "首页")
                        }

                        OutlinedIconButton(
                            onClick = { pageNo -= 1 }, enabled = pageNo > 1, modifier = Modifier.size(36.dp)
                        ) {
                            Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "上一页")
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            OutlinedTextField(
                                value = pageNo.toString(),
                                onValueChange = {
                                    if (it.isNumber()) {

                                        pageNo = it.toInt()
                                    }
                                },
                                modifier = Modifier.width(60.dp),
                                singleLine = true,
                                textStyle = MaterialTheme.typography.bodyMedium,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )
                            Text(
                                "/$totalPages", style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        OutlinedIconButton(
                            onClick = { pageNo++ }, enabled = pageNo < totalPages, modifier = Modifier.size(36.dp)
                        ) {
                            Icon(Icons.Default.KeyboardArrowRight, contentDescription = "下一页")
                        }

                        OutlinedIconButton(
                            onClick = { pageNo = totalPages },
                            enabled = pageNo < totalPages,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(Icons.Default.KeyboardDoubleArrowRight, contentDescription = "末页")
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text(
                            "每页显示: ", style = MaterialTheme.typography.bodyMedium
                        )
                        listOf(10, 20, 30, 40, 50).forEach { size ->
                            OutlinedButton(
                                onClick = { pageSize = size },
                                modifier = Modifier.padding(horizontal = 4.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Text("$size")
                            }
                        }

                        OutlinedTextField(
                            value = pageSize.toString(),
                            onValueChange = {
                                if (it.isNumber()) {
                                    pageSize = it.toInt()

                                }


                            },
                            modifier = Modifier.width(80.dp).padding(horizontal = 4.dp),
                            singleLine = true,
                            textStyle = MaterialTheme.typography.bodyMedium,
                            placeholder = { Text("自定义", style = MaterialTheme.typography.bodyMedium) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }
                }
            }
        }
}