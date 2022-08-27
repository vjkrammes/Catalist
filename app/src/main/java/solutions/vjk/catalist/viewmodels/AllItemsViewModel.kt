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
import solutions.vjk.catalist.interfaces.IItemRepository
import solutions.vjk.catalist.models.Item
import solutions.vjk.catalist.models.ToDoList
import solutions.vjk.catalist.state.AllItemsState
import javax.inject.Inject

@HiltViewModel
class AllItemsViewModel @Inject constructor(
    private val itemRepository: IItemRepository
) : ViewModel() {
    private val _state = mutableStateOf(
        AllItemsState(
            items = emptyList(),
            isLoading = true
        )
    )
    val state = _state.asState()

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage = _toastMessage.asSharedFlow()

    private val currentList = mutableStateOf(ToDoList())

    private fun sendToastMessage(message: String) {
        viewModelScope.launch {
            _toastMessage.emit(message)
        }
    }

    fun load(list: ToDoList) {
        currentList.value = list
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val i = itemRepository.getForList(list.id)
                withContext(Dispatchers.Main) {
                    _state.value = state.value.copy(
                        items = i.toList(),
                        isLoading = false
                    )
                }
            }
        }
    }

    fun refresh() {
        load(currentList.value)
    }

    fun deleteItem(item: Item) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val response = itemRepository.delete(item)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessResult) {
                        refresh()
                    } else {
                        sendToastMessage(response.getMessage())
                    }
                }
            }
        }
    }

    fun completeItem(item: Item) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val response = itemRepository.complete(item)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessResult) {
                        refresh()
                    } else {
                        sendToastMessage(response.getMessage())
                    }
                }
            }
        }
    }
}