package  com.addzero.web.ui.hooks
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

interface UseHook<T : UseHook<T>> {
    //      var  xxxxxx  by    mutableStateOf(value)

    val modifier: Modifier
        get() = Modifier

    val state: T
        get() = this as T

    val render: @Composable () -> Unit

    @Composable
    fun render(content: @Composable T.() -> Unit ) {
        val state = getState()
        if (content == {}) {
            state.render()
        } else {
            content(state)
        }
    }

    @Composable
    fun getState(): T {
        val remember = remember { state }
        return remember
    }


}
