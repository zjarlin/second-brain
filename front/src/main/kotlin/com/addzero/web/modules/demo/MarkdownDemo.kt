package com.addzero.web.modules.demo

import androidx.compose.runtime.Composable
import com.addzero.Route
import com.mikepenz.markdown.m3.Markdown


@Composable
@Route
fun 测试markdown() {
    val markdown = """
### What's included 🚀

- Super simple setup
- Cross-platform ready
- Lightweight
""".trimIndent()

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
