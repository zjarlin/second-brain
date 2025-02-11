package com.addzero.web

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import cn.hutool.core.net.NetUtil
import cn.hutool.core.util.StrUtil
import com.addzero.web.ui.layout.MainLayout
import com.addzero.web.ui.theme.AppTheme
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.core.env.Environment
import java.util.*
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

        val run = runApplication<Application>(*args)
        val env: Environment = run.environment
        val ip = NetUtil.getLocalhostStr()
        val port = env.getProperty("server.port")
        var property = env.getProperty("server.servlet.context-path")
        property = StrUtil.removeSuffix(property, "/")
        val path = Optional.ofNullable(property).orElse("")

        println(
            """
    ----------------------------------------------------------
        Application is running! Access URLs:
        Knife4j文档: http://$ip:$port$path/doc.html
        Jimmer接口文档:  http://$ip:$port$path/openapi-ui.html
        Jimmer面对接ts.zip:  http://localhost:$port$path/ts.zip
    ----------------------------------------------------------
    """.trimIndent()
        )


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