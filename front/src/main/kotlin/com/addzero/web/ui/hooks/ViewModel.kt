package  com.addzero.web.ui.hooks

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

interface ViewModel<T : ViewModel<T>> {
    //      var  xxxxxx  by    mutableStateOf(value)

    val modifier: Modifier
        get() = Modifier

    val state: T
        get() = this as T

    val render: @Composable T.() -> Unit

    @Composable
    fun render(content: T.() -> Unit) {
        val state = rememberState()
        content(state)
        state.render()
    }

    @Composable
    fun rememberState(): T {
        val remember = remember { state }
        return remember
    }


}
