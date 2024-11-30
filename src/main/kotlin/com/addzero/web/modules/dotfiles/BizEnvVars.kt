package com.addzero.web.modules.dotfiles

import kotlinx.serialization.Serializable

@Serializable
data class BizEnvVars(
    /** ID */
    val id: String,

    /** 操作系统 */
    val osType: String,

    /** 系统架构 */
    val osStructure: String,

    /** 定义类型 */
    val defType: String,

    /** 名称 */
    val name: String,

    /** 值 */
    val value: String,

    /** 注释 */
    val describtion: String?,

    /** 状态 */
    val status: String,

    /** 文件地址 */
    val fileUrl: String,

    /** 创建时间 */
    val createdAt: Long,

    /** 更新时间 */
    val updatedAt: Long,

    /** 创建人 */
    val createdBy: String,

    /** 更新人 */
    val updatedBy: String
)