package com.addzero.web.modules.functionbox.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.addzero.web.ui.components.system.dynamicroute.MetaSpec


interface FunctionBoxSpec  {
    val name: String
    val description: String
    val icon: ImageVector


    @Composable
    fun invoke()
}