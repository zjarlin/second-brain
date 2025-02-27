package com.addzero.web.ui.hooks.form

/**
 * 表单组件类型枚举
 */
enum class ComponentType {
    /** 单行文本框 */
    TEXT,
    /** 多行文本框 */
    TEXTAREA,
    /** 日期选择器 */
    DATE,
    /** 时间选择器 */
    DATETIME,
    /** 文件上传 */
    UPLOAD,
    /** 树形选择器 */
    TREE_SELECT,
    /** 级联选择器 */
    CASCADE_SELECT,
    /** 自动完成 */
    AUTO_COMPLETE
}