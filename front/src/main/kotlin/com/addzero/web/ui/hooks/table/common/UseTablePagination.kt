package com.addzero.web.ui.hooks.table.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cn.hutool.core.util.NumberUtil
import com.addzero.web.ui.hooks.UseHook

/**
 * 分页控件
 */
class UseTablePagination(private val initPageNo: Int = 1) : UseHook<UseTablePagination> {

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

    var pageSize by mutableStateOf(10)

    //设置总页数
    var totalPages by mutableStateOf(0)

    override val render: @Composable () -> Unit
        get() = {
            Surface(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        // 分页导航
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                        ) {
                            OutlinedButton(
                                onClick = { pageNo -= 1 },
                                enabled = pageNo > (initPageNo - 1),
                                modifier = Modifier.height(36.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp)
                            ) {
                                Text("上一页", style = MaterialTheme.typography.bodyMedium)
                            }

                            Text(
                                "$pageNo/$totalPages",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )

                            OutlinedButton(
                                onClick = { pageNo += 1 },
                                enabled = pageNo < totalPages,
                                modifier = Modifier.height(36.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp)
                            ) {
                                Text("下一页", style = MaterialTheme.typography.bodyMedium)
                            }
                        }

                        // 页面大小选择器
                        Row(
                            verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Text(
                                "每页显示: ", style = MaterialTheme.typography.bodyMedium
                            )
                            listOf(10L, 30L, 50L, 100L).forEach { size ->
                                OutlinedButton(
                                    onClick = { pageNo = size.toInt() },
                                    modifier = Modifier.padding(horizontal = 4.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                                ) {
                                    Text("$size")
                                }
                            }

                            // 自定义页面大小输入
                            OutlinedTextField(
                                value = pageSize.toString(),
                                onValueChange = {
                                    val b = NumberUtil.isNumber(it) && it.isNotBlank()
                                    if (b) {
                                        pageNo = initPageNo

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

}


