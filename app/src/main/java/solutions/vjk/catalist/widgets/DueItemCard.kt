package solutions.vjk.catalist.widgets

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import solutions.vjk.catalist.infrastructure.displayDate
import solutions.vjk.catalist.models.Item
import java.text.SimpleDateFormat

@Composable
fun DueItemCard(
    item: Item,
    onClick: (Item) -> Unit,
    rowHeight: Dp = 40.dp,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    backgroundAlpha: Float = 1f
) {
    Log.i("Info", "Item id = ${item.id}, assignee = ${item.assignee?.name ?: "<null>"}")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor.copy(alpha = backgroundAlpha))
            .padding(all = 8.dp)
            .defaultMinSize(minHeight = rowHeight)
            .clickable { onClick(item) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = item.name,
            fontSize = 18.sp,
            fontStyle = if (item.inProgress) FontStyle.Italic else FontStyle.Normal,
            modifier = Modifier.weight(0.666667f),
            overflow = TextOverflow.Ellipsis
        )
        Column(
            modifier = Modifier.weight(0.333333f),
            horizontalAlignment = Alignment.End
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