package com.addzero.web.ui.lowcode.examples

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.web.ui.hooks.table.entity.OptionItem
import com.addzero.web.ui.hooks.table.entity.RenderType
import com.addzero.web.ui.lowcode.forms.EditForm
import com.addzero.web.ui.lowcode.forms.SearchForm
import com.addzero.web.ui.lowcode.metadata.FieldMetadata
import com.addzero.web.ui.lowcode.table.DataTable

// 示例数据类
data class Product(
    val id: Long = 0,
    val name: String = "",
    val category: String = "",
    val price: Double = 0.0,
    val stock: Int = 0,
    val isActive: Boolean = true,
    val description: String = ""
)

/**
 * 完整的CRUD示例
 * 包含搜索区、表格和表单
 */
@Composable
fun CompleteCrudExample() {
    // 状态
    var products by remember {
        mutableStateOf(
            listOf(
                Product(1, "笔记本电脑", "电子产品", 5999.0, 100, true, "高性能笔记本电脑"),
                Product(2, "智能手机", "电子产品", 3999.0, 200, true, "最新款智能手机"),
                Product(3, "无线耳机", "配件", 999.0, 300, true, "高音质无线耳机"),
                Product(4, "机械键盘", "配件", 499.0, 150, true, "机械轴体键盘"),
                Product(5, "显示器", "电子产品", 1999.0, 80, true, "高清显示器")
            )
        )
    }
    var filteredProducts by remember { mutableStateOf(products) }
    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    var isAddingNew by remember { mutableStateOf(false) }
    var searchProduct by remember { mutableStateOf(Product()) }
    
    // 定义字段元数据
    val fields = listOf(
        FieldMetadata<Product>(
            name = "id",
            title = "ID",
            renderType = RenderType.NUMBER,
            getValue = { it.id },
            setValue = { product, value -> product.copy(id = (value as? Number)?.toLong() ?: 0) },
            showInSearch = false,
            showInForm = false
        ),
        FieldMetadata(
            name = "name",
            title = "产品名称",
            renderType = RenderType.TEXT,
            getValue = { it.name },
            setValue = { product, value -> product.copy(name = value as? String ?: "") },
            required = true,
            placeholder = "请输入产品名称",
            showInSearch = true,
            showInTable = true,
            showInForm = true
        ),
        FieldMetadata(
            name = "category",
            title = "分类",
            renderType = RenderType.SELECT,
            getValue = { it.category },
            setValue = { product, value -> product.copy(category = value as? String ?: "") },
            required = true,
            options = listOf(
                OptionItem("电子产品", "电子产品"),
                OptionItem("配件", "配件"),
                OptionItem("软件", "软件"),
                OptionItem("其他", "其他")
            ),
            showInSearch = true,
            showInTable = true,
            showInForm = true
        ),
        FieldMetadata(
            name = "price",
            title = "价格",
            renderType = RenderType.NUMBER,
            getValue = { it.price },
            setValue = { product, value -> 
                val price = when (value) {
                    is Number -> value.toDouble()
                    is String -> value.toDoubleOrNull() ?: 0.0
                    else -> 0.0
                }
                product.copy(price = price)
            },
            required = true,
            validator = { value ->
                when (value) {
                    is Number -> value.toDouble() > 0
                    is String -> value.toDoubleOrNull()?.let { it > 0 } ?: false
                    else -> false
                }
            },
            errorMessage = "价格必须大于0",
            showInSearch = true,
            showInTable = true,
            showInForm = true
        ),
        FieldMetadata(
            name = "stock",
            title = "库存",
            renderType = RenderType.NUMBER,
            getValue = { it.stock },
            setValue = { product, value -> 
                val stock = when (value) {
                    is Number -> value.toInt()
                    is String -> value.toIntOrNull() ?: 0
                    else -> 0
                }
                product.copy(stock = stock)
            },
            required = true,
            validator = { value ->
                when (value) {
                    is Number -> value.toInt() >= 0
                    is String -> value.toIntOrNull()?.let { it >= 0 } ?: false
                    else -> false
                }
            },
            errorMessage = "库存不能为负数",
            showInSearch = true,
            showInTable = true,
            showInForm = true
        ),
        FieldMetadata(
            name = "isActive",
            title = "是否上架",
            renderType = RenderType.SWITCH,
            getValue = { it.isActive },
            setValue = { product, value -> product.copy(isActive = value as? Boolean ?: false) },
            showInSearch = true,
            showInTable = true,
            showInForm = true
        ),
        FieldMetadata(
            name = "description",
            title = "描述",
            renderType = RenderType.TEXTAREA,
            getValue = { it.description },
            setValue = { product, value -> product.copy(description = value as? String ?: "") },
            placeholder = "请输入产品描述",
            showInSearch = false,
            showInTable = false,
            showInForm = true
        )
    )
    
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 标题
        Text(
            text = "产品管理",
            style = MaterialTheme.typography.headlineMedium
        )
        
        // 搜索区
        Surface(
            modifier = Modifier.fillMaxWidth(),
            tonalElevation = 1.dp,
            shape = MaterialTheme.shapes.medium
        ) {
            SearchForm(
                initialData = Product(),
                fields = fields,
                onSearch = { searchData ->
                    // 根据搜索条件过滤产品
                    filteredProducts = products.filter { product ->
                        (searchData.name.isEmpty() || product.name.contains(searchData.name, ignoreCase = true)) &&
                        (searchData.category.isEmpty() || product.category == searchData.category) &&
                        (searchData.price == 0.0 || product.price >= searchData.price) &&
                        (searchData.stock == 0 || product.stock >= searchData.stock) &&
                        (searchData.isActive == product.isActive)
                    }
                    searchProduct = searchData
                },
                onReset = {
                    filteredProducts = products
                    searchProduct = Product()
                }
            )
        }
        
        // 工具栏
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = { 
                    isAddingNew = true
                    selectedProduct = null
                },
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "添加",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("添加产品")
            }
        }
        
        // 数据表格
        Surface(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            tonalElevation = 1.dp,
            shape = MaterialTheme.shapes.medium
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // 表格
                    DataTable(
                        data = filteredProducts,
                        fields = fields,
                        onRowClick = { 
                            selectedProduct = it
                            isAddingNew = false
                        }
                    )
                    
                    // 如果没有数据，显示空状态
                    if (filteredProducts.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxWidth().height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "没有找到匹配的产品",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
        
        // 编辑表单
        if (selectedProduct != null) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                tonalElevation = 2.dp,
                shape = MaterialTheme.shapes.medium
            ) {
                EditForm(
                    data = selectedProduct!!,
                    fields = fields,
                    onSave = { updatedProduct ->
                        // 更新产品
                        products = products.map { 
                            if (it.id == updatedProduct.id) updatedProduct else it 
                        }
                        filteredProducts = filteredProducts.map { 
                            if (it.id == updatedProduct.id) updatedProduct else it 
                        }
                        selectedProduct = null
                    },
                    onCancel = {
                        selectedProduct = null
                    },
                    title = "编辑产品",
                    columnCount = 2
                )
            }
        }
        
        // 新增表单
        if (isAddingNew) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                tonalElevation = 2.dp,
                shape = MaterialTheme.shapes.medium
            ) {
                EditForm(
                    data = Product(id = products.maxOfOrNull { it.id }?.plus(1) ?: 1),
                    fields = fields,
                    onSave = { newProduct ->
                        // 添加新产品
                        products = products + newProduct
                        filteredProducts = filteredProducts + newProduct
                        isAddingNew = false
                    },
                    onCancel = {
                        isAddingNew = false
                    },
                    title = "添加产品",
                    columnCount = 2
                )
            }
        }
    }
} 