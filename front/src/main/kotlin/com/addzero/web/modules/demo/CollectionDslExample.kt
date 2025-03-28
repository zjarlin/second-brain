package com.addzero.web.modules.demo

import com.addzero.Dsl

// 使用isCollection=true标记为集合类型
@Dsl(isCollection = true)
//@Dsl
data class Item(
    val id: String,
    val name: String,
    val value: Int
)

// 自定义DSL函数名称
@Dsl(value = "product", isCollection = true)
//@Dsl
data class Product(
    val id: String,
    val name: String,
    val price: Double
)

fun main() {
    // 使用单个Item的DSL
    val singleItem = item {
        id = "1"
        name = "测试项目"
        value = 100
    }
    println("单个Item: $singleItem")

    // 使用Item集合的DSL
    val itemList = items {
        item {
            id = "1"
            name = "项目1"
            value = 100
        }
        item {
            id = "2"
            name = "项目2"
            value = 200
        }
        item {
            id = "3"
            name = "项目3"
            value = 300
        }
    }
    println("Item集合大小: ${itemList.size}")

    // 使用自定义名称的Product集合DSL
    val productList = products {
        product {
            id = "p1"
            name = "产品1"
            price = 99.9
        }
        product {
            id = "p2"
            name = "产品2"
            price = 199.9
        }
    }
    println("Product集合大小: ${productList.size}")
}
