package com.addzero.common.util.data_structure.tree

import java.util.function.Function

/**
 * @author zjarlin
 * @since 2023/4/8 15:42
 */
open class TreeConfig<T> {
    private val nodeName: String? = null

    private val groupFunction: Function<T, *>? = null

    private val sortFunction: Comparator<in T>? = null
}