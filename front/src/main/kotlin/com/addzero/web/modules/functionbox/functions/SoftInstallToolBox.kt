package com.addzero.web.modules.functionbox.functions

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.addzero.web.modules.functionbox.functions.`package`.PackageManager
import com.addzero.web.ui.components.dialog.AddDialog
import com.addzero.web.ui.components.dialog.DefaultDialogButton
import com.addzero.web.modules.functionbox.model.FunctionBoxSpec
import com.addzero.web.modules.functionbox.functions.`package`.PackageManagerFactory
import com.addzero.web.modules.functionbox.functions.`package`.PackageStatus
import com.addzero.web.ui.components.button.AddButton
import kotlinx.coroutines.*

class SoftInstallFunctionBox : FunctionBoxSpec {
    private val packageManager = PackageManagerFactory.createPackageManager()
    private val packageList = listOf(
        "wechatwebdevtools"
    )

    override val name: String
        get() = "auto install"
    override val description: String
        get() = "自动安装所需软件"
    override val icon: ImageVector
        get() = Icons.Default.Download

    @Composable
    private fun PackageStatusItem(
        packageStatus: PackageStatus, onInstall: () -> Unit, onUninstall: () -> Unit
    ) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), elevation = 4.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(packageStatus.name)
                    when (packageStatus.status) {
                        PackageStatus.Status.CHECKING -> CircularProgressIndicator(modifier = Modifier.size(16.dp))
                        PackageStatus.Status.INSTALLED -> {
                            Button(onClick = onUninstall) {
                                Text("卸载")
                            }
                        }

                        PackageStatus.Status.INSTALLING -> {
                            Column {
                                Text("安装中...")
                                LinearProgressIndicator(
                                    progress = packageStatus.progress,
                                    modifier = Modifier.width(100.dp).padding(top = 4.dp)
                                )
                            }
                        }

                        PackageStatus.Status.UNINSTALLING -> {
                            Column {
                                Text("卸载中...")
                                LinearProgressIndicator(
                                    progress = packageStatus.progress,
                                    modifier = Modifier.width(100.dp).padding(top = 4.dp)
                                )
                            }
                        }

                        PackageStatus.Status.ERROR -> {
                            Column {
                                Text("安装失败", color = MaterialTheme.colors.error)
                                Text(
                                    text = packageStatus.error,
                                    style = MaterialTheme.typography.caption,
                                    color = MaterialTheme.colors.error
                                )
                                Button(
                                    onClick = onInstall, modifier = Modifier.padding(top = 8.dp)
                                ) {
                                    Text("重试")
                                }
                            }
                        }

                        PackageStatus.Status.NOT_INSTALLED -> {
                            Button(onClick = onInstall) {
                                Text("安装")
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    override fun invoke() {
        val scope = rememberCoroutineScope()
        val packageManager = remember { PackageManagerFactory.createPackageManager() }
        var isOpen by remember { mutableStateOf(true) }
        var packageStatuses by remember { mutableStateOf(packageList.map { PackageStatus(it) }) }


        LaunchedEffect(Unit) {
            packageList.forEachIndexed { index, pkg ->
                scope.launch {
                    try {
                        val status = packageManager.checkPackageStatus(pkg)
                        packageStatuses = packageStatuses.toMutableList().also { it[index] = status }
                    } catch (e: Exception) {
                        packageStatuses = packageStatuses.toMutableList().also {
                            it[index] = PackageStatus(pkg).apply {
                                status = PackageStatus.Status.ERROR
                                error = e.message ?: "检查失败"
                            }
                        }
                    }
                }
            }
        }

        if (isOpen) {
            AddDialog(onDismissRequest = {
                isOpen = false
            }, title = "软件安装状态", content = {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().heightIn(max = 400.dp)
                ) {
                    items(packageStatuses) { status ->
                        PackageStatusItem(packageStatus = status, onInstall = {
                            onInstall(packageStatuses, status, packageManager)
                        }, onUninstall = {
                            packageStatuses = packageStatuses.toMutableList().also { mutableList ->
                                val index = mutableList.indexOfFirst { it.name == status.name }
                                if (index != -1) {
                                    mutableList[index] = status.copy(status = PackageStatus.Status.UNINSTALLING)
                                }
                            }
                            scope.launch {
                                packageManager.uninstallPackage(
                                    status,
                                )
                            }
                        })
                    }
                }
            }, confirmButton = {
                DefaultDialogButton(
                    onClick = {
                        val uninstalledPackages = packageStatuses.filter {
                            it.status == PackageStatus.Status.NOT_INSTALLED
                        }
                        uninstalledPackages.forEach { status ->
                            onInstall(packageStatuses, status, packageManager)
                        }
                    }, text = "一键安装"
                )
            }, dismissButton = {

                AddButton("关闭", onClick = {
                    isOpen = false
                })


//                    DefaultDialogButton(
//                        onClick = {
//                            isOpen = false
//                        }, text = "关闭"
//                    )
            })
        }
    }

}

private fun onInstall(
    packageStatuses: List<PackageStatus>, status: PackageStatus, packageManager: PackageManager
) {
    var packageStatuses1 = packageStatuses


    packageStatuses1 = packageStatuses1.toMutableList().also { mutableList ->
        val index = mutableList.indexOfFirst { it.name == status.name }
        if (index != -1) {
            mutableList[index] = status.copy(status = PackageStatus.Status.INSTALLING)
        }
    }
    // 在协程中启动后端服务
    val scope = CoroutineScope(Dispatchers.IO)
    scope.launch {
        packageManager.installPackage(status)
    }


}
