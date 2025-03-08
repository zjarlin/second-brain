package com.addzero.web.ui.hooks.table.generic.dialog

import androidx.compose.runtime.Composable
import com.addzero.web.ui.hooks.form.UseDynamicForm
import com.addzero.web.ui.hooks.table.common.UseTableContent
import kotlinx.serialization.json.JsonNull.content

/**
 * 编辑对话框组件
 */
@Composable
fun <E : Any> FormDialog(
    useTableContent: UseTableContent<E>, onFormSubmit: (E) -> Unit, columnCount: Int = 2
) {
    val item = useTableContent.currentSelectItem ?: return

    val useDynamicForm = UseDynamicForm(useTableContent, columnCount).getState()

    GenericDialog(
        showFlag = useTableContent.showFormFlag,
        title = "编辑",
        onDismiss = { useTableContent.showFormFlag = false },
        onConfirm = {
            //验证表单
            val validate = useDynamicForm.validate()
            if (validate) {
                // 使用useDynamicForm中的currentFormItem而不是原始的item
                useDynamicForm.currentFormItem?.let { updatedItem ->
                    onFormSubmit(updatedItem)
                }

            }
            useTableContent.showFormFlag = false
        },
        content = {
            useDynamicForm.render()
        })
}