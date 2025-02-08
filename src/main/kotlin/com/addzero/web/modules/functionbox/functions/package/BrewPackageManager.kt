package com.addzero.web.modules.functionbox.functions.`package`

import com.addzero.web.util.shell.CommandExecutor
import kotlinx.coroutines.*

class BrewPackageManager : PackageManager {
    override suspend fun checkPackageStatus(packageName: String): PackageStatus {
        return withContext(Dispatchers.IO) {
            val status = PackageStatus(packageName)
            try {
                withTimeout(5000) {
                    val execute = CommandExecutor.execute("brew list|grep $packageName")
                    val notBlank = execute.isNotBlank()
                    status.status = if (notBlank) {
                        PackageStatus.Status.INSTALLED
                    } else {
                        PackageStatus.Status.NOT_INSTALLED
                    }
                }
            } catch (e: Exception) {
                status.status = PackageStatus.Status.ERROR
                status.error = when (e) {
                    is TimeoutCancellationException -> "检查超时"
                    else -> e.message ?: "检查失败"
                }
            }
            status
        }
    }

    override suspend fun installPackage(packageStatus: PackageStatus, scope: CoroutineScope) {
        packageStatus.status = PackageStatus.Status.INSTALLING
        withContext(Dispatchers.IO) {
            try {
                val execute = CommandExecutor.execute("brew install ${packageStatus.name}")

                scope.launch(Dispatchers.Main) {
                    val contains = execute.contains("🍺")
                    if (contains) {
                        packageStatus.status = PackageStatus.Status.INSTALLED
                    } else {
                        packageStatus.status = PackageStatus.Status.ERROR
                        packageStatus.error = "安装失败"
                    }
                }
            } catch (e: Exception) {
                scope.launch(Dispatchers.Main) {
                    packageStatus.status = PackageStatus.Status.ERROR
                    packageStatus.error = e.message ?: "未知错误"
                }
            }
        }
    }

    override suspend fun uninstallPackage(packageStatus: PackageStatus, scope: CoroutineScope) {
        packageStatus.status = PackageStatus.Status.UNINSTALLING
        withContext(Dispatchers.IO) {
            try {
                val execute = CommandExecutor.execute("brew uninstall ${packageStatus.name}")
                scope.launch(Dispatchers.Main) {
                    if (execute.contains("Uninstalling")) {
                        packageStatus.status = PackageStatus.Status.NOT_INSTALLED
                    } else {
                        packageStatus.status = PackageStatus.Status.ERROR
                        packageStatus.error = "卸载失败"
                    }
                }
            } catch (e: Exception) {
                scope.launch(Dispatchers.Main) {
                    packageStatus.status = PackageStatus.Status.ERROR
                    packageStatus.error = e.message ?: "未知错误"
                }
            }
        }
    }

    override fun isAvailable(): Boolean {
        return try {
            val execute = CommandExecutor.execute("which brew")
            execute.isNotBlank()
        } catch (e: Exception) {
            false
        }
    }
}