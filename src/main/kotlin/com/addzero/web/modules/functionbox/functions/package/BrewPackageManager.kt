package com.addzero.web.modules.functionbox.functions.`package`

import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.InputStreamReader

class BrewPackageManager : PackageManager {
    override suspend fun checkPackageStatus(packageName: String): PackageStatus {
        return withContext(Dispatchers.IO) {
            val status = PackageStatus(packageName)
            try {
                withTimeout(5000) {
                    val process = ProcessBuilder("brew", "list", packageName)
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

    override suspend fun installPackage(packageStatus: PackageStatus, scope: CoroutineScope) {
        withContext(Dispatchers.IO) {
            try {
                packageStatus.status = PackageStatus.Status.INSTALLING
                val process = ProcessBuilder("brew", "install", packageStatus.name)
                    .redirectErrorStream(true)
                    .start()

                val reader = BufferedReader(InputStreamReader(process.inputStream))
                var line: String?

                while (reader.readLine().also { line = it } != null) {
                    scope.launch(Dispatchers.Main) {
                        packageStatus.progress += 0.1f
                        if (packageStatus.progress > 1f) packageStatus.progress = 1f
                    }
                }

                val exitCode = process.waitFor()
                scope.launch(Dispatchers.Main) {
                    if (exitCode == 0) {
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

    override fun isAvailable(): Boolean {
        return try {
            val process = ProcessBuilder("which", "brew")
                .redirectErrorStream(true)
                .start()
            process.waitFor() == 0
        } catch (e: Exception) {
            false
        }
    }
}