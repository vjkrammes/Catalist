package solutions.vjk.catalist.state

import solutions.vjk.catalist.models.Assignee
import solutions.vjk.catalist.models.Item

data class AssigneeDetailState(
    val assignee: Assignee,
    val assignedItems: List<Item>,
    val isLoading: Boolean
)
