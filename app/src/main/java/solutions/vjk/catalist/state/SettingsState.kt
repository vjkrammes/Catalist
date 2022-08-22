package solutions.vjk.catalist.state

import solutions.vjk.catalist.models.Settings

data class SettingsState(
    val settings: Settings,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)
