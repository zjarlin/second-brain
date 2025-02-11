package com.addzero.web.modules.sys.low.metadata

import ColumnMeta
import com.addzero.web.infra.jimmer.dynamicdatasource.DataSourceContextHolder

interface MetadataService {
    fun getDbMetaInfos(): List<ColumnMeta>
    val dbEnum: DbEnum
    val support: Boolean get() = DataSourceContextHolder.getKey() == dbEnum.key
}