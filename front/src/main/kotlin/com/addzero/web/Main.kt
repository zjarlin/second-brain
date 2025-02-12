package com.addzero.web

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.addzero.SpringBootApp
import com.addzero.SpringBootApp.Companion.runSpringBootApp
import com.addzero.web.ui.layout.MainLayout
import com.addzero.web.ui.theme.AppTheme
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
open class Application


fun main(args: Array<String>) {

    application {
        val runApp = runSpringBootApp(args)


        val windowState = rememberWindowState(
            width = 1200.dp, height = 800.dp
        )

        Window(
            onCloseRequest = ::exitApplication, title = "addzero soft", state = windowState
        ) {
            App()
        }
    }
}