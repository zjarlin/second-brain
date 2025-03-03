package com.addzero.common.consts

enum class EnumOrm(val code: String, val des: String) {
    DELETED("1", "deleted")
}

val DEFAULT_EXCLUDE_FIELDS = setOf(
    "createTime", "createdBy", "updateTime", "updatedBy", "id", "sid",
    "orderid","createuse","delflag"
)
