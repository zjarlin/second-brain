package com.addzero.web.ui.hooks.table.generic.dialog

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.addzero.web.ui.hooks.table.common.UseTableContent

/**
 * 删除确认对话框组件
 */
@Composable
fun <E : Any> DeleteDialog(
    useTableContent: UseTableContent<E>, onDeleted: (Any) -> Unit
) {
//    val useTableContent = UseTableContent<E>().getState()
    val idFun = useTableContent.getIdFun
    val currentSelectItem = useTableContent.currentSelectItem ?: return

    GenericDialog(
        show = useTableContent.showDeleteFlag,
        title = "确认删除",
        onDismiss = { useTableContent.showDeleteFlag = false },
        onConfirm = { onDeleted((idFun(currentSelectItem))) },
        content = {
            Text("确定要删除这条记录吗？")
        })
}