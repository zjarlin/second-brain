package com.addzero.common.kt_util

fun <E> List<E>.removeAt(i: Int) {
    this.toMutableList().removeAt(i)
}
 fun <T> List<T>.add(currentNode: T) {
    this.toMutableList().add(currentNode)

}

fun <E> List<E>.addAll(allLeafNodes: List<E>) {
    this.toMutableList().addAll(allLeafNodes)
}
 fun <E> List<E>.removeIf(predicate: (E) -> Boolean) {
    this.toMutableList().removeIf(predicate)
}

/**
 * 根据多个比较条件计算两个集合的差集。
 *
 * @param T 集合元素的类型
 * @param other 另一个集合
 * @param predicates 多个 lambda 表达式，用于自定义比较规则
 * @return 差集（当前集合中不存在于 `other` 集合中的元素）
 */
fun <T> Collection<T>.differenceBy(
    other: Collection<T>,
    vararg predicates: (T, T) -> Boolean,
): List<T> {
    return this.filter { item ->
        other.none { otherItem ->
            predicates.all { predicate -> predicate(item, otherItem) }
        }
    }
}

/**
 * 根据多个比较条件计算两个集合的交集。
 *
 * @param T 集合元素的类型
 * @param other 另一个集合
 * @param predicates 多个 lambda 表达式，用于自定义比较规则
 * @return 交集（同时存在于两个集合中的元素）
 */
fun <T> Collection<T>.intersectBy(
    other: Collection<T>,
    vararg predicates: (T, T) -> Boolean,
): List<T> {
    return this.filter { item ->
        other.any { otherItem ->
            predicates.all { predicate -> predicate(item, otherItem) }
        }
    }
}



data class Column(
    val columnName: String,
    val dataType: String,
    val comment: String = ""
)

fun main() {
    // 模拟两个表的列定义
    val table1Columns = listOf(
        Column("ID", "BIGINT", "主键"),
        Column("user_name", "VARCHAR", "用户名"),
        Column("age", "INT", "年龄"),
        Column("create_time", "TIMESTAMP", "创建时间")
    )

    val table2Columns = listOf(
        Column("id", "INTEGER", "主键"), // 类型不同
        Column("USER_NAME", "VARCHAR", "用户名"), // 名称大小写不同但类型相同
        Column("AGE", "BIGINT", "年龄"), // 类型不同
        Column("status", "CHAR", "状态") // 新列
    )

    // 找出名称相同但类型不同的列
    val conflictColumns = table1Columns.intersectBy(table2Columns,
        { col1, col2 -> col1.columnName.equals(col2.columnName, ignoreCase = true) }, // 列名相同（不区分大小写）
        { col1, col2 -> !col1.dataType.equals(col2.dataType, ignoreCase = true) }     // 类型不同（不区分大小写）
    )

    println("列名相同但类型不同的列：")
    conflictColumns.forEach { col ->
        // 找到对应的另一个表中的列，展示类型差异
        val otherCol = table2Columns.first {
            it.columnName.equals(col.columnName, ignoreCase = true)
        }
        println("""
            |列名: ${col.columnName}
            |表1类型: ${col.dataType}
            |表2类型: ${otherCol.dataType}
            |""".trimMargin())
    }

    // 找出表1中存在但表2中不存在的列
    val missingColumns = table1Columns.differenceBy(table2Columns,
        { col1, col2 -> col1.columnName.equals(col2.columnName, ignoreCase = true) }
    )

    println("\n表2中缺少的列：")
    missingColumns.forEach { col ->
        println("${col.columnName} (${col.dataType})")
    }

    // 找出表2中新增的列
    val newColumns = table2Columns.differenceBy(table1Columns,
        { col1, col2 -> col1.columnName.equals(col2.columnName, ignoreCase = true) }
    )

    println("\n表2中新增的列：")
    newColumns.forEach { col ->
        println("${col.columnName} (${col.dataType})")
    }
}