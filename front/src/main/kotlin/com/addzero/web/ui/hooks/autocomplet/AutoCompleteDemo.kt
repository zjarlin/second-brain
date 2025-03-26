package com.addzero.web.ui.hooks.autocomplet

import androidx.compose.runtime.*
import com.addzero.Route

@Composable
@Route(homePageFlag = true)
fun AutoCompleteDemo() {
    var text by remember { mutableStateOf("") }
    val suggestions = remember {
        listOf(
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
        )
    }


    val useAutoComplet = UseAutoComplet(
        "水果",
        suggestions,
    ) {
        it
    }.render()


}