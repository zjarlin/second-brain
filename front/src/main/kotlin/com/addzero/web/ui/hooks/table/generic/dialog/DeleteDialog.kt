package com.addzero.web.ui.hooks.table.generic.dialog

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.addzero.web.ui.hooks.table.table.UseTableContent

/**
 * 删除确认对话框组件
 */
@Composable
fun <E : Any> DeleteDialog(
    useTableContent: UseTableContent<E>, onDelete: (Any) -> Unit
) {
//    val useTableContent = UseTableContent<E>().getState()
    val idFun = useTableContent.getIdFun
    val currentSelectItem = useTableContent.currentSelectItem ?: return

    GenericDialog(
        showFlag = useTableContent.showDeleteFlag,
        title = "确认删除",
        onDismiss = { useTableContent.showDeleteFlag = false },
        onConfirm = { onDelete((idFun(currentSelectItem))) },
        content = {
            Text("确定要删除这条记录吗？")
        })
}