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

class SurfaceDemo : MetaSpec {

    override val metadata: RouteMetadata
        get() = RouteMetadata(
            parentName = "测试demo",
            title = "测试Surface",
            visible = true,
        )

    @Composable
    override fun render() {
        Surface(
            shape = RoundedCornerShape(8.dp)
//            , elevation = 10.dp
            , modifier = Modifier.width(300.dp).height(100.dp)
        ) {
            Row(
                modifier = Modifier.clickable {}) {
                Spacer(Modifier.padding(horizontal = 12.dp))
                Column(
                    modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Liratie", style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(Modifier.padding(vertical = 8.dp))
                    Text(
                        text = "礼谙"
                    )
                }
            }
        }
    }

}
