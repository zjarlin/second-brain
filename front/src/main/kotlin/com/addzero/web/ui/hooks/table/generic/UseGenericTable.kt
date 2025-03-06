package com.addzero.web.ui.hooks.table.generic

import androidx.compose.runtime.Composable
import com.addzero.common.consts.DEFAULT_EXCLUDE_FIELDS
import com.addzero.common.kt_util.getMetadata
import com.addzero.common.kt_util.ignoreCaseNotIn
import com.addzero.common.kt_util.toNotBlankStr
import com.addzero.web.ui.hooks.UseHook
import com.addzero.web.ui.hooks.table.common.UseSearch
import com.addzero.web.ui.hooks.table.common.UseTableContent
import com.addzero.web.ui.hooks.table.common.UseTablePagination
import com.addzero.web.ui.hooks.table.entity.AddColumn
import com.addzero.web.ui.hooks.table.generic.dialog.DeleteDialog
import com.addzero.web.ui.hooks.table.generic.dialog.FormDialog
import org.babyfish.jimmer.Page
import kotlin.reflect.KClass


fun <E : Any> getDefaultColumns(
    clazz: KClass<E>, excludeFields: MutableSet<String>
): List<AddColumn<E>> {

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
            val addColumn = AddColumn<E>(title = field.description.toNotBlankStr(), getFun = { getter.call(it) })
            addColumn.fieldName = field.name
            if (addColumn.title.isBlank()) {
                addColumn.title = field.name
            }
            addColumn
        }

    return filter
}

class UseTable<E : Any>(
    val clazz: KClass<E>,
    val excludeFields: MutableSet<String>,
    val columns: List<AddColumn<E>> = emptyList(),
    val getIdFun: (E) -> Any = { it.hashCode() },
    val onLoadData: (UseTable<E>) -> Page<E>? = { null },
    val onSave: (E) -> Unit,
    val onDelete: (Any) -> Unit,
) : UseHook<UseTable<E>> {


    val useSearch = UseSearch(
        onSearch = {
            val pageResult = onLoadData(this)!!
            useTableContent.dataList = pageResult.rows
            useTablePagination.totalPages = pageResult.totalPageCount.toInt()
        })

    private val useTableContent = UseTableContent<E>().apply {
        this.columns += columns
    }

    val useTablePagination = UseTablePagination()


//    private val viewModel = remember {
//        GenericTableViewModel<E>()
//    }

    init {
//        viewModel.searchText= useSearch.searchText
//        viewModel.pageNo= useTablePagination.pageNo
//        viewModel.pageSize = useTablePagination.pageSize

        val defaultColumns = getDefaultColumns(clazz, excludeFields)
        useTableContent.columns += defaultColumns
    }


    override val render:

            @Composable () -> Unit
        get() = {
            val useSearch = useSearch.getState()
            val useTableContent = useTableContent.getState()
            val useTablePagination = useTablePagination.getState()

            useSearch.render()
            useTableContent.render()

            FormDialog(useTableContent, onFormSubmit = onSave)

            DeleteDialog(useTableContent, onDelete = onDelete)


            useTablePagination.render()
        }
}

@Composable
inline fun <reified E : Any> GenericTable(
    excludeFields: MutableSet<String> = DEFAULT_EXCLUDE_FIELDS,
    columns: List<AddColumn<E>> = emptyList(),
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



