package com.addzero.web.modules.demo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.web.ui.system.dynamicroute.MetaSpec
import com.addzero.web.ui.system.dynamicroute.RouteMetadata

class FormDemo : MetaSpec {

    override val metadata: RouteMetadata
        get() = RouteMetadata(
            parentName = "测试demo",
            title = "测试form",
            visible = true,
        )

    @Composable
    override fun render() {
    }

}
