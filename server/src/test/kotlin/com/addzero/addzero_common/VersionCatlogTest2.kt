package com.addzero.addzero_common

import cn.hutool.core.util.StrUtil
import com.addzero.addzero_common.versioncatlogutil.TomlUtils
import org.junit.jupiter.api.Test


class JvmToolTest {

    @Test
    fun 测试解析tomlByOrg() {
        val str1 = " dasdasd   [plugins]    Hello        vasdvads"
        val result1 = StrUtil.appendIfMissing(str1, "World")  // 结果: "HelloWorld"

        // 示例使用
        val content = """
    [versions]
    kotlin = "1.8.0"
    
    [plugins]
    kotlin-jvm = { id = "org.jetbrains.kotlin.jvm", version.ref = "kotlin" }
    
    [libraries]
    commons-io = { group = "commons-io", name = "commons-io", version = "2.11.0" }
""".trimIndent()

// 在 [plugins] 标记后追加新插件
        val result = TomlUtils.appendAfterTag(
            content,
            "plugins]",
            """    spring-boot = { id = "org.springframework.boot", version = "3.0.0" }"""
        )

        println(result)

        val path1 = "/Users/zjarlin/Downloads/addzero_common/gradle/libs.versions.toml"

        val toTomlDTO = TomlUtils. toTomlDTO(path1)
        val merge = TomlUtils.merge(toTomlDTO)
        println()
    }

    @Test
    fun 融合(): Unit {
        val s = "/Users/zjarlin/IdeaProjects/addzero_common/gradle/ooo/libs.versions.toml"

        val s1 = "/Users/zjarlin/IdeaProjects/addzero_common/gradle/otherlibs/libs.versions.toml"
        val s2 = "/Users/zjarlin/IdeaProjects/addzero_common/gradle/libs.versions.toml"
        val merge = TomlUtils.merge(s, s1, s2)
        println(merge)


    }







}