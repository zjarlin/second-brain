package com.addzero.web.modules.software

import androidx.compose.runtime.*
import com.addzero.web.model.PageResult
import com.addzero.web.model.enums.OsType
import com.addzero.web.model.enums.PlatformType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class SoftwareViewModel(
    private val coroutineScope: CoroutineScope
) {
    private val service = SoftwareService()

    var pageResult by mutableStateOf<PageResult<Software>?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    var currentPage by mutableStateOf(0)
        private set

    var pageSize by mutableStateOf(10)
        private set

    init {
        searchSoftware()
    }

    fun searchSoftware(
        keyword: String = "",
        platform: PlatformType = PlatformType.ALL,
        osTypes: Set<OsType> = emptySet(),
        useAI: Boolean = false,
        page: Int = currentPage,
        size: Int = pageSize
    ) {
        coroutineScope.launch {
            try {
                isLoading = true
                error = null
                pageResult = service.searchSoftware(
                    keyword = keyword,
                    platform = if (platform == PlatformType.ALL) null else platform,
                    osTypes = osTypes.filter { it != OsType.ALL }.takeIf { it.isNotEmpty() },
                    useAI = useAI,
                    page = page,
                    size = size
                )
                currentPage = page
            } catch (e: Exception) {
                error = "搜索失败: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun nextPage() {
        pageResult?.let {
            if (!it.isLast) {
                searchSoftware(page = currentPage + 1)
            }
        }
    }

    fun previousPage() {
        if (currentPage > 0) {
            searchSoftware(page = currentPage - 1)
        }
    }

    fun downloadSoftware(software: Software): ByteArray? {
        var result: ByteArray? = null
        coroutineScope.launch {
            try {
                isLoading = true
                error = null
                result = service.downloadSoftware(software.id)
            } catch (e: Exception) {
                error = "下载失败: ${e.message}"
            } finally {
                isLoading = false
            }
        }
        return result
    }
}