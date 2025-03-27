package com.addzero.web.modules

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping
class SwaggerController {
    @GetMapping("/ui")
    fun ui(): String {
        return "templates/openapi.html"
    }
}