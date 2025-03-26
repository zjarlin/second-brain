package com.addzero.web.ui.components

import androidx.compose.runtime.*
import com.addzero.Route

@Composable
@Route(homePageFlag = true)
fun AutoCompleteDemo() {
    var text by remember { mutableStateOf("") }
    val suggestions = remember {
        listOf(
            "Apple", "Banana", "Cherry", "Date", "Elderberry",
            "Fig", "Grape", "Honeydew", "Iceberg Lettuce", "Jackfruit"
        )
    }


    val useAutoCompleteTextField = UseAutoCompleteTextField(
        "水果",
        suggestions, { it }
         ).render()


//    AutoCompleteTextField(
//        value = text,
//        onValueChange = { text = it },
//        suggestions = suggestions,
//        onSuggestionSelected = { selectedText ->
//            text = selectedText
//        },
//        label = { Text("Search fruits") },
//        placeholder = { Text("Type to search...") },
//        modifier = Modifier.Companion.padding(16.dp)
//    )
}