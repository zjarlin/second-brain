//package com.addzero.web.ui.hooks.table.entity
//
//import com.addzero.common.kt_util.containsAnyIgnoreCase
//
///**
// * 文本列渲染类型扩展
// */
//fun <E : Any> IColumn<E, String>.getRenderType(): RenderType {
//    return when {
//        getFieldName().containsAnyIgnoreCase("password") -> RenderType.TEXT_PASSWORD
//        getFieldName().containsAnyIgnoreCase("email") -> RenderType.TEXT_EMAIL
//        getFieldName().containsAnyIgnoreCase("phone", "mobile") -> RenderType.TEXT_PHONE
//        getFieldName().containsAnyIgnoreCase("url", "link") -> RenderType.TEXT_URL
//        getFieldName().containsAnyIgnoreCase("code") -> RenderType.TEXT_CODE
//        getFieldName().containsAnyIgnoreCase("html") -> RenderType.TEXT_HTML
//        getFieldName().containsAnyIgnoreCase("tag") -> RenderType.TEXT_TAG
//        getFieldName().contains("description") || getFieldName().contains("content") || getFieldName().contains("text") -> RenderType.TEXT_AREA
//        else -> RenderType.TEXT
//    }
//}
//
///**
// * 数字列渲染类型扩展
// */
//fun <E : Any> IColumn<E, Number>.getRenderType(): RenderType {
//    return when {
//        getFieldName().containsAnyIgnoreCase("money", "price", "amount") -> RenderType.NUMBER_MONEY
//        getFieldName().containsAnyIgnoreCase("currency") -> RenderType.NUMBER_CURRENCY
//        getFieldName().containsAnyIgnoreCase("percent", "percentage") -> RenderType.NUMBER_PERCENT
//        getFieldName().containsAnyIgnoreCase("bar", "progress") -> RenderType.NUMBER_BAR
//        else -> RenderType.NUMBER
//    }
//}
//
///**
// * 布尔列渲染类型扩展
// */
//fun <E : Any> IColumn<E, Boolean>.getRenderType(): RenderType {
//    return when {
//        getFieldName().containsAnyIgnoreCase("switch") -> RenderType.BOOL_SWITCH
//        else -> RenderType.BOOL_CHECKBOX
//    }
//}
//
///**
// * 日期列渲染类型扩展
// */
//fun <E : Any> IColumn<E, java.util.Date>.getRenderType(): RenderType {
//    return when {
//        getFieldName().containsAnyIgnoreCase("time") -> RenderType.DATE_TIME
//        else -> RenderType.DATE
//    }
//}
//
///**
// * 列表列渲染类型扩展
// */
//fun <E : Any, T : Any> IColumn<E, List<T>>.getRenderType(): RenderType {
//    return when {
//        getFieldName().containsAnyIgnoreCase("tree") -> RenderType.SELECT_TREE
//        getFieldName().containsAnyIgnoreCase("cascade") -> RenderType.SELECT_CASCADE
//        getFieldName().containsAnyIgnoreCase("tag") -> RenderType.TEXT_TAG
//        getOptions() != null -> RenderType.SELECT_MULTI
//        else -> RenderType.SELECT
//    }
//}
//
///**
// * 自定义列渲染类型扩展
// */
//fun <E : Any, P : Any> IColumn<E, P>.getRenderType(): RenderType {
//    return when {
//        getFieldName().containsAnyIgnoreCase("computed") -> RenderType.COMPUTED
//        getFieldName().containsAnyIgnoreCase("auto") -> RenderType.SELECT_AUTO
//        getFieldName().containsAnyIgnoreCase("custom") -> RenderType.CUSTOM
//        else -> {RenderType.TEXT}
//    }
//}