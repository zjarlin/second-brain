import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp


@OptIn(
    ExperimentalMaterialApi::class, ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class
)
@Composable
fun AutoCompleteTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    suggestions: List<String>,
    onSuggestionSelected: (String) -> Unit,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    isError: Boolean = false,
    singleLine: Boolean = true,
    maxSuggestions: Int = 5
) {
    var showSuggestions by remember { mutableStateOf(false) }
    var selectedSuggestionIndex by remember { mutableStateOf(-1) }
    var hasFocus by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    // Filter suggestions based on input
    val filteredSuggestions = remember(value, suggestions) {
        if (value.isBlank()) {
            suggestions.take(maxSuggestions)
        } else {
            suggestions.filter { it.contains(value, ignoreCase = true) }.take(maxSuggestions)
        }
    }

    Column(modifier = modifier) {
        TextField(
            value = value,
            onValueChange = {
                onValueChange(it)
                showSuggestions = it.isNotBlank() || hasFocus
                selectedSuggestionIndex = -1
            },
            modifier = Modifier.fillMaxWidth().onFocusChanged { focusState ->
                hasFocus = focusState.isFocused
                showSuggestions = focusState.isFocused
            }.onKeyEvent { keyEvent ->
                when (keyEvent.key) {
                    Key.Tab -> {
                        if (showSuggestions && filteredSuggestions.isNotEmpty()) {
                            selectedSuggestionIndex = (selectedSuggestionIndex + 1) % filteredSuggestions.size
                            true
                        } else {
                            false
                        }
                    }

                    Key.DirectionUp -> {
                        if (showSuggestions && filteredSuggestions.isNotEmpty()) {
                            selectedSuggestionIndex = (selectedSuggestionIndex - 1).coerceAtLeast(0)
                            true
                        } else {
                            false
                        }
                    }

                    Key.DirectionDown -> {
                        if (showSuggestions && filteredSuggestions.isNotEmpty()) {
                            selectedSuggestionIndex = (selectedSuggestionIndex + 1) % filteredSuggestions.size
                            true
                        } else {
                            false
                        }
                    }

                    Key.Enter -> {
                        if (showSuggestions && selectedSuggestionIndex >= 0) {
                            onSuggestionSelected(filteredSuggestions[selectedSuggestionIndex])
                            showSuggestions = false
                            keyboardController?.hide()
                            true
                        } else {
                            false
                        }
                    }

                    Key.Backspace -> {
                        if (value.isEmpty()) {
                            showSuggestions = hasFocus
                        }
                        false // Let the default backspace behavior happen
                    }

                    Key.Escape -> {
                        showSuggestions = false
                        true
                    }

                    else -> false
                }
            }.focusRequester(focusRequester),
            label = label,
            placeholder = placeholder,
            enabled = enabled,
            isError = isError,
            singleLine = singleLine,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (showSuggestions && selectedSuggestionIndex >= 0) {
                        onSuggestionSelected(filteredSuggestions[selectedSuggestionIndex])
                    }
                    showSuggestions = false
                    keyboardController?.hide()
                }),
            colors = TextFieldDefaults.textFieldColors()
        )

        if (showSuggestions && filteredSuggestions.isNotEmpty()) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().border(1.dp, Color.Gray).background(Color.White)
                ) {
                    items(filteredSuggestions) { suggestion ->
                        val isSelected = filteredSuggestions.indexOf(suggestion) == selectedSuggestionIndex
                        Text(text = suggestion, modifier = Modifier.fillMaxWidth().clickable {
                            onSuggestionSelected(suggestion)
                            showSuggestions = false
                            keyboardController?.hide()
                        }.background(if (isSelected) Color.LightGray else Color.White).padding(16.dp))
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}