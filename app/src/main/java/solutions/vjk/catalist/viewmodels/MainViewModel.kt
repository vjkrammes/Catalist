package solutions.vjk.catalist.viewmodels

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import solutions.vjk.catalist.CURRENT_LIST_NAME
import solutions.vjk.catalist.DEFAULT_LIST_NAME
import solutions.vjk.catalist.SHARED_PREFERENCES_NAME
import solutions.vjk.catalist.infrastructure.DEFAULT_DATE
import solutions.vjk.catalist.infrastructure.asState
import solutions.vjk.catalist.infrastructure.filterText
import solutions.vjk.catalist.infrastructure.toInt
import solutions.vjk.catalist.interfaces.ICategoryRepository
import solutions.vjk.catalist.interfaces.IItemRepository
import solutions.vjk.catalist.interfaces.IListRepository
import solutions.vjk.catalist.models.Category
import solutions.vjk.catalist.models.Item
import solutions.vjk.catalist.models.RepositoryResult
import solutions.vjk.catalist.models.ToDoList
import solutions.vjk.catalist.repositories.Initializer
import solutions.vjk.catalist.state.ItemState
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val itemRepository: IItemRepository,
    private val listRepository: IListRepository,
    private val categoryRepository: ICategoryRepository,
    application: Application
) : AndroidViewModel(application) {
    private val _state = mutableStateOf(
        ItemState(
            currentList = ToDoList(),
            lists = emptyList(),
            categories = emptyList(),
            items = emptyList(),
            isLoading = true,
            errorMessage = null,
            dueCount = 0,
            overdueCount = 0
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

    fun noCategories() {
        viewModelScope.launch {
            _toastMessage.emit("No categories are defined")
        }
    }

    fun isValid() = state.value.currentList.id != 0

    fun switchToList(listName: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val list = listRepository.read(listName)
                switchToList(list)
            }
        }
    }

    private fun createDefaultList() {
        val categories = listOf(
            Category(id = 1, listId = 0, name = "Produce"),
            Category(id = 2, listId = 0, name = "Bakery"),
            Category(id = 3, listId = 0, name = "Dairy"),
            Category(id = 4, listId = 0, name = "Meat"),
            Category(id = 5, listId = 0, name = "Seafood"),
            Category(id = 6, listId = 0, name = "Frozen Items"),
            Category(id = 7, listId = 0, name = "Canned Goods"),
            Category(id = 8, listId = 0, name = "Dry Goods"),
            Category(id = 9, listId = 0, name = "Pets")
        )
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Initializer(listRepository, categoryRepository).initialize(
                    ToDoList(0, DEFAULT_LIST_NAME, Calendar.getInstance().toInt()),
                    categories
                )
                withContext(Dispatchers.Main) {
                    switchToList(DEFAULT_LIST_NAME)
                }
            }
        }
    }

    private fun switchToList(list: ToDoList?) {
        if (list == null) {
            createDefaultList()
        } else {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    val items = itemRepository.getForList(list.id)
                    val lists = listRepository.get()
                    val categories = categoryRepository.getForList(list.id).sortedBy { it.name }
                    val sevendaysago = Calendar.getInstance()
                    sevendaysago.add(Calendar.DAY_OF_YEAR, 7)
                    val sevendays = sevendaysago.toInt()
                    val due =
                        itemRepository.itemCountByDueDate(list.id, sevendays)
                    val overdue = itemRepository.itemCountByDueDate(
                        list.id,
                        Calendar.getInstance().toInt()
                    )
                    withContext(Dispatchers.Main) {
                        _state.value = state.value.copy(
                            currentList = list,
                            lists = lists.toList(),
                            categories = categories.toList(),
                            items = items.toList(),
                            isLoading = false,
                            errorMessage = null,
                            dueCount = due,
                            overdueCount = overdue
                        )
                    }
                }
            }
        }
    }

    fun refreshItems() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val categories = categoryRepository.getForList(state.value.currentList.id)
                val items = itemRepository.getForList(state.value.currentList.id)
                val sevendaysago = Calendar.getInstance()
                sevendaysago.add(Calendar.DAY_OF_YEAR, 7)
                val sevendays = sevendaysago.toInt()
                val due = itemRepository.itemCountByDueDate(state.value.currentList.id, sevendays)
                val overdue = itemRepository.itemCountByDueDate(
                    state.value.currentList.id,
                    Calendar.getInstance().toInt()
                )
                withContext(Dispatchers.Main) {
                    _state.value = state.value.copy(
                        categories = categories.sortedBy { it.name }.toList(),
                        items = items.toList(),
                        dueCount = due,
                        overdueCount = overdue
                    )
                }
            }
        }
    }

    fun deleteItem(item: Item) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {}
            val response = itemRepository.delete(item)
            withContext(Dispatchers.Main) {
                if (response.isSuccessResult) {
                    refreshItems()
                } else {
                    sendToastMessage(response.getMessage())
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
                        refreshItems()
                    } else {
                        sendToastMessage(response.getMessage())
                    }
                }
            }
        }
    }

    fun addBasicItem(name: String, category: Category) {
        val item = Item(
            id = 0,
            listId = state.value.currentList.id,
            assigneeId = 0,
            categoryId = category.id,
            name = name,
            dueDate = DEFAULT_DATE,
            budget = 0,
            spent = 0,
            inProgress = false,
            complete = false,
        )
        var result: RepositoryResult
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                result = itemRepository.insert(item)
                val items = itemRepository.getForList(state.value.currentList.id)
                withContext(Dispatchers.Main) {
                    if (result.isSuccessResult) {
                        _state.value = state.value.copy(items = items)
                        sendToastMessage("Item Created")
                    } else {
                        sendToastMessage(result.getMessage())
                    }
                }
            }
        }
    }

    fun renameCurrentList(newName: String) {
        val newList = state.value.currentList.copy(name = newName)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val result = listRepository.update(newList)
                if (result.isSuccessResult) {
                    withContext(Dispatchers.Main) {
                        _state.value = state.value.copy(currentList = newList)
                        val prefs = getApplication<Application>().getSharedPreferences(
                            SHARED_PREFERENCES_NAME, MODE_PRIVATE
                        )
                        val editor = prefs.edit()
                        editor.putString(CURRENT_LIST_NAME, newName)
                        editor.commit()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        sendToastMessage(result.getMessage())
                    }
                }
            }
        }
    }

    fun renameCategory(category: Category, newName: String) {
        if (category.name != newName) {
            val newCategory = category.copy(name = newName.filterText("\r\n"))
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    val result = categoryRepository.update(newCategory)
                    withContext(Dispatchers.Main) {
                        if (result.isSuccessResult) {
                            sendToastMessage("Category Rename Successful")
                            refreshItems()
                        } else {
                            sendToastMessage(result.getMessage())
                        }
                    }
                }
            }
        }
    }
}