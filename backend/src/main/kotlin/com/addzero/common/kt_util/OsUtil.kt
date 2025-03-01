package com.addzero.common.kt_util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

object OsUtil {
    /**
     * 获取当前平台类型
     */
    fun getPlatformType(): PlatformType {
        val osName = System.getProperty("os.name").lowercase(Locale.getDefault())
        return when {
            osName.contains("win") -> PlatformType.WINDOWS
            osName.contains("mac") -> PlatformType.MAC
            osName.contains("nix") || osName.contains("nux") || osName.contains("aix") -> PlatformType.LINUX
            else -> PlatformType.UNKNOWN
        }
    }


    /**
     * 执行终端命令
     */
    fun executeTerminalCommand(command: String) {
        runCatching {
            Runtime.getRuntime().exec(command)
        }.onFailure { e ->
            val s = "执行出错：${e.message}"
            println(s)
        }
    }

    fun openFolder(path: String) {
        when (getPlatformType()) {
            PlatformType.WINDOWS, PlatformType.UNKNOWN -> {
                executeTerminalCommand("explorer.exe $path")
            }

            PlatformType.MAC -> {
                executeTerminalCommand("open $path")
            }

            PlatformType.LINUX -> {
                executeTerminalCommand("xdg-open $path")
            }
        }
    }


    /**
     * 执行命令，获取输出
     */
    suspend fun executeCommandWithResult(command: String) = withContext(Dispatchers.IO) {
        val processBuilder = ProcessBuilder(*command.split(" ").toTypedArray())
        val process = processBuilder.start()

        val reader = BufferedReader(InputStreamReader(process.inputStream))
        val output = StringBuilder()
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            output.append(line).append("\n")
        }
        // 等待进程结束
        process.waitFor()
        // 关闭输入流
        reader.close()
        output.toString()
    }

}

enum class PlatformType {
    UNKNOWN, WINDOWS, MAC, LINUX,
}
