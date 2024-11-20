package com.addzero.web.ui.components.crud

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 通用 CRUD 布局组件
 * @param T 数据类型
 * @param searchBar 搜索区域插槽
 * @param actionButtons 操作按钮插槽
 * @param content 主内容区插槽
 * @param pagination 分页控制插槽
 * @param isLoading 是否加载中
 * @param error 错误信息
 */
@Composable
fun <T> CrudLayout(
    isLoading: Boolean,
    error: String?,
    searchBar: @Composable () -> Unit = {},
    actionButtons: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
    pagination: @Composable () -> Unit = {}
) {
    Column {
        // 搜索区域插槽
        searchBar()

        Spacer(modifier = Modifier.height(16.dp))

        // 操作按钮插槽
        actionButtons()

        // 加载状态
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }

        // 错误提示
        error?.let { errorMsg ->
            Text(
                text = errorMsg,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(16.dp)
            )
        }

        // 主内容区插槽
        content()

        // 分页控制插槽
        pagination()
    }
} 