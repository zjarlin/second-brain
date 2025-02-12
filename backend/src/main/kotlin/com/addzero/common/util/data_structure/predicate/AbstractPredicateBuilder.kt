package com.addzero.common.util.data_structure.predicate

import cn.hutool.core.collection.CollUtil
import cn.hutool.core.util.ObjUtil
import cn.hutool.core.util.StrUtil
import com.addzero.common.util.data_structure.predicate.ex.PredicateBuilder
import java.util.function.Consumer
import java.util.function.Function
import java.util.function.Predicate

abstract class AbstractPredicateBuilder<T, R : AbstractPredicateBuilder<T, R>> protected constructor() : IPredicateBuilder<T, R> {
    protected open var predicate: Predicate<T> = Predicate { true }
    protected var useOr: Boolean = false
    protected var useNegate: Boolean = false
    protected var useNot: Boolean = false

    private fun applyCondition(newCondition: Predicate<T>, condition: Boolean) {
        if (condition) {
            predicate = when {
                useOr -> {
                    useOr = false
                    predicate.or(newCondition)
                }
                useNot -> {
                    useNot = false
                    predicate.and(newCondition.negate())
                }
                else -> predicate.and(newCondition)
            }
        }
    }

    override fun eq(condition: Boolean, getter: Function<T, *>, value: Any): R {
        applyCondition({ t: T -> ObjUtil.equals(getter.apply(t), value) }, condition)
        return this as R
    }

    override fun eq(getter: Function<T, *>, value: Any): R {
        return eq(true, getter, value)
    }

    override fun like(condition: Boolean, getter: Function<T, String>, pattern: String): R {
        applyCondition({ t: T -> StrUtil.containsIgnoreCase(getter.apply(t), pattern) }, condition)
        return this as R
    }

    override fun like(getter: Function<T, String>, pattern: String): R {
        return like(true, getter, pattern)
    }

    override fun notLike(condition: Boolean, getter: Function<T, String>, pattern: String): R {
        applyCondition({ t: T -> !StrUtil.containsIgnoreCase(getter.apply(t), pattern) }, condition)
        return this as R
    }

    override fun notLike(getter: Function<T, String>, pattern: String): R {
        return notLike(true, getter, pattern)
    }

    override fun `in`(condition: Boolean, getter: Function<T, *>, values: List<*>): R {
        applyCondition({ t: T -> CollUtil.contains(values, getter.apply(t)) }, condition)
        return this as R
    }

    override fun `in`(getter: Function<T, *>, values: List<*>): R {
        return `in`(true, getter, values)
    }

    override fun notIn(condition: Boolean, getter: Function<T, *>, values: List<*>): R {
        applyCondition({ t: T -> !CollUtil.contains(values, getter.apply(t)) }, condition)
        return this as R
    }

    override fun notIn(getter: Function<T, *>, values: List<*>): R {
        return notIn(true, getter, values)
    }

    override fun <V : Comparable<V>> ge(condition: Boolean, getter: Function<T, V>, value: V): R {
        applyCondition(Predicate<T> { t -> ObjUtil.compare(getter.apply(t), value) >= 0 }, condition)
        return this as R
    }

    override fun <V : Comparable<V>> ge(getter: Function<T, V>, value: V): R {
        return ge(true, getter, value)
    }

    override fun <V : Comparable<V>> le(condition: Boolean, getter: Function<T, V>, value: V): R {
        applyCondition(Predicate<T> { t -> ObjUtil.compare(getter.apply(t), value) <= 0 }, condition)
        return this as R
    }

    override fun <V : Comparable<V>> le(getter: Function<T, V>, value: V): R {
        return le(true, getter, value)
    }

    override fun <V : Comparable<V>> gt(condition: Boolean, getter: Function<T, V>, value: V): R {
        applyCondition(Predicate<T> { t -> ObjUtil.compare(getter.apply(t), value) > 0 }, condition)
        return this as R
    }

    override fun <V : Comparable<V>> gt(getter: Function<T, V>, value: V): R {
        return gt(true, getter, value)
    }

    override fun <V : Comparable<V>> lt(condition: Boolean, getter: Function<T, V>, value: V): R {
        applyCondition(Predicate<T> { t -> ObjUtil.compare(getter.apply(t), value) < 0 }, condition)
        return this as R
    }

    override fun <V : Comparable<V>> lt(getter: Function<T, V>, value: V): R {
        return lt(true, getter, value)
    }

    override fun or(): R {
        this.useOr = true
        return this as R
    }


    override fun and(otherPredicate: Predicate<T>): R {
        this.predicate = predicate.and(otherPredicate)
        return this as R
    }

    override fun not(): R {
        this.useNot = true
        return this as R
    }

    override fun negate(): R {
        predicate = predicate.negate()
        return this as R
    }

}