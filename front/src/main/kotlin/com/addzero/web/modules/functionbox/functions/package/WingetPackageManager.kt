package com.addzero.web.modules.functionbox.functions.`package`

import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.InputStreamReader

class WingetPackageManager : PackageManager {
    override suspend fun checkPackageStatus(packageName: String): PackageStatus {
        return withContext(Dispatchers.IO) {
            val status = PackageStatus(packageName)
            try {
                withTimeout(5000) {
                    val process = ProcessBuilder("winget", "list", packageName)
                        .redirectErrorStream(true)
                        .start()

                    val exitCode = process.waitFor()
                    status.status = if (exitCode == 0) {
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

    override suspend fun installPackage(packageStatus: PackageStatus) {
        try {
            packageStatus.status = PackageStatus.Status.INSTALLING
            packageStatus.progress = 0f

            val process = ProcessBuilder(
                "winget",
                "install",
                packageStatus.name,
                "--accept-source-agreements",
                "--accept-package-agreements"
            )
                .redirectErrorStream(true)
                .start()

            val reader = BufferedReader(InputStreamReader(process.inputStream))
            var line: String?
            var progressStep = 0.1f

            withContext(Dispatchers.IO) {
                while (reader.readLine().also { line = it } != null) {
                    withContext(Dispatchers.Main) {
                        packageStatus.progress += progressStep
                        if (packageStatus.progress >= 1f) {
                            packageStatus.progress = 0.9f
                            progressStep = 0.01f
                        }
                    }
                }

                val exitCode = process.waitFor()
                withContext(Dispatchers.Main) {
                    if (exitCode == 0) {
                        packageStatus.progress = 1f
                        packageStatus.status = PackageStatus.Status.INSTALLED
                    } else {
                        packageStatus.status = PackageStatus.Status.ERROR
                        packageStatus.error = "安装失败"
                    }
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                packageStatus.status = PackageStatus.Status.ERROR
                packageStatus.error = e.message ?: "未知错误"
            }
        }
    }

    override suspend fun uninstallPackage(packageStatus: PackageStatus) {
        withContext(Dispatchers.IO) {
            try {
                packageStatus.status = PackageStatus.Status.UNINSTALLING
                packageStatus.progress = 0f
                
                val process = ProcessBuilder("winget", "uninstall", packageStatus.name)
                    .redirectErrorStream(true)
                    .start()

                val reader = BufferedReader(InputStreamReader(process.inputStream))
                var line: String?
                var progressStep = 0.1f

                while (reader.readLine().also { line = it } != null) {
                    withContext(Dispatchers.Main) {
                        packageStatus.progress += progressStep
                        if (packageStatus.progress >= 1f) {
                            packageStatus.progress = 0.9f
                            progressStep = 0.01f
                        }
                    }
                }

                val exitCode = process.waitFor()
                withContext(Dispatchers.Main) {
                    if (exitCode == 0) {
                        packageStatus.progress = 1f
                        packageStatus.status = PackageStatus.Status.NOT_INSTALLED
                    } else {
                        packageStatus.status = PackageStatus.Status.ERROR
                        packageStatus.error = "卸载失败"
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    packageStatus.status = PackageStatus.Status.ERROR
                    packageStatus.error = e.message ?: "未知错误"
                }
            }
        }
    }
}
