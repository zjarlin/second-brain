package com.addzero

/**
 * 用于生成DSL风格构建器的注解
 * 在实体类上添加此注解，将自动生成DSL风格的构建器函数
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Dsl