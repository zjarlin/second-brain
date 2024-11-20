import androidx.compose.runtime.*
import com.addzero.web.service.DotfilesService
import com.addzero.web.viewmodel.BizEnvVars
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class DotfilesViewModel(
    private val coroutineScope: CoroutineScope
) {
    private val service = DotfilesService()

    var dotfilesList by mutableStateOf<List<BizEnvVars>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    init {
        loadDotfiles()
    }

    fun loadDotfiles() {
        coroutineScope.launch {
            try {
                isLoading = true
                error = null
                dotfilesList = service.listDotfiles()
            } catch (e: Exception) {
                error = "加载失败: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun addDotfile(item: BizEnvVars) {
        coroutineScope.launch {
            try {
                isLoading = true
                error = null
                service.addDotfile(item)
                loadDotfiles()
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
                service.updateDotfile(item)
                loadDotfiles()
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
                service.deleteDotfile(id)
                loadDotfiles()
            } catch (e: Exception) {
                error = "删除失败: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun importDotfiles(files: List<ByteArray>) {
        coroutineScope.launch {
            try {
                isLoading = true
                error = null
                service.importDotfiles(files)
                loadDotfiles()
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
            service.exportDotfiles()
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

    fun searchDotfiles(
        name: String = "",
        platforms: Set<String> = emptySet(),
        osTypes: Set<String> = emptySet()
    ) {
        coroutineScope.launch {
            try {
                isLoading = true
                error = null
                // 这里应该调用后端的搜索 API
                // 暂时使用本地过滤模拟
                dotfilesList = service.listDotfiles().filter { dotfile ->
                    (name.isEmpty() || dotfile.name.contains(name, ignoreCase = true)) &&
                    (platforms.isEmpty() || platforms.contains(dotfile.osStructure)) &&
                    (osTypes.isEmpty() || osTypes.contains(dotfile.osType))
                }
            } catch (e: Exception) {
                error = "搜索失败: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}