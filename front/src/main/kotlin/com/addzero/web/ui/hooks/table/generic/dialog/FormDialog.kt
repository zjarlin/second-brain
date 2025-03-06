package com.addzero.web.ui.hooks.table.generic.dialog

import androidx.compose.runtime.Composable
import com.addzero.web.ui.hooks.form.DynamicFormComponent
import com.addzero.web.ui.hooks.table.common.UseTableContent

/**
 * 编辑对话框组件
 */
@Composable
fun <E : Any> FormDialog(
    useTableContent: UseTableContent<E>, onFormSubmit: (E) -> Unit
) {
    val item = useTableContent.currentSelectItem
    val show= useTableContent.showFormFlag

    if (item==null) {
        return
    }
    GenericDialog(
        show = show,
        title = "编辑",
        onDismiss ={useTableContent.showFormFlag=false} ,
        onConfirm = { onFormSubmit(item) },
        content = {
            DynamicFormComponent(
                columns = useTableContent.columns,
                data = item,
                onDataChange = TODO(),
            )
        }
    )
}