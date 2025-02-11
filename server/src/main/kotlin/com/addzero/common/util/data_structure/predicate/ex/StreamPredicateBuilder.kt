package com.addzero.common.util.data_structure.predicate.ex

import com.addzero.common.util.data_structure.predicate.AbstractPredicateBuilder
import java.util.stream.Collectors
import java.util.stream.Stream

class StreamPredicateBuilder<T> private constructor(private val stream: Stream<T>) :
    AbstractPredicateBuilder<T, StreamPredicateBuilder<T>>() {
    fun list(): List<T> {
        return stream.filter(predicate).collect(Collectors.toList<T>())
    }


    companion object {
        fun <T> lambdaQuery(stream: Stream<T>): StreamPredicateBuilder<T> {
            return StreamPredicateBuilder(stream)
        }

        fun <E : Collection<T>?, T> lambdaQuery(collection: E): StreamPredicateBuilder<T> {
            return StreamPredicateBuilder(collection!!.stream())
        }
    }
}