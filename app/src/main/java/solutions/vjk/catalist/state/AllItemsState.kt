package solutions.vjk.catalist.state

import solutions.vjk.catalist.models.Item

data class AllItemsState(
    val items: List<Item>,
    val isLoading: Boolean
)
