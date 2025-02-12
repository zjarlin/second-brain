//package com.addzero.web.modules.sys.dept
//
//import org.babyfish.jimmer.sql.kt.KSqlClient
//import org.babyfish.jimmer.sql.kt.KTransientResolver
//import org.springframework.stereotype.Component
//
//@Component
//class AssociateUserListResolver( // 构造注入
//    private val sqlClient: KSqlClient
//) : KTransientResolver<Long, List<Long>> {
//    override fun resolve(ids: Collection<Long>): Map<Long, List<Long>> {
//        val deptList = sqlClient.findByIds(Dept::class, ids)
//
//
//        //部门id->关联部门id(父级部门，自己，子孙部门)
//        val deptAssociateDeptIds: MutableMap<Long, List<Long>>  = mutableMapOf()
//
//        deptList.forEach { dept ->
//            val associateDeptIds = mutableListOf<Long>()
//            associateDeptIds += dept.id
//            dept.parent?.id?.let { parentId ->
//                associateDeptIds += parentId
//            }
//            associateDeptIds += dept.children.map { it.id }
//            deptAssociateDeptIds[dept.id] = associateDeptIds
//        }
//
//        //部门Id-> 部门直属用户Id列表
//        val deptUserIdMap = sqlClient.createQuery(User::class) {
//            val flatten = deptAssociateDeptIds.values.flatten()
//            where(table.deptId valueIn flatten)
//            select(table.deptId.asNonNull(), table.id)
//        }.execute().groupBy({ it._1 }, { it._2 })
//
//        return deptAssociateDeptIds.mapValues { (_,deptIdList)->
//            deptIdList.flatMap { deptId->deptUserIdMap[deptId]?: emptyList() }
//        }
//    }
//
//}