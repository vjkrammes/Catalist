package solutions.vjk.catalist.state

import solutions.vjk.catalist.models.Category
import solutions.vjk.catalist.models.ItemCount

data class ManageCategoriesState(
    val categories: List<Category>,
    val items: List<ItemCount>,
    val isLoading: Boolean
)
