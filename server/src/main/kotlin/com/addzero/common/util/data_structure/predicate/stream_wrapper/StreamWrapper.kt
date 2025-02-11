package com.addzero.common.util.data_structure.predicate.stream_wrapper

import java.util.function.Function
import java.util.function.Predicate
import java.util.stream.Collectors
import java.util.stream.Stream

abstract class StreamWrapper<T>(

    protected var stream: Stream<T>,
    protected var predicate: Predicate<T> = Predicate { true }
) {

    abstract fun eq(
        condition: Boolean,
        getFun: Function<T, out CharSequence>,
        searchSeq: CharSequence
    ): StreamWrapper<T>

    abstract fun like(
        condition: Boolean,
        getFun: Function<T, out CharSequence>,
        searchSeq: CharSequence
    ): StreamWrapper<T>

    abstract fun `in`(
        condition: Boolean,
        getFun: Function<T, out CharSequence>,
        searchSeqs: Collection<*>
    ): StreamWrapper<T>

    fun list(): List<T> {
        return stream.filter(predicate).collect(Collectors.toList())
    }

    fun one(): T? {
        return stream.filter(predicate).findAny().orElse(null)
    }

    fun or(): StreamWrapper<T> {
        return OrStreamWrapper(stream, predicate)
    }

    fun and(): StreamWrapper<T> {
        return AndStreamWrapper(stream, predicate)
    }

    fun negate(): StreamWrapper<T> {
        predicate = predicate.negate()
        return this
    }

    fun not(): StreamWrapper<T> {
        return NotStreamWrapper(stream, predicate)
    }

    companion object {
        fun <T> lambdaquery(collection: Collection<T>): StreamWrapper<T> {
            return lambdaquery(collection.stream())
        }

        fun <T> lambdaquery(stream: Stream<T>): StreamWrapper<T> {
            return AndStreamWrapper(stream)
        }
    }
}