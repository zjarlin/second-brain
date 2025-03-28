package com.addzero.web.modules.demo

import com.addzero.dsl.flexible.FlexibleDsl
import org.springframework.http.HttpMethod


@FlexibleDsl
data class Http(
    val url: String,
    val method: HttpMethod,
) {

    @FlexibleDsl
     data class Body(val bb: String) {

        @FlexibleDsl
        data class Json(val bb1: String,val bb3 :Int?) {}
    }


    @FlexibleDsl
    data class Header(val bb2: String) {
    }

}

fun main() {
    val (url, method) = httpFlexible {
        url = "https://api.example.com"
        method = HttpMethod.POST
        bodyFlexible {
            jsonFlexible {
                "name" to "Kotlin"
                "age" to 10
            }
        }
        headerFlexible {
            "Authorization" to "Bearer token"
        }
    }
    println()

}

