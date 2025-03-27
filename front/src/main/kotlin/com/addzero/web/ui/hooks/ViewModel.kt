package  com.addzero.web.ui.hooks

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

/**
 * 自己瞎琢磨的viewModel
 * @author zjarlin
 * @date 2025/03/27
 * @constructor 创建[ViewModel]
 */
interface ViewModel<T : ViewModel<T>> {
    //      var  xxxxxx  by    mutableStateOf(value)

    val modifier: Modifier
        get() = Modifier

    val state: T
        get() = this as T


    @Composable
    fun rememberState(): T {
        val remember = remember { state }
        return remember
    }

    val render: @Composable () -> Unit


    @Composable
    fun render(block: T.() -> Unit) {
        val state = rememberState()
        block(state)
        state.render()
    }


}
