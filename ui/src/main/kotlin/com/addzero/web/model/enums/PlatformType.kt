package com.addzero.web.model.enums

enum class PlatformType(val label: String) {
    ALL("不限"),
    ARM64("arm64"),
    X86_64("x86_64");

    companion object {
        fun fromLabel(label: String): PlatformType = values().find { it.label == label } ?: ALL
    }
} 