package com.addzero.web.modules.dotfiles

import com.addzero.web.base.BaseServiceImpl
import com.addzero.web.model.PageResult
import io.ktor.client.call.*
import io.ktor.client.request.*

class DotfilesService : BaseServiceImpl<BizEnvVars>() {
    override val restPath: String
        get() = "/dotfiles"

    suspend fun generateConfig(): ByteArray {
        return client.get("$thisUrl/generate-config").body()
    }

     suspend fun searchDotfiles(
        name: String = "",
        platforms: Set<String> = emptySet(),
        osTypes: Set<String> = emptySet(),
        page: Int = 0,
        size: Int = 10,
    ): PageResult<BizEnvVars> {
        val body = client.get("$thisUrl/search") {
            url {
                parameters.append("page", page.toString())
                parameters.append("size", size.toString())
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