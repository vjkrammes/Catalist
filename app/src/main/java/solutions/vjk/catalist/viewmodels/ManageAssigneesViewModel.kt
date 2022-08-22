package solutions.vjk.catalist.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import solutions.vjk.catalist.infrastructure.asState
import solutions.vjk.catalist.infrastructure.filterText
import solutions.vjk.catalist.interfaces.IAssigneeRepository
import solutions.vjk.catalist.interfaces.IItemRepository
import solutions.vjk.catalist.models.Assignee
import solutions.vjk.catalist.models.ItemCount
import solutions.vjk.catalist.state.ManageAssigneesState
import javax.inject.Inject

@HiltViewModel
class ManageAssigneesViewModel @Inject constructor(
    private val assigneeRepository: IAssigneeRepository,
    private val itemRepository: IItemRepository
) : ViewModel() {
    private val _state = mutableStateOf(
        ManageAssigneesState(
            assignees = emptyList(),
            assignments = emptyList(),
            isLoading = true
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

    fun getAssignmentCount(assignee: Assignee): Int =
        state.value.assignments.singleOrNull { it.id == assignee.id }?.count ?: 0

    fun loadAssignees() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val assignees = assigneeRepository.get()
                val assignments = assignees.map { assignee ->
                    ItemCount(assignee.id, itemRepository.assignmentCount(assignee.id))
                }
                withContext(Dispatchers.Main) {
                    _state.value = state.value.copy(
                        assignees = assignees.toList(),
                        assignments = assignments.toList(),
                        isLoading = false
                    )
                }
            }
        }
    }

    fun assigneeDetails(assignee: Assignee, navController: NavController) {
        val count = getAssignmentCount(assignee)
        if (count == 0) {
            sendToastMessage("Assignee has no details to display")
        } else {
            navController.navigate("assigneedetail/${assignee.id}")
        }
    }

    fun addAssignee(name: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val assignee = Assignee(id = 0, name = name.filterText("\r\n"))
                val result = assigneeRepository.insert(assignee)
                val newAssignees = assigneeRepository.get()
                withContext(Dispatchers.Main) {
                    if (result.isSuccessResult) {
                        _state.value = state.value.copy(assignees = newAssignees.toList())
                        sendToastMessage("Assignee created successfully")
                    } else {
                        sendToastMessage(result.getMessage())
                    }
                }
            }
        }
    }

    fun deleteAssignee(assignee: Assignee) {
        if (getAssignmentCount(assignee) == 0) {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    val result = assigneeRepository.delete(assignee)
                    val newAssignees = assigneeRepository.get()
                    withContext(Dispatchers.Main) {
                        if (result.isSuccessResult) {
                            _state.value = state.value.copy(assignees = newAssignees.toList())
                            sendToastMessage("Assignee deleted successfully")
                        } else {
                            sendToastMessage(result.getMessage())
                        }
                    }
                }
            }
        } else {
            sendToastMessage("Assignee has assigned tasks")
        }
    }

    companion object {
        fun create(
            assigneeRepository: IAssigneeRepository,
            itemRepository: IItemRepository
        ): ManageAssigneesViewModel {
            val vm = ManageAssigneesViewModel(assigneeRepository, itemRepository)
            vm.loadAssignees()
            return vm
        }
    }
}