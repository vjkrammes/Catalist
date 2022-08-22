package solutions.vjk.catalist.state

import solutions.vjk.catalist.models.Item

data class SearchState(
    val searchText: String,
    val matchingItems: List<Item>
)
