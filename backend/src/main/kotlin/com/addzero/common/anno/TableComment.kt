package com.addzero.common.anno


/**
 * @author zjarlin
 * @since 2023/4/21 16:46
 */
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.ANNOTATION_CLASS)
annotation class TableComment(val value: String = "")