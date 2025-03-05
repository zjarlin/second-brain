package com.addzero.web.modules.demo

import androidx.compose.runtime.Composable
import com.addzero.web.ui.system.dynamicroute.MetaSpec
import com.addzero.web.ui.system.dynamicroute.RouteMetadata
import com.mikepenz.markdown.m3.Markdown


class MarkdownDemo : MetaSpec {

    override val metadata: RouteMetadata
        get() = RouteMetadata(
            parentName = "测试demo",
            title = "测试markdown",
//            icon = Icons.Filled.Apps,
            visible = true,
//            permissions = emptyList()
        )

    @Composable
    override fun render() {
        val markdown = """
### What's included 🚀

- Super simple setup
- Cross-platform ready
- Lightweight
""".trimIndent()

//
        Markdown(
            content = markdown,
//            components = markdownComponents(
//                codeBlock = { MarkdownHighlightedCodeBlock(it.content, it.node, highlightsBuilder) },
//                codeFence = { MarkdownHighlightedCodeFence(it.content, it.node, highlightsBuilder) },
//
//
//                )
        )
    }
}
