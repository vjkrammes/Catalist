package solutions.vjk.catalist.widgets

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun RootFAB(
    click: () -> Unit
) {
    return FloatingActionButton(
        onClick = click,
        shape = CircleShape,
        backgroundColor = MaterialTheme.colorScheme.secondary
    ) {
        Icon(Icons.Filled.Add, "Add a new Item")
    }
}