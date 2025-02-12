package com.addzero.common.util.data_structure.predicate

import java.util.function.Function
import java.util.function.Predicate

interface IPredicateBuilder<T, R : IPredicateBuilder<T, R>> {
    fun eq(condition: Boolean, getter: Function<T, *>, value: Any): R

    fun eq(getter: Function<T, *>, value: Any): R

    fun like(condition: Boolean, getter: Function<T, String>, pattern: String): R

    fun like(getter: Function<T, String>, pattern: String): R

    fun notLike(condition: Boolean, getter: Function<T, String>, pattern: String): R

    fun notLike(getter: Function<T, String>, pattern: String): R

    fun `in`(condition: Boolean, getter: Function<T, *>, values: List<*>): R

    fun `in`(getter: Function<T, *>, values: List<*>): R

    fun notIn(condition: Boolean, getter: Function<T, *>, values: List<*>): R

    fun notIn(getter: Function<T, *>, values: List<*>): R

    fun <V : Comparable<V>> ge(condition: Boolean, getter: Function<T, V>, value: V): R

    fun <V : Comparable<V>> ge(getter: Function<T, V>, value: V): R

    fun <V : Comparable<V>> le(condition: Boolean, getter: Function<T, V>, value: V): R

    fun <V : Comparable<V>> le(getter: Function<T, V>, value: V): R

    fun or(): R

    fun and(otherPredicate: Predicate<T>): R

    fun <V : Comparable<V>> gt(condition: Boolean, getter: Function<T, V>, value: V): R

    fun <V : Comparable<V>> gt(getter: Function<T, V>, value: V): R

    fun <V : Comparable<V>> lt(condition: Boolean, getter: Function<T, V>, value: V): R

    fun <V : Comparable<V>> lt(getter: Function<T, V>, value: V): R

    fun not(): R

    fun negate(): R
}