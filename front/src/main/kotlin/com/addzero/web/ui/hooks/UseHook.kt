package  com.addzero.web.ui.hooks
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

interface UseHook<T : UseHook<T>> {
    //      var  xxxxxx  by    mutableStateOf(value)

    val modifier: Modifier
        get() = Modifier

    val state: T
        get() = this as T
//         set(value) {
//      var  xxxxxx  by    mutableStateOf(value)
//         }

    val render: @Composable () -> Unit
        get() = {
           Text(text = "")
        }

    @Composable
    fun getState(): T {
        val remember = remember { state }
        return remember
    }


}
