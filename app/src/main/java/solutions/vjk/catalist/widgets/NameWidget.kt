package solutions.vjk.catalist.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NameWidget(
    name: String,
    valueChanged: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        AutofocusTextInput(
            value = name,
            onValueChange = valueChanged,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Name") },
            placeholder = { Text(text = "Item Name") },
            singleLine = true
        )
    }
}
