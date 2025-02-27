package com.addzero.web.modules.functionbox.functions.`package`

import cn.hutool.core.util.StrUtil
import com.addzero.common.util.shell.CommandExecutor
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.InputStreamReader

class BrewPackageManager : PackageManager {
    override suspend fun checkPackageStatus(packageName: String): PackageStatus {
        return withContext(Dispatchers.IO) {
            val status = PackageStatus(packageName)
            try {
                val execute = CommandExecutor.execute("brew list | grep $packageName")
                withContext(Dispatchers.IO) {
                    status.status = if (execute.isNotBlank()) {
                        PackageStatus.Status.INSTALLED
                    } else {
                        PackageStatus.Status.NOT_INSTALLED
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.IO) {
                    status.status = PackageStatus.Status.ERROR
                    status.error = e.message ?: "检查失败"
                }
            }
            status
        }
    }

    override suspend fun installPackage(packageStatus: PackageStatus) {
        withContext(Dispatchers.IO) {
            try {
                packageStatus.status = PackageStatus.Status.INSTALLING
                packageStatus.progress = 0f

                val process = ProcessBuilder("brew", "install", packageStatus.name)
                    .redirectErrorStream(true)
                    .start()

                val reader = BufferedReader(InputStreamReader(process.inputStream))
                var line: String?
                var progressStep = 0.1f

                while (reader.readLine().also { line = it } != null) {
                    withContext(Dispatchers.IO) {
                        packageStatus.progress += progressStep
                        if (packageStatus.progress >= 1f) {
                            packageStatus.progress = 0.9f
                            progressStep = 0.01f
                        }
                    }
                }

                val exitCode = process.waitFor()
                withContext(Dispatchers.IO) {
                    if (exitCode == 0) {
                        packageStatus.progress = 1f
                        packageStatus.status = PackageStatus.Status.INSTALLED
                    } else {
                        packageStatus.status = PackageStatus.Status.ERROR
                        packageStatus.error = "安装失败"
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.IO) {
                    packageStatus.status = PackageStatus.Status.ERROR
                    packageStatus.error = e.message ?: "未知错误"
                }
            }
        }
    }

    override suspend fun uninstallPackage(packageStatus: PackageStatus) {
        withContext(Dispatchers.IO) {
            try {
                packageStatus.status = PackageStatus.Status.UNINSTALLING
                packageStatus.progress = 0f

                val process = ProcessBuilder("brew", "uninstall", packageStatus.name)
                    .redirectErrorStream(true)
                    .start()

                val reader = BufferedReader(InputStreamReader(process.inputStream))
                var line: String?
                var progressStep = 0.1f

                while (reader.readLine().also { line = it } != null) {
                    withContext(Dispatchers.IO) {
                        packageStatus.progress += progressStep
                        if (packageStatus.progress >= 1f) {
                            packageStatus.progress = 0.9f
                            progressStep = 0.01f
                        }
                    }
                }

                val exitCode = process.waitFor()
                withContext(Dispatchers.IO) {
                    if (exitCode == 0) {
                        packageStatus.progress = 1f
                        packageStatus.status = PackageStatus.Status.NOT_INSTALLED
                    } else {
                        packageStatus.status = PackageStatus.Status.ERROR
                        packageStatus.error = "卸载失败"
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.IO) {
                    packageStatus.status = PackageStatus.Status.ERROR
                    packageStatus.error = e.message ?: "未知错误"
                }
            }
        }
    }
}
