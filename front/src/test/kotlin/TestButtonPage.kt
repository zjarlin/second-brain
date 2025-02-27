import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class TestButtonPage {
    @Composable
    @Preview
    fun render() {
        var question by remember { mutableStateOf(1) }
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "父组件计数: $question")
            Button(onClick = { question += 1 }) {
                Text("增加")
            }

            Spacer(modifier = Modifier.height(16.dp))

            ChildCounter(
                count = question,
                onCountChange = { question = it }
            )
        }
    }
}

@Composable
@Preview
fun ChildCounter(
    count: Int,
    onCountChange: (Int) -> Unit
) {
    Column {
        Text(text = "子组件显示: $count")
        Button(
            onClick = { onCountChange(count + 1) }
        ) {
            Text("子组件增加")
        }
    }
}
