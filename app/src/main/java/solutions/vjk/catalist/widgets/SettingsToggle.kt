package solutions.vjk.catalist.widgets

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Switch
import androidx.compose.material.SwitchColors
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun SettingsToggle(
    text: String,
    initialValue: Boolean,
    doToggle: () -> Unit,
    modifier: Modifier = Modifier,
    fontFamily: FontFamily = FontFamily.Default,
    fontSize: TextUnit = 16.sp,
    fontStyle: FontStyle = FontStyle.Normal,
    fontWeight: FontWeight = FontWeight.Normal,
    colors: SwitchColors = SwitchDefaults.colors()
) {
    Row(
        modifier = modifier.then(Modifier.fillMaxWidth()),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Switch(
            checked = initialValue,
            onCheckedChange = { doToggle() },
            colors = colors
        )
        Text(
            text = text,
            modifier = Modifier.weight(1f),
            fontFamily = fontFamily,
            fontSize = fontSize,
            fontStyle = fontStyle,
            fontWeight = fontWeight
        )
    }
}