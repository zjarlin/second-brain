package com.addzero

import cn.hutool.core.net.NetUtil
import cn.hutool.core.util.StrUtil
import com.addzero.SpringBootApp.Companion.runSpringBootApp
import org.babyfish.jimmer.client.EnableImplicitApi
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.core.env.Environment
import java.util.*

@SpringBootApplication
@EnableImplicitApi
open class SpringBootApp {
    companion object {
        fun runSpringBootApp(args: Array<String>) {
            val run = runApplication<SpringBootApp>(*args)
            val env: Environment = run.environment
            // 获取本机IP地址
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
    """
            )
        }
    }
}


fun main(args: Array<String>) {
    val runApp = runSpringBootApp(args)
}