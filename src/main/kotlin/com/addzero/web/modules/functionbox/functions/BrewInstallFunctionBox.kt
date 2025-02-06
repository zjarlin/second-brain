package com.addzero.web.modules.functionbox.functions

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.addzero.web.modules.functionbox.model.FunctionBoxSpec
import com.addzero.web.modules.functionbox.functions.`package`.PackageManagerFactory
import com.addzero.web.modules.functionbox.functions.`package`.PackageStatus
import kotlinx.coroutines.*



/**
 * brew自动安装所需软件
 */
class BrewInstallFunctionBox : FunctionBoxSpec {
    private val packageManager = PackageManagerFactory.createPackageManager()
    override val name: String
        get() = "brew auto install"
    override val description: String
        get() = "brew自动安装所需软件"
    override val icon: ImageVector
        get() = Icons.Default.Home

    private val packageList = listOf(
        "git",
        "node",
        "python",
        "wget"
    )

    @Composable
    override fun invoke() {
        val scope = rememberCoroutineScope()
        val packageManager = remember { PackageManagerFactory.createPackageManager() }
        var packageStatuses by remember { mutableStateOf(packageList.map { PackageStatus(it) }) }
        var isOpen by remember { mutableStateOf(true) }
        var installJobs by remember { mutableStateOf(mutableMapOf<String, Job>()) }

        if (!isOpen) return

        LaunchedEffect(Unit) {
            packageList.forEachIndexed { index, pkg ->
                try {
                    val status = packageManager.checkPackageStatus(pkg)
                    packageStatuses = packageStatuses.toMutableList().also { list ->
                        list[index] = status
                    }
                } catch (e: Exception) {
                    packageStatuses = packageStatuses.toMutableList().also { list ->
                        list[index] = PackageStatus(pkg).apply {
                            status = PackageStatus.Status.ERROR
                            error = e.message ?: "检查失败"
                        }
                    }
                }
            }
        }

        DisposableEffect(Unit) {
            onDispose {
                installJobs.values.forEach { it.cancel() }
                installJobs.clear()
            }
        }

        Dialog(
            onDismissRequest = { isOpen = false }
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(0.8f),
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colors.surface,
                elevation = 24.dp
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "软件安装状态",
                            style = MaterialTheme.typography.h6
                        )
                        IconButton(onClick = {
                            installJobs.values.forEach { it.cancel() }
                            installJobs.clear()
                            isOpen = false
                        }) {
                            Icon(Icons.Default.Close, contentDescription = "关闭")
                        }
                    }

                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(max = 400.dp)
                    ) {
                        items(packageStatuses) { status ->
                            PackageStatusItem(
                                packageStatus = status,
                                onInstall = {
                                    installJobs[status.name]?.cancel()
                                    installJobs[status.name] = scope.launch {
                                        packageManager.installPackage(status, scope)
                                    }
                                }
                            )
                        }
                    }

                    Button(
                        onClick = {
                            installJobs.values.forEach { it.cancel() }
                            installJobs.clear()
                            isOpen = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        Text("取消")
                    }
                }
            }
        }
    }

    @Composable
    private fun PackageStatusItem(
        packageStatus: PackageStatus,
        onInstall: () -> Unit
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            elevation = 4.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
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
}
