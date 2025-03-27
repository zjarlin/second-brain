package com.addzero.web.modules.demo

import androidx.compose.runtime.Composable
import cn.hutool.core.io.FileUtil
import com.addzero.Route
import com.addzero.common.kt_util.getParentPathAndmkdir
import com.addzero.web.ui.components.ScrollableContainer
import com.mikepenz.markdown.m3.Markdown
import java.io.File

fun main() {
    val absolutePath = File("").absolutePath
    val baseProject = absolutePath.getParentPathAndmkdir("")

}

@Composable
@Route
fun 测试markdown() {
    val absolutePath = File("").absolutePath
    val parent = FileUtil.getParent(absolutePath,1)
    val readmeContent = FileUtil.readUtf8String(parent + File.separator + "README.md")

    ScrollableContainer {
        Markdown(
            content = readmeContent,
        )
    }
}
