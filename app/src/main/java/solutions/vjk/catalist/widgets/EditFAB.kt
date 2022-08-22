package solutions.vjk.catalist.widgets

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun EditFAB(
    click: () -> Unit
) {
    FloatingActionButton(
        onClick = click,
        shape = CircleShape,
        backgroundColor = MaterialTheme.colorScheme.secondary
    ) {
        Icon(Icons.Default.Check, "Save changes")
    }
}