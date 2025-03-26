//package com.addzero.web.ui.hooks.form
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.heightIn
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.KeyboardActions
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Clear
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.focus.FocusRequester
//import androidx.compose.ui.focus.focusRequester
//import androidx.compose.ui.focus.onFocusChanged
//import androidx.compose.ui.unit.dp
//import com.addzero.Route
//import com.addzero.common.anno.Shit
//import com.addzero.web.modules.sys.area.SysArea
//import com.addzero.web.ui.hooks.UseHook
//import reactor.core.publisher.Signal.isError
//
//@Route(parent = "测试组件")
//@Shit
//@Composable
//fun 测试UseAutoComplete(): Unit {
//    val map = (1..3).map {
//        val sysArea = SysArea {
//            name = "aaa" + it
//            leveltype = "111" + it
//        }
//        sysArea
//    }
//    val useAutoComplete = UseAutoComplete("aaa", map, { it.name.toString() })
//    useAutoComplete.getState().render()
//}
//
//class UseAutoComplete<T>(
//    val title: String,
//    val options: List<T>,
//    val getLabelFun: (T) -> String,
//) : UseHook<UseAutoComplete<T>> {
//    // 内部状态管理
//    private var inputValue by mutableStateOf("")
//    private var selectedItem by mutableStateOf<T?>(null)
//    private var hasFocus by mutableStateOf(false)
//    private var showSuggestions by mutableStateOf(false)
//
//    // 过滤选项
//
//
//    override val render: @Composable () -> Unit
//        get() = {
//            val focusRequester = remember { FocusRequester() }
//
//
//            // 计算过滤后的选项
//            val filteredOptions = remember(inputValue) {
//                if (inputValue.isBlank()) options
//                else options.filter { getLabelFun(it).contains(other = inputValue, ignoreCase = true) }
//            }
//
//
//
//
//
//
//            Column(modifier = modifier) {
//                // 输入框部分
//                OutlinedTextField(
//                    value = inputValue,
//                    onValueChange = { newValue ->
//                        inputValue = newValue
//                        showSuggestions = true
//                    },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .focusRequester(focusRequester)
//                        .onFocusChanged {
//                            hasFocus = it.hasFocus
//                        },
//                    label = title,
//                    placeholder = "请输入$title",
//                    trailingIcon = if (inputValue.isNotEmpty()) {
//                        {
//                            IconButton(onClick = {
//                                inputValue = ""
//                                selectedItem = null
//                                showSuggestions = false
//                            }) {
//                                Icon(
//                                    imageVector = Icons.Default.Clear,
//                                    contentDescription = "Clear"
//                                )
//                            }
//                        }
//                    } else null,
//                    singleLine = true,
//                )
//
//
//
//                // 建议列表
//                if (hasFocus && showSuggestions && filteredOptions.isNotEmpty()) {
//                    val suggestionModifier = Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 8.dp)
//                        .border(
//                            width = 1.dp,
//                            color = MaterialTheme.colorScheme.outline,
//                            shape = RoundedCornerShape(4.dp)
//                        )
//                        .background(MaterialTheme.colorScheme.surface)
//                        .heightIn(max = 200.dp) // 限制最大高度
//
//                    LazyColumn(
//                        modifier = suggestionModifier,
//                        contentPadding = PaddingValues(vertical = 8.dp)
//                    ) {
//                        items(filteredOptions) { item ->
//                            val itemModifier = if (item == selectedItem) {
//                                Modifier.background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
//                            } else {
//                                Modifier
//                            }
//
//                            Text(
//                                text = getLabelFun(item),
//                                modifier = itemModifier
//                                    .fillMaxWidth()
//                                    .clickable {
//                                        inputValue = getLabelFun(item)
//                                        selectedItem = item
//                                        showSuggestions = false
//                                    }
//                                    .padding(horizontal = 16.dp, vertical = 12.dp),
//                                color = MaterialTheme.colorScheme.onSurface
//                            )
//                        }
//                    }
//                }
//            }
//        }
//
//
//}
