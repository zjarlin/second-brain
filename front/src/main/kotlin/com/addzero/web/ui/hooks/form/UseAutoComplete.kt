package com.addzero.web.ui.hooks.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.input.ImeAction
import com.addzero.Route
import com.addzero.common.anno.Shit
import com.addzero.web.modules.sys.area.SysArea
import com.addzero.web.ui.hooks.UseHook
import java.awt.Color

@Route("测试组件")
@Shit
@Composable
fun 测试UseAutoComplete(): Unit {
    val map = (1..3).map {
        val sysArea = SysArea {
            name = "aaa" + it
            leveltype = "111" + it
        }
        sysArea
    }
    val useAutoComplete = UseAutoComplete("aaa", map, { it.name.toString() })
    useAutoComplete.getState().render()
}

class UseAutoComplete<T>(
    val title: String,
    val options: List<T>,
    val getLabelFun: (T) -> String,
) : UseHook<UseAutoComplete<T>> {
    // 内部状态管理
    private var inputValue by mutableStateOf("")
    private var selectedItem by mutableStateOf<T?>(null)
    private var hasFocus by mutableStateOf(false)
    private var showSuggestions by mutableStateOf(false)
    private var highlightedIndex by mutableStateOf(-1)

    // 公开属性
    val value: T? get() = selectedItem

    // 处理键盘事件的函数
    private fun handleKeyEvent(event: KeyEvent, filteredOptions: List<T>): Boolean {
        if (event.type == KeyEventType.KeyDown) {
            when (event.key) {
                Key.DirectionDown -> {
                    if (filteredOptions.isNotEmpty()) {
                        highlightedIndex = (highlightedIndex + 1).coerceAtMost(filteredOptions.size - 1)
                        return true
                    }
                }

                Key.DirectionUp -> {
                    if (filteredOptions.isNotEmpty()) {
                        highlightedIndex = (highlightedIndex - 1).coerceAtLeast(-1)
                        return true
                    }
                }

                Key.Enter -> {
                    if (highlightedIndex in filteredOptions.indices) {
                        selectItem(filteredOptions[highlightedIndex])
                        return true
                    }
                }

                Key.Escape -> {
                    showSuggestions = false
                    return true
                }
            }
        }
        return false
    }

    private fun selectItem(item: T) {
        inputValue = getLabelFun(item)
        selectedItem = item
        showSuggestions = false
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override val render: @Composable () -> Unit
        get() = {
            val focusRequester = remember { FocusRequester() }

            // 自动请求焦点
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }

            // 过滤选项
            val filteredOptions = remember(inputValue) {
                if (inputValue.isBlank()) options
                else options.filter { getLabelFun(it).contains(inputValue, true) }
            }

            LaunchedEffect(filteredOptions) {
                highlightedIndex = -1
            }

            Column(modifier = modifier) {
                ExposedDropdownMenuBox(
                    expanded = showSuggestions && filteredOptions.isNotEmpty(),
                    onExpandedChange = { showSuggestions = it }
                ) {
                    OutlinedTextField(
                        value = inputValue,
                        onValueChange = { newValue ->
                            inputValue = newValue
                            showSuggestions = true
                            highlightedIndex = -1
                            selectedItem = null // 清除选中项当用户输入时
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                            .focusRequester(focusRequester)
                            .onFocusChanged {
                                hasFocus = it.hasFocus
                                if (it.hasFocus && inputValue.isNotEmpty()) {
                                    showSuggestions = true
                                }
                            }
                            .onKeyEvent { event ->
                                handleKeyEvent(event, filteredOptions)
                            },
                        label = { Text(title) },
                        placeholder = { Text("请输入$title") },
                        trailingIcon = {
                            if (inputValue.isNotEmpty()) {
                                IconButton(onClick = {
                                    inputValue = ""
                                    selectedItem = null
                                    showSuggestions = false
                                }) {
                                    Icon(Icons.Default.Clear, "Clear")
                                }
                            }
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                if (highlightedIndex in filteredOptions.indices) {
                                    selectItem(filteredOptions[highlightedIndex])
                                }
                                showSuggestions = false
                            }
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = showSuggestions && filteredOptions.isNotEmpty(),
                        onDismissRequest = { showSuggestions = false }
                    ) {
                        filteredOptions.forEachIndexed { index, item ->
                            val backgroundColor = when {
                                index == highlightedIndex -> MaterialTheme.colorScheme.primaryContainer
                                item == selectedItem -> MaterialTheme.colorScheme.surfaceVariant
                                else -> Color.white
                            }

                            DropdownMenuItem(
                                text = { Text(getLabelFun(item)) },
                                onClick = { selectItem(item) },
                            )
                        }
                    }
                }
            }
        }
}