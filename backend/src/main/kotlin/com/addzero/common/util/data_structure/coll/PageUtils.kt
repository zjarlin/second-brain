import java.util.function.BiConsumer
import java.util.stream.Collectors
import kotlin.math.max

fun <T, Page> list2Page(
    list: List<T>,
    pageNum: Int,
    pageSize: Int,
    setPageFun: BiConsumer<Page, Int>,
    setRecordFun: BiConsumer<Page, List<T>>,
    setTotalFun: BiConsumer<Page, Int>,
    createPageFun: () -> Page // 用于构建新的 Page 对象
): Page {
    // 截取分页后的数据
    val data = list.stream()
        .skip((max(1.0, pageNum.toDouble()) - 1).toLong() * pageSize)
        .limit(pageSize.toLong())
        .collect(Collectors.toList())

    val totalRecords = list.size
    // 总页数
    val totalPages = (totalRecords + pageSize - 1) / pageSize

    // 创建新的 Page 对象
    val page = createPageFun()

    // 设置当前页
    setPageFun.accept(page, pageNum)
    // 设置当前页的数据
    setRecordFun.accept(page, data)
    // 设置总记录数
    setTotalFun.accept(page, totalRecords)

    return page
}