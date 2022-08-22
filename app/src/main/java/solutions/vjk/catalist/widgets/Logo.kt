package solutions.vjk.catalist.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp

@Composable
fun Logo(
    logo: ImageBitmap,
    text: @Composable (() -> Unit),
    showText: Boolean = true
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            logo,
            contentDescription = "VJK Solutions Logo",
            modifier = Modifier.size(32.dp)
        )
        if (showText) {
            text()
        }
    }
}