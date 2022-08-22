package solutions.vjk.catalist.widgets

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import solutions.vjk.catalist.models.Category

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoryCard(
    category: Category,
    itemCount: Int,
    onButtonClick: (Category) -> Unit,
    onCategoryClick: (Category) -> Unit,
    onLongClick: (Category) -> Unit,
    rowHeight: Dp = 40.dp
) {
    Row(
        modifier = Modifier
            .padding(all = 8.dp)
            .fillMaxWidth()
            .combinedClickable(
                onClick = {
                    onCategoryClick(category)
                },
                onLongClick = {
                    onLongClick(category)
                }
            )
            .height(rowHeight),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { onButtonClick(category) },
            enabled = itemCount == 0
        ) {
            Icon(Icons.Default.Delete, "Delete Category")
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = category.name,
            fontSize = 24.sp,
            modifier = Modifier.weight(1f),
            overflow = TextOverflow.Ellipsis
        )
        if (itemCount > 0) {
            Text(
                text = itemCount.toString(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End,
                fontStyle = FontStyle.Italic
            )
        }
    }
}
