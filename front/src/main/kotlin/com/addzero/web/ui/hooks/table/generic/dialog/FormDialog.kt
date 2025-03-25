package com.addzero.web.ui.hooks.table.generic.dialog

import androidx.compose.runtime.Composable
import com.addzero.web.ui.hooks.form.UseDynamicForm
import com.addzero.web.ui.hooks.table.table.UseTableContent

/**
 * 编辑对话框组件
 */
@Composable
fun <E : Any> FormDialog(
    useTableContent: UseTableContent<E>, onFormSubmit: (E) -> Unit, columnCount: Int = 2
) {
    useTableContent.currentSelectItem ?: return

    val useDynamicForm = UseDynamicForm<E>(columnCount)
        .apply {
            columns = useTableContent.columns
            currentSelectItem = useTableContent.currentSelectItem
        }.getState()

    GenericDialog(
        showFlag = useTableContent.showFormFlag,
        title = "编辑",
        onDismiss = { useTableContent.showFormFlag = false },
        onConfirm = {
            //验证表单
            val validate = useDynamicForm.validate()
            if (validate) {
                onFormSubmit(useTableContent.currentSelectItem!!)
            }
            useTableContent.showFormFlag = false
        },
        content = {
            useDynamicForm.render()
        })
}