package com.addzero.web.ui.hooks.table.entity

import androidx.compose.runtime.Composable
import kotlin.reflect.KClass

/**
 * 表格列定义接口
 * @param E 数据实体类型
 */
interface IAddColumn<E : Any> {
    /** 字段标题 */
    var title: String
    
    /** 字段名称 */
    var fieldName: String
    
    /** 实体类类型 */
    var clazz: KClass<E>?
    
    /** 获取列数据的函数 */
    val getFun: (E) -> Any?
    
    /** 设置值的函数 */
    val setFun: (E?, IAddColumn<E>, Any?) -> E?
    
    /** 是否必填 */
    val required: Boolean
    
    /** 选项列表 */
    val options: List<Pair<Any, String>>
    
    /** 占位文本 */
    val placeholder: String
    
    /** 验证错误提示信息 */
    val errorMessage: String
    
    /** 渲染类型 */
    val renderType: RenderType
    
    /** 自定义列表渲染函数 */
    var customRender: @Composable (E) -> Unit
    
    /** 验证函数 */
    val validator: (E?) -> Boolean
}