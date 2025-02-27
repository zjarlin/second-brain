package com.addzero.web.jdbc.metadata

import io.swagger.v3.oas.annotations.media.Schema

data class ColumnMetadata(
    @Schema(description = "MD5哈希生成的唯一ID")
    val md5: String,
    @Schema(description = "表名")
    val tableName: String,
    @Schema(description = "列名")
    val columnName: String,
    @Schema(description = "列类型")
    val columnType: String,
    @Schema(description = "列长度")
    val columnLength: Int?,
    @Schema(description = "是否可空")
    val nullableFlag: String
)
