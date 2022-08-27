package solutions.vjk.catalist.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import solutions.vjk.catalist.infrastructure.DEFAULT_DATE
import solutions.vjk.catalist.infrastructure.asState
import solutions.vjk.catalist.infrastructure.filterText
import solutions.vjk.catalist.interfaces.IAssigneeRepository
import solutions.vjk.catalist.interfaces.ICategoryRepository
import solutions.vjk.catalist.interfaces.IItemRepository
import solutions.vjk.catalist.models.Assignee
import solutions.vjk.catalist.models.Category
import solutions.vjk.catalist.models.Item
import solutions.vjk.catalist.state.EditItemState
import java.util.*
import javax.inject.Inject

@HiltViewModel
class EditItemViewModel @Inject constructor(
    private val assigneeRepository: IAssigneeRepository,
    private val categoryRepository: ICategoryRepository,
    private val itemRepository: IItemRepository
) : ViewModel() {

    private val _state = mutableStateOf(
        EditItemState(
            id = 0,
            assigneeId = 0,
            listId = 0,
            categoryId = 0,
            name = "",
            hasDueDate = false,
            dueDate = DEFAULT_DATE,
            hasBudget = true,
            budget = 0,
            spent = 0,
            inProgress = false,
            complete = false,
            assignees = emptyList(),
            selectedAssignee = null,
            displayedAssigneeName = "",
            categories = emptyList(),
            selectedCategory = null,
            displayedCategoryName = "",
            isLoading = true,
            errorMessage = null
        )
    )
    val state = _state.asState()

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage = _toastMessage.asSharedFlow()

    private var editingItem: Item = Item()

    private fun sendToastMessage(message: String) {
        viewModelScope.launch {
            _toastMessage.emit(message)
        }
    }

    private fun loadAssignees() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val assignees = assigneeRepository.get()
                withContext(Dispatchers.Main) {
                    _state.value = state.value.copy(
                        assignees = assignees.toList()
                    )
                }
            }
        }
    }

    fun setItem(itemId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val i = itemRepository.read(itemId)
                if (i != null) {
                    setItem(i)
                }
            }
        }
    }

    private fun setItem(item: Item, force: Boolean = false) {
        if (editingItem.id != item.id || force) {
            editingItem = item
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    val assignees = assigneeRepository.get()
                    val categories = categoryRepository.getForList(item.listId)
                    val selectedAssignee = assigneeRepository.read(item.assigneeId)
                    val selectedCategory = categoryRepository.read(item.categoryId)
                    withContext(Dispatchers.Main) {
                        _state.value = state.value.copy(
                            id = item.id,
                            assigneeId = item.assigneeId,
                            listId = item.listId,
                            categoryId = item.categoryId,
                            name = item.name,
                            hasDueDate = item.hasDueDate(),
                            dueDate = item.dueDate,
                            hasBudget = item.budget > 0,
                            budget = item.budget,
                            spent = item.spent,
                            inProgress = item.inProgress,
                            complete = item.complete,
                            assignees = assignees.toList(),
                            selectedAssignee = selectedAssignee,
                            displayedAssigneeName = selectedAssignee?.name ?: "Me",
                            categories = categories.toList(),
                            selectedCategory = selectedCategory,
                            displayedCategoryName = selectedCategory?.name ?: "",
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }
            }
        }
    }

    // state update functions

    fun setName(name: String) {
        _state.value = state.value.copy(name = name.filterText("\r\n"))
    }

    fun setAssignee(assignee: Assignee?) {
        if (assignee == null) {
            _state.value = state.value.copy(
                assigneeId = 0,
                selectedAssignee = null,
                displayedAssigneeName = "Me"
            )
        } else {
            _state.value = state.value.copy(
                assigneeId = assignee.id,
                selectedAssignee = assignee,
                displayedAssigneeName = assignee.name
            )
        }
    }

    fun setCategory(category: Category) {
        _state.value = state.value.copy(
            categoryId = category.id,
            selectedCategory = category,
            displayedCategoryName = category.name
        )
    }

    fun setDueDate(hasDueDate: Boolean, dueDate: Calendar?) {
        _state.value = state.value.copy(
            hasDueDate = hasDueDate,
            dueDate = dueDate ?: DEFAULT_DATE
        )
    }

    fun setBudget(hasBudget: Boolean, budget: Int, spent: Int) {
        _state.value = state.value.copy(
            hasBudget = hasBudget,
            budget = budget,
            spent = spent
        )
    }

    fun toggleInProgress() {
        val newInProgress = !state.value.inProgress
        _state.value = state.value.copy(
            inProgress = newInProgress,
            complete = false
        )
    }

    fun toggleComplete() {
        val newComplete = !state.value.complete
        _state.value = state.value.copy(
            inProgress = false,
            complete = newComplete
        )
    }

    fun saveChanges() {
        if (state.value.name.isEmpty()) {
            sendToastMessage("Name is required")
            return
        }
        if (state.value.selectedCategory == null) {
            sendToastMessage("Please select a category")
            return
        }
        val newItem = Item(
            id = state.value.id,
            listId = state.value.listId,
            assigneeId = state.value.assigneeId,
            categoryId = state.value.categoryId,
            name = state.value.name,
            dueDate = if (state.value.hasDueDate) state.value.dueDate else DEFAULT_DATE,
            budget = state.value.budget,
            spent = state.value.spent,
            inProgress = state.value.inProgress,
            complete = state.value.complete
        )
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val response = itemRepository.update(newItem)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessResult) {
                        sendToastMessage("Item updated successfully")
                        setItem(newItem, true)
                    } else {
                        sendToastMessage(response.getMessage())
                        _state.value = state.value.copy(errorMessage = response.getMessage())
                    }
                }
            }
        }
    }

    fun deleteItem() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val response = itemRepository.delete(editingItem)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessResult) {
                        sendToastMessage("Item deleted successfully")

                    }
                }
            }
        }
    }

    companion object {
        fun create(
            assigneeRepository: IAssigneeRepository,
            categoryRepository: ICategoryRepository,
            itemRepository: IItemRepository
        ): EditItemViewModel {
            val vm = EditItemViewModel(assigneeRepository, categoryRepository, itemRepository)
            vm.loadAssignees()
            return vm
        }
    }
}