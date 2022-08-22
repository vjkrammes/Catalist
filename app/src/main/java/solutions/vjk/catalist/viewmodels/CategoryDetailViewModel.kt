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
import solutions.vjk.catalist.interfaces.ICategoryRepository
import solutions.vjk.catalist.interfaces.IItemRepository
import solutions.vjk.catalist.models.Category
import solutions.vjk.catalist.state.CategoryDetailState
import javax.inject.Inject

@HiltViewModel
class CategoryDetailViewModel @Inject constructor(
    private val categoryRepository: ICategoryRepository,
    private val itemRepository: IItemRepository
) : ViewModel() {
    private val _state = mutableStateOf(
        CategoryDetailState(
            category = Category(),
            items = emptyList(),
            isLoading = true
        )
    )
    val state = _state.asState()

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage = _toastMessage.asSharedFlow()

    private var categoryId: Int = 0

    private fun sendToastMessage(message: String) {
        viewModelScope.launch {
            _toastMessage.emit(message)
        }
    }

    fun load(categoryId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val c = categoryRepository.read(categoryId)
                if (c == null) {
                    withContext(Dispatchers.Main) {
                        sendToastMessage("Category Not Found")
                        _state.value = state.value.copy(isLoading = false)
                    }
                } else {
                    this@CategoryDetailViewModel.categoryId = categoryId
                    val i = itemRepository.getForCategory(categoryId)
                    withContext(Dispatchers.Main) {
                        _state.value = state.value.copy(
                            category = c,
                            items = i.toList(),
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun deleteItem(itemId: Int, sendMessage: Boolean = true) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val result = itemRepository.delete(itemId)
                withContext(Dispatchers.Main) {
                    if (result.isSuccessResult) {
                        if (sendMessage) {
                            sendToastMessage("Item Deleted")
                        }
                        load(this@CategoryDetailViewModel.categoryId)
                    } else {
                        if (sendMessage) {
                            sendToastMessage(result.getMessage())
                        }
                    }
                }
            }
        }
    }

    fun deleteAllItems() {
        for (item in state.value.items) {
            deleteItem(item.id, false)
        }
        sendToastMessage("All items Deleted")
    }
}