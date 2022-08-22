package solutions.vjk.catalist.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import solutions.vjk.catalist.models.Category
import solutions.vjk.catalist.ui.theme.gray1

@Composable
fun SelectCategoryWidget(
    displayedCategoryName: String,
    doSetCategory: (Category) -> Unit,
    categories: List<Category>
) {
    val categoryListExpanded = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FormLabel(
            text = "Category",
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Box(
            modifier = Modifier
                .wrapContentSize(Alignment.TopStart)
                .fillMaxWidth()
        ) {
            Text(
                text = displayedCategoryName,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(categories.isNotEmpty()) { categoryListExpanded.value = true }
                    .background(gray1)
                    .padding(vertical = 8.dp, horizontal = 4.dp)
            )
            DropdownMenu(
                expanded = categoryListExpanded.value,
                onDismissRequest = {
                    categoryListExpanded.value = false
                }
            ) {
                categories.forEach { category ->
                    DropdownMenuItem(
                        onClick = {
                            doSetCategory(category)
                            categoryListExpanded.value = false
                        }
                    ) {
                        Text(text = category.name)
                    }
                }
            }
        }
    }
}
