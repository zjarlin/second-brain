package com.addzero.web.modules.functionbox.functions.`package`

import kotlinx.coroutines.CoroutineScope

interface PackageManager {
    suspend fun checkPackageStatus(packageName: String): PackageStatus
    suspend fun installPackage(packageStatus: PackageStatus, scope: CoroutineScope)
    suspend fun uninstallPackage(packageStatus: PackageStatus, scope: CoroutineScope)
    fun isAvailable(): Boolean
}

data class PackageStatus(
    val name: String,
    var status: Status = Status.CHECKING,
    var progress: Float = 0f,
    var error: String = ""
) {
    enum class Status {
        CHECKING,
        INSTALLED,
        INSTALLING,
        UNINSTALLING,
        ERROR,
        NOT_INSTALLED
    }
}