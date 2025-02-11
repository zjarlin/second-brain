data class ColumnMeta(
    val id: String,
    val tableName: String,
    val columnName: String,
    val columnType: String,
    val columnLength: String?,
    val nullableFlag: String,
)