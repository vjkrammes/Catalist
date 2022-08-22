package solutions.vjk.catalist.state

import solutions.vjk.catalist.models.Assignee
import solutions.vjk.catalist.models.ItemCount

data class ManageAssigneesState(
    val assignees: List<Assignee>,
    val assignments: List<ItemCount>,
    val isLoading: Boolean,
)
