package solutions.vjk.catalist.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import solutions.vjk.catalist.infrastructure.asState
import solutions.vjk.catalist.interfaces.ISettingsRepository
import solutions.vjk.catalist.models.Settings
import solutions.vjk.catalist.repositories.SettingsRepository
import solutions.vjk.catalist.state.SettingsState
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: ISettingsRepository
) : ViewModel() {

    private val _state = mutableStateOf(
        SettingsState(
            Settings(),
            true,
            null
        )
    )
    val state: State<SettingsState> = _state.asState()

    fun loadSettings() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val settings = settingsRepository.read()
                withContext(Dispatchers.Main) {
                    _state.value = state.value.copy(
                        settings = settings,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun toggleDeleteOnComplete() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                settingsRepository.setDeleteOnComplete(!state.value.settings.deleteOnComplete)
                val settings = settingsRepository.read()
                withContext(Dispatchers.Main) {
                    _state.value = state.value.copy(settings = settings)
                }
            }
        }
    }

    companion object {
        fun create(
            settingsRepository: ISettingsRepository
        ): SettingsViewModel {
            val vm = SettingsViewModel(settingsRepository)
            vm.loadSettings()
            return vm
        }
    }
}