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
import solutions.vjk.catalist.state.NewItemState
import java.util.*
import javax.inject.Inject

@HiltViewModel
class NewItemViewModel @Inject constructor(
    private val assigneeRepository: IAssigneeRepository,
    private val categoryRepository: ICategoryRepository,
    private val itemRepository: IItemRepository
) : ViewModel() {

    private val _state = mutableStateOf(
        NewItemState(
            assigneeId = 0,
            listId = 0,
            categoryId = 0,
            name = "",
            hasDueDate = false,
            dueDate = DEFAULT_DATE,
            hasBudget = false,
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
                        assignees = assignees.toList(),
                        displayedAssigneeName = "Me"
                    )
                }
            }
        }
    }

    // state update functions

    fun setListId(listId: Int) {
        if (listId == 0) {
            return
        }
        if (state.value.isLoading) {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    val categories = categoryRepository.getForList(listId)
                    withContext(Dispatchers.Main) {
                        _state.value = state.value.copy(
                            name = "",
                            listId = listId,
                            categories = categories.toList(),
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun setName(name: String) {
        _state.value = state.value.copy(
            name = name.filterText("\r\n")
        )
    }

    fun setAssignee(assignee: Assignee?) {
        if (assignee == null) {
            _state.value = state.value.copy(
                selectedAssignee = null,
                displayedAssigneeName = "Me"
            )
        } else {
            _state.value = state.value.copy(
                selectedAssignee = assignee,
                displayedAssigneeName = assignee.name,
                assigneeId = assignee.id
            )
        }
    }

    fun setCategory(category: Category) {
        _state.value = state.value.copy(
            selectedCategory = category,
            displayedCategoryName = category.name,
            categoryId = category.id
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

    fun toggleInProgres() {
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
            sendToastMessage("Item name is required")
            return
        }
        val newItem = Item(
            id = 0,
            listId = state.value.listId,
            categoryId = state.value.categoryId,
            assigneeId = state.value.assigneeId,
            name = state.value.name,
            dueDate = if (state.value.hasDueDate) state.value.dueDate else DEFAULT_DATE,
            budget = state.value.budget,
            spent = state.value.spent,
            inProgress = state.value.inProgress,
            complete = state.value.complete
        )
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val response = itemRepository.insert(newItem)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessResult) {
                        sendToastMessage("Item Created Successfully")
                    } else {
                        sendToastMessage(response.getMessage())
                        _state.value = state.value.copy(errorMessage = response.getMessage())
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
        ): NewItemViewModel {
            val vm = NewItemViewModel(assigneeRepository, categoryRepository, itemRepository)
            vm.loadAssignees()
            return vm
        }
    }
}