package solutions.vjk.catalist.viewmodels

import android.app.Application
import android.content.Context.MODE_PRIVATE
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
import solutions.vjk.catalist.CURRENT_LIST_NAME
import solutions.vjk.catalist.SHARED_PREFERENCES_NAME
import solutions.vjk.catalist.infrastructure.asState
import solutions.vjk.catalist.interfaces.IListRepository
import solutions.vjk.catalist.state.OpenListState
import javax.inject.Inject

@HiltViewModel
class OpenListViewModel @Inject constructor(
    private val listRepository: IListRepository,
    application: Application,
) : AndroidViewModel(application) {
    private val _state = mutableStateOf(
        OpenListState(
            lists = emptyList(),
            currentListName = "",
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

    fun load() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val l = listRepository.get()
                withContext(Dispatchers.Main) {
                    _state.value = state.value.copy(
                        lists = l.sortedBy { it.name }.toList(),
                        isLoading = false
                    )
                }
            }
        }
    }

    fun setCurrentList(listName: String) {
        _state.value = state.value.copy(
            currentListName = listName
        )
    }

    fun deleteList(listId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val result = listRepository.deleteList(listId)
                withContext(Dispatchers.Main) {
                    if (result.isSuccessResult) {
                        sendToastMessage("List Deleted")
                        load()
                    } else {
                        sendToastMessage(result.getMessage())
                    }
                }
            }
        }
    }

    fun switchTo(listName: String, navController: NavController) {
        val prefs = getApplication<Application>().getSharedPreferences(
            SHARED_PREFERENCES_NAME, MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString(CURRENT_LIST_NAME, listName)
        editor.commit()
        navController.navigate("switch/${listName}")
    }

    companion object {
        fun create(listRepository: IListRepository, application: Application): OpenListViewModel {
            val vm = OpenListViewModel(listRepository, application)
            vm.load()
            return vm
        }
    }
}