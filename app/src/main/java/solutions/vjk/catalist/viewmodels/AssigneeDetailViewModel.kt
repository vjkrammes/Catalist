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
import solutions.vjk.catalist.infrastructure.asState
import solutions.vjk.catalist.interfaces.IAssigneeRepository
import solutions.vjk.catalist.interfaces.IItemRepository
import solutions.vjk.catalist.models.Assignee
import solutions.vjk.catalist.state.AssigneeDetailState
import javax.inject.Inject

@HiltViewModel
class AssigneeDetailViewModel @Inject constructor(
    private val itemRepository: IItemRepository,
    private val assigneeRepository: IAssigneeRepository
) : ViewModel() {
    private val _state = mutableStateOf(
        AssigneeDetailState(
            assignee = Assignee(),
            assignedItems = emptyList(),
            isLoading = true
        )
    )
    val state = _state.asState()

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage = _toastMessage.asSharedFlow()

    private var assigneeId: Int = 0

    private fun sendToastMessage(message: String) {
        viewModelScope.launch {
            _toastMessage.emit(message)
        }
    }

    fun load(assigneeId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val a = assigneeRepository.read(assigneeId)
                if (a == null) {
                    withContext(Dispatchers.Main) {
                        sendToastMessage("Assignee not found")
                        _state.value = state.value.copy(isLoading = false)
                    }
                } else {
                    this@AssigneeDetailViewModel.assigneeId = assigneeId
                    val i = itemRepository.getForAssignee(assigneeId)
                    withContext(Dispatchers.Main) {
                        _state.value = state.value.copy(
                            assignee = a,
                            assignedItems = i.toList(),
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun clearAssignment(itemId: Int, sendMessage: Boolean = true) {
        if (assigneeId == 0) {
            sendToastMessage("Assignee not found")
        } else {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    val result = itemRepository.removeAssignee(itemId)
                    withContext(Dispatchers.Main) {
                        if (result.isSuccessResult) {
                            if (sendMessage) {
                                sendToastMessage("Assignment Removed")
                            }
                            load(assigneeId)
                        } else {
                            if (sendMessage) {
                                sendToastMessage(result.getMessage())
                            }
                        }
                    }
                }
            }
        }
    }

    fun clearAllAssignments() {
        for (item in state.value.assignedItems) {
            clearAssignment(item.id, false)
        }
        sendToastMessage("All assignments removed")
    }
}