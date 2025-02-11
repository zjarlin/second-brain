package com.addzero.common.kt_util

import cn.hutool.core.io.FileUtil
import cn.hutool.core.text.CharSequenceUtil
import cn.hutool.core.util.StrUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap
import java.util.regex.Pattern



/**
 * 检查字符串是否不在列表中（不区分大小写）
 */
infix fun String.ignoreCaseNotIn(collection: Collection<String>): Boolean {
    val b = this ignoreCaseIn collection
    return !b
}

/**
 * 检查字符串是否在列表中（不区分大小写）
 */
infix fun String.ignoreCaseIn(collection: Collection<String>): Boolean =
    collection.any { it.equals(this, ignoreCase = true) }

object JlStrUtil {


    /**
     * 删除字符串中最后一次出现的指定字符。
     * 注意这和removeSuffixifnot有所不同(该方法只是移除最后一个字符,而不是最后出现的字符,例如如最后一个是空格就翻车了)
     *
     * @param str 字符串
     * @param ch 要删除的字符
     * @return 删除指定字符后的字符串
     */
    fun removeLastCharOccurrence(str: String, ch: Char): String {
        if (StrUtil.isBlank(str)) {
            return ""
        }

        val lastIndex = str.lastIndexOf(ch) // 获取指定字符最后一次出现的位置
        return if (lastIndex != -1) {
            // 如果找到了指定字符，则删除它
            str.substring(0, lastIndex) + str.substring(lastIndex + 1)
        } else {
            // 如果没有找到指定字符，则返回原字符串
            str!!
        }
    }


    fun <T> groupBySeparator(lines: List<T>, predicate: (T) -> Boolean): Map<T, List<T>> {
        val separatorIndices = lines.indices.filter { predicate(lines[it]) }
        return separatorIndices.mapIndexed { index, spe ->
            val next = if (index + 1 < separatorIndices.size) {
                separatorIndices[index + 1]
            } else {
                lines.size // 如果没有下一个分隔符，取行的总数
            }

            val subList = lines.subList(spe + 1, next)
            lines[spe] to subList // 使用 Pair 进行配对
        }.toMap()
    }

    /**
     *提取markdown代码块中的内容
     * @param [markdown]
     * @return [String]
     */
    fun extractMarkdownBlockContent(markdown: String): String {
        val regex = Regex("```\\w*\\s*(.*?)\\s*```", RegexOption.DOT_MATCHES_ALL)
        val matchResult = regex.find(markdown)
        return matchResult?.groups?.get(1)?.value?.trim() ?: ""
    }

}

fun String?.isBlank(): Boolean {
    return StrUtil.isBlank(this)
}

fun String?.isNotBlank(): Boolean {
    return StrUtil.isNotBlank(this)
}

/**
 * 扩展函数：移除重复符号
 */
fun String?.removeDuplicateSymbol(duplicateElement: String): String {
    if (this.isNullOrEmpty() || duplicateElement.isEmpty()) {
        return this ?: ""
    }

    val sb = StringBuilder()
    var previous = "" // 初始化前一个元素，用于比较
    var i = 0

    while (i < this.length) {
        val elementLength = duplicateElement.length
        if (i + elementLength <= this.length && this.substring(i, i + elementLength) == duplicateElement) {
            if (previous != duplicateElement) {
                sb.append(duplicateElement)
                previous = duplicateElement
            }
            i += elementLength
        } else {
            sb.append(this[i])
            previous = this[i].toString()
            i++
        }
    }
    return sb.toString()
}

/**
 * 扩展函数：清理多余的char
 */
fun String?.removeDuplicateSymbol(symbol: Char): String {
    if (StrUtil.isBlank(this)) {
        return ""
    }
    val sb = StringBuilder()
    var prevIsSymbol = false

    for (c in this!!.toCharArray()) {
        if (c == symbol) {
            if (!prevIsSymbol) {
                sb.append(c)
                prevIsSymbol = true
            }
        } else {
            sb.append(c)
            prevIsSymbol = false
        }
    }
    return sb.toString()
}

/**
 * 扩展函数：提取路径部分
 */
fun String.getPathFromRight(n: Int): String? {
    val parts = this.split(".").filter { it.isNotEmpty() }

    if (parts!!.size < n) {
        return this // 输入字符串中的路径部分不足n个，返回整个输入字符串
    }

    return parts.dropLast(n).joinToString(".")
}

/**
 * 扩展函数：添加前后缀
 */
fun String?.makeSurroundWith(fix: String?): String? {
    return this?.let { StrUtil.addPrefixIfNot(it, fix).let { StrUtil.addSuffixIfNot(it, fix) } }
}

/**
 * 扩展函数：提取REST URL
 */
fun String?.getRestUrl(): String {
    if (CharSequenceUtil.isBlank(this)) {
        return ""
    }
    val pattern = Pattern.compile(".*:\\d+(/[^/]+)(/.*)")
    val matcher = pattern.matcher(this)

    return if (matcher.matches() && matcher.groupCount() > 1) {
        matcher.group(2)
    } else {
        ""
    }
}

/**
 * 扩展函数：用HTML P标签包裹
 */
fun String?.makeSurroundWithHtmlP(): String? {
    if (StrUtil.isBlank(this)) {
        return ""
    }
    return StrUtil.addPrefixIfNot(this, "<p>").let { StrUtil.addSuffixIfNot(it, "</p>") }
}

/**
 * 扩展函数：检查是否包含中文
 */
fun String?.containsChinese(): Boolean {
    if (StrUtil.isBlank(this)) {
        return false
    }
    val pattern = Pattern.compile("[\\u4e00-\\u9fa5]")
    val matcher = pattern.matcher(this)
    return matcher.find()
}

fun String?.lowerCase(): String {
    if (StrUtil.isBlank(this)) {
        return ""
    }
    val lowerCase = this.lowerCase()
    return lowerCase

}

fun String?.cleanBlank(): String {
    if (StrUtil.isBlank(this)) {
        return ""
    }
    return StrUtil.cleanBlank(this)

}

fun String?.addPrefixIfNot(prefix: String): String {
    if (StrUtil.isBlank(this)) {
        return prefix
    }
    return StrUtil.addPrefixIfNot(this, prefix)

}


fun CharSequence.containsAny(vararg testStrs: CharSequence): Boolean {
    val containsAny = StrUtil.containsAny(this, *testStrs)
    return containsAny
}

fun CharSequence.containsAnyIgnoreCase(vararg testStrs: CharSequence): Boolean {
    val containsAny = StrUtil.containsAnyIgnoreCase(this, *testStrs)
    return containsAny
}
fun String.getParentPathAndmkdir(childPath: String): String {
    val parent1 = FileUtil.getParent(this, 1)
    //            val parent2 = FileUtil.getParent(filePath, 2)
    //            val parent3 = FileUtil.getParent(filePath, 0)
    val mkParentDirs = FileUtil.mkdir("$parent1/$childPath")
    //            val canonicalPath = virtualFile.canonicalPath
    //            val parent = psiFile!!.parent
    val filePath1 = mkParentDirs.path
    return filePath1
}


fun CharSequence.toCamelCase(): String {
    return StrUtil.toCamelCase(this)
}

fun CharSequence.removeAny(vararg testStrs: CharSequence): String {
    return StrUtil.removeAny(this, *testStrs)
}

/**
 * 删除空格或者引号
 * @param [testStrs]
 * @return [String]
 */
fun CharSequence.removeBlankOrQuotation(): String {
    return StrUtil.removeAny(this, " ","\"")
}


fun CharSequence.toUnderlineCase(): String {
    val toUnderlineCase = StrUtil.toUnderlineCase(this)
    return toUnderlineCase
}

fun String.equalsIgnoreCase(string: String): Boolean {
    return StrUtil.equalsIgnoreCase(this, string)
}