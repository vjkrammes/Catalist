package solutions.vjk.catalist.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import solutions.vjk.catalist.ui.theme.gray2
import solutions.vjk.catalist.ui.theme.gray4

@Composable
fun Expander(
    title: String,
    items: List<String>,
    itemLeadSpace: Dp = 36.dp
) {
    val isCollapsedState = remember { mutableStateOf(true) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .clickable(
                    enabled = items.isNotEmpty()
                ) {
                    isCollapsedState.value = !isCollapsedState.value
                }
                .padding(all = 4.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                if (isCollapsedState.value) {
                    Icons.Default.KeyboardArrowDown
                } else {
                    Icons.Default.KeyboardArrowUp
                },
                "",
                tint = if (items.isNotEmpty()) gray4 else gray2
            )
            Text(
                text = title,
                fontWeight = FontWeight.Bold
            )
        }
        if (!isCollapsedState.value) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Divider()
                for (item in items) {
                    Text(
                        modifier = Modifier.padding(start = itemLeadSpace),
                        text = item
                    )
                }
            }
        }
    }
}