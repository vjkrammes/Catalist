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
import solutions.vjk.catalist.infrastructure.toInt
import solutions.vjk.catalist.interfaces.IItemRepository
import solutions.vjk.catalist.state.DueItemState
import java.util.*
import javax.inject.Inject

@HiltViewModel
class DueItemViewModel @Inject constructor(
    private val itemRepository: IItemRepository
): ViewModel() {
    private val _state = mutableStateOf(
        DueItemState(
            items = emptyList(),
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

    fun load(listId: Int) {
        val plusseven = Calendar.getInstance()
        plusseven.add(Calendar.DAY_OF_YEAR, 7)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val i = itemRepository.itemsByDueDate(listId, plusseven.toInt())
                withContext(Dispatchers.Main) {
                    _state.value = state.value.copy(
                        items = i.toList(),
                        isLoading = false
                    )
                }
            }
        }
    }
}