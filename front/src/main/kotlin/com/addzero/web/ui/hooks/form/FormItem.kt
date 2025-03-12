package com.addzero.web.ui.hooks.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.addzero.common.kt_util.toNotBlankStr
import com.addzero.web.ui.hooks.table.common.UseTableContent
import com.addzero.web.ui.hooks.table.entity.IColumn
import com.addzero.web.ui.hooks.table.entity.RenderType
import org.babyfish.jimmer.Formula
import kotlin.reflect.full.hasAnnotation

@Composable
fun <E : Any> FormItem(
    icolumn: IColumn<E>,
    useDynamicForm: UseDynamicForm<E>,
    useTableContent: UseTableContent<E>
) {
    val currentFormItem = useTableContent.currentSelectItem
    val renderType = icolumn.renderType
    val getFun = icolumn.getFun
    val setFun = icolumn.setFun

    val fieldValue = currentFormItem?.let { getFun(it) }

    val validRes = currentFormItem?.let { icolumn.validator(it) }

    val text = fieldValue.toNotBlankStr()

    when (renderType) {
        RenderType.TEXT -> {
            OutlinedTextField(
                enabled = icolumn.enabled,
                label = {
                    if (icolumn.required) {
                        Text("* ${icolumn.title}")
                    } else {
                        Text(icolumn.title)
                    }
                },
                value = text,
                onValueChange = { newval ->
                    val oldValue = currentFormItem?.let { icolumn.getFun(it) }
                    // 只有当值真正发生变化时才更新表单
                    if (oldValue != newval) {
                        val newItem = currentFormItem?.let { setFun(it, icolumn, newval) }
                        useDynamicForm.updateFormItem(newItem)
                    }
                },
                modifier = Modifier.Companion.fillMaxWidth(),
                placeholder = { Text(icolumn.placeholder) },
                isError = validRes == false,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Companion.Text)
            )
        }

        RenderType.IMAGE -> {
            Text(text)
        }

        RenderType.CUSTOM -> {
        }

        RenderType.TEXTAREA -> {
            OutlinedTextField(
                enabled = icolumn.enabled,
                label = {
                    if (icolumn.required) {
                        Text("* ${icolumn.title}")
                    } else {
                        Text(icolumn.title)
                    }
                },
                value = text,
                onValueChange = { newval ->
                    val oldValue = currentFormItem?.let { icolumn.getFun(it) }
                    // 只有当值真正发生变化时才更新表单
                    if (oldValue != newval) {
                        val newItem = currentFormItem?.let { setFun(it, icolumn, newval) }
                        useDynamicForm.updateFormItem(newItem)
                    }
                },
                modifier = Modifier.Companion.fillMaxWidth(),
                placeholder = { Text(icolumn.placeholder) },
                isError = validRes == false,
                //                singleLine = true,
                maxLines = 5,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Companion.Text)
            )

        }

        RenderType.SWITCH -> {
            Column {
                Text(icolumn.title)
                Switch(
                    enabled = icolumn.enabled,
                    checked = fieldValue == true,
                    onCheckedChange = {
                        val oldValue = currentFormItem?.let { icolumn.getFun(it) }
                        // 只有当值真正发生变化时才更新表单
                        if (oldValue != it) {
                            val newItem = currentFormItem?.let { p1 -> setFun(p1, icolumn, it) }
                            useDynamicForm.updateFormItem(newItem)
                        }
                    }
                )

            }
        }

        RenderType.TAG -> {
            Text(text)
        }

        RenderType.NUMBER -> {}
        RenderType.LINK -> {}
        RenderType.DATE -> {}
        RenderType.DATETIME -> {}
        RenderType.SELECT -> {}
        RenderType.MULTISELECT -> {}
        RenderType.CHECKBOX -> {}
        RenderType.RADIO -> {}
        RenderType.CODE -> {}
        RenderType.HTML -> {}
        RenderType.MONEY -> {}
        RenderType.CURRENCY -> {}
        RenderType.PERCENT -> {}
        RenderType.BAR -> {}
        RenderType.TREE -> {}
        RenderType.COMPUTED -> {}
        RenderType.AUTO_COMPLETE -> {}
        RenderType.FILE -> {}
    }
    //    Column(modifier = Modifier.fillMaxWidth()) {
//        Text(
//            text = columnMeta.title,
//            style = MaterialTheme.typography.bodySmall,
//            modifier = Modifier.fillMaxWidth(),
//            textAlign = androidx.compose.ui.text.style.TextAlign.Center
//        )
//        Spacer(modifier = Modifier.height(4.dp))
//    }
}