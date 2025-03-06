package com.addzero.web.ui.hooks.table.generic

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import cn.hutool.db.meta.Table
import com.addzero.common.consts.DEFAULT_EXCLUDE_FIELDS
import com.addzero.common.kt_util.FieldMetadata
import com.addzero.common.kt_util.addAllIfAbsentByKey
import com.addzero.common.kt_util.getMetadata
import com.addzero.common.kt_util.toNotBlankStr
import com.addzero.web.ui.hooks.UseHook
import com.addzero.web.ui.hooks.table.common.*
import com.addzero.web.ui.hooks.table.entity.AddColumn
import com.addzero.web.ui.hooks.table.generic.dialog.DeleteDialog
import com.addzero.web.ui.hooks.table.generic.dialog.FormDialog




inline fun<reified E> Table(
    excludeFields: Set<String> = DEFAULT_EXCLUDE_FIELDS,
    columns: List<AddColumn<E>> = emptyList(),
    onValueChange: (UseGenericTable<E>) -> Unit = {},

) {
    val clazz = E::class
    LaunchedEffect(clazz){
        val metadata = clazz.getMetadata()
        val defaultColumns = metadata.fields.filter { !excludeFields.contains(it.property.name) }
            .map<FieldMetadata<E>, AddColumn<E>> { field ->
                val getter = field.property.getter
                AddColumn(title = field.description.toNotBlankStr(), getFun = { getter.call(it) })
            }.filter { it.title.isNotBlank() }
        columns.addAllIfAbsentByKey(defaultColumns, { it.title })

    }


}




class UseGenericTable<E : Any>(
    excludeFields: Set<String> = DEFAULT_EXCLUDE_FIELDS,
    columns: List<AddColumn<E>> = emptyList(),
    onValueChange: (UseGenericTable<E>) -> Unit = {},
) : UseHook<UseGenericTable<E>> {

    init {
        //设置默认值
    }


    companion object {
        // 搜索Hook
        private val useSearch = UseSearch().getState()

        // 表格头部Hook
        // 分页控件Hook
        private val useTablePagination = UseTablePagination().getState()
    }

    // 表格内容Hook
    private val useTableContent = UseTableContent<E>().getState()

    private val useTableHeader = UseTableHeader<E>().getState()

    // 表格操作区Hook
    private val useTableOperations = UseTableOperations<E>().getState()



    override val render: @Composable () -> Unit
        get() = {
            // 搜索栏
            val searchText = useSearch.searchText
            //表头滚动状态
            val scrollState = useTableHeader.scrollState

            //表头
            val columns = useTableHeader.columns

            // 表格内容的列,设置为表头的列
            useTableContent.columns = columns

            //表格内容数据
            val dataList = useTableContent.dataList

            // 表格内容滚动状态 设置为表头滚动状态
            useTableContent.scrollState = scrollState



//            todo 观察 公共包下的hook实现,完成表格的渲染,注意数据区与表头的同步滚动
            Box {
                useSearch.render()

                // 表格头部和内容区域
                val scrollState = rememberScrollState()

                // 表格头部

                HorizontalDivider()

                // 表格内容

                // 右侧操作区域

                // 分页控件

                // 编辑对话框
                if (showEditDialog && selectedItem != null) {
                    FormDialog(
                        item = selectedItem as E,
                        columns = viewModel.columns,
                        onConfirm = { item ->
                            onEdit(item)
                            showEditDialog = false
                        },
                        onDismiss = { showEditDialog = false }
                    )
                }

                // 删除确认对话框

                DeleteDialog(
                    show = showFlag,
                    item = currentItem,
                    onDeleted = TODO()
                )
                if (showDeleteDialog && selectedItem != null) {
                    DeleteDialog(
                        onDeleted = {
                            selectedItem?.let { onDelete(it) }
                            showDeleteDialog = false
                        },
                        onDismiss = { showDeleteDialog = false }
                    )
                }
            }
        }
}

