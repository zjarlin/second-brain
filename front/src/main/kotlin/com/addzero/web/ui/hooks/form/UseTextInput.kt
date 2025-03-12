package com.addzero.web.ui.hooks.form

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.addzero.web.ui.hooks.UseHook

class UseTextInput(val title: String) : UseHook<UseTextInput> {

    var value by mutableStateOf("")

    override val render: @Composable (() -> Unit)
        get() = {

            OutlinedTextField(
                value = value,
                onValueChange = { value = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(text = "请输入$title") },
                singleLine = true
            )

        }
}