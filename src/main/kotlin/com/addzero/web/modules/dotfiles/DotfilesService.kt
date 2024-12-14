package com.addzero.web.modules.dotfiles

import com.addzero.web.base.BaseServiceImpl
import com.addzero.web.model.PageResult
import io.ktor.client.call.*
import io.ktor.client.request.*

private const val PAGE = "pageNum"

private const val SIZE = "pageSize"

class DotfilesService : BaseServiceImpl<BizEnvVars>() {
    override val restPath: String
        get() = "/dotfiles"

    suspend fun generateConfig(): ByteArray {
        val body = client.get("$thisUrl/generate-config").body<ByteArray>()
        return body
    }

    suspend fun searchDotfiles(
        name: String = "",
        platforms: Set<String> = emptySet(),
        osTypes: Set<String> = emptySet(),
        page: Int = 1,
        size: Int = 10,
    ): PageResult<BizEnvVars> {
        val body = client.get("$thisUrl/page") {
            url {
//                val mapOf = mapOf(
//                    PAGE to page, SIZE to size, "name" to name, "platforms" to platforms, "osTypes" to osTypes
//                )
//                mapOf.forEach(::parameter)

                parameters.append(PAGE, page.toString())
                parameters.append(SIZE, size.toString())
                if (name.isNotEmpty()) {
                    parameters.append("name", name)
                }
                platforms.forEach { platform ->
                    parameters.append("platforms", platform)
                }
                osTypes.forEach { osType ->
                    parameters.append("osTypes", osType)
                }
            }
        }.body<PageResult<BizEnvVars>>()
        return body
    }

}