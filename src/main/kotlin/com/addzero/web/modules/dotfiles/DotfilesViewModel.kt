package com.addzero.web.modules.dotfiles

import BizEnvVars
import androidx.compose.runtime.*
import com.addzero.web.model.PageResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File

class DotfilesViewModel(
    private val coroutineScope: CoroutineScope
) {
    private val service = DotfilesService()

    var pageResult by mutableStateOf<PageResult<BizEnvVars>?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    var currentPage by mutableStateOf(1)
        private set

    var pageSize by mutableStateOf(10)
        private set

    init {
        loadData()
    }

    fun loadData(
        name: String = "",
        platforms: Set<String> = emptySet(),
        osTypes: Set<String> = emptySet(),
        page: Int = currentPage,
        size: Int = pageSize
    ) {
        coroutineScope.launch {
            try {
                isLoading = true
                error = null
                pageResult = service.searchDotfiles(
                    name = name,
                    platforms = platforms,
                    osTypes = osTypes,
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
                loadData(page = currentPage + 1)
            }
        }
    }

    fun previousPage() {
        if (currentPage > 0) {
            loadData(page = currentPage - 1)
        }
    }

    fun addDotfile(item: BizEnvVars) {
        coroutineScope.launch {
            try {
                isLoading = true
                error = null
                service.save(item)
                loadData()
            } catch (e: Exception) {
                error = "添加失败: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun updateDotfile(item: BizEnvVars) {
        coroutineScope.launch {
            try {
                isLoading = true
                error = null
                service.update(item)
                loadData()
            } catch (e: Exception) {
                error = "更新失败: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun deleteDotfile(id: String) {
        coroutineScope.launch {
            try {
                isLoading = true
                error = null
                service.delete(id)
                loadData()
            } catch (e: Exception) {
                error = "删除失败: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun importDotfiles(files: List<File>) {
        coroutineScope.launch {
            try {
                isLoading = true
                error = null
                service.import(files)
                loadData()
            } catch (e: Exception) {
                error = "导入失败: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    suspend fun exportDotfiles(): ByteArray? {
        return try {
            isLoading = true
            error = null
            service.export()
        } catch (e: Exception) {
            error = "导出失败: ${e.message}"
            null
        } finally {
            isLoading = false
        }
    }

    suspend fun generateConfig(): ByteArray? {
        return try {
            isLoading = true
            error = null
            service.generateConfig()
        } catch (e: Exception) {
            error = "生成配置失败: ${e.message}"
            null
        } finally {
            isLoading = false
        }
    }
}