package com.addzero.web.modules.sys.low.metadata

import com.addzero.web.modules.sys.low.metadata.impl.PostgresMetadataService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/testmeta")
class MetaDataController(private val postgresMetadataService: PostgresMetadataService) {
    @GetMapping("test")
    fun test(): Unit {
        val dbMetaInfos = postgresMetadataService.getDbMetaInfos()
        println(dbMetaInfos)

    }

}