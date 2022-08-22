package solutions.vjk.catalist.state

import solutions.vjk.catalist.models.Category

data class RenameCategoryState(
    val category: Category,
    val newName: String
)
