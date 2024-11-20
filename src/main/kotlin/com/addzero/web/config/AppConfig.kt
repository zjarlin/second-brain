package com.addzero.web.config

object AppConfig {
    const val ENABLE_LOGIN = false
    
    object Theme {
        // 云母渐变色值
        val primaryGradient = listOf(
            0xFF9EC5FE,  // 浅蓝色
            0xFF7EB6FF,  // 中蓝色
            0xFF5E9EFF   // 深蓝色
        )
        
        val backgroundGradient = listOf(
            0xFFF0F7FF,  // 浅色背景
            0xFFE6F0FF   // 深色背景
        )
        
        const val MICA_OPACITY = 0.85f
    }
} 