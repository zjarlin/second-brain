package com.addzero.web.modules.functionbox.functions.`package`


interface PackageManager {

    suspend fun checkPackageStatus(packageName: String): PackageStatus
    suspend fun installPackage(packageStatus: PackageStatus)
    suspend fun uninstallPackage(packageStatus: PackageStatus)

}

data class PackageStatus(
    val name: String, var status: Status = Status.CHECKING, var progress: Float = 0f, var error: String = ""
) {
    enum class Status {
        CHECKING, INSTALLED, INSTALLING, UNINSTALLING, ERROR, NOT_INSTALLED
    }
}
