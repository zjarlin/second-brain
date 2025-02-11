package com.addzero.common.util.data_structure.predicate.ex

import com.addzero.common.util.data_structure.predicate.AbstractPredicateBuilder
import java.util.function.Predicate

class PredicateBuilder< T>(public override var predicate: Predicate<T>) :
    AbstractPredicateBuilder<T, PredicateBuilder<T>>() {
    constructor(): this({true})

    companion object {
        fun <T> lambdaQuery(): PredicateBuilder<T> {
            return PredicateBuilder(Predicate { true })
        }
    }
}