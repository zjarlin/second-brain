package com.addzero.web.modules.sys.user

import java.time.LocalDateTime

data class User(
    val id: Long,
    val username: String,
    val email: String,
    val role: String,
    val status: Boolean,
    val phone: String,
    val address: String,
    val department: String,
    val position: String,
    val salary: Double,
    val hireDate: LocalDateTime,
    val lastLoginTime: LocalDateTime,
    val createdTime: LocalDateTime,
    val updatedTime: LocalDateTime,
    val remark: String
)