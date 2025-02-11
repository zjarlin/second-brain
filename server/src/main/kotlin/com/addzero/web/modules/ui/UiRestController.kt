package com.addzero.web.modules.ui

import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin
@RequestMapping("/api")
class UiRestController {
    @PostMapping("/handleRouter")
    fun odjasdoi(@RequestBody jsonObject: RouterMeta): Unit {
        println(jsonObject)
        println()

    }

}