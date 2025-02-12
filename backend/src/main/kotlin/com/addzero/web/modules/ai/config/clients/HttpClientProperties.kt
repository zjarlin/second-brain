package com.example.demo.modules.ai.config.clients

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import java.time.Duration

/**
 * Common configuration properties for the OpenAI clients.
 */

@ConfigurationProperties(prefix = "mousike.spring.web.client")
@Configuration
class HttpClientProperties {
    /**
     * Maximum time to wait for a connection.
     */
    val connectTimeout: Duration = Duration.ofMinutes(60)

    /**
     * Maximum time to wait for a response.
     */
     val readTimeout: Duration = Duration.ofMinutes(60)

    /**
     * SSL certificate bundle to use to establish a secure connection.
     */
     val sslBundle: String? = null

    /**
     * Whether to log requests.
     */
     val logRequests:Boolean = false

    /**
     * Whether to log responses.
     */
     val logResponses :Boolean= false

}