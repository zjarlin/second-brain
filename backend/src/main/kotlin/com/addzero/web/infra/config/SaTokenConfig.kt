//package com.addzero.web.infra.config
//
//import cn.dev33.satoken.interceptor.SaInterceptor
//import org.springframework.boot.autoconfigure.security.SecurityProperties
//import org.springframework.boot.context.properties.EnableConfigurationProperties
//import org.springframework.context.annotation.Configuration
//import org.springframework.core.env.Environment
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
//
//@Configuration
//@EnableConfigurationProperties(SecurityProperties::class)
//open class SaTokenConfig(
//    private val environment: Environment, // 注入 Spring 的 Environment 对象
//) : WebMvcConfigurer {
//
//    override fun addInterceptors(registry: InterceptorRegistry) {
//        // 获取当前激活的 profile
//        val activeProfile = environment.activeProfiles.firstOrNull() ?: "default"
//
//        // 配置免登录路径
//        val excludePathPatterns = listOf(
//            "/login", "/logout", "/register", "/error", "/static/**", "/openapi-ui.html", "favicon.ico", "/v2/api-docs", "/swagger-resources/**", "/swagger-ui.html", "/webjars/**"
//        )
//
//        if (activeProfile == "dev") {
//            // 开发环境，直接放行所有路径，免登录
//            registry.addInterceptor(SaInterceptor()).addPathPatterns("/**").excludePathPatterns("/**") // 放行所有路径
//        } else {
//            // 非开发环境，添加正常的拦截规则
//            registry.addInterceptor(SaInterceptor()).addPathPatterns("/**").excludePathPatterns(excludePathPatterns)
//        }
//    }
//}