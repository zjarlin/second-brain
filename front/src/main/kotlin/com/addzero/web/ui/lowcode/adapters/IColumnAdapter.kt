package com.addzero.web.ui.lowcode.adapters

import com.addzero.web.ui.hooks.table.entity.IColumn
import com.addzero.web.ui.lowcode.metadata.FieldMetadata

/**
 * 将IColumn转换为FieldMetadata的适配器
 */
fun <E : Any> IColumn<E>.toFieldMetadata(): FieldMetadata<E> {
    return FieldMetadata(
        name = this.fieldName,
        title = this.title,
        renderType = this.renderType,
        getValue = this.getFun,
        setValue = { entity, value -> this.setFun(entity, this, value) },
        required = this.required,
        validator = { value -> 
            // 这里需要一个适配，因为IColumn的validator接受E类型，而FieldMetadata的validator接受Any?类型
            if (value == null) false else this.validator(value as? E)
        },
        errorMessage = this.errorMessage,
        placeholder = this.placeholder,
        enabled = this.enabled,
        visible = true,
        customRender = { value -> 
            // 这里也需要适配，因为IColumn的customRender接受E类型，而FieldMetadata的customRender接受Any?类型
            if (value != null) {
                this.customRender(value as E)
            }
        },
        options = this.options,
        showInSearch = this.showInSearch,
        showInTable = true,
        showInForm = true
    )
}

/**
 * 将IColumn列表转换为FieldMetadata列表
 */
fun <E : Any> List<IColumn<E>>.toFieldMetadataList(): List<FieldMetadata<E>> {
    return this.map { it.toFieldMetadata() }
} 