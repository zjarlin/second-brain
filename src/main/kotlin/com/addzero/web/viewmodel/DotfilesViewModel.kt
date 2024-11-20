import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.addzero.web.viewmodel.BizEnvVars

class DotfilesViewModel {
    var dotfilesList by mutableStateOf<List<BizEnvVars>>(getMockData())
        private set

    fun addDotfile(item: BizEnvVars) {
        dotfilesList = dotfilesList + item
    }

    fun updateDotfile(item: BizEnvVars) {
        dotfilesList = dotfilesList.map {
            if (it.id == item.id) item else it
        }
    }

    fun deleteDotfile(id: String) {
        dotfilesList = dotfilesList.filter { it.id != id }
    }

    fun importDotfiles(items: List<BizEnvVars>) {
        dotfilesList = items
    }

    fun exportDotfiles(): List<BizEnvVars> {
        return dotfilesList
    }

    // 模拟数据
    private fun getMockData(): List<BizEnvVars> {
        return listOf(
            BizEnvVars(
                id = "1",
                osType = "Linux",
                osStructure = "x86_64",
                defType = "PATH",
                name = "JAVA_HOME",
                value = "/usr/lib/jvm/java-11-openjdk",
                describtion = "Java 环境变量",
                status = "ENABLED",
                fileUrl = "/etc/profile.d/java.sh",
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis(),
                createdBy = "admin",
                updatedBy = "admin"
            ),
            BizEnvVars(
                id = "2",
                osType = "MacOS",
                osStructure = "arm64",
                defType = "PATH",
                name = "MAVEN_HOME",
                value = "/usr/local/maven",
                describtion = "Maven 环境变量",
                status = "ENABLED",
                fileUrl = "~/.zshrc",
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis(),
                createdBy = "admin",
                updatedBy = "admin"
            ),
            BizEnvVars(
                id = "3",
                osType = "Windows",
                osStructure = "x86_64",
                defType = "USER_VAR",
                name = "PYTHON_HOME",
                value = "C:\\Python39",
                describtion = "Python 环境变量",
                status = "DISABLED",
                fileUrl = "System Environment Variables",
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis(),
                createdBy = "admin",
                updatedBy = "admin"
            )
        )
    }
}