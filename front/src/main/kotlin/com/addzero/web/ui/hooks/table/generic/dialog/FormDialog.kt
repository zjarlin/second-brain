package com.addzero.web.ui.hooks.table.generic.dialog

import androidx.compose.runtime.Composable
import com.addzero.web.ui.hooks.form.UseDynamicForm
import com.addzero.web.ui.hooks.table.common.UseTableContent

/**
 * 编辑对话框组件
 */
@Composable
fun <E : Any> FormDialog(
    useTableContent: UseTableContent<E>, onFormSubmit: (E) -> Unit, columnCount: Int = 2
) {
    val item = useTableContent.currentSelectItem
    val show = useTableContent.showFormFlag

    if (item == null) {
        return
    }

    val useDynamicForm = UseDynamicForm(useTableContent, columnCount).getState()

    GenericDialog(
        showFlag = show,
        title = "编辑",
        onDismiss = { useTableContent.showFormFlag = false },
        onConfirm = {
            //验证表单
            val validate = useDynamicForm.validate()
            onFormSubmit(item)
        },
        content = {
            useDynamicForm.render()
        }
    )
}