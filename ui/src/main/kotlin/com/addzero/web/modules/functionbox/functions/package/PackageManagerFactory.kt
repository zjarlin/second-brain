package com.addzero.web.modules.functionbox.functions.`package`

import jdk.internal.util.OperatingSystem.isMacOS

class PackageManagerFactory {
    companion object {
        fun createPackageManager(): PackageManager {
            return when {
                isWindows() -> WingetPackageManager().takeIf { it.isAvailable() }
                isMacOS() -> BrewPackageManager().takeIf { it.isAvailable() }
                else -> null
            } ?: throw UnsupportedOperationException("当前系统不支持包管理器或包管理器未安装")
        }

        private fun isWindows(): Boolean {
            return System.getProperty("os.name").lowercase().contains("win")
        }
        private fun isMacOS(): Boolean {
            return System.getProperty("os.name").lowercase().contains("mac")
        }

    }
}

