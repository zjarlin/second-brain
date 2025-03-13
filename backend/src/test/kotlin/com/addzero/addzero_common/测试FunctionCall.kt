package com.addzero.addzero_common

import com.addzero.web.modules.ai.util.ai.ai_abs_builder.AiUtil
import com.addzero.web.modules.second_brain.dotfiles.*
import com.addzero.web.modules.second_brain.dotfiles.dto.BizDotfilesSpec
import com.addzero.web.modules.second_brain.tag.BizTag
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.junit.jupiter.api.Test
import org.springframework.ai.ollama.OllamaChatModel
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import kotlin.test.todo

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class 测试FunctionCall(
    val ollamaChatModel: OllamaChatModel,
) {
    @Test
    fun test(): Unit {
        val aiUtil = AiUtil("deepseek-r1:latest", "hello").ask("","")
        println(aiUtil)


    }
}
