//package com.addzero.web.infra.controller_advice
//
//import com.addzero.web.infra.exception_advice.ErrorEnum
//
//data class R<T>(
//    val code: Int = 200,
//    val msg: String? = "success",
//    val data: T? = null
//) {
//    companion object {
//        fun <T> success(data: T): R<T> {
//            return R(data = data)
//        }
//
//        fun <T> success(msg: String?,data: T): R<T> {
//            return R(code = 200, msg = msg,data = data)
//        }
//
//        fun <T> error(message: String? = "error"): R<T> {
//            return R(code = 500, msg = message)
//        }
//
//        fun <T> error(code: Int, message: String?): R<T> {
//            return R(code = code, msg = message)
//        }
//
//        fun error(errorEnum: ErrorEnum): R<String> {
//            return error(errorEnum.code, errorEnum.msg)
//        }
//    }
//}