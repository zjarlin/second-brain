package com.addzero.web.modules.ai.config

import cn.hutool.extra.spring.SpringUtil
import io.micrometer.observation.ObservationRegistry
import org.springframework.ai.chat.memory.ChatMemory
import org.springframework.ai.chat.memory.InMemoryChatMemory
import org.springframework.ai.chat.model.ChatModel
import org.springframework.ai.embedding.EmbeddingModel
import org.springframework.ai.image.ImageModel
import org.springframework.ai.model.function.FunctionCallback
import org.springframework.ai.model.function.FunctionCallbackResolver
import org.springframework.ai.ollama.OllamaChatModel
import org.springframework.ai.ollama.OllamaEmbeddingModel
import org.springframework.ai.ollama.api.OllamaApi
import org.springframework.ai.ollama.api.OllamaOptions
import org.springframework.ai.ollama.management.ModelManagementOptions
import org.springframework.ai.transformer.splitter.TokenTextSplitter
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary


@Configuration
class AiConfig {
    @Value("\${spring.ai.ollama.base-url}")
    lateinit var ollamaUrl: String

//    @Value("\${spring.web.zhipuai.api-key}")
//    lateinit var zhipuaiApiKey: String
//
//    @Value("\${spring.web.dashscope.api-key}")
//    lateinit var dashscopeApiKey: String
//
//    @Value("\${spring.web.openai.api-key}")
//    lateinit var openaiKey: String

    @Bean
    fun tokenTextSplitter(): TokenTextSplitter {
        // Return an instance of TokenTextSplitter
        return TokenTextSplitter()
    }


    @Bean
    @Primary
    @ConditionalOnClass(value = [EmbeddingModel::class])
    fun embeddingModel(
        ollamaEmbeddingModel: OllamaEmbeddingModel

    ): EmbeddingModel {
        return ollamaEmbeddingModel

//        val ollamaApi = OllamaApi(ollamaUrl)
//        OllamaOptions.create().withModel()
//        val ollamaEmbeddingModel = OllamaEmbeddingModel(ollamaApi)
//        return ollamaEmbeddingModel
//
//        val build = OllamaOptions.builder()
//            .withModel(OllamaModel.MISTRAL.id())
//            .build()
//        val create = ObservationRegistry.create()
//        val embeddingModel = OllamaEmbeddingModel(
//            ollamaApi,
//            build
//            , create

//        return OpenAiEmbeddingModel(OpenAiApi(openaiKey))
//        val dashScopeAiApi = DashScopeAiApi(dashscopeApiKey)
//        DashScopeAiEmbeddingModel(,)
//        val dashScopeAiEmbeddingModel = DashScopeAiEmbeddingModel(dashScopeAiApi)
//        return DashscopeEmbeddingModel(DashscopeApi.builder().withApiKey(dashscopeApiKey).build());
        //        return new OllamaEmbeddingModel(new OllamaApi(ollamaUrl));

//        return new DashscopeEmbeddingModel(DashscopeApi.builder().withApiKey(dashscopeApiKey).build());
//        dashs
//        return new ZhiPuAiEmbeddingModel(new ZhiPuAiApi(zhipuaiApiKey));
    }

    @Bean
    fun chatMemory(): ChatMemory {
        val inMemoryChatMemory = InMemoryChatMemory()
        return inMemoryChatMemory
    }

//    @Bean
//    @Primary
//    fun ollamaChatModel(): ChatModel {
//        val ollamaApi = OllamaApi(ollamaUrl)
//        OllamaOptions.builder()
//        val defaultOptions = OllamaOptions.create()
//        val observationRegistry = ObservationRegistry.create()
//        val modelManagementOptions = ModelManagementOptions.builder().build()
//        val toolCallingManager = org.springframework.ai.model.tool.ToolCallingManager.builder().build()
//
//        return OllamaChatModel(
//            ollamaApi,
//            defaultOptions,
//            toolCallingManager,
//            observationRegistry,
//            modelManagementOptions
//        )
//    }

    //    public void diajsoid() {

    //        ChatClient.create(new OllamaChatModel(new OllamaApi(ollamaUrl), OllamaOptions.create().withEmbeddingModel(embeddingModel())))
    //    }
//    @Bean
//    fun vectorStore(): VectorStore {
//        return SpringUtil.getBean(PgVectorStore::class.java)
//    }
//    @Bean
//    fun imageModel(): ImageModel {
//        return SpringUtil.getBean(ollamai::class.java)
//    }

//    @Bean
//    fun restClientCustomizer(): RestClientCustomizer {
//        return RestClientCustomizer { restClientBuilder: RestClient.Builder ->
//            restClientBuilder
//                .requestFactory(
//                    ClientHttpRequestFactories.get(
//                        ClientHttpRequestFactorySettings.DEFAULTS
//                            .withConnectTimeout(Duration.ofSeconds(10))
//                            .withReadTimeout(Duration.ofSeconds(20))
//                    )
//                )
//        }
//    }


}