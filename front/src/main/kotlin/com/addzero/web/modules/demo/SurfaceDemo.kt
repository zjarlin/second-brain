package com.addzero.web.modules.demo

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.addzero.web.ui.components.system.dynamicroute.MetaSpec
import com.addzero.web.ui.components.system.dynamicroute.RouteMetadata
import com.addzero.web.ui.hooks.table.UseTableExample

class SurfaceDemo : MetaSpec {

    override val metadata: RouteMetadata
        get() = RouteMetadata(
            parentName = "测试demo",
            title = "测试Surface",
            visible = true,
        )

    @Composable
    override fun render() {
//        Surface(
//            shape = RoundedCornerShape(8.dp), elevation = 10.dp, modifier = Modifier.width(300.dp).height(100.dp)
//        ) {
//            Row(
//                modifier = Modifier.clickable {}) {
//                Image(
//                    painter = painterResource(id = R.drawable.pic),
//                    contentDescription = stringResource(R.string.description),
//                    modifier = Modifier.size(100.dp),
//                    contentScale = ContentScale.Crop
//                )
//                Spacer(Modifier.padding(horizontal = 12.dp))
//                Column(
//                    modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center
//                ) {
//                    Text(
//                        text = "Liratie", style = MaterialTheme.typography.h6
//                    )
//                    Spacer(Modifier.padding(vertical = 8.dp))
//                    Text(
//                        text = "礼谙"
//                    )
//                }
//            }
//        }
    }

}
