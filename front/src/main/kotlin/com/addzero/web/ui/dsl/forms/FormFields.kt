//package com.addzero.web.ui.dsl.forms
//
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.text.KeyboardActions
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.text.input.PasswordVisualTransformation
//import androidx.compose.ui.text.input.VisualTransformation
//
///**
// * 文本输入字段
// */
//fun FormFieldScope.textField(
//    value: String,
//    onValueChange: (String) -> Unit,
//    modifier: Modifier = Modifier,
//    placeholder: String? = null,
//    singleLine: Boolean = true,
//    visualTransformation: VisualTransformation = VisualTransformation.None,
//    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
//    keyboardActions: KeyboardActions = KeyboardActions.Default,
//) {
//    content {
//        OutlinedTextField(
//            value = value,
//            onValueChange = onValueChange,
//            modifier = modifier.fillMaxWidth(),
//            placeholder = placeholder?.let { { Text(it) } },
//            singleLine = singleLine,
//            visualTransformation = visualTransformation,
//            keyboardOptions = keyboardOptions,
//            keyboardActions = keyboardActions
//        )
//    }
//}
//
///**
// * 密码输入字段
// */
//fun FormFieldScope.passwordField(
//    value: String,
//    onValueChange: (String) -> Unit,
//    modifier: Modifier = Modifier,
//    placeholder: String? = null,
//) {
//    textField(
//        value = value,
//        onValueChange = onValueChange,
//        modifier = modifier,
//        placeholder = placeholder,
//        visualTransformation = PasswordVisualTransformation(),
//        keyboardOptions = KeyboardOptions(
//            keyboardType = KeyboardType.Password
//        )
//    )
//}
//
///**
// * 选择字段
// */
//@OptIn(ExperimentalMaterial3Api::class)
//fun <T> FormFieldScope.select(
//    value: T?,
//    onValueChange: (T) -> Unit,
//    options: List<T>,
//    getLabel: (T) -> String,
//    modifier: Modifier
//    = Modifier,
//    placeholder: String? = null,
//) {
//    var expanded by remember { mutableStateOf(false) }
//
//    content {
//        ExposedDropdownMenuBox(
//            expanded = expanded,
//            onExpandedChange = { expanded = it }
//        ) {
//            OutlinedTextField(
//                value = value?.let(getLabel) ?: "",
//                onValueChange = {},
//                readOnly = true,
//                modifier = modifier.fillMaxWidth(),
//                placeholder = placeholder?.let { { Text(it) } },
//                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
//            )
//
//            ExposedDropdownMenu(
//                expanded = expanded,
//                onDismissRequest = { expanded = false }
//            ) {
//                options.forEach { option ->
//                    DropdownMenuItem(
//                        text = { Text(getLabel(option)) },
//                        onClick = {
//                            onValueChange(option)
//                            expanded = false
//                        }
//                    )
//                }
//            }
//        }
//    }
//}
//
///**
// * 开关字段
// */
//fun FormFieldScope.switch(
//    checked: Boolean,
//    onCheckedChange: (Boolean) -> Unit,
//    modifier: Modifier = Modifier,
//) {
//    content {
//        Switch(
//            checked = checked,
//            onCheckedChange = onCheckedChange,
//            modifier = modifier
//        )
//    }
//}
//
///**
// * 复选框字段
// */
//fun FormFieldScope.checkbox(
//    checked: Boolean,
//    onCheckedChange: (Boolean) -> Unit,
//    modifier: Modifier = Modifier,
//) {
//    content {
//        Checkbox(
//            checked = checked,
//            onCheckedChange = onCheckedChange,
//            modifier = modifier
//        )
//    }
//}
//
//// 使用示例
//@Composable
//fun FormFieldsExample() {
//    var username by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    var selectedOption by remember { mutableStateOf<String?>(null) }
//    var isChecked by remember { mutableStateOf(false) }
//    var isAgreed by remember { mutableStateOf(false) }
//
//    val options = listOf("选项1", "选项2", "选项3")
//
//    forms  {
//        form {
//            field {
//                label("用户名")
//                required()
//                textField(
//                    value = username,
//                    onValueChange = { username = it },
//                    placeholder = "请输入用户名"
//                )
//            }
//
//            field {
//                label("密码")
//                required()
//                passwordField(
//                    value = password,
//                    onValueChange = { password = it },
//                    placeholder = "请输入密码"
//                )
//            }
//
//            field {
//                label("选择")
//                select(
//                    value = selectedOption,
//                    onValueChange = { selectedOption = it },
//                    options = options,
//                    getLabel = { it },
//                    placeholder = "请选择"
//                )
//            }
//
//            field {
//                label("开关")
//                switch(
//                    checked = isChecked,
//                    onCheckedChange = { isChecked = it }
//                )
//            }
//
//            field {
//                label("同意条款")
//                checkbox(
//                    checked = isAgreed,
//                    onCheckedChange = { isAgreed = it }
//                )
//            }
//        }
//    }
//}
