package solutions.vjk.catalist.state

import solutions.vjk.catalist.models.Category
import solutions.vjk.catalist.models.Item
import solutions.vjk.catalist.models.ItemCollection
import solutions.vjk.catalist.models.ToDoList

data class ItemState(
    val currentList: ToDoList = ToDoList(),
    val lists: List<ToDoList> = emptyList(),
    val categories: List<Category> = emptyList(),
    val items: List<Item> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)