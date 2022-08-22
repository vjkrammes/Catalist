package solutions.vjk.catalist.interfaces

import solutions.vjk.catalist.models.RepositoryResult
import solutions.vjk.catalist.models.Settings

interface ISettingsRepository {
    suspend fun read(): Settings
    suspend fun update(settings: Settings): RepositoryResult
    suspend fun setDeleteOnComplete(newValue: Boolean): RepositoryResult
}