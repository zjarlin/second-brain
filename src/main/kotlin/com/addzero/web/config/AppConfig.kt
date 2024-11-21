package com.addzero.web.config

object AppConfig {
    const val ENABLE_LOGIN = false
    const val USE_MOCK_DATA = true
    
    object Theme {
        const val MICA_OPACITY = 0.8f
        val backgroundGradient = listOf(0xFFF6F6F6, 0xFFFFFFFF)
        val primaryGradient = listOf(0xFF1976D2, 0xFF2196F3)
    }
} 