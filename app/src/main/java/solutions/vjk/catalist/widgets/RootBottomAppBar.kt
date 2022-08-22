package solutions.vjk.catalist.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun RootBottomAppBar(
    logo: ImageBitmap,
    appName: String,
    fontFamily: FontFamily = FontFamily.Default,
    fontSize: TextUnit = 16.sp,
    fontStyle: FontStyle = FontStyle.Normal
) {
    return BottomAppBar(
        backgroundColor = MaterialTheme.colorScheme.primary,
        cutoutShape = CircleShape
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Logo(
                logo = logo,
                text = {
                    Text(
                        text = appName,
                        fontFamily = fontFamily,
                        fontSize = fontSize,
                        fontStyle = fontStyle
                    )
                }
            )
        }
    }
}