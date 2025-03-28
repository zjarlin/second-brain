//package com.addzero.web.ui.dsl.forms
//
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material.OutlinedTextField
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.text.input.PasswordVisualTransformation
//import androidx.compose.ui.unit.dp
//
///**
// * 表单容器
// */
//@Composable
//fun forms(
//    modifier: Modifier = Modifier,
//    content: FormsScope.() -> Unit
//) {
//    FormsScope().apply(content)
//}
//
///**
// * 表单作用域
// */
//class FormsScope {
//    private val forms = mutableListOf<FormScope>()
//
//    /**
//     * 添加一个表单
//     */
//    fun form(
//        modifier: Modifier = Modifier,
//        content: FormScope.() -> Unit
//    ) {
//        forms.add(FormScope(modifier).apply(content))
//    }
//
//    /**
//     * 渲染所有表单
//     */
//    @Composable
//    fun render() {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            verticalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            forms.forEach { form ->
//                form.render()
//            }
//        }
//    }
//}
//
///**
// * 表单作用域
// */
//class FormScope(
//    private val modifier: Modifier
//) {
//    private val fields = mutableListOf<FormFieldScope>()
//
//    /**
//     * 添加一个表单字段
//     */
//    fun field(
//        modifier: Modifier = Modifier,
//        content: FormFieldScope.() -> Unit
//    ) {
//        fields.add(FormFieldScope(modifier).apply(content))
//    }
//
//    /**
//     * 渲染表单
//     */
//    @Composable
//    fun render() {
//        Column(
//            modifier = modifier,
//            verticalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            fields.forEach { field ->
//                field.render()
//            }
//        }
//    }
//}
//
///**
// * 表单字段作用域
// */
//class FormFieldScope(
//    private val modifier: Modifier
//) {
//    private var label: String? = null
//    private var content: @Composable () -> Unit = {}
//    private var error: String? = null
//    private var required: Boolean = false
//
//    /**
//     * 设置标签
//     */
//    fun label(text: String) {
//        label = text
//    }
//
//    /**
//     * 设置内容
//     */
//    fun content(content: @Composable () -> Unit) {
//        this.content = content
//    }
//
//    /**
//     * 设置错误信息
//     */
//    fun error(text: String?) {
//        error = text
//    }
//
//    /**
//     * 设置是否必填
//     */
//    fun required(value: Boolean = true) {
//        required = value
//    }
//
//    /**
//     * 渲染字段
//     */
//    @Composable
//    fun render() {
//        Column(modifier = modifier) {
//            // 标签
//            label?.let {
//                Text(
//                    text = if (required) "$it*" else it,
//                    style = MaterialTheme.typography.bodyMedium
//                )
//            }
//
//            // 内容
//            content()
//
//            // 错误信息
//            error?.let {
//                Text(
//                    text = it,
//                    color = MaterialTheme.colorScheme.error,
//                    style = MaterialTheme.typography.bodySmall,
//                    modifier = Modifier.padding(top = 4.dp)
//                )
//            }
//        }
//    }
//}
//
//// 使用示例
//@Composable
//fun FormsExample() {
//    forms {
//        form {
//            field {
//                label("用户名")
//                required()
//                content {
//                    OutlinedTextField(
//                        value = "",
//                        onValueChange = {},
//                        modifier = Modifier.fillMaxWidth()
//                    )
//                }
//            }
//
//            field {
//                label("密码")
//                required()
//                content {
//                    OutlinedTextField(
//                        value = "",
//                        onValueChange = {},
//                        modifier = Modifier.fillMaxWidth(),
//                        visualTransformation = PasswordVisualTransformation()
//                    )
//                }
//            }
//        }
//
//        form {
//            field {
//                label("邮箱")
//                content {
//                    OutlinedTextField(
//                        value = "",
//                        onValueChange = {},
//                        modifier = Modifier.fillMaxWidth()
//                    )
//                }
//                error("请输入有效的邮箱地址")
//            }
//        }
//    }
//}
