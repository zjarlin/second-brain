package com.addzero.web.model.enums

enum class OsType(val label: String) {
    ALL("不限"),
    MACOS("MacOS"),
    LINUX("Linux"),
    WINDOWS("Windows");

    companion object {
        fun fromLabel(label: String): OsType = values().find { it.label == label } ?: ALL
    }
} 