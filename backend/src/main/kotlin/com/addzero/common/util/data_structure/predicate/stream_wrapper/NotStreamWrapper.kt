package com.addzero.common.util.data_structure.predicate.stream_wrapper

import cn.hutool.core.util.StrUtil
import java.util.function.Function
import java.util.function.Predicate
import java.util.stream.Stream

open class NotStreamWrapper<T>(
    stream: Stream<T>,
    predicate: Predicate<T> = Predicate { true },
) : StreamWrapper<T>(stream, predicate) {

    override fun eq(
        condition: Boolean,
        getFun: Function<T, out CharSequence>,
        searchSeq: CharSequence,
    ): StreamWrapper<T> {
        if (condition) {
            val predicate = Predicate<T> { StrUtil.equals(getFun.apply(it), searchSeq) }
            this.predicate = predicate.and(predicate.negate())
        }
        return this
    }

    override fun like(
        condition: Boolean,
        getFun: Function<T, out CharSequence>,
        searchSeq: CharSequence,
    ): StreamWrapper<T> {
        if (condition) {
            val predicate = Predicate<T> { StrUtil.containsIgnoreCase(getFun.apply(it), searchSeq) }
            this.predicate = predicate.and(predicate.negate())
        }
        return this
    }

    override fun `in`(
        condition: Boolean,
        getFun: Function<T, out CharSequence?>,
        searchSeqs: Collection<*>,
    ): StreamWrapper<T> {
        if (condition) {
            val predicate = Predicate<T> {searchSeqs.contains(getFun.apply(it))}
            this.predicate = predicate.and(predicate.negate())
        }
        return this
    }
}