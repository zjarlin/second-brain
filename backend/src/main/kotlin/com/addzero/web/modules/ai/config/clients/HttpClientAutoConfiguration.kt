//package com.example.demo.modules.web.config.clients
//
//import org.springframework.boot.context.properties.EnableConfigurationProperties
//import org.springframework.boot.web.client.ClientHttpRequestFactories
//import org.springframework.boot.web.client.ClientHttpRequestFactorySettings
//import org.springframework.boot.web.client.RestClientCustomizer
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.http.client.BufferingClientHttpRequestFactory
//import org.springframework.http.client.ClientHttpRequestInterceptor
//import org.springframework.web.client.RestClient
//
//@Configuration(proxyBeanMethods = false)
//@EnableConfigurationProperties(
//    HttpClientProperties::class
//)
//open class HttpClientAutoConfiguration {
//    @Bean
//    open fun restClientCustomizer(httpClientProperties: HttpClientProperties): RestClientCustomizer {
//        val clientConfig: HttpClientConfig = HttpClientConfig.builder()
//
//            .connectTimeout(httpClientProperties.connectTimeout)
//            .readTimeout(httpClientProperties.readTimeout)
//            .sslBundle(httpClientProperties.sslBundle)
//            .logRequests(httpClientProperties.logRequests)
//            .logResponses(httpClientProperties.logResponses)
//            .build()
//
//        return RestClientCustomizer { restClientBuilder: RestClient.Builder ->
//            restClientBuilder
//                .requestFactory(
//                    BufferingClientHttpRequestFactory(
//                        ClientHttpRequestFactories.get(
//                            ClientHttpRequestFactorySettings.DEFAULTS
//                                .withConnectTimeout(clientConfig.connectTimeout)
//                                .withReadTimeout(clientConfig.readTimeout)
//                        )
//                    )
//                )
//                .requestInterceptors { interceptors: MutableList<ClientHttpRequestInterceptor?> ->
//                    if (clientConfig.logRequests || clientConfig.logResponses) {
//                        interceptors.add(HttpLoggingInterceptor(clientConfig.logRequests, clientConfig.logResponses))
//                    }
//                }
//        }
//    }
//}