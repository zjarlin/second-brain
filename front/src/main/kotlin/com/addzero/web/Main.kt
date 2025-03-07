package com.addzero.web

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.addzero.SpringBootApp.Companion.runSpringBootApp
import com.addzero.web.ui.system.layout.MainLayout
import com.addzero.web.ui.system.theme.AppTheme
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import java.util.concurrent.ConcurrentHashMap

val loggerMap = ConcurrentHashMap<Class<*>, Logger>()
inline val <reified T> T.log: Logger get() = loggerMap.computeIfAbsent(T::class.java) { LoggerFactory.getLogger(it) }

@Composable
@Preview
fun App() {
    AppTheme {
        MainLayout()
    }
}

@SpringBootApplication
open class Compose4desktop


fun main(args: Array<String>) {
    //怕springboot没完全跑起来影响前台
    application {
        // 在协程中启动后端服务
//        val scope = CoroutineScope(Dispatchers.IO)
//        scope.launch {
            runSpringBootApp(args)
//        }


        val windowState = rememberWindowState(
            width = 1200.dp, height = 800.dp
        )

        Window(
            onCloseRequest = ::exitApplication, title = "+0 后台管理系统", state = windowState
        ) {
            App()
        }
    }
}
