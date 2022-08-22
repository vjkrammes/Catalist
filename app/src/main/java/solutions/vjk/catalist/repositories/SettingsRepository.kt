package solutions.vjk.catalist.repositories

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import solutions.vjk.catalist.interfaces.ISettingsRepository
import solutions.vjk.catalist.models.RepositoryResult
import solutions.vjk.catalist.models.Settings
import solutions.vjk.catalist.repositories.dao.SettingsDao
import javax.inject.Inject

class SettingsRepository @Inject constructor(private val dao: SettingsDao) : ISettingsRepository {

    private suspend fun settingsExist(): Boolean {
        return withContext(Dispatchers.IO) {
            val count = dao.getSettingsCount()
            return@withContext count > 0
        }
    }

    private suspend fun initialize() {
        if (!settingsExist()) {
            withContext(Dispatchers.IO) {
                dao.insert(Settings())
            }
        }
    }

    override suspend fun read(): Settings {
        initialize()
        var settings = Settings()
        withContext(Dispatchers.IO) {
            try {
                settings = dao.read()
            } catch (ex: Exception) {
                Log.e("DAO Failure/read", ex.message ?: "Unknown")
                Log.e("DAO Failure/read", ex.printStackTrace().toString())
            }
        }
        return settings
    }

    override suspend fun update(settings: Settings): RepositoryResult {
        initialize()
        val model = settings.copy(deleteOnComplete = settings.deleteOnComplete)
        return try {
            dao.update(model)
            RepositoryResult.success()
        } catch (ex: Exception) {
            RepositoryResult.fromException(ex)
        }
    }

    override suspend fun setDeleteOnComplete(newValue: Boolean): RepositoryResult {
        initialize()
        val model = read()
        return update(model.copy(deleteOnComplete = newValue))
    }
}