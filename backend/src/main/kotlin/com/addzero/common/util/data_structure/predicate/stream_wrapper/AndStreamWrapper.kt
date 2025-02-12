package com.addzero.common.util.data_structure.predicate.stream_wrapper

import cn.hutool.core.util.StrUtil
import java.util.function.Function
import java.util.function.Predicate
import java.util.stream.Stream

open class AndStreamWrapper<T>(
    stream: Stream<T>,
    predicate: Predicate<T> = Predicate { true }
) : StreamWrapper<T>(stream, predicate) {

    override fun eq(
        condition: Boolean,
        getFun: Function<T, out CharSequence>,
        searchSeq: CharSequence
    ): StreamWrapper<T> {
        if (condition) {
            predicate = predicate.and { t: T -> StrUtil.equals(getFun.apply(t),
                searchSeq) }
        }
        return this
    }

    override fun like(
        condition: Boolean,
        getFun: Function<T, out CharSequence>,
        searchSeq: CharSequence
    ): StreamWrapper<T> {
        if (condition) {
            predicate = predicate.and { t: T ->
                val value = getFun.apply(t)
                value != null && StrUtil.containsIgnoreCase(value, searchSeq)
            }
        }
        return this
    }

    override fun `in`(
        condition: Boolean,
        getFun: Function<T, out CharSequence?>,
        searchSeqs: Collection<*>
    ): StreamWrapper<T> {
        if (condition) {
            predicate = predicate.and { t: T ->
                searchSeqs.contains(getFun.apply(t))
            }
        }
        return this
    }
}