package solutions.vjk.catalist.widgets

import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun StandardFAB(
    text: String,
    icon: ImageVector,
    click: () -> Unit,
    contentDescription: String? = null
) {
    ExtendedFloatingActionButton(
        text = { Text(text = text) },
        onClick = click,
        backgroundColor = MaterialTheme.colorScheme.secondary,
        icon = { Icon(icon, contentDescription ?: text) }
    )
}