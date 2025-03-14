package com.addzero.web.ui.lowcode.core

import androidx.compose.runtime.Composable
import com.addzero.web.ui.lowcode.metadata.FieldMetadata

/**
 * 表单项渲染函数类型
 * 用于渲染表单项
 */
typealias RenderField<E> = @Composable (
    field: FieldMetadata<E>,
    value: Any?,
    onChange: (Any?) -> Unit,
    error: String?
) -> Unit 