package solutions.vjk.catalist.models

import solutions.vjk.catalist.models.Category
import solutions.vjk.catalist.models.Item

data class ItemCollection(
    val category: Category,
    val items: List<Item>
)