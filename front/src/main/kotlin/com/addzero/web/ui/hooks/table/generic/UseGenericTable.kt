package com.addzero.web.ui.hooks.table.generic

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.addzero.common.consts.DEFAULT_EXCLUDE_FIELDS
import com.addzero.common.kt_util.*
import com.addzero.web.ui.hooks.UseHook
import com.addzero.web.ui.hooks.table.common.UseSearch
import com.addzero.web.ui.hooks.table.common.UseTableContent
import com.addzero.web.ui.hooks.table.common.UseTablePagination
import com.addzero.web.ui.hooks.table.entity.IColumn
import com.addzero.web.ui.hooks.table.entity.JimmerColumn
import com.addzero.web.ui.hooks.table.entity.RenderType
import com.addzero.web.ui.hooks.table.generic.dialog.DeleteDialog
import com.addzero.web.ui.hooks.table.generic.dialog.FormDialog
import org.babyfish.jimmer.Page
import kotlin.collections.filter
import kotlin.reflect.KClass
import kotlin.text.isBlank


fun <E : Any> getDefaultColumns(
    clazz: KClass<E>, excludeFields: MutableSet<String>
): List<IColumn<E>> {

    DEFAULT_EXCLUDE_FIELDS.forEach {
        excludeFields.add(it)
    }

    val metadata = clazz.getMetadata()
    val filter = metadata.fields

        .filter {
            val ignoreCaseNotIn = it.name ignoreCaseNotIn excludeFields
            ignoreCaseNotIn
        }.map { field ->
            val getter = field.property.getter
            val addColumn = JimmerColumn<E>(
                title = field.description.toNotBlankStr(),
                getFun = { getter.call(it) },
            )
            addColumn.fieldName = field.name
            if (addColumn.title.isBlank()) {
                addColumn.title = field.name
            }
            addColumn.currentField = field
            addColumn
        }
    return filter
}

class UseTable<E : Any>(
    val clazz: KClass<E>,
    val excludeFields: MutableSet<String>,
    val columns: List<IColumn<E>> = emptyList(),
    val getIdFun: (E) -> Any = { it.hashCode() },
    val onLoadData: (UseTable<E>) -> Page<E>? = { null },
    val onSave: (E) -> Unit,
    val onDelete: (Any) -> Unit,
) : UseHook<UseTable<E>> {

    val useSearch = UseSearch(
        onSearch = {
            useTablePagination.pageNo = 1
            useTablePagination.pageSize = 10
            refreshData()
        })


    fun refreshData() {
        val pageResult = onLoadData(this)!!
        useTableContent.dataList = pageResult.rows
        useTablePagination.totalPages = pageResult.totalPageCount.toInt()
    }

    private val useTableContent = UseTableContent<E>()

    val useTablePagination = UseTablePagination()


//    private val viewModel = remember {
//        GenericTableViewModel<E>()
//    }

    private fun <T> withRefresh(action: () -> T): T {
        return action().also { refreshData() }
    }

    init {
        val defaultColumns = getDefaultColumns(clazz, excludeFields)
        val existingTitles = mutableSetOf<String>()

        // 使用sequence优化性能
        useTableContent.columns = sequence {
            // 添加默认列
            defaultColumns.forEach { column ->
                existingTitles.add(column.title)
                yield(column)
            }

            // 处理自定义列
            if (columns.isNotEmpty()) {
                val customColumnMap = columns.associateBy { it.title }

                // 更新已存在的列的自定义渲染
                defaultColumns.filter { it.title in customColumnMap }
                    .forEach { column ->
                        column.customRender = customColumnMap[column.title]!!.customRender
                        //自定义列
                        column.renderType= RenderType.CUSTOM
                    }

                // 添加新的自定义列
                columns.filterNot { it.title in existingTitles }
                    .forEach { column ->
                        yield(column)
                    }
            }
        }.toList()
    }


    override val render: @Composable () -> Unit
        get() = {
            val useSearch = useSearch.getState()
            val useTableContent = useTableContent.getState()
            val useTablePagination = useTablePagination.getState()

            Column(modifier = Modifier.fillMaxSize()) {
                useSearch.render()

                Box(modifier = Modifier.weight(1f)) {
                    Column {
                        useTableContent.render()
                        FormDialog(useTableContent, onFormSubmit = { item ->
                            withRefresh { onSave(item) }
                        })
                        DeleteDialog(useTableContent, onDelete = { id ->
                            withRefresh { onDelete(id) }
                        })
                    }
                }

                // 优化LaunchedEffect依赖项
                LaunchedEffect(useTablePagination.pageNo, useTablePagination.pageSize) {
                    refreshData()
                }

                useTablePagination.render()
            }
        }
}

@Composable
inline fun <reified E : Any> GenericTable(
    excludeFields: MutableSet<String> = DEFAULT_EXCLUDE_FIELDS,
    columns: List<IColumn<E>> = emptyList(),
    noinline getIdFun: (E) -> Any = { it.hashCode() },
    noinline onSave: (E) -> Unit,
    noinline onDelete: (Any) -> Unit,
    noinline onLoadData: (UseTable<E>) -> Page<E>? = { null },
) {

    val useTable = UseTable(
        clazz = E::class,
        excludeFields = excludeFields,
        columns = columns,
        getIdFun = getIdFun,
        onSave = onSave,
        onDelete = onDelete,
        onLoadData = onLoadData
    ).getState()

    useTable.render()

}



