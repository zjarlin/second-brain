package com.addzero.web.components

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class SwaagerController {
    @GetMapping("/ui")
    fun ui(): String {
        return "openapi.html"

    }

}
