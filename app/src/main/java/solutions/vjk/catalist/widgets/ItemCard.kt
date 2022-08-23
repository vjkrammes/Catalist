package solutions.vjk.catalist.widgets

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import solutions.vjk.catalist.infrastructure.displayDate
import solutions.vjk.catalist.infrastructure.faded
import solutions.vjk.catalist.models.Item
import java.text.SimpleDateFormat

@Composable
fun ItemCard(
    item: Item,
    onButtonClick: (Item) -> Unit,
    onItemClick: (Item) -> Unit,
    rowHeight: Dp = 40.dp,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    backgroundAlpha: Float = 1f
) {
    Row(
        modifier = Modifier
            .background(backgroundColor.faded(backgroundAlpha))
            .padding(all = 8.dp)
            .fillMaxWidth()
            .defaultMinSize(minWidth = rowHeight),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { onButtonClick(item) }
        ) {
            Crossfade(targetState = item.complete) { complete ->
                if (complete) {
                    Icon(Icons.Default.Delete, "")
                } else {
                    Icon(Icons.Default.Check, "")
                }
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Row(
            modifier = Modifier
                .clickable { onItemClick(item) }
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.name,
                fontSize = 18.sp,
                fontWeight = if (item.hasDueDate()) FontWeight.Bold else FontWeight.Normal,
                style = if (item.complete) {
                    TextStyle(textDecoration = TextDecoration.LineThrough)
                } else {
                    if (item.inProgress) {
                        TextStyle(fontStyle = FontStyle.Italic)
                    } else {
                        TextStyle(textDecoration = TextDecoration.None)
                    }
                },
                modifier = Modifier.weight(0.666667f),
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.width(4.dp))
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.weight(0.333333f)
            ) {
                Annotations(item = item)
                if (item.hasDueDate()) {
                    Text(
                        text = displayDate(item.dueDate, SimpleDateFormat("yyyy/MM/dd")),
                        fontSize = 16.sp,
                        fontStyle = FontStyle.Italic,
                        textAlign = TextAlign.End
                    )
                }
                if (item.assigneeId != 0 && item.assignee != null) {
                    val a = item.assignee!!
                    Text(
                        text = a.name,
                        fontSize = 16.sp,
                        fontStyle = FontStyle.Italic,
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    }
}