package com.addzero.web.ui.hooks.table.generic

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.addzero.common.consts.DEFAULT_EXCLUDE_FIELDS
import com.addzero.common.kt_util.getMetadata
import com.addzero.common.kt_util.ignoreCaseNotIn
import com.addzero.common.kt_util.toNotBlankStr
import com.addzero.web.ui.hooks.table.entity.AddColumn
import kotlin.reflect.KClass

/**
 * 通用表格ViewModel规范
 */
class GenericTableViewModel<E : Any> {
    var searchText by mutableStateOf("")

    var dataList by mutableStateOf<List<E>>(emptyList())
    var columns by mutableStateOf<List<AddColumn<E>>>(emptyList())

    var pageNo by mutableStateOf(1)
    var pageSize by mutableStateOf(10)
    var totalPages by mutableStateOf(0)


    fun getDefaultColumns(clazz: KClass<E>, excludeFields: MutableSet<String>): List<AddColumn<E>> {

        DEFAULT_EXCLUDE_FIELDS.forEach {
            excludeFields.add(it)
        }
        val metadata = clazz.getMetadata()

        val filter = metadata.fields

            .filter {
                val ignoreCaseNotIn = it.name ignoreCaseNotIn excludeFields
                ignoreCaseNotIn
            }
            .map { field ->
                val getter = field.property.getter

                AddColumn<E>(title = field.description.toNotBlankStr(), getFun = { getter.call(it) })
            }

            .filter { it.title.isNotBlank() }


        return filter
    }
}

