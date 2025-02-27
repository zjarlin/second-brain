//package com.addzero.web.ui.hooks
//
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import java.time.LocalDate
//import java.time.LocalDateTime
//
///**
// * 搜索字段的元数据定义
// */
//data class SearchField<T>(
//    val name: String,
//    val title: String,
//    val defaultValue: T? = null,
//    val childrenField: String? = null,
//    val cascadeOptions: List<T>? = null,
//    val dependsOn: String? = null,
//    val customRender: @Composable (value: T?, onValueChange: (T?) -> Unit, options: List<T>?, context: Map<String, Any?>) -> Unit = { value, onValueChange, options, context ->
//
//        when (T::class) {
//            LocalDateTime::class -> DateTimePickerField(value as LocalDateTime?, onValueChange as (LocalDateTime?) -> Unit)
//            LocalDate::class -> DatePickerField(value as LocalDate?, onValueChange as (LocalDate?) -> Unit)
//            String::class -> when {
//                // 根据字段名称判断使用何种文本输入组件
//                name.contains("description", ignoreCase = true) -> LongTextField(value as String?, onValueChange as (String?) -> Unit)
//                name.contains("suggest", ignoreCase = true) -> AutoCompleteField(value as String?, onValueChange as (String?) -> Unit)
//                else -> TextField(value as String?, onValueChange as (String?) -> Unit)
//            }
//            else -> when {
//                // 处理树形选择
//                childrenField != null -> TreeSelectField(value, onValueChange, options ?: emptyList(), childrenField)
//                // 处理级联选择
//                cascadeOptions != null -> CascadeSelectField(value, onValueChange, cascadeOptions, context)
//                // 处理枚举类型
//                (T::class.java.isEnum) -> EnumDropdownField(value, onValueChange, T::class.java)
//                else -> TextField(value?.toString() ?: "", { onValueChange(it as T?) })
//            }
//        }
//    }
//)
//
//@Composable
//private fun DateTimePickerField(value: LocalDateTime?, onValueChange: (LocalDateTime?) -> Unit) {
//
//    // 实现日期时间选择器
//    OutlinedTextField(
//        value = value?.toString() ?: "",
//        onValueChange = { onValueChange(LocalDateTime.parse(it)) },
//        placeholder = { Text("选择日期时间") }
//    )
//}
//
//@Composable
//private fun DatePickerField(value: LocalDate?, onValueChange: (LocalDate?) -> Unit) {
//    // 实现日期选择器
//    OutlinedTextField(
//        value = value?.toString() ?: "",
//        onValueChange = { onValueChange(LocalDate.parse(it)) },
//        placeholder = { Text("选择日期") }
//    )
//}
//
//@Composable
//private fun LongTextField(value: String?, onValueChange: (String?) -> Unit) {
//    OutlinedTextField(
//        value = value ?: "",
//        onValueChange = { onValueChange(it) },
//        maxLines = 5,
//        placeholder = { Text("请输入详细描述") }
//    )
//}
//
//@Composable
//private fun TextField(value: String?, onValueChange: (String?) -> Unit) {
//    OutlinedTextField(
//        value = value ?: "",
//        onValueChange = { onValueChange(it) },
//        singleLine = true
//    )
//}
//
//@Composable
//private fun AutoCompleteField(value: String?, onValueChange: (String?) -> Unit) {
//    // 实现自动完成输入框
//    OutlinedTextField(
//        value = value ?: "",
//        onValueChange = { onValueChange(it) },
//        placeholder = { Text("输入关键字自动提示") }
//    )
//}
//
//@Composable
//private fun <T : Enum<T>> EnumDropdownField(value: T?, onValueChange: (T?) -> Unit, enumClass: Class<T>) {
//    var expanded by remember { mutableStateOf(false) }
//    val options = enumClass.enumConstants.toList()
//
//    Box {
//        OutlinedTextField(
//            value = value?.name ?: "",
//            onValueChange = {},
//            readOnly = true,
//            placeholder = { Text("请选择") },
//            modifier = Modifier.clickable { expanded = true }
//        )
//
//        DropdownMenu(
//            expanded = expanded,
//            onDismissRequest = { expanded = false }
//        ) {
//            options.forEach { option ->
//                DropdownMenuItem(
//                    text = { Text(option.name) },
//                    onClick = {
//                        onValueChange(option)
//                        expanded = false
//                    }
//                )
//            }
//        }
//    }
//}
//
//@Composable
//private fun <T> TreeSelectField(
//    value: T?,
//    onValueChange: (T?) -> Unit,
//    options: List<T>,
//    childrenField: String
//) {
//    var expanded by remember { mutableStateOf(false) }
//
//    Box {
//        OutlinedTextField(
//            value = value?.toString() ?: "",
//            onValueChange = {},
//            readOnly = true,
//            placeholder = { Text("请选择") },
//            modifier = Modifier.clickable { expanded = true }
//        )
//
//        DropdownMenu(
//            expanded = expanded,
//            onDismissRequest = { expanded = false }
//        ) {
//            TreeSelectItems(options, childrenField, value, onValueChange) { expanded = false }
//        }
//    }
//}
//
//@Composable
//private fun <T> TreeSelectItems(
//    items: List<T>,
//    childrenField: String,
//    selectedValue: T?,
//    onValueChange: (T?) -> Unit,
//    onDismiss: () -> Unit,
//    level: Int = 0
//) {
//    items.forEach { item ->
//        val children = item::class.java.getDeclaredField(childrenField).apply { isAccessible = true }.get(item) as? List<T>
//
//        Row(modifier = Modifier.padding(start = (level * 16).dp)) {
//            DropdownMenuItem(
//                text = { Text(item.toString()) },
//                onClick = {
//                    onValueChange(item)
//                    onDismiss()
//                }
//            )
//        }
//
//        children?.let { childItems ->
//            TreeSelectItems(
//                items = childItems,
//                childrenField = childrenField,
//                selectedValue = selectedValue,
//                onValueChange = onValueChange,
//                onDismiss = onDismiss,
//                level = level + 1
//            )
//        }
//    }
//}
//
//@Composable
//private fun <T> CascadeSelectField(
//    value: T?,
//    onValueChange: (T?) -> Unit,
//    options: List<T>,
//    context: Map<String, Any?>
//) {
//    var expanded by remember { mutableStateOf(false) }
//    val dependentValue = context["dependsOn"] as? T
//
//    // 根据依赖值过滤选项
//    val filteredOptions = if (dependentValue != null) {
//        options.filter { option ->
//            // 这里需要根据实际业务逻辑实现过滤规则
//            true
//        }
//    } else {
//        options
//    }
//
//    Box {
//        OutlinedTextField(
//            value = value?.toString() ?: "",
//            onValueChange = {},
//            readOnly = true,
//            placeholder = { Text("请选择") },
//            modifier = Modifier.clickable { expanded = true }
//        )
//
//        DropdownMenu(
//            expanded = expanded,
//            onDismissRequest = { expanded = false }
//        ) {
//            filteredOptions.forEach { option ->
//                DropdownMenuItem(
//                    text = { Text(option.toString()) },
//                    onClick = {
//                        onValueChange(option)
//                        expanded = false
//                    }
//                )
//            }
//        }
//    }
//}
//
///**
// * 搜索控件的Hook
// */
//class UseSearch(
//    private val fields: List<SearchField<*>>,
//    private val modifier: Modifier = Modifier,
//    private val onFinish: (Map<String, Any?>) -> Unit = {},
//    private val onClose: () -> Unit = {}
//) : UseHook<UseSearch>() {
//
//    // 存储所有字段的值
//    private var fieldValues by mutableStateOf(mutableMapOf<String, Any?>())
//
//    init {
//        // 初始化默认值
//        fields.forEach { field ->
//            fieldValues[field.name] = field.defaultValue
//        }
//    }
//
//    // 清空所有字段值
//    fun clear() {
//        fieldValues.clear()
//        fields.forEach { field ->
//            fieldValues[field.name] = field.defaultValue
//        }
//        onClose()
//    }
//
//    // 提交搜索
//    private fun submit() {
//        onFinish(fieldValues.toMap())
//    }
//
//    @Composable
//    override fun show(state: UseSearch) {
//        Column(
//            modifier = modifier.padding(16.dp),
//            verticalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            // 渲染搜索字段
//            fields.forEach { field ->
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.spacedBy(8.dp)
//                ) {
//                    Text(
//                        text = field.title,
//                        style = MaterialTheme.typography.bodyMedium,
//                        modifier = Modifier.width(80.dp)
//                    )
//
//                    Box(modifier = Modifier.weight(1f)) {
//                        @Suppress("UNCHECKED_CAST")
//                        (field as SearchField<Any?>).customRender(
//                            fieldValues[field.name],
//                            { value -> fieldValues[field.name] = value }
//                        )
//                    }
//                }
//            }
//
//            // 操作按钮
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.End,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                TextButton(onClick = { state.clear() }) {
//                    Text("清空")
//                }
//                Button(
//                    onClick = { state.submit() },
//                    modifier = Modifier.padding(start = 8.dp)
//                ) {
//                    Text("搜索")
//                }
//            }
//        }
//    }
//}
