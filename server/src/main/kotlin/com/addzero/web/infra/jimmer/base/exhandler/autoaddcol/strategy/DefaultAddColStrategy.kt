package com.addzero.web.infra.jimmer.base.exhandler.autoaddcol.strategy

import com.addzero.web.infra.jimmer.base.exhandler.autoaddcol.AutoAddColStrategy
import fail
import org.springframework.stereotype.Component

@Component
class DefaultAddColStrategy : AutoAddColStrategy {
    override fun canHandle(message: String?, causeMessage: String?): Boolean = true

    override fun handle(message: String?, causeMessage: String?): Any? {
        return causeMessage?.fail()
    }
}