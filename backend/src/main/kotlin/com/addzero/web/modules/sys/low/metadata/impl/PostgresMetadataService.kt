package com.addzero.web.modules.sys.low.metadata.impl

import ColumnMeta
import cn.hutool.core.map.MapUtil
import com.addzero.web.modules.sys.low.metadata.DbEnum
import com.addzero.web.modules.sys.low.metadata.MetadataService
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service

@Service
class PostgresMetadataService(private val jdbcTemplate: JdbcTemplate) : MetadataService {
    override fun getDbMetaInfos(): List<ColumnMeta> {
        val trimIndent = """
           SELECT
    md5(table_name || '.' || column_name) AS id,  -- 使用 MD5 哈希生成唯一 ID
    table_name,
    column_name,
    CASE
        WHEN data_type = 'character varying' THEN
            'varchar'  -- 只返回类型，不包括长度
        WHEN data_type = 'character' THEN
            'char'  -- 只返回类型，不包括长度
        ELSE
            data_type  -- 其他数据类型保持原样
    END AS column_type,
    character_maximum_length AS column_length,  -- 独立出字段长度
    is_nullable AS nullable_flag
FROM
    information_schema.columns
WHERE
    table_schema = 'public'  -- 根据需要修改 schema
ORDER BY
    table_name, ordinal_position;
        """.trimIndent()
        val queryForList = jdbcTemplate.queryForList(trimIndent)
        val map = queryForList.map {
            val id = MapUtil.getStr(it, "id")
            val tableName = MapUtil.getStr(it, "table_name")
            val columnName = MapUtil.getStr(it, "column_name")
            val columnType = MapUtil.getStr(it, "column_type")
            val columnLength = MapUtil.getStr(it, "column_length")
            val nullableFlag = MapUtil.getStr(it, "nullable_flag")
            ColumnMeta(id, tableName, columnName, columnType, columnLength, nullableFlag)
        }
        return map
    }

    override val dbEnum: DbEnum get() = DbEnum.POSTGRESQL


}