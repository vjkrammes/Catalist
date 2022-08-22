package solutions.vjk.catalist.widgets

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import solutions.vjk.catalist.models.Category
import solutions.vjk.catalist.models.Item
import solutions.vjk.catalist.ui.theme.gray2
import solutions.vjk.catalist.ui.theme.gray4

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoryWidget(
    categories: List<Category>,
    items: List<Item>,
    onIncompleteClick: (Item) -> Unit,
    onCompletedClick: (Item) -> Unit,
    onItemClick: (Item) -> Unit,
    onItemLongClick: (Category) -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    backgroundAlpha: Float = 1f
) {
    val isCollapsedState = remember(categories) { categories.map { true }.toMutableStateList() }
    LazyColumn(
        modifier = modifier
    ) {
        categories.forEachIndexed { ix, category ->
            val isCollapsed = isCollapsedState[ix]
            val catitems = items.filter { it.categoryId == category.id }.sortedBy { it.name }
            val itemcount = catitems.size
            item(key = "category$ix") {
                Row(
                    modifier = Modifier
                        .combinedClickable(
                            onClick = {
                                isCollapsedState[ix] = !isCollapsed
                            },
                            onLongClick = {
                                onItemLongClick(category)
                            }
                        )
                        .background(
                            if (catitems.isEmpty()) {
                                MaterialTheme.colorScheme.surface
                            } else {
                                if (isCollapsed) {
                                    MaterialTheme.colorScheme.surface
                                } else {
                                    MaterialTheme.colorScheme.primary
                                }
                            }
                        )
                        .padding(all = 4.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            if (catitems.isEmpty()) {
                                Icons.Default.KeyboardArrowDown
                            } else {
                                if (isCollapsed) {
                                    Icons.Default.KeyboardArrowDown
                                } else {
                                    Icons.Default.KeyboardArrowUp
                                }
                            },
                            "",
                            tint = if (catitems.isNotEmpty()) gray4 else gray2
                        )
                        Text(
                            text = category.name,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                        NumberBadge(
                            count = itemcount,
                            fontSize = 12.sp,
                            radius = 32f,
                            textColor = MaterialTheme.colorScheme.onSecondary,
                            backgroundColor = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
                Divider(modifier = Modifier.padding(vertical = 4.dp))
            }
            if (!isCollapsed) {
                items(catitems) { item ->
                    Row {
                        ItemCard(
                            item = item,
                            onButtonClick = if (item.complete) onCompletedClick else onIncompleteClick,
                            onItemClick = onItemClick,
                            backgroundColor = backgroundColor,
                            backgroundAlpha = backgroundAlpha
                        )
                    }
                    Divider()
                }
            }
        }
    }
}