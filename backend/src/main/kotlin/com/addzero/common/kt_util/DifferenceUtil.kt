package com.addzero.common.kt_util

/**
 * 根据多个比较条件计算两个集合的差集。
 */
fun <T> Collection<T>.differenceBy(
    predicates: Array<(T, T) -> Boolean>,
    other: Collection<T>
): List<T> {
    return this.filter { item ->
        other.none { otherItem ->
            predicates.all { predicate -> predicate(item, otherItem) }
        }
    }
}

data class Person(val id: Int, val name: String, val age: Int)

fun main() {
    // 1. 测试简单类型：整数
    val list1 = listOf(1, 2, 3, 4, 5)
    val list2 = listOf(3, 4, 5, 6, 7)

    println("1. 整数测试:")
    val intPredicate = arrayOf<(Int, Int) -> Boolean>({ a, b -> a == b })
    println(list1.differenceBy(intPredicate, list2))
    // 预期输出: [1, 2]

    // 2. 测试字符串，忽略大小写
    val strList1 = listOf("Apple", "Banana", "cherry", "Date")
    val strList2 = listOf("banana", "Cherry", "Elderberry")

    println("\n2. 字符串测试（忽略大小写）:")
    val strPredicate = arrayOf<(String, String) -> Boolean>(
        { a, b -> a.equals(b, ignoreCase = true) }
    )
    println(strList1.differenceBy(strPredicate, strList2))
    // 预期输出: [Apple, Date]

    // 3. 测试复杂对象：Person
    val persons1 = listOf(
        Person(1, "Alice", 25),
        Person(2, "Bob", 30),
        Person(3, "Charlie", 35),
        Person(4, "David", 40)
    )
    val persons2 = listOf(
        Person(2, "Bob", 31),
        Person(3, "Charlie", 35),
        Person(5, "Eve", 45)
    )

    println("\n3. 复杂对象测试（仅比较 id）:")
    val idPredicate = arrayOf<(Person, Person) -> Boolean>(
        { a, b -> a.id == b.id }
    )
    println(persons1.differenceBy(idPredicate, persons2))
    // 预期输出: [Person(id=1...), Person(id=4...)]

    println("\n4. 复杂对象测试（比较 id 和 name）:")
    val idAndNamePredicates = arrayOf<(Person, Person) -> Boolean>(
        { a, b -> a.id == b.id },
        { a, b -> a.name == b.name }
    )
    println(persons1.differenceBy(idAndNamePredicates, persons2))
    // 预期输出: [Person(id=1...), Person(id=4...)]

    println("\n5. 复杂对象测试（比较 id、name 和 age）:")
    val allPredicates = arrayOf<(Person, Person) -> Boolean>(
        { a, b -> a.id == b.id },
        { a, b -> a.name == b.name },
        { a, b -> a.age == b.age }
    )
    println(persons1.differenceBy(allPredicates, persons2))
    // 预期输出: [Person(id=1...), Person(id=2...), Person(id=4...)]

    // 6. 测试空集合
    val emptyList = emptyList<Int>()
    println("\n6. 空集合测试:")
    println(list1.differenceBy(intPredicate, emptyList))
    // 预期输出: [1, 2, 3, 4, 5]
    println(emptyList.differenceBy(intPredicate, list1))
    // 预期输出: []

    // 7. 测试自定义比较逻辑
    val numbers1 = listOf(1, 4, 9, 16, 25)
    val numbers2 = listOf(1, 2, 3, 4, 5)
    println("\n7. 自定义比较逻辑测试（比较平方根）:")
    val sqrtPredicate = arrayOf<(Int, Int) -> Boolean>(
        { a, b -> Math.sqrt(a.toDouble()) == b.toDouble() }
    )
    println(numbers1.differenceBy(sqrtPredicate, numbers2))
    // 预期输出: [9, 16, 25]

    // 8. 测试多个复杂条件
    println("\n8. 复杂条件组合测试:")
    val complexNumbers1 = listOf(1, 4, 9, 16, 25, 36)
    val complexNumbers2 = listOf(1, 2, 3, 4, 5, 7)
    val complexPredicates = arrayOf<(Int, Int) -> Boolean>(
        { a, b -> Math.sqrt(a.toDouble()) == b.toDouble() },
        { a, b -> a % 2 == b % 2 } // 同为奇数或偶数
    )
    println(complexNumbers1.differenceBy(complexPredicates, complexNumbers2))
    // 预期输出: [9, 16, 25, 36]

    // 9. 测试边界情况
    println("\n9. 边界情况测试:")
    // 空谓词数组
    val emptyPredicates = arrayOf<(Int, Int) -> Boolean>()
    println(list1.differenceBy(emptyPredicates, list2))
    // 预期输出: [1, 2, 3, 4, 5]
}