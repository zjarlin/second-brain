package com.addzero.web.infra.exception_advice

import io.swagger.v3.oas.annotations.media.Schema

/**
 * 业务异常
 */
@Schema(description = "业务异常")
open class BizException(
    @field:Schema(description = "用户提示", example = "操作成功！")
    val msg: String,

    @field:Schema(description = "错误码")
    val code: Int,

    ) : RuntimeException("错误码：[$code]，错误信息：[$msg]") {

    constructor(errorEnum: ErrorEnum) : this(
        msg = errorEnum.msg,
        code = errorEnum.code
    )
    constructor(msg: String) : this(
        msg = msg
        , code = 500
    )
}