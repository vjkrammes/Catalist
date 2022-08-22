package solutions.vjk.catalist.state

import solutions.vjk.catalist.models.Category
import solutions.vjk.catalist.models.Item

data class CategoryDetailState(
    val category: Category,
    val items: List<Item>,
    val isLoading: Boolean
)
