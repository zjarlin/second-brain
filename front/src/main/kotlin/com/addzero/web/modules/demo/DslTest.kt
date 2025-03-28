package com.addzero.web.modules.demo

import androidx.compose.runtime.Composable
import com.addzero.dsl.Dsl
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

// 基础数据类型测试
@Dsl
data class BasicTypes(
    val string: String,
    val int: Int,
    val long: Long,
    val double: Double,
    val boolean: Boolean,
    val dateTime: LocalDateTime
)

// 可空类型测试
@Dsl
data class NullableTypes(
    val nullableString: String?,
    val nullableInt: Int?,
    val nullableList: List<String>?
)

// 集合类型测试
@Dsl(genCollectionDslBuilder = true)
data class CollectionTypes(
    val list: List<String>,
    val mutableList: MutableList<Int>,
    val set: Set<Double>,
    val map: Map<String, Int>
)

// 函数类型测试
@Dsl
data class FunctionTypes(
    val noParams: () -> Unit,
    val oneParam: (Int) -> String,
    val twoParams: (String, Int) -> Boolean,
    val nullableFunction: ((Int) -> Unit)?
)

// 嵌套类测试

// 自定义名称测试
@Dsl(value = "customName")
data class CustomNameTest(
    val value: String
)

// 前缀后缀移除测试
@Dsl(removePrefix = "Test", removeSuffix = "Entity")
data class TestUserEntity(
    val id: Long,
    val name: String
)


@Dsl
data class User4(
    val id: String,
    val name: String,
    val age: Int,
    val age1: Double,
    val age2: Long,
    val date1: LocalDateTime,
    val date2: LocalDate,
    val date3: LocalTime,
    val date4: Date,
    val testPropty1: Byte,
    val testPropty2: Short,
    val testPropty3: Float,
    val testPropty4: Double,
    val testPropty5: Char,
    val testPropty6: String?,
    val testPropty7: Int?,
    val testPropty8: Long?,
    val self: com.addzero.web.modules.demo.User4?,
    val children: List<User4>,
    val onChange: @Composable (String) -> Unit
) {

    var testPropty9: String? = ""
}

//@Dsl
@Dsl
class User5 {
    var id: String = ""
    var name: String = ""
    var age: Int = 1
    var age1: Double = 2.0
    var age2: Long = 3
    var date1: LocalDateTime = LocalDateTime.now()
    var date2: LocalDate = LocalDate.now()
    var date3: LocalTime = LocalTime.now()
    var date4: Date = Date()
    var testPropty1: Byte = 0.toByte()
    var testPropty2: Short = 0.toShort()
    var testPropty3: Float = 0f
    var testPropty4: Double = 0.0
    var testPropty5: Char = ' '
    var testPropty6: String? = null
    var testPropty7: Int? = null
    var testPropty8: Long? = null
    var children: List<User5> = emptyList()
    var self: User5 = User5()
}


fun main() {
    // 基础类型测试
    val basic = basicTypes {
        string = "test"
        int = 42
        long = 123L
        double = 3.14
        boolean = true
        dateTime = LocalDateTime.now()
    }
    println("Basic types: $basic")

    // 可空类型测试
    val nullable = nullableTypes {
        nullableString = null
        nullableInt = 10
        nullableList = listOf("a", "b")
    }
    println("Nullable types: $nullable")

    // 集合类型测试
    val collections = collectionTypes {
        list = listOf("one", "two")
        mutableList = mutableListOf(1, 2, 3)
        set = setOf(1.0, 2.0)
        map = mapOf("key" to 1)
    }
    println("Collection types: $collections")

    // 集合构建器测试
    val collectionsList = collectionTypess {
        collectionTypes {
            list = listOf("a")
            mutableList = mutableListOf(1)
            set = setOf(1.0)
            map = mapOf("a" to 1)
        }
        collectionTypes {
            list = listOf("b")
            mutableList = mutableListOf(2)
            set = setOf(2.0)
            map = mapOf("b" to 2)
        }
    }
    println("Collections list: $collectionsList")

    // 函数类型测试
    val functions = functionTypes {
        noParams = { println("No params") }
        oneParam = { it.toString() }
        twoParams = { str, num -> str.length == num }
        nullableFunction = null
    }
    println("Function types: $functions")

    // 嵌套类测试


    // 自定义名称测试
    val custom = customName {
        value = "custom"
    }
    println("Custom name: $custom")

    // 前缀后缀移除测试
    val user = user {

    }
    println("User: $user")
}
