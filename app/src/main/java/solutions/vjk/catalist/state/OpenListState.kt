package solutions.vjk.catalist.state

import solutions.vjk.catalist.models.ToDoList

data class OpenListState(
    val lists: List<ToDoList>,
    val currentListName: String,
    val isLoading: Boolean
)
