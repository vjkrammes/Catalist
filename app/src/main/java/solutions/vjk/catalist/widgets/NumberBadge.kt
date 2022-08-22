package solutions.vjk.catalist.widgets

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NumberBadge(
    count: Int,
    padding: Dp = 16.dp,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    textColor: Color = MaterialTheme.colorScheme.onPrimary,
    fontSize: TextUnit = 16.sp,
    radius: Float = 0f
) {
    if (count > 0) {
        val numberText = if (count > 8) "9+" else count.toString()
        Text(
            text = numberText,
            fontSize = fontSize,
            color = textColor,
            modifier = Modifier
                .padding(all = padding)
                .drawBehind {
                    drawCircle(
                        color = backgroundColor,
                        radius = if (radius == 0f) this.size.maxDimension else radius
                    )
                },
        )
    }
}