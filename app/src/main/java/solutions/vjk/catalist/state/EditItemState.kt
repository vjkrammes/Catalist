package solutions.vjk.catalist.state

import solutions.vjk.catalist.models.Assignee
import solutions.vjk.catalist.models.Category
import java.util.*

data class EditItemState(
    val id: Int = 0,
    val assigneeId: Int = 0,
    val listId: Int = 0,
    val categoryId: Int = 0,
    val name: String = "",
    val hasDueDate: Boolean = false,
    val dueDate: Calendar = Calendar.getInstance(),
    val hasBudget: Boolean = false,
    val budget: Int = 0,
    val spent: Int = 0,
    val inProgress: Boolean = false,
    val complete: Boolean = false,
    val assignees: List<Assignee> = emptyList(),
    val selectedAssignee: Assignee? = null,
    val displayedAssigneeName: String = "",
    val categories: List<Category> = emptyList(),
    val selectedCategory: Category? = null,
    val displayedCategoryName: String = "",
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)
