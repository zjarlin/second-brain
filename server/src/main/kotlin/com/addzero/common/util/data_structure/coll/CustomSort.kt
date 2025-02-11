package com.addzero.common.util.data_structure.coll

import java.util.*
import java.util.function.Function
import java.util.stream.Collectors

object CustomSort {
    @JvmStatic
    fun main(args: Array<String>) {
        val items = Arrays.asList(
            Item("客厅", "1"),
            Item("餐厅", "2"),
            Item("卫生间", "3"),
            Item("主卧", "4"),
            Item("次卧", "5")
        )
        val field1Reference: List<String> = mutableListOf("客厅", "餐厅", "卫生间", "主卧", "次卧", "书房")
        val field2Reference: List<String> = mutableListOf("卫生间", "主卧", "次卧", "餐厅", "客厅")

        val sortedItemsField1 = customSort<Item, String>( items, { it.field1 }, field2Reference, true )
        println(sortedItemsField1)
    }

    fun <T, S> customSort(
        inputList: List<T>,
        getSortField: Function<T, S>,
        needList: List<S>,
        useEquals: Boolean
    ): List<T> {
        val indexMap = createIndexMap(needList)

        val customComparator = Comparator { item1: T, item2: T ->
            val field1Value1 = getSortField.apply(item1)
            val field1Value2 = getSortField.apply(item2)

            val score1 = indexMap.getOrDefault(field1Value1, Int.MAX_VALUE)
            val score2 = indexMap.getOrDefault(field1Value2, Int.MAX_VALUE)
            Integer.compare(score1, score2)
        }

        return inputList.stream()
            .sorted(customComparator)
            .collect(Collectors.toList())
    }

    private fun <S> createIndexMap(referenceList: List<S>): Map<S, Int> {
        val indexMap: MutableMap<S, Int> = HashMap()
        for (i in referenceList.indices) {
            indexMap[referenceList[i]] = i
        }
        return indexMap
    }

    private class Item(val field1: String, val field2: String) {
        override fun toString(): String {
            return "Field1: $field1, Field2: $field2"
        }
    }
}