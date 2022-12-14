package solutions.vjk.catalist.viewmodels

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
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
import solutions.vjk.catalist.infrastructure.toInt
import solutions.vjk.catalist.interfaces.ICategoryRepository
import solutions.vjk.catalist.interfaces.IListRepository
import solutions.vjk.catalist.models.ToDoList
import solutions.vjk.catalist.state.NewListState
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MakeListViewModel @Inject constructor(
    private val listRepository: IListRepository,
    private val categoryRepository: ICategoryRepository,
    application: Application
) : AndroidViewModel(application) {
    private val _state = mutableStateOf(
        NewListState(
            name = "",
            importCategories = false,
            lists = emptyList(),
            selectedList = null,
            displayedListName = "",
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

    fun loadLists() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val l = listRepository.get()
                withContext(Dispatchers.Main) {
                    _state.value = state.value.copy(
                        lists = l.toList(),
                        isLoading = false
                    )
                }
            }
        }
    }

    fun setName(name: String) {
        _state.value = state.value.copy(
            name = name.filterText("\r\n")
        )
    }

    fun toggleImport() {
        val newValue = !state.value.importCategories
        if (newValue) {
            _state.value = state.value.copy(
                importCategories = newValue
            )
        } else {
            _state.value = state.value.copy(
                importCategories = newValue,
                displayedListName = "",
                selectedList = null
            )
        }
    }

    fun selectList(list: ToDoList) {
        _state.value = state.value.copy(
            selectedList = list,
            displayedListName = list.name
        )
    }

    private fun importCategories() {
        viewModelScope.launch {
            if (state.value.selectedList != null) {
                val importListId = state.value.selectedList!!.id
                withContext(Dispatchers.IO) {
                    val list = listRepository.read(state.value.name)
                    if (list != null) {
                        val cats = categoryRepository.getForList(importListId)
                        if (cats.isNotEmpty()) {
                            cats.forEach { category ->
                                val newCategory = category.copy(
                                    id = 0,
                                    listId = list.id
                                )
                                categoryRepository.insert(newCategory)
                            }
                        }
                    }
                }
            }
        }
    }

    fun create(navController: NavController) {
        if (state.value.name.isNotEmpty()) {
            val newList = ToDoList(
                id = 0,
                name = state.value.name,
                dateCreated = Calendar.getInstance().toInt()
            )
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    val existing = listRepository.read(state.value.name)
                    if (existing == null) {
                        val result = listRepository.insert(newList)
                        withContext(Dispatchers.Main) {
                            if (result.isSuccessResult) {
                                if (state.value.importCategories) {
                                    importCategories()
                                }
                                navController.navigate("switch/${state.value.name}")
                            } else {
                                sendToastMessage(result.getMessage())
                            }
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            sendToastMessage("Duplicate List Name")
                        }
                    }
                }
            }
        } else {
            sendToastMessage("List name is required")
        }
    }

    companion object {
        fun create(
            listRepository: IListRepository,
            categoryRepository: ICategoryRepository,
            application: Application
        ): MakeListViewModel {
            val vm = MakeListViewModel(listRepository, categoryRepository, application)
            vm.loadLists()
            return vm
        }
    }
}