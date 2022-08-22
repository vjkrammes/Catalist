package solutions.vjk.catalist.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import solutions.vjk.catalist.models.NavigationMenuItem

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NavigationDrawerItem(item: NavigationMenuItem, selected: Boolean, onItemClick: () -> Unit) {
    val backgroundColor = if (selected) MaterialTheme.colorScheme.primary else Color.White
    val keyboardController = LocalSoftwareKeyboardController.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = {
                    keyboardController?.hide()
                    onItemClick()
                }
            )
            .background(backgroundColor)
    ) {
        Image(
            painterResource(id = item.iconId),
            contentDescription = item.text,
            modifier = Modifier
                .size(item.iconSize)
                .padding(end = 4.dp)
        )
        Text(
            text = item.text,
            style = item.itemStyle,
            fontStyle = item.fontStyle,
            fontWeight = item.fontWeight
        )
    }
    Spacer(modifier = Modifier.size(4.dp))
}
