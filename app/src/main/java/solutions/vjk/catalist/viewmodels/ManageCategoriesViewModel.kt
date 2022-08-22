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
import solutions.vjk.catalist.interfaces.ICategoryRepository
import solutions.vjk.catalist.interfaces.IItemRepository
import solutions.vjk.catalist.models.Category
import solutions.vjk.catalist.models.ItemCount
import solutions.vjk.catalist.state.ManageCategoriesState
import javax.inject.Inject

@HiltViewModel
class ManageCategoriesViewModel @Inject constructor(
    private val categoryRepository: ICategoryRepository,
    private val itemRepository: IItemRepository
) : ViewModel() {
    private val _state = mutableStateOf(
        ManageCategoriesState(
            categories = emptyList(),
            items = emptyList(),
            isLoading = true
        )
    )
    val state = _state.asState()

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage = _toastMessage.asSharedFlow()

    private var listId = 0

    private fun sendToastMessage(message: String) {
        viewModelScope.launch {
            _toastMessage.emit(message)
        }
    }

    private fun getItemCount(category: Category): Int =
        state.value.items.singleOrNull { it.id == category.id }?.count ?: 0

    fun setListId(listId: Int) {
        if (listId > 0) {
            this.listId = listId
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    val categories = categoryRepository.getForList(listId)
                    val counts = categories.map { category ->
                        ItemCount(category.id, itemRepository.countForCategory(category.id))
                    }
                    withContext(Dispatchers.Main) {
                        _state.value = state.value.copy(
                            categories = categories.sortedBy { it.name }.toList(),
                            items = counts.toList(),
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun categoryDetails(category: Category, navController: NavController) {
        val count = getItemCount(category)
        if (count == 0) {
            sendToastMessage("Category has no details to display")
        } else {
            navController.navigate("categorydetail/${category.id}")
        }
    }

    fun addCategory(name: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val category = Category(
                    id = 0,
                    listId = listId,
                    name = name,
                    iconId = 0,
                    background = "white",
                    argb = 0xFFFFFFFF,
                    isDefault = false
                )
                val result = categoryRepository.insert(category)
                val newCategories = categoryRepository.getForList(listId)
                withContext(Dispatchers.Main) {
                    if (result.isSuccessResult) {
                        _state.value = state.value.copy(
                            categories = newCategories.sortedBy { it.name }.toList()
                        )
                        sendToastMessage("Category created successfully")
                    } else {
                        sendToastMessage(result.getMessage())
                    }
                }
            }
        }
    }

    fun deleteCategory(category: Category) {
        if (getItemCount(category) == 0) {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    val result = categoryRepository.delete(category)
                    val newCategories = categoryRepository.getForList(listId)
                    withContext(Dispatchers.Main) {
                        if (result.isSuccessResult) {
                            _state.value = state.value.copy(categories = newCategories.toList())
                            sendToastMessage("Category deleted successfully")
                        } else {
                            sendToastMessage(result.getMessage())
                        }
                    }
                }
            }
        } else {
            sendToastMessage("Category has assigned items")
        }
    }
}