package solutions.vjk.catalist.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.KeyboardCapitalization

@Composable
fun AutofocusTextInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    singleLine: Boolean = true,
    colors: TextFieldColors = TextFieldDefaults.textFieldColors()
) {
    val focusRequester = remember { FocusRequester() }
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.then(Modifier.focusRequester(focusRequester)),
        label = label,
        placeholder = placeholder,
        singleLine = singleLine,
        colors = colors,
        trailingIcon = {
            Icon(
                Icons.Default.Clear,
                contentDescription = "Clear Input",
                modifier = Modifier.clickable {
                    onValueChange("")
                }
            )
        },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words
        )
    )

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}