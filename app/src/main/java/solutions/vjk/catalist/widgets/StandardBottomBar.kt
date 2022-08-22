package solutions.vjk.catalist.widgets

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import solutions.vjk.catalist.R

@Composable
fun StandardBottomBar(
    text: String
) {
    val logoBitmap = ImageBitmap.imageResource(id = R.drawable.logo64)
    val lara = FontFamily(Font(R.font.lora, FontWeight.Bold))
    BottomAppBar(
        backgroundColor = MaterialTheme.colorScheme.primary,
        cutoutShape = CircleShape
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Logo(
                logo = logoBitmap,
                text = {
                    Text(
                        text = text,
                        fontFamily = lara,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                },
                showText = true
            )
        }
    }
}