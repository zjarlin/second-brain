package com.addzero.web.ui.hooks.autocomplet

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.addzero.Route


@Composable
@Route()
fun AutoCompleteDemo() {
    Column {
        (1..3).map { index ->
            val map = listOf<String>(
                "Apple",
                "Banana",
                "Cherry",
                "Date",
                "Elderberry",
                "Fig",
                "Grape",
                "Honeydew",
                "Iceberg Lettuce",
                "Jackfruit"
            ).map { it + index }
            Text("测试自动完成" + index)
            val useAutoComplet = UseAutoComplet("水果", map)
            useAutoComplet.render {}
//            useAutoComplet.render()

        }


    }


}