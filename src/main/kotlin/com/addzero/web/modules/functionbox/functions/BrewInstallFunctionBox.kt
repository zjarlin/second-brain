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
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.addzero.web.modules.common.dialog.CommonDialog
import com.addzero.web.modules.common.dialog.DefaultDialogButton
import com.addzero.web.modules.functionbox.model.FunctionBoxSpec
import com.addzero.web.modules.functionbox.functions.`package`.PackageManagerFactory
import com.addzero.web.modules.functionbox.functions.`package`.PackageStatus
import com.addzero.web.modules.functionbox.utils.CoroutineManager
import kotlinx.coroutines.*

class BrewInstallFunctionBox : FunctionBoxSpec {
    private val packageManager = PackageManagerFactory.createPackageManager()
    override val name: String
        get() = "brew auto install"
    override val description: String
        get() = "brew自动安装所需软件"
    override val icon: ImageVector
        get() = Icons.Default.Home

    private val packageList = listOf(
        "wechatwebdevtools"
    )

    @Composable
    private fun PackageStatusItem(packageStatus: PackageStatus, onInstall: () -> Unit) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            elevation = 4.dp
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
                        PackageStatus.Status.INSTALLED -> Text("已安装", color = MaterialTheme.colors.primary)
                        PackageStatus.Status.INSTALLING -> {
                            Column {
                                Text("安装中...")
                                LinearProgressIndicator(
                                    progress = packageStatus.progress,
                                    modifier = Modifier
                                        .width(100.dp)
                                        .padding(top = 4.dp)
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
                                    onClick = onInstall,
                                    modifier = Modifier.padding(top = 8.dp)
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
        
        val coroutineManager = remember { 
            CoroutineManager(scope, packageList.map { PackageStatus(it) })
        }
        val packageStatuses by coroutineManager.state.collectAsState()

        LaunchedEffect(Unit) {
            packageList.forEachIndexed { index, pkg ->
                coroutineManager.launch("check_$pkg") {
                    try {
                        val status = packageManager.checkPackageStatus(pkg)
                        coroutineManager.updateState { list ->
                            list.toMutableList().also { it[index] = status }
                        }
                    } catch (e: Exception) {
                        coroutineManager.updateState { list ->
                            list.toMutableList().also { 
                                it[index] = PackageStatus(pkg).apply {
                                    status = PackageStatus.Status.ERROR
                                    error = e.message ?: "检查失败"
                                }
                            }
                        }
                    }
                }
            }
        }

        DisposableEffect(Unit) {
            onDispose {
                coroutineManager.dispose()
            }
        }

        if (isOpen) {
            CommonDialog(
                onDismissRequest = {
                    coroutineManager.dispose()
                    isOpen = false
                },
                title = "软件安装状态",
                content = {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 400.dp)
                    ) {
                        items(packageStatuses) { status ->
                            PackageStatusItem(
                                packageStatus = status,
                                onInstall = {
                                    coroutineManager.launch("install_${status.name}") {
                                        packageManager.installPackage(status, scope)
                                    }
                                }
                            )
                        }
                    }
                },
                confirmButton = {
                    DefaultDialogButton(
                        onClick = {
                            val uninstalledPackages = packageStatuses.filter { 
                                it.status == PackageStatus.Status.NOT_INSTALLED 
                            }
                            uninstalledPackages.forEach { status ->
                                coroutineManager.launch("install_${status.name}") {
                                    packageManager.installPackage(status, scope)
                                }
                            }
                        },
                        text = "一键安装"
                    )
                },
                dismissButton = {
                    DefaultDialogButton(
                        onClick = {
                            coroutineManager.dispose()
                            isOpen = false
                        },
                        text = "关闭"
                    )
                }
            )
        }
    }
}
