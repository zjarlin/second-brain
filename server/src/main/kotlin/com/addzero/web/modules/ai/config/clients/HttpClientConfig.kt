package com.example.demo.modules.ai.config.clients

import org.springframework.lang.Nullable
import org.springframework.util.Assert
import java.time.Duration

/**
 * Options for configuring the HTTP clients used by the models.
 */
data class HttpClientConfig(
    val connectTimeout: Duration,
    val readTimeout: Duration,
    @field:Nullable @param:Nullable val sslBundle: String?,
    val logRequests: Boolean,
    val logResponses: Boolean
) {
    class Builder {
        private var connectTimeout: Duration = Duration.ofSeconds(10)
        private var readTimeout: Duration = Duration.ofSeconds(60)

            private var sslBundle: String? = null
        private var logRequests = false
        private var logResponses = false

        fun connectTimeout(connectTimeout: Duration): Builder {
            this.connectTimeout = connectTimeout
            return this
        }

        fun readTimeout(readTimeout: Duration): Builder {
            this.readTimeout = readTimeout
            return this
        }

        fun sslBundle(sslBundle: String?): Builder {
            this.sslBundle = sslBundle
            return this
        }

        fun logRequests(logRequests: Boolean): Builder {
            this.logRequests = logRequests
            return this
        }

        fun logResponses(logResponses: Boolean): Builder {
            this.logResponses = logResponses
            return this
        }

        fun build(): HttpClientConfig {
            return HttpClientConfig(connectTimeout, readTimeout, sslBundle, logRequests, logResponses)
        }
    }

    init {
        Assert.notNull(connectTimeout, "connectTimeout must not be null")
        Assert.notNull(readTimeout, "readTimeout must not be null")
    }

    companion object {
        fun builder(): Builder {
            return Builder()
        }
    }
}