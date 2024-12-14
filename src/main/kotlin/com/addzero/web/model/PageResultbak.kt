//package com.addzero.web.model
//
//import kotlinx.serialization.SerialName
//import kotlinx.serialization.Serializable
//
//@Serializable
//class PageResultbak<T>(
//    @SerialName("content")
//    val content: List<T> = emptyList(),
//
//    @SerialName("totalElements")
//    val totalElements: Long = 0L,
//
//    @SerialName("totalPages")
//    val totalPages: Int = 0,
//
//    @SerialName("pageNumber")
//    val pageNumber: Int = 0,
//
//    @SerialName("pageSize")
//    val pageSize: Int = 20,
//
//    @SerialName("isFirst")
//    val isFirst: Boolean = true,
//
//    @SerialName("isLast")
//    val isLast: Boolean = true
//) {
//    companion object {
//        fun <T> empty(pageSize: Int = 20): PageResult<T> = PageResult(
//            content = emptyList(),
//            totalElements = 0L,
//            totalPages = 0,
//            pageNumber = 0,
//            pageSize = pageSize,
//            isFirst = true,
//            isLast = true
//        )
//    }
//
//    // 手动实现 toString 方法（如果需要）
//    override fun toString(): String {
//        return "PageResult(content=$content, totalElements=$totalElements, totalPages=$totalPages, " +
//                "pageNumber=$pageNumber, pageSize=$pageSize, isFirst=$isFirst, isLast=$isLast)"
//    }
//
//    // 手动实现 equals 方法（如果需要）
//    override fun equals(other: Any?): Boolean {
//        if (this === other) return true
//        if (other == null || javaClass != other.javaClass) return false
//        other as PageResult<*>
//        return content == other.content &&
//                totalElements == other.totalElements &&
//                totalPages == other.totalPages &&
//                pageNumber == other.pageNumber &&
//                pageSize == other.pageSize &&
//                isFirst == other.isFirst &&
//                isLast == other.isLast
//    }
//
//    // 手动实现 hashCode 方法（如果需要）
//    override fun hashCode(): Int {
//        var result = content.hashCode()
//        result = 31 * result + totalElements.hashCode()
//        result = 31 * result + totalPages
//        result = 31 * result + pageNumber
//        result = 31 * result + pageSize
//        result = 31 * result + isFirst.hashCode()
//        result = 31 * result + isLast.hashCode()
//        return result
//    }
//}