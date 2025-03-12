package com.addzero.web.ui.hooks.form

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.web.ui.hooks.UseHook

class UseTextAreaInput(val title: String) : UseHook<UseTextAreaInput> {
    var modelValue by mutableStateOf("")
    override val render: @Composable  () -> Unit
        get() = {
            OutlinedTextField(
                value = modelValue,
                onValueChange = { modelValue = it },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                placeholder = { Text("请输入$title") },
//                isError = error != null,
                maxLines = 5
            )
        }
}
