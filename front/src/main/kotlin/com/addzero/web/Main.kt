package com.addzero.web

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.addzero.SpringBootApp.Companion.runSpringBootApp
import com.addzero.web.ui.system.animation.UseSplashScreen
import com.addzero.web.ui.system.layout.MainLayout
import com.addzero.web.ui.system.theme.AppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap

val loggerMap = ConcurrentHashMap<Class<*>, Logger>()
inline val <reified T> T.log: Logger get() = loggerMap.computeIfAbsent(T::class.java) { LoggerFactory.getLogger(it) }


fun main(args: Array<String>) {
    //ksp生成版dsl
    application {
        val useSplashScreen = UseSplashScreen(
            { MainLayout() }
        )


        // 在协程中启动后端服务
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            runSpringBootApp(args)
            useSplashScreen.showAnimation = false
        }

        val windowState = rememberWindowState(
            width = 1200.dp, height = 800.dp
        )

        Window(
            onCloseRequest = ::exitApplication, title = "compose desktop", state = windowState
        ) {
            AppTheme {
                useSplashScreen.render {}
            }
        }
    }
}
