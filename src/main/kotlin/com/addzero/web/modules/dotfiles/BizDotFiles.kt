import com.addzero.web.ui.components.table.ColumnName
import kotlinx.serialization.Serializable

//@Serializable
data class BizDotFiles(
   /** ID */
   val id: String = "",

   /** 操作系统 */
   @field:ColumnName("操作系统")
   val osType: String = "",

   /** 系统架构 */
   @field:ColumnName("系统架构")
   val osStructure: String = "",

   /** 定义类型 */
   @field:ColumnName("定义类型")
   val defType: String = "",

   /** 名称 */
   @field:ColumnName("名称")
   val name: String = "",

   /** 值 */
   @field:ColumnName("值")
   val value: String = "",

   /** 注释 */
   @field:ColumnName("注释")
   val describtion: String? = "",

   /** 状态 */
   @field:ColumnName("状态")
   val status: String = "",

   /** 文件地址 */
   @field:ColumnName("文件地址")
   val fileUrl: String = "",

   /** 创建时间 */
   @field:ColumnName("创建时间")
   val createTime: String = "",

   /** 更新时间 */
   @field:ColumnName("更新时间")
   val updateTime: String = "",

   /** 创建人 */
   @field:ColumnName("创建人")
   val createdBy: String = "",

   /** 更新人 */
   @field:ColumnName("更新人")
   val updatedBy: String = ""
)