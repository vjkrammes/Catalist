package solutions.vjk.catalist.state

import solutions.vjk.catalist.models.ToDoList

data class NewListState(
    val name: String,
    val importCategories: Boolean,
    val lists: List<ToDoList>,
    val selectedList: ToDoList?,
    val displayedListName: String,
    val isLoading: Boolean
)
