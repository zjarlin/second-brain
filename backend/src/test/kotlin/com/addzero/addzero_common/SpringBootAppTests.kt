package com.addzero.addzero_common

import cn.hutool.extra.spring.SpringUtil
import com.addzero.web.jdbc.metadata.ColumnMetadata
import com.addzero.web.jdbc.metadata.DatabaseMetadataService
import com.addzero.web.modules.second_brain.note.BizNote
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.babyfish.jimmer.sql.runtime.Customizer
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import kotlin.reflect.full.declaredMemberProperties

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class SpringBootAppTests(
    val kSqlClient: KSqlClient,
) {
    @Autowired
    private lateinit var databaseMetadataService: DatabaseMetadataService

    //    @Autowired
//    private lateinit var postgresMetadataService: PostgresMetadataService
    @Test
    fun testMeta() {
        val declaredMemberProperties = ColumnMetadata::class.declaredMemberProperties

    }


    @Test
    fun 测试数据库元数据(): Unit {
        val columnsMetadata = databaseMetadataService.getColumnsMetadata()
        val foreignKeysMetadata = databaseMetadataService.getForeignKeysMetadata()
        val primaryKeysMetadata = databaseMetadataService.getPrimaryKeysMetadata()
        println()
    }


//    @Test
//    fun djoaisdjoij(): Unit {
//        val 测试笔记upsert = bizNoteController.测试笔记upsert()
//        println(测试笔记upsert)
//    }

    @Test
    fun `test biz notes upsert`() {
//        val save = kSqlClient.save(BizNote {
//            id = 1111222
//            title = "test title"
//            content = "test content"
//        })
        val createQuery = kSqlClient.createQuery(BizNote::class) {
            select(table)
        }.execute()
        println()


    }


    @Test
    fun contextLoads() {
        val java = Customizer::class.java
        val bean = SpringUtil.getBean(java)
        println(bean)
//        val dbMetaInfos = postgresMetadataService.getDbMetaInfos()
//        println(dbMetaInfos)
    }


}
